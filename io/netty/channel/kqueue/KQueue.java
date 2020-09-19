/*    */ package io.netty.channel.kqueue;
/*    */ 
/*    */ import io.netty.channel.unix.FileDescriptor;
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import io.netty.util.internal.SystemPropertyUtil;
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
/*    */ public final class KQueue
/*    */ {
/*    */   private static final Throwable UNAVAILABILITY_CAUSE;
/*    */   
/*    */   static {
/* 30 */     Throwable cause = null;
/* 31 */     if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
/* 32 */       cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
/*    */     } else {
/*    */       
/* 35 */       FileDescriptor kqueueFd = null;
/*    */       try {
/* 37 */         kqueueFd = Native.newKQueue();
/* 38 */       } catch (Throwable t) {
/* 39 */         cause = t;
/*    */       } finally {
/* 41 */         if (kqueueFd != null) {
/*    */           try {
/* 43 */             kqueueFd.close();
/* 44 */           } catch (Exception exception) {}
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 51 */     if (cause != null) {
/* 52 */       UNAVAILABILITY_CAUSE = cause;
/*    */     
/*    */     }
/*    */     else {
/*    */ 
/*    */       
/* 58 */       UNAVAILABILITY_CAUSE = PlatformDependent.hasUnsafe() ? null : new IllegalStateException("sun.misc.Unsafe not available", PlatformDependent.getUnsafeUnavailabilityCause());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAvailable() {
/* 67 */     return (UNAVAILABILITY_CAUSE == null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void ensureAvailability() {
/* 77 */     if (UNAVAILABILITY_CAUSE != null) {
/* 78 */       throw (Error)(new UnsatisfiedLinkError("failed to load the required native library"))
/* 79 */         .initCause(UNAVAILABILITY_CAUSE);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Throwable unavailabilityCause() {
/* 90 */     return UNAVAILABILITY_CAUSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */