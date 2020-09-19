/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class IceSpikeFeature extends Feature<NoneFeatureConfiguration> {
/*     */   public IceSpikeFeature(Codec<NoneFeatureConfiguration> debug1) {
/*  17 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/*  22 */     while (debug1.isEmptyBlock(debug4) && debug4.getY() > 2) {
/*  23 */       debug4 = debug4.below();
/*     */     }
/*     */     
/*  26 */     if (!debug1.getBlockState(debug4).is(Blocks.SNOW_BLOCK)) {
/*  27 */       return false;
/*     */     }
/*  29 */     debug4 = debug4.above(debug3.nextInt(4));
/*     */     
/*  31 */     int debug6 = debug3.nextInt(4) + 7;
/*  32 */     int debug7 = debug6 / 4 + debug3.nextInt(2);
/*     */     
/*  34 */     if (debug7 > 1 && debug3.nextInt(60) == 0) {
/*  35 */       debug4 = debug4.above(10 + debug3.nextInt(30));
/*     */     }
/*     */     int debug8;
/*  38 */     for (debug8 = 0; debug8 < debug6; debug8++) {
/*  39 */       float f = (1.0F - debug8 / debug6) * debug7;
/*  40 */       int debug10 = Mth.ceil(f);
/*     */       
/*  42 */       for (int debug11 = -debug10; debug11 <= debug10; debug11++) {
/*  43 */         float debug12 = Mth.abs(debug11) - 0.25F;
/*  44 */         for (int debug13 = -debug10; debug13 <= debug10; debug13++) {
/*  45 */           float debug14 = Mth.abs(debug13) - 0.25F;
/*  46 */           if ((debug11 == 0 && debug13 == 0) || debug12 * debug12 + debug14 * debug14 <= f * f)
/*     */           {
/*     */             
/*  49 */             if ((debug11 != -debug10 && debug11 != debug10 && debug13 != -debug10 && debug13 != debug10) || 
/*  50 */               debug3.nextFloat() <= 0.75F) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  55 */               BlockState debug15 = debug1.getBlockState(debug4.offset(debug11, debug8, debug13));
/*  56 */               Block debug16 = debug15.getBlock();
/*     */               
/*  58 */               if (debug15.isAir() || isDirt(debug16) || debug16 == Blocks.SNOW_BLOCK || debug16 == Blocks.ICE) {
/*  59 */                 setBlock((LevelWriter)debug1, debug4.offset(debug11, debug8, debug13), Blocks.PACKED_ICE.defaultBlockState());
/*     */               }
/*     */               
/*  62 */               if (debug8 != 0 && debug10 > 1) {
/*  63 */                 debug15 = debug1.getBlockState(debug4.offset(debug11, -debug8, debug13));
/*  64 */                 debug16 = debug15.getBlock();
/*     */                 
/*  66 */                 if (debug15.isAir() || isDirt(debug16) || debug16 == Blocks.SNOW_BLOCK || debug16 == Blocks.ICE)
/*  67 */                   setBlock((LevelWriter)debug1, debug4.offset(debug11, -debug8, debug13), Blocks.PACKED_ICE.defaultBlockState()); 
/*     */               } 
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  73 */     debug8 = debug7 - 1;
/*  74 */     if (debug8 < 0) {
/*  75 */       debug8 = 0;
/*  76 */     } else if (debug8 > 1) {
/*  77 */       debug8 = 1;
/*     */     } 
/*  79 */     for (int debug9 = -debug8; debug9 <= debug8; debug9++) {
/*  80 */       for (int debug10 = -debug8; debug10 <= debug8; debug10++) {
/*  81 */         BlockPos debug11 = debug4.offset(debug9, -1, debug10);
/*  82 */         int debug12 = 50;
/*  83 */         if (Math.abs(debug9) == 1 && Math.abs(debug10) == 1) {
/*  84 */           debug12 = debug3.nextInt(5);
/*     */         }
/*  86 */         while (debug11.getY() > 50) {
/*  87 */           BlockState debug13 = debug1.getBlockState(debug11);
/*  88 */           Block debug14 = debug13.getBlock();
/*     */           
/*  90 */           if (debug13.isAir() || isDirt(debug14) || debug14 == Blocks.SNOW_BLOCK || debug14 == Blocks.ICE || debug14 == Blocks.PACKED_ICE) {
/*  91 */             setBlock((LevelWriter)debug1, debug11, Blocks.PACKED_ICE.defaultBlockState());
/*     */ 
/*     */ 
/*     */             
/*  95 */             debug11 = debug11.below();
/*  96 */             debug12--;
/*  97 */             if (debug12 <= 0) {
/*  98 */               debug11 = debug11.below(debug3.nextInt(5) + 1);
/*  99 */               debug12 = debug3.nextInt(5);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\IceSpikeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */