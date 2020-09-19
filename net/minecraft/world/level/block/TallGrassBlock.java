/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallGrassBlock extends BushBlock implements BonemealableBlock {
/* 15 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
/*    */   
/*    */   protected TallGrassBlock(BlockBehaviour.Properties debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 23 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 38 */     DoublePlantBlock debug5 = (this == Blocks.FERN) ? (DoublePlantBlock)Blocks.LARGE_FERN : (DoublePlantBlock)Blocks.TALL_GRASS;
/*    */     
/* 40 */     if (debug5.defaultBlockState().canSurvive((LevelReader)debug1, debug3) && debug1.isEmptyBlock(debug3.above())) {
/* 41 */       debug5.placeAt((LevelAccessor)debug1, debug3, 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBehaviour.OffsetType getOffsetType() {
/* 47 */     return BlockBehaviour.OffsetType.XYZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TallGrassBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */