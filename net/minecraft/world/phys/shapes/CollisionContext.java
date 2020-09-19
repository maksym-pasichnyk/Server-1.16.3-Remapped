/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.material.FlowingFluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public interface CollisionContext {
/*    */   static CollisionContext empty() {
/* 11 */     return EntityCollisionContext.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity debug0) {
/* 16 */     return new EntityCollisionContext(debug0);
/*    */   }
/*    */   
/*    */   boolean isDescending();
/*    */   
/*    */   boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean);
/*    */   
/*    */   boolean isHoldingItem(Item paramItem);
/*    */   
/*    */   boolean canStandOnFluid(FluidState paramFluidState, FlowingFluid paramFlowingFluid);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\CollisionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */