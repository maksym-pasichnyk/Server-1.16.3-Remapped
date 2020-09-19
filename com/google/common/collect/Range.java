/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
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
/*     */ @GwtCompatible
/*     */ public final class Range<C extends Comparable>
/*     */   implements Predicate<C>, Serializable
/*     */ {
/* 117 */   private static final Function<Range, Cut> LOWER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range)
/*     */       {
/* 121 */         return range.lowerBound;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
/* 127 */     return (Function)LOWER_BOUND_FN;
/*     */   }
/*     */   
/* 130 */   private static final Function<Range, Cut> UPPER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range)
/*     */       {
/* 134 */         return range.upperBound;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
/* 140 */     return (Function)UPPER_BOUND_FN;
/*     */   }
/*     */   
/* 143 */   static final Ordering<Range<?>> RANGE_LEX_ORDERING = new RangeLexOrdering();
/*     */   
/*     */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
/* 146 */     return (Range)new Range<>(lowerBound, upperBound);
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
/*     */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
/* 158 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
/* 170 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
/* 182 */     return create((Cut)Cut.belowValue((Comparable)lower), (Cut)Cut.belowValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
/* 194 */     return create((Cut)Cut.aboveValue((Comparable)lower), (Cut)Cut.aboveValue((Comparable)upper));
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
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 208 */     Preconditions.checkNotNull(lowerType);
/* 209 */     Preconditions.checkNotNull(upperType);
/*     */ 
/*     */     
/* 212 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut)Cut.<Comparable>aboveValue((Comparable)lower) : (Cut)Cut.<Comparable>belowValue((Comparable)lower);
/*     */     
/* 214 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut)Cut.<Comparable>belowValue((Comparable)upper) : (Cut)Cut.<Comparable>aboveValue((Comparable)upper);
/* 215 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
/* 225 */     return create((Cut)Cut.belowAll(), (Cut)Cut.belowValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
/* 235 */     return create((Cut)Cut.belowAll(), (Cut)Cut.aboveValue((Comparable)endpoint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 245 */     switch (boundType) {
/*     */       case OPEN:
/* 247 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 249 */         return atMost(endpoint);
/*     */     } 
/* 251 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
/* 262 */     return create((Cut)Cut.aboveValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
/* 272 */     return create((Cut)Cut.belowValue((Comparable)endpoint), (Cut)Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 282 */     switch (boundType) {
/*     */       case OPEN:
/* 284 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 286 */         return atLeast(endpoint);
/*     */     } 
/* 288 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 292 */   private static final Range<Comparable> ALL = new Range(
/* 293 */       (Cut)Cut.belowAll(), (Cut)Cut.aboveAll());
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> all() {
/* 302 */     return (Range)ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> singleton(C value) {
/* 313 */     return closed(value, value);
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
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 328 */     Preconditions.checkNotNull(values);
/* 329 */     if (values instanceof ContiguousSet) {
/* 330 */       return ((ContiguousSet)values).range();
/*     */     }
/* 332 */     Iterator<C> valueIterator = values.iterator();
/* 333 */     Comparable comparable1 = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 334 */     Comparable comparable2 = comparable1;
/* 335 */     while (valueIterator.hasNext()) {
/* 336 */       Comparable comparable = (Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 337 */       comparable1 = (Comparable)Ordering.<Comparable>natural().min(comparable1, comparable);
/* 338 */       comparable2 = (Comparable)Ordering.<Comparable>natural().max(comparable2, comparable);
/*     */     } 
/* 340 */     return closed((C)comparable1, (C)comparable2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 347 */     this.lowerBound = (Cut<C>)Preconditions.checkNotNull(lowerBound);
/* 348 */     this.upperBound = (Cut<C>)Preconditions.checkNotNull(upperBound);
/* 349 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == 
/* 350 */       Cut.aboveAll() || upperBound == 
/* 351 */       Cut.belowAll()) {
/* 352 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLowerBound() {
/* 360 */     return (this.lowerBound != Cut.belowAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C lowerEndpoint() {
/* 370 */     return this.lowerBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType lowerBoundType() {
/* 381 */     return this.lowerBound.typeAsLowerBound();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUpperBound() {
/* 388 */     return (this.upperBound != Cut.aboveAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public C upperEndpoint() {
/* 398 */     return this.upperBound.endpoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundType upperBoundType() {
/* 409 */     return this.upperBound.typeAsUpperBound();
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
/*     */   public boolean isEmpty() {
/* 422 */     return this.lowerBound.equals(this.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 431 */     Preconditions.checkNotNull(value);
/*     */     
/* 433 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean apply(C input) {
/* 443 */     return contains(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 451 */     if (Iterables.isEmpty(values)) {
/* 452 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 456 */     if (values instanceof SortedSet) {
/* 457 */       SortedSet<? extends C> set = cast(values);
/* 458 */       Comparator<?> comparator = set.comparator();
/* 459 */       if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
/* 460 */         return (contains(set.first()) && contains(set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 464 */     for (Comparable comparable : values) {
/* 465 */       if (!contains((C)comparable)) {
/* 466 */         return false;
/*     */       }
/*     */     } 
/* 469 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean encloses(Range<C> other) {
/* 497 */     return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound
/* 498 */       .compareTo(other.upperBound) >= 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnected(Range<C> other) {
/* 526 */     return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound
/* 527 */       .compareTo(this.upperBound) <= 0);
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
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 547 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 548 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 549 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 550 */       return this; 
/* 551 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 552 */       return connectedRange;
/*     */     }
/* 554 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 555 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/* 556 */     return (Range)create(newLower, newUpper);
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
/*     */   public Range<C> span(Range<C> other) {
/* 572 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 573 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 574 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 575 */       return this; 
/* 576 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 577 */       return other;
/*     */     }
/* 579 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 580 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 581 */     return (Range)create(newLower, newUpper);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 610 */     Preconditions.checkNotNull(domain);
/* 611 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 612 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 613 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range)create(lower, upper);
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
/*     */   public boolean equals(@Nullable Object object) {
/* 625 */     if (object instanceof Range) {
/* 626 */       Range<?> other = (Range)object;
/* 627 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/* 629 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 635 */     return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 644 */     return toString(this.lowerBound, this.upperBound);
/*     */   }
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 648 */     StringBuilder sb = new StringBuilder(16);
/* 649 */     lowerBound.describeAsLowerBound(sb);
/* 650 */     sb.append("..");
/* 651 */     upperBound.describeAsUpperBound(sb);
/* 652 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> SortedSet<T> cast(Iterable<T> iterable) {
/* 659 */     return (SortedSet<T>)iterable;
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 663 */     if (equals(ALL)) {
/* 664 */       return all();
/*     */     }
/* 666 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
/* 672 */     return left.compareTo(right);
/*     */   }
/*     */   
/*     */   private static class RangeLexOrdering
/*     */     extends Ordering<Range<?>> implements Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private RangeLexOrdering() {}
/*     */     
/*     */     public int compare(Range<?> left, Range<?> right) {
/* 682 */       return ComparisonChain.start()
/* 683 */         .compare(left.lowerBound, right.lowerBound)
/* 684 */         .compare(left.upperBound, right.upperBound)
/* 685 */         .result();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */