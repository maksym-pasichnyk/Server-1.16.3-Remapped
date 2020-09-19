/*     */ package it.unimi.dsi.fastutil.bytes;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanArrays;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanCollection;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanIterator;
/*     */ import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Byte2BooleanArrayMap
/*     */   extends AbstractByte2BooleanMap
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient byte[] key;
/*     */   private transient boolean[] value;
/*     */   private int size;
/*     */   
/*     */   public Byte2BooleanArrayMap(byte[] key, boolean[] value) {
/*  56 */     this.key = key;
/*  57 */     this.value = value;
/*  58 */     this.size = key.length;
/*  59 */     if (key.length != value.length) {
/*  60 */       throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte2BooleanArrayMap() {
/*  67 */     this.key = ByteArrays.EMPTY_ARRAY;
/*  68 */     this.value = BooleanArrays.EMPTY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte2BooleanArrayMap(int capacity) {
/*  77 */     this.key = new byte[capacity];
/*  78 */     this.value = new boolean[capacity];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte2BooleanArrayMap(Byte2BooleanMap m) {
/*  87 */     this(m.size());
/*  88 */     putAll(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte2BooleanArrayMap(Map<? extends Byte, ? extends Boolean> m) {
/*  97 */     this(m.size());
/*  98 */     putAll(m);
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
/*     */   public Byte2BooleanArrayMap(byte[] key, boolean[] value, int size) {
/* 117 */     this.key = key;
/* 118 */     this.value = value;
/* 119 */     this.size = size;
/* 120 */     if (key.length != value.length) {
/* 121 */       throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
/*     */     }
/* 123 */     if (size > key.length)
/* 124 */       throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")"); 
/*     */   }
/*     */   
/*     */   private final class EntrySet
/*     */     extends AbstractObjectSet<Byte2BooleanMap.Entry> implements Byte2BooleanMap.FastEntrySet {
/*     */     public ObjectIterator<Byte2BooleanMap.Entry> iterator() {
/* 130 */       return new ObjectIterator<Byte2BooleanMap.Entry>() {
/* 131 */           int curr = -1; int next = 0;
/*     */           
/*     */           public boolean hasNext() {
/* 134 */             return (this.next < Byte2BooleanArrayMap.this.size);
/*     */           }
/*     */ 
/*     */           
/*     */           public Byte2BooleanMap.Entry next() {
/* 139 */             if (!hasNext())
/* 140 */               throw new NoSuchElementException(); 
/* 141 */             return new AbstractByte2BooleanMap.BasicEntry(Byte2BooleanArrayMap.this.key[this.curr = this.next], Byte2BooleanArrayMap.this.value[this.next++]);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 145 */             if (this.curr == -1)
/* 146 */               throw new IllegalStateException(); 
/* 147 */             this.curr = -1;
/* 148 */             int tail = Byte2BooleanArrayMap.this.size-- - this.next--;
/* 149 */             System.arraycopy(Byte2BooleanArrayMap.this.key, this.next + 1, Byte2BooleanArrayMap.this.key, this.next, tail);
/* 150 */             System.arraycopy(Byte2BooleanArrayMap.this.value, this.next + 1, Byte2BooleanArrayMap.this.value, this.next, tail);
/*     */           }
/*     */         };
/*     */     }
/*     */     private EntrySet() {}
/*     */     public ObjectIterator<Byte2BooleanMap.Entry> fastIterator() {
/* 156 */       return new ObjectIterator<Byte2BooleanMap.Entry>() {
/* 157 */           int next = 0; int curr = -1;
/* 158 */           final AbstractByte2BooleanMap.BasicEntry entry = new AbstractByte2BooleanMap.BasicEntry();
/*     */           
/*     */           public boolean hasNext() {
/* 161 */             return (this.next < Byte2BooleanArrayMap.this.size);
/*     */           }
/*     */ 
/*     */           
/*     */           public Byte2BooleanMap.Entry next() {
/* 166 */             if (!hasNext())
/* 167 */               throw new NoSuchElementException(); 
/* 168 */             this.entry.key = Byte2BooleanArrayMap.this.key[this.curr = this.next];
/* 169 */             this.entry.value = Byte2BooleanArrayMap.this.value[this.next++];
/* 170 */             return this.entry;
/*     */           }
/*     */           
/*     */           public void remove() {
/* 174 */             if (this.curr == -1)
/* 175 */               throw new IllegalStateException(); 
/* 176 */             this.curr = -1;
/* 177 */             int tail = Byte2BooleanArrayMap.this.size-- - this.next--;
/* 178 */             System.arraycopy(Byte2BooleanArrayMap.this.key, this.next + 1, Byte2BooleanArrayMap.this.key, this.next, tail);
/* 179 */             System.arraycopy(Byte2BooleanArrayMap.this.value, this.next + 1, Byte2BooleanArrayMap.this.value, this.next, tail);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public int size() {
/* 185 */       return Byte2BooleanArrayMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 190 */       if (!(o instanceof Map.Entry))
/* 191 */         return false; 
/* 192 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 193 */       if (e.getKey() == null || !(e.getKey() instanceof Byte))
/* 194 */         return false; 
/* 195 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 196 */         return false; 
/* 197 */       byte k = ((Byte)e.getKey()).byteValue();
/* 198 */       return (Byte2BooleanArrayMap.this.containsKey(k) && Byte2BooleanArrayMap.this
/* 199 */         .get(k) == ((Boolean)e.getValue()).booleanValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 204 */       if (!(o instanceof Map.Entry))
/* 205 */         return false; 
/* 206 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 207 */       if (e.getKey() == null || !(e.getKey() instanceof Byte))
/* 208 */         return false; 
/* 209 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 210 */         return false; 
/* 211 */       byte k = ((Byte)e.getKey()).byteValue();
/* 212 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/* 213 */       int oldPos = Byte2BooleanArrayMap.this.findKey(k);
/* 214 */       if (oldPos == -1 || v != Byte2BooleanArrayMap.this.value[oldPos])
/* 215 */         return false; 
/* 216 */       int tail = Byte2BooleanArrayMap.this.size - oldPos - 1;
/* 217 */       System.arraycopy(Byte2BooleanArrayMap.this.key, oldPos + 1, Byte2BooleanArrayMap.this.key, oldPos, tail);
/* 218 */       System.arraycopy(Byte2BooleanArrayMap.this.value, oldPos + 1, Byte2BooleanArrayMap.this.value, oldPos, tail);
/*     */       
/* 220 */       Byte2BooleanArrayMap.this.size--;
/* 221 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public Byte2BooleanMap.FastEntrySet byte2BooleanEntrySet() {
/* 226 */     return new EntrySet();
/*     */   }
/*     */   private int findKey(byte k) {
/* 229 */     byte[] key = this.key;
/* 230 */     for (int i = this.size; i-- != 0;) {
/* 231 */       if (key[i] == k)
/* 232 */         return i; 
/* 233 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean get(byte k) {
/* 238 */     byte[] key = this.key;
/* 239 */     for (int i = this.size; i-- != 0;) {
/* 240 */       if (key[i] == k)
/* 241 */         return this.value[i]; 
/* 242 */     }  return this.defRetValue;
/*     */   }
/*     */   
/*     */   public int size() {
/* 246 */     return this.size;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 250 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public boolean containsKey(byte k) {
/* 254 */     return (findKey(k) != -1);
/*     */   }
/*     */   
/*     */   public boolean containsValue(boolean v) {
/* 258 */     for (int i = this.size; i-- != 0;) {
/* 259 */       if (this.value[i] == v)
/* 260 */         return true; 
/* 261 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 265 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean put(byte k, boolean v) {
/* 270 */     int oldKey = findKey(k);
/* 271 */     if (oldKey != -1) {
/* 272 */       boolean oldValue = this.value[oldKey];
/* 273 */       this.value[oldKey] = v;
/* 274 */       return oldValue;
/*     */     } 
/* 276 */     if (this.size == this.key.length) {
/* 277 */       byte[] newKey = new byte[(this.size == 0) ? 2 : (this.size * 2)];
/* 278 */       boolean[] newValue = new boolean[(this.size == 0) ? 2 : (this.size * 2)];
/* 279 */       for (int i = this.size; i-- != 0; ) {
/* 280 */         newKey[i] = this.key[i];
/* 281 */         newValue[i] = this.value[i];
/*     */       } 
/* 283 */       this.key = newKey;
/* 284 */       this.value = newValue;
/*     */     } 
/* 286 */     this.key[this.size] = k;
/* 287 */     this.value[this.size] = v;
/* 288 */     this.size++;
/* 289 */     return this.defRetValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(byte k) {
/* 294 */     int oldPos = findKey(k);
/* 295 */     if (oldPos == -1)
/* 296 */       return this.defRetValue; 
/* 297 */     boolean oldValue = this.value[oldPos];
/* 298 */     int tail = this.size - oldPos - 1;
/* 299 */     System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
/* 300 */     System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
/* 301 */     this.size--;
/* 302 */     return oldValue;
/*     */   }
/*     */   
/*     */   public ByteSet keySet() {
/* 306 */     return new AbstractByteSet()
/*     */       {
/*     */         public boolean contains(byte k) {
/* 309 */           return (Byte2BooleanArrayMap.this.findKey(k) != -1);
/*     */         }
/*     */         
/*     */         public boolean remove(byte k) {
/* 313 */           int oldPos = Byte2BooleanArrayMap.this.findKey(k);
/* 314 */           if (oldPos == -1)
/* 315 */             return false; 
/* 316 */           int tail = Byte2BooleanArrayMap.this.size - oldPos - 1;
/* 317 */           System.arraycopy(Byte2BooleanArrayMap.this.key, oldPos + 1, Byte2BooleanArrayMap.this.key, oldPos, tail);
/* 318 */           System.arraycopy(Byte2BooleanArrayMap.this.value, oldPos + 1, Byte2BooleanArrayMap.this.value, oldPos, tail);
/* 319 */           Byte2BooleanArrayMap.this.size--;
/* 320 */           return true;
/*     */         }
/*     */         
/*     */         public ByteIterator iterator() {
/* 324 */           return new ByteIterator() {
/* 325 */               int pos = 0;
/*     */               
/*     */               public boolean hasNext() {
/* 328 */                 return (this.pos < Byte2BooleanArrayMap.this.size);
/*     */               }
/*     */ 
/*     */               
/*     */               public byte nextByte() {
/* 333 */                 if (!hasNext())
/* 334 */                   throw new NoSuchElementException(); 
/* 335 */                 return Byte2BooleanArrayMap.this.key[this.pos++];
/*     */               }
/*     */               
/*     */               public void remove() {
/* 339 */                 if (this.pos == 0)
/* 340 */                   throw new IllegalStateException(); 
/* 341 */                 int tail = Byte2BooleanArrayMap.this.size - this.pos;
/* 342 */                 System.arraycopy(Byte2BooleanArrayMap.this.key, this.pos, Byte2BooleanArrayMap.this.key, this.pos - 1, tail);
/* 343 */                 System.arraycopy(Byte2BooleanArrayMap.this.value, this.pos, Byte2BooleanArrayMap.this.value, this.pos - 1, tail);
/* 344 */                 Byte2BooleanArrayMap.this.size--;
/*     */               }
/*     */             };
/*     */         }
/*     */         
/*     */         public int size() {
/* 350 */           return Byte2BooleanArrayMap.this.size;
/*     */         }
/*     */         
/*     */         public void clear() {
/* 354 */           Byte2BooleanArrayMap.this.clear();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BooleanCollection values() {
/* 360 */     return (BooleanCollection)new AbstractBooleanCollection()
/*     */       {
/*     */         public boolean contains(boolean v) {
/* 363 */           return Byte2BooleanArrayMap.this.containsValue(v);
/*     */         }
/*     */         
/*     */         public BooleanIterator iterator() {
/* 367 */           return new BooleanIterator() {
/* 368 */               int pos = 0;
/*     */               
/*     */               public boolean hasNext() {
/* 371 */                 return (this.pos < Byte2BooleanArrayMap.this.size);
/*     */               }
/*     */ 
/*     */               
/*     */               public boolean nextBoolean() {
/* 376 */                 if (!hasNext())
/* 377 */                   throw new NoSuchElementException(); 
/* 378 */                 return Byte2BooleanArrayMap.this.value[this.pos++];
/*     */               }
/*     */               
/*     */               public void remove() {
/* 382 */                 if (this.pos == 0)
/* 383 */                   throw new IllegalStateException(); 
/* 384 */                 int tail = Byte2BooleanArrayMap.this.size - this.pos;
/* 385 */                 System.arraycopy(Byte2BooleanArrayMap.this.key, this.pos, Byte2BooleanArrayMap.this.key, this.pos - 1, tail);
/* 386 */                 System.arraycopy(Byte2BooleanArrayMap.this.value, this.pos, Byte2BooleanArrayMap.this.value, this.pos - 1, tail);
/* 387 */                 Byte2BooleanArrayMap.this.size--;
/*     */               }
/*     */             };
/*     */         }
/*     */         
/*     */         public int size() {
/* 393 */           return Byte2BooleanArrayMap.this.size;
/*     */         }
/*     */         
/*     */         public void clear() {
/* 397 */           Byte2BooleanArrayMap.this.clear();
/*     */         }
/*     */       };
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
/*     */   public Byte2BooleanArrayMap clone() {
/*     */     Byte2BooleanArrayMap c;
/*     */     try {
/* 416 */       c = (Byte2BooleanArrayMap)super.clone();
/* 417 */     } catch (CloneNotSupportedException cantHappen) {
/* 418 */       throw new InternalError();
/*     */     } 
/* 420 */     c.key = (byte[])this.key.clone();
/* 421 */     c.value = (boolean[])this.value.clone();
/* 422 */     return c;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 425 */     s.defaultWriteObject();
/* 426 */     for (int i = 0; i < this.size; i++) {
/* 427 */       s.writeByte(this.key[i]);
/* 428 */       s.writeBoolean(this.value[i]);
/*     */     } 
/*     */   }
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 432 */     s.defaultReadObject();
/* 433 */     this.key = new byte[this.size];
/* 434 */     this.value = new boolean[this.size];
/* 435 */     for (int i = 0; i < this.size; i++) {
/* 436 */       this.key[i] = s.readByte();
/* 437 */       this.value[i] = s.readBoolean();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\Byte2BooleanArrayMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */