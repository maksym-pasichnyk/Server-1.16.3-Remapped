/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public final class NoiseColumn
/*    */   implements BlockGetter {
/*    */   private final BlockState[] column;
/*    */   
/*    */   public NoiseColumn(BlockState[] debug1) {
/* 15 */     this.column = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockEntity getBlockEntity(BlockPos debug1) {
/* 21 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos debug1) {
/* 26 */     int debug2 = debug1.getY();
/* 27 */     if (debug2 < 0 || debug2 >= this.column.length) {
/* 28 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 30 */     return this.column[debug2];
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockPos debug1) {
/* 35 */     return getBlockState(debug1).getFluidState();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\NoiseColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */