/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChorusFlowerBlock;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class ChorusPlantFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public ChorusPlantFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 20 */     if (debug1.isEmptyBlock(debug4) && debug1.getBlockState(debug4.below()).is(Blocks.END_STONE)) {
/* 21 */       ChorusFlowerBlock.generatePlant((LevelAccessor)debug1, debug4, debug3, 8);
/* 22 */       return true;
/*    */     } 
/* 24 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ChorusPlantFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */