/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Predicate;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableCollection<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1296;
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 177 */     return Spliterators.spliterator(this, 1296);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object[] toArray() {
/* 182 */     int size = size();
/* 183 */     if (size == 0) {
/* 184 */       return ObjectArrays.EMPTY_ARRAY;
/*     */     }
/* 186 */     Object[] result = new Object[size];
/* 187 */     copyIntoArray(result, 0);
/* 188 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <T> T[] toArray(T[] other) {
/* 194 */     Preconditions.checkNotNull(other);
/* 195 */     int size = size();
/* 196 */     if (other.length < size) {
/* 197 */       other = ObjectArrays.newArray(other, size);
/* 198 */     } else if (other.length > size) {
/* 199 */       other[size] = null;
/*     */     } 
/* 201 */     copyIntoArray((Object[])other, 0);
/* 202 */     return other;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(E e) {
/* 218 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean remove(Object object) {
/* 231 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean addAll(Collection<? extends E> newElements) {
/* 244 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean removeAll(Collection<?> oldElements) {
/* 257 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean removeIf(Predicate<? super E> filter) {
/* 270 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean retainAll(Collection<?> elementsToKeep) {
/* 282 */     throw new UnsupportedOperationException();
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
/*     */   public final void clear() {
/* 294 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<E> asList() {
/* 308 */     switch (size()) {
/*     */       case 0:
/* 310 */         return ImmutableList.of();
/*     */       case 1:
/* 312 */         return ImmutableList.of(iterator().next());
/*     */     } 
/* 314 */     return new RegularImmutableAsList<>(this, toArray());
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
/*     */   @CanIgnoreReturnValue
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 332 */     for (UnmodifiableIterator<E> unmodifiableIterator = iterator(); unmodifiableIterator.hasNext(); ) { E e = unmodifiableIterator.next();
/* 333 */       dst[offset++] = e; }
/*     */     
/* 335 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 340 */     return new ImmutableList.SerializedForm(toArray());
/*     */   }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   public abstract boolean contains(@Nullable Object paramObject);
/*     */   
/*     */   abstract boolean isPartialView();
/*     */   
/*     */   public static abstract class Builder<E> { static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */     
/*     */     static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 352 */       if (minCapacity < 0) {
/* 353 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 356 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 357 */       if (newCapacity < minCapacity) {
/* 358 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 360 */       if (newCapacity < 0) {
/* 361 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 364 */       return newCapacity;
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
/*     */     @CanIgnoreReturnValue
/*     */     public abstract Builder<E> add(E param1E);
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
/*     */     public Builder<E> add(E... elements) {
/* 396 */       for (E element : elements) {
/* 397 */         add(element);
/*     */       }
/* 399 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 416 */       for (E element : elements) {
/* 417 */         add(element);
/*     */       }
/* 419 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 436 */       while (elements.hasNext()) {
/* 437 */         add(elements.next());
/*     */       }
/* 439 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract ImmutableCollection<E> build(); }
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class ArrayBasedBuilder<E>
/*     */     extends Builder<E>
/*     */   {
/*     */     Object[] contents;
/*     */     
/*     */     int size;
/*     */ 
/*     */     
/*     */     ArrayBasedBuilder(int initialCapacity) {
/* 457 */       CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
/* 458 */       this.contents = new Object[initialCapacity];
/* 459 */       this.size = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 467 */       if (this.contents.length < minCapacity) {
/* 468 */         this
/* 469 */           .contents = Arrays.copyOf(this.contents, 
/* 470 */             expandedCapacity(this.contents.length, minCapacity));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ArrayBasedBuilder<E> add(E element) {
/* 477 */       Preconditions.checkNotNull(element);
/* 478 */       ensureCapacity(this.size + 1);
/* 479 */       this.contents[this.size++] = element;
/* 480 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ImmutableCollection.Builder<E> add(E... elements) {
/* 486 */       ObjectArrays.checkElementsNotNull((Object[])elements);
/* 487 */       ensureCapacity(this.size + elements.length);
/* 488 */       System.arraycopy(elements, 0, this.contents, this.size, elements.length);
/* 489 */       this.size += elements.length;
/* 490 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ImmutableCollection.Builder<E> addAll(Iterable<? extends E> elements) {
/* 496 */       if (elements instanceof Collection) {
/* 497 */         Collection<?> collection = (Collection)elements;
/* 498 */         ensureCapacity(this.size + collection.size());
/*     */       } 
/* 500 */       super.addAll(elements);
/* 501 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     ArrayBasedBuilder<E> combine(ArrayBasedBuilder<E> builder) {
/* 506 */       Preconditions.checkNotNull(builder);
/* 507 */       ensureCapacity(this.size + builder.size);
/* 508 */       System.arraycopy(builder.contents, 0, this.contents, this.size, builder.size);
/* 509 */       this.size += builder.size;
/* 510 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */