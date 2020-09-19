/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URL;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Provider
/*     */ {
/*     */   public static final String FACTORY_PRIORITY = "FactoryPriority";
/*     */   public static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
/*     */   public static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
/*  45 */   private static final Integer DEFAULT_PRIORITY = Integer.valueOf(-1);
/*  46 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Integer priority;
/*     */   private final String className;
/*     */   private final String threadContextMap;
/*     */   private final URL url;
/*     */   private final WeakReference<ClassLoader> classLoader;
/*     */   
/*     */   public Provider(Properties props, URL url, ClassLoader classLoader) {
/*  55 */     this.url = url;
/*  56 */     this.classLoader = new WeakReference<>(classLoader);
/*  57 */     String weight = props.getProperty("FactoryPriority");
/*  58 */     this.priority = (weight == null) ? DEFAULT_PRIORITY : Integer.valueOf(weight);
/*  59 */     this.className = props.getProperty("LoggerContextFactory");
/*  60 */     this.threadContextMap = props.getProperty("ThreadContextMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPriority() {
/*  69 */     return this.priority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  79 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
/*  88 */     if (this.className == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     ClassLoader loader = this.classLoader.get();
/*  92 */     if (loader == null) {
/*  93 */       return null;
/*     */     }
/*     */     try {
/*  96 */       Class<?> clazz = loader.loadClass(this.className);
/*  97 */       if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
/*  98 */         return clazz.asSubclass(LoggerContextFactory.class);
/*     */       }
/* 100 */     } catch (Exception e) {
/* 101 */       LOGGER.error("Unable to create class {} specified in {}", this.className, this.url.toString(), e);
/*     */     } 
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadContextMap() {
/* 112 */     return this.threadContextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends ThreadContextMap> loadThreadContextMap() {
/* 121 */     if (this.threadContextMap == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     ClassLoader loader = this.classLoader.get();
/* 125 */     if (loader == null) {
/* 126 */       return null;
/*     */     }
/*     */     try {
/* 129 */       Class<?> clazz = loader.loadClass(this.threadContextMap);
/* 130 */       if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/* 131 */         return clazz.asSubclass(ThreadContextMap.class);
/*     */       }
/* 133 */     } catch (Exception e) {
/* 134 */       LOGGER.error("Unable to create class {} specified in {}", this.threadContextMap, this.url.toString(), e);
/*     */     } 
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/* 145 */     return this.url;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     String result = "Provider[";
/* 151 */     if (!DEFAULT_PRIORITY.equals(this.priority)) {
/* 152 */       result = result + "priority=" + this.priority + ", ";
/*     */     }
/* 154 */     if (this.threadContextMap != null) {
/* 155 */       result = result + "threadContextMap=" + this.threadContextMap + ", ";
/*     */     }
/* 157 */     if (this.className != null) {
/* 158 */       result = result + "className=" + this.className + ", ";
/*     */     }
/* 160 */     result = result + "url=" + this.url;
/* 161 */     ClassLoader loader = this.classLoader.get();
/* 162 */     if (loader == null) {
/* 163 */       result = result + ", classLoader=null(not reachable)";
/*     */     } else {
/* 165 */       result = result + ", classLoader=" + loader;
/*     */     } 
/* 167 */     result = result + "]";
/* 168 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\Provider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */