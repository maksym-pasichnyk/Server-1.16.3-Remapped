/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSequenceTranslator
/*     */ {
/*  33 */   static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int translate(CharSequence paramCharSequence, int paramInt, Writer paramWriter) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String translate(CharSequence input) {
/*  55 */     if (input == null) {
/*  56 */       return null;
/*     */     }
/*     */     try {
/*  59 */       StringWriter writer = new StringWriter(input.length() * 2);
/*  60 */       translate(input, writer);
/*  61 */       return writer.toString();
/*  62 */     } catch (IOException ioe) {
/*     */       
/*  64 */       throw new RuntimeException(ioe);
/*     */     } 
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
/*     */   public final void translate(CharSequence input, Writer out) throws IOException {
/*  77 */     if (out == null) {
/*  78 */       throw new IllegalArgumentException("The Writer must not be null");
/*     */     }
/*  80 */     if (input == null) {
/*     */       return;
/*     */     }
/*  83 */     int pos = 0;
/*  84 */     int len = input.length();
/*  85 */     while (pos < len) {
/*  86 */       int consumed = translate(input, pos, out);
/*  87 */       if (consumed == 0) {
/*     */ 
/*     */         
/*  90 */         char c1 = input.charAt(pos);
/*  91 */         out.write(c1);
/*  92 */         pos++;
/*  93 */         if (Character.isHighSurrogate(c1) && pos < len) {
/*  94 */           char c2 = input.charAt(pos);
/*  95 */           if (Character.isLowSurrogate(c2)) {
/*  96 */             out.write(c2);
/*  97 */             pos++;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 104 */       for (int pt = 0; pt < consumed; pt++) {
/* 105 */         pos += Character.charCount(Character.codePointAt(input, pos));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CharSequenceTranslator with(CharSequenceTranslator... translators) {
/* 118 */     CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
/* 119 */     newArray[0] = this;
/* 120 */     System.arraycopy(translators, 0, newArray, 1, translators.length);
/* 121 */     return new AggregateTranslator(newArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hex(int codepoint) {
/* 132 */     return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\CharSequenceTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */