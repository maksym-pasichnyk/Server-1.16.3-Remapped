/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class ChunkTickList<T> implements TickList<T> {
/*    */   private final List<ScheduledTick<T>> ticks;
/*    */   private final Function<T, ResourceLocation> toId;
/*    */   
/*    */   static class ScheduledTick<T> {
/*    */     private final T type;
/*    */     public final BlockPos pos;
/*    */     
/*    */     private ScheduledTick(T debug1, BlockPos debug2, int debug3, TickPriority debug4) {
/* 21 */       this.type = debug1;
/* 22 */       this.pos = debug2;
/* 23 */       this.delay = debug3;
/* 24 */       this.priority = debug4;
/*    */     }
/*    */     public final int delay; public final TickPriority priority;
/*    */     
/*    */     public String toString() {
/* 29 */       return (new StringBuilder()).append(this.type).append(": ").append(this.pos).append(", ").append(this.delay).append(", ").append(this.priority).toString();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ChunkTickList(Function<T, ResourceLocation> debug1, List<TickNextTickData<T>> debug2, long debug3) {
/* 37 */     this(debug1, (List<ScheduledTick<T>>)debug2.stream().map(debug2 -> new ScheduledTick(debug2.getType(), debug2.pos, (int)(debug2.triggerTick - debug0), debug2.priority)).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   private ChunkTickList(Function<T, ResourceLocation> debug1, List<ScheduledTick<T>> debug2) {
/* 41 */     this.ticks = debug2;
/* 42 */     this.toId = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos debug1, T debug2) {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scheduleTick(BlockPos debug1, T debug2, int debug3, TickPriority debug4) {
/* 52 */     this.ticks.add(new ScheduledTick<>(debug2, debug1, debug3, debug4));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean willTickThisTick(BlockPos debug1, T debug2) {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public ListTag save() {
/* 61 */     ListTag debug1 = new ListTag();
/* 62 */     for (ScheduledTick<T> debug3 : this.ticks) {
/* 63 */       CompoundTag debug4 = new CompoundTag();
/* 64 */       debug4.putString("i", ((ResourceLocation)this.toId.apply(debug3.type)).toString());
/* 65 */       debug4.putInt("x", debug3.pos.getX());
/* 66 */       debug4.putInt("y", debug3.pos.getY());
/* 67 */       debug4.putInt("z", debug3.pos.getZ());
/* 68 */       debug4.putInt("t", debug3.delay);
/* 69 */       debug4.putInt("p", debug3.priority.getValue());
/*    */       
/* 71 */       debug1.add(debug4);
/*    */     } 
/*    */     
/* 74 */     return debug1;
/*    */   }
/*    */   
/*    */   public static <T> ChunkTickList<T> create(ListTag debug0, Function<T, ResourceLocation> debug1, Function<ResourceLocation, T> debug2) {
/* 78 */     List<ScheduledTick<T>> debug3 = Lists.newArrayList();
/* 79 */     for (int debug4 = 0; debug4 < debug0.size(); debug4++) {
/* 80 */       CompoundTag debug5 = debug0.getCompound(debug4);
/*    */       
/* 82 */       T debug6 = debug2.apply(new ResourceLocation(debug5.getString("i")));
/* 83 */       if (debug6 != null) {
/* 84 */         BlockPos debug7 = new BlockPos(debug5.getInt("x"), debug5.getInt("y"), debug5.getInt("z"));
/* 85 */         debug3.add(new ScheduledTick<>(debug6, debug7, debug5.getInt("t"), TickPriority.byValue(debug5.getInt("p"))));
/*    */       } 
/*    */     } 
/* 88 */     return new ChunkTickList<>(debug1, debug3);
/*    */   }
/*    */   
/*    */   public void copyOut(TickList<T> debug1) {
/* 92 */     this.ticks.forEach(debug1 -> debug0.scheduleTick(debug1.pos, debug1.type, debug1.delay, debug1.priority));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ChunkTickList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */