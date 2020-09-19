/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSet<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1297;
/*     */   static final int MAX_TABLE_SIZE = 1073741824;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.7D;
/*     */   private static final int CUTOFF = 751619276;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableList<E> asList;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
/*  64 */     return CollectCollectors.toImmutableSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of() {
/*  73 */     return RegularImmutableSet.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E element) {
/*  82 */     return new SingletonImmutableSet<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2) {
/*  91 */     return construct(2, new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
/* 100 */     return construct(3, new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
/* 109 */     return construct(4, new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 118 */     return construct(5, new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 130 */     int paramCount = 6;
/* 131 */     Object[] elements = new Object[6 + others.length];
/* 132 */     elements[0] = e1;
/* 133 */     elements[1] = e2;
/* 134 */     elements[2] = e3;
/* 135 */     elements[3] = e4;
/* 136 */     elements[4] = e5;
/* 137 */     elements[5] = e6;
/* 138 */     System.arraycopy(others, 0, elements, 6, others.length);
/* 139 */     return construct(elements.length, elements);
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
/*     */   private static <E> ImmutableSet<E> construct(int n, Object... elements) {
/*     */     E elem;
/* 158 */     switch (n) {
/*     */       case 0:
/* 160 */         return of();
/*     */       
/*     */       case 1:
/* 163 */         elem = (E)elements[0];
/* 164 */         return of(elem);
/*     */     } 
/*     */ 
/*     */     
/* 168 */     int tableSize = chooseTableSize(n);
/* 169 */     Object[] table = new Object[tableSize];
/* 170 */     int mask = tableSize - 1;
/* 171 */     int hashCode = 0;
/* 172 */     int uniques = 0;
/* 173 */     for (int i = 0; i < n; i++) {
/* 174 */       Object element = ObjectArrays.checkElementNotNull(elements[i], i);
/* 175 */       int hash = element.hashCode();
/* 176 */       for (int j = Hashing.smear(hash);; j++) {
/* 177 */         int index = j & mask;
/* 178 */         Object value = table[index];
/* 179 */         if (value == null) {
/*     */           
/* 181 */           elements[uniques++] = element;
/* 182 */           table[index] = element;
/* 183 */           hashCode += hash; break;
/*     */         } 
/* 185 */         if (value.equals(element)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 190 */     Arrays.fill(elements, uniques, n, (Object)null);
/* 191 */     if (uniques == 1) {
/*     */ 
/*     */       
/* 194 */       E element = (E)elements[0];
/* 195 */       return new SingletonImmutableSet<>(element, hashCode);
/* 196 */     }  if (tableSize != chooseTableSize(uniques))
/*     */     {
/*     */       
/* 199 */       return construct(uniques, elements);
/*     */     }
/*     */     
/* 202 */     Object[] uniqueElements = (uniques < elements.length) ? Arrays.<Object>copyOf(elements, uniques) : elements;
/* 203 */     return new RegularImmutableSet<>(uniqueElements, hashCode, table, mask);
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
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/* 226 */     if (setSize < 751619276) {
/*     */       
/* 228 */       int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/* 229 */       while (tableSize * 0.7D < setSize) {
/* 230 */         tableSize <<= 1;
/*     */       }
/* 232 */       return tableSize;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     Preconditions.checkArgument((setSize < 1073741824), "collection too large");
/* 237 */     return 1073741824;
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
/*     */   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
/* 257 */     if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {
/*     */       
/* 259 */       ImmutableSet<E> set = (ImmutableSet)elements;
/* 260 */       if (!set.isPartialView()) {
/* 261 */         return set;
/*     */       }
/* 263 */     } else if (elements instanceof EnumSet) {
/* 264 */       return copyOfEnumSet((EnumSet)elements);
/*     */     } 
/* 266 */     Object[] array = elements.toArray();
/* 267 */     return construct(array.length, array);
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
/*     */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
/* 283 */     return (elements instanceof Collection) ? 
/* 284 */       copyOf((Collection<? extends E>)elements) : 
/* 285 */       copyOf(elements.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
/* 296 */     if (!elements.hasNext()) {
/* 297 */       return of();
/*     */     }
/* 299 */     E first = elements.next();
/* 300 */     if (!elements.hasNext()) {
/* 301 */       return of(first);
/*     */     }
/* 303 */     return (new Builder<>()).add(first).addAll(elements).build();
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
/*     */   public static <E> ImmutableSet<E> copyOf(E[] elements) {
/* 315 */     switch (elements.length) {
/*     */       case 0:
/* 317 */         return of();
/*     */       case 1:
/* 319 */         return of(elements[0]);
/*     */     } 
/* 321 */     return construct(elements.length, (Object[])elements.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableSet copyOfEnumSet(EnumSet<Enum> enumSet) {
/* 327 */     return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 334 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 339 */     if (object == this)
/* 340 */       return true; 
/* 341 */     if (object instanceof ImmutableSet && 
/* 342 */       isHashCodeFast() && ((ImmutableSet)object)
/* 343 */       .isHashCodeFast() && 
/* 344 */       hashCode() != object.hashCode()) {
/* 345 */       return false;
/*     */     }
/* 347 */     return Sets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 352 */     return Sets.hashCodeImpl(this);
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
/* 366 */     ImmutableList<E> result = this.asList;
/* 367 */     return (result == null) ? (this.asList = createAsList()) : result;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 371 */     return new RegularImmutableAsList<>(this, toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class Indexed<E>
/*     */     extends ImmutableSet<E>
/*     */   {
/*     */     public UnmodifiableIterator<E> iterator() {
/* 379 */       return asList().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 384 */       return CollectSpliterators.indexed(size(), 1297, this::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> consumer) {
/* 389 */       Preconditions.checkNotNull(consumer);
/* 390 */       int n = size();
/* 391 */       for (int i = 0; i < n; i++) {
/* 392 */         consumer.accept(get(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<E> createAsList() {
/* 398 */       return new ImmutableAsList<E>()
/*     */         {
/*     */           public E get(int index) {
/* 401 */             return ImmutableSet.Indexed.this.get(index);
/*     */           }
/*     */ 
/*     */           
/*     */           ImmutableSet.Indexed<E> delegateCollection() {
/* 406 */             return ImmutableSet.Indexed.this;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     abstract E get(int param1Int);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 423 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 427 */       return ImmutableSet.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 435 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 443 */     return new Builder<>();
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
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.ArrayBasedBuilder<E>
/*     */   {
/*     */     public Builder() {
/* 467 */       this(4);
/*     */     }
/*     */     
/*     */     Builder(int capacity) {
/* 471 */       super(capacity);
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
/*     */     public Builder<E> add(E element) {
/* 486 */       super.add(element);
/* 487 */       return this;
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
/*     */     public Builder<E> add(E... elements) {
/* 502 */       super.add(elements);
/* 503 */       return this;
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
/* 518 */       super.addAll(elements);
/* 519 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 534 */       super.addAll(elements);
/* 535 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableCollection.ArrayBasedBuilder<E> builder) {
/* 541 */       super.combine(builder);
/* 542 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> build() {
/* 551 */       ImmutableSet<E> result = ImmutableSet.construct(this.size, this.contents);
/*     */ 
/*     */       
/* 554 */       this.size = result.size();
/* 555 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */