/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.util.LinearCongruentialGenerator;
/*    */ 
/*    */ public enum FuzzyOffsetBiomeZoomer implements BiomeZoomer {
/*  6 */   INSTANCE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Biome getBiome(long debug1, int debug3, int debug4, int debug5, BiomeManager.NoiseBiomeSource debug6) {
/* 14 */     int debug7 = debug3 - 2;
/* 15 */     int debug8 = debug4 - 2;
/* 16 */     int debug9 = debug5 - 2;
/*    */     
/* 18 */     int debug10 = debug7 >> 2;
/* 19 */     int debug11 = debug8 >> 2;
/* 20 */     int debug12 = debug9 >> 2;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     double debug13 = (debug7 & 0x3) / 4.0D;
/* 26 */     double debug15 = (debug8 & 0x3) / 4.0D;
/* 27 */     double debug17 = (debug9 & 0x3) / 4.0D;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     double[] debug19 = new double[8];
/*    */     int debug20;
/* 34 */     for (debug20 = 0; debug20 < 8; debug20++) {
/* 35 */       boolean bool1 = ((debug20 & 0x4) == 0);
/* 36 */       boolean debug22 = ((debug20 & 0x2) == 0);
/* 37 */       boolean bool2 = ((debug20 & 0x1) == 0);
/*    */       
/* 39 */       int i = bool1 ? debug10 : (debug10 + 1);
/* 40 */       int j = debug22 ? debug11 : (debug11 + 1);
/* 41 */       int debug26 = bool2 ? debug12 : (debug12 + 1);
/*    */       
/* 43 */       double debug27 = bool1 ? debug13 : (debug13 - 1.0D);
/* 44 */       double debug29 = debug22 ? debug15 : (debug15 - 1.0D);
/* 45 */       double debug31 = bool2 ? debug17 : (debug17 - 1.0D);
/*    */       
/* 47 */       debug19[debug20] = getFiddledDistance(debug1, i, j, debug26, debug27, debug29, debug31);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 52 */     debug20 = 0;
/* 53 */     double debug21 = debug19[0]; int debug23;
/* 54 */     for (debug23 = 1; debug23 < 8; debug23++) {
/* 55 */       if (debug21 > debug19[debug23]) {
/* 56 */         debug20 = debug23;
/* 57 */         debug21 = debug19[debug23];
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     debug23 = ((debug20 & 0x4) == 0) ? debug10 : (debug10 + 1);
/* 62 */     int debug24 = ((debug20 & 0x2) == 0) ? debug11 : (debug11 + 1);
/* 63 */     int debug25 = ((debug20 & 0x1) == 0) ? debug12 : (debug12 + 1);
/*    */     
/* 65 */     return debug6.getNoiseBiome(debug23, debug24, debug25);
/*    */   }
/*    */   
/*    */   private static double getFiddledDistance(long debug0, int debug2, int debug3, int debug4, double debug5, double debug7, double debug9) {
/* 69 */     long debug11 = debug0;
/*    */     
/* 71 */     debug11 = LinearCongruentialGenerator.next(debug11, debug2);
/* 72 */     debug11 = LinearCongruentialGenerator.next(debug11, debug3);
/* 73 */     debug11 = LinearCongruentialGenerator.next(debug11, debug4);
/* 74 */     debug11 = LinearCongruentialGenerator.next(debug11, debug2);
/* 75 */     debug11 = LinearCongruentialGenerator.next(debug11, debug3);
/* 76 */     debug11 = LinearCongruentialGenerator.next(debug11, debug4);
/*    */     
/* 78 */     double debug13 = getFiddle(debug11);
/*    */     
/* 80 */     debug11 = LinearCongruentialGenerator.next(debug11, debug0);
/*    */     
/* 82 */     double debug15 = getFiddle(debug11);
/*    */     
/* 84 */     debug11 = LinearCongruentialGenerator.next(debug11, debug0);
/*    */     
/* 86 */     double debug17 = getFiddle(debug11);
/*    */     
/* 88 */     return sqr(debug9 + debug17) + sqr(debug7 + debug15) + sqr(debug5 + debug13);
/*    */   }
/*    */   
/*    */   private static double getFiddle(long debug0) {
/* 92 */     double debug2 = (int)Math.floorMod(debug0 >> 24L, 1024L) / 1024.0D;
/* 93 */     return (debug2 - 0.5D) * 0.9D;
/*    */   }
/*    */   
/*    */   private static double sqr(double debug0) {
/* 97 */     return debug0 * debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\FuzzyOffsetBiomeZoomer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */