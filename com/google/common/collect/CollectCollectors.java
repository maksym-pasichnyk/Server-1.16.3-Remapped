/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Comparator;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collector;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class CollectCollectors
/*    */ {
/*    */   static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 34 */     Preconditions.checkNotNull(keyFunction);
/* 35 */     Preconditions.checkNotNull(valueFunction);
/* 36 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build, new Collector.Characteristics[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/* 45 */     return Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build, new Collector.Characteristics[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 55 */     Preconditions.checkNotNull(keyFunction);
/* 56 */     Preconditions.checkNotNull(valueFunction);
/* 57 */     return Collector.of(Builder::new, (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableMap.Builder::combine, ImmutableMap.Builder::build, new Collector.Characteristics[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/* 65 */     return Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build, new Collector.Characteristics[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/* 76 */     Preconditions.checkNotNull(comparator);
/* 77 */     Preconditions.checkNotNull(keyFunction);
/* 78 */     Preconditions.checkNotNull(valueFunction);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 83 */     return Collector.of(() -> new ImmutableSortedMap.Builder<>(comparator), (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)), ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/* 93 */     Preconditions.checkNotNull(comparator);
/* 94 */     return Collector.of(() -> new ImmutableSortedSet.Builder(comparator), ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build, new Collector.Characteristics[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\CollectCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */