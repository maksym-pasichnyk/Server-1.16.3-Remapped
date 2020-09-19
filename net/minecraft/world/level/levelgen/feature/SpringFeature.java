/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*    */ 
/*    */ public class SpringFeature
/*    */   extends Feature<SpringConfiguration> {
/*    */   public SpringFeature(Codec<SpringConfiguration> debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, SpringConfiguration debug5) {
/* 20 */     if (!debug5.validBlocks.contains(debug1.getBlockState(debug4.above()).getBlock())) {
/* 21 */       return false;
/*    */     }
/* 23 */     if (debug5.requiresBlockBelow && !debug5.validBlocks.contains(debug1.getBlockState(debug4.below()).getBlock())) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     BlockState debug6 = debug1.getBlockState(debug4);
/* 28 */     if (!debug6.isAir() && !debug5.validBlocks.contains(debug6.getBlock())) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     int debug7 = 0;
/*    */     
/* 34 */     int debug8 = 0;
/* 35 */     if (debug5.validBlocks.contains(debug1.getBlockState(debug4.west()).getBlock())) {
/* 36 */       debug8++;
/*    */     }
/* 38 */     if (debug5.validBlocks.contains(debug1.getBlockState(debug4.east()).getBlock())) {
/* 39 */       debug8++;
/*    */     }
/* 41 */     if (debug5.validBlocks.contains(debug1.getBlockState(debug4.north()).getBlock())) {
/* 42 */       debug8++;
/*    */     }
/* 44 */     if (debug5.validBlocks.contains(debug1.getBlockState(debug4.south()).getBlock())) {
/* 45 */       debug8++;
/*    */     }
/* 47 */     if (debug5.validBlocks.contains(debug1.getBlockState(debug4.below()).getBlock())) {
/* 48 */       debug8++;
/*    */     }
/*    */     
/* 51 */     int debug9 = 0;
/* 52 */     if (debug1.isEmptyBlock(debug4.west())) {
/* 53 */       debug9++;
/*    */     }
/* 55 */     if (debug1.isEmptyBlock(debug4.east())) {
/* 56 */       debug9++;
/*    */     }
/* 58 */     if (debug1.isEmptyBlock(debug4.north())) {
/* 59 */       debug9++;
/*    */     }
/* 61 */     if (debug1.isEmptyBlock(debug4.south())) {
/* 62 */       debug9++;
/*    */     }
/* 64 */     if (debug1.isEmptyBlock(debug4.below())) {
/* 65 */       debug9++;
/*    */     }
/*    */     
/* 68 */     if (debug8 == debug5.rockCount && debug9 == debug5.holeCount) {
/* 69 */       debug1.setBlock(debug4, debug5.state.createLegacyBlock(), 2);
/* 70 */       debug1.getLiquidTicks().scheduleTick(debug4, debug5.state.getType(), 0);
/* 71 */       debug7++;
/*    */     } 
/*    */     
/* 74 */     return (debug7 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SpringFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */