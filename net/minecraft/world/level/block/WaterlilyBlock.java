/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WaterlilyBlock extends BushBlock {
/* 17 */   protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.5D, 15.0D);
/*    */   
/*    */   protected WaterlilyBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 25 */     super.entityInside(debug1, debug2, debug3, debug4);
/*    */     
/* 27 */     if (debug2 instanceof net.minecraft.server.level.ServerLevel && debug4 instanceof net.minecraft.world.entity.vehicle.Boat) {
/* 28 */       debug2.destroyBlock(new BlockPos((Vec3i)debug3), true, debug4);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 34 */     return AABB;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 39 */     FluidState debug4 = debug2.getFluidState(debug3);
/* 40 */     FluidState debug5 = debug2.getFluidState(debug3.above());
/* 41 */     return ((debug4.getType() == Fluids.WATER || debug1.getMaterial() == Material.ICE) && debug5.getType() == Fluids.EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WaterlilyBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */