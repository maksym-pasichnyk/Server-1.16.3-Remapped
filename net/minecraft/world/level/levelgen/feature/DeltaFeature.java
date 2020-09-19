/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class DeltaFeature extends Feature<DeltaFeatureConfiguration> {
/* 18 */   private static final ImmutableList<Block> CANNOT_REPLACE = ImmutableList.of(Blocks.BEDROCK, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */ 
/*    */   
/*    */   public DeltaFeature(Codec<DeltaFeatureConfiguration> debug1) {
/* 29 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, DeltaFeatureConfiguration debug5) {
/* 34 */     boolean debug6 = false;
/* 35 */     boolean debug7 = (debug3.nextDouble() < 0.9D);
/* 36 */     int debug8 = debug7 ? debug5.rimSize().sample(debug3) : 0;
/* 37 */     int debug9 = debug7 ? debug5.rimSize().sample(debug3) : 0;
/* 38 */     boolean debug10 = (debug7 && debug8 != 0 && debug9 != 0);
/*    */     
/* 40 */     int debug11 = debug5.size().sample(debug3);
/* 41 */     int debug12 = debug5.size().sample(debug3);
/* 42 */     int debug13 = Math.max(debug11, debug12);
/* 43 */     for (BlockPos debug15 : BlockPos.withinManhattan(debug4, debug11, 0, debug12)) {
/* 44 */       if (debug15.distManhattan((Vec3i)debug4) > debug13) {
/*    */         break;
/*    */       }
/*    */       
/* 48 */       if (isClear((LevelAccessor)debug1, debug15, debug5)) {
/* 49 */         if (debug10) {
/* 50 */           debug6 = true;
/* 51 */           setBlock((LevelWriter)debug1, debug15, debug5.rim());
/*    */         } 
/*    */         
/* 54 */         BlockPos debug16 = debug15.offset(debug8, 0, debug9);
/* 55 */         if (isClear((LevelAccessor)debug1, debug16, debug5)) {
/* 56 */           debug6 = true;
/* 57 */           setBlock((LevelWriter)debug1, debug16, debug5.contents());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     return debug6;
/*    */   }
/*    */   
/*    */   private static boolean isClear(LevelAccessor debug0, BlockPos debug1, DeltaFeatureConfiguration debug2) {
/* 66 */     BlockState debug3 = debug0.getBlockState(debug1);
/* 67 */     if (debug3.is(debug2.contents().getBlock())) {
/* 68 */       return false;
/*    */     }
/*    */     
/* 71 */     if (CANNOT_REPLACE.contains(debug3.getBlock())) {
/* 72 */       return false;
/*    */     }
/*    */     
/* 75 */     for (Direction debug7 : DIRECTIONS) {
/* 76 */       boolean debug8 = debug0.getBlockState(debug1.relative(debug7)).isAir();
/* 77 */       if ((debug8 && debug7 != Direction.UP) || (!debug8 && debug7 == Direction.UP)) {
/* 78 */         return false;
/*    */       }
/*    */     } 
/* 81 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DeltaFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */