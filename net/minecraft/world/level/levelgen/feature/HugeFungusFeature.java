/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ 
/*     */ public class HugeFungusFeature
/*     */   extends Feature<HugeFungusConfiguration> {
/*     */   public HugeFungusFeature(Codec<HugeFungusConfiguration> debug1) {
/*  22 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, HugeFungusConfiguration debug5) {
/*  27 */     Block debug6 = debug5.validBaseState.getBlock();
/*  28 */     BlockPos debug7 = null;
/*     */     
/*  30 */     Block debug8 = debug1.getBlockState(debug4.below()).getBlock();
/*  31 */     if (debug8 == debug6) {
/*  32 */       debug7 = debug4;
/*     */     }
/*     */     
/*  35 */     if (debug7 == null) {
/*  36 */       return false;
/*     */     }
/*     */     
/*  39 */     int debug9 = Mth.nextInt(debug3, 4, 13);
/*  40 */     if (debug3.nextInt(12) == 0) {
/*  41 */       debug9 *= 2;
/*     */     }
/*     */     
/*  44 */     if (!debug5.planted) {
/*  45 */       int i = debug2.getGenDepth();
/*  46 */       if (debug7.getY() + debug9 + 1 >= i) {
/*  47 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  51 */     boolean debug10 = (!debug5.planted && debug3.nextFloat() < 0.06F);
/*     */     
/*  53 */     debug1.setBlock(debug4, Blocks.AIR.defaultBlockState(), 4);
/*     */     
/*  55 */     placeStem((LevelAccessor)debug1, debug3, debug5, debug7, debug9, debug10);
/*  56 */     placeHat((LevelAccessor)debug1, debug3, debug5, debug7, debug9, debug10);
/*     */     
/*  58 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isReplaceable(LevelAccessor debug0, BlockPos debug1, boolean debug2) {
/*  62 */     return debug0.isStateAtPosition(debug1, debug1 -> {
/*     */           Material debug2 = debug1.getMaterial();
/*  64 */           return (debug1.getMaterial().isReplaceable() || (debug0 && debug2 == Material.PLANT));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeStem(LevelAccessor debug1, Random debug2, HugeFungusConfiguration debug3, BlockPos debug4, int debug5, boolean debug6) {
/*  70 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/*  71 */     BlockState debug8 = debug3.stemState;
/*  72 */     int debug9 = debug6 ? 1 : 0;
/*     */     
/*  74 */     for (int debug10 = -debug9; debug10 <= debug9; debug10++) {
/*  75 */       for (int debug11 = -debug9; debug11 <= debug9; debug11++) {
/*  76 */         boolean debug12 = (debug6 && Mth.abs(debug10) == debug9 && Mth.abs(debug11) == debug9);
/*     */         
/*  78 */         for (int debug13 = 0; debug13 < debug5; debug13++) {
/*  79 */           debug7.setWithOffset((Vec3i)debug4, debug10, debug13, debug11);
/*  80 */           if (isReplaceable(debug1, (BlockPos)debug7, true)) {
/*  81 */             if (debug3.planted) {
/*  82 */               if (!debug1.getBlockState(debug7.below()).isAir()) {
/*  83 */                 debug1.destroyBlock((BlockPos)debug7, true);
/*     */               }
/*     */               
/*  86 */               debug1.setBlock((BlockPos)debug7, debug8, 3);
/*     */             }
/*  88 */             else if (debug12) {
/*  89 */               if (debug2.nextFloat() < 0.1F) {
/*  90 */                 setBlock((LevelWriter)debug1, (BlockPos)debug7, debug8);
/*     */               }
/*     */             } else {
/*  93 */               setBlock((LevelWriter)debug1, (BlockPos)debug7, debug8);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeHat(LevelAccessor debug1, Random debug2, HugeFungusConfiguration debug3, BlockPos debug4, int debug5, boolean debug6) {
/* 103 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/* 104 */     boolean debug8 = debug3.hatState.is(Blocks.NETHER_WART_BLOCK);
/* 105 */     int debug9 = Math.min(debug2.nextInt(1 + debug5 / 3) + 5, debug5);
/* 106 */     int debug10 = debug5 - debug9;
/* 107 */     for (int debug11 = debug10; debug11 <= debug5; debug11++) {
/* 108 */       int debug12 = (debug11 < debug5 - debug2.nextInt(3)) ? 2 : 1;
/* 109 */       if (debug9 > 8 && debug11 < debug10 + 4) {
/* 110 */         debug12 = 3;
/*     */       }
/*     */       
/* 113 */       if (debug6) {
/* 114 */         debug12++;
/*     */       }
/*     */       
/* 117 */       for (int debug13 = -debug12; debug13 <= debug12; debug13++) {
/* 118 */         for (int debug14 = -debug12; debug14 <= debug12; debug14++) {
/* 119 */           boolean debug15 = (debug13 == -debug12 || debug13 == debug12);
/* 120 */           boolean debug16 = (debug14 == -debug12 || debug14 == debug12);
/* 121 */           boolean debug17 = (!debug15 && !debug16 && debug11 != debug5);
/* 122 */           boolean debug18 = (debug15 && debug16);
/* 123 */           boolean debug19 = (debug11 < debug10 + 3);
/*     */           
/* 125 */           debug7.setWithOffset((Vec3i)debug4, debug13, debug11, debug14);
/* 126 */           if (isReplaceable(debug1, (BlockPos)debug7, false)) {
/* 127 */             if (debug3.planted && !debug1.getBlockState(debug7.below()).isAir()) {
/* 128 */               debug1.destroyBlock((BlockPos)debug7, true);
/*     */             }
/*     */             
/* 131 */             if (debug19) {
/* 132 */               if (!debug17) {
/* 133 */                 placeHatDropBlock(debug1, debug2, (BlockPos)debug7, debug3.hatState, debug8);
/*     */               }
/* 135 */             } else if (debug17) {
/* 136 */               placeHatBlock(debug1, debug2, debug3, debug7, 0.1F, 0.2F, debug8 ? 0.1F : 0.0F);
/* 137 */             } else if (debug18) {
/* 138 */               placeHatBlock(debug1, debug2, debug3, debug7, 0.01F, 0.7F, debug8 ? 0.083F : 0.0F);
/*     */             } else {
/* 140 */               placeHatBlock(debug1, debug2, debug3, debug7, 5.0E-4F, 0.98F, debug8 ? 0.07F : 0.0F);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatBlock(LevelAccessor debug1, Random debug2, HugeFungusConfiguration debug3, BlockPos.MutableBlockPos debug4, float debug5, float debug6, float debug7) {
/* 149 */     if (debug2.nextFloat() < debug5) {
/* 150 */       setBlock((LevelWriter)debug1, (BlockPos)debug4, debug3.decorState);
/* 151 */     } else if (debug2.nextFloat() < debug6) {
/* 152 */       setBlock((LevelWriter)debug1, (BlockPos)debug4, debug3.hatState);
/* 153 */       if (debug2.nextFloat() < debug7) {
/* 154 */         tryPlaceWeepingVines((BlockPos)debug4, debug1, debug2);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeHatDropBlock(LevelAccessor debug1, Random debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 160 */     if (debug1.getBlockState(debug3.below()).is(debug4.getBlock())) {
/* 161 */       setBlock((LevelWriter)debug1, debug3, debug4);
/* 162 */     } else if (debug2.nextFloat() < 0.15D) {
/* 163 */       setBlock((LevelWriter)debug1, debug3, debug4);
/* 164 */       if (debug5 && debug2.nextInt(11) == 0) {
/* 165 */         tryPlaceWeepingVines(debug3, debug1, debug2);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void tryPlaceWeepingVines(BlockPos debug0, LevelAccessor debug1, Random debug2) {
/* 171 */     BlockPos.MutableBlockPos debug3 = debug0.mutable().move(Direction.DOWN);
/*     */     
/* 173 */     if (!debug1.isEmptyBlock((BlockPos)debug3)) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     int debug4 = Mth.nextInt(debug2, 1, 5);
/* 178 */     if (debug2.nextInt(7) == 0) {
/* 179 */       debug4 *= 2;
/*     */     }
/*     */     
/* 182 */     int debug5 = 23;
/* 183 */     int debug6 = 25;
/* 184 */     WeepingVinesFeature.placeWeepingVinesColumn(debug1, debug2, debug3, debug4, 23, 25);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */