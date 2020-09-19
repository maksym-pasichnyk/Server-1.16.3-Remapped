/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class StateDefinition<O, S extends StateHolder<O, S>> {
/*  28 */   private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
/*     */   
/*     */   private final O owner;
/*     */   private final ImmutableSortedMap<String, Property<?>> propertiesByName;
/*     */   private final ImmutableList<S> states;
/*     */   
/*     */   protected StateDefinition(Function<O, S> debug1, O debug2, Factory<O, S> debug3, Map<String, Property<?>> debug4) {
/*  35 */     this.owner = debug2;
/*  36 */     this.propertiesByName = ImmutableSortedMap.copyOf(debug4);
/*     */     
/*  38 */     Supplier<S> debug5 = () -> (StateHolder)debug0.apply(debug1);
/*  39 */     MapCodec<S> debug6 = MapCodec.of(Encoder.empty(), Decoder.unit(debug5));
/*  40 */     for (UnmodifiableIterator<Map.Entry<String, Property<?>>> unmodifiableIterator = this.propertiesByName.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<String, Property<?>> entry = unmodifiableIterator.next();
/*  41 */       debug6 = appendPropertyCodec(debug6, debug5, entry.getKey(), (Property<Comparable>)entry.getValue()); }
/*     */ 
/*     */     
/*  44 */     MapCodec<S> debug7 = debug6;
/*     */ 
/*     */     
/*  47 */     Map<Map<Property<?>, Comparable<?>>, S> debug8 = Maps.newLinkedHashMap();
/*  48 */     List<S> debug9 = Lists.newArrayList();
/*     */     
/*  50 */     Stream<List<Pair<Property<?>, Comparable<?>>>> debug10 = Stream.of(Collections.emptyList());
/*  51 */     for (UnmodifiableIterator<Property> unmodifiableIterator1 = this.propertiesByName.values().iterator(); unmodifiableIterator1.hasNext(); ) { Property<?> debug12 = unmodifiableIterator1.next();
/*  52 */       debug10 = debug10.flatMap(debug1 -> debug0.getPossibleValues().stream().map(())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     debug10.forEach(debug5 -> {
/*     */           ImmutableMap<Property<?>, Comparable<?>> debug6 = (ImmutableMap<Property<?>, Comparable<?>>)debug5.stream().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
/*     */           
/*     */           StateHolder stateHolder = debug0.create(debug1, debug6, debug2);
/*     */           
/*     */           debug3.put(debug6, stateHolder);
/*     */           debug4.add(stateHolder);
/*     */         });
/*  67 */     for (StateHolder stateHolder : debug9) {
/*  68 */       stateHolder.populateNeighbours(debug8);
/*     */     }
/*     */     
/*  71 */     this.states = ImmutableList.copyOf(debug9);
/*     */   }
/*     */   
/*     */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> debug0, Supplier<S> debug1, String debug2, Property<T> debug3) {
/*  75 */     return Codec.mapPair(debug0, debug3.valueCodec().fieldOf(debug2).setPartial(() -> debug0.value(debug1.get()))).xmap(debug1 -> (StateHolder)((StateHolder)debug1.getFirst()).setValue(debug0, ((Property.Value)debug1.getSecond()).value()), debug1 -> Pair.of(debug1, debug0.value(debug1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<S> getPossibleStates() {
/*  82 */     return this.states;
/*     */   }
/*     */   
/*     */   public S any() {
/*  86 */     return (S)this.states.get(0);
/*     */   }
/*     */   
/*     */   public O getOwner() {
/*  90 */     return this.owner;
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/*  94 */     return (Collection<Property<?>>)this.propertiesByName.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return MoreObjects.toStringHelper(this)
/* 100 */       .add("block", this.owner)
/* 101 */       .add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList()))
/* 102 */       .toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Property<?> getProperty(String debug1) {
/* 107 */     return (Property)this.propertiesByName.get(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<O, S extends StateHolder<O, S>>
/*     */   {
/*     */     private final O owner;
/*     */     
/* 116 */     private final Map<String, Property<?>> properties = Maps.newHashMap();
/*     */     
/*     */     public Builder(O debug1) {
/* 119 */       this.owner = debug1;
/*     */     }
/*     */     
/*     */     public Builder<O, S> add(Property<?>... debug1) {
/* 123 */       for (Property<?> debug5 : debug1) {
/* 124 */         validateProperty(debug5);
/* 125 */         this.properties.put(debug5.getName(), debug5);
/*     */       } 
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> void validateProperty(Property<T> debug1) {
/* 131 */       String debug2 = debug1.getName();
/* 132 */       if (!StateDefinition.NAME_PATTERN.matcher(debug2).matches()) {
/* 133 */         throw new IllegalArgumentException((new StringBuilder()).append(this.owner).append(" has invalidly named property: ").append(debug2).toString());
/*     */       }
/*     */       
/* 136 */       Collection<T> debug3 = debug1.getPossibleValues();
/* 137 */       if (debug3.size() <= 1) {
/* 138 */         throw new IllegalArgumentException((new StringBuilder()).append(this.owner).append(" attempted use property ").append(debug2).append(" with <= 1 possible values").toString());
/*     */       }
/*     */       
/* 141 */       for (Comparable comparable : debug3) {
/* 142 */         String debug6 = debug1.getName(comparable);
/* 143 */         if (!StateDefinition.NAME_PATTERN.matcher(debug6).matches()) {
/* 144 */           throw new IllegalArgumentException((new StringBuilder()).append(this.owner).append(" has property: ").append(debug2).append(" with invalidly named value: ").append(debug6).toString());
/*     */         }
/*     */       } 
/*     */       
/* 148 */       if (this.properties.containsKey(debug2)) {
/* 149 */         throw new IllegalArgumentException((new StringBuilder()).append(this.owner).append(" has duplicate property: ").append(debug2).toString());
/*     */       }
/*     */     }
/*     */     
/*     */     public StateDefinition<O, S> create(Function<O, S> debug1, StateDefinition.Factory<O, S> debug2) {
/* 154 */       return new StateDefinition<>(debug1, this.owner, debug2, this.properties);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Factory<O, S> {
/*     */     S create(O param1O, ImmutableMap<Property<?>, Comparable<?>> param1ImmutableMap, MapCodec<S> param1MapCodec);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\StateDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */