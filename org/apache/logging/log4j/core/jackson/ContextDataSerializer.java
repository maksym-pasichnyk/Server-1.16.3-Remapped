/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*    */ import org.apache.logging.log4j.util.TriConsumer;
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
/*    */ public class ContextDataSerializer
/*    */   extends StdSerializer<ReadOnlyStringMap>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected ContextDataSerializer() {
/* 40 */     super(Map.class, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(ReadOnlyStringMap contextData, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 47 */     jgen.writeStartObject();
/* 48 */     contextData.forEach(WRITE_STRING_FIELD_INTO, jgen);
/* 49 */     jgen.writeEndObject();
/*    */   }
/*    */   
/* 52 */   private static final TriConsumer<String, Object, JsonGenerator> WRITE_STRING_FIELD_INTO = new TriConsumer<String, Object, JsonGenerator>()
/*    */     {
/*    */       
/*    */       public void accept(String key, Object value, JsonGenerator jsonGenerator)
/*    */       {
/*    */         try {
/* 58 */           jsonGenerator.writeStringField(key, String.valueOf(value));
/* 59 */         } catch (Exception ex) {
/* 60 */           throw new IllegalStateException("Problem with key " + key, ex);
/*    */         } 
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\ContextDataSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */