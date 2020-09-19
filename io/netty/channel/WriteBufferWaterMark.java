/*    */ package io.netty.channel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WriteBufferWaterMark
/*    */ {
/*    */   private static final int DEFAULT_LOW_WATER_MARK = 32768;
/*    */   private static final int DEFAULT_HIGH_WATER_MARK = 65536;
/* 36 */   public static final WriteBufferWaterMark DEFAULT = new WriteBufferWaterMark(32768, 65536, false);
/*    */ 
/*    */ 
/*    */   
/*    */   private final int low;
/*    */ 
/*    */ 
/*    */   
/*    */   private final int high;
/*    */ 
/*    */ 
/*    */   
/*    */   public WriteBufferWaterMark(int low, int high) {
/* 49 */     this(low, high, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   WriteBufferWaterMark(int low, int high, boolean validate) {
/* 56 */     if (validate) {
/* 57 */       if (low < 0) {
/* 58 */         throw new IllegalArgumentException("write buffer's low water mark must be >= 0");
/*    */       }
/* 60 */       if (high < low) {
/* 61 */         throw new IllegalArgumentException("write buffer's high water mark cannot be less than  low water mark (" + low + "): " + high);
/*    */       }
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 67 */     this.low = low;
/* 68 */     this.high = high;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int low() {
/* 75 */     return this.low;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int high() {
/* 82 */     return this.high;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     StringBuilder builder = (new StringBuilder(55)).append("WriteBufferWaterMark(low: ").append(this.low).append(", high: ").append(this.high).append(")");
/* 93 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\WriteBufferWaterMark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */