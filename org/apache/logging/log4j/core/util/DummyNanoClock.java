/*    */ package org.apache.logging.log4j.core.util;
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
/*    */ public final class DummyNanoClock
/*    */   implements NanoClock
/*    */ {
/*    */   private final long fixedNanoTime;
/*    */   
/*    */   public DummyNanoClock() {
/* 27 */     this(0L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DummyNanoClock(long fixedNanoTime) {
/* 35 */     this.fixedNanoTime = fixedNanoTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long nanoTime() {
/* 45 */     return this.fixedNanoTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\DummyNanoClock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */