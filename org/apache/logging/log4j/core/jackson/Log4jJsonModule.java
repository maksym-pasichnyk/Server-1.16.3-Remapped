/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
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
/*    */ class Log4jJsonModule
/*    */   extends SimpleModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final boolean encodeThreadContextAsList;
/*    */   private final boolean includeStacktrace;
/*    */   
/*    */   Log4jJsonModule(boolean encodeThreadContextAsList, boolean includeStacktrace) {
/* 38 */     super(Log4jJsonModule.class.getName(), new Version(2, 0, 0, null, null, null));
/* 39 */     this.encodeThreadContextAsList = encodeThreadContextAsList;
/* 40 */     this.includeStacktrace = includeStacktrace;
/*    */ 
/*    */ 
/*    */     
/* 44 */     (new Initializers.SimpleModuleInitializer()).initialize(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 50 */     super.setupModule(context);
/* 51 */     if (this.encodeThreadContextAsList) {
/* 52 */       (new Initializers.SetupContextInitializer()).setupModule(context, this.includeStacktrace);
/*    */     } else {
/* 54 */       (new Initializers.SetupContextJsonInitializer()).setupModule(context, this.includeStacktrace);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jackson\Log4jJsonModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */