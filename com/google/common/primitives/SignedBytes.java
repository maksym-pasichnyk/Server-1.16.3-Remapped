/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @GwtCompatible
/*     */ public final class SignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = 64;
/*     */   
/*     */   public static byte checkedCast(long value) {
/*  56 */     byte result = (byte)(int)value;
/*  57 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  58 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte saturatedCast(long value) {
/*  69 */     if (value > 127L) {
/*  70 */       return Byte.MAX_VALUE;
/*     */     }
/*  72 */     if (value < -128L) {
/*  73 */       return Byte.MIN_VALUE;
/*     */     }
/*  75 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/*  92 */     return a - b;
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
/* 104 */     Preconditions.checkArgument((array.length > 0));
/* 105 */     byte min = array[0];
/* 106 */     for (int i = 1; i < array.length; i++) {
/* 107 */       if (array[i] < min) {
/* 108 */         min = array[i];
/*     */       }
/*     */     } 
/* 111 */     return min;
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
/* 123 */     Preconditions.checkArgument((array.length > 0));
/* 124 */     byte max = array[0];
/* 125 */     for (int i = 1; i < array.length; i++) {
/* 126 */       if (array[i] > max) {
/* 127 */         max = array[i];
/*     */       }
/*     */     } 
/* 130 */     return max;
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
/*     */   public static String join(String separator, byte... array) {
/* 142 */     Preconditions.checkNotNull(separator);
/* 143 */     if (array.length == 0) {
/* 144 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 148 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 149 */     builder.append(array[0]);
/* 150 */     for (int i = 1; i < array.length; i++) {
/* 151 */       builder.append(separator).append(array[i]);
/*     */     }
/* 153 */     return builder.toString();
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
/* 171 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<byte[]> {
/* 175 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(byte[] left, byte[] right) {
/* 179 */       int minLength = Math.min(left.length, right.length);
/* 180 */       for (int i = 0; i < minLength; i++) {
/* 181 */         int result = SignedBytes.compare(left[i], right[i]);
/* 182 */         if (result != 0) {
/* 183 */           return result;
/*     */         }
/*     */       } 
/* 186 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 191 */       return "SignedBytes.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\SignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */