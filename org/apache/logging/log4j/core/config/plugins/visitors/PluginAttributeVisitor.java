/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
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
/*    */ public class PluginAttributeVisitor
/*    */   extends AbstractPluginVisitor<PluginAttribute>
/*    */ {
/*    */   public PluginAttributeVisitor() {
/* 34 */     super(PluginAttribute.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 40 */     String name = this.annotation.value();
/* 41 */     Map<String, String> attributes = node.getAttributes();
/* 42 */     String rawValue = removeAttributeValue(attributes, name, this.aliases);
/* 43 */     String replacedValue = this.substitutor.replace(event, rawValue);
/* 44 */     Object defaultValue = findDefaultValue(event);
/* 45 */     Object value = convert(replacedValue, defaultValue);
/* 46 */     Object debugValue = this.annotation.sensitive() ? NameUtil.md5(value + getClass().getName()) : value;
/* 47 */     StringBuilders.appendKeyDqValue(log, name, debugValue);
/* 48 */     return value;
/*    */   }
/*    */   
/*    */   private Object findDefaultValue(LogEvent event) {
/* 52 */     if (this.conversionType == int.class || this.conversionType == Integer.class) {
/* 53 */       return Integer.valueOf(this.annotation.defaultInt());
/*    */     }
/* 55 */     if (this.conversionType == long.class || this.conversionType == Long.class) {
/* 56 */       return Long.valueOf(this.annotation.defaultLong());
/*    */     }
/* 58 */     if (this.conversionType == boolean.class || this.conversionType == Boolean.class) {
/* 59 */       return Boolean.valueOf(this.annotation.defaultBoolean());
/*    */     }
/* 61 */     if (this.conversionType == float.class || this.conversionType == Float.class) {
/* 62 */       return Float.valueOf(this.annotation.defaultFloat());
/*    */     }
/* 64 */     if (this.conversionType == double.class || this.conversionType == Double.class) {
/* 65 */       return Double.valueOf(this.annotation.defaultDouble());
/*    */     }
/* 67 */     if (this.conversionType == byte.class || this.conversionType == Byte.class) {
/* 68 */       return Byte.valueOf(this.annotation.defaultByte());
/*    */     }
/* 70 */     if (this.conversionType == char.class || this.conversionType == Character.class) {
/* 71 */       return Character.valueOf(this.annotation.defaultChar());
/*    */     }
/* 73 */     if (this.conversionType == short.class || this.conversionType == Short.class) {
/* 74 */       return Short.valueOf(this.annotation.defaultShort());
/*    */     }
/* 76 */     if (this.conversionType == Class.class) {
/* 77 */       return this.annotation.defaultClass();
/*    */     }
/* 79 */     return this.substitutor.replace(event, this.annotation.defaultString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginAttributeVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */