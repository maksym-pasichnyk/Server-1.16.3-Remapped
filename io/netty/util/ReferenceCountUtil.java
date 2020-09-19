/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public final class ReferenceCountUtil
/*     */ {
/*  27 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
/*     */   
/*     */   static {
/*  30 */     ResourceLeakDetector.addExclusions(ReferenceCountUtil.class, new String[] { "touch" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T retain(T msg) {
/*  39 */     if (msg instanceof ReferenceCounted) {
/*  40 */       return (T)((ReferenceCounted)msg).retain();
/*     */     }
/*  42 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T retain(T msg, int increment) {
/*  51 */     if (msg instanceof ReferenceCounted) {
/*  52 */       return (T)((ReferenceCounted)msg).retain(increment);
/*     */     }
/*  54 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T touch(T msg) {
/*  63 */     if (msg instanceof ReferenceCounted) {
/*  64 */       return (T)((ReferenceCounted)msg).touch();
/*     */     }
/*  66 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T touch(T msg, Object hint) {
/*  76 */     if (msg instanceof ReferenceCounted) {
/*  77 */       return (T)((ReferenceCounted)msg).touch(hint);
/*     */     }
/*  79 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean release(Object msg) {
/*  87 */     if (msg instanceof ReferenceCounted) {
/*  88 */       return ((ReferenceCounted)msg).release();
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean release(Object msg, int decrement) {
/*  98 */     if (msg instanceof ReferenceCounted) {
/*  99 */       return ((ReferenceCounted)msg).release(decrement);
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void safeRelease(Object msg) {
/*     */     try {
/* 113 */       release(msg);
/* 114 */     } catch (Throwable t) {
/* 115 */       logger.warn("Failed to release a message: {}", msg, t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void safeRelease(Object msg, int decrement) {
/*     */     try {
/* 128 */       release(msg, decrement);
/* 129 */     } catch (Throwable t) {
/* 130 */       if (logger.isWarnEnabled()) {
/* 131 */         logger.warn("Failed to release a message: {} (decrement: {})", new Object[] { msg, Integer.valueOf(decrement), t });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <T> T releaseLater(T msg) {
/* 145 */     return releaseLater(msg, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <T> T releaseLater(T msg, int decrement) {
/* 157 */     if (msg instanceof ReferenceCounted) {
/* 158 */       ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)msg, decrement));
/*     */     }
/* 160 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int refCnt(Object msg) {
/* 168 */     return (msg instanceof ReferenceCounted) ? ((ReferenceCounted)msg).refCnt() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ReleasingTask
/*     */     implements Runnable
/*     */   {
/*     */     private final ReferenceCounted obj;
/*     */     
/*     */     private final int decrement;
/*     */     
/*     */     ReleasingTask(ReferenceCounted obj, int decrement) {
/* 180 */       this.obj = obj;
/* 181 */       this.decrement = decrement;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 187 */         if (!this.obj.release(this.decrement)) {
/* 188 */           ReferenceCountUtil.logger.warn("Non-zero refCnt: {}", this);
/*     */         } else {
/* 190 */           ReferenceCountUtil.logger.debug("Released: {}", this);
/*     */         } 
/* 192 */       } catch (Exception ex) {
/* 193 */         ReferenceCountUtil.logger.warn("Failed to release an object: {}", this.obj, ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 199 */       return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ReferenceCountUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */