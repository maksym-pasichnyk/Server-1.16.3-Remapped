/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocalThread;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ public final class InternalThreadLocalMap
/*     */   extends UnpaddedInternalThreadLocalMap
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(InternalThreadLocalMap.class);
/*     */   
/*     */   private static final int DEFAULT_ARRAY_LIST_INITIAL_CAPACITY = 8;
/*     */   
/*     */   private static final int STRING_BUILDER_INITIAL_SIZE;
/*     */   private static final int STRING_BUILDER_MAX_SIZE;
/*  47 */   public static final Object UNSET = new Object(); private BitSet cleanerFlags; public long rp1;
/*     */   public long rp2;
/*     */   public long rp3;
/*     */   public long rp4;
/*     */   
/*     */   static {
/*  53 */     STRING_BUILDER_INITIAL_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalMap.stringBuilder.initialSize", 1024);
/*  54 */     logger.debug("-Dio.netty.threadLocalMap.stringBuilder.initialSize: {}", Integer.valueOf(STRING_BUILDER_INITIAL_SIZE));
/*     */     
/*  56 */     STRING_BUILDER_MAX_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalMap.stringBuilder.maxSize", 4096);
/*  57 */     logger.debug("-Dio.netty.threadLocalMap.stringBuilder.maxSize: {}", Integer.valueOf(STRING_BUILDER_MAX_SIZE));
/*     */   }
/*     */   public long rp5; public long rp6; public long rp7; public long rp8; public long rp9;
/*     */   public static InternalThreadLocalMap getIfSet() {
/*  61 */     Thread thread = Thread.currentThread();
/*  62 */     if (thread instanceof FastThreadLocalThread) {
/*  63 */       return ((FastThreadLocalThread)thread).threadLocalMap();
/*     */     }
/*  65 */     return slowThreadLocalMap.get();
/*     */   }
/*     */   
/*     */   public static InternalThreadLocalMap get() {
/*  69 */     Thread thread = Thread.currentThread();
/*  70 */     if (thread instanceof FastThreadLocalThread) {
/*  71 */       return fastGet((FastThreadLocalThread)thread);
/*     */     }
/*  73 */     return slowGet();
/*     */   }
/*     */ 
/*     */   
/*     */   private static InternalThreadLocalMap fastGet(FastThreadLocalThread thread) {
/*  78 */     InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
/*  79 */     if (threadLocalMap == null) {
/*  80 */       thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
/*     */     }
/*  82 */     return threadLocalMap;
/*     */   }
/*     */   
/*     */   private static InternalThreadLocalMap slowGet() {
/*  86 */     ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
/*  87 */     InternalThreadLocalMap ret = slowThreadLocalMap.get();
/*  88 */     if (ret == null) {
/*  89 */       ret = new InternalThreadLocalMap();
/*  90 */       slowThreadLocalMap.set(ret);
/*     */     } 
/*  92 */     return ret;
/*     */   }
/*     */   
/*     */   public static void remove() {
/*  96 */     Thread thread = Thread.currentThread();
/*  97 */     if (thread instanceof FastThreadLocalThread) {
/*  98 */       ((FastThreadLocalThread)thread).setThreadLocalMap(null);
/*     */     } else {
/* 100 */       slowThreadLocalMap.remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void destroy() {
/* 105 */     slowThreadLocalMap.remove();
/*     */   }
/*     */   
/*     */   public static int nextVariableIndex() {
/* 109 */     int index = nextIndex.getAndIncrement();
/* 110 */     if (index < 0) {
/* 111 */       nextIndex.decrementAndGet();
/* 112 */       throw new IllegalStateException("too many thread-local indexed variables");
/*     */     } 
/* 114 */     return index;
/*     */   }
/*     */   
/*     */   public static int lastVariableIndex() {
/* 118 */     return nextIndex.get() - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InternalThreadLocalMap() {
/* 126 */     super(newIndexedVariableTable());
/*     */   }
/*     */   
/*     */   private static Object[] newIndexedVariableTable() {
/* 130 */     Object[] array = new Object[32];
/* 131 */     Arrays.fill(array, UNSET);
/* 132 */     return array;
/*     */   }
/*     */   
/*     */   public int size() {
/* 136 */     int count = 0;
/*     */     
/* 138 */     if (this.futureListenerStackDepth != 0) {
/* 139 */       count++;
/*     */     }
/* 141 */     if (this.localChannelReaderStackDepth != 0) {
/* 142 */       count++;
/*     */     }
/* 144 */     if (this.handlerSharableCache != null) {
/* 145 */       count++;
/*     */     }
/* 147 */     if (this.counterHashCode != null) {
/* 148 */       count++;
/*     */     }
/* 150 */     if (this.random != null) {
/* 151 */       count++;
/*     */     }
/* 153 */     if (this.typeParameterMatcherGetCache != null) {
/* 154 */       count++;
/*     */     }
/* 156 */     if (this.typeParameterMatcherFindCache != null) {
/* 157 */       count++;
/*     */     }
/* 159 */     if (this.stringBuilder != null) {
/* 160 */       count++;
/*     */     }
/* 162 */     if (this.charsetEncoderCache != null) {
/* 163 */       count++;
/*     */     }
/* 165 */     if (this.charsetDecoderCache != null) {
/* 166 */       count++;
/*     */     }
/* 168 */     if (this.arrayList != null) {
/* 169 */       count++;
/*     */     }
/*     */     
/* 172 */     for (Object o : this.indexedVariables) {
/* 173 */       if (o != UNSET) {
/* 174 */         count++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 180 */     return count - 1;
/*     */   }
/*     */   
/*     */   public StringBuilder stringBuilder() {
/* 184 */     StringBuilder sb = this.stringBuilder;
/* 185 */     if (sb == null) {
/* 186 */       return this.stringBuilder = new StringBuilder(STRING_BUILDER_INITIAL_SIZE);
/*     */     }
/* 188 */     if (sb.capacity() > STRING_BUILDER_MAX_SIZE) {
/* 189 */       sb.setLength(STRING_BUILDER_INITIAL_SIZE);
/* 190 */       sb.trimToSize();
/*     */     } 
/* 192 */     sb.setLength(0);
/* 193 */     return sb;
/*     */   }
/*     */   
/*     */   public Map<Charset, CharsetEncoder> charsetEncoderCache() {
/* 197 */     Map<Charset, CharsetEncoder> cache = this.charsetEncoderCache;
/* 198 */     if (cache == null) {
/* 199 */       this.charsetEncoderCache = cache = new IdentityHashMap<Charset, CharsetEncoder>();
/*     */     }
/* 201 */     return cache;
/*     */   }
/*     */   
/*     */   public Map<Charset, CharsetDecoder> charsetDecoderCache() {
/* 205 */     Map<Charset, CharsetDecoder> cache = this.charsetDecoderCache;
/* 206 */     if (cache == null) {
/* 207 */       this.charsetDecoderCache = cache = new IdentityHashMap<Charset, CharsetDecoder>();
/*     */     }
/* 209 */     return cache;
/*     */   }
/*     */   
/*     */   public <E> ArrayList<E> arrayList() {
/* 213 */     return arrayList(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E> ArrayList<E> arrayList(int minCapacity) {
/* 218 */     ArrayList<Object> arrayList = this.arrayList;
/* 219 */     if (arrayList == null) {
/* 220 */       this.arrayList = new ArrayList(minCapacity);
/* 221 */       return (ArrayList)this.arrayList;
/*     */     } 
/* 223 */     arrayList.clear();
/* 224 */     arrayList.ensureCapacity(minCapacity);
/* 225 */     return (ArrayList)arrayList;
/*     */   }
/*     */   
/*     */   public int futureListenerStackDepth() {
/* 229 */     return this.futureListenerStackDepth;
/*     */   }
/*     */   
/*     */   public void setFutureListenerStackDepth(int futureListenerStackDepth) {
/* 233 */     this.futureListenerStackDepth = futureListenerStackDepth;
/*     */   }
/*     */   
/*     */   public ThreadLocalRandom random() {
/* 237 */     ThreadLocalRandom r = this.random;
/* 238 */     if (r == null) {
/* 239 */       this.random = r = new ThreadLocalRandom();
/*     */     }
/* 241 */     return r;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
/* 245 */     Map<Class<?>, TypeParameterMatcher> cache = this.typeParameterMatcherGetCache;
/* 246 */     if (cache == null) {
/* 247 */       this.typeParameterMatcherGetCache = cache = new IdentityHashMap<Class<?>, TypeParameterMatcher>();
/*     */     }
/* 249 */     return cache;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
/* 253 */     Map<Class<?>, Map<String, TypeParameterMatcher>> cache = this.typeParameterMatcherFindCache;
/* 254 */     if (cache == null) {
/* 255 */       this.typeParameterMatcherFindCache = cache = new IdentityHashMap<Class<?>, Map<String, TypeParameterMatcher>>();
/*     */     }
/* 257 */     return cache;
/*     */   }
/*     */   
/*     */   public IntegerHolder counterHashCode() {
/* 261 */     return this.counterHashCode;
/*     */   }
/*     */   
/*     */   public void setCounterHashCode(IntegerHolder counterHashCode) {
/* 265 */     this.counterHashCode = counterHashCode;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, Boolean> handlerSharableCache() {
/* 269 */     Map<Class<?>, Boolean> cache = this.handlerSharableCache;
/* 270 */     if (cache == null)
/*     */     {
/* 272 */       this.handlerSharableCache = cache = new WeakHashMap<Class<?>, Boolean>(4);
/*     */     }
/* 274 */     return cache;
/*     */   }
/*     */   
/*     */   public int localChannelReaderStackDepth() {
/* 278 */     return this.localChannelReaderStackDepth;
/*     */   }
/*     */   
/*     */   public void setLocalChannelReaderStackDepth(int localChannelReaderStackDepth) {
/* 282 */     this.localChannelReaderStackDepth = localChannelReaderStackDepth;
/*     */   }
/*     */   
/*     */   public Object indexedVariable(int index) {
/* 286 */     Object[] lookup = this.indexedVariables;
/* 287 */     return (index < lookup.length) ? lookup[index] : UNSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setIndexedVariable(int index, Object value) {
/* 294 */     Object[] lookup = this.indexedVariables;
/* 295 */     if (index < lookup.length) {
/* 296 */       Object oldValue = lookup[index];
/* 297 */       lookup[index] = value;
/* 298 */       return (oldValue == UNSET);
/*     */     } 
/* 300 */     expandIndexedVariableTableAndSet(index, value);
/* 301 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void expandIndexedVariableTableAndSet(int index, Object value) {
/* 306 */     Object[] oldArray = this.indexedVariables;
/* 307 */     int oldCapacity = oldArray.length;
/* 308 */     int newCapacity = index;
/* 309 */     newCapacity |= newCapacity >>> 1;
/* 310 */     newCapacity |= newCapacity >>> 2;
/* 311 */     newCapacity |= newCapacity >>> 4;
/* 312 */     newCapacity |= newCapacity >>> 8;
/* 313 */     newCapacity |= newCapacity >>> 16;
/* 314 */     newCapacity++;
/*     */     
/* 316 */     Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
/* 317 */     Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
/* 318 */     newArray[index] = value;
/* 319 */     this.indexedVariables = newArray;
/*     */   }
/*     */   
/*     */   public Object removeIndexedVariable(int index) {
/* 323 */     Object[] lookup = this.indexedVariables;
/* 324 */     if (index < lookup.length) {
/* 325 */       Object v = lookup[index];
/* 326 */       lookup[index] = UNSET;
/* 327 */       return v;
/*     */     } 
/* 329 */     return UNSET;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndexedVariableSet(int index) {
/* 334 */     Object[] lookup = this.indexedVariables;
/* 335 */     return (index < lookup.length && lookup[index] != UNSET);
/*     */   }
/*     */   
/*     */   public boolean isCleanerFlagSet(int index) {
/* 339 */     return (this.cleanerFlags != null && this.cleanerFlags.get(index));
/*     */   }
/*     */   
/*     */   public void setCleanerFlag(int index) {
/* 343 */     if (this.cleanerFlags == null) {
/* 344 */       this.cleanerFlags = new BitSet();
/*     */     }
/* 346 */     this.cleanerFlags.set(index);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\InternalThreadLocalMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */