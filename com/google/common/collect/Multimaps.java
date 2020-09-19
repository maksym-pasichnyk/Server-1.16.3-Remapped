/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Multimaps
/*      */ {
/*      */   @Beta
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
/*  110 */     Preconditions.checkNotNull(keyFunction);
/*  111 */     Preconditions.checkNotNull(valueFunction);
/*  112 */     Preconditions.checkNotNull(multimapSupplier);
/*  113 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> multimap.put(keyFunction.apply(input), valueFunction.apply(input)), (multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
/*  160 */     Preconditions.checkNotNull(keyFunction);
/*  161 */     Preconditions.checkNotNull(valueFunction);
/*  162 */     Preconditions.checkNotNull(multimapSupplier);
/*  163 */     return (Collector)Collector.of(multimapSupplier, (multimap, input) -> { K key = keyFunction.apply(input); Collection<V> valuesForKey = multimap.get(key); ((Stream)valueFunction.apply(input)).forEachOrdered(valuesForKey::add); }(multimap1, multimap2) -> { multimap1.putAll(multimap2); return multimap1; }new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  214 */     return new CustomMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> { transient Supplier<? extends Collection<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  221 */       super(map);
/*  222 */       this.factory = (Supplier<? extends Collection<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<V> createCollection() {
/*  227 */       return (Collection<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  236 */       stream.defaultWriteObject();
/*  237 */       stream.writeObject(this.factory);
/*  238 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  244 */       stream.defaultReadObject();
/*  245 */       this.factory = (Supplier<? extends Collection<V>>)stream.readObject();
/*  246 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  247 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  294 */     return new CustomListMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> { transient Supplier<? extends List<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  301 */       super(map);
/*  302 */       this.factory = (Supplier<? extends List<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     protected List<V> createCollection() {
/*  307 */       return (List<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  313 */       stream.defaultWriteObject();
/*  314 */       stream.writeObject(this.factory);
/*  315 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  321 */       stream.defaultReadObject();
/*  322 */       this.factory = (Supplier<? extends List<V>>)stream.readObject();
/*  323 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  324 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  370 */     return new CustomSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> { transient Supplier<? extends Set<V>> factory;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  377 */       super(map);
/*  378 */       this.factory = (Supplier<? extends Set<V>>)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<V> createCollection() {
/*  383 */       return (Set<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  389 */       stream.defaultWriteObject();
/*  390 */       stream.writeObject(this.factory);
/*  391 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  397 */       stream.defaultReadObject();
/*  398 */       this.factory = (Supplier<? extends Set<V>>)stream.readObject();
/*  399 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  400 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  445 */     return new CustomSortedSetMultimap<>(map, factory);
/*      */   }
/*      */   private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> { transient Supplier<? extends SortedSet<V>> factory;
/*      */     transient Comparator<? super V> valueComparator;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  453 */       super(map);
/*  454 */       this.factory = (Supplier<? extends SortedSet<V>>)Preconditions.checkNotNull(factory);
/*  455 */       this.valueComparator = ((SortedSet<V>)factory.get()).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<V> createCollection() {
/*  460 */       return (SortedSet<V>)this.factory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  465 */       return this.valueComparator;
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  471 */       stream.defaultWriteObject();
/*  472 */       stream.writeObject(this.factory);
/*  473 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  479 */       stream.defaultReadObject();
/*  480 */       this.factory = (Supplier<? extends SortedSet<V>>)stream.readObject();
/*  481 */       this.valueComparator = ((SortedSet<V>)this.factory.get()).comparator();
/*  482 */       Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
/*  483 */       setMap(map);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  504 */     Preconditions.checkNotNull(dest);
/*  505 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  506 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  508 */     return dest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
/*  545 */     return Synchronized.multimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  566 */     if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
/*  567 */       return delegate;
/*      */     }
/*  569 */     return new UnmodifiableMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
/*  580 */     return (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Multiset<K> keys;
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> values;
/*      */     transient Map<K, Collection<V>> map;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableMultimap(Multimap<K, V> delegate) {
/*  593 */       this.delegate = (Multimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Multimap<K, V> delegate() {
/*  598 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  603 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  608 */       Map<K, Collection<V>> result = this.map;
/*  609 */       if (result == null)
/*      */       {
/*      */         
/*  612 */         result = this.map = Collections.<K, V>unmodifiableMap(
/*  613 */             Maps.transformValues(this.delegate
/*  614 */               .asMap(), new Function<Collection<V>, Collection<V>>()
/*      */               {
/*      */                 public Collection<V> apply(Collection<V> collection)
/*      */                 {
/*  618 */                   return Multimaps.unmodifiableValueCollection(collection);
/*      */                 }
/*      */               }));
/*      */       }
/*  622 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  627 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  628 */       if (result == null) {
/*  629 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  631 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  636 */       return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  641 */       Multiset<K> result = this.keys;
/*  642 */       if (result == null) {
/*  643 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  645 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  650 */       Set<K> result = this.keySet;
/*  651 */       if (result == null) {
/*  652 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  654 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  659 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  664 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  669 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  674 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  679 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  684 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  689 */       Collection<V> result = this.values;
/*  690 */       if (result == null) {
/*  691 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  693 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  702 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListMultimap<K, V> delegate() {
/*  707 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  712 */       return Collections.unmodifiableList(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(Object key) {
/*  717 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  722 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  731 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SetMultimap<K, V> delegate() {
/*  736 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  745 */       return Collections.unmodifiableSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  750 */       return Maps.unmodifiableEntrySet(delegate().entries());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/*  755 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  760 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V>
/*      */     extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  769 */       super(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSetMultimap<K, V> delegate() {
/*  774 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  779 */       return Collections.unmodifiableSortedSet(delegate().get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(Object key) {
/*  784 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  789 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  794 */       return delegate().valueComparator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
/*  813 */     return Synchronized.setMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  835 */     if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
/*  836 */       return delegate;
/*      */     }
/*  838 */     return new UnmodifiableSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
/*  850 */     return (SetMultimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
/*  867 */     return Synchronized.sortedSetMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  890 */     if (delegate instanceof UnmodifiableSortedSetMultimap) {
/*  891 */       return delegate;
/*      */     }
/*  893 */     return new UnmodifiableSortedSetMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
/*  906 */     return Synchronized.listMultimap(multimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  928 */     if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
/*  929 */       return delegate;
/*      */     }
/*  931 */     return new UnmodifiableListMultimap<>(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
/*  943 */     return (ListMultimap<K, V>)Preconditions.checkNotNull(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/*  955 */     if (collection instanceof SortedSet)
/*  956 */       return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
/*  957 */     if (collection instanceof Set)
/*  958 */       return Collections.unmodifiableSet((Set<? extends V>)collection); 
/*  959 */     if (collection instanceof List) {
/*  960 */       return Collections.unmodifiableList((List<? extends V>)collection);
/*      */     }
/*  962 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/*  976 */     if (entries instanceof Set) {
/*  977 */       return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>)entries);
/*      */     }
/*  979 */     return new Maps.UnmodifiableEntries<>(Collections.unmodifiableCollection(entries));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) {
/*  992 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) {
/* 1005 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) {
/* 1019 */     return (Map)multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) {
/* 1030 */     return multimap.asMap();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
/* 1051 */     return new MapMultimap<>(map);
/*      */   }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable {
/*      */     final Map<K, V> map;
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*      */     MapMultimap(Map<K, V> map) {
/* 1060 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1065 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1070 */       return this.map.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1075 */       return this.map.containsValue(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/* 1080 */       return this.map.entrySet().contains(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(final K key) {
/* 1085 */       return new Sets.ImprovedAbstractSet<V>()
/*      */         {
/*      */           public Iterator<V> iterator() {
/* 1088 */             return new Iterator<V>()
/*      */               {
/*      */                 int i;
/*      */                 
/*      */                 public boolean hasNext() {
/* 1093 */                   return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public V next() {
/* 1098 */                   if (!hasNext()) {
/* 1099 */                     throw new NoSuchElementException();
/*      */                   }
/* 1101 */                   this.i++;
/* 1102 */                   return (V)Multimaps.MapMultimap.this.map.get(key);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void remove() {
/* 1107 */                   CollectPreconditions.checkRemove((this.i == 1));
/* 1108 */                   this.i = -1;
/* 1109 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */ 
/*      */           
/*      */           public int size() {
/* 1116 */             return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/* 1123 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/* 1128 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 1133 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 1138 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1143 */       return this.map.entrySet().remove(Maps.immutableEntry(key, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/* 1148 */       Set<V> values = new HashSet<>(2);
/* 1149 */       if (!this.map.containsKey(key)) {
/* 1150 */         return values;
/*      */       }
/* 1152 */       values.add(this.map.remove(key));
/* 1153 */       return values;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1158 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1163 */       return this.map.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1168 */       return this.map.values();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/* 1173 */       return this.map.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1178 */       return this.map.entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     Map<K, Collection<V>> createAsMap() {
/* 1183 */       return new Multimaps.AsMap<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1188 */       return this.map.hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1240 */     Preconditions.checkNotNull(function);
/* 1241 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1242 */     return transformEntries(fromMultimap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1302 */     return new TransformedEntriesMultimap<>(fromMap, transformer);
/*      */   }
/*      */   
/*      */   private static class TransformedEntriesMultimap<K, V1, V2>
/*      */     extends AbstractMultimap<K, V2>
/*      */   {
/*      */     final Multimap<K, V1> fromMultimap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1312 */       this.fromMultimap = (Multimap<K, V1>)Preconditions.checkNotNull(fromMultimap);
/* 1313 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */     
/*      */     Collection<V2> transform(K key, Collection<V1> values) {
/* 1317 */       Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
/* 1318 */       if (values instanceof List) {
/* 1319 */         return Lists.transform((List<V1>)values, function);
/*      */       }
/* 1321 */       return Collections2.transform(values, function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, Collection<V2>> createAsMap() {
/* 1327 */       return Maps.transformEntries(this.fromMultimap
/* 1328 */           .asMap(), (Maps.EntryTransformer)new Maps.EntryTransformer<K, Collection<Collection<V1>>, Collection<Collection<V2>>>()
/*      */           {
/*      */             public Collection<V2> transformEntry(K key, Collection<V1> value)
/*      */             {
/* 1332 */               return Multimaps.TransformedEntriesMultimap.this.transform(key, value);
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1339 */       this.fromMultimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1344 */       return this.fromMultimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 1349 */       return Iterators.transform(this.fromMultimap
/* 1350 */           .entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> get(K key) {
/* 1355 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1360 */       return this.fromMultimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1365 */       return this.fromMultimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/* 1370 */       return this.fromMultimap.keys();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V2 value) {
/* 1375 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V2> values) {
/* 1380 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
/* 1385 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1391 */       return get((K)key).remove(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V2> removeAll(Object key) {
/* 1397 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1402 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1407 */       return this.fromMultimap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V2> createValues() {
/* 1412 */       return Collections2.transform(this.fromMultimap
/* 1413 */           .entries(), Maps.asEntryToValueFunction(this.transformer));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1460 */     Preconditions.checkNotNull(function);
/* 1461 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1462 */     return transformEntries(fromMultimap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1519 */     return new TransformedEntriesListMultimap<>(fromMap, transformer);
/*      */   }
/*      */   
/*      */   private static final class TransformedEntriesListMultimap<K, V1, V2>
/*      */     extends TransformedEntriesMultimap<K, V1, V2>
/*      */     implements ListMultimap<K, V2>
/*      */   {
/*      */     TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1527 */       super(fromMultimap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     List<V2> transform(K key, Collection<V1> values) {
/* 1532 */       return Lists.transform((List)values, Maps.asValueToValueFunction(this.transformer, key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> get(K key) {
/* 1537 */       return transform(key, this.fromMultimap.get(key));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public List<V2> removeAll(Object key) {
/* 1543 */       return transform((K)key, this.fromMultimap.removeAll(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V2> replaceValues(K key, Iterable<? extends V2> values) {
/* 1548 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1596 */     return index(values.iterator(), keyFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1644 */     Preconditions.checkNotNull(keyFunction);
/* 1645 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/* 1646 */     while (values.hasNext()) {
/* 1647 */       V value = values.next();
/* 1648 */       Preconditions.checkNotNull(value, values);
/* 1649 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/* 1651 */     return builder.build();
/*      */   }
/*      */   
/*      */   static class Keys<K, V> extends AbstractMultiset<K> { @Weak
/*      */     final Multimap<K, V> multimap;
/*      */     
/*      */     Keys(Multimap<K, V> multimap) {
/* 1658 */       this.multimap = multimap;
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<K>> entryIterator() {
/* 1663 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this.multimap
/* 1664 */           .asMap().entrySet().iterator())
/*      */         {
/*      */           Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
/* 1667 */             return new Multisets.AbstractEntry<K>()
/*      */               {
/*      */                 public K getElement() {
/* 1670 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public int getCount() {
/* 1675 */                   return ((Collection)backingEntry.getValue()).size();
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/* 1684 */       return CollectSpliterators.map(this.multimap.entries().spliterator(), Map.Entry::getKey);
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1689 */       Preconditions.checkNotNull(consumer);
/* 1690 */       this.multimap.entries().forEach(entry -> consumer.accept(entry.getKey()));
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/* 1695 */       return this.multimap.asMap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<K>> createEntrySet() {
/* 1700 */       return new KeysEntrySet();
/*      */     }
/*      */     
/*      */     class KeysEntrySet
/*      */       extends Multisets.EntrySet<K>
/*      */     {
/*      */       Multiset<K> multiset() {
/* 1707 */         return Multimaps.Keys.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Multiset.Entry<K>> iterator() {
/* 1712 */         return Multimaps.Keys.this.entryIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/* 1717 */         return Multimaps.Keys.this.distinctElements();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isEmpty() {
/* 1722 */         return Multimaps.Keys.this.multimap.isEmpty();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean contains(@Nullable Object o) {
/* 1727 */         if (o instanceof Multiset.Entry) {
/* 1728 */           Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1729 */           Collection<V> collection = (Collection<V>)Multimaps.Keys.this.multimap.asMap().get(entry.getElement());
/* 1730 */           return (collection != null && collection.size() == entry.getCount());
/*      */         } 
/* 1732 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(@Nullable Object o) {
/* 1737 */         if (o instanceof Multiset.Entry) {
/* 1738 */           Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1739 */           Collection<V> collection = (Collection<V>)Multimaps.Keys.this.multimap.asMap().get(entry.getElement());
/* 1740 */           if (collection != null && collection.size() == entry.getCount()) {
/* 1741 */             collection.clear();
/* 1742 */             return true;
/*      */           } 
/*      */         } 
/* 1745 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object element) {
/* 1751 */       return this.multimap.containsKey(element);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1756 */       return Maps.keyIterator(this.multimap.entries().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@Nullable Object element) {
/* 1761 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/* 1762 */       return (values == null) ? 0 : values.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@Nullable Object element, int occurrences) {
/* 1767 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 1768 */       if (occurrences == 0) {
/* 1769 */         return count(element);
/*      */       }
/*      */       
/* 1772 */       Collection<V> values = Maps.<Collection<V>>safeGet(this.multimap.asMap(), element);
/*      */       
/* 1774 */       if (values == null) {
/* 1775 */         return 0;
/*      */       }
/*      */       
/* 1778 */       int oldCount = values.size();
/* 1779 */       if (occurrences >= oldCount) {
/* 1780 */         values.clear();
/*      */       } else {
/* 1782 */         Iterator<V> iterator = values.iterator();
/* 1783 */         for (int i = 0; i < occurrences; i++) {
/* 1784 */           iterator.next();
/* 1785 */           iterator.remove();
/*      */         } 
/*      */       } 
/* 1788 */       return oldCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1793 */       this.multimap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> elementSet() {
/* 1798 */       return this.multimap.keySet();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class Entries<K, V>
/*      */     extends AbstractCollection<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Multimap<K, V> multimap();
/*      */ 
/*      */     
/*      */     public int size() {
/* 1810 */       return multimap().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 1815 */       if (o instanceof Map.Entry) {
/* 1816 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1817 */         return multimap().containsEntry(entry.getKey(), entry.getValue());
/*      */       } 
/* 1819 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(@Nullable Object o) {
/* 1824 */       if (o instanceof Map.Entry) {
/* 1825 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1826 */         return multimap().remove(entry.getKey(), entry.getValue());
/*      */       } 
/* 1828 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1833 */       multimap().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static final class AsMap<K, V>
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*      */   {
/*      */     @Weak
/*      */     private final Multimap<K, V> multimap;
/*      */     
/*      */     AsMap(Multimap<K, V> multimap) {
/* 1844 */       this.multimap = (Multimap<K, V>)Preconditions.checkNotNull(multimap);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1849 */       return this.multimap.keySet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1854 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     void removeValuesForKey(Object key) {
/* 1858 */       this.multimap.keySet().remove(key);
/*      */     }
/*      */     
/*      */     class EntrySet
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1865 */         return Multimaps.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1870 */         return Maps.asMapEntryIterator(Multimaps.AsMap.this
/* 1871 */             .multimap.keySet(), new Function<K, Collection<V>>()
/*      */             {
/*      */               public Collection<V> apply(K key)
/*      */               {
/* 1875 */                 return Multimaps.AsMap.this.multimap.get(key);
/*      */               }
/*      */             });
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1882 */         if (!contains(o)) {
/* 1883 */           return false;
/*      */         }
/* 1885 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1886 */         Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
/* 1887 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1894 */       return containsKey(key) ? this.multimap.get((K)key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1899 */       return containsKey(key) ? this.multimap.removeAll(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1904 */       return this.multimap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1909 */       return this.multimap.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1914 */       return this.multimap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1919 */       this.multimap.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1955 */     if (unfiltered instanceof SetMultimap)
/* 1956 */       return filterKeys((SetMultimap<K, V>)unfiltered, keyPredicate); 
/* 1957 */     if (unfiltered instanceof ListMultimap)
/* 1958 */       return filterKeys((ListMultimap<K, V>)unfiltered, keyPredicate); 
/* 1959 */     if (unfiltered instanceof FilteredKeyMultimap) {
/* 1960 */       FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap<K, V>)unfiltered;
/* 1961 */       return new FilteredKeyMultimap<>(prev.unfiltered, 
/* 1962 */           Predicates.and(prev.keyPredicate, keyPredicate));
/* 1963 */     }  if (unfiltered instanceof FilteredMultimap) {
/* 1964 */       FilteredMultimap<K, V> prev = (FilteredMultimap<K, V>)unfiltered;
/* 1965 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1967 */     return new FilteredKeyMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2003 */     if (unfiltered instanceof FilteredKeySetMultimap) {
/* 2004 */       FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap<K, V>)unfiltered;
/* 2005 */       return new FilteredKeySetMultimap<>(prev
/* 2006 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/* 2007 */     }  if (unfiltered instanceof FilteredSetMultimap) {
/* 2008 */       FilteredSetMultimap<K, V> prev = (FilteredSetMultimap<K, V>)unfiltered;
/* 2009 */       return filterFiltered(prev, (Predicate)Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 2011 */     return new FilteredKeySetMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2047 */     if (unfiltered instanceof FilteredKeyListMultimap) {
/* 2048 */       FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap<K, V>)unfiltered;
/* 2049 */       return new FilteredKeyListMultimap<>(prev
/* 2050 */           .unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 2052 */     return new FilteredKeyListMultimap<>(unfiltered, keyPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2088 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2123 */     return filterEntries(unfiltered, (Predicate)Maps.valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2156 */     Preconditions.checkNotNull(entryPredicate);
/* 2157 */     if (unfiltered instanceof SetMultimap) {
/* 2158 */       return filterEntries((SetMultimap<K, V>)unfiltered, entryPredicate);
/*      */     }
/* 2160 */     return (unfiltered instanceof FilteredMultimap) ? 
/* 2161 */       filterFiltered((FilteredMultimap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMultimap<>(
/* 2162 */         (Multimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2195 */     Preconditions.checkNotNull(entryPredicate);
/* 2196 */     return (unfiltered instanceof FilteredSetMultimap) ? 
/* 2197 */       filterFiltered((FilteredSetMultimap<K, V>)unfiltered, entryPredicate) : new FilteredEntrySetMultimap<>(
/* 2198 */         (SetMultimap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2211 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2212 */     return new FilteredEntryMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2224 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/* 2225 */     return new FilteredEntrySetMultimap<>(multimap.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static boolean equalsImpl(Multimap<?, ?> multimap, @Nullable Object object) {
/* 2229 */     if (object == multimap) {
/* 2230 */       return true;
/*      */     }
/* 2232 */     if (object instanceof Multimap) {
/* 2233 */       Multimap<?, ?> that = (Multimap<?, ?>)object;
/* 2234 */       return multimap.asMap().equals(that.asMap());
/*      */     } 
/* 2236 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Multimaps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */