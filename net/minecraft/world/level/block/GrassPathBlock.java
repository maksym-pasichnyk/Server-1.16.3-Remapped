/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class GrassPathBlock extends Block {
/* 18 */   protected static final VoxelShape SHAPE = FarmBlock.SHAPE;
/*    */   
/*    */   protected GrassPathBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/* 26 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 31 */     if (!defaultBlockState().canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/* 32 */       return Block.pushEntitiesUp(defaultBlockState(), Blocks.DIRT.defaultBlockState(), debug1.getLevel(), debug1.getClickedPos());
/*    */     }
/* 34 */     return super.getStateForPlacement(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 39 */     if (debug2 == Direction.UP && 
/* 40 */       !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 41 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*    */     }
/*    */     
/* 44 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 49 */     FarmBlock.turnToDirt(debug1, (Level)debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 54 */     BlockState debug4 = debug2.getBlockState(debug3.above());
/* 55 */     return (!debug4.getMaterial().isSolid() || debug4.getBlock() instanceof FenceGateBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 60 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 65 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrassPathBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */