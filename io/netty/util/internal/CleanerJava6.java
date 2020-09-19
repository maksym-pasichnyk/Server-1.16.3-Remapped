/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.lang.reflect.Field;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CleanerJava6
/*    */   implements Cleaner
/*    */ {
/*    */   private static final long CLEANER_FIELD_OFFSET;
/*    */   private static final Method CLEAN_METHOD;
/* 36 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CleanerJava6.class);
/*    */   
/*    */   static {
/* 39 */     long fieldOffset = -1L;
/* 40 */     Method clean = null;
/* 41 */     Throwable error = null;
/* 42 */     if (PlatformDependent0.hasUnsafe()) {
/* 43 */       ByteBuffer direct = ByteBuffer.allocateDirect(1);
/*    */       try {
/* 45 */         Field cleanerField = direct.getClass().getDeclaredField("cleaner");
/* 46 */         fieldOffset = PlatformDependent0.objectFieldOffset(cleanerField);
/* 47 */         Object cleaner = PlatformDependent0.getObject(direct, fieldOffset);
/* 48 */         clean = cleaner.getClass().getDeclaredMethod("clean", new Class[0]);
/* 49 */         clean.invoke(cleaner, new Object[0]);
/* 50 */       } catch (Throwable t) {
/*    */         
/* 52 */         fieldOffset = -1L;
/* 53 */         clean = null;
/* 54 */         error = t;
/*    */       } 
/*    */     } else {
/* 57 */       error = new UnsupportedOperationException("sun.misc.Unsafe unavailable");
/*    */     } 
/* 59 */     if (error == null) {
/* 60 */       logger.debug("java.nio.ByteBuffer.cleaner(): available");
/*    */     } else {
/* 62 */       logger.debug("java.nio.ByteBuffer.cleaner(): unavailable", error);
/*    */     } 
/* 64 */     CLEANER_FIELD_OFFSET = fieldOffset;
/* 65 */     CLEAN_METHOD = clean;
/*    */   }
/*    */   
/*    */   static boolean isSupported() {
/* 69 */     return (CLEANER_FIELD_OFFSET != -1L);
/*    */   }
/*    */ 
/*    */   
/*    */   public void freeDirectBuffer(ByteBuffer buffer) {
/* 74 */     if (!buffer.isDirect()) {
/*    */       return;
/*    */     }
/*    */     try {
/* 78 */       Object cleaner = PlatformDependent0.getObject(buffer, CLEANER_FIELD_OFFSET);
/* 79 */       if (cleaner != null) {
/* 80 */         CLEAN_METHOD.invoke(cleaner, new Object[0]);
/*    */       }
/* 82 */     } catch (Throwable cause) {
/* 83 */       PlatformDependent0.throwException(cause);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\CleanerJava6.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */