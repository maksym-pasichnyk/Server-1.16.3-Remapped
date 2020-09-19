/*      */ package io.netty.util.internal;
/*      */ 
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscChunkedArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscGrowableAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.SpscLinkedAtomicQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.LinkedBlockingDeque;
/*      */ import java.util.concurrent.ThreadLocalRandom;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PlatformDependent
/*      */ {
/*   69 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
/*      */   
/*   71 */   private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
/*      */ 
/*      */   
/*   74 */   private static final boolean IS_WINDOWS = isWindows0();
/*   75 */   private static final boolean IS_OSX = isOsx0();
/*      */   
/*      */   private static final boolean MAYBE_SUPER_USER;
/*      */   
/*   79 */   private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
/*      */   
/*   81 */   private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause0();
/*   82 */   private static final boolean DIRECT_BUFFER_PREFERRED = (UNSAFE_UNAVAILABILITY_CAUSE == null && 
/*   83 */     !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
/*   84 */   private static final long MAX_DIRECT_MEMORY = maxDirectMemory0();
/*      */   
/*      */   private static final int MPSC_CHUNK_SIZE = 1024;
/*      */   
/*      */   private static final int MIN_MAX_MPSC_CAPACITY = 2048;
/*      */   private static final int MAX_ALLOWED_MPSC_CAPACITY = 1073741824;
/*   90 */   private static final long BYTE_ARRAY_BASE_OFFSET = byteArrayBaseOffset0();
/*      */   
/*   92 */   private static final File TMPDIR = tmpdir0();
/*      */   
/*   94 */   private static final int BIT_MODE = bitMode0();
/*   95 */   private static final String NORMALIZED_ARCH = normalizeArch(SystemPropertyUtil.get("os.arch", ""));
/*   96 */   private static final String NORMALIZED_OS = normalizeOs(SystemPropertyUtil.get("os.name", ""));
/*      */   
/*   98 */   private static final int ADDRESS_SIZE = addressSize0();
/*      */   
/*      */   private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
/*      */   private static final AtomicLong DIRECT_MEMORY_COUNTER;
/*      */   private static final long DIRECT_MEMORY_LIMIT;
/*      */   private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
/*      */   private static final Cleaner CLEANER;
/*      */   private static final int UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD;
/*  106 */   public static final boolean BIG_ENDIAN_NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
/*      */   
/*  108 */   private static final Cleaner NOOP = new Cleaner()
/*      */     {
/*      */       public void freeDirectBuffer(ByteBuffer buffer) {}
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  116 */     if (javaVersion() >= 7) {
/*  117 */       RANDOM_PROVIDER = new ThreadLocalRandomProvider()
/*      */         {
/*      */           public Random current() {
/*  120 */             return ThreadLocalRandom.current();
/*      */           }
/*      */         };
/*      */     } else {
/*  124 */       RANDOM_PROVIDER = new ThreadLocalRandomProvider()
/*      */         {
/*      */           public Random current() {
/*  127 */             return ThreadLocalRandom.current();
/*      */           }
/*      */         };
/*      */     } 
/*  131 */     if (logger.isDebugEnabled()) {
/*  132 */       logger.debug("-Dio.netty.noPreferDirect: {}", Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  139 */     if (!hasUnsafe() && !isAndroid() && !PlatformDependent0.isExplicitNoUnsafe()) {
/*  140 */       logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system instability.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  153 */     long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1L);
/*      */     
/*  155 */     if (maxDirectMemory == 0L || !hasUnsafe() || !PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
/*  156 */       USE_DIRECT_BUFFER_NO_CLEANER = false;
/*  157 */       DIRECT_MEMORY_COUNTER = null;
/*      */     } else {
/*  159 */       USE_DIRECT_BUFFER_NO_CLEANER = true;
/*  160 */       if (maxDirectMemory < 0L) {
/*  161 */         maxDirectMemory = maxDirectMemory0();
/*  162 */         if (maxDirectMemory <= 0L) {
/*  163 */           DIRECT_MEMORY_COUNTER = null;
/*      */         } else {
/*  165 */           DIRECT_MEMORY_COUNTER = new AtomicLong();
/*      */         } 
/*      */       } else {
/*  168 */         DIRECT_MEMORY_COUNTER = new AtomicLong();
/*      */       } 
/*      */     } 
/*  171 */     DIRECT_MEMORY_LIMIT = maxDirectMemory;
/*  172 */     logger.debug("-Dio.netty.maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
/*      */ 
/*      */     
/*  175 */     int tryAllocateUninitializedArray = SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
/*  176 */     UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = (javaVersion() >= 9 && PlatformDependent0.hasAllocateArrayMethod()) ? tryAllocateUninitializedArray : -1;
/*      */     
/*  178 */     logger.debug("-Dio.netty.uninitializedArrayAllocationThreshold: {}", Integer.valueOf(UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD));
/*      */     
/*  180 */     MAYBE_SUPER_USER = maybeSuperUser0();
/*      */     
/*  182 */     if (!isAndroid() && hasUnsafe()) {
/*      */ 
/*      */       
/*  185 */       if (javaVersion() >= 9) {
/*  186 */         CLEANER = CleanerJava9.isSupported() ? new CleanerJava9() : NOOP;
/*      */       } else {
/*  188 */         CLEANER = CleanerJava6.isSupported() ? new CleanerJava6() : NOOP;
/*      */       } 
/*      */     } else {
/*  191 */       CLEANER = NOOP;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean hasDirectBufferNoCleanerConstructor() {
/*  196 */     return PlatformDependent0.hasDirectBufferNoCleanerConstructor();
/*      */   }
/*      */   
/*      */   public static byte[] allocateUninitializedArray(int size) {
/*  200 */     return (UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD < 0 || UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD > size) ? new byte[size] : 
/*  201 */       PlatformDependent0.allocateUninitializedArray(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAndroid() {
/*  208 */     return PlatformDependent0.isAndroid();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isWindows() {
/*  215 */     return IS_WINDOWS;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isOsx() {
/*  222 */     return IS_OSX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean maybeSuperUser() {
/*  230 */     return MAYBE_SUPER_USER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int javaVersion() {
/*  237 */     return PlatformDependent0.javaVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean canEnableTcpNoDelayByDefault() {
/*  244 */     return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasUnsafe() {
/*  252 */     return (UNSAFE_UNAVAILABILITY_CAUSE == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Throwable getUnsafeUnavailabilityCause() {
/*  259 */     return UNSAFE_UNAVAILABILITY_CAUSE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUnaligned() {
/*  268 */     return PlatformDependent0.isUnaligned();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean directBufferPreferred() {
/*  276 */     return DIRECT_BUFFER_PREFERRED;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long maxDirectMemory() {
/*  283 */     return MAX_DIRECT_MEMORY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File tmpdir() {
/*  290 */     return TMPDIR;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int bitMode() {
/*  297 */     return BIT_MODE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int addressSize() {
/*  305 */     return ADDRESS_SIZE;
/*      */   }
/*      */   
/*      */   public static long allocateMemory(long size) {
/*  309 */     return PlatformDependent0.allocateMemory(size);
/*      */   }
/*      */   
/*      */   public static void freeMemory(long address) {
/*  313 */     PlatformDependent0.freeMemory(address);
/*      */   }
/*      */   
/*      */   public static long reallocateMemory(long address, long newSize) {
/*  317 */     return PlatformDependent0.reallocateMemory(address, newSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void throwException(Throwable t) {
/*  324 */     if (hasUnsafe()) {
/*  325 */       PlatformDependent0.throwException(t);
/*      */     } else {
/*  327 */       throwException0(t);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E extends Throwable> void throwException0(Throwable t) throws E {
/*  333 */     throw (E)t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
/*  340 */     return new ConcurrentHashMap<K, V>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LongCounter newLongCounter() {
/*  347 */     if (javaVersion() >= 8) {
/*  348 */       return new LongAdderCounter();
/*      */     }
/*  350 */     return new AtomicLongCounter();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity) {
/*  358 */     return new ConcurrentHashMap<K, V>(initialCapacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor) {
/*  365 */     return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
/*  373 */     return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor, concurrencyLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
/*  380 */     return new ConcurrentHashMap<K, V>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void freeDirectBuffer(ByteBuffer buffer) {
/*  388 */     CLEANER.freeDirectBuffer(buffer);
/*      */   }
/*      */   
/*      */   public static long directBufferAddress(ByteBuffer buffer) {
/*  392 */     return PlatformDependent0.directBufferAddress(buffer);
/*      */   }
/*      */   
/*      */   public static ByteBuffer directBuffer(long memoryAddress, int size) {
/*  396 */     if (PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
/*  397 */       return PlatformDependent0.newDirectBuffer(memoryAddress, size);
/*      */     }
/*  399 */     throw new UnsupportedOperationException("sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available");
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getInt(Object object, long fieldOffset) {
/*  404 */     return PlatformDependent0.getInt(object, fieldOffset);
/*      */   }
/*      */   
/*      */   public static byte getByte(long address) {
/*  408 */     return PlatformDependent0.getByte(address);
/*      */   }
/*      */   
/*      */   public static short getShort(long address) {
/*  412 */     return PlatformDependent0.getShort(address);
/*      */   }
/*      */   
/*      */   public static int getInt(long address) {
/*  416 */     return PlatformDependent0.getInt(address);
/*      */   }
/*      */   
/*      */   public static long getLong(long address) {
/*  420 */     return PlatformDependent0.getLong(address);
/*      */   }
/*      */   
/*      */   public static byte getByte(byte[] data, int index) {
/*  424 */     return PlatformDependent0.getByte(data, index);
/*      */   }
/*      */   
/*      */   public static short getShort(byte[] data, int index) {
/*  428 */     return PlatformDependent0.getShort(data, index);
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] data, int index) {
/*  432 */     return PlatformDependent0.getInt(data, index);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] data, int index) {
/*  436 */     return PlatformDependent0.getLong(data, index);
/*      */   }
/*      */   
/*      */   private static long getLongSafe(byte[] bytes, int offset) {
/*  440 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  441 */       return bytes[offset] << 56L | (bytes[offset + 1] & 0xFFL) << 48L | (bytes[offset + 2] & 0xFFL) << 40L | (bytes[offset + 3] & 0xFFL) << 32L | (bytes[offset + 4] & 0xFFL) << 24L | (bytes[offset + 5] & 0xFFL) << 16L | (bytes[offset + 6] & 0xFFL) << 8L | bytes[offset + 7] & 0xFFL;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  450 */     return bytes[offset] & 0xFFL | (bytes[offset + 1] & 0xFFL) << 8L | (bytes[offset + 2] & 0xFFL) << 16L | (bytes[offset + 3] & 0xFFL) << 24L | (bytes[offset + 4] & 0xFFL) << 32L | (bytes[offset + 5] & 0xFFL) << 40L | (bytes[offset + 6] & 0xFFL) << 48L | bytes[offset + 7] << 56L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getIntSafe(byte[] bytes, int offset) {
/*  461 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  462 */       return bytes[offset] << 24 | (bytes[offset + 1] & 0xFF) << 16 | (bytes[offset + 2] & 0xFF) << 8 | bytes[offset + 3] & 0xFF;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  467 */     return bytes[offset] & 0xFF | (bytes[offset + 1] & 0xFF) << 8 | (bytes[offset + 2] & 0xFF) << 16 | bytes[offset + 3] << 24;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static short getShortSafe(byte[] bytes, int offset) {
/*  474 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  475 */       return (short)(bytes[offset] << 8 | bytes[offset + 1] & 0xFF);
/*      */     }
/*  477 */     return (short)(bytes[offset] & 0xFF | bytes[offset + 1] << 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int hashCodeAsciiCompute(CharSequence value, int offset, int hash) {
/*  484 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  485 */       return hash * -862048943 + 
/*      */         
/*  487 */         hashCodeAsciiSanitizeInt(value, offset + 4) * 461845907 + 
/*      */         
/*  489 */         hashCodeAsciiSanitizeInt(value, offset);
/*      */     }
/*  491 */     return hash * -862048943 + 
/*      */       
/*  493 */       hashCodeAsciiSanitizeInt(value, offset) * 461845907 + 
/*      */       
/*  495 */       hashCodeAsciiSanitizeInt(value, offset + 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int hashCodeAsciiSanitizeInt(CharSequence value, int offset) {
/*  502 */     if (BIG_ENDIAN_NATIVE_ORDER)
/*      */     {
/*  504 */       return value.charAt(offset + 3) & 0x1F | (value
/*  505 */         .charAt(offset + 2) & 0x1F) << 8 | (value
/*  506 */         .charAt(offset + 1) & 0x1F) << 16 | (value
/*  507 */         .charAt(offset) & 0x1F) << 24;
/*      */     }
/*  509 */     return (value.charAt(offset + 3) & 0x1F) << 24 | (value
/*  510 */       .charAt(offset + 2) & 0x1F) << 16 | (value
/*  511 */       .charAt(offset + 1) & 0x1F) << 8 | value
/*  512 */       .charAt(offset) & 0x1F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int hashCodeAsciiSanitizeShort(CharSequence value, int offset) {
/*  519 */     if (BIG_ENDIAN_NATIVE_ORDER)
/*      */     {
/*  521 */       return value.charAt(offset + 1) & 0x1F | (value
/*  522 */         .charAt(offset) & 0x1F) << 8;
/*      */     }
/*  524 */     return (value.charAt(offset + 1) & 0x1F) << 8 | value
/*  525 */       .charAt(offset) & 0x1F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int hashCodeAsciiSanitizeByte(char value) {
/*  532 */     return value & 0x1F;
/*      */   }
/*      */   
/*      */   public static void putByte(long address, byte value) {
/*  536 */     PlatformDependent0.putByte(address, value);
/*      */   }
/*      */   
/*      */   public static void putShort(long address, short value) {
/*  540 */     PlatformDependent0.putShort(address, value);
/*      */   }
/*      */   
/*      */   public static void putInt(long address, int value) {
/*  544 */     PlatformDependent0.putInt(address, value);
/*      */   }
/*      */   
/*      */   public static void putLong(long address, long value) {
/*  548 */     PlatformDependent0.putLong(address, value);
/*      */   }
/*      */   
/*      */   public static void putByte(byte[] data, int index, byte value) {
/*  552 */     PlatformDependent0.putByte(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putShort(byte[] data, int index, short value) {
/*  556 */     PlatformDependent0.putShort(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putInt(byte[] data, int index, int value) {
/*  560 */     PlatformDependent0.putInt(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putLong(byte[] data, int index, long value) {
/*  564 */     PlatformDependent0.putLong(data, index, value);
/*      */   }
/*      */   
/*      */   public static void copyMemory(long srcAddr, long dstAddr, long length) {
/*  568 */     PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
/*      */   }
/*      */   
/*      */   public static void copyMemory(byte[] src, int srcIndex, long dstAddr, long length) {
/*  572 */     PlatformDependent0.copyMemory(src, BYTE_ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
/*      */   }
/*      */   
/*      */   public static void copyMemory(long srcAddr, byte[] dst, int dstIndex, long length) {
/*  576 */     PlatformDependent0.copyMemory(null, srcAddr, dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, length);
/*      */   }
/*      */   
/*      */   public static void setMemory(byte[] dst, int dstIndex, long bytes, byte value) {
/*  580 */     PlatformDependent0.setMemory(dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, bytes, value);
/*      */   }
/*      */   
/*      */   public static void setMemory(long address, long bytes, byte value) {
/*  584 */     PlatformDependent0.setMemory(address, bytes, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteBuffer allocateDirectNoCleaner(int capacity) {
/*  592 */     assert USE_DIRECT_BUFFER_NO_CLEANER;
/*      */     
/*  594 */     incrementMemoryCounter(capacity);
/*      */     try {
/*  596 */       return PlatformDependent0.allocateDirectNoCleaner(capacity);
/*  597 */     } catch (Throwable e) {
/*  598 */       decrementMemoryCounter(capacity);
/*  599 */       throwException(e);
/*  600 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
/*  609 */     assert USE_DIRECT_BUFFER_NO_CLEANER;
/*      */     
/*  611 */     int len = capacity - buffer.capacity();
/*  612 */     incrementMemoryCounter(len);
/*      */     try {
/*  614 */       return PlatformDependent0.reallocateDirectNoCleaner(buffer, capacity);
/*  615 */     } catch (Throwable e) {
/*  616 */       decrementMemoryCounter(len);
/*  617 */       throwException(e);
/*  618 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void freeDirectNoCleaner(ByteBuffer buffer) {
/*  627 */     assert USE_DIRECT_BUFFER_NO_CLEANER;
/*      */     
/*  629 */     int capacity = buffer.capacity();
/*  630 */     PlatformDependent0.freeMemory(PlatformDependent0.directBufferAddress(buffer));
/*  631 */     decrementMemoryCounter(capacity);
/*      */   }
/*      */   
/*      */   private static void incrementMemoryCounter(int capacity) {
/*  635 */     if (DIRECT_MEMORY_COUNTER != null) {
/*      */       long usedMemory; long newUsedMemory; do {
/*  637 */         usedMemory = DIRECT_MEMORY_COUNTER.get();
/*  638 */         newUsedMemory = usedMemory + capacity;
/*  639 */         if (newUsedMemory > DIRECT_MEMORY_LIMIT) {
/*  640 */           throw new OutOfDirectMemoryError("failed to allocate " + capacity + " byte(s) of direct memory (used: " + usedMemory + ", max: " + DIRECT_MEMORY_LIMIT + ')');
/*      */         }
/*      */       }
/*  643 */       while (!DIRECT_MEMORY_COUNTER.compareAndSet(usedMemory, newUsedMemory));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void decrementMemoryCounter(int capacity) {
/*  651 */     if (DIRECT_MEMORY_COUNTER != null) {
/*  652 */       long usedMemory = DIRECT_MEMORY_COUNTER.addAndGet(-capacity);
/*  653 */       assert usedMemory >= 0L;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean useDirectBufferNoCleaner() {
/*  658 */     return USE_DIRECT_BUFFER_NO_CLEANER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/*  673 */     return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? 
/*  674 */       equalsSafe(bytes1, startPos1, bytes2, startPos2, length) : 
/*  675 */       PlatformDependent0.equals(bytes1, startPos1, bytes2, startPos2, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isZero(byte[] bytes, int startPos, int length) {
/*  686 */     return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? 
/*  687 */       isZeroSafe(bytes, startPos, length) : 
/*  688 */       PlatformDependent0.isZero(bytes, startPos, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/*  713 */     return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? 
/*  714 */       ConstantTimeUtils.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length) : 
/*  715 */       PlatformDependent0.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hashCodeAscii(byte[] bytes, int startPos, int length) {
/*  728 */     return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? 
/*  729 */       hashCodeAsciiSafe(bytes, startPos, length) : 
/*  730 */       PlatformDependent0.hashCodeAscii(bytes, startPos, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hashCodeAscii(CharSequence bytes) {
/*  744 */     int i, hash = -1028477387;
/*  745 */     int remainingBytes = bytes.length() & 0x7;
/*      */ 
/*      */ 
/*      */     
/*  749 */     switch (bytes.length()) {
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 30:
/*      */       case 31:
/*  758 */         hash = hashCodeAsciiCompute(bytes, bytes.length() - 24, 
/*  759 */             hashCodeAsciiCompute(bytes, bytes.length() - 16, 
/*  760 */               hashCodeAsciiCompute(bytes, bytes.length() - 8, hash)));
/*      */         break;
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*  770 */         hash = hashCodeAsciiCompute(bytes, bytes.length() - 16, 
/*  771 */             hashCodeAsciiCompute(bytes, bytes.length() - 8, hash));
/*      */         break;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*  781 */         hash = hashCodeAsciiCompute(bytes, bytes.length() - 8, hash);
/*      */         break;
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */         break;
/*      */       default:
/*  793 */         for (i = bytes.length() - 8; i >= remainingBytes; i -= 8) {
/*  794 */           hash = hashCodeAsciiCompute(bytes, i, hash);
/*      */         }
/*      */         break;
/*      */     } 
/*  798 */     switch (remainingBytes) {
/*      */       case 7:
/*  800 */         return ((hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + 
/*  801 */           hashCodeAsciiSanitizeShort(bytes, 1)) * -862048943 + 
/*  802 */           hashCodeAsciiSanitizeInt(bytes, 3);
/*      */       case 6:
/*  804 */         return (hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0)) * 461845907 + 
/*  805 */           hashCodeAsciiSanitizeInt(bytes, 2);
/*      */       case 5:
/*  807 */         return (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + 
/*  808 */           hashCodeAsciiSanitizeInt(bytes, 1);
/*      */       case 4:
/*  810 */         return hash * -862048943 + hashCodeAsciiSanitizeInt(bytes, 0);
/*      */       case 3:
/*  812 */         return (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + 
/*  813 */           hashCodeAsciiSanitizeShort(bytes, 1);
/*      */       case 2:
/*  815 */         return hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0);
/*      */       case 1:
/*  817 */         return hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0));
/*      */     } 
/*  819 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Mpsc
/*      */   {
/*      */     private static final boolean USE_MPSC_CHUNKED_ARRAY_QUEUE;
/*      */ 
/*      */     
/*      */     static {
/*  830 */       Object unsafe = null;
/*  831 */       if (PlatformDependent.hasUnsafe())
/*      */       {
/*      */ 
/*      */         
/*  835 */         unsafe = AccessController.doPrivileged(new PrivilegedAction()
/*      */             {
/*      */               public Object run()
/*      */               {
/*  839 */                 return UnsafeAccess.UNSAFE;
/*      */               }
/*      */             });
/*      */       }
/*      */       
/*  844 */       if (unsafe == null) {
/*  845 */         PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: unavailable");
/*  846 */         USE_MPSC_CHUNKED_ARRAY_QUEUE = false;
/*      */       } else {
/*  848 */         PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: available");
/*  849 */         USE_MPSC_CHUNKED_ARRAY_QUEUE = true;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static <T> Queue<T> newMpscQueue(int maxCapacity) {
/*  857 */       int capacity = Math.max(Math.min(maxCapacity, 1073741824), 2048);
/*  858 */       return USE_MPSC_CHUNKED_ARRAY_QUEUE ? (Queue<T>)new MpscChunkedArrayQueue(1024, capacity) : (Queue<T>)new MpscGrowableAtomicArrayQueue(1024, capacity);
/*      */     }
/*      */ 
/*      */     
/*      */     static <T> Queue<T> newMpscQueue() {
/*  863 */       return USE_MPSC_CHUNKED_ARRAY_QUEUE ? (Queue<T>)new MpscUnboundedArrayQueue(1024) : (Queue<T>)new MpscUnboundedAtomicArrayQueue(1024);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Queue<T> newMpscQueue() {
/*  874 */     return Mpsc.newMpscQueue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Queue<T> newMpscQueue(int maxCapacity) {
/*  882 */     return Mpsc.newMpscQueue(maxCapacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Queue<T> newSpscQueue() {
/*  890 */     return hasUnsafe() ? (Queue<T>)new SpscLinkedQueue() : (Queue<T>)new SpscLinkedAtomicQueue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Queue<T> newFixedMpscQueue(int capacity) {
/*  898 */     return hasUnsafe() ? (Queue<T>)new MpscArrayQueue(capacity) : (Queue<T>)new MpscAtomicArrayQueue(capacity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassLoader getClassLoader(Class<?> clazz) {
/*  905 */     return PlatformDependent0.getClassLoader(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassLoader getContextClassLoader() {
/*  912 */     return PlatformDependent0.getContextClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassLoader getSystemClassLoader() {
/*  919 */     return PlatformDependent0.getSystemClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C> Deque<C> newConcurrentDeque() {
/*  926 */     if (javaVersion() < 7) {
/*  927 */       return new LinkedBlockingDeque<C>();
/*      */     }
/*  929 */     return new ConcurrentLinkedDeque<C>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Random threadLocalRandom() {
/*  937 */     return RANDOM_PROVIDER.current();
/*      */   }
/*      */   
/*      */   private static boolean isWindows0() {
/*  941 */     boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
/*  942 */     if (windows) {
/*  943 */       logger.debug("Platform: Windows");
/*      */     }
/*  945 */     return windows;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isOsx0() {
/*  950 */     String osname = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
/*  951 */     boolean osx = (osname.startsWith("macosx") || osname.startsWith("osx"));
/*      */     
/*  953 */     if (osx) {
/*  954 */       logger.debug("Platform: MacOS");
/*      */     }
/*  956 */     return osx;
/*      */   }
/*      */   
/*      */   private static boolean maybeSuperUser0() {
/*  960 */     String username = SystemPropertyUtil.get("user.name");
/*  961 */     if (isWindows()) {
/*  962 */       return "Administrator".equals(username);
/*      */     }
/*      */     
/*  965 */     return ("root".equals(username) || "toor".equals(username));
/*      */   }
/*      */   
/*      */   private static Throwable unsafeUnavailabilityCause0() {
/*  969 */     if (isAndroid()) {
/*  970 */       logger.debug("sun.misc.Unsafe: unavailable (Android)");
/*  971 */       return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (Android)");
/*      */     } 
/*  973 */     Throwable cause = PlatformDependent0.getUnsafeUnavailabilityCause();
/*  974 */     if (cause != null) {
/*  975 */       return cause;
/*      */     }
/*      */     
/*      */     try {
/*  979 */       boolean hasUnsafe = PlatformDependent0.hasUnsafe();
/*  980 */       logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
/*  981 */       return hasUnsafe ? null : PlatformDependent0.getUnsafeUnavailabilityCause();
/*  982 */     } catch (Throwable t) {
/*  983 */       logger.trace("Could not determine if Unsafe is available", t);
/*      */       
/*  985 */       return new UnsupportedOperationException("Could not determine if Unsafe is available", t);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static long maxDirectMemory0() {
/*  990 */     long maxDirectMemory = 0L;
/*      */     
/*  992 */     ClassLoader systemClassLoader = null;
/*      */     try {
/*  994 */       systemClassLoader = getSystemClassLoader();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  999 */       if (!SystemPropertyUtil.get("os.name", "").toLowerCase().contains("z/os")) {
/*      */         
/* 1001 */         Class<?> vmClass = Class.forName("sun.misc.VM", true, systemClassLoader);
/* 1002 */         Method m = vmClass.getDeclaredMethod("maxDirectMemory", new Class[0]);
/* 1003 */         maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
/*      */       } 
/* 1005 */     } catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */     
/* 1009 */     if (maxDirectMemory > 0L) {
/* 1010 */       return maxDirectMemory;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1016 */       Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, systemClassLoader);
/*      */       
/* 1018 */       Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, systemClassLoader);
/*      */ 
/*      */       
/* 1021 */       Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
/*      */ 
/*      */       
/* 1024 */       List<String> vmArgs = (List<String>)runtimeClass.getDeclaredMethod("getInputArguments", new Class[0]).invoke(runtime, new Object[0]);
/* 1025 */       for (int i = vmArgs.size() - 1; i >= 0; ) {
/* 1026 */         Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher(vmArgs.get(i));
/* 1027 */         if (!m.matches()) {
/*      */           i--;
/*      */           continue;
/*      */         } 
/* 1031 */         maxDirectMemory = Long.parseLong(m.group(1));
/* 1032 */         switch (m.group(2).charAt(0)) { case 'K':
/*      */           case 'k':
/* 1034 */             maxDirectMemory *= 1024L; break;
/*      */           case 'M':
/*      */           case 'm':
/* 1037 */             maxDirectMemory *= 1048576L; break;
/*      */           case 'G':
/*      */           case 'g':
/* 1040 */             maxDirectMemory *= 1073741824L;
/*      */             break; }
/*      */ 
/*      */       
/*      */       } 
/* 1045 */     } catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */     
/* 1049 */     if (maxDirectMemory <= 0L) {
/* 1050 */       maxDirectMemory = Runtime.getRuntime().maxMemory();
/* 1051 */       logger.debug("maxDirectMemory: {} bytes (maybe)", Long.valueOf(maxDirectMemory));
/*      */     } else {
/* 1053 */       logger.debug("maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
/*      */     } 
/*      */     
/* 1056 */     return maxDirectMemory;
/*      */   }
/*      */   
/*      */   private static File tmpdir0() {
/*      */     File f;
/*      */     try {
/* 1062 */       f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
/* 1063 */       if (f != null) {
/* 1064 */         logger.debug("-Dio.netty.tmpdir: {}", f);
/* 1065 */         return f;
/*      */       } 
/*      */       
/* 1068 */       f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
/* 1069 */       if (f != null) {
/* 1070 */         logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
/* 1071 */         return f;
/*      */       } 
/*      */ 
/*      */       
/* 1075 */       if (isWindows()) {
/* 1076 */         f = toDirectory(System.getenv("TEMP"));
/* 1077 */         if (f != null) {
/* 1078 */           logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
/* 1079 */           return f;
/*      */         } 
/*      */         
/* 1082 */         String userprofile = System.getenv("USERPROFILE");
/* 1083 */         if (userprofile != null) {
/* 1084 */           f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
/* 1085 */           if (f != null) {
/* 1086 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
/* 1087 */             return f;
/*      */           } 
/*      */           
/* 1090 */           f = toDirectory(userprofile + "\\Local Settings\\Temp");
/* 1091 */           if (f != null) {
/* 1092 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
/* 1093 */             return f;
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1097 */         f = toDirectory(System.getenv("TMPDIR"));
/* 1098 */         if (f != null) {
/* 1099 */           logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
/* 1100 */           return f;
/*      */         } 
/*      */       } 
/* 1103 */     } catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1108 */     if (isWindows()) {
/* 1109 */       f = new File("C:\\Windows\\Temp");
/*      */     } else {
/* 1111 */       f = new File("/tmp");
/*      */     } 
/*      */     
/* 1114 */     logger.warn("Failed to get the temporary directory; falling back to: {}", f);
/* 1115 */     return f;
/*      */   }
/*      */ 
/*      */   
/*      */   private static File toDirectory(String path) {
/* 1120 */     if (path == null) {
/* 1121 */       return null;
/*      */     }
/*      */     
/* 1124 */     File f = new File(path);
/* 1125 */     f.mkdirs();
/*      */     
/* 1127 */     if (!f.isDirectory()) {
/* 1128 */       return null;
/*      */     }
/*      */     
/*      */     try {
/* 1132 */       return f.getAbsoluteFile();
/* 1133 */     } catch (Exception ignored) {
/* 1134 */       return f;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int bitMode0() {
/* 1140 */     int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
/* 1141 */     if (bitMode > 0) {
/* 1142 */       logger.debug("-Dio.netty.bitMode: {}", Integer.valueOf(bitMode));
/* 1143 */       return bitMode;
/*      */     } 
/*      */ 
/*      */     
/* 1147 */     bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
/* 1148 */     if (bitMode > 0) {
/* 1149 */       logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", Integer.valueOf(bitMode));
/* 1150 */       return bitMode;
/*      */     } 
/* 1152 */     bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
/* 1153 */     if (bitMode > 0) {
/* 1154 */       logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", Integer.valueOf(bitMode));
/* 1155 */       return bitMode;
/*      */     } 
/*      */ 
/*      */     
/* 1159 */     String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
/* 1160 */     if ("amd64".equals(arch) || "x86_64".equals(arch)) {
/* 1161 */       bitMode = 64;
/* 1162 */     } else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
/* 1163 */       bitMode = 32;
/*      */     } 
/*      */     
/* 1166 */     if (bitMode > 0) {
/* 1167 */       logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(bitMode), arch);
/*      */     }
/*      */ 
/*      */     
/* 1171 */     String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
/* 1172 */     Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
/* 1173 */     Matcher m = BIT_PATTERN.matcher(vm);
/* 1174 */     if (m.find()) {
/* 1175 */       return Integer.parseInt(m.group(1));
/*      */     }
/* 1177 */     return 64;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int addressSize0() {
/* 1182 */     if (!hasUnsafe()) {
/* 1183 */       return -1;
/*      */     }
/* 1185 */     return PlatformDependent0.addressSize();
/*      */   }
/*      */   
/*      */   private static long byteArrayBaseOffset0() {
/* 1189 */     if (!hasUnsafe()) {
/* 1190 */       return -1L;
/*      */     }
/* 1192 */     return PlatformDependent0.byteArrayBaseOffset();
/*      */   }
/*      */   
/*      */   private static boolean equalsSafe(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 1196 */     int end = startPos1 + length;
/* 1197 */     for (; startPos1 < end; startPos1++, startPos2++) {
/* 1198 */       if (bytes1[startPos1] != bytes2[startPos2]) {
/* 1199 */         return false;
/*      */       }
/*      */     } 
/* 1202 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isZeroSafe(byte[] bytes, int startPos, int length) {
/* 1206 */     int end = startPos + length;
/* 1207 */     for (; startPos < end; startPos++) {
/* 1208 */       if (bytes[startPos] != 0) {
/* 1209 */         return false;
/*      */       }
/*      */     } 
/* 1212 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeAsciiSafe(byte[] bytes, int startPos, int length) {
/* 1219 */     int hash = -1028477387;
/* 1220 */     int remainingBytes = length & 0x7;
/* 1221 */     int end = startPos + remainingBytes;
/* 1222 */     for (int i = startPos - 8 + length; i >= end; i -= 8) {
/* 1223 */       hash = PlatformDependent0.hashCodeAsciiCompute(getLongSafe(bytes, i), hash);
/*      */     }
/* 1225 */     switch (remainingBytes) {
/*      */       case 7:
/* 1227 */         return ((hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + 
/* 1228 */           PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1))) * -862048943 + 
/* 1229 */           PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 3));
/*      */       case 6:
/* 1231 */         return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos))) * 461845907 + 
/* 1232 */           PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 2));
/*      */       case 5:
/* 1234 */         return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + 
/* 1235 */           PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 1));
/*      */       case 4:
/* 1237 */         return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos));
/*      */       case 3:
/* 1239 */         return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + 
/* 1240 */           PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1));
/*      */       case 2:
/* 1242 */         return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos));
/*      */       case 1:
/* 1244 */         return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos]);
/*      */     } 
/* 1246 */     return hash;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String normalizedArch() {
/* 1251 */     return NORMALIZED_ARCH;
/*      */   }
/*      */   
/*      */   public static String normalizedOs() {
/* 1255 */     return NORMALIZED_OS;
/*      */   }
/*      */   
/*      */   private static String normalize(String value) {
/* 1259 */     return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
/*      */   }
/*      */   
/*      */   private static String normalizeArch(String value) {
/* 1263 */     value = normalize(value);
/* 1264 */     if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
/* 1265 */       return "x86_64";
/*      */     }
/* 1267 */     if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
/* 1268 */       return "x86_32";
/*      */     }
/* 1270 */     if (value.matches("^(ia64|itanium64)$")) {
/* 1271 */       return "itanium_64";
/*      */     }
/* 1273 */     if (value.matches("^(sparc|sparc32)$")) {
/* 1274 */       return "sparc_32";
/*      */     }
/* 1276 */     if (value.matches("^(sparcv9|sparc64)$")) {
/* 1277 */       return "sparc_64";
/*      */     }
/* 1279 */     if (value.matches("^(arm|arm32)$")) {
/* 1280 */       return "arm_32";
/*      */     }
/* 1282 */     if ("aarch64".equals(value)) {
/* 1283 */       return "aarch_64";
/*      */     }
/* 1285 */     if (value.matches("^(ppc|ppc32)$")) {
/* 1286 */       return "ppc_32";
/*      */     }
/* 1288 */     if ("ppc64".equals(value)) {
/* 1289 */       return "ppc_64";
/*      */     }
/* 1291 */     if ("ppc64le".equals(value)) {
/* 1292 */       return "ppcle_64";
/*      */     }
/* 1294 */     if ("s390".equals(value)) {
/* 1295 */       return "s390_32";
/*      */     }
/* 1297 */     if ("s390x".equals(value)) {
/* 1298 */       return "s390_64";
/*      */     }
/*      */     
/* 1301 */     return "unknown";
/*      */   }
/*      */   
/*      */   private static String normalizeOs(String value) {
/* 1305 */     value = normalize(value);
/* 1306 */     if (value.startsWith("aix")) {
/* 1307 */       return "aix";
/*      */     }
/* 1309 */     if (value.startsWith("hpux")) {
/* 1310 */       return "hpux";
/*      */     }
/* 1312 */     if (value.startsWith("os400"))
/*      */     {
/* 1314 */       if (value.length() <= 5 || !Character.isDigit(value.charAt(5))) {
/* 1315 */         return "os400";
/*      */       }
/*      */     }
/* 1318 */     if (value.startsWith("linux")) {
/* 1319 */       return "linux";
/*      */     }
/* 1321 */     if (value.startsWith("macosx") || value.startsWith("osx")) {
/* 1322 */       return "osx";
/*      */     }
/* 1324 */     if (value.startsWith("freebsd")) {
/* 1325 */       return "freebsd";
/*      */     }
/* 1327 */     if (value.startsWith("openbsd")) {
/* 1328 */       return "openbsd";
/*      */     }
/* 1330 */     if (value.startsWith("netbsd")) {
/* 1331 */       return "netbsd";
/*      */     }
/* 1333 */     if (value.startsWith("solaris") || value.startsWith("sunos")) {
/* 1334 */       return "sunos";
/*      */     }
/* 1336 */     if (value.startsWith("windows")) {
/* 1337 */       return "windows";
/*      */     }
/*      */     
/* 1340 */     return "unknown";
/*      */   }
/*      */   
/*      */   private static final class AtomicLongCounter extends AtomicLong implements LongCounter { private static final long serialVersionUID = 4074772784610639305L;
/*      */     
/*      */     private AtomicLongCounter() {}
/*      */     
/*      */     public void add(long delta) {
/* 1348 */       addAndGet(delta);
/*      */     }
/*      */ 
/*      */     
/*      */     public void increment() {
/* 1353 */       incrementAndGet();
/*      */     }
/*      */ 
/*      */     
/*      */     public void decrement() {
/* 1358 */       decrementAndGet();
/*      */     }
/*      */ 
/*      */     
/*      */     public long value() {
/* 1363 */       return get();
/*      */     } }
/*      */ 
/*      */   
/*      */   private static interface ThreadLocalRandomProvider {
/*      */     Random current();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\PlatformDependent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */