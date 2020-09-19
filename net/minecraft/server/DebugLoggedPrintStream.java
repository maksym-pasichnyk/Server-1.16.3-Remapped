/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class DebugLoggedPrintStream extends LoggedPrintStream {
/*    */   public DebugLoggedPrintStream(String debug1, OutputStream debug2) {
/*  7 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void logLine(String debug1) {
/* 12 */     StackTraceElement[] debug2 = Thread.currentThread().getStackTrace();
/* 13 */     StackTraceElement debug3 = debug2[Math.min(3, debug2.length)];
/* 14 */     LOGGER.info("[{}]@.({}:{}): {}", this.name, debug3.getFileName(), Integer.valueOf(debug3.getLineNumber()), debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\DebugLoggedPrintStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */