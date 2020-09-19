/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.data.worldgen.Features;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.NetherForestVegetationFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.TwistingVinesFeature;
/*    */ import net.minecraft.world.level.lighting.LayerLightEngine;
/*    */ 
/*    */ public class NyliumBlock extends Block implements BonemealableBlock {
/*    */   protected NyliumBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */   
/*    */   private static boolean canBeNylium(BlockState debug0, LevelReader debug1, BlockPos debug2) {
/* 24 */     BlockPos debug3 = debug2.above();
/* 25 */     BlockState debug4 = debug1.getBlockState(debug3);
/*    */ 
/*    */     
/* 28 */     int debug5 = LayerLightEngine.getLightBlockInto((BlockGetter)debug1, debug0, debug2, debug4, debug3, Direction.UP, debug4.getLightBlock((BlockGetter)debug1, debug3));
/* 29 */     return (debug5 < debug1.getMaxLightLevel());
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 34 */     if (!canBeNylium(debug1, (LevelReader)debug2, debug3)) {
/* 35 */       debug2.setBlockAndUpdate(debug3, Blocks.NETHERRACK.defaultBlockState());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 41 */     return debug1.getBlockState(debug2.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 51 */     BlockState debug5 = debug1.getBlockState(debug3);
/* 52 */     BlockPos debug6 = debug3.above();
/* 53 */     if (debug5.is(Blocks.CRIMSON_NYLIUM)) {
/* 54 */       NetherForestVegetationFeature.place((LevelAccessor)debug1, debug2, debug6, Features.Configs.CRIMSON_FOREST_CONFIG, 3, 1);
/* 55 */     } else if (debug5.is(Blocks.WARPED_NYLIUM)) {
/* 56 */       NetherForestVegetationFeature.place((LevelAccessor)debug1, debug2, debug6, Features.Configs.WARPED_FOREST_CONFIG, 3, 1);
/* 57 */       NetherForestVegetationFeature.place((LevelAccessor)debug1, debug2, debug6, Features.Configs.NETHER_SPROUTS_CONFIG, 3, 1);
/* 58 */       if (debug2.nextInt(8) == 0)
/* 59 */         TwistingVinesFeature.place((LevelAccessor)debug1, debug2, debug6, 3, 1, 2); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NyliumBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */