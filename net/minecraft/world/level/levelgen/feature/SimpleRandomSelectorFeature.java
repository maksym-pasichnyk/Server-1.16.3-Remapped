/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*    */ 
/*    */ public class SimpleRandomSelectorFeature extends Feature<SimpleRandomFeatureConfiguration> {
/*    */   public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, SimpleRandomFeatureConfiguration debug5) {
/* 18 */     int debug6 = debug3.nextInt(debug5.features.size());
/* 19 */     ConfiguredFeature<?, ?> debug7 = ((Supplier<ConfiguredFeature<?, ?>>)debug5.features.get(debug6)).get();
/* 20 */     return debug7.place(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SimpleRandomSelectorFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */