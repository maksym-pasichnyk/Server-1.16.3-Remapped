/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil;
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*     */ abstract class AtomicReferenceArrayQueue<E>
/*     */   extends AbstractQueue<E>
/*     */   implements IndexedQueueSizeUtil.IndexedQueue, QueueProgressIndicators, MessagePassingQueue<E>
/*     */ {
/*     */   protected final AtomicReferenceArray<E> buffer;
/*     */   protected final int mask;
/*     */   
/*     */   public AtomicReferenceArrayQueue(int capacity) {
/*  34 */     int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
/*  35 */     this.mask = actualCapacity - 1;
/*  36 */     this.buffer = new AtomicReferenceArray<E>(actualCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  42 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  48 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  54 */     while (poll() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int calcElementOffset(long index, int mask) {
/*  62 */     return (int)index & mask;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final int calcElementOffset(long index) {
/*  67 */     return (int)index & this.mask;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> E lvElement(AtomicReferenceArray<E> buffer, int offset) {
/*  72 */     return buffer.get(offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> E lpElement(AtomicReferenceArray<E> buffer, int offset) {
/*  77 */     return buffer.get(offset);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final E lpElement(int offset) {
/*  82 */     return this.buffer.get(offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> void spElement(AtomicReferenceArray<E> buffer, int offset, E value) {
/*  87 */     buffer.lazySet(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void spElement(int offset, E value) {
/*  92 */     this.buffer.lazySet(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> void soElement(AtomicReferenceArray<E> buffer, int offset, E value) {
/*  97 */     buffer.lazySet(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void soElement(int offset, E value) {
/* 102 */     this.buffer.lazySet(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> void svElement(AtomicReferenceArray<E> buffer, int offset, E value) {
/* 107 */     buffer.set(offset, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final E lvElement(int offset) {
/* 112 */     return lvElement(this.buffer, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int capacity() {
/* 118 */     return this.mask + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/* 128 */     return IndexedQueueSizeUtil.size(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 134 */     return IndexedQueueSizeUtil.isEmpty(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long currentProducerIndex() {
/* 140 */     return lvProducerIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final long currentConsumerIndex() {
/* 146 */     return lvConsumerIndex();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\AtomicReferenceArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */