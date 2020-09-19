/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public enum SupportType
/*    */ {
/* 13 */   FULL
/*    */   {
/*    */     public boolean isSupporting(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 16 */       return Block.isFaceFull(debug1.getBlockSupportShape(debug2, debug3), debug4);
/*    */     }
/*    */   },
/* 19 */   CENTER {
/* 20 */     private final int CENTER_SUPPORT_WIDTH = 1;
/* 21 */     private final VoxelShape CENTER_SUPPORT_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D);
/*    */ 
/*    */     
/*    */     public boolean isSupporting(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 25 */       return !Shapes.joinIsNotEmpty(debug1.getBlockSupportShape(debug2, debug3).getFaceShape(debug4), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   },
/* 28 */   RIGID {
/* 29 */     private final int RIGID_SUPPORT_WIDTH = 2;
/* 30 */     private final VoxelShape RIGID_SUPPORT_SHAPE = Shapes.join(
/* 31 */         Shapes.block(), 
/* 32 */         Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), BooleanOp.ONLY_FIRST);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean isSupporting(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 38 */       return !Shapes.joinIsNotEmpty(debug1.getBlockSupportShape(debug2, debug3).getFaceShape(debug4), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean isSupporting(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SupportType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */