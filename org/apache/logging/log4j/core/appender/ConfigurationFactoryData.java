/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
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
/*    */ public class ConfigurationFactoryData
/*    */ {
/*    */   public final Configuration configuration;
/*    */   
/*    */   public ConfigurationFactoryData(Configuration configuration) {
/* 34 */     this.configuration = configuration;
/*    */   }
/*    */   
/*    */   public Configuration getConfiguration() {
/* 38 */     return this.configuration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getLoggerContext() {
/* 47 */     return (this.configuration != null) ? this.configuration.getLoggerContext() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\ConfigurationFactoryData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */