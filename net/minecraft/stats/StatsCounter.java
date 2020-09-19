/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class StatsCounter {
/*  9 */   protected final Object2IntMap<Stat<?>> stats = Object2IntMaps.synchronize((Object2IntMap)new Object2IntOpenHashMap());
/*    */   
/*    */   public StatsCounter() {
/* 12 */     this.stats.defaultReturnValue(0);
/*    */   }
/*    */   
/*    */   public void increment(Player debug1, Stat<?> debug2, int debug3) {
/* 16 */     int debug4 = (int)Math.min(getValue(debug2) + debug3, 2147483647L);
/* 17 */     setValue(debug1, debug2, debug4);
/*    */   }
/*    */   
/*    */   public void setValue(Player debug1, Stat<?> debug2, int debug3) {
/* 21 */     this.stats.put(debug2, debug3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue(Stat<?> debug1) {
/* 29 */     return this.stats.getInt(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\StatsCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */