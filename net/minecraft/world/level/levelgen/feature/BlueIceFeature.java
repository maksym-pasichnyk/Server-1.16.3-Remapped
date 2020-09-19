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
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class BlueIceFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/*    */   public BlueIceFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 23 */     if (debug4.getY() > debug1.getSeaLevel() - 1) {
/* 24 */       return false;
/*    */     }
/* 26 */     if (!debug1.getBlockState(debug4).is(Blocks.WATER) && !debug1.getBlockState(debug4.below()).is(Blocks.WATER)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     boolean debug6 = false;
/* 31 */     for (Direction debug10 : Direction.values()) {
/* 32 */       if (debug10 != Direction.DOWN)
/*    */       {
/*    */         
/* 35 */         if (debug1.getBlockState(debug4.relative(debug10)).is(Blocks.PACKED_ICE)) {
/* 36 */           debug6 = true;
/*    */           break;
/*    */         }  } 
/*    */     } 
/* 40 */     if (!debug6) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     debug1.setBlock(debug4, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */     
/* 46 */     for (int debug7 = 0; debug7 < 200; debug7++) {
/* 47 */       int debug8 = debug3.nextInt(5) - debug3.nextInt(6);
/* 48 */       int debug9 = 3;
/* 49 */       if (debug8 < 2) {
/* 50 */         debug9 += debug8 / 2;
/*    */       }
/* 52 */       if (debug9 >= 1) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         BlockPos debug10 = debug4.offset(debug3.nextInt(debug9) - debug3.nextInt(debug9), debug8, debug3.nextInt(debug9) - debug3.nextInt(debug9));
/* 57 */         BlockState debug11 = debug1.getBlockState(debug10);
/* 58 */         if (debug11.getMaterial() == Material.AIR || debug11.is(Blocks.WATER) || debug11.is(Blocks.PACKED_ICE) || debug11.is(Blocks.ICE))
/*    */         {
/*    */ 
/*    */           
/* 62 */           for (Direction debug15 : Direction.values()) {
/* 63 */             BlockState debug16 = debug1.getBlockState(debug10.relative(debug15));
/* 64 */             if (debug16.is(Blocks.BLUE_ICE)) {
/* 65 */               debug1.setBlock(debug10, Blocks.BLUE_ICE.defaultBlockState(), 2);
/*    */               break;
/*    */             } 
/*    */           }  } 
/*    */       } 
/*    */     } 
/* 71 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlueIceFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */