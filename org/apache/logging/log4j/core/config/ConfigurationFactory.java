/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*     */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ public abstract class ConfigurationFactory
/*     */   extends ConfigurationBuilderFactory
/*     */ {
/*     */   public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
/*     */   public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
/*     */   public static final String CATEGORY = "ConfigurationFactory";
/* 105 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String TEST_PREFIX = "log4j2-test";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DEFAULT_PREFIX = "log4j2";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CLASS_LOADER_SCHEME = "classloader";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CLASS_PATH_SCHEME = "classpath";
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static volatile List<ConfigurationFactory> factories = null;
/*     */   
/* 129 */   private static ConfigurationFactory configFactory = new Factory();
/*     */   
/* 131 */   protected final StrSubstitutor substitutor = new StrSubstitutor((StrLookup)new Interpolator());
/*     */   
/* 133 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurationFactory getInstance() {
/* 142 */     if (factories == null) {
/* 143 */       LOCK.lock();
/*     */       try {
/* 145 */         if (factories == null) {
/* 146 */           List<ConfigurationFactory> list = new ArrayList<>();
/* 147 */           String factoryClass = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
/* 148 */           if (factoryClass != null) {
/* 149 */             addFactory(list, factoryClass);
/*     */           }
/* 151 */           PluginManager manager = new PluginManager("ConfigurationFactory");
/* 152 */           manager.collectPlugins();
/* 153 */           Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 154 */           List<Class<? extends ConfigurationFactory>> ordered = new ArrayList<>(plugins.size());
/* 155 */           for (PluginType<?> type : plugins.values()) {
/*     */             try {
/* 157 */               ordered.add(type.getPluginClass().asSubclass(ConfigurationFactory.class));
/* 158 */             } catch (Exception ex) {
/* 159 */               LOGGER.warn("Unable to add class {}", type.getPluginClass(), ex);
/*     */             } 
/*     */           } 
/* 162 */           Collections.sort(ordered, OrderComparator.getInstance());
/* 163 */           for (Class<? extends ConfigurationFactory> clazz : ordered) {
/* 164 */             addFactory(list, clazz);
/*     */           }
/*     */ 
/*     */           
/* 168 */           factories = Collections.unmodifiableList(list);
/*     */         } 
/*     */       } finally {
/* 171 */         LOCK.unlock();
/*     */       } 
/*     */     } 
/*     */     
/* 175 */     LOGGER.debug("Using configurationFactory {}", configFactory);
/* 176 */     return configFactory;
/*     */   }
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, String factoryClass) {
/*     */     try {
/* 181 */       addFactory(list, LoaderUtil.loadClass(factoryClass).asSubclass(ConfigurationFactory.class));
/* 182 */     } catch (Exception ex) {
/* 183 */       LOGGER.error("Unable to load class {}", factoryClass, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, Class<? extends ConfigurationFactory> factoryClass) {
/*     */     try {
/* 190 */       list.add(ReflectionUtil.instantiate(factoryClass));
/* 191 */     } catch (Exception ex) {
/* 192 */       LOGGER.error("Unable to create instance of {}", factoryClass.getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConfigurationFactory(ConfigurationFactory factory) {
/* 201 */     configFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetConfigurationFactory() {
/* 209 */     configFactory = new Factory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeConfigurationFactory(ConfigurationFactory factory) {
/* 217 */     if (configFactory == factory) {
/* 218 */       configFactory = new Factory();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract String[] getSupportedTypes();
/*     */   
/*     */   protected boolean isActive() {
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Configuration getConfiguration(LoggerContext paramLoggerContext, ConfigurationSource paramConfigurationSource);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
/* 238 */     if (!isActive()) {
/* 239 */       return null;
/*     */     }
/* 241 */     if (configLocation != null) {
/* 242 */       ConfigurationSource source = getInputFromUri(configLocation);
/* 243 */       if (source != null) {
/* 244 */         return getConfiguration(loggerContext, source);
/*     */       }
/*     */     } 
/* 247 */     return null;
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
/*     */   public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation, ClassLoader loader) {
/* 261 */     if (!isActive()) {
/* 262 */       return null;
/*     */     }
/* 264 */     if (loader == null) {
/* 265 */       return getConfiguration(loggerContext, name, configLocation);
/*     */     }
/* 267 */     if (isClassLoaderUri(configLocation)) {
/* 268 */       String path = extractClassLoaderUriPath(configLocation);
/* 269 */       ConfigurationSource source = getInputFromResource(path, loader);
/* 270 */       if (source != null) {
/* 271 */         Configuration configuration = getConfiguration(loggerContext, source);
/* 272 */         if (configuration != null) {
/* 273 */           return configuration;
/*     */         }
/*     */       } 
/*     */     } 
/* 277 */     return getConfiguration(loggerContext, name, configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromUri(URI configLocation) {
/* 286 */     File configFile = FileUtils.fileFromUri(configLocation);
/* 287 */     if (configFile != null && configFile.exists() && configFile.canRead()) {
/*     */       try {
/* 289 */         return new ConfigurationSource(new FileInputStream(configFile), configFile);
/* 290 */       } catch (FileNotFoundException ex) {
/* 291 */         LOGGER.error("Cannot locate file {}", configLocation.getPath(), ex);
/*     */       } 
/*     */     }
/* 294 */     if (isClassLoaderUri(configLocation)) {
/* 295 */       ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 296 */       String path = extractClassLoaderUriPath(configLocation);
/* 297 */       ConfigurationSource source = getInputFromResource(path, loader);
/* 298 */       if (source != null) {
/* 299 */         return source;
/*     */       }
/*     */     } 
/* 302 */     if (!configLocation.isAbsolute()) {
/* 303 */       LOGGER.error("File not found in file system or classpath: {}", configLocation.toString());
/* 304 */       return null;
/*     */     } 
/*     */     try {
/* 307 */       return new ConfigurationSource(configLocation.toURL().openStream(), configLocation.toURL());
/* 308 */     } catch (MalformedURLException ex) {
/* 309 */       LOGGER.error("Invalid URL {}", configLocation.toString(), ex);
/* 310 */     } catch (Exception ex) {
/* 311 */       LOGGER.error("Unable to access {}", configLocation.toString(), ex);
/*     */     } 
/* 313 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isClassLoaderUri(URI uri) {
/* 317 */     if (uri == null) {
/* 318 */       return false;
/*     */     }
/* 320 */     String scheme = uri.getScheme();
/* 321 */     return (scheme == null || scheme.equals("classloader") || scheme.equals("classpath"));
/*     */   }
/*     */   
/*     */   private static String extractClassLoaderUriPath(URI uri) {
/* 325 */     return (uri.getScheme() == null) ? uri.getPath() : uri.getSchemeSpecificPart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromString(String config, ClassLoader loader) {
/*     */     try {
/* 336 */       URL url = new URL(config);
/* 337 */       return new ConfigurationSource(url.openStream(), FileUtils.fileFromUri(url.toURI()));
/* 338 */     } catch (Exception ex) {
/* 339 */       ConfigurationSource source = getInputFromResource(config, loader);
/* 340 */       if (source == null) {
/*     */         try {
/* 342 */           File file = new File(config);
/* 343 */           return new ConfigurationSource(new FileInputStream(file), file);
/* 344 */         } catch (FileNotFoundException fnfe) {
/*     */           
/* 346 */           LOGGER.catching(Level.DEBUG, fnfe);
/*     */         } 
/*     */       }
/* 349 */       return source;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromResource(String resource, ClassLoader loader) {
/* 360 */     URL url = Loader.getResource(resource, loader);
/* 361 */     if (url == null) {
/* 362 */       return null;
/*     */     }
/* 364 */     InputStream is = null;
/*     */     try {
/* 366 */       is = url.openStream();
/* 367 */     } catch (IOException ioe) {
/* 368 */       LOGGER.catching(Level.DEBUG, ioe);
/* 369 */       return null;
/*     */     } 
/* 371 */     if (is == null) {
/* 372 */       return null;
/*     */     }
/*     */     
/* 375 */     if (FileUtils.isFile(url)) {
/*     */       try {
/* 377 */         return new ConfigurationSource(is, FileUtils.fileFromUri(url.toURI()));
/* 378 */       } catch (URISyntaxException ex) {
/*     */         
/* 380 */         LOGGER.catching(Level.DEBUG, ex);
/*     */       } 
/*     */     }
/* 383 */     return new ConfigurationSource(is, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Factory
/*     */     extends ConfigurationFactory
/*     */   {
/*     */     private static final String ALL_TYPES = "*";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Factory() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
/* 402 */       if (configLocation == null) {
/* 403 */         String configLocationStr = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
/*     */         
/* 405 */         if (configLocationStr != null) {
/* 406 */           String[] sources = configLocationStr.split(",");
/* 407 */           if (sources.length > 1) {
/* 408 */             List<AbstractConfiguration> configs = new ArrayList<>();
/* 409 */             for (String sourceLocation : sources) {
/* 410 */               Configuration configuration = getConfiguration(loggerContext, sourceLocation.trim());
/* 411 */               if (configuration != null && configuration instanceof AbstractConfiguration) {
/* 412 */                 configs.add((AbstractConfiguration)configuration);
/*     */               } else {
/* 414 */                 LOGGER.error("Failed to created configuration at {}", sourceLocation);
/* 415 */                 return null;
/*     */               } 
/*     */             } 
/* 418 */             return (Configuration)new CompositeConfiguration(configs);
/*     */           } 
/* 420 */           return getConfiguration(loggerContext, configLocationStr);
/*     */         } 
/* 422 */         for (ConfigurationFactory factory : getFactories()) {
/* 423 */           String[] types = factory.getSupportedTypes();
/* 424 */           if (types != null) {
/* 425 */             for (String type : types) {
/* 426 */               if (type.equals("*")) {
/* 427 */                 Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
/* 428 */                 if (configuration != null) {
/* 429 */                   return configuration;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 437 */         String configLocationStr = configLocation.toString();
/* 438 */         for (ConfigurationFactory factory : getFactories()) {
/* 439 */           String[] types = factory.getSupportedTypes();
/* 440 */           if (types != null) {
/* 441 */             for (String type : types) {
/* 442 */               if (type.equals("*") || configLocationStr.endsWith(type)) {
/* 443 */                 Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
/* 444 */                 if (configuration != null) {
/* 445 */                   return configuration;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 453 */       Configuration config = getConfiguration(loggerContext, true, name);
/* 454 */       if (config == null) {
/* 455 */         config = getConfiguration(loggerContext, true, (String)null);
/* 456 */         if (config == null) {
/* 457 */           config = getConfiguration(loggerContext, false, name);
/* 458 */           if (config == null) {
/* 459 */             config = getConfiguration(loggerContext, false, (String)null);
/*     */           }
/*     */         } 
/*     */       } 
/* 463 */       if (config != null) {
/* 464 */         return config;
/*     */       }
/* 466 */       LOGGER.error("No log4j2 configuration file found. Using default configuration: logging only errors to the console. Set system property 'org.apache.logging.log4j.simplelog.StatusLogger.level' to TRACE to show Log4j2 internal initialization logging.");
/*     */ 
/*     */ 
/*     */       
/* 470 */       return new DefaultConfiguration();
/*     */     }
/*     */     
/*     */     private Configuration getConfiguration(LoggerContext loggerContext, String configLocationStr) {
/* 474 */       ConfigurationSource source = null;
/*     */       try {
/* 476 */         source = getInputFromUri(NetUtils.toURI(configLocationStr));
/* 477 */       } catch (Exception ex) {
/*     */         
/* 479 */         LOGGER.catching(Level.DEBUG, ex);
/*     */       } 
/* 481 */       if (source == null) {
/* 482 */         ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 483 */         source = getInputFromString(configLocationStr, loader);
/*     */       } 
/* 485 */       if (source != null) {
/* 486 */         for (ConfigurationFactory factory : getFactories()) {
/* 487 */           String[] types = factory.getSupportedTypes();
/* 488 */           if (types != null) {
/* 489 */             for (String type : types) {
/* 490 */               if (type.equals("*") || configLocationStr.endsWith(type)) {
/* 491 */                 Configuration config = factory.getConfiguration(loggerContext, source);
/* 492 */                 if (config != null) {
/* 493 */                   return config;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       }
/* 500 */       return null;
/*     */     }
/*     */     
/*     */     private Configuration getConfiguration(LoggerContext loggerContext, boolean isTest, String name) {
/* 504 */       boolean named = Strings.isNotEmpty(name);
/* 505 */       ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 506 */       for (ConfigurationFactory factory : getFactories()) {
/*     */         
/* 508 */         String prefix = isTest ? "log4j2-test" : "log4j2";
/* 509 */         String[] types = factory.getSupportedTypes();
/* 510 */         if (types == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 514 */         for (String suffix : types) {
/* 515 */           if (!suffix.equals("*")) {
/*     */ 
/*     */             
/* 518 */             String configName = named ? (prefix + name + suffix) : (prefix + suffix);
/*     */             
/* 520 */             ConfigurationSource source = getInputFromResource(configName, loader);
/* 521 */             if (source != null)
/* 522 */               return factory.getConfiguration(loggerContext, source); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 526 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getSupportedTypes() {
/* 531 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 536 */       if (source != null) {
/* 537 */         String config = source.getLocation();
/* 538 */         for (ConfigurationFactory factory : getFactories()) {
/* 539 */           String[] types = factory.getSupportedTypes();
/* 540 */           if (types != null) {
/* 541 */             for (String type : types) {
/* 542 */               if (type.equals("*") || (config != null && config.endsWith(type))) {
/* 543 */                 Configuration c = factory.getConfiguration(loggerContext, source);
/* 544 */                 if (c != null) {
/* 545 */                   LOGGER.debug("Loaded configuration from {}", source);
/* 546 */                   return c;
/*     */                 } 
/* 548 */                 LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
/* 549 */                 return null;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 555 */       LOGGER.error("Cannot process configuration, input source is null");
/* 556 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   static List<ConfigurationFactory> getFactories() {
/* 561 */     return factories;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\ConfigurationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */