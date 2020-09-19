/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public DefaultUncaughtExceptionHandler(Logger debug1) {
/*  9 */     this.logger = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread debug1, Throwable debug2) {
/* 14 */     this.logger.error("Caught previously unhandled exception :", debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\DefaultUncaughtExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */