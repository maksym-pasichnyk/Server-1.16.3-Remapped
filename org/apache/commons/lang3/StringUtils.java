/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.text.Normalizer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   public static final String SPACE = " ";
/*      */   public static final String EMPTY = "";
/*      */   public static final String LF = "\n";
/*      */   public static final String CR = "\r";
/*      */   public static final int INDEX_NOT_FOUND = -1;
/*      */   private static final int PAD_LIMIT = 8192;
/*      */   
/*      */   public static boolean isEmpty(CharSequence cs) {
/*  209 */     return (cs == null || cs.length() == 0);
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
/*      */   public static boolean isNotEmpty(CharSequence cs) {
/*  228 */     return !isEmpty(cs);
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
/*      */   public static boolean isAnyEmpty(CharSequence... css) {
/*  249 */     if (ArrayUtils.isEmpty((Object[])css)) {
/*  250 */       return true;
/*      */     }
/*  252 */     for (CharSequence cs : css) {
/*  253 */       if (isEmpty(cs)) {
/*  254 */         return true;
/*      */       }
/*      */     } 
/*  257 */     return false;
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
/*      */   public static boolean isNoneEmpty(CharSequence... css) {
/*  278 */     return !isAnyEmpty(css);
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
/*      */   public static boolean isBlank(CharSequence cs) {
/*      */     int strLen;
/*  298 */     if (cs == null || (strLen = cs.length()) == 0) {
/*  299 */       return true;
/*      */     }
/*  301 */     for (int i = 0; i < strLen; i++) {
/*  302 */       if (!Character.isWhitespace(cs.charAt(i))) {
/*  303 */         return false;
/*      */       }
/*      */     } 
/*  306 */     return true;
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
/*      */   public static boolean isNotBlank(CharSequence cs) {
/*  327 */     return !isBlank(cs);
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
/*      */   public static boolean isAnyBlank(CharSequence... css) {
/*  349 */     if (ArrayUtils.isEmpty((Object[])css)) {
/*  350 */       return true;
/*      */     }
/*  352 */     for (CharSequence cs : css) {
/*  353 */       if (isBlank(cs)) {
/*  354 */         return true;
/*      */       }
/*      */     } 
/*  357 */     return false;
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
/*      */   public static boolean isNoneBlank(CharSequence... css) {
/*  379 */     return !isAnyBlank(css);
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
/*      */   public static String trim(String str) {
/*  408 */     return (str == null) ? null : str.trim();
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
/*      */   public static String trimToNull(String str) {
/*  434 */     String ts = trim(str);
/*  435 */     return isEmpty(ts) ? null : ts;
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
/*      */   public static String trimToEmpty(String str) {
/*  460 */     return (str == null) ? "" : str.trim();
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
/*      */   public static String truncate(String str, int maxWidth) {
/*  495 */     return truncate(str, 0, maxWidth);
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
/*      */   public static String truncate(String str, int offset, int maxWidth) {
/*  558 */     if (offset < 0) {
/*  559 */       throw new IllegalArgumentException("offset cannot be negative");
/*      */     }
/*  561 */     if (maxWidth < 0) {
/*  562 */       throw new IllegalArgumentException("maxWith cannot be negative");
/*      */     }
/*  564 */     if (str == null) {
/*  565 */       return null;
/*      */     }
/*  567 */     if (offset > str.length()) {
/*  568 */       return "";
/*      */     }
/*  570 */     if (str.length() > maxWidth) {
/*  571 */       int ix = (offset + maxWidth > str.length()) ? str.length() : (offset + maxWidth);
/*  572 */       return str.substring(offset, ix);
/*      */     } 
/*  574 */     return str.substring(offset);
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
/*      */   public static String strip(String str) {
/*  602 */     return strip(str, null);
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
/*      */   public static String stripToNull(String str) {
/*  629 */     if (str == null) {
/*  630 */       return null;
/*      */     }
/*  632 */     str = strip(str, null);
/*  633 */     return str.isEmpty() ? null : str;
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
/*      */   public static String stripToEmpty(String str) {
/*  659 */     return (str == null) ? "" : strip(str, null);
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
/*      */   public static String strip(String str, String stripChars) {
/*  689 */     if (isEmpty(str)) {
/*  690 */       return str;
/*      */     }
/*  692 */     str = stripStart(str, stripChars);
/*  693 */     return stripEnd(str, stripChars);
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
/*      */   public static String stripStart(String str, String stripChars) {
/*      */     int strLen;
/*  722 */     if (str == null || (strLen = str.length()) == 0) {
/*  723 */       return str;
/*      */     }
/*  725 */     int start = 0;
/*  726 */     if (stripChars == null) {
/*  727 */       while (start != strLen && Character.isWhitespace(str.charAt(start)))
/*  728 */         start++; 
/*      */     } else {
/*  730 */       if (stripChars.isEmpty()) {
/*  731 */         return str;
/*      */       }
/*  733 */       while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
/*  734 */         start++;
/*      */       }
/*      */     } 
/*  737 */     return str.substring(start);
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
/*      */   public static String stripEnd(String str, String stripChars) {
/*      */     int end;
/*  767 */     if (str == null || (end = str.length()) == 0) {
/*  768 */       return str;
/*      */     }
/*      */     
/*  771 */     if (stripChars == null) {
/*  772 */       while (end != 0 && Character.isWhitespace(str.charAt(end - 1)))
/*  773 */         end--; 
/*      */     } else {
/*  775 */       if (stripChars.isEmpty()) {
/*  776 */         return str;
/*      */       }
/*  778 */       while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
/*  779 */         end--;
/*      */       }
/*      */     } 
/*  782 */     return str.substring(0, end);
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
/*      */   public static String[] stripAll(String... strs) {
/*  807 */     return stripAll(strs, null);
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
/*      */   public static String[] stripAll(String[] strs, String stripChars) {
/*      */     int strsLen;
/*  837 */     if (strs == null || (strsLen = strs.length) == 0) {
/*  838 */       return strs;
/*      */     }
/*  840 */     String[] newArr = new String[strsLen];
/*  841 */     for (int i = 0; i < strsLen; i++) {
/*  842 */       newArr[i] = strip(strs[i], stripChars);
/*      */     }
/*  844 */     return newArr;
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
/*      */   public static String stripAccents(String input) {
/*  866 */     if (input == null) {
/*  867 */       return null;
/*      */     }
/*  869 */     Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
/*  870 */     StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
/*  871 */     convertRemainingAccentCharacters(decomposed);
/*      */     
/*  873 */     return pattern.matcher(decomposed).replaceAll("");
/*      */   }
/*      */   
/*      */   private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
/*  877 */     for (int i = 0; i < decomposed.length(); i++) {
/*  878 */       if (decomposed.charAt(i) == 'Ł') {
/*  879 */         decomposed.deleteCharAt(i);
/*  880 */         decomposed.insert(i, 'L');
/*  881 */       } else if (decomposed.charAt(i) == 'ł') {
/*  882 */         decomposed.deleteCharAt(i);
/*  883 */         decomposed.insert(i, 'l');
/*      */       } 
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
/*      */   public static boolean equals(CharSequence cs1, CharSequence cs2) {
/*  912 */     if (cs1 == cs2) {
/*  913 */       return true;
/*      */     }
/*  915 */     if (cs1 == null || cs2 == null) {
/*  916 */       return false;
/*      */     }
/*  918 */     if (cs1.length() != cs2.length()) {
/*  919 */       return false;
/*      */     }
/*  921 */     if (cs1 instanceof String && cs2 instanceof String) {
/*  922 */       return cs1.equals(cs2);
/*      */     }
/*  924 */     return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, cs1.length());
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
/*      */   public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
/*  949 */     if (str1 == null || str2 == null)
/*  950 */       return (str1 == str2); 
/*  951 */     if (str1 == str2)
/*  952 */       return true; 
/*  953 */     if (str1.length() != str2.length()) {
/*  954 */       return false;
/*      */     }
/*  956 */     return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
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
/*      */   public static int compare(String str1, String str2) {
/*  995 */     return compare(str1, str2, true);
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
/*      */   public static int compare(String str1, String str2, boolean nullIsLess) {
/* 1033 */     if (str1 == str2) {
/* 1034 */       return 0;
/*      */     }
/* 1036 */     if (str1 == null) {
/* 1037 */       return nullIsLess ? -1 : 1;
/*      */     }
/* 1039 */     if (str2 == null) {
/* 1040 */       return nullIsLess ? 1 : -1;
/*      */     }
/* 1042 */     return str1.compareTo(str2);
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
/*      */   public static int compareIgnoreCase(String str1, String str2) {
/* 1083 */     return compareIgnoreCase(str1, str2, true);
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
/*      */   public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
/* 1126 */     if (str1 == str2) {
/* 1127 */       return 0;
/*      */     }
/* 1129 */     if (str1 == null) {
/* 1130 */       return nullIsLess ? -1 : 1;
/*      */     }
/* 1132 */     if (str2 == null) {
/* 1133 */       return nullIsLess ? 1 : -1;
/*      */     }
/* 1135 */     return str1.compareToIgnoreCase(str2);
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
/*      */   public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
/* 1158 */     if (ArrayUtils.isNotEmpty(searchStrings)) {
/* 1159 */       for (CharSequence next : searchStrings) {
/* 1160 */         if (equals(string, next)) {
/* 1161 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 1165 */     return false;
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
/*      */   public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
/* 1189 */     if (ArrayUtils.isNotEmpty(searchStrings)) {
/* 1190 */       for (CharSequence next : searchStrings) {
/* 1191 */         if (equalsIgnoreCase(string, next)) {
/* 1192 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/* 1196 */     return false;
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
/*      */   public static int indexOf(CharSequence seq, int searchChar) {
/* 1222 */     if (isEmpty(seq)) {
/* 1223 */       return -1;
/*      */     }
/* 1225 */     return CharSequenceUtils.indexOf(seq, searchChar, 0);
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
/*      */   public static int indexOf(CharSequence seq, int searchChar, int startPos) {
/* 1255 */     if (isEmpty(seq)) {
/* 1256 */       return -1;
/*      */     }
/* 1258 */     return CharSequenceUtils.indexOf(seq, searchChar, startPos);
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq) {
/* 1286 */     if (seq == null || searchSeq == null) {
/* 1287 */       return -1;
/*      */     }
/* 1289 */     return CharSequenceUtils.indexOf(seq, searchSeq, 0);
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
/*      */   public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/* 1326 */     if (seq == null || searchSeq == null) {
/* 1327 */       return -1;
/*      */     }
/* 1329 */     return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
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
/*      */   public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/* 1383 */     return ordinalIndexOf(str, searchStr, ordinal, false);
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
/*      */   private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
/* 1402 */     if (str == null || searchStr == null || ordinal <= 0) {
/* 1403 */       return -1;
/*      */     }
/* 1405 */     if (searchStr.length() == 0) {
/* 1406 */       return lastIndex ? str.length() : 0;
/*      */     }
/* 1408 */     int found = 0;
/*      */ 
/*      */     
/* 1411 */     int index = lastIndex ? str.length() : -1;
/*      */     while (true) {
/* 1413 */       if (lastIndex) {
/* 1414 */         index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
/*      */       } else {
/* 1416 */         index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
/*      */       } 
/* 1418 */       if (index < 0) {
/* 1419 */         return index;
/*      */       }
/* 1421 */       found++;
/* 1422 */       if (found >= ordinal) {
/* 1423 */         return index;
/*      */       }
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1452 */     return indexOfIgnoreCase(str, searchStr, 0);
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
/*      */   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 1488 */     if (str == null || searchStr == null) {
/* 1489 */       return -1;
/*      */     }
/* 1491 */     if (startPos < 0) {
/* 1492 */       startPos = 0;
/*      */     }
/* 1494 */     int endLimit = str.length() - searchStr.length() + 1;
/* 1495 */     if (startPos > endLimit) {
/* 1496 */       return -1;
/*      */     }
/* 1498 */     if (searchStr.length() == 0) {
/* 1499 */       return startPos;
/*      */     }
/* 1501 */     for (int i = startPos; i < endLimit; i++) {
/* 1502 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
/* 1503 */         return i;
/*      */       }
/*      */     } 
/* 1506 */     return -1;
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
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar) {
/* 1532 */     if (isEmpty(seq)) {
/* 1533 */       return -1;
/*      */     }
/* 1535 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
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
/*      */   public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
/* 1570 */     if (isEmpty(seq)) {
/* 1571 */       return -1;
/*      */     }
/* 1573 */     return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
/* 1600 */     if (seq == null || searchSeq == null) {
/* 1601 */       return -1;
/*      */     }
/* 1603 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
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
/*      */   public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
/* 1641 */     return ordinalIndexOf(str, searchStr, ordinal, true);
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
/*      */   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
/* 1681 */     if (seq == null || searchSeq == null) {
/* 1682 */       return -1;
/*      */     }
/* 1684 */     return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1711 */     if (str == null || searchStr == null) {
/* 1712 */       return -1;
/*      */     }
/* 1714 */     return lastIndexOfIgnoreCase(str, searchStr, str.length());
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
/*      */   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
/* 1750 */     if (str == null || searchStr == null) {
/* 1751 */       return -1;
/*      */     }
/* 1753 */     if (startPos > str.length() - searchStr.length()) {
/* 1754 */       startPos = str.length() - searchStr.length();
/*      */     }
/* 1756 */     if (startPos < 0) {
/* 1757 */       return -1;
/*      */     }
/* 1759 */     if (searchStr.length() == 0) {
/* 1760 */       return startPos;
/*      */     }
/*      */     
/* 1763 */     for (int i = startPos; i >= 0; i--) {
/* 1764 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
/* 1765 */         return i;
/*      */       }
/*      */     } 
/* 1768 */     return -1;
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
/*      */   public static boolean contains(CharSequence seq, int searchChar) {
/* 1794 */     if (isEmpty(seq)) {
/* 1795 */       return false;
/*      */     }
/* 1797 */     return (CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0);
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
/*      */   public static boolean contains(CharSequence seq, CharSequence searchSeq) {
/* 1823 */     if (seq == null || searchSeq == null) {
/* 1824 */       return false;
/*      */     }
/* 1826 */     return (CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0);
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
/*      */   public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
/* 1854 */     if (str == null || searchStr == null) {
/* 1855 */       return false;
/*      */     }
/* 1857 */     int len = searchStr.length();
/* 1858 */     int max = str.length() - len;
/* 1859 */     for (int i = 0; i <= max; i++) {
/* 1860 */       if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
/* 1861 */         return true;
/*      */       }
/*      */     } 
/* 1864 */     return false;
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
/*      */   public static boolean containsWhitespace(CharSequence seq) {
/* 1877 */     if (isEmpty(seq)) {
/* 1878 */       return false;
/*      */     }
/* 1880 */     int strLen = seq.length();
/* 1881 */     for (int i = 0; i < strLen; i++) {
/* 1882 */       if (Character.isWhitespace(seq.charAt(i))) {
/* 1883 */         return true;
/*      */       }
/*      */     } 
/* 1886 */     return false;
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
/*      */   public static int indexOfAny(CharSequence cs, char... searchChars) {
/* 1915 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1916 */       return -1;
/*      */     }
/* 1918 */     int csLen = cs.length();
/* 1919 */     int csLast = csLen - 1;
/* 1920 */     int searchLen = searchChars.length;
/* 1921 */     int searchLast = searchLen - 1;
/* 1922 */     for (int i = 0; i < csLen; i++) {
/* 1923 */       char ch = cs.charAt(i);
/* 1924 */       for (int j = 0; j < searchLen; j++) {
/* 1925 */         if (searchChars[j] == ch) {
/* 1926 */           if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
/*      */             
/* 1928 */             if (searchChars[j + 1] == cs.charAt(i + 1)) {
/* 1929 */               return i;
/*      */             }
/*      */           } else {
/* 1932 */             return i;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 1937 */     return -1;
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
/*      */   public static int indexOfAny(CharSequence cs, String searchChars) {
/* 1964 */     if (isEmpty(cs) || isEmpty(searchChars)) {
/* 1965 */       return -1;
/*      */     }
/* 1967 */     return indexOfAny(cs, searchChars.toCharArray());
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
/*      */   public static boolean containsAny(CharSequence cs, char... searchChars) {
/* 1998 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 1999 */       return false;
/*      */     }
/* 2001 */     int csLength = cs.length();
/* 2002 */     int searchLength = searchChars.length;
/* 2003 */     int csLast = csLength - 1;
/* 2004 */     int searchLast = searchLength - 1;
/* 2005 */     for (int i = 0; i < csLength; i++) {
/* 2006 */       char ch = cs.charAt(i);
/* 2007 */       for (int j = 0; j < searchLength; j++) {
/* 2008 */         if (searchChars[j] == ch) {
/* 2009 */           if (Character.isHighSurrogate(ch)) {
/* 2010 */             if (j == searchLast)
/*      */             {
/* 2012 */               return true;
/*      */             }
/* 2014 */             if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 2015 */               return true;
/*      */             }
/*      */           } else {
/*      */             
/* 2019 */             return true;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 2024 */     return false;
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
/* 2059 */     if (searchChars == null) {
/* 2060 */       return false;
/*      */     }
/* 2062 */     return containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
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
/*      */   public static boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
/* 2091 */     if (isEmpty(cs) || ArrayUtils.isEmpty((Object[])searchCharSequences)) {
/* 2092 */       return false;
/*      */     }
/* 2094 */     for (CharSequence searchCharSequence : searchCharSequences) {
/* 2095 */       if (contains(cs, searchCharSequence)) {
/* 2096 */         return true;
/*      */       }
/*      */     } 
/* 2099 */     return false;
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
/*      */   public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
/* 2129 */     if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
/* 2130 */       return -1;
/*      */     }
/* 2132 */     int csLen = cs.length();
/* 2133 */     int csLast = csLen - 1;
/* 2134 */     int searchLen = searchChars.length;
/* 2135 */     int searchLast = searchLen - 1;
/*      */     
/* 2137 */     for (int i = 0; i < csLen; i++) {
/* 2138 */       char ch = cs.charAt(i);
/* 2139 */       int j = 0; while (true) { if (j < searchLen) {
/* 2140 */           if (searchChars[j] == ch && (
/* 2141 */             i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch) || 
/* 2142 */             searchChars[j + 1] == cs.charAt(i + 1))) {
/*      */             break;
/*      */           }
/*      */           
/*      */           j++;
/*      */           
/*      */           continue;
/*      */         } 
/* 2150 */         return i; }
/*      */     
/* 2152 */     }  return -1;
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
/*      */   public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
/* 2179 */     if (isEmpty(seq) || isEmpty(searchChars)) {
/* 2180 */       return -1;
/*      */     }
/* 2182 */     int strLen = seq.length();
/* 2183 */     for (int i = 0; i < strLen; i++) {
/* 2184 */       char ch = seq.charAt(i);
/* 2185 */       boolean chFound = (CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0);
/* 2186 */       if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
/* 2187 */         char ch2 = seq.charAt(i + 1);
/* 2188 */         if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
/* 2189 */           return i;
/*      */         }
/*      */       }
/* 2192 */       else if (!chFound) {
/* 2193 */         return i;
/*      */       } 
/*      */     } 
/*      */     
/* 2197 */     return -1;
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
/*      */   public static boolean containsOnly(CharSequence cs, char... valid) {
/* 2226 */     if (valid == null || cs == null) {
/* 2227 */       return false;
/*      */     }
/* 2229 */     if (cs.length() == 0) {
/* 2230 */       return true;
/*      */     }
/* 2232 */     if (valid.length == 0) {
/* 2233 */       return false;
/*      */     }
/* 2235 */     return (indexOfAnyBut(cs, valid) == -1);
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
/*      */   public static boolean containsOnly(CharSequence cs, String validChars) {
/* 2262 */     if (cs == null || validChars == null) {
/* 2263 */       return false;
/*      */     }
/* 2265 */     return containsOnly(cs, validChars.toCharArray());
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
/*      */   public static boolean containsNone(CharSequence cs, char... searchChars) {
/* 2294 */     if (cs == null || searchChars == null) {
/* 2295 */       return true;
/*      */     }
/* 2297 */     int csLen = cs.length();
/* 2298 */     int csLast = csLen - 1;
/* 2299 */     int searchLen = searchChars.length;
/* 2300 */     int searchLast = searchLen - 1;
/* 2301 */     for (int i = 0; i < csLen; i++) {
/* 2302 */       char ch = cs.charAt(i);
/* 2303 */       for (int j = 0; j < searchLen; j++) {
/* 2304 */         if (searchChars[j] == ch) {
/* 2305 */           if (Character.isHighSurrogate(ch)) {
/* 2306 */             if (j == searchLast)
/*      */             {
/* 2308 */               return false;
/*      */             }
/* 2310 */             if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
/* 2311 */               return false;
/*      */             }
/*      */           } else {
/*      */             
/* 2315 */             return false;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/* 2320 */     return true;
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
/*      */   public static boolean containsNone(CharSequence cs, String invalidChars) {
/* 2347 */     if (cs == null || invalidChars == null) {
/* 2348 */       return true;
/*      */     }
/* 2350 */     return containsNone(cs, invalidChars.toCharArray());
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
/*      */   public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 2383 */     if (str == null || searchStrs == null) {
/* 2384 */       return -1;
/*      */     }
/* 2386 */     int sz = searchStrs.length;
/*      */ 
/*      */     
/* 2389 */     int ret = Integer.MAX_VALUE;
/*      */     
/* 2391 */     int tmp = 0;
/* 2392 */     for (int i = 0; i < sz; i++) {
/* 2393 */       CharSequence search = searchStrs[i];
/* 2394 */       if (search != null) {
/*      */ 
/*      */         
/* 2397 */         tmp = CharSequenceUtils.indexOf(str, search, 0);
/* 2398 */         if (tmp != -1)
/*      */         {
/*      */ 
/*      */           
/* 2402 */           if (tmp < ret)
/* 2403 */             ret = tmp; 
/*      */         }
/*      */       } 
/*      */     } 
/* 2407 */     return (ret == Integer.MAX_VALUE) ? -1 : ret;
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
/*      */   public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
/* 2437 */     if (str == null || searchStrs == null) {
/* 2438 */       return -1;
/*      */     }
/* 2440 */     int sz = searchStrs.length;
/* 2441 */     int ret = -1;
/* 2442 */     int tmp = 0;
/* 2443 */     for (int i = 0; i < sz; i++) {
/* 2444 */       CharSequence search = searchStrs[i];
/* 2445 */       if (search != null) {
/*      */ 
/*      */         
/* 2448 */         tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
/* 2449 */         if (tmp > ret)
/* 2450 */           ret = tmp; 
/*      */       } 
/*      */     } 
/* 2453 */     return ret;
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
/*      */   public static String substring(String str, int start) {
/* 2483 */     if (str == null) {
/* 2484 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2488 */     if (start < 0) {
/* 2489 */       start = str.length() + start;
/*      */     }
/*      */     
/* 2492 */     if (start < 0) {
/* 2493 */       start = 0;
/*      */     }
/* 2495 */     if (start > str.length()) {
/* 2496 */       return "";
/*      */     }
/*      */     
/* 2499 */     return str.substring(start);
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
/*      */   public static String substring(String str, int start, int end) {
/* 2538 */     if (str == null) {
/* 2539 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2543 */     if (end < 0) {
/* 2544 */       end = str.length() + end;
/*      */     }
/* 2546 */     if (start < 0) {
/* 2547 */       start = str.length() + start;
/*      */     }
/*      */ 
/*      */     
/* 2551 */     if (end > str.length()) {
/* 2552 */       end = str.length();
/*      */     }
/*      */ 
/*      */     
/* 2556 */     if (start > end) {
/* 2557 */       return "";
/*      */     }
/*      */     
/* 2560 */     if (start < 0) {
/* 2561 */       start = 0;
/*      */     }
/* 2563 */     if (end < 0) {
/* 2564 */       end = 0;
/*      */     }
/*      */     
/* 2567 */     return str.substring(start, end);
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
/*      */   public static String left(String str, int len) {
/* 2593 */     if (str == null) {
/* 2594 */       return null;
/*      */     }
/* 2596 */     if (len < 0) {
/* 2597 */       return "";
/*      */     }
/* 2599 */     if (str.length() <= len) {
/* 2600 */       return str;
/*      */     }
/* 2602 */     return str.substring(0, len);
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
/*      */   public static String right(String str, int len) {
/* 2626 */     if (str == null) {
/* 2627 */       return null;
/*      */     }
/* 2629 */     if (len < 0) {
/* 2630 */       return "";
/*      */     }
/* 2632 */     if (str.length() <= len) {
/* 2633 */       return str;
/*      */     }
/* 2635 */     return str.substring(str.length() - len);
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
/*      */   public static String mid(String str, int pos, int len) {
/* 2664 */     if (str == null) {
/* 2665 */       return null;
/*      */     }
/* 2667 */     if (len < 0 || pos > str.length()) {
/* 2668 */       return "";
/*      */     }
/* 2670 */     if (pos < 0) {
/* 2671 */       pos = 0;
/*      */     }
/* 2673 */     if (str.length() <= pos + len) {
/* 2674 */       return str.substring(pos);
/*      */     }
/* 2676 */     return str.substring(pos, pos + len);
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
/*      */   public static String substringBefore(String str, String separator) {
/* 2709 */     if (isEmpty(str) || separator == null) {
/* 2710 */       return str;
/*      */     }
/* 2712 */     if (separator.isEmpty()) {
/* 2713 */       return "";
/*      */     }
/* 2715 */     int pos = str.indexOf(separator);
/* 2716 */     if (pos == -1) {
/* 2717 */       return str;
/*      */     }
/* 2719 */     return str.substring(0, pos);
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
/*      */   public static String substringAfter(String str, String separator) {
/* 2751 */     if (isEmpty(str)) {
/* 2752 */       return str;
/*      */     }
/* 2754 */     if (separator == null) {
/* 2755 */       return "";
/*      */     }
/* 2757 */     int pos = str.indexOf(separator);
/* 2758 */     if (pos == -1) {
/* 2759 */       return "";
/*      */     }
/* 2761 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringBeforeLast(String str, String separator) {
/* 2792 */     if (isEmpty(str) || isEmpty(separator)) {
/* 2793 */       return str;
/*      */     }
/* 2795 */     int pos = str.lastIndexOf(separator);
/* 2796 */     if (pos == -1) {
/* 2797 */       return str;
/*      */     }
/* 2799 */     return str.substring(0, pos);
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
/*      */   public static String substringAfterLast(String str, String separator) {
/* 2832 */     if (isEmpty(str)) {
/* 2833 */       return str;
/*      */     }
/* 2835 */     if (isEmpty(separator)) {
/* 2836 */       return "";
/*      */     }
/* 2838 */     int pos = str.lastIndexOf(separator);
/* 2839 */     if (pos == -1 || pos == str.length() - separator.length()) {
/* 2840 */       return "";
/*      */     }
/* 2842 */     return str.substring(pos + separator.length());
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
/*      */   public static String substringBetween(String str, String tag) {
/* 2869 */     return substringBetween(str, tag, tag);
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
/*      */   public static String substringBetween(String str, String open, String close) {
/* 2900 */     if (str == null || open == null || close == null) {
/* 2901 */       return null;
/*      */     }
/* 2903 */     int start = str.indexOf(open);
/* 2904 */     if (start != -1) {
/* 2905 */       int end = str.indexOf(close, start + open.length());
/* 2906 */       if (end != -1) {
/* 2907 */         return str.substring(start + open.length(), end);
/*      */       }
/*      */     } 
/* 2910 */     return null;
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
/*      */   public static String[] substringsBetween(String str, String open, String close) {
/* 2936 */     if (str == null || isEmpty(open) || isEmpty(close)) {
/* 2937 */       return null;
/*      */     }
/* 2939 */     int strLen = str.length();
/* 2940 */     if (strLen == 0) {
/* 2941 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 2943 */     int closeLen = close.length();
/* 2944 */     int openLen = open.length();
/* 2945 */     List<String> list = new ArrayList<String>();
/* 2946 */     int pos = 0;
/* 2947 */     while (pos < strLen - closeLen) {
/* 2948 */       int start = str.indexOf(open, pos);
/* 2949 */       if (start < 0) {
/*      */         break;
/*      */       }
/* 2952 */       start += openLen;
/* 2953 */       int end = str.indexOf(close, start);
/* 2954 */       if (end < 0) {
/*      */         break;
/*      */       }
/* 2957 */       list.add(str.substring(start, end));
/* 2958 */       pos = end + closeLen;
/*      */     } 
/* 2960 */     if (list.isEmpty()) {
/* 2961 */       return null;
/*      */     }
/* 2963 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] split(String str) {
/* 2994 */     return split(str, null, -1);
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
/*      */   public static String[] split(String str, char separatorChar) {
/* 3022 */     return splitWorker(str, separatorChar, false);
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
/*      */   public static String[] split(String str, String separatorChars) {
/* 3051 */     return splitWorker(str, separatorChars, -1, false);
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
/*      */   public static String[] split(String str, String separatorChars, int max) {
/* 3085 */     return splitWorker(str, separatorChars, max, false);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator) {
/* 3112 */     return splitByWholeSeparatorWorker(str, separator, -1, false);
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
/*      */   public static String[] splitByWholeSeparator(String str, String separator, int max) {
/* 3143 */     return splitByWholeSeparatorWorker(str, separator, max, false);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
/* 3172 */     return splitByWholeSeparatorWorker(str, separator, -1, true);
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
/*      */   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
/* 3205 */     return splitByWholeSeparatorWorker(str, separator, max, true);
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
/*      */   private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
/* 3224 */     if (str == null) {
/* 3225 */       return null;
/*      */     }
/*      */     
/* 3228 */     int len = str.length();
/*      */     
/* 3230 */     if (len == 0) {
/* 3231 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 3234 */     if (separator == null || "".equals(separator))
/*      */     {
/* 3236 */       return splitWorker(str, null, max, preserveAllTokens);
/*      */     }
/*      */     
/* 3239 */     int separatorLength = separator.length();
/*      */     
/* 3241 */     ArrayList<String> substrings = new ArrayList<String>();
/* 3242 */     int numberOfSubstrings = 0;
/* 3243 */     int beg = 0;
/* 3244 */     int end = 0;
/* 3245 */     while (end < len) {
/* 3246 */       end = str.indexOf(separator, beg);
/*      */       
/* 3248 */       if (end > -1) {
/* 3249 */         if (end > beg) {
/* 3250 */           numberOfSubstrings++;
/*      */           
/* 3252 */           if (numberOfSubstrings == max) {
/* 3253 */             end = len;
/* 3254 */             substrings.add(str.substring(beg));
/*      */             
/*      */             continue;
/*      */           } 
/* 3258 */           substrings.add(str.substring(beg, end));
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3263 */           beg = end + separatorLength;
/*      */           
/*      */           continue;
/*      */         } 
/* 3267 */         if (preserveAllTokens) {
/* 3268 */           numberOfSubstrings++;
/* 3269 */           if (numberOfSubstrings == max) {
/* 3270 */             end = len;
/* 3271 */             substrings.add(str.substring(beg));
/*      */           } else {
/* 3273 */             substrings.add("");
/*      */           } 
/*      */         } 
/* 3276 */         beg = end + separatorLength;
/*      */         
/*      */         continue;
/*      */       } 
/* 3280 */       substrings.add(str.substring(beg));
/* 3281 */       end = len;
/*      */     } 
/*      */ 
/*      */     
/* 3285 */     return substrings.<String>toArray(new String[substrings.size()]);
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
/*      */   public static String[] splitPreserveAllTokens(String str) {
/* 3314 */     return splitWorker(str, null, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, char separatorChar) {
/* 3350 */     return splitWorker(str, separatorChar, true);
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
/*      */   private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
/* 3368 */     if (str == null) {
/* 3369 */       return null;
/*      */     }
/* 3371 */     int len = str.length();
/* 3372 */     if (len == 0) {
/* 3373 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 3375 */     List<String> list = new ArrayList<String>();
/* 3376 */     int i = 0, start = 0;
/* 3377 */     boolean match = false;
/* 3378 */     boolean lastMatch = false;
/* 3379 */     while (i < len) {
/* 3380 */       if (str.charAt(i) == separatorChar) {
/* 3381 */         if (match || preserveAllTokens) {
/* 3382 */           list.add(str.substring(start, i));
/* 3383 */           match = false;
/* 3384 */           lastMatch = true;
/*      */         } 
/* 3386 */         start = ++i;
/*      */         continue;
/*      */       } 
/* 3389 */       lastMatch = false;
/* 3390 */       match = true;
/* 3391 */       i++;
/*      */     } 
/* 3393 */     if (match || (preserveAllTokens && lastMatch)) {
/* 3394 */       list.add(str.substring(start, i));
/*      */     }
/* 3396 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars) {
/* 3433 */     return splitWorker(str, separatorChars, -1, true);
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
/*      */   public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
/* 3473 */     return splitWorker(str, separatorChars, max, true);
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
/*      */   private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
/* 3495 */     if (str == null) {
/* 3496 */       return null;
/*      */     }
/* 3498 */     int len = str.length();
/* 3499 */     if (len == 0) {
/* 3500 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 3502 */     List<String> list = new ArrayList<String>();
/* 3503 */     int sizePlus1 = 1;
/* 3504 */     int i = 0, start = 0;
/* 3505 */     boolean match = false;
/* 3506 */     boolean lastMatch = false;
/* 3507 */     if (separatorChars == null) {
/*      */       
/* 3509 */       while (i < len) {
/* 3510 */         if (Character.isWhitespace(str.charAt(i))) {
/* 3511 */           if (match || preserveAllTokens) {
/* 3512 */             lastMatch = true;
/* 3513 */             if (sizePlus1++ == max) {
/* 3514 */               i = len;
/* 3515 */               lastMatch = false;
/*      */             } 
/* 3517 */             list.add(str.substring(start, i));
/* 3518 */             match = false;
/*      */           } 
/* 3520 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3523 */         lastMatch = false;
/* 3524 */         match = true;
/* 3525 */         i++;
/*      */       } 
/* 3527 */     } else if (separatorChars.length() == 1) {
/*      */       
/* 3529 */       char sep = separatorChars.charAt(0);
/* 3530 */       while (i < len) {
/* 3531 */         if (str.charAt(i) == sep) {
/* 3532 */           if (match || preserveAllTokens) {
/* 3533 */             lastMatch = true;
/* 3534 */             if (sizePlus1++ == max) {
/* 3535 */               i = len;
/* 3536 */               lastMatch = false;
/*      */             } 
/* 3538 */             list.add(str.substring(start, i));
/* 3539 */             match = false;
/*      */           } 
/* 3541 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3544 */         lastMatch = false;
/* 3545 */         match = true;
/* 3546 */         i++;
/*      */       } 
/*      */     } else {
/*      */       
/* 3550 */       while (i < len) {
/* 3551 */         if (separatorChars.indexOf(str.charAt(i)) >= 0) {
/* 3552 */           if (match || preserveAllTokens) {
/* 3553 */             lastMatch = true;
/* 3554 */             if (sizePlus1++ == max) {
/* 3555 */               i = len;
/* 3556 */               lastMatch = false;
/*      */             } 
/* 3558 */             list.add(str.substring(start, i));
/* 3559 */             match = false;
/*      */           } 
/* 3561 */           start = ++i;
/*      */           continue;
/*      */         } 
/* 3564 */         lastMatch = false;
/* 3565 */         match = true;
/* 3566 */         i++;
/*      */       } 
/*      */     } 
/* 3569 */     if (match || (preserveAllTokens && lastMatch)) {
/* 3570 */       list.add(str.substring(start, i));
/*      */     }
/* 3572 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static String[] splitByCharacterType(String str) {
/* 3595 */     return splitByCharacterType(str, false);
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
/*      */   public static String[] splitByCharacterTypeCamelCase(String str) {
/* 3623 */     return splitByCharacterType(str, true);
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
/*      */   private static String[] splitByCharacterType(String str, boolean camelCase) {
/* 3641 */     if (str == null) {
/* 3642 */       return null;
/*      */     }
/* 3644 */     if (str.isEmpty()) {
/* 3645 */       return ArrayUtils.EMPTY_STRING_ARRAY;
/*      */     }
/* 3647 */     char[] c = str.toCharArray();
/* 3648 */     List<String> list = new ArrayList<String>();
/* 3649 */     int tokenStart = 0;
/* 3650 */     int currentType = Character.getType(c[tokenStart]);
/* 3651 */     for (int pos = tokenStart + 1; pos < c.length; pos++) {
/* 3652 */       int type = Character.getType(c[pos]);
/* 3653 */       if (type != currentType) {
/*      */ 
/*      */         
/* 3656 */         if (camelCase && type == 2 && currentType == 1) {
/* 3657 */           int newTokenStart = pos - 1;
/* 3658 */           if (newTokenStart != tokenStart) {
/* 3659 */             list.add(new String(c, tokenStart, newTokenStart - tokenStart));
/* 3660 */             tokenStart = newTokenStart;
/*      */           } 
/*      */         } else {
/* 3663 */           list.add(new String(c, tokenStart, pos - tokenStart));
/* 3664 */           tokenStart = pos;
/*      */         } 
/* 3666 */         currentType = type;
/*      */       } 
/* 3668 */     }  list.add(new String(c, tokenStart, c.length - tokenStart));
/* 3669 */     return list.<String>toArray(new String[list.size()]);
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
/*      */   public static <T> String join(T... elements) {
/* 3697 */     return join((Object[])elements, (String)null);
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
/*      */   public static String join(Object[] array, char separator) {
/* 3723 */     if (array == null) {
/* 3724 */       return null;
/*      */     }
/* 3726 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(long[] array, char separator) {
/* 3755 */     if (array == null) {
/* 3756 */       return null;
/*      */     }
/* 3758 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(int[] array, char separator) {
/* 3787 */     if (array == null) {
/* 3788 */       return null;
/*      */     }
/* 3790 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(short[] array, char separator) {
/* 3819 */     if (array == null) {
/* 3820 */       return null;
/*      */     }
/* 3822 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(byte[] array, char separator) {
/* 3851 */     if (array == null) {
/* 3852 */       return null;
/*      */     }
/* 3854 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(char[] array, char separator) {
/* 3883 */     if (array == null) {
/* 3884 */       return null;
/*      */     }
/* 3886 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(float[] array, char separator) {
/* 3915 */     if (array == null) {
/* 3916 */       return null;
/*      */     }
/* 3918 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(double[] array, char separator) {
/* 3947 */     if (array == null) {
/* 3948 */       return null;
/*      */     }
/* 3950 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(Object[] array, char separator, int startIndex, int endIndex) {
/* 3981 */     if (array == null) {
/* 3982 */       return null;
/*      */     }
/* 3984 */     int noOfItems = endIndex - startIndex;
/* 3985 */     if (noOfItems <= 0) {
/* 3986 */       return "";
/*      */     }
/* 3988 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 3989 */     for (int i = startIndex; i < endIndex; i++) {
/* 3990 */       if (i > startIndex) {
/* 3991 */         buf.append(separator);
/*      */       }
/* 3993 */       if (array[i] != null) {
/* 3994 */         buf.append(array[i]);
/*      */       }
/*      */     } 
/* 3997 */     return buf.toString();
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
/*      */   public static String join(long[] array, char separator, int startIndex, int endIndex) {
/* 4032 */     if (array == null) {
/* 4033 */       return null;
/*      */     }
/* 4035 */     int noOfItems = endIndex - startIndex;
/* 4036 */     if (noOfItems <= 0) {
/* 4037 */       return "";
/*      */     }
/* 4039 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4040 */     for (int i = startIndex; i < endIndex; i++) {
/* 4041 */       if (i > startIndex) {
/* 4042 */         buf.append(separator);
/*      */       }
/* 4044 */       buf.append(array[i]);
/*      */     } 
/* 4046 */     return buf.toString();
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
/*      */   public static String join(int[] array, char separator, int startIndex, int endIndex) {
/* 4081 */     if (array == null) {
/* 4082 */       return null;
/*      */     }
/* 4084 */     int noOfItems = endIndex - startIndex;
/* 4085 */     if (noOfItems <= 0) {
/* 4086 */       return "";
/*      */     }
/* 4088 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4089 */     for (int i = startIndex; i < endIndex; i++) {
/* 4090 */       if (i > startIndex) {
/* 4091 */         buf.append(separator);
/*      */       }
/* 4093 */       buf.append(array[i]);
/*      */     } 
/* 4095 */     return buf.toString();
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
/*      */   public static String join(byte[] array, char separator, int startIndex, int endIndex) {
/* 4130 */     if (array == null) {
/* 4131 */       return null;
/*      */     }
/* 4133 */     int noOfItems = endIndex - startIndex;
/* 4134 */     if (noOfItems <= 0) {
/* 4135 */       return "";
/*      */     }
/* 4137 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4138 */     for (int i = startIndex; i < endIndex; i++) {
/* 4139 */       if (i > startIndex) {
/* 4140 */         buf.append(separator);
/*      */       }
/* 4142 */       buf.append(array[i]);
/*      */     } 
/* 4144 */     return buf.toString();
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
/*      */   public static String join(short[] array, char separator, int startIndex, int endIndex) {
/* 4179 */     if (array == null) {
/* 4180 */       return null;
/*      */     }
/* 4182 */     int noOfItems = endIndex - startIndex;
/* 4183 */     if (noOfItems <= 0) {
/* 4184 */       return "";
/*      */     }
/* 4186 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4187 */     for (int i = startIndex; i < endIndex; i++) {
/* 4188 */       if (i > startIndex) {
/* 4189 */         buf.append(separator);
/*      */       }
/* 4191 */       buf.append(array[i]);
/*      */     } 
/* 4193 */     return buf.toString();
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
/*      */   public static String join(char[] array, char separator, int startIndex, int endIndex) {
/* 4228 */     if (array == null) {
/* 4229 */       return null;
/*      */     }
/* 4231 */     int noOfItems = endIndex - startIndex;
/* 4232 */     if (noOfItems <= 0) {
/* 4233 */       return "";
/*      */     }
/* 4235 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4236 */     for (int i = startIndex; i < endIndex; i++) {
/* 4237 */       if (i > startIndex) {
/* 4238 */         buf.append(separator);
/*      */       }
/* 4240 */       buf.append(array[i]);
/*      */     } 
/* 4242 */     return buf.toString();
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
/*      */   public static String join(double[] array, char separator, int startIndex, int endIndex) {
/* 4277 */     if (array == null) {
/* 4278 */       return null;
/*      */     }
/* 4280 */     int noOfItems = endIndex - startIndex;
/* 4281 */     if (noOfItems <= 0) {
/* 4282 */       return "";
/*      */     }
/* 4284 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4285 */     for (int i = startIndex; i < endIndex; i++) {
/* 4286 */       if (i > startIndex) {
/* 4287 */         buf.append(separator);
/*      */       }
/* 4289 */       buf.append(array[i]);
/*      */     } 
/* 4291 */     return buf.toString();
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
/*      */   public static String join(float[] array, char separator, int startIndex, int endIndex) {
/* 4326 */     if (array == null) {
/* 4327 */       return null;
/*      */     }
/* 4329 */     int noOfItems = endIndex - startIndex;
/* 4330 */     if (noOfItems <= 0) {
/* 4331 */       return "";
/*      */     }
/* 4333 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/* 4334 */     for (int i = startIndex; i < endIndex; i++) {
/* 4335 */       if (i > startIndex) {
/* 4336 */         buf.append(separator);
/*      */       }
/* 4338 */       buf.append(array[i]);
/*      */     } 
/* 4340 */     return buf.toString();
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
/*      */   public static String join(Object[] array, String separator) {
/* 4368 */     if (array == null) {
/* 4369 */       return null;
/*      */     }
/* 4371 */     return join(array, separator, 0, array.length);
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
/*      */   public static String join(Object[] array, String separator, int startIndex, int endIndex) {
/* 4410 */     if (array == null) {
/* 4411 */       return null;
/*      */     }
/* 4413 */     if (separator == null) {
/* 4414 */       separator = "";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 4419 */     int noOfItems = endIndex - startIndex;
/* 4420 */     if (noOfItems <= 0) {
/* 4421 */       return "";
/*      */     }
/*      */     
/* 4424 */     StringBuilder buf = new StringBuilder(noOfItems * 16);
/*      */     
/* 4426 */     for (int i = startIndex; i < endIndex; i++) {
/* 4427 */       if (i > startIndex) {
/* 4428 */         buf.append(separator);
/*      */       }
/* 4430 */       if (array[i] != null) {
/* 4431 */         buf.append(array[i]);
/*      */       }
/*      */     } 
/* 4434 */     return buf.toString();
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
/*      */   public static String join(Iterator<?> iterator, char separator) {
/* 4454 */     if (iterator == null) {
/* 4455 */       return null;
/*      */     }
/* 4457 */     if (!iterator.hasNext()) {
/* 4458 */       return "";
/*      */     }
/* 4460 */     Object first = iterator.next();
/* 4461 */     if (!iterator.hasNext()) {
/*      */ 
/*      */       
/* 4464 */       String result = ObjectUtils.toString(first);
/* 4465 */       return result;
/*      */     } 
/*      */ 
/*      */     
/* 4469 */     StringBuilder buf = new StringBuilder(256);
/* 4470 */     if (first != null) {
/* 4471 */       buf.append(first);
/*      */     }
/*      */     
/* 4474 */     while (iterator.hasNext()) {
/* 4475 */       buf.append(separator);
/* 4476 */       Object obj = iterator.next();
/* 4477 */       if (obj != null) {
/* 4478 */         buf.append(obj);
/*      */       }
/*      */     } 
/*      */     
/* 4482 */     return buf.toString();
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
/*      */   public static String join(Iterator<?> iterator, String separator) {
/* 4501 */     if (iterator == null) {
/* 4502 */       return null;
/*      */     }
/* 4504 */     if (!iterator.hasNext()) {
/* 4505 */       return "";
/*      */     }
/* 4507 */     Object first = iterator.next();
/* 4508 */     if (!iterator.hasNext()) {
/*      */       
/* 4510 */       String result = ObjectUtils.toString(first);
/* 4511 */       return result;
/*      */     } 
/*      */ 
/*      */     
/* 4515 */     StringBuilder buf = new StringBuilder(256);
/* 4516 */     if (first != null) {
/* 4517 */       buf.append(first);
/*      */     }
/*      */     
/* 4520 */     while (iterator.hasNext()) {
/* 4521 */       if (separator != null) {
/* 4522 */         buf.append(separator);
/*      */       }
/* 4524 */       Object obj = iterator.next();
/* 4525 */       if (obj != null) {
/* 4526 */         buf.append(obj);
/*      */       }
/*      */     } 
/* 4529 */     return buf.toString();
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
/*      */   public static String join(Iterable<?> iterable, char separator) {
/* 4547 */     if (iterable == null) {
/* 4548 */       return null;
/*      */     }
/* 4550 */     return join(iterable.iterator(), separator);
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
/*      */   public static String join(Iterable<?> iterable, String separator) {
/* 4568 */     if (iterable == null) {
/* 4569 */       return null;
/*      */     }
/* 4571 */     return join(iterable.iterator(), separator);
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
/*      */   public static String joinWith(String separator, Object... objects) {
/* 4595 */     if (objects == null) {
/* 4596 */       throw new IllegalArgumentException("Object varargs must not be null");
/*      */     }
/*      */     
/* 4599 */     String sanitizedSeparator = defaultString(separator, "");
/*      */     
/* 4601 */     StringBuilder result = new StringBuilder();
/*      */     
/* 4603 */     Iterator<Object> iterator = Arrays.<Object>asList(objects).iterator();
/* 4604 */     while (iterator.hasNext()) {
/*      */       
/* 4606 */       String value = ObjectUtils.toString(iterator.next());
/* 4607 */       result.append(value);
/*      */       
/* 4609 */       if (iterator.hasNext()) {
/* 4610 */         result.append(sanitizedSeparator);
/*      */       }
/*      */     } 
/*      */     
/* 4614 */     return result.toString();
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
/*      */   public static String deleteWhitespace(String str) {
/* 4634 */     if (isEmpty(str)) {
/* 4635 */       return str;
/*      */     }
/* 4637 */     int sz = str.length();
/* 4638 */     char[] chs = new char[sz];
/* 4639 */     int count = 0;
/* 4640 */     for (int i = 0; i < sz; i++) {
/* 4641 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 4642 */         chs[count++] = str.charAt(i);
/*      */       }
/*      */     } 
/* 4645 */     if (count == sz) {
/* 4646 */       return str;
/*      */     }
/* 4648 */     return new String(chs, 0, count);
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
/*      */   public static String removeStart(String str, String remove) {
/* 4678 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4679 */       return str;
/*      */     }
/* 4681 */     if (str.startsWith(remove)) {
/* 4682 */       return str.substring(remove.length());
/*      */     }
/* 4684 */     return str;
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
/*      */   public static String removeStartIgnoreCase(String str, String remove) {
/* 4713 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4714 */       return str;
/*      */     }
/* 4716 */     if (startsWithIgnoreCase(str, remove)) {
/* 4717 */       return str.substring(remove.length());
/*      */     }
/* 4719 */     return str;
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
/*      */   public static String removeEnd(String str, String remove) {
/* 4747 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4748 */       return str;
/*      */     }
/* 4750 */     if (str.endsWith(remove)) {
/* 4751 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 4753 */     return str;
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
/*      */   public static String removeEndIgnoreCase(String str, String remove) {
/* 4783 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4784 */       return str;
/*      */     }
/* 4786 */     if (endsWithIgnoreCase(str, remove)) {
/* 4787 */       return str.substring(0, str.length() - remove.length());
/*      */     }
/* 4789 */     return str;
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
/*      */   public static String remove(String str, String remove) {
/* 4816 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4817 */       return str;
/*      */     }
/* 4819 */     return replace(str, remove, "", -1);
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
/*      */   public static String removeIgnoreCase(String str, String remove) {
/* 4856 */     if (isEmpty(str) || isEmpty(remove)) {
/* 4857 */       return str;
/*      */     }
/* 4859 */     return replaceIgnoreCase(str, remove, "", -1);
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
/*      */   public static String remove(String str, char remove) {
/* 4882 */     if (isEmpty(str) || str.indexOf(remove) == -1) {
/* 4883 */       return str;
/*      */     }
/* 4885 */     char[] chars = str.toCharArray();
/* 4886 */     int pos = 0;
/* 4887 */     for (int i = 0; i < chars.length; i++) {
/* 4888 */       if (chars[i] != remove) {
/* 4889 */         chars[pos++] = chars[i];
/*      */       }
/*      */     } 
/* 4892 */     return new String(chars, 0, pos);
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
/*      */   public static String removeAll(String text, String regex) {
/* 4939 */     return replaceAll(text, regex, "");
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
/*      */   public static String removeFirst(String text, String regex) {
/* 4985 */     return replaceFirst(text, regex, "");
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
/*      */   public static String replaceOnce(String text, String searchString, String replacement) {
/* 5014 */     return replace(text, searchString, replacement, 1);
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
/*      */   public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
/* 5043 */     return replaceIgnoreCase(text, searchString, replacement, 1);
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
/*      */   public static String replacePattern(String source, String regex, String replacement) {
/* 5086 */     if (source == null || regex == null || replacement == null) {
/* 5087 */       return source;
/*      */     }
/* 5089 */     return Pattern.compile(regex, 32).matcher(source).replaceAll(replacement);
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
/*      */   public static String removePattern(String source, String regex) {
/* 5123 */     return replacePattern(source, regex, "");
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
/*      */   public static String replaceAll(String text, String regex, String replacement) {
/* 5175 */     if (text == null || regex == null || replacement == null) {
/* 5176 */       return text;
/*      */     }
/* 5178 */     return text.replaceAll(regex, replacement);
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
/*      */   public static String replaceFirst(String text, String regex, String replacement) {
/* 5228 */     if (text == null || regex == null || replacement == null) {
/* 5229 */       return text;
/*      */     }
/* 5231 */     return text.replaceFirst(regex, replacement);
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
/*      */   public static String replace(String text, String searchString, String replacement) {
/* 5258 */     return replace(text, searchString, replacement, -1);
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
/*      */   public static String replaceIgnoreCase(String text, String searchString, String replacement) {
/* 5286 */     return replaceIgnoreCase(text, searchString, replacement, -1);
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
/*      */   public static String replace(String text, String searchString, String replacement, int max) {
/* 5318 */     return replace(text, searchString, replacement, max, false);
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
/*      */   private static String replace(String text, String searchString, String replacement, int max, boolean ignoreCase) {
/* 5353 */     if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
/* 5354 */       return text;
/*      */     }
/* 5356 */     String searchText = text;
/* 5357 */     if (ignoreCase) {
/* 5358 */       searchText = text.toLowerCase();
/* 5359 */       searchString = searchString.toLowerCase();
/*      */     } 
/* 5361 */     int start = 0;
/* 5362 */     int end = searchText.indexOf(searchString, start);
/* 5363 */     if (end == -1) {
/* 5364 */       return text;
/*      */     }
/* 5366 */     int replLength = searchString.length();
/* 5367 */     int increase = replacement.length() - replLength;
/* 5368 */     increase = (increase < 0) ? 0 : increase;
/* 5369 */     increase *= (max < 0) ? 16 : ((max > 64) ? 64 : max);
/* 5370 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/* 5371 */     while (end != -1) {
/* 5372 */       buf.append(text.substring(start, end)).append(replacement);
/* 5373 */       start = end + replLength;
/* 5374 */       if (--max == 0) {
/*      */         break;
/*      */       }
/* 5377 */       end = searchText.indexOf(searchString, start);
/*      */     } 
/* 5379 */     buf.append(text.substring(start));
/* 5380 */     return buf.toString();
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
/*      */   public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
/* 5413 */     return replace(text, searchString, replacement, max, true);
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
/*      */   public static String replaceEach(String text, String[] searchList, String[] replacementList) {
/* 5456 */     return replaceEach(text, searchList, replacementList, false, 0);
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
/*      */   public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
/* 5504 */     int timeToLive = (searchList == null) ? 0 : searchList.length;
/* 5505 */     return replaceEach(text, searchList, replacementList, true, timeToLive);
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
/*      */   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
/* 5564 */     if (text == null || text.isEmpty() || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0)
/*      */     {
/* 5566 */       return text;
/*      */     }
/*      */ 
/*      */     
/* 5570 */     if (timeToLive < 0) {
/* 5571 */       throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
/*      */     }
/*      */ 
/*      */     
/* 5575 */     int searchLength = searchList.length;
/* 5576 */     int replacementLength = replacementList.length;
/*      */ 
/*      */     
/* 5579 */     if (searchLength != replacementLength) {
/* 5580 */       throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5587 */     boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
/*      */ 
/*      */     
/* 5590 */     int textIndex = -1;
/* 5591 */     int replaceIndex = -1;
/* 5592 */     int tempIndex = -1;
/*      */ 
/*      */ 
/*      */     
/* 5596 */     for (int i = 0; i < searchLength; i++) {
/* 5597 */       if (!noMoreMatchesForReplIndex[i] && searchList[i] != null && 
/* 5598 */         !searchList[i].isEmpty() && replacementList[i] != null) {
/*      */ 
/*      */         
/* 5601 */         tempIndex = text.indexOf(searchList[i]);
/*      */ 
/*      */         
/* 5604 */         if (tempIndex == -1) {
/* 5605 */           noMoreMatchesForReplIndex[i] = true;
/*      */         }
/* 5607 */         else if (textIndex == -1 || tempIndex < textIndex) {
/* 5608 */           textIndex = tempIndex;
/* 5609 */           replaceIndex = i;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 5616 */     if (textIndex == -1) {
/* 5617 */       return text;
/*      */     }
/*      */     
/* 5620 */     int start = 0;
/*      */ 
/*      */     
/* 5623 */     int increase = 0;
/*      */ 
/*      */     
/* 5626 */     for (int j = 0; j < searchList.length; j++) {
/* 5627 */       if (searchList[j] != null && replacementList[j] != null) {
/*      */ 
/*      */         
/* 5630 */         int greater = replacementList[j].length() - searchList[j].length();
/* 5631 */         if (greater > 0) {
/* 5632 */           increase += 3 * greater;
/*      */         }
/*      */       } 
/*      */     } 
/* 5636 */     increase = Math.min(increase, text.length() / 5);
/*      */     
/* 5638 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/*      */     
/* 5640 */     while (textIndex != -1) {
/*      */       int m;
/* 5642 */       for (m = start; m < textIndex; m++) {
/* 5643 */         buf.append(text.charAt(m));
/*      */       }
/* 5645 */       buf.append(replacementList[replaceIndex]);
/*      */       
/* 5647 */       start = textIndex + searchList[replaceIndex].length();
/*      */       
/* 5649 */       textIndex = -1;
/* 5650 */       replaceIndex = -1;
/* 5651 */       tempIndex = -1;
/*      */ 
/*      */       
/* 5654 */       for (m = 0; m < searchLength; m++) {
/* 5655 */         if (!noMoreMatchesForReplIndex[m] && searchList[m] != null && 
/* 5656 */           !searchList[m].isEmpty() && replacementList[m] != null) {
/*      */ 
/*      */           
/* 5659 */           tempIndex = text.indexOf(searchList[m], start);
/*      */ 
/*      */           
/* 5662 */           if (tempIndex == -1) {
/* 5663 */             noMoreMatchesForReplIndex[m] = true;
/*      */           }
/* 5665 */           else if (textIndex == -1 || tempIndex < textIndex) {
/* 5666 */             textIndex = tempIndex;
/* 5667 */             replaceIndex = m;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 5674 */     int textLength = text.length();
/* 5675 */     for (int k = start; k < textLength; k++) {
/* 5676 */       buf.append(text.charAt(k));
/*      */     }
/* 5678 */     String result = buf.toString();
/* 5679 */     if (!repeat) {
/* 5680 */       return result;
/*      */     }
/*      */     
/* 5683 */     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
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
/*      */   public static String replaceChars(String str, char searchChar, char replaceChar) {
/* 5709 */     if (str == null) {
/* 5710 */       return null;
/*      */     }
/* 5712 */     return str.replace(searchChar, replaceChar);
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
/*      */   public static String replaceChars(String str, String searchChars, String replaceChars) {
/* 5752 */     if (isEmpty(str) || isEmpty(searchChars)) {
/* 5753 */       return str;
/*      */     }
/* 5755 */     if (replaceChars == null) {
/* 5756 */       replaceChars = "";
/*      */     }
/* 5758 */     boolean modified = false;
/* 5759 */     int replaceCharsLength = replaceChars.length();
/* 5760 */     int strLength = str.length();
/* 5761 */     StringBuilder buf = new StringBuilder(strLength);
/* 5762 */     for (int i = 0; i < strLength; i++) {
/* 5763 */       char ch = str.charAt(i);
/* 5764 */       int index = searchChars.indexOf(ch);
/* 5765 */       if (index >= 0) {
/* 5766 */         modified = true;
/* 5767 */         if (index < replaceCharsLength) {
/* 5768 */           buf.append(replaceChars.charAt(index));
/*      */         }
/*      */       } else {
/* 5771 */         buf.append(ch);
/*      */       } 
/*      */     } 
/* 5774 */     if (modified) {
/* 5775 */       return buf.toString();
/*      */     }
/* 5777 */     return str;
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
/*      */   public static String overlay(String str, String overlay, int start, int end) {
/* 5812 */     if (str == null) {
/* 5813 */       return null;
/*      */     }
/* 5815 */     if (overlay == null) {
/* 5816 */       overlay = "";
/*      */     }
/* 5818 */     int len = str.length();
/* 5819 */     if (start < 0) {
/* 5820 */       start = 0;
/*      */     }
/* 5822 */     if (start > len) {
/* 5823 */       start = len;
/*      */     }
/* 5825 */     if (end < 0) {
/* 5826 */       end = 0;
/*      */     }
/* 5828 */     if (end > len) {
/* 5829 */       end = len;
/*      */     }
/* 5831 */     if (start > end) {
/* 5832 */       int temp = start;
/* 5833 */       start = end;
/* 5834 */       end = temp;
/*      */     } 
/* 5836 */     return (new StringBuilder(len + start - end + overlay.length() + 1))
/* 5837 */       .append(str.substring(0, start))
/* 5838 */       .append(overlay)
/* 5839 */       .append(str.substring(end))
/* 5840 */       .toString();
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
/*      */   public static String chomp(String str) {
/* 5871 */     if (isEmpty(str)) {
/* 5872 */       return str;
/*      */     }
/*      */     
/* 5875 */     if (str.length() == 1) {
/* 5876 */       char ch = str.charAt(0);
/* 5877 */       if (ch == '\r' || ch == '\n') {
/* 5878 */         return "";
/*      */       }
/* 5880 */       return str;
/*      */     } 
/*      */     
/* 5883 */     int lastIdx = str.length() - 1;
/* 5884 */     char last = str.charAt(lastIdx);
/*      */     
/* 5886 */     if (last == '\n') {
/* 5887 */       if (str.charAt(lastIdx - 1) == '\r') {
/* 5888 */         lastIdx--;
/*      */       }
/* 5890 */     } else if (last != '\r') {
/* 5891 */       lastIdx++;
/*      */     } 
/* 5893 */     return str.substring(0, lastIdx);
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
/*      */   @Deprecated
/*      */   public static String chomp(String str, String separator) {
/* 5925 */     return removeEnd(str, separator);
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
/*      */   public static String chop(String str) {
/* 5954 */     if (str == null) {
/* 5955 */       return null;
/*      */     }
/* 5957 */     int strLen = str.length();
/* 5958 */     if (strLen < 2) {
/* 5959 */       return "";
/*      */     }
/* 5961 */     int lastIdx = strLen - 1;
/* 5962 */     String ret = str.substring(0, lastIdx);
/* 5963 */     char last = str.charAt(lastIdx);
/* 5964 */     if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
/* 5965 */       return ret.substring(0, lastIdx - 1);
/*      */     }
/* 5967 */     return ret;
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
/*      */   public static String repeat(String str, int repeat) {
/*      */     char ch0, ch1, output2[];
/*      */     int i;
/* 5996 */     if (str == null) {
/* 5997 */       return null;
/*      */     }
/* 5999 */     if (repeat <= 0) {
/* 6000 */       return "";
/*      */     }
/* 6002 */     int inputLength = str.length();
/* 6003 */     if (repeat == 1 || inputLength == 0) {
/* 6004 */       return str;
/*      */     }
/* 6006 */     if (inputLength == 1 && repeat <= 8192) {
/* 6007 */       return repeat(str.charAt(0), repeat);
/*      */     }
/*      */     
/* 6010 */     int outputLength = inputLength * repeat;
/* 6011 */     switch (inputLength) {
/*      */       case 1:
/* 6013 */         return repeat(str.charAt(0), repeat);
/*      */       case 2:
/* 6015 */         ch0 = str.charAt(0);
/* 6016 */         ch1 = str.charAt(1);
/* 6017 */         output2 = new char[outputLength];
/* 6018 */         for (i = repeat * 2 - 2; i >= 0; i--, i--) {
/* 6019 */           output2[i] = ch0;
/* 6020 */           output2[i + 1] = ch1;
/*      */         } 
/* 6022 */         return new String(output2);
/*      */     } 
/* 6024 */     StringBuilder buf = new StringBuilder(outputLength);
/* 6025 */     for (int j = 0; j < repeat; j++) {
/* 6026 */       buf.append(str);
/*      */     }
/* 6028 */     return buf.toString();
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
/*      */   public static String repeat(String str, String separator, int repeat) {
/* 6053 */     if (str == null || separator == null) {
/* 6054 */       return repeat(str, repeat);
/*      */     }
/*      */     
/* 6057 */     String result = repeat(str + separator, repeat);
/* 6058 */     return removeEnd(result, separator);
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
/*      */   public static String repeat(char ch, int repeat) {
/* 6084 */     if (repeat <= 0) {
/* 6085 */       return "";
/*      */     }
/* 6087 */     char[] buf = new char[repeat];
/* 6088 */     for (int i = repeat - 1; i >= 0; i--) {
/* 6089 */       buf[i] = ch;
/*      */     }
/* 6091 */     return new String(buf);
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
/*      */   public static String rightPad(String str, int size) {
/* 6114 */     return rightPad(str, size, ' ');
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
/*      */   public static String rightPad(String str, int size, char padChar) {
/* 6139 */     if (str == null) {
/* 6140 */       return null;
/*      */     }
/* 6142 */     int pads = size - str.length();
/* 6143 */     if (pads <= 0) {
/* 6144 */       return str;
/*      */     }
/* 6146 */     if (pads > 8192) {
/* 6147 */       return rightPad(str, size, String.valueOf(padChar));
/*      */     }
/* 6149 */     return str.concat(repeat(padChar, pads));
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
/*      */   public static String rightPad(String str, int size, String padStr) {
/* 6176 */     if (str == null) {
/* 6177 */       return null;
/*      */     }
/* 6179 */     if (isEmpty(padStr)) {
/* 6180 */       padStr = " ";
/*      */     }
/* 6182 */     int padLen = padStr.length();
/* 6183 */     int strLen = str.length();
/* 6184 */     int pads = size - strLen;
/* 6185 */     if (pads <= 0) {
/* 6186 */       return str;
/*      */     }
/* 6188 */     if (padLen == 1 && pads <= 8192) {
/* 6189 */       return rightPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 6192 */     if (pads == padLen)
/* 6193 */       return str.concat(padStr); 
/* 6194 */     if (pads < padLen) {
/* 6195 */       return str.concat(padStr.substring(0, pads));
/*      */     }
/* 6197 */     char[] padding = new char[pads];
/* 6198 */     char[] padChars = padStr.toCharArray();
/* 6199 */     for (int i = 0; i < pads; i++) {
/* 6200 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 6202 */     return str.concat(new String(padding));
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
/*      */   public static String leftPad(String str, int size) {
/* 6226 */     return leftPad(str, size, ' ');
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
/*      */   public static String leftPad(String str, int size, char padChar) {
/* 6251 */     if (str == null) {
/* 6252 */       return null;
/*      */     }
/* 6254 */     int pads = size - str.length();
/* 6255 */     if (pads <= 0) {
/* 6256 */       return str;
/*      */     }
/* 6258 */     if (pads > 8192) {
/* 6259 */       return leftPad(str, size, String.valueOf(padChar));
/*      */     }
/* 6261 */     return repeat(padChar, pads).concat(str);
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
/*      */   public static String leftPad(String str, int size, String padStr) {
/* 6288 */     if (str == null) {
/* 6289 */       return null;
/*      */     }
/* 6291 */     if (isEmpty(padStr)) {
/* 6292 */       padStr = " ";
/*      */     }
/* 6294 */     int padLen = padStr.length();
/* 6295 */     int strLen = str.length();
/* 6296 */     int pads = size - strLen;
/* 6297 */     if (pads <= 0) {
/* 6298 */       return str;
/*      */     }
/* 6300 */     if (padLen == 1 && pads <= 8192) {
/* 6301 */       return leftPad(str, size, padStr.charAt(0));
/*      */     }
/*      */     
/* 6304 */     if (pads == padLen)
/* 6305 */       return padStr.concat(str); 
/* 6306 */     if (pads < padLen) {
/* 6307 */       return padStr.substring(0, pads).concat(str);
/*      */     }
/* 6309 */     char[] padding = new char[pads];
/* 6310 */     char[] padChars = padStr.toCharArray();
/* 6311 */     for (int i = 0; i < pads; i++) {
/* 6312 */       padding[i] = padChars[i % padLen];
/*      */     }
/* 6314 */     return (new String(padding)).concat(str);
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
/*      */   public static int length(CharSequence cs) {
/* 6330 */     return (cs == null) ? 0 : cs.length();
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
/*      */   public static String center(String str, int size) {
/* 6359 */     return center(str, size, ' ');
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
/*      */   public static String center(String str, int size, char padChar) {
/* 6387 */     if (str == null || size <= 0) {
/* 6388 */       return str;
/*      */     }
/* 6390 */     int strLen = str.length();
/* 6391 */     int pads = size - strLen;
/* 6392 */     if (pads <= 0) {
/* 6393 */       return str;
/*      */     }
/* 6395 */     str = leftPad(str, strLen + pads / 2, padChar);
/* 6396 */     str = rightPad(str, size, padChar);
/* 6397 */     return str;
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
/*      */   public static String center(String str, int size, String padStr) {
/* 6427 */     if (str == null || size <= 0) {
/* 6428 */       return str;
/*      */     }
/* 6430 */     if (isEmpty(padStr)) {
/* 6431 */       padStr = " ";
/*      */     }
/* 6433 */     int strLen = str.length();
/* 6434 */     int pads = size - strLen;
/* 6435 */     if (pads <= 0) {
/* 6436 */       return str;
/*      */     }
/* 6438 */     str = leftPad(str, strLen + pads / 2, padStr);
/* 6439 */     str = rightPad(str, size, padStr);
/* 6440 */     return str;
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
/*      */   public static String upperCase(String str) {
/* 6465 */     if (str == null) {
/* 6466 */       return null;
/*      */     }
/* 6468 */     return str.toUpperCase();
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
/*      */   public static String upperCase(String str, Locale locale) {
/* 6488 */     if (str == null) {
/* 6489 */       return null;
/*      */     }
/* 6491 */     return str.toUpperCase(locale);
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
/*      */   public static String lowerCase(String str) {
/* 6514 */     if (str == null) {
/* 6515 */       return null;
/*      */     }
/* 6517 */     return str.toLowerCase();
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
/*      */   public static String lowerCase(String str, Locale locale) {
/* 6537 */     if (str == null) {
/* 6538 */       return null;
/*      */     }
/* 6540 */     return str.toLowerCase(locale);
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
/*      */   public static String capitalize(String str) {
/*      */     int strLen;
/* 6566 */     if (str == null || (strLen = str.length()) == 0) {
/* 6567 */       return str;
/*      */     }
/*      */     
/* 6570 */     char firstChar = str.charAt(0);
/* 6571 */     char newChar = Character.toTitleCase(firstChar);
/* 6572 */     if (firstChar == newChar)
/*      */     {
/* 6574 */       return str;
/*      */     }
/*      */     
/* 6577 */     char[] newChars = new char[strLen];
/* 6578 */     newChars[0] = newChar;
/* 6579 */     str.getChars(1, strLen, newChars, 1);
/* 6580 */     return String.valueOf(newChars);
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
/*      */   public static String uncapitalize(String str) {
/*      */     int strLen;
/* 6606 */     if (str == null || (strLen = str.length()) == 0) {
/* 6607 */       return str;
/*      */     }
/*      */     
/* 6610 */     char firstChar = str.charAt(0);
/* 6611 */     char newChar = Character.toLowerCase(firstChar);
/* 6612 */     if (firstChar == newChar)
/*      */     {
/* 6614 */       return str;
/*      */     }
/*      */     
/* 6617 */     char[] newChars = new char[strLen];
/* 6618 */     newChars[0] = newChar;
/* 6619 */     str.getChars(1, strLen, newChars, 1);
/* 6620 */     return String.valueOf(newChars);
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
/*      */   public static String swapCase(String str) {
/* 6651 */     if (isEmpty(str)) {
/* 6652 */       return str;
/*      */     }
/*      */     
/* 6655 */     char[] buffer = str.toCharArray();
/*      */     
/* 6657 */     for (int i = 0; i < buffer.length; i++) {
/* 6658 */       char ch = buffer[i];
/* 6659 */       if (Character.isUpperCase(ch)) {
/* 6660 */         buffer[i] = Character.toLowerCase(ch);
/* 6661 */       } else if (Character.isTitleCase(ch)) {
/* 6662 */         buffer[i] = Character.toLowerCase(ch);
/* 6663 */       } else if (Character.isLowerCase(ch)) {
/* 6664 */         buffer[i] = Character.toUpperCase(ch);
/*      */       } 
/*      */     } 
/* 6667 */     return new String(buffer);
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
/*      */   public static int countMatches(CharSequence str, CharSequence sub) {
/* 6693 */     if (isEmpty(str) || isEmpty(sub)) {
/* 6694 */       return 0;
/*      */     }
/* 6696 */     int count = 0;
/* 6697 */     int idx = 0;
/* 6698 */     while ((idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1) {
/* 6699 */       count++;
/* 6700 */       idx += sub.length();
/*      */     } 
/* 6702 */     return count;
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
/*      */   public static int countMatches(CharSequence str, char ch) {
/* 6725 */     if (isEmpty(str)) {
/* 6726 */       return 0;
/*      */     }
/* 6728 */     int count = 0;
/*      */     
/* 6730 */     for (int i = 0; i < str.length(); i++) {
/* 6731 */       if (ch == str.charAt(i)) {
/* 6732 */         count++;
/*      */       }
/*      */     } 
/* 6735 */     return count;
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
/*      */   public static boolean isAlpha(CharSequence cs) {
/* 6761 */     if (isEmpty(cs)) {
/* 6762 */       return false;
/*      */     }
/* 6764 */     int sz = cs.length();
/* 6765 */     for (int i = 0; i < sz; i++) {
/* 6766 */       if (!Character.isLetter(cs.charAt(i))) {
/* 6767 */         return false;
/*      */       }
/*      */     } 
/* 6770 */     return true;
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
/*      */   public static boolean isAlphaSpace(CharSequence cs) {
/* 6796 */     if (cs == null) {
/* 6797 */       return false;
/*      */     }
/* 6799 */     int sz = cs.length();
/* 6800 */     for (int i = 0; i < sz; i++) {
/* 6801 */       if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 6802 */         return false;
/*      */       }
/*      */     } 
/* 6805 */     return true;
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
/*      */   public static boolean isAlphanumeric(CharSequence cs) {
/* 6831 */     if (isEmpty(cs)) {
/* 6832 */       return false;
/*      */     }
/* 6834 */     int sz = cs.length();
/* 6835 */     for (int i = 0; i < sz; i++) {
/* 6836 */       if (!Character.isLetterOrDigit(cs.charAt(i))) {
/* 6837 */         return false;
/*      */       }
/*      */     } 
/* 6840 */     return true;
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
/*      */   public static boolean isAlphanumericSpace(CharSequence cs) {
/* 6866 */     if (cs == null) {
/* 6867 */       return false;
/*      */     }
/* 6869 */     int sz = cs.length();
/* 6870 */     for (int i = 0; i < sz; i++) {
/* 6871 */       if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 6872 */         return false;
/*      */       }
/*      */     } 
/* 6875 */     return true;
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
/*      */   public static boolean isAsciiPrintable(CharSequence cs) {
/* 6905 */     if (cs == null) {
/* 6906 */       return false;
/*      */     }
/* 6908 */     int sz = cs.length();
/* 6909 */     for (int i = 0; i < sz; i++) {
/* 6910 */       if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
/* 6911 */         return false;
/*      */       }
/*      */     } 
/* 6914 */     return true;
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
/*      */   public static boolean isNumeric(CharSequence cs) {
/* 6949 */     if (isEmpty(cs)) {
/* 6950 */       return false;
/*      */     }
/* 6952 */     int sz = cs.length();
/* 6953 */     for (int i = 0; i < sz; i++) {
/* 6954 */       if (!Character.isDigit(cs.charAt(i))) {
/* 6955 */         return false;
/*      */       }
/*      */     } 
/* 6958 */     return true;
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
/*      */   public static boolean isNumericSpace(CharSequence cs) {
/* 6988 */     if (cs == null) {
/* 6989 */       return false;
/*      */     }
/* 6991 */     int sz = cs.length();
/* 6992 */     for (int i = 0; i < sz; i++) {
/* 6993 */       if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
/* 6994 */         return false;
/*      */       }
/*      */     } 
/* 6997 */     return true;
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
/*      */   public static boolean isWhitespace(CharSequence cs) {
/* 7021 */     if (cs == null) {
/* 7022 */       return false;
/*      */     }
/* 7024 */     int sz = cs.length();
/* 7025 */     for (int i = 0; i < sz; i++) {
/* 7026 */       if (!Character.isWhitespace(cs.charAt(i))) {
/* 7027 */         return false;
/*      */       }
/*      */     } 
/* 7030 */     return true;
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
/*      */   public static boolean isAllLowerCase(CharSequence cs) {
/* 7056 */     if (cs == null || isEmpty(cs)) {
/* 7057 */       return false;
/*      */     }
/* 7059 */     int sz = cs.length();
/* 7060 */     for (int i = 0; i < sz; i++) {
/* 7061 */       if (!Character.isLowerCase(cs.charAt(i))) {
/* 7062 */         return false;
/*      */       }
/*      */     } 
/* 7065 */     return true;
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
/*      */   public static boolean isAllUpperCase(CharSequence cs) {
/* 7091 */     if (cs == null || isEmpty(cs)) {
/* 7092 */       return false;
/*      */     }
/* 7094 */     int sz = cs.length();
/* 7095 */     for (int i = 0; i < sz; i++) {
/* 7096 */       if (!Character.isUpperCase(cs.charAt(i))) {
/* 7097 */         return false;
/*      */       }
/*      */     } 
/* 7100 */     return true;
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
/*      */   public static String defaultString(String str) {
/* 7122 */     return (str == null) ? "" : str;
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
/*      */   public static String defaultString(String str, String defaultStr) {
/* 7143 */     return (str == null) ? defaultStr : str;
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
/*      */   public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
/* 7165 */     return isBlank((CharSequence)str) ? defaultStr : str;
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
/*      */   public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
/* 7187 */     return isEmpty((CharSequence)str) ? defaultStr : str;
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
/*      */   public static String rotate(String str, int shift) {
/* 7219 */     if (str == null) {
/* 7220 */       return null;
/*      */     }
/*      */     
/* 7223 */     int strLen = str.length();
/* 7224 */     if (shift == 0 || strLen == 0 || shift % strLen == 0) {
/* 7225 */       return str;
/*      */     }
/*      */     
/* 7228 */     StringBuilder builder = new StringBuilder(strLen);
/* 7229 */     int offset = -(shift % strLen);
/* 7230 */     builder.append(substring(str, offset));
/* 7231 */     builder.append(substring(str, 0, offset));
/* 7232 */     return builder.toString();
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
/*      */   public static String reverse(String str) {
/* 7252 */     if (str == null) {
/* 7253 */       return null;
/*      */     }
/* 7255 */     return (new StringBuilder(str)).reverse().toString();
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
/*      */   public static String reverseDelimited(String str, char separatorChar) {
/* 7278 */     if (str == null) {
/* 7279 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 7283 */     String[] strs = split(str, separatorChar);
/* 7284 */     ArrayUtils.reverse((Object[])strs);
/* 7285 */     return join((Object[])strs, separatorChar);
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
/*      */   public static String abbreviate(String str, int maxWidth) {
/* 7322 */     return abbreviate(str, 0, maxWidth);
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
/*      */   public static String abbreviate(String str, int offset, int maxWidth) {
/* 7361 */     if (str == null) {
/* 7362 */       return null;
/*      */     }
/* 7364 */     if (maxWidth < 4) {
/* 7365 */       throw new IllegalArgumentException("Minimum abbreviation width is 4");
/*      */     }
/* 7367 */     if (str.length() <= maxWidth) {
/* 7368 */       return str;
/*      */     }
/* 7370 */     if (offset > str.length()) {
/* 7371 */       offset = str.length();
/*      */     }
/* 7373 */     if (str.length() - offset < maxWidth - 3) {
/* 7374 */       offset = str.length() - maxWidth - 3;
/*      */     }
/* 7376 */     String abrevMarker = "...";
/* 7377 */     if (offset <= 4) {
/* 7378 */       return str.substring(0, maxWidth - 3) + "...";
/*      */     }
/* 7380 */     if (maxWidth < 7) {
/* 7381 */       throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
/*      */     }
/* 7383 */     if (offset + maxWidth - 3 < str.length()) {
/* 7384 */       return "..." + abbreviate(str.substring(offset), maxWidth - 3);
/*      */     }
/* 7386 */     return "..." + str.substring(str.length() - maxWidth - 3);
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
/*      */   public static String abbreviateMiddle(String str, String middle, int length) {
/* 7419 */     if (isEmpty(str) || isEmpty(middle)) {
/* 7420 */       return str;
/*      */     }
/*      */     
/* 7423 */     if (length >= str.length() || length < middle.length() + 2) {
/* 7424 */       return str;
/*      */     }
/*      */     
/* 7427 */     int targetSting = length - middle.length();
/* 7428 */     int startOffset = targetSting / 2 + targetSting % 2;
/* 7429 */     int endOffset = str.length() - targetSting / 2;
/*      */     
/* 7431 */     StringBuilder builder = new StringBuilder(length);
/* 7432 */     builder.append(str.substring(0, startOffset));
/* 7433 */     builder.append(middle);
/* 7434 */     builder.append(str.substring(endOffset));
/*      */     
/* 7436 */     return builder.toString();
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
/*      */   public static String difference(String str1, String str2) {
/* 7470 */     if (str1 == null) {
/* 7471 */       return str2;
/*      */     }
/* 7473 */     if (str2 == null) {
/* 7474 */       return str1;
/*      */     }
/* 7476 */     int at = indexOfDifference(str1, str2);
/* 7477 */     if (at == -1) {
/* 7478 */       return "";
/*      */     }
/* 7480 */     return str2.substring(at);
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
/*      */   public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
/* 7509 */     if (cs1 == cs2) {
/* 7510 */       return -1;
/*      */     }
/* 7512 */     if (cs1 == null || cs2 == null) {
/* 7513 */       return 0;
/*      */     }
/*      */     int i;
/* 7516 */     for (i = 0; i < cs1.length() && i < cs2.length() && 
/* 7517 */       cs1.charAt(i) == cs2.charAt(i); i++);
/*      */ 
/*      */ 
/*      */     
/* 7521 */     if (i < cs2.length() || i < cs1.length()) {
/* 7522 */       return i;
/*      */     }
/* 7524 */     return -1;
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
/*      */   public static int indexOfDifference(CharSequence... css) {
/* 7560 */     if (css == null || css.length <= 1) {
/* 7561 */       return -1;
/*      */     }
/* 7563 */     boolean anyStringNull = false;
/* 7564 */     boolean allStringsNull = true;
/* 7565 */     int arrayLen = css.length;
/* 7566 */     int shortestStrLen = Integer.MAX_VALUE;
/* 7567 */     int longestStrLen = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7572 */     for (int i = 0; i < arrayLen; i++) {
/* 7573 */       if (css[i] == null) {
/* 7574 */         anyStringNull = true;
/* 7575 */         shortestStrLen = 0;
/*      */       } else {
/* 7577 */         allStringsNull = false;
/* 7578 */         shortestStrLen = Math.min(css[i].length(), shortestStrLen);
/* 7579 */         longestStrLen = Math.max(css[i].length(), longestStrLen);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 7584 */     if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
/* 7585 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 7589 */     if (shortestStrLen == 0) {
/* 7590 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 7594 */     int firstDiff = -1;
/* 7595 */     for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
/* 7596 */       char comparisonChar = css[0].charAt(stringPos);
/* 7597 */       for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
/* 7598 */         if (css[arrayPos].charAt(stringPos) != comparisonChar) {
/* 7599 */           firstDiff = stringPos;
/*      */           break;
/*      */         } 
/*      */       } 
/* 7603 */       if (firstDiff != -1) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 7608 */     if (firstDiff == -1 && shortestStrLen != longestStrLen)
/*      */     {
/*      */ 
/*      */       
/* 7612 */       return shortestStrLen;
/*      */     }
/* 7614 */     return firstDiff;
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
/*      */   public static String getCommonPrefix(String... strs) {
/* 7651 */     if (strs == null || strs.length == 0) {
/* 7652 */       return "";
/*      */     }
/* 7654 */     int smallestIndexOfDiff = indexOfDifference((CharSequence[])strs);
/* 7655 */     if (smallestIndexOfDiff == -1) {
/*      */       
/* 7657 */       if (strs[0] == null) {
/* 7658 */         return "";
/*      */       }
/* 7660 */       return strs[0];
/* 7661 */     }  if (smallestIndexOfDiff == 0)
/*      */     {
/* 7663 */       return "";
/*      */     }
/*      */     
/* 7666 */     return strs[0].substring(0, smallestIndexOfDiff);
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
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
/* 7711 */     if (s == null || t == null) {
/* 7712 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7732 */     int n = s.length();
/* 7733 */     int m = t.length();
/*      */     
/* 7735 */     if (n == 0)
/* 7736 */       return m; 
/* 7737 */     if (m == 0) {
/* 7738 */       return n;
/*      */     }
/*      */     
/* 7741 */     if (n > m) {
/*      */       
/* 7743 */       CharSequence tmp = s;
/* 7744 */       s = t;
/* 7745 */       t = tmp;
/* 7746 */       n = m;
/* 7747 */       m = t.length();
/*      */     } 
/*      */     
/* 7750 */     int[] p = new int[n + 1];
/* 7751 */     int[] d = new int[n + 1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7762 */     for (i = 0; i <= n; i++) {
/* 7763 */       p[i] = i;
/*      */     }
/*      */     
/* 7766 */     for (int j = 1; j <= m; j++) {
/* 7767 */       char t_j = t.charAt(j - 1);
/* 7768 */       d[0] = j;
/*      */       
/* 7770 */       for (i = 1; i <= n; i++) {
/* 7771 */         int cost = (s.charAt(i - 1) == t_j) ? 0 : 1;
/*      */         
/* 7773 */         d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
/*      */       } 
/*      */ 
/*      */       
/* 7777 */       int[] _d = p;
/* 7778 */       p = d;
/* 7779 */       d = _d;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 7784 */     return p[n];
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
/*      */   public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
/* 7820 */     if (s == null || t == null) {
/* 7821 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/* 7823 */     if (threshold < 0) {
/* 7824 */       throw new IllegalArgumentException("Threshold must not be negative");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 7871 */     int n = s.length();
/* 7872 */     int m = t.length();
/*      */ 
/*      */     
/* 7875 */     if (n == 0)
/* 7876 */       return (m <= threshold) ? m : -1; 
/* 7877 */     if (m == 0) {
/* 7878 */       return (n <= threshold) ? n : -1;
/*      */     }
/*      */     
/* 7881 */     if (Math.abs(n - m) > threshold) {
/* 7882 */       return -1;
/*      */     }
/*      */     
/* 7885 */     if (n > m) {
/*      */       
/* 7887 */       CharSequence tmp = s;
/* 7888 */       s = t;
/* 7889 */       t = tmp;
/* 7890 */       n = m;
/* 7891 */       m = t.length();
/*      */     } 
/*      */     
/* 7894 */     int[] p = new int[n + 1];
/* 7895 */     int[] d = new int[n + 1];
/*      */ 
/*      */ 
/*      */     
/* 7899 */     int boundary = Math.min(n, threshold) + 1;
/* 7900 */     for (int i = 0; i < boundary; i++) {
/* 7901 */       p[i] = i;
/*      */     }
/*      */ 
/*      */     
/* 7905 */     Arrays.fill(p, boundary, p.length, 2147483647);
/* 7906 */     Arrays.fill(d, 2147483647);
/*      */ 
/*      */     
/* 7909 */     for (int j = 1; j <= m; j++) {
/* 7910 */       char t_j = t.charAt(j - 1);
/* 7911 */       d[0] = j;
/*      */ 
/*      */       
/* 7914 */       int min = Math.max(1, j - threshold);
/* 7915 */       int max = (j > Integer.MAX_VALUE - threshold) ? n : Math.min(n, j + threshold);
/*      */ 
/*      */       
/* 7918 */       if (min > max) {
/* 7919 */         return -1;
/*      */       }
/*      */ 
/*      */       
/* 7923 */       if (min > 1) {
/* 7924 */         d[min - 1] = Integer.MAX_VALUE;
/*      */       }
/*      */ 
/*      */       
/* 7928 */       for (int k = min; k <= max; k++) {
/* 7929 */         if (s.charAt(k - 1) == t_j) {
/*      */           
/* 7931 */           d[k] = p[k - 1];
/*      */         } else {
/*      */           
/* 7934 */           d[k] = 1 + Math.min(Math.min(d[k - 1], p[k]), p[k - 1]);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 7939 */       int[] _d = p;
/* 7940 */       p = d;
/* 7941 */       d = _d;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 7946 */     if (p[n] <= threshold) {
/* 7947 */       return p[n];
/*      */     }
/* 7949 */     return -1;
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
/*      */   public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
/* 7985 */     double DEFAULT_SCALING_FACTOR = 0.1D;
/*      */     
/* 7987 */     if (first == null || second == null) {
/* 7988 */       throw new IllegalArgumentException("Strings must not be null");
/*      */     }
/*      */     
/* 7991 */     int[] mtp = matches(first, second);
/* 7992 */     double m = mtp[0];
/* 7993 */     if (m == 0.0D) {
/* 7994 */       return 0.0D;
/*      */     }
/* 7996 */     double j = (m / first.length() + m / second.length() + (m - mtp[1]) / m) / 3.0D;
/* 7997 */     double jw = (j < 0.7D) ? j : (j + Math.min(0.1D, 1.0D / mtp[3]) * mtp[2] * (1.0D - j));
/* 7998 */     return Math.round(jw * 100.0D) / 100.0D;
/*      */   }
/*      */   
/*      */   private static int[] matches(CharSequence first, CharSequence second) {
/*      */     CharSequence max, min;
/* 8003 */     if (first.length() > second.length()) {
/* 8004 */       max = first;
/* 8005 */       min = second;
/*      */     } else {
/* 8007 */       max = second;
/* 8008 */       min = first;
/*      */     } 
/* 8010 */     int range = Math.max(max.length() / 2 - 1, 0);
/* 8011 */     int[] matchIndexes = new int[min.length()];
/* 8012 */     Arrays.fill(matchIndexes, -1);
/* 8013 */     boolean[] matchFlags = new boolean[max.length()];
/* 8014 */     int matches = 0;
/* 8015 */     for (int mi = 0; mi < min.length(); mi++) {
/* 8016 */       char c1 = min.charAt(mi);
/* 8017 */       for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
/* 8018 */         if (!matchFlags[xi] && c1 == max.charAt(xi)) {
/* 8019 */           matchIndexes[mi] = xi;
/* 8020 */           matchFlags[xi] = true;
/* 8021 */           matches++;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 8026 */     char[] ms1 = new char[matches];
/* 8027 */     char[] ms2 = new char[matches]; int i, si;
/* 8028 */     for (i = 0, si = 0; i < min.length(); i++) {
/* 8029 */       if (matchIndexes[i] != -1) {
/* 8030 */         ms1[si] = min.charAt(i);
/* 8031 */         si++;
/*      */       } 
/*      */     } 
/* 8034 */     for (i = 0, si = 0; i < max.length(); i++) {
/* 8035 */       if (matchFlags[i]) {
/* 8036 */         ms2[si] = max.charAt(i);
/* 8037 */         si++;
/*      */       } 
/*      */     } 
/* 8040 */     int transpositions = 0;
/* 8041 */     for (int j = 0; j < ms1.length; j++) {
/* 8042 */       if (ms1[j] != ms2[j]) {
/* 8043 */         transpositions++;
/*      */       }
/*      */     } 
/* 8046 */     int prefix = 0;
/* 8047 */     for (int k = 0; k < min.length() && 
/* 8048 */       first.charAt(k) == second.charAt(k); k++) {
/* 8049 */       prefix++;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 8054 */     return new int[] { matches, transpositions / 2, prefix, max.length() };
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
/*      */   public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
/* 8084 */     if (term == null || query == null)
/* 8085 */       throw new IllegalArgumentException("Strings must not be null"); 
/* 8086 */     if (locale == null) {
/* 8087 */       throw new IllegalArgumentException("Locale must not be null");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 8094 */     String termLowerCase = term.toString().toLowerCase(locale);
/* 8095 */     String queryLowerCase = query.toString().toLowerCase(locale);
/*      */ 
/*      */     
/* 8098 */     int score = 0;
/*      */ 
/*      */ 
/*      */     
/* 8102 */     int termIndex = 0;
/*      */ 
/*      */     
/* 8105 */     int previousMatchingCharacterIndex = Integer.MIN_VALUE;
/*      */     
/* 8107 */     for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
/* 8108 */       char queryChar = queryLowerCase.charAt(queryIndex);
/*      */       
/* 8110 */       boolean termCharacterMatchFound = false;
/* 8111 */       for (; termIndex < termLowerCase.length() && !termCharacterMatchFound; termIndex++) {
/* 8112 */         char termChar = termLowerCase.charAt(termIndex);
/*      */         
/* 8114 */         if (queryChar == termChar) {
/*      */           
/* 8116 */           score++;
/*      */ 
/*      */ 
/*      */           
/* 8120 */           if (previousMatchingCharacterIndex + 1 == termIndex) {
/* 8121 */             score += 2;
/*      */           }
/*      */           
/* 8124 */           previousMatchingCharacterIndex = termIndex;
/*      */ 
/*      */ 
/*      */           
/* 8128 */           termCharacterMatchFound = true;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 8133 */     return score;
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
/*      */   public static boolean startsWith(CharSequence str, CharSequence prefix) {
/* 8162 */     return startsWith(str, prefix, false);
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
/*      */   public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
/* 8188 */     return startsWith(str, prefix, true);
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
/*      */   private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
/* 8203 */     if (str == null || prefix == null) {
/* 8204 */       return (str == null && prefix == null);
/*      */     }
/* 8206 */     if (prefix.length() > str.length()) {
/* 8207 */       return false;
/*      */     }
/* 8209 */     return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
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
/*      */   public static boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
/* 8235 */     if (isEmpty(sequence) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 8236 */       return false;
/*      */     }
/* 8238 */     for (CharSequence searchString : searchStrings) {
/* 8239 */       if (startsWith(sequence, searchString)) {
/* 8240 */         return true;
/*      */       }
/*      */     } 
/* 8243 */     return false;
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
/*      */   public static boolean endsWith(CharSequence str, CharSequence suffix) {
/* 8274 */     return endsWith(str, suffix, false);
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
/*      */   public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
/* 8301 */     return endsWith(str, suffix, true);
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
/*      */   private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
/* 8316 */     if (str == null || suffix == null) {
/* 8317 */       return (str == null && suffix == null);
/*      */     }
/* 8319 */     if (suffix.length() > str.length()) {
/* 8320 */       return false;
/*      */     }
/* 8322 */     int strOffset = str.length() - suffix.length();
/* 8323 */     return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
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
/*      */   public static String normalizeSpace(String str) {
/* 8370 */     if (isEmpty(str)) {
/* 8371 */       return str;
/*      */     }
/* 8373 */     int size = str.length();
/* 8374 */     char[] newChars = new char[size];
/* 8375 */     int count = 0;
/* 8376 */     int whitespacesCount = 0;
/* 8377 */     boolean startWhitespaces = true;
/* 8378 */     for (int i = 0; i < size; i++) {
/* 8379 */       char actualChar = str.charAt(i);
/* 8380 */       boolean isWhitespace = Character.isWhitespace(actualChar);
/* 8381 */       if (!isWhitespace) {
/* 8382 */         startWhitespaces = false;
/* 8383 */         newChars[count++] = (actualChar == ' ') ? ' ' : actualChar;
/* 8384 */         whitespacesCount = 0;
/*      */       } else {
/* 8386 */         if (whitespacesCount == 0 && !startWhitespaces) {
/* 8387 */           newChars[count++] = " ".charAt(0);
/*      */         }
/* 8389 */         whitespacesCount++;
/*      */       } 
/*      */     } 
/* 8392 */     if (startWhitespaces) {
/* 8393 */       return "";
/*      */     }
/* 8395 */     return (new String(newChars, 0, count - ((whitespacesCount > 0) ? 1 : 0))).trim();
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
/*      */   public static boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
/* 8420 */     if (isEmpty(sequence) || ArrayUtils.isEmpty((Object[])searchStrings)) {
/* 8421 */       return false;
/*      */     }
/* 8423 */     for (CharSequence searchString : searchStrings) {
/* 8424 */       if (endsWith(sequence, searchString)) {
/* 8425 */         return true;
/*      */       }
/*      */     } 
/* 8428 */     return false;
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
/*      */   private static String appendIfMissing(String str, CharSequence suffix, boolean ignoreCase, CharSequence... suffixes) {
/* 8443 */     if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
/* 8444 */       return str;
/*      */     }
/* 8446 */     if (suffixes != null && suffixes.length > 0) {
/* 8447 */       for (CharSequence s : suffixes) {
/* 8448 */         if (endsWith(str, s, ignoreCase)) {
/* 8449 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/* 8453 */     return str + suffix.toString();
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
/*      */   public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
/* 8491 */     return appendIfMissing(str, suffix, false, suffixes);
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
/*      */   public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
/* 8529 */     return appendIfMissing(str, suffix, true, suffixes);
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
/*      */   private static String prependIfMissing(String str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
/* 8544 */     if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
/* 8545 */       return str;
/*      */     }
/* 8547 */     if (prefixes != null && prefixes.length > 0) {
/* 8548 */       for (CharSequence p : prefixes) {
/* 8549 */         if (startsWith(str, p, ignoreCase)) {
/* 8550 */           return str;
/*      */         }
/*      */       } 
/*      */     }
/* 8554 */     return prefix.toString() + str;
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
/*      */   public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
/* 8592 */     return prependIfMissing(str, prefix, false, prefixes);
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
/*      */   public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
/* 8630 */     return prependIfMissing(str, prefix, true, prefixes);
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
/*      */   @Deprecated
/*      */   public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
/* 8650 */     return (charsetName != null) ? new String(bytes, charsetName) : new String(bytes, Charset.defaultCharset());
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
/*      */   public static String toEncodedString(byte[] bytes, Charset charset) {
/* 8667 */     return new String(bytes, (charset != null) ? charset : Charset.defaultCharset());
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
/*      */   public static String wrap(String str, char wrapWith) {
/* 8693 */     if (isEmpty(str) || wrapWith == '\000') {
/* 8694 */       return str;
/*      */     }
/*      */     
/* 8697 */     return wrapWith + str + wrapWith;
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
/*      */   public static String wrap(String str, String wrapWith) {
/* 8731 */     if (isEmpty(str) || isEmpty(wrapWith)) {
/* 8732 */       return str;
/*      */     }
/*      */     
/* 8735 */     return wrapWith.concat(str).concat(wrapWith);
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
/*      */   public static String wrapIfMissing(String str, char wrapWith) {
/* 8764 */     if (isEmpty(str) || wrapWith == '\000') {
/* 8765 */       return str;
/*      */     }
/* 8767 */     StringBuilder builder = new StringBuilder(str.length() + 2);
/* 8768 */     if (str.charAt(0) != wrapWith) {
/* 8769 */       builder.append(wrapWith);
/*      */     }
/* 8771 */     builder.append(str);
/* 8772 */     if (str.charAt(str.length() - 1) != wrapWith) {
/* 8773 */       builder.append(wrapWith);
/*      */     }
/* 8775 */     return builder.toString();
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
/*      */   public static String wrapIfMissing(String str, String wrapWith) {
/* 8808 */     if (isEmpty(str) || isEmpty(wrapWith)) {
/* 8809 */       return str;
/*      */     }
/* 8811 */     StringBuilder builder = new StringBuilder(str.length() + wrapWith.length() + wrapWith.length());
/* 8812 */     if (!str.startsWith(wrapWith)) {
/* 8813 */       builder.append(wrapWith);
/*      */     }
/* 8815 */     builder.append(str);
/* 8816 */     if (!str.endsWith(wrapWith)) {
/* 8817 */       builder.append(wrapWith);
/*      */     }
/* 8819 */     return builder.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */