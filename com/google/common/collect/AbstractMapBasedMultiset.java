/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<E, Count> backingMap;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
/*  61 */     this.backingMap = (Map<E, Count>)Preconditions.checkNotNull(backingMap);
/*  62 */     this.size = super.size();
/*     */   }
/*     */ 
/*     */   
/*     */   void setBackingMap(Map<E, Count> backingMap) {
/*  67 */     this.backingMap = backingMap;
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
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  81 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/*  86 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/*  87 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/*  92 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/*  97 */           final Map.Entry<E, Count> mapEntry = backingEntries.next();
/*  98 */           this.toRemove = mapEntry;
/*  99 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 102 */                 return (E)mapEntry.getKey();
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 107 */                 Count count = (Count)mapEntry.getValue();
/* 108 */                 if (count == null || count.get() == 0) {
/* 109 */                   Count frequency = (Count)AbstractMapBasedMultiset.this.backingMap.get(getElement());
/* 110 */                   if (frequency != null) {
/* 111 */                     return frequency.get();
/*     */                   }
/*     */                 } 
/* 114 */                 return (count == null) ? 0 : count.get();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 121 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 122 */           AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - ((Count)this.toRemove.getValue()).getAndSet(0);
/* 123 */           backingEntries.remove();
/* 124 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 130 */     Preconditions.checkNotNull(action);
/* 131 */     this.backingMap.forEach((element, count) -> action.accept(element, count.get()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 136 */     for (Count frequency : this.backingMap.values()) {
/* 137 */       frequency.set(0);
/*     */     }
/* 139 */     this.backingMap.clear();
/* 140 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 145 */     return this.backingMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 152 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 157 */     return new MapBasedMultisetIterator();
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
/*     */   private class MapBasedMultisetIterator
/*     */     implements Iterator<E>
/*     */   {
/* 172 */     final Iterator<Map.Entry<E, Count>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
/*     */     
/*     */     Map.Entry<E, Count> currentEntry;
/*     */     
/*     */     public boolean hasNext() {
/* 177 */       return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */     int occurrencesLeft; boolean canRemove;
/*     */     
/*     */     public E next() {
/* 182 */       if (this.occurrencesLeft == 0) {
/* 183 */         this.currentEntry = this.entryIterator.next();
/* 184 */         this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
/*     */       } 
/* 186 */       this.occurrencesLeft--;
/* 187 */       this.canRemove = true;
/* 188 */       return this.currentEntry.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 193 */       CollectPreconditions.checkRemove(this.canRemove);
/* 194 */       int frequency = ((Count)this.currentEntry.getValue()).get();
/* 195 */       if (frequency <= 0) {
/* 196 */         throw new ConcurrentModificationException();
/*     */       }
/* 198 */       if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 199 */         this.entryIterator.remove();
/*     */       }
/* 201 */       AbstractMapBasedMultiset.this.size--;
/* 202 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/* 208 */     Count frequency = Maps.<Count>safeGet(this.backingMap, element);
/* 209 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public int add(@Nullable E element, int occurrences) {
/*     */     int oldCount;
/* 224 */     if (occurrences == 0) {
/* 225 */       return count(element);
/*     */     }
/* 227 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 228 */     Count frequency = this.backingMap.get(element);
/*     */     
/* 230 */     if (frequency == null) {
/* 231 */       oldCount = 0;
/* 232 */       this.backingMap.put(element, new Count(occurrences));
/*     */     } else {
/* 234 */       oldCount = frequency.get();
/* 235 */       long newCount = oldCount + occurrences;
/* 236 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 237 */       frequency.add(occurrences);
/*     */     } 
/* 239 */     this.size += occurrences;
/* 240 */     return oldCount;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@Nullable Object element, int occurrences) {
/*     */     int numberRemoved;
/* 246 */     if (occurrences == 0) {
/* 247 */       return count(element);
/*     */     }
/* 249 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 250 */     Count frequency = this.backingMap.get(element);
/* 251 */     if (frequency == null) {
/* 252 */       return 0;
/*     */     }
/*     */     
/* 255 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 258 */     if (oldCount > occurrences) {
/* 259 */       numberRemoved = occurrences;
/*     */     } else {
/* 261 */       numberRemoved = oldCount;
/* 262 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 265 */     frequency.add(-numberRemoved);
/* 266 */     this.size -= numberRemoved;
/* 267 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(@Nullable E element, int count) {
/*     */     int oldCount;
/* 274 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 278 */     if (count == 0) {
/* 279 */       Count existingCounter = this.backingMap.remove(element);
/* 280 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 282 */       Count existingCounter = this.backingMap.get(element);
/* 283 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 285 */       if (existingCounter == null) {
/* 286 */         this.backingMap.put(element, new Count(count));
/*     */       }
/*     */     } 
/*     */     
/* 290 */     this.size += (count - oldCount);
/* 291 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(@Nullable Count i, int count) {
/* 295 */     if (i == null) {
/* 296 */       return 0;
/*     */     }
/*     */     
/* 299 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObjectNoData() throws ObjectStreamException {
/* 305 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractMapBasedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */