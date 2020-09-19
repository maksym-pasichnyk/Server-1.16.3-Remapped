/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolChunkList<T>
/*     */   implements PoolChunkListMetric
/*     */ {
/*  29 */   private static final Iterator<PoolChunkMetric> EMPTY_METRICS = Collections.<PoolChunkMetric>emptyList().iterator();
/*     */   
/*     */   private final PoolArena<T> arena;
/*     */   
/*     */   private final PoolChunkList<T> nextList;
/*     */   
/*     */   private final int minUsage;
/*     */   
/*     */   private final int maxUsage;
/*     */   
/*     */   private final int maxCapacity;
/*     */   private PoolChunk<T> head;
/*     */   private PoolChunkList<T> prevList;
/*     */   
/*     */   PoolChunkList(PoolArena<T> arena, PoolChunkList<T> nextList, int minUsage, int maxUsage, int chunkSize) {
/*  44 */     assert minUsage <= maxUsage;
/*  45 */     this.arena = arena;
/*  46 */     this.nextList = nextList;
/*  47 */     this.minUsage = minUsage;
/*  48 */     this.maxUsage = maxUsage;
/*  49 */     this.maxCapacity = calculateMaxCapacity(minUsage, chunkSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calculateMaxCapacity(int minUsage, int chunkSize) {
/*  57 */     minUsage = minUsage0(minUsage);
/*     */     
/*  59 */     if (minUsage == 100)
/*     */     {
/*  61 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     return (int)(chunkSize * (100L - minUsage) / 100L);
/*     */   }
/*     */   
/*     */   void prevList(PoolChunkList<T> prevList) {
/*  73 */     assert this.prevList == null;
/*  74 */     this.prevList = prevList;
/*     */   }
/*     */   boolean allocate(PooledByteBuf<T> buf, int reqCapacity, int normCapacity) {
/*     */     long handle;
/*  78 */     if (this.head == null || normCapacity > this.maxCapacity)
/*     */     {
/*     */       
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     PoolChunk<T> cur = this.head; while (true) {
/*  85 */       handle = cur.allocate(normCapacity);
/*  86 */       if (handle < 0L) {
/*  87 */         cur = cur.next;
/*  88 */         if (cur == null)
/*  89 */           return false;  continue;
/*     */       }  break;
/*     */     } 
/*  92 */     cur.initBuf(buf, handle, reqCapacity);
/*  93 */     if (cur.usage() >= this.maxUsage) {
/*  94 */       remove(cur);
/*  95 */       this.nextList.add(cur);
/*     */     } 
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean free(PoolChunk<T> chunk, long handle) {
/* 103 */     chunk.free(handle);
/* 104 */     if (chunk.usage() < this.minUsage) {
/* 105 */       remove(chunk);
/*     */       
/* 107 */       return move0(chunk);
/*     */     } 
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   private boolean move(PoolChunk<T> chunk) {
/* 113 */     assert chunk.usage() < this.maxUsage;
/*     */     
/* 115 */     if (chunk.usage() < this.minUsage)
/*     */     {
/* 117 */       return move0(chunk);
/*     */     }
/*     */ 
/*     */     
/* 121 */     add0(chunk);
/* 122 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean move0(PoolChunk<T> chunk) {
/* 130 */     if (this.prevList == null) {
/*     */ 
/*     */       
/* 133 */       assert chunk.usage() == 0;
/* 134 */       return false;
/*     */     } 
/* 136 */     return this.prevList.move(chunk);
/*     */   }
/*     */   
/*     */   void add(PoolChunk<T> chunk) {
/* 140 */     if (chunk.usage() >= this.maxUsage) {
/* 141 */       this.nextList.add(chunk);
/*     */       return;
/*     */     } 
/* 144 */     add0(chunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void add0(PoolChunk<T> chunk) {
/* 151 */     chunk.parent = this;
/* 152 */     if (this.head == null) {
/* 153 */       this.head = chunk;
/* 154 */       chunk.prev = null;
/* 155 */       chunk.next = null;
/*     */     } else {
/* 157 */       chunk.prev = null;
/* 158 */       chunk.next = this.head;
/* 159 */       this.head.prev = chunk;
/* 160 */       this.head = chunk;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void remove(PoolChunk<T> cur) {
/* 165 */     if (cur == this.head) {
/* 166 */       this.head = cur.next;
/* 167 */       if (this.head != null) {
/* 168 */         this.head.prev = null;
/*     */       }
/*     */     } else {
/* 171 */       PoolChunk<T> next = cur.next;
/* 172 */       cur.prev.next = next;
/* 173 */       if (next != null) {
/* 174 */         next.prev = cur.prev;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int minUsage() {
/* 181 */     return minUsage0(this.minUsage);
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxUsage() {
/* 186 */     return Math.min(this.maxUsage, 100);
/*     */   }
/*     */   
/*     */   private static int minUsage0(int value) {
/* 190 */     return Math.max(1, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<PoolChunkMetric> iterator() {
/* 195 */     synchronized (this.arena) {
/* 196 */       if (this.head == null) {
/* 197 */         return EMPTY_METRICS;
/*     */       }
/* 199 */       List<PoolChunkMetric> metrics = new ArrayList<PoolChunkMetric>();
/* 200 */       PoolChunk<T> cur = this.head; do {
/* 201 */         metrics.add(cur);
/* 202 */         cur = cur.next;
/* 203 */       } while (cur != null);
/*     */ 
/*     */ 
/*     */       
/* 207 */       return metrics.iterator();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 213 */     StringBuilder buf = new StringBuilder();
/* 214 */     synchronized (this.arena) {
/* 215 */       if (this.head == null) {
/* 216 */         return "none";
/*     */       }
/*     */       
/* 219 */       PoolChunk<T> cur = this.head; while (true) {
/* 220 */         buf.append(cur);
/* 221 */         cur = cur.next;
/* 222 */         if (cur == null) {
/*     */           break;
/*     */         }
/* 225 */         buf.append(StringUtil.NEWLINE);
/*     */       } 
/*     */     } 
/* 228 */     return buf.toString();
/*     */   }
/*     */   
/*     */   void destroy(PoolArena<T> arena) {
/* 232 */     PoolChunk<T> chunk = this.head;
/* 233 */     while (chunk != null) {
/* 234 */       arena.destroyChunk(chunk);
/* 235 */       chunk = chunk.next;
/*     */     } 
/* 237 */     this.head = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolChunkList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */