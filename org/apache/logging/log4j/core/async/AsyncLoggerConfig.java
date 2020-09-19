/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ @Plugin(name = "asyncLogger", category = "Core", printObject = true)
/*     */ public class AsyncLoggerConfig
/*     */   extends LoggerConfig
/*     */ {
/*     */   private final AsyncLoggerConfigDelegate delegate;
/*     */   
/*     */   protected AsyncLoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/*  80 */     super(name, appenders, filter, level, additive, properties, config, includeLocation);
/*     */     
/*  82 */     this.delegate = config.getAsyncLoggerConfigDelegate();
/*  83 */     this.delegate.setLogEventFactory(getLogEventFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void callAppenders(LogEvent event) {
/*  92 */     populateLazilyInitializedFields(event);
/*     */     
/*  94 */     if (!this.delegate.tryEnqueue(event, this)) {
/*  95 */       EventRoute eventRoute = this.delegate.getEventRoute(event.getLevel());
/*  96 */       eventRoute.logMessage(this, event);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateLazilyInitializedFields(LogEvent event) {
/* 101 */     event.getSource();
/* 102 */     event.getThreadName();
/*     */   }
/*     */   
/*     */   void callAppendersInCurrentThread(LogEvent event) {
/* 106 */     super.callAppenders(event);
/*     */   }
/*     */   
/*     */   void callAppendersInBackgroundThread(LogEvent event) {
/* 110 */     this.delegate.enqueueEvent(event, this);
/*     */   }
/*     */ 
/*     */   
/*     */   void asyncCallAppenders(LogEvent event) {
/* 115 */     super.callAppenders(event);
/*     */   }
/*     */   
/*     */   private String displayName() {
/* 119 */     return "".equals(getName()) ? "root" : getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 124 */     LOGGER.trace("AsyncLoggerConfig[{}] starting...", displayName());
/* 125 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 130 */     setStopping();
/* 131 */     stop(timeout, timeUnit, false);
/* 132 */     LOGGER.trace("AsyncLoggerConfig[{}] stopping...", displayName());
/* 133 */     setStopped();
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName) {
/* 145 */     return this.delegate.createRingBufferAdmin(contextName, getName());
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
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") String levelName, @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/*     */     Level level;
/* 171 */     if (loggerName == null) {
/* 172 */       LOGGER.error("Loggers cannot be configured without a name");
/* 173 */       return null;
/*     */     } 
/*     */     
/* 176 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/*     */     
/*     */     try {
/* 179 */       level = Level.toLevel(levelName, Level.ERROR);
/* 180 */     } catch (Exception ex) {
/* 181 */       LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
/*     */ 
/*     */       
/* 184 */       level = Level.ERROR;
/*     */     } 
/* 186 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 187 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 189 */     return new AsyncLoggerConfig(name, appenderRefs, filter, level, additive, properties, config, includeLocation(includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 195 */     return Boolean.parseBoolean(includeLocationConfigValue);
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
/*     */   @Plugin(name = "asyncRoot", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") String levelName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/*     */       Level level;
/* 213 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/*     */       
/*     */       try {
/* 216 */         level = Level.toLevel(levelName, Level.ERROR);
/* 217 */       } catch (Exception ex) {
/* 218 */         LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
/*     */ 
/*     */         
/* 221 */         level = Level.ERROR;
/*     */       } 
/* 223 */       boolean additive = Booleans.parseBoolean(additivity, true);
/*     */       
/* 225 */       return new AsyncLoggerConfig("", appenderRefs, filter, level, additive, properties, config, AsyncLoggerConfig.includeLocation(includeLocation));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */