/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ public class PatternFormatter
/*    */ {
/*    */   private final LogEventPatternConverter converter;
/*    */   private final FormattingInfo field;
/*    */   private final boolean skipFormattingInfo;
/*    */   
/*    */   public PatternFormatter(LogEventPatternConverter converter, FormattingInfo field) {
/* 31 */     this.converter = converter;
/* 32 */     this.field = field;
/* 33 */     this.skipFormattingInfo = (field == FormattingInfo.getDefault());
/*    */   }
/*    */   
/*    */   public void format(LogEvent event, StringBuilder buf) {
/* 37 */     if (this.skipFormattingInfo) {
/* 38 */       this.converter.format(event, buf);
/*    */     } else {
/* 40 */       formatWithInfo(event, buf);
/*    */     } 
/*    */   }
/*    */   private void formatWithInfo(LogEvent event, StringBuilder buf) {
/* 44 */     int startField = buf.length();
/* 45 */     this.converter.format(event, buf);
/* 46 */     this.field.format(startField, buf);
/*    */   }
/*    */   
/*    */   public LogEventPatternConverter getConverter() {
/* 50 */     return this.converter;
/*    */   }
/*    */   
/*    */   public FormattingInfo getFormattingInfo() {
/* 54 */     return this.field;
/*    */   }
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
/*    */   public boolean handlesThrowable() {
/* 67 */     return this.converter.handlesThrowable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     StringBuilder sb = new StringBuilder();
/* 78 */     sb.append(super.toString());
/* 79 */     sb.append("[converter=");
/* 80 */     sb.append(this.converter);
/* 81 */     sb.append(", field=");
/* 82 */     sb.append(this.field);
/* 83 */     sb.append(']');
/* 84 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\PatternFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */