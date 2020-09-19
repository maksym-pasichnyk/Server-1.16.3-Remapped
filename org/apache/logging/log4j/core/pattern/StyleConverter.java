/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "style", category = "Converter")
/*     */ @ConverterKeys({"style"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class StyleConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */   private final boolean noAnsi;
/*     */   private final String style;
/*     */   private final String defaultStyle;
/*     */   
/*     */   private StyleConverter(List<PatternFormatter> patternFormatters, String style, boolean noAnsi) {
/*  61 */     super("style", "style");
/*  62 */     this.patternFormatters = patternFormatters;
/*  63 */     this.style = style;
/*  64 */     this.defaultStyle = AnsiEscape.getDefaultStyle();
/*  65 */     this.noAnsi = noAnsi;
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
/*     */   public static StyleConverter newInstance(Configuration config, String[] options) {
/*  79 */     if (options.length < 1) {
/*  80 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/*  81 */       return null;
/*     */     } 
/*  83 */     if (options[0] == null) {
/*  84 */       LOGGER.error("No pattern supplied on style");
/*  85 */       return null;
/*     */     } 
/*  87 */     if (options[1] == null) {
/*  88 */       LOGGER.error("No style attributes provided");
/*  89 */       return null;
/*     */     } 
/*  91 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  92 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  93 */     String style = AnsiEscape.createSequence(options[1].split(Patterns.COMMA_SEPARATOR));
/*  94 */     boolean disableAnsi = Arrays.toString((Object[])options).contains("disableAnsi=true");
/*  95 */     boolean noConsoleNoAnsi = Arrays.toString((Object[])options).contains("noConsoleNoAnsi=true");
/*  96 */     boolean hideAnsi = (disableAnsi || (noConsoleNoAnsi && System.console() == null));
/*  97 */     return new StyleConverter(formatters, style, hideAnsi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 105 */     int start = 0;
/* 106 */     int end = 0;
/* 107 */     if (!this.noAnsi) {
/* 108 */       start = toAppendTo.length();
/* 109 */       toAppendTo.append(this.style);
/* 110 */       end = toAppendTo.length();
/*     */     } 
/*     */ 
/*     */     
/* 114 */     for (int i = 0, size = this.patternFormatters.size(); i < size; i++) {
/* 115 */       ((PatternFormatter)this.patternFormatters.get(i)).format(event, toAppendTo);
/*     */     }
/*     */ 
/*     */     
/* 119 */     if (!this.noAnsi) {
/* 120 */       if (toAppendTo.length() == end) {
/* 121 */         toAppendTo.setLength(start);
/*     */       } else {
/* 123 */         toAppendTo.append(this.defaultStyle);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 130 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 131 */       if (formatter.handlesThrowable()) {
/* 132 */         return true;
/*     */       }
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     StringBuilder sb = new StringBuilder();
/* 146 */     sb.append(super.toString());
/* 147 */     sb.append("[style=");
/* 148 */     sb.append(this.style);
/* 149 */     sb.append(", defaultStyle=");
/* 150 */     sb.append(this.defaultStyle);
/* 151 */     sb.append(", patternFormatters=");
/* 152 */     sb.append(this.patternFormatters);
/* 153 */     sb.append(", noAnsi=");
/* 154 */     sb.append(this.noAnsi);
/* 155 */     sb.append(']');
/* 156 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\StyleConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */