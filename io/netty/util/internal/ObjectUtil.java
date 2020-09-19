/*     */ package io.netty.util.internal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectUtil
/*     */ {
/*     */   public static <T> T checkNotNull(T arg, String text) {
/*  32 */     if (arg == null) {
/*  33 */       throw new NullPointerException(text);
/*     */     }
/*  35 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkPositive(int i, String name) {
/*  43 */     if (i <= 0) {
/*  44 */       throw new IllegalArgumentException(name + ": " + i + " (expected: > 0)");
/*     */     }
/*  46 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long checkPositive(long i, String name) {
/*  54 */     if (i <= 0L) {
/*  55 */       throw new IllegalArgumentException(name + ": " + i + " (expected: > 0)");
/*     */     }
/*  57 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkPositiveOrZero(int i, String name) {
/*  65 */     if (i < 0) {
/*  66 */       throw new IllegalArgumentException(name + ": " + i + " (expected: >= 0)");
/*     */     }
/*  68 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long checkPositiveOrZero(long i, String name) {
/*  76 */     if (i < 0L) {
/*  77 */       throw new IllegalArgumentException(name + ": " + i + " (expected: >= 0)");
/*     */     }
/*  79 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T[] checkNonEmpty(T[] array, String name) {
/*  88 */     checkNotNull(array, name);
/*  89 */     checkPositive(array.length, name + ".length");
/*  90 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends java.util.Collection<?>> T checkNonEmpty(T collection, String name) {
/*  99 */     checkNotNull(collection, name);
/* 100 */     checkPositive(collection.size(), name + ".size");
/* 101 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intValue(Integer wrapper, int defaultValue) {
/* 111 */     return (wrapper != null) ? wrapper.intValue() : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long longValue(Long wrapper, long defaultValue) {
/* 121 */     return (wrapper != null) ? wrapper.longValue() : defaultValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\ObjectUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */