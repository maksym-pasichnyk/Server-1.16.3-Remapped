/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Comparator;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedLongs
/*     */ {
/*     */   public static final long MAX_VALUE = -1L;
/*     */   
/*     */   private static long flip(long a) {
/*  63 */     return a ^ Long.MIN_VALUE;
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
/*     */   public static int compare(long a, long b) {
/*  76 */     return Longs.compare(flip(a), flip(b));
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
/*     */   public static long min(long... array) {
/*  88 */     Preconditions.checkArgument((array.length > 0));
/*  89 */     long min = flip(array[0]);
/*  90 */     for (int i = 1; i < array.length; i++) {
/*  91 */       long next = flip(array[i]);
/*  92 */       if (next < min) {
/*  93 */         min = next;
/*     */       }
/*     */     } 
/*  96 */     return flip(min);
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
/*     */   public static long max(long... array) {
/* 108 */     Preconditions.checkArgument((array.length > 0));
/* 109 */     long max = flip(array[0]);
/* 110 */     for (int i = 1; i < array.length; i++) {
/* 111 */       long next = flip(array[i]);
/* 112 */       if (next > max) {
/* 113 */         max = next;
/*     */       }
/*     */     } 
/* 116 */     return flip(max);
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
/*     */   public static String join(String separator, long... array) {
/* 128 */     Preconditions.checkNotNull(separator);
/* 129 */     if (array.length == 0) {
/* 130 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 134 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 135 */     builder.append(toString(array[0]));
/* 136 */     for (int i = 1; i < array.length; i++) {
/* 137 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 139 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 154 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<long[]> {
/* 158 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 162 */       int minLength = Math.min(left.length, right.length);
/* 163 */       for (int i = 0; i < minLength; i++) {
/* 164 */         if (left[i] != right[i]) {
/* 165 */           return UnsignedLongs.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 168 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 173 */       return "UnsignedLongs.lexicographicalComparator()";
/*     */     }
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
/*     */   public static long divide(long dividend, long divisor) {
/* 186 */     if (divisor < 0L) {
/* 187 */       if (compare(dividend, divisor) < 0) {
/* 188 */         return 0L;
/*     */       }
/* 190 */       return 1L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 195 */     if (dividend >= 0L) {
/* 196 */       return dividend / divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 206 */     long rem = dividend - quotient * divisor;
/* 207 */     return quotient + ((compare(rem, divisor) >= 0) ? 1L : 0L);
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
/*     */   public static long remainder(long dividend, long divisor) {
/* 220 */     if (divisor < 0L) {
/* 221 */       if (compare(dividend, divisor) < 0) {
/* 222 */         return dividend;
/*     */       }
/* 224 */       return dividend - divisor;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 229 */     if (dividend >= 0L) {
/* 230 */       return dividend % divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 240 */     long rem = dividend - quotient * divisor;
/* 241 */     return rem - ((compare(rem, divisor) >= 0) ? divisor : 0L);
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
/*     */   public static long parseUnsignedLong(String string) {
/* 254 */     return parseUnsignedLong(string, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long decode(String stringValue) {
/* 275 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 278 */       return parseUnsignedLong(request.rawValue, request.radix);
/* 279 */     } catch (NumberFormatException e) {
/* 280 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 282 */       decodeException.initCause(e);
/* 283 */       throw decodeException;
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long parseUnsignedLong(String string, int radix) {
/* 300 */     Preconditions.checkNotNull(string);
/* 301 */     if (string.length() == 0) {
/* 302 */       throw new NumberFormatException("empty string");
/*     */     }
/* 304 */     if (radix < 2 || radix > 36) {
/* 305 */       throw new NumberFormatException("illegal radix: " + radix);
/*     */     }
/*     */     
/* 308 */     int maxSafePos = maxSafeDigits[radix] - 1;
/* 309 */     long value = 0L;
/* 310 */     for (int pos = 0; pos < string.length(); pos++) {
/* 311 */       int digit = Character.digit(string.charAt(pos), radix);
/* 312 */       if (digit == -1) {
/* 313 */         throw new NumberFormatException(string);
/*     */       }
/* 315 */       if (pos > maxSafePos && overflowInParse(value, digit, radix)) {
/* 316 */         throw new NumberFormatException("Too large for unsigned long: " + string);
/*     */       }
/* 318 */       value = value * radix + digit;
/*     */     } 
/*     */     
/* 321 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean overflowInParse(long current, int digit, int radix) {
/* 331 */     if (current >= 0L) {
/* 332 */       if (current < maxValueDivs[radix]) {
/* 333 */         return false;
/*     */       }
/* 335 */       if (current > maxValueDivs[radix]) {
/* 336 */         return true;
/*     */       }
/*     */       
/* 339 */       return (digit > maxValueMods[radix]);
/*     */     } 
/*     */ 
/*     */     
/* 343 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(long x) {
/* 350 */     return toString(x, 10);
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
/*     */   public static String toString(long x, int radix) {
/* 363 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */     
/* 367 */     if (x == 0L)
/*     */     {
/* 369 */       return "0"; } 
/* 370 */     if (x > 0L) {
/* 371 */       return Long.toString(x, radix);
/*     */     }
/* 373 */     char[] buf = new char[64];
/* 374 */     int i = buf.length;
/* 375 */     if ((radix & radix - 1) == 0) {
/*     */       
/* 377 */       int shift = Integer.numberOfTrailingZeros(radix);
/* 378 */       int mask = radix - 1;
/*     */       do {
/* 380 */         buf[--i] = Character.forDigit((int)x & mask, radix);
/* 381 */         x >>>= shift;
/* 382 */       } while (x != 0L);
/*     */     } else {
/*     */       long quotient;
/*     */ 
/*     */       
/* 387 */       if ((radix & 0x1) == 0) {
/*     */         
/* 389 */         quotient = (x >>> 1L) / (radix >>> 1);
/*     */       } else {
/* 391 */         quotient = divide(x, radix);
/*     */       } 
/* 393 */       long rem = x - quotient * radix;
/* 394 */       buf[--i] = Character.forDigit((int)rem, radix);
/* 395 */       x = quotient;
/*     */       
/* 397 */       while (x > 0L) {
/* 398 */         buf[--i] = Character.forDigit((int)(x % radix), radix);
/* 399 */         x /= radix;
/*     */       } 
/*     */     } 
/*     */     
/* 403 */     return new String(buf, i, buf.length - i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 408 */   private static final long[] maxValueDivs = new long[37];
/* 409 */   private static final int[] maxValueMods = new int[37];
/* 410 */   private static final int[] maxSafeDigits = new int[37];
/*     */   
/*     */   static {
/* 413 */     BigInteger overflow = new BigInteger("10000000000000000", 16);
/* 414 */     for (int i = 2; i <= 36; i++) {
/* 415 */       maxValueDivs[i] = divide(-1L, i);
/* 416 */       maxValueMods[i] = (int)remainder(-1L, i);
/* 417 */       maxSafeDigits[i] = overflow.toString(i).length() - 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\UnsignedLongs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */