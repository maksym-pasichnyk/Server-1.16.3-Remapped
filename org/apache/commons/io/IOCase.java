/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum IOCase
/*     */   implements Serializable
/*     */ {
/*  42 */   SENSITIVE("Sensitive", true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   INSENSITIVE("Insensitive", false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   SYSTEM("System", !FilenameUtils.isSystemWindows());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -6343169151696340687L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   private final transient boolean sensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOCase forName(String name) {
/*  81 */     for (IOCase ioCase : values()) {
/*     */       
/*  83 */       if (ioCase.getName().equals(name))
/*     */       {
/*  85 */         return ioCase;
/*     */       }
/*     */     } 
/*  88 */     throw new IllegalArgumentException("Invalid IOCase name: " + name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IOCase(String name, boolean sensitive) {
/*  99 */     this.name = name;
/* 100 */     this.sensitive = sensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 110 */     return forName(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 120 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/* 129 */     return this.sensitive;
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
/*     */   public int checkCompareTo(String str1, String str2) {
/* 145 */     if (str1 == null || str2 == null) {
/* 146 */       throw new NullPointerException("The strings must not be null");
/*     */     }
/* 148 */     return this.sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
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
/*     */   public boolean checkEquals(String str1, String str2) {
/* 163 */     if (str1 == null || str2 == null) {
/* 164 */       throw new NullPointerException("The strings must not be null");
/*     */     }
/* 166 */     return this.sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
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
/*     */   public boolean checkStartsWith(String str, String start) {
/* 181 */     return str.regionMatches(!this.sensitive, 0, start, 0, start.length());
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
/*     */   public boolean checkEndsWith(String str, String end) {
/* 196 */     int endLen = end.length();
/* 197 */     return str.regionMatches(!this.sensitive, str.length() - endLen, end, 0, endLen);
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
/*     */   public int checkIndexOf(String str, int strStartIndex, String search) {
/* 216 */     int endIndex = str.length() - search.length();
/* 217 */     if (endIndex >= strStartIndex) {
/* 218 */       for (int i = strStartIndex; i <= endIndex; i++) {
/* 219 */         if (checkRegionMatches(str, i, search)) {
/* 220 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/* 224 */     return -1;
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
/*     */   public boolean checkRegionMatches(String str, int strStartIndex, String search) {
/* 240 */     return str.regionMatches(!this.sensitive, strStartIndex, search, 0, search.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 251 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\IOCase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */