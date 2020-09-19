/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.text.FieldPosition;
/*     */ import java.text.Format;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeFormat
/*     */   extends Format
/*     */ {
/*     */   private static final long serialVersionUID = -4329119827877627683L;
/*     */   private final Format parser;
/*     */   private final Format formatter;
/*     */   
/*     */   public CompositeFormat(Format parser, Format formatter) {
/*  51 */     this.parser = parser;
/*  52 */     this.formatter = formatter;
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
/*     */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/*  67 */     return this.formatter.format(obj, toAppendTo, pos);
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
/*     */   public Object parseObject(String source, ParsePosition pos) {
/*  82 */     return this.parser.parseObject(source, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Format getParser() {
/*  91 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Format getFormatter() {
/* 100 */     return this.formatter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String reformat(String input) throws ParseException {
/* 111 */     return format(parseObject(input));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\CompositeFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */