/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.message.SimpleMessage;
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
/*    */ public final class SimpleMessageDeserializer
/*    */   extends StdScalarDeserializer<SimpleMessage>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   SimpleMessageDeserializer() {
/* 38 */     super(SimpleMessage.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleMessage deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 44 */     return new SimpleMessage(jp.getValueAsString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\SimpleMessageDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */