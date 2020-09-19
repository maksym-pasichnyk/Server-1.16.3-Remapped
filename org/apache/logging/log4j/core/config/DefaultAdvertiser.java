/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.net.Advertiser;
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
/*    */ @Plugin(name = "default", category = "Core", elementType = "advertiser", printObject = false)
/*    */ public class DefaultAdvertiser
/*    */   implements Advertiser
/*    */ {
/*    */   public Object advertise(Map<String, String> properties) {
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public void unadvertise(Object advertisedObject) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\DefaultAdvertiser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */