/*    */ package net.minecraft.world.level.levelgen.synth;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
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
/*    */ public class NormalNoise
/*    */ {
/*    */   private final double valueFactor;
/*    */   private final PerlinNoise first;
/*    */   private final PerlinNoise second;
/*    */   
/*    */   public static NormalNoise create(WorldgenRandom debug0, int debug1, DoubleList debug2) {
/* 31 */     return new NormalNoise(debug0, debug1, debug2);
/*    */   }
/*    */   
/*    */   private NormalNoise(WorldgenRandom debug1, int debug2, DoubleList debug3) {
/* 35 */     this.first = PerlinNoise.create(debug1, debug2, debug3);
/* 36 */     this.second = PerlinNoise.create(debug1, debug2, debug3);
/*    */     
/* 38 */     int debug4 = Integer.MAX_VALUE;
/* 39 */     int debug5 = Integer.MIN_VALUE;
/*    */     
/* 41 */     DoubleListIterator debug6 = debug3.iterator();
/* 42 */     while (debug6.hasNext()) {
/* 43 */       int debug7 = debug6.nextIndex();
/* 44 */       double debug8 = debug6.nextDouble();
/* 45 */       if (debug8 != 0.0D) {
/* 46 */         debug4 = Math.min(debug4, debug7);
/* 47 */         debug5 = Math.max(debug5, debug7);
/*    */       } 
/*    */     } 
/*    */     
/* 51 */     this.valueFactor = 0.16666666666666666D / expectedDeviation(debug5 - debug4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static double expectedDeviation(int debug0) {
/* 58 */     return 0.1D * (1.0D + 1.0D / (debug0 + 1));
/*    */   }
/*    */   
/*    */   public double getValue(double debug1, double debug3, double debug5) {
/* 62 */     double debug7 = debug1 * 1.0181268882175227D;
/* 63 */     double debug9 = debug3 * 1.0181268882175227D;
/* 64 */     double debug11 = debug5 * 1.0181268882175227D;
/* 65 */     return (this.first.getValue(debug1, debug3, debug5) + this.second.getValue(debug7, debug9, debug11)) * this.valueFactor;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\synth\NormalNoise.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */