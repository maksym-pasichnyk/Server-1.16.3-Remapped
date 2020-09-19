/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class NetherForestVegetationFeature extends Feature<BlockPileConfiguration> {
/*    */   public NetherForestVegetationFeature(Codec<BlockPileConfiguration> debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, BlockPileConfiguration debug5) {
/* 25 */     return place((LevelAccessor)debug1, debug3, debug4, debug5, 8, 4);
/*    */   }
/*    */   
/*    */   public static boolean place(LevelAccessor debug0, Random debug1, BlockPos debug2, BlockPileConfiguration debug3, int debug4, int debug5) {
/* 29 */     Block debug6 = debug0.getBlockState(debug2.below()).getBlock();
/*    */     
/* 31 */     if (!debug6.is((Tag)BlockTags.NYLIUM)) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     int debug7 = debug2.getY();
/*    */     
/* 37 */     if (debug7 < 1 || debug7 + 1 >= 256) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     int debug8 = 0;
/*    */     
/* 43 */     for (int debug9 = 0; debug9 < debug4 * debug4; debug9++) {
/* 44 */       BlockPos debug10 = debug2.offset(debug1.nextInt(debug4) - debug1.nextInt(debug4), debug1.nextInt(debug5) - debug1.nextInt(debug5), debug1.nextInt(debug4) - debug1.nextInt(debug4));
/* 45 */       BlockState debug11 = debug3.stateProvider.getState(debug1, debug10);
/* 46 */       if (debug0.isEmptyBlock(debug10) && debug10.getY() > 0 && 
/* 47 */         debug11.canSurvive((LevelReader)debug0, debug10)) {
/* 48 */         debug0.setBlock(debug10, debug11, 2);
/* 49 */         debug8++;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 54 */     return (debug8 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\NetherForestVegetationFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */