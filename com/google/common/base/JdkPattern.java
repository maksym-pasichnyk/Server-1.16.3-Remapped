/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class JdkPattern
/*     */   extends CommonPattern
/*     */   implements Serializable
/*     */ {
/*     */   private final Pattern pattern;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   JdkPattern(Pattern pattern) {
/*  30 */     this.pattern = Preconditions.<Pattern>checkNotNull(pattern);
/*     */   }
/*     */ 
/*     */   
/*     */   CommonMatcher matcher(CharSequence t) {
/*  35 */     return new JdkMatcher(this.pattern.matcher(t));
/*     */   }
/*     */ 
/*     */   
/*     */   String pattern() {
/*  40 */     return this.pattern.pattern();
/*     */   }
/*     */ 
/*     */   
/*     */   int flags() {
/*  45 */     return this.pattern.flags();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  50 */     return this.pattern.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  55 */     return this.pattern.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  60 */     if (!(o instanceof JdkPattern)) {
/*  61 */       return false;
/*     */     }
/*  63 */     return this.pattern.equals(((JdkPattern)o).pattern);
/*     */   }
/*     */   
/*     */   private static final class JdkMatcher extends CommonMatcher {
/*     */     final Matcher matcher;
/*     */     
/*     */     JdkMatcher(Matcher matcher) {
/*  70 */       this.matcher = Preconditions.<Matcher>checkNotNull(matcher);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean matches() {
/*  75 */       return this.matcher.matches();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean find() {
/*  80 */       return this.matcher.find();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean find(int index) {
/*  85 */       return this.matcher.find(index);
/*     */     }
/*     */ 
/*     */     
/*     */     String replaceAll(String replacement) {
/*  90 */       return this.matcher.replaceAll(replacement);
/*     */     }
/*     */ 
/*     */     
/*     */     int end() {
/*  95 */       return this.matcher.end();
/*     */     }
/*     */ 
/*     */     
/*     */     int start() {
/* 100 */       return this.matcher.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\JdkPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */