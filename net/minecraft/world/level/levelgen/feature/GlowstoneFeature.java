/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class GlowstoneFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/*    */   public GlowstoneFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 22 */     if (!debug1.isEmptyBlock(debug4)) {
/* 23 */       return false;
/*    */     }
/*    */     
/* 26 */     BlockState debug6 = debug1.getBlockState(debug4.above());
/* 27 */     if (!debug6.is(Blocks.NETHERRACK) && !debug6.is(Blocks.BASALT) && !debug6.is(Blocks.BLACKSTONE)) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     debug1.setBlock(debug4, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */     
/* 33 */     for (int debug7 = 0; debug7 < 1500; debug7++) {
/* 34 */       BlockPos debug8 = debug4.offset(debug3.nextInt(8) - debug3.nextInt(8), -debug3.nextInt(12), debug3.nextInt(8) - debug3.nextInt(8));
/* 35 */       if (debug1.getBlockState(debug8).isAir()) {
/*    */ 
/*    */ 
/*    */         
/* 39 */         int debug9 = 0;
/* 40 */         for (Direction debug13 : Direction.values()) {
/* 41 */           if (debug1.getBlockState(debug8.relative(debug13)).is(Blocks.GLOWSTONE)) {
/* 42 */             debug9++;
/*    */           }
/*    */           
/* 45 */           if (debug9 > 1) {
/*    */             break;
/*    */           }
/*    */         } 
/*    */         
/* 50 */         if (debug9 == 1) {
/* 51 */           debug1.setBlock(debug8, Blocks.GLOWSTONE.defaultBlockState(), 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\GlowstoneFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */