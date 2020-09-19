/*    */ package org.apache.logging.log4j.core.appender.db.jpa.converter;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import javax.persistence.AttributeConverter;
/*    */ import javax.persistence.Converter;
/*    */ import javax.persistence.PersistenceException;
/*    */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*    */ import org.apache.logging.log4j.util.BiConsumer;
/*    */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*    */ import org.apache.logging.log4j.util.StringMap;
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
/*    */ @Converter(autoApply = false)
/*    */ public class ContextDataJsonAttributeConverter
/*    */   implements AttributeConverter<ReadOnlyStringMap, String>
/*    */ {
/* 47 */   static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
/*    */ 
/*    */   
/*    */   public String convertToDatabaseColumn(ReadOnlyStringMap contextData) {
/* 51 */     if (contextData == null) {
/* 52 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 56 */       JsonNodeFactory factory = OBJECT_MAPPER.getNodeFactory();
/* 57 */       final ObjectNode root = factory.objectNode();
/* 58 */       contextData.forEach(new BiConsumer<String, Object>()
/*    */           {
/*    */             public void accept(String key, Object value)
/*    */             {
/* 62 */               root.put(key, String.valueOf(value));
/*    */             }
/*    */           });
/* 65 */       return OBJECT_MAPPER.writeValueAsString(root);
/* 66 */     } catch (Exception e) {
/* 67 */       throw new PersistenceException("Failed to convert contextData to JSON string.", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ReadOnlyStringMap convertToEntityAttribute(String s) {
/* 73 */     if (Strings.isEmpty(s)) {
/* 74 */       return null;
/*    */     }
/*    */     try {
/* 77 */       StringMap result = ContextDataFactory.createContextData();
/* 78 */       ObjectNode root = (ObjectNode)OBJECT_MAPPER.readTree(s);
/* 79 */       Iterator<Map.Entry<String, JsonNode>> entries = root.fields();
/* 80 */       while (entries.hasNext()) {
/* 81 */         Map.Entry<String, JsonNode> entry = entries.next();
/*    */ 
/*    */ 
/*    */         
/* 85 */         Object value = ((JsonNode)entry.getValue()).textValue();
/* 86 */         result.putValue(entry.getKey(), value);
/*    */       } 
/* 88 */       return (ReadOnlyStringMap)result;
/* 89 */     } catch (IOException e) {
/* 90 */       throw new PersistenceException("Failed to convert JSON string to map.", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jpa\converter\ContextDataJsonAttributeConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */