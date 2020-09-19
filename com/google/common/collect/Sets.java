/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
/*      */ import java.util.function.Predicate;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public boolean removeAll(Collection<?> c) {
/*   77 */       return Sets.removeAllImpl(this, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*   82 */       return super.retainAll((Collection)Preconditions.checkNotNull(c));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
/*  101 */     return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  118 */     if (elements instanceof ImmutableEnumSet)
/*  119 */       return (ImmutableEnumSet)elements; 
/*  120 */     if (elements instanceof Collection) {
/*  121 */       Collection<E> collection = (Collection<E>)elements;
/*  122 */       if (collection.isEmpty()) {
/*  123 */         return ImmutableSet.of();
/*      */       }
/*  125 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  128 */     Iterator<E> itr = elements.iterator();
/*  129 */     if (itr.hasNext()) {
/*  130 */       EnumSet<E> enumSet = EnumSet.of(itr.next());
/*  131 */       Iterators.addAll(enumSet, itr);
/*  132 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  134 */     return ImmutableSet.of();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Accumulator<E extends Enum<E>>
/*      */   {
/*  143 */     static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET = Collector.of(Accumulator::new, Accumulator::add, Accumulator::combine, Accumulator::toImmutableSet, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*      */ 
/*      */ 
/*      */     
/*      */     private EnumSet<E> set;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void add(E e) {
/*  153 */       if (this.set == null) {
/*  154 */         this.set = EnumSet.of(e);
/*      */       } else {
/*  156 */         this.set.add(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     Accumulator<E> combine(Accumulator<E> other) {
/*  161 */       if (this.set == null)
/*  162 */         return other; 
/*  163 */       if (other.set == null) {
/*  164 */         return this;
/*      */       }
/*  166 */       this.set.addAll(other.set);
/*  167 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableSet<E> toImmutableSet() {
/*  172 */       return (this.set == null) ? ImmutableSet.<E>of() : ImmutableEnumSet.asImmutable(this.set);
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
/*      */   @Beta
/*      */   public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
/*  185 */     return (Collector)Accumulator.TO_IMMUTABLE_ENUM_SET;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  195 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  196 */     Iterables.addAll(set, iterable);
/*  197 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSet() {
/*  215 */     return new HashSet<>();
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
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  232 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  233 */     Collections.addAll(set, elements);
/*  234 */     return set;
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
/*      */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
/*  250 */     return new HashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
/*  272 */     return (elements instanceof Collection) ? new HashSet<>(
/*  273 */         Collections2.cast(elements)) : 
/*  274 */       newHashSet(elements.iterator());
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
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  290 */     HashSet<E> set = newHashSet();
/*  291 */     Iterators.addAll(set, elements);
/*  292 */     return set;
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
/*      */   public static <E> Set<E> newConcurrentHashSet() {
/*  307 */     return Collections.newSetFromMap(new ConcurrentHashMap<>());
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
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  325 */     Set<E> set = newConcurrentHashSet();
/*  326 */     Iterables.addAll(set, elements);
/*  327 */     return set;
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet() {
/*  344 */     return new LinkedHashSet<>();
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  360 */     return new LinkedHashSet<>(Maps.capacity(expectedSize));
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
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  379 */     if (elements instanceof Collection) {
/*  380 */       return new LinkedHashSet<>(Collections2.cast(elements));
/*      */     }
/*  382 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  383 */     Iterables.addAll(set, elements);
/*  384 */     return set;
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet() {
/*  402 */     return new TreeSet<>();
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
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  427 */     TreeSet<E> set = newTreeSet();
/*  428 */     Iterables.addAll(set, elements);
/*  429 */     return set;
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
/*      */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
/*  449 */     return new TreeSet<>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
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
/*      */   public static <E> Set<E> newIdentityHashSet() {
/*  463 */     return Collections.newSetFromMap(Maps.newIdentityHashMap());
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
/*  477 */     return new CopyOnWriteArraySet<>();
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  494 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : Lists.<E>newArrayList(elements);
/*  495 */     return new CopyOnWriteArraySet<>(elementsCollection);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  514 */     if (collection instanceof EnumSet) {
/*  515 */       return EnumSet.complementOf((EnumSet<E>)collection);
/*      */     }
/*  517 */     Preconditions.checkArgument(
/*  518 */         !collection.isEmpty(), "collection is empty; use the other version of this method");
/*  519 */     Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
/*  520 */     return makeComplementByHand(collection, type);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  537 */     Preconditions.checkNotNull(collection);
/*  538 */     return (collection instanceof EnumSet) ? 
/*  539 */       EnumSet.<E>complementOf((EnumSet<E>)collection) : 
/*  540 */       makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  545 */     EnumSet<E> result = EnumSet.allOf(type);
/*  546 */     result.removeAll(collection);
/*  547 */     return result;
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
/*      */   @Deprecated
/*      */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
/*  583 */     return Collections.newSetFromMap(map);
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
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableSet<E> immutableCopy() {
/*  609 */       return ImmutableSet.copyOf(this);
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
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  623 */       set.addAll(this);
/*  624 */       return set;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean add(E e) {
/*  637 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean remove(Object object) {
/*  650 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean addAll(Collection<? extends E> newElements) {
/*  663 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeAll(Collection<?> oldElements) {
/*  676 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean removeIf(Predicate<? super E> filter) {
/*  689 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean retainAll(Collection<?> elementsToKeep) {
/*  702 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public final void clear() {
/*  714 */       throw new UnsupportedOperationException();
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
/*      */     
/*      */     public abstract UnmodifiableIterator<E> iterator();
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  738 */     Preconditions.checkNotNull(set1, "set1");
/*  739 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  741 */     final Set<? extends E> set2minus1 = difference(set2, set1);
/*      */     
/*  743 */     return new SetView<E>()
/*      */       {
/*      */         public int size() {
/*  746 */           return IntMath.saturatedAdd(set1.size(), set2minus1.size());
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  751 */           return (set1.isEmpty() && set2.isEmpty());
/*      */         }
/*      */ 
/*      */         
/*      */         public UnmodifiableIterator<E> iterator() {
/*  756 */           return Iterators.unmodifiableIterator(
/*  757 */               Iterators.concat(set1.iterator(), set2minus1.iterator()));
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  762 */           return Stream.concat(set1.stream(), set2minus1.stream());
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  767 */           return Stream.concat(set1.parallelStream(), set2minus1.parallelStream());
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  772 */           return (set1.contains(object) || set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  777 */           set.addAll(set1);
/*  778 */           set.addAll(set2);
/*  779 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public ImmutableSet<E> immutableCopy() {
/*  784 */           return (new ImmutableSet.Builder<>()).addAll(set1).addAll(set2).build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  816 */     Preconditions.checkNotNull(set1, "set1");
/*  817 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  819 */     final Predicate<Object> inSet2 = Predicates.in(set2);
/*  820 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  823 */           return Iterators.filter(set1.iterator(), inSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  828 */           return set1.stream().filter((Predicate<? super E>)inSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  833 */           return set1.parallelStream().filter((Predicate<? super E>)inSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  838 */           return Iterators.size(iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  843 */           return !iterator().hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object object) {
/*  848 */           return (set1.contains(object) && set2.contains(object));
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean containsAll(Collection<?> collection) {
/*  853 */           return (set1.containsAll(collection) && set2.containsAll(collection));
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  870 */     Preconditions.checkNotNull(set1, "set1");
/*  871 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  873 */     final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
/*  874 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  877 */           return Iterators.filter(set1.iterator(), notInSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> stream() {
/*  882 */           return set1.stream().filter((Predicate<? super E>)notInSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public Stream<E> parallelStream() {
/*  887 */           return set1.parallelStream().filter((Predicate<? super E>)notInSet2);
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  892 */           return Iterators.size(iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  897 */           return set2.containsAll(set1);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/*  902 */           return (set1.contains(element) && !set2.contains(element));
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
/*      */   public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  921 */     Preconditions.checkNotNull(set1, "set1");
/*  922 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  924 */     return new SetView<E>()
/*      */       {
/*      */         public UnmodifiableIterator<E> iterator() {
/*  927 */           final Iterator<? extends E> itr1 = set1.iterator();
/*  928 */           final Iterator<? extends E> itr2 = set2.iterator();
/*  929 */           return new AbstractIterator()
/*      */             {
/*      */               public E computeNext() {
/*  932 */                 while (itr1.hasNext()) {
/*  933 */                   E elem1 = itr1.next();
/*  934 */                   if (!set2.contains(elem1)) {
/*  935 */                     return elem1;
/*      */                   }
/*      */                 } 
/*  938 */                 while (itr2.hasNext()) {
/*  939 */                   E elem2 = itr2.next();
/*  940 */                   if (!set1.contains(elem2)) {
/*  941 */                     return elem2;
/*      */                   }
/*      */                 } 
/*  944 */                 return endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public int size() {
/*  951 */           return Iterators.size(iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isEmpty() {
/*  956 */           return set1.equals(set2);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean contains(Object element) {
/*  961 */           return set1.contains(element) ^ set2.contains(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/*  998 */     if (unfiltered instanceof SortedSet) {
/*  999 */       return filter((SortedSet<E>)unfiltered, predicate);
/*      */     }
/* 1001 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1004 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1005 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1006 */       return new FilteredSet<>((Set<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1009 */     return new FilteredSet<>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
/*      */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
/* 1014 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1019 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1024 */       return Sets.hashCodeImpl(this);
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
/*      */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1058 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1061 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1062 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1063 */       return new FilteredSortedSet<>((SortedSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1066 */     return new FilteredSortedSet<>((SortedSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E> implements SortedSet<E> {
/*      */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1072 */       super(unfiltered, predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1077 */       return ((SortedSet<E>)this.unfiltered).comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1082 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered)
/* 1083 */           .subSet(fromElement, toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1088 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).headSet(toElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1093 */       return new FilteredSortedSet(((SortedSet<E>)this.unfiltered).tailSet(fromElement), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 1098 */       return iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1103 */       SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
/*      */       while (true) {
/* 1105 */         E element = sortedUnfiltered.last();
/* 1106 */         if (this.predicate.apply(element)) {
/* 1107 */           return element;
/*      */         }
/* 1109 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
/*      */       } 
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
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1147 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/* 1150 */       FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
/* 1151 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/* 1152 */       return new FilteredNavigableSet<>((NavigableSet<E>)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */     
/* 1155 */     return new FilteredNavigableSet<>((NavigableSet<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E> implements NavigableSet<E> {
/*      */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/* 1162 */       super(unfiltered, predicate);
/*      */     }
/*      */     
/*      */     NavigableSet<E> unfiltered() {
/* 1166 */       return (NavigableSet<E>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public E lower(E e) {
/* 1172 */       return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public E floor(E e) {
/* 1178 */       return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1183 */       return Iterables.getFirst(tailSet(e, true), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1188 */       return Iterables.getFirst(tailSet(e, false), null);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1193 */       return Iterables.removeFirstMatching(unfiltered(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1198 */       return Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1203 */       return Sets.filter(unfiltered().descendingSet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1208 */       return Iterators.filter(unfiltered().descendingIterator(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1213 */       return descendingIterator().next();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1219 */       return Sets.filter(
/* 1220 */           unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1225 */       return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1230 */       return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
/* 1290 */     return CartesianSet.create(sets);
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
/*      */   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
/* 1349 */     return cartesianProduct(Arrays.asList(sets));
/*      */   }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>>
/*      */     implements Set<List<E>> {
/*      */     private final transient ImmutableList<ImmutableSet<E>> axes;
/*      */     private final transient CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1359 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<>(sets.size());
/* 1360 */       for (Set<? extends E> set : sets) {
/* 1361 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1362 */         if (copy.isEmpty()) {
/* 1363 */           return ImmutableSet.of();
/*      */         }
/* 1365 */         axesBuilder.add(copy);
/*      */       } 
/* 1367 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1368 */       ImmutableList<List<E>> listAxes = (ImmutableList)new ImmutableList<List<List<E>>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1372 */             return axes.size();
/*      */           }
/*      */ 
/*      */           
/*      */           public List<E> get(int index) {
/* 1377 */             return ((ImmutableSet<E>)axes.get(index)).asList();
/*      */           }
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1382 */             return true;
/*      */           }
/*      */         };
/* 1385 */       return new CartesianSet<>(axes, new CartesianList<>(listAxes));
/*      */     }
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1389 */       this.axes = axes;
/* 1390 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<List<E>> delegate() {
/* 1395 */       return this.delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1402 */       if (object instanceof CartesianSet) {
/* 1403 */         CartesianSet<?> that = (CartesianSet)object;
/* 1404 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1406 */       return super.equals(object);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1415 */       int adjust = size() - 1;
/* 1416 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1417 */         adjust *= 31;
/* 1418 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1421 */       int hash = 1;
/* 1422 */       for (UnmodifiableIterator<ImmutableSet<E>> unmodifiableIterator = this.axes.iterator(); unmodifiableIterator.hasNext(); ) { Set<E> axis = unmodifiableIterator.next();
/* 1423 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1425 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF; }
/*      */       
/* 1427 */       hash += adjust;
/* 1428 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/*      */   public static <E> Set<Set<E>> powerSet(Set<E> set) {
/* 1463 */     return new PowerSet<>(set);
/*      */   }
/*      */   
/*      */   private static final class SubSet<E> extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1471 */       this.inputSet = inputSet;
/* 1472 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1477 */       return new UnmodifiableIterator<E>() {
/* 1478 */           final ImmutableList<E> elements = Sets.SubSet.this.inputSet.keySet().asList();
/* 1479 */           int remainingSetBits = Sets.SubSet.this.mask;
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/* 1483 */             return (this.remainingSetBits != 0);
/*      */           }
/*      */ 
/*      */           
/*      */           public E next() {
/* 1488 */             int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
/* 1489 */             if (index == 32) {
/* 1490 */               throw new NoSuchElementException();
/*      */             }
/* 1492 */             this.remainingSetBits &= 1 << index ^ 0xFFFFFFFF;
/* 1493 */             return this.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1500 */       return Integer.bitCount(this.mask);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 1505 */       Integer index = this.inputSet.get(o);
/* 1506 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1514 */       this.inputSet = Maps.indexMap(input);
/* 1515 */       Preconditions.checkArgument(
/* 1516 */           (this.inputSet.size() <= 30), "Too many elements to create power set: %s > 30", this.inputSet.size());
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1521 */       return 1 << this.inputSet.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1526 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1531 */       return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(size())
/*      */         {
/*      */           protected Set<E> get(int setBits) {
/* 1534 */             return new Sets.SubSet<>(Sets.PowerSet.this.inputSet, setBits);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object obj) {
/* 1541 */       if (obj instanceof Set) {
/* 1542 */         Set<?> set = (Set)obj;
/* 1543 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1545 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object obj) {
/* 1550 */       if (obj instanceof PowerSet) {
/* 1551 */         PowerSet<?> that = (PowerSet)obj;
/* 1552 */         return this.inputSet.equals(that.inputSet);
/*      */       } 
/* 1554 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1564 */       return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1569 */       return "powerSet(" + this.inputSet + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1577 */     int hashCode = 0;
/* 1578 */     for (Object o : s) {
/* 1579 */       hashCode += (o != null) ? o.hashCode() : 0;
/*      */       
/* 1581 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1584 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, @Nullable Object object) {
/* 1591 */     if (s == object) {
/* 1592 */       return true;
/*      */     }
/* 1594 */     if (object instanceof Set) {
/* 1595 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1598 */         return (s.size() == o.size() && s.containsAll(o));
/* 1599 */       } catch (NullPointerException ignored) {
/* 1600 */         return false;
/* 1601 */       } catch (ClassCastException ignored) {
/* 1602 */         return false;
/*      */       } 
/*      */     } 
/* 1605 */     return false;
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
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1625 */     if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet) {
/* 1626 */       return set;
/*      */     }
/* 1628 */     return new UnmodifiableNavigableSet<>(set);
/*      */   }
/*      */   
/*      */   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable { private final NavigableSet<E> delegate;
/*      */     private transient UnmodifiableNavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableNavigableSet(NavigableSet<E> delegate) {
/* 1636 */       this.delegate = (NavigableSet<E>)Preconditions.checkNotNull(delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedSet<E> delegate() {
/* 1641 */       return Collections.unmodifiableSortedSet(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1646 */       return this.delegate.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1651 */       return this.delegate.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1656 */       return this.delegate.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1661 */       return this.delegate.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1666 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1671 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1678 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1679 */       if (result == null) {
/* 1680 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/* 1681 */         result.descendingSet = this;
/*      */       } 
/* 1683 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1688 */       return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1694 */       return Sets.unmodifiableNavigableSet(this.delegate
/* 1695 */           .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1700 */       return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1705 */       return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
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
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
/* 1756 */     return Synchronized.navigableSet(navigableSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1763 */     boolean changed = false;
/* 1764 */     while (iterator.hasNext()) {
/* 1765 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1767 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 1771 */     Preconditions.checkNotNull(collection);
/* 1772 */     if (collection instanceof Multiset) {
/* 1773 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1782 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 1783 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 1785 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E> {
/*      */     private final NavigableSet<E> forward;
/*      */     
/*      */     DescendingSet(NavigableSet<E> forward) {
/* 1794 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableSet<E> delegate() {
/* 1799 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public E lower(E e) {
/* 1804 */       return this.forward.higher(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E floor(E e) {
/* 1809 */       return this.forward.ceiling(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1814 */       return this.forward.floor(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1819 */       return this.forward.lower(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1824 */       return this.forward.pollLast();
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1829 */       return this.forward.pollFirst();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1834 */       return this.forward;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1839 */       return this.forward.iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1845 */       return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1850 */       return this.forward.tailSet(toElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1855 */       return this.forward.headSet(fromElement, inclusive).descendingSet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1861 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 1862 */       if (forwardComparator == null) {
/* 1863 */         return Ordering.<Comparable>natural().reverse();
/*      */       }
/* 1865 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 1871 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/* 1876 */       return this.forward.last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/* 1881 */       return standardHeadSet(toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/* 1886 */       return this.forward.first();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1891 */       return standardSubSet(fromElement, toElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/* 1896 */       return standardTailSet(fromElement);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1901 */       return this.forward.descendingIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1906 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1911 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1916 */       return standardToString();
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range) {
/* 1941 */     if (set.comparator() != null && set
/* 1942 */       .comparator() != Ordering.natural() && range
/* 1943 */       .hasLowerBound() && range
/* 1944 */       .hasUpperBound()) {
/* 1945 */       Preconditions.checkArgument(
/* 1946 */           (set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "set is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 1949 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 1950 */       return set.subSet(range
/* 1951 */           .lowerEndpoint(), 
/* 1952 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 1953 */           .upperEndpoint(), 
/* 1954 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 1955 */     if (range.hasLowerBound())
/* 1956 */       return set.tailSet(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 1957 */     if (range.hasUpperBound()) {
/* 1958 */       return set.headSet(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 1960 */     return (NavigableSet<K>)Preconditions.checkNotNull(set);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Sets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */