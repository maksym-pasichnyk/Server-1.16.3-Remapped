/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.LogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ 
/*     */ @Plugin(name = "logger", category = "Core", printObject = true)
/*     */ public class LoggerConfig
/*     */   extends AbstractFilterable
/*     */ {
/*     */   public static final String ROOT = "root";
/*  61 */   private static LogEventFactory LOG_EVENT_FACTORY = null;
/*     */   
/*  63 */   private List<AppenderRef> appenderRefs = new ArrayList<>();
/*  64 */   private final AppenderControlArraySet appenders = new AppenderControlArraySet();
/*     */   private final String name;
/*     */   private LogEventFactory logEventFactory;
/*     */   private Level level;
/*     */   private boolean additive = true;
/*     */   private boolean includeLocation = true;
/*     */   private LoggerConfig parent;
/*     */   private Map<Property, Boolean> propertiesMap;
/*     */   private final List<Property> properties;
/*     */   private final boolean propertiesRequireLookup;
/*     */   private final Configuration config;
/*     */   private final ReliabilityStrategy reliabilityStrategy;
/*     */   
/*     */   static {
/*  78 */     String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
/*  79 */     if (factory != null) {
/*     */       try {
/*  81 */         Class<?> clazz = LoaderUtil.loadClass(factory);
/*  82 */         if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
/*  83 */           LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
/*     */         }
/*  85 */       } catch (Exception ex) {
/*  86 */         LOGGER.error("Unable to create LogEventFactory {}", factory, ex);
/*     */       } 
/*     */     }
/*  89 */     if (LOG_EVENT_FACTORY == null) {
/*  90 */       LOG_EVENT_FACTORY = Constants.ENABLE_THREADLOCALS ? (LogEventFactory)new ReusableLogEventFactory() : (LogEventFactory)new DefaultLogEventFactory();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig() {
/* 100 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 101 */     this.level = Level.ERROR;
/* 102 */     this.name = "";
/* 103 */     this.properties = null;
/* 104 */     this.propertiesRequireLookup = false;
/* 105 */     this.config = null;
/* 106 */     this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig(String name, Level level, boolean additive) {
/* 117 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 118 */     this.name = name;
/* 119 */     this.level = level;
/* 120 */     this.additive = additive;
/* 121 */     this.properties = null;
/* 122 */     this.propertiesRequireLookup = false;
/* 123 */     this.config = null;
/* 124 */     this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/* 130 */     super(filter);
/* 131 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 132 */     this.name = name;
/* 133 */     this.appenderRefs = appenders;
/* 134 */     this.level = level;
/* 135 */     this.additive = additive;
/* 136 */     this.includeLocation = includeLocation;
/* 137 */     this.config = config;
/* 138 */     if (properties != null && properties.length > 0) {
/* 139 */       this.properties = Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(properties, properties.length)));
/*     */     } else {
/*     */       
/* 142 */       this.properties = null;
/*     */     } 
/* 144 */     this.propertiesRequireLookup = containsPropertyRequiringLookup(properties);
/* 145 */     this.reliabilityStrategy = config.getReliabilityStrategy(this);
/*     */   }
/*     */   
/*     */   private static boolean containsPropertyRequiringLookup(Property[] properties) {
/* 149 */     if (properties == null) {
/* 150 */       return false;
/*     */     }
/* 152 */     for (int i = 0; i < properties.length; i++) {
/* 153 */       if (properties[i].isValueNeedsLookup()) {
/* 154 */         return true;
/*     */       }
/*     */     } 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 162 */     return super.getFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 171 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(LoggerConfig parent) {
/* 180 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig getParent() {
/* 189 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender, Level level, Filter filter) {
/* 200 */     this.appenders.add(new AppenderControl(appender, level, filter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 209 */     AppenderControl removed = null;
/* 210 */     while ((removed = this.appenders.remove(name)) != null) {
/* 211 */       cleanupFilter(removed);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 221 */     return this.appenders.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearAppenders() {
/*     */     do {
/* 229 */       AppenderControl[] original = this.appenders.clear();
/* 230 */       for (AppenderControl ctl : original) {
/* 231 */         cleanupFilter(ctl);
/*     */       }
/* 233 */     } while (!this.appenders.isEmpty());
/*     */   }
/*     */   
/*     */   private void cleanupFilter(AppenderControl ctl) {
/* 237 */     Filter filter = ctl.getFilter();
/* 238 */     if (filter != null) {
/* 239 */       ctl.removeFilter(filter);
/* 240 */       filter.stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AppenderRef> getAppenderRefs() {
/* 250 */     return this.appenderRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/* 259 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 268 */     return (this.level == null) ? this.parent.getLevel() : this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEventFactory getLogEventFactory() {
/* 277 */     return this.logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogEventFactory(LogEventFactory logEventFactory) {
/* 286 */     this.logEventFactory = logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 295 */     return this.additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 304 */     this.additive = additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 314 */     return this.includeLocation;
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
/*     */   @Deprecated
/*     */   public Map<Property, Boolean> getProperties() {
/* 332 */     if (this.properties == null) {
/* 333 */       return null;
/*     */     }
/* 335 */     if (this.propertiesMap == null) {
/* 336 */       Map<Property, Boolean> result = new HashMap<>(this.properties.size() * 2);
/* 337 */       for (int i = 0; i < this.properties.size(); i++) {
/* 338 */         result.put(this.properties.get(i), Boolean.valueOf(((Property)this.properties.get(i)).isValueNeedsLookup()));
/*     */       }
/* 340 */       this.propertiesMap = Collections.unmodifiableMap(result);
/*     */     } 
/* 342 */     return this.propertiesMap;
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
/*     */   public List<Property> getPropertyList() {
/* 358 */     return this.properties;
/*     */   }
/*     */   
/*     */   public boolean isPropertiesRequireLookup() {
/* 362 */     return this.propertiesRequireLookup;
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
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public void log(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/* 378 */     List<Property> props = null;
/* 379 */     if (!this.propertiesRequireLookup) {
/* 380 */       props = this.properties;
/*     */     }
/* 382 */     else if (this.properties != null) {
/* 383 */       props = new ArrayList<>(this.properties.size());
/* 384 */       Log4jLogEvent log4jLogEvent = Log4jLogEvent.newBuilder().setMessage(data).setMarker(marker).setLevel(level).setLoggerName(loggerName).setLoggerFqcn(fqcn).setThrown(t).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 392 */       for (int i = 0; i < this.properties.size(); i++) {
/* 393 */         Property prop = this.properties.get(i);
/* 394 */         String value = prop.isValueNeedsLookup() ? this.config.getStrSubstitutor().replace((LogEvent)log4jLogEvent, prop.getValue()) : prop.getValue();
/*     */ 
/*     */         
/* 397 */         props.add(Property.createProperty(prop.getName(), value));
/*     */       } 
/*     */     } 
/*     */     
/* 401 */     LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, level, data, props, t);
/*     */     try {
/* 403 */       log(logEvent);
/*     */     } finally {
/*     */       
/* 406 */       ReusableLogEventFactory.release(logEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(LogEvent event) {
/* 416 */     if (!isFiltered(event)) {
/* 417 */       processLogEvent(event);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliabilityStrategy getReliabilityStrategy() {
/* 428 */     return this.reliabilityStrategy;
/*     */   }
/*     */   
/*     */   private void processLogEvent(LogEvent event) {
/* 432 */     event.setIncludeLocation(isIncludeLocation());
/* 433 */     callAppenders(event);
/* 434 */     logParent(event);
/*     */   }
/*     */   
/*     */   private void logParent(LogEvent event) {
/* 438 */     if (this.additive && this.parent != null) {
/* 439 */       this.parent.log(event);
/*     */     }
/*     */   }
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   protected void callAppenders(LogEvent event) {
/* 445 */     AppenderControl[] controls = this.appenders.get();
/*     */     
/* 447 */     for (int i = 0; i < controls.length; i++) {
/* 448 */       controls[i].callAppender(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 454 */     return Strings.isEmpty(this.name) ? "root" : this.name;
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static LoggerConfig createLogger(String additivity, Level level, @PluginAttribute("name") String loggerName, String includeLocation, AppenderRef[] refs, Property[] properties, @PluginConfiguration Configuration config, Filter filter) {
/* 482 */     if (loggerName == null) {
/* 483 */       LOGGER.error("Loggers cannot be configured without a name");
/* 484 */       return null;
/*     */     } 
/*     */     
/* 487 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 488 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 489 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 491 */     return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) boolean additivity, @PluginAttribute("level") Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 522 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 523 */     return new LoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, includeLocation(includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 530 */     if (includeLocationConfigValue == null) {
/* 531 */       boolean sync = !AsyncLoggerContextSelector.isSelected();
/* 532 */       return sync;
/*     */     } 
/* 534 */     return Boolean.parseBoolean(includeLocationConfigValue);
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
/*     */   @Plugin(name = "root", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 554 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 555 */       Level actualLevel = (level == null) ? Level.ERROR : level;
/* 556 */       boolean additive = Booleans.parseBoolean(additivity, true);
/*     */       
/* 558 */       return new LoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, includeLocation(includeLocation));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\LoggerConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */