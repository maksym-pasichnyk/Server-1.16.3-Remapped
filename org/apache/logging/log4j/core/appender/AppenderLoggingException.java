/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.LoggingException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AppenderLoggingException
/*    */   extends LoggingException
/*    */ {
/*    */   private static final long serialVersionUID = 6545990597472958303L;
/*    */   
/*    */   public AppenderLoggingException(String message) {
/* 42 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderLoggingException(String message, Throwable cause) {
/* 52 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderLoggingException(Throwable cause) {
/* 61 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AppenderLoggingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */