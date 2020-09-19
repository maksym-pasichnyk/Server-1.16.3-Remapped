/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
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
/*    */ @Plugin(name = "Null", category = "Core", elementType = "appender", printObject = true)
/*    */ public class NullAppender
/*    */   extends AbstractAppender
/*    */ {
/*    */   public static final String PLUGIN_NAME = "Null";
/*    */   
/*    */   @PluginFactory
/*    */   public static NullAppender createAppender(@PluginAttribute("name") String name) {
/* 36 */     return new NullAppender(name);
/*    */   }
/*    */   
/*    */   private NullAppender(String name) {
/* 40 */     super(name, null, null);
/*    */   }
/*    */   
/*    */   public void append(LogEvent event) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\NullAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */