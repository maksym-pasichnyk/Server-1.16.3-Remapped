/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedInts
/*     */ {
/*     */   static final long INT_MASK = 4294967295L;
/*     */   
/*     */   static int flip(int value) {
/*  55 */     return value ^ Integer.MIN_VALUE;
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
/*     */   public static int compare(int a, int b) {
/*  68 */     return Ints.compare(flip(a), flip(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toLong(int value) {
/*  75 */     return value & 0xFFFFFFFFL;
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
/*     */   public static int checkedCast(long value) {
/*  89 */     Preconditions.checkArgument((value >> 32L == 0L), "out of range: %s", value);
/*  90 */     return (int)value;
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
/*     */   public static int saturatedCast(long value) {
/* 103 */     if (value <= 0L)
/* 104 */       return 0; 
/* 105 */     if (value >= 4294967296L) {
/* 106 */       return -1;
/*     */     }
/* 108 */     return (int)value;
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
/*     */   public static int min(int... array) {
/* 121 */     Preconditions.checkArgument((array.length > 0));
/* 122 */     int min = flip(array[0]);
/* 123 */     for (int i = 1; i < array.length; i++) {
/* 124 */       int next = flip(array[i]);
/* 125 */       if (next < min) {
/* 126 */         min = next;
/*     */       }
/*     */     } 
/* 129 */     return flip(min);
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
/*     */   public static int max(int... array) {
/* 141 */     Preconditions.checkArgument((array.length > 0));
/* 142 */     int max = flip(array[0]);
/* 143 */     for (int i = 1; i < array.length; i++) {
/* 144 */       int next = flip(array[i]);
/* 145 */       if (next > max) {
/* 146 */         max = next;
/*     */       }
/*     */     } 
/* 149 */     return flip(max);
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
/*     */   public static String join(String separator, int... array) {
/* 161 */     Preconditions.checkNotNull(separator);
/* 162 */     if (array.length == 0) {
/* 163 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 167 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 168 */     builder.append(toString(array[0]));
/* 169 */     for (int i = 1; i < array.length; i++) {
/* 170 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 172 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 186 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<int[]> {
/* 190 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 194 */       int minLength = Math.min(left.length, right.length);
/* 195 */       for (int i = 0; i < minLength; i++) {
/* 196 */         if (left[i] != right[i]) {
/* 197 */           return UnsignedInts.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 200 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 205 */       return "UnsignedInts.lexicographicalComparator()";
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
/*     */   public static int divide(int dividend, int divisor) {
/* 218 */     return (int)(toLong(dividend) / toLong(divisor));
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
/*     */   public static int remainder(int dividend, int divisor) {
/* 230 */     return (int)(toLong(dividend) % toLong(divisor));
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int decode(String stringValue) {
/* 250 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 253 */       return parseUnsignedInt(request.rawValue, request.radix);
/* 254 */     } catch (NumberFormatException e) {
/* 255 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 257 */       decodeException.initCause(e);
/* 258 */       throw decodeException;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int parseUnsignedInt(String s) {
/* 271 */     return parseUnsignedInt(s, 10);
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
/*     */   public static int parseUnsignedInt(String string, int radix) {
/* 287 */     Preconditions.checkNotNull(string);
/* 288 */     long result = Long.parseLong(string, radix);
/* 289 */     if ((result & 0xFFFFFFFFL) != result) {
/* 290 */       throw new NumberFormatException("Input " + string + " in base " + radix + " is not in the range of an unsigned integer");
/*     */     }
/*     */     
/* 293 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int x) {
/* 300 */     return toString(x, 10);
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
/*     */   public static String toString(int x, int radix) {
/* 313 */     long asLong = x & 0xFFFFFFFFL;
/* 314 */     return Long.toString(asLong, radix);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\UnsignedInts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */