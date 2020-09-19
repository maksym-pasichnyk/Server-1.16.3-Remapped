/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadContextMapFactory
/*     */ {
/*  49 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
/*     */   
/*     */   private static final String GC_FREE_THREAD_CONTEXT_KEY = "log4j2.garbagefree.threadContextMap";
/*     */ 
/*     */   
/*     */   public static ThreadContextMap createThreadContextMap() {
/*  57 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/*  58 */     String threadContextMapName = managerProps.getStringProperty("log4j2.threadContextMap");
/*  59 */     ClassLoader cl = ProviderUtil.findClassLoader();
/*  60 */     ThreadContextMap result = null;
/*  61 */     if (threadContextMapName != null) {
/*     */       try {
/*  63 */         Class<?> clazz = cl.loadClass(threadContextMapName);
/*  64 */         if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/*  65 */           result = (ThreadContextMap)clazz.newInstance();
/*     */         }
/*  67 */       } catch (ClassNotFoundException cnfe) {
/*  68 */         LOGGER.error("Unable to locate configured ThreadContextMap {}", threadContextMapName);
/*  69 */       } catch (Exception ex) {
/*  70 */         LOGGER.error("Unable to create configured ThreadContextMap {}", threadContextMapName, ex);
/*     */       } 
/*     */     }
/*  73 */     if (result == null && ProviderUtil.hasProviders() && LogManager.getFactory() != null) {
/*  74 */       String factoryClassName = LogManager.getFactory().getClass().getName();
/*  75 */       for (Provider provider : ProviderUtil.getProviders()) {
/*  76 */         if (factoryClassName.equals(provider.getClassName())) {
/*  77 */           Class<? extends ThreadContextMap> clazz = provider.loadThreadContextMap();
/*  78 */           if (clazz != null) {
/*     */             try {
/*  80 */               result = clazz.newInstance();
/*     */               break;
/*  82 */             } catch (Exception e) {
/*  83 */               LOGGER.error("Unable to locate or load configured ThreadContextMap {}", provider.getThreadContextMap(), e);
/*     */               
/*  85 */               result = createDefaultThreadContextMap();
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*  91 */     if (result == null) {
/*  92 */       result = createDefaultThreadContextMap();
/*     */     }
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   private static ThreadContextMap createDefaultThreadContextMap() {
/*  98 */     if (Constants.ENABLE_THREADLOCALS) {
/*  99 */       if (PropertiesUtil.getProperties().getBooleanProperty("log4j2.garbagefree.threadContextMap")) {
/* 100 */         return new GarbageFreeSortedArrayThreadContextMap();
/*     */       }
/* 102 */       return new CopyOnWriteSortedArrayThreadContextMap();
/*     */     } 
/* 104 */     return new DefaultThreadContextMap(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\ThreadContextMapFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */