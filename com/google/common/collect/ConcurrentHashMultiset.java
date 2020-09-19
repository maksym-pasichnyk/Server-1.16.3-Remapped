/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @GwtIncompatible
/*     */ public final class ConcurrentHashMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient ConcurrentMap<E, AtomicInteger> countMap;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/*  78 */     static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentHashMultiset<E> create() {
/*  89 */     return new ConcurrentHashMultiset<>(new ConcurrentHashMap<>());
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
/*     */   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
/* 101 */     ConcurrentHashMultiset<E> multiset = create();
/* 102 */     Iterables.addAll(multiset, elements);
/* 103 */     return multiset;
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
/*     */   @Beta
/*     */   public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
/* 122 */     return new ConcurrentHashMultiset<>(countMap);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
/* 127 */     Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
/* 128 */     this.countMap = countMap;
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
/*     */   public int count(@Nullable Object element) {
/* 141 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 142 */     return (existingCounter == null) ? 0 : existingCounter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 153 */     long sum = 0L;
/* 154 */     for (AtomicInteger value : this.countMap.values()) {
/* 155 */       sum += value.get();
/*     */     }
/* 157 */     return Ints.saturatedCast(sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 167 */     return snapshot().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 172 */     return snapshot().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<E> snapshot() {
/* 180 */     List<E> list = Lists.newArrayListWithExpectedSize(size());
/* 181 */     for (Multiset.Entry<E> entry : (Iterable<Multiset.Entry<E>>)entrySet()) {
/* 182 */       E element = entry.getElement();
/* 183 */       for (int i = entry.getCount(); i > 0; i--) {
/* 184 */         list.add(element);
/*     */       }
/*     */     } 
/* 187 */     return list;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*     */     AtomicInteger existingCounter, newCounter;
/* 204 */     Preconditions.checkNotNull(element);
/* 205 */     if (occurrences == 0) {
/* 206 */       return count(element);
/*     */     }
/* 208 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/*     */     do {
/* 211 */       existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 212 */       if (existingCounter == null) {
/* 213 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
/* 214 */         if (existingCounter == null) {
/* 215 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       while (true) {
/* 221 */         int oldValue = existingCounter.get();
/* 222 */         if (oldValue != 0) {
/*     */           try {
/* 224 */             int newValue = IntMath.checkedAdd(oldValue, occurrences);
/* 225 */             if (existingCounter.compareAndSet(oldValue, newValue))
/*     */             {
/* 227 */               return oldValue;
/*     */             }
/* 229 */           } catch (ArithmeticException overflow) {
/* 230 */             throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 237 */       newCounter = new AtomicInteger(occurrences);
/* 238 */     } while (this.countMap.putIfAbsent(element, newCounter) != null && 
/* 239 */       !this.countMap.replace(element, existingCounter, newCounter));
/* 240 */     return 0;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@Nullable Object element, int occurrences) {
/* 271 */     if (occurrences == 0) {
/* 272 */       return count(element);
/*     */     }
/* 274 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/* 276 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 277 */     if (existingCounter == null) {
/* 278 */       return 0;
/*     */     }
/*     */     while (true) {
/* 281 */       int oldValue = existingCounter.get();
/* 282 */       if (oldValue != 0) {
/* 283 */         int newValue = Math.max(0, oldValue - occurrences);
/* 284 */         if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 285 */           if (newValue == 0)
/*     */           {
/*     */             
/* 288 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 290 */           return oldValue;
/*     */         }  continue;
/*     */       }  break;
/* 293 */     }  return 0;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeExactly(@Nullable Object element, int occurrences) {
/* 312 */     if (occurrences == 0) {
/* 313 */       return true;
/*     */     }
/* 315 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/* 317 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 318 */     if (existingCounter == null) {
/* 319 */       return false;
/*     */     }
/*     */     while (true) {
/* 322 */       int oldValue = existingCounter.get();
/* 323 */       if (oldValue < occurrences) {
/* 324 */         return false;
/*     */       }
/* 326 */       int newValue = oldValue - occurrences;
/* 327 */       if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 328 */         if (newValue == 0)
/*     */         {
/*     */           
/* 331 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 333 */         return true;
/*     */       } 
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 348 */     Preconditions.checkNotNull(element);
/* 349 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */     label26: while (true) {
/* 351 */       AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 352 */       if (existingCounter == null) {
/* 353 */         if (count == 0) {
/* 354 */           return 0;
/*     */         }
/* 356 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count));
/* 357 */         if (existingCounter == null) {
/* 358 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 365 */         int oldValue = existingCounter.get();
/* 366 */         if (oldValue == 0) {
/* 367 */           if (count == 0) {
/* 368 */             return 0;
/*     */           }
/* 370 */           AtomicInteger newCounter = new AtomicInteger(count);
/* 371 */           if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 372 */             .replace(element, existingCounter, newCounter)) {
/* 373 */             return 0;
/*     */           }
/*     */           
/*     */           continue label26;
/*     */         } 
/* 378 */         if (existingCounter.compareAndSet(oldValue, count)) {
/* 379 */           if (count == 0)
/*     */           {
/*     */             
/* 382 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 384 */           return oldValue;
/*     */         } 
/*     */       } 
/*     */       break;
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int expectedOldCount, int newCount) {
/* 405 */     Preconditions.checkNotNull(element);
/* 406 */     CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
/* 407 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*     */     
/* 409 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 410 */     if (existingCounter == null) {
/* 411 */       if (expectedOldCount != 0)
/* 412 */         return false; 
/* 413 */       if (newCount == 0) {
/* 414 */         return true;
/*     */       }
/*     */       
/* 417 */       return (this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
/*     */     } 
/*     */     
/* 420 */     int oldValue = existingCounter.get();
/* 421 */     if (oldValue == expectedOldCount) {
/* 422 */       if (oldValue == 0) {
/* 423 */         if (newCount == 0) {
/*     */           
/* 425 */           this.countMap.remove(element, existingCounter);
/* 426 */           return true;
/*     */         } 
/* 428 */         AtomicInteger newCounter = new AtomicInteger(newCount);
/* 429 */         return (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 430 */           .replace(element, existingCounter, newCounter));
/*     */       } 
/*     */       
/* 433 */       if (existingCounter.compareAndSet(oldValue, newCount)) {
/* 434 */         if (newCount == 0)
/*     */         {
/*     */           
/* 437 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 439 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 443 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 450 */     final Set<E> delegate = this.countMap.keySet();
/* 451 */     return new ForwardingSet<E>()
/*     */       {
/*     */         protected Set<E> delegate() {
/* 454 */           return delegate;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@Nullable Object object) {
/* 459 */           return (object != null && Collections2.safeContains(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean containsAll(Collection<?> collection) {
/* 464 */           return standardContainsAll(collection);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object object) {
/* 469 */           return (object != null && Collections2.safeRemove(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 474 */           return standardRemoveAll(c);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> createEntrySet() {
/* 481 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 486 */     return this.countMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 491 */     return this.countMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 498 */     final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>()
/*     */       {
/* 500 */         private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this
/* 501 */           .countMap.entrySet().iterator();
/*     */ 
/*     */         
/*     */         protected Multiset.Entry<E> computeNext() {
/*     */           while (true) {
/* 506 */             if (!this.mapEntries.hasNext()) {
/* 507 */               return endOfData();
/*     */             }
/* 509 */             Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
/* 510 */             int count = ((AtomicInteger)mapEntry.getValue()).get();
/* 511 */             if (count != 0) {
/* 512 */               return Multisets.immutableEntry(mapEntry.getKey(), count);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 518 */     return new ForwardingIterator<Multiset.Entry<E>>()
/*     */       {
/*     */         private Multiset.Entry<E> last;
/*     */         
/*     */         protected Iterator<Multiset.Entry<E>> delegate() {
/* 523 */           return readOnlyIterator;
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 528 */           this.last = super.next();
/* 529 */           return this.last;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 534 */           CollectPreconditions.checkRemove((this.last != null));
/* 535 */           ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
/* 536 */           this.last = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 543 */     this.countMap.clear();
/*     */   }
/*     */   
/*     */   private class EntrySet extends AbstractMultiset<E>.EntrySet {
/*     */     private EntrySet() {}
/*     */     
/*     */     ConcurrentHashMultiset<E> multiset() {
/* 550 */       return ConcurrentHashMultiset.this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 560 */       return snapshot().toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 565 */       return snapshot().toArray(array);
/*     */     }
/*     */     
/*     */     private List<Multiset.Entry<E>> snapshot() {
/* 569 */       List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
/*     */       
/* 571 */       Iterators.addAll(list, iterator());
/* 572 */       return list;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 580 */     stream.defaultWriteObject();
/* 581 */     stream.writeObject(this.countMap);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 585 */     stream.defaultReadObject();
/*     */ 
/*     */     
/* 588 */     ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap<E, Integer>)stream.readObject();
/* 589 */     FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ConcurrentHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */