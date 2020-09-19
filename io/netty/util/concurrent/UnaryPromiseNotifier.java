/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public final class UnaryPromiseNotifier<T>
/*    */   implements FutureListener<T>
/*    */ {
/* 23 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(UnaryPromiseNotifier.class);
/*    */   private final Promise<? super T> promise;
/*    */   
/*    */   public UnaryPromiseNotifier(Promise<? super T> promise) {
/* 27 */     this.promise = (Promise<? super T>)ObjectUtil.checkNotNull(promise, "promise");
/*    */   }
/*    */ 
/*    */   
/*    */   public void operationComplete(Future<T> future) throws Exception {
/* 32 */     cascadeTo(future, this.promise);
/*    */   }
/*    */   
/*    */   public static <X> void cascadeTo(Future<X> completedFuture, Promise<? super X> promise) {
/* 36 */     if (completedFuture.isSuccess()) {
/* 37 */       if (!promise.trySuccess(completedFuture.getNow())) {
/* 38 */         logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
/*    */       }
/* 40 */     } else if (completedFuture.isCancelled()) {
/* 41 */       if (!promise.cancel(false)) {
/* 42 */         logger.warn("Failed to cancel a promise because it is done already: {}", promise);
/*    */       }
/*    */     }
/* 45 */     else if (!promise.tryFailure(completedFuture.cause())) {
/* 46 */       logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, completedFuture
/* 47 */           .cause());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\UnaryPromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */