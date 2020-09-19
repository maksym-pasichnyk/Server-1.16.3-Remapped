/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class LakeFeature extends Feature<BlockStateConfiguration> {
/*  19 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public LakeFeature(Codec<BlockStateConfiguration> debug1) {
/*  22 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, BlockStateConfiguration debug5) {
/*  27 */     while (debug4.getY() > 5 && debug1.isEmptyBlock(debug4)) {
/*  28 */       debug4 = debug4.below();
/*     */     }
/*  30 */     if (debug4.getY() <= 4) {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     debug4 = debug4.below(4);
/*     */     
/*  36 */     if (debug1.startsForFeature(SectionPos.of(debug4), StructureFeature.VILLAGE).findAny().isPresent()) {
/*  37 */       return false;
/*     */     }
/*     */     
/*  40 */     boolean[] debug6 = new boolean[2048];
/*     */     
/*  42 */     int debug7 = debug3.nextInt(4) + 4; int debug8;
/*  43 */     for (debug8 = 0; debug8 < debug7; debug8++) {
/*  44 */       double debug9 = debug3.nextDouble() * 6.0D + 3.0D;
/*  45 */       double debug11 = debug3.nextDouble() * 4.0D + 2.0D;
/*  46 */       double debug13 = debug3.nextDouble() * 6.0D + 3.0D;
/*     */       
/*  48 */       double debug15 = debug3.nextDouble() * (16.0D - debug9 - 2.0D) + 1.0D + debug9 / 2.0D;
/*  49 */       double debug17 = debug3.nextDouble() * (8.0D - debug11 - 4.0D) + 2.0D + debug11 / 2.0D;
/*  50 */       double debug19 = debug3.nextDouble() * (16.0D - debug13 - 2.0D) + 1.0D + debug13 / 2.0D;
/*     */       
/*  52 */       for (int debug21 = 1; debug21 < 15; debug21++) {
/*  53 */         for (int debug22 = 1; debug22 < 15; debug22++) {
/*  54 */           for (int debug23 = 1; debug23 < 7; debug23++) {
/*  55 */             double debug24 = (debug21 - debug15) / debug9 / 2.0D;
/*  56 */             double debug26 = (debug23 - debug17) / debug11 / 2.0D;
/*  57 */             double debug28 = (debug22 - debug19) / debug13 / 2.0D;
/*  58 */             double debug30 = debug24 * debug24 + debug26 * debug26 + debug28 * debug28;
/*  59 */             if (debug30 < 1.0D) {
/*  60 */               debug6[(debug21 * 16 + debug22) * 8 + debug23] = true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     for (debug8 = 0; debug8 < 16; debug8++) {
/*  68 */       for (int debug9 = 0; debug9 < 16; debug9++) {
/*  69 */         for (int debug10 = 0; debug10 < 8; debug10++) {
/*  70 */           boolean debug11 = (!debug6[(debug8 * 16 + debug9) * 8 + debug10] && ((debug8 < 15 && debug6[((debug8 + 1) * 16 + debug9) * 8 + debug10]) || (debug8 > 0 && debug6[((debug8 - 1) * 16 + debug9) * 8 + debug10]) || (debug9 < 15 && debug6[(debug8 * 16 + debug9 + 1) * 8 + debug10]) || (debug9 > 0 && debug6[(debug8 * 16 + debug9 - 1) * 8 + debug10]) || (debug10 < 7 && debug6[(debug8 * 16 + debug9) * 8 + debug10 + 1]) || (debug10 > 0 && debug6[(debug8 * 16 + debug9) * 8 + debug10 - 1])));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  79 */           if (debug11) {
/*  80 */             Material debug12 = debug1.getBlockState(debug4.offset(debug8, debug10, debug9)).getMaterial();
/*  81 */             if (debug10 >= 4 && debug12.isLiquid()) {
/*  82 */               return false;
/*     */             }
/*  84 */             if (debug10 < 4 && !debug12.isSolid() && debug1.getBlockState(debug4.offset(debug8, debug10, debug9)) != debug5.state) {
/*  85 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     for (debug8 = 0; debug8 < 16; debug8++) {
/*  93 */       for (int debug9 = 0; debug9 < 16; debug9++) {
/*  94 */         for (int debug10 = 0; debug10 < 8; debug10++) {
/*  95 */           if (debug6[(debug8 * 16 + debug9) * 8 + debug10]) {
/*  96 */             debug1.setBlock(debug4.offset(debug8, debug10, debug9), (debug10 >= 4) ? AIR : debug5.state, 2);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     for (debug8 = 0; debug8 < 16; debug8++) {
/* 103 */       for (int debug9 = 0; debug9 < 16; debug9++) {
/* 104 */         for (int debug10 = 4; debug10 < 8; debug10++) {
/* 105 */           if (debug6[(debug8 * 16 + debug9) * 8 + debug10]) {
/* 106 */             BlockPos debug11 = debug4.offset(debug8, debug10 - 1, debug9);
/*     */             
/* 108 */             if (isDirt(debug1.getBlockState(debug11).getBlock()) && debug1.getBrightness(LightLayer.SKY, debug4.offset(debug8, debug10, debug9)) > 0) {
/* 109 */               Biome debug12 = debug1.getBiome(debug11);
/* 110 */               if (debug12.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial().is(Blocks.MYCELIUM)) {
/* 111 */                 debug1.setBlock(debug11, Blocks.MYCELIUM.defaultBlockState(), 2);
/*     */               } else {
/* 113 */                 debug1.setBlock(debug11, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (debug5.state.getMaterial() == Material.LAVA) {
/* 122 */       for (debug8 = 0; debug8 < 16; debug8++) {
/* 123 */         for (int debug9 = 0; debug9 < 16; debug9++) {
/* 124 */           for (int debug10 = 0; debug10 < 8; debug10++) {
/* 125 */             boolean debug11 = (!debug6[(debug8 * 16 + debug9) * 8 + debug10] && ((debug8 < 15 && debug6[((debug8 + 1) * 16 + debug9) * 8 + debug10]) || (debug8 > 0 && debug6[((debug8 - 1) * 16 + debug9) * 8 + debug10]) || (debug9 < 15 && debug6[(debug8 * 16 + debug9 + 1) * 8 + debug10]) || (debug9 > 0 && debug6[(debug8 * 16 + debug9 - 1) * 8 + debug10]) || (debug10 < 7 && debug6[(debug8 * 16 + debug9) * 8 + debug10 + 1]) || (debug10 > 0 && debug6[(debug8 * 16 + debug9) * 8 + debug10 - 1])));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 134 */             if (debug11 && (
/* 135 */               debug10 < 4 || debug3.nextInt(2) != 0) && debug1.getBlockState(debug4.offset(debug8, debug10, debug9)).getMaterial().isSolid()) {
/* 136 */               debug1.setBlock(debug4.offset(debug8, debug10, debug9), Blocks.STONE.defaultBlockState(), 2);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 144 */     if (debug5.state.getMaterial() == Material.WATER) {
/* 145 */       for (debug8 = 0; debug8 < 16; debug8++) {
/* 146 */         for (int debug9 = 0; debug9 < 16; debug9++) {
/* 147 */           int debug10 = 4;
/* 148 */           BlockPos debug11 = debug4.offset(debug8, 4, debug9);
/* 149 */           if (debug1.getBiome(debug11).shouldFreeze((LevelReader)debug1, debug11, false)) {
/* 150 */             debug1.setBlock(debug11, Blocks.ICE.defaultBlockState(), 2);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 156 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\LakeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */