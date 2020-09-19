/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public enum EmptyBlockGetter
/*    */   implements BlockGetter {
/* 13 */   INSTANCE;
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockEntity getBlockEntity(BlockPos debug1) {
/* 18 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos debug1) {
/* 23 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos debug1) {
/* 28 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\EmptyBlockGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */