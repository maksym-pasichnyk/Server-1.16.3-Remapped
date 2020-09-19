/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatType<T>
/*    */   implements Iterable<Stat<T>>
/*    */ {
/*    */   private final Registry<T> registry;
/* 14 */   private final Map<T, Stat<T>> map = new IdentityHashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public StatType(Registry<T> debug1) {
/* 19 */     this.registry = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Stat<T> get(T debug1, StatFormatter debug2) {
/* 28 */     return this.map.computeIfAbsent(debug1, debug2 -> new Stat<>(this, (T)debug2, debug1));
/*    */   }
/*    */   
/*    */   public Registry<T> getRegistry() {
/* 32 */     return this.registry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<Stat<T>> iterator() {
/* 41 */     return this.map.values().iterator();
/*    */   }
/*    */   
/*    */   public Stat<T> get(T debug1) {
/* 45 */     return get(debug1, StatFormatter.DEFAULT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\StatType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */