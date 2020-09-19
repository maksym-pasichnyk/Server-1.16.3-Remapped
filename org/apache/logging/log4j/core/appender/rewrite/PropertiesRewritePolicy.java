/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class PropertiesRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  44 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<Property, Boolean> properties;
/*     */   
/*     */   private final Configuration config;
/*     */   
/*     */   private PropertiesRewritePolicy(Configuration config, List<Property> props) {
/*  51 */     this.config = config;
/*  52 */     this.properties = new HashMap<>(props.size());
/*  53 */     for (Property property : props) {
/*  54 */       Boolean interpolate = Boolean.valueOf(property.getValue().contains("${"));
/*  55 */       this.properties.put(property, interpolate);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent rewrite(LogEvent source) {
/*  67 */     Map<String, String> props = new HashMap<>(source.getContextData().toMap());
/*  68 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  69 */       Property prop = entry.getKey();
/*  70 */       props.put(prop.getName(), ((Boolean)entry.getValue()).booleanValue() ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue());
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return (LogEvent)(new Log4jLogEvent.Builder(source)).setContextMap(props).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     StringBuilder sb = new StringBuilder();
/*  81 */     sb.append(" {");
/*  82 */     boolean first = true;
/*  83 */     for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
/*  84 */       if (!first) {
/*  85 */         sb.append(", ");
/*     */       }
/*  87 */       Property prop = entry.getKey();
/*  88 */       sb.append(prop.getName()).append('=').append(prop.getValue());
/*  89 */       first = false;
/*     */     } 
/*  91 */     sb.append('}');
/*  92 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration config, @PluginElement("Properties") Property[] props) {
/* 104 */     if (props == null || props.length == 0) {
/* 105 */       LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
/* 106 */       return null;
/*     */     } 
/* 108 */     List<Property> properties = Arrays.asList(props);
/* 109 */     return new PropertiesRewritePolicy(config, properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rewrite\PropertiesRewritePolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */