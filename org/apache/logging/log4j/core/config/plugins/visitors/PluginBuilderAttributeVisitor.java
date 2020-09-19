/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*    */ import org.apache.logging.log4j.core.util.NameUtil;
/*    */ import org.apache.logging.log4j.util.StringBuilders;
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
/*    */ public class PluginBuilderAttributeVisitor
/*    */   extends AbstractPluginVisitor<PluginBuilderAttribute>
/*    */ {
/*    */   public PluginBuilderAttributeVisitor() {
/* 39 */     super(PluginBuilderAttribute.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 45 */     String overridden = this.annotation.value();
/* 46 */     String name = overridden.isEmpty() ? this.member.getName() : overridden;
/* 47 */     Map<String, String> attributes = node.getAttributes();
/* 48 */     String rawValue = removeAttributeValue(attributes, name, this.aliases);
/* 49 */     String replacedValue = this.substitutor.replace(event, rawValue);
/* 50 */     Object value = convert(replacedValue, null);
/* 51 */     Object debugValue = this.annotation.sensitive() ? NameUtil.md5(value + getClass().getName()) : value;
/* 52 */     StringBuilders.appendKeyDqValue(log, name, debugValue);
/* 53 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginBuilderAttributeVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */