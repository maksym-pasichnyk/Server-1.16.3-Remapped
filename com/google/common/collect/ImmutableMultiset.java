/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMultiset<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient ImmutableList<E> asList;
/*     */   @LazyInit
/*     */   private transient ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
/*  67 */     return toImmutableMultiset((Function)Function.identity(), e -> 1);
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
/*     */   private static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  83 */     Preconditions.checkNotNull(elementFunction);
/*  84 */     Preconditions.checkNotNull(countFunction);
/*  85 */     return Collector.of(LinkedHashMultiset::create, (multiset, t) -> multiset.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> copyFromEntries(multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   public static <E> ImmutableMultiset<E> of() {
/* 100 */     return RegularImmutableMultiset.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E element) {
/* 111 */     return copyFromElements((E[])new Object[] { element });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2) {
/* 122 */     return copyFromElements((E[])new Object[] { e1, e2 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
/* 134 */     return copyFromElements((E[])new Object[] { e1, e2, e3 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 146 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 158 */     return copyFromElements((E[])new Object[] { e1, e2, e3, e4, e5 });
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
/*     */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 170 */     return (new Builder<>()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
/* 181 */     return copyFromElements(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 191 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/* 193 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/* 194 */       if (!result.isPartialView()) {
/* 195 */         return result;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);
/*     */     
/* 204 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
/* 208 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 209 */     Collections.addAll(multiset, elements);
/* 210 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 215 */     if (entries.isEmpty()) {
/* 216 */       return of();
/*     */     }
/* 218 */     return new RegularImmutableMultiset<>(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 229 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 230 */     Iterators.addAll(multiset, elements);
/* 231 */     return copyFromEntries(multiset.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 238 */     final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 239 */     return new UnmodifiableIterator<E>()
/*     */       {
/*     */         int remaining;
/*     */         E element;
/*     */         
/*     */         public boolean hasNext() {
/* 245 */           return (this.remaining > 0 || entryIterator.hasNext());
/*     */         }
/*     */ 
/*     */         
/*     */         public E next() {
/* 250 */           if (this.remaining <= 0) {
/* 251 */             Multiset.Entry<E> entry = entryIterator.next();
/* 252 */             this.element = entry.getElement();
/* 253 */             this.remaining = entry.getCount();
/*     */           } 
/* 255 */           this.remaining--;
/* 256 */           return this.element;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> asList() {
/* 266 */     ImmutableList<E> result = this.asList;
/* 267 */     return (result == null) ? (this.asList = createAsList()) : result;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 271 */     if (isEmpty()) {
/* 272 */       return ImmutableList.of();
/*     */     }
/* 274 */     return new RegularImmutableAsList<>(this, toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 279 */     return (count(object) > 0);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final int add(E element, int occurrences) {
/* 292 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final int remove(Object element, int occurrences) {
/* 305 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final int setCount(E element, int count) {
/* 318 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean setCount(E element, int oldCount, int newCount) {
/* 331 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 337 */     for (UnmodifiableIterator<Multiset.Entry<E>> unmodifiableIterator = entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Multiset.Entry<E> entry = unmodifiableIterator.next();
/* 338 */       Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
/* 339 */       offset += entry.getCount(); }
/*     */     
/* 341 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 346 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 351 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 356 */     return entrySet().toString();
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
/*     */   public ImmutableSet<Multiset.Entry<E>> entrySet() {
/* 368 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 369 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */   
/*     */   private final ImmutableSet<Multiset.Entry<E>> createEntrySet() {
/* 373 */     return isEmpty() ? ImmutableSet.<Multiset.Entry<E>>of() : new EntrySet();
/*     */   }
/*     */   
/*     */   private final class EntrySet extends ImmutableSet.Indexed<Multiset.Entry<E>> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private EntrySet() {}
/*     */     
/*     */     boolean isPartialView() {
/* 382 */       return ImmutableMultiset.this.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<E> get(int index) {
/* 387 */       return ImmutableMultiset.this.getEntry(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 392 */       return ImmutableMultiset.this.elementSet().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 397 */       if (o instanceof Multiset.Entry) {
/* 398 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 399 */         if (entry.getCount() <= 0) {
/* 400 */           return false;
/*     */         }
/* 402 */         int count = ImmutableMultiset.this.count(entry.getElement());
/* 403 */         return (count == entry.getCount());
/*     */       } 
/* 405 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 410 */       return ImmutableMultiset.this.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 417 */       return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
/*     */     }
/*     */   }
/*     */   
/*     */   static class EntrySetSerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
/* 427 */       this.multiset = multiset;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 431 */       return this.multiset.entrySet();
/*     */     } }
/*     */   
/*     */   private static class SerializedForm implements Serializable {
/*     */     final Object[] elements;
/*     */     final int[] counts;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Multiset<?> multiset) {
/* 440 */       int distinct = multiset.entrySet().size();
/* 441 */       this.elements = new Object[distinct];
/* 442 */       this.counts = new int[distinct];
/* 443 */       int i = 0;
/* 444 */       for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 445 */         this.elements[i] = entry.getElement();
/* 446 */         this.counts[i] = entry.getCount();
/* 447 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 452 */       LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
/* 453 */       for (int i = 0; i < this.elements.length; i++) {
/* 454 */         multiset.add(this.elements[i], this.counts[i]);
/*     */       }
/* 456 */       return ImmutableMultiset.copyOf(multiset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 465 */     return new SerializedForm(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 473 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ImmutableSet<E> elementSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset.Entry<E> getEntry(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     final Multiset<E> contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 502 */       this(LinkedHashMultiset.create());
/*     */     }
/*     */     
/*     */     Builder(Multiset<E> contents) {
/* 506 */       this.contents = contents;
/*     */     }
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
/*     */     public Builder<E> add(E element) {
/* 519 */       this.contents.add((E)Preconditions.checkNotNull(element));
/* 520 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 538 */       this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
/* 539 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> setCount(E element, int count) {
/* 554 */       this.contents.setCount((E)Preconditions.checkNotNull(element), count);
/* 555 */       return this;
/*     */     }
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
/*     */     public Builder<E> add(E... elements) {
/* 569 */       super.add(elements);
/* 570 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 585 */       if (elements instanceof Multiset) {
/* 586 */         Multiset<? extends E> multiset = Multisets.cast(elements);
/* 587 */         for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
/* 588 */           addCopies(entry.getElement(), entry.getCount());
/*     */         }
/*     */       } else {
/* 591 */         super.addAll(elements);
/*     */       } 
/* 593 */       return this;
/*     */     }
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 607 */       super.addAll(elements);
/* 608 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultiset<E> build() {
/* 617 */       return ImmutableMultiset.copyOf(this.contents);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */