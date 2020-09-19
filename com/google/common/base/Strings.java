/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Strings
/*     */ {
/*     */   public static String nullToEmpty(@Nullable String string) {
/*  41 */     return (string == null) ? "" : string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String emptyToNull(@Nullable String string) {
/*  52 */     return isNullOrEmpty(string) ? null : string;
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
/*     */   public static boolean isNullOrEmpty(@Nullable String string) {
/*  67 */     return Platform.stringIsNullOrEmpty(string);
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
/*     */   public static String padStart(String string, int minLength, char padChar) {
/*  89 */     Preconditions.checkNotNull(string);
/*  90 */     if (string.length() >= minLength) {
/*  91 */       return string;
/*     */     }
/*  93 */     StringBuilder sb = new StringBuilder(minLength);
/*  94 */     for (int i = string.length(); i < minLength; i++) {
/*  95 */       sb.append(padChar);
/*     */     }
/*  97 */     sb.append(string);
/*  98 */     return sb.toString();
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
/*     */   public static String padEnd(String string, int minLength, char padChar) {
/* 120 */     Preconditions.checkNotNull(string);
/* 121 */     if (string.length() >= minLength) {
/* 122 */       return string;
/*     */     }
/* 124 */     StringBuilder sb = new StringBuilder(minLength);
/* 125 */     sb.append(string);
/* 126 */     for (int i = string.length(); i < minLength; i++) {
/* 127 */       sb.append(padChar);
/*     */     }
/* 129 */     return sb.toString();
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
/*     */   public static String repeat(String string, int count) {
/* 143 */     Preconditions.checkNotNull(string);
/*     */     
/* 145 */     if (count <= 1) {
/* 146 */       Preconditions.checkArgument((count >= 0), "invalid count: %s", count);
/* 147 */       return (count == 0) ? "" : string;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     int len = string.length();
/* 152 */     long longSize = len * count;
/* 153 */     int size = (int)longSize;
/* 154 */     if (size != longSize) {
/* 155 */       throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
/*     */     }
/*     */     
/* 158 */     char[] array = new char[size];
/* 159 */     string.getChars(0, len, array, 0);
/*     */     int n;
/* 161 */     for (n = len; n < size - n; n <<= 1) {
/* 162 */       System.arraycopy(array, 0, array, n, n);
/*     */     }
/* 164 */     System.arraycopy(array, 0, array, n, size - n);
/* 165 */     return new String(array);
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
/*     */   public static String commonPrefix(CharSequence a, CharSequence b) {
/* 177 */     Preconditions.checkNotNull(a);
/* 178 */     Preconditions.checkNotNull(b);
/*     */     
/* 180 */     int maxPrefixLength = Math.min(a.length(), b.length());
/* 181 */     int p = 0;
/* 182 */     while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
/* 183 */       p++;
/*     */     }
/* 185 */     if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
/* 186 */       p--;
/*     */     }
/* 188 */     return a.subSequence(0, p).toString();
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
/*     */   public static String commonSuffix(CharSequence a, CharSequence b) {
/* 200 */     Preconditions.checkNotNull(a);
/* 201 */     Preconditions.checkNotNull(b);
/*     */     
/* 203 */     int maxSuffixLength = Math.min(a.length(), b.length());
/* 204 */     int s = 0;
/* 205 */     while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1)) {
/* 206 */       s++;
/*     */     }
/* 208 */     if (validSurrogatePairAt(a, a.length() - s - 1) || 
/* 209 */       validSurrogatePairAt(b, b.length() - s - 1)) {
/* 210 */       s--;
/*     */     }
/* 212 */     return a.subSequence(a.length() - s, a.length()).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean validSurrogatePairAt(CharSequence string, int index) {
/* 221 */     return (index >= 0 && index <= string
/* 222 */       .length() - 2 && 
/* 223 */       Character.isHighSurrogate(string.charAt(index)) && 
/* 224 */       Character.isLowSurrogate(string.charAt(index + 1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */