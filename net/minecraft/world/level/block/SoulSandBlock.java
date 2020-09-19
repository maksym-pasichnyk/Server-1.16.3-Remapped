/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SoulSandBlock extends Block {
/* 18 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
/*    */ 
/*    */   
/*    */   public SoulSandBlock(BlockBehaviour.Properties debug1) {
/* 22 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 27 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getBlockSupportShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 32 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getVisualShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 37 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 42 */     BubbleColumnBlock.growColumn((LevelAccessor)debug2, debug3.above(), false);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 47 */     if (debug2 == Direction.UP && debug3.is(Blocks.WATER)) {
/* 48 */       debug4.getBlockTicks().scheduleTick(debug5, this, 20);
/*    */     }
/*    */     
/* 51 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 56 */     debug2.getBlockTicks().scheduleTick(debug3, this, 20);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SoulSandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */