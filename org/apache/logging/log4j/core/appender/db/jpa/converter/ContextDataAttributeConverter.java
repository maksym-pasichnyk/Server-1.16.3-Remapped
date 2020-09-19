/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*    */ @Converter(autoApply = false)
/*    */ public class ContextDataAttributeConverter
/*    */   implements AttributeConverter<ReadOnlyStringMap, String>
/*    */ {
/*    */   public String convertToDatabaseColumn(ReadOnlyStringMap contextData) {
/* 35 */     if (contextData == null) {
/* 36 */       return null;
/*    */     }
/*    */     
/* 39 */     return contextData.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public ReadOnlyStringMap convertToEntityAttribute(String s) {
/* 44 */     throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\ContextDataAttributeConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */