/*      */ package org.apache.commons.lang3.math;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.SystemUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NumberUtils
/*      */ {
/*   35 */   public static final Long LONG_ZERO = Long.valueOf(0L);
/*      */   
/*   37 */   public static final Long LONG_ONE = Long.valueOf(1L);
/*      */   
/*   39 */   public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
/*      */   
/*   41 */   public static final Integer INTEGER_ZERO = Integer.valueOf(0);
/*      */   
/*   43 */   public static final Integer INTEGER_ONE = Integer.valueOf(1);
/*      */   
/*   45 */   public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
/*      */   
/*   47 */   public static final Short SHORT_ZERO = Short.valueOf((short)0);
/*      */   
/*   49 */   public static final Short SHORT_ONE = Short.valueOf((short)1);
/*      */   
/*   51 */   public static final Short SHORT_MINUS_ONE = Short.valueOf((short)-1);
/*      */   
/*   53 */   public static final Byte BYTE_ZERO = Byte.valueOf((byte)0);
/*      */   
/*   55 */   public static final Byte BYTE_ONE = Byte.valueOf((byte)1);
/*      */   
/*   57 */   public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte)-1);
/*      */   
/*   59 */   public static final Double DOUBLE_ZERO = Double.valueOf(0.0D);
/*      */   
/*   61 */   public static final Double DOUBLE_ONE = Double.valueOf(1.0D);
/*      */   
/*   63 */   public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0D);
/*      */   
/*   65 */   public static final Float FLOAT_ZERO = Float.valueOf(0.0F);
/*      */   
/*   67 */   public static final Float FLOAT_ONE = Float.valueOf(1.0F);
/*      */   
/*   69 */   public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int toInt(String str) {
/*  101 */     return toInt(str, 0);
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
/*      */   public static int toInt(String str, int defaultValue) {
/*  122 */     if (str == null) {
/*  123 */       return defaultValue;
/*      */     }
/*      */     try {
/*  126 */       return Integer.parseInt(str);
/*  127 */     } catch (NumberFormatException nfe) {
/*  128 */       return defaultValue;
/*      */     } 
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
/*      */   public static long toLong(String str) {
/*  150 */     return toLong(str, 0L);
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
/*      */   public static long toLong(String str, long defaultValue) {
/*  171 */     if (str == null) {
/*  172 */       return defaultValue;
/*      */     }
/*      */     try {
/*  175 */       return Long.parseLong(str);
/*  176 */     } catch (NumberFormatException nfe) {
/*  177 */       return defaultValue;
/*      */     } 
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
/*      */   public static float toFloat(String str) {
/*  200 */     return toFloat(str, 0.0F);
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
/*      */   public static float toFloat(String str, float defaultValue) {
/*  223 */     if (str == null) {
/*  224 */       return defaultValue;
/*      */     }
/*      */     try {
/*  227 */       return Float.parseFloat(str);
/*  228 */     } catch (NumberFormatException nfe) {
/*  229 */       return defaultValue;
/*      */     } 
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
/*      */   public static double toDouble(String str) {
/*  252 */     return toDouble(str, 0.0D);
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
/*      */   public static double toDouble(String str, double defaultValue) {
/*  275 */     if (str == null) {
/*  276 */       return defaultValue;
/*      */     }
/*      */     try {
/*  279 */       return Double.parseDouble(str);
/*  280 */     } catch (NumberFormatException nfe) {
/*  281 */       return defaultValue;
/*      */     } 
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
/*      */   public static byte toByte(String str) {
/*  304 */     return toByte(str, (byte)0);
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
/*      */   public static byte toByte(String str, byte defaultValue) {
/*  325 */     if (str == null) {
/*  326 */       return defaultValue;
/*      */     }
/*      */     try {
/*  329 */       return Byte.parseByte(str);
/*  330 */     } catch (NumberFormatException nfe) {
/*  331 */       return defaultValue;
/*      */     } 
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
/*      */   public static short toShort(String str) {
/*  353 */     return toShort(str, (short)0);
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
/*      */   public static short toShort(String str, short defaultValue) {
/*  374 */     if (str == null) {
/*  375 */       return defaultValue;
/*      */     }
/*      */     try {
/*  378 */       return Short.parseShort(str);
/*  379 */     } catch (NumberFormatException nfe) {
/*  380 */       return defaultValue;
/*      */     } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Number createNumber(String str) throws NumberFormatException {
/*      */     String mant, dec, exp;
/*  452 */     if (str == null) {
/*  453 */       return null;
/*      */     }
/*  455 */     if (StringUtils.isBlank(str)) {
/*  456 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/*      */     
/*  459 */     String[] hex_prefixes = { "0x", "0X", "-0x", "-0X", "#", "-#" };
/*  460 */     int pfxLen = 0;
/*  461 */     for (String pfx : hex_prefixes) {
/*  462 */       if (str.startsWith(pfx)) {
/*  463 */         pfxLen += pfx.length();
/*      */         break;
/*      */       } 
/*      */     } 
/*  467 */     if (pfxLen > 0) {
/*  468 */       char firstSigDigit = Character.MIN_VALUE;
/*  469 */       for (int i = pfxLen; i < str.length(); ) {
/*  470 */         firstSigDigit = str.charAt(i);
/*  471 */         if (firstSigDigit == '0') {
/*  472 */           pfxLen++;
/*      */           
/*      */           i++;
/*      */         } 
/*      */       } 
/*  477 */       int hexDigits = str.length() - pfxLen;
/*  478 */       if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) {
/*  479 */         return createBigInteger(str);
/*      */       }
/*  481 */       if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) {
/*  482 */         return createLong(str);
/*      */       }
/*  484 */       return createInteger(str);
/*      */     } 
/*  486 */     char lastChar = str.charAt(str.length() - 1);
/*      */ 
/*      */ 
/*      */     
/*  490 */     int decPos = str.indexOf('.');
/*  491 */     int expPos = str.indexOf('e') + str.indexOf('E') + 1;
/*      */ 
/*      */ 
/*      */     
/*  495 */     if (decPos > -1) {
/*  496 */       if (expPos > -1) {
/*  497 */         if (expPos < decPos || expPos > str.length()) {
/*  498 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  500 */         dec = str.substring(decPos + 1, expPos);
/*      */       } else {
/*  502 */         dec = str.substring(decPos + 1);
/*      */       } 
/*  504 */       mant = getMantissa(str, decPos);
/*      */     } else {
/*  506 */       if (expPos > -1) {
/*  507 */         if (expPos > str.length()) {
/*  508 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         }
/*  510 */         mant = getMantissa(str, expPos);
/*      */       } else {
/*  512 */         mant = getMantissa(str);
/*      */       } 
/*  514 */       dec = null;
/*      */     } 
/*  516 */     if (!Character.isDigit(lastChar) && lastChar != '.') {
/*  517 */       if (expPos > -1 && expPos < str.length() - 1) {
/*  518 */         exp = str.substring(expPos + 1, str.length() - 1);
/*      */       } else {
/*  520 */         exp = null;
/*      */       } 
/*      */       
/*  523 */       String numeric = str.substring(0, str.length() - 1);
/*  524 */       boolean bool = (isAllZeros(mant) && isAllZeros(exp));
/*  525 */       switch (lastChar) {
/*      */         case 'L':
/*      */         case 'l':
/*  528 */           if (dec == null && exp == null && ((numeric
/*      */             
/*  530 */             .charAt(0) == '-' && isDigits(numeric.substring(1))) || isDigits(numeric))) {
/*      */             try {
/*  532 */               return createLong(numeric);
/*  533 */             } catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */               
/*  536 */               return createBigInteger(numeric);
/*      */             } 
/*      */           }
/*  539 */           throw new NumberFormatException(str + " is not a valid number.");
/*      */         case 'F':
/*      */         case 'f':
/*      */           try {
/*  543 */             Float f = createFloat(str);
/*  544 */             if (!f.isInfinite() && (f.floatValue() != 0.0F || bool))
/*      */             {
/*      */               
/*  547 */               return f;
/*      */             }
/*      */           }
/*  550 */           catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */ 
/*      */         
/*      */         case 'D':
/*      */         case 'd':
/*      */           try {
/*  557 */             Double d = createDouble(str);
/*  558 */             if (!d.isInfinite() && (d.floatValue() != 0.0D || bool)) {
/*  559 */               return d;
/*      */             }
/*  561 */           } catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */           
/*      */           try {
/*  565 */             return createBigDecimal(numeric);
/*  566 */           } catch (NumberFormatException numberFormatException) {
/*      */             break;
/*      */           } 
/*      */       } 
/*      */       
/*  571 */       throw new NumberFormatException(str + " is not a valid number.");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  577 */     if (expPos > -1 && expPos < str.length() - 1) {
/*  578 */       exp = str.substring(expPos + 1, str.length());
/*      */     } else {
/*  580 */       exp = null;
/*      */     } 
/*  582 */     if (dec == null && exp == null) {
/*      */       
/*      */       try {
/*  585 */         return createInteger(str);
/*  586 */       } catch (NumberFormatException numberFormatException) {
/*      */ 
/*      */         
/*      */         try {
/*  590 */           return createLong(str);
/*  591 */         } catch (NumberFormatException numberFormatException1) {
/*      */ 
/*      */           
/*  594 */           return createBigInteger(str);
/*      */         } 
/*      */       } 
/*      */     }
/*  598 */     boolean allZeros = (isAllZeros(mant) && isAllZeros(exp));
/*      */     try {
/*  600 */       Float f = createFloat(str);
/*  601 */       Double d = createDouble(str);
/*  602 */       if (!f.isInfinite() && (f
/*  603 */         .floatValue() != 0.0F || allZeros) && f
/*  604 */         .toString().equals(d.toString())) {
/*  605 */         return f;
/*      */       }
/*  607 */       if (!d.isInfinite() && (d.doubleValue() != 0.0D || allZeros)) {
/*  608 */         BigDecimal b = createBigDecimal(str);
/*  609 */         if (b.compareTo(BigDecimal.valueOf(d.doubleValue())) == 0) {
/*  610 */           return d;
/*      */         }
/*  612 */         return b;
/*      */       } 
/*  614 */     } catch (NumberFormatException numberFormatException) {}
/*      */ 
/*      */     
/*  617 */     return createBigDecimal(str);
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
/*      */   private static String getMantissa(String str) {
/*  629 */     return getMantissa(str, str.length());
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
/*      */   private static String getMantissa(String str, int stopPos) {
/*  642 */     char firstChar = str.charAt(0);
/*  643 */     boolean hasSign = (firstChar == '-' || firstChar == '+');
/*      */     
/*  645 */     return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
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
/*      */   private static boolean isAllZeros(String str) {
/*  657 */     if (str == null) {
/*  658 */       return true;
/*      */     }
/*  660 */     for (int i = str.length() - 1; i >= 0; i--) {
/*  661 */       if (str.charAt(i) != '0') {
/*  662 */         return false;
/*      */       }
/*      */     } 
/*  665 */     return (str.length() > 0);
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
/*      */   public static Float createFloat(String str) {
/*  679 */     if (str == null) {
/*  680 */       return null;
/*      */     }
/*  682 */     return Float.valueOf(str);
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
/*      */   public static Double createDouble(String str) {
/*  695 */     if (str == null) {
/*  696 */       return null;
/*      */     }
/*  698 */     return Double.valueOf(str);
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
/*      */   public static Integer createInteger(String str) {
/*  713 */     if (str == null) {
/*  714 */       return null;
/*      */     }
/*      */     
/*  717 */     return Integer.decode(str);
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
/*      */   public static Long createLong(String str) {
/*  732 */     if (str == null) {
/*  733 */       return null;
/*      */     }
/*  735 */     return Long.decode(str);
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
/*      */   public static BigInteger createBigInteger(String str) {
/*  749 */     if (str == null) {
/*  750 */       return null;
/*      */     }
/*  752 */     int pos = 0;
/*  753 */     int radix = 10;
/*  754 */     boolean negate = false;
/*  755 */     if (str.startsWith("-")) {
/*  756 */       negate = true;
/*  757 */       pos = 1;
/*      */     } 
/*  759 */     if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) {
/*  760 */       radix = 16;
/*  761 */       pos += 2;
/*  762 */     } else if (str.startsWith("#", pos)) {
/*  763 */       radix = 16;
/*  764 */       pos++;
/*  765 */     } else if (str.startsWith("0", pos) && str.length() > pos + 1) {
/*  766 */       radix = 8;
/*  767 */       pos++;
/*      */     } 
/*      */     
/*  770 */     BigInteger value = new BigInteger(str.substring(pos), radix);
/*  771 */     return negate ? value.negate() : value;
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
/*      */   public static BigDecimal createBigDecimal(String str) {
/*  784 */     if (str == null) {
/*  785 */       return null;
/*      */     }
/*      */     
/*  788 */     if (StringUtils.isBlank(str)) {
/*  789 */       throw new NumberFormatException("A blank string is not a valid number");
/*      */     }
/*  791 */     if (str.trim().startsWith("--"))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  796 */       throw new NumberFormatException(str + " is not a valid number.");
/*      */     }
/*  798 */     return new BigDecimal(str);
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
/*      */   public static long min(long... array) {
/*  814 */     validateArray(array);
/*      */ 
/*      */     
/*  817 */     long min = array[0];
/*  818 */     for (int i = 1; i < array.length; i++) {
/*  819 */       if (array[i] < min) {
/*  820 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  824 */     return min;
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
/*      */   public static int min(int... array) {
/*  838 */     validateArray(array);
/*      */ 
/*      */     
/*  841 */     int min = array[0];
/*  842 */     for (int j = 1; j < array.length; j++) {
/*  843 */       if (array[j] < min) {
/*  844 */         min = array[j];
/*      */       }
/*      */     } 
/*      */     
/*  848 */     return min;
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
/*      */   public static short min(short... array) {
/*  862 */     validateArray(array);
/*      */ 
/*      */     
/*  865 */     short min = array[0];
/*  866 */     for (int i = 1; i < array.length; i++) {
/*  867 */       if (array[i] < min) {
/*  868 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  872 */     return min;
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
/*      */   public static byte min(byte... array) {
/*  886 */     validateArray(array);
/*      */ 
/*      */     
/*  889 */     byte min = array[0];
/*  890 */     for (int i = 1; i < array.length; i++) {
/*  891 */       if (array[i] < min) {
/*  892 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  896 */     return min;
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
/*      */   public static double min(double... array) {
/*  911 */     validateArray(array);
/*      */ 
/*      */     
/*  914 */     double min = array[0];
/*  915 */     for (int i = 1; i < array.length; i++) {
/*  916 */       if (Double.isNaN(array[i])) {
/*  917 */         return Double.NaN;
/*      */       }
/*  919 */       if (array[i] < min) {
/*  920 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  924 */     return min;
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
/*      */   public static float min(float... array) {
/*  939 */     validateArray(array);
/*      */ 
/*      */     
/*  942 */     float min = array[0];
/*  943 */     for (int i = 1; i < array.length; i++) {
/*  944 */       if (Float.isNaN(array[i])) {
/*  945 */         return Float.NaN;
/*      */       }
/*  947 */       if (array[i] < min) {
/*  948 */         min = array[i];
/*      */       }
/*      */     } 
/*      */     
/*  952 */     return min;
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
/*      */   public static long max(long... array) {
/*  968 */     validateArray(array);
/*      */ 
/*      */     
/*  971 */     long max = array[0];
/*  972 */     for (int j = 1; j < array.length; j++) {
/*  973 */       if (array[j] > max) {
/*  974 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/*  978 */     return max;
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
/*      */   public static int max(int... array) {
/*  992 */     validateArray(array);
/*      */ 
/*      */     
/*  995 */     int max = array[0];
/*  996 */     for (int j = 1; j < array.length; j++) {
/*  997 */       if (array[j] > max) {
/*  998 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1002 */     return max;
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
/*      */   public static short max(short... array) {
/* 1016 */     validateArray(array);
/*      */ 
/*      */     
/* 1019 */     short max = array[0];
/* 1020 */     for (int i = 1; i < array.length; i++) {
/* 1021 */       if (array[i] > max) {
/* 1022 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1026 */     return max;
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
/*      */   public static byte max(byte... array) {
/* 1040 */     validateArray(array);
/*      */ 
/*      */     
/* 1043 */     byte max = array[0];
/* 1044 */     for (int i = 1; i < array.length; i++) {
/* 1045 */       if (array[i] > max) {
/* 1046 */         max = array[i];
/*      */       }
/*      */     } 
/*      */     
/* 1050 */     return max;
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
/*      */   public static double max(double... array) {
/* 1065 */     validateArray(array);
/*      */ 
/*      */     
/* 1068 */     double max = array[0];
/* 1069 */     for (int j = 1; j < array.length; j++) {
/* 1070 */       if (Double.isNaN(array[j])) {
/* 1071 */         return Double.NaN;
/*      */       }
/* 1073 */       if (array[j] > max) {
/* 1074 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1078 */     return max;
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
/*      */   public static float max(float... array) {
/* 1093 */     validateArray(array);
/*      */ 
/*      */     
/* 1096 */     float max = array[0];
/* 1097 */     for (int j = 1; j < array.length; j++) {
/* 1098 */       if (Float.isNaN(array[j])) {
/* 1099 */         return Float.NaN;
/*      */       }
/* 1101 */       if (array[j] > max) {
/* 1102 */         max = array[j];
/*      */       }
/*      */     } 
/*      */     
/* 1106 */     return max;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateArray(Object array) {
/* 1116 */     if (array == null) {
/* 1117 */       throw new IllegalArgumentException("The Array must not be null");
/*      */     }
/* 1119 */     Validate.isTrue((Array.getLength(array) != 0), "Array cannot be empty.", new Object[0]);
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
/*      */   public static long min(long a, long b, long c) {
/* 1133 */     if (b < a) {
/* 1134 */       a = b;
/*      */     }
/* 1136 */     if (c < a) {
/* 1137 */       a = c;
/*      */     }
/* 1139 */     return a;
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
/*      */   public static int min(int a, int b, int c) {
/* 1151 */     if (b < a) {
/* 1152 */       a = b;
/*      */     }
/* 1154 */     if (c < a) {
/* 1155 */       a = c;
/*      */     }
/* 1157 */     return a;
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
/*      */   public static short min(short a, short b, short c) {
/* 1169 */     if (b < a) {
/* 1170 */       a = b;
/*      */     }
/* 1172 */     if (c < a) {
/* 1173 */       a = c;
/*      */     }
/* 1175 */     return a;
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
/*      */   public static byte min(byte a, byte b, byte c) {
/* 1187 */     if (b < a) {
/* 1188 */       a = b;
/*      */     }
/* 1190 */     if (c < a) {
/* 1191 */       a = c;
/*      */     }
/* 1193 */     return a;
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
/*      */   public static double min(double a, double b, double c) {
/* 1209 */     return Math.min(Math.min(a, b), c);
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
/*      */   public static float min(float a, float b, float c) {
/* 1225 */     return Math.min(Math.min(a, b), c);
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
/*      */   public static long max(long a, long b, long c) {
/* 1239 */     if (b > a) {
/* 1240 */       a = b;
/*      */     }
/* 1242 */     if (c > a) {
/* 1243 */       a = c;
/*      */     }
/* 1245 */     return a;
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
/*      */   public static int max(int a, int b, int c) {
/* 1257 */     if (b > a) {
/* 1258 */       a = b;
/*      */     }
/* 1260 */     if (c > a) {
/* 1261 */       a = c;
/*      */     }
/* 1263 */     return a;
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
/*      */   public static short max(short a, short b, short c) {
/* 1275 */     if (b > a) {
/* 1276 */       a = b;
/*      */     }
/* 1278 */     if (c > a) {
/* 1279 */       a = c;
/*      */     }
/* 1281 */     return a;
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
/*      */   public static byte max(byte a, byte b, byte c) {
/* 1293 */     if (b > a) {
/* 1294 */       a = b;
/*      */     }
/* 1296 */     if (c > a) {
/* 1297 */       a = c;
/*      */     }
/* 1299 */     return a;
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
/*      */   public static double max(double a, double b, double c) {
/* 1315 */     return Math.max(Math.max(a, b), c);
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
/*      */   public static float max(float a, float b, float c) {
/* 1331 */     return Math.max(Math.max(a, b), c);
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
/*      */   public static boolean isDigits(String str) {
/* 1346 */     return StringUtils.isNumeric(str);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isNumber(String str) {
/* 1376 */     return isCreatable(str);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCreatable(String str) {
/* 1402 */     if (StringUtils.isEmpty(str)) {
/* 1403 */       return false;
/*      */     }
/* 1405 */     char[] chars = str.toCharArray();
/* 1406 */     int sz = chars.length;
/* 1407 */     boolean hasExp = false;
/* 1408 */     boolean hasDecPoint = false;
/* 1409 */     boolean allowSigns = false;
/* 1410 */     boolean foundDigit = false;
/*      */     
/* 1412 */     int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
/* 1413 */     boolean hasLeadingPlusSign = (start == 1 && chars[0] == '+');
/* 1414 */     if (sz > start + 1 && chars[start] == '0') {
/* 1415 */       if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
/* 1416 */         int j = start + 2;
/* 1417 */         if (j == sz) {
/* 1418 */           return false;
/*      */         }
/*      */         
/* 1421 */         for (; j < chars.length; j++) {
/* 1422 */           if ((chars[j] < '0' || chars[j] > '9') && (chars[j] < 'a' || chars[j] > 'f') && (chars[j] < 'A' || chars[j] > 'F'))
/*      */           {
/*      */             
/* 1425 */             return false;
/*      */           }
/*      */         } 
/* 1428 */         return true;
/* 1429 */       }  if (Character.isDigit(chars[start + 1])) {
/*      */         
/* 1431 */         int j = start + 1;
/* 1432 */         for (; j < chars.length; j++) {
/* 1433 */           if (chars[j] < '0' || chars[j] > '7') {
/* 1434 */             return false;
/*      */           }
/*      */         } 
/* 1437 */         return true;
/*      */       } 
/*      */     } 
/* 1440 */     sz--;
/*      */     
/* 1442 */     int i = start;
/*      */ 
/*      */     
/* 1445 */     while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
/* 1446 */       if (chars[i] >= '0' && chars[i] <= '9') {
/* 1447 */         foundDigit = true;
/* 1448 */         allowSigns = false;
/*      */       }
/* 1450 */       else if (chars[i] == '.') {
/* 1451 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1453 */           return false;
/*      */         }
/* 1455 */         hasDecPoint = true;
/* 1456 */       } else if (chars[i] == 'e' || chars[i] == 'E') {
/*      */         
/* 1458 */         if (hasExp)
/*      */         {
/* 1460 */           return false;
/*      */         }
/* 1462 */         if (!foundDigit) {
/* 1463 */           return false;
/*      */         }
/* 1465 */         hasExp = true;
/* 1466 */         allowSigns = true;
/* 1467 */       } else if (chars[i] == '+' || chars[i] == '-') {
/* 1468 */         if (!allowSigns) {
/* 1469 */           return false;
/*      */         }
/* 1471 */         allowSigns = false;
/* 1472 */         foundDigit = false;
/*      */       } else {
/* 1474 */         return false;
/*      */       } 
/* 1476 */       i++;
/*      */     } 
/* 1478 */     if (i < chars.length) {
/* 1479 */       if (chars[i] >= '0' && chars[i] <= '9') {
/* 1480 */         if (SystemUtils.IS_JAVA_1_6 && hasLeadingPlusSign && !hasDecPoint) {
/* 1481 */           return false;
/*      */         }
/*      */         
/* 1484 */         return true;
/*      */       } 
/* 1486 */       if (chars[i] == 'e' || chars[i] == 'E')
/*      */       {
/* 1488 */         return false;
/*      */       }
/* 1490 */       if (chars[i] == '.') {
/* 1491 */         if (hasDecPoint || hasExp)
/*      */         {
/* 1493 */           return false;
/*      */         }
/*      */         
/* 1496 */         return foundDigit;
/*      */       } 
/* 1498 */       if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F'))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1503 */         return foundDigit;
/*      */       }
/* 1505 */       if (chars[i] == 'l' || chars[i] == 'L')
/*      */       {
/*      */         
/* 1508 */         return (foundDigit && !hasExp && !hasDecPoint);
/*      */       }
/*      */       
/* 1511 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1515 */     return (!allowSigns && foundDigit);
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
/*      */   public static boolean isParsable(String str) {
/* 1536 */     if (StringUtils.isEmpty(str)) {
/* 1537 */       return false;
/*      */     }
/* 1539 */     if (str.charAt(str.length() - 1) == '.') {
/* 1540 */       return false;
/*      */     }
/* 1542 */     if (str.charAt(0) == '-') {
/* 1543 */       if (str.length() == 1) {
/* 1544 */         return false;
/*      */       }
/* 1546 */       return withDecimalsParsing(str, 1);
/*      */     } 
/* 1548 */     return withDecimalsParsing(str, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean withDecimalsParsing(String str, int beginIdx) {
/* 1553 */     int decimalPoints = 0;
/* 1554 */     for (int i = beginIdx; i < str.length(); i++) {
/* 1555 */       boolean isDecimalPoint = (str.charAt(i) == '.');
/* 1556 */       if (isDecimalPoint) {
/* 1557 */         decimalPoints++;
/*      */       }
/* 1559 */       if (decimalPoints > 1) {
/* 1560 */         return false;
/*      */       }
/* 1562 */       if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
/* 1563 */         return false;
/*      */       }
/*      */     } 
/* 1566 */     return true;
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
/*      */   public static int compare(int x, int y) {
/* 1580 */     if (x == y) {
/* 1581 */       return 0;
/*      */     }
/* 1583 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(long x, long y) {
/* 1597 */     if (x == y) {
/* 1598 */       return 0;
/*      */     }
/* 1600 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(short x, short y) {
/* 1614 */     if (x == y) {
/* 1615 */       return 0;
/*      */     }
/* 1617 */     return (x < y) ? -1 : 1;
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
/*      */   public static int compare(byte x, byte y) {
/* 1631 */     return x - y;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\math\NumberUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */