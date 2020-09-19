/*   */ package net.minecraft.world.level;
/*   */ 
/*   */ import net.minecraft.core.BlockPos;
/*   */ 
/*   */ public interface TickList<T> {
/*   */   boolean hasScheduledTick(BlockPos paramBlockPos, T paramT);
/*   */   
/*   */   default void scheduleTick(BlockPos debug1, T debug2, int debug3) {
/* 9 */     scheduleTick(debug1, debug2, debug3, TickPriority.NORMAL);
/*   */   }
/*   */   
/*   */   void scheduleTick(BlockPos paramBlockPos, T paramT, int paramInt, TickPriority paramTickPriority);
/*   */   
/*   */   boolean willTickThisTick(BlockPos paramBlockPos, T paramT);
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\TickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */