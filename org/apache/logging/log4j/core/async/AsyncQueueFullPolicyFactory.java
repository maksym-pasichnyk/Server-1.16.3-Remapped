/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*    */ public class AsyncQueueFullPolicyFactory
/*    */ {
/*    */   static final String PROPERTY_NAME_ASYNC_EVENT_ROUTER = "log4j2.AsyncQueueFullPolicy";
/*    */   static final String PROPERTY_VALUE_DEFAULT_ASYNC_EVENT_ROUTER = "Default";
/*    */   static final String PROPERTY_VALUE_DISCARDING_ASYNC_EVENT_ROUTER = "Discard";
/*    */   static final String PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL = "log4j2.DiscardThreshold";
/* 51 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   public static AsyncQueueFullPolicy create() {
/* 68 */     String router = PropertiesUtil.getProperties().getStringProperty("log4j2.AsyncQueueFullPolicy");
/* 69 */     if (router == null || "Default".equals(router) || DefaultAsyncQueueFullPolicy.class.getSimpleName().equals(router) || DefaultAsyncQueueFullPolicy.class.getName().equals(router))
/*    */     {
/*    */       
/* 72 */       return new DefaultAsyncQueueFullPolicy();
/*    */     }
/* 74 */     if ("Discard".equals(router) || DiscardingAsyncQueueFullPolicy.class.getSimpleName().equals(router) || DiscardingAsyncQueueFullPolicy.class.getName().equals(router))
/*    */     {
/*    */       
/* 77 */       return createDiscardingAsyncQueueFullPolicy();
/*    */     }
/* 79 */     return createCustomRouter(router);
/*    */   }
/*    */   
/*    */   private static AsyncQueueFullPolicy createCustomRouter(String router) {
/*    */     try {
/* 84 */       Class<? extends AsyncQueueFullPolicy> cls = LoaderUtil.loadClass(router).asSubclass(AsyncQueueFullPolicy.class);
/* 85 */       LOGGER.debug("Creating custom AsyncQueueFullPolicy '{}'", router);
/* 86 */       return cls.newInstance();
/* 87 */     } catch (Exception ex) {
/* 88 */       LOGGER.debug("Using DefaultAsyncQueueFullPolicy. Could not create custom AsyncQueueFullPolicy '{}': {}", router, ex.toString());
/*    */       
/* 90 */       return new DefaultAsyncQueueFullPolicy();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static AsyncQueueFullPolicy createDiscardingAsyncQueueFullPolicy() {
/* 95 */     PropertiesUtil util = PropertiesUtil.getProperties();
/* 96 */     String level = util.getStringProperty("log4j2.DiscardThreshold", Level.INFO.name());
/* 97 */     Level thresholdLevel = Level.toLevel(level, Level.INFO);
/* 98 */     LOGGER.debug("Creating custom DiscardingAsyncQueueFullPolicy(discardThreshold:{})", thresholdLevel);
/* 99 */     return new DiscardingAsyncQueueFullPolicy(thresholdLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncQueueFullPolicyFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */