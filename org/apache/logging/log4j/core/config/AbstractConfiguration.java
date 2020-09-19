/*      */ package org.apache.logging.log4j.core.config;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.core.Appender;
/*      */ import org.apache.logging.log4j.core.Filter;
/*      */ import org.apache.logging.log4j.core.Layout;
/*      */ import org.apache.logging.log4j.core.LifeCycle;
/*      */ import org.apache.logging.log4j.core.LifeCycle2;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.Logger;
/*      */ import org.apache.logging.log4j.core.LoggerContext;
/*      */ import org.apache.logging.log4j.core.appender.ConsoleAppender;
/*      */ import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
/*      */ import org.apache.logging.log4j.core.async.AsyncLoggerConfigDisruptor;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*      */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*      */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*      */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*      */ import org.apache.logging.log4j.core.lookup.MapLookup;
/*      */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*      */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*      */ import org.apache.logging.log4j.core.net.Advertiser;
/*      */ import org.apache.logging.log4j.core.script.AbstractScript;
/*      */ import org.apache.logging.log4j.core.script.ScriptManager;
/*      */ import org.apache.logging.log4j.core.util.DummyNanoClock;
/*      */ import org.apache.logging.log4j.core.util.Loader;
/*      */ import org.apache.logging.log4j.core.util.NameUtil;
/*      */ import org.apache.logging.log4j.core.util.NanoClock;
/*      */ import org.apache.logging.log4j.core.util.WatchManager;
/*      */ import org.apache.logging.log4j.util.PropertiesUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractConfiguration
/*      */   extends AbstractFilterable
/*      */   implements Configuration
/*      */ {
/*      */   private static final int BUF_SIZE = 16384;
/*      */   protected Node rootNode;
/*   87 */   protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   protected final List<String> pluginPackages = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PluginManager pluginManager;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isShutdownHookEnabled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   protected long shutdownTimeoutMillis = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ScriptManager scriptManager;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  117 */   private Advertiser advertiser = new DefaultAdvertiser();
/*  118 */   private Node advertiserNode = null;
/*      */   private Object advertisement;
/*      */   private String name;
/*  121 */   private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<>();
/*  122 */   private ConcurrentMap<String, LoggerConfig> loggerConfigs = new ConcurrentHashMap<>();
/*  123 */   private List<CustomLevelConfig> customLevels = Collections.emptyList();
/*  124 */   private final ConcurrentMap<String, String> properties = new ConcurrentHashMap<>();
/*  125 */   private final StrLookup tempLookup = (StrLookup)new Interpolator(this.properties);
/*  126 */   private final StrSubstitutor subst = new StrSubstitutor(this.tempLookup);
/*  127 */   private LoggerConfig root = new LoggerConfig();
/*  128 */   private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<>();
/*      */   private final ConfigurationSource configurationSource;
/*  130 */   private final ConfigurationScheduler configurationScheduler = new ConfigurationScheduler();
/*  131 */   private final WatchManager watchManager = new WatchManager(this.configurationScheduler);
/*      */   private AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor;
/*  133 */   private NanoClock nanoClock = (NanoClock)new DummyNanoClock();
/*      */ 
/*      */   
/*      */   private final WeakReference<LoggerContext> loggerContext;
/*      */ 
/*      */   
/*      */   protected AbstractConfiguration(LoggerContext loggerContext, ConfigurationSource configurationSource) {
/*  140 */     this.loggerContext = new WeakReference<>(loggerContext);
/*      */ 
/*      */     
/*  143 */     this.configurationSource = Objects.<ConfigurationSource>requireNonNull(configurationSource, "configurationSource is null");
/*  144 */     this.componentMap.put("ContextProperties", this.properties);
/*  145 */     this.pluginManager = new PluginManager("Core");
/*  146 */     this.rootNode = new Node();
/*  147 */     setState(LifeCycle.State.INITIALIZING);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ConfigurationSource getConfigurationSource() {
/*  153 */     return this.configurationSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> getPluginPackages() {
/*  158 */     return this.pluginPackages;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> getProperties() {
/*  163 */     return this.properties;
/*      */   }
/*      */ 
/*      */   
/*      */   public ScriptManager getScriptManager() {
/*  168 */     return this.scriptManager;
/*      */   }
/*      */   
/*      */   public void setScriptManager(ScriptManager scriptManager) {
/*  172 */     this.scriptManager = scriptManager;
/*      */   }
/*      */   
/*      */   public PluginManager getPluginManager() {
/*  176 */     return this.pluginManager;
/*      */   }
/*      */   
/*      */   public void setPluginManager(PluginManager pluginManager) {
/*  180 */     this.pluginManager = pluginManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public WatchManager getWatchManager() {
/*  185 */     return this.watchManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public ConfigurationScheduler getScheduler() {
/*  190 */     return this.configurationScheduler;
/*      */   }
/*      */   
/*      */   public Node getRootNode() {
/*  194 */     return this.rootNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate() {
/*  201 */     if (this.asyncLoggerConfigDisruptor == null) {
/*  202 */       this.asyncLoggerConfigDisruptor = new AsyncLoggerConfigDisruptor();
/*      */     }
/*  204 */     return (AsyncLoggerConfigDelegate)this.asyncLoggerConfigDisruptor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initialize() {
/*  212 */     LOGGER.debug("Initializing configuration {}", this);
/*  213 */     this.subst.setConfiguration(this);
/*  214 */     this.scriptManager = new ScriptManager(this, this.watchManager);
/*  215 */     this.pluginManager.collectPlugins(this.pluginPackages);
/*  216 */     PluginManager levelPlugins = new PluginManager("Level");
/*  217 */     levelPlugins.collectPlugins(this.pluginPackages);
/*  218 */     Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
/*  219 */     if (plugins != null) {
/*  220 */       for (PluginType<?> type : plugins.values()) {
/*      */         
/*      */         try {
/*  223 */           Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
/*  224 */         } catch (Exception e) {
/*  225 */           LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass().getSimpleName(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  230 */     setup();
/*  231 */     setupAdvertisement();
/*  232 */     doConfigure();
/*  233 */     setState(LifeCycle.State.INITIALIZED);
/*  234 */     LOGGER.debug("Configuration {} initialized", this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() {
/*  243 */     if (getState().equals(LifeCycle.State.INITIALIZING)) {
/*  244 */       initialize();
/*      */     }
/*  246 */     LOGGER.debug("Starting configuration {}", this);
/*  247 */     setStarting();
/*  248 */     if (this.watchManager.getIntervalSeconds() > 0) {
/*  249 */       this.watchManager.start();
/*      */     }
/*  251 */     if (hasAsyncLoggers()) {
/*  252 */       this.asyncLoggerConfigDisruptor.start();
/*      */     }
/*  254 */     Set<LoggerConfig> alreadyStarted = new HashSet<>();
/*  255 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  256 */       logger.start();
/*  257 */       alreadyStarted.add(logger);
/*      */     } 
/*  259 */     for (Appender appender : this.appenders.values()) {
/*  260 */       appender.start();
/*      */     }
/*  262 */     if (!alreadyStarted.contains(this.root)) {
/*  263 */       this.root.start();
/*      */     }
/*  265 */     super.start();
/*  266 */     LOGGER.debug("Started configuration {} OK.", this);
/*      */   }
/*      */   
/*      */   private boolean hasAsyncLoggers() {
/*  270 */     if (this.root instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/*  271 */       return true;
/*      */     }
/*  273 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  274 */       if (logger instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/*  275 */         return true;
/*      */       }
/*      */     } 
/*  278 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  286 */     setStopping();
/*  287 */     stop(timeout, timeUnit, false);
/*  288 */     LOGGER.trace("Stopping {}...", this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  303 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
/*  304 */       loggerConfig.getReliabilityStrategy().beforeStopConfiguration(this);
/*      */     }
/*  306 */     this.root.getReliabilityStrategy().beforeStopConfiguration(this);
/*      */     
/*  308 */     String cls = getClass().getSimpleName();
/*  309 */     LOGGER.trace("{} notified {} ReliabilityStrategies that config will be stopped.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
/*      */ 
/*      */     
/*  312 */     if (!this.loggerConfigs.isEmpty()) {
/*  313 */       LOGGER.trace("{} stopping {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size()));
/*  314 */       for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  315 */         logger.stop(timeout, timeUnit);
/*      */       }
/*      */     } 
/*  318 */     LOGGER.trace("{} stopping root LoggerConfig.", cls);
/*  319 */     if (!this.root.isStopped()) {
/*  320 */       this.root.stop(timeout, timeUnit);
/*      */     }
/*      */     
/*  323 */     if (hasAsyncLoggers()) {
/*  324 */       LOGGER.trace("{} stopping AsyncLoggerConfigDisruptor.", cls);
/*  325 */       this.asyncLoggerConfigDisruptor.stop(timeout, timeUnit);
/*      */     } 
/*      */ 
/*      */     
/*  329 */     Appender[] array = (Appender[])this.appenders.values().toArray((Object[])new Appender[this.appenders.size()]);
/*  330 */     List<Appender> async = getAsyncAppenders(array);
/*  331 */     if (!async.isEmpty()) {
/*      */       
/*  333 */       LOGGER.trace("{} stopping {} AsyncAppenders.", cls, Integer.valueOf(async.size()));
/*  334 */       for (Appender appender : async) {
/*  335 */         if (appender instanceof LifeCycle2) {
/*  336 */           ((LifeCycle2)appender).stop(timeout, timeUnit); continue;
/*      */         } 
/*  338 */         appender.stop();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  343 */     LOGGER.trace("{} notifying ReliabilityStrategies that appenders will be stopped.", cls);
/*  344 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
/*  345 */       loggerConfig.getReliabilityStrategy().beforeStopAppenders();
/*      */     }
/*  347 */     this.root.getReliabilityStrategy().beforeStopAppenders();
/*      */     
/*  349 */     LOGGER.trace("{} stopping remaining Appenders.", cls);
/*  350 */     int appenderCount = 0;
/*  351 */     for (int i = array.length - 1; i >= 0; i--) {
/*  352 */       if (array[i].isStarted()) {
/*  353 */         if (array[i] instanceof LifeCycle2) {
/*  354 */           ((LifeCycle2)array[i]).stop(timeout, timeUnit);
/*      */         } else {
/*  356 */           array[i].stop();
/*      */         } 
/*  358 */         appenderCount++;
/*      */       } 
/*      */     } 
/*  361 */     LOGGER.trace("{} stopped {} remaining Appenders.", cls, Integer.valueOf(appenderCount));
/*      */     
/*  363 */     LOGGER.trace("{} cleaning Appenders from {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
/*  364 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  371 */       loggerConfig.clearAppenders();
/*      */     }
/*  373 */     this.root.clearAppenders();
/*      */     
/*  375 */     if (this.watchManager.isStarted()) {
/*  376 */       this.watchManager.stop(timeout, timeUnit);
/*      */     }
/*  378 */     this.configurationScheduler.stop(timeout, timeUnit);
/*      */     
/*  380 */     if (this.advertiser != null && this.advertisement != null) {
/*  381 */       this.advertiser.unadvertise(this.advertisement);
/*      */     }
/*  383 */     setStopped();
/*  384 */     LOGGER.debug("Stopped {} OK", this);
/*  385 */     return true;
/*      */   }
/*      */   
/*      */   private List<Appender> getAsyncAppenders(Appender[] all) {
/*  389 */     List<Appender> result = new ArrayList<>();
/*  390 */     for (int i = all.length - 1; i >= 0; i--) {
/*  391 */       if (all[i] instanceof org.apache.logging.log4j.core.appender.AsyncAppender) {
/*  392 */         result.add(all[i]);
/*      */       }
/*      */     } 
/*  395 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShutdownHookEnabled() {
/*  400 */     return this.isShutdownHookEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getShutdownTimeoutMillis() {
/*  405 */     return this.shutdownTimeoutMillis;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setup() {}
/*      */ 
/*      */   
/*      */   protected Level getDefaultStatus() {
/*  413 */     String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());
/*      */     
/*      */     try {
/*  416 */       return Level.toLevel(statusLevel);
/*  417 */     } catch (Exception ex) {
/*  418 */       return Level.ERROR;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void createAdvertiser(String advertiserString, ConfigurationSource configSource, byte[] buffer, String contentType) {
/*  424 */     if (advertiserString != null) {
/*  425 */       Node node = new Node(null, advertiserString, null);
/*  426 */       Map<String, String> attributes = node.getAttributes();
/*  427 */       attributes.put("content", new String(buffer));
/*  428 */       attributes.put("contentType", contentType);
/*  429 */       attributes.put("name", "configuration");
/*  430 */       if (configSource.getLocation() != null) {
/*  431 */         attributes.put("location", configSource.getLocation());
/*      */       }
/*  433 */       this.advertiserNode = node;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupAdvertisement() {
/*  438 */     if (this.advertiserNode != null) {
/*  439 */       String nodeName = this.advertiserNode.getName();
/*  440 */       PluginType<?> type = this.pluginManager.getPluginType(nodeName);
/*  441 */       if (type != null) {
/*  442 */         Class<? extends Advertiser> clazz = type.getPluginClass().asSubclass(Advertiser.class);
/*      */         try {
/*  444 */           this.advertiser = clazz.newInstance();
/*  445 */           this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
/*  446 */         } catch (InstantiationException e) {
/*  447 */           LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", nodeName, e);
/*  448 */         } catch (IllegalAccessException e) {
/*  449 */           LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", nodeName, e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getComponent(String componentName) {
/*  458 */     return (T)this.componentMap.get(componentName);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addComponent(String componentName, Object obj) {
/*  463 */     this.componentMap.putIfAbsent(componentName, obj);
/*      */   }
/*      */   
/*      */   protected void preConfigure(Node node) {
/*      */     try {
/*  468 */       for (Node child : node.getChildren()) {
/*  469 */         if (child.getType() == null) {
/*  470 */           LOGGER.error("Unable to locate plugin type for " + child.getName());
/*      */           continue;
/*      */         } 
/*  473 */         Class<?> clazz = child.getType().getPluginClass();
/*  474 */         if (clazz.isAnnotationPresent((Class)Scheduled.class)) {
/*  475 */           this.configurationScheduler.incrementScheduledItems();
/*      */         }
/*  477 */         preConfigure(child);
/*      */       } 
/*  479 */     } catch (Exception ex) {
/*  480 */       LOGGER.error("Error capturing node data for node " + node.getName(), ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void doConfigure() {
/*  485 */     preConfigure(this.rootNode);
/*  486 */     this.configurationScheduler.start();
/*  487 */     if (this.rootNode.hasChildren() && ((Node)this.rootNode.getChildren().get(0)).getName().equalsIgnoreCase("Properties")) {
/*  488 */       Node first = this.rootNode.getChildren().get(0);
/*  489 */       createConfiguration(first, (LogEvent)null);
/*  490 */       if (first.getObject() != null) {
/*  491 */         this.subst.setVariableResolver(first.<StrLookup>getObject());
/*      */       }
/*      */     } else {
/*  494 */       Map<String, String> map = getComponent("ContextProperties");
/*  495 */       MapLookup mapLookup = (map == null) ? null : new MapLookup(map);
/*  496 */       this.subst.setVariableResolver((StrLookup)new Interpolator((StrLookup)mapLookup, this.pluginPackages));
/*      */     } 
/*      */     
/*  499 */     boolean setLoggers = false;
/*  500 */     boolean setRoot = false;
/*  501 */     for (Node child : this.rootNode.getChildren()) {
/*  502 */       if (child.getName().equalsIgnoreCase("Properties")) {
/*  503 */         if (this.tempLookup == this.subst.getVariableResolver()) {
/*  504 */           LOGGER.error("Properties declaration must be the first element in the configuration");
/*      */         }
/*      */         continue;
/*      */       } 
/*  508 */       createConfiguration(child, (LogEvent)null);
/*  509 */       if (child.getObject() == null) {
/*      */         continue;
/*      */       }
/*  512 */       if (child.getName().equalsIgnoreCase("Scripts")) {
/*  513 */         for (AbstractScript script : (AbstractScript[])child.<AbstractScript[]>getObject((Class)AbstractScript[].class)) {
/*  514 */           if (script instanceof org.apache.logging.log4j.core.script.ScriptRef) {
/*  515 */             LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references", script.getName());
/*      */           } else {
/*      */             
/*  518 */             this.scriptManager.addScript(script);
/*      */           } 
/*      */         }  continue;
/*  521 */       }  if (child.getName().equalsIgnoreCase("Appenders")) {
/*  522 */         this.appenders = child.<ConcurrentMap<String, Appender>>getObject(); continue;
/*  523 */       }  if (child.isInstanceOf(Filter.class)) {
/*  524 */         addFilter(child.<Filter>getObject(Filter.class)); continue;
/*  525 */       }  if (child.getName().equalsIgnoreCase("Loggers")) {
/*  526 */         Loggers l = child.<Loggers>getObject();
/*  527 */         this.loggerConfigs = l.getMap();
/*  528 */         setLoggers = true;
/*  529 */         if (l.getRoot() != null) {
/*  530 */           this.root = l.getRoot();
/*  531 */           setRoot = true;
/*      */         }  continue;
/*  533 */       }  if (child.getName().equalsIgnoreCase("CustomLevels")) {
/*  534 */         this.customLevels = ((CustomLevels)child.<CustomLevels>getObject(CustomLevels.class)).getCustomLevels(); continue;
/*  535 */       }  if (child.isInstanceOf(CustomLevelConfig.class)) {
/*  536 */         List<CustomLevelConfig> copy = new ArrayList<>(this.customLevels);
/*  537 */         copy.add(child.getObject(CustomLevelConfig.class));
/*  538 */         this.customLevels = copy; continue;
/*      */       } 
/*  540 */       List<String> expected = Arrays.asList(new String[] { "\"Appenders\"", "\"Loggers\"", "\"Properties\"", "\"Scripts\"", "\"CustomLevels\"" });
/*      */       
/*  542 */       LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.", child.getName(), child.<T>getObject().getClass().getName(), expected);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  547 */     if (!setLoggers) {
/*  548 */       LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
/*  549 */       setToDefault(); return;
/*      */     } 
/*  551 */     if (!setRoot) {
/*  552 */       LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
/*  553 */       setToDefault();
/*      */     } 
/*      */ 
/*      */     
/*  557 */     for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
/*  558 */       LoggerConfig loggerConfig = entry.getValue();
/*  559 */       for (AppenderRef ref : loggerConfig.getAppenderRefs()) {
/*  560 */         Appender app = this.appenders.get(ref.getRef());
/*  561 */         if (app != null) {
/*  562 */           loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter()); continue;
/*      */         } 
/*  564 */         LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(), loggerConfig);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  571 */     setParents();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setToDefault() {
/*  576 */     setName("Default@" + Integer.toHexString(hashCode()));
/*  577 */     PatternLayout patternLayout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
/*      */ 
/*      */ 
/*      */     
/*  581 */     ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout((Layout)patternLayout);
/*  582 */     consoleAppender.start();
/*  583 */     addAppender((Appender)consoleAppender);
/*  584 */     LoggerConfig rootLoggerConfig = getRootLogger();
/*  585 */     rootLoggerConfig.addAppender((Appender)consoleAppender, (Level)null, (Filter)null);
/*      */     
/*  587 */     Level defaultLevel = Level.ERROR;
/*  588 */     String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level", defaultLevel.name());
/*      */     
/*  590 */     Level level = Level.valueOf(levelName);
/*  591 */     rootLoggerConfig.setLevel((level != null) ? level : defaultLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  600 */     this.name = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  610 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(ConfigurationListener listener) {
/*  620 */     this.listeners.add(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeListener(ConfigurationListener listener) {
/*  630 */     this.listeners.remove(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Appender> T getAppender(String appenderName) {
/*  642 */     return (T)this.appenders.get(appenderName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Appender> getAppenders() {
/*  652 */     return this.appenders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAppender(Appender appender) {
/*  662 */     this.appenders.putIfAbsent(appender.getName(), appender);
/*      */   }
/*      */ 
/*      */   
/*      */   public StrSubstitutor getStrSubstitutor() {
/*  667 */     return this.subst;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAdvertiser(Advertiser advertiser) {
/*  672 */     this.advertiser = advertiser;
/*      */   }
/*      */ 
/*      */   
/*      */   public Advertiser getAdvertiser() {
/*  677 */     return this.advertiser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
/*  688 */     return ReliabilityStrategyFactory.getReliabilityStrategy(loggerConfig);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLoggerAppender(Logger logger, Appender appender) {
/*  703 */     String loggerName = logger.getName();
/*  704 */     this.appenders.putIfAbsent(appender.getName(), appender);
/*  705 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  706 */     if (lc.getName().equals(loggerName)) {
/*  707 */       lc.addAppender(appender, (Level)null, (Filter)null);
/*      */     } else {
/*  709 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
/*  710 */       nlc.addAppender(appender, (Level)null, (Filter)null);
/*  711 */       nlc.setParent(lc);
/*  712 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  713 */       setParents();
/*  714 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLoggerFilter(Logger logger, Filter filter) {
/*  729 */     String loggerName = logger.getName();
/*  730 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  731 */     if (lc.getName().equals(loggerName)) {
/*  732 */       lc.addFilter(filter);
/*      */     } else {
/*  734 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
/*  735 */       nlc.addFilter(filter);
/*  736 */       nlc.setParent(lc);
/*  737 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  738 */       setParents();
/*  739 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setLoggerAdditive(Logger logger, boolean additive) {
/*  754 */     String loggerName = logger.getName();
/*  755 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  756 */     if (lc.getName().equals(loggerName)) {
/*  757 */       lc.setAdditive(additive);
/*      */     } else {
/*  759 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), additive);
/*  760 */       nlc.setParent(lc);
/*  761 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  762 */       setParents();
/*  763 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeAppender(String appenderName) {
/*  775 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  776 */       logger.removeAppender(appenderName);
/*      */     }
/*  778 */     Appender app = this.appenders.remove(appenderName);
/*      */     
/*  780 */     if (app != null) {
/*  781 */       app.stop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<CustomLevelConfig> getCustomLevels() {
/*  792 */     return Collections.unmodifiableList(this.customLevels);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getLoggerConfig(String loggerName) {
/*  804 */     LoggerConfig loggerConfig = this.loggerConfigs.get(loggerName);
/*  805 */     if (loggerConfig != null) {
/*  806 */       return loggerConfig;
/*      */     }
/*  808 */     String substr = loggerName;
/*  809 */     while ((substr = NameUtil.getSubName(substr)) != null) {
/*  810 */       loggerConfig = this.loggerConfigs.get(substr);
/*  811 */       if (loggerConfig != null) {
/*  812 */         return loggerConfig;
/*      */       }
/*      */     } 
/*  815 */     return this.root;
/*      */   }
/*      */ 
/*      */   
/*      */   public LoggerContext getLoggerContext() {
/*  820 */     return this.loggerContext.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getRootLogger() {
/*  830 */     return this.root;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, LoggerConfig> getLoggers() {
/*  840 */     return Collections.unmodifiableMap(this.loggerConfigs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getLogger(String loggerName) {
/*  850 */     return this.loggerConfigs.get(loggerName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLogger(String loggerName, LoggerConfig loggerConfig) {
/*  862 */     this.loggerConfigs.putIfAbsent(loggerName, loggerConfig);
/*  863 */     setParents();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeLogger(String loggerName) {
/*  873 */     this.loggerConfigs.remove(loggerName);
/*  874 */     setParents();
/*      */   }
/*      */ 
/*      */   
/*      */   public void createConfiguration(Node node, LogEvent event) {
/*  879 */     PluginType<?> type = node.getType();
/*  880 */     if (type != null && type.isDeferChildren()) {
/*  881 */       node.setObject(createPluginObject(type, node, event));
/*      */     } else {
/*  883 */       for (Node child : node.getChildren()) {
/*  884 */         createConfiguration(child, event);
/*      */       }
/*      */       
/*  887 */       if (type == null) {
/*  888 */         if (node.getParent() != null) {
/*  889 */           LOGGER.error("Unable to locate plugin for {}", node.getName());
/*      */         }
/*      */       } else {
/*  892 */         node.setObject(createPluginObject(type, node, event));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object createPluginObject(PluginType<?> type, Node node, LogEvent event) {
/*  934 */     Class<?> clazz = type.getPluginClass();
/*      */     
/*  936 */     if (Map.class.isAssignableFrom(clazz)) {
/*      */       try {
/*  938 */         return createPluginMap(node);
/*  939 */       } catch (Exception e) {
/*  940 */         LOGGER.warn("Unable to create Map for {} of class {}", type.getElementName(), clazz, e);
/*      */       } 
/*      */     }
/*      */     
/*  944 */     if (Collection.class.isAssignableFrom(clazz)) {
/*      */       try {
/*  946 */         return createPluginCollection(node);
/*  947 */       } catch (Exception e) {
/*  948 */         LOGGER.warn("Unable to create List for {} of class {}", type.getElementName(), clazz, e);
/*      */       } 
/*      */     }
/*      */     
/*  952 */     return (new PluginBuilder(type)).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
/*      */   }
/*      */   
/*      */   private static Map<String, ?> createPluginMap(Node node) {
/*  956 */     Map<String, Object> map = new LinkedHashMap<>();
/*  957 */     for (Node child : node.getChildren()) {
/*  958 */       Object object = child.getObject();
/*  959 */       map.put(child.getName(), object);
/*      */     } 
/*  961 */     return map;
/*      */   }
/*      */   
/*      */   private static Collection<?> createPluginCollection(Node node) {
/*  965 */     List<Node> children = node.getChildren();
/*  966 */     Collection<Object> list = new ArrayList(children.size());
/*  967 */     for (Node child : children) {
/*  968 */       Object object = child.getObject();
/*  969 */       list.add(object);
/*      */     } 
/*  971 */     return list;
/*      */   }
/*      */   
/*      */   private void setParents() {
/*  975 */     for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
/*  976 */       LoggerConfig logger = entry.getValue();
/*  977 */       String key = entry.getKey();
/*  978 */       if (!key.isEmpty()) {
/*  979 */         int i = key.lastIndexOf('.');
/*  980 */         if (i > 0) {
/*  981 */           key = key.substring(0, i);
/*  982 */           LoggerConfig parent = getLoggerConfig(key);
/*  983 */           if (parent == null) {
/*  984 */             parent = this.root;
/*      */           }
/*  986 */           logger.setParent(parent); continue;
/*      */         } 
/*  988 */         logger.setParent(this.root);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static byte[] toByteArray(InputStream is) throws IOException {
/* 1003 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*      */ 
/*      */     
/* 1006 */     byte[] data = new byte[16384];
/*      */     int nRead;
/* 1008 */     while ((nRead = is.read(data, 0, data.length)) != -1) {
/* 1009 */       buffer.write(data, 0, nRead);
/*      */     }
/*      */     
/* 1012 */     return buffer.toByteArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public NanoClock getNanoClock() {
/* 1017 */     return this.nanoClock;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNanoClock(NanoClock nanoClock) {
/* 1022 */     this.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "nanoClock");
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\AbstractConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */