/*    */ package net.minecraft.server;
/*    */ 
/*    */ public final class RunningOnDifferentThreadException extends RuntimeException {
/*  4 */   public static final RunningOnDifferentThreadException RUNNING_ON_DIFFERENT_THREAD = new RunningOnDifferentThreadException();
/*    */   
/*    */   private RunningOnDifferentThreadException() {
/*  7 */     setStackTrace(new StackTraceElement[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Throwable fillInStackTrace() {
/* 12 */     setStackTrace(new StackTraceElement[0]);
/* 13 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\RunningOnDifferentThreadException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */