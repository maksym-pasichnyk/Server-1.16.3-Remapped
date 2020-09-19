/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.BiConsumer;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private transient Map<K, Collection<V>> map;
/*      */   private transient int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  123 */     Preconditions.checkArgument(map.isEmpty());
/*  124 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  129 */     this.map = map;
/*  130 */     this.totalSize = 0;
/*  131 */     for (Collection<V> values : map.values()) {
/*  132 */       Preconditions.checkArgument(!values.isEmpty());
/*  133 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> createUnmodifiableEmptyCollection() {
/*  143 */     return unmodifiableCollectionSubclass(createCollection());
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
/*      */   Collection<V> createCollection(@Nullable K key) {
/*  169 */     return createCollection();
/*      */   }
/*      */   
/*      */   Map<K, Collection<V>> backingMap() {
/*  173 */     return this.map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  180 */     return this.totalSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/*  185 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@Nullable K key, @Nullable V value) {
/*  192 */     Collection<V> collection = this.map.get(key);
/*  193 */     if (collection == null) {
/*  194 */       collection = createCollection(key);
/*  195 */       if (collection.add(value)) {
/*  196 */         this.totalSize++;
/*  197 */         this.map.put(key, collection);
/*  198 */         return true;
/*      */       } 
/*  200 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  202 */     if (collection.add(value)) {
/*  203 */       this.totalSize++;
/*  204 */       return true;
/*      */     } 
/*  206 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@Nullable K key) {
/*  211 */     Collection<V> collection = this.map.get(key);
/*  212 */     if (collection == null) {
/*  213 */       collection = createCollection(key);
/*  214 */       this.map.put(key, collection);
/*      */     } 
/*  216 */     return collection;
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
/*      */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  228 */     Iterator<? extends V> iterator = values.iterator();
/*  229 */     if (!iterator.hasNext()) {
/*  230 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  234 */     Collection<V> collection = getOrCreateCollection(key);
/*  235 */     Collection<V> oldValues = createCollection();
/*  236 */     oldValues.addAll(collection);
/*      */     
/*  238 */     this.totalSize -= collection.size();
/*  239 */     collection.clear();
/*      */     
/*  241 */     while (iterator.hasNext()) {
/*  242 */       if (collection.add(iterator.next())) {
/*  243 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  247 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@Nullable Object key) {
/*  257 */     Collection<V> collection = this.map.remove(key);
/*      */     
/*  259 */     if (collection == null) {
/*  260 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  263 */     Collection<V> output = createCollection();
/*  264 */     output.addAll(collection);
/*  265 */     this.totalSize -= collection.size();
/*  266 */     collection.clear();
/*      */     
/*  268 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */   
/*      */   static <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
/*  272 */     if (collection instanceof NavigableSet)
/*  273 */       return Sets.unmodifiableNavigableSet((NavigableSet<E>)collection); 
/*  274 */     if (collection instanceof SortedSet)
/*  275 */       return Collections.unmodifiableSortedSet((SortedSet<E>)collection); 
/*  276 */     if (collection instanceof Set)
/*  277 */       return Collections.unmodifiableSet((Set<? extends E>)collection); 
/*  278 */     if (collection instanceof List) {
/*  279 */       return Collections.unmodifiableList((List<? extends E>)collection);
/*      */     }
/*  281 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  288 */     for (Collection<V> collection : this.map.values()) {
/*  289 */       collection.clear();
/*      */     }
/*  291 */     this.map.clear();
/*  292 */     this.totalSize = 0;
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
/*      */   public Collection<V> get(@Nullable K key) {
/*  304 */     Collection<V> collection = this.map.get(key);
/*  305 */     if (collection == null) {
/*  306 */       collection = createCollection(key);
/*      */     }
/*  308 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
/*  317 */     if (collection instanceof NavigableSet)
/*  318 */       return new WrappedNavigableSet(key, (NavigableSet<V>)collection, null); 
/*  319 */     if (collection instanceof SortedSet)
/*  320 */       return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
/*  321 */     if (collection instanceof Set)
/*  322 */       return new WrappedSet(key, (Set<V>)collection); 
/*  323 */     if (collection instanceof List) {
/*  324 */       return wrapList(key, (List<V>)collection, null);
/*      */     }
/*  326 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */   
/*      */   private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) {
/*  331 */     return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(@Nullable K key, Collection<V> delegate, WrappedCollection ancestor) {
/*  362 */       this.key = key;
/*  363 */       this.delegate = delegate;
/*  364 */       this.ancestor = ancestor;
/*  365 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  376 */       if (this.ancestor != null) {
/*  377 */         this.ancestor.refreshIfEmpty();
/*  378 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  379 */           throw new ConcurrentModificationException();
/*      */         }
/*  381 */       } else if (this.delegate.isEmpty()) {
/*  382 */         Collection<V> newDelegate = (Collection<V>)AbstractMapBasedMultimap.this.map.get(this.key);
/*  383 */         if (newDelegate != null) {
/*  384 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  394 */       if (this.ancestor != null) {
/*  395 */         this.ancestor.removeIfEmpty();
/*  396 */       } else if (this.delegate.isEmpty()) {
/*  397 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */     
/*      */     K getKey() {
/*  402 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  413 */       if (this.ancestor != null) {
/*  414 */         this.ancestor.addToMap();
/*      */       } else {
/*  416 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  422 */       refreshIfEmpty();
/*  423 */       return this.delegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  428 */       if (object == this) {
/*  429 */         return true;
/*      */       }
/*  431 */       refreshIfEmpty();
/*  432 */       return this.delegate.equals(object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  437 */       refreshIfEmpty();
/*  438 */       return this.delegate.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  443 */       refreshIfEmpty();
/*  444 */       return this.delegate.toString();
/*      */     }
/*      */     
/*      */     Collection<V> getDelegate() {
/*  448 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  453 */       refreshIfEmpty();
/*  454 */       return new WrappedIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Spliterator<V> spliterator() {
/*  459 */       refreshIfEmpty();
/*  460 */       return this.delegate.spliterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator
/*      */       implements Iterator<V> {
/*      */       final Iterator<V> delegateIterator;
/*  466 */       final Collection<V> originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */       
/*      */       WrappedIterator() {
/*  469 */         this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       }
/*      */       
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*  473 */         this.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  481 */         AbstractMapBasedMultimap.WrappedCollection.this.refreshIfEmpty();
/*  482 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
/*  483 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  489 */         validateIterator();
/*  490 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public V next() {
/*  495 */         validateIterator();
/*  496 */         return this.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  501 */         this.delegateIterator.remove();
/*  502 */         AbstractMapBasedMultimap.this.totalSize--;
/*  503 */         AbstractMapBasedMultimap.WrappedCollection.this.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  507 */         validateIterator();
/*  508 */         return this.delegateIterator;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(V value) {
/*  514 */       refreshIfEmpty();
/*  515 */       boolean wasEmpty = this.delegate.isEmpty();
/*  516 */       boolean changed = this.delegate.add(value);
/*  517 */       if (changed) {
/*  518 */         AbstractMapBasedMultimap.this.totalSize++;
/*  519 */         if (wasEmpty) {
/*  520 */           addToMap();
/*      */         }
/*      */       } 
/*  523 */       return changed;
/*      */     }
/*      */     
/*      */     WrappedCollection getAncestor() {
/*  527 */       return this.ancestor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  534 */       if (collection.isEmpty()) {
/*  535 */         return false;
/*      */       }
/*  537 */       int oldSize = size();
/*  538 */       boolean changed = this.delegate.addAll(collection);
/*  539 */       if (changed) {
/*  540 */         int newSize = this.delegate.size();
/*  541 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  542 */         if (oldSize == 0) {
/*  543 */           addToMap();
/*      */         }
/*      */       } 
/*  546 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  551 */       refreshIfEmpty();
/*  552 */       return this.delegate.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  557 */       refreshIfEmpty();
/*  558 */       return this.delegate.containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  563 */       int oldSize = size();
/*  564 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  567 */       this.delegate.clear();
/*  568 */       AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - oldSize;
/*  569 */       removeIfEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  574 */       refreshIfEmpty();
/*  575 */       boolean changed = this.delegate.remove(o);
/*  576 */       if (changed) {
/*  577 */         AbstractMapBasedMultimap.this.totalSize--;
/*  578 */         removeIfEmpty();
/*      */       } 
/*  580 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  585 */       if (c.isEmpty()) {
/*  586 */         return false;
/*      */       }
/*  588 */       int oldSize = size();
/*  589 */       boolean changed = this.delegate.removeAll(c);
/*  590 */       if (changed) {
/*  591 */         int newSize = this.delegate.size();
/*  592 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  593 */         removeIfEmpty();
/*      */       } 
/*  595 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  600 */       Preconditions.checkNotNull(c);
/*  601 */       int oldSize = size();
/*  602 */       boolean changed = this.delegate.retainAll(c);
/*  603 */       if (changed) {
/*  604 */         int newSize = this.delegate.size();
/*  605 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  606 */         removeIfEmpty();
/*      */       } 
/*  608 */       return changed;
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
/*  613 */     return (collection instanceof List) ? ((List<E>)collection)
/*  614 */       .listIterator() : collection
/*  615 */       .iterator();
/*      */   }
/*      */   
/*      */   private class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V> {
/*      */     WrappedSet(K key, Set<V> delegate) {
/*  622 */       super(key, delegate, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  627 */       if (c.isEmpty()) {
/*  628 */         return false;
/*      */       }
/*  630 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  635 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  636 */       if (changed) {
/*  637 */         int newSize = this.delegate.size();
/*  638 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  639 */         removeIfEmpty();
/*      */       } 
/*  641 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*      */     WrappedSortedSet(@Nullable K key, SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  651 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     SortedSet<V> getSortedSetDelegate() {
/*  655 */       return (SortedSet<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super V> comparator() {
/*  660 */       return getSortedSetDelegate().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V first() {
/*  665 */       refreshIfEmpty();
/*  666 */       return getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public V last() {
/*  671 */       refreshIfEmpty();
/*  672 */       return getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  677 */       refreshIfEmpty();
/*  678 */       return new WrappedSortedSet(
/*  679 */           getKey(), 
/*  680 */           getSortedSetDelegate().headSet(toElement), 
/*  681 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  686 */       refreshIfEmpty();
/*  687 */       return new WrappedSortedSet(
/*  688 */           getKey(), 
/*  689 */           getSortedSetDelegate().subSet(fromElement, toElement), 
/*  690 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  695 */       refreshIfEmpty();
/*  696 */       return new WrappedSortedSet(
/*  697 */           getKey(), 
/*  698 */           getSortedSetDelegate().tailSet(fromElement), 
/*  699 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */   
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V> {
/*      */     WrappedNavigableSet(@Nullable K key, NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  707 */       super(key, delegate, ancestor);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<V> getSortedSetDelegate() {
/*  712 */       return (NavigableSet<V>)super.getSortedSetDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public V lower(V v) {
/*  717 */       return getSortedSetDelegate().lower(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V floor(V v) {
/*  722 */       return getSortedSetDelegate().floor(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V ceiling(V v) {
/*  727 */       return getSortedSetDelegate().ceiling(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V higher(V v) {
/*  732 */       return getSortedSetDelegate().higher(v);
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollFirst() {
/*  737 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public V pollLast() {
/*  742 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */     
/*      */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
/*  746 */       return new WrappedNavigableSet(this.key, wrapped, (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> descendingSet() {
/*  751 */       return wrap(getSortedSetDelegate().descendingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> descendingIterator() {
/*  756 */       return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) {
/*  762 */       return wrap(
/*  763 */           getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> headSet(V toElement, boolean inclusive) {
/*  768 */       return wrap(getSortedSetDelegate().headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<V> tailSet(V fromElement, boolean inclusive) {
/*  773 */       return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */   
/*      */   private class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V> {
/*      */     WrappedList(@Nullable K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  781 */       super(key, delegate, ancestor);
/*      */     }
/*      */     
/*      */     List<V> getListDelegate() {
/*  785 */       return (List<V>)getDelegate();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  790 */       if (c.isEmpty()) {
/*  791 */         return false;
/*      */       }
/*  793 */       int oldSize = size();
/*  794 */       boolean changed = getListDelegate().addAll(index, c);
/*  795 */       if (changed) {
/*  796 */         int newSize = getDelegate().size();
/*  797 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + newSize - oldSize;
/*  798 */         if (oldSize == 0) {
/*  799 */           addToMap();
/*      */         }
/*      */       } 
/*  802 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(int index) {
/*  807 */       refreshIfEmpty();
/*  808 */       return getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public V set(int index, V element) {
/*  813 */       refreshIfEmpty();
/*  814 */       return getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, V element) {
/*  819 */       refreshIfEmpty();
/*  820 */       boolean wasEmpty = getDelegate().isEmpty();
/*  821 */       getListDelegate().add(index, element);
/*  822 */       AbstractMapBasedMultimap.this.totalSize++;
/*  823 */       if (wasEmpty) {
/*  824 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(int index) {
/*  830 */       refreshIfEmpty();
/*  831 */       V value = getListDelegate().remove(index);
/*  832 */       AbstractMapBasedMultimap.this.totalSize--;
/*  833 */       removeIfEmpty();
/*  834 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  839 */       refreshIfEmpty();
/*  840 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  845 */       refreshIfEmpty();
/*  846 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  851 */       refreshIfEmpty();
/*  852 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  857 */       refreshIfEmpty();
/*  858 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  863 */       refreshIfEmpty();
/*  864 */       return AbstractMapBasedMultimap.this.wrapList(
/*  865 */           getKey(), 
/*  866 */           getListDelegate().subList(fromIndex, toIndex), 
/*  867 */           (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
/*      */       WrappedListIterator() {}
/*      */       
/*      */       public WrappedListIterator(int index) {
/*  875 */         super(AbstractMapBasedMultimap.WrappedList.this.getListDelegate().listIterator(index));
/*      */       }
/*      */       
/*      */       private ListIterator<V> getDelegateListIterator() {
/*  879 */         return (ListIterator<V>)getDelegateIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*  884 */         return getDelegateListIterator().hasPrevious();
/*      */       }
/*      */ 
/*      */       
/*      */       public V previous() {
/*  889 */         return getDelegateListIterator().previous();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*  894 */         return getDelegateListIterator().nextIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  899 */         return getDelegateListIterator().previousIndex();
/*      */       }
/*      */ 
/*      */       
/*      */       public void set(V value) {
/*  904 */         getDelegateListIterator().set(value);
/*      */       }
/*      */ 
/*      */       
/*      */       public void add(V value) {
/*  909 */         boolean wasEmpty = AbstractMapBasedMultimap.WrappedList.this.isEmpty();
/*  910 */         getDelegateListIterator().add(value);
/*  911 */         AbstractMapBasedMultimap.this.totalSize++;
/*  912 */         if (wasEmpty) {
/*  913 */           AbstractMapBasedMultimap.WrappedList.this.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*      */     RandomAccessWrappedList(@Nullable K key, List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
/*  926 */       super(key, delegate, ancestor);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   Set<K> createKeySet() {
/*  932 */     if (this.map instanceof NavigableMap)
/*  933 */       return new NavigableKeySet((NavigableMap<K, Collection<V>>)this.map); 
/*  934 */     if (this.map instanceof SortedMap) {
/*  935 */       return new SortedKeySet((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/*  937 */     return new KeySet(this.map);
/*      */   }
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>>
/*      */   {
/*      */     KeySet(Map<K, Collection<V>> subMap) {
/*  944 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  949 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*  950 */       return new Iterator<K>()
/*      */         {
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */           
/*      */           public boolean hasNext() {
/*  955 */             return entryIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public K next() {
/*  960 */             this.entry = entryIterator.next();
/*  961 */             return this.entry.getKey();
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  966 */             CollectPreconditions.checkRemove((this.entry != null));
/*  967 */             Collection<V> collection = this.entry.getValue();
/*  968 */             entryIterator.remove();
/*  969 */             AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
/*  970 */             collection.clear();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<K> spliterator() {
/*  979 */       return map().keySet().spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  984 */       int count = 0;
/*  985 */       Collection<V> collection = map().remove(key);
/*  986 */       if (collection != null) {
/*  987 */         count = collection.size();
/*  988 */         collection.clear();
/*  989 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - count;
/*      */       } 
/*  991 */       return (count > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  996 */       Iterators.clear(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/* 1001 */       return map().keySet().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1006 */       return (this == object || map().keySet().equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1011 */       return map().keySet().hashCode();
/*      */     }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet
/*      */     implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, Collection<V>> subMap) {
/* 1019 */       super(subMap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1023 */       return (SortedMap<K, Collection<V>>)map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1028 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 1033 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 1038 */       return new SortedKeySet(sortedMap().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 1043 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 1048 */       return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 1053 */       return new SortedKeySet(sortedMap().tailMap(fromElement));
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableKeySet
/*      */     extends SortedKeySet implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
/* 1060 */       super(subMap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1065 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K k) {
/* 1070 */       return sortedMap().lowerKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K k) {
/* 1075 */       return sortedMap().floorKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K k) {
/* 1080 */       return sortedMap().ceilingKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K k) {
/* 1085 */       return sortedMap().higherKey(k);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 1090 */       return Iterators.pollNext(iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 1095 */       return Iterators.pollNext(descendingIterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 1100 */       return new NavigableKeySet(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 1105 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement) {
/* 1110 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 1115 */       return new NavigableKeySet(sortedMap().headMap(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, K toElement) {
/* 1120 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 1126 */       return new NavigableKeySet(
/* 1127 */           sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement) {
/* 1132 */       return tailSet(fromElement, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 1137 */       return new NavigableKeySet(sortedMap().tailMap(fromElement, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeValuesForKey(Object key) {
/* 1145 */     Collection<V> collection = Maps.<Collection<V>>safeRemove(this.map, key);
/*      */     
/* 1147 */     if (collection != null) {
/* 1148 */       int count = collection.size();
/* 1149 */       collection.clear();
/* 1150 */       this.totalSize -= count;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class Itr<T>
/*      */     implements Iterator<T>
/*      */   {
/* 1161 */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
/* 1162 */     K key = null;
/* 1163 */     Collection<V> collection = null;
/* 1164 */     Iterator<V> valueIterator = Iterators.emptyModifiableIterator();
/*      */ 
/*      */     
/*      */     abstract T output(K param1K, V param1V);
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1171 */       return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1176 */       if (!this.valueIterator.hasNext()) {
/* 1177 */         Map.Entry<K, Collection<V>> mapEntry = this.keyIterator.next();
/* 1178 */         this.key = mapEntry.getKey();
/* 1179 */         this.collection = mapEntry.getValue();
/* 1180 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/* 1182 */       return output(this.key, this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1187 */       this.valueIterator.remove();
/* 1188 */       if (this.collection.isEmpty()) {
/* 1189 */         this.keyIterator.remove();
/*      */       }
/* 1191 */       AbstractMapBasedMultimap.this.totalSize--;
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
/*      */   public Collection<V> values() {
/* 1203 */     return super.values();
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1208 */     return new Itr<V>()
/*      */       {
/*      */         V output(K key, V value) {
/* 1211 */           return value;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<V> valueSpliterator() {
/* 1218 */     return CollectSpliterators.flatMap(this.map
/* 1219 */         .values().spliterator(), Collection::spliterator, 64, size());
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
/*      */   public Collection<Map.Entry<K, V>> entries() {
/* 1240 */     return super.entries();
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
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1253 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> output(K key, V value) {
/* 1256 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1263 */     return CollectSpliterators.flatMap(this.map
/* 1264 */         .entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }64, 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1272 */         size());
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1277 */     Preconditions.checkNotNull(action);
/* 1278 */     this.map.forEach((key, valueCollection) -> valueCollection.forEach(()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Map<K, Collection<V>> createAsMap() {
/* 1284 */     if (this.map instanceof NavigableMap)
/* 1285 */       return new NavigableAsMap((NavigableMap<K, Collection<V>>)this.map); 
/* 1286 */     if (this.map instanceof SortedMap) {
/* 1287 */       return new SortedAsMap((SortedMap<K, Collection<V>>)this.map);
/*      */     }
/* 1289 */     return new AsMap(this.map);
/*      */   }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ViewCachingAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final transient Map<K, Collection<V>> submap;
/*      */     
/*      */     AsMap(Map<K, Collection<V>> submap) {
/* 1302 */       this.submap = submap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 1307 */       return new AsMapEntries();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1314 */       return Maps.safeContainsKey(this.submap, key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1319 */       Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
/* 1320 */       if (collection == null) {
/* 1321 */         return null;
/*      */       }
/*      */       
/* 1324 */       K k = (K)key;
/* 1325 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1330 */       return AbstractMapBasedMultimap.this.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1335 */       return this.submap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1340 */       Collection<V> collection = this.submap.remove(key);
/* 1341 */       if (collection == null) {
/* 1342 */         return null;
/*      */       }
/*      */       
/* 1345 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1346 */       output.addAll(collection);
/* 1347 */       AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - collection.size();
/* 1348 */       collection.clear();
/* 1349 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1354 */       return (this == object || this.submap.equals(object));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1359 */       return this.submap.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1364 */       return this.submap.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1369 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1370 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1372 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1377 */       K key = entry.getKey();
/* 1378 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/*      */       Map<K, Collection<V>> map() {
/* 1385 */         return AbstractMapBasedMultimap.AsMap.this;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1390 */         return new AbstractMapBasedMultimap.AsMap.AsMapIterator();
/*      */       }
/*      */ 
/*      */       
/*      */       public Spliterator<Map.Entry<K, Collection<V>>> spliterator() {
/* 1395 */         return CollectSpliterators.map(AbstractMapBasedMultimap.AsMap.this.submap.entrySet().spliterator(), AbstractMapBasedMultimap.AsMap.this::wrapEntry);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean contains(Object o) {
/* 1402 */         return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1407 */         if (!contains(o)) {
/* 1408 */           return false;
/*      */         }
/* 1410 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 1411 */         AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
/* 1412 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */     class AsMapIterator
/*      */       implements Iterator<Map.Entry<K, Collection<V>>> {
/* 1418 */       final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator();
/*      */       
/*      */       Collection<V> collection;
/*      */       
/*      */       public boolean hasNext() {
/* 1423 */         return this.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1428 */         Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
/* 1429 */         this.collection = entry.getValue();
/* 1430 */         return AbstractMapBasedMultimap.AsMap.this.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1435 */         this.delegateIterator.remove();
/* 1436 */         AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - this.collection.size();
/* 1437 */         this.collection.clear();
/*      */       } }
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/*      */     SortedAsMap(SortedMap<K, Collection<V>> submap) {
/* 1445 */       super(submap);
/*      */     }
/*      */     
/*      */     SortedMap<K, Collection<V>> sortedMap() {
/* 1449 */       return (SortedMap<K, Collection<V>>)this.submap;
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1454 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1459 */       return sortedMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1464 */       return sortedMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> headMap(K toKey) {
/* 1469 */       return new SortedAsMap(sortedMap().headMap(toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1474 */       return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, Collection<V>> tailMap(K fromKey) {
/* 1479 */       return new SortedAsMap(sortedMap().tailMap(fromKey));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1488 */       SortedSet<K> result = this.sortedKeySet;
/* 1489 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 1494 */       return new AbstractMapBasedMultimap.SortedKeySet(sortedMap());
/*      */     }
/*      */   }
/*      */   
/*      */   class NavigableAsMap
/*      */     extends SortedAsMap implements NavigableMap<K, Collection<V>> {
/*      */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
/* 1501 */       super(submap);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, Collection<V>> sortedMap() {
/* 1506 */       return (NavigableMap<K, Collection<V>>)super.sortedMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(K key) {
/* 1511 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1512 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 1517 */       return sortedMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> floorEntry(K key) {
/* 1522 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1523 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 1528 */       return sortedMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
/* 1533 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1534 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1539 */       return sortedMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> higherEntry(K key) {
/* 1544 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1545 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 1550 */       return sortedMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1555 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1556 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1561 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1562 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollFirstEntry() {
/* 1567 */       return pollAsMapEntry(entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> pollLastEntry() {
/* 1572 */       return pollAsMapEntry(descendingMap().entrySet().iterator());
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1576 */       if (!entryIterator.hasNext()) {
/* 1577 */         return null;
/*      */       }
/* 1579 */       Map.Entry<K, Collection<V>> entry = entryIterator.next();
/* 1580 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1581 */       output.addAll(entry.getValue());
/* 1582 */       entryIterator.remove();
/* 1583 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> descendingMap() {
/* 1588 */       return new NavigableAsMap(sortedMap().descendingMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> keySet() {
/* 1593 */       return (NavigableSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableSet<K> createKeySet() {
/* 1598 */       return new AbstractMapBasedMultimap.NavigableKeySet(sortedMap());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1603 */       return keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1608 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) {
/* 1613 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1619 */       return new NavigableAsMap(sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey) {
/* 1624 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) {
/* 1629 */       return new NavigableAsMap(sortedMap().headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey) {
/* 1634 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) {
/* 1639 */       return new NavigableAsMap(sortedMap().tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractMapBasedMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */