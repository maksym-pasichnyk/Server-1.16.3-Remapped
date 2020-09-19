/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class CharEscaperBuilder
/*     */ {
/*     */   private final Map<Character, String> map;
/*     */   
/*     */   private static class CharArrayDecorator
/*     */     extends CharEscaper
/*     */   {
/*     */     private final char[][] replacements;
/*     */     private final int replaceLength;
/*     */     
/*     */     CharArrayDecorator(char[][] replacements) {
/*  46 */       this.replacements = replacements;
/*  47 */       this.replaceLength = replacements.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String escape(String s) {
/*  56 */       int slen = s.length();
/*  57 */       for (int index = 0; index < slen; index++) {
/*  58 */         char c = s.charAt(index);
/*  59 */         if (c < this.replacements.length && this.replacements[c] != null) {
/*  60 */           return escapeSlow(s, index);
/*     */         }
/*     */       } 
/*  63 */       return s;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] escape(char c) {
/*  68 */       return (c < this.replaceLength) ? this.replacements[c] : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private int max = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharEscaperBuilder() {
/*  82 */     this.map = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscape(char c, String r) {
/*  90 */     this.map.put(Character.valueOf(c), Preconditions.checkNotNull(r));
/*  91 */     if (c > this.max) {
/*  92 */       this.max = c;
/*     */     }
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscapes(char[] cs, String r) {
/* 102 */     Preconditions.checkNotNull(r);
/* 103 */     for (char c : cs) {
/* 104 */       addEscape(c, r);
/*     */     }
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[][] toArray() {
/* 117 */     char[][] result = new char[this.max + 1][];
/* 118 */     for (Map.Entry<Character, String> entry : this.map.entrySet()) {
/* 119 */       result[((Character)entry.getKey()).charValue()] = ((String)entry.getValue()).toCharArray();
/*     */     }
/* 121 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Escaper toEscaper() {
/* 131 */     return new CharArrayDecorator(toArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\escape\CharEscaperBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */