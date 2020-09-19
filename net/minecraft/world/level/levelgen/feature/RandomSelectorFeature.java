/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*    */ 
/*    */ public class RandomSelectorFeature extends Feature<RandomFeatureConfiguration> {
/*    */   public RandomSelectorFeature(Codec<RandomFeatureConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, RandomFeatureConfiguration debug5) {
/* 18 */     for (WeightedConfiguredFeature debug7 : debug5.features) {
/* 19 */       if (debug3.nextFloat() < debug7.chance) {
/* 20 */         return debug7.place(debug1, debug2, debug3, debug4);
/*    */       }
/*    */     } 
/* 23 */     return ((ConfiguredFeature)debug5.defaultFeature.get()).place(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RandomSelectorFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */