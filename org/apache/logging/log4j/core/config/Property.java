/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "property", category = "Core", printObject = true)
/*    */ public final class Property
/*    */ {
/* 35 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final String name;
/*    */   private final String value;
/*    */   private final boolean valueNeedsLookup;
/*    */   
/*    */   private Property(String name, String value) {
/* 42 */     this.name = name;
/* 43 */     this.value = value;
/* 44 */     this.valueNeedsLookup = (value != null && value.contains("${"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 60 */     return Objects.toString(this.value, "");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValueNeedsLookup() {
/* 68 */     return this.valueNeedsLookup;
/*    */   }
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
/*    */   @PluginFactory
/*    */   public static Property createProperty(@PluginAttribute("name") String name, @PluginValue("value") String value) {
/* 82 */     if (name == null) {
/* 83 */       LOGGER.error("Property name cannot be null");
/*    */     }
/* 85 */     return new Property(name, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return this.name + '=' + getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\Property.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */