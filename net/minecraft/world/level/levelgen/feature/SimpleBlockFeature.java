/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*    */ 
/*    */ public class SimpleBlockFeature
/*    */   extends Feature<SimpleBlockConfiguration> {
/*    */   public SimpleBlockFeature(Codec<SimpleBlockConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, SimpleBlockConfiguration debug5) {
/* 19 */     if (debug5.placeOn.contains(debug1.getBlockState(debug4.below())) && debug5.placeIn.contains(debug1.getBlockState(debug4)) && debug5.placeUnder.contains(debug1.getBlockState(debug4.above()))) {
/* 20 */       debug1.setBlock(debug4, debug5.toPlace, 2);
/* 21 */       return true;
/*    */     } 
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SimpleBlockFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */