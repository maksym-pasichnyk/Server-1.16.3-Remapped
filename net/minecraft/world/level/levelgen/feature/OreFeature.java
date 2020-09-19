/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ 
/*     */ public class OreFeature
/*     */   extends Feature<OreConfiguration> {
/*     */   public OreFeature(Codec<OreConfiguration> debug1) {
/*  18 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, OreConfiguration debug5) {
/*  23 */     float debug6 = debug3.nextFloat() * 3.1415927F;
/*     */     
/*  25 */     float debug7 = debug5.size / 8.0F;
/*  26 */     int debug8 = Mth.ceil((debug5.size / 16.0F * 2.0F + 1.0F) / 2.0F);
/*  27 */     double debug9 = debug4.getX() + Math.sin(debug6) * debug7;
/*  28 */     double debug11 = debug4.getX() - Math.sin(debug6) * debug7;
/*  29 */     double debug13 = debug4.getZ() + Math.cos(debug6) * debug7;
/*  30 */     double debug15 = debug4.getZ() - Math.cos(debug6) * debug7;
/*     */     
/*  32 */     int debug17 = 2;
/*  33 */     double debug18 = (debug4.getY() + debug3.nextInt(3) - 2);
/*  34 */     double debug20 = (debug4.getY() + debug3.nextInt(3) - 2);
/*     */     
/*  36 */     int debug22 = debug4.getX() - Mth.ceil(debug7) - debug8;
/*  37 */     int debug23 = debug4.getY() - 2 - debug8;
/*  38 */     int debug24 = debug4.getZ() - Mth.ceil(debug7) - debug8;
/*  39 */     int debug25 = 2 * (Mth.ceil(debug7) + debug8);
/*  40 */     int debug26 = 2 * (2 + debug8);
/*     */ 
/*     */     
/*  43 */     for (int debug27 = debug22; debug27 <= debug22 + debug25; debug27++) {
/*  44 */       for (int debug28 = debug24; debug28 <= debug24 + debug25; debug28++) {
/*  45 */         if (debug23 <= debug1.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, debug27, debug28)) {
/*  46 */           return doPlace((LevelAccessor)debug1, debug3, debug5, debug9, debug11, debug13, debug15, debug18, debug20, debug22, debug23, debug24, debug25, debug26);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean doPlace(LevelAccessor debug1, Random debug2, OreConfiguration debug3, double debug4, double debug6, double debug8, double debug10, double debug12, double debug14, int debug16, int debug17, int debug18, int debug19, int debug20) {
/*  55 */     int debug21 = 0;
/*     */     
/*  57 */     BitSet debug22 = new BitSet(debug19 * debug20 * debug19);
/*  58 */     BlockPos.MutableBlockPos debug23 = new BlockPos.MutableBlockPos();
/*  59 */     int debug24 = debug3.size;
/*  60 */     double[] debug25 = new double[debug24 * 4];
/*     */     int debug26;
/*  62 */     for (debug26 = 0; debug26 < debug24; debug26++) {
/*  63 */       float debug27 = debug26 / debug24;
/*  64 */       double debug28 = Mth.lerp(debug27, debug4, debug6);
/*  65 */       double debug30 = Mth.lerp(debug27, debug12, debug14);
/*  66 */       double debug32 = Mth.lerp(debug27, debug8, debug10);
/*     */       
/*  68 */       double debug34 = debug2.nextDouble() * debug24 / 16.0D;
/*  69 */       double debug36 = ((Mth.sin(3.1415927F * debug27) + 1.0F) * debug34 + 1.0D) / 2.0D;
/*     */       
/*  71 */       debug25[debug26 * 4 + 0] = debug28;
/*  72 */       debug25[debug26 * 4 + 1] = debug30;
/*  73 */       debug25[debug26 * 4 + 2] = debug32;
/*  74 */       debug25[debug26 * 4 + 3] = debug36;
/*     */     } 
/*     */     
/*  77 */     for (debug26 = 0; debug26 < debug24 - 1; debug26++) {
/*  78 */       if (debug25[debug26 * 4 + 3] > 0.0D)
/*     */       {
/*     */ 
/*     */         
/*  82 */         for (int debug27 = debug26 + 1; debug27 < debug24; debug27++) {
/*  83 */           if (debug25[debug27 * 4 + 3] > 0.0D) {
/*     */ 
/*     */ 
/*     */             
/*  87 */             double debug28 = debug25[debug26 * 4 + 0] - debug25[debug27 * 4 + 0];
/*  88 */             double debug30 = debug25[debug26 * 4 + 1] - debug25[debug27 * 4 + 1];
/*  89 */             double debug32 = debug25[debug26 * 4 + 2] - debug25[debug27 * 4 + 2];
/*  90 */             double debug34 = debug25[debug26 * 4 + 3] - debug25[debug27 * 4 + 3];
/*     */             
/*  92 */             if (debug34 * debug34 > debug28 * debug28 + debug30 * debug30 + debug32 * debug32)
/*  93 */               if (debug34 > 0.0D) {
/*  94 */                 debug25[debug27 * 4 + 3] = -1.0D;
/*     */               } else {
/*  96 */                 debug25[debug26 * 4 + 3] = -1.0D;
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 102 */     for (debug26 = 0; debug26 < debug24; debug26++) {
/* 103 */       double debug27 = debug25[debug26 * 4 + 3];
/* 104 */       if (debug27 >= 0.0D) {
/*     */ 
/*     */ 
/*     */         
/* 108 */         double debug29 = debug25[debug26 * 4 + 0];
/* 109 */         double debug31 = debug25[debug26 * 4 + 1];
/* 110 */         double debug33 = debug25[debug26 * 4 + 2];
/*     */ 
/*     */         
/* 113 */         int debug35 = Math.max(Mth.floor(debug29 - debug27), debug16);
/* 114 */         int debug36 = Math.max(Mth.floor(debug31 - debug27), debug17);
/* 115 */         int debug37 = Math.max(Mth.floor(debug33 - debug27), debug18);
/*     */         
/* 117 */         int debug38 = Math.max(Mth.floor(debug29 + debug27), debug35);
/* 118 */         int debug39 = Math.max(Mth.floor(debug31 + debug27), debug36);
/* 119 */         int debug40 = Math.max(Mth.floor(debug33 + debug27), debug37);
/*     */         
/* 121 */         for (int debug41 = debug35; debug41 <= debug38; debug41++) {
/* 122 */           double debug42 = (debug41 + 0.5D - debug29) / debug27;
/* 123 */           if (debug42 * debug42 < 1.0D)
/* 124 */             for (int debug44 = debug36; debug44 <= debug39; debug44++) {
/* 125 */               double debug45 = (debug44 + 0.5D - debug31) / debug27;
/* 126 */               if (debug42 * debug42 + debug45 * debug45 < 1.0D) {
/* 127 */                 for (int debug47 = debug37; debug47 <= debug40; debug47++) {
/* 128 */                   double debug48 = (debug47 + 0.5D - debug33) / debug27;
/* 129 */                   if (debug42 * debug42 + debug45 * debug45 + debug48 * debug48 < 1.0D) {
/* 130 */                     int debug50 = debug41 - debug16 + (debug44 - debug17) * debug19 + (debug47 - debug18) * debug19 * debug20;
/* 131 */                     if (!debug22.get(debug50)) {
/*     */ 
/*     */                       
/* 134 */                       debug22.set(debug50);
/*     */                       
/* 136 */                       debug23.set(debug41, debug44, debug47);
/* 137 */                       if (debug3.target.test(debug1.getBlockState((BlockPos)debug23), debug2)) {
/* 138 */                         debug1.setBlock((BlockPos)debug23, debug3.state, 2);
/* 139 */                         debug21++;
/*     */                       } 
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/* 149 */     return (debug21 > 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\OreFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */