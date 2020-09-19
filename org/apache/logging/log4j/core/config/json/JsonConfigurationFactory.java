/*    */ package org.apache.logging.log4j.core.config.json;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.util.Loader;
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
/*    */ @Plugin(name = "JsonConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(6)
/*    */ public class JsonConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 37 */   private static final String[] SUFFIXES = new String[] { ".json", ".jsn" };
/*    */   
/* 39 */   private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isActive;
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonConfigurationFactory() {
/* 48 */     for (String dependency : dependencies) {
/* 49 */       if (!Loader.isClassAvailable(dependency)) {
/* 50 */         LOGGER.debug("Missing dependencies for Json support");
/* 51 */         this.isActive = false;
/*    */         return;
/*    */       } 
/*    */     } 
/* 55 */     this.isActive = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isActive() {
/* 60 */     return this.isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 65 */     if (!this.isActive) {
/* 66 */       return null;
/*    */     }
/* 68 */     return (Configuration)new JsonConfiguration(loggerContext, source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 73 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\json\JsonConfigurationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */