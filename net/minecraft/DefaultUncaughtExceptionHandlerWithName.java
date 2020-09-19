/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandlerWithName implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public DefaultUncaughtExceptionHandlerWithName(Logger debug1) {
/*  9 */     this.logger = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread debug1, Throwable debug2) {
/* 14 */     this.logger.error("Caught previously unhandled exception :");
/* 15 */     this.logger.error(debug1.getName(), debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\DefaultUncaughtExceptionHandlerWithName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */