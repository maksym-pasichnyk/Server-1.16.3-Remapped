/*     */ package org.apache.commons.lang3.text.translate;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumericEntityUnescaper
/*     */   extends CharSequenceTranslator
/*     */ {
/*     */   private final EnumSet<OPTION> options;
/*     */   
/*     */   public enum OPTION
/*     */   {
/*  34 */     semiColonRequired, semiColonOptional, errorIfNoSemiColon;
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
/*     */   public NumericEntityUnescaper(OPTION... options) {
/*  56 */     if (options.length > 0) {
/*  57 */       this.options = EnumSet.copyOf(Arrays.asList(options));
/*     */     } else {
/*  59 */       this.options = EnumSet.copyOf(Arrays.asList(new OPTION[] { OPTION.semiColonRequired }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSet(OPTION option) {
/*  70 */     return (this.options == null) ? false : this.options.contains(option);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/*  78 */     int seqEnd = input.length();
/*     */     
/*  80 */     if (input.charAt(index) == '&' && index < seqEnd - 2 && input.charAt(index + 1) == '#') {
/*  81 */       int entityValue, start = index + 2;
/*  82 */       boolean isHex = false;
/*     */       
/*  84 */       char firstChar = input.charAt(start);
/*  85 */       if (firstChar == 'x' || firstChar == 'X') {
/*  86 */         start++;
/*  87 */         isHex = true;
/*     */ 
/*     */         
/*  90 */         if (start == seqEnd) {
/*  91 */           return 0;
/*     */         }
/*     */       } 
/*     */       
/*  95 */       int end = start;
/*     */       
/*  97 */       while (end < seqEnd && ((input.charAt(end) >= '0' && input.charAt(end) <= '9') || (input
/*  98 */         .charAt(end) >= 'a' && input.charAt(end) <= 'f') || (input
/*  99 */         .charAt(end) >= 'A' && input.charAt(end) <= 'F')))
/*     */       {
/* 101 */         end++;
/*     */       }
/*     */       
/* 104 */       boolean semiNext = (end != seqEnd && input.charAt(end) == ';');
/*     */       
/* 106 */       if (!semiNext) {
/* 107 */         if (isSet(OPTION.semiColonRequired)) {
/* 108 */           return 0;
/*     */         }
/* 110 */         if (isSet(OPTION.errorIfNoSemiColon)) {
/* 111 */           throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 117 */         if (isHex) {
/* 118 */           entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
/*     */         } else {
/* 120 */           entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
/*     */         } 
/* 122 */       } catch (NumberFormatException nfe) {
/* 123 */         return 0;
/*     */       } 
/*     */       
/* 126 */       if (entityValue > 65535) {
/* 127 */         char[] chrs = Character.toChars(entityValue);
/* 128 */         out.write(chrs[0]);
/* 129 */         out.write(chrs[1]);
/*     */       } else {
/* 131 */         out.write(entityValue);
/*     */       } 
/*     */       
/* 134 */       return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
/*     */     } 
/* 136 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\NumericEntityUnescaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */