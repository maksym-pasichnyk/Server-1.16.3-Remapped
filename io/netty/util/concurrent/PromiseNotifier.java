/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import io.netty.util.internal.PromiseNotificationUtil;
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*    */ public class PromiseNotifier<V, F extends Future<V>>
/*    */   implements GenericFutureListener<F>
/*    */ {
/* 33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PromiseNotifier.class);
/*    */ 
/*    */   
/*    */   private final Promise<? super V>[] promises;
/*    */ 
/*    */   
/*    */   private final boolean logNotifyFailure;
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   public PromiseNotifier(Promise<? super V>... promises) {
/* 44 */     this(true, promises);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   public PromiseNotifier(boolean logNotifyFailure, Promise<? super V>... promises) {
/* 55 */     ObjectUtil.checkNotNull(promises, "promises");
/* 56 */     for (Promise<? super V> promise : promises) {
/* 57 */       if (promise == null) {
/* 58 */         throw new IllegalArgumentException("promises contains null Promise");
/*    */       }
/*    */     } 
/* 61 */     this.promises = (Promise<? super V>[])promises.clone();
/* 62 */     this.logNotifyFailure = logNotifyFailure;
/*    */   }
/*    */ 
/*    */   
/*    */   public void operationComplete(F future) throws Exception {
/* 67 */     InternalLogger internalLogger = this.logNotifyFailure ? logger : null;
/* 68 */     if (future.isSuccess()) {
/* 69 */       V result = future.get();
/* 70 */       for (Promise<? super V> p : this.promises) {
/* 71 */         PromiseNotificationUtil.trySuccess(p, result, internalLogger);
/*    */       }
/* 73 */     } else if (future.isCancelled()) {
/* 74 */       for (Promise<? super V> p : this.promises) {
/* 75 */         PromiseNotificationUtil.tryCancel(p, internalLogger);
/*    */       }
/*    */     } else {
/* 78 */       Throwable cause = future.cause();
/* 79 */       for (Promise<? super V> p : this.promises)
/* 80 */         PromiseNotificationUtil.tryFailure(p, cause, internalLogger); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\PromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */