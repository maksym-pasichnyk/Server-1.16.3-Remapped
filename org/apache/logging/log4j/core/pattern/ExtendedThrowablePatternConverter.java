/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "ExtendedThrowablePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"xEx", "xThrowable", "xException"})
/*    */ public final class ExtendedThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private ExtendedThrowablePatternConverter(String[] options) {
/* 42 */     super("ExtendedThrowable", "throwable", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ExtendedThrowablePatternConverter newInstance(String[] options) {
/* 53 */     return new ExtendedThrowablePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 61 */     ThrowableProxy proxy = event.getThrownProxy();
/* 62 */     Throwable throwable = event.getThrown();
/* 63 */     if ((throwable != null || proxy != null) && this.options.anyLines()) {
/* 64 */       if (proxy == null) {
/* 65 */         super.format(event, toAppendTo);
/*    */         return;
/*    */       } 
/* 68 */       String extStackTrace = proxy.getExtendedStackTraceAsString(this.options.getIgnorePackages(), this.options.getTextRenderer());
/* 69 */       int len = toAppendTo.length();
/* 70 */       if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
/* 71 */         toAppendTo.append(' ');
/*    */       }
/* 73 */       if (!this.options.allLines() || !Strings.LINE_SEPARATOR.equals(this.options.getSeparator())) {
/* 74 */         StringBuilder sb = new StringBuilder();
/* 75 */         String[] array = extStackTrace.split(Strings.LINE_SEPARATOR);
/* 76 */         int limit = this.options.minLines(array.length) - 1;
/* 77 */         for (int i = 0; i <= limit; i++) {
/* 78 */           sb.append(array[i]);
/* 79 */           if (i < limit) {
/* 80 */             sb.append(this.options.getSeparator());
/*    */           }
/*    */         } 
/* 83 */         toAppendTo.append(sb.toString());
/*    */       } else {
/* 85 */         toAppendTo.append(extStackTrace);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\ExtendedThrowablePatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */