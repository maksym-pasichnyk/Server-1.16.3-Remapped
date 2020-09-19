/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.logging.log4j.core.Appender;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "appenders", category = "Core")
/*    */ public final class AppendersPlugin
/*    */ {
/*    */   @PluginFactory
/*    */   public static ConcurrentMap<String, Appender> createAppenders(@PluginElement("Appenders") Appender[] appenders) {
/* 46 */     ConcurrentMap<String, Appender> map = new ConcurrentHashMap<>(appenders.length);
/*    */     
/* 48 */     for (Appender appender : appenders) {
/* 49 */       map.put(appender.getName(), appender);
/*    */     }
/*    */     
/* 52 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\AppendersPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */