/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.appender.AsyncAppender;
/*    */ import org.apache.logging.log4j.message.Message;
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
/*    */ public enum EventRoute
/*    */ {
/* 38 */   ENQUEUE
/*    */   {
/*    */     public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
/* 46 */       asyncLoggerConfig.callAppendersInBackgroundThread(event);
/*    */     }
/*    */ 
/*    */     
/*    */     public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
/* 51 */       asyncAppender.logMessageInBackgroundThread(logEvent);
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */   
/* 57 */   SYNCHRONOUS
/*    */   {
/*    */     public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {
/* 65 */       asyncLoggerConfig.callAppendersInCurrentThread(event);
/*    */     }
/*    */ 
/*    */     
/*    */     public void logMessage(AsyncAppender asyncAppender, LogEvent logEvent) {
/* 70 */       asyncAppender.logMessageInCurrentThread(logEvent);
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */   
/* 76 */   DISCARD {
/*    */     public void logMessage(AsyncLogger asyncLogger, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {}
/*    */     
/*    */     public void logMessage(AsyncLoggerConfig asyncLoggerConfig, LogEvent event) {}
/*    */     
/*    */     public void logMessage(AsyncAppender asyncAppender, LogEvent coreEvent) {}
/*    */   };
/*    */   
/*    */   public abstract void logMessage(AsyncLogger paramAsyncLogger, String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
/*    */   
/*    */   public abstract void logMessage(AsyncLoggerConfig paramAsyncLoggerConfig, LogEvent paramLogEvent);
/*    */   
/*    */   public abstract void logMessage(AsyncAppender paramAsyncAppender, LogEvent paramLogEvent);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\EventRoute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */