/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public final class Configurator
/*     */ {
/*  41 */   private static final String FQCN = Configurator.class.getName();
/*     */   
/*  43 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static Log4jContextFactory getFactory() {
/*  46 */     LoggerContextFactory factory = LogManager.getFactory();
/*  47 */     if (factory instanceof Log4jContextFactory)
/*  48 */       return (Log4jContextFactory)factory; 
/*  49 */     if (factory != null) {
/*  50 */       LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j.", factory.getClass().getName(), Log4jContextFactory.class.getName());
/*     */       
/*  52 */       return null;
/*     */     } 
/*  54 */     LOGGER.fatal("LogManager did not return a LoggerContextFactory. This indicates something has gone terribly wrong!");
/*  55 */     return null;
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
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source) {
/*  67 */     return initialize(loader, source, (Object)null);
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
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source, Object externalContext) {
/*     */     try {
/*  84 */       Log4jContextFactory factory = getFactory();
/*  85 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, source);
/*     */     }
/*  87 */     catch (Exception ex) {
/*  88 */       LOGGER.error("There was a problem obtaining a LoggerContext using the configuration source [{}]", source, ex);
/*     */       
/*  90 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation) {
/* 101 */     return initialize(name, loader, configLocation, (Object)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation, Object externalContext) {
/* 115 */     if (Strings.isBlank(configLocation)) {
/* 116 */       return initialize(name, loader, (URI)null, externalContext);
/*     */     }
/* 118 */     if (configLocation.contains(",")) {
/* 119 */       String[] parts = configLocation.split(",");
/* 120 */       String scheme = null;
/* 121 */       List<URI> uris = new ArrayList<>(parts.length);
/* 122 */       for (String part : parts) {
/* 123 */         URI uri = NetUtils.toURI((scheme != null) ? (scheme + ":" + part.trim()) : part.trim());
/* 124 */         if (scheme == null && uri.getScheme() != null) {
/* 125 */           scheme = uri.getScheme();
/*     */         }
/* 127 */         uris.add(uri);
/*     */       } 
/* 129 */       return initialize(name, loader, uris, externalContext);
/*     */     } 
/* 131 */     return initialize(name, loader, NetUtils.toURI(configLocation), externalContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation) {
/* 142 */     return initialize(name, loader, configLocation, (Object)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation, Object externalContext) {
/*     */     try {
/* 157 */       Log4jContextFactory factory = getFactory();
/* 158 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, configLocation, name);
/*     */     }
/* 160 */     catch (Exception ex) {
/* 161 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configuration at [{}].", name, configLocation, ex);
/*     */ 
/*     */       
/* 164 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, List<URI> configLocations, Object externalContext) {
/*     */     try {
/* 170 */       Log4jContextFactory factory = getFactory();
/* 171 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, configLocations, name);
/*     */     
/*     */     }
/* 174 */     catch (Exception ex) {
/* 175 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configurations at [{}].", name, configLocations, ex);
/*     */ 
/*     */       
/* 178 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, String configLocation) {
/* 188 */     return initialize(name, (ClassLoader)null, configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(Configuration configuration) {
/* 197 */     return initialize((ClassLoader)null, configuration, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, Configuration configuration) {
/* 207 */     return initialize(loader, configuration, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, Configuration configuration, Object externalContext) {
/*     */     try {
/* 219 */       Log4jContextFactory factory = getFactory();
/* 220 */       return (factory == null) ? null : factory.getContext(FQCN, loader, externalContext, false, configuration);
/*     */     }
/* 222 */     catch (Exception ex) {
/* 223 */       LOGGER.error("There was a problem initializing the LoggerContext using configuration {}", configuration.getName(), ex);
/*     */ 
/*     */       
/* 226 */       return null;
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
/*     */   public static void setAllLevels(String parentLogger, Level level) {
/* 240 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 241 */     Configuration config = loggerContext.getConfiguration();
/* 242 */     boolean set = setLevel(parentLogger, level, config);
/* 243 */     for (Map.Entry<String, LoggerConfig> entry : config.getLoggers().entrySet()) {
/* 244 */       if (((String)entry.getKey()).startsWith(parentLogger)) {
/* 245 */         set |= setLevel(entry.getValue(), level);
/*     */       }
/*     */     } 
/* 248 */     if (set) {
/* 249 */       loggerContext.updateLoggers();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean setLevel(LoggerConfig loggerConfig, Level level) {
/* 254 */     boolean set = !loggerConfig.getLevel().equals(level);
/* 255 */     if (set) {
/* 256 */       loggerConfig.setLevel(level);
/*     */     }
/* 258 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setLevel(Map<String, Level> levelMap) {
/* 269 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 270 */     Configuration config = loggerContext.getConfiguration();
/* 271 */     boolean set = false;
/* 272 */     for (Map.Entry<String, Level> entry : levelMap.entrySet()) {
/* 273 */       String loggerName = entry.getKey();
/* 274 */       Level level = entry.getValue();
/* 275 */       set |= setLevel(loggerName, level, config);
/*     */     } 
/* 277 */     if (set) {
/* 278 */       loggerContext.updateLoggers();
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
/*     */   public static void setLevel(String loggerName, Level level) {
/* 291 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 292 */     if (Strings.isEmpty(loggerName)) {
/* 293 */       setRootLevel(level);
/*     */     }
/* 295 */     else if (setLevel(loggerName, level, loggerContext.getConfiguration())) {
/* 296 */       loggerContext.updateLoggers();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean setLevel(String loggerName, Level level, Configuration config) {
/*     */     boolean set;
/* 303 */     LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
/* 304 */     if (!loggerName.equals(loggerConfig.getName())) {
/*     */       
/* 306 */       loggerConfig = new LoggerConfig(loggerName, level, true);
/* 307 */       config.addLogger(loggerName, loggerConfig);
/* 308 */       loggerConfig.setLevel(level);
/* 309 */       set = true;
/*     */     } else {
/* 311 */       set = setLevel(loggerConfig, level);
/*     */     } 
/* 313 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRootLevel(Level level) {
/* 323 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 324 */     LoggerConfig loggerConfig = loggerContext.getConfiguration().getRootLogger();
/* 325 */     if (!loggerConfig.getLevel().equals(level)) {
/* 326 */       loggerConfig.setLevel(level);
/* 327 */       loggerContext.updateLoggers();
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
/*     */   public static void shutdown(LoggerContext ctx) {
/* 343 */     if (ctx != null) {
/* 344 */       ctx.stop();
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
/*     */ 
/*     */   
/*     */   public static boolean shutdown(LoggerContext ctx, long timeout, TimeUnit timeUnit) {
/* 369 */     if (ctx != null) {
/* 370 */       return ctx.stop(timeout, timeUnit);
/*     */     }
/* 372 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\Configurator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */