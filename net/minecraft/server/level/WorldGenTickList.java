/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.TickList;
/*    */ import net.minecraft.world.level.TickPriority;
/*    */ 
/*    */ public class WorldGenTickList<T>
/*    */   implements TickList<T> {
/*    */   private final Function<BlockPos, TickList<T>> index;
/*    */   
/*    */   public WorldGenTickList(Function<BlockPos, TickList<T>> debug1) {
/* 13 */     this.index = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos debug1, T debug2) {
/* 18 */     return ((TickList)this.index.apply(debug1)).hasScheduledTick(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void scheduleTick(BlockPos debug1, T debug2, int debug3, TickPriority debug4) {
/* 23 */     ((TickList)this.index.apply(debug1)).scheduleTick(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean willTickThisTick(BlockPos debug1, T debug2) {
/* 28 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\WorldGenTickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */