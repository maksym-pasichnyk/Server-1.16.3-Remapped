/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaUnicodeEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*     */   public static JavaUnicodeEscaper above(int codepoint) {
/*  36 */     return outsideOf(0, codepoint);
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
/*     */   public static JavaUnicodeEscaper below(int codepoint) {
/*  49 */     return outsideOf(codepoint, 2147483647);
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
/*     */   public static JavaUnicodeEscaper between(int codepointLow, int codepointHigh) {
/*  64 */     return new JavaUnicodeEscaper(codepointLow, codepointHigh, true);
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
/*     */   public static JavaUnicodeEscaper outsideOf(int codepointLow, int codepointHigh) {
/*  79 */     return new JavaUnicodeEscaper(codepointLow, codepointHigh, false);
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
/*     */   public JavaUnicodeEscaper(int below, int above, boolean between) {
/*  97 */     super(below, above, between);
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
/*     */   protected String toUtf16Escape(int codepoint) {
/* 109 */     char[] surrogatePair = Character.toChars(codepoint);
/* 110 */     return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\JavaUnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */