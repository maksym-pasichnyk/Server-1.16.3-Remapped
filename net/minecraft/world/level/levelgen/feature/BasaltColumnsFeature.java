/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ 
/*     */ public class BasaltColumnsFeature extends Feature<ColumnFeatureConfiguration> {
/*  19 */   private static final ImmutableList<Block> CANNOT_PLACE_ON = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasaltColumnsFeature(Codec<ColumnFeatureConfiguration> debug1) {
/*  33 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, ColumnFeatureConfiguration debug5) {
/*  38 */     int debug6 = debug2.getSeaLevel();
/*  39 */     if (!canPlaceAt((LevelAccessor)debug1, debug6, debug4.mutable())) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     int debug7 = debug5.height().sample(debug3);
/*     */     
/*  45 */     boolean debug8 = (debug3.nextFloat() < 0.9F);
/*  46 */     int debug9 = Math.min(debug7, debug8 ? 5 : 8);
/*  47 */     int debug10 = debug8 ? 50 : 15;
/*     */ 
/*     */     
/*  50 */     boolean debug11 = false;
/*  51 */     for (BlockPos debug13 : BlockPos.randomBetweenClosed(debug3, debug10, debug4.getX() - debug9, debug4.getY(), debug4.getZ() - debug9, debug4.getX() + debug9, debug4.getY(), debug4.getZ() + debug9)) {
/*  52 */       int debug14 = debug7 - debug13.distManhattan((Vec3i)debug4);
/*  53 */       if (debug14 >= 0) {
/*  54 */         debug11 |= placeColumn((LevelAccessor)debug1, debug6, debug13, debug14, debug5.reach().sample(debug3));
/*     */       }
/*     */     } 
/*     */     
/*  58 */     return debug11;
/*     */   }
/*     */   
/*     */   private boolean placeColumn(LevelAccessor debug1, int debug2, BlockPos debug3, int debug4, int debug5) {
/*  62 */     boolean debug6 = false;
/*     */     
/*  64 */     for (BlockPos debug8 : BlockPos.betweenClosed(debug3.getX() - debug5, debug3.getY(), debug3.getZ() - debug5, debug3.getX() + debug5, debug3.getY(), debug3.getZ() + debug5)) {
/*  65 */       int debug9 = debug8.distManhattan((Vec3i)debug3);
/*     */ 
/*     */ 
/*     */       
/*  69 */       BlockPos debug10 = isAirOrLavaOcean(debug1, debug2, debug8) ? findSurface(debug1, debug2, debug8.mutable(), debug9) : findAir(debug1, debug8.mutable(), debug9);
/*  70 */       if (debug10 == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  74 */       int debug11 = debug4 - debug9 / 2;
/*  75 */       BlockPos.MutableBlockPos debug12 = debug10.mutable();
/*  76 */       while (debug11 >= 0) {
/*  77 */         if (isAirOrLavaOcean(debug1, debug2, (BlockPos)debug12)) {
/*  78 */           setBlock((LevelWriter)debug1, (BlockPos)debug12, Blocks.BASALT.defaultBlockState());
/*  79 */           debug12.move(Direction.UP);
/*  80 */           debug6 = true;
/*  81 */         } else if (debug1.getBlockState((BlockPos)debug12).is(Blocks.BASALT)) {
/*  82 */           debug12.move(Direction.UP);
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */         
/*  87 */         debug11--;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     return debug6;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static BlockPos findSurface(LevelAccessor debug0, int debug1, BlockPos.MutableBlockPos debug2, int debug3) {
/*  96 */     while (debug2.getY() > 1 && debug3 > 0) {
/*  97 */       debug3--;
/*  98 */       if (canPlaceAt(debug0, debug1, debug2)) {
/*  99 */         return (BlockPos)debug2;
/*     */       }
/* 101 */       debug2.move(Direction.DOWN);
/*     */     } 
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceAt(LevelAccessor debug0, int debug1, BlockPos.MutableBlockPos debug2) {
/* 107 */     if (isAirOrLavaOcean(debug0, debug1, (BlockPos)debug2)) {
/* 108 */       BlockState debug3 = debug0.getBlockState((BlockPos)debug2.move(Direction.DOWN));
/* 109 */       debug2.move(Direction.UP);
/* 110 */       return (!debug3.isAir() && !CANNOT_PLACE_ON.contains(debug3.getBlock()));
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static BlockPos findAir(LevelAccessor debug0, BlockPos.MutableBlockPos debug1, int debug2) {
/* 117 */     while (debug1.getY() < debug0.getMaxBuildHeight() && debug2 > 0) {
/* 118 */       debug2--;
/*     */       
/* 120 */       BlockState debug3 = debug0.getBlockState((BlockPos)debug1);
/* 121 */       if (CANNOT_PLACE_ON.contains(debug3.getBlock())) {
/* 122 */         return null;
/*     */       }
/*     */       
/* 125 */       if (debug3.isAir()) {
/* 126 */         return (BlockPos)debug1;
/*     */       }
/*     */       
/* 129 */       debug1.move(Direction.UP);
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isAirOrLavaOcean(LevelAccessor debug0, int debug1, BlockPos debug2) {
/* 135 */     BlockState debug3 = debug0.getBlockState(debug2);
/* 136 */     return (debug3.isAir() || (debug3.is(Blocks.LAVA) && debug2.getY() <= debug1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BasaltColumnsFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */