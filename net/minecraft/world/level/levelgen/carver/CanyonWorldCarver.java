/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.BitSet;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class CanyonWorldCarver
/*    */   extends WorldCarver<ProbabilityFeatureConfiguration>
/*    */ {
/* 16 */   private final float[] rs = new float[1024];
/*    */   
/*    */   public CanyonWorldCarver(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 19 */     super(debug1, 256);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStartChunk(Random debug1, int debug2, int debug3, ProbabilityFeatureConfiguration debug4) {
/* 24 */     return (debug1.nextFloat() <= debug4.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean carve(ChunkAccess debug1, Function<BlockPos, Biome> debug2, Random debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BitSet debug9, ProbabilityFeatureConfiguration debug10) {
/* 29 */     int debug11 = (getRange() * 2 - 1) * 16;
/*    */     
/* 31 */     double debug12 = (debug5 * 16 + debug3.nextInt(16));
/* 32 */     double debug14 = (debug3.nextInt(debug3.nextInt(40) + 8) + 20);
/* 33 */     double debug16 = (debug6 * 16 + debug3.nextInt(16));
/*    */     
/* 35 */     float debug18 = debug3.nextFloat() * 6.2831855F;
/* 36 */     float debug19 = (debug3.nextFloat() - 0.5F) * 2.0F / 8.0F;
/* 37 */     double debug20 = 3.0D;
/* 38 */     float debug22 = (debug3.nextFloat() * 2.0F + debug3.nextFloat()) * 2.0F;
/* 39 */     int debug23 = debug11 - debug3.nextInt(debug11 / 4);
/* 40 */     int debug24 = 0;
/* 41 */     genCanyon(debug1, debug2, debug3.nextLong(), debug4, debug7, debug8, debug12, debug14, debug16, debug22, debug18, debug19, 0, debug23, 3.0D, debug9);
/*    */     
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   private void genCanyon(ChunkAccess debug1, Function<BlockPos, Biome> debug2, long debug3, int debug5, int debug6, int debug7, double debug8, double debug10, double debug12, float debug14, float debug15, float debug16, int debug17, int debug18, double debug19, BitSet debug21) {
/* 47 */     Random debug22 = new Random(debug3);
/*    */     
/* 49 */     float debug23 = 1.0F;
/* 50 */     for (int i = 0; i < 256; i++) {
/* 51 */       if (i == 0 || debug22.nextInt(3) == 0) {
/* 52 */         debug23 = 1.0F + debug22.nextFloat() * debug22.nextFloat();
/*    */       }
/* 54 */       this.rs[i] = debug23 * debug23;
/*    */     } 
/*    */     
/* 57 */     float debug24 = 0.0F;
/* 58 */     float debug25 = 0.0F;
/*    */     
/* 60 */     for (int debug26 = debug17; debug26 < debug18; debug26++) {
/* 61 */       double debug27 = 1.5D + (Mth.sin(debug26 * 3.1415927F / debug18) * debug14);
/* 62 */       double debug29 = debug27 * debug19;
/*    */       
/* 64 */       debug27 *= debug22.nextFloat() * 0.25D + 0.75D;
/* 65 */       debug29 *= debug22.nextFloat() * 0.25D + 0.75D;
/*    */       
/* 67 */       float debug31 = Mth.cos(debug16);
/* 68 */       float debug32 = Mth.sin(debug16);
/* 69 */       debug8 += (Mth.cos(debug15) * debug31);
/* 70 */       debug10 += debug32;
/* 71 */       debug12 += (Mth.sin(debug15) * debug31);
/*    */       
/* 73 */       debug16 *= 0.7F;
/*    */       
/* 75 */       debug16 += debug25 * 0.05F;
/* 76 */       debug15 += debug24 * 0.05F;
/*    */       
/* 78 */       debug25 *= 0.8F;
/* 79 */       debug24 *= 0.5F;
/* 80 */       debug25 += (debug22.nextFloat() - debug22.nextFloat()) * debug22.nextFloat() * 2.0F;
/* 81 */       debug24 += (debug22.nextFloat() - debug22.nextFloat()) * debug22.nextFloat() * 4.0F;
/*    */       
/* 83 */       if (debug22.nextInt(4) != 0) {
/*    */ 
/*    */ 
/*    */         
/* 87 */         if (!canReach(debug6, debug7, debug8, debug12, debug26, debug18, debug14)) {
/*    */           return;
/*    */         }
/*    */         
/* 91 */         carveSphere(debug1, debug2, debug3, debug5, debug6, debug7, debug8, debug10, debug12, debug27, debug29, debug21);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected boolean skip(double debug1, double debug3, double debug5, int debug7) {
/* 97 */     return ((debug1 * debug1 + debug5 * debug5) * this.rs[debug7 - 1] + debug3 * debug3 / 6.0D >= 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CanyonWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */