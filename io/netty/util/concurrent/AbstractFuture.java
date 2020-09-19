/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.CancellationException;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ public abstract class AbstractFuture<V>
/*    */   implements Future<V>
/*    */ {
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 32 */     await();
/*    */     
/* 34 */     Throwable cause = cause();
/* 35 */     if (cause == null) {
/* 36 */       return getNow();
/*    */     }
/* 38 */     if (cause instanceof CancellationException) {
/* 39 */       throw (CancellationException)cause;
/*    */     }
/* 41 */     throw new ExecutionException(cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 46 */     if (await(timeout, unit)) {
/* 47 */       Throwable cause = cause();
/* 48 */       if (cause == null) {
/* 49 */         return getNow();
/*    */       }
/* 51 */       if (cause instanceof CancellationException) {
/* 52 */         throw (CancellationException)cause;
/*    */       }
/* 54 */       throw new ExecutionException(cause);
/*    */     } 
/* 56 */     throw new TimeoutException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\AbstractFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */