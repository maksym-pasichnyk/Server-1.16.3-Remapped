/*    */ package io.netty.util;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*    */ public abstract class AbstractReferenceCounted
/*    */   implements ReferenceCounted
/*    */ {
/* 28 */   private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt");
/*    */   
/* 30 */   private volatile int refCnt = 1;
/*    */ 
/*    */   
/*    */   public final int refCnt() {
/* 34 */     return this.refCnt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void setRefCnt(int refCnt) {
/* 41 */     refCntUpdater.set(this, refCnt);
/*    */   }
/*    */ 
/*    */   
/*    */   public ReferenceCounted retain() {
/* 46 */     return retain0(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ReferenceCounted retain(int increment) {
/* 51 */     return retain0(ObjectUtil.checkPositive(increment, "increment"));
/*    */   }
/*    */   
/*    */   private ReferenceCounted retain0(int increment) {
/* 55 */     int oldRef = refCntUpdater.getAndAdd(this, increment);
/* 56 */     if (oldRef <= 0 || oldRef + increment < oldRef) {
/*    */       
/* 58 */       refCntUpdater.getAndAdd(this, -increment);
/* 59 */       throw new IllegalReferenceCountException(oldRef, increment);
/*    */     } 
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReferenceCounted touch() {
/* 66 */     return touch(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean release() {
/* 71 */     return release0(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean release(int decrement) {
/* 76 */     return release0(ObjectUtil.checkPositive(decrement, "decrement"));
/*    */   }
/*    */   
/*    */   private boolean release0(int decrement) {
/* 80 */     int oldRef = refCntUpdater.getAndAdd(this, -decrement);
/* 81 */     if (oldRef == decrement) {
/* 82 */       deallocate();
/* 83 */       return true;
/* 84 */     }  if (oldRef < decrement || oldRef - decrement > oldRef) {
/*    */       
/* 86 */       refCntUpdater.getAndAdd(this, decrement);
/* 87 */       throw new IllegalReferenceCountException(oldRef, -decrement);
/*    */     } 
/* 89 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract void deallocate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\AbstractReferenceCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */