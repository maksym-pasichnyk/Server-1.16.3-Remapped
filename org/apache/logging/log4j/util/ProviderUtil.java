/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.Provider;
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
/*     */ public final class ProviderUtil
/*     */ {
/*     */   protected static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
/*  47 */   protected static final Collection<Provider> PROVIDERS = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   protected static final Lock STARTUP_LOCK = new ReentrantLock();
/*     */   
/*     */   private static final String API_VERSION = "Log4jAPIVersion";
/*  57 */   private static final String[] COMPATIBLE_API_VERSIONS = new String[] { "2.0.0", "2.1.0", "2.2.0", "2.3.0", "2.4.0", "2.5.0", "2.6.0" };
/*  58 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static volatile ProviderUtil instance;
/*     */ 
/*     */   
/*     */   private ProviderUtil() {
/*  65 */     for (LoaderUtil.UrlResource resource : LoaderUtil.findUrlResources("META-INF/log4j-provider.properties")) {
/*  66 */       loadProvider(resource.getUrl(), resource.getClassLoader());
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
/*     */   protected static void loadProvider(URL url, ClassLoader cl) {
/*     */     try {
/*  79 */       Properties props = PropertiesUtil.loadClose(url.openStream(), url);
/*  80 */       if (validVersion(props.getProperty("Log4jAPIVersion"))) {
/*  81 */         Provider provider = new Provider(props, url, cl);
/*  82 */         PROVIDERS.add(provider);
/*  83 */         LOGGER.debug("Loaded Provider {}", provider);
/*     */       } 
/*  85 */     } catch (IOException e) {
/*  86 */       LOGGER.error("Unable to open {}", url, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected static void loadProviders(Enumeration<URL> urls, ClassLoader cl) {
/*  95 */     if (urls != null) {
/*  96 */       while (urls.hasMoreElements()) {
/*  97 */         loadProvider(urls.nextElement(), cl);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Iterable<Provider> getProviders() {
/* 103 */     lazyInit();
/* 104 */     return PROVIDERS;
/*     */   }
/*     */   
/*     */   public static boolean hasProviders() {
/* 108 */     lazyInit();
/* 109 */     return !PROVIDERS.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void lazyInit() {
/* 119 */     if (instance == null) {
/*     */       try {
/* 121 */         STARTUP_LOCK.lockInterruptibly();
/*     */         try {
/* 123 */           if (instance == null) {
/* 124 */             instance = new ProviderUtil();
/*     */           }
/*     */         } finally {
/* 127 */           STARTUP_LOCK.unlock();
/*     */         } 
/* 129 */       } catch (InterruptedException e) {
/* 130 */         LOGGER.fatal("Interrupted before Log4j Providers could be loaded.", e);
/* 131 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static ClassLoader findClassLoader() {
/* 137 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */   
/*     */   private static boolean validVersion(String version) {
/* 141 */     for (String v : COMPATIBLE_API_VERSIONS) {
/* 142 */       if (version.startsWith(v)) {
/* 143 */         return true;
/*     */       }
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\ProviderUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */