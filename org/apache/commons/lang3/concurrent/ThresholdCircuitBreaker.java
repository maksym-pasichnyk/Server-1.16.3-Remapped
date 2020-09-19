/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public class ThresholdCircuitBreaker
/*     */   extends AbstractCircuitBreaker<Long>
/*     */ {
/*     */   private static final long INITIAL_COUNT = 0L;
/*     */   private final long threshold;
/*     */   private final AtomicLong used;
/*     */   
/*     */   public ThresholdCircuitBreaker(long threshold) {
/*  77 */     this.used = new AtomicLong(0L);
/*  78 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThreshold() {
/*  87 */     return this.threshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkState() throws CircuitBreakingException {
/*  95 */     return isOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 105 */     super.close();
/* 106 */     this.used.set(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean incrementAndCheckState(Long increment) throws CircuitBreakingException {
/* 116 */     if (this.threshold == 0L) {
/* 117 */       open();
/*     */     }
/*     */     
/* 120 */     long used = this.used.addAndGet(increment.longValue());
/* 121 */     if (used > this.threshold) {
/* 122 */       open();
/*     */     }
/*     */     
/* 125 */     return checkState();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\ThresholdCircuitBreaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */