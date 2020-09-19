/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ public class BinaryHeap {
/*   4 */   private Node[] heap = new Node[128];
/*     */   
/*     */   private int size;
/*     */   
/*     */   public Node insert(Node debug1) {
/*   9 */     if (debug1.heapIdx >= 0) {
/*  10 */       throw new IllegalStateException("OW KNOWS!");
/*     */     }
/*     */     
/*  13 */     if (this.size == this.heap.length) {
/*  14 */       Node[] debug2 = new Node[this.size << 1];
/*  15 */       System.arraycopy(this.heap, 0, debug2, 0, this.size);
/*  16 */       this.heap = debug2;
/*     */     } 
/*     */ 
/*     */     
/*  20 */     this.heap[this.size] = debug1;
/*  21 */     debug1.heapIdx = this.size;
/*  22 */     upHeap(this.size++);
/*     */     
/*  24 */     return debug1;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  28 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node pop() {
/*  36 */     Node debug1 = this.heap[0];
/*  37 */     this.heap[0] = this.heap[--this.size];
/*  38 */     this.heap[this.size] = null;
/*  39 */     if (this.size > 0) {
/*  40 */       downHeap(0);
/*     */     }
/*  42 */     debug1.heapIdx = -1;
/*  43 */     return debug1;
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
/*     */   public void changeCost(Node debug1, float debug2) {
/*  62 */     float debug3 = debug1.f;
/*  63 */     debug1.f = debug2;
/*  64 */     if (debug2 < debug3) {
/*  65 */       upHeap(debug1.heapIdx);
/*     */     } else {
/*  67 */       downHeap(debug1.heapIdx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void upHeap(int debug1) {
/*  76 */     Node debug2 = this.heap[debug1];
/*  77 */     float debug3 = debug2.f;
/*  78 */     while (debug1 > 0) {
/*  79 */       int debug4 = debug1 - 1 >> 1;
/*  80 */       Node debug5 = this.heap[debug4];
/*  81 */       if (debug3 < debug5.f) {
/*  82 */         this.heap[debug1] = debug5;
/*  83 */         debug5.heapIdx = debug1;
/*  84 */         debug1 = debug4;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  89 */     this.heap[debug1] = debug2;
/*  90 */     debug2.heapIdx = debug1;
/*     */   }
/*     */   
/*     */   private void downHeap(int debug1) {
/*  94 */     Node debug2 = this.heap[debug1];
/*  95 */     float debug3 = debug2.f; while (true) {
/*     */       Node debug8;
/*     */       float debug9;
/*  98 */       int debug4 = 1 + (debug1 << 1);
/*  99 */       int debug5 = debug4 + 1;
/*     */       
/* 101 */       if (debug4 >= this.size) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 106 */       Node debug6 = this.heap[debug4];
/* 107 */       float debug7 = debug6.f;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (debug5 >= this.size) {
/*     */         
/* 114 */         debug8 = null;
/* 115 */         debug9 = Float.POSITIVE_INFINITY;
/*     */       } else {
/* 117 */         debug8 = this.heap[debug5];
/* 118 */         debug9 = debug8.f;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 123 */       if (debug7 < debug9) {
/* 124 */         if (debug7 < debug3) {
/* 125 */           this.heap[debug1] = debug6;
/* 126 */           debug6.heapIdx = debug1;
/* 127 */           debug1 = debug4;
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 132 */       if (debug9 < debug3) {
/* 133 */         this.heap[debug1] = debug8;
/* 134 */         debug8.heapIdx = debug1;
/* 135 */         debug1 = debug5;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 142 */     this.heap[debug1] = debug2;
/* 143 */     debug2.heapIdx = debug1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 147 */     return (this.size == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\BinaryHeap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */