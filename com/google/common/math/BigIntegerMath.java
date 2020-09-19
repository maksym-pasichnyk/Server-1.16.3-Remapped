/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class BigIntegerMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
/*     */   
/*     */   @Beta
/*     */   public static BigInteger ceilingPowerOfTwo(BigInteger x) {
/*  59 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.CEILING));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static BigInteger floorPowerOfTwo(BigInteger x) {
/*  71 */     return BigInteger.ZERO.setBit(log2(x, RoundingMode.FLOOR));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(BigInteger x) {
/*  78 */     Preconditions.checkNotNull(x);
/*  79 */     return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int log2(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2;
/*     */     int logX2Floor;
/*  92 */     MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
/*  93 */     int logFloor = x.bitLength() - 1;
/*  94 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  96 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/*  99 */         return logFloor;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 103 */         return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 108 */         if (logFloor < 256) {
/*     */           
/* 110 */           BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
/* 111 */           if (x.compareTo(halfPower) <= 0) {
/* 112 */             return logFloor;
/*     */           }
/* 114 */           return logFloor + 1;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         x2 = x.pow(2);
/* 122 */         logX2Floor = x2.bitLength() - 1;
/* 123 */         return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
/*     */     } 
/*     */     
/* 126 */     throw new AssertionError();
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
/*     */   @VisibleForTesting
/* 138 */   static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
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
/*     */   public static int log10(BigInteger x, RoundingMode mode) {
/*     */     BigInteger x2, halfPowerSquared;
/* 151 */     MathPreconditions.checkPositive("x", x);
/* 152 */     if (fitsInLong(x)) {
/* 153 */       return LongMath.log10(x.longValue(), mode);
/*     */     }
/*     */     
/* 156 */     int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
/* 157 */     BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
/* 158 */     int approxCmp = approxPow.compareTo(x);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     if (approxCmp > 0) {
/*     */ 
/*     */       
/*     */       do {
/*     */ 
/*     */ 
/*     */         
/* 172 */         approxLog10--;
/* 173 */         approxPow = approxPow.divide(BigInteger.TEN);
/* 174 */         approxCmp = approxPow.compareTo(x);
/* 175 */       } while (approxCmp > 0);
/*     */     } else {
/* 177 */       BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
/* 178 */       int nextCmp = nextPow.compareTo(x);
/* 179 */       while (nextCmp <= 0) {
/* 180 */         approxLog10++;
/* 181 */         approxPow = nextPow;
/* 182 */         approxCmp = nextCmp;
/* 183 */         nextPow = BigInteger.TEN.multiply(approxPow);
/* 184 */         nextCmp = nextPow.compareTo(x);
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     int floorLog = approxLog10;
/* 189 */     BigInteger floorPow = approxPow;
/* 190 */     int floorCmp = approxCmp;
/*     */     
/* 192 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 194 */         MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 198 */         return floorLog;
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/* 202 */         return floorPow.equals(x) ? floorLog : (floorLog + 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 208 */         x2 = x.pow(2);
/* 209 */         halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
/* 210 */         return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
/*     */     } 
/* 212 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 216 */   private static final double LN_10 = Math.log(10.0D);
/* 217 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
/*     */     int sqrtFloorInt;
/*     */     boolean sqrtFloorIsExact;
/*     */     BigInteger halfSquare;
/* 229 */     MathPreconditions.checkNonNegative("x", x);
/* 230 */     if (fitsInLong(x)) {
/* 231 */       return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode));
/*     */     }
/* 233 */     BigInteger sqrtFloor = sqrtFloor(x);
/* 234 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 236 */         MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 239 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 242 */         sqrtFloorInt = sqrtFloor.intValue();
/*     */ 
/*     */         
/* 245 */         sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
/* 246 */         return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 250 */         halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 256 */         return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
/*     */     } 
/* 258 */     throw new AssertionError();
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
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtFloor(BigInteger x) {
/*     */     BigInteger sqrt0;
/* 284 */     int log2 = log2(x, RoundingMode.FLOOR);
/* 285 */     if (log2 < 1023) {
/* 286 */       sqrt0 = sqrtApproxWithDoubles(x);
/*     */     } else {
/* 288 */       int shift = log2 - 52 & 0xFFFFFFFE;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
/*     */     } 
/* 295 */     BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 296 */     if (sqrt0.equals(sqrt1)) {
/* 297 */       return sqrt0;
/*     */     }
/*     */     while (true) {
/* 300 */       sqrt0 = sqrt1;
/* 301 */       sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
/* 302 */       if (sqrt1.compareTo(sqrt0) >= 0)
/* 303 */         return sqrt0; 
/*     */     } 
/*     */   }
/*     */   @GwtIncompatible
/*     */   private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
/* 308 */     return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
/* 320 */     BigDecimal pDec = new BigDecimal(p);
/* 321 */     BigDecimal qDec = new BigDecimal(q);
/* 322 */     return pDec.divide(qDec, 0, mode).toBigIntegerExact();
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
/*     */   public static BigInteger factorial(int n) {
/* 338 */     MathPreconditions.checkNonNegative("n", n);
/*     */ 
/*     */     
/* 341 */     if (n < LongMath.factorials.length) {
/* 342 */       return BigInteger.valueOf(LongMath.factorials[n]);
/*     */     }
/*     */ 
/*     */     
/* 346 */     int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
/* 347 */     ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
/*     */ 
/*     */     
/* 350 */     int startingNumber = LongMath.factorials.length;
/* 351 */     long product = LongMath.factorials[startingNumber - 1];
/*     */     
/* 353 */     int shift = Long.numberOfTrailingZeros(product);
/* 354 */     product >>= shift;
/*     */ 
/*     */     
/* 357 */     int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/* 358 */     int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
/*     */     
/* 360 */     int nextPowerOfTwo = 1 << bits - 1;
/*     */     
/*     */     long num;
/* 363 */     for (num = startingNumber; num <= n; num++) {
/*     */       
/* 365 */       if ((num & nextPowerOfTwo) != 0L) {
/* 366 */         nextPowerOfTwo <<= 1;
/* 367 */         bits++;
/*     */       } 
/*     */       
/* 370 */       int tz = Long.numberOfTrailingZeros(num);
/* 371 */       long normalizedNum = num >> tz;
/* 372 */       shift += tz;
/*     */       
/* 374 */       int normalizedBits = bits - tz;
/*     */       
/* 376 */       if (normalizedBits + productBits >= 64) {
/* 377 */         bignums.add(BigInteger.valueOf(product));
/* 378 */         product = 1L;
/* 379 */         productBits = 0;
/*     */       } 
/* 381 */       product *= normalizedNum;
/* 382 */       productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
/*     */     } 
/*     */     
/* 385 */     if (product > 1L) {
/* 386 */       bignums.add(BigInteger.valueOf(product));
/*     */     }
/*     */     
/* 389 */     return listProduct(bignums).shiftLeft(shift);
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums) {
/* 393 */     return listProduct(nums, 0, nums.size());
/*     */   }
/*     */   
/*     */   static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
/* 397 */     switch (end - start) {
/*     */       case 0:
/* 399 */         return BigInteger.ONE;
/*     */       case 1:
/* 401 */         return nums.get(start);
/*     */       case 2:
/* 403 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
/*     */       case 3:
/* 405 */         return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
/*     */     } 
/*     */     
/* 408 */     int m = end + start >>> 1;
/* 409 */     return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
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
/*     */   public static BigInteger binomial(int n, int k) {
/* 422 */     MathPreconditions.checkNonNegative("n", n);
/* 423 */     MathPreconditions.checkNonNegative("k", k);
/* 424 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", k, n);
/* 425 */     if (k > n >> 1) {
/* 426 */       k = n - k;
/*     */     }
/* 428 */     if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k]) {
/* 429 */       return BigInteger.valueOf(LongMath.binomial(n, k));
/*     */     }
/*     */     
/* 432 */     BigInteger accum = BigInteger.ONE;
/*     */     
/* 434 */     long numeratorAccum = n;
/* 435 */     long denominatorAccum = 1L;
/*     */     
/* 437 */     int bits = LongMath.log2(n, RoundingMode.CEILING);
/*     */     
/* 439 */     int numeratorBits = bits;
/*     */     
/* 441 */     for (int i = 1; i < k; i++) {
/* 442 */       int p = n - i;
/* 443 */       int q = i + 1;
/*     */ 
/*     */ 
/*     */       
/* 447 */       if (numeratorBits + bits >= 63) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 453 */         accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
/* 454 */         numeratorAccum = p;
/* 455 */         denominatorAccum = q;
/* 456 */         numeratorBits = bits;
/*     */       } else {
/*     */         
/* 459 */         numeratorAccum *= p;
/* 460 */         denominatorAccum *= q;
/* 461 */         numeratorBits += bits;
/*     */       } 
/*     */     } 
/* 464 */     return accum
/* 465 */       .multiply(BigInteger.valueOf(numeratorAccum))
/* 466 */       .divide(BigInteger.valueOf(denominatorAccum));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static boolean fitsInLong(BigInteger x) {
/* 472 */     return (x.bitLength() <= 63);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\BigIntegerMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */