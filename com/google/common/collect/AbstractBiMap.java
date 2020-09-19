/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   @RetainedWith
/*     */   transient AbstractBiMap<V, K> inverse;
/*     */   private transient Set<K> keySet;
/*     */   private transient Set<V> valueSet;
/*     */   private transient Set<Map.Entry<K, V>> entrySet;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  60 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  65 */     this.delegate = backward;
/*  66 */     this.inverse = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  71 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   K checkKey(@Nullable K key) {
/*  79 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   V checkValue(@Nullable V value) {
/*  87 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  95 */     Preconditions.checkState((this.delegate == null));
/*  96 */     Preconditions.checkState((this.inverse == null));
/*  97 */     Preconditions.checkArgument(forward.isEmpty());
/*  98 */     Preconditions.checkArgument(backward.isEmpty());
/*  99 */     Preconditions.checkArgument((forward != backward));
/* 100 */     this.delegate = forward;
/* 101 */     this.inverse = makeInverse(backward);
/*     */   }
/*     */   
/*     */   AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
/* 105 */     return new Inverse<>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(AbstractBiMap<V, K> inverse) {
/* 109 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 116 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@Nullable K key, @Nullable V value) {
/* 124 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(@Nullable K key, @Nullable V value) {
/* 130 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   private V putInBothMaps(@Nullable K key, @Nullable V value, boolean force) {
/* 134 */     checkKey(key);
/* 135 */     checkValue(value);
/* 136 */     boolean containedKey = containsKey(key);
/* 137 */     if (containedKey && Objects.equal(value, get(key))) {
/* 138 */       return value;
/*     */     }
/* 140 */     if (force) {
/* 141 */       inverse().remove(value);
/*     */     } else {
/* 143 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
/*     */     } 
/* 145 */     V oldValue = this.delegate.put(key, value);
/* 146 */     updateInverseMap(key, containedKey, oldValue, value);
/* 147 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 151 */     if (containedKey) {
/* 152 */       removeFromInverseMap(oldValue);
/*     */     }
/* 154 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@Nullable Object key) {
/* 160 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private V removeFromBothMaps(Object key) {
/* 165 */     V oldValue = this.delegate.remove(key);
/* 166 */     removeFromInverseMap(oldValue);
/* 167 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(V oldValue) {
/* 171 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 178 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 179 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 185 */     this.delegate.replaceAll(function);
/* 186 */     this.inverse.delegate.clear();
/* 187 */     Map.Entry<K, V> broken = null;
/* 188 */     Iterator<Map.Entry<K, V>> itr = this.delegate.entrySet().iterator();
/* 189 */     while (itr.hasNext()) {
/* 190 */       Map.Entry<K, V> entry = itr.next();
/* 191 */       K k = entry.getKey();
/* 192 */       V v = entry.getValue();
/* 193 */       K conflict = (K)this.inverse.delegate.putIfAbsent((K)v, (V)k);
/* 194 */       if (conflict != null) {
/* 195 */         broken = entry;
/*     */ 
/*     */         
/* 198 */         itr.remove();
/*     */       } 
/*     */     } 
/* 201 */     if (broken != null) {
/* 202 */       throw new IllegalArgumentException("value already present: " + broken.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 208 */     this.delegate.clear();
/* 209 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 216 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 223 */     Set<K> result = this.keySet;
/* 224 */     return (result == null) ? (this.keySet = new KeySet()) : result;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     private KeySet() {}
/*     */     
/*     */     protected Set<K> delegate() {
/* 231 */       return AbstractBiMap.this.delegate.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 236 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 241 */       if (!contains(key)) {
/* 242 */         return false;
/*     */       }
/* 244 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 245 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 250 */       return standardRemoveAll(keysToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 255 */       return standardRetainAll(keysToRetain);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 260 */       return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
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
/*     */   public Set<V> values() {
/* 272 */     Set<V> result = this.valueSet;
/* 273 */     return (result == null) ? (this.valueSet = new ValueSet()) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet
/*     */     extends ForwardingSet<V> {
/* 278 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */ 
/*     */     
/*     */     protected Set<V> delegate() {
/* 282 */       return this.valuesDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 287 */       return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 292 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 297 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 302 */       return standardToString();
/*     */     }
/*     */ 
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 310 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 311 */     return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*     */   }
/*     */   
/*     */   class BiMapEntry extends ForwardingMapEntry<K, V> {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     BiMapEntry(Map.Entry<K, V> delegate) {
/* 318 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map.Entry<K, V> delegate() {
/* 323 */       return this.delegate;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 329 */       Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
/*     */       
/* 331 */       if (Objects.equal(value, getValue())) {
/* 332 */         return value;
/*     */       }
/* 334 */       Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
/* 335 */       V oldValue = this.delegate.setValue(value);
/* 336 */       Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
/* 337 */       AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 338 */       return oldValue;
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/* 343 */     final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
/* 344 */     return new Iterator<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> entry;
/*     */         
/*     */         public boolean hasNext() {
/* 349 */           return iterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 354 */           this.entry = iterator.next();
/* 355 */           return new AbstractBiMap.BiMapEntry(this.entry);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 360 */           CollectPreconditions.checkRemove((this.entry != null));
/* 361 */           V value = this.entry.getValue();
/* 362 */           iterator.remove();
/* 363 */           AbstractBiMap.this.removeFromInverseMap(value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ForwardingSet<Map.Entry<K, V>> {
/* 370 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 374 */       return this.esDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 379 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object object) {
/* 384 */       if (!this.esDelegate.contains(object)) {
/* 385 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 389 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 390 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 396 */       this.esDelegate.remove(entry);
/* 397 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 402 */       return AbstractBiMap.this.entrySetIterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 409 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 414 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 419 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 424 */       return standardContainsAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 429 */       return standardRemoveAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 434 */       return standardRetainAll(c);
/*     */     }
/*     */     
/*     */     private EntrySet() {} }
/*     */   
/*     */   static class Inverse<K, V> extends AbstractBiMap<K, V> {
/*     */     Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/* 441 */       super(backward, forward);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     K checkKey(K key) {
/* 455 */       return this.inverse.checkValue(key);
/*     */     }
/*     */ 
/*     */     
/*     */     V checkValue(V value) {
/* 460 */       return this.inverse.checkKey(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 468 */       stream.defaultWriteObject();
/* 469 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 475 */       stream.defaultReadObject();
/* 476 */       setInverse((AbstractBiMap<V, K>)stream.readObject());
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object readResolve() {
/* 481 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */