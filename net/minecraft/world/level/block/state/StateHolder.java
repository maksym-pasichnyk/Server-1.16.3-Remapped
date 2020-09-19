/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.collect.ArrayTable;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class StateHolder<O, S> {
/*  22 */   private static final Function<Map.Entry<Property<?>, Comparable<?>>, String> PROPERTY_ENTRY_TO_STRING_FUNCTION = new Function<Map.Entry<Property<?>, Comparable<?>>, String>()
/*     */     {
/*     */       public String apply(@Nullable Map.Entry<Property<?>, Comparable<?>> debug1) {
/*  25 */         if (debug1 == null) {
/*  26 */           return "<NULL>";
/*     */         }
/*     */         
/*  29 */         Property<?> debug2 = debug1.getKey();
/*  30 */         return debug2.getName() + "=" + getName(debug2, debug1.getValue());
/*     */       }
/*     */ 
/*     */       
/*     */       private <T extends Comparable<T>> String getName(Property<T> debug1, Comparable<?> debug2) {
/*  35 */         return debug1.getName(debug2);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected final O owner;
/*     */   private final ImmutableMap<Property<?>, Comparable<?>> values;
/*     */   private Table<Property<?>, Comparable<?>, S> neighbours;
/*     */   protected final MapCodec<S> propertiesCodec;
/*     */   
/*     */   protected StateHolder(O debug1, ImmutableMap<Property<?>, Comparable<?>> debug2, MapCodec<S> debug3) {
/*  46 */     this.owner = debug1;
/*  47 */     this.values = debug2;
/*  48 */     this.propertiesCodec = debug3;
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> S cycle(Property<T> debug1) {
/*  52 */     return setValue(debug1, findNextInCollection(debug1.getPossibleValues(), getValue(debug1)));
/*     */   }
/*     */   
/*     */   protected static <T> T findNextInCollection(Collection<T> debug0, T debug1) {
/*  56 */     Iterator<T> debug2 = debug0.iterator();
/*     */     
/*  58 */     while (debug2.hasNext()) {
/*  59 */       if (debug2.next().equals(debug1)) {
/*  60 */         if (debug2.hasNext()) {
/*  61 */           return debug2.next();
/*     */         }
/*  63 */         return debug0.iterator().next();
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return debug2.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     StringBuilder debug1 = new StringBuilder();
/*  73 */     debug1.append(this.owner);
/*     */     
/*  75 */     if (!getValues().isEmpty()) {
/*  76 */       debug1.append('[');
/*  77 */       debug1.append(getValues().entrySet().stream().<CharSequence>map((Function)PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
/*  78 */       debug1.append(']');
/*     */     } 
/*     */     
/*  81 */     return debug1.toString();
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/*  85 */     return Collections.unmodifiableCollection((Collection<? extends Property<?>>)this.values.keySet());
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> boolean hasProperty(Property<T> debug1) {
/*  89 */     return this.values.containsKey(debug1);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> T getValue(Property<T> debug1) {
/*  93 */     Comparable<?> debug2 = (Comparable)this.values.get(debug1);
/*  94 */     if (debug2 == null) {
/*  95 */       throw new IllegalArgumentException("Cannot get property " + debug1 + " as it does not exist in " + this.owner);
/*     */     }
/*     */     
/*  98 */     return (T)debug1.getValueClass().cast(debug2);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> debug1) {
/* 102 */     Comparable<?> debug2 = (Comparable)this.values.get(debug1);
/* 103 */     if (debug2 == null) {
/* 104 */       return Optional.empty();
/*     */     }
/*     */     
/* 107 */     return Optional.of(debug1.getValueClass().cast(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S setValue(Property<T> debug1, V debug2) {
/* 112 */     Comparable<?> debug3 = (Comparable)this.values.get(debug1);
/* 113 */     if (debug3 == null) {
/* 114 */       throw new IllegalArgumentException("Cannot set property " + debug1 + " as it does not exist in " + this.owner);
/*     */     }
/* 116 */     if (debug3 == debug2) {
/* 117 */       return (S)this;
/*     */     }
/*     */     
/* 120 */     S debug4 = (S)this.neighbours.get(debug1, debug2);
/* 121 */     if (debug4 == null) {
/* 122 */       throw new IllegalArgumentException("Cannot set property " + debug1 + " to " + debug2 + " on " + this.owner + ", it is not an allowed value");
/*     */     }
/*     */     
/* 125 */     return debug4;
/*     */   }
/*     */   
/*     */   public void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, S> debug1) {
/* 129 */     if (this.neighbours != null) {
/* 130 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 133 */     HashBasedTable hashBasedTable = HashBasedTable.create();
/* 134 */     for (UnmodifiableIterator<Map.Entry<Property<?>, Comparable<?>>> unmodifiableIterator = this.values.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Property<?>, Comparable<?>> debug4 = unmodifiableIterator.next();
/* 135 */       Property<?> debug5 = debug4.getKey();
/* 136 */       for (Comparable<?> debug7 : (Iterable<Comparable<?>>)debug5.getPossibleValues()) {
/* 137 */         if (debug7 != debug4.getValue()) {
/* 138 */           hashBasedTable.put(debug5, debug7, debug1.get(makeNeighbourValues(debug5, debug7)));
/*     */         }
/*     */       }  }
/*     */ 
/*     */     
/* 143 */     this.neighbours = hashBasedTable.isEmpty() ? (Table<Property<?>, Comparable<?>, S>)hashBasedTable : (Table<Property<?>, Comparable<?>, S>)ArrayTable.create((Table)hashBasedTable);
/*     */   }
/*     */   
/*     */   private Map<Property<?>, Comparable<?>> makeNeighbourValues(Property<?> debug1, Comparable<?> debug2) {
/* 147 */     Map<Property<?>, Comparable<?>> debug3 = Maps.newHashMap((Map)this.values);
/* 148 */     debug3.put(debug1, debug2);
/* 149 */     return debug3;
/*     */   }
/*     */   
/*     */   public ImmutableMap<Property<?>, Comparable<?>> getValues() {
/* 153 */     return this.values;
/*     */   }
/*     */   
/*     */   protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> debug0, Function<O, S> debug1) {
/* 157 */     return debug0.dispatch("Name", debug0 -> debug0.owner, debug1 -> {
/*     */           StateHolder stateHolder = debug0.apply(debug1);
/*     */           return stateHolder.getValues().isEmpty() ? Codec.unit(stateHolder) : stateHolder.propertiesCodec.fieldOf("Properties").codec();
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\StateHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */