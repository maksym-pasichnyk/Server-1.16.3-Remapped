/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PoolThreadCache
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
/*     */   
/*     */   final PoolArena<byte[]> heapArena;
/*     */   
/*     */   final PoolArena<ByteBuffer> directArena;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
/*     */   
/*     */   private final MemoryRegionCache<byte[]>[] normalHeapCaches;
/*     */   
/*     */   private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
/*     */   
/*     */   private final int numShiftsNormalDirect;
/*     */   
/*     */   private final int numShiftsNormalHeap;
/*     */   
/*     */   private final int freeSweepAllocationThreshold;
/*     */   private int allocations;
/*     */   
/*     */   PoolThreadCache(PoolArena<byte[]> heapArena, PoolArena<ByteBuffer> directArena, int tinyCacheSize, int smallCacheSize, int normalCacheSize, int maxCachedBufferCapacity, int freeSweepAllocationThreshold) {
/*  66 */     if (maxCachedBufferCapacity < 0) {
/*  67 */       throw new IllegalArgumentException("maxCachedBufferCapacity: " + maxCachedBufferCapacity + " (expected: >= 0)");
/*     */     }
/*     */     
/*  70 */     this.freeSweepAllocationThreshold = freeSweepAllocationThreshold;
/*  71 */     this.heapArena = heapArena;
/*  72 */     this.directArena = directArena;
/*  73 */     if (directArena != null) {
/*  74 */       this.tinySubPageDirectCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
/*     */       
/*  76 */       this.smallSubPageDirectCaches = createSubPageCaches(smallCacheSize, directArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
/*     */ 
/*     */       
/*  79 */       this.numShiftsNormalDirect = log2(directArena.pageSize);
/*  80 */       this.normalDirectCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, directArena);
/*     */ 
/*     */       
/*  83 */       directArena.numThreadCaches.getAndIncrement();
/*     */     } else {
/*     */       
/*  86 */       this.tinySubPageDirectCaches = null;
/*  87 */       this.smallSubPageDirectCaches = null;
/*  88 */       this.normalDirectCaches = null;
/*  89 */       this.numShiftsNormalDirect = -1;
/*     */     } 
/*  91 */     if (heapArena != null) {
/*     */       
/*  93 */       this.tinySubPageHeapCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
/*     */       
/*  95 */       this.smallSubPageHeapCaches = createSubPageCaches(smallCacheSize, heapArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
/*     */ 
/*     */       
/*  98 */       this.numShiftsNormalHeap = log2(heapArena.pageSize);
/*  99 */       this.normalHeapCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, (PoolArena)heapArena);
/*     */ 
/*     */       
/* 102 */       heapArena.numThreadCaches.getAndIncrement();
/*     */     } else {
/*     */       
/* 105 */       this.tinySubPageHeapCaches = null;
/* 106 */       this.smallSubPageHeapCaches = null;
/* 107 */       this.normalHeapCaches = null;
/* 108 */       this.numShiftsNormalHeap = -1;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     if ((this.tinySubPageDirectCaches != null || this.smallSubPageDirectCaches != null || this.normalDirectCaches != null || this.tinySubPageHeapCaches != null || this.smallSubPageHeapCaches != null || this.normalHeapCaches != null) && freeSweepAllocationThreshold < 1)
/*     */     {
/*     */       
/* 115 */       throw new IllegalArgumentException("freeSweepAllocationThreshold: " + freeSweepAllocationThreshold + " (expected: > 0)");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> MemoryRegionCache<T>[] createSubPageCaches(int cacheSize, int numCaches, PoolArena.SizeClass sizeClass) {
/* 122 */     if (cacheSize > 0 && numCaches > 0) {
/*     */       
/* 124 */       MemoryRegionCache[] arrayOfMemoryRegionCache = new MemoryRegionCache[numCaches];
/* 125 */       for (int i = 0; i < arrayOfMemoryRegionCache.length; i++)
/*     */       {
/* 127 */         arrayOfMemoryRegionCache[i] = new SubPageMemoryRegionCache(cacheSize, sizeClass);
/*     */       }
/* 129 */       return (MemoryRegionCache<T>[])arrayOfMemoryRegionCache;
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> MemoryRegionCache<T>[] createNormalCaches(int cacheSize, int maxCachedBufferCapacity, PoolArena<T> area) {
/* 137 */     if (cacheSize > 0 && maxCachedBufferCapacity > 0) {
/* 138 */       int max = Math.min(area.chunkSize, maxCachedBufferCapacity);
/* 139 */       int arraySize = Math.max(1, log2(max / area.pageSize) + 1);
/*     */ 
/*     */       
/* 142 */       MemoryRegionCache[] arrayOfMemoryRegionCache = new MemoryRegionCache[arraySize];
/* 143 */       for (int i = 0; i < arrayOfMemoryRegionCache.length; i++) {
/* 144 */         arrayOfMemoryRegionCache[i] = new NormalMemoryRegionCache(cacheSize);
/*     */       }
/* 146 */       return (MemoryRegionCache<T>[])arrayOfMemoryRegionCache;
/*     */     } 
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int log2(int val) {
/* 153 */     int res = 0;
/* 154 */     while (val > 1) {
/* 155 */       val >>= 1;
/* 156 */       res++;
/*     */     } 
/* 158 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean allocateTiny(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity) {
/* 165 */     return allocate(cacheForTiny(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean allocateSmall(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity) {
/* 172 */     return allocate(cacheForSmall(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean allocateNormal(PoolArena<?> area, PooledByteBuf<?> buf, int reqCapacity, int normCapacity) {
/* 179 */     return allocate(cacheForNormal(area, normCapacity), buf, reqCapacity);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean allocate(MemoryRegionCache<?> cache, PooledByteBuf<?> buf, int reqCapacity) {
/* 184 */     if (cache == null)
/*     */     {
/* 186 */       return false;
/*     */     }
/* 188 */     boolean allocated = cache.allocate(buf, reqCapacity);
/* 189 */     if (++this.allocations >= this.freeSweepAllocationThreshold) {
/* 190 */       this.allocations = 0;
/* 191 */       trim();
/*     */     } 
/* 193 */     return allocated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean add(PoolArena<?> area, PoolChunk<?> chunk, long handle, int normCapacity, PoolArena.SizeClass sizeClass) {
/* 202 */     MemoryRegionCache<?> cache = cache(area, normCapacity, sizeClass);
/* 203 */     if (cache == null) {
/* 204 */       return false;
/*     */     }
/* 206 */     return cache.add(chunk, handle);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cache(PoolArena<?> area, int normCapacity, PoolArena.SizeClass sizeClass) {
/* 210 */     switch (sizeClass) {
/*     */       case Normal:
/* 212 */         return cacheForNormal(area, normCapacity);
/*     */       case Small:
/* 214 */         return cacheForSmall(area, normCapacity);
/*     */       case Tiny:
/* 216 */         return cacheForTiny(area, normCapacity);
/*     */     } 
/* 218 */     throw new Error();
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
/*     */   void free() {
/* 231 */     int numFreed = free((MemoryRegionCache<?>[])this.tinySubPageDirectCaches) + free((MemoryRegionCache<?>[])this.smallSubPageDirectCaches) + free((MemoryRegionCache<?>[])this.normalDirectCaches) + free((MemoryRegionCache<?>[])this.tinySubPageHeapCaches) + free((MemoryRegionCache<?>[])this.smallSubPageHeapCaches) + free((MemoryRegionCache<?>[])this.normalHeapCaches);
/*     */     
/* 233 */     if (numFreed > 0 && logger.isDebugEnabled()) {
/* 234 */       logger.debug("Freed {} thread-local buffer(s) from thread: {}", Integer.valueOf(numFreed), Thread.currentThread().getName());
/*     */     }
/*     */     
/* 237 */     if (this.directArena != null) {
/* 238 */       this.directArena.numThreadCaches.getAndDecrement();
/*     */     }
/*     */     
/* 241 */     if (this.heapArena != null) {
/* 242 */       this.heapArena.numThreadCaches.getAndDecrement();
/*     */     }
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?>[] caches) {
/* 247 */     if (caches == null) {
/* 248 */       return 0;
/*     */     }
/*     */     
/* 251 */     int numFreed = 0;
/* 252 */     for (MemoryRegionCache<?> c : caches) {
/* 253 */       numFreed += free(c);
/*     */     }
/* 255 */     return numFreed;
/*     */   }
/*     */   
/*     */   private static int free(MemoryRegionCache<?> cache) {
/* 259 */     if (cache == null) {
/* 260 */       return 0;
/*     */     }
/* 262 */     return cache.free();
/*     */   }
/*     */   
/*     */   void trim() {
/* 266 */     trim((MemoryRegionCache<?>[])this.tinySubPageDirectCaches);
/* 267 */     trim((MemoryRegionCache<?>[])this.smallSubPageDirectCaches);
/* 268 */     trim((MemoryRegionCache<?>[])this.normalDirectCaches);
/* 269 */     trim((MemoryRegionCache<?>[])this.tinySubPageHeapCaches);
/* 270 */     trim((MemoryRegionCache<?>[])this.smallSubPageHeapCaches);
/* 271 */     trim((MemoryRegionCache<?>[])this.normalHeapCaches);
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?>[] caches) {
/* 275 */     if (caches == null) {
/*     */       return;
/*     */     }
/* 278 */     for (MemoryRegionCache<?> c : caches) {
/* 279 */       trim(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void trim(MemoryRegionCache<?> cache) {
/* 284 */     if (cache == null) {
/*     */       return;
/*     */     }
/* 287 */     cache.trim();
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForTiny(PoolArena<?> area, int normCapacity) {
/* 291 */     int idx = PoolArena.tinyIdx(normCapacity);
/* 292 */     if (area.isDirect()) {
/* 293 */       return cache((MemoryRegionCache<?>[])this.tinySubPageDirectCaches, idx);
/*     */     }
/* 295 */     return cache((MemoryRegionCache<?>[])this.tinySubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForSmall(PoolArena<?> area, int normCapacity) {
/* 299 */     int idx = PoolArena.smallIdx(normCapacity);
/* 300 */     if (area.isDirect()) {
/* 301 */       return cache((MemoryRegionCache<?>[])this.smallSubPageDirectCaches, idx);
/*     */     }
/* 303 */     return cache((MemoryRegionCache<?>[])this.smallSubPageHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private MemoryRegionCache<?> cacheForNormal(PoolArena<?> area, int normCapacity) {
/* 307 */     if (area.isDirect()) {
/* 308 */       int i = log2(normCapacity >> this.numShiftsNormalDirect);
/* 309 */       return cache((MemoryRegionCache<?>[])this.normalDirectCaches, i);
/*     */     } 
/* 311 */     int idx = log2(normCapacity >> this.numShiftsNormalHeap);
/* 312 */     return cache((MemoryRegionCache<?>[])this.normalHeapCaches, idx);
/*     */   }
/*     */   
/*     */   private static <T> MemoryRegionCache<T> cache(MemoryRegionCache<T>[] cache, int idx) {
/* 316 */     if (cache == null || idx > cache.length - 1) {
/* 317 */       return null;
/*     */     }
/* 319 */     return cache[idx];
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class SubPageMemoryRegionCache<T>
/*     */     extends MemoryRegionCache<T>
/*     */   {
/*     */     SubPageMemoryRegionCache(int size, PoolArena.SizeClass sizeClass) {
/* 327 */       super(size, sizeClass);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity) {
/* 333 */       chunk.initBufWithSubpage(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class NormalMemoryRegionCache<T>
/*     */     extends MemoryRegionCache<T>
/*     */   {
/*     */     NormalMemoryRegionCache(int size) {
/* 342 */       super(size, PoolArena.SizeClass.Normal);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void initBuf(PoolChunk<T> chunk, long handle, PooledByteBuf<T> buf, int reqCapacity) {
/* 348 */       chunk.initBuf(buf, handle, reqCapacity);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class MemoryRegionCache<T> {
/*     */     private final int size;
/*     */     private final Queue<Entry<T>> queue;
/*     */     private final PoolArena.SizeClass sizeClass;
/*     */     private int allocations;
/*     */     
/*     */     MemoryRegionCache(int size, PoolArena.SizeClass sizeClass) {
/* 359 */       this.size = MathUtil.safeFindNextPositivePowerOfTwo(size);
/* 360 */       this.queue = PlatformDependent.newFixedMpscQueue(this.size);
/* 361 */       this.sizeClass = sizeClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void initBuf(PoolChunk<T> param1PoolChunk, long param1Long, PooledByteBuf<T> param1PooledByteBuf, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean add(PoolChunk<T> chunk, long handle) {
/* 375 */       Entry<T> entry = newEntry(chunk, handle);
/* 376 */       boolean queued = this.queue.offer(entry);
/* 377 */       if (!queued)
/*     */       {
/* 379 */         entry.recycle();
/*     */       }
/*     */       
/* 382 */       return queued;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final boolean allocate(PooledByteBuf<T> buf, int reqCapacity) {
/* 389 */       Entry<T> entry = this.queue.poll();
/* 390 */       if (entry == null) {
/* 391 */         return false;
/*     */       }
/* 393 */       initBuf(entry.chunk, entry.handle, buf, reqCapacity);
/* 394 */       entry.recycle();
/*     */ 
/*     */       
/* 397 */       this.allocations++;
/* 398 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final int free() {
/* 405 */       return free(2147483647);
/*     */     }
/*     */     
/*     */     private int free(int max) {
/* 409 */       int numFreed = 0;
/* 410 */       for (; numFreed < max; numFreed++) {
/* 411 */         Entry<T> entry = this.queue.poll();
/* 412 */         if (entry != null) {
/* 413 */           freeEntry(entry);
/*     */         } else {
/*     */           
/* 416 */           return numFreed;
/*     */         } 
/*     */       } 
/* 419 */       return numFreed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void trim() {
/* 426 */       int free = this.size - this.allocations;
/* 427 */       this.allocations = 0;
/*     */ 
/*     */       
/* 430 */       if (free > 0) {
/* 431 */         free(free);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private void freeEntry(Entry entry) {
/* 437 */       PoolChunk chunk = entry.chunk;
/* 438 */       long handle = entry.handle;
/*     */ 
/*     */       
/* 441 */       entry.recycle();
/*     */       
/* 443 */       chunk.arena.freeChunk(chunk, handle, this.sizeClass);
/*     */     }
/*     */     
/*     */     static final class Entry<T> {
/*     */       final Recycler.Handle<Entry<?>> recyclerHandle;
/*     */       PoolChunk<T> chunk;
/* 449 */       long handle = -1L;
/*     */       
/*     */       Entry(Recycler.Handle<Entry<?>> recyclerHandle) {
/* 452 */         this.recyclerHandle = recyclerHandle;
/*     */       }
/*     */       
/*     */       void recycle() {
/* 456 */         this.chunk = null;
/* 457 */         this.handle = -1L;
/* 458 */         this.recyclerHandle.recycle(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private static Entry newEntry(PoolChunk<?> chunk, long handle) {
/* 464 */       Entry entry = (Entry)RECYCLER.get();
/* 465 */       entry.chunk = chunk;
/* 466 */       entry.handle = handle;
/* 467 */       return entry;
/*     */     }
/*     */ 
/*     */     
/* 471 */     private static final Recycler<Entry> RECYCLER = new Recycler<Entry>()
/*     */       {
/*     */         protected PoolThreadCache.MemoryRegionCache.Entry newObject(Recycler.Handle<PoolThreadCache.MemoryRegionCache.Entry> handle)
/*     */         {
/* 475 */           return new PoolThreadCache.MemoryRegionCache.Entry((Recycler.Handle)handle);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolThreadCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */