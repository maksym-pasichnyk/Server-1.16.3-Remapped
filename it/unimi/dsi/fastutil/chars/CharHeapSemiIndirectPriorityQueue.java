/*     */ package it.unimi.dsi.fastutil.chars;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*     */ import java.util.Comparator;
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
/*     */ public class CharHeapSemiIndirectPriorityQueue
/*     */   implements CharIndirectPriorityQueue
/*     */ {
/*     */   protected final char[] refArray;
/*  36 */   protected int[] heap = IntArrays.EMPTY_ARRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int size;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CharComparator c;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int capacity, CharComparator c) {
/*  54 */     if (capacity > 0)
/*  55 */       this.heap = new int[capacity]; 
/*  56 */     this.refArray = refArray;
/*  57 */     this.c = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int capacity) {
/*  68 */     this(refArray, capacity, (CharComparator)null);
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
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, CharComparator c) {
/*  81 */     this(refArray, refArray.length, c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray) {
/*  91 */     this(refArray, refArray.length, (CharComparator)null);
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
/*     */ 
/*     */   
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int[] a, int size, CharComparator c) {
/* 112 */     this(refArray, 0, c);
/* 113 */     this.heap = a;
/* 114 */     this.size = size;
/* 115 */     CharSemiIndirectHeaps.makeHeap(refArray, a, size, c);
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
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int[] a, CharComparator c) {
/* 134 */     this(refArray, a, a.length, c);
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
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int[] a, int size) {
/* 152 */     this(refArray, a, size, null);
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
/*     */   public CharHeapSemiIndirectPriorityQueue(char[] refArray, int[] a) {
/* 168 */     this(refArray, a, a.length);
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
/*     */   protected void ensureElement(int index) {
/* 180 */     if (index < 0)
/* 181 */       throw new IndexOutOfBoundsException("Index (" + index + ") is negative"); 
/* 182 */     if (index >= this.refArray.length) {
/* 183 */       throw new IndexOutOfBoundsException("Index (" + index + ") is larger than or equal to reference array size (" + this.refArray.length + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   public void enqueue(int x) {
/* 188 */     ensureElement(x);
/* 189 */     if (this.size == this.heap.length)
/* 190 */       this.heap = IntArrays.grow(this.heap, this.size + 1); 
/* 191 */     this.heap[this.size++] = x;
/* 192 */     CharSemiIndirectHeaps.upHeap(this.refArray, this.heap, this.size, this.size - 1, this.c);
/*     */   }
/*     */   
/*     */   public int dequeue() {
/* 196 */     if (this.size == 0)
/* 197 */       throw new NoSuchElementException(); 
/* 198 */     int result = this.heap[0];
/* 199 */     this.heap[0] = this.heap[--this.size];
/* 200 */     if (this.size != 0)
/* 201 */       CharSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c); 
/* 202 */     return result;
/*     */   }
/*     */   
/*     */   public int first() {
/* 206 */     if (this.size == 0)
/* 207 */       throw new NoSuchElementException(); 
/* 208 */     return this.heap[0];
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
/*     */   public void changed() {
/* 221 */     CharSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allChanged() {
/* 226 */     CharSemiIndirectHeaps.makeHeap(this.refArray, this.heap, this.size, this.c);
/*     */   }
/*     */   
/*     */   public int size() {
/* 230 */     return this.size;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 234 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void trim() {
/* 238 */     this.heap = IntArrays.trim(this.heap, this.size);
/*     */   }
/*     */   
/*     */   public CharComparator comparator() {
/* 242 */     return this.c;
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
/*     */   public int front(int[] a) {
/* 255 */     return (this.c == null) ? 
/* 256 */       CharSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a) : 
/* 257 */       CharSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a, this.c);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 261 */     StringBuffer s = new StringBuffer();
/* 262 */     s.append("[");
/* 263 */     for (int i = 0; i < this.size; i++) {
/* 264 */       if (i != 0)
/* 265 */         s.append(", "); 
/* 266 */       s.append(this.refArray[this.heap[i]]);
/*     */     } 
/* 268 */     s.append("]");
/* 269 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharHeapSemiIndirectPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */