/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*    */ import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
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
/*    */ final class Log4jXmlModule
/*    */   extends JacksonXmlModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final boolean includeStacktrace;
/*    */   
/*    */   Log4jXmlModule(boolean includeStacktrace) {
/* 36 */     this.includeStacktrace = includeStacktrace;
/*    */ 
/*    */     
/* 39 */     (new Initializers.SimpleModuleInitializer()).initialize((SimpleModule)this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 45 */     super.setupModule(context);
/* 46 */     (new Initializers.SetupContextInitializer()).setupModule(context, this.includeStacktrace);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\Log4jXmlModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */