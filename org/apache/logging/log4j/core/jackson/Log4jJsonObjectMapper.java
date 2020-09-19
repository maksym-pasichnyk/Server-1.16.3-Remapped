/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
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
/*    */ public class Log4jJsonObjectMapper
/*    */   extends ObjectMapper
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jJsonObjectMapper() {
/* 36 */     this(false, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jJsonObjectMapper(boolean encodeThreadContextAsList, boolean includeStacktrace) {
/* 43 */     registerModule((Module)new Log4jJsonModule(encodeThreadContextAsList, includeStacktrace));
/* 44 */     setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\Log4jJsonObjectMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */