/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ArrayBasedUnicodeEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*     */   private final char[][] replacements;
/*     */   private final int replacementsLength;
/*     */   private final int safeMin;
/*     */   private final int safeMax;
/*     */   private final char safeMinChar;
/*     */   private final char safeMaxChar;
/*     */   
/*     */   protected ArrayBasedUnicodeEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax, @Nullable String unsafeReplacement) {
/*  77 */     this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax, unsafeReplacement);
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
/*     */   protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @Nullable String unsafeReplacement) {
/* 100 */     Preconditions.checkNotNull(escaperMap);
/* 101 */     this.replacements = escaperMap.getReplacementArray();
/* 102 */     this.replacementsLength = this.replacements.length;
/* 103 */     if (safeMax < safeMin) {
/*     */ 
/*     */       
/* 106 */       safeMax = -1;
/* 107 */       safeMin = Integer.MAX_VALUE;
/*     */     } 
/* 109 */     this.safeMin = safeMin;
/* 110 */     this.safeMax = safeMax;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (safeMin >= 55296) {
/*     */ 
/*     */       
/* 128 */       this.safeMinChar = Character.MAX_VALUE;
/* 129 */       this.safeMaxChar = Character.MIN_VALUE;
/*     */     }
/*     */     else {
/*     */       
/* 133 */       this.safeMinChar = (char)safeMin;
/* 134 */       this.safeMaxChar = (char)Math.min(safeMax, 55295);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String escape(String s) {
/* 144 */     Preconditions.checkNotNull(s);
/* 145 */     for (int i = 0; i < s.length(); i++) {
/* 146 */       char c = s.charAt(i);
/* 147 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar)
/*     */       {
/*     */         
/* 150 */         return escapeSlow(s, i);
/*     */       }
/*     */     } 
/* 153 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 159 */     while (index < end) {
/* 160 */       char c = csq.charAt(index);
/* 161 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 166 */       index++;
/*     */     } 
/* 168 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final char[] escape(int cp) {
/* 178 */     if (cp < this.replacementsLength) {
/* 179 */       char[] chars = this.replacements[cp];
/* 180 */       if (chars != null) {
/* 181 */         return chars;
/*     */       }
/*     */     } 
/* 184 */     if (cp >= this.safeMin && cp <= this.safeMax) {
/* 185 */       return null;
/*     */     }
/* 187 */     return escapeUnsafe(cp);
/*     */   }
/*     */   
/*     */   protected abstract char[] escapeUnsafe(int paramInt);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\escape\ArrayBasedUnicodeEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */