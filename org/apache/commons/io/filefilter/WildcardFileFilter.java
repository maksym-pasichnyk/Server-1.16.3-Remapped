/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.io.IOCase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WildcardFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7426486598995782105L;
/*     */   private final String[] wildcards;
/*     */   private final IOCase caseSensitivity;
/*     */   
/*     */   public WildcardFileFilter(String wildcard) {
/*  66 */     this(wildcard, IOCase.SENSITIVE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFileFilter(String wildcard, IOCase caseSensitivity) {
/*  77 */     if (wildcard == null) {
/*  78 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/*  80 */     this.wildcards = new String[] { wildcard };
/*  81 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFileFilter(String[] wildcards) {
/*  92 */     this(wildcards, IOCase.SENSITIVE);
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
/*     */   public WildcardFileFilter(String[] wildcards, IOCase caseSensitivity) {
/* 104 */     if (wildcards == null) {
/* 105 */       throw new IllegalArgumentException("The wildcard array must not be null");
/*     */     }
/* 107 */     this.wildcards = new String[wildcards.length];
/* 108 */     System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
/* 109 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFileFilter(List<String> wildcards) {
/* 120 */     this(wildcards, IOCase.SENSITIVE);
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
/*     */   public WildcardFileFilter(List<String> wildcards, IOCase caseSensitivity) {
/* 132 */     if (wildcards == null) {
/* 133 */       throw new IllegalArgumentException("The wildcard list must not be null");
/*     */     }
/* 135 */     this.wildcards = wildcards.<String>toArray(new String[wildcards.size()]);
/* 136 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public boolean accept(File dir, String name) {
/* 149 */     for (String wildcard : this.wildcards) {
/* 150 */       if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
/* 151 */         return true;
/*     */       }
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(File file) {
/* 165 */     String name = file.getName();
/* 166 */     for (String wildcard : this.wildcards) {
/* 167 */       if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
/* 168 */         return true;
/*     */       }
/*     */     } 
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 181 */     StringBuilder buffer = new StringBuilder();
/* 182 */     buffer.append(super.toString());
/* 183 */     buffer.append("(");
/* 184 */     if (this.wildcards != null) {
/* 185 */       for (int i = 0; i < this.wildcards.length; i++) {
/* 186 */         if (i > 0) {
/* 187 */           buffer.append(",");
/*     */         }
/* 189 */         buffer.append(this.wildcards[i]);
/*     */       } 
/*     */     }
/* 192 */     buffer.append(")");
/* 193 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\WildcardFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */