/*     */ package net.minecraft.data.models.blockstates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class PropertyDispatch
/*     */ {
/*  17 */   private final Map<Selector, List<Variant>> values = Maps.newHashMap();
/*     */   
/*     */   protected void putValue(Selector debug1, List<Variant> debug2) {
/*  20 */     List<Variant> debug3 = this.values.put(debug1, debug2);
/*  21 */     if (debug3 != null) {
/*  22 */       throw new IllegalStateException("Value " + debug1 + " is already defined");
/*     */     }
/*     */   }
/*     */   
/*     */   Map<Selector, List<Variant>> getEntries() {
/*  27 */     verifyComplete();
/*  28 */     return (Map<Selector, List<Variant>>)ImmutableMap.copyOf(this.values);
/*     */   }
/*     */   
/*     */   private void verifyComplete() {
/*  32 */     List<Property<?>> debug1 = getDefinedProperties();
/*  33 */     Stream<Selector> debug2 = Stream.of(Selector.empty());
/*  34 */     for (Property<?> debug4 : debug1) {
/*  35 */       debug2 = debug2.flatMap(debug1 -> debug0.getAllValues().map(debug1::extend));
/*     */     }
/*  37 */     List<Selector> debug3 = (List<Selector>)debug2.filter(debug1 -> !this.values.containsKey(debug1)).collect(Collectors.toList());
/*  38 */     if (!debug3.isEmpty()) {
/*  39 */       throw new IllegalStateException("Missing definition for properties: " + debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T1 extends Comparable<T1>> C1<T1> property(Property<T1> debug0) {
/*  46 */     return new C1<>(debug0);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<T1, T2> properties(Property<T1> debug0, Property<T2> debug1) {
/*  50 */     return new C2<>(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<T1, T2, T3> properties(Property<T1> debug0, Property<T2> debug1, Property<T3> debug2) {
/*  54 */     return new C3<>(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<T1, T2, T3, T4> properties(Property<T1> debug0, Property<T2> debug1, Property<T3> debug2, Property<T4> debug3) {
/*  58 */     return new C4<>(debug0, debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<T1, T2, T3, T4, T5> properties(Property<T1> debug0, Property<T2> debug1, Property<T3> debug2, Property<T4> debug3, Property<T5> debug4) {
/*  62 */     return new C5<>(debug0, debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   abstract List<Property<?>> getDefinedProperties();
/*     */   
/*     */   public static class C1<T1 extends Comparable<T1>> extends PropertyDispatch {
/*     */     private C1(Property<T1> debug1) {
/*  69 */       this.property1 = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/*  74 */       return (List<Property<?>>)ImmutableList.of(this.property1);
/*     */     }
/*     */     private final Property<T1> property1;
/*     */     public C1<T1> select(T1 debug1, List<Variant> debug2) {
/*  78 */       Selector debug3 = Selector.of((Property.Value<?>[])new Property.Value[] { this.property1
/*  79 */             .value((Comparable)debug1) });
/*     */       
/*  81 */       putValue(debug3, debug2);
/*  82 */       return this;
/*     */     }
/*     */     
/*     */     public C1<T1> select(T1 debug1, Variant debug2) {
/*  86 */       return select(debug1, Collections.singletonList(debug2));
/*     */     }
/*     */     
/*     */     public PropertyDispatch generate(Function<T1, Variant> debug1) {
/*  90 */       this.property1.getPossibleValues().forEach(debug2 -> select((T1)debug2, debug1.apply(debug2)));
/*     */ 
/*     */       
/*  93 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class C2<T1 extends Comparable<T1>, T2 extends Comparable<T2>>
/*     */     extends PropertyDispatch
/*     */   {
/*     */     private final Property<T1> property1;
/*     */ 
/*     */     
/*     */     private final Property<T2> property2;
/*     */ 
/*     */     
/*     */     private C2(Property<T1> debug1, Property<T2> debug2) {
/* 109 */       this.property1 = debug1;
/* 110 */       this.property2 = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 115 */       return (List<Property<?>>)ImmutableList.of(this.property1, this.property2);
/*     */     }
/*     */     
/*     */     public C2<T1, T2> select(T1 debug1, T2 debug2, List<Variant> debug3) {
/* 119 */       Selector debug4 = Selector.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 120 */             .value((Comparable)debug1), this.property2
/* 121 */             .value((Comparable)debug2) });
/*     */       
/* 123 */       putValue(debug4, debug3);
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public C2<T1, T2> select(T1 debug1, T2 debug2, Variant debug3) {
/* 128 */       return select(debug1, debug2, Collections.singletonList(debug3));
/*     */     }
/*     */     
/*     */     public PropertyDispatch generate(BiFunction<T1, T2, Variant> debug1) {
/* 132 */       this.property1.getPossibleValues().forEach(debug2 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch generateList(BiFunction<T1, T2, List<Variant>> debug1) {
/* 141 */       this.property1.getPossibleValues().forEach(debug2 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C3<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch {
/*     */     private final Property<T1> property1;
/*     */     private final Property<T2> property2;
/*     */     private final Property<T3> property3;
/*     */     
/*     */     private C3(Property<T1> debug1, Property<T2> debug2, Property<T3> debug3) {
/* 156 */       this.property1 = debug1;
/* 157 */       this.property2 = debug2;
/* 158 */       this.property3 = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 163 */       return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3);
/*     */     }
/*     */     
/*     */     public C3<T1, T2, T3> select(T1 debug1, T2 debug2, T3 debug3, List<Variant> debug4) {
/* 167 */       Selector debug5 = Selector.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 168 */             .value((Comparable)debug1), this.property2
/* 169 */             .value((Comparable)debug2), this.property3
/* 170 */             .value((Comparable)debug3) });
/*     */       
/* 172 */       putValue(debug5, debug4);
/* 173 */       return this;
/*     */     }
/*     */     
/*     */     public C3<T1, T2, T3> select(T1 debug1, T2 debug2, T3 debug3, Variant debug4) {
/* 177 */       return select(debug1, debug2, debug3, Collections.singletonList(debug4));
/*     */     }
/*     */     
/*     */     public PropertyDispatch generate(PropertyDispatch.TriFunction<T1, T2, T3, Variant> debug1) {
/* 181 */       this.property1.getPossibleValues().forEach(debug2 -> this.property2.getPossibleValues().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class C4<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>>
/*     */     extends PropertyDispatch
/*     */   {
/*     */     private final Property<T1> property1;
/*     */ 
/*     */     
/*     */     private final Property<T2> property2;
/*     */ 
/*     */     
/*     */     private final Property<T3> property3;
/*     */ 
/*     */     
/*     */     private final Property<T4> property4;
/*     */ 
/*     */     
/*     */     private C4(Property<T1> debug1, Property<T2> debug2, Property<T3> debug3, Property<T4> debug4) {
/* 210 */       this.property1 = debug1;
/* 211 */       this.property2 = debug2;
/* 212 */       this.property3 = debug3;
/* 213 */       this.property4 = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 218 */       return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3, this.property4);
/*     */     }
/*     */     
/*     */     public C4<T1, T2, T3, T4> select(T1 debug1, T2 debug2, T3 debug3, T4 debug4, List<Variant> debug5) {
/* 222 */       Selector debug6 = Selector.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 223 */             .value((Comparable)debug1), this.property2
/* 224 */             .value((Comparable)debug2), this.property3
/* 225 */             .value((Comparable)debug3), this.property4
/* 226 */             .value((Comparable)debug4) });
/*     */       
/* 228 */       putValue(debug6, debug5);
/* 229 */       return this;
/*     */     }
/*     */     
/*     */     public C4<T1, T2, T3, T4> select(T1 debug1, T2 debug2, T3 debug3, T4 debug4, Variant debug5) {
/* 233 */       return select(debug1, debug2, debug3, debug4, Collections.singletonList(debug5));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class C5<T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>>
/*     */     extends PropertyDispatch
/*     */   {
/*     */     private final Property<T1> property1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Property<T2> property2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Property<T3> property3;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Property<T4> property4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Property<T5> property5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private C5(Property<T1> debug1, Property<T2> debug2, Property<T3> debug3, Property<T4> debug4, Property<T5> debug5) {
/* 271 */       this.property1 = debug1;
/* 272 */       this.property2 = debug2;
/* 273 */       this.property3 = debug3;
/* 274 */       this.property4 = debug4;
/* 275 */       this.property5 = debug5;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 280 */       return (List<Property<?>>)ImmutableList.of(this.property1, this.property2, this.property3, this.property4, this.property5);
/*     */     }
/*     */     
/*     */     public C5<T1, T2, T3, T4, T5> select(T1 debug1, T2 debug2, T3 debug3, T4 debug4, T5 debug5, List<Variant> debug6) {
/* 284 */       Selector debug7 = Selector.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 285 */             .value((Comparable)debug1), this.property2
/* 286 */             .value((Comparable)debug2), this.property3
/* 287 */             .value((Comparable)debug3), this.property4
/* 288 */             .value((Comparable)debug4), this.property5
/* 289 */             .value((Comparable)debug5) });
/*     */       
/* 291 */       putValue(debug7, debug6);
/* 292 */       return this;
/*     */     }
/*     */     
/*     */     public C5<T1, T2, T3, T4, T5> select(T1 debug1, T2 debug2, T3 debug3, T4 debug4, T5 debug5, Variant debug6) {
/* 296 */       return select(debug1, debug2, debug3, debug4, debug5, Collections.singletonList(debug6));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TriFunction<P1, P2, P3, R> {
/*     */     R apply(P1 param1P1, P2 param1P2, P3 param1P3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\PropertyDispatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */