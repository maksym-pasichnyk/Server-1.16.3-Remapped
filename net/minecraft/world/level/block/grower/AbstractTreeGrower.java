/*    */ package net.minecraft.world.level.block.grower;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*    */ 
/*    */ public abstract class AbstractTreeGrower {
/*    */   @Nullable
/*    */   protected abstract ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random paramRandom, boolean paramBoolean);
/*    */   
/*    */   public boolean growTree(ServerLevel debug1, ChunkGenerator debug2, BlockPos debug3, BlockState debug4, Random debug5) {
/* 22 */     ConfiguredFeature<TreeConfiguration, ?> debug6 = getConfiguredFeature(debug5, hasFlowers((LevelAccessor)debug1, debug3));
/* 23 */     if (debug6 == null) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     debug1.setBlock(debug3, Blocks.AIR.defaultBlockState(), 4);
/*    */     
/* 29 */     ((TreeConfiguration)debug6.config).setFromSapling();
/* 30 */     if (debug6.place((WorldGenLevel)debug1, debug2, debug5, debug3)) {
/* 31 */       return true;
/*    */     }
/* 33 */     debug1.setBlock(debug3, debug4, 4);
/* 34 */     return false;
/*    */   }
/*    */   
/*    */   private boolean hasFlowers(LevelAccessor debug1, BlockPos debug2) {
/* 38 */     for (BlockPos debug4 : BlockPos.MutableBlockPos.betweenClosed(debug2.below().north(2).west(2), debug2.above().south(2).east(2))) {
/* 39 */       if (debug1.getBlockState(debug4).is((Tag)BlockTags.FLOWERS)) {
/* 40 */         return true;
/*    */       }
/*    */     } 
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\grower\AbstractTreeGrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */