/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Ascii
/*     */ {
/*     */   public static final byte NUL = 0;
/*     */   public static final byte SOH = 1;
/*     */   public static final byte STX = 2;
/*     */   public static final byte ETX = 3;
/*     */   public static final byte EOT = 4;
/*     */   public static final byte ENQ = 5;
/*     */   public static final byte ACK = 6;
/*     */   public static final byte BEL = 7;
/*     */   public static final byte BS = 8;
/*     */   public static final byte HT = 9;
/*     */   public static final byte LF = 10;
/*     */   public static final byte NL = 10;
/*     */   public static final byte VT = 11;
/*     */   public static final byte FF = 12;
/*     */   public static final byte CR = 13;
/*     */   public static final byte SO = 14;
/*     */   public static final byte SI = 15;
/*     */   public static final byte DLE = 16;
/*     */   public static final byte DC1 = 17;
/*     */   public static final byte XON = 17;
/*     */   public static final byte DC2 = 18;
/*     */   public static final byte DC3 = 19;
/*     */   public static final byte XOFF = 19;
/*     */   public static final byte DC4 = 20;
/*     */   public static final byte NAK = 21;
/*     */   public static final byte SYN = 22;
/*     */   public static final byte ETB = 23;
/*     */   public static final byte CAN = 24;
/*     */   public static final byte EM = 25;
/*     */   public static final byte SUB = 26;
/*     */   public static final byte ESC = 27;
/*     */   public static final byte FS = 28;
/*     */   public static final byte GS = 29;
/*     */   public static final byte RS = 30;
/*     */   public static final byte US = 31;
/*     */   public static final byte SP = 32;
/*     */   public static final byte SPACE = 32;
/*     */   public static final byte DEL = 127;
/*     */   public static final char MIN = '\000';
/*     */   public static final char MAX = '';
/*     */   
/*     */   public static String toLowerCase(String string) {
/* 403 */     int length = string.length();
/* 404 */     for (int i = 0; i < length; i++) {
/* 405 */       if (isUpperCase(string.charAt(i))) {
/* 406 */         char[] chars = string.toCharArray();
/* 407 */         for (; i < length; i++) {
/* 408 */           char c = chars[i];
/* 409 */           if (isUpperCase(c)) {
/* 410 */             chars[i] = (char)(c ^ 0x20);
/*     */           }
/*     */         } 
/* 413 */         return String.valueOf(chars);
/*     */       } 
/*     */     } 
/* 416 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toLowerCase(CharSequence chars) {
/* 427 */     if (chars instanceof String) {
/* 428 */       return toLowerCase((String)chars);
/*     */     }
/* 430 */     char[] newChars = new char[chars.length()];
/* 431 */     for (int i = 0; i < newChars.length; i++) {
/* 432 */       newChars[i] = toLowerCase(chars.charAt(i));
/*     */     }
/* 434 */     return String.valueOf(newChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toLowerCase(char c) {
/* 442 */     return isUpperCase(c) ? (char)(c ^ 0x20) : c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUpperCase(String string) {
/* 451 */     int length = string.length();
/* 452 */     for (int i = 0; i < length; i++) {
/* 453 */       if (isLowerCase(string.charAt(i))) {
/* 454 */         char[] chars = string.toCharArray();
/* 455 */         for (; i < length; i++) {
/* 456 */           char c = chars[i];
/* 457 */           if (isLowerCase(c)) {
/* 458 */             chars[i] = (char)(c & 0x5F);
/*     */           }
/*     */         } 
/* 461 */         return String.valueOf(chars);
/*     */       } 
/*     */     } 
/* 464 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUpperCase(CharSequence chars) {
/* 475 */     if (chars instanceof String) {
/* 476 */       return toUpperCase((String)chars);
/*     */     }
/* 478 */     char[] newChars = new char[chars.length()];
/* 479 */     for (int i = 0; i < newChars.length; i++) {
/* 480 */       newChars[i] = toUpperCase(chars.charAt(i));
/*     */     }
/* 482 */     return String.valueOf(newChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toUpperCase(char c) {
/* 490 */     return isLowerCase(c) ? (char)(c & 0x5F) : c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLowerCase(char c) {
/* 501 */     return (c >= 'a' && c <= 'z');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpperCase(char c) {
/* 510 */     return (c >= 'A' && c <= 'Z');
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
/*     */   public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
/* 546 */     Preconditions.checkNotNull(seq);
/*     */ 
/*     */     
/* 549 */     int truncationLength = maxLength - truncationIndicator.length();
/*     */ 
/*     */ 
/*     */     
/* 553 */     Preconditions.checkArgument((truncationLength >= 0), "maxLength (%s) must be >= length of the truncation indicator (%s)", maxLength, truncationIndicator
/*     */ 
/*     */ 
/*     */         
/* 557 */         .length());
/*     */     
/* 559 */     if (seq.length() <= maxLength) {
/* 560 */       String string = seq.toString();
/* 561 */       if (string.length() <= maxLength) {
/* 562 */         return string;
/*     */       }
/*     */       
/* 565 */       seq = string;
/*     */     } 
/*     */     
/* 568 */     return (new StringBuilder(maxLength))
/* 569 */       .append(seq, 0, truncationLength)
/* 570 */       .append(truncationIndicator)
/* 571 */       .toString();
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
/*     */   public static boolean equalsIgnoreCase(CharSequence s1, CharSequence s2) {
/* 596 */     int length = s1.length();
/* 597 */     if (s1 == s2) {
/* 598 */       return true;
/*     */     }
/* 600 */     if (length != s2.length()) {
/* 601 */       return false;
/*     */     }
/* 603 */     for (int i = 0; i < length; i++) {
/* 604 */       char c1 = s1.charAt(i);
/* 605 */       char c2 = s2.charAt(i);
/* 606 */       if (c1 != c2) {
/*     */ 
/*     */         
/* 609 */         int alphaIndex = getAlphaIndex(c1);
/*     */ 
/*     */         
/* 612 */         if (alphaIndex >= 26 || alphaIndex != getAlphaIndex(c2))
/*     */         {
/*     */           
/* 615 */           return false; } 
/*     */       } 
/* 617 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getAlphaIndex(char c) {
/* 626 */     return (char)((c | 0x20) - 97);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */