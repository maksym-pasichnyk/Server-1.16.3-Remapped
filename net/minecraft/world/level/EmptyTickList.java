/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EmptyTickList<T> implements TickList<T> {
/*  6 */   private static final EmptyTickList<Object> INSTANCE = new EmptyTickList();
/*    */ 
/*    */   
/*    */   public static <T> EmptyTickList<T> empty() {
/* 10 */     return (EmptyTickList)INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos debug1, T debug2) {
/* 15 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void scheduleTick(BlockPos debug1, T debug2, int debug3) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void scheduleTick(BlockPos debug1, T debug2, int debug3, TickPriority debug4) {}
/*    */ 
/*    */   
/*    */   public boolean willTickThisTick(BlockPos debug1, T debug2) {
/* 28 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\EmptyTickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */