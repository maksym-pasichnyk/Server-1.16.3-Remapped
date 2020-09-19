/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.nio.ByteBuffer;
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
/*    */ final class CleanerJava9
/*    */   implements Cleaner
/*    */ {
/* 29 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CleanerJava9.class);
/*    */ 
/*    */   
/*    */   private static final Method INVOKE_CLEANER;
/*    */ 
/*    */   
/*    */   static {
/* 36 */     if (PlatformDependent0.hasUnsafe()) {
/* 37 */       Object maybeInvokeMethod; ByteBuffer buffer = ByteBuffer.allocateDirect(1);
/*    */ 
/*    */       
/*    */       try {
/* 41 */         Method m = PlatformDependent0.UNSAFE.getClass().getDeclaredMethod("invokeCleaner", new Class[] { ByteBuffer.class });
/* 42 */         m.invoke(PlatformDependent0.UNSAFE, new Object[] { buffer });
/* 43 */         maybeInvokeMethod = m;
/* 44 */       } catch (NoSuchMethodException e) {
/* 45 */         maybeInvokeMethod = e;
/* 46 */       } catch (InvocationTargetException e) {
/* 47 */         maybeInvokeMethod = e;
/* 48 */       } catch (IllegalAccessException e) {
/* 49 */         maybeInvokeMethod = e;
/*    */       } 
/* 51 */       if (maybeInvokeMethod instanceof Throwable) {
/* 52 */         method = null;
/* 53 */         error = (Throwable)maybeInvokeMethod;
/*    */       } else {
/* 55 */         method = (Method)maybeInvokeMethod;
/* 56 */         error = null;
/*    */       } 
/*    */     } else {
/* 59 */       method = null;
/* 60 */       error = new UnsupportedOperationException("sun.misc.Unsafe unavailable");
/*    */     } 
/* 62 */     if (error == null) {
/* 63 */       logger.debug("java.nio.ByteBuffer.cleaner(): available");
/*    */     } else {
/* 65 */       logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
/*    */     } 
/* 67 */     INVOKE_CLEANER = method; } static {
/*    */     Method method;
/*    */     Throwable error;
/*    */   } static boolean isSupported() {
/* 71 */     return (INVOKE_CLEANER != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void freeDirectBuffer(ByteBuffer buffer) {
/*    */     try {
/* 77 */       INVOKE_CLEANER.invoke(PlatformDependent0.UNSAFE, new Object[] { buffer });
/* 78 */     } catch (Throwable cause) {
/* 79 */       PlatformDependent0.throwException(cause);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\CleanerJava9.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */