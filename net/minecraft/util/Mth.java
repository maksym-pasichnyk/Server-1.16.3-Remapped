/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntPredicate;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Vec3i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mth
/*     */ {
/*  32 */   public static final float SQRT_OF_TWO = sqrt(2.0F);
/*     */   private static final float[] SIN;
/*     */   
/*     */   static {
/*  36 */     SIN = (float[])Util.make(new float[65536], debug0 -> {
/*     */           for (int debug1 = 0; debug1 < debug0.length; debug1++)
/*     */             debug0[debug1] = (float)Math.sin(debug1 * Math.PI * 2.0D / 65536.0D); 
/*     */         });
/*     */   }
/*     */   
/*  42 */   private static final Random RANDOM = new Random();
/*     */   
/*     */   public static float sin(float debug0) {
/*  45 */     return SIN[(int)(debug0 * 10430.378F) & 0xFFFF];
/*     */   }
/*     */   
/*     */   public static float cos(float debug0) {
/*  49 */     return SIN[(int)(debug0 * 10430.378F + 16384.0F) & 0xFFFF];
/*     */   }
/*     */   
/*     */   public static float sqrt(float debug0) {
/*  53 */     return (float)Math.sqrt(debug0);
/*     */   }
/*     */   
/*     */   public static float sqrt(double debug0) {
/*  57 */     return (float)Math.sqrt(debug0);
/*     */   }
/*     */   
/*     */   public static int floor(float debug0) {
/*  61 */     int debug1 = (int)debug0;
/*  62 */     return (debug0 < debug1) ? (debug1 - 1) : debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int floor(double debug0) {
/*  70 */     int debug2 = (int)debug0;
/*  71 */     return (debug0 < debug2) ? (debug2 - 1) : debug2;
/*     */   }
/*     */   
/*     */   public static long lfloor(double debug0) {
/*  75 */     long debug2 = (long)debug0;
/*  76 */     return (debug0 < debug2) ? (debug2 - 1L) : debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float abs(float debug0) {
/*  84 */     return Math.abs(debug0);
/*     */   }
/*     */   
/*     */   public static int abs(int debug0) {
/*  88 */     return Math.abs(debug0);
/*     */   }
/*     */   
/*     */   public static int ceil(float debug0) {
/*  92 */     int debug1 = (int)debug0;
/*  93 */     return (debug0 > debug1) ? (debug1 + 1) : debug1;
/*     */   }
/*     */   
/*     */   public static int ceil(double debug0) {
/*  97 */     int debug2 = (int)debug0;
/*  98 */     return (debug0 > debug2) ? (debug2 + 1) : debug2;
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
/*     */   public static int clamp(int debug0, int debug1, int debug2) {
/* 112 */     if (debug0 < debug1) {
/* 113 */       return debug1;
/*     */     }
/* 115 */     if (debug0 > debug2) {
/* 116 */       return debug2;
/*     */     }
/* 118 */     return debug0;
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
/*     */   public static float clamp(float debug0, float debug1, float debug2) {
/* 132 */     if (debug0 < debug1) {
/* 133 */       return debug1;
/*     */     }
/* 135 */     if (debug0 > debug2) {
/* 136 */       return debug2;
/*     */     }
/* 138 */     return debug0;
/*     */   }
/*     */   
/*     */   public static double clamp(double debug0, double debug2, double debug4) {
/* 142 */     if (debug0 < debug2) {
/* 143 */       return debug2;
/*     */     }
/* 145 */     if (debug0 > debug4) {
/* 146 */       return debug4;
/*     */     }
/* 148 */     return debug0;
/*     */   }
/*     */   
/*     */   public static double clampedLerp(double debug0, double debug2, double debug4) {
/* 152 */     if (debug4 < 0.0D) {
/* 153 */       return debug0;
/*     */     }
/* 155 */     if (debug4 > 1.0D) {
/* 156 */       return debug2;
/*     */     }
/* 158 */     return lerp(debug4, debug0, debug2);
/*     */   }
/*     */   
/*     */   public static double absMax(double debug0, double debug2) {
/* 162 */     if (debug0 < 0.0D) {
/* 163 */       debug0 = -debug0;
/*     */     }
/* 165 */     if (debug2 < 0.0D) {
/* 166 */       debug2 = -debug2;
/*     */     }
/* 168 */     return (debug0 > debug2) ? debug0 : debug2;
/*     */   }
/*     */   
/*     */   public static int intFloorDiv(int debug0, int debug1) {
/* 172 */     return Math.floorDiv(debug0, debug1);
/*     */   }
/*     */   
/*     */   public static int nextInt(Random debug0, int debug1, int debug2) {
/* 176 */     if (debug1 >= debug2) {
/* 177 */       return debug1;
/*     */     }
/* 179 */     return debug0.nextInt(debug2 - debug1 + 1) + debug1;
/*     */   }
/*     */   
/*     */   public static float nextFloat(Random debug0, float debug1, float debug2) {
/* 183 */     if (debug1 >= debug2) {
/* 184 */       return debug1;
/*     */     }
/* 186 */     return debug0.nextFloat() * (debug2 - debug1) + debug1;
/*     */   }
/*     */   
/*     */   public static double nextDouble(Random debug0, double debug1, double debug3) {
/* 190 */     if (debug1 >= debug3) {
/* 191 */       return debug1;
/*     */     }
/* 193 */     return debug0.nextDouble() * (debug3 - debug1) + debug1;
/*     */   }
/*     */   
/*     */   public static double average(long[] debug0) {
/* 197 */     long debug1 = 0L;
/*     */     
/* 199 */     for (long debug6 : debug0) {
/* 200 */       debug1 += debug6;
/*     */     }
/*     */     
/* 203 */     return debug1 / debug0.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(double debug0, double debug2) {
/* 211 */     return (Math.abs(debug2 - debug0) < 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   public static int positiveModulo(int debug0, int debug1) {
/* 215 */     return Math.floorMod(debug0, debug1);
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
/*     */   public static float wrapDegrees(float debug0) {
/* 244 */     float debug1 = debug0 % 360.0F;
/* 245 */     if (debug1 >= 180.0F) {
/* 246 */       debug1 -= 360.0F;
/*     */     }
/* 248 */     if (debug1 < -180.0F) {
/* 249 */       debug1 += 360.0F;
/*     */     }
/* 251 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double wrapDegrees(double debug0) {
/* 258 */     double debug2 = debug0 % 360.0D;
/* 259 */     if (debug2 >= 180.0D) {
/* 260 */       debug2 -= 360.0D;
/*     */     }
/* 262 */     if (debug2 < -180.0D) {
/* 263 */       debug2 += 360.0D;
/*     */     }
/* 265 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float degreesDifference(float debug0, float debug1) {
/* 273 */     return wrapDegrees(debug1 - debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float degreesDifferenceAbs(float debug0, float debug1) {
/* 281 */     return abs(degreesDifference(debug0, debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float rotateIfNecessary(float debug0, float debug1, float debug2) {
/* 290 */     float debug3 = degreesDifference(debug0, debug1);
/* 291 */     float debug4 = clamp(debug3, -debug2, debug2);
/* 292 */     return debug1 - debug4;
/*     */   }
/*     */   
/*     */   public static float approach(float debug0, float debug1, float debug2) {
/* 296 */     debug2 = abs(debug2);
/*     */     
/* 298 */     if (debug0 < debug1) {
/* 299 */       return clamp(debug0 + debug2, debug0, debug1);
/*     */     }
/* 301 */     return clamp(debug0 - debug2, debug1, debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float approachDegrees(float debug0, float debug1, float debug2) {
/* 306 */     float debug3 = degreesDifference(debug0, debug1);
/* 307 */     return approach(debug0, debug0 + debug3, debug2);
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
/*     */   public static int smallestEncompassingPowerOfTwo(int debug0) {
/* 332 */     int debug1 = debug0 - 1;
/* 333 */     debug1 |= debug1 >> 1;
/* 334 */     debug1 |= debug1 >> 2;
/* 335 */     debug1 |= debug1 >> 4;
/* 336 */     debug1 |= debug1 >> 8;
/* 337 */     debug1 |= debug1 >> 16;
/* 338 */     return debug1 + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(int debug0) {
/* 343 */     return (debug0 != 0 && (debug0 & debug0 - 1) == 0);
/*     */   }
/*     */ 
/*     */   
/* 347 */   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceillog2(int debug0) {
/* 352 */     debug0 = isPowerOfTwo(debug0) ? debug0 : smallestEncompassingPowerOfTwo(debug0);
/* 353 */     return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(debug0 * 125613361L >> 27L) & 0x1F];
/*     */   }
/*     */   
/*     */   public static int log2(int debug0) {
/* 357 */     return ceillog2(debug0) - (isPowerOfTwo(debug0) ? 0 : 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int roundUp(int debug0, int debug1) {
/* 362 */     if (debug1 == 0) {
/* 363 */       return 0;
/*     */     }
/* 365 */     if (debug0 == 0) {
/* 366 */       return debug1;
/*     */     }
/*     */     
/* 369 */     if (debug0 < 0) {
/* 370 */       debug1 *= -1;
/*     */     }
/*     */     
/* 373 */     int debug2 = debug0 % debug1;
/* 374 */     if (debug2 == 0) {
/* 375 */       return debug0;
/*     */     }
/* 377 */     return debug0 + debug1 - debug2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float frac(float debug0) {
/* 420 */     return debug0 - floor(debug0);
/*     */   }
/*     */   
/*     */   public static double frac(double debug0) {
/* 424 */     return debug0 - lfloor(debug0);
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
/*     */   public static long getSeed(Vec3i debug0) {
/* 437 */     return getSeed(debug0.getX(), debug0.getY(), debug0.getZ());
/*     */   }
/*     */   
/*     */   public static long getSeed(int debug0, int debug1, int debug2) {
/* 441 */     long debug3 = (debug0 * 3129871) ^ debug2 * 116129781L ^ debug1;
/* 442 */     debug3 = debug3 * debug3 * 42317861L + debug3 * 11L;
/* 443 */     return debug3 >> 16L;
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID(Random debug0) {
/* 447 */     long debug1 = debug0.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
/* 448 */     long debug3 = debug0.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
/* 449 */     return new UUID(debug1, debug3);
/*     */   }
/*     */   
/*     */   public static UUID createInsecureUUID() {
/* 453 */     return createInsecureUUID(RANDOM);
/*     */   }
/*     */   
/*     */   public static double inverseLerp(double debug0, double debug2, double debug4) {
/* 457 */     return (debug0 - debug2) / (debug4 - debug2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double atan2(double debug0, double debug2) {
/* 503 */     double debug4 = debug2 * debug2 + debug0 * debug0;
/*     */ 
/*     */     
/* 506 */     if (Double.isNaN(debug4)) {
/* 507 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/* 511 */     boolean debug6 = (debug0 < 0.0D);
/* 512 */     if (debug6) {
/* 513 */       debug0 = -debug0;
/*     */     }
/* 515 */     boolean debug7 = (debug2 < 0.0D);
/* 516 */     if (debug7) {
/* 517 */       debug2 = -debug2;
/*     */     }
/* 519 */     boolean debug8 = (debug0 > debug2);
/* 520 */     if (debug8) {
/* 521 */       double d = debug2;
/* 522 */       debug2 = debug0;
/* 523 */       debug0 = d;
/*     */     } 
/*     */ 
/*     */     
/* 527 */     double debug9 = fastInvSqrt(debug4);
/* 528 */     debug2 *= debug9;
/* 529 */     debug0 *= debug9;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 538 */     double debug11 = FRAC_BIAS + debug0;
/* 539 */     int debug13 = (int)Double.doubleToRawLongBits(debug11);
/*     */ 
/*     */     
/* 542 */     double debug14 = ASIN_TAB[debug13];
/* 543 */     double debug16 = COS_TAB[debug13];
/*     */ 
/*     */ 
/*     */     
/* 547 */     double debug18 = debug11 - FRAC_BIAS;
/* 548 */     double debug20 = debug0 * debug16 - debug2 * debug18;
/*     */ 
/*     */     
/* 551 */     double debug22 = (6.0D + debug20 * debug20) * debug20 * 0.16666666666666666D;
/* 552 */     double debug24 = debug14 + debug22;
/*     */ 
/*     */     
/* 555 */     if (debug8) {
/* 556 */       debug24 = 1.5707963267948966D - debug24;
/*     */     }
/* 558 */     if (debug7) {
/* 559 */       debug24 = Math.PI - debug24;
/*     */     }
/* 561 */     if (debug6) {
/* 562 */       debug24 = -debug24;
/*     */     }
/*     */     
/* 565 */     return debug24;
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
/*     */   public static double fastInvSqrt(double debug0) {
/* 578 */     double debug2 = 0.5D * debug0;
/* 579 */     long debug4 = Double.doubleToRawLongBits(debug0);
/* 580 */     debug4 = 6910469410427058090L - (debug4 >> 1L);
/* 581 */     debug0 = Double.longBitsToDouble(debug4);
/* 582 */     debug0 *= 1.5D - debug2 * debug0 * debug0;
/* 583 */     return debug0;
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
/* 598 */   private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
/* 599 */   private static final double[] ASIN_TAB = new double[257];
/* 600 */   private static final double[] COS_TAB = new double[257];
/*     */ 
/*     */   
/*     */   static {
/* 604 */     for (int debug0 = 0; debug0 < 257; debug0++) {
/* 605 */       double debug1 = debug0 / 256.0D;
/* 606 */       double debug3 = Math.asin(debug1);
/* 607 */       COS_TAB[debug0] = Math.cos(debug3);
/* 608 */       ASIN_TAB[debug0] = debug3;
/*     */     } 
/*     */   }
/*     */   public static int hsvToRgb(float debug0, float debug1, float debug2) {
/*     */     float debug8, debug9, debug10;
/* 613 */     int debug11, debug12, debug13, debug3 = (int)(debug0 * 6.0F) % 6;
/* 614 */     float debug4 = debug0 * 6.0F - debug3;
/* 615 */     float debug5 = debug2 * (1.0F - debug1);
/* 616 */     float debug6 = debug2 * (1.0F - debug4 * debug1);
/* 617 */     float debug7 = debug2 * (1.0F - (1.0F - debug4) * debug1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 623 */     switch (debug3) {
/*     */       case 0:
/* 625 */         debug8 = debug2;
/* 626 */         debug9 = debug7;
/* 627 */         debug10 = debug5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 658 */         debug11 = clamp((int)(debug8 * 255.0F), 0, 255);
/* 659 */         debug12 = clamp((int)(debug9 * 255.0F), 0, 255);
/* 660 */         debug13 = clamp((int)(debug10 * 255.0F), 0, 255);
/*     */         
/* 662 */         return debug11 << 16 | debug12 << 8 | debug13;case 1: debug8 = debug6; debug9 = debug2; debug10 = debug5; debug11 = clamp((int)(debug8 * 255.0F), 0, 255); debug12 = clamp((int)(debug9 * 255.0F), 0, 255); debug13 = clamp((int)(debug10 * 255.0F), 0, 255); return debug11 << 16 | debug12 << 8 | debug13;case 2: debug8 = debug5; debug9 = debug2; debug10 = debug7; debug11 = clamp((int)(debug8 * 255.0F), 0, 255); debug12 = clamp((int)(debug9 * 255.0F), 0, 255); debug13 = clamp((int)(debug10 * 255.0F), 0, 255); return debug11 << 16 | debug12 << 8 | debug13;case 3: debug8 = debug5; debug9 = debug6; debug10 = debug2; debug11 = clamp((int)(debug8 * 255.0F), 0, 255); debug12 = clamp((int)(debug9 * 255.0F), 0, 255); debug13 = clamp((int)(debug10 * 255.0F), 0, 255); return debug11 << 16 | debug12 << 8 | debug13;case 4: debug8 = debug7; debug9 = debug5; debug10 = debug2; debug11 = clamp((int)(debug8 * 255.0F), 0, 255); debug12 = clamp((int)(debug9 * 255.0F), 0, 255); debug13 = clamp((int)(debug10 * 255.0F), 0, 255); return debug11 << 16 | debug12 << 8 | debug13;case 5: debug8 = debug2; debug9 = debug5; debug10 = debug6; debug11 = clamp((int)(debug8 * 255.0F), 0, 255); debug12 = clamp((int)(debug9 * 255.0F), 0, 255); debug13 = clamp((int)(debug10 * 255.0F), 0, 255); return debug11 << 16 | debug12 << 8 | debug13;
/*     */     } 
/*     */     throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + debug0 + ", " + debug1 + ", " + debug2);
/*     */   }
/*     */   public static int murmurHash3Mixer(int debug0) {
/* 667 */     debug0 ^= debug0 >>> 16;
/* 668 */     debug0 *= -2048144789;
/* 669 */     debug0 ^= debug0 >>> 13;
/* 670 */     debug0 *= -1028477387;
/* 671 */     debug0 ^= debug0 >>> 16;
/* 672 */     return debug0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int binarySearch(int debug0, int debug1, IntPredicate debug2) {
/* 769 */     int debug3 = debug1 - debug0;
/* 770 */     while (debug3 > 0) {
/* 771 */       int debug4 = debug3 / 2;
/* 772 */       int debug5 = debug0 + debug4;
/* 773 */       if (debug2.test(debug5)) {
/*     */         
/* 775 */         debug3 = debug4; continue;
/*     */       } 
/* 777 */       debug0 = debug5 + 1;
/* 778 */       debug3 -= debug4 + 1;
/*     */     } 
/*     */     
/* 781 */     return debug0;
/*     */   }
/*     */   
/*     */   public static float lerp(float debug0, float debug1, float debug2) {
/* 785 */     return debug1 + debug0 * (debug2 - debug1);
/*     */   }
/*     */   
/*     */   public static double lerp(double debug0, double debug2, double debug4) {
/* 789 */     return debug2 + debug0 * (debug4 - debug2);
/*     */   }
/*     */   
/*     */   public static double lerp2(double debug0, double debug2, double debug4, double debug6, double debug8, double debug10) {
/* 793 */     return lerp(debug2, 
/*     */         
/* 795 */         lerp(debug0, debug4, debug6), 
/* 796 */         lerp(debug0, debug8, debug10));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double lerp3(double debug0, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12, double debug14, double debug16, double debug18, double debug20) {
/* 805 */     return lerp(debug4, 
/*     */         
/* 807 */         lerp2(debug0, debug2, debug6, debug8, debug10, debug12), 
/* 808 */         lerp2(debug0, debug2, debug14, debug16, debug18, debug20));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double smoothstep(double debug0) {
/* 816 */     return debug0 * debug0 * debug0 * (debug0 * (debug0 * 6.0D - 15.0D) + 10.0D);
/*     */   }
/*     */   
/*     */   public static int sign(double debug0) {
/* 820 */     if (debug0 == 0.0D) {
/* 821 */       return 0;
/*     */     }
/* 823 */     return (debug0 > 0.0D) ? 1 : -1;
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
/*     */   @Deprecated
/*     */   public static float rotlerp(float debug0, float debug1, float debug2) {
/* 837 */     float debug3 = debug1 - debug0;
/* 838 */     while (debug3 < -180.0F) {
/* 839 */       debug3 += 360.0F;
/*     */     }
/* 841 */     while (debug3 >= 180.0F) {
/* 842 */       debug3 -= 360.0F;
/*     */     }
/* 844 */     return debug0 + debug2 * debug3;
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
/*     */   public static float square(float debug0) {
/* 871 */     return debug0 * debug0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\Mth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */