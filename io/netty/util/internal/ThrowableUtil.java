/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
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
/*    */ public final class ThrowableUtil
/*    */ {
/*    */   public static <T extends Throwable> T unknownStackTrace(T cause, Class<?> clazz, String method) {
/* 31 */     cause.setStackTrace(new StackTraceElement[] { new StackTraceElement(clazz.getName(), method, null, -1) });
/* 32 */     return cause;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String stackTraceToString(Throwable cause) {
/* 42 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 43 */     PrintStream pout = new PrintStream(out);
/* 44 */     cause.printStackTrace(pout);
/* 45 */     pout.flush();
/*    */     try {
/* 47 */       return new String(out.toByteArray());
/*    */     } finally {
/*    */       try {
/* 50 */         out.close();
/* 51 */       } catch (IOException iOException) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean haveSuppressed() {
/* 58 */     return (PlatformDependent.javaVersion() >= 7);
/*    */   }
/*    */   
/*    */   @SuppressJava6Requirement(reason = "Throwable addSuppressed is only available for >= 7. Has check for < 7.")
/*    */   public static void addSuppressed(Throwable target, Throwable suppressed) {
/* 63 */     if (!haveSuppressed()) {
/*    */       return;
/*    */     }
/* 66 */     target.addSuppressed(suppressed);
/*    */   }
/*    */   
/*    */   public static void addSuppressedAndClear(Throwable target, List<Throwable> suppressed) {
/* 70 */     addSuppressed(target, suppressed);
/* 71 */     suppressed.clear();
/*    */   }
/*    */   
/*    */   public static void addSuppressed(Throwable target, List<Throwable> suppressed) {
/* 75 */     for (Throwable t : suppressed)
/* 76 */       addSuppressed(target, t); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\ThrowableUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */