/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ 
/*     */ public class CaveWorldCarver
/*     */   extends WorldCarver<ProbabilityFeatureConfiguration> {
/*     */   public CaveWorldCarver(Codec<ProbabilityFeatureConfiguration> debug1, int debug2) {
/*  16 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStartChunk(Random debug1, int debug2, int debug3, ProbabilityFeatureConfiguration debug4) {
/*  21 */     return (debug1.nextFloat() <= debug4.probability);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean carve(ChunkAccess debug1, Function<BlockPos, Biome> debug2, Random debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BitSet debug9, ProbabilityFeatureConfiguration debug10) {
/*  26 */     int debug11 = (getRange() * 2 - 1) * 16;
/*  27 */     int debug12 = debug3.nextInt(debug3.nextInt(debug3.nextInt(getCaveBound()) + 1) + 1);
/*     */     
/*  29 */     for (int debug13 = 0; debug13 < debug12; debug13++) {
/*     */       
/*  31 */       double debug14 = (debug5 * 16 + debug3.nextInt(16));
/*  32 */       double debug16 = getCaveY(debug3);
/*  33 */       double debug18 = (debug6 * 16 + debug3.nextInt(16));
/*     */       
/*  35 */       int debug20 = 1;
/*  36 */       if (debug3.nextInt(4) == 0) {
/*     */         
/*  38 */         double d = 0.5D;
/*  39 */         float debug23 = 1.0F + debug3.nextFloat() * 6.0F;
/*  40 */         genRoom(debug1, debug2, debug3.nextLong(), debug4, debug7, debug8, debug14, debug16, debug18, debug23, 0.5D, debug9);
/*  41 */         debug20 += debug3.nextInt(4);
/*     */       } 
/*     */       
/*  44 */       for (int debug21 = 0; debug21 < debug20; debug21++) {
/*     */         
/*  46 */         float debug22 = debug3.nextFloat() * 6.2831855F;
/*  47 */         float debug23 = (debug3.nextFloat() - 0.5F) / 4.0F;
/*  48 */         float debug24 = getThickness(debug3);
/*  49 */         int debug25 = debug11 - debug3.nextInt(debug11 / 4);
/*  50 */         int debug26 = 0;
/*  51 */         genTunnel(debug1, debug2, debug3.nextLong(), debug4, debug7, debug8, debug14, debug16, debug18, debug24, debug22, debug23, 0, debug25, getYScale(), debug9);
/*     */       } 
/*     */     } 
/*     */     
/*  55 */     return true;
/*     */   }
/*     */   
/*     */   protected int getCaveBound() {
/*  59 */     return 15;
/*     */   }
/*     */   
/*     */   protected float getThickness(Random debug1) {
/*  63 */     float debug2 = debug1.nextFloat() * 2.0F + debug1.nextFloat();
/*  64 */     if (debug1.nextInt(10) == 0) {
/*  65 */       debug2 *= debug1.nextFloat() * debug1.nextFloat() * 3.0F + 1.0F;
/*     */     }
/*  67 */     return debug2;
/*     */   }
/*     */   
/*     */   protected double getYScale() {
/*  71 */     return 1.0D;
/*     */   }
/*     */   
/*     */   protected int getCaveY(Random debug1) {
/*  75 */     return debug1.nextInt(debug1.nextInt(120) + 8);
/*     */   }
/*     */   
/*     */   protected void genRoom(ChunkAccess debug1, Function<BlockPos, Biome> debug2, long debug3, int debug5, int debug6, int debug7, double debug8, double debug10, double debug12, float debug14, double debug15, BitSet debug17) {
/*  79 */     double debug18 = 1.5D + (Mth.sin(1.5707964F) * debug14);
/*  80 */     double debug20 = debug18 * debug15;
/*     */     
/*  82 */     carveSphere(debug1, debug2, debug3, debug5, debug6, debug7, debug8 + 1.0D, debug10, debug12, debug18, debug20, debug17);
/*     */   }
/*     */   
/*     */   protected void genTunnel(ChunkAccess debug1, Function<BlockPos, Biome> debug2, long debug3, int debug5, int debug6, int debug7, double debug8, double debug10, double debug12, float debug14, float debug15, float debug16, int debug17, int debug18, double debug19, BitSet debug21) {
/*  86 */     Random debug22 = new Random(debug3);
/*     */     
/*  88 */     int debug23 = debug22.nextInt(debug18 / 2) + debug18 / 4;
/*  89 */     boolean debug24 = (debug22.nextInt(6) == 0);
/*     */     
/*  91 */     float debug25 = 0.0F;
/*  92 */     float debug26 = 0.0F;
/*     */     
/*  94 */     for (int debug27 = debug17; debug27 < debug18; debug27++) {
/*  95 */       double debug28 = 1.5D + (Mth.sin(3.1415927F * debug27 / debug18) * debug14);
/*  96 */       double debug30 = debug28 * debug19;
/*     */       
/*  98 */       float debug32 = Mth.cos(debug16);
/*  99 */       debug8 += (Mth.cos(debug15) * debug32);
/* 100 */       debug10 += Mth.sin(debug16);
/* 101 */       debug12 += (Mth.sin(debug15) * debug32);
/*     */       
/* 103 */       debug16 *= debug24 ? 0.92F : 0.7F;
/* 104 */       debug16 += debug26 * 0.1F;
/* 105 */       debug15 += debug25 * 0.1F;
/*     */       
/* 107 */       debug26 *= 0.9F;
/* 108 */       debug25 *= 0.75F;
/* 109 */       debug26 += (debug22.nextFloat() - debug22.nextFloat()) * debug22.nextFloat() * 2.0F;
/* 110 */       debug25 += (debug22.nextFloat() - debug22.nextFloat()) * debug22.nextFloat() * 4.0F;
/*     */ 
/*     */       
/* 113 */       if (debug27 == debug23 && debug14 > 1.0F) {
/* 114 */         genTunnel(debug1, debug2, debug22.nextLong(), debug5, debug6, debug7, debug8, debug10, debug12, debug22.nextFloat() * 0.5F + 0.5F, debug15 - 1.5707964F, debug16 / 3.0F, debug27, debug18, 1.0D, debug21);
/* 115 */         genTunnel(debug1, debug2, debug22.nextLong(), debug5, debug6, debug7, debug8, debug10, debug12, debug22.nextFloat() * 0.5F + 0.5F, debug15 + 1.5707964F, debug16 / 3.0F, debug27, debug18, 1.0D, debug21);
/*     */         
/*     */         return;
/*     */       } 
/* 119 */       if (debug22.nextInt(4) != 0) {
/*     */ 
/*     */ 
/*     */         
/* 123 */         if (!canReach(debug6, debug7, debug8, debug12, debug27, debug18, debug14)) {
/*     */           return;
/*     */         }
/* 126 */         carveSphere(debug1, debug2, debug3, debug5, debug6, debug7, debug8, debug10, debug12, debug28, debug30, debug21);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean skip(double debug1, double debug3, double debug5, int debug7) {
/* 132 */     return (debug3 <= -0.7D || debug1 * debug1 + debug3 * debug3 + debug5 * debug5 >= 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CaveWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */