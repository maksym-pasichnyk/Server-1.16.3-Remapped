/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.util.UUID;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Conversion
/*      */ {
/*   68 */   private static final boolean[] TTTT = new boolean[] { true, true, true, true };
/*   69 */   private static final boolean[] FTTT = new boolean[] { false, true, true, true };
/*   70 */   private static final boolean[] TFTT = new boolean[] { true, false, true, true };
/*   71 */   private static final boolean[] FFTT = new boolean[] { false, false, true, true };
/*   72 */   private static final boolean[] TTFT = new boolean[] { true, true, false, true };
/*   73 */   private static final boolean[] FTFT = new boolean[] { false, true, false, true };
/*   74 */   private static final boolean[] TFFT = new boolean[] { true, false, false, true };
/*   75 */   private static final boolean[] FFFT = new boolean[] { false, false, false, true };
/*   76 */   private static final boolean[] TTTF = new boolean[] { true, true, true, false };
/*   77 */   private static final boolean[] FTTF = new boolean[] { false, true, true, false };
/*   78 */   private static final boolean[] TFTF = new boolean[] { true, false, true, false };
/*   79 */   private static final boolean[] FFTF = new boolean[] { false, false, true, false };
/*   80 */   private static final boolean[] TTFF = new boolean[] { true, true, false, false };
/*   81 */   private static final boolean[] FTFF = new boolean[] { false, true, false, false };
/*   82 */   private static final boolean[] TFFF = new boolean[] { true, false, false, false };
/*   83 */   private static final boolean[] FFFF = new boolean[] { false, false, false, false };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitToInt(char hexDigit) {
/*   98 */     int digit = Character.digit(hexDigit, 16);
/*   99 */     if (digit < 0) {
/*  100 */       throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */     }
/*  102 */     return digit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitMsb0ToInt(char hexDigit) {
/*  118 */     switch (hexDigit) {
/*      */       case '0':
/*  120 */         return 0;
/*      */       case '1':
/*  122 */         return 8;
/*      */       case '2':
/*  124 */         return 4;
/*      */       case '3':
/*  126 */         return 12;
/*      */       case '4':
/*  128 */         return 2;
/*      */       case '5':
/*  130 */         return 10;
/*      */       case '6':
/*  132 */         return 6;
/*      */       case '7':
/*  134 */         return 14;
/*      */       case '8':
/*  136 */         return 1;
/*      */       case '9':
/*  138 */         return 9;
/*      */       case 'A':
/*      */       case 'a':
/*  141 */         return 5;
/*      */       case 'B':
/*      */       case 'b':
/*  144 */         return 13;
/*      */       case 'C':
/*      */       case 'c':
/*  147 */         return 3;
/*      */       case 'D':
/*      */       case 'd':
/*  150 */         return 11;
/*      */       case 'E':
/*      */       case 'e':
/*  153 */         return 7;
/*      */       case 'F':
/*      */       case 'f':
/*  156 */         return 15;
/*      */     } 
/*  158 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitToBinary(char hexDigit) {
/*  176 */     switch (hexDigit) {
/*      */       case '0':
/*  178 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  180 */         return (boolean[])TFFF.clone();
/*      */       case '2':
/*  182 */         return (boolean[])FTFF.clone();
/*      */       case '3':
/*  184 */         return (boolean[])TTFF.clone();
/*      */       case '4':
/*  186 */         return (boolean[])FFTF.clone();
/*      */       case '5':
/*  188 */         return (boolean[])TFTF.clone();
/*      */       case '6':
/*  190 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  192 */         return (boolean[])TTTF.clone();
/*      */       case '8':
/*  194 */         return (boolean[])FFFT.clone();
/*      */       case '9':
/*  196 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  199 */         return (boolean[])FTFT.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  202 */         return (boolean[])TTFT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  205 */         return (boolean[])FFTT.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  208 */         return (boolean[])TFTT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  211 */         return (boolean[])FTTT.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  214 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  216 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitMsb0ToBinary(char hexDigit) {
/*  234 */     switch (hexDigit) {
/*      */       case '0':
/*  236 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  238 */         return (boolean[])FFFT.clone();
/*      */       case '2':
/*  240 */         return (boolean[])FFTF.clone();
/*      */       case '3':
/*  242 */         return (boolean[])FFTT.clone();
/*      */       case '4':
/*  244 */         return (boolean[])FTFF.clone();
/*      */       case '5':
/*  246 */         return (boolean[])FTFT.clone();
/*      */       case '6':
/*  248 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  250 */         return (boolean[])FTTT.clone();
/*      */       case '8':
/*  252 */         return (boolean[])TFFF.clone();
/*      */       case '9':
/*  254 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  257 */         return (boolean[])TFTF.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  260 */         return (boolean[])TFTT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  263 */         return (boolean[])TTFF.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  266 */         return (boolean[])TTFT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  269 */         return (boolean[])TTTF.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  272 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  274 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src) {
/*  293 */     return binaryToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src, int srcPos) {
/*  312 */     if (src.length == 0) {
/*  313 */       throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */     }
/*  315 */     if (src.length > srcPos + 3 && src[srcPos + 3]) {
/*  316 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  317 */         if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  318 */           return src[srcPos] ? 'f' : 'e';
/*      */         }
/*  320 */         return src[srcPos] ? 'd' : 'c';
/*      */       } 
/*  322 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  323 */         return src[srcPos] ? 'b' : 'a';
/*      */       }
/*  325 */       return src[srcPos] ? '9' : '8';
/*      */     } 
/*  327 */     if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  328 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  329 */         return src[srcPos] ? '7' : '6';
/*      */       }
/*  331 */       return src[srcPos] ? '5' : '4';
/*      */     } 
/*  333 */     if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  334 */       return src[srcPos] ? '3' : '2';
/*      */     }
/*  336 */     return src[srcPos] ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src) {
/*  355 */     return binaryToHexDigitMsb0_4bits(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src, int srcPos) {
/*  376 */     if (src.length > 8) {
/*  377 */       throw new IllegalArgumentException("src.length>8: src.length=" + src.length);
/*      */     }
/*  379 */     if (src.length - srcPos < 4) {
/*  380 */       throw new IllegalArgumentException("src.length-srcPos<4: src.length=" + src.length + ", srcPos=" + srcPos);
/*      */     }
/*  382 */     if (src[srcPos + 3]) {
/*  383 */       if (src[srcPos + 2]) {
/*  384 */         if (src[srcPos + 1]) {
/*  385 */           return src[srcPos] ? 'f' : '7';
/*      */         }
/*  387 */         return src[srcPos] ? 'b' : '3';
/*      */       } 
/*  389 */       if (src[srcPos + 1]) {
/*  390 */         return src[srcPos] ? 'd' : '5';
/*      */       }
/*  392 */       return src[srcPos] ? '9' : '1';
/*      */     } 
/*  394 */     if (src[srcPos + 2]) {
/*  395 */       if (src[srcPos + 1]) {
/*  396 */         return src[srcPos] ? 'e' : '6';
/*      */       }
/*  398 */       return src[srcPos] ? 'a' : '2';
/*      */     } 
/*  400 */     if (src[srcPos + 1]) {
/*  401 */       return src[srcPos] ? 'c' : '4';
/*      */     }
/*  403 */     return src[srcPos] ? '8' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src) {
/*  422 */     return binaryBeMsb0ToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src, int srcPos) {
/*  442 */     if (src.length == 0) {
/*  443 */       throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */     }
/*  445 */     int beSrcPos = src.length - 1 - srcPos;
/*  446 */     int srcLen = Math.min(4, beSrcPos + 1);
/*  447 */     boolean[] paddedSrc = new boolean[4];
/*  448 */     System.arraycopy(src, beSrcPos + 1 - srcLen, paddedSrc, 4 - srcLen, srcLen);
/*  449 */     src = paddedSrc;
/*  450 */     srcPos = 0;
/*  451 */     if (src[srcPos]) {
/*  452 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  453 */         if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  454 */           return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'f' : 'e';
/*      */         }
/*  456 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'd' : 'c';
/*      */       } 
/*  458 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  459 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'b' : 'a';
/*      */       }
/*  461 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '9' : '8';
/*      */     } 
/*  463 */     if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  464 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  465 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? '7' : '6';
/*      */       }
/*  467 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '5' : '4';
/*      */     } 
/*  469 */     if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  470 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '3' : '2';
/*      */     }
/*  472 */     return (src.length > srcPos + 3 && src[srcPos + 3]) ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigit(int nibble) {
/*  494 */     char c = Character.forDigit(nibble, 16);
/*  495 */     if (c == '\000') {
/*  496 */       throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */     }
/*  498 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigitMsb0(int nibble) {
/*  520 */     switch (nibble) {
/*      */       case 0:
/*  522 */         return '0';
/*      */       case 1:
/*  524 */         return '8';
/*      */       case 2:
/*  526 */         return '4';
/*      */       case 3:
/*  528 */         return 'c';
/*      */       case 4:
/*  530 */         return '2';
/*      */       case 5:
/*  532 */         return 'a';
/*      */       case 6:
/*  534 */         return '6';
/*      */       case 7:
/*  536 */         return 'e';
/*      */       case 8:
/*  538 */         return '1';
/*      */       case 9:
/*  540 */         return '9';
/*      */       case 10:
/*  542 */         return '5';
/*      */       case 11:
/*  544 */         return 'd';
/*      */       case 12:
/*  546 */         return '3';
/*      */       case 13:
/*  548 */         return 'b';
/*      */       case 14:
/*  550 */         return '7';
/*      */       case 15:
/*  552 */         return 'f';
/*      */     } 
/*  554 */     throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long intArrayToLong(int[] src, int srcPos, long dstInit, int dstPos, int nInts) {
/*  577 */     if ((src.length == 0 && srcPos == 0) || 0 == nInts) {
/*  578 */       return dstInit;
/*      */     }
/*  580 */     if ((nInts - 1) * 32 + dstPos >= 64) {
/*  581 */       throw new IllegalArgumentException("(nInts-1)*32+dstPos is greather or equal to than 64");
/*      */     }
/*  583 */     long out = dstInit;
/*  584 */     for (int i = 0; i < nInts; i++) {
/*  585 */       int shift = i * 32 + dstPos;
/*  586 */       long bits = (0xFFFFFFFFL & src[i + srcPos]) << shift;
/*  587 */       long mask = 4294967295L << shift;
/*  588 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  590 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long shortArrayToLong(short[] src, int srcPos, long dstInit, int dstPos, int nShorts) {
/*  612 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  613 */       return dstInit;
/*      */     }
/*  615 */     if ((nShorts - 1) * 16 + dstPos >= 64) {
/*  616 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 64");
/*      */     }
/*  618 */     long out = dstInit;
/*  619 */     for (int i = 0; i < nShorts; i++) {
/*  620 */       int shift = i * 16 + dstPos;
/*  621 */       long bits = (0xFFFFL & src[i + srcPos]) << shift;
/*  622 */       long mask = 65535L << shift;
/*  623 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  625 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shortArrayToInt(short[] src, int srcPos, int dstInit, int dstPos, int nShorts) {
/*  647 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  648 */       return dstInit;
/*      */     }
/*  650 */     if ((nShorts - 1) * 16 + dstPos >= 32) {
/*  651 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 32");
/*      */     }
/*  653 */     int out = dstInit;
/*  654 */     for (int i = 0; i < nShorts; i++) {
/*  655 */       int shift = i * 16 + dstPos;
/*  656 */       int bits = (0xFFFF & src[i + srcPos]) << shift;
/*  657 */       int mask = 65535 << shift;
/*  658 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  660 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long byteArrayToLong(byte[] src, int srcPos, long dstInit, int dstPos, int nBytes) {
/*  682 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  683 */       return dstInit;
/*      */     }
/*  685 */     if ((nBytes - 1) * 8 + dstPos >= 64) {
/*  686 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 64");
/*      */     }
/*  688 */     long out = dstInit;
/*  689 */     for (int i = 0; i < nBytes; i++) {
/*  690 */       int shift = i * 8 + dstPos;
/*  691 */       long bits = (0xFFL & src[i + srcPos]) << shift;
/*  692 */       long mask = 255L << shift;
/*  693 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  695 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int byteArrayToInt(byte[] src, int srcPos, int dstInit, int dstPos, int nBytes) {
/*  717 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  718 */       return dstInit;
/*      */     }
/*  720 */     if ((nBytes - 1) * 8 + dstPos >= 32) {
/*  721 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 32");
/*      */     }
/*  723 */     int out = dstInit;
/*  724 */     for (int i = 0; i < nBytes; i++) {
/*  725 */       int shift = i * 8 + dstPos;
/*  726 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  727 */       int mask = 255 << shift;
/*  728 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  730 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short byteArrayToShort(byte[] src, int srcPos, short dstInit, int dstPos, int nBytes) {
/*  752 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  753 */       return dstInit;
/*      */     }
/*  755 */     if ((nBytes - 1) * 8 + dstPos >= 16) {
/*  756 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 16");
/*      */     }
/*  758 */     short out = dstInit;
/*  759 */     for (int i = 0; i < nBytes; i++) {
/*  760 */       int shift = i * 8 + dstPos;
/*  761 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  762 */       int mask = 255 << shift;
/*  763 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  765 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long hexToLong(String src, int srcPos, long dstInit, int dstPos, int nHex) {
/*  785 */     if (0 == nHex) {
/*  786 */       return dstInit;
/*      */     }
/*  788 */     if ((nHex - 1) * 4 + dstPos >= 64) {
/*  789 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 64");
/*      */     }
/*  791 */     long out = dstInit;
/*  792 */     for (int i = 0; i < nHex; i++) {
/*  793 */       int shift = i * 4 + dstPos;
/*  794 */       long bits = (0xFL & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  795 */       long mask = 15L << shift;
/*  796 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  798 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexToInt(String src, int srcPos, int dstInit, int dstPos, int nHex) {
/*  817 */     if (0 == nHex) {
/*  818 */       return dstInit;
/*      */     }
/*  820 */     if ((nHex - 1) * 4 + dstPos >= 32) {
/*  821 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 32");
/*      */     }
/*  823 */     int out = dstInit;
/*  824 */     for (int i = 0; i < nHex; i++) {
/*  825 */       int shift = i * 4 + dstPos;
/*  826 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  827 */       int mask = 15 << shift;
/*  828 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  830 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short hexToShort(String src, int srcPos, short dstInit, int dstPos, int nHex) {
/*  850 */     if (0 == nHex) {
/*  851 */       return dstInit;
/*      */     }
/*  853 */     if ((nHex - 1) * 4 + dstPos >= 16) {
/*  854 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 16");
/*      */     }
/*  856 */     short out = dstInit;
/*  857 */     for (int i = 0; i < nHex; i++) {
/*  858 */       int shift = i * 4 + dstPos;
/*  859 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  860 */       int mask = 15 << shift;
/*  861 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  863 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte hexToByte(String src, int srcPos, byte dstInit, int dstPos, int nHex) {
/*  883 */     if (0 == nHex) {
/*  884 */       return dstInit;
/*      */     }
/*  886 */     if ((nHex - 1) * 4 + dstPos >= 8) {
/*  887 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 8");
/*      */     }
/*  889 */     byte out = dstInit;
/*  890 */     for (int i = 0; i < nHex; i++) {
/*  891 */       int shift = i * 4 + dstPos;
/*  892 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  893 */       int mask = 15 << shift;
/*  894 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  896 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binaryToLong(boolean[] src, int srcPos, long dstInit, int dstPos, int nBools) {
/*  918 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  919 */       return dstInit;
/*      */     }
/*  921 */     if (nBools - 1 + dstPos >= 64) {
/*  922 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 64");
/*      */     }
/*  924 */     long out = dstInit;
/*  925 */     for (int i = 0; i < nBools; i++) {
/*  926 */       int shift = i + dstPos;
/*  927 */       long bits = (src[i + srcPos] ? 1L : 0L) << shift;
/*  928 */       long mask = 1L << shift;
/*  929 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  931 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binaryToInt(boolean[] src, int srcPos, int dstInit, int dstPos, int nBools) {
/*  953 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  954 */       return dstInit;
/*      */     }
/*  956 */     if (nBools - 1 + dstPos >= 32) {
/*  957 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 32");
/*      */     }
/*  959 */     int out = dstInit;
/*  960 */     for (int i = 0; i < nBools; i++) {
/*  961 */       int shift = i + dstPos;
/*  962 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  963 */       int mask = 1 << shift;
/*  964 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  966 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short binaryToShort(boolean[] src, int srcPos, short dstInit, int dstPos, int nBools) {
/*  988 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  989 */       return dstInit;
/*      */     }
/*  991 */     if (nBools - 1 + dstPos >= 16) {
/*  992 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 16");
/*      */     }
/*  994 */     short out = dstInit;
/*  995 */     for (int i = 0; i < nBools; i++) {
/*  996 */       int shift = i + dstPos;
/*  997 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  998 */       int mask = 1 << shift;
/*  999 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/* 1001 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte binaryToByte(boolean[] src, int srcPos, byte dstInit, int dstPos, int nBools) {
/* 1023 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/* 1024 */       return dstInit;
/*      */     }
/* 1026 */     if (nBools - 1 + dstPos >= 8) {
/* 1027 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 8");
/*      */     }
/* 1029 */     byte out = dstInit;
/* 1030 */     for (int i = 0; i < nBools; i++) {
/* 1031 */       int shift = i + dstPos;
/* 1032 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/* 1033 */       int mask = 1 << shift;
/* 1034 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/* 1036 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] longToIntArray(long src, int srcPos, int[] dst, int dstPos, int nInts) {
/* 1058 */     if (0 == nInts) {
/* 1059 */       return dst;
/*      */     }
/* 1061 */     if ((nInts - 1) * 32 + srcPos >= 64) {
/* 1062 */       throw new IllegalArgumentException("(nInts-1)*32+srcPos is greather or equal to than 64");
/*      */     }
/* 1064 */     for (int i = 0; i < nInts; i++) {
/* 1065 */       int shift = i * 32 + srcPos;
/* 1066 */       dst[dstPos + i] = (int)(0xFFFFFFFFFFFFFFFFL & src >> shift);
/*      */     } 
/* 1068 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] longToShortArray(long src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1090 */     if (0 == nShorts) {
/* 1091 */       return dst;
/*      */     }
/* 1093 */     if ((nShorts - 1) * 16 + srcPos >= 64) {
/* 1094 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 64");
/*      */     }
/* 1096 */     for (int i = 0; i < nShorts; i++) {
/* 1097 */       int shift = i * 16 + srcPos;
/* 1098 */       dst[dstPos + i] = (short)(int)(0xFFFFL & src >> shift);
/*      */     } 
/* 1100 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] intToShortArray(int src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1122 */     if (0 == nShorts) {
/* 1123 */       return dst;
/*      */     }
/* 1125 */     if ((nShorts - 1) * 16 + srcPos >= 32) {
/* 1126 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 32");
/*      */     }
/* 1128 */     for (int i = 0; i < nShorts; i++) {
/* 1129 */       int shift = i * 16 + srcPos;
/* 1130 */       dst[dstPos + i] = (short)(0xFFFF & src >> shift);
/*      */     } 
/* 1132 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] longToByteArray(long src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1154 */     if (0 == nBytes) {
/* 1155 */       return dst;
/*      */     }
/* 1157 */     if ((nBytes - 1) * 8 + srcPos >= 64) {
/* 1158 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 64");
/*      */     }
/* 1160 */     for (int i = 0; i < nBytes; i++) {
/* 1161 */       int shift = i * 8 + srcPos;
/* 1162 */       dst[dstPos + i] = (byte)(int)(0xFFL & src >> shift);
/*      */     } 
/* 1164 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] intToByteArray(int src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1186 */     if (0 == nBytes) {
/* 1187 */       return dst;
/*      */     }
/* 1189 */     if ((nBytes - 1) * 8 + srcPos >= 32) {
/* 1190 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 32");
/*      */     }
/* 1192 */     for (int i = 0; i < nBytes; i++) {
/* 1193 */       int shift = i * 8 + srcPos;
/* 1194 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1196 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] shortToByteArray(short src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1218 */     if (0 == nBytes) {
/* 1219 */       return dst;
/*      */     }
/* 1221 */     if ((nBytes - 1) * 8 + srcPos >= 16) {
/* 1222 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 16");
/*      */     }
/* 1224 */     for (int i = 0; i < nBytes; i++) {
/* 1225 */       int shift = i * 8 + srcPos;
/* 1226 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1228 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String longToHex(long src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1249 */     if (0 == nHexs) {
/* 1250 */       return dstInit;
/*      */     }
/* 1252 */     if ((nHexs - 1) * 4 + srcPos >= 64) {
/* 1253 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 64");
/*      */     }
/* 1255 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1256 */     int append = sb.length();
/* 1257 */     for (int i = 0; i < nHexs; i++) {
/* 1258 */       int shift = i * 4 + srcPos;
/* 1259 */       int bits = (int)(0xFL & src >> shift);
/* 1260 */       if (dstPos + i == append) {
/* 1261 */         append++;
/* 1262 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1264 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1267 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String intToHex(int src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1288 */     if (0 == nHexs) {
/* 1289 */       return dstInit;
/*      */     }
/* 1291 */     if ((nHexs - 1) * 4 + srcPos >= 32) {
/* 1292 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 32");
/*      */     }
/* 1294 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1295 */     int append = sb.length();
/* 1296 */     for (int i = 0; i < nHexs; i++) {
/* 1297 */       int shift = i * 4 + srcPos;
/* 1298 */       int bits = 0xF & src >> shift;
/* 1299 */       if (dstPos + i == append) {
/* 1300 */         append++;
/* 1301 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1303 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1306 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String shortToHex(short src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1327 */     if (0 == nHexs) {
/* 1328 */       return dstInit;
/*      */     }
/* 1330 */     if ((nHexs - 1) * 4 + srcPos >= 16) {
/* 1331 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 16");
/*      */     }
/* 1333 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1334 */     int append = sb.length();
/* 1335 */     for (int i = 0; i < nHexs; i++) {
/* 1336 */       int shift = i * 4 + srcPos;
/* 1337 */       int bits = 0xF & src >> shift;
/* 1338 */       if (dstPos + i == append) {
/* 1339 */         append++;
/* 1340 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1342 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1345 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String byteToHex(byte src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1366 */     if (0 == nHexs) {
/* 1367 */       return dstInit;
/*      */     }
/* 1369 */     if ((nHexs - 1) * 4 + srcPos >= 8) {
/* 1370 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 8");
/*      */     }
/* 1372 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1373 */     int append = sb.length();
/* 1374 */     for (int i = 0; i < nHexs; i++) {
/* 1375 */       int shift = i * 4 + srcPos;
/* 1376 */       int bits = 0xF & src >> shift;
/* 1377 */       if (dstPos + i == append) {
/* 1378 */         append++;
/* 1379 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1381 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1384 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] longToBinary(long src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1406 */     if (0 == nBools) {
/* 1407 */       return dst;
/*      */     }
/* 1409 */     if (nBools - 1 + srcPos >= 64) {
/* 1410 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 64");
/*      */     }
/* 1412 */     for (int i = 0; i < nBools; i++) {
/* 1413 */       int shift = i + srcPos;
/* 1414 */       dst[dstPos + i] = ((0x1L & src >> shift) != 0L);
/*      */     } 
/* 1416 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] intToBinary(int src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1438 */     if (0 == nBools) {
/* 1439 */       return dst;
/*      */     }
/* 1441 */     if (nBools - 1 + srcPos >= 32) {
/* 1442 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 32");
/*      */     }
/* 1444 */     for (int i = 0; i < nBools; i++) {
/* 1445 */       int shift = i + srcPos;
/* 1446 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1448 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] shortToBinary(short src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1470 */     if (0 == nBools) {
/* 1471 */       return dst;
/*      */     }
/* 1473 */     if (nBools - 1 + srcPos >= 16) {
/* 1474 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 16");
/*      */     }
/* 1476 */     assert nBools - 1 < 16 - srcPos;
/* 1477 */     for (int i = 0; i < nBools; i++) {
/* 1478 */       int shift = i + srcPos;
/* 1479 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1481 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] byteToBinary(byte src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1503 */     if (0 == nBools) {
/* 1504 */       return dst;
/*      */     }
/* 1506 */     if (nBools - 1 + srcPos >= 8) {
/* 1507 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 8");
/*      */     }
/* 1509 */     for (int i = 0; i < nBools; i++) {
/* 1510 */       int shift = i + srcPos;
/* 1511 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1513 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] uuidToByteArray(UUID src, byte[] dst, int dstPos, int nBytes) {
/* 1533 */     if (0 == nBytes) {
/* 1534 */       return dst;
/*      */     }
/* 1536 */     if (nBytes > 16) {
/* 1537 */       throw new IllegalArgumentException("nBytes is greather than 16");
/*      */     }
/* 1539 */     longToByteArray(src.getMostSignificantBits(), 0, dst, dstPos, (nBytes > 8) ? 8 : nBytes);
/* 1540 */     if (nBytes >= 8) {
/* 1541 */       longToByteArray(src.getLeastSignificantBits(), 0, dst, dstPos + 8, nBytes - 8);
/*      */     }
/* 1543 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UUID byteArrayToUuid(byte[] src, int srcPos) {
/* 1560 */     if (src.length - srcPos < 16) {
/* 1561 */       throw new IllegalArgumentException("Need at least 16 bytes for UUID");
/*      */     }
/* 1563 */     return new UUID(byteArrayToLong(src, srcPos, 0L, 0, 8), byteArrayToLong(src, srcPos + 8, 0L, 0, 8));
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\Conversion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */