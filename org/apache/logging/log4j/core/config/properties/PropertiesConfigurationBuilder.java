/*     */ package org.apache.logging.log4j.core.config.properties;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
/*     */ import org.apache.logging.log4j.core.util.Builder;
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
/*     */ public class PropertiesConfigurationBuilder
/*     */   extends ConfigurationBuilderFactory
/*     */   implements Builder<PropertiesConfiguration>
/*     */ {
/*     */   private static final String ADVERTISER_KEY = "advertiser";
/*     */   private static final String STATUS_KEY = "status";
/*     */   private static final String SHUTDOWN_HOOK = "shutdownHook";
/*     */   private static final String SHUTDOWN_TIMEOUT = "shutdownTimeout";
/*     */   private static final String VERBOSE = "verbose";
/*     */   private static final String DEST = "dest";
/*     */   private static final String PACKAGES = "packages";
/*     */   private static final String CONFIG_NAME = "name";
/*     */   private static final String MONITOR_INTERVAL = "monitorInterval";
/*     */   private static final String CONFIG_TYPE = "type";
/*  71 */   private final ConfigurationBuilder<PropertiesConfiguration> builder = newConfigurationBuilder(PropertiesConfiguration.class);
/*     */   private LoggerContext loggerContext;
/*     */   
/*     */   public PropertiesConfigurationBuilder setRootProperties(Properties rootProperties) {
/*  75 */     this.rootProperties = rootProperties;
/*  76 */     return this;
/*     */   }
/*     */   private Properties rootProperties;
/*     */   public PropertiesConfigurationBuilder setConfigurationSource(ConfigurationSource source) {
/*  80 */     this.builder.setConfigurationSource(source);
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertiesConfiguration build() {
/*  86 */     for (String key : this.rootProperties.stringPropertyNames()) {
/*  87 */       if (!key.contains(".")) {
/*  88 */         this.builder.addRootProperty(key, this.rootProperties.getProperty(key));
/*     */       }
/*     */     } 
/*  91 */     this.builder.setStatusLevel(Level.toLevel(this.rootProperties.getProperty("status"), Level.ERROR)).setShutdownHook(this.rootProperties.getProperty("shutdownHook")).setShutdownTimeout(Long.parseLong(this.rootProperties.getProperty("shutdownTimeout", "0")), TimeUnit.MILLISECONDS).setVerbosity(this.rootProperties.getProperty("verbose")).setDestination(this.rootProperties.getProperty("dest")).setPackages(this.rootProperties.getProperty("packages")).setConfigurationName(this.rootProperties.getProperty("name")).setMonitorInterval(this.rootProperties.getProperty("monitorInterval", "0")).setAdvertiser(this.rootProperties.getProperty("advertiser"));
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
/* 102 */     Properties propertyPlaceholders = PropertiesUtil.extractSubset(this.rootProperties, "property");
/* 103 */     for (String key : propertyPlaceholders.stringPropertyNames()) {
/* 104 */       this.builder.addProperty(key, propertyPlaceholders.getProperty(key));
/*     */     }
/*     */     
/* 107 */     Map<String, Properties> scripts = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "script"));
/*     */     
/* 109 */     for (Map.Entry<String, Properties> entry : scripts.entrySet()) {
/* 110 */       Properties scriptProps = entry.getValue();
/* 111 */       String type = (String)scriptProps.remove("type");
/* 112 */       if (type == null) {
/* 113 */         throw new ConfigurationException("No type provided for script - must be Script or ScriptFile");
/*     */       }
/* 115 */       if (type.equalsIgnoreCase("script")) {
/* 116 */         this.builder.add(createScript(scriptProps)); continue;
/*     */       } 
/* 118 */       this.builder.add(createScriptFile(scriptProps));
/*     */     } 
/*     */ 
/*     */     
/* 122 */     Properties levelProps = PropertiesUtil.extractSubset(this.rootProperties, "customLevel");
/* 123 */     if (levelProps.size() > 0) {
/* 124 */       for (String key : levelProps.stringPropertyNames()) {
/* 125 */         this.builder.add(this.builder.newCustomLevel(key, Integer.parseInt(levelProps.getProperty(key))));
/*     */       }
/*     */     }
/*     */     
/* 129 */     String filterProp = this.rootProperties.getProperty("filters");
/* 130 */     if (filterProp != null) {
/* 131 */       String[] filterNames = filterProp.split(",");
/* 132 */       for (String filterName : filterNames) {
/* 133 */         String name = filterName.trim();
/* 134 */         this.builder.add(createFilter(name, PropertiesUtil.extractSubset(this.rootProperties, "filter." + name)));
/*     */       } 
/*     */     } else {
/*     */       
/* 138 */       Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "filter"));
/*     */       
/* 140 */       for (Map.Entry<String, Properties> entry : filters.entrySet()) {
/* 141 */         this.builder.add(createFilter(((String)entry.getKey()).trim(), entry.getValue()));
/*     */       }
/*     */     } 
/*     */     
/* 145 */     String appenderProp = this.rootProperties.getProperty("appenders");
/* 146 */     if (appenderProp != null) {
/* 147 */       String[] appenderNames = appenderProp.split(",");
/* 148 */       for (String appenderName : appenderNames) {
/* 149 */         String name = appenderName.trim();
/* 150 */         this.builder.add(createAppender(appenderName.trim(), PropertiesUtil.extractSubset(this.rootProperties, "appender." + name)));
/*     */       } 
/*     */     } else {
/*     */       
/* 154 */       Map<String, Properties> appenders = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "appender"));
/*     */       
/* 156 */       for (Map.Entry<String, Properties> entry : appenders.entrySet()) {
/* 157 */         this.builder.add(createAppender(((String)entry.getKey()).trim(), entry.getValue()));
/*     */       }
/*     */     } 
/*     */     
/* 161 */     String loggerProp = this.rootProperties.getProperty("loggers");
/* 162 */     if (loggerProp != null) {
/* 163 */       String[] loggerNames = loggerProp.split(",");
/* 164 */       for (String loggerName : loggerNames) {
/* 165 */         String name = loggerName.trim();
/* 166 */         if (!name.equals("root")) {
/* 167 */           this.builder.add(createLogger(name, PropertiesUtil.extractSubset(this.rootProperties, "logger." + name)));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 172 */       Map<String, Properties> loggers = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "logger"));
/*     */       
/* 174 */       for (Map.Entry<String, Properties> entry : loggers.entrySet()) {
/* 175 */         String name = ((String)entry.getKey()).trim();
/* 176 */         if (!name.equals("root")) {
/* 177 */           this.builder.add(createLogger(name, entry.getValue()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     Properties props = PropertiesUtil.extractSubset(this.rootProperties, "rootLogger");
/* 183 */     if (props.size() > 0) {
/* 184 */       this.builder.add(createRootLogger(props));
/*     */     }
/*     */     
/* 187 */     this.builder.setLoggerContext(this.loggerContext);
/*     */     
/* 189 */     return (PropertiesConfiguration)this.builder.build(false);
/*     */   }
/*     */   
/*     */   private ScriptComponentBuilder createScript(Properties properties) {
/* 193 */     String name = (String)properties.remove("name");
/* 194 */     String language = (String)properties.remove("language");
/* 195 */     String text = (String)properties.remove("text");
/* 196 */     ScriptComponentBuilder scriptBuilder = this.builder.newScript(name, language, text);
/* 197 */     return processRemainingProperties(scriptBuilder, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   private ScriptFileComponentBuilder createScriptFile(Properties properties) {
/* 202 */     String name = (String)properties.remove("name");
/* 203 */     String path = (String)properties.remove("path");
/* 204 */     ScriptFileComponentBuilder scriptFileBuilder = this.builder.newScriptFile(name, path);
/* 205 */     return processRemainingProperties(scriptFileBuilder, properties);
/*     */   }
/*     */   
/*     */   private AppenderComponentBuilder createAppender(String key, Properties properties) {
/* 209 */     String name = (String)properties.remove("name");
/* 210 */     if (Strings.isEmpty(name)) {
/* 211 */       throw new ConfigurationException("No name attribute provided for Appender " + key);
/*     */     }
/* 213 */     String type = (String)properties.remove("type");
/* 214 */     if (Strings.isEmpty(type)) {
/* 215 */       throw new ConfigurationException("No type attribute provided for Appender " + key);
/*     */     }
/* 217 */     AppenderComponentBuilder appenderBuilder = this.builder.newAppender(name, type);
/* 218 */     addFiltersToComponent(appenderBuilder, properties);
/* 219 */     Properties layoutProps = PropertiesUtil.extractSubset(properties, "layout");
/* 220 */     if (layoutProps.size() > 0) {
/* 221 */       appenderBuilder.add(createLayout(name, layoutProps));
/*     */     }
/*     */     
/* 224 */     return processRemainingProperties(appenderBuilder, properties);
/*     */   }
/*     */   
/*     */   private FilterComponentBuilder createFilter(String key, Properties properties) {
/* 228 */     String type = (String)properties.remove("type");
/* 229 */     if (Strings.isEmpty(type)) {
/* 230 */       throw new ConfigurationException("No type attribute provided for Appender " + key);
/*     */     }
/* 232 */     String onMatch = (String)properties.remove("onMatch");
/* 233 */     String onMisMatch = (String)properties.remove("onMisMatch");
/* 234 */     FilterComponentBuilder filterBuilder = this.builder.newFilter(type, onMatch, onMisMatch);
/* 235 */     return processRemainingProperties(filterBuilder, properties);
/*     */   }
/*     */   
/*     */   private AppenderRefComponentBuilder createAppenderRef(String key, Properties properties) {
/* 239 */     String ref = (String)properties.remove("ref");
/* 240 */     if (Strings.isEmpty(ref)) {
/* 241 */       throw new ConfigurationException("No ref attribute provided for AppenderRef " + key);
/*     */     }
/* 243 */     AppenderRefComponentBuilder appenderRefBuilder = this.builder.newAppenderRef(ref);
/* 244 */     String level = (String)properties.remove("level");
/* 245 */     if (!Strings.isEmpty(level)) {
/* 246 */       appenderRefBuilder.addAttribute("level", level);
/*     */     }
/* 248 */     return addFiltersToComponent(appenderRefBuilder, properties);
/*     */   }
/*     */   private LoggerComponentBuilder createLogger(String key, Properties properties) {
/*     */     LoggerComponentBuilder loggerBuilder;
/* 252 */     String name = (String)properties.remove("name");
/* 253 */     String location = (String)properties.remove("includeLocation");
/* 254 */     if (Strings.isEmpty(name)) {
/* 255 */       throw new ConfigurationException("No name attribute provided for Logger " + key);
/*     */     }
/* 257 */     String level = (String)properties.remove("level");
/* 258 */     String type = (String)properties.remove("type");
/*     */ 
/*     */     
/* 261 */     if (type != null) {
/* 262 */       if (type.equalsIgnoreCase("asyncLogger")) {
/* 263 */         if (location != null) {
/* 264 */           boolean includeLocation = Boolean.parseBoolean(location);
/* 265 */           loggerBuilder = this.builder.newAsyncLogger(name, level, includeLocation);
/*     */         } else {
/* 267 */           loggerBuilder = this.builder.newAsyncLogger(name, level);
/*     */         } 
/*     */       } else {
/* 270 */         throw new ConfigurationException("Unknown Logger type " + type + " for Logger " + name);
/*     */       }
/*     */     
/* 273 */     } else if (location != null) {
/* 274 */       boolean includeLocation = Boolean.parseBoolean(location);
/* 275 */       loggerBuilder = this.builder.newLogger(name, level, includeLocation);
/*     */     } else {
/* 277 */       loggerBuilder = this.builder.newLogger(name, level);
/*     */     } 
/*     */     
/* 280 */     addLoggersToComponent(loggerBuilder, properties);
/* 281 */     addFiltersToComponent(loggerBuilder, properties);
/* 282 */     String additivity = (String)properties.remove("additivity");
/* 283 */     if (!Strings.isEmpty(additivity)) {
/* 284 */       loggerBuilder.addAttribute("additivity", additivity);
/*     */     }
/* 286 */     return loggerBuilder;
/*     */   }
/*     */   private RootLoggerComponentBuilder createRootLogger(Properties properties) {
/*     */     RootLoggerComponentBuilder loggerBuilder;
/* 290 */     String level = (String)properties.remove("level");
/* 291 */     String type = (String)properties.remove("type");
/* 292 */     String location = (String)properties.remove("includeLocation");
/*     */ 
/*     */     
/* 295 */     if (type != null) {
/* 296 */       if (type.equalsIgnoreCase("asyncRoot")) {
/* 297 */         if (location != null) {
/* 298 */           boolean includeLocation = Boolean.parseBoolean(location);
/* 299 */           loggerBuilder = this.builder.newAsyncRootLogger(level, includeLocation);
/*     */         } else {
/* 301 */           loggerBuilder = this.builder.newAsyncRootLogger(level);
/*     */         } 
/*     */       } else {
/* 304 */         throw new ConfigurationException("Unknown Logger type for root logger" + type);
/*     */       }
/*     */     
/* 307 */     } else if (location != null) {
/* 308 */       boolean includeLocation = Boolean.parseBoolean(location);
/* 309 */       loggerBuilder = this.builder.newRootLogger(level, includeLocation);
/*     */     } else {
/* 311 */       loggerBuilder = this.builder.newRootLogger(level);
/*     */     } 
/*     */     
/* 314 */     addLoggersToComponent(loggerBuilder, properties);
/* 315 */     return addFiltersToComponent(loggerBuilder, properties);
/*     */   }
/*     */   
/*     */   private LayoutComponentBuilder createLayout(String appenderName, Properties properties) {
/* 319 */     String type = (String)properties.remove("type");
/* 320 */     if (Strings.isEmpty(type)) {
/* 321 */       throw new ConfigurationException("No type attribute provided for Layout on Appender " + appenderName);
/*     */     }
/* 323 */     LayoutComponentBuilder layoutBuilder = this.builder.newLayout(type);
/* 324 */     return processRemainingProperties(layoutBuilder, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <B extends ComponentBuilder<B>> ComponentBuilder<B> createComponent(ComponentBuilder<?> parent, String key, Properties properties) {
/* 330 */     String name = (String)properties.remove("name");
/* 331 */     String type = (String)properties.remove("type");
/* 332 */     if (Strings.isEmpty(type)) {
/* 333 */       throw new ConfigurationException("No type attribute provided for component " + key);
/*     */     }
/* 335 */     ComponentBuilder<B> componentBuilder = parent.getBuilder().newComponent(name, type);
/* 336 */     return processRemainingProperties(componentBuilder, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <B extends ComponentBuilder<?>> B processRemainingProperties(B builder, Properties properties) {
/* 341 */     while (properties.size() > 0) {
/* 342 */       String propertyName = properties.stringPropertyNames().iterator().next();
/* 343 */       int index = propertyName.indexOf('.');
/* 344 */       if (index > 0) {
/* 345 */         String prefix = propertyName.substring(0, index);
/* 346 */         Properties componentProperties = PropertiesUtil.extractSubset(properties, prefix);
/* 347 */         builder.addComponent(createComponent((ComponentBuilder<?>)builder, prefix, componentProperties)); continue;
/*     */       } 
/* 349 */       builder.addAttribute(propertyName, properties.getProperty(propertyName));
/* 350 */       properties.remove(propertyName);
/*     */     } 
/*     */     
/* 353 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <B extends org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder<? extends ComponentBuilder<?>>> B addFiltersToComponent(B componentBuilder, Properties properties) {
/* 358 */     Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "filter"));
/*     */     
/* 360 */     for (Map.Entry<String, Properties> entry : filters.entrySet()) {
/* 361 */       componentBuilder.add(createFilter(((String)entry.getKey()).trim(), entry.getValue()));
/*     */     }
/* 363 */     return componentBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <B extends org.apache.logging.log4j.core.config.builder.api.LoggableComponentBuilder<? extends ComponentBuilder<?>>> B addLoggersToComponent(B loggerBuilder, Properties properties) {
/* 368 */     Map<String, Properties> appenderRefs = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "appenderRef"));
/*     */     
/* 370 */     for (Map.Entry<String, Properties> entry : appenderRefs.entrySet()) {
/* 371 */       loggerBuilder.add(createAppenderRef(((String)entry.getKey()).trim(), entry.getValue()));
/*     */     }
/* 373 */     return loggerBuilder;
/*     */   }
/*     */   
/*     */   public PropertiesConfigurationBuilder setLoggerContext(LoggerContext loggerContext) {
/* 377 */     this.loggerContext = loggerContext;
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public LoggerContext getLoggerContext() {
/* 382 */     return this.loggerContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfigurationBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */