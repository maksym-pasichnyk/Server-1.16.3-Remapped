/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginNode;
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
/*    */ public class PluginNodeVisitor
/*    */   extends AbstractPluginVisitor<PluginNode>
/*    */ {
/*    */   public PluginNodeVisitor() {
/* 30 */     super(PluginNode.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 36 */     if (this.conversionType.isInstance(node)) {
/* 37 */       log.append("Node=").append(node.getName());
/* 38 */       return node;
/*    */     } 
/* 40 */     LOGGER.warn("Variable annotated with @PluginNode is not compatible with the type {}.", node.getClass());
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginNodeVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */