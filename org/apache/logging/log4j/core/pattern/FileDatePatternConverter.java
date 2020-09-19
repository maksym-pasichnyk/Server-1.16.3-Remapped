/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*    */ @Plugin(name = "FileDatePatternConverter", category = "FileConverter")
/*    */ @ConverterKeys({"d", "date"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class FileDatePatternConverter
/*    */ {
/*    */   public static PatternConverter newInstance(String[] options) {
/* 44 */     if (options == null || options.length == 0) {
/* 45 */       return DatePatternConverter.newInstance(new String[] { "yyyy-MM-dd" });
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     return DatePatternConverter.newInstance(options);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\FileDatePatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */