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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumericEntityEscaper
/*     */   extends CodePointTranslator
/*     */ {
/*     */   private final int below;
/*     */   private final int above;
/*     */   private final boolean between;
/*     */   
/*     */   private NumericEntityEscaper(int below, int above, boolean between) {
/*  44 */     this.below = below;
/*  45 */     this.above = above;
/*  46 */     this.between = between;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NumericEntityEscaper() {
/*  53 */     this(0, 2147483647, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper below(int codepoint) {
/*  63 */     return outsideOf(codepoint, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper above(int codepoint) {
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
/*     */   public static NumericEntityEscaper between(int codepointLow, int codepointHigh) {
/*  84 */     return new NumericEntityEscaper(codepointLow, codepointHigh, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NumericEntityEscaper outsideOf(int codepointLow, int codepointHigh) {
/*  95 */     return new NumericEntityEscaper(codepointLow, codepointHigh, false);
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
/* 113 */     out.write("&#");
/* 114 */     out.write(Integer.toString(codepoint, 10));
/* 115 */     out.write(59);
/* 116 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\NumericEntityEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */