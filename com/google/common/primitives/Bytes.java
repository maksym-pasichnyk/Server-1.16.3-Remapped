/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
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
/*     */ @GwtCompatible
/*     */ public final class Bytes
/*     */ {
/*     */   public static int hashCode(byte value) {
/*  60 */     return value;
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
/*     */   public static boolean contains(byte[] array, byte target) {
/*  72 */     for (byte value : array) {
/*  73 */       if (value == target) {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*  77 */     return false;
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
/*     */   public static int indexOf(byte[] array, byte target) {
/*  89 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(byte[] array, byte target, int start, int end) {
/*  94 */     for (int i = start; i < end; i++) {
/*  95 */       if (array[i] == target) {
/*  96 */         return i;
/*     */       }
/*     */     } 
/*  99 */     return -1;
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
/*     */   public static int indexOf(byte[] array, byte[] target) {
/* 114 */     Preconditions.checkNotNull(array, "array");
/* 115 */     Preconditions.checkNotNull(target, "target");
/* 116 */     if (target.length == 0) {
/* 117 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 121 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 122 */       int j = 0; while (true) { if (j < target.length) {
/* 123 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 127 */         return i; }
/*     */     
/* 129 */     }  return -1;
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
/*     */   public static int lastIndexOf(byte[] array, byte target) {
/* 141 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(byte[] array, byte target, int start, int end) {
/* 146 */     for (int i = end - 1; i >= start; i--) {
/* 147 */       if (array[i] == target) {
/* 148 */         return i;
/*     */       }
/*     */     } 
/* 151 */     return -1;
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
/*     */   public static byte[] concat(byte[]... arrays) {
/* 163 */     int length = 0;
/* 164 */     for (byte[] array : arrays) {
/* 165 */       length += array.length;
/*     */     }
/* 167 */     byte[] result = new byte[length];
/* 168 */     int pos = 0;
/* 169 */     for (byte[] array : arrays) {
/* 170 */       System.arraycopy(array, 0, result, pos, array.length);
/* 171 */       pos += array.length;
/*     */     } 
/* 173 */     return result;
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
/*     */   public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
/* 190 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 191 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 192 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static byte[] toArray(Collection<? extends Number> collection) {
/* 209 */     if (collection instanceof ByteArrayAsList) {
/* 210 */       return ((ByteArrayAsList)collection).toByteArray();
/*     */     }
/*     */     
/* 213 */     Object[] boxedArray = collection.toArray();
/* 214 */     int len = boxedArray.length;
/* 215 */     byte[] array = new byte[len];
/* 216 */     for (int i = 0; i < len; i++)
/*     */     {
/* 218 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue();
/*     */     }
/* 220 */     return array;
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
/*     */   public static List<Byte> asList(byte... backingArray) {
/* 236 */     if (backingArray.length == 0) {
/* 237 */       return Collections.emptyList();
/*     */     }
/* 239 */     return new ByteArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
/*     */     final byte[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ByteArrayAsList(byte[] array) {
/* 250 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ByteArrayAsList(byte[] array, int start, int end) {
/* 254 */       this.array = array;
/* 255 */       this.start = start;
/* 256 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 261 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte get(int index) {
/* 271 */       Preconditions.checkElementIndex(index, size());
/* 272 */       return Byte.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 278 */       return (target instanceof Byte && Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 284 */       if (target instanceof Byte) {
/* 285 */         int i = Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 286 */         if (i >= 0) {
/* 287 */           return i - this.start;
/*     */         }
/*     */       } 
/* 290 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 296 */       if (target instanceof Byte) {
/* 297 */         int i = Bytes.lastIndexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 298 */         if (i >= 0) {
/* 299 */           return i - this.start;
/*     */         }
/*     */       } 
/* 302 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte set(int index, Byte element) {
/* 307 */       Preconditions.checkElementIndex(index, size());
/* 308 */       byte oldValue = this.array[this.start + index];
/*     */       
/* 310 */       this.array[this.start + index] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
/* 311 */       return Byte.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Byte> subList(int fromIndex, int toIndex) {
/* 316 */       int size = size();
/* 317 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 318 */       if (fromIndex == toIndex) {
/* 319 */         return Collections.emptyList();
/*     */       }
/* 321 */       return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 326 */       if (object == this) {
/* 327 */         return true;
/*     */       }
/* 329 */       if (object instanceof ByteArrayAsList) {
/* 330 */         ByteArrayAsList that = (ByteArrayAsList)object;
/* 331 */         int size = size();
/* 332 */         if (that.size() != size) {
/* 333 */           return false;
/*     */         }
/* 335 */         for (int i = 0; i < size; i++) {
/* 336 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 337 */             return false;
/*     */           }
/*     */         } 
/* 340 */         return true;
/*     */       } 
/* 342 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 347 */       int result = 1;
/* 348 */       for (int i = this.start; i < this.end; i++) {
/* 349 */         result = 31 * result + Bytes.hashCode(this.array[i]);
/*     */       }
/* 351 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 356 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 357 */       builder.append('[').append(this.array[this.start]);
/* 358 */       for (int i = this.start + 1; i < this.end; i++) {
/* 359 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 361 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     byte[] toByteArray() {
/* 365 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Bytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */