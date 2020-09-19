/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.StringFormatterMessageFactory;
/*     */ import org.apache.logging.log4j.simple.SimpleLoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.Provider;
/*     */ import org.apache.logging.log4j.spi.Terminable;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
/*     */ import org.apache.logging.log4j.util.ReflectionUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogManager
/*     */ {
/*     */   public static final String FACTORY_PROPERTY_NAME = "log4j2.loggerContextFactory";
/*     */   public static final String ROOT_LOGGER_NAME = "";
/*  60 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*  63 */   private static final String FQCN = LogManager.class.getName();
/*     */ 
/*     */ 
/*     */   
/*     */   private static volatile LoggerContextFactory factory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  73 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  74 */     String factoryClassName = managerProps.getStringProperty("log4j2.loggerContextFactory");
/*  75 */     if (factoryClassName != null) {
/*     */       try {
/*  77 */         factory = (LoggerContextFactory)LoaderUtil.newCheckedInstanceOf(factoryClassName, LoggerContextFactory.class);
/*  78 */       } catch (ClassNotFoundException cnfe) {
/*  79 */         LOGGER.error("Unable to locate configured LoggerContextFactory {}", factoryClassName);
/*  80 */       } catch (Exception ex) {
/*  81 */         LOGGER.error("Unable to create configured LoggerContextFactory {}", factoryClassName, ex);
/*     */       } 
/*     */     }
/*     */     
/*  85 */     if (factory == null) {
/*  86 */       SortedMap<Integer, LoggerContextFactory> factories = new TreeMap<>();
/*     */ 
/*     */       
/*  89 */       if (ProviderUtil.hasProviders()) {
/*  90 */         for (Provider provider : ProviderUtil.getProviders()) {
/*  91 */           Class<? extends LoggerContextFactory> factoryClass = provider.loadLoggerContextFactory();
/*  92 */           if (factoryClass != null) {
/*     */             try {
/*  94 */               factories.put(provider.getPriority(), factoryClass.newInstance());
/*  95 */             } catch (Exception e) {
/*  96 */               LOGGER.error("Unable to create class {} specified in {}", factoryClass.getName(), provider.getUrl().toString(), e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 102 */         if (factories.isEmpty()) {
/* 103 */           LOGGER.error("Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...");
/*     */           
/* 105 */           factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
/* 106 */         } else if (factories.size() == 1) {
/* 107 */           factory = factories.get(factories.lastKey());
/*     */         } else {
/* 109 */           StringBuilder sb = new StringBuilder("Multiple logging implementations found: \n");
/* 110 */           for (Map.Entry<Integer, LoggerContextFactory> entry : factories.entrySet()) {
/* 111 */             sb.append("Factory: ").append(((LoggerContextFactory)entry.getValue()).getClass().getName());
/* 112 */             sb.append(", Weighting: ").append(entry.getKey()).append('\n');
/*     */           } 
/* 114 */           factory = factories.get(factories.lastKey());
/* 115 */           sb.append("Using factory: ").append(factory.getClass().getName());
/* 116 */           LOGGER.warn(sb.toString());
/*     */         } 
/*     */       } else {
/*     */         
/* 120 */         LOGGER.error("Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...");
/*     */         
/* 122 */         factory = (LoggerContextFactory)new SimpleLoggerContextFactory();
/*     */       } 
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
/*     */   public static boolean exists(String name) {
/* 141 */     return getContext().hasLogger(name);
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
/*     */   public static LoggerContext getContext() {
/*     */     try {
/* 155 */       return factory.getContext(FQCN, null, null, true);
/* 156 */     } catch (IllegalStateException ex) {
/* 157 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 158 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, null, null, true);
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
/*     */   public static LoggerContext getContext(boolean currentContext) {
/*     */     try {
/* 174 */       return factory.getContext(FQCN, null, null, currentContext, null, null);
/* 175 */     } catch (IllegalStateException ex) {
/* 176 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 177 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, null, null, currentContext, null, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext) {
/*     */     try {
/* 194 */       return factory.getContext(FQCN, loader, null, currentContext);
/* 195 */     } catch (IllegalStateException ex) {
/* 196 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 197 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, null, currentContext);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext) {
/*     */     try {
/* 216 */       return factory.getContext(FQCN, loader, externalContext, currentContext);
/* 217 */     } catch (IllegalStateException ex) {
/* 218 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 219 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
/*     */     try {
/* 238 */       return factory.getContext(FQCN, loader, null, currentContext, configLocation, null);
/* 239 */     } catch (IllegalStateException ex) {
/* 240 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 241 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, null, currentContext, configLocation, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation) {
/*     */     try {
/* 262 */       return factory.getContext(FQCN, loader, externalContext, currentContext, configLocation, null);
/* 263 */     } catch (IllegalStateException ex) {
/* 264 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 265 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext, configLocation, null);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, Object externalContext, URI configLocation, String name) {
/*     */     try {
/* 287 */       return factory.getContext(FQCN, loader, externalContext, currentContext, configLocation, name);
/* 288 */     } catch (IllegalStateException ex) {
/* 289 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 290 */       return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, externalContext, currentContext, configLocation, name);
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
/*     */   protected static LoggerContext getContext(String fqcn, boolean currentContext) {
/*     */     try {
/* 307 */       return factory.getContext(fqcn, null, null, currentContext);
/* 308 */     } catch (IllegalStateException ex) {
/* 309 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 310 */       return (new SimpleLoggerContextFactory()).getContext(fqcn, null, null, currentContext);
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
/*     */   protected static LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*     */     try {
/* 329 */       return factory.getContext(fqcn, loader, null, currentContext);
/* 330 */     } catch (IllegalStateException ex) {
/* 331 */       LOGGER.warn(ex.getMessage() + " Using SimpleLogger");
/* 332 */       return (new SimpleLoggerContextFactory()).getContext(fqcn, loader, null, currentContext);
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
/*     */   public static void shutdown() {
/* 346 */     shutdown(false);
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
/*     */   public static void shutdown(boolean currentContext) {
/* 365 */     shutdown(getContext(currentContext));
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
/*     */   public static void shutdown(LoggerContext context) {
/* 378 */     if (context != null && context instanceof Terminable) {
/* 379 */       ((Terminable)context).terminate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContextFactory getFactory() {
/* 389 */     return factory;
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
/*     */   public static void setFactory(LoggerContextFactory factory) {
/* 407 */     LogManager.factory = factory;
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
/*     */   public static Logger getFormatterLogger() {
/* 421 */     return getFormatterLogger(ReflectionUtil.getCallerClass(2));
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
/*     */   
/*     */   public static Logger getFormatterLogger(Class<?> clazz) {
/* 452 */     return getLogger((clazz != null) ? clazz : ReflectionUtil.getCallerClass(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */ 
/*     */   
/*     */   public static Logger getFormatterLogger(Object value) {
/* 484 */     return getLogger((value != null) ? value.getClass() : ReflectionUtil.getCallerClass(2), (MessageFactory)StringFormatterMessageFactory.INSTANCE);
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
/*     */   
/*     */   public static Logger getFormatterLogger(String name) {
/* 515 */     return (name == null) ? getFormatterLogger(ReflectionUtil.getCallerClass(2)) : getLogger(name, (MessageFactory)StringFormatterMessageFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<?> callerClass(Class<?> clazz) {
/* 520 */     if (clazz != null) {
/* 521 */       return clazz;
/*     */     }
/* 523 */     Class<?> candidate = ReflectionUtil.getCallerClass(3);
/* 524 */     if (candidate == null) {
/* 525 */       throw new UnsupportedOperationException("No class provided, and an appropriate one cannot be found.");
/*     */     }
/* 527 */     return candidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger() {
/* 537 */     return getLogger(ReflectionUtil.getCallerClass(2));
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
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 550 */     Class<?> cls = callerClass(clazz);
/* 551 */     return (Logger)getContext(cls.getClassLoader(), false).getLogger(cls.getName());
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
/*     */   public static Logger getLogger(Class<?> clazz, MessageFactory messageFactory) {
/* 566 */     Class<?> cls = callerClass(clazz);
/* 567 */     return (Logger)getContext(cls.getClassLoader(), false).getLogger(cls.getName(), messageFactory);
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
/*     */   public static Logger getLogger(MessageFactory messageFactory) {
/* 579 */     return getLogger(ReflectionUtil.getCallerClass(2), messageFactory);
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
/*     */   public static Logger getLogger(Object value) {
/* 592 */     return getLogger((value != null) ? value.getClass() : ReflectionUtil.getCallerClass(2));
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
/*     */   public static Logger getLogger(Object value, MessageFactory messageFactory) {
/* 607 */     return getLogger((value != null) ? value.getClass() : ReflectionUtil.getCallerClass(2), messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(String name) {
/* 618 */     return (name != null) ? (Logger)getContext(false).getLogger(name) : getLogger(ReflectionUtil.getCallerClass(2));
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
/*     */   public static Logger getLogger(String name, MessageFactory messageFactory) {
/* 631 */     return (name != null) ? (Logger)getContext(false).getLogger(name, messageFactory) : getLogger(ReflectionUtil.getCallerClass(2), messageFactory);
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
/*     */   protected static Logger getLogger(String fqcn, String name) {
/* 643 */     return (Logger)factory.getContext(fqcn, null, null, false).getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getRootLogger() {
/* 652 */     return getLogger("");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\LogManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */