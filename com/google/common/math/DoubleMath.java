/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class DoubleMath
/*     */ {
/*     */   private static final double MIN_INT_AS_DOUBLE = -2.147483648E9D;
/*     */   private static final double MAX_INT_AS_DOUBLE = 2.147483647E9D;
/*     */   private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18D;
/*     */   private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18D;
/*     */   
/*     */   @GwtIncompatible
/*     */   static double roundIntermediate(double x, RoundingMode mode) {
/*     */     double z;
/*  56 */     if (!DoubleUtils.isFinite(x)) {
/*  57 */       throw new ArithmeticException("input is infinite or NaN");
/*     */     }
/*  59 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  61 */         MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
/*  62 */         return x;
/*     */       
/*     */       case FLOOR:
/*  65 */         if (x >= 0.0D || isMathematicalInteger(x)) {
/*  66 */           return x;
/*     */         }
/*  68 */         return ((long)x - 1L);
/*     */ 
/*     */       
/*     */       case CEILING:
/*  72 */         if (x <= 0.0D || isMathematicalInteger(x)) {
/*  73 */           return x;
/*     */         }
/*  75 */         return ((long)x + 1L);
/*     */ 
/*     */       
/*     */       case DOWN:
/*  79 */         return x;
/*     */       
/*     */       case UP:
/*  82 */         if (isMathematicalInteger(x)) {
/*  83 */           return x;
/*     */         }
/*  85 */         return ((long)x + ((x > 0.0D) ? 1L : -1L));
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*  89 */         return Math.rint(x);
/*     */ 
/*     */       
/*     */       case HALF_UP:
/*  93 */         z = Math.rint(x);
/*  94 */         if (Math.abs(x - z) == 0.5D) {
/*  95 */           return x + Math.copySign(0.5D, x);
/*     */         }
/*  97 */         return z;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/* 103 */         z = Math.rint(x);
/* 104 */         if (Math.abs(x - z) == 0.5D) {
/* 105 */           return x;
/*     */         }
/* 107 */         return z;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 112 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   public static int roundToInt(double x, RoundingMode mode) {
/* 132 */     double z = roundIntermediate(x, mode);
/* 133 */     MathPreconditions.checkInRange(((z > -2.147483649E9D)) & ((z < 2.147483648E9D)));
/* 134 */     return (int)z;
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
/*     */   @GwtIncompatible
/*     */   public static long roundToLong(double x, RoundingMode mode) {
/* 156 */     double z = roundIntermediate(x, mode);
/* 157 */     MathPreconditions.checkInRange(((-9.223372036854776E18D - z < 1.0D)) & ((z < 9.223372036854776E18D)));
/* 158 */     return (long)z;
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
/*     */   @GwtIncompatible
/*     */   public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
/* 182 */     x = roundIntermediate(x, mode);
/* 183 */     if ((((-9.223372036854776E18D - x < 1.0D) ? 1 : 0) & ((x < 9.223372036854776E18D) ? 1 : 0)) != 0) {
/* 184 */       return BigInteger.valueOf((long)x);
/*     */     }
/* 186 */     int exponent = Math.getExponent(x);
/* 187 */     long significand = DoubleUtils.getSignificand(x);
/* 188 */     BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
/* 189 */     return (x < 0.0D) ? result.negate() : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean isPowerOfTwo(double x) {
/* 198 */     return (x > 0.0D && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x)));
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
/*     */   public static double log2(double x) {
/* 217 */     return Math.log(x) / LN_2;
/*     */   }
/*     */   
/* 220 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_FACTORIAL = 170;
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static int log2(double x, RoundingMode mode) {
/*     */     boolean bool1;
/*     */     int j, i;
/*     */     boolean increment;
/*     */     double xScaled;
/* 234 */     Preconditions.checkArgument((x > 0.0D && DoubleUtils.isFinite(x)), "x must be positive and finite");
/* 235 */     int exponent = Math.getExponent(x);
/* 236 */     if (!DoubleUtils.isNormal(x)) {
/* 237 */       return log2(x * 4.503599627370496E15D, mode) - 52;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 242 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 244 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       
/*     */       case FLOOR:
/* 247 */         bool1 = false;
/*     */         break;
/*     */       case CEILING:
/* 250 */         bool1 = !isPowerOfTwo(x);
/*     */         break;
/*     */       case DOWN:
/* 253 */         j = ((exponent < 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case UP:
/* 256 */         i = ((exponent >= 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case HALF_EVEN:
/*     */       case HALF_UP:
/*     */       case HALF_DOWN:
/* 261 */         xScaled = DoubleUtils.scaleNormalize(x);
/*     */ 
/*     */         
/* 264 */         increment = (xScaled * xScaled > 2.0D);
/*     */         break;
/*     */       default:
/* 267 */         throw new AssertionError();
/*     */     } 
/* 269 */     return increment ? (exponent + 1) : exponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static boolean isMathematicalInteger(double x) {
/* 280 */     return (DoubleUtils.isFinite(x) && (x == 0.0D || 52 - 
/*     */       
/* 282 */       Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x)));
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
/*     */   public static double factorial(int n) {
/* 295 */     MathPreconditions.checkNonNegative("n", n);
/* 296 */     if (n > 170) {
/* 297 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */     
/* 301 */     double accum = 1.0D;
/* 302 */     for (int i = 1 + (n & 0xFFFFFFF0); i <= n; i++) {
/* 303 */       accum *= i;
/*     */     }
/* 305 */     return accum * everySixteenthFactorial[n >> 4];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 312 */   static final double[] everySixteenthFactorial = new double[] { 1.0D, 2.0922789888E13D, 2.631308369336935E35D, 1.2413915592536073E61D, 1.2688693218588417E89D, 7.156945704626381E118D, 9.916779348709496E149D, 1.974506857221074E182D, 3.856204823625804E215D, 5.5502938327393044E249D, 4.7147236359920616E284D };
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
/*     */   public static boolean fuzzyEquals(double a, double b, double tolerance) {
/* 353 */     MathPreconditions.checkNonNegative("tolerance", tolerance);
/* 354 */     return (Math.copySign(a - b, 1.0D) <= tolerance || a == b || (
/*     */ 
/*     */       
/* 357 */       Double.isNaN(a) && Double.isNaN(b)));
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
/*     */   public static int fuzzyCompare(double a, double b, double tolerance) {
/* 375 */     if (fuzzyEquals(a, b, tolerance))
/* 376 */       return 0; 
/* 377 */     if (a < b)
/* 378 */       return -1; 
/* 379 */     if (a > b) {
/* 380 */       return 1;
/*     */     }
/* 382 */     return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(double... values) {
/* 402 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 403 */     long count = 1L;
/* 404 */     double mean = checkFinite(values[0]);
/* 405 */     for (int index = 1; index < values.length; index++) {
/* 406 */       checkFinite(values[index]);
/* 407 */       count++;
/*     */       
/* 409 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 411 */     return mean;
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
/*     */   @Deprecated
/*     */   public static double mean(int... values) {
/* 428 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/*     */ 
/*     */ 
/*     */     
/* 432 */     long sum = 0L;
/* 433 */     for (int index = 0; index < values.length; index++) {
/* 434 */       sum += values[index];
/*     */     }
/* 436 */     return sum / values.length;
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
/*     */   @Deprecated
/*     */   public static double mean(long... values) {
/* 454 */     Preconditions.checkArgument((values.length > 0), "Cannot take mean of 0 values");
/* 455 */     long count = 1L;
/* 456 */     double mean = values[0];
/* 457 */     for (int index = 1; index < values.length; index++) {
/* 458 */       count++;
/*     */       
/* 460 */       mean += (values[index] - mean) / count;
/*     */     } 
/* 462 */     return mean;
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterable<? extends Number> values) {
/* 482 */     return mean(values.iterator());
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
/*     */   @Deprecated
/*     */   @GwtIncompatible
/*     */   public static double mean(Iterator<? extends Number> values) {
/* 502 */     Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
/* 503 */     long count = 1L;
/* 504 */     double mean = checkFinite(((Number)values.next()).doubleValue());
/* 505 */     while (values.hasNext()) {
/* 506 */       double value = checkFinite(((Number)values.next()).doubleValue());
/* 507 */       count++;
/*     */       
/* 509 */       mean += (value - mean) / count;
/*     */     } 
/* 511 */     return mean;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @CanIgnoreReturnValue
/*     */   private static double checkFinite(double argument) {
/* 517 */     Preconditions.checkArgument(DoubleUtils.isFinite(argument));
/* 518 */     return argument;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\DoubleMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */