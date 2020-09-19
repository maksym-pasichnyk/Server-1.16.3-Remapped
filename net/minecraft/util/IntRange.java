/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Random;
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
/*    */ public class IntRange
/*    */ {
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   public IntRange(int debug1, int debug2) {
/* 22 */     if (debug2 < debug1) {
/* 23 */       throw new IllegalArgumentException("max must be >= minInclusive! Given minInclusive: " + debug1 + ", Given max: " + debug2);
/*    */     }
/* 25 */     this.minInclusive = debug1;
/* 26 */     this.maxInclusive = debug2;
/*    */   }
/*    */   
/*    */   public static IntRange of(int debug0, int debug1) {
/* 30 */     return new IntRange(debug0, debug1);
/*    */   }
/*    */   
/*    */   public int randomValue(Random debug1) {
/* 34 */     if (this.minInclusive == this.maxInclusive) {
/* 35 */       return this.minInclusive;
/*    */     }
/* 37 */     return debug1.nextInt(this.maxInclusive - this.minInclusive + 1) + this.minInclusive;
/*    */   }
/*    */   
/*    */   public int getMinInclusive() {
/* 41 */     return this.minInclusive;
/*    */   }
/*    */   
/*    */   public int getMaxInclusive() {
/* 45 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return "IntRange[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\IntRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */