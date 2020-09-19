/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ConcurrentCircularArrayQueue<E>
/*     */   extends ConcurrentCircularArrayQueueL0Pad<E>
/*     */ {
/*     */   protected final long mask;
/*     */   protected final E[] buffer;
/*     */   
/*     */   public ConcurrentCircularArrayQueue(int capacity) {
/*  43 */     int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
/*  44 */     this.mask = (actualCapacity - 1);
/*  45 */     this.buffer = CircularArrayOffsetCalculator.allocate(actualCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static long calcElementOffset(long index, long mask) {
/*  55 */     return CircularArrayOffsetCalculator.calcElementOffset(index, mask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final long calcElementOffset(long index) {
/*  64 */     return calcElementOffset(index, this.mask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  70 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/*  76 */     return IndexedQueueSizeUtil.size(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/*  82 */     return IndexedQueueSizeUtil.isEmpty(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  94 */     while (poll() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 103 */     return (int)(this.mask + 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long currentProducerIndex() {
/* 109 */     return lvProducerIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long currentConsumerIndex() {
/* 115 */     return lvConsumerIndex();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\ConcurrentCircularArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */