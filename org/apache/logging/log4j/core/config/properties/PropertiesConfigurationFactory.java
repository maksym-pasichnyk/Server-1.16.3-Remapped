/*    */ package org.apache.logging.log4j.core.config.properties;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "PropertiesConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(8)
/*    */ public class PropertiesConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/*    */   protected String[] getSupportedTypes() {
/* 41 */     return new String[] { ".properties" };
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertiesConfiguration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 46 */     Properties properties = new Properties();
/* 47 */     try (InputStream configStream = source.getInputStream()) {
/* 48 */       properties.load(configStream);
/* 49 */     } catch (IOException ioe) {
/* 50 */       throw new ConfigurationException("Unable to load " + source.toString(), ioe);
/*    */     } 
/* 52 */     return (new PropertiesConfigurationBuilder()).setConfigurationSource(source).setRootProperties(properties).setLoggerContext(loggerContext).build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfigurationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */