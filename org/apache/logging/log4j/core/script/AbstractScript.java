/*    */ package org.apache.logging.log4j.core.script;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public abstract class AbstractScript
/*    */ {
/* 27 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   protected static final String DEFAULT_LANGUAGE = "JavaScript";
/*    */   private final String language;
/*    */   private final String scriptText;
/*    */   private final String name;
/*    */   
/*    */   public AbstractScript(String name, String language, String scriptText) {
/* 35 */     this.language = language;
/* 36 */     this.scriptText = scriptText;
/* 37 */     this.name = (name == null) ? toString() : name;
/*    */   }
/*    */   
/*    */   public String getLanguage() {
/* 41 */     return this.language;
/*    */   }
/*    */   
/*    */   public String getScriptText() {
/* 45 */     return this.scriptText;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 49 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\script\AbstractScript.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */