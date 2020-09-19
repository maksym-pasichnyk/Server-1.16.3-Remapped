/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.util.Arrays;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedArrayStringMap
/*     */   implements IndexedStringMap
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */   private static final long serialVersionUID = -5748905872274478116L;
/*     */   private static final int HASHVAL = 31;
/*     */   
/*  62 */   private static final TriConsumer<String, Object, StringMap> PUT_ALL = new TriConsumer<String, Object, StringMap>()
/*     */     {
/*     */       public void accept(String key, Object value, StringMap contextData) {
/*  65 */         contextData.putValue(key, value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final String[] EMPTY = new String[0];
/*     */   
/*     */   private static final String FROZEN = "Frozen collection cannot be modified";
/*  75 */   private transient String[] keys = EMPTY;
/*  76 */   private transient Object[] values = (Object[])EMPTY;
/*     */ 
/*     */ 
/*     */   
/*     */   private transient int size;
/*     */ 
/*     */   
/*     */   private int threshold;
/*     */ 
/*     */   
/*     */   private boolean immutable;
/*     */ 
/*     */   
/*     */   private transient boolean iterating;
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedArrayStringMap() {
/*  94 */     this(4);
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(int initialCapacity) {
/*  98 */     if (initialCapacity < 1) {
/*  99 */       throw new IllegalArgumentException("Initial capacity must be at least one but was " + initialCapacity);
/*     */     }
/* 101 */     this.threshold = ceilingNextPowerOfTwo(initialCapacity);
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(ReadOnlyStringMap other) {
/* 105 */     if (other instanceof SortedArrayStringMap) {
/* 106 */       initFrom0((SortedArrayStringMap)other);
/* 107 */     } else if (other != null) {
/* 108 */       resize(ceilingNextPowerOfTwo(other.size()));
/* 109 */       other.forEach(PUT_ALL, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(Map<String, ?> map) {
/* 114 */     resize(ceilingNextPowerOfTwo(map.size()));
/* 115 */     for (Map.Entry<String, ?> entry : map.entrySet()) {
/* 116 */       putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertNotFrozen() {
/* 121 */     if (this.immutable) {
/* 122 */       throw new UnsupportedOperationException("Frozen collection cannot be modified");
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertNoConcurrentModification() {
/* 127 */     if (this.iterating) {
/* 128 */       throw new ConcurrentModificationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 134 */     if (this.keys == EMPTY) {
/*     */       return;
/*     */     }
/* 137 */     assertNotFrozen();
/* 138 */     assertNoConcurrentModification();
/*     */     
/* 140 */     Arrays.fill((Object[])this.keys, 0, this.size, (Object)null);
/* 141 */     Arrays.fill(this.values, 0, this.size, (Object)null);
/* 142 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 147 */     return (indexOfKey(key) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toMap() {
/* 152 */     Map<String, String> result = new HashMap<>(size());
/* 153 */     for (int i = 0; i < size(); i++) {
/* 154 */       Object value = getValueAt(i);
/* 155 */       result.put(getKeyAt(i), (value == null) ? null : String.valueOf(value));
/*     */     } 
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 162 */     this.immutable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 167 */     return this.immutable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 173 */     int index = indexOfKey(key);
/* 174 */     if (index < 0) {
/* 175 */       return null;
/*     */     }
/* 177 */     return (V)this.values[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 182 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOfKey(String key) {
/* 187 */     if (this.keys == EMPTY) {
/* 188 */       return -1;
/*     */     }
/* 190 */     if (key == null) {
/* 191 */       return nullKeyIndex();
/*     */     }
/* 193 */     int start = (this.size > 0 && this.keys[0] == null) ? 1 : 0;
/* 194 */     return Arrays.binarySearch((Object[])this.keys, start, this.size, key);
/*     */   }
/*     */   
/*     */   private int nullKeyIndex() {
/* 198 */     return (this.size > 0 && this.keys[0] == null) ? 0 : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 203 */     assertNotFrozen();
/* 204 */     assertNoConcurrentModification();
/*     */     
/* 206 */     if (this.keys == EMPTY) {
/* 207 */       inflateTable(this.threshold);
/*     */     }
/* 209 */     int index = indexOfKey(key);
/* 210 */     if (index >= 0) {
/* 211 */       this.keys[index] = key;
/* 212 */       this.values[index] = value;
/*     */     } else {
/* 214 */       insertAt(index ^ 0xFFFFFFFF, key, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void insertAt(int index, String key, Object value) {
/* 219 */     ensureCapacity();
/* 220 */     System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
/* 221 */     System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
/* 222 */     this.keys[index] = key;
/* 223 */     this.values[index] = value;
/* 224 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(ReadOnlyStringMap source) {
/* 229 */     if (source == this || source == null || source.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 235 */     assertNotFrozen();
/* 236 */     assertNoConcurrentModification();
/*     */     
/* 238 */     if (source instanceof SortedArrayStringMap) {
/* 239 */       if (this.size == 0) {
/* 240 */         initFrom0((SortedArrayStringMap)source);
/*     */       } else {
/* 242 */         merge((SortedArrayStringMap)source);
/*     */       } 
/* 244 */     } else if (source != null) {
/* 245 */       source.forEach(PUT_ALL, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initFrom0(SortedArrayStringMap other) {
/* 250 */     if (this.keys.length < other.size) {
/* 251 */       this.keys = new String[other.threshold];
/* 252 */       this.values = new Object[other.threshold];
/*     */     } 
/* 254 */     System.arraycopy(other.keys, 0, this.keys, 0, other.size);
/* 255 */     System.arraycopy(other.values, 0, this.values, 0, other.size);
/*     */     
/* 257 */     this.size = other.size;
/* 258 */     this.threshold = other.threshold;
/*     */   }
/*     */   
/*     */   private void merge(SortedArrayStringMap other) {
/* 262 */     String[] myKeys = this.keys;
/* 263 */     Object[] myVals = this.values;
/* 264 */     int newSize = other.size + this.size;
/* 265 */     this.threshold = ceilingNextPowerOfTwo(newSize);
/* 266 */     if (this.keys.length < this.threshold) {
/* 267 */       this.keys = new String[this.threshold];
/* 268 */       this.values = new Object[this.threshold];
/*     */     } 
/*     */     
/* 271 */     boolean overwrite = true;
/* 272 */     if (other.size() > size()) {
/*     */       
/* 274 */       System.arraycopy(myKeys, 0, this.keys, other.size, this.size);
/* 275 */       System.arraycopy(myVals, 0, this.values, other.size, this.size);
/*     */ 
/*     */       
/* 278 */       System.arraycopy(other.keys, 0, this.keys, 0, other.size);
/* 279 */       System.arraycopy(other.values, 0, this.values, 0, other.size);
/* 280 */       this.size = other.size;
/*     */ 
/*     */       
/* 283 */       overwrite = false;
/*     */     } else {
/* 285 */       System.arraycopy(myKeys, 0, this.keys, 0, this.size);
/* 286 */       System.arraycopy(myVals, 0, this.values, 0, this.size);
/*     */ 
/*     */       
/* 289 */       System.arraycopy(other.keys, 0, this.keys, this.size, other.size);
/* 290 */       System.arraycopy(other.values, 0, this.values, this.size, other.size);
/*     */     } 
/*     */ 
/*     */     
/* 294 */     for (int i = this.size; i < newSize; i++) {
/* 295 */       int index = indexOfKey(this.keys[i]);
/* 296 */       if (index < 0) {
/* 297 */         insertAt(index ^ 0xFFFFFFFF, this.keys[i], this.values[i]);
/* 298 */       } else if (overwrite) {
/* 299 */         this.keys[index] = this.keys[i];
/* 300 */         this.values[index] = this.values[i];
/*     */       } 
/*     */     } 
/*     */     
/* 304 */     Arrays.fill((Object[])this.keys, this.size, newSize, (Object)null);
/* 305 */     Arrays.fill(this.values, this.size, newSize, (Object)null);
/*     */   }
/*     */   
/*     */   private void ensureCapacity() {
/* 309 */     if (this.size >= this.threshold) {
/* 310 */       resize(this.threshold * 2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resize(int newCapacity) {
/* 315 */     String[] oldKeys = this.keys;
/* 316 */     Object[] oldValues = this.values;
/*     */     
/* 318 */     this.keys = new String[newCapacity];
/* 319 */     this.values = new Object[newCapacity];
/*     */     
/* 321 */     System.arraycopy(oldKeys, 0, this.keys, 0, this.size);
/* 322 */     System.arraycopy(oldValues, 0, this.values, 0, this.size);
/*     */     
/* 324 */     this.threshold = newCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inflateTable(int toSize) {
/* 331 */     this.threshold = toSize;
/* 332 */     this.keys = new String[toSize];
/* 333 */     this.values = new Object[toSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 338 */     if (this.keys == EMPTY) {
/*     */       return;
/*     */     }
/*     */     
/* 342 */     int index = indexOfKey(key);
/* 343 */     if (index >= 0) {
/* 344 */       assertNotFrozen();
/* 345 */       assertNoConcurrentModification();
/*     */       
/* 347 */       System.arraycopy(this.keys, index + 1, this.keys, index, this.size - 1 - index);
/* 348 */       System.arraycopy(this.values, index + 1, this.values, index, this.size - 1 - index);
/* 349 */       this.keys[this.size - 1] = null;
/* 350 */       this.values[this.size - 1] = null;
/* 351 */       this.size--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKeyAt(int index) {
/* 357 */     if (index < 0 || index >= this.size) {
/* 358 */       return null;
/*     */     }
/* 360 */     return this.keys[index];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValueAt(int index) {
/* 366 */     if (index < 0 || index >= this.size) {
/* 367 */       return null;
/*     */     }
/* 369 */     return (V)this.values[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 374 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) {
/* 380 */     this.iterating = true;
/*     */     try {
/* 382 */       for (int i = 0; i < this.size; i++) {
/* 383 */         action.accept(this.keys[i], (V)this.values[i]);
/*     */       }
/*     */     } finally {
/* 386 */       this.iterating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V, T> void forEach(TriConsumer<String, ? super V, T> action, T state) {
/* 393 */     this.iterating = true;
/*     */     try {
/* 395 */       for (int i = 0; i < this.size; i++) {
/* 396 */         action.accept(this.keys[i], (V)this.values[i], state);
/*     */       }
/*     */     } finally {
/* 399 */       this.iterating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 405 */     if (obj == this) {
/* 406 */       return true;
/*     */     }
/* 408 */     if (!(obj instanceof SortedArrayStringMap)) {
/* 409 */       return false;
/*     */     }
/* 411 */     SortedArrayStringMap other = (SortedArrayStringMap)obj;
/* 412 */     if (size() != other.size()) {
/* 413 */       return false;
/*     */     }
/* 415 */     for (int i = 0; i < size(); i++) {
/* 416 */       if (!Objects.equals(this.keys[i], other.keys[i])) {
/* 417 */         return false;
/*     */       }
/* 419 */       if (!Objects.equals(this.values[i], other.values[i])) {
/* 420 */         return false;
/*     */       }
/*     */     } 
/* 423 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 428 */     int result = 37;
/* 429 */     result = 31 * result + this.size;
/* 430 */     result = 31 * result + hashCode((Object[])this.keys, this.size);
/* 431 */     result = 31 * result + hashCode(this.values, this.size);
/* 432 */     return result;
/*     */   }
/*     */   
/*     */   private static int hashCode(Object[] values, int length) {
/* 436 */     int result = 1;
/* 437 */     for (int i = 0; i < length; i++) {
/* 438 */       result = 31 * result + ((values[i] == null) ? 0 : values[i].hashCode());
/*     */     }
/* 440 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 445 */     StringBuilder sb = new StringBuilder(256);
/* 446 */     sb.append('{');
/* 447 */     for (int i = 0; i < this.size; i++) {
/* 448 */       if (i > 0) {
/* 449 */         sb.append(", ");
/*     */       }
/* 451 */       sb.append(this.keys[i]).append('=');
/* 452 */       sb.append((this.values[i] == this) ? "(this map)" : this.values[i]);
/*     */     } 
/* 454 */     sb.append('}');
/* 455 */     return sb.toString();
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
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 471 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 474 */     if (this.keys == EMPTY) {
/* 475 */       s.writeInt(ceilingNextPowerOfTwo(this.threshold));
/*     */     } else {
/* 477 */       s.writeInt(this.keys.length);
/*     */     } 
/*     */ 
/*     */     
/* 481 */     s.writeInt(this.size);
/*     */ 
/*     */     
/* 484 */     if (this.size > 0) {
/* 485 */       for (int i = 0; i < this.size; i++) {
/* 486 */         s.writeObject(this.keys[i]);
/*     */         try {
/* 488 */           s.writeObject(new MarshalledObject(this.values[i]));
/* 489 */         } catch (Exception e) {
/* 490 */           handleSerializationException(e, i, this.keys[i]);
/* 491 */           s.writeObject(null);
/*     */         } 
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
/*     */   private static int ceilingNextPowerOfTwo(int x) {
/* 506 */     int BITS_PER_INT = 32;
/* 507 */     return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 516 */     s.defaultReadObject();
/*     */ 
/*     */     
/* 519 */     this.keys = EMPTY;
/* 520 */     this.values = (Object[])EMPTY;
/*     */ 
/*     */     
/* 523 */     int capacity = s.readInt();
/* 524 */     if (capacity < 0) {
/* 525 */       throw new InvalidObjectException("Illegal capacity: " + capacity);
/*     */     }
/*     */ 
/*     */     
/* 529 */     int mappings = s.readInt();
/* 530 */     if (mappings < 0) {
/* 531 */       throw new InvalidObjectException("Illegal mappings count: " + mappings);
/*     */     }
/*     */ 
/*     */     
/* 535 */     if (mappings > 0) {
/* 536 */       inflateTable(capacity);
/*     */     } else {
/* 538 */       this.threshold = capacity;
/*     */     } 
/*     */ 
/*     */     
/* 542 */     for (int i = 0; i < mappings; i++) {
/* 543 */       this.keys[i] = (String)s.readObject();
/*     */       try {
/* 545 */         MarshalledObject<Object> marshalledObject = (MarshalledObject<Object>)s.readObject();
/* 546 */         this.values[i] = (marshalledObject == null) ? null : marshalledObject.get();
/* 547 */       } catch (Exception|LinkageError error) {
/* 548 */         handleSerializationException(error, i, this.keys[i]);
/* 549 */         this.values[i] = null;
/*     */       } 
/*     */     } 
/* 552 */     this.size = mappings;
/*     */   }
/*     */   
/*     */   private void handleSerializationException(Throwable t, int i, String key) {
/* 556 */     StatusLogger.getLogger().warn("Ignoring {} for key[{}] ('{}')", String.valueOf(t), Integer.valueOf(i), this.keys[i]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\SortedArrayStringMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */