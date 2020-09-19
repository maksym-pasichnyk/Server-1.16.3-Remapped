/*    */ package org.apache.logging.log4j.core.config.builder.api;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class Component
/*    */ {
/* 31 */   private final Map<String, String> attributes = new LinkedHashMap<>();
/* 32 */   private final List<Component> components = new ArrayList<>();
/*    */   private final String pluginType;
/*    */   private final String value;
/*    */   
/*    */   public Component(String pluginType) {
/* 37 */     this(pluginType, null, null);
/*    */   }
/*    */   
/*    */   public Component(String pluginType, String name) {
/* 41 */     this(pluginType, name, null);
/*    */   }
/*    */   
/*    */   public Component(String pluginType, String name, String value) {
/* 45 */     this.pluginType = pluginType;
/* 46 */     this.value = value;
/* 47 */     if (name != null && name.length() > 0) {
/* 48 */       this.attributes.put("name", name);
/*    */     }
/*    */   }
/*    */   
/*    */   public Component() {
/* 53 */     this.pluginType = null;
/* 54 */     this.value = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String addAttribute(String key, String newValue) {
/* 59 */     return this.attributes.put(key, newValue);
/*    */   }
/*    */   
/*    */   public void addComponent(Component component) {
/* 63 */     this.components.add(component);
/*    */   }
/*    */   
/*    */   public Map<String, String> getAttributes() {
/* 67 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public List<Component> getComponents() {
/* 71 */     return this.components;
/*    */   }
/*    */   
/*    */   public String getPluginType() {
/* 75 */     return this.pluginType;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 79 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\api\Component.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */