/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.NullConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.jmx.Server;
/*     */ import org.apache.logging.log4j.core.util.Cancellable;
/*     */ import org.apache.logging.log4j.core.util.ExecutorServices;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerRegistry;
/*     */ import org.apache.logging.log4j.spi.Terminable;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggerContext
/*     */   extends AbstractLifeCycle
/*     */   implements LoggerContext, AutoCloseable, Terminable, ConfigurationListener
/*     */ {
/*     */   public static final String PROPERTY_CONFIG = "config";
/*     */   
/*     */   static {
/*     */     try {
/*  66 */       LoaderUtil.loadClass(ExecutorServices.class.getName());
/*  67 */     } catch (Exception e) {
/*  68 */       LOGGER.error("Failed to preload ExecutorServices class.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final Configuration NULL_CONFIGURATION = (Configuration)new NullConfiguration();
/*     */   
/*  79 */   private final LoggerRegistry<Logger> loggerRegistry = new LoggerRegistry();
/*  80 */   private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private volatile Configuration configuration = (Configuration)new DefaultConfiguration();
/*     */   
/*     */   private Object externalContext;
/*     */   private String contextName;
/*     */   private volatile URI configLocation;
/*     */   private Cancellable shutdownCallback;
/*  92 */   private final Lock configLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name) {
/* 100 */     this(name, (Object)null, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext) {
/* 110 */     this(name, externalContext, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext, URI configLocn) {
/* 121 */     this.contextName = name;
/* 122 */     this.externalContext = externalContext;
/* 123 */     this.configLocation = configLocn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext, String configLocn) {
/* 135 */     this.contextName = name;
/* 136 */     this.externalContext = externalContext;
/* 137 */     if (configLocn != null) {
/*     */       URI uri;
/*     */       try {
/* 140 */         uri = (new File(configLocn)).toURI();
/* 141 */       } catch (Exception ex) {
/* 142 */         uri = null;
/*     */       } 
/* 144 */       this.configLocation = uri;
/*     */     } else {
/* 146 */       this.configLocation = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext() {
/* 169 */     return (LoggerContext)LogManager.getContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(boolean currentContext) {
/* 190 */     return (LoggerContext)LogManager.getContext(currentContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
/* 215 */     return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 220 */     LOGGER.debug("Starting LoggerContext[name={}, {}]...", getName(), this);
/* 221 */     if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false)) {
/* 222 */       LOGGER.debug("Stack trace to locate invoker", new Exception("Not a real error, showing stack trace to locate invoker"));
/*     */     }
/*     */     
/* 225 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 227 */         if (isInitialized() || isStopped()) {
/* 228 */           setStarting();
/* 229 */           reconfigure();
/* 230 */           if (this.configuration.isShutdownHookEnabled()) {
/* 231 */             setUpShutdownHook();
/*     */           }
/* 233 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 236 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 239 */     LOGGER.debug("LoggerContext[name={}, {}] started OK.", getName(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Configuration config) {
/* 248 */     LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", getName(), this, config);
/* 249 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 251 */         if (isInitialized() || isStopped()) {
/* 252 */           if (this.configuration.isShutdownHookEnabled()) {
/* 253 */             setUpShutdownHook();
/*     */           }
/* 255 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 258 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 261 */     setConfiguration(config);
/* 262 */     LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", getName(), this, config);
/*     */   }
/*     */   
/*     */   private void setUpShutdownHook() {
/* 266 */     if (this.shutdownCallback == null) {
/* 267 */       LoggerContextFactory factory = LogManager.getFactory();
/* 268 */       if (factory instanceof ShutdownCallbackRegistry) {
/* 269 */         LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Shutdown hook enabled. Registering a new one.");
/*     */         try {
/* 271 */           final long shutdownTimeoutMillis = this.configuration.getShutdownTimeoutMillis();
/* 272 */           this.shutdownCallback = ((ShutdownCallbackRegistry)factory).addShutdownCallback(new Runnable()
/*     */               {
/*     */                 public void run()
/*     */                 {
/* 276 */                   LoggerContext context = LoggerContext.this;
/* 277 */                   AbstractLifeCycle.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Stopping LoggerContext[name={}, {}]", context.getName(), context);
/*     */                   
/* 279 */                   context.stop(shutdownTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 284 */                   return "Shutdown callback for LoggerContext[name=" + LoggerContext.this.getName() + ']';
/*     */                 }
/*     */               });
/* 287 */         } catch (IllegalStateException e) {
/* 288 */           throw new IllegalStateException("Unable to register Log4j shutdown hook because JVM is shutting down.", e);
/*     */         }
/* 290 */         catch (SecurityException e) {
/* 291 */           LOGGER.error(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Unable to register shutdown hook due to security restrictions", e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 300 */     stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void terminate() {
/* 305 */     stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 329 */     LOGGER.debug("Stopping LoggerContext[name={}, {}]...", getName(), this);
/* 330 */     this.configLock.lock();
/*     */     try {
/* 332 */       if (isStopped()) {
/* 333 */         return true;
/*     */       }
/*     */       
/* 336 */       setStopping();
/*     */       try {
/* 338 */         Server.unregisterLoggerContext(getName());
/* 339 */       } catch (LinkageError|Exception e) {
/*     */         
/* 341 */         LOGGER.error("Unable to unregister MBeans", e);
/*     */       } 
/* 343 */       if (this.shutdownCallback != null) {
/* 344 */         this.shutdownCallback.cancel();
/* 345 */         this.shutdownCallback = null;
/*     */       } 
/* 347 */       Configuration prev = this.configuration;
/* 348 */       this.configuration = NULL_CONFIGURATION;
/* 349 */       updateLoggers();
/* 350 */       if (prev instanceof LifeCycle2) {
/* 351 */         ((LifeCycle2)prev).stop(timeout, timeUnit);
/*     */       } else {
/* 353 */         prev.stop();
/*     */       } 
/* 355 */       this.externalContext = null;
/* 356 */       LogManager.getFactory().removeContext(this);
/*     */     } finally {
/* 358 */       this.configLock.unlock();
/* 359 */       setStopped();
/*     */     } 
/* 361 */     LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", getName(), this, Boolean.valueOf(true));
/* 362 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 371 */     return this.contextName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getRootLogger() {
/* 380 */     return getLogger("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 390 */     this.contextName = Objects.<String>requireNonNull(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExternalContext(Object context) {
/* 399 */     this.externalContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getExternalContext() {
/* 409 */     return this.externalContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name) {
/* 420 */     return getLogger(name, (MessageFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Logger> getLoggers() {
/* 433 */     return this.loggerRegistry.getLoggers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name, MessageFactory messageFactory) {
/* 447 */     Logger logger = (Logger)this.loggerRegistry.getLogger(name, messageFactory);
/* 448 */     if (logger != null) {
/* 449 */       AbstractLogger.checkMessageFactory((ExtendedLogger)logger, messageFactory);
/* 450 */       return logger;
/*     */     } 
/*     */     
/* 453 */     logger = newInstance(this, name, messageFactory);
/* 454 */     this.loggerRegistry.putIfAbsent(name, messageFactory, (ExtendedLogger)logger);
/* 455 */     return (Logger)this.loggerRegistry.getLogger(name, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 466 */     return this.loggerRegistry.hasLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, MessageFactory messageFactory) {
/* 477 */     return this.loggerRegistry.hasLogger(name, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
/* 488 */     return this.loggerRegistry.hasLogger(name, messageFactoryClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration() {
/* 497 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 507 */     this.configuration.addFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeFilter(Filter filter) {
/* 516 */     this.configuration.removeFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Configuration setConfiguration(Configuration config) {
/* 526 */     if (config == null) {
/* 527 */       LOGGER.error("No configuration found for context '{}'.", this.contextName);
/*     */       
/* 529 */       return this.configuration;
/*     */     } 
/* 531 */     this.configLock.lock();
/*     */     try {
/* 533 */       Configuration prev = this.configuration;
/* 534 */       config.addListener(this);
/*     */       
/* 536 */       ConcurrentMap<String, String> map = (ConcurrentMap<String, String>)config.getComponent("ContextProperties");
/*     */       
/*     */       try {
/* 539 */         map.putIfAbsent("hostName", NetUtils.getLocalHostname());
/* 540 */       } catch (Exception ex) {
/* 541 */         LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
/* 542 */         map.putIfAbsent("hostName", "unknown");
/*     */       } 
/* 544 */       map.putIfAbsent("contextName", this.contextName);
/* 545 */       config.start();
/* 546 */       this.configuration = config;
/* 547 */       updateLoggers();
/* 548 */       if (prev != null) {
/* 549 */         prev.removeListener(this);
/* 550 */         prev.stop();
/*     */       } 
/*     */       
/* 553 */       firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
/*     */       
/*     */       try {
/* 556 */         Server.reregisterMBeansAfterReconfigure();
/* 557 */       } catch (LinkageError|Exception e) {
/*     */         
/* 559 */         LOGGER.error("Could not reconfigure JMX", e);
/*     */       } 
/*     */       
/* 562 */       Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
/*     */       
/* 564 */       return prev;
/*     */     } finally {
/* 566 */       this.configLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void firePropertyChangeEvent(PropertyChangeEvent event) {
/* 571 */     for (PropertyChangeListener listener : this.propertyChangeListeners) {
/* 572 */       listener.propertyChange(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener) {
/* 577 */     this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/* 581 */     this.propertyChangeListeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getConfigLocation() {
/* 593 */     return this.configLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(URI configLocation) {
/* 602 */     this.configLocation = configLocation;
/* 603 */     reconfigure(configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reconfigure(URI configURI) {
/* 610 */     ClassLoader cl = ClassLoader.class.isInstance(this.externalContext) ? (ClassLoader)this.externalContext : null;
/* 611 */     LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
/*     */     
/* 613 */     Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
/* 614 */     if (instance == null) {
/* 615 */       LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
/*     */     } else {
/* 617 */       setConfiguration(instance);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 622 */       String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
/* 623 */       LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reconfigure() {
/* 634 */     reconfigure(this.configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers() {
/* 641 */     updateLoggers(this.configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers(Configuration config) {
/* 650 */     Configuration old = this.configuration;
/* 651 */     for (Logger logger : this.loggerRegistry.getLoggers()) {
/* 652 */       logger.updateConfiguration(config);
/*     */     }
/* 654 */     firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void onChange(Reconfigurable reconfigurable) {
/* 664 */     LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
/* 665 */     Configuration newConfig = reconfigurable.reconfigure();
/* 666 */     if (newConfig != null) {
/* 667 */       setConfiguration(newConfig);
/* 668 */       LOGGER.debug("Reconfiguration completed for {} ({})", this.contextName, this);
/*     */     } else {
/* 670 */       LOGGER.debug("Reconfiguration failed for {} ({})", this.contextName, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
/* 676 */     return new Logger(ctx, name, messageFactory);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\LoggerContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */