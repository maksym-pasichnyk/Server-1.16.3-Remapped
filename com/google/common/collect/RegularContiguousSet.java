/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   private final Range<C> range;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
/*  38 */     super(domain);
/*  39 */     this.range = range;
/*     */   }
/*     */   
/*     */   private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
/*  43 */     return this.range.isConnected(other) ? 
/*  44 */       ContiguousSet.<C>create(this.range.intersection(other), this.domain) : new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
/*  50 */     return intersectionInCurrentDomain((Range)Range.upTo((Comparable<?>)toElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  56 */     if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive)
/*     */     {
/*  58 */       return new EmptyContiguousSet<>(this.domain);
/*     */     }
/*  60 */     return intersectionInCurrentDomain(
/*  61 */         (Range)Range.range((Comparable<?>)fromElement, 
/*  62 */           BoundType.forBoolean(fromInclusive), (Comparable<?>)toElement, 
/*  63 */           BoundType.forBoolean(toInclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
/*  68 */     return intersectionInCurrentDomain((Range)Range.downTo((Comparable<?>)fromElement, BoundType.forBoolean(inclusive)));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int indexOf(Object target) {
/*  74 */     return contains(target) ? (int)this.domain.distance(first(), (C)target) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  79 */     return new AbstractSequentialIterator<C>((Comparable)first()) {
/*  80 */         final C last = RegularContiguousSet.this.last();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  84 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.last) ? null : RegularContiguousSet.this.domain.next(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/*  92 */     return new AbstractSequentialIterator<C>((Comparable)last()) {
/*  93 */         final C first = RegularContiguousSet.this.first();
/*     */ 
/*     */         
/*     */         protected C computeNext(C previous) {
/*  97 */           return RegularContiguousSet.equalsOrThrow((Comparable<?>)previous, (Comparable<?>)this.first) ? null : RegularContiguousSet.this.domain.previous(previous);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static boolean equalsOrThrow(Comparable<?> left, @Nullable Comparable<?> right) {
/* 103 */     return (right != null && Range.compareOrThrow(left, right) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public C first() {
/* 113 */     return this.range.lowerBound.leastValueAbove(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public C last() {
/* 118 */     return this.range.upperBound.greatestValueBelow(this.domain);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 123 */     long distance = this.domain.distance(first(), last());
/* 124 */     return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 129 */     if (object == null) {
/* 130 */       return false;
/*     */     }
/*     */     try {
/* 133 */       return this.range.contains((C)object);
/* 134 */     } catch (ClassCastException e) {
/* 135 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/* 141 */     return Collections2.containsAllImpl(this, targets);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/* 151 */     Preconditions.checkNotNull(other);
/* 152 */     Preconditions.checkArgument(this.domain.equals(other.domain));
/* 153 */     if (other.isEmpty()) {
/* 154 */       return other;
/*     */     }
/* 156 */     Comparable<Comparable> comparable1 = (Comparable)Ordering.<Comparable>natural().max(first(), other.first());
/* 157 */     Comparable<Comparable> comparable2 = (Comparable)Ordering.<Comparable>natural().min(last(), other.last());
/* 158 */     return (comparable1.compareTo(comparable2) <= 0) ? 
/* 159 */       ContiguousSet.<C>create((Range)Range.closed(comparable1, comparable2), this.domain) : new EmptyContiguousSet<>(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> range() {
/* 166 */     return range(BoundType.CLOSED, BoundType.CLOSED);
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
/* 171 */     return (Range)Range.create(this.range.lowerBound
/* 172 */         .withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound
/* 173 */         .withUpperBoundType(upperBoundType, this.domain));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 178 */     if (object == this)
/* 179 */       return true; 
/* 180 */     if (object instanceof RegularContiguousSet) {
/* 181 */       RegularContiguousSet<?> that = (RegularContiguousSet)object;
/* 182 */       if (this.domain.equals(that.domain)) {
/* 183 */         return (first().equals(that.first()) && last().equals(that.last()));
/*     */       }
/*     */     } 
/* 186 */     return super.equals(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 192 */     return Sets.hashCodeImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class SerializedForm<C extends Comparable> implements Serializable {
/*     */     final Range<C> range;
/*     */     final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
/* 201 */       this.range = range;
/* 202 */       this.domain = domain;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 206 */       return new RegularContiguousSet<>(this.range, this.domain);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 213 */     return new SerializedForm<>(this.range, this.domain);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularContiguousSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */