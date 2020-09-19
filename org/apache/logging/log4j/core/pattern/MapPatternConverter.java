/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.message.MapMessage;
/*    */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
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
/*    */ @Plugin(name = "MapPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"K", "map", "MAP"})
/*    */ public final class MapPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final String key;
/*    */   
/*    */   private MapPatternConverter(String[] options) {
/* 44 */     super((options != null && options.length > 0) ? ("MAP{" + options[0] + '}') : "MAP", "map");
/* 45 */     this.key = (options != null && options.length > 0) ? options[0] : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MapPatternConverter newInstance(String[] options) {
/* 55 */     return new MapPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*    */     MapMessage msg;
/* 64 */     if (event.getMessage() instanceof MapMessage) {
/* 65 */       msg = (MapMessage)event.getMessage();
/*    */     } else {
/*    */       return;
/*    */     } 
/* 69 */     IndexedReadOnlyStringMap sortedMap = msg.getIndexedReadOnlyStringMap();
/*    */ 
/*    */     
/* 72 */     if (this.key == null) {
/* 73 */       if (sortedMap.isEmpty()) {
/* 74 */         toAppendTo.append("{}");
/*    */         return;
/*    */       } 
/* 77 */       toAppendTo.append("{");
/* 78 */       for (int i = 0; i < sortedMap.size(); i++) {
/* 79 */         if (i > 0) {
/* 80 */           toAppendTo.append(", ");
/*    */         }
/* 82 */         toAppendTo.append(sortedMap.getKeyAt(i)).append('=').append(sortedMap.getValueAt(i));
/*    */       } 
/* 84 */       toAppendTo.append('}');
/*    */     } else {
/*    */       
/* 87 */       String val = (String)sortedMap.getValue(this.key);
/*    */       
/* 89 */       if (val != null)
/* 90 */         toAppendTo.append(val); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\MapPatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */