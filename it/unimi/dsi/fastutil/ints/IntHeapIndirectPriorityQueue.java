/*     */ package it.unimi.dsi.fastutil.ints;
/*     */ 
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntHeapIndirectPriorityQueue
/*     */   extends IntHeapSemiIndirectPriorityQueue
/*     */ {
/*     */   protected final int[] inv;
/*     */   
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int capacity, IntComparator c) {
/*  49 */     super(refArray, capacity, c);
/*  50 */     if (capacity > 0)
/*  51 */       this.heap = new int[capacity]; 
/*  52 */     this.c = c;
/*  53 */     this.inv = new int[refArray.length];
/*  54 */     Arrays.fill(this.inv, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int capacity) {
/*  65 */     this(refArray, capacity, (IntComparator)null);
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
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, IntComparator c) {
/*  78 */     this(refArray, refArray.length, c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray) {
/*  88 */     this(refArray, refArray.length, (IntComparator)null);
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
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int[] a, int size, IntComparator c) {
/* 109 */     this(refArray, 0, c);
/* 110 */     this.heap = a;
/* 111 */     this.size = size;
/* 112 */     int i = size;
/* 113 */     while (i-- != 0) {
/* 114 */       if (this.inv[a[i]] != -1)
/* 115 */         throw new IllegalArgumentException("Index " + a[i] + " appears twice in the heap"); 
/* 116 */       this.inv[a[i]] = i;
/*     */     } 
/* 118 */     IntIndirectHeaps.makeHeap(refArray, a, this.inv, size, c);
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
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int[] a, IntComparator c) {
/* 137 */     this(refArray, a, a.length, c);
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
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int[] a, int size) {
/* 155 */     this(refArray, a, size, (IntComparator)null);
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
/*     */   public IntHeapIndirectPriorityQueue(int[] refArray, int[] a) {
/* 171 */     this(refArray, a, a.length);
/*     */   }
/*     */   
/*     */   public void enqueue(int x) {
/* 175 */     if (this.inv[x] >= 0)
/* 176 */       throw new IllegalArgumentException("Index " + x + " belongs to the queue"); 
/* 177 */     if (this.size == this.heap.length)
/* 178 */       this.heap = IntArrays.grow(this.heap, this.size + 1); 
/* 179 */     this.heap[this.size] = x; this.inv[x] = this.size++;
/* 180 */     IntIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, this.size - 1, this.c);
/*     */   }
/*     */   
/*     */   public boolean contains(int index) {
/* 184 */     return (this.inv[index] >= 0);
/*     */   }
/*     */   
/*     */   public int dequeue() {
/* 188 */     if (this.size == 0)
/* 189 */       throw new NoSuchElementException(); 
/* 190 */     int result = this.heap[0];
/* 191 */     if (--this.size != 0) {
/* 192 */       this.heap[0] = this.heap[this.size]; this.inv[this.heap[this.size]] = 0;
/* 193 */     }  this.inv[result] = -1;
/* 194 */     if (this.size != 0)
/* 195 */       IntIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, 0, this.c); 
/* 196 */     return result;
/*     */   }
/*     */   
/*     */   public void changed() {
/* 200 */     IntIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, 0, this.c);
/*     */   }
/*     */   
/*     */   public void changed(int index) {
/* 204 */     int pos = this.inv[index];
/* 205 */     if (pos < 0)
/* 206 */       throw new IllegalArgumentException("Index " + index + " does not belong to the queue"); 
/* 207 */     int newPos = IntIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, pos, this.c);
/* 208 */     IntIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, newPos, this.c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allChanged() {
/* 213 */     IntIndirectHeaps.makeHeap(this.refArray, this.heap, this.inv, this.size, this.c);
/*     */   }
/*     */   
/*     */   public boolean remove(int index) {
/* 217 */     int result = this.inv[index];
/* 218 */     if (result < 0)
/* 219 */       return false; 
/* 220 */     this.inv[index] = -1;
/* 221 */     if (result < --this.size) {
/* 222 */       this.heap[result] = this.heap[this.size]; this.inv[this.heap[this.size]] = result;
/* 223 */       int newPos = IntIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, result, this.c);
/* 224 */       IntIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, newPos, this.c);
/*     */     } 
/* 226 */     return true;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 230 */     this.size = 0;
/* 231 */     Arrays.fill(this.inv, -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntHeapIndirectPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */