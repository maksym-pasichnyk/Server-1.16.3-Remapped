/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.AbstractFlowerFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class GrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
/*    */   public GrassBlock(BlockBehaviour.Properties debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 21 */     return debug1.getBlockState(debug2.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 31 */     BlockPos debug5 = debug3.above();
/*    */     
/* 33 */     BlockState debug6 = Blocks.GRASS.defaultBlockState();
/*    */     
/*    */     int debug7;
/* 36 */     label32: for (debug7 = 0; debug7 < 128; debug7++) {
/* 37 */       BlockState debug10; BlockPos debug8 = debug5;
/* 38 */       for (int i = 0; i < debug7 / 16; ) {
/* 39 */         debug8 = debug8.offset(debug2.nextInt(3) - 1, (debug2.nextInt(3) - 1) * debug2.nextInt(3) / 2, debug2.nextInt(3) - 1);
/* 40 */         if (debug1.getBlockState(debug8.below()).is(this)) { if (debug1.getBlockState(debug8).isCollisionShapeFullBlock((BlockGetter)debug1, debug8))
/*    */             continue label32; 
/*    */           i++; }
/*    */         
/*    */         continue label32;
/*    */       } 
/* 46 */       BlockState debug9 = debug1.getBlockState(debug8);
/* 47 */       if (debug9.is(debug6.getBlock()) && debug2.nextInt(10) == 0) {
/* 48 */         ((BonemealableBlock)debug6.getBlock()).performBonemeal(debug1, debug2, debug8, debug9);
/*    */       }
/*    */       
/* 51 */       if (!debug9.isAir()) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 56 */       if (debug2.nextInt(8) == 0) {
/* 57 */         List<ConfiguredFeature<?, ?>> debug11 = debug1.getBiome(debug8).getGenerationSettings().getFlowerFeatures();
/* 58 */         if (debug11.isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 62 */         ConfiguredFeature<?, ?> debug12 = debug11.get(0);
/* 63 */         AbstractFlowerFeature debug13 = (AbstractFlowerFeature)debug12.feature;
/* 64 */         debug10 = debug13.getRandomFlower(debug2, debug8, debug12.config());
/*    */       } else {
/*    */         
/* 67 */         debug10 = debug6;
/*    */       } 
/*    */       
/* 70 */       if (debug10.canSurvive((LevelReader)debug1, debug8))
/* 71 */         debug1.setBlock(debug8, debug10, 3); 
/*    */       continue;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrassBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */