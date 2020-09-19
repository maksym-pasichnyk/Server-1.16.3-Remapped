/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnicodeEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final int below;
/*     */   private final int above;
/*     */   private final boolean between;
/*     */   
/*     */   public UnicodeEscaper() {
/*  37 */     this(0, 2147483647, true);
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
/*     */   protected UnicodeEscaper(int below, int above, boolean between) {
/*  51 */     this.below = below;
/*  52 */     this.above = above;
/*  53 */     this.between = between;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper below(int codepoint) {
/*  63 */     return outsideOf(codepoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper above(int codepoint) {
/*  73 */     return outsideOf(0, codepoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper outsideOf(int codepointLow, int codepointHigh) {
/*  84 */     return new UnicodeEscaper(codepointLow, codepointHigh, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnicodeEscaper between(int codepointLow, int codepointHigh) {
/*  95 */     return new UnicodeEscaper(codepointLow, codepointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean translate(int codepoint, Writer out) throws IOException {
/* 103 */     if (this.between) {
/* 104 */       if (codepoint < this.below || codepoint > this.above) {
/* 105 */         return false;
/*     */       }
/*     */     }
/* 108 */     else if (codepoint >= this.below && codepoint <= this.above) {
/* 109 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 114 */     if (codepoint > 65535) {
/* 115 */       out.write(toUtf16Escape(codepoint));
/*     */     } else {
/* 117 */       out.write("\\u");
/* 118 */       out.write(HEX_DIGITS[codepoint >> 12 & 0xF]);
/* 119 */       out.write(HEX_DIGITS[codepoint >> 8 & 0xF]);
/* 120 */       out.write(HEX_DIGITS[codepoint >> 4 & 0xF]);
/* 121 */       out.write(HEX_DIGITS[codepoint & 0xF]);
/*     */     } 
/* 123 */     return true;
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
/*     */   protected String toUtf16Escape(int codepoint) {
/* 136 */     return "\\u" + hex(codepoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\UnicodeEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */