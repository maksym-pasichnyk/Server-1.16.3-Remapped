/*     */ package it.unimi.dsi.fastutil.bytes;
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
/*     */ public class ByteArrayFrontCodedList
/*     */   extends AbstractObjectList<byte[]>
/*     */   implements Serializable, Cloneable, RandomAccess
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final int n;
/*     */   protected final int ratio;
/*     */   protected final byte[][] array;
/*     */   protected transient long[] p;
/*     */   
/*     */   public ByteArrayFrontCodedList(Iterator<byte[]> arrays, int ratio) {
/* 110 */     if (ratio < 1)
/* 111 */       throw new IllegalArgumentException("Illegal ratio (" + ratio + ")"); 
/* 112 */     byte[][] array = ByteBigArrays.EMPTY_BIG_ARRAY;
/* 113 */     long[] p = LongArrays.EMPTY_ARRAY;
/* 114 */     byte[][] a = new byte[2][];
/* 115 */     long curSize = 0L;
/* 116 */     int n = 0, b = 0;
/* 117 */     while (arrays.hasNext()) {
/* 118 */       a[b] = arrays.next();
/* 119 */       int length = (a[b]).length;
/* 120 */       if (n % ratio == 0) {
/* 121 */         p = LongArrays.grow(p, n / ratio + 1);
/* 122 */         p[n / ratio] = curSize;
/* 123 */         array = ByteBigArrays.grow(array, curSize + count(length) + length, curSize);
/* 124 */         curSize += writeInt(array, length, curSize);
/* 125 */         ByteBigArrays.copyToBig(a[b], 0, array, curSize, length);
/* 126 */         curSize += length;
/*     */       } else {
/* 128 */         int minLength = (a[1 - b]).length;
/* 129 */         if (length < minLength)
/* 130 */           minLength = length;  int common;
/* 131 */         for (common = 0; common < minLength && 
/* 132 */           a[0][common] == a[1][common]; common++);
/*     */         
/* 134 */         length -= common;
/* 135 */         array = ByteBigArrays.grow(array, curSize + count(length) + count(common) + length, curSize);
/* 136 */         curSize += writeInt(array, length, curSize);
/* 137 */         curSize += writeInt(array, common, curSize);
/* 138 */         ByteBigArrays.copyToBig(a[b], common, array, curSize, length);
/* 139 */         curSize += length;
/*     */       } 
/* 141 */       b = 1 - b;
/* 142 */       n++;
/*     */     } 
/* 144 */     this.n = n;
/* 145 */     this.ratio = ratio;
/* 146 */     this.array = ByteBigArrays.trim(array, curSize);
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
/*     */   public ByteArrayFrontCodedList(Collection<byte[]> c, int ratio) {
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
/*     */   private static int readInt(byte[][] a, long pos) {
/* 175 */     byte b0 = ByteBigArrays.get(a, pos);
/* 176 */     if (b0 >= 0)
/* 177 */       return b0; 
/* 178 */     byte b1 = ByteBigArrays.get(a, pos + 1L);
/* 179 */     if (b1 >= 0)
/* 180 */       return -b0 - 1 << 7 | b1; 
/* 181 */     byte b2 = ByteBigArrays.get(a, pos + 2L);
/* 182 */     if (b2 >= 0)
/* 183 */       return -b0 - 1 << 14 | -b1 - 1 << 7 | b2; 
/* 184 */     byte b3 = ByteBigArrays.get(a, pos + 3L);
/* 185 */     if (b3 >= 0)
/* 186 */       return -b0 - 1 << 21 | -b1 - 1 << 14 | -b2 - 1 << 7 | b3; 
/* 187 */     return -b0 - 1 << 28 | -b1 - 1 << 21 | -b2 - 1 << 14 | -b3 - 1 << 7 | ByteBigArrays.get(a, pos + 4L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int count(int length) {
/* 197 */     if (length < 128)
/* 198 */       return 1; 
/* 199 */     if (length < 16384)
/* 200 */       return 2; 
/* 201 */     if (length < 2097152)
/* 202 */       return 3; 
/* 203 */     if (length < 268435456)
/* 204 */       return 4; 
/* 205 */     return 5;
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
/*     */   private static int writeInt(byte[][] a, int length, long pos) {
/* 219 */     int count = count(length);
/* 220 */     ByteBigArrays.set(a, pos + count - 1L, (byte)(length & 0x7F));
/* 221 */     if (count != 1) {
/* 222 */       int i = count - 1;
/* 223 */       while (i-- != 0) {
/* 224 */         length >>>= 7;
/* 225 */         ByteBigArrays.set(a, pos + i, (byte)(-(length & 0x7F) - 1));
/*     */       } 
/*     */     } 
/* 228 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int ratio() {
/* 236 */     return this.ratio;
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
/* 250 */     byte[][] array = this.array;
/* 251 */     int delta = index % this.ratio;
/* 252 */     long pos = this.p[index / this.ratio];
/* 253 */     int length = readInt(array, pos);
/* 254 */     if (delta == 0) {
/* 255 */       return length;
/*     */     }
/*     */ 
/*     */     
/* 259 */     pos += (count(length) + length);
/* 260 */     length = readInt(array, pos);
/* 261 */     int common = readInt(array, pos + count(length));
/* 262 */     for (int i = 0; i < delta - 1; i++) {
/* 263 */       pos += (count(length) + count(common) + length);
/* 264 */       length = readInt(array, pos);
/* 265 */       common = readInt(array, pos + count(length));
/*     */     } 
/* 267 */     return length + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int arrayLength(int index) {
/* 277 */     ensureRestrictedIndex(index);
/* 278 */     return length(index);
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
/*     */   private int extract(int index, byte[] a, int offset, int length) {
/* 295 */     int delta = index % this.ratio;
/* 296 */     long startPos = this.p[index / this.ratio];
/*     */     
/*     */     long pos;
/* 299 */     int arrayLength = readInt(this.array, pos = startPos), currLen = 0;
/* 300 */     if (delta == 0) {
/* 301 */       pos = this.p[index / this.ratio] + count(arrayLength);
/* 302 */       ByteBigArrays.copyFromBig(this.array, pos, a, offset, Math.min(length, arrayLength));
/* 303 */       return arrayLength;
/*     */     } 
/* 305 */     int common = 0;
/* 306 */     for (int i = 0; i < delta; i++) {
/* 307 */       long prevArrayPos = pos + count(arrayLength) + ((i != 0) ? count(common) : 0L);
/* 308 */       pos = prevArrayPos + arrayLength;
/* 309 */       arrayLength = readInt(this.array, pos);
/* 310 */       common = readInt(this.array, pos + count(arrayLength));
/* 311 */       int actualCommon = Math.min(common, length);
/* 312 */       if (actualCommon <= currLen) {
/* 313 */         currLen = actualCommon;
/*     */       } else {
/* 315 */         ByteBigArrays.copyFromBig(this.array, prevArrayPos, a, currLen + offset, actualCommon - currLen);
/* 316 */         currLen = actualCommon;
/*     */       } 
/*     */     } 
/* 319 */     if (currLen < length)
/* 320 */       ByteBigArrays.copyFromBig(this.array, pos + count(arrayLength) + count(common), a, currLen + offset, 
/* 321 */           Math.min(arrayLength, length - currLen)); 
/* 322 */     return arrayLength + common;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get(int index) {
/* 331 */     return getArray(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getArray(int index) {
/* 341 */     ensureRestrictedIndex(index);
/* 342 */     int length = length(index);
/* 343 */     byte[] a = new byte[length];
/* 344 */     extract(index, a, 0, length);
/* 345 */     return a;
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
/*     */   public int get(int index, byte[] a, int offset, int length) {
/* 364 */     ensureRestrictedIndex(index);
/* 365 */     ByteArrays.ensureOffsetLength(a, offset, length);
/* 366 */     int arrayLength = extract(index, a, offset, length);
/* 367 */     if (length >= arrayLength)
/* 368 */       return arrayLength; 
/* 369 */     return length - arrayLength;
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
/*     */   public int get(int index, byte[] a) {
/* 384 */     return get(index, a, 0, a.length);
/*     */   }
/*     */   
/*     */   public int size() {
/* 388 */     return this.n;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<byte[]> listIterator(final int start) {
/* 392 */     ensureIndex(start);
/* 393 */     return new ObjectListIterator<byte[]>() {
/* 394 */         byte[] s = ByteArrays.EMPTY_ARRAY;
/* 395 */         int i = 0;
/* 396 */         long pos = 0L;
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
/* 414 */           return (this.i < ByteArrayFrontCodedList.this.n);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 418 */           return (this.i > 0);
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 422 */           return this.i - 1;
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 426 */           return this.i;
/*     */         }
/*     */         
/*     */         public byte[] next() {
/*     */           int length;
/* 431 */           if (!hasNext())
/* 432 */             throw new NoSuchElementException(); 
/* 433 */           if (this.i % ByteArrayFrontCodedList.this.ratio == 0) {
/* 434 */             this.pos = ByteArrayFrontCodedList.this.p[this.i / ByteArrayFrontCodedList.this.ratio];
/* 435 */             length = ByteArrayFrontCodedList.readInt(ByteArrayFrontCodedList.this.array, this.pos);
/* 436 */             this.s = ByteArrays.ensureCapacity(this.s, length, 0);
/* 437 */             ByteBigArrays.copyFromBig(ByteArrayFrontCodedList.this.array, this.pos + ByteArrayFrontCodedList.count(length), this.s, 0, length);
/* 438 */             this.pos += (length + ByteArrayFrontCodedList.count(length));
/* 439 */             this.inSync = true;
/*     */           }
/* 441 */           else if (this.inSync) {
/* 442 */             length = ByteArrayFrontCodedList.readInt(ByteArrayFrontCodedList.this.array, this.pos);
/* 443 */             int common = ByteArrayFrontCodedList.readInt(ByteArrayFrontCodedList.this.array, this.pos + ByteArrayFrontCodedList.count(length));
/* 444 */             this.s = ByteArrays.ensureCapacity(this.s, length + common, common);
/* 445 */             ByteBigArrays.copyFromBig(ByteArrayFrontCodedList.this.array, this.pos + ByteArrayFrontCodedList.count(length) + ByteArrayFrontCodedList.count(common), this.s, common, length);
/* 446 */             this.pos += (ByteArrayFrontCodedList.count(length) + ByteArrayFrontCodedList.count(common) + length);
/* 447 */             length += common;
/*     */           } else {
/* 449 */             this.s = ByteArrays.ensureCapacity(this.s, length = ByteArrayFrontCodedList.this.length(this.i), 0);
/* 450 */             ByteArrayFrontCodedList.this.extract(this.i, this.s, 0, length);
/*     */           } 
/*     */           
/* 453 */           this.i++;
/* 454 */           return ByteArrays.copy(this.s, 0, length);
/*     */         }
/*     */         
/*     */         public byte[] previous() {
/* 458 */           if (!hasPrevious())
/* 459 */             throw new NoSuchElementException(); 
/* 460 */           this.inSync = false;
/* 461 */           return ByteArrayFrontCodedList.this.getArray(--this.i);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayFrontCodedList clone() {
/* 472 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 476 */     StringBuffer s = new StringBuffer();
/* 477 */     s.append("[");
/* 478 */     for (int i = 0; i < this.n; i++) {
/* 479 */       if (i != 0)
/* 480 */         s.append(", "); 
/* 481 */       s.append(ByteArrayList.wrap(getArray(i)).toString());
/*     */     } 
/* 483 */     s.append("]");
/* 484 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long[] rebuildPointerArray() {
/* 493 */     long[] p = new long[(this.n + this.ratio - 1) / this.ratio];
/* 494 */     byte[][] a = this.array;
/*     */     
/* 496 */     long pos = 0L;
/* 497 */     for (int i = 0, j = 0, skip = this.ratio - 1; i < this.n; i++) {
/* 498 */       int length = readInt(a, pos);
/* 499 */       int count = count(length);
/* 500 */       if (++skip == this.ratio) {
/* 501 */         skip = 0;
/* 502 */         p[j++] = pos;
/* 503 */         pos += (count + length);
/*     */       } else {
/* 505 */         pos += (count + count(readInt(a, pos + count)) + length);
/*     */       } 
/* 507 */     }  return p;
/*     */   }
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 510 */     s.defaultReadObject();
/*     */     
/* 512 */     this.p = rebuildPointerArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\ByteArrayFrontCodedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */