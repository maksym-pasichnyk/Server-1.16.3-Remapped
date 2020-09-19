/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
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
/*     */ abstract class MpscArrayQueueProducerLimitField<E>
/*     */   extends MpscArrayQueueMidPad<E>
/*     */ {
/*     */   private static final long P_LIMIT_OFFSET;
/*     */   private volatile long producerLimit;
/*     */   
/*     */   static {
/*     */     try {
/*  90 */       P_LIMIT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueProducerLimitField.class.getDeclaredField("producerLimit"));
/*     */     }
/*  92 */     catch (NoSuchFieldException e) {
/*     */       
/*  94 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MpscArrayQueueProducerLimitField(int capacity) {
/* 103 */     super(capacity);
/* 104 */     this.producerLimit = capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final long lvProducerLimit() {
/* 109 */     return this.producerLimit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void soProducerLimit(long newValue) {
/* 114 */     UnsafeAccess.UNSAFE.putOrderedLong(this, P_LIMIT_OFFSET, newValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\MpscArrayQueueProducerLimitField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */