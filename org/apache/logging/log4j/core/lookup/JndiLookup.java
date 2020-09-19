/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import javax.naming.NamingException;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.net.JndiManager;
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
/*    */ @Plugin(name = "jndi", category = "Lookup")
/*    */ public class JndiLookup
/*    */   extends AbstractLookup
/*    */ {
/* 35 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 36 */   private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 49 */     if (key == null) {
/* 50 */       return null;
/*    */     }
/* 52 */     String jndiName = convertJndiName(key);
/* 53 */     try (JndiManager jndiManager = JndiManager.getDefaultManager()) {
/* 54 */       Object value = jndiManager.lookup(jndiName);
/* 55 */       return (value == null) ? null : String.valueOf(value);
/* 56 */     } catch (NamingException e) {
/* 57 */       LOGGER.warn(LOOKUP, "Error looking up JNDI resource [{}].", jndiName, e);
/* 58 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String convertJndiName(String jndiName) {
/* 70 */     if (!jndiName.startsWith("java:comp/env/") && jndiName.indexOf(':') == -1) {
/* 71 */       return "java:comp/env/" + jndiName;
/*    */     }
/* 73 */     return jndiName;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\JndiLookup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */