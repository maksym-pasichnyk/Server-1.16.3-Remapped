/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class RegexFileFilter
/*     */   extends AbstractFileFilter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4269646126155225062L;
/*     */   private final Pattern pattern;
/*     */   
/*     */   public RegexFileFilter(String pattern) {
/*  58 */     if (pattern == null) {
/*  59 */       throw new IllegalArgumentException("Pattern is missing");
/*     */     }
/*     */     
/*  62 */     this.pattern = Pattern.compile(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexFileFilter(String pattern, IOCase caseSensitivity) {
/*  73 */     if (pattern == null) {
/*  74 */       throw new IllegalArgumentException("Pattern is missing");
/*     */     }
/*  76 */     int flags = 0;
/*  77 */     if (caseSensitivity != null && !caseSensitivity.isCaseSensitive()) {
/*  78 */       flags = 2;
/*     */     }
/*  80 */     this.pattern = Pattern.compile(pattern, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexFileFilter(String pattern, int flags) {
/*  91 */     if (pattern == null) {
/*  92 */       throw new IllegalArgumentException("Pattern is missing");
/*     */     }
/*  94 */     this.pattern = Pattern.compile(pattern, flags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexFileFilter(Pattern pattern) {
/* 104 */     if (pattern == null) {
/* 105 */       throw new IllegalArgumentException("Pattern is missing");
/*     */     }
/*     */     
/* 108 */     this.pattern = pattern;
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
/*     */   public boolean accept(File dir, String name) {
/* 120 */     return this.pattern.matcher(name).matches();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\filefilter\RegexFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */