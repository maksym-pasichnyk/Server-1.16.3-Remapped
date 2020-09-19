/*     */ package it.unimi.dsi.fastutil.shorts;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArrays;
/*     */ import it.unimi.dsi.fastutil.objects.AbstractObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShortArrayFrontCodedList
/*     */   extends AbstractObjectList<short[]>
/*     */   implements Serializable, Cloneable, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int n;
/*     */   protected final int ratio;
/*     */   protected final short[][] array;
/*     */   protected transient long[] p;
/*     */   
/*     */   public ShortArrayFrontCodedList(Iterator<short[]> arrays, int ratio) {
/* 110 */     if (ratio < 1)
/* 111 */       throw new IllegalArgumentException("Illegal ratio (" + ratio + ")"); 
/* 112 */     short[][] array = ShortBigArrays.EMPTY_BIG_ARRAY;
/* 113 */     long[] p = LongArrays.EMPTY_ARRAY;
/* 114 */     short[][] a = new short[2][];
/* 115 */     long curSize = 0L;
/* 116 */     int n = 0, b = 0;
/* 117 */     while (arrays.hasNext()) {
/* 118 */       a[b] = arrays.next();
/* 119 */       int length = (a[b]).length;
/* 120 */       if (n % ratio == 0) {
/* 121 */         p = LongArrays.grow(p, n / ratio + 1);
/* 122 */         p[n / ratio] = curSize;
/* 123 */         array = ShortBigArrays.grow(array, curSize + count(length) + length, curSize);
/* 124 */         curSize += writeInt(array, length, curSize);
/* 125 */         ShortBigArrays.copyToBig(a[b], 0, array, curSize, length);
/* 126 */         curSize += length;
/*     */       } else {
/* 128 */         int minLength = (a[1 - b]).length;
/* 129 */         if (length < minLength)
/* 130 */           minLength = length;  int common;
/* 131 */         for (common = 0; common < minLength && 
/* 132 */           a[0][common] == a[1][common]; common++);
/*     */         
/* 134 */         length -= common;
/* 135 */         array = ShortBigArrays.grow(array, curSize + count(length) + count(common) + length, curSize);
/* 136 */         curSize += writeInt(array, length, curSize);
/* 137 */         curSize += writeInt(array, common, curSize);
/* 138 */         ShortBigArrays.copyToBig(a[b], common, array, curSize, length);
/* 139 */         curSize += length;
/*     */       } 
/* 141 */       b = 1 - b;
/* 142 */       n++;
/*     */     } 
/* 144 */     this.n = n;
/* 145 */     this.ratio = ratio;
/* 146 */     this.array = ShortBigArrays.trim(array, curSize);
/* 147 */     this.p = LongArrays.trim(p, (n + ratio - 1) / ratio);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArrayFrontCodedList(Collection<short[]> c, int ratio) {
/* 158 */     this((Iterator)c.iterator(), ratio);
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
/*     */   private static int readInt(short[][] a, long pos) {
/* 175 */     short s0 = ShortBigArrays.get(a, pos);
/* 176 */     return (s0 >= 0) ? s0 : (s0 << 16 | ShortBigArrays.get(a, pos + 1L) & 0xFFFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int count(int length) {
/* 186 */     return (length < 32768) ? 1 : 2;
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
/*     */   private static int writeInt(short[][] a, int length, long pos) {
/* 200 */     if (length < 32768) {
/* 201 */       ShortBigArrays.set(a, pos, (short)length);
/* 202 */       return 1;
/*     */     } 
/* 204 */     ShortBigArrays.set(a, pos++, (short)(-(length >>> 16) - 1));
/* 205 */     ShortBigArrays.set(a, pos, (short)(length & 0xFFFF));
/* 206 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int ratio() {
/* 214 */     return this.ratio;
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
/*     */   private int length(int index) {
/* 228 */     short[][] array = this.array;
/* 229 */     int delta = index % this.ratio;
/* 230 */     long pos = this.p[index / this.ratio];
/* 231 */     int length = readInt(array, pos);
/* 232 */     if (delta == 0) {
/* 233 */       return length;
/*     */     }
/*     */ 
/*     */     
/* 237 */     pos += (count(length) + length);
/* 238 */     length = readInt(array, pos);
/* 239 */     int common = readInt(array, pos + count(length));
/* 240 */     for (int i = 0; i < delta - 1; i++) {
/* 241 */       pos += (count(length) + count(common) + length);
/* 242 */       length = readInt(array, pos);
/* 243 */       common = readInt(array, pos + count(length));
/*     */     } 
/* 245 */     return length + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int arrayLength(int index) {
/* 255 */     ensureRestrictedIndex(index);
/* 256 */     return length(index);
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
/*     */   private int extract(int index, short[] a, int offset, int length) {
/* 273 */     int delta = index % this.ratio;
/* 274 */     long startPos = this.p[index / this.ratio];
/*     */     
/*     */     long pos;
/* 277 */     int arrayLength = readInt(this.array, pos = startPos), currLen = 0;
/* 278 */     if (delta == 0) {
/* 279 */       pos = this.p[index / this.ratio] + count(arrayLength);
/* 280 */       ShortBigArrays.copyFromBig(this.array, pos, a, offset, Math.min(length, arrayLength));
/* 281 */       return arrayLength;
/*     */     } 
/* 283 */     int common = 0;
/* 284 */     for (int i = 0; i < delta; i++) {
/* 285 */       long prevArrayPos = pos + count(arrayLength) + ((i != 0) ? count(common) : 0L);
/* 286 */       pos = prevArrayPos + arrayLength;
/* 287 */       arrayLength = readInt(this.array, pos);
/* 288 */       common = readInt(this.array, pos + count(arrayLength));
/* 289 */       int actualCommon = Math.min(common, length);
/* 290 */       if (actualCommon <= currLen) {
/* 291 */         currLen = actualCommon;
/*     */       } else {
/* 293 */         ShortBigArrays.copyFromBig(this.array, prevArrayPos, a, currLen + offset, actualCommon - currLen);
/* 294 */         currLen = actualCommon;
/*     */       } 
/*     */     } 
/* 297 */     if (currLen < length)
/* 298 */       ShortBigArrays.copyFromBig(this.array, pos + count(arrayLength) + count(common), a, currLen + offset, 
/* 299 */           Math.min(arrayLength, length - currLen)); 
/* 300 */     return arrayLength + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] get(int index) {
/* 309 */     return getArray(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getArray(int index) {
/* 319 */     ensureRestrictedIndex(index);
/* 320 */     int length = length(index);
/* 321 */     short[] a = new short[length];
/* 322 */     extract(index, a, 0, length);
/* 323 */     return a;
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
/*     */   public int get(int index, short[] a, int offset, int length) {
/* 342 */     ensureRestrictedIndex(index);
/* 343 */     ShortArrays.ensureOffsetLength(a, offset, length);
/* 344 */     int arrayLength = extract(index, a, offset, length);
/* 345 */     if (length >= arrayLength)
/* 346 */       return arrayLength; 
/* 347 */     return length - arrayLength;
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
/*     */   public int get(int index, short[] a) {
/* 362 */     return get(index, a, 0, a.length);
/*     */   }
/*     */   
/*     */   public int size() {
/* 366 */     return this.n;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<short[]> listIterator(final int start) {
/* 370 */     ensureIndex(start);
/* 371 */     return new ObjectListIterator<short[]>() {
/* 372 */         short[] s = ShortArrays.EMPTY_ARRAY;
/* 373 */         int i = 0;
/* 374 */         long pos = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         boolean inSync;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 392 */           return (this.i < ShortArrayFrontCodedList.this.n);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 396 */           return (this.i > 0);
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 400 */           return this.i - 1;
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 404 */           return this.i;
/*     */         }
/*     */         
/*     */         public short[] next() {
/*     */           int length;
/* 409 */           if (!hasNext())
/* 410 */             throw new NoSuchElementException(); 
/* 411 */           if (this.i % ShortArrayFrontCodedList.this.ratio == 0) {
/* 412 */             this.pos = ShortArrayFrontCodedList.this.p[this.i / ShortArrayFrontCodedList.this.ratio];
/* 413 */             length = ShortArrayFrontCodedList.readInt(ShortArrayFrontCodedList.this.array, this.pos);
/* 414 */             this.s = ShortArrays.ensureCapacity(this.s, length, 0);
/* 415 */             ShortBigArrays.copyFromBig(ShortArrayFrontCodedList.this.array, this.pos + ShortArrayFrontCodedList.count(length), this.s, 0, length);
/* 416 */             this.pos += (length + ShortArrayFrontCodedList.count(length));
/* 417 */             this.inSync = true;
/*     */           }
/* 419 */           else if (this.inSync) {
/* 420 */             length = ShortArrayFrontCodedList.readInt(ShortArrayFrontCodedList.this.array, this.pos);
/* 421 */             int common = ShortArrayFrontCodedList.readInt(ShortArrayFrontCodedList.this.array, this.pos + ShortArrayFrontCodedList.count(length));
/* 422 */             this.s = ShortArrays.ensureCapacity(this.s, length + common, common);
/* 423 */             ShortBigArrays.copyFromBig(ShortArrayFrontCodedList.this.array, this.pos + ShortArrayFrontCodedList.count(length) + ShortArrayFrontCodedList.count(common), this.s, common, length);
/* 424 */             this.pos += (ShortArrayFrontCodedList.count(length) + ShortArrayFrontCodedList.count(common) + length);
/* 425 */             length += common;
/*     */           } else {
/* 427 */             this.s = ShortArrays.ensureCapacity(this.s, length = ShortArrayFrontCodedList.this.length(this.i), 0);
/* 428 */             ShortArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
/*     */           } 
/*     */           
/* 431 */           this.i++;
/* 432 */           return ShortArrays.copy(this.s, 0, length);
/*     */         }
/*     */         
/*     */         public short[] previous() {
/* 436 */           if (!hasPrevious())
/* 437 */             throw new NoSuchElementException(); 
/* 438 */           this.inSync = false;
/* 439 */           return ShortArrayFrontCodedList.this.getArray(--this.i);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArrayFrontCodedList clone() {
/* 450 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 454 */     StringBuffer s = new StringBuffer();
/* 455 */     s.append("[");
/* 456 */     for (int i = 0; i < this.n; i++) {
/* 457 */       if (i != 0)
/* 458 */         s.append(", "); 
/* 459 */       s.append(ShortArrayList.wrap(getArray(i)).toString());
/*     */     } 
/* 461 */     s.append("]");
/* 462 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long[] rebuildPointerArray() {
/* 471 */     long[] p = new long[(this.n + this.ratio - 1) / this.ratio];
/* 472 */     short[][] a = this.array;
/*     */     
/* 474 */     long pos = 0L;
/* 475 */     for (int i = 0, j = 0, skip = this.ratio - 1; i < this.n; i++) {
/* 476 */       int length = readInt(a, pos);
/* 477 */       int count = count(length);
/* 478 */       if (++skip == this.ratio) {
/* 479 */         skip = 0;
/* 480 */         p[j++] = pos;
/* 481 */         pos += (count + length);
/*     */       } else {
/* 483 */         pos += (count + count(readInt(a, pos + count)) + length);
/*     */       } 
/* 485 */     }  return p;
/*     */   }
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 488 */     s.defaultReadObject();
/*     */     
/* 490 */     this.p = rebuildPointerArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortArrayFrontCodedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */