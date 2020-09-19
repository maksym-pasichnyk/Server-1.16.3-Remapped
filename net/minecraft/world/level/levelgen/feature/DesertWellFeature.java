/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class DesertWellFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 17 */   private static final BlockStatePredicate IS_SAND = BlockStatePredicate.forBlock(Blocks.SAND);
/*    */   
/* 19 */   private final BlockState sandSlab = Blocks.SANDSTONE_SLAB.defaultBlockState();
/* 20 */   private final BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
/* 21 */   private final BlockState water = Blocks.WATER.defaultBlockState();
/*    */   
/*    */   public DesertWellFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 29 */     debug4 = debug4.above();
/*    */     
/* 31 */     while (debug1.isEmptyBlock(debug4) && debug4.getY() > 2) {
/* 32 */       debug4 = debug4.below();
/*    */     }
/*    */     
/* 35 */     if (!IS_SAND.test(debug1.getBlockState(debug4))) {
/* 36 */       return false;
/*    */     }
/*    */     
/*    */     int debug6;
/*    */     
/* 41 */     for (debug6 = -2; debug6 <= 2; debug6++) {
/* 42 */       for (int debug7 = -2; debug7 <= 2; debug7++) {
/* 43 */         if (debug1.isEmptyBlock(debug4.offset(debug6, -1, debug7)) && debug1.isEmptyBlock(debug4.offset(debug6, -2, debug7))) {
/* 44 */           return false;
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 50 */     for (debug6 = -1; debug6 <= 0; debug6++) {
/* 51 */       for (int debug7 = -2; debug7 <= 2; debug7++) {
/* 52 */         for (int debug8 = -2; debug8 <= 2; debug8++) {
/* 53 */           debug1.setBlock(debug4.offset(debug7, debug6, debug8), this.sandstone, 2);
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 59 */     debug1.setBlock(debug4, this.water, 2);
/* 60 */     for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/* 61 */       debug1.setBlock(debug4.relative(debug7), this.water, 2);
/*    */     }
/*    */ 
/*    */     
/* 65 */     for (debug6 = -2; debug6 <= 2; debug6++) {
/* 66 */       for (int debug7 = -2; debug7 <= 2; debug7++) {
/* 67 */         if (debug6 == -2 || debug6 == 2 || debug7 == -2 || debug7 == 2) {
/* 68 */           debug1.setBlock(debug4.offset(debug6, 1, debug7), this.sandstone, 2);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 73 */     debug1.setBlock(debug4.offset(2, 1, 0), this.sandSlab, 2);
/* 74 */     debug1.setBlock(debug4.offset(-2, 1, 0), this.sandSlab, 2);
/* 75 */     debug1.setBlock(debug4.offset(0, 1, 2), this.sandSlab, 2);
/* 76 */     debug1.setBlock(debug4.offset(0, 1, -2), this.sandSlab, 2);
/*    */ 
/*    */     
/* 79 */     for (debug6 = -1; debug6 <= 1; debug6++) {
/* 80 */       for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 81 */         if (debug6 == 0 && debug7 == 0) {
/* 82 */           debug1.setBlock(debug4.offset(debug6, 4, debug7), this.sandstone, 2);
/*    */         } else {
/* 84 */           debug1.setBlock(debug4.offset(debug6, 4, debug7), this.sandSlab, 2);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 90 */     for (debug6 = 1; debug6 <= 3; debug6++) {
/* 91 */       debug1.setBlock(debug4.offset(-1, debug6, -1), this.sandstone, 2);
/* 92 */       debug1.setBlock(debug4.offset(-1, debug6, 1), this.sandstone, 2);
/* 93 */       debug1.setBlock(debug4.offset(1, debug6, -1), this.sandstone, 2);
/* 94 */       debug1.setBlock(debug4.offset(1, debug6, 1), this.sandstone, 2);
/*    */     } 
/*    */     
/* 97 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DesertWellFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */