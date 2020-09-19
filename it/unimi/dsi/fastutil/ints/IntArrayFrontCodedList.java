/*     */ package it.unimi.dsi.fastutil.ints;
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
/*     */ public class IntArrayFrontCodedList
/*     */   extends AbstractObjectList<int[]>
/*     */   implements Serializable, Cloneable, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int n;
/*     */   protected final int ratio;
/*     */   protected final int[][] array;
/*     */   protected transient long[] p;
/*     */   
/*     */   public IntArrayFrontCodedList(Iterator<int[]> arrays, int ratio) {
/* 106 */     if (ratio < 1)
/* 107 */       throw new IllegalArgumentException("Illegal ratio (" + ratio + ")"); 
/* 108 */     int[][] array = IntBigArrays.EMPTY_BIG_ARRAY;
/* 109 */     long[] p = LongArrays.EMPTY_ARRAY;
/* 110 */     int[][] a = new int[2][];
/* 111 */     long curSize = 0L;
/* 112 */     int n = 0, b = 0;
/* 113 */     while (arrays.hasNext()) {
/* 114 */       a[b] = arrays.next();
/* 115 */       int length = (a[b]).length;
/* 116 */       if (n % ratio == 0) {
/* 117 */         p = LongArrays.grow(p, n / ratio + 1);
/* 118 */         p[n / ratio] = curSize;
/* 119 */         array = IntBigArrays.grow(array, curSize + count(length) + length, curSize);
/* 120 */         curSize += writeInt(array, length, curSize);
/* 121 */         IntBigArrays.copyToBig(a[b], 0, array, curSize, length);
/* 122 */         curSize += length;
/*     */       } else {
/* 124 */         int minLength = (a[1 - b]).length;
/* 125 */         if (length < minLength)
/* 126 */           minLength = length;  int common;
/* 127 */         for (common = 0; common < minLength && 
/* 128 */           a[0][common] == a[1][common]; common++);
/*     */         
/* 130 */         length -= common;
/* 131 */         array = IntBigArrays.grow(array, curSize + count(length) + count(common) + length, curSize);
/* 132 */         curSize += writeInt(array, length, curSize);
/* 133 */         curSize += writeInt(array, common, curSize);
/* 134 */         IntBigArrays.copyToBig(a[b], common, array, curSize, length);
/* 135 */         curSize += length;
/*     */       } 
/* 137 */       b = 1 - b;
/* 138 */       n++;
/*     */     } 
/* 140 */     this.n = n;
/* 141 */     this.ratio = ratio;
/* 142 */     this.array = IntBigArrays.trim(array, curSize);
/* 143 */     this.p = LongArrays.trim(p, (n + ratio - 1) / ratio);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntArrayFrontCodedList(Collection<int[]> c, int ratio) {
/* 154 */     this((Iterator)c.iterator(), ratio);
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
/*     */   private static int readInt(int[][] a, long pos) {
/* 171 */     return IntBigArrays.get(a, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int count(int length) {
/* 181 */     return 1;
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
/*     */   private static int writeInt(int[][] a, int length, long pos) {
/* 195 */     IntBigArrays.set(a, pos, length);
/* 196 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int ratio() {
/* 204 */     return this.ratio;
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
/* 218 */     int[][] array = this.array;
/* 219 */     int delta = index % this.ratio;
/* 220 */     long pos = this.p[index / this.ratio];
/* 221 */     int length = readInt(array, pos);
/* 222 */     if (delta == 0) {
/* 223 */       return length;
/*     */     }
/*     */ 
/*     */     
/* 227 */     pos += (count(length) + length);
/* 228 */     length = readInt(array, pos);
/* 229 */     int common = readInt(array, pos + count(length));
/* 230 */     for (int i = 0; i < delta - 1; i++) {
/* 231 */       pos += (count(length) + count(common) + length);
/* 232 */       length = readInt(array, pos);
/* 233 */       common = readInt(array, pos + count(length));
/*     */     } 
/* 235 */     return length + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int arrayLength(int index) {
/* 245 */     ensureRestrictedIndex(index);
/* 246 */     return length(index);
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
/*     */   private int extract(int index, int[] a, int offset, int length) {
/* 263 */     int delta = index % this.ratio;
/* 264 */     long startPos = this.p[index / this.ratio];
/*     */     
/*     */     long pos;
/* 267 */     int arrayLength = readInt(this.array, pos = startPos), currLen = 0;
/* 268 */     if (delta == 0) {
/* 269 */       pos = this.p[index / this.ratio] + count(arrayLength);
/* 270 */       IntBigArrays.copyFromBig(this.array, pos, a, offset, Math.min(length, arrayLength));
/* 271 */       return arrayLength;
/*     */     } 
/* 273 */     int common = 0;
/* 274 */     for (int i = 0; i < delta; i++) {
/* 275 */       long prevArrayPos = pos + count(arrayLength) + ((i != 0) ? count(common) : 0L);
/* 276 */       pos = prevArrayPos + arrayLength;
/* 277 */       arrayLength = readInt(this.array, pos);
/* 278 */       common = readInt(this.array, pos + count(arrayLength));
/* 279 */       int actualCommon = Math.min(common, length);
/* 280 */       if (actualCommon <= currLen) {
/* 281 */         currLen = actualCommon;
/*     */       } else {
/* 283 */         IntBigArrays.copyFromBig(this.array, prevArrayPos, a, currLen + offset, actualCommon - currLen);
/* 284 */         currLen = actualCommon;
/*     */       } 
/*     */     } 
/* 287 */     if (currLen < length)
/* 288 */       IntBigArrays.copyFromBig(this.array, pos + count(arrayLength) + count(common), a, currLen + offset, 
/* 289 */           Math.min(arrayLength, length - currLen)); 
/* 290 */     return arrayLength + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] get(int index) {
/* 299 */     return getArray(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getArray(int index) {
/* 309 */     ensureRestrictedIndex(index);
/* 310 */     int length = length(index);
/* 311 */     int[] a = new int[length];
/* 312 */     extract(index, a, 0, length);
/* 313 */     return a;
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
/*     */   public int get(int index, int[] a, int offset, int length) {
/* 332 */     ensureRestrictedIndex(index);
/* 333 */     IntArrays.ensureOffsetLength(a, offset, length);
/* 334 */     int arrayLength = extract(index, a, offset, length);
/* 335 */     if (length >= arrayLength)
/* 336 */       return arrayLength; 
/* 337 */     return length - arrayLength;
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
/*     */   public int get(int index, int[] a) {
/* 352 */     return get(index, a, 0, a.length);
/*     */   }
/*     */   
/*     */   public int size() {
/* 356 */     return this.n;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<int[]> listIterator(final int start) {
/* 360 */     ensureIndex(start);
/* 361 */     return new ObjectListIterator<int[]>() {
/* 362 */         int[] s = IntArrays.EMPTY_ARRAY;
/* 363 */         int i = 0;
/* 364 */         long pos = 0L;
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
/* 382 */           return (this.i < IntArrayFrontCodedList.this.n);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 386 */           return (this.i > 0);
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 390 */           return this.i - 1;
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 394 */           return this.i;
/*     */         }
/*     */         
/*     */         public int[] next() {
/*     */           int length;
/* 399 */           if (!hasNext())
/* 400 */             throw new NoSuchElementException(); 
/* 401 */           if (this.i % IntArrayFrontCodedList.this.ratio == 0) {
/* 402 */             this.pos = IntArrayFrontCodedList.this.p[this.i / IntArrayFrontCodedList.this.ratio];
/* 403 */             length = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos);
/* 404 */             this.s = IntArrays.ensureCapacity(this.s, length, 0);
/* 405 */             IntBigArrays.copyFromBig(IntArrayFrontCodedList.this.array, this.pos + IntArrayFrontCodedList.count(length), this.s, 0, length);
/* 406 */             this.pos += (length + IntArrayFrontCodedList.count(length));
/* 407 */             this.inSync = true;
/*     */           }
/* 409 */           else if (this.inSync) {
/* 410 */             length = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos);
/* 411 */             int common = IntArrayFrontCodedList.readInt(IntArrayFrontCodedList.this.array, this.pos + IntArrayFrontCodedList.count(length));
/* 412 */             this.s = IntArrays.ensureCapacity(this.s, length + common, common);
/* 413 */             IntBigArrays.copyFromBig(IntArrayFrontCodedList.this.array, this.pos + IntArrayFrontCodedList.count(length) + IntArrayFrontCodedList.count(common), this.s, common, length);
/* 414 */             this.pos += (IntArrayFrontCodedList.count(length) + IntArrayFrontCodedList.count(common) + length);
/* 415 */             length += common;
/*     */           } else {
/* 417 */             this.s = IntArrays.ensureCapacity(this.s, length = IntArrayFrontCodedList.this.length(this.i), 0);
/* 418 */             IntArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
/*     */           } 
/*     */           
/* 421 */           this.i++;
/* 422 */           return IntArrays.copy(this.s, 0, length);
/*     */         }
/*     */         
/*     */         public int[] previous() {
/* 426 */           if (!hasPrevious())
/* 427 */             throw new NoSuchElementException(); 
/* 428 */           this.inSync = false;
/* 429 */           return IntArrayFrontCodedList.this.getArray(--this.i);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntArrayFrontCodedList clone() {
/* 440 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 444 */     StringBuffer s = new StringBuffer();
/* 445 */     s.append("[");
/* 446 */     for (int i = 0; i < this.n; i++) {
/* 447 */       if (i != 0)
/* 448 */         s.append(", "); 
/* 449 */       s.append(IntArrayList.wrap(getArray(i)).toString());
/*     */     } 
/* 451 */     s.append("]");
/* 452 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long[] rebuildPointerArray() {
/* 461 */     long[] p = new long[(this.n + this.ratio - 1) / this.ratio];
/* 462 */     int[][] a = this.array;
/*     */     
/* 464 */     long pos = 0L;
/* 465 */     for (int i = 0, j = 0, skip = this.ratio - 1; i < this.n; i++) {
/* 466 */       int length = readInt(a, pos);
/* 467 */       int count = count(length);
/* 468 */       if (++skip == this.ratio) {
/* 469 */         skip = 0;
/* 470 */         p[j++] = pos;
/* 471 */         pos += (count + length);
/*     */       } else {
/* 473 */         pos += (count + count(readInt(a, pos + count)) + length);
/*     */       } 
/* 475 */     }  return p;
/*     */   }
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 478 */     s.defaultReadObject();
/*     */     
/* 480 */     this.p = rebuildPointerArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntArrayFrontCodedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */