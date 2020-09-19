/*    */ package org.apache.logging.log4j.core;
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
/*    */ public interface LifeCycle
/*    */ {
/*    */   State getState();
/*    */   
/*    */   void initialize();
/*    */   
/*    */   void start();
/*    */   
/*    */   void stop();
/*    */   
/*    */   boolean isStarted();
/*    */   
/*    */   boolean isStopped();
/*    */   
/*    */   public enum State
/*    */   {
/* 39 */     INITIALIZING,
/*    */     
/* 41 */     INITIALIZED,
/*    */     
/* 43 */     STARTING,
/*    */     
/* 45 */     STARTED,
/*    */     
/* 47 */     STOPPING,
/*    */     
/* 49 */     STOPPED;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\LifeCycle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */