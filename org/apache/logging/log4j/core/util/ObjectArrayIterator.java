/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectArrayIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   final E[] array;
/*     */   final int startIndex;
/*     */   final int endIndex;
/*  44 */   int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public ObjectArrayIterator(E... array) {
/*  56 */     this(array, 0, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start) {
/*  69 */     this(array, start, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start, int end) {
/*  85 */     if (start < 0) {
/*  86 */       throw new ArrayIndexOutOfBoundsException("Start index must not be less than zero");
/*     */     }
/*  88 */     if (end > array.length) {
/*  89 */       throw new ArrayIndexOutOfBoundsException("End index must not be greater than the array length");
/*     */     }
/*  91 */     if (start > array.length) {
/*  92 */       throw new ArrayIndexOutOfBoundsException("Start index must not be greater than the array length");
/*     */     }
/*  94 */     if (end < start) {
/*  95 */       throw new IllegalArgumentException("End index must not be less than start index");
/*     */     }
/*  97 */     this.array = array;
/*  98 */     this.startIndex = start;
/*  99 */     this.endIndex = end;
/* 100 */     this.index = start;
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
/*     */   public boolean hasNext() {
/* 113 */     return (this.index < this.endIndex);
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
/*     */   public E next() {
/* 125 */     if (!hasNext()) {
/* 126 */       throw new NoSuchElementException();
/*     */     }
/* 128 */     return this.array[this.index++];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 138 */     throw new UnsupportedOperationException("remove() method is not supported for an ObjectArrayIterator");
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
/*     */   public E[] getArray() {
/* 150 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartIndex() {
/* 159 */     return this.startIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndIndex() {
/* 168 */     return this.endIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 176 */     this.index = this.startIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\ObjectArrayIterator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */