/*    */ package org.apache.logging.log4j.core.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClockFactory
/*    */ {
/*    */   public static final String PROPERTY_NAME = "log4j.Clock";
/* 32 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Clock getClock() {
/* 62 */     return createClock();
/*    */   }
/*    */   
/*    */   private static Clock createClock() {
/* 66 */     String userRequest = PropertiesUtil.getProperties().getStringProperty("log4j.Clock");
/* 67 */     if (userRequest == null || "SystemClock".equals(userRequest)) {
/* 68 */       LOGGER.trace("Using default SystemClock for timestamps.");
/* 69 */       return new SystemClock();
/*    */     } 
/* 71 */     if (CachedClock.class.getName().equals(userRequest) || "CachedClock".equals(userRequest)) {
/*    */       
/* 73 */       LOGGER.trace("Using specified CachedClock for timestamps.");
/* 74 */       return CachedClock.instance();
/*    */     } 
/* 76 */     if (CoarseCachedClock.class.getName().equals(userRequest) || "CoarseCachedClock".equals(userRequest)) {
/*    */       
/* 78 */       LOGGER.trace("Using specified CoarseCachedClock for timestamps.");
/* 79 */       return CoarseCachedClock.instance();
/*    */     } 
/*    */     try {
/* 82 */       Clock result = Loader.<Clock>newCheckedInstanceOf(userRequest, Clock.class);
/* 83 */       LOGGER.trace("Using {} for timestamps.", result.getClass().getName());
/* 84 */       return result;
/* 85 */     } catch (Exception e) {
/* 86 */       String fmt = "Could not create {}: {}, using default SystemClock for timestamps.";
/* 87 */       LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps.", userRequest, e);
/* 88 */       return new SystemClock();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\ClockFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */