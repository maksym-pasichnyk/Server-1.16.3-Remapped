/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.Unsafe;
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
/*     */ final class PlatformDependent0
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final long ADDRESS_FIELD_OFFSET;
/*     */   private static final long BYTE_ARRAY_BASE_OFFSET;
/*     */   private static final Constructor<?> DIRECT_BUFFER_CONSTRUCTOR;
/*     */   private static final Throwable EXPLICIT_NO_UNSAFE_CAUSE;
/*     */   private static final Method ALLOCATE_ARRAY_METHOD;
/*     */   private static final int JAVA_VERSION;
/*     */   private static final boolean IS_ANDROID;
/*     */   private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
/*     */   
/*     */   static {
/*  38 */     logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
/*     */ 
/*     */ 
/*     */     
/*  42 */     EXPLICIT_NO_UNSAFE_CAUSE = explicitNoUnsafeCause0();
/*     */     
/*  44 */     JAVA_VERSION = javaVersion0();
/*  45 */     IS_ANDROID = isAndroid0();
/*     */ 
/*     */ 
/*     */     
/*  49 */     IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE = explicitTryReflectionSetAccessible0();
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
/*  68 */     Field addressField = null;
/*  69 */     Method allocateArrayMethod = null;
/*  70 */     Throwable unsafeUnavailabilityCause = null;
/*     */     
/*  72 */     Object internalUnsafe = null;
/*     */     
/*  74 */     if ((unsafeUnavailabilityCause = EXPLICIT_NO_UNSAFE_CAUSE) != null) {
/*  75 */       direct = null;
/*  76 */       addressField = null;
/*  77 */       unsafe = null;
/*  78 */       internalUnsafe = null;
/*     */     } else {
/*  80 */       direct = ByteBuffer.allocateDirect(1);
/*     */ 
/*     */       
/*  83 */       Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/*     */               try {
/*  87 */                 Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
/*     */ 
/*     */                 
/*  90 */                 Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
/*  91 */                 if (cause != null) {
/*  92 */                   return cause;
/*     */                 }
/*     */                 
/*  95 */                 return unsafeField.get(null);
/*  96 */               } catch (NoSuchFieldException e) {
/*  97 */                 return e;
/*  98 */               } catch (SecurityException e) {
/*  99 */                 return e;
/* 100 */               } catch (IllegalAccessException e) {
/* 101 */                 return e;
/* 102 */               } catch (NoClassDefFoundError e) {
/*     */ 
/*     */                 
/* 105 */                 return e;
/*     */               } 
/*     */             }
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 114 */       if (maybeUnsafe instanceof Throwable) {
/* 115 */         unsafe = null;
/* 116 */         unsafeUnavailabilityCause = (Throwable)maybeUnsafe;
/* 117 */         logger.debug("sun.misc.Unsafe.theUnsafe: unavailable", (Throwable)maybeUnsafe);
/*     */       } else {
/* 119 */         unsafe = (Unsafe)maybeUnsafe;
/* 120 */         logger.debug("sun.misc.Unsafe.theUnsafe: available");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       if (unsafe != null) {
/* 127 */         final Unsafe finalUnsafe = unsafe;
/* 128 */         Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Object run() {
/*     */                 try {
/* 132 */                   finalUnsafe.getClass().getDeclaredMethod("copyMemory", new Class[] { Object.class, long.class, Object.class, long.class, long.class });
/*     */                   
/* 134 */                   return null;
/* 135 */                 } catch (NoSuchMethodException e) {
/* 136 */                   return e;
/* 137 */                 } catch (SecurityException e) {
/* 138 */                   return e;
/*     */                 } 
/*     */               }
/*     */             });
/*     */         
/* 143 */         if (maybeException == null) {
/* 144 */           logger.debug("sun.misc.Unsafe.copyMemory: available");
/*     */         } else {
/*     */           
/* 147 */           unsafe = null;
/* 148 */           unsafeUnavailabilityCause = (Throwable)maybeException;
/* 149 */           logger.debug("sun.misc.Unsafe.copyMemory: unavailable", (Throwable)maybeException);
/*     */         } 
/*     */       } 
/*     */       
/* 153 */       if (unsafe != null) {
/* 154 */         final Unsafe finalUnsafe = unsafe;
/*     */ 
/*     */         
/* 157 */         Object maybeAddressField = AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Object run() {
/*     */                 try {
/* 161 */                   Field field = Buffer.class.getDeclaredField("address");
/*     */ 
/*     */                   
/* 164 */                   long offset = finalUnsafe.objectFieldOffset(field);
/* 165 */                   long address = finalUnsafe.getLong(direct, offset);
/*     */ 
/*     */                   
/* 168 */                   if (address == 0L) {
/* 169 */                     return null;
/*     */                   }
/* 171 */                   return field;
/* 172 */                 } catch (NoSuchFieldException e) {
/* 173 */                   return e;
/* 174 */                 } catch (SecurityException e) {
/* 175 */                   return e;
/*     */                 } 
/*     */               }
/*     */             });
/*     */         
/* 180 */         if (maybeAddressField instanceof Field) {
/* 181 */           addressField = (Field)maybeAddressField;
/* 182 */           logger.debug("java.nio.Buffer.address: available");
/*     */         } else {
/* 184 */           unsafeUnavailabilityCause = (Throwable)maybeAddressField;
/* 185 */           logger.debug("java.nio.Buffer.address: unavailable", (Throwable)maybeAddressField);
/*     */ 
/*     */ 
/*     */           
/* 189 */           unsafe = null;
/*     */         } 
/*     */       } 
/*     */       
/* 193 */       if (unsafe != null) {
/*     */ 
/*     */         
/* 196 */         long byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
/* 197 */         if (byteArrayIndexScale != 1L) {
/* 198 */           logger.debug("unsafe.arrayIndexScale is {} (expected: 1). Not using unsafe.", Long.valueOf(byteArrayIndexScale));
/* 199 */           unsafeUnavailabilityCause = new UnsupportedOperationException("Unexpected unsafe.arrayIndexScale");
/* 200 */           unsafe = null;
/*     */         } 
/*     */       } 
/*     */     } 
/* 204 */     UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause;
/* 205 */     UNSAFE = unsafe;
/*     */     
/* 207 */     if (unsafe == null) {
/* 208 */       ADDRESS_FIELD_OFFSET = -1L;
/* 209 */       BYTE_ARRAY_BASE_OFFSET = -1L;
/* 210 */       UNALIGNED = false;
/* 211 */       DIRECT_BUFFER_CONSTRUCTOR = null;
/* 212 */       ALLOCATE_ARRAY_METHOD = null;
/*     */     } else {
/*     */       Constructor<?> directBufferConstructor; boolean unaligned;
/* 215 */       long address = -1L;
/*     */       
/*     */       try {
/* 218 */         Object maybeDirectBufferConstructor = AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Object run()
/*     */               {
/*     */                 try {
/* 223 */                   Constructor<?> constructor = direct.getClass().getDeclaredConstructor(new Class[] { long.class, int.class });
/* 224 */                   Throwable cause = ReflectionUtil.trySetAccessible(constructor, true);
/* 225 */                   if (cause != null) {
/* 226 */                     return cause;
/*     */                   }
/* 228 */                   return constructor;
/* 229 */                 } catch (NoSuchMethodException e) {
/* 230 */                   return e;
/* 231 */                 } catch (SecurityException e) {
/* 232 */                   return e;
/*     */                 } 
/*     */               }
/*     */             });
/*     */         
/* 237 */         if (maybeDirectBufferConstructor instanceof Constructor) {
/* 238 */           address = UNSAFE.allocateMemory(1L);
/*     */           
/*     */           try {
/* 241 */             ((Constructor)maybeDirectBufferConstructor).newInstance(new Object[] { Long.valueOf(address), Integer.valueOf(1) });
/* 242 */             directBufferConstructor = (Constructor)maybeDirectBufferConstructor;
/* 243 */             logger.debug("direct buffer constructor: available");
/* 244 */           } catch (InstantiationException e) {
/* 245 */             directBufferConstructor = null;
/* 246 */           } catch (IllegalAccessException e) {
/* 247 */             directBufferConstructor = null;
/* 248 */           } catch (InvocationTargetException e) {
/* 249 */             directBufferConstructor = null;
/*     */           } 
/*     */         } else {
/* 252 */           logger.debug("direct buffer constructor: unavailable", (Throwable)maybeDirectBufferConstructor);
/*     */ 
/*     */           
/* 255 */           directBufferConstructor = null;
/*     */         } 
/*     */       } finally {
/* 258 */         if (address != -1L) {
/* 259 */           UNSAFE.freeMemory(address);
/*     */         }
/*     */       } 
/* 262 */       DIRECT_BUFFER_CONSTRUCTOR = directBufferConstructor;
/* 263 */       ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
/* 264 */       BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
/*     */       
/* 266 */       Object maybeUnaligned = AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run()
/*     */             {
/*     */               try {
/* 271 */                 Class<?> bitsClass = Class.forName("java.nio.Bits", false, PlatformDependent0.getSystemClassLoader());
/* 272 */                 Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", new Class[0]);
/* 273 */                 Throwable cause = ReflectionUtil.trySetAccessible(unalignedMethod, true);
/* 274 */                 if (cause != null) {
/* 275 */                   return cause;
/*     */                 }
/* 277 */                 return unalignedMethod.invoke(null, new Object[0]);
/* 278 */               } catch (NoSuchMethodException e) {
/* 279 */                 return e;
/* 280 */               } catch (SecurityException e) {
/* 281 */                 return e;
/* 282 */               } catch (IllegalAccessException e) {
/* 283 */                 return e;
/* 284 */               } catch (ClassNotFoundException e) {
/* 285 */                 return e;
/* 286 */               } catch (InvocationTargetException e) {
/* 287 */                 return e;
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 292 */       if (maybeUnaligned instanceof Boolean) {
/* 293 */         unaligned = ((Boolean)maybeUnaligned).booleanValue();
/* 294 */         logger.debug("java.nio.Bits.unaligned: available, {}", Boolean.valueOf(unaligned));
/*     */       } else {
/* 296 */         String arch = SystemPropertyUtil.get("os.arch", "");
/*     */         
/* 298 */         unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
/* 299 */         Throwable t = (Throwable)maybeUnaligned;
/* 300 */         logger.debug("java.nio.Bits.unaligned: unavailable {}", Boolean.valueOf(unaligned), t);
/*     */       } 
/*     */       
/* 303 */       UNALIGNED = unaligned;
/*     */       
/* 305 */       if (javaVersion() >= 9) {
/* 306 */         Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */ 
/*     */               
/*     */               public Object run()
/*     */               {
/*     */                 try {
/* 313 */                   Class<?> internalUnsafeClass = PlatformDependent0.getClassLoader(PlatformDependent0.class).loadClass("jdk.internal.misc.Unsafe");
/* 314 */                   Method method = internalUnsafeClass.getDeclaredMethod("getUnsafe", new Class[0]);
/* 315 */                   return method.invoke(null, new Object[0]);
/* 316 */                 } catch (Throwable e) {
/* 317 */                   return e;
/*     */                 } 
/*     */               }
/*     */             });
/* 321 */         if (!(maybeException instanceof Throwable)) {
/* 322 */           internalUnsafe = maybeException;
/* 323 */           final Object finalInternalUnsafe = internalUnsafe;
/* 324 */           maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */               {
/*     */                 public Object run() {
/*     */                   try {
/* 328 */                     return finalInternalUnsafe.getClass().getDeclaredMethod("allocateUninitializedArray", new Class[] { Class.class, int.class });
/*     */                   }
/* 330 */                   catch (NoSuchMethodException e) {
/* 331 */                     return e;
/* 332 */                   } catch (SecurityException e) {
/* 333 */                     return e;
/*     */                   } 
/*     */                 }
/*     */               });
/*     */           
/* 338 */           if (maybeException instanceof Method) {
/*     */             try {
/* 340 */               Method m = (Method)maybeException;
/* 341 */               byte[] bytes = (byte[])m.invoke(finalInternalUnsafe, new Object[] { byte.class, Integer.valueOf(8) });
/* 342 */               assert bytes.length == 8;
/* 343 */               allocateArrayMethod = m;
/* 344 */             } catch (IllegalAccessException e) {
/* 345 */               maybeException = e;
/* 346 */             } catch (InvocationTargetException e) {
/* 347 */               maybeException = e;
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 352 */         if (maybeException instanceof Throwable) {
/* 353 */           logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable", (Throwable)maybeException);
/*     */         } else {
/*     */           
/* 356 */           logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): available");
/*     */         } 
/*     */       } else {
/* 359 */         logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable prior to Java9");
/*     */       } 
/* 361 */       ALLOCATE_ARRAY_METHOD = allocateArrayMethod;
/*     */     } 
/*     */     
/* 364 */     INTERNAL_UNSAFE = internalUnsafe;
/*     */     
/* 366 */     logger.debug("java.nio.DirectByteBuffer.<init>(long, int): {}", (DIRECT_BUFFER_CONSTRUCTOR != null) ? "available" : "unavailable");
/*     */   } private static final Object INTERNAL_UNSAFE; private static final boolean IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE; static final Unsafe UNSAFE; static final int HASH_CODE_ASCII_SEED = -1028477387; static final int HASH_CODE_C1 = -862048943; static final int HASH_CODE_C2 = 461845907; private static final long UNSAFE_COPY_THRESHOLD = 1048576L; private static final boolean UNALIGNED; static {
/*     */     final ByteBuffer direct;
/*     */     Unsafe unsafe;
/*     */   } static boolean isExplicitNoUnsafe() {
/* 371 */     return (EXPLICIT_NO_UNSAFE_CAUSE == null);
/*     */   }
/*     */   private static Throwable explicitNoUnsafeCause0() {
/*     */     String unsafePropName;
/* 375 */     boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
/* 376 */     logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(noUnsafe));
/*     */     
/* 378 */     if (noUnsafe) {
/* 379 */       logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
/* 380 */       return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 385 */     if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
/* 386 */       unsafePropName = "io.netty.tryUnsafe";
/*     */     } else {
/* 388 */       unsafePropName = "org.jboss.netty.tryUnsafe";
/*     */     } 
/*     */     
/* 391 */     if (!SystemPropertyUtil.getBoolean(unsafePropName, true)) {
/* 392 */       String msg = "sun.misc.Unsafe: unavailable (" + unsafePropName + ")";
/* 393 */       logger.debug(msg);
/* 394 */       return new UnsupportedOperationException(msg);
/*     */     } 
/*     */     
/* 397 */     return null;
/*     */   }
/*     */   
/*     */   static boolean isUnaligned() {
/* 401 */     return UNALIGNED;
/*     */   }
/*     */   
/*     */   static boolean hasUnsafe() {
/* 405 */     return (UNSAFE != null);
/*     */   }
/*     */   
/*     */   static Throwable getUnsafeUnavailabilityCause() {
/* 409 */     return UNSAFE_UNAVAILABILITY_CAUSE;
/*     */   }
/*     */   
/*     */   static boolean unalignedAccess() {
/* 413 */     return UNALIGNED;
/*     */   }
/*     */ 
/*     */   
/*     */   static void throwException(Throwable cause) {
/* 418 */     UNSAFE.throwException(ObjectUtil.<Throwable>checkNotNull(cause, "cause"));
/*     */   }
/*     */   
/*     */   static boolean hasDirectBufferNoCleanerConstructor() {
/* 422 */     return (DIRECT_BUFFER_CONSTRUCTOR != null);
/*     */   }
/*     */   
/*     */   static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
/* 426 */     return newDirectBuffer(UNSAFE.reallocateMemory(directBufferAddress(buffer), capacity), capacity);
/*     */   }
/*     */   
/*     */   static ByteBuffer allocateDirectNoCleaner(int capacity) {
/* 430 */     return newDirectBuffer(UNSAFE.allocateMemory(capacity), capacity);
/*     */   }
/*     */   
/*     */   static boolean hasAllocateArrayMethod() {
/* 434 */     return (ALLOCATE_ARRAY_METHOD != null);
/*     */   }
/*     */   
/*     */   static byte[] allocateUninitializedArray(int size) {
/*     */     try {
/* 439 */       return (byte[])ALLOCATE_ARRAY_METHOD.invoke(INTERNAL_UNSAFE, new Object[] { byte.class, Integer.valueOf(size) });
/* 440 */     } catch (IllegalAccessException e) {
/* 441 */       throw new Error(e);
/* 442 */     } catch (InvocationTargetException e) {
/* 443 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static ByteBuffer newDirectBuffer(long address, int capacity) {
/* 448 */     ObjectUtil.checkPositiveOrZero(capacity, "capacity");
/*     */     
/*     */     try {
/* 451 */       return (ByteBuffer)DIRECT_BUFFER_CONSTRUCTOR.newInstance(new Object[] { Long.valueOf(address), Integer.valueOf(capacity) });
/* 452 */     } catch (Throwable cause) {
/*     */       
/* 454 */       if (cause instanceof Error) {
/* 455 */         throw (Error)cause;
/*     */       }
/* 457 */       throw new Error(cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   static long directBufferAddress(ByteBuffer buffer) {
/* 462 */     return getLong(buffer, ADDRESS_FIELD_OFFSET);
/*     */   }
/*     */   
/*     */   static long byteArrayBaseOffset() {
/* 466 */     return BYTE_ARRAY_BASE_OFFSET;
/*     */   }
/*     */   
/*     */   static Object getObject(Object object, long fieldOffset) {
/* 470 */     return UNSAFE.getObject(object, fieldOffset);
/*     */   }
/*     */   
/*     */   static int getInt(Object object, long fieldOffset) {
/* 474 */     return UNSAFE.getInt(object, fieldOffset);
/*     */   }
/*     */   
/*     */   private static long getLong(Object object, long fieldOffset) {
/* 478 */     return UNSAFE.getLong(object, fieldOffset);
/*     */   }
/*     */   
/*     */   static long objectFieldOffset(Field field) {
/* 482 */     return UNSAFE.objectFieldOffset(field);
/*     */   }
/*     */   
/*     */   static byte getByte(long address) {
/* 486 */     return UNSAFE.getByte(address);
/*     */   }
/*     */   
/*     */   static short getShort(long address) {
/* 490 */     return UNSAFE.getShort(address);
/*     */   }
/*     */   
/*     */   static int getInt(long address) {
/* 494 */     return UNSAFE.getInt(address);
/*     */   }
/*     */   
/*     */   static long getLong(long address) {
/* 498 */     return UNSAFE.getLong(address);
/*     */   }
/*     */   
/*     */   static byte getByte(byte[] data, int index) {
/* 502 */     return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static short getShort(byte[] data, int index) {
/* 506 */     return UNSAFE.getShort(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static int getInt(byte[] data, int index) {
/* 510 */     return UNSAFE.getInt(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static long getLong(byte[] data, int index) {
/* 514 */     return UNSAFE.getLong(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static void putByte(long address, byte value) {
/* 518 */     UNSAFE.putByte(address, value);
/*     */   }
/*     */   
/*     */   static void putShort(long address, short value) {
/* 522 */     UNSAFE.putShort(address, value);
/*     */   }
/*     */   
/*     */   static void putInt(long address, int value) {
/* 526 */     UNSAFE.putInt(address, value);
/*     */   }
/*     */   
/*     */   static void putLong(long address, long value) {
/* 530 */     UNSAFE.putLong(address, value);
/*     */   }
/*     */   
/*     */   static void putByte(byte[] data, int index, byte value) {
/* 534 */     UNSAFE.putByte(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putShort(byte[] data, int index, short value) {
/* 538 */     UNSAFE.putShort(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putInt(byte[] data, int index, int value) {
/* 542 */     UNSAFE.putInt(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putLong(byte[] data, int index, long value) {
/* 546 */     UNSAFE.putLong(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   static void copyMemory(long srcAddr, long dstAddr, long length) {
/* 551 */     while (length > 0L) {
/* 552 */       long size = Math.min(length, 1048576L);
/* 553 */       UNSAFE.copyMemory(srcAddr, dstAddr, size);
/* 554 */       length -= size;
/* 555 */       srcAddr += size;
/* 556 */       dstAddr += size;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length) {
/* 562 */     while (length > 0L) {
/* 563 */       long size = Math.min(length, 1048576L);
/* 564 */       UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
/* 565 */       length -= size;
/* 566 */       srcOffset += size;
/* 567 */       dstOffset += size;
/*     */     } 
/*     */   }
/*     */   
/*     */   static void setMemory(long address, long bytes, byte value) {
/* 572 */     UNSAFE.setMemory(address, bytes, value);
/*     */   }
/*     */   
/*     */   static void setMemory(Object o, long offset, long bytes, byte value) {
/* 576 */     UNSAFE.setMemory(o, offset, bytes, value);
/*     */   }
/*     */   
/*     */   static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 580 */     if (length <= 0) {
/* 581 */       return true;
/*     */     }
/* 583 */     long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
/* 584 */     long baseOffset2 = BYTE_ARRAY_BASE_OFFSET + startPos2;
/* 585 */     int remainingBytes = length & 0x7;
/* 586 */     long end = baseOffset1 + remainingBytes; long j;
/* 587 */     for (long i = baseOffset1 - 8L + length; i >= end; i -= 8L, j -= 8L) {
/* 588 */       if (UNSAFE.getLong(bytes1, i) != UNSAFE.getLong(bytes2, j)) {
/* 589 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 593 */     if (remainingBytes >= 4) {
/* 594 */       remainingBytes -= 4;
/* 595 */       if (UNSAFE.getInt(bytes1, baseOffset1 + remainingBytes) != UNSAFE
/* 596 */         .getInt(bytes2, baseOffset2 + remainingBytes)) {
/* 597 */         return false;
/*     */       }
/*     */     } 
/* 600 */     if (remainingBytes >= 2) {
/* 601 */       return (UNSAFE.getChar(bytes1, baseOffset1) == UNSAFE.getChar(bytes2, baseOffset2) && (remainingBytes == 2 || bytes1[startPos1 + 2] == bytes2[startPos2 + 2]));
/*     */     }
/*     */     
/* 604 */     return (bytes1[startPos1] == bytes2[startPos2]);
/*     */   }
/*     */   
/*     */   static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 608 */     long result = 0L;
/* 609 */     long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
/* 610 */     long baseOffset2 = BYTE_ARRAY_BASE_OFFSET + startPos2;
/* 611 */     int remainingBytes = length & 0x7;
/* 612 */     long end = baseOffset1 + remainingBytes; long j;
/* 613 */     for (long i = baseOffset1 - 8L + length; i >= end; i -= 8L, j -= 8L) {
/* 614 */       result |= UNSAFE.getLong(bytes1, i) ^ UNSAFE.getLong(bytes2, j);
/*     */     }
/* 616 */     switch (remainingBytes) {
/*     */       case 7:
/* 618 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 619 */             .getInt(bytes1, baseOffset1 + 3L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 3L)) | (UNSAFE
/* 620 */             .getChar(bytes1, baseOffset1 + 1L) ^ UNSAFE.getChar(bytes2, baseOffset2 + 1L)) | (UNSAFE
/* 621 */             .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2)), 0L);
/*     */       case 6:
/* 623 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 624 */             .getInt(bytes1, baseOffset1 + 2L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 2L)) | (UNSAFE
/* 625 */             .getChar(bytes1, baseOffset1) ^ UNSAFE.getChar(bytes2, baseOffset2)), 0L);
/*     */       case 5:
/* 627 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 628 */             .getInt(bytes1, baseOffset1 + 1L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 1L)) | (UNSAFE
/* 629 */             .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2)), 0L);
/*     */       case 4:
/* 631 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 632 */             .getInt(bytes1, baseOffset1) ^ UNSAFE.getInt(bytes2, baseOffset2)), 0L);
/*     */       case 3:
/* 634 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 635 */             .getChar(bytes1, baseOffset1 + 1L) ^ UNSAFE.getChar(bytes2, baseOffset2 + 1L)) | (UNSAFE
/* 636 */             .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2)), 0L);
/*     */       case 2:
/* 638 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 639 */             .getChar(bytes1, baseOffset1) ^ UNSAFE.getChar(bytes2, baseOffset2)), 0L);
/*     */       case 1:
/* 641 */         return ConstantTimeUtils.equalsConstantTime(result | (UNSAFE
/* 642 */             .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2)), 0L);
/*     */     } 
/* 644 */     return ConstantTimeUtils.equalsConstantTime(result, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isZero(byte[] bytes, int startPos, int length) {
/* 649 */     if (length <= 0) {
/* 650 */       return true;
/*     */     }
/* 652 */     long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
/* 653 */     int remainingBytes = length & 0x7;
/* 654 */     long end = baseOffset + remainingBytes; long i;
/* 655 */     for (i = baseOffset - 8L + length; i >= end; i -= 8L) {
/* 656 */       if (UNSAFE.getLong(bytes, i) != 0L) {
/* 657 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 661 */     if (remainingBytes >= 4) {
/* 662 */       remainingBytes -= 4;
/* 663 */       if (UNSAFE.getInt(bytes, baseOffset + remainingBytes) != 0) {
/* 664 */         return false;
/*     */       }
/*     */     } 
/* 667 */     if (remainingBytes >= 2) {
/* 668 */       return (UNSAFE.getChar(bytes, baseOffset) == '\000' && (remainingBytes == 2 || bytes[startPos + 2] == 0));
/*     */     }
/*     */     
/* 671 */     return (bytes[startPos] == 0);
/*     */   }
/*     */   
/*     */   static int hashCodeAscii(byte[] bytes, int startPos, int length) {
/* 675 */     int hash = -1028477387;
/* 676 */     long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
/* 677 */     int remainingBytes = length & 0x7;
/* 678 */     long end = baseOffset + remainingBytes; long i;
/* 679 */     for (i = baseOffset - 8L + length; i >= end; i -= 8L) {
/* 680 */       hash = hashCodeAsciiCompute(UNSAFE.getLong(bytes, i), hash);
/*     */     }
/* 682 */     switch (remainingBytes) {
/*     */       case 7:
/* 684 */         return ((hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + 
/* 685 */           hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset + 1L))) * -862048943 + 
/* 686 */           hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 3L));
/*     */       case 6:
/* 688 */         return (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset))) * 461845907 + 
/* 689 */           hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 2L));
/*     */       case 5:
/* 691 */         return (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + 
/* 692 */           hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 1L));
/*     */       case 4:
/* 694 */         return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset));
/*     */       case 3:
/* 696 */         return (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + 
/* 697 */           hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset + 1L));
/*     */       case 2:
/* 699 */         return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset));
/*     */       case 1:
/* 701 */         return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset));
/*     */     } 
/* 703 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int hashCodeAsciiCompute(long value, int hash) {
/* 710 */     return hash * -862048943 + 
/*     */       
/* 712 */       hashCodeAsciiSanitize((int)value) * 461845907 + (int)((value & 0x1F1F1F1F00000000L) >>> 32L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int hashCodeAsciiSanitize(int value) {
/* 718 */     return value & 0x1F1F1F1F;
/*     */   }
/*     */   
/*     */   static int hashCodeAsciiSanitize(short value) {
/* 722 */     return value & 0x1F1F;
/*     */   }
/*     */   
/*     */   static int hashCodeAsciiSanitize(byte value) {
/* 726 */     return value & 0x1F;
/*     */   }
/*     */   
/*     */   static ClassLoader getClassLoader(final Class<?> clazz) {
/* 730 */     if (System.getSecurityManager() == null) {
/* 731 */       return clazz.getClassLoader();
/*     */     }
/* 733 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*     */         {
/*     */           public ClassLoader run() {
/* 736 */             return clazz.getClassLoader();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static ClassLoader getContextClassLoader() {
/* 743 */     if (System.getSecurityManager() == null) {
/* 744 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/* 746 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*     */         {
/*     */           public ClassLoader run() {
/* 749 */             return Thread.currentThread().getContextClassLoader();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static ClassLoader getSystemClassLoader() {
/* 756 */     if (System.getSecurityManager() == null) {
/* 757 */       return ClassLoader.getSystemClassLoader();
/*     */     }
/* 759 */     return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>()
/*     */         {
/*     */           public ClassLoader run() {
/* 762 */             return ClassLoader.getSystemClassLoader();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static int addressSize() {
/* 769 */     return UNSAFE.addressSize();
/*     */   }
/*     */   
/*     */   static long allocateMemory(long size) {
/* 773 */     return UNSAFE.allocateMemory(size);
/*     */   }
/*     */   
/*     */   static void freeMemory(long address) {
/* 777 */     UNSAFE.freeMemory(address);
/*     */   }
/*     */   
/*     */   static long reallocateMemory(long address, long newSize) {
/* 781 */     return UNSAFE.reallocateMemory(address, newSize);
/*     */   }
/*     */   
/*     */   static boolean isAndroid() {
/* 785 */     return IS_ANDROID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAndroid0() {
/* 796 */     String vmName = SystemPropertyUtil.get("java.vm.name");
/* 797 */     boolean isAndroid = "Dalvik".equals(vmName);
/* 798 */     if (isAndroid) {
/* 799 */       logger.debug("Platform: Android");
/*     */     }
/* 801 */     return isAndroid;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean explicitTryReflectionSetAccessible0() {
/* 806 */     return SystemPropertyUtil.getBoolean("io.netty.tryReflectionSetAccessible", (javaVersion() < 9));
/*     */   }
/*     */   
/*     */   static boolean isExplicitTryReflectionSetAccessible() {
/* 810 */     return IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
/*     */   }
/*     */   
/*     */   static int javaVersion() {
/* 814 */     return JAVA_VERSION;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int javaVersion0() {
/*     */     int majorVersion;
/* 820 */     if (isAndroid0()) {
/* 821 */       majorVersion = 6;
/*     */     } else {
/* 823 */       majorVersion = majorVersionFromJavaSpecificationVersion();
/*     */     } 
/*     */     
/* 826 */     logger.debug("Java version: {}", Integer.valueOf(majorVersion));
/*     */     
/* 828 */     return majorVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   static int majorVersionFromJavaSpecificationVersion() {
/* 833 */     return majorVersion(SystemPropertyUtil.get("java.specification.version", "1.6"));
/*     */   }
/*     */ 
/*     */   
/*     */   static int majorVersion(String javaSpecVersion) {
/* 838 */     String[] components = javaSpecVersion.split("\\.");
/* 839 */     int[] version = new int[components.length];
/* 840 */     for (int i = 0; i < components.length; i++) {
/* 841 */       version[i] = Integer.parseInt(components[i]);
/*     */     }
/*     */     
/* 844 */     if (version[0] == 1) {
/* 845 */       assert version[1] >= 6;
/* 846 */       return version[1];
/*     */     } 
/* 848 */     return version[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\PlatformDependent0.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */