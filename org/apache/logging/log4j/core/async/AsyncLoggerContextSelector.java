/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ 
/*    */ 
/*    */ public class AsyncLoggerContextSelector
/*    */   extends ClassLoaderContextSelector
/*    */ {
/*    */   public static boolean isSelected() {
/* 40 */     return AsyncLoggerContextSelector.class.getName().equals(PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected LoggerContext createContext(String name, URI configLocation) {
/* 46 */     return new AsyncLoggerContext(name, null, configLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String toContextMapKey(ClassLoader loader) {
/* 52 */     return "AsyncContext@" + Integer.toHexString(System.identityHashCode(loader));
/*    */   }
/*    */ 
/*    */   
/*    */   protected String defaultContextName() {
/* 57 */     return "DefaultAsyncContext@" + Thread.currentThread().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerContextSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */