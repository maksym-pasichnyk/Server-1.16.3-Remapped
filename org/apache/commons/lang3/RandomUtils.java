/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomUtils
/*     */ {
/*  32 */   private static final Random RANDOM = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean nextBoolean() {
/*  59 */     return RANDOM.nextBoolean();
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
/*     */   public static byte[] nextBytes(int count) {
/*  73 */     Validate.isTrue((count >= 0), "Count cannot be negative.", new Object[0]);
/*     */     
/*  75 */     byte[] result = new byte[count];
/*  76 */     RANDOM.nextBytes(result);
/*  77 */     return result;
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
/*     */   public static int nextInt(int startInclusive, int endExclusive) {
/*  95 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/*  97 */     Validate.isTrue((startInclusive >= 0), "Both range values must be non-negative.", new Object[0]);
/*     */     
/*  99 */     if (startInclusive == endExclusive) {
/* 100 */       return startInclusive;
/*     */     }
/*     */     
/* 103 */     return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nextInt() {
/* 114 */     return nextInt(0, 2147483647);
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
/*     */   public static long nextLong(long startInclusive, long endExclusive) {
/* 132 */     Validate.isTrue((endExclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 134 */     Validate.isTrue((startInclusive >= 0L), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 136 */     if (startInclusive == endExclusive) {
/* 137 */       return startInclusive;
/*     */     }
/*     */     
/* 140 */     return (long)nextDouble(startInclusive, endExclusive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long nextLong() {
/* 151 */     return nextLong(0L, Long.MAX_VALUE);
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
/*     */   public static double nextDouble(double startInclusive, double endInclusive) {
/* 169 */     Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 171 */     Validate.isTrue((startInclusive >= 0.0D), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 173 */     if (startInclusive == endInclusive) {
/* 174 */       return startInclusive;
/*     */     }
/*     */     
/* 177 */     return startInclusive + (endInclusive - startInclusive) * RANDOM.nextDouble();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double nextDouble() {
/* 188 */     return nextDouble(0.0D, Double.MAX_VALUE);
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
/*     */   public static float nextFloat(float startInclusive, float endInclusive) {
/* 206 */     Validate.isTrue((endInclusive >= startInclusive), "Start value must be smaller or equal to end value.", new Object[0]);
/*     */     
/* 208 */     Validate.isTrue((startInclusive >= 0.0F), "Both range values must be non-negative.", new Object[0]);
/*     */     
/* 210 */     if (startInclusive == endInclusive) {
/* 211 */       return startInclusive;
/*     */     }
/*     */     
/* 214 */     return startInclusive + (endInclusive - startInclusive) * RANDOM.nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float nextFloat() {
/* 225 */     return nextFloat(0.0F, Float.MAX_VALUE);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\RandomUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */