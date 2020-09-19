/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "encode", category = "Converter")
/*     */ @ConverterKeys({"enc", "encode"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class EncodingPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   
/*     */   private EncodingPatternConverter(List<PatternFormatter> formatters) {
/*  43 */     super("encode", "encode");
/*  44 */     this.formatters = formatters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EncodingPatternConverter newInstance(Configuration config, String[] options) {
/*  55 */     if (options.length != 1) {
/*  56 */       LOGGER.error("Incorrect number of options on escape. Expected 1, received " + options.length);
/*  57 */       return null;
/*     */     } 
/*  59 */     if (options[0] == null) {
/*  60 */       LOGGER.error("No pattern supplied on escape");
/*  61 */       return null;
/*     */     } 
/*  63 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  64 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  65 */     return new EncodingPatternConverter(formatters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  73 */     int start = toAppendTo.length(); int i;
/*  74 */     for (i = 0; i < this.formatters.size(); i++) {
/*  75 */       ((PatternFormatter)this.formatters.get(i)).format(event, toAppendTo);
/*     */     }
/*  77 */     for (i = toAppendTo.length() - 1; i >= start; i--) {
/*  78 */       char c = toAppendTo.charAt(i);
/*  79 */       switch (c) {
/*     */         case '\r':
/*  81 */           toAppendTo.setCharAt(i, '\\');
/*  82 */           toAppendTo.insert(i + 1, 'r');
/*     */           break;
/*     */         case '\n':
/*  85 */           toAppendTo.setCharAt(i, '\\');
/*  86 */           toAppendTo.insert(i + 1, 'n');
/*     */           break;
/*     */         case '&':
/*  89 */           toAppendTo.setCharAt(i, '&');
/*  90 */           toAppendTo.insert(i + 1, "amp;");
/*     */           break;
/*     */         case '<':
/*  93 */           toAppendTo.setCharAt(i, '&');
/*  94 */           toAppendTo.insert(i + 1, "lt;");
/*     */           break;
/*     */         case '>':
/*  97 */           toAppendTo.setCharAt(i, '&');
/*  98 */           toAppendTo.insert(i + 1, "gt;");
/*     */           break;
/*     */         case '"':
/* 101 */           toAppendTo.setCharAt(i, '&');
/* 102 */           toAppendTo.insert(i + 1, "quot;");
/*     */           break;
/*     */         case '\'':
/* 105 */           toAppendTo.setCharAt(i, '&');
/* 106 */           toAppendTo.insert(i + 1, "apos;");
/*     */           break;
/*     */         case '/':
/* 109 */           toAppendTo.setCharAt(i, '&');
/* 110 */           toAppendTo.insert(i + 1, "#x2F;");
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\EncodingPatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */