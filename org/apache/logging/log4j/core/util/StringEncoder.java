/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringEncoder
/*     */ {
/*     */   public static byte[] toBytes(String str, Charset charset) {
/*  41 */     if (str != null) {
/*  42 */       if (StandardCharsets.ISO_8859_1.equals(charset)) {
/*  43 */         return encodeSingleByteChars(str);
/*     */       }
/*  45 */       Charset actual = (charset != null) ? charset : Charset.defaultCharset();
/*     */       try {
/*  47 */         return str.getBytes(actual.name());
/*  48 */       } catch (UnsupportedEncodingException e) {
/*  49 */         return str.getBytes(actual);
/*     */       } 
/*     */     } 
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeSingleByteChars(CharSequence s) {
/*  63 */     int length = s.length();
/*  64 */     byte[] result = new byte[length];
/*  65 */     encodeString(s, 0, length, result);
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int encodeIsoChars(CharSequence charArray, int charIndex, byte[] byteArray, int byteIndex, int length) {
/*  75 */     int i = 0;
/*  76 */     for (; i < length; i++) {
/*  77 */       char c = charArray.charAt(charIndex++);
/*  78 */       if (c > 'ÿ') {
/*     */         break;
/*     */       }
/*  81 */       byteArray[byteIndex++] = (byte)c;
/*     */     } 
/*  83 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int encodeString(CharSequence charArray, int charOffset, int charLength, byte[] byteArray) {
/*  88 */     int byteOffset = 0;
/*  89 */     int length = Math.min(charLength, byteArray.length);
/*  90 */     int charDoneIndex = charOffset + length;
/*  91 */     while (charOffset < charDoneIndex) {
/*  92 */       int done = encodeIsoChars(charArray, charOffset, byteArray, byteOffset, length);
/*  93 */       charOffset += done;
/*  94 */       byteOffset += done;
/*  95 */       if (done != length) {
/*  96 */         char c = charArray.charAt(charOffset++);
/*  97 */         if (Character.isHighSurrogate(c) && charOffset < charDoneIndex && Character.isLowSurrogate(charArray.charAt(charOffset))) {
/*     */           
/*  99 */           if (charLength > byteArray.length) {
/* 100 */             charDoneIndex++;
/* 101 */             charLength--;
/*     */           } 
/* 103 */           charOffset++;
/*     */         } 
/* 105 */         byteArray[byteOffset++] = 63;
/* 106 */         length = Math.min(charDoneIndex - charOffset, byteArray.length - byteOffset);
/*     */       } 
/*     */     } 
/* 109 */     return byteOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\StringEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */