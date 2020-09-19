/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CharEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD_MULTIPLIER = 2;
/*     */   
/*     */   public String escape(String string) {
/*  57 */     Preconditions.checkNotNull(string);
/*     */     
/*  59 */     int length = string.length();
/*  60 */     for (int index = 0; index < length; index++) {
/*  61 */       if (escape(string.charAt(index)) != null) {
/*  62 */         return escapeSlow(string, index);
/*     */       }
/*     */     } 
/*  65 */     return string;
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
/*     */   protected final String escapeSlow(String s, int index) {
/*  80 */     int slen = s.length();
/*     */ 
/*     */     
/*  83 */     char[] dest = Platform.charBufferFromThreadLocal();
/*  84 */     int destSize = dest.length;
/*  85 */     int destIndex = 0;
/*  86 */     int lastEscape = 0;
/*     */ 
/*     */ 
/*     */     
/*  90 */     for (; index < slen; index++) {
/*     */ 
/*     */       
/*  93 */       char[] r = escape(s.charAt(index));
/*     */ 
/*     */       
/*  96 */       if (r != null) {
/*     */ 
/*     */ 
/*     */         
/* 100 */         int rlen = r.length;
/* 101 */         int charsSkipped = index - lastEscape;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 106 */         int sizeNeeded = destIndex + charsSkipped + rlen;
/* 107 */         if (destSize < sizeNeeded) {
/* 108 */           destSize = sizeNeeded + 2 * (slen - index);
/* 109 */           dest = growBuffer(dest, destIndex, destSize);
/*     */         } 
/*     */ 
/*     */         
/* 113 */         if (charsSkipped > 0) {
/* 114 */           s.getChars(lastEscape, index, dest, destIndex);
/* 115 */           destIndex += charsSkipped;
/*     */         } 
/*     */ 
/*     */         
/* 119 */         if (rlen > 0) {
/* 120 */           System.arraycopy(r, 0, dest, destIndex, rlen);
/* 121 */           destIndex += rlen;
/*     */         } 
/* 123 */         lastEscape = index + 1;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     int charsLeft = slen - lastEscape;
/* 128 */     if (charsLeft > 0) {
/* 129 */       int sizeNeeded = destIndex + charsLeft;
/* 130 */       if (destSize < sizeNeeded)
/*     */       {
/*     */         
/* 133 */         dest = growBuffer(dest, destIndex, sizeNeeded);
/*     */       }
/* 135 */       s.getChars(lastEscape, slen, dest, destIndex);
/* 136 */       destIndex = sizeNeeded;
/*     */     } 
/* 138 */     return new String(dest, 0, destIndex);
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
/*     */   protected abstract char[] escape(char paramChar);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 163 */     if (size < 0) {
/* 164 */       throw new AssertionError("Cannot increase internal buffer any further");
/*     */     }
/* 166 */     char[] copy = new char[size];
/* 167 */     if (index > 0) {
/* 168 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 170 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\escape\CharEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */