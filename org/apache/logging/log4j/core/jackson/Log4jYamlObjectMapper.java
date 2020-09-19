/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
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
/*    */ public class Log4jYamlObjectMapper
/*    */   extends YAMLMapper
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jYamlObjectMapper() {
/* 37 */     this(false, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jYamlObjectMapper(boolean encodeThreadContextAsList, boolean includeStacktrace) {
/* 44 */     registerModule((Module)new Log4jYamlModule(encodeThreadContextAsList, includeStacktrace));
/* 45 */     setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\Log4jYamlObjectMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */