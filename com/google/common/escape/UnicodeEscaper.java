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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class UnicodeEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
/* 106 */     int index = start;
/* 107 */     while (index < end) {
/* 108 */       int cp = codePointAt(csq, index, end);
/* 109 */       if (cp < 0 || escape(cp) != null) {
/*     */         break;
/*     */       }
/* 112 */       index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/*     */     } 
/* 114 */     return index;
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
/*     */   public String escape(String string) {
/* 137 */     Preconditions.checkNotNull(string);
/* 138 */     int end = string.length();
/* 139 */     int index = nextEscapeIndex(string, 0, end);
/* 140 */     return (index == end) ? string : escapeSlow(string, index);
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
/*     */   protected final String escapeSlow(String s, int index) {
/* 159 */     int end = s.length();
/*     */ 
/*     */     
/* 162 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 163 */     int destIndex = 0;
/* 164 */     int unescapedChunkStart = 0;
/*     */     
/* 166 */     while (index < end) {
/* 167 */       int cp = codePointAt(s, index, end);
/* 168 */       if (cp < 0) {
/* 169 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 174 */       char[] escaped = escape(cp);
/* 175 */       int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/* 176 */       if (escaped != null) {
/* 177 */         int i = index - unescapedChunkStart;
/*     */ 
/*     */ 
/*     */         
/* 181 */         int sizeNeeded = destIndex + i + escaped.length;
/* 182 */         if (dest.length < sizeNeeded) {
/* 183 */           int destLength = sizeNeeded + end - index + 32;
/* 184 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         } 
/*     */         
/* 187 */         if (i > 0) {
/* 188 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 189 */           destIndex += i;
/*     */         } 
/* 191 */         if (escaped.length > 0) {
/* 192 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 193 */           destIndex += escaped.length;
/*     */         } 
/*     */         
/* 196 */         unescapedChunkStart = nextIndex;
/*     */       } 
/* 198 */       index = nextEscapeIndex(s, nextIndex, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 203 */     int charsSkipped = end - unescapedChunkStart;
/* 204 */     if (charsSkipped > 0) {
/* 205 */       int endIndex = destIndex + charsSkipped;
/* 206 */       if (dest.length < endIndex) {
/* 207 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 209 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 210 */       destIndex = endIndex;
/*     */     } 
/* 212 */     return new String(dest, 0, destIndex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int codePointAt(CharSequence seq, int index, int end) {
/* 246 */     Preconditions.checkNotNull(seq);
/* 247 */     if (index < end) {
/* 248 */       char c1 = seq.charAt(index++);
/* 249 */       if (c1 < '?' || c1 > '?')
/*     */       {
/* 251 */         return c1; } 
/* 252 */       if (c1 <= '?') {
/*     */         
/* 254 */         if (index == end) {
/* 255 */           return -c1;
/*     */         }
/*     */         
/* 258 */         char c2 = seq.charAt(index);
/* 259 */         if (Character.isLowSurrogate(c2)) {
/* 260 */           return Character.toCodePoint(c1, c2);
/*     */         }
/* 262 */         throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + c2 + " at index " + index + " in '" + seq + "'");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 273 */       throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + c1 + " at index " + (index - 1) + " in '" + seq + "'");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 293 */     if (size < 0) {
/* 294 */       throw new AssertionError("Cannot increase internal buffer any further");
/*     */     }
/* 296 */     char[] copy = new char[size];
/* 297 */     if (index > 0) {
/* 298 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 300 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\escape\UnicodeEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */