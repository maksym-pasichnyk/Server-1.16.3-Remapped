/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
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
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*   74 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*   76 */       return (Multiset)multiset;
/*      */     }
/*      */     
/*   79 */     return new UnmodifiableMultiset<>((Multiset<? extends E>)Preconditions.checkNotNull(multiset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) {
/*   90 */     return (Multiset<E>)Preconditions.checkNotNull(multiset);
/*      */   }
/*      */   
/*      */   static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable { final Multiset<? extends E> delegate;
/*      */     transient Set<E> elementSet;
/*      */     
/*      */     UnmodifiableMultiset(Multiset<? extends E> delegate) {
/*   97 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */     transient Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     protected Multiset<E> delegate() {
/*  104 */       return (Multiset)this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  110 */       return Collections.unmodifiableSet(this.delegate.elementSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  115 */       Set<E> es = this.elementSet;
/*  116 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  124 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  125 */       return (es == null) ? (this
/*      */ 
/*      */         
/*  128 */         .entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  134 */       return Iterators.unmodifiableIterator(this.delegate.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E element) {
/*  139 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E element, int occurences) {
/*  144 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> elementsToAdd) {
/*  149 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object element) {
/*  154 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object element, int occurrences) {
/*  159 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> elementsToRemove) {
/*  164 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> elementsToRetain) {
/*  169 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  174 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  179 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  184 */       throw new UnsupportedOperationException();
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
/*      */   @Beta
/*      */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
/*  207 */     return new UnmodifiableSortedMultiset<>((SortedMultiset<E>)Preconditions.checkNotNull(sortedMultiset));
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
/*      */   public static <E> Multiset.Entry<E> immutableEntry(@Nullable E e, int n) {
/*  219 */     return new ImmutableEntry<>(e, n);
/*      */   }
/*      */   
/*      */   static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
/*      */     @Nullable
/*      */     private final E element;
/*      */     
/*      */     ImmutableEntry(@Nullable E element, int count) {
/*  227 */       this.element = element;
/*  228 */       this.count = count;
/*  229 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */     private final int count; private static final long serialVersionUID = 0L;
/*      */     
/*      */     @Nullable
/*      */     public final E getElement() {
/*  235 */       return this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int getCount() {
/*  240 */       return this.count;
/*      */     }
/*      */     
/*      */     public ImmutableEntry<E> nextInBucket() {
/*  244 */       return null;
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  278 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  281 */       FilteredMultiset<E> filtered = (FilteredMultiset<E>)unfiltered;
/*  282 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*  283 */       return new FilteredMultiset<>(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  285 */     return new FilteredMultiset<>(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  293 */       this.unfiltered = (Multiset<E>)Preconditions.checkNotNull(unfiltered);
/*  294 */       this.predicate = (Predicate<? super E>)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public UnmodifiableIterator<E> iterator() {
/*  299 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<E> createElementSet() {
/*  304 */       return Sets.filter(this.unfiltered.elementSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  309 */       return Sets.filter(this.unfiltered
/*  310 */           .entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry)
/*      */             {
/*  314 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<E>> entryIterator() {
/*  321 */       throw new AssertionError("should never be called");
/*      */     }
/*      */ 
/*      */     
/*      */     int distinctElements() {
/*  326 */       return elementSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public int count(@Nullable Object element) {
/*  331 */       int count = this.unfiltered.count(element);
/*  332 */       if (count > 0) {
/*      */         
/*  334 */         E e = (E)element;
/*  335 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  337 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@Nullable E element, int occurrences) {
/*  342 */       Preconditions.checkArgument(this.predicate
/*  343 */           .apply(element), "Element %s does not match predicate %s", element, this.predicate);
/*  344 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@Nullable Object element, int occurrences) {
/*  349 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  350 */       if (occurrences == 0) {
/*  351 */         return count(element);
/*      */       }
/*  353 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  359 */       elementSet().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  370 */     if (elements instanceof Multiset) {
/*  371 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  373 */     return 11;
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  394 */     Preconditions.checkNotNull(multiset1);
/*  395 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  397 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  400 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  405 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  410 */           return Math.max(multiset1.count(element), multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  415 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  420 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  421 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  423 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  426 */                 if (iterator1.hasNext()) {
/*  427 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  428 */                   E element = entry1.getElement();
/*  429 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  430 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  432 */                 while (iterator2.hasNext()) {
/*  433 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  434 */                   E element = entry2.getElement();
/*  435 */                   if (!multiset1.contains(element)) {
/*  436 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  439 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  446 */           return elementSet().size();
/*      */         }
/*      */       };
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
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  467 */     Preconditions.checkNotNull(multiset1);
/*  468 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  470 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  473 */           int count1 = multiset1.count(element);
/*  474 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  479 */           return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  484 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  486 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  489 */                 while (iterator1.hasNext()) {
/*  490 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  491 */                   E element = entry1.getElement();
/*  492 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  493 */                   if (count > 0) {
/*  494 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  497 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  504 */           return elementSet().size();
/*      */         }
/*      */       };
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  527 */     Preconditions.checkNotNull(multiset1);
/*  528 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  531 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  534 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  539 */           return (multiset1.isEmpty() && multiset2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  544 */           return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
/*      */         }
/*      */ 
/*      */         
/*      */         public int count(Object element) {
/*  549 */           return multiset1.count(element) + multiset2.count(element);
/*      */         }
/*      */ 
/*      */         
/*      */         Set<E> createElementSet() {
/*  554 */           return Sets.union(multiset1.elementSet(), multiset2.elementSet());
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  559 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*  560 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*  561 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  564 */                 if (iterator1.hasNext()) {
/*  565 */                   Multiset.Entry<? extends E> entry1 = iterator1.next();
/*  566 */                   E element = entry1.getElement();
/*  567 */                   int count = entry1.getCount() + multiset2.count(element);
/*  568 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  570 */                 while (iterator2.hasNext()) {
/*  571 */                   Multiset.Entry<? extends E> entry2 = iterator2.next();
/*  572 */                   E element = entry2.getElement();
/*  573 */                   if (!multiset1.contains(element)) {
/*  574 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  577 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  584 */           return elementSet().size();
/*      */         }
/*      */       };
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  607 */     Preconditions.checkNotNull(multiset1);
/*  608 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  611 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(@Nullable Object element) {
/*  614 */           int count1 = multiset1.count(element);
/*  615 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  620 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  621 */           return new AbstractIterator()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  624 */                 while (iterator1.hasNext()) {
/*  625 */                   Multiset.Entry<E> entry1 = iterator1.next();
/*  626 */                   E element = entry1.getElement();
/*  627 */                   int count = entry1.getCount() - multiset2.count(element);
/*  628 */                   if (count > 0) {
/*  629 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  632 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         int distinctElements() {
/*  639 */           return Iterators.size(entryIterator());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  652 */     Preconditions.checkNotNull(superMultiset);
/*  653 */     Preconditions.checkNotNull(subMultiset);
/*  654 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  655 */       int superCount = superMultiset.count(entry.getElement());
/*  656 */       if (superCount < entry.getCount()) {
/*  657 */         return false;
/*      */       }
/*      */     } 
/*  660 */     return true;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) {
/*  685 */     return retainOccurrencesImpl(multisetToModify, multisetToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  693 */     Preconditions.checkNotNull(multisetToModify);
/*  694 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  696 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  697 */     boolean changed = false;
/*  698 */     while (entryIterator.hasNext()) {
/*  699 */       Multiset.Entry<E> entry = entryIterator.next();
/*  700 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  701 */       if (retainCount == 0) {
/*  702 */         entryIterator.remove();
/*  703 */         changed = true; continue;
/*  704 */       }  if (retainCount < entry.getCount()) {
/*  705 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  706 */         changed = true;
/*      */       } 
/*      */     } 
/*  709 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Iterable<?> occurrencesToRemove) {
/*  739 */     if (occurrencesToRemove instanceof Multiset) {
/*  740 */       return removeOccurrences(multisetToModify, (Multiset)occurrencesToRemove);
/*      */     }
/*  742 */     Preconditions.checkNotNull(multisetToModify);
/*  743 */     Preconditions.checkNotNull(occurrencesToRemove);
/*  744 */     boolean changed = false;
/*  745 */     for (Object o : occurrencesToRemove) {
/*  746 */       changed |= multisetToModify.remove(o);
/*      */     }
/*  748 */     return changed;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  778 */     Preconditions.checkNotNull(multisetToModify);
/*  779 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  781 */     boolean changed = false;
/*  782 */     Iterator<? extends Multiset.Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
/*  783 */     while (entryIterator.hasNext()) {
/*  784 */       Multiset.Entry<?> entry = entryIterator.next();
/*  785 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  786 */       if (removeCount >= entry.getCount()) {
/*  787 */         entryIterator.remove();
/*  788 */         changed = true; continue;
/*  789 */       }  if (removeCount > 0) {
/*  790 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  791 */         changed = true;
/*      */       } 
/*      */     } 
/*  794 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(@Nullable Object object) {
/*  808 */       if (object instanceof Multiset.Entry) {
/*  809 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  810 */         return (getCount() == that.getCount() && 
/*  811 */           Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*  813 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  822 */       E e = getElement();
/*  823 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  835 */       String text = String.valueOf(getElement());
/*  836 */       int n = getCount();
/*  837 */       return (n == 1) ? text : (text + " x " + n);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
/*  845 */     if (object == multiset) {
/*  846 */       return true;
/*      */     }
/*  848 */     if (object instanceof Multiset) {
/*  849 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  856 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
/*  857 */         return false;
/*      */       }
/*  859 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  860 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  861 */           return false;
/*      */         }
/*      */       } 
/*  864 */       return true;
/*      */     } 
/*  866 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  873 */     if (elements.isEmpty()) {
/*  874 */       return false;
/*      */     }
/*  876 */     if (elements instanceof Multiset) {
/*  877 */       Multiset<? extends E> that = cast(elements);
/*  878 */       for (Multiset.Entry<? extends E> entry : that.entrySet()) {
/*  879 */         self.add(entry.getElement(), entry.getCount());
/*      */       }
/*      */     } else {
/*  882 */       Iterators.addAll(self, elements.iterator());
/*      */     } 
/*  884 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  893 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */ 
/*      */     
/*  896 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  903 */     Preconditions.checkNotNull(elementsToRetain);
/*      */ 
/*      */     
/*  906 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */ 
/*      */     
/*  909 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/*  916 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  918 */     int oldCount = self.count(element);
/*      */     
/*  920 */     int delta = count - oldCount;
/*  921 */     if (delta > 0) {
/*  922 */       self.add(element, delta);
/*  923 */     } else if (delta < 0) {
/*  924 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  927 */     return oldCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/*  934 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  935 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  937 */     if (self.count(element) == oldCount) {
/*  938 */       self.setCount(element, newCount);
/*  939 */       return true;
/*      */     } 
/*  941 */     return false;
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E>
/*      */   {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public void clear() {
/*  950 */       multiset().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  955 */       return multiset().contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  960 */       return multiset().containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  965 */       return multiset().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  970 */       return new TransformedIterator<Multiset.Entry<E>, E>(multiset().entrySet().iterator())
/*      */         {
/*      */           E transform(Multiset.Entry<E> entry) {
/*  973 */             return entry.getElement();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  980 */       return (multiset().remove(o, 2147483647) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  985 */       return multiset().entrySet().size();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E>
/*      */     extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/*  994 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  999 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1000 */         if (entry.getCount() <= 0) {
/* 1001 */           return false;
/*      */         }
/* 1003 */         int count = multiset().count(entry.getElement());
/* 1004 */         return (count == entry.getCount());
/*      */       } 
/* 1006 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object object) {
/* 1013 */       if (object instanceof Multiset.Entry) {
/* 1014 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/* 1015 */         Object element = entry.getElement();
/* 1016 */         int entryCount = entry.getCount();
/* 1017 */         if (entryCount != 0) {
/*      */ 
/*      */           
/* 1020 */           Multiset<Object> multiset = (Multiset)multiset();
/* 1021 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/* 1024 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1029 */       multiset().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
/* 1037 */     return new MultisetIteratorImpl<>(multiset, multiset.entrySet().iterator());
/*      */   }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1054 */       this.multiset = multiset;
/* 1055 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1060 */       return (this.laterCount > 0 || this.entryIterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1065 */       if (!hasNext()) {
/* 1066 */         throw new NoSuchElementException();
/*      */       }
/* 1068 */       if (this.laterCount == 0) {
/* 1069 */         this.currentEntry = this.entryIterator.next();
/* 1070 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1072 */       this.laterCount--;
/* 1073 */       this.canRemove = true;
/* 1074 */       return this.currentEntry.getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1079 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1080 */       if (this.totalCount == 1) {
/* 1081 */         this.entryIterator.remove();
/*      */       } else {
/* 1083 */         this.multiset.remove(this.currentEntry.getElement());
/*      */       } 
/* 1085 */       this.totalCount--;
/* 1086 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */   
/*      */   static <E> Spliterator<E> spliteratorImpl(Multiset<E> multiset) {
/* 1091 */     Spliterator<Multiset.Entry<E>> entrySpliterator = multiset.entrySet().spliterator();
/* 1092 */     return CollectSpliterators.flatMap(entrySpliterator, entry -> Collections.nCopies(entry.getCount(), entry.getElement()).spliterator(), 0x40 | entrySpliterator
/*      */ 
/*      */ 
/*      */         
/* 1096 */         .characteristics() & 0x510, multiset
/*      */         
/* 1098 */         .size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int sizeImpl(Multiset<?> multiset) {
/* 1105 */     long size = 0L;
/* 1106 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1107 */       size += entry.getCount();
/*      */     }
/* 1109 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Multiset<T> cast(Iterable<T> iterable) {
/* 1116 */     return (Multiset<T>)iterable;
/*      */   }
/*      */   
/* 1119 */   private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>()
/*      */     {
/*      */       public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2)
/*      */       {
/* 1123 */         return Ints.compare(entry2.getCount(), entry1.getCount());
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1136 */     List<Multiset.Entry<E>> sortedEntries = DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet());
/* 1137 */     return ImmutableMultiset.copyFromEntries(sortedEntries);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Multisets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */