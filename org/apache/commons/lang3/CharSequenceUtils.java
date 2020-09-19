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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSequenceUtils
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/*     */   public static CharSequence subSequence(CharSequence cs, int start) {
/*  57 */     return (cs == null) ? null : cs.subSequence(start, cs.length());
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
/*     */   static int indexOf(CharSequence cs, int searchChar, int start) {
/*  71 */     if (cs instanceof String) {
/*  72 */       return ((String)cs).indexOf(searchChar, start);
/*     */     }
/*  74 */     int sz = cs.length();
/*  75 */     if (start < 0) {
/*  76 */       start = 0;
/*     */     }
/*  78 */     for (int i = start; i < sz; i++) {
/*  79 */       if (cs.charAt(i) == searchChar) {
/*  80 */         return i;
/*     */       }
/*     */     } 
/*  83 */     return -1;
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
/*     */   static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
/*  95 */     return cs.toString().indexOf(searchChar.toString(), start);
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
/*     */   static int lastIndexOf(CharSequence cs, int searchChar, int start) {
/* 117 */     if (cs instanceof String) {
/* 118 */       return ((String)cs).lastIndexOf(searchChar, start);
/*     */     }
/* 120 */     int sz = cs.length();
/* 121 */     if (start < 0) {
/* 122 */       return -1;
/*     */     }
/* 124 */     if (start >= sz) {
/* 125 */       start = sz - 1;
/*     */     }
/* 127 */     for (int i = start; i >= 0; i--) {
/* 128 */       if (cs.charAt(i) == searchChar) {
/* 129 */         return i;
/*     */       }
/*     */     } 
/* 132 */     return -1;
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
/*     */   static int lastIndexOf(CharSequence cs, CharSequence searchChar, int start) {
/* 144 */     return cs.toString().lastIndexOf(searchChar.toString(), start);
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
/*     */   static char[] toCharArray(CharSequence cs) {
/* 163 */     if (cs instanceof String) {
/* 164 */       return ((String)cs).toCharArray();
/*     */     }
/* 166 */     int sz = cs.length();
/* 167 */     char[] array = new char[cs.length()];
/* 168 */     for (int i = 0; i < sz; i++) {
/* 169 */       array[i] = cs.charAt(i);
/*     */     }
/* 171 */     return array;
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
/*     */   static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
/* 187 */     if (cs instanceof String && substring instanceof String) {
/* 188 */       return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
/*     */     }
/* 190 */     int index1 = thisStart;
/* 191 */     int index2 = start;
/* 192 */     int tmpLen = length;
/*     */ 
/*     */     
/* 195 */     int srcLen = cs.length() - thisStart;
/* 196 */     int otherLen = substring.length() - start;
/*     */ 
/*     */     
/* 199 */     if (thisStart < 0 || start < 0 || length < 0) {
/* 200 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 204 */     if (srcLen < length || otherLen < length) {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     while (tmpLen-- > 0) {
/* 209 */       char c1 = cs.charAt(index1++);
/* 210 */       char c2 = substring.charAt(index2++);
/*     */       
/* 212 */       if (c1 == c2) {
/*     */         continue;
/*     */       }
/*     */       
/* 216 */       if (!ignoreCase) {
/* 217 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 221 */       if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && 
/* 222 */         Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
/* 223 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 227 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\CharSequenceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */