/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Comparator;
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
/*     */ @GwtIncompatible
/*     */ public final class UnsignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = -128;
/*     */   public static final byte MAX_VALUE = -1;
/*     */   private static final int UNSIGNED_MASK = 255;
/*     */   
/*     */   public static int toInt(byte value) {
/*  71 */     return value & 0xFF;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static byte checkedCast(long value) {
/*  84 */     Preconditions.checkArgument((value >> 8L == 0L), "out of range: %s", value);
/*  85 */     return (byte)(int)value;
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
/*     */   public static byte saturatedCast(long value) {
/*  97 */     if (value > toInt((byte)-1)) {
/*  98 */       return -1;
/*     */     }
/* 100 */     if (value < 0L) {
/* 101 */       return 0;
/*     */     }
/* 103 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/* 117 */     return toInt(a) - toInt(b);
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
/*     */   public static byte min(byte... array) {
/* 129 */     Preconditions.checkArgument((array.length > 0));
/* 130 */     int min = toInt(array[0]);
/* 131 */     for (int i = 1; i < array.length; i++) {
/* 132 */       int next = toInt(array[i]);
/* 133 */       if (next < min) {
/* 134 */         min = next;
/*     */       }
/*     */     } 
/* 137 */     return (byte)min;
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
/*     */   public static byte max(byte... array) {
/* 149 */     Preconditions.checkArgument((array.length > 0));
/* 150 */     int max = toInt(array[0]);
/* 151 */     for (int i = 1; i < array.length; i++) {
/* 152 */       int next = toInt(array[i]);
/* 153 */       if (next > max) {
/* 154 */         max = next;
/*     */       }
/*     */     } 
/* 157 */     return (byte)max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static String toString(byte x) {
/* 167 */     return toString(x, 10);
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
/*     */   @Beta
/*     */   public static String toString(byte x, int radix) {
/* 182 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     return Integer.toString(toInt(x), radix);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string) {
/* 202 */     return parseUnsignedByte(string, 10);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string, int radix) {
/* 220 */     int parse = Integer.parseInt((String)Preconditions.checkNotNull(string), radix);
/*     */     
/* 222 */     if (parse >> 8 == 0) {
/* 223 */       return (byte)parse;
/*     */     }
/* 225 */     throw new NumberFormatException("out of range: " + parse);
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
/*     */   public static String join(String separator, byte... array) {
/* 239 */     Preconditions.checkNotNull(separator);
/* 240 */     if (array.length == 0) {
/* 241 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 245 */     StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
/* 246 */     builder.append(toInt(array[0]));
/* 247 */     for (int i = 1; i < array.length; i++) {
/* 248 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 250 */     return builder.toString();
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
/*     */   public static Comparator<byte[]> lexicographicalComparator() {
/* 268 */     return LexicographicalComparatorHolder.BEST_COMPARATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
/* 273 */     return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class LexicographicalComparatorHolder
/*     */   {
/* 285 */     static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class
/* 286 */       .getName() + "$UnsafeComparator";
/*     */     
/* 288 */     static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
/*     */     
/*     */     @VisibleForTesting
/*     */     enum UnsafeComparator implements Comparator<byte[]> {
/* 292 */       INSTANCE;
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
/*     */       static final int BYTE_ARRAY_BASE_OFFSET;
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
/* 318 */       static final Unsafe theUnsafe = getUnsafe();
/*     */       static {
/* 320 */         BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
/*     */ 
/*     */         
/* 323 */         if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
/* 324 */           throw new AssertionError();
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
/*     */ 
/*     */ 
/*     */       
/*     */       private static Unsafe getUnsafe() {
/*     */         try {
/* 336 */           return Unsafe.getUnsafe();
/* 337 */         } catch (SecurityException securityException) {
/*     */ 
/*     */           
/*     */           try {
/* 341 */             return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */                 {
/*     */                   public Unsafe run() throws Exception
/*     */                   {
/* 345 */                     Class<Unsafe> k = Unsafe.class;
/* 346 */                     for (Field f : k.getDeclaredFields()) {
/* 347 */                       f.setAccessible(true);
/* 348 */                       Object x = f.get(null);
/* 349 */                       if (k.isInstance(x)) {
/* 350 */                         return k.cast(x);
/*     */                       }
/*     */                     } 
/* 353 */                     throw new NoSuchFieldError("the Unsafe");
/*     */                   }
/*     */                 });
/* 356 */           } catch (PrivilegedActionException e) {
/* 357 */             throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 363 */         int minLength = Math.min(left.length, right.length);
/* 364 */         int minWords = minLength / 8;
/*     */ 
/*     */ 
/*     */         
/*     */         int i;
/*     */ 
/*     */         
/* 371 */         for (i = 0; i < minWords * 8; i += 8) {
/* 372 */           long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
/* 373 */           long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
/* 374 */           if (lw != rw) {
/* 375 */             if (BIG_ENDIAN) {
/* 376 */               return UnsignedLongs.compare(lw, rw);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 386 */             int n = Long.numberOfTrailingZeros(lw ^ rw) & 0xFFFFFFF8;
/* 387 */             return (int)(lw >>> n & 0xFFL) - (int)(rw >>> n & 0xFFL);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 392 */         for (i = minWords * 8; i < minLength; i++) {
/* 393 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 394 */           if (result != 0) {
/* 395 */             return result;
/*     */           }
/*     */         } 
/* 398 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 403 */         return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
/*     */       }
/*     */     }
/*     */     
/*     */     enum PureJavaComparator implements Comparator<byte[]> {
/* 408 */       INSTANCE;
/*     */ 
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 412 */         int minLength = Math.min(left.length, right.length);
/* 413 */         for (int i = 0; i < minLength; i++) {
/* 414 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 415 */           if (result != 0) {
/* 416 */             return result;
/*     */           }
/*     */         } 
/* 419 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 424 */         return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static Comparator<byte[]> getBestComparator() {
/*     */       try {
/* 434 */         Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);
/*     */ 
/*     */ 
/*     */         
/* 438 */         Comparator<byte[]> comparator = (Comparator<byte[]>)theClass.getEnumConstants()[0];
/* 439 */         return comparator;
/* 440 */       } catch (Throwable t) {
/* 441 */         return UnsignedBytes.lexicographicalComparatorJavaImpl();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   enum PureJavaComparator implements Comparator<byte[]> {
/*     */     INSTANCE;
/*     */     
/*     */     public int compare(byte[] left, byte[] right) {
/*     */       int minLength = Math.min(left.length, right.length);
/*     */       for (int i = 0; i < minLength; i++) {
/*     */         int result = UnsignedBytes.compare(left[i], right[i]);
/*     */         if (result != 0)
/*     */           return result; 
/*     */       } 
/*     */       return left.length - right.length;
/*     */     }
/*     */     
/*     */     public String toString() {
/*     */       return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\UnsignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */