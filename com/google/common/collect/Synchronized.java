/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Queue;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.UnaryOperator;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     final Object delegate;
/*      */     final Object mutex;
/*      */     @GwtIncompatible
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedObject(Object delegate, @Nullable Object mutex) {
/*   75 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   76 */       this.mutex = (mutex == null) ? this : mutex;
/*      */     }
/*      */     
/*      */     Object delegate() {
/*   80 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   87 */       synchronized (this.mutex) {
/*   88 */         return this.delegate.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*   99 */       synchronized (this.mutex) {
/*  100 */         stream.defaultWriteObject();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) {
/*  109 */     return new SynchronizedCollection<>(collection, mutex);
/*      */   }
/*      */   @VisibleForTesting
/*      */   static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) {
/*  115 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<E> delegate() {
/*  121 */       return (Collection<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E e) {
/*  126 */       synchronized (this.mutex) {
/*  127 */         return delegate().add(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> c) {
/*  133 */       synchronized (this.mutex) {
/*  134 */         return delegate().addAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  140 */       synchronized (this.mutex) {
/*  141 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  147 */       synchronized (this.mutex) {
/*  148 */         return delegate().contains(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  154 */       synchronized (this.mutex) {
/*  155 */         return delegate().containsAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  161 */       synchronized (this.mutex) {
/*  162 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  168 */       return delegate().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<E> spliterator() {
/*  173 */       synchronized (this.mutex) {
/*  174 */         return delegate().spliterator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> stream() {
/*  180 */       synchronized (this.mutex) {
/*  181 */         return delegate().stream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<E> parallelStream() {
/*  187 */       synchronized (this.mutex) {
/*  188 */         return delegate().parallelStream();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super E> action) {
/*  194 */       synchronized (this.mutex) {
/*  195 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  201 */       synchronized (this.mutex) {
/*  202 */         return delegate().remove(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  208 */       synchronized (this.mutex) {
/*  209 */         return delegate().removeAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  215 */       synchronized (this.mutex) {
/*  216 */         return delegate().retainAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super E> filter) {
/*  222 */       synchronized (this.mutex) {
/*  223 */         return delegate().removeIf(filter);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  229 */       synchronized (this.mutex) {
/*  230 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  236 */       synchronized (this.mutex) {
/*  237 */         return delegate().toArray();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  243 */       synchronized (this.mutex) {
/*  244 */         return delegate().toArray(a);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <E> Set<E> set(Set<E> set, @Nullable Object mutex) {
/*  253 */     return new SynchronizedSet<>(set, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSet(Set<E> delegate, @Nullable Object mutex) {
/*  259 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> delegate() {
/*  264 */       return (Set<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  269 */       if (o == this) {
/*  270 */         return true;
/*      */       }
/*  272 */       synchronized (this.mutex) {
/*  273 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  279 */       synchronized (this.mutex) {
/*  280 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) {
/*  288 */     return new SynchronizedSortedSet<>(set, mutex);
/*      */   }
/*      */   static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) {
/*  293 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<E> delegate() {
/*  298 */       return (SortedSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/*  303 */       synchronized (this.mutex) {
/*  304 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  310 */       synchronized (this.mutex) {
/*  311 */         return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  317 */       synchronized (this.mutex) {
/*  318 */         return Synchronized.sortedSet(delegate().headSet(toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  324 */       synchronized (this.mutex) {
/*  325 */         return Synchronized.sortedSet(delegate().tailSet(fromElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/*  331 */       synchronized (this.mutex) {
/*  332 */         return delegate().first();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  338 */       synchronized (this.mutex) {
/*  339 */         return delegate().last();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> List<E> list(List<E> list, @Nullable Object mutex) {
/*  347 */     return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList<>(list, mutex) : new SynchronizedList<>(list, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedList(List<E> delegate, @Nullable Object mutex) {
/*  354 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     List<E> delegate() {
/*  359 */       return (List<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/*  364 */       synchronized (this.mutex) {
/*  365 */         delegate().add(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/*  371 */       synchronized (this.mutex) {
/*  372 */         return delegate().addAll(index, c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  378 */       synchronized (this.mutex) {
/*  379 */         return delegate().get(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  385 */       synchronized (this.mutex) {
/*  386 */         return delegate().indexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  392 */       synchronized (this.mutex) {
/*  393 */         return delegate().lastIndexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator() {
/*  399 */       return delegate().listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator(int index) {
/*  404 */       return delegate().listIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/*  409 */       synchronized (this.mutex) {
/*  410 */         return delegate().remove(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/*  416 */       synchronized (this.mutex) {
/*  417 */         return delegate().set(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(UnaryOperator<E> operator) {
/*  423 */       synchronized (this.mutex) {
/*  424 */         delegate().replaceAll(operator);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void sort(Comparator<? super E> c) {
/*  430 */       synchronized (this.mutex) {
/*  431 */         delegate().sort(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<E> subList(int fromIndex, int toIndex) {
/*  437 */       synchronized (this.mutex) {
/*  438 */         return Synchronized.list(delegate().subList(fromIndex, toIndex), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  444 */       if (o == this) {
/*  445 */         return true;
/*      */       }
/*  447 */       synchronized (this.mutex) {
/*  448 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  454 */       synchronized (this.mutex) {
/*  455 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedRandomAccessList<E>
/*      */     extends SynchronizedList<E> implements RandomAccess {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex) {
/*  465 */       super(list, mutex);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex) {
/*  472 */     if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset) {
/*  473 */       return multiset;
/*      */     }
/*  475 */     return new SynchronizedMultiset<>(multiset, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E> {
/*      */     transient Set<E> elementSet;
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) {
/*  484 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Multiset<E> delegate() {
/*  489 */       return (Multiset<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(Object o) {
/*  494 */       synchronized (this.mutex) {
/*  495 */         return delegate().count(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E e, int n) {
/*  501 */       synchronized (this.mutex) {
/*  502 */         return delegate().add(e, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object o, int n) {
/*  508 */       synchronized (this.mutex) {
/*  509 */         return delegate().remove(o, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  515 */       synchronized (this.mutex) {
/*  516 */         return delegate().setCount(element, count);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  522 */       synchronized (this.mutex) {
/*  523 */         return delegate().setCount(element, oldCount, newCount);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  529 */       synchronized (this.mutex) {
/*  530 */         if (this.elementSet == null) {
/*  531 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  533 */         return this.elementSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  539 */       synchronized (this.mutex) {
/*  540 */         if (this.entrySet == null) {
/*  541 */           this.entrySet = (Set)Synchronized.typePreservingSet((Set)delegate().entrySet(), this.mutex);
/*      */         }
/*  543 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  549 */       if (o == this) {
/*  550 */         return true;
/*      */       }
/*  552 */       synchronized (this.mutex) {
/*  553 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  559 */       synchronized (this.mutex) {
/*  560 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex) {
/*  568 */     if (multimap instanceof SynchronizedMultimap || multimap instanceof ImmutableMultimap) {
/*  569 */       return multimap;
/*      */     }
/*  571 */     return new SynchronizedMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultimap<K, V>
/*      */     extends SynchronizedObject
/*      */     implements Multimap<K, V> {
/*      */     transient Set<K> keySet;
/*      */     transient Collection<V> valuesCollection;
/*      */     transient Collection<Map.Entry<K, V>> entries;
/*      */     transient Map<K, Collection<V>> asMap;
/*      */     transient Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     Multimap<K, V> delegate() {
/*  585 */       return (Multimap<K, V>)super.delegate();
/*      */     }
/*      */     
/*      */     SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) {
/*  589 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  594 */       synchronized (this.mutex) {
/*  595 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  601 */       synchronized (this.mutex) {
/*  602 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  608 */       synchronized (this.mutex) {
/*  609 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  615 */       synchronized (this.mutex) {
/*  616 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/*  622 */       synchronized (this.mutex) {
/*  623 */         return delegate().containsEntry(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  629 */       synchronized (this.mutex) {
/*  630 */         return Synchronized.typePreservingCollection(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  636 */       synchronized (this.mutex) {
/*  637 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  643 */       synchronized (this.mutex) {
/*  644 */         return delegate().putAll(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  650 */       synchronized (this.mutex) {
/*  651 */         return delegate().putAll(multimap);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  657 */       synchronized (this.mutex) {
/*  658 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  664 */       synchronized (this.mutex) {
/*  665 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  671 */       synchronized (this.mutex) {
/*  672 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  678 */       synchronized (this.mutex) {
/*  679 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  685 */       synchronized (this.mutex) {
/*  686 */         if (this.keySet == null) {
/*  687 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  689 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  695 */       synchronized (this.mutex) {
/*  696 */         if (this.valuesCollection == null) {
/*  697 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  699 */         return this.valuesCollection;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  705 */       synchronized (this.mutex) {
/*  706 */         if (this.entries == null) {
/*  707 */           this.entries = (Collection)Synchronized.typePreservingCollection((Collection)delegate().entries(), this.mutex);
/*      */         }
/*  709 */         return this.entries;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  715 */       synchronized (this.mutex) {
/*  716 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  722 */       synchronized (this.mutex) {
/*  723 */         if (this.asMap == null) {
/*  724 */           this.asMap = new Synchronized.SynchronizedAsMap<>(delegate().asMap(), this.mutex);
/*      */         }
/*  726 */         return this.asMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  732 */       synchronized (this.mutex) {
/*  733 */         if (this.keys == null) {
/*  734 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  736 */         return this.keys;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  742 */       if (o == this) {
/*  743 */         return true;
/*      */       }
/*  745 */       synchronized (this.mutex) {
/*  746 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  752 */       synchronized (this.mutex) {
/*  753 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex) {
/*  762 */     if (multimap instanceof SynchronizedListMultimap || multimap instanceof ImmutableListMultimap) {
/*  763 */       return multimap;
/*      */     }
/*  765 */     return new SynchronizedListMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) {
/*  771 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     ListMultimap<K, V> delegate() {
/*  776 */       return (ListMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> get(K key) {
/*  781 */       synchronized (this.mutex) {
/*  782 */         return Synchronized.list(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> removeAll(Object key) {
/*  788 */       synchronized (this.mutex) {
/*  789 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  795 */       synchronized (this.mutex) {
/*  796 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  804 */     if (multimap instanceof SynchronizedSetMultimap || multimap instanceof ImmutableSetMultimap) {
/*  805 */       return multimap;
/*      */     }
/*  807 */     return new SynchronizedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) {
/*  815 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SetMultimap<K, V> delegate() {
/*  820 */       return (SetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> get(K key) {
/*  825 */       synchronized (this.mutex) {
/*  826 */         return Synchronized.set(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/*  832 */       synchronized (this.mutex) {
/*  833 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  839 */       synchronized (this.mutex) {
/*  840 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  846 */       synchronized (this.mutex) {
/*  847 */         if (this.entrySet == null) {
/*  848 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  850 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  859 */     if (multimap instanceof SynchronizedSortedSetMultimap) {
/*  860 */       return multimap;
/*      */     }
/*  862 */     return new SynchronizedSortedSetMultimap<>(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) {
/*  868 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSetMultimap<K, V> delegate() {
/*  873 */       return (SortedSetMultimap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  878 */       synchronized (this.mutex) {
/*  879 */         return Synchronized.sortedSet(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> removeAll(Object key) {
/*  885 */       synchronized (this.mutex) {
/*  886 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  892 */       synchronized (this.mutex) {
/*  893 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  899 */       synchronized (this.mutex) {
/*  900 */         return delegate().valueComparator();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex) {
/*  909 */     if (collection instanceof SortedSet) {
/*  910 */       return sortedSet((SortedSet<E>)collection, mutex);
/*      */     }
/*  912 */     if (collection instanceof Set) {
/*  913 */       return set((Set<E>)collection, mutex);
/*      */     }
/*  915 */     if (collection instanceof List) {
/*  916 */       return list((List<E>)collection, mutex);
/*      */     }
/*  918 */     return collection(collection, mutex);
/*      */   }
/*      */   
/*      */   private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
/*  922 */     if (set instanceof SortedSet) {
/*  923 */       return sortedSet((SortedSet<E>)set, mutex);
/*      */     }
/*  925 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapEntries<K, V> extends SynchronizedSet<Map.Entry<K, Collection<V>>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) {
/*  932 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/*  938 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Map.Entry<K, Collection<V>>>(super
/*  939 */           .iterator())
/*      */         {
/*      */           Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry) {
/*  942 */             return (Map.Entry)new ForwardingMapEntry<K, Collection<Collection<V>>>()
/*      */               {
/*      */                 protected Map.Entry<K, Collection<V>> delegate() {
/*  945 */                   return entry;
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public Collection<V> getValue() {
/*  950 */                   return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex);
/*      */                 }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  961 */       synchronized (this.mutex) {
/*  962 */         return ObjectArrays.toArrayImpl(delegate());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/*  968 */       synchronized (this.mutex) {
/*  969 */         return ObjectArrays.toArrayImpl(delegate(), array);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  975 */       synchronized (this.mutex) {
/*  976 */         return Maps.containsEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  982 */       synchronized (this.mutex) {
/*  983 */         return Collections2.containsAllImpl(delegate(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  989 */       if (o == this) {
/*  990 */         return true;
/*      */       }
/*  992 */       synchronized (this.mutex) {
/*  993 */         return Sets.equalsImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  999 */       synchronized (this.mutex) {
/* 1000 */         return Maps.removeEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/* 1006 */       synchronized (this.mutex) {
/* 1007 */         return Iterators.removeAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/* 1013 */       synchronized (this.mutex) {
/* 1014 */         return Iterators.retainAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex) {
/* 1023 */     return new SynchronizedMap<>(map, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> { transient Set<K> keySet;
/*      */     transient Collection<V> values;
/*      */     transient Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) {
/* 1032 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, V> delegate() {
/* 1038 */       return (Map<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1043 */       synchronized (this.mutex) {
/* 1044 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1050 */       synchronized (this.mutex) {
/* 1051 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 1057 */       synchronized (this.mutex) {
/* 1058 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1064 */       synchronized (this.mutex) {
/* 1065 */         if (this.entrySet == null) {
/* 1066 */           this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */         }
/* 1068 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1074 */       synchronized (this.mutex) {
/* 1075 */         delegate().forEach(action);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 1081 */       synchronized (this.mutex) {
/* 1082 */         return delegate().get(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/* 1088 */       synchronized (this.mutex) {
/* 1089 */         return delegate().getOrDefault(key, defaultValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1095 */       synchronized (this.mutex) {
/* 1096 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1102 */       synchronized (this.mutex) {
/* 1103 */         if (this.keySet == null) {
/* 1104 */           this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */         }
/* 1106 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 1112 */       synchronized (this.mutex) {
/* 1113 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V putIfAbsent(K key, V value) {
/* 1119 */       synchronized (this.mutex) {
/* 1120 */         return delegate().putIfAbsent(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean replace(K key, V oldValue, V newValue) {
/* 1126 */       synchronized (this.mutex) {
/* 1127 */         return delegate().replace(key, oldValue, newValue);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V replace(K key, V value) {
/* 1133 */       synchronized (this.mutex) {
/* 1134 */         return delegate().replace(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 1140 */       synchronized (this.mutex) {
/* 1141 */         return delegate().computeIfAbsent(key, mappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1148 */       synchronized (this.mutex) {
/* 1149 */         return delegate().computeIfPresent(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 1155 */       synchronized (this.mutex) {
/* 1156 */         return delegate().compute(key, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 1163 */       synchronized (this.mutex) {
/* 1164 */         return delegate().merge(key, value, remappingFunction);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 1170 */       synchronized (this.mutex) {
/* 1171 */         delegate().putAll(map);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 1177 */       synchronized (this.mutex) {
/* 1178 */         delegate().replaceAll(function);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 1184 */       synchronized (this.mutex) {
/* 1185 */         return delegate().remove(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/* 1191 */       synchronized (this.mutex) {
/* 1192 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1198 */       synchronized (this.mutex) {
/* 1199 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1205 */       synchronized (this.mutex) {
/* 1206 */         if (this.values == null) {
/* 1207 */           this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/* 1209 */         return this.values;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1215 */       if (o == this) {
/* 1216 */         return true;
/*      */       }
/* 1218 */       synchronized (this.mutex) {
/* 1219 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1225 */       synchronized (this.mutex) {
/* 1226 */         return delegate().hashCode();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object mutex) {
/* 1234 */     return new SynchronizedSortedMap<>(sortedMap, mutex);
/*      */   }
/*      */   
/*      */   static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedSortedMap(SortedMap<K, V> delegate, @Nullable Object mutex) {
/* 1241 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> delegate() {
/* 1246 */       return (SortedMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1251 */       synchronized (this.mutex) {
/* 1252 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1258 */       synchronized (this.mutex) {
/* 1259 */         return delegate().firstKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1265 */       synchronized (this.mutex) {
/* 1266 */         return Synchronized.sortedMap(delegate().headMap(toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1272 */       synchronized (this.mutex) {
/* 1273 */         return delegate().lastKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1279 */       synchronized (this.mutex) {
/* 1280 */         return Synchronized.sortedMap(delegate().subMap(fromKey, toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1286 */       synchronized (this.mutex) {
/* 1287 */         return Synchronized.sortedMap(delegate().tailMap(fromKey), this.mutex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex) {
/* 1295 */     if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap) {
/* 1296 */       return bimap;
/*      */     }
/* 1298 */     return new SynchronizedBiMap<>(bimap, mutex, null);
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedBiMap<K, V>
/*      */     extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     private transient Set<V> valueSet;
/*      */     @RetainedWith
/*      */     private transient BiMap<V, K> inverse;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
/* 1310 */       super(delegate, mutex);
/* 1311 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     BiMap<K, V> delegate() {
/* 1316 */       return (BiMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1321 */       synchronized (this.mutex) {
/* 1322 */         if (this.valueSet == null) {
/* 1323 */           this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */         }
/* 1325 */         return this.valueSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1331 */       synchronized (this.mutex) {
/* 1332 */         return delegate().forcePut(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1338 */       synchronized (this.mutex) {
/* 1339 */         if (this.inverse == null) {
/* 1340 */           this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */         }
/* 1342 */         return this.inverse;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMap<K, V>
/*      */     extends SynchronizedMap<K, Collection<V>> {
/*      */     transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */     transient Collection<Collection<V>> asMapValues;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) {
/* 1354 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1359 */       synchronized (this.mutex) {
/* 1360 */         Collection<V> collection = super.get(key);
/* 1361 */         return (collection == null) ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1367 */       synchronized (this.mutex) {
/* 1368 */         if (this.asMapEntrySet == null) {
/* 1369 */           this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries<>(delegate().entrySet(), this.mutex);
/*      */         }
/* 1371 */         return this.asMapEntrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Collection<V>> values() {
/* 1377 */       synchronized (this.mutex) {
/* 1378 */         if (this.asMapValues == null) {
/* 1379 */           this.asMapValues = new Synchronized.SynchronizedAsMapValues<>(delegate().values(), this.mutex);
/*      */         }
/* 1381 */         return this.asMapValues;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object o) {
/* 1388 */       return values().contains(o);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapValues<V> extends SynchronizedCollection<Collection<V>> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) {
/* 1396 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Collection<V>> iterator() {
/* 1402 */       return new TransformedIterator<Collection<V>, Collection<V>>(super.iterator())
/*      */         {
/*      */           Collection<V> transform(Collection<V> from) {
/* 1405 */             return Synchronized.typePreservingCollection(from, Synchronized.SynchronizedAsMapValues.this.mutex);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
/*      */     transient NavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedNavigableSet(NavigableSet<E> delegate, @Nullable Object mutex) {
/* 1418 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<E> delegate() {
/* 1423 */       return (NavigableSet<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1428 */       synchronized (this.mutex) {
/* 1429 */         return delegate().ceiling(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1435 */       return delegate().descendingIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1442 */       synchronized (this.mutex) {
/* 1443 */         if (this.descendingSet == null) {
/* 1444 */           NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
/* 1445 */           this.descendingSet = dS;
/* 1446 */           return dS;
/*      */         } 
/* 1448 */         return this.descendingSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1454 */       synchronized (this.mutex) {
/* 1455 */         return delegate().floor(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1461 */       synchronized (this.mutex) {
/* 1462 */         return Synchronized.navigableSet(delegate().headSet(toElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1468 */       synchronized (this.mutex) {
/* 1469 */         return delegate().higher(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1475 */       synchronized (this.mutex) {
/* 1476 */         return delegate().lower(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1482 */       synchronized (this.mutex) {
/* 1483 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1489 */       synchronized (this.mutex) {
/* 1490 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1497 */       synchronized (this.mutex) {
/* 1498 */         return Synchronized.navigableSet(
/* 1499 */             delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1505 */       synchronized (this.mutex) {
/* 1506 */         return Synchronized.navigableSet(delegate().tailSet(fromElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1512 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1517 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1522 */       return tailSet(fromElement, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object mutex) {
/* 1530 */     return new SynchronizedNavigableSet<>(navigableSet, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
/* 1535 */     return navigableSet(navigableSet, null);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
/* 1540 */     return navigableMap(navigableMap, null);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object mutex) {
/* 1546 */     return new SynchronizedNavigableMap<>(navigableMap, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { transient NavigableSet<K> descendingKeySet;
/*      */     transient NavigableMap<K, V> descendingMap;
/*      */     
/*      */     SynchronizedNavigableMap(NavigableMap<K, V> delegate, @Nullable Object mutex) {
/* 1555 */       super(delegate, mutex);
/*      */     }
/*      */     transient NavigableSet<K> navigableKeySet; private static final long serialVersionUID = 0L;
/*      */     
/*      */     NavigableMap<K, V> delegate() {
/* 1560 */       return (NavigableMap<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 1565 */       synchronized (this.mutex) {
/* 1566 */         return Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1572 */       synchronized (this.mutex) {
/* 1573 */         return delegate().ceilingKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1581 */       synchronized (this.mutex) {
/* 1582 */         if (this.descendingKeySet == null) {
/* 1583 */           return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
/*      */         }
/* 1585 */         return this.descendingKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1593 */       synchronized (this.mutex) {
/* 1594 */         if (this.descendingMap == null) {
/* 1595 */           return this.descendingMap = Synchronized.<K, V>navigableMap(delegate().descendingMap(), this.mutex);
/*      */         }
/* 1597 */         return this.descendingMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 1603 */       synchronized (this.mutex) {
/* 1604 */         return Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 1610 */       synchronized (this.mutex) {
/* 1611 */         return Synchronized.nullableSynchronizedEntry(delegate().floorEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1617 */       synchronized (this.mutex) {
/* 1618 */         return delegate().floorKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1624 */       synchronized (this.mutex) {
/* 1625 */         return Synchronized.navigableMap(delegate().headMap(toKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 1631 */       synchronized (this.mutex) {
/* 1632 */         return Synchronized.nullableSynchronizedEntry(delegate().higherEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1638 */       synchronized (this.mutex) {
/* 1639 */         return delegate().higherKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 1645 */       synchronized (this.mutex) {
/* 1646 */         return Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 1652 */       synchronized (this.mutex) {
/* 1653 */         return Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1659 */       synchronized (this.mutex) {
/* 1660 */         return delegate().lowerKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1666 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1673 */       synchronized (this.mutex) {
/* 1674 */         if (this.navigableKeySet == null) {
/* 1675 */           return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
/*      */         }
/* 1677 */         return this.navigableKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 1683 */       synchronized (this.mutex) {
/* 1684 */         return Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 1690 */       synchronized (this.mutex) {
/* 1691 */         return Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1698 */       synchronized (this.mutex) {
/* 1699 */         return Synchronized.navigableMap(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1705 */       synchronized (this.mutex) {
/* 1706 */         return Synchronized.navigableMap(delegate().tailMap(fromKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1712 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1717 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1722 */       return tailMap(fromKey, true);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable Map.Entry<K, V> entry, @Nullable Object mutex) {
/* 1731 */     if (entry == null) {
/* 1732 */       return null;
/*      */     }
/* 1734 */     return new SynchronizedEntry<>(entry, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class SynchronizedEntry<K, V> extends SynchronizedObject implements Map.Entry<K, V> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedEntry(Map.Entry<K, V> delegate, @Nullable Object mutex) {
/* 1741 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map.Entry<K, V> delegate() {
/* 1747 */       return (Map.Entry<K, V>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1752 */       synchronized (this.mutex) {
/* 1753 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1759 */       synchronized (this.mutex) {
/* 1760 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 1766 */       synchronized (this.mutex) {
/* 1767 */         return delegate().getKey();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 1773 */       synchronized (this.mutex) {
/* 1774 */         return delegate().getValue();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V value) {
/* 1780 */       synchronized (this.mutex) {
/* 1781 */         return delegate().setValue(value);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Queue<E> queue(Queue<E> queue, @Nullable Object mutex) {
/* 1789 */     return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue<>(queue, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedQueue(Queue<E> delegate, @Nullable Object mutex) {
/* 1795 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Queue<E> delegate() {
/* 1800 */       return (Queue<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public E element() {
/* 1805 */       synchronized (this.mutex) {
/* 1806 */         return delegate().element();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offer(E e) {
/* 1812 */       synchronized (this.mutex) {
/* 1813 */         return delegate().offer(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1819 */       synchronized (this.mutex) {
/* 1820 */         return delegate().peek();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E poll() {
/* 1826 */       synchronized (this.mutex) {
/* 1827 */         return delegate().poll();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove() {
/* 1833 */       synchronized (this.mutex) {
/* 1834 */         return delegate().remove();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Deque<E> deque(Deque<E> deque, @Nullable Object mutex) {
/* 1842 */     return new SynchronizedDeque<>(deque, mutex);
/*      */   }
/*      */   
/*      */   private static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedDeque(Deque<E> delegate, @Nullable Object mutex) {
/* 1848 */       super(delegate, mutex);
/*      */     }
/*      */ 
/*      */     
/*      */     Deque<E> delegate() {
/* 1853 */       return (Deque<E>)super.delegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public void addFirst(E e) {
/* 1858 */       synchronized (this.mutex) {
/* 1859 */         delegate().addFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void addLast(E e) {
/* 1865 */       synchronized (this.mutex) {
/* 1866 */         delegate().addLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerFirst(E e) {
/* 1872 */       synchronized (this.mutex) {
/* 1873 */         return delegate().offerFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerLast(E e) {
/* 1879 */       synchronized (this.mutex) {
/* 1880 */         return delegate().offerLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeFirst() {
/* 1886 */       synchronized (this.mutex) {
/* 1887 */         return delegate().removeFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeLast() {
/* 1893 */       synchronized (this.mutex) {
/* 1894 */         return delegate().removeLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1900 */       synchronized (this.mutex) {
/* 1901 */         return delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1907 */       synchronized (this.mutex) {
/* 1908 */         return delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getFirst() {
/* 1914 */       synchronized (this.mutex) {
/* 1915 */         return delegate().getFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getLast() {
/* 1921 */       synchronized (this.mutex) {
/* 1922 */         return delegate().getLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekFirst() {
/* 1928 */       synchronized (this.mutex) {
/* 1929 */         return delegate().peekFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekLast() {
/* 1935 */       synchronized (this.mutex) {
/* 1936 */         return delegate().peekLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeFirstOccurrence(Object o) {
/* 1942 */       synchronized (this.mutex) {
/* 1943 */         return delegate().removeFirstOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeLastOccurrence(Object o) {
/* 1949 */       synchronized (this.mutex) {
/* 1950 */         return delegate().removeLastOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void push(E e) {
/* 1956 */       synchronized (this.mutex) {
/* 1957 */         delegate().push(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pop() {
/* 1963 */       synchronized (this.mutex) {
/* 1964 */         return delegate().pop();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1970 */       synchronized (this.mutex) {
/* 1971 */         return delegate().descendingIterator();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Synchronized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */