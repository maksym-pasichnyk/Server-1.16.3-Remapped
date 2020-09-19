/*     */ package it.unimi.dsi.fastutil.longs;
/*     */ 
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
/*     */ 
/*     */ public class LongArrayFrontCodedList
/*     */   extends AbstractObjectList<long[]>
/*     */   implements Serializable, Cloneable, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int n;
/*     */   protected final int ratio;
/*     */   protected final long[][] array;
/*     */   protected transient long[] p;
/*     */   
/*     */   public LongArrayFrontCodedList(Iterator<long[]> arrays, int ratio) {
/* 110 */     if (ratio < 1)
/* 111 */       throw new IllegalArgumentException("Illegal ratio (" + ratio + ")"); 
/* 112 */     long[][] array = LongBigArrays.EMPTY_BIG_ARRAY;
/* 113 */     long[] p = LongArrays.EMPTY_ARRAY;
/* 114 */     long[][] a = new long[2][];
/* 115 */     long curSize = 0L;
/* 116 */     int n = 0, b = 0;
/* 117 */     while (arrays.hasNext()) {
/* 118 */       a[b] = arrays.next();
/* 119 */       int length = (a[b]).length;
/* 120 */       if (n % ratio == 0) {
/* 121 */         p = LongArrays.grow(p, n / ratio + 1);
/* 122 */         p[n / ratio] = curSize;
/* 123 */         array = LongBigArrays.grow(array, curSize + count(length) + length, curSize);
/* 124 */         curSize += writeInt(array, length, curSize);
/* 125 */         LongBigArrays.copyToBig(a[b], 0, array, curSize, length);
/* 126 */         curSize += length;
/*     */       } else {
/* 128 */         int minLength = (a[1 - b]).length;
/* 129 */         if (length < minLength)
/* 130 */           minLength = length;  int common;
/* 131 */         for (common = 0; common < minLength && 
/* 132 */           a[0][common] == a[1][common]; common++);
/*     */         
/* 134 */         length -= common;
/* 135 */         array = LongBigArrays.grow(array, curSize + count(length) + count(common) + length, curSize);
/* 136 */         curSize += writeInt(array, length, curSize);
/* 137 */         curSize += writeInt(array, common, curSize);
/* 138 */         LongBigArrays.copyToBig(a[b], common, array, curSize, length);
/* 139 */         curSize += length;
/*     */       } 
/* 141 */       b = 1 - b;
/* 142 */       n++;
/*     */     } 
/* 144 */     this.n = n;
/* 145 */     this.ratio = ratio;
/* 146 */     this.array = LongBigArrays.trim(array, curSize);
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
/*     */   public LongArrayFrontCodedList(Collection<long[]> c, int ratio) {
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
/*     */   private static int readInt(long[][] a, long pos) {
/* 175 */     return (int)LongBigArrays.get(a, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int count(int length) {
/* 185 */     return 1;
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
/*     */   private static int writeInt(long[][] a, int length, long pos) {
/* 199 */     LongBigArrays.set(a, pos, length);
/* 200 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int ratio() {
/* 208 */     return this.ratio;
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
/* 222 */     long[][] array = this.array;
/* 223 */     int delta = index % this.ratio;
/* 224 */     long pos = this.p[index / this.ratio];
/* 225 */     int length = readInt(array, pos);
/* 226 */     if (delta == 0) {
/* 227 */       return length;
/*     */     }
/*     */ 
/*     */     
/* 231 */     pos += (count(length) + length);
/* 232 */     length = readInt(array, pos);
/* 233 */     int common = readInt(array, pos + count(length));
/* 234 */     for (int i = 0; i < delta - 1; i++) {
/* 235 */       pos += (count(length) + count(common) + length);
/* 236 */       length = readInt(array, pos);
/* 237 */       common = readInt(array, pos + count(length));
/*     */     } 
/* 239 */     return length + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int arrayLength(int index) {
/* 249 */     ensureRestrictedIndex(index);
/* 250 */     return length(index);
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
/*     */   private int extract(int index, long[] a, int offset, int length) {
/* 267 */     int delta = index % this.ratio;
/* 268 */     long startPos = this.p[index / this.ratio];
/*     */     
/*     */     long pos;
/* 271 */     int arrayLength = readInt(this.array, pos = startPos), currLen = 0;
/* 272 */     if (delta == 0) {
/* 273 */       pos = this.p[index / this.ratio] + count(arrayLength);
/* 274 */       LongBigArrays.copyFromBig(this.array, pos, a, offset, Math.min(length, arrayLength));
/* 275 */       return arrayLength;
/*     */     } 
/* 277 */     int common = 0;
/* 278 */     for (int i = 0; i < delta; i++) {
/* 279 */       long prevArrayPos = pos + count(arrayLength) + ((i != 0) ? count(common) : 0L);
/* 280 */       pos = prevArrayPos + arrayLength;
/* 281 */       arrayLength = readInt(this.array, pos);
/* 282 */       common = readInt(this.array, pos + count(arrayLength));
/* 283 */       int actualCommon = Math.min(common, length);
/* 284 */       if (actualCommon <= currLen) {
/* 285 */         currLen = actualCommon;
/*     */       } else {
/* 287 */         LongBigArrays.copyFromBig(this.array, prevArrayPos, a, currLen + offset, actualCommon - currLen);
/* 288 */         currLen = actualCommon;
/*     */       } 
/*     */     } 
/* 291 */     if (currLen < length)
/* 292 */       LongBigArrays.copyFromBig(this.array, pos + count(arrayLength) + count(common), a, currLen + offset, 
/* 293 */           Math.min(arrayLength, length - currLen)); 
/* 294 */     return arrayLength + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] get(int index) {
/* 303 */     return getArray(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] getArray(int index) {
/* 313 */     ensureRestrictedIndex(index);
/* 314 */     int length = length(index);
/* 315 */     long[] a = new long[length];
/* 316 */     extract(index, a, 0, length);
/* 317 */     return a;
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
/*     */   public int get(int index, long[] a, int offset, int length) {
/* 336 */     ensureRestrictedIndex(index);
/* 337 */     LongArrays.ensureOffsetLength(a, offset, length);
/* 338 */     int arrayLength = extract(index, a, offset, length);
/* 339 */     if (length >= arrayLength)
/* 340 */       return arrayLength; 
/* 341 */     return length - arrayLength;
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
/*     */   public int get(int index, long[] a) {
/* 356 */     return get(index, a, 0, a.length);
/*     */   }
/*     */   
/*     */   public int size() {
/* 360 */     return this.n;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<long[]> listIterator(final int start) {
/* 364 */     ensureIndex(start);
/* 365 */     return new ObjectListIterator<long[]>() {
/* 366 */         long[] s = LongArrays.EMPTY_ARRAY;
/* 367 */         int i = 0;
/* 368 */         long pos = 0L;
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
/* 386 */           return (this.i < LongArrayFrontCodedList.this.n);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 390 */           return (this.i > 0);
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 394 */           return this.i - 1;
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 398 */           return this.i;
/*     */         }
/*     */         
/*     */         public long[] next() {
/*     */           int length;
/* 403 */           if (!hasNext())
/* 404 */             throw new NoSuchElementException(); 
/* 405 */           if (this.i % LongArrayFrontCodedList.this.ratio == 0) {
/* 406 */             this.pos = LongArrayFrontCodedList.this.p[this.i / LongArrayFrontCodedList.this.ratio];
/* 407 */             length = LongArrayFrontCodedList.readInt(LongArrayFrontCodedList.this.array, this.pos);
/* 408 */             this.s = LongArrays.ensureCapacity(this.s, length, 0);
/* 409 */             LongBigArrays.copyFromBig(LongArrayFrontCodedList.this.array, this.pos + LongArrayFrontCodedList.count(length), this.s, 0, length);
/* 410 */             this.pos += (length + LongArrayFrontCodedList.count(length));
/* 411 */             this.inSync = true;
/*     */           }
/* 413 */           else if (this.inSync) {
/* 414 */             length = LongArrayFrontCodedList.readInt(LongArrayFrontCodedList.this.array, this.pos);
/* 415 */             int common = LongArrayFrontCodedList.readInt(LongArrayFrontCodedList.this.array, this.pos + LongArrayFrontCodedList.count(length));
/* 416 */             this.s = LongArrays.ensureCapacity(this.s, length + common, common);
/* 417 */             LongBigArrays.copyFromBig(LongArrayFrontCodedList.this.array, this.pos + LongArrayFrontCodedList.count(length) + LongArrayFrontCodedList.count(common), this.s, common, length);
/* 418 */             this.pos += (LongArrayFrontCodedList.count(length) + LongArrayFrontCodedList.count(common) + length);
/* 419 */             length += common;
/*     */           } else {
/* 421 */             this.s = LongArrays.ensureCapacity(this.s, length = LongArrayFrontCodedList.this.length(this.i), 0);
/* 422 */             LongArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
/*     */           } 
/*     */           
/* 425 */           this.i++;
/* 426 */           return LongArrays.copy(this.s, 0, length);
/*     */         }
/*     */         
/*     */         public long[] previous() {
/* 430 */           if (!hasPrevious())
/* 431 */             throw new NoSuchElementException(); 
/* 432 */           this.inSync = false;
/* 433 */           return LongArrayFrontCodedList.this.getArray(--this.i);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongArrayFrontCodedList clone() {
/* 444 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 448 */     StringBuffer s = new StringBuffer();
/* 449 */     s.append("[");
/* 450 */     for (int i = 0; i < this.n; i++) {
/* 451 */       if (i != 0)
/* 452 */         s.append(", "); 
/* 453 */       s.append(LongArrayList.wrap(getArray(i)).toString());
/*     */     } 
/* 455 */     s.append("]");
/* 456 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long[] rebuildPointerArray() {
/* 465 */     long[] p = new long[(this.n + this.ratio - 1) / this.ratio];
/* 466 */     long[][] a = this.array;
/*     */     
/* 468 */     long pos = 0L;
/* 469 */     for (int i = 0, j = 0, skip = this.ratio - 1; i < this.n; i++) {
/* 470 */       int length = readInt(a, pos);
/* 471 */       int count = count(length);
/* 472 */       if (++skip == this.ratio) {
/* 473 */         skip = 0;
/* 474 */         p[j++] = pos;
/* 475 */         pos += (count + length);
/*     */       } else {
/* 477 */         pos += (count + count(readInt(a, pos + count)) + length);
/*     */       } 
/* 479 */     }  return p;
/*     */   }
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 482 */     s.defaultReadObject();
/*     */     
/* 484 */     this.p = rebuildPointerArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\longs\LongArrayFrontCodedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */