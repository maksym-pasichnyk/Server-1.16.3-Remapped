/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class FrameTimer
/*    */ {
/*  6 */   private final long[] loggedTimes = new long[240];
/*    */   
/*    */   private int logStart;
/*    */   
/*    */   private int logLength;
/*    */   
/*    */   private int logEnd;
/*    */   
/*    */   public void logFrameDuration(long debug1) {
/* 15 */     this.loggedTimes[this.logEnd] = debug1;
/*    */     
/* 17 */     this.logEnd++;
/* 18 */     if (this.logEnd == 240) {
/* 19 */       this.logEnd = 0;
/*    */     }
/*    */     
/* 22 */     if (this.logLength < 240) {
/* 23 */       this.logStart = 0;
/* 24 */       this.logLength++;
/*    */     } else {
/* 26 */       this.logStart = wrapIndex(this.logEnd + 1);
/*    */     } 
/*    */   }
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
/*    */   public int wrapIndex(int debug1) {
/* 60 */     return debug1 % 240;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\FrameTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */