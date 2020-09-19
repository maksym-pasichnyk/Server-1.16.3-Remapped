/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*    */ 
/*    */ public class FillLayerFeature
/*    */   extends Feature<LayerConfiguration> {
/*    */   public FillLayerFeature(Codec<LayerConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, LayerConfiguration debug5) {
/* 19 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/*    */     
/* 21 */     for (int debug7 = 0; debug7 < 16; debug7++) {
/* 22 */       for (int debug8 = 0; debug8 < 16; debug8++) {
/* 23 */         int debug9 = debug4.getX() + debug7;
/* 24 */         int debug10 = debug4.getZ() + debug8;
/* 25 */         int debug11 = debug5.height;
/* 26 */         debug6.set(debug9, debug11, debug10);
/*    */         
/* 28 */         if (debug1.getBlockState((BlockPos)debug6).isAir()) {
/* 29 */           debug1.setBlock((BlockPos)debug6, debug5.state, 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FillLayerFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */