/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.LineNumberReader;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringReader;
/*    */ import java.io.StringWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public final class Throwables
/*    */ {
/*    */   public static Throwable getRootCause(Throwable throwable) {
/* 44 */     Throwable root = throwable; Throwable cause;
/* 45 */     while ((cause = root.getCause()) != null) {
/* 46 */       root = cause;
/*    */     }
/* 48 */     return root;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<String> toStringList(Throwable throwable) {
/* 58 */     StringWriter sw = new StringWriter();
/* 59 */     PrintWriter pw = new PrintWriter(sw);
/*    */     try {
/* 61 */       throwable.printStackTrace(pw);
/* 62 */     } catch (RuntimeException runtimeException) {}
/*    */ 
/*    */     
/* 65 */     pw.flush();
/* 66 */     List<String> lines = new ArrayList<>();
/* 67 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/*    */     try {
/* 69 */       String line = reader.readLine();
/* 70 */       while (line != null) {
/* 71 */         lines.add(line);
/* 72 */         line = reader.readLine();
/*    */       } 
/* 74 */     } catch (IOException ex) {
/* 75 */       if (ex instanceof java.io.InterruptedIOException) {
/* 76 */         Thread.currentThread().interrupt();
/*    */       }
/* 78 */       lines.add(ex.toString());
/*    */     } finally {
/* 80 */       Closer.closeSilently(reader);
/*    */     } 
/* 82 */     return lines;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void rethrow(Throwable t) {
/* 92 */     rethrow0(t);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T extends Throwable> void rethrow0(Throwable t) throws T {
/* 97 */     throw (T)t;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\Throwables.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */