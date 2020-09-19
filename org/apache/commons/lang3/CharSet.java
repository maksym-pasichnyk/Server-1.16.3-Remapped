/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSet
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5947847346149275958L;
/*  47 */   public static final CharSet EMPTY = new CharSet(new String[] { (String)null });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final CharSet ASCII_ALPHA = new CharSet(new String[] { "a-zA-Z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final CharSet ASCII_ALPHA_LOWER = new CharSet(new String[] { "a-z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static final CharSet ASCII_ALPHA_UPPER = new CharSet(new String[] { "A-Z" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final CharSet ASCII_NUMERIC = new CharSet(new String[] { "0-9" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected static final Map<String, CharSet> COMMON = Collections.synchronizedMap(new HashMap<String, CharSet>());
/*     */   
/*     */   static {
/*  81 */     COMMON.put(null, EMPTY);
/*  82 */     COMMON.put("", EMPTY);
/*  83 */     COMMON.put("a-zA-Z", ASCII_ALPHA);
/*  84 */     COMMON.put("A-Za-z", ASCII_ALPHA);
/*  85 */     COMMON.put("a-z", ASCII_ALPHA_LOWER);
/*  86 */     COMMON.put("A-Z", ASCII_ALPHA_UPPER);
/*  87 */     COMMON.put("0-9", ASCII_NUMERIC);
/*     */   }
/*     */ 
/*     */   
/*  91 */   private final Set<CharRange> set = Collections.synchronizedSet(new HashSet<CharRange>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet getInstance(String... setStrs) {
/* 156 */     if (setStrs == null) {
/* 157 */       return null;
/*     */     }
/* 159 */     if (setStrs.length == 1) {
/* 160 */       CharSet common = COMMON.get(setStrs[0]);
/* 161 */       if (common != null) {
/* 162 */         return common;
/*     */       }
/*     */     } 
/* 165 */     return new CharSet(setStrs);
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
/*     */   protected CharSet(String... set) {
/* 178 */     int sz = set.length;
/* 179 */     for (int i = 0; i < sz; i++) {
/* 180 */       add(set[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(String str) {
/* 191 */     if (str == null) {
/*     */       return;
/*     */     }
/*     */     
/* 195 */     int len = str.length();
/* 196 */     int pos = 0;
/* 197 */     while (pos < len) {
/* 198 */       int remainder = len - pos;
/* 199 */       if (remainder >= 4 && str.charAt(pos) == '^' && str.charAt(pos + 2) == '-') {
/*     */         
/* 201 */         this.set.add(CharRange.isNotIn(str.charAt(pos + 1), str.charAt(pos + 3)));
/* 202 */         pos += 4; continue;
/* 203 */       }  if (remainder >= 3 && str.charAt(pos + 1) == '-') {
/*     */         
/* 205 */         this.set.add(CharRange.isIn(str.charAt(pos), str.charAt(pos + 2)));
/* 206 */         pos += 3; continue;
/* 207 */       }  if (remainder >= 2 && str.charAt(pos) == '^') {
/*     */         
/* 209 */         this.set.add(CharRange.isNot(str.charAt(pos + 1)));
/* 210 */         pos += 2;
/*     */         continue;
/*     */       } 
/* 213 */       this.set.add(CharRange.is(str.charAt(pos)));
/* 214 */       pos++;
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
/*     */   CharRange[] getCharRanges() {
/* 229 */     return this.set.<CharRange>toArray(new CharRange[this.set.size()]);
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
/*     */   public boolean contains(char ch) {
/* 241 */     for (CharRange range : this.set) {
/* 242 */       if (range.contains(ch)) {
/* 243 */         return true;
/*     */       }
/*     */     } 
/* 246 */     return false;
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
/*     */   public boolean equals(Object obj) {
/* 264 */     if (obj == this) {
/* 265 */       return true;
/*     */     }
/* 267 */     if (!(obj instanceof CharSet)) {
/* 268 */       return false;
/*     */     }
/* 270 */     CharSet other = (CharSet)obj;
/* 271 */     return this.set.equals(other.set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 282 */     return 89 + this.set.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 292 */     return this.set.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\CharSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */