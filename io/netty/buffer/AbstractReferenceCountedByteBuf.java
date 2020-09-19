/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReferenceCountedByteBuf
/*     */   extends AbstractByteBuf
/*     */ {
/*  31 */   private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
/*     */   
/*     */   private volatile int refCnt;
/*     */   
/*     */   protected AbstractReferenceCountedByteBuf(int maxCapacity) {
/*  36 */     super(maxCapacity);
/*  37 */     refCntUpdater.set(this, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  42 */     return this.refCnt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setRefCnt(int refCnt) {
/*  49 */     refCntUpdater.set(this, refCnt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retain() {
/*  54 */     return retain0(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf retain(int increment) {
/*  59 */     return retain0(ObjectUtil.checkPositive(increment, "increment"));
/*     */   }
/*     */   
/*     */   private ByteBuf retain0(int increment) {
/*  63 */     int oldRef = refCntUpdater.getAndAdd(this, increment);
/*  64 */     if (oldRef <= 0 || oldRef + increment < oldRef) {
/*     */       
/*  66 */       refCntUpdater.getAndAdd(this, -increment);
/*  67 */       throw new IllegalReferenceCountException(oldRef, increment);
/*     */     } 
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf touch() {
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf touch(Object hint) {
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/*  84 */     return release0(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/*  89 */     return release0(ObjectUtil.checkPositive(decrement, "decrement"));
/*     */   }
/*     */   
/*     */   private boolean release0(int decrement) {
/*  93 */     int oldRef = refCntUpdater.getAndAdd(this, -decrement);
/*  94 */     if (oldRef == decrement) {
/*  95 */       deallocate();
/*  96 */       return true;
/*  97 */     }  if (oldRef < decrement || oldRef - decrement > oldRef) {
/*     */       
/*  99 */       refCntUpdater.getAndAdd(this, decrement);
/* 100 */       throw new IllegalReferenceCountException(oldRef, -decrement);
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract void deallocate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\AbstractReferenceCountedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */