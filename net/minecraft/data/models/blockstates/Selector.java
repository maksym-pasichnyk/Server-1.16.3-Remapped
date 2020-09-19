/*    */ package net.minecraft.data.models.blockstates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class Selector
/*    */ {
/* 11 */   private static final Selector EMPTY = new Selector((List<Property.Value<?>>)ImmutableList.of()); private static final Comparator<Property.Value<?>> COMPARE_BY_NAME; static {
/* 12 */     COMPARE_BY_NAME = Comparator.comparing(debug0 -> debug0.getProperty().getName());
/*    */   }
/*    */   private final List<Property.Value<?>> values;
/*    */   
/*    */   public Selector extend(Property.Value<?> debug1) {
/* 17 */     return new Selector((List<Property.Value<?>>)ImmutableList.builder().addAll(this.values).add(debug1).build());
/*    */   }
/*    */   
/*    */   public Selector extend(Selector debug1) {
/* 21 */     return new Selector((List<Property.Value<?>>)ImmutableList.builder().addAll(this.values).addAll(debug1.values).build());
/*    */   }
/*    */   
/*    */   private Selector(List<Property.Value<?>> debug1) {
/* 25 */     this.values = debug1;
/*    */   }
/*    */   
/*    */   public static Selector empty() {
/* 29 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public static Selector of(Property.Value<?>... debug0) {
/* 33 */     return new Selector((List<Property.Value<?>>)ImmutableList.copyOf((Object[])debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 38 */     return (this == debug1 || (debug1 instanceof Selector && this.values.equals(((Selector)debug1).values)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 43 */     return this.values.hashCode();
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 47 */     return this.values.stream().sorted(COMPARE_BY_NAME).map(Property.Value::toString).collect(Collectors.joining(","));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getKey();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\Selector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */