/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
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
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public abstract class NamePatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final NameAbbreviator abbreviator;
/*    */   
/*    */   protected NamePatternConverter(String name, String style, String[] options) {
/* 40 */     super(name, style);
/*    */     
/* 42 */     if (options != null && options.length > 0) {
/* 43 */       this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
/*    */     } else {
/* 45 */       this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void abbreviate(String original, StringBuilder destination) {
/* 57 */     this.abbreviator.abbreviate(original, destination);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\NamePatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */