/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.escape.UnicodeEscaper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*  56 */   private static final char[] PLUS_SIGN = new char[] { '+' };
/*     */ 
/*     */   
/*  59 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean plusForSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] safeOctets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace) {
/*  89 */     Preconditions.checkNotNull(safeChars);
/*     */     
/*  91 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/*  92 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */     
/*  95 */     safeChars = safeChars + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
/*     */ 
/*     */     
/*  98 */     if (plusForSpace && safeChars.contains(" ")) {
/*  99 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 102 */     this.plusForSpace = plusForSpace;
/* 103 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] createSafeOctets(String safeChars) {
/* 112 */     int maxChar = -1;
/* 113 */     char[] safeCharArray = safeChars.toCharArray();
/* 114 */     for (char c : safeCharArray) {
/* 115 */       maxChar = Math.max(c, maxChar);
/*     */     }
/* 117 */     boolean[] octets = new boolean[maxChar + 1];
/* 118 */     for (char c : safeCharArray) {
/* 119 */       octets[c] = true;
/*     */     }
/* 121 */     return octets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 130 */     Preconditions.checkNotNull(csq);
/* 131 */     for (; index < end; index++) {
/* 132 */       char c = csq.charAt(index);
/* 133 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/*     */         break;
/*     */       }
/*     */     } 
/* 137 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escape(String s) {
/* 146 */     Preconditions.checkNotNull(s);
/* 147 */     int slen = s.length();
/* 148 */     for (int index = 0; index < slen; index++) {
/* 149 */       char c = s.charAt(index);
/* 150 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/* 151 */         return escapeSlow(s, index);
/*     */       }
/*     */     } 
/* 154 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] escape(int cp) {
/* 164 */     if (cp < this.safeOctets.length && this.safeOctets[cp])
/* 165 */       return null; 
/* 166 */     if (cp == 32 && this.plusForSpace)
/* 167 */       return PLUS_SIGN; 
/* 168 */     if (cp <= 127) {
/*     */ 
/*     */       
/* 171 */       char[] dest = new char[3];
/* 172 */       dest[0] = '%';
/* 173 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 174 */       dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
/* 175 */       return dest;
/* 176 */     }  if (cp <= 2047) {
/*     */ 
/*     */       
/* 179 */       char[] dest = new char[6];
/* 180 */       dest[0] = '%';
/* 181 */       dest[3] = '%';
/* 182 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 183 */       cp >>>= 4;
/* 184 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 185 */       cp >>>= 2;
/* 186 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 187 */       cp >>>= 4;
/* 188 */       dest[1] = UPPER_HEX_DIGITS[0xC | cp];
/* 189 */       return dest;
/* 190 */     }  if (cp <= 65535) {
/*     */ 
/*     */       
/* 193 */       char[] dest = new char[9];
/* 194 */       dest[0] = '%';
/* 195 */       dest[1] = 'E';
/* 196 */       dest[3] = '%';
/* 197 */       dest[6] = '%';
/* 198 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 199 */       cp >>>= 4;
/* 200 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 201 */       cp >>>= 2;
/* 202 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 203 */       cp >>>= 4;
/* 204 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 205 */       cp >>>= 2;
/* 206 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 207 */       return dest;
/* 208 */     }  if (cp <= 1114111) {
/* 209 */       char[] dest = new char[12];
/*     */ 
/*     */       
/* 212 */       dest[0] = '%';
/* 213 */       dest[1] = 'F';
/* 214 */       dest[3] = '%';
/* 215 */       dest[6] = '%';
/* 216 */       dest[9] = '%';
/* 217 */       dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
/* 218 */       cp >>>= 4;
/* 219 */       dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 220 */       cp >>>= 2;
/* 221 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 222 */       cp >>>= 4;
/* 223 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 224 */       cp >>>= 2;
/* 225 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 226 */       cp >>>= 4;
/* 227 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 228 */       cp >>>= 2;
/* 229 */       dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
/* 230 */       return dest;
/*     */     } 
/*     */     
/* 233 */     throw new IllegalArgumentException("Invalid unicode character value " + cp);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\PercentEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */