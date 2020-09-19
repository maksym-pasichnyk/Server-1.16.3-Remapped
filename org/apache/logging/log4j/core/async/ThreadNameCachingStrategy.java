/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ThreadNameCachingStrategy
/*    */ {
/* 27 */   CACHED
/*    */   {
/*    */     public String getThreadName() {
/* 30 */       String result = ThreadNameCachingStrategy.THREADLOCAL_NAME.get();
/* 31 */       if (result == null) {
/* 32 */         result = Thread.currentThread().getName();
/* 33 */         ThreadNameCachingStrategy.THREADLOCAL_NAME.set(result);
/*    */       } 
/* 35 */       return result;
/*    */     }
/*    */   },
/* 38 */   UNCACHED
/*    */   {
/*    */     public String getThreadName() {
/* 41 */       return Thread.currentThread().getName();
/*    */     } };
/*    */   private static final StatusLogger LOGGER;
/*    */   static {
/* 45 */     LOGGER = StatusLogger.getLogger();
/* 46 */     THREADLOCAL_NAME = new ThreadLocal<>();
/*    */   }
/*    */   private static final ThreadLocal<String> THREADLOCAL_NAME;
/*    */   
/*    */   public static ThreadNameCachingStrategy create() {
/* 51 */     String name = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ThreadNameStrategy", CACHED.name());
/*    */     
/*    */     try {
/* 54 */       ThreadNameCachingStrategy result = valueOf(name);
/* 55 */       LOGGER.debug("AsyncLogger.ThreadNameStrategy={}", result);
/* 56 */       return result;
/* 57 */     } catch (Exception ex) {
/* 58 */       LOGGER.debug("Using AsyncLogger.ThreadNameStrategy.CACHED: '{}' not valid: {}", name, ex.toString());
/* 59 */       return CACHED;
/*    */     } 
/*    */   }
/*    */   
/*    */   abstract String getThreadName();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\ThreadNameCachingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */