/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class WildcardFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5037645902506953517L;
/*     */   private final String[] wildcards;
/*     */   
/*     */   public WildcardFilter(String wildcard) {
/*  66 */     if (wildcard == null) {
/*  67 */       throw new IllegalArgumentException("The wildcard must not be null");
/*     */     }
/*  69 */     this.wildcards = new String[] { wildcard };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFilter(String[] wildcards) {
/*  79 */     if (wildcards == null) {
/*  80 */       throw new IllegalArgumentException("The wildcard array must not be null");
/*     */     }
/*  82 */     this.wildcards = new String[wildcards.length];
/*  83 */     System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WildcardFilter(List<String> wildcards) {
/*  94 */     if (wildcards == null) {
/*  95 */       throw new IllegalArgumentException("The wildcard list must not be null");
/*     */     }
/*  97 */     this.wildcards = wildcards.<String>toArray(new String[wildcards.size()]);
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
/* 110 */     if (dir != null && (new File(dir, name)).isDirectory()) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     for (String wildcard : this.wildcards) {
/* 115 */       if (FilenameUtils.wildcardMatch(name, wildcard)) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 120 */     return false;
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
/* 131 */     if (file.isDirectory()) {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     for (String wildcard : this.wildcards) {
/* 136 */       if (FilenameUtils.wildcardMatch(file.getName(), wildcard)) {
/* 137 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\WildcardFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */