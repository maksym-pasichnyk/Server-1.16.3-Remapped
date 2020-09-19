/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class Stat<T>
/*    */   extends ObjectiveCriteria {
/*    */   private final StatFormatter formatter;
/*    */   private final T value;
/*    */   private final StatType<T> type;
/*    */   
/*    */   protected Stat(StatType<T> debug1, T debug2, StatFormatter debug3) {
/* 16 */     super(buildName(debug1, debug2));
/* 17 */     this.type = debug1;
/* 18 */     this.formatter = debug3;
/* 19 */     this.value = debug2;
/*    */   }
/*    */   
/*    */   public static <T> String buildName(StatType<T> debug0, T debug1) {
/* 23 */     return locationToKey(Registry.STAT_TYPE.getKey(debug0)) + ":" + locationToKey(debug0.getRegistry().getKey(debug1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> String locationToKey(@Nullable ResourceLocation debug0) {
/* 29 */     return debug0.toString().replace(':', '.');
/*    */   }
/*    */   
/*    */   public StatType<T> getType() {
/* 33 */     return this.type;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 37 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 46 */     return (this == debug1 || (debug1 instanceof Stat && Objects.equals(getName(), ((Stat)debug1).getName())));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return getName().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "Stat{name=" + 
/* 57 */       getName() + ", formatter=" + this.formatter + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\Stat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */