/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.Writer;
/*    */ import java.util.Objects;
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
/*    */ final class LowLevelLogUtil
/*    */ {
/* 34 */   private static PrintWriter writer = new PrintWriter(System.err, true);
/*    */   
/*    */   public static void logException(Throwable exception) {
/* 37 */     exception.printStackTrace(writer);
/*    */   }
/*    */   
/*    */   public static void logException(String message, Throwable exception) {
/* 41 */     if (message != null) {
/* 42 */       writer.println(message);
/*    */     }
/* 44 */     logException(exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setOutputStream(OutputStream out) {
/* 53 */     writer = new PrintWriter(Objects.<OutputStream>requireNonNull(out), true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setWriter(Writer writer) {
/* 62 */     LowLevelLogUtil.writer = new PrintWriter(Objects.<Writer>requireNonNull(writer), true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\LowLevelLogUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */