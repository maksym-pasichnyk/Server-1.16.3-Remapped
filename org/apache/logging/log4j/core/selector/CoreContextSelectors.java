/*    */ package org.apache.logging.log4j.core.selector;
/*    */ 
/*    */ import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
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
/*    */ public class CoreContextSelectors
/*    */ {
/* 23 */   public static final Class<?>[] CLASSES = new Class[] { ClassLoaderContextSelector.class, BasicContextSelector.class, AsyncLoggerContextSelector.class };
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\selector\CoreContextSelectors.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */