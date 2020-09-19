/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelWriter
/*    */ {
/*    */   boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2);
/*    */   
/*    */   default boolean setBlock(BlockPos debug1, BlockState debug2, int debug3) {
/* 15 */     return setBlock(debug1, debug2, debug3, 512);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean);
/*    */   
/*    */   default boolean destroyBlock(BlockPos debug1, boolean debug2) {
/* 22 */     return destroyBlock(debug1, debug2, null);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean destroyBlock(BlockPos debug1, boolean debug2, @Nullable Entity debug3) {
/* 27 */     return destroyBlock(debug1, debug2, debug3, 512);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, @Nullable Entity paramEntity, int paramInt);
/*    */ 
/*    */   
/*    */   default boolean addFreshEntity(Entity debug1) {
/* 36 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */