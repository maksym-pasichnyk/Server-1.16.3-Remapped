/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*    */ 
/*    */ public class ReplaceBlockFeature
/*    */   extends Feature<ReplaceBlockConfiguration> {
/*    */   public ReplaceBlockFeature(Codec<ReplaceBlockConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, ReplaceBlockConfiguration debug5) {
/* 19 */     if (debug1.getBlockState(debug4).is(debug5.target.getBlock())) {
/* 20 */       debug1.setBlock(debug4, debug5.state, 2);
/*    */     }
/* 22 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ReplaceBlockFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */