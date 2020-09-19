/*    */ package net.minecraft.world.level.block.grower;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public abstract class AbstractMegaTreeGrower
/*    */   extends AbstractTreeGrower {
/*    */   public boolean growTree(ServerLevel debug1, ChunkGenerator debug2, BlockPos debug3, BlockState debug4, Random debug5) {
/* 19 */     for (int debug6 = 0; debug6 >= -1; debug6--) {
/* 20 */       for (int debug7 = 0; debug7 >= -1; debug7--) {
/* 21 */         if (isTwoByTwoSapling(debug4, (BlockGetter)debug1, debug3, debug6, debug7)) {
/* 22 */           return placeMega(debug1, debug2, debug3, debug4, debug5, debug6, debug7);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 27 */     return super.growTree(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract ConfiguredFeature<TreeConfiguration, ?> getConfiguredMegaFeature(Random paramRandom);
/*    */   
/*    */   public boolean placeMega(ServerLevel debug1, ChunkGenerator debug2, BlockPos debug3, BlockState debug4, Random debug5, int debug6, int debug7) {
/* 34 */     ConfiguredFeature<TreeConfiguration, ?> debug8 = getConfiguredMegaFeature(debug5);
/*    */     
/* 36 */     if (debug8 == null) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     ((TreeConfiguration)debug8.config).setFromSapling();
/* 41 */     BlockState debug9 = Blocks.AIR.defaultBlockState();
/* 42 */     debug1.setBlock(debug3.offset(debug6, 0, debug7), debug9, 4);
/* 43 */     debug1.setBlock(debug3.offset(debug6 + 1, 0, debug7), debug9, 4);
/* 44 */     debug1.setBlock(debug3.offset(debug6, 0, debug7 + 1), debug9, 4);
/* 45 */     debug1.setBlock(debug3.offset(debug6 + 1, 0, debug7 + 1), debug9, 4);
/*    */     
/* 47 */     if (debug8.place((WorldGenLevel)debug1, debug2, debug5, debug3.offset(debug6, 0, debug7))) {
/* 48 */       return true;
/*    */     }
/* 50 */     debug1.setBlock(debug3.offset(debug6, 0, debug7), debug4, 4);
/* 51 */     debug1.setBlock(debug3.offset(debug6 + 1, 0, debug7), debug4, 4);
/* 52 */     debug1.setBlock(debug3.offset(debug6, 0, debug7 + 1), debug4, 4);
/* 53 */     debug1.setBlock(debug3.offset(debug6 + 1, 0, debug7 + 1), debug4, 4);
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isTwoByTwoSapling(BlockState debug0, BlockGetter debug1, BlockPos debug2, int debug3, int debug4) {
/* 58 */     Block debug5 = debug0.getBlock();
/* 59 */     return (debug5 == debug1.getBlockState(debug2.offset(debug3, 0, debug4)).getBlock() && debug5 == debug1
/* 60 */       .getBlockState(debug2.offset(debug3 + 1, 0, debug4)).getBlock() && debug5 == debug1
/* 61 */       .getBlockState(debug2.offset(debug3, 0, debug4 + 1)).getBlock() && debug5 == debug1
/* 62 */       .getBlockState(debug2.offset(debug3 + 1, 0, debug4 + 1)).getBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\grower\AbstractMegaTreeGrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */