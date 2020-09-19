/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
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
/*    */ public class PluginConfigurationVisitor
/*    */   extends AbstractPluginVisitor<PluginConfiguration>
/*    */ {
/*    */   public PluginConfigurationVisitor() {
/* 30 */     super(PluginConfiguration.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 36 */     if (this.conversionType.isInstance(configuration)) {
/* 37 */       log.append("Configuration");
/* 38 */       if (configuration.getName() != null) {
/* 39 */         log.append('(').append(configuration.getName()).append(')');
/*    */       }
/* 41 */       return configuration;
/*    */     } 
/* 43 */     LOGGER.warn("Variable annotated with @PluginConfiguration is not compatible with type {}.", configuration.getClass());
/*    */     
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginConfigurationVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */