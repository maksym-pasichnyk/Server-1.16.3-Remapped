/*     */ package io.netty.buffer;
/*     */ import io.netty.util.internal.LongCounter;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ abstract class PoolArena<T> implements PoolArenaMetric {
/*     */   static final int numTinySubpagePools = 32;
/*     */   final PooledByteBufAllocator parent;
/*     */   private final int maxOrder;
/*     */   final int pageSize;
/*     */   final int pageShifts;
/*     */   final int chunkSize;
/*     */   final int subpageOverflowMask;
/*     */   final int numSmallSubpagePools;
/*     */   final int directMemoryCacheAlignment;
/*     */   final int directMemoryCacheAlignmentMask;
/*     */   private final PoolSubpage<T>[] tinySubpagePools;
/*     */   private final PoolSubpage<T>[] smallSubpagePools;
/*     */   private final PoolChunkList<T> q050;
/*     */   private final PoolChunkList<T> q025;
/*     */   private final PoolChunkList<T> q000;
/*     */   private final PoolChunkList<T> qInit;
/*     */   private final PoolChunkList<T> q075;
/*     */   private final PoolChunkList<T> q100;
/*     */   private final List<PoolChunkListMetric> chunkListMetrics;
/*     */   private long allocationsNormal;
/*  32 */   static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe();
/*     */   
/*     */   enum SizeClass {
/*  35 */     Tiny,
/*  36 */     Small,
/*  37 */     Normal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private final LongCounter allocationsTiny = PlatformDependent.newLongCounter();
/*  68 */   private final LongCounter allocationsSmall = PlatformDependent.newLongCounter();
/*  69 */   private final LongCounter allocationsHuge = PlatformDependent.newLongCounter();
/*  70 */   private final LongCounter activeBytesHuge = PlatformDependent.newLongCounter();
/*     */   
/*     */   private long deallocationsTiny;
/*     */   
/*     */   private long deallocationsSmall;
/*     */   
/*     */   private long deallocationsNormal;
/*  77 */   private final LongCounter deallocationsHuge = PlatformDependent.newLongCounter();
/*     */ 
/*     */   
/*  80 */   final AtomicInteger numThreadCaches = new AtomicInteger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PoolArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int cacheAlignment) {
/*  87 */     this.parent = parent;
/*  88 */     this.pageSize = pageSize;
/*  89 */     this.maxOrder = maxOrder;
/*  90 */     this.pageShifts = pageShifts;
/*  91 */     this.chunkSize = chunkSize;
/*  92 */     this.directMemoryCacheAlignment = cacheAlignment;
/*  93 */     this.directMemoryCacheAlignmentMask = cacheAlignment - 1;
/*  94 */     this.subpageOverflowMask = pageSize - 1 ^ 0xFFFFFFFF;
/*  95 */     this.tinySubpagePools = newSubpagePoolArray(32); int i;
/*  96 */     for (i = 0; i < this.tinySubpagePools.length; i++) {
/*  97 */       this.tinySubpagePools[i] = newSubpagePoolHead(pageSize);
/*     */     }
/*     */     
/* 100 */     this.numSmallSubpagePools = pageShifts - 9;
/* 101 */     this.smallSubpagePools = newSubpagePoolArray(this.numSmallSubpagePools);
/* 102 */     for (i = 0; i < this.smallSubpagePools.length; i++) {
/* 103 */       this.smallSubpagePools[i] = newSubpagePoolHead(pageSize);
/*     */     }
/*     */     
/* 106 */     this.q100 = new PoolChunkList<T>(this, null, 100, 2147483647, chunkSize);
/* 107 */     this.q075 = new PoolChunkList<T>(this, this.q100, 75, 100, chunkSize);
/* 108 */     this.q050 = new PoolChunkList<T>(this, this.q075, 50, 100, chunkSize);
/* 109 */     this.q025 = new PoolChunkList<T>(this, this.q050, 25, 75, chunkSize);
/* 110 */     this.q000 = new PoolChunkList<T>(this, this.q025, 1, 50, chunkSize);
/* 111 */     this.qInit = new PoolChunkList<T>(this, this.q000, -2147483648, 25, chunkSize);
/*     */     
/* 113 */     this.q100.prevList(this.q075);
/* 114 */     this.q075.prevList(this.q050);
/* 115 */     this.q050.prevList(this.q025);
/* 116 */     this.q025.prevList(this.q000);
/* 117 */     this.q000.prevList(null);
/* 118 */     this.qInit.prevList(this.qInit);
/*     */     
/* 120 */     List<PoolChunkListMetric> metrics = new ArrayList<PoolChunkListMetric>(6);
/* 121 */     metrics.add(this.qInit);
/* 122 */     metrics.add(this.q000);
/* 123 */     metrics.add(this.q025);
/* 124 */     metrics.add(this.q050);
/* 125 */     metrics.add(this.q075);
/* 126 */     metrics.add(this.q100);
/* 127 */     this.chunkListMetrics = Collections.unmodifiableList(metrics);
/*     */   }
/*     */   
/*     */   private PoolSubpage<T> newSubpagePoolHead(int pageSize) {
/* 131 */     PoolSubpage<T> head = new PoolSubpage<T>(pageSize);
/* 132 */     head.prev = head;
/* 133 */     head.next = head;
/* 134 */     return head;
/*     */   }
/*     */ 
/*     */   
/*     */   private PoolSubpage<T>[] newSubpagePoolArray(int size) {
/* 139 */     return (PoolSubpage<T>[])new PoolSubpage[size];
/*     */   }
/*     */   
/*     */   abstract boolean isDirect();
/*     */   
/*     */   PooledByteBuf<T> allocate(PoolThreadCache cache, int reqCapacity, int maxCapacity) {
/* 145 */     PooledByteBuf<T> buf = newByteBuf(maxCapacity);
/* 146 */     allocate(cache, buf, reqCapacity);
/* 147 */     return buf;
/*     */   }
/*     */   
/*     */   static int tinyIdx(int normCapacity) {
/* 151 */     return normCapacity >>> 4;
/*     */   }
/*     */   
/*     */   static int smallIdx(int normCapacity) {
/* 155 */     int tableIdx = 0;
/* 156 */     int i = normCapacity >>> 10;
/* 157 */     while (i != 0) {
/* 158 */       i >>>= 1;
/* 159 */       tableIdx++;
/*     */     } 
/* 161 */     return tableIdx;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isTinyOrSmall(int normCapacity) {
/* 166 */     return ((normCapacity & this.subpageOverflowMask) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isTiny(int normCapacity) {
/* 171 */     return ((normCapacity & 0xFFFFFE00) == 0);
/*     */   }
/*     */   
/*     */   private void allocate(PoolThreadCache cache, PooledByteBuf<T> buf, int reqCapacity) {
/* 175 */     int normCapacity = normalizeCapacity(reqCapacity);
/* 176 */     if (isTinyOrSmall(normCapacity)) {
/*     */       int tableIdx;
/*     */       PoolSubpage<T>[] table;
/* 179 */       boolean tiny = isTiny(normCapacity);
/* 180 */       if (tiny) {
/* 181 */         if (cache.allocateTiny(this, buf, reqCapacity, normCapacity)) {
/*     */           return;
/*     */         }
/*     */         
/* 185 */         tableIdx = tinyIdx(normCapacity);
/* 186 */         table = this.tinySubpagePools;
/*     */       } else {
/* 188 */         if (cache.allocateSmall(this, buf, reqCapacity, normCapacity)) {
/*     */           return;
/*     */         }
/*     */         
/* 192 */         tableIdx = smallIdx(normCapacity);
/* 193 */         table = this.smallSubpagePools;
/*     */       } 
/*     */       
/* 196 */       PoolSubpage<T> head = table[tableIdx];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 202 */       synchronized (head) {
/* 203 */         PoolSubpage<T> s = head.next;
/* 204 */         if (s != head) {
/* 205 */           assert s.doNotDestroy && s.elemSize == normCapacity;
/* 206 */           long handle = s.allocate();
/* 207 */           assert handle >= 0L;
/* 208 */           s.chunk.initBufWithSubpage(buf, handle, reqCapacity);
/* 209 */           incTinySmallAllocation(tiny);
/*     */           return;
/*     */         } 
/*     */       } 
/* 213 */       synchronized (this) {
/* 214 */         allocateNormal(buf, reqCapacity, normCapacity);
/*     */       } 
/*     */       
/* 217 */       incTinySmallAllocation(tiny);
/*     */       return;
/*     */     } 
/* 220 */     if (normCapacity <= this.chunkSize) {
/* 221 */       if (cache.allocateNormal(this, buf, reqCapacity, normCapacity)) {
/*     */         return;
/*     */       }
/*     */       
/* 225 */       synchronized (this) {
/* 226 */         allocateNormal(buf, reqCapacity, normCapacity);
/* 227 */         this.allocationsNormal++;
/*     */       } 
/*     */     } else {
/*     */       
/* 231 */       allocateHuge(buf, reqCapacity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void allocateNormal(PooledByteBuf<T> buf, int reqCapacity, int normCapacity) {
/* 237 */     if (this.q050.allocate(buf, reqCapacity, normCapacity) || this.q025.allocate(buf, reqCapacity, normCapacity) || this.q000
/* 238 */       .allocate(buf, reqCapacity, normCapacity) || this.qInit.allocate(buf, reqCapacity, normCapacity) || this.q075
/* 239 */       .allocate(buf, reqCapacity, normCapacity)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 244 */     PoolChunk<T> c = newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
/* 245 */     long handle = c.allocate(normCapacity);
/* 246 */     assert handle > 0L;
/* 247 */     c.initBuf(buf, handle, reqCapacity);
/* 248 */     this.qInit.add(c);
/*     */   }
/*     */   
/*     */   private void incTinySmallAllocation(boolean tiny) {
/* 252 */     if (tiny) {
/* 253 */       this.allocationsTiny.increment();
/*     */     } else {
/* 255 */       this.allocationsSmall.increment();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void allocateHuge(PooledByteBuf<T> buf, int reqCapacity) {
/* 260 */     PoolChunk<T> chunk = newUnpooledChunk(reqCapacity);
/* 261 */     this.activeBytesHuge.add(chunk.chunkSize());
/* 262 */     buf.initUnpooled(chunk, reqCapacity);
/* 263 */     this.allocationsHuge.increment();
/*     */   }
/*     */   
/*     */   void free(PoolChunk<T> chunk, long handle, int normCapacity, PoolThreadCache cache) {
/* 267 */     if (chunk.unpooled) {
/* 268 */       int size = chunk.chunkSize();
/* 269 */       destroyChunk(chunk);
/* 270 */       this.activeBytesHuge.add(-size);
/* 271 */       this.deallocationsHuge.increment();
/*     */     } else {
/* 273 */       SizeClass sizeClass = sizeClass(normCapacity);
/* 274 */       if (cache != null && cache.add(this, chunk, handle, normCapacity, sizeClass)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 279 */       freeChunk(chunk, handle, sizeClass);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SizeClass sizeClass(int normCapacity) {
/* 284 */     if (!isTinyOrSmall(normCapacity)) {
/* 285 */       return SizeClass.Normal;
/*     */     }
/* 287 */     return isTiny(normCapacity) ? SizeClass.Tiny : SizeClass.Small;
/*     */   }
/*     */   
/*     */   void freeChunk(PoolChunk<T> chunk, long handle, SizeClass sizeClass) {
/*     */     boolean destroyChunk;
/* 292 */     synchronized (this) {
/* 293 */       switch (sizeClass) {
/*     */         case Normal:
/* 295 */           this.deallocationsNormal++;
/*     */           break;
/*     */         case Small:
/* 298 */           this.deallocationsSmall++;
/*     */           break;
/*     */         case Tiny:
/* 301 */           this.deallocationsTiny++;
/*     */           break;
/*     */         default:
/* 304 */           throw new Error();
/*     */       } 
/* 306 */       destroyChunk = !chunk.parent.free(chunk, handle);
/*     */     } 
/* 308 */     if (destroyChunk)
/*     */     {
/* 310 */       destroyChunk(chunk);
/*     */     }
/*     */   }
/*     */   
/*     */   PoolSubpage<T> findSubpagePoolHead(int elemSize) {
/*     */     int tableIdx;
/*     */     PoolSubpage<T>[] table;
/* 317 */     if (isTiny(elemSize)) {
/* 318 */       tableIdx = elemSize >>> 4;
/* 319 */       table = this.tinySubpagePools;
/*     */     } else {
/* 321 */       tableIdx = 0;
/* 322 */       elemSize >>>= 10;
/* 323 */       while (elemSize != 0) {
/* 324 */         elemSize >>>= 1;
/* 325 */         tableIdx++;
/*     */       } 
/* 327 */       table = this.smallSubpagePools;
/*     */     } 
/*     */     
/* 330 */     return table[tableIdx];
/*     */   }
/*     */   
/*     */   int normalizeCapacity(int reqCapacity) {
/* 334 */     if (reqCapacity < 0) {
/* 335 */       throw new IllegalArgumentException("capacity: " + reqCapacity + " (expected: 0+)");
/*     */     }
/*     */     
/* 338 */     if (reqCapacity >= this.chunkSize) {
/* 339 */       return (this.directMemoryCacheAlignment == 0) ? reqCapacity : alignCapacity(reqCapacity);
/*     */     }
/*     */     
/* 342 */     if (!isTiny(reqCapacity)) {
/*     */ 
/*     */       
/* 345 */       int normalizedCapacity = reqCapacity;
/* 346 */       normalizedCapacity--;
/* 347 */       normalizedCapacity |= normalizedCapacity >>> 1;
/* 348 */       normalizedCapacity |= normalizedCapacity >>> 2;
/* 349 */       normalizedCapacity |= normalizedCapacity >>> 4;
/* 350 */       normalizedCapacity |= normalizedCapacity >>> 8;
/* 351 */       normalizedCapacity |= normalizedCapacity >>> 16;
/* 352 */       normalizedCapacity++;
/*     */       
/* 354 */       if (normalizedCapacity < 0) {
/* 355 */         normalizedCapacity >>>= 1;
/*     */       }
/* 357 */       assert this.directMemoryCacheAlignment == 0 || (normalizedCapacity & this.directMemoryCacheAlignmentMask) == 0;
/*     */       
/* 359 */       return normalizedCapacity;
/*     */     } 
/*     */     
/* 362 */     if (this.directMemoryCacheAlignment > 0) {
/* 363 */       return alignCapacity(reqCapacity);
/*     */     }
/*     */ 
/*     */     
/* 367 */     if ((reqCapacity & 0xF) == 0) {
/* 368 */       return reqCapacity;
/*     */     }
/*     */     
/* 371 */     return (reqCapacity & 0xFFFFFFF0) + 16;
/*     */   }
/*     */   
/*     */   int alignCapacity(int reqCapacity) {
/* 375 */     int delta = reqCapacity & this.directMemoryCacheAlignmentMask;
/* 376 */     return (delta == 0) ? reqCapacity : (reqCapacity + this.directMemoryCacheAlignment - delta);
/*     */   }
/*     */   
/*     */   void reallocate(PooledByteBuf<T> buf, int newCapacity, boolean freeOldMemory) {
/* 380 */     if (newCapacity < 0 || newCapacity > buf.maxCapacity()) {
/* 381 */       throw new IllegalArgumentException("newCapacity: " + newCapacity);
/*     */     }
/*     */     
/* 384 */     int oldCapacity = buf.length;
/* 385 */     if (oldCapacity == newCapacity) {
/*     */       return;
/*     */     }
/*     */     
/* 389 */     PoolChunk<T> oldChunk = buf.chunk;
/* 390 */     long oldHandle = buf.handle;
/* 391 */     T oldMemory = buf.memory;
/* 392 */     int oldOffset = buf.offset;
/* 393 */     int oldMaxLength = buf.maxLength;
/* 394 */     int readerIndex = buf.readerIndex();
/* 395 */     int writerIndex = buf.writerIndex();
/*     */     
/* 397 */     allocate(this.parent.threadCache(), buf, newCapacity);
/* 398 */     if (newCapacity > oldCapacity) {
/* 399 */       memoryCopy(oldMemory, oldOffset, buf.memory, buf.offset, oldCapacity);
/*     */     
/*     */     }
/* 402 */     else if (newCapacity < oldCapacity) {
/* 403 */       if (readerIndex < newCapacity) {
/* 404 */         if (writerIndex > newCapacity) {
/* 405 */           writerIndex = newCapacity;
/*     */         }
/* 407 */         memoryCopy(oldMemory, oldOffset + readerIndex, buf.memory, buf.offset + readerIndex, writerIndex - readerIndex);
/*     */       }
/*     */       else {
/*     */         
/* 411 */         readerIndex = writerIndex = newCapacity;
/*     */       } 
/*     */     } 
/*     */     
/* 415 */     buf.setIndex(readerIndex, writerIndex);
/*     */     
/* 417 */     if (freeOldMemory) {
/* 418 */       free(oldChunk, oldHandle, oldMaxLength, buf.cache);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int numThreadCaches() {
/* 424 */     return this.numThreadCaches.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public int numTinySubpages() {
/* 429 */     return this.tinySubpagePools.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int numSmallSubpages() {
/* 434 */     return this.smallSubpagePools.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int numChunkLists() {
/* 439 */     return this.chunkListMetrics.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PoolSubpageMetric> tinySubpages() {
/* 444 */     return subPageMetricList((PoolSubpage<?>[])this.tinySubpagePools);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PoolSubpageMetric> smallSubpages() {
/* 449 */     return subPageMetricList((PoolSubpage<?>[])this.smallSubpagePools);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PoolChunkListMetric> chunkLists() {
/* 454 */     return this.chunkListMetrics;
/*     */   }
/*     */   
/*     */   private static List<PoolSubpageMetric> subPageMetricList(PoolSubpage<?>[] pages) {
/* 458 */     List<PoolSubpageMetric> metrics = new ArrayList<PoolSubpageMetric>();
/* 459 */     for (PoolSubpage<?> head : pages) {
/* 460 */       if (head.next != head) {
/*     */ 
/*     */         
/* 463 */         PoolSubpage<?> s = head.next;
/*     */         do {
/* 465 */           metrics.add(s);
/* 466 */           s = s.next;
/* 467 */         } while (s != head);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 472 */     return metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public long numAllocations() {
/*     */     long allocsNormal;
/* 478 */     synchronized (this) {
/* 479 */       allocsNormal = this.allocationsNormal;
/*     */     } 
/* 481 */     return this.allocationsTiny.value() + this.allocationsSmall.value() + allocsNormal + this.allocationsHuge.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public long numTinyAllocations() {
/* 486 */     return this.allocationsTiny.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public long numSmallAllocations() {
/* 491 */     return this.allocationsSmall.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long numNormalAllocations() {
/* 496 */     return this.allocationsNormal;
/*     */   }
/*     */ 
/*     */   
/*     */   public long numDeallocations() {
/*     */     long deallocs;
/* 502 */     synchronized (this) {
/* 503 */       deallocs = this.deallocationsTiny + this.deallocationsSmall + this.deallocationsNormal;
/*     */     } 
/* 505 */     return deallocs + this.deallocationsHuge.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long numTinyDeallocations() {
/* 510 */     return this.deallocationsTiny;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long numSmallDeallocations() {
/* 515 */     return this.deallocationsSmall;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long numNormalDeallocations() {
/* 520 */     return this.deallocationsNormal;
/*     */   }
/*     */ 
/*     */   
/*     */   public long numHugeAllocations() {
/* 525 */     return this.allocationsHuge.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public long numHugeDeallocations() {
/* 530 */     return this.deallocationsHuge.value();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long numActiveAllocations() {
/* 536 */     long val = this.allocationsTiny.value() + this.allocationsSmall.value() + this.allocationsHuge.value() - this.deallocationsHuge.value();
/* 537 */     synchronized (this) {
/* 538 */       val += this.allocationsNormal - this.deallocationsTiny + this.deallocationsSmall + this.deallocationsNormal;
/*     */     } 
/* 540 */     return Math.max(val, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long numActiveTinyAllocations() {
/* 545 */     return Math.max(numTinyAllocations() - numTinyDeallocations(), 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long numActiveSmallAllocations() {
/* 550 */     return Math.max(numSmallAllocations() - numSmallDeallocations(), 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long numActiveNormalAllocations() {
/*     */     long val;
/* 556 */     synchronized (this) {
/* 557 */       val = this.allocationsNormal - this.deallocationsNormal;
/*     */     } 
/* 559 */     return Math.max(val, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long numActiveHugeAllocations() {
/* 564 */     return Math.max(numHugeAllocations() - numHugeDeallocations(), 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long numActiveBytes() {
/* 569 */     long val = this.activeBytesHuge.value();
/* 570 */     synchronized (this) {
/* 571 */       for (int i = 0; i < this.chunkListMetrics.size(); i++) {
/* 572 */         for (PoolChunkMetric m : this.chunkListMetrics.get(i)) {
/* 573 */           val += m.chunkSize();
/*     */         }
/*     */       } 
/*     */     } 
/* 577 */     return Math.max(0L, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract PoolChunk<T> newChunk(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract PoolChunk<T> newUnpooledChunk(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract PooledByteBuf<T> newByteBuf(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void memoryCopy(T paramT1, int paramInt1, T paramT2, int paramInt2, int paramInt3);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void destroyChunk(PoolChunk<T> paramPoolChunk);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 613 */     StringBuilder buf = (new StringBuilder()).append("Chunk(s) at 0~25%:").append(StringUtil.NEWLINE).append(this.qInit).append(StringUtil.NEWLINE).append("Chunk(s) at 0~50%:").append(StringUtil.NEWLINE).append(this.q000).append(StringUtil.NEWLINE).append("Chunk(s) at 25~75%:").append(StringUtil.NEWLINE).append(this.q025).append(StringUtil.NEWLINE).append("Chunk(s) at 50~100%:").append(StringUtil.NEWLINE).append(this.q050).append(StringUtil.NEWLINE).append("Chunk(s) at 75~100%:").append(StringUtil.NEWLINE).append(this.q075).append(StringUtil.NEWLINE).append("Chunk(s) at 100%:").append(StringUtil.NEWLINE).append(this.q100).append(StringUtil.NEWLINE).append("tiny subpages:");
/* 614 */     appendPoolSubPages(buf, (PoolSubpage<?>[])this.tinySubpagePools);
/* 615 */     buf.append(StringUtil.NEWLINE)
/* 616 */       .append("small subpages:");
/* 617 */     appendPoolSubPages(buf, (PoolSubpage<?>[])this.smallSubpagePools);
/* 618 */     buf.append(StringUtil.NEWLINE);
/*     */     
/* 620 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static void appendPoolSubPages(StringBuilder buf, PoolSubpage<?>[] subpages) {
/* 624 */     for (int i = 0; i < subpages.length; i++) {
/* 625 */       PoolSubpage<?> head = subpages[i];
/* 626 */       if (head.next != head) {
/*     */ 
/*     */ 
/*     */         
/* 630 */         buf.append(StringUtil.NEWLINE)
/* 631 */           .append(i)
/* 632 */           .append(": ");
/* 633 */         PoolSubpage<?> s = head.next;
/*     */         do {
/* 635 */           buf.append(s);
/* 636 */           s = s.next;
/* 637 */         } while (s != head);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void finalize() throws Throwable {
/*     */     try {
/* 647 */       super.finalize();
/*     */     } finally {
/* 649 */       destroyPoolSubPages((PoolSubpage<?>[])this.smallSubpagePools);
/* 650 */       destroyPoolSubPages((PoolSubpage<?>[])this.tinySubpagePools);
/* 651 */       destroyPoolChunkLists((PoolChunkList<T>[])new PoolChunkList[] { this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100 });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void destroyPoolSubPages(PoolSubpage<?>[] pages) {
/* 656 */     for (PoolSubpage<?> page : pages) {
/* 657 */       page.destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   private void destroyPoolChunkLists(PoolChunkList<T>... chunkLists) {
/* 662 */     for (PoolChunkList<T> chunkList : chunkLists) {
/* 663 */       chunkList.destroy(this);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class HeapArena
/*     */     extends PoolArena<byte[]>
/*     */   {
/*     */     HeapArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int directMemoryCacheAlignment) {
/* 671 */       super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
/*     */     }
/*     */ 
/*     */     
/*     */     private static byte[] newByteArray(int size) {
/* 676 */       return PlatformDependent.allocateUninitializedArray(size);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isDirect() {
/* 681 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected PoolChunk<byte[]> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize) {
/* 686 */       return (PoolChunk)new PoolChunk<byte>(this, newByteArray(chunkSize), pageSize, maxOrder, pageShifts, chunkSize, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PoolChunk<byte[]> newUnpooledChunk(int capacity) {
/* 691 */       return (PoolChunk)new PoolChunk<byte>(this, newByteArray(capacity), capacity, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void destroyChunk(PoolChunk<byte[]> chunk) {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected PooledByteBuf<byte[]> newByteBuf(int maxCapacity) {
/* 701 */       return HAS_UNSAFE ? PooledUnsafeHeapByteBuf.newUnsafeInstance(maxCapacity) : 
/* 702 */         PooledHeapByteBuf.newInstance(maxCapacity);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void memoryCopy(byte[] src, int srcOffset, byte[] dst, int dstOffset, int length) {
/* 707 */       if (length == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 711 */       System.arraycopy(src, srcOffset, dst, dstOffset, length);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DirectArena
/*     */     extends PoolArena<ByteBuffer>
/*     */   {
/*     */     DirectArena(PooledByteBufAllocator parent, int pageSize, int maxOrder, int pageShifts, int chunkSize, int directMemoryCacheAlignment) {
/* 719 */       super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isDirect() {
/* 725 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int offsetCacheLine(ByteBuffer memory) {
/* 731 */       return HAS_UNSAFE ? 
/* 732 */         (int)(PlatformDependent.directBufferAddress(memory) & this.directMemoryCacheAlignmentMask) : 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PoolChunk<ByteBuffer> newChunk(int pageSize, int maxOrder, int pageShifts, int chunkSize) {
/* 738 */       if (this.directMemoryCacheAlignment == 0) {
/* 739 */         return new PoolChunk<ByteBuffer>(this, 
/* 740 */             allocateDirect(chunkSize), pageSize, maxOrder, pageShifts, chunkSize, 0);
/*     */       }
/*     */       
/* 743 */       ByteBuffer memory = allocateDirect(chunkSize + this.directMemoryCacheAlignment);
/*     */       
/* 745 */       return new PoolChunk<ByteBuffer>(this, memory, pageSize, maxOrder, pageShifts, chunkSize, 
/*     */           
/* 747 */           offsetCacheLine(memory));
/*     */     }
/*     */ 
/*     */     
/*     */     protected PoolChunk<ByteBuffer> newUnpooledChunk(int capacity) {
/* 752 */       if (this.directMemoryCacheAlignment == 0) {
/* 753 */         return new PoolChunk<ByteBuffer>(this, 
/* 754 */             allocateDirect(capacity), capacity, 0);
/*     */       }
/* 756 */       ByteBuffer memory = allocateDirect(capacity + this.directMemoryCacheAlignment);
/*     */       
/* 758 */       return new PoolChunk<ByteBuffer>(this, memory, capacity, 
/* 759 */           offsetCacheLine(memory));
/*     */     }
/*     */     
/*     */     private static ByteBuffer allocateDirect(int capacity) {
/* 763 */       return PlatformDependent.useDirectBufferNoCleaner() ? 
/* 764 */         PlatformDependent.allocateDirectNoCleaner(capacity) : ByteBuffer.allocateDirect(capacity);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void destroyChunk(PoolChunk<ByteBuffer> chunk) {
/* 769 */       if (PlatformDependent.useDirectBufferNoCleaner()) {
/* 770 */         PlatformDependent.freeDirectNoCleaner((ByteBuffer)chunk.memory);
/*     */       } else {
/* 772 */         PlatformDependent.freeDirectBuffer((ByteBuffer)chunk.memory);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected PooledByteBuf<ByteBuffer> newByteBuf(int maxCapacity) {
/* 778 */       if (HAS_UNSAFE) {
/* 779 */         return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
/*     */       }
/* 781 */       return PooledDirectByteBuf.newInstance(maxCapacity);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void memoryCopy(ByteBuffer src, int srcOffset, ByteBuffer dst, int dstOffset, int length) {
/* 787 */       if (length == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 791 */       if (HAS_UNSAFE) {
/* 792 */         PlatformDependent.copyMemory(
/* 793 */             PlatformDependent.directBufferAddress(src) + srcOffset, 
/* 794 */             PlatformDependent.directBufferAddress(dst) + dstOffset, length);
/*     */       } else {
/*     */         
/* 797 */         src = src.duplicate();
/* 798 */         dst = dst.duplicate();
/* 799 */         src.position(srcOffset).limit(srcOffset + length);
/* 800 */         dst.position(dstOffset);
/* 801 */         dst.put(src);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolArena.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */