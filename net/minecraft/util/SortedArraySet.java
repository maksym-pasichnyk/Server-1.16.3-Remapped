/*     */ package net.minecraft.util;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrays;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
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
/*     */ public class SortedArraySet<T>
/*     */   extends AbstractSet<T>
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private T[] contents;
/*     */   private int size;
/*     */   
/*     */   private SortedArraySet(int debug1, Comparator<T> debug2) {
/*  26 */     this.comparator = debug2;
/*     */     
/*  28 */     if (debug1 < 0) {
/*  29 */       throw new IllegalArgumentException("Initial capacity (" + debug1 + ") is negative");
/*     */     }
/*  31 */     this.contents = castRawArray(new Object[debug1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<T>> SortedArraySet<T> create(int debug0) {
/*  39 */     return new SortedArraySet<>(debug0, (Comparator)Comparator.naturalOrder());
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
/*     */   private static <T> T[] castRawArray(Object[] debug0) {
/*  52 */     return (T[])debug0;
/*     */   }
/*     */   
/*     */   private int findIndex(T debug1) {
/*  56 */     return Arrays.binarySearch(this.contents, 0, this.size, debug1, this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getInsertionPosition(int debug0) {
/*  65 */     return -debug0 - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(T debug1) {
/*  70 */     int debug2 = findIndex(debug1);
/*  71 */     if (debug2 >= 0) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     int debug3 = getInsertionPosition(debug2);
/*  76 */     addInternal(debug1, debug3);
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   private void grow(int debug1) {
/*  81 */     if (debug1 <= this.contents.length) {
/*     */       return;
/*     */     }
/*  84 */     if (this.contents != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
/*  85 */       debug1 = (int)Math.max(Math.min(this.contents.length + (this.contents.length >> 1), 2147483639L), debug1);
/*  86 */     } else if (debug1 < 10) {
/*  87 */       debug1 = 10;
/*     */     } 
/*     */     
/*  90 */     Object[] debug2 = new Object[debug1];
/*  91 */     System.arraycopy(this.contents, 0, debug2, 0, this.size);
/*  92 */     this.contents = castRawArray(debug2);
/*     */   }
/*     */   
/*     */   private void addInternal(T debug1, int debug2) {
/*  96 */     grow(this.size + 1);
/*  97 */     if (debug2 != this.size) {
/*  98 */       System.arraycopy(this.contents, debug2, this.contents, debug2 + 1, this.size - debug2);
/*     */     }
/* 100 */     this.contents[debug2] = debug1;
/* 101 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeInternal(int debug1) {
/* 106 */     this.size--;
/* 107 */     if (debug1 != this.size) {
/* 108 */       System.arraycopy(this.contents, debug1 + 1, this.contents, debug1, this.size - debug1);
/*     */     }
/* 110 */     this.contents[this.size] = null;
/*     */   }
/*     */   
/*     */   private T getInternal(int debug1) {
/* 114 */     return this.contents[debug1];
/*     */   }
/*     */   
/*     */   public T addOrGet(T debug1) {
/* 118 */     int debug2 = findIndex(debug1);
/* 119 */     if (debug2 >= 0) {
/* 120 */       return getInternal(debug2);
/*     */     }
/*     */     
/* 123 */     addInternal(debug1, getInsertionPosition(debug2));
/* 124 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object debug1) {
/* 130 */     int debug2 = findIndex((T)debug1);
/* 131 */     if (debug2 >= 0) {
/* 132 */       removeInternal(debug2);
/* 133 */       return true;
/*     */     } 
/* 135 */     return false;
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
/*     */   public T first() {
/* 148 */     return getInternal(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object debug1) {
/* 158 */     int debug2 = findIndex((T)debug1);
/* 159 */     return (debug2 >= 0);
/*     */   }
/*     */   
/*     */   class ArrayIterator implements Iterator<T> {
/*     */     private int index;
/* 164 */     private int last = -1;
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 168 */       return (this.index < SortedArraySet.this.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 173 */       if (this.index >= SortedArraySet.this.size) {
/* 174 */         throw new NoSuchElementException();
/*     */       }
/* 176 */       this.last = this.index++;
/* 177 */       return (T)SortedArraySet.this.contents[this.last];
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 182 */       if (this.last == -1) {
/* 183 */         throw new IllegalStateException();
/*     */       }
/* 185 */       SortedArraySet.this.removeInternal(this.last);
/* 186 */       this.index--;
/* 187 */       this.last = -1;
/*     */     }
/*     */     
/*     */     private ArrayIterator() {} }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 193 */     return new ArrayIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 198 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 203 */     return (Object[])this.contents.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> U[] toArray(U[] debug1) {
/* 209 */     if (debug1.length < this.size) {
/* 210 */       return (U[])Arrays.<Object, T>copyOf(this.contents, this.size, (Class)debug1.getClass());
/*     */     }
/* 212 */     System.arraycopy(this.contents, 0, debug1, 0, this.size);
/* 213 */     if (debug1.length > this.size) {
/* 214 */       debug1[this.size] = null;
/*     */     }
/* 216 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 221 */     Arrays.fill((Object[])this.contents, 0, this.size, (Object)null);
/* 222 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 227 */     if (this == debug1) {
/* 228 */       return true;
/*     */     }
/* 230 */     if (debug1 instanceof SortedArraySet) {
/* 231 */       SortedArraySet<?> debug2 = (SortedArraySet)debug1;
/* 232 */       if (this.comparator.equals(debug2.comparator)) {
/* 233 */         return (this.size == debug2.size && Arrays.equals((Object[])this.contents, (Object[])debug2.contents));
/*     */       }
/*     */     } 
/*     */     
/* 237 */     return super.equals(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\SortedArraySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */