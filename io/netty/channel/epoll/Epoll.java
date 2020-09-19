/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Epoll
/*     */ {
/*     */   private static final Throwable UNAVAILABILITY_CAUSE;
/*     */   
/*     */   static {
/*  30 */     Throwable cause = null;
/*     */     
/*  32 */     if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
/*  33 */       cause = new UnsupportedOperationException("Native transport was explicit disabled with -Dio.netty.transport.noNative=true");
/*     */     } else {
/*     */       
/*  36 */       FileDescriptor epollFd = null;
/*  37 */       FileDescriptor eventFd = null;
/*     */       try {
/*  39 */         epollFd = Native.newEpollCreate();
/*  40 */         eventFd = Native.newEventFd();
/*  41 */       } catch (Throwable t) {
/*  42 */         cause = t;
/*     */       } finally {
/*  44 */         if (epollFd != null) {
/*     */           try {
/*  46 */             epollFd.close();
/*  47 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/*  51 */         if (eventFd != null) {
/*     */           try {
/*  53 */             eventFd.close();
/*  54 */           } catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  61 */     if (cause != null) {
/*  62 */       UNAVAILABILITY_CAUSE = cause;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  68 */       UNAVAILABILITY_CAUSE = PlatformDependent.hasUnsafe() ? null : new IllegalStateException("sun.misc.Unsafe not available", PlatformDependent.getUnsafeUnavailabilityCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailable() {
/*  77 */     return (UNAVAILABILITY_CAUSE == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void ensureAvailability() {
/*  87 */     if (UNAVAILABILITY_CAUSE != null) {
/*  88 */       throw (Error)(new UnsatisfiedLinkError("failed to load the required native library"))
/*  89 */         .initCause(UNAVAILABILITY_CAUSE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Throwable unavailabilityCause() {
/* 100 */     return UNAVAILABILITY_CAUSE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\Epoll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */