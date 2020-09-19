/*    */ package org.apache.logging.log4j.core.script;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
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
/*    */ @Plugin(name = "Script", category = "Core", printObject = true)
/*    */ public class Script
/*    */   extends AbstractScript
/*    */ {
/*    */   public Script(String name, String language, String scriptText) {
/* 32 */     super(name, language, scriptText);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static Script createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginValue("scriptText") String scriptText) {
/* 42 */     if (language == null) {
/* 43 */       LOGGER.info("No script language supplied, defaulting to {}", "JavaScript");
/* 44 */       language = "JavaScript";
/*    */     } 
/* 46 */     if (scriptText == null) {
/* 47 */       LOGGER.error("No scriptText attribute provided for ScriptFile {}", name);
/* 48 */       return null;
/*    */     } 
/* 50 */     return new Script(name, language, scriptText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return (getName() != null) ? getName() : super.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\script\Script.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */