/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class TickNextTickData<T>
/*    */ {
/*    */   private static long counter;
/*    */   private final T type;
/*    */   public final BlockPos pos;
/*    */   public final long triggerTick;
/*    */   public final TickPriority priority;
/* 13 */   private final long c = counter++;
/*    */   
/*    */   public TickNextTickData(BlockPos debug1, T debug2) {
/* 16 */     this(debug1, debug2, 0L, TickPriority.NORMAL);
/*    */   }
/*    */   
/*    */   public TickNextTickData(BlockPos debug1, T debug2, long debug3, TickPriority debug5) {
/* 20 */     this.pos = debug1.immutable();
/* 21 */     this.type = debug2;
/* 22 */     this.triggerTick = debug3;
/* 23 */     this.priority = debug5;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 40 */     if (debug1 instanceof TickNextTickData) {
/* 41 */       TickNextTickData<?> debug2 = (TickNextTickData)debug1;
/* 42 */       return (this.pos.equals(debug2.pos) && this.type == debug2.type);
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     return this.pos.hashCode();
/*    */   }
/*    */   
/*    */   public static <T> Comparator<TickNextTickData<T>> createTimeComparator() {
/* 53 */     return Comparator.comparingLong(debug0 -> debug0.triggerTick).thenComparing(debug0 -> debug0.priority).thenComparingLong(debug0 -> debug0.c);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return (new StringBuilder()).append(this.type).append(": ").append(this.pos).append(", ").append(this.triggerTick).append(", ").append(this.priority).append(", ").append(this.c).toString();
/*    */   }
/*    */   
/*    */   public T getType() {
/* 62 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\TickNextTickData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */