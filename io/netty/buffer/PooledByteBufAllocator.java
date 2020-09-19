/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.NettyRuntime;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class PooledByteBufAllocator
/*     */   extends AbstractByteBufAllocator
/*     */   implements ByteBufAllocatorMetricProvider
/*     */ {
/*  35 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
/*     */   
/*     */   private static final int DEFAULT_NUM_HEAP_ARENA;
/*     */   
/*     */   private static final int DEFAULT_NUM_DIRECT_ARENA;
/*     */   
/*     */   private static final int DEFAULT_PAGE_SIZE;
/*     */   private static final int DEFAULT_MAX_ORDER;
/*     */   private static final int DEFAULT_TINY_CACHE_SIZE;
/*     */   private static final int DEFAULT_SMALL_CACHE_SIZE;
/*     */   private static final int DEFAULT_NORMAL_CACHE_SIZE;
/*     */   private static final int DEFAULT_MAX_CACHED_BUFFER_CAPACITY;
/*     */   private static final int DEFAULT_CACHE_TRIM_INTERVAL;
/*     */   private static final boolean DEFAULT_USE_CACHE_FOR_ALL_THREADS;
/*     */   private static final int DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT;
/*     */   private static final int MIN_PAGE_SIZE = 4096;
/*     */   
/*     */   static {
/*  53 */     int defaultPageSize = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
/*  54 */     Throwable pageSizeFallbackCause = null;
/*     */     try {
/*  56 */       validateAndCalculatePageShifts(defaultPageSize);
/*  57 */     } catch (Throwable t) {
/*  58 */       pageSizeFallbackCause = t;
/*  59 */       defaultPageSize = 8192;
/*     */     } 
/*  61 */     DEFAULT_PAGE_SIZE = defaultPageSize;
/*     */     
/*  63 */     int defaultMaxOrder = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
/*  64 */     Throwable maxOrderFallbackCause = null;
/*     */     try {
/*  66 */       validateAndCalculateChunkSize(DEFAULT_PAGE_SIZE, defaultMaxOrder);
/*  67 */     } catch (Throwable t) {
/*  68 */       maxOrderFallbackCause = t;
/*  69 */       defaultMaxOrder = 11;
/*     */     } 
/*  71 */     DEFAULT_MAX_ORDER = defaultMaxOrder;
/*     */ 
/*     */ 
/*     */     
/*  75 */     Runtime runtime = Runtime.getRuntime();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     int defaultMinNumArena = NettyRuntime.availableProcessors() * 2;
/*  85 */     int defaultChunkSize = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
/*  86 */     DEFAULT_NUM_HEAP_ARENA = Math.max(0, 
/*  87 */         SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", 
/*     */           
/*  89 */           (int)Math.min(defaultMinNumArena, runtime
/*     */             
/*  91 */             .maxMemory() / defaultChunkSize / 2L / 3L)));
/*  92 */     DEFAULT_NUM_DIRECT_ARENA = Math.max(0, 
/*  93 */         SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", 
/*     */           
/*  95 */           (int)Math.min(defaultMinNumArena, 
/*     */             
/*  97 */             PlatformDependent.maxDirectMemory() / defaultChunkSize / 2L / 3L)));
/*     */ 
/*     */     
/* 100 */     DEFAULT_TINY_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.tinyCacheSize", 512);
/* 101 */     DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
/* 102 */     DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
/*     */ 
/*     */ 
/*     */     
/* 106 */     DEFAULT_MAX_CACHED_BUFFER_CAPACITY = SystemPropertyUtil.getInt("io.netty.allocator.maxCachedBufferCapacity", 32768);
/*     */ 
/*     */ 
/*     */     
/* 110 */     DEFAULT_CACHE_TRIM_INTERVAL = SystemPropertyUtil.getInt("io.netty.allocator.cacheTrimInterval", 8192);
/*     */ 
/*     */     
/* 113 */     DEFAULT_USE_CACHE_FOR_ALL_THREADS = SystemPropertyUtil.getBoolean("io.netty.allocator.useCacheForAllThreads", true);
/*     */ 
/*     */     
/* 116 */     DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT = SystemPropertyUtil.getInt("io.netty.allocator.directMemoryCacheAlignment", 0);
/*     */ 
/*     */     
/* 119 */     if (logger.isDebugEnabled()) {
/* 120 */       logger.debug("-Dio.netty.allocator.numHeapArenas: {}", Integer.valueOf(DEFAULT_NUM_HEAP_ARENA));
/* 121 */       logger.debug("-Dio.netty.allocator.numDirectArenas: {}", Integer.valueOf(DEFAULT_NUM_DIRECT_ARENA));
/* 122 */       if (pageSizeFallbackCause == null) {
/* 123 */         logger.debug("-Dio.netty.allocator.pageSize: {}", Integer.valueOf(DEFAULT_PAGE_SIZE));
/*     */       } else {
/* 125 */         logger.debug("-Dio.netty.allocator.pageSize: {}", Integer.valueOf(DEFAULT_PAGE_SIZE), pageSizeFallbackCause);
/*     */       } 
/* 127 */       if (maxOrderFallbackCause == null) {
/* 128 */         logger.debug("-Dio.netty.allocator.maxOrder: {}", Integer.valueOf(DEFAULT_MAX_ORDER));
/*     */       } else {
/* 130 */         logger.debug("-Dio.netty.allocator.maxOrder: {}", Integer.valueOf(DEFAULT_MAX_ORDER), maxOrderFallbackCause);
/*     */       } 
/* 132 */       logger.debug("-Dio.netty.allocator.chunkSize: {}", Integer.valueOf(DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER));
/* 133 */       logger.debug("-Dio.netty.allocator.tinyCacheSize: {}", Integer.valueOf(DEFAULT_TINY_CACHE_SIZE));
/* 134 */       logger.debug("-Dio.netty.allocator.smallCacheSize: {}", Integer.valueOf(DEFAULT_SMALL_CACHE_SIZE));
/* 135 */       logger.debug("-Dio.netty.allocator.normalCacheSize: {}", Integer.valueOf(DEFAULT_NORMAL_CACHE_SIZE));
/* 136 */       logger.debug("-Dio.netty.allocator.maxCachedBufferCapacity: {}", Integer.valueOf(DEFAULT_MAX_CACHED_BUFFER_CAPACITY));
/* 137 */       logger.debug("-Dio.netty.allocator.cacheTrimInterval: {}", Integer.valueOf(DEFAULT_CACHE_TRIM_INTERVAL));
/* 138 */       logger.debug("-Dio.netty.allocator.useCacheForAllThreads: {}", Boolean.valueOf(DEFAULT_USE_CACHE_FOR_ALL_THREADS));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int MAX_CHUNK_SIZE = 1073741824;
/*     */   
/*     */   public static final PooledByteBufAllocator DEFAULT;
/*     */   
/*     */   private final PoolArena<byte[]>[] heapArenas;
/*     */   private final PoolArena<ByteBuffer>[] directArenas;
/*     */   private final int tinyCacheSize;
/*     */   private final int smallCacheSize;
/*     */   
/*     */   public PooledByteBufAllocator() {
/* 157 */     this(false);
/*     */   }
/*     */   private final int normalCacheSize; private final List<PoolArenaMetric> heapArenaMetrics; private final List<PoolArenaMetric> directArenaMetrics; private final PoolThreadLocalCache threadCache; private final int chunkSize; private final PooledByteBufAllocatorMetric metric;
/*     */   
/*     */   public PooledByteBufAllocator(boolean preferDirect) {
/* 162 */     this(preferDirect, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledByteBufAllocator(int nHeapArena, int nDirectArena, int pageSize, int maxOrder) {
/* 167 */     this(false, nHeapArena, nDirectArena, pageSize, maxOrder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder) {
/* 176 */     this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, DEFAULT_TINY_CACHE_SIZE, DEFAULT_SMALL_CACHE_SIZE, DEFAULT_NORMAL_CACHE_SIZE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder, int tinyCacheSize, int smallCacheSize, int normalCacheSize) {
/* 187 */     this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, tinyCacheSize, smallCacheSize, normalCacheSize, DEFAULT_USE_CACHE_FOR_ALL_THREADS, DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder, int tinyCacheSize, int smallCacheSize, int normalCacheSize, boolean useCacheForAllThreads) {
/* 195 */     this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, tinyCacheSize, smallCacheSize, normalCacheSize, useCacheForAllThreads, DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder, int tinyCacheSize, int smallCacheSize, int normalCacheSize, boolean useCacheForAllThreads, int directMemoryCacheAlignment) {
/* 203 */     super(preferDirect);
/* 204 */     this.threadCache = new PoolThreadLocalCache(useCacheForAllThreads);
/* 205 */     this.tinyCacheSize = tinyCacheSize;
/* 206 */     this.smallCacheSize = smallCacheSize;
/* 207 */     this.normalCacheSize = normalCacheSize;
/* 208 */     this.chunkSize = validateAndCalculateChunkSize(pageSize, maxOrder);
/*     */     
/* 210 */     if (nHeapArena < 0) {
/* 211 */       throw new IllegalArgumentException("nHeapArena: " + nHeapArena + " (expected: >= 0)");
/*     */     }
/* 213 */     if (nDirectArena < 0) {
/* 214 */       throw new IllegalArgumentException("nDirectArea: " + nDirectArena + " (expected: >= 0)");
/*     */     }
/*     */     
/* 217 */     if (directMemoryCacheAlignment < 0) {
/* 218 */       throw new IllegalArgumentException("directMemoryCacheAlignment: " + directMemoryCacheAlignment + " (expected: >= 0)");
/*     */     }
/*     */     
/* 221 */     if (directMemoryCacheAlignment > 0 && !isDirectMemoryCacheAlignmentSupported()) {
/* 222 */       throw new IllegalArgumentException("directMemoryCacheAlignment is not supported");
/*     */     }
/*     */     
/* 225 */     if ((directMemoryCacheAlignment & -directMemoryCacheAlignment) != directMemoryCacheAlignment) {
/* 226 */       throw new IllegalArgumentException("directMemoryCacheAlignment: " + directMemoryCacheAlignment + " (expected: power of two)");
/*     */     }
/*     */ 
/*     */     
/* 230 */     int pageShifts = validateAndCalculatePageShifts(pageSize);
/*     */     
/* 232 */     if (nHeapArena > 0) {
/* 233 */       this.heapArenas = newArenaArray(nHeapArena);
/* 234 */       List<PoolArenaMetric> metrics = new ArrayList<PoolArenaMetric>(this.heapArenas.length);
/* 235 */       for (int i = 0; i < this.heapArenas.length; i++) {
/* 236 */         PoolArena.HeapArena arena = new PoolArena.HeapArena(this, pageSize, maxOrder, pageShifts, this.chunkSize, directMemoryCacheAlignment);
/*     */ 
/*     */         
/* 239 */         this.heapArenas[i] = arena;
/* 240 */         metrics.add(arena);
/*     */       } 
/* 242 */       this.heapArenaMetrics = Collections.unmodifiableList(metrics);
/*     */     } else {
/* 244 */       this.heapArenas = null;
/* 245 */       this.heapArenaMetrics = Collections.emptyList();
/*     */     } 
/*     */     
/* 248 */     if (nDirectArena > 0) {
/* 249 */       this.directArenas = newArenaArray(nDirectArena);
/* 250 */       List<PoolArenaMetric> metrics = new ArrayList<PoolArenaMetric>(this.directArenas.length);
/* 251 */       for (int i = 0; i < this.directArenas.length; i++) {
/* 252 */         PoolArena.DirectArena arena = new PoolArena.DirectArena(this, pageSize, maxOrder, pageShifts, this.chunkSize, directMemoryCacheAlignment);
/*     */         
/* 254 */         this.directArenas[i] = arena;
/* 255 */         metrics.add(arena);
/*     */       } 
/* 257 */       this.directArenaMetrics = Collections.unmodifiableList(metrics);
/*     */     } else {
/* 259 */       this.directArenas = null;
/* 260 */       this.directArenaMetrics = Collections.emptyList();
/*     */     } 
/* 262 */     this.metric = new PooledByteBufAllocatorMetric(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> PoolArena<T>[] newArenaArray(int size) {
/* 267 */     return (PoolArena<T>[])new PoolArena[size];
/*     */   }
/*     */   
/*     */   private static int validateAndCalculatePageShifts(int pageSize) {
/* 271 */     if (pageSize < 4096) {
/* 272 */       throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: " + 'က' + ")");
/*     */     }
/*     */     
/* 275 */     if ((pageSize & pageSize - 1) != 0) {
/* 276 */       throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: power of 2)");
/*     */     }
/*     */ 
/*     */     
/* 280 */     return 31 - Integer.numberOfLeadingZeros(pageSize);
/*     */   }
/*     */   
/*     */   private static int validateAndCalculateChunkSize(int pageSize, int maxOrder) {
/* 284 */     if (maxOrder > 14) {
/* 285 */       throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
/*     */     }
/*     */ 
/*     */     
/* 289 */     int chunkSize = pageSize;
/* 290 */     for (int i = maxOrder; i > 0; i--) {
/* 291 */       if (chunkSize > 536870912)
/* 292 */         throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", new Object[] {
/* 293 */                 Integer.valueOf(pageSize), Integer.valueOf(maxOrder), Integer.valueOf(1073741824)
/*     */               })); 
/* 295 */       chunkSize <<= 1;
/*     */     } 
/* 297 */     return chunkSize;
/*     */   }
/*     */   
/*     */   protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
/*     */     ByteBuf buf;
/* 302 */     PoolThreadCache cache = (PoolThreadCache)this.threadCache.get();
/* 303 */     PoolArena<byte[]> heapArena = cache.heapArena;
/*     */ 
/*     */     
/* 306 */     if (heapArena != null) {
/* 307 */       buf = heapArena.allocate(cache, initialCapacity, maxCapacity);
/*     */     } else {
/* 309 */       buf = PlatformDependent.hasUnsafe() ? new UnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new UnpooledHeapByteBuf(this, initialCapacity, maxCapacity);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 314 */     return toLeakAwareBuffer(buf);
/*     */   }
/*     */   
/*     */   protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
/*     */     ByteBuf buf;
/* 319 */     PoolThreadCache cache = (PoolThreadCache)this.threadCache.get();
/* 320 */     PoolArena<ByteBuffer> directArena = cache.directArena;
/*     */ 
/*     */     
/* 323 */     if (directArena != null) {
/* 324 */       buf = directArena.allocate(cache, initialCapacity, maxCapacity);
/*     */     } else {
/*     */       
/* 327 */       buf = PlatformDependent.hasUnsafe() ? UnsafeByteBufUtil.newUnsafeDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
/*     */     } 
/*     */ 
/*     */     
/* 331 */     return toLeakAwareBuffer(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultNumHeapArena() {
/* 338 */     return DEFAULT_NUM_HEAP_ARENA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultNumDirectArena() {
/* 345 */     return DEFAULT_NUM_DIRECT_ARENA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultPageSize() {
/* 352 */     return DEFAULT_PAGE_SIZE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultMaxOrder() {
/* 359 */     return DEFAULT_MAX_ORDER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean defaultUseCacheForAllThreads() {
/* 366 */     return DEFAULT_USE_CACHE_FOR_ALL_THREADS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean defaultPreferDirect() {
/* 373 */     return PlatformDependent.directBufferPreferred();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultTinyCacheSize() {
/* 380 */     return DEFAULT_TINY_CACHE_SIZE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultSmallCacheSize() {
/* 387 */     return DEFAULT_SMALL_CACHE_SIZE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int defaultNormalCacheSize() {
/* 394 */     return DEFAULT_NORMAL_CACHE_SIZE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDirectMemoryCacheAlignmentSupported() {
/* 401 */     return PlatformDependent.hasUnsafe();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirectBufferPooled() {
/* 406 */     return (this.directArenas != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean hasThreadLocalCache() {
/* 415 */     return this.threadCache.isSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void freeThreadLocalCache() {
/* 423 */     this.threadCache.remove();
/*     */   }
/*     */   
/*     */   final class PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache> {
/*     */     private final boolean useCacheForAllThreads;
/*     */     
/*     */     PoolThreadLocalCache(boolean useCacheForAllThreads) {
/* 430 */       this.useCacheForAllThreads = useCacheForAllThreads;
/*     */     }
/*     */ 
/*     */     
/*     */     protected synchronized PoolThreadCache initialValue() {
/* 435 */       PoolArena<byte[]> heapArena = (PoolArena)leastUsedArena((PoolArena[])PooledByteBufAllocator.this.heapArenas);
/* 436 */       PoolArena<ByteBuffer> directArena = leastUsedArena(PooledByteBufAllocator.this.directArenas);
/*     */       
/* 438 */       Thread current = Thread.currentThread();
/* 439 */       if (this.useCacheForAllThreads || current instanceof io.netty.util.concurrent.FastThreadLocalThread) {
/* 440 */         return new PoolThreadCache(heapArena, directArena, PooledByteBufAllocator.this
/* 441 */             .tinyCacheSize, PooledByteBufAllocator.this.smallCacheSize, PooledByteBufAllocator.this.normalCacheSize, PooledByteBufAllocator
/* 442 */             .DEFAULT_MAX_CACHED_BUFFER_CAPACITY, PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
/*     */       }
/*     */       
/* 445 */       return new PoolThreadCache(heapArena, directArena, 0, 0, 0, 0, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onRemoval(PoolThreadCache threadCache) {
/* 450 */       threadCache.free();
/*     */     }
/*     */     
/*     */     private <T> PoolArena<T> leastUsedArena(PoolArena<T>[] arenas) {
/* 454 */       if (arenas == null || arenas.length == 0) {
/* 455 */         return null;
/*     */       }
/*     */       
/* 458 */       PoolArena<T> minArena = arenas[0];
/* 459 */       for (int i = 1; i < arenas.length; i++) {
/* 460 */         PoolArena<T> arena = arenas[i];
/* 461 */         if (arena.numThreadCaches.get() < minArena.numThreadCaches.get()) {
/* 462 */           minArena = arena;
/*     */         }
/*     */       } 
/*     */       
/* 466 */       return minArena;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledByteBufAllocatorMetric metric() {
/* 472 */     return this.metric;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int numHeapArenas() {
/* 482 */     return this.heapArenaMetrics.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int numDirectArenas() {
/* 492 */     return this.directArenaMetrics.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<PoolArenaMetric> heapArenas() {
/* 502 */     return this.heapArenaMetrics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<PoolArenaMetric> directArenas() {
/* 512 */     return this.directArenaMetrics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int numThreadLocalCaches() {
/* 522 */     PoolArena<?>[] arenas = (this.heapArenas != null) ? (PoolArena<?>[])this.heapArenas : (PoolArena<?>[])this.directArenas;
/* 523 */     if (arenas == null) {
/* 524 */       return 0;
/*     */     }
/*     */     
/* 527 */     int total = 0;
/* 528 */     for (PoolArena<?> arena : arenas) {
/* 529 */       total += arena.numThreadCaches.get();
/*     */     }
/*     */     
/* 532 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int tinyCacheSize() {
/* 542 */     return this.tinyCacheSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int smallCacheSize() {
/* 552 */     return this.smallCacheSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int normalCacheSize() {
/* 562 */     return this.normalCacheSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final int chunkSize() {
/* 572 */     return this.chunkSize;
/*     */   }
/*     */   
/*     */   final long usedHeapMemory() {
/* 576 */     return usedMemory((PoolArena<?>[])this.heapArenas);
/*     */   }
/*     */   
/*     */   final long usedDirectMemory() {
/* 580 */     return usedMemory((PoolArena<?>[])this.directArenas);
/*     */   }
/*     */   
/*     */   private static long usedMemory(PoolArena<?>... arenas) {
/* 584 */     if (arenas == null) {
/* 585 */       return -1L;
/*     */     }
/* 587 */     long used = 0L;
/* 588 */     for (PoolArena<?> arena : arenas) {
/* 589 */       used += arena.numActiveBytes();
/* 590 */       if (used < 0L) {
/* 591 */         return Long.MAX_VALUE;
/*     */       }
/*     */     } 
/* 594 */     return used;
/*     */   }
/*     */   
/*     */   final PoolThreadCache threadCache() {
/* 598 */     PoolThreadCache cache = (PoolThreadCache)this.threadCache.get();
/* 599 */     assert cache != null;
/* 600 */     return cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpStats() {
/* 608 */     int heapArenasLen = (this.heapArenas == null) ? 0 : this.heapArenas.length;
/*     */ 
/*     */ 
/*     */     
/* 612 */     StringBuilder buf = (new StringBuilder(512)).append(heapArenasLen).append(" heap arena(s):").append(StringUtil.NEWLINE);
/* 613 */     if (heapArenasLen > 0) {
/* 614 */       for (PoolArena<byte[]> a : this.heapArenas) {
/* 615 */         buf.append(a);
/*     */       }
/*     */     }
/*     */     
/* 619 */     int directArenasLen = (this.directArenas == null) ? 0 : this.directArenas.length;
/*     */     
/* 621 */     buf.append(directArenasLen)
/* 622 */       .append(" direct arena(s):")
/* 623 */       .append(StringUtil.NEWLINE);
/* 624 */     if (directArenasLen > 0) {
/* 625 */       for (PoolArena<ByteBuffer> a : this.directArenas) {
/* 626 */         buf.append(a);
/*     */       }
/*     */     }
/*     */     
/* 630 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PooledByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */