/*    */ package org.apache.logging.log4j.core.config.plugins.util;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;
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
/*    */ public class PluginType<T>
/*    */ {
/*    */   private final PluginEntry pluginEntry;
/*    */   private final Class<T> pluginClass;
/*    */   private final String elementName;
/*    */   
/*    */   public PluginType(PluginEntry pluginEntry, Class<T> pluginClass, String elementName) {
/* 38 */     this.pluginEntry = pluginEntry;
/* 39 */     this.pluginClass = pluginClass;
/* 40 */     this.elementName = elementName;
/*    */   }
/*    */   
/*    */   public Class<T> getPluginClass() {
/* 44 */     return this.pluginClass;
/*    */   }
/*    */   
/*    */   public String getElementName() {
/* 48 */     return this.elementName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 55 */     return this.pluginEntry.getKey();
/*    */   }
/*    */   
/*    */   public boolean isObjectPrintable() {
/* 59 */     return this.pluginEntry.isPrintable();
/*    */   }
/*    */   
/*    */   public boolean isDeferChildren() {
/* 63 */     return this.pluginEntry.isDefer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCategory() {
/* 70 */     return this.pluginEntry.getCategory();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "PluginType [pluginClass=" + this.pluginClass + ", key=" + this.pluginEntry.getKey() + ", elementName=" + this.pluginEntry.getName() + ", isObjectPrintable=" + this.pluginEntry.isPrintable() + ", isDeferChildren==" + this.pluginEntry.isDefer() + ", category=" + this.pluginEntry.getCategory() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */