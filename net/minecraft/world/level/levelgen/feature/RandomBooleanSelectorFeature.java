/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*    */ 
/*    */ public class RandomBooleanSelectorFeature extends Feature<RandomBooleanFeatureConfiguration> {
/*    */   public RandomBooleanSelectorFeature(Codec<RandomBooleanFeatureConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, RandomBooleanFeatureConfiguration debug5) {
/* 18 */     boolean debug6 = debug3.nextBoolean();
/* 19 */     if (debug6) {
/* 20 */       return ((ConfiguredFeature)debug5.featureTrue.get()).place(debug1, debug2, debug3, debug4);
/*    */     }
/* 22 */     return ((ConfiguredFeature)debug5.featureFalse.get()).place(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomBooleanSelectorFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */