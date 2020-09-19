/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
/*    */ public class Log4jXmlObjectMapper
/*    */   extends XmlMapper
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jXmlObjectMapper() {
/* 37 */     this(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jXmlObjectMapper(boolean includeStacktrace) {
/* 44 */     super(new Log4jXmlModule(includeStacktrace));
/* 45 */     setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\Log4jXmlObjectMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */