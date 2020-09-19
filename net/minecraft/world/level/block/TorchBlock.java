/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TorchBlock
/*    */   extends Block
/*    */ {
/* 19 */   protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);
/*    */   protected final ParticleOptions flameParticle;
/*    */   
/*    */   protected TorchBlock(BlockBehaviour.Properties debug1, ParticleOptions debug2) {
/* 23 */     super(debug1);
/* 24 */     this.flameParticle = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 29 */     return AABB;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 34 */     if (debug2 == Direction.DOWN && !canSurvive(debug1, (LevelReader)debug4, debug5)) {
/* 35 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 37 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 42 */     return canSupportCenter(debug2, debug3.below(), Direction.UP);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TorchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */