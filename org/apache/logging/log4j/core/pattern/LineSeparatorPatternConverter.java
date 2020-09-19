/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*    */ import org.apache.logging.log4j.util.Strings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "LineSeparatorPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"n"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class LineSeparatorPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 35 */   private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final String lineSep;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LineSeparatorPatternConverter() {
/* 46 */     super("Line Sep", "lineSep");
/* 47 */     this.lineSep = Strings.LINE_SEPARATOR;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LineSeparatorPatternConverter newInstance(String[] options) {
/* 58 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 66 */     toAppendTo.append(this.lineSep);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\LineSeparatorPatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */