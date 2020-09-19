/*     */ package org.apache.commons.lang3.math;
/*     */ 
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IEEE754rUtils
/*     */ {
/*     */   public static double min(double... array) {
/*  41 */     if (array == null) {
/*  42 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/*  44 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */ 
/*     */     
/*  48 */     double min = array[0];
/*  49 */     for (int i = 1; i < array.length; i++) {
/*  50 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  53 */     return min;
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
/*     */   public static float min(float... array) {
/*  67 */     if (array == null) {
/*  68 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/*  70 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/*  73 */     float min = array[0];
/*  74 */     for (int i = 1; i < array.length; i++) {
/*  75 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  78 */     return min;
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
/*     */   public static double min(double a, double b, double c) {
/*  92 */     return min(min(a, b), c);
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
/*     */   public static double min(double a, double b) {
/* 105 */     if (Double.isNaN(a)) {
/* 106 */       return b;
/*     */     }
/* 108 */     if (Double.isNaN(b)) {
/* 109 */       return a;
/*     */     }
/* 111 */     return Math.min(a, b);
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
/*     */   public static float min(float a, float b, float c) {
/* 126 */     return min(min(a, b), c);
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
/*     */   public static float min(float a, float b) {
/* 139 */     if (Float.isNaN(a)) {
/* 140 */       return b;
/*     */     }
/* 142 */     if (Float.isNaN(b)) {
/* 143 */       return a;
/*     */     }
/* 145 */     return Math.min(a, b);
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
/*     */   public static double max(double... array) {
/* 160 */     if (array == null) {
/* 161 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/* 163 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 166 */     double max = array[0];
/* 167 */     for (int j = 1; j < array.length; j++) {
/* 168 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 171 */     return max;
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
/*     */   public static float max(float... array) {
/* 185 */     if (array == null) {
/* 186 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/* 188 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 191 */     float max = array[0];
/* 192 */     for (int j = 1; j < array.length; j++) {
/* 193 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 196 */     return max;
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
/*     */   public static double max(double a, double b, double c) {
/* 210 */     return max(max(a, b), c);
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
/*     */   public static double max(double a, double b) {
/* 223 */     if (Double.isNaN(a)) {
/* 224 */       return b;
/*     */     }
/* 226 */     if (Double.isNaN(b)) {
/* 227 */       return a;
/*     */     }
/* 229 */     return Math.max(a, b);
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
/*     */   public static float max(float a, float b, float c) {
/* 244 */     return max(max(a, b), c);
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
/*     */   public static float max(float a, float b) {
/* 257 */     if (Float.isNaN(a)) {
/* 258 */       return b;
/*     */     }
/* 260 */     if (Float.isNaN(b)) {
/* 261 */       return a;
/*     */     }
/* 263 */     return Math.max(a, b);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\math\IEEE754rUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */