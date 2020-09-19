/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ImmutableRangeSet<C extends Comparable>
/*     */   extends AbstractRangeSet<C>
/*     */   implements Serializable
/*     */ {
/*  50 */   private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet(
/*  51 */       ImmutableList.of());
/*     */   
/*  53 */   private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet(
/*  54 */       ImmutableList.of((Range)Range.all()));
/*     */   
/*     */   private final transient ImmutableList<Range<C>> ranges;
/*     */   @LazyInit
/*     */   private transient ImmutableRangeSet<C> complement;
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of() {
/*  61 */     return (ImmutableRangeSet)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <C extends Comparable> ImmutableRangeSet<C> all() {
/*  69 */     return (ImmutableRangeSet)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> of(Range<C> range) {
/*  77 */     Preconditions.checkNotNull(range);
/*  78 */     if (range.isEmpty())
/*  79 */       return of(); 
/*  80 */     if (range.equals(Range.all())) {
/*  81 */       return all();
/*     */     }
/*  83 */     return new ImmutableRangeSet<>(ImmutableList.of(range));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
/*  91 */     Preconditions.checkNotNull(rangeSet);
/*  92 */     if (rangeSet.isEmpty())
/*  93 */       return of(); 
/*  94 */     if (rangeSet.encloses((Range)Range.all())) {
/*  95 */       return all();
/*     */     }
/*     */     
/*  98 */     if (rangeSet instanceof ImmutableRangeSet) {
/*  99 */       ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)rangeSet;
/* 100 */       if (!immutableRangeSet.isPartialView()) {
/* 101 */         return immutableRangeSet;
/*     */       }
/*     */     } 
/* 104 */     return new ImmutableRangeSet<>(ImmutableList.copyOf(rangeSet.asRanges()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(Iterable<Range<C>> ranges) {
/* 116 */     return (ImmutableRangeSet)copyOf(TreeRangeSet.create(ranges));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(Iterable<Range<C>> ranges) {
/* 128 */     return (new Builder<>()).addAll(ranges).build();
/*     */   }
/*     */   
/*     */   ImmutableRangeSet(ImmutableList<Range<C>> ranges) {
/* 132 */     this.ranges = ranges;
/*     */   }
/*     */   
/*     */   private ImmutableRangeSet(ImmutableList<Range<C>> ranges, ImmutableRangeSet<C> complement) {
/* 136 */     this.ranges = ranges;
/* 137 */     this.complement = complement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(Range<C> otherRange) {
/* 145 */     int ceilingIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 147 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 149 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     
/* 152 */     if (ceilingIndex < this.ranges.size() && ((Range<C>)this.ranges
/* 153 */       .get(ceilingIndex)).isConnected(otherRange) && 
/* 154 */       !((Range<C>)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty()) {
/* 155 */       return true;
/*     */     }
/* 157 */     return (ceilingIndex > 0 && ((Range<C>)this.ranges
/* 158 */       .get(ceilingIndex - 1)).isConnected(otherRange) && 
/* 159 */       !((Range<C>)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> otherRange) {
/* 165 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 167 */         (Function)Range.lowerBoundFn(), otherRange.lowerBound, 
/*     */         
/* 169 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 172 */     return (index != -1 && ((Range<C>)this.ranges.get(index)).encloses(otherRange));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> rangeContaining(C value) {
/* 178 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 180 */         (Function)Range.lowerBoundFn(), 
/* 181 */         Cut.belowValue(value), 
/* 182 */         Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 185 */     if (index != -1) {
/* 186 */       Range<C> range = this.ranges.get(index);
/* 187 */       return range.contains(value) ? range : null;
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> span() {
/* 194 */     if (this.ranges.isEmpty()) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return (Range)Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 202 */     return this.ranges.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void add(Range<C> range) {
/* 214 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void addAll(RangeSet<C> other) {
/* 226 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void addAll(Iterable<Range<C>> other) {
/* 238 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void remove(Range<C> range) {
/* 250 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void removeAll(RangeSet<C> other) {
/* 262 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void removeAll(Iterable<Range<C>> other) {
/* 274 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asRanges() {
/* 279 */     if (this.ranges.isEmpty()) {
/* 280 */       return ImmutableSet.of();
/*     */     }
/* 282 */     return (ImmutableSet)new RegularImmutableSortedSet<>(this.ranges, Range.RANGE_LEX_ORDERING);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
/* 287 */     if (this.ranges.isEmpty()) {
/* 288 */       return ImmutableSet.of();
/*     */     }
/* 290 */     return new RegularImmutableSortedSet<>(this.ranges
/* 291 */         .reverse(), Range.RANGE_LEX_ORDERING.reverse());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ComplementRanges
/*     */     extends ImmutableList<Range<C>>
/*     */   {
/* 307 */     private final boolean positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
/* 308 */     private final boolean positiveBoundedAbove = ((Range)Iterables.<Range>getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
/*     */     ComplementRanges() {
/* 310 */       int size = ImmutableRangeSet.this.ranges.size() - 1;
/* 311 */       if (this.positiveBoundedBelow) {
/* 312 */         size++;
/*     */       }
/* 314 */       if (this.positiveBoundedAbove) {
/* 315 */         size++;
/*     */       }
/* 317 */       this.size = size;
/*     */     }
/*     */     private final int size;
/*     */     
/*     */     public int size() {
/* 322 */       return this.size;
/*     */     }
/*     */     
/*     */     public Range<C> get(int index) {
/*     */       Cut<C> lowerBound, upperBound;
/* 327 */       Preconditions.checkElementIndex(index, this.size);
/*     */ 
/*     */       
/* 330 */       if (this.positiveBoundedBelow) {
/* 331 */         lowerBound = (index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound;
/*     */       } else {
/* 333 */         lowerBound = ((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
/*     */       } 
/*     */ 
/*     */       
/* 337 */       if (this.positiveBoundedAbove && index == this.size - 1) {
/* 338 */         upperBound = Cut.aboveAll();
/*     */       } else {
/* 340 */         upperBound = ((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
/*     */       } 
/*     */       
/* 343 */       return (Range)Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 348 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> complement() {
/* 354 */     ImmutableRangeSet<C> result = this.complement;
/* 355 */     if (result != null)
/* 356 */       return result; 
/* 357 */     if (this.ranges.isEmpty())
/* 358 */       return this.complement = all(); 
/* 359 */     if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
/* 360 */       return this.complement = of();
/*     */     }
/* 362 */     ImmutableList<Range<C>> complementRanges = new ComplementRanges();
/* 363 */     result = this.complement = new ImmutableRangeSet(complementRanges, this);
/*     */     
/* 365 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> union(RangeSet<C> other) {
/* 377 */     return (ImmutableRangeSet)unionOf((Iterable)Iterables.concat(asRanges(), other.asRanges()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> intersection(RangeSet<C> other) {
/* 390 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 391 */     copy.removeAll(other.complement());
/* 392 */     return copyOf(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> difference(RangeSet<C> other) {
/* 404 */     RangeSet<C> copy = (RangeSet)TreeRangeSet.create(this);
/* 405 */     copy.removeAll(other);
/* 406 */     return copyOf(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
/*     */     final int fromIndex, toIndex;
/* 414 */     if (this.ranges.isEmpty() || range.isEmpty())
/* 415 */       return ImmutableList.of(); 
/* 416 */     if (range.encloses(span())) {
/* 417 */       return this.ranges;
/*     */     }
/*     */ 
/*     */     
/* 421 */     if (range.hasLowerBound()) {
/*     */       
/* 423 */       fromIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 425 */           (Function)Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 430 */       fromIndex = 0;
/*     */     } 
/*     */ 
/*     */     
/* 434 */     if (range.hasUpperBound()) {
/*     */       
/* 436 */       toIndex = SortedLists.binarySearch(this.ranges, 
/*     */           
/* 438 */           (Function)Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 443 */       toIndex = this.ranges.size();
/*     */     } 
/* 445 */     final int length = toIndex - fromIndex;
/* 446 */     if (length == 0) {
/* 447 */       return ImmutableList.of();
/*     */     }
/* 449 */     return new ImmutableList<Range<C>>()
/*     */       {
/*     */         public int size() {
/* 452 */           return length;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<C> get(int index) {
/* 457 */           Preconditions.checkElementIndex(index, length);
/* 458 */           if (index == 0 || index == length - 1) {
/* 459 */             return ((Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
/*     */           }
/* 461 */           return ImmutableRangeSet.this.ranges.get(index + fromIndex);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 467 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
/* 478 */     if (!isEmpty()) {
/* 479 */       Range<C> span = span();
/* 480 */       if (range.encloses(span))
/* 481 */         return this; 
/* 482 */       if (range.isConnected(span)) {
/* 483 */         return new ImmutableRangeSet(intersectRanges(range));
/*     */       }
/*     */     } 
/* 486 */     return of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<C> asSet(DiscreteDomain<C> domain) {
/* 509 */     Preconditions.checkNotNull(domain);
/* 510 */     if (isEmpty()) {
/* 511 */       return ImmutableSortedSet.of();
/*     */     }
/* 513 */     Range<C> span = span().canonical(domain);
/* 514 */     if (!span.hasLowerBound())
/*     */     {
/*     */       
/* 517 */       throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
/*     */     }
/* 519 */     if (!span.hasUpperBound()) {
/*     */       try {
/* 521 */         domain.maxValue();
/* 522 */       } catch (NoSuchElementException e) {
/* 523 */         throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 528 */     return new AsSet(domain);
/*     */   }
/*     */   
/*     */   private final class AsSet extends ImmutableSortedSet<C> { private final DiscreteDomain<C> domain;
/*     */     private transient Integer size;
/*     */     
/*     */     AsSet(DiscreteDomain<C> domain) {
/* 535 */       super(Ordering.natural());
/* 536 */       this.domain = domain;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 544 */       Integer result = this.size;
/* 545 */       if (result == null) {
/* 546 */         long total = 0L;
/* 547 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 548 */           total += ContiguousSet.<C>create(range, this.domain).size();
/* 549 */           if (total >= 2147483647L) {
/*     */             break;
/*     */           } }
/*     */         
/* 553 */         result = this.size = Integer.valueOf(Ints.saturatedCast(total));
/*     */       } 
/* 555 */       return result.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<C> iterator() {
/* 560 */       return new AbstractIterator<C>() {
/* 561 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
/* 562 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 566 */             while (!this.elemItr.hasNext()) {
/* 567 */               if (this.rangeItr.hasNext()) {
/* 568 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).iterator(); continue;
/*     */               } 
/* 570 */               return endOfData();
/*     */             } 
/*     */             
/* 573 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("NavigableSet")
/*     */     public UnmodifiableIterator<C> descendingIterator() {
/* 581 */       return new AbstractIterator<C>() {
/* 582 */           final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
/* 583 */           Iterator<C> elemItr = Iterators.emptyIterator();
/*     */ 
/*     */           
/*     */           protected C computeNext() {
/* 587 */             while (!this.elemItr.hasNext()) {
/* 588 */               if (this.rangeItr.hasNext()) {
/* 589 */                 this.elemItr = ContiguousSet.<C>create(this.rangeItr.next(), ImmutableRangeSet.AsSet.this.domain).descendingIterator(); continue;
/*     */               } 
/* 591 */               return endOfData();
/*     */             } 
/*     */             
/* 594 */             return this.elemItr.next();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     ImmutableSortedSet<C> subSet(Range<C> range) {
/* 600 */       return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> headSetImpl(C toElement, boolean inclusive) {
/* 605 */       return subSet((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/* 611 */       if (!fromInclusive && !toInclusive && Range.compareOrThrow((Comparable)fromElement, (Comparable)toElement) == 0) {
/* 612 */         return ImmutableSortedSet.of();
/*     */       }
/* 614 */       return subSet(
/* 615 */           (Range)Range.range((Comparable<?>)fromElement, 
/* 616 */             BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/* 617 */             BoundType.forBoolean(toInclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSortedSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/* 622 */       return subSet((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object o) {
/* 627 */       if (o == null) {
/* 628 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 632 */         Comparable comparable = (Comparable)o;
/* 633 */         return ImmutableRangeSet.this.contains(comparable);
/* 634 */       } catch (ClassCastException e) {
/* 635 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int indexOf(Object target) {
/* 641 */       if (contains(target)) {
/*     */         
/* 643 */         Comparable comparable = (Comparable)target;
/* 644 */         long total = 0L;
/* 645 */         for (UnmodifiableIterator<Range<C>> unmodifiableIterator = ImmutableRangeSet.this.ranges.iterator(); unmodifiableIterator.hasNext(); ) { Range<C> range = unmodifiableIterator.next();
/* 646 */           if (range.contains((C)comparable)) {
/* 647 */             return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(comparable));
/*     */           }
/* 649 */           total += ContiguousSet.<C>create(range, this.domain).size(); }
/*     */ 
/*     */         
/* 652 */         throw new AssertionError("impossible");
/*     */       } 
/* 654 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 659 */       return ImmutableRangeSet.this.ranges.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 664 */       return ImmutableRangeSet.this.ranges.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 669 */       return new ImmutableRangeSet.AsSetSerializedForm<>(ImmutableRangeSet.this.ranges, this.domain);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     private final DiscreteDomain<C> domain;
/*     */     
/*     */     AsSetSerializedForm(ImmutableList<Range<C>> ranges, DiscreteDomain<C> domain) {
/* 678 */       this.ranges = ranges;
/* 679 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 683 */       return (new ImmutableRangeSet<>(this.ranges)).asSet(this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 694 */     return this.ranges.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Builder<C> builder() {
/* 701 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<C extends Comparable<?>>
/*     */   {
/* 711 */     private final List<Range<C>> ranges = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> add(Range<C> range) {
/* 724 */       Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
/* 725 */       this.ranges.add(range);
/* 726 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(RangeSet<C> ranges) {
/* 736 */       return addAll(ranges.asRanges());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<C> addAll(Iterable<Range<C>> ranges) {
/* 748 */       for (Range<C> range : ranges) {
/* 749 */         add(range);
/*     */       }
/* 751 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeSet<C> build() {
/* 761 */       ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder<>(this.ranges.size());
/* 762 */       Collections.sort(this.ranges, Range.RANGE_LEX_ORDERING);
/* 763 */       PeekingIterator<Range<C>> peekingItr = Iterators.peekingIterator(this.ranges.iterator());
/* 764 */       while (peekingItr.hasNext()) {
/* 765 */         Range<C> range = peekingItr.next();
/* 766 */         while (peekingItr.hasNext()) {
/* 767 */           Range<C> nextRange = peekingItr.peek();
/* 768 */           if (range.isConnected(nextRange)) {
/* 769 */             Preconditions.checkArgument(range
/* 770 */                 .intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
/*     */ 
/*     */ 
/*     */             
/* 774 */             range = range.span(peekingItr.next());
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 779 */         mergedRangesBuilder.add(range);
/*     */       } 
/* 781 */       ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
/* 782 */       if (mergedRanges.isEmpty())
/* 783 */         return (ImmutableRangeSet)ImmutableRangeSet.of(); 
/* 784 */       if (mergedRanges.size() == 1 && (
/* 785 */         (Range)Iterables.<Range>getOnlyElement((Iterable)mergedRanges)).equals(Range.all())) {
/* 786 */         return (ImmutableRangeSet)ImmutableRangeSet.all();
/*     */       }
/* 788 */       return (ImmutableRangeSet)new ImmutableRangeSet<>(mergedRanges);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<C extends Comparable>
/*     */     implements Serializable {
/*     */     private final ImmutableList<Range<C>> ranges;
/*     */     
/*     */     SerializedForm(ImmutableList<Range<C>> ranges) {
/* 797 */       this.ranges = ranges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 801 */       if (this.ranges.isEmpty())
/* 802 */         return ImmutableRangeSet.of(); 
/* 803 */       if (this.ranges.equals(ImmutableList.of(Range.all()))) {
/* 804 */         return ImmutableRangeSet.all();
/*     */       }
/* 806 */       return new ImmutableRangeSet<>(this.ranges);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 812 */     return new SerializedForm<>(this.ranges);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */