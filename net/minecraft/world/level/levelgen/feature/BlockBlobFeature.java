/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class BlockBlobFeature extends Feature<BlockStateConfiguration> {
/*    */   public BlockBlobFeature(Codec<BlockStateConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, BlockStateConfiguration debug5) {
/* 19 */     while (debug4.getY() > 3) {
/* 20 */       if (!debug1.isEmptyBlock(debug4.below())) {
/* 21 */         Block block = debug1.getBlockState(debug4.below()).getBlock();
/* 22 */         if (isDirt(block) || isStone(block)) {
/*    */           break;
/*    */         }
/*    */       } 
/* 26 */       debug4 = debug4.below();
/*    */     } 
/* 28 */     if (debug4.getY() <= 3) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     int debug6 = 0;
/* 33 */     while (debug6 < 3) {
/* 34 */       int debug7 = debug3.nextInt(2);
/* 35 */       int debug8 = debug3.nextInt(2);
/* 36 */       int debug9 = debug3.nextInt(2);
/* 37 */       float debug10 = (debug7 + debug8 + debug9) * 0.333F + 0.5F;
/*    */       
/* 39 */       for (BlockPos debug12 : BlockPos.betweenClosed(debug4.offset(-debug7, -debug8, -debug9), debug4.offset(debug7, debug8, debug9))) {
/* 40 */         if (debug12.distSqr((Vec3i)debug4) <= (debug10 * debug10)) {
/* 41 */           debug1.setBlock(debug12, debug5.state, 4);
/*    */         }
/*    */       } 
/*    */       
/* 45 */       debug4 = debug4.offset(-1 + debug3.nextInt(2), -debug3.nextInt(2), -1 + debug3.nextInt(2));
/* 46 */       debug6++;
/*    */     } 
/*    */     
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlockBlobFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */