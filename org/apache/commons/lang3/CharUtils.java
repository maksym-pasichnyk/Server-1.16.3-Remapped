/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharUtils
/*     */ {
/*  31 */   private static final String[] CHAR_STRING_ARRAY = new String[128];
/*     */   
/*  33 */   private static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final char LF = '\n';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final char CR = '\r';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  55 */     for (char c = Character.MIN_VALUE; c < CHAR_STRING_ARRAY.length; c = (char)(c + 1)) {
/*  56 */       CHAR_STRING_ARRAY[c] = String.valueOf(c);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static Character toCharacterObject(char ch) {
/*  89 */     return Character.valueOf(ch);
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
/*     */   public static Character toCharacterObject(String str) {
/* 110 */     if (StringUtils.isEmpty(str)) {
/* 111 */       return null;
/*     */     }
/* 113 */     return Character.valueOf(str.charAt(0));
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
/*     */   public static char toChar(Character ch) {
/* 131 */     if (ch == null) {
/* 132 */       throw new IllegalArgumentException("The Character must not be null");
/*     */     }
/* 134 */     return ch.charValue();
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
/*     */   public static char toChar(Character ch, char defaultValue) {
/* 151 */     if (ch == null) {
/* 152 */       return defaultValue;
/*     */     }
/* 154 */     return ch.charValue();
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
/*     */   public static char toChar(String str) {
/* 174 */     if (StringUtils.isEmpty(str)) {
/* 175 */       throw new IllegalArgumentException("The String must not be empty");
/*     */     }
/* 177 */     return str.charAt(0);
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
/*     */   public static char toChar(String str, char defaultValue) {
/* 196 */     if (StringUtils.isEmpty(str)) {
/* 197 */       return defaultValue;
/*     */     }
/* 199 */     return str.charAt(0);
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
/*     */   public static int toIntValue(char ch) {
/* 219 */     if (!isAsciiNumeric(ch)) {
/* 220 */       throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
/*     */     }
/* 222 */     return ch - 48;
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
/*     */   public static int toIntValue(char ch, int defaultValue) {
/* 241 */     if (!isAsciiNumeric(ch)) {
/* 242 */       return defaultValue;
/*     */     }
/* 244 */     return ch - 48;
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
/*     */   public static int toIntValue(Character ch) {
/* 264 */     if (ch == null) {
/* 265 */       throw new IllegalArgumentException("The character must not be null");
/*     */     }
/* 267 */     return toIntValue(ch.charValue());
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
/*     */   public static int toIntValue(Character ch, int defaultValue) {
/* 287 */     if (ch == null) {
/* 288 */       return defaultValue;
/*     */     }
/* 290 */     return toIntValue(ch.charValue(), defaultValue);
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
/*     */   public static String toString(char ch) {
/* 309 */     if (ch < '') {
/* 310 */       return CHAR_STRING_ARRAY[ch];
/*     */     }
/* 312 */     return new String(new char[] { ch });
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
/*     */   public static String toString(Character ch) {
/* 333 */     if (ch == null) {
/* 334 */       return null;
/*     */     }
/* 336 */     return toString(ch.charValue());
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
/*     */   public static String unicodeEscaped(char ch) {
/* 354 */     StringBuilder sb = new StringBuilder(6);
/* 355 */     sb.append("\\u");
/* 356 */     sb.append(HEX_DIGITS[ch >> 12 & 0xF]);
/* 357 */     sb.append(HEX_DIGITS[ch >> 8 & 0xF]);
/* 358 */     sb.append(HEX_DIGITS[ch >> 4 & 0xF]);
/* 359 */     sb.append(HEX_DIGITS[ch & 0xF]);
/* 360 */     return sb.toString();
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
/*     */   public static String unicodeEscaped(Character ch) {
/* 380 */     if (ch == null) {
/* 381 */       return null;
/*     */     }
/* 383 */     return unicodeEscaped(ch.charValue());
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
/*     */   public static boolean isAscii(char ch) {
/* 403 */     return (ch < '');
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
/*     */   public static boolean isAsciiPrintable(char ch) {
/* 422 */     return (ch >= ' ' && ch < '');
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
/*     */   public static boolean isAsciiControl(char ch) {
/* 441 */     return (ch < ' ' || ch == '');
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
/*     */   public static boolean isAsciiAlpha(char ch) {
/* 460 */     return (isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch));
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
/*     */   public static boolean isAsciiAlphaUpper(char ch) {
/* 479 */     return (ch >= 'A' && ch <= 'Z');
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
/*     */   public static boolean isAsciiAlphaLower(char ch) {
/* 498 */     return (ch >= 'a' && ch <= 'z');
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
/*     */   public static boolean isAsciiNumeric(char ch) {
/* 517 */     return (ch >= '0' && ch <= '9');
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
/*     */   public static boolean isAsciiAlphanumeric(char ch) {
/* 536 */     return (isAsciiAlpha(ch) || isAsciiNumeric(ch));
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
/*     */   public static int compare(char x, char y) {
/* 550 */     return x - y;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\CharUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */