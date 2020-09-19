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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSetUtils
/*     */ {
/*     */   public static String squeeze(String str, String... set) {
/*  64 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/*  65 */       return str;
/*     */     }
/*  67 */     CharSet chars = CharSet.getInstance(set);
/*  68 */     StringBuilder buffer = new StringBuilder(str.length());
/*  69 */     char[] chrs = str.toCharArray();
/*  70 */     int sz = chrs.length;
/*  71 */     char lastChar = chrs[0];
/*  72 */     char ch = ' ';
/*  73 */     Character inChars = null;
/*  74 */     Character notInChars = null;
/*  75 */     buffer.append(lastChar);
/*  76 */     int i = 1; while (true) { if (i < sz)
/*  77 */       { ch = chrs[i];
/*  78 */         if (ch == lastChar)
/*  79 */         { if (inChars != null && ch == inChars.charValue()) {
/*     */             continue;
/*     */           }
/*  82 */           if (notInChars == null || ch != notInChars.charValue())
/*  83 */           { if (chars.contains(ch))
/*  84 */             { inChars = Character.valueOf(ch); }
/*     */             else
/*     */             
/*  87 */             { notInChars = Character.valueOf(ch);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  92 */               buffer.append(ch);
/*  93 */               lastChar = ch; }  continue; }  }  } else { break; }  buffer.append(ch); lastChar = ch; i++; }
/*     */     
/*  95 */     return buffer.toString();
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
/*     */   public static boolean containsAny(String str, String... set) {
/* 120 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 121 */       return false;
/*     */     }
/* 123 */     CharSet chars = CharSet.getInstance(set);
/* 124 */     for (char c : str.toCharArray()) {
/* 125 */       if (chars.contains(c)) {
/* 126 */         return true;
/*     */       }
/*     */     } 
/* 129 */     return false;
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
/*     */   public static int count(String str, String... set) {
/* 153 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 154 */       return 0;
/*     */     }
/* 156 */     CharSet chars = CharSet.getInstance(set);
/* 157 */     int count = 0;
/* 158 */     for (char c : str.toCharArray()) {
/* 159 */       if (chars.contains(c)) {
/* 160 */         count++;
/*     */       }
/*     */     } 
/* 163 */     return count;
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
/*     */   public static String keep(String str, String... set) {
/* 188 */     if (str == null) {
/* 189 */       return null;
/*     */     }
/* 191 */     if (str.isEmpty() || deepEmpty(set)) {
/* 192 */       return "";
/*     */     }
/* 194 */     return modify(str, set, true);
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
/*     */   public static String delete(String str, String... set) {
/* 218 */     if (StringUtils.isEmpty(str) || deepEmpty(set)) {
/* 219 */       return str;
/*     */     }
/* 221 */     return modify(str, set, false);
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
/*     */   private static String modify(String str, String[] set, boolean expect) {
/* 234 */     CharSet chars = CharSet.getInstance(set);
/* 235 */     StringBuilder buffer = new StringBuilder(str.length());
/* 236 */     char[] chrs = str.toCharArray();
/* 237 */     int sz = chrs.length;
/* 238 */     for (int i = 0; i < sz; i++) {
/* 239 */       if (chars.contains(chrs[i]) == expect) {
/* 240 */         buffer.append(chrs[i]);
/*     */       }
/*     */     } 
/* 243 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean deepEmpty(String[] strings) {
/* 254 */     if (strings != null) {
/* 255 */       for (String s : strings) {
/* 256 */         if (StringUtils.isNotEmpty(s)) {
/* 257 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 261 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\CharSetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */