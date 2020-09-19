/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ public class TrappedChestBlockEntity extends ChestBlockEntity {
/*    */   public TrappedChestBlockEntity() {
/*  5 */     super(BlockEntityType.TRAPPED_CHEST);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void signalOpenCount() {
/* 10 */     super.signalOpenCount();
/* 11 */     this.level.updateNeighborsAt(this.worldPosition.below(), getBlockState().getBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\TrappedChestBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */