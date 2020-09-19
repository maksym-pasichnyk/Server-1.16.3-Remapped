/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.Recycler;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ import io.netty.util.concurrent.Promise;
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
/*    */ public final class PendingWrite
/*    */ {
/* 26 */   private static final Recycler<PendingWrite> RECYCLER = new Recycler<PendingWrite>()
/*    */     {
/*    */       protected PendingWrite newObject(Recycler.Handle<PendingWrite> handle) {
/* 29 */         return new PendingWrite(handle);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   private final Recycler.Handle<PendingWrite> handle;
/*    */   
/*    */   public static PendingWrite newInstance(Object msg, Promise<Void> promise) {
/* 37 */     PendingWrite pending = (PendingWrite)RECYCLER.get();
/* 38 */     pending.msg = msg;
/* 39 */     pending.promise = promise;
/* 40 */     return pending;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object msg;
/*    */   private Promise<Void> promise;
/*    */   
/*    */   private PendingWrite(Recycler.Handle<PendingWrite> handle) {
/* 48 */     this.handle = handle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean recycle() {
/* 55 */     this.msg = null;
/* 56 */     this.promise = null;
/* 57 */     this.handle.recycle(this);
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean failAndRecycle(Throwable cause) {
/* 65 */     ReferenceCountUtil.release(this.msg);
/* 66 */     if (this.promise != null) {
/* 67 */       this.promise.setFailure(cause);
/*    */     }
/* 69 */     return recycle();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean successAndRecycle() {
/* 76 */     if (this.promise != null) {
/* 77 */       this.promise.setSuccess(null);
/*    */     }
/* 79 */     return recycle();
/*    */   }
/*    */   
/*    */   public Object msg() {
/* 83 */     return this.msg;
/*    */   }
/*    */   
/*    */   public Promise<Void> promise() {
/* 87 */     return this.promise;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Promise<Void> recycleAndGet() {
/* 94 */     Promise<Void> promise = this.promise;
/* 95 */     recycle();
/* 96 */     return promise;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\PendingWrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */