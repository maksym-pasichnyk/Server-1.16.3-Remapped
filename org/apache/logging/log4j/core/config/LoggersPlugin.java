/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "loggers", category = "Core")
/*    */ public final class LoggersPlugin
/*    */ {
/*    */   @PluginFactory
/*    */   public static Loggers createLoggers(@PluginElement("Loggers") LoggerConfig[] loggers) {
/* 42 */     ConcurrentMap<String, LoggerConfig> loggerMap = new ConcurrentHashMap<>();
/* 43 */     LoggerConfig root = null;
/*    */     
/* 45 */     for (LoggerConfig logger : loggers) {
/* 46 */       if (logger != null) {
/* 47 */         if (logger.getName().isEmpty()) {
/* 48 */           root = logger;
/*    */         }
/* 50 */         loggerMap.put(logger.getName(), logger);
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     return new Loggers(loggerMap, root);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\LoggersPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */