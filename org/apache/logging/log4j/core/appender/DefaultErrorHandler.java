/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.ErrorHandler;
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ public class DefaultErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private static final int MAX_EXCEPTIONS = 3;
/*    */   
/* 36 */   private static final long EXCEPTION_INTERVAL = TimeUnit.MINUTES.toNanos(5L);
/*    */   
/* 38 */   private int exceptionCount = 0;
/*    */   
/* 40 */   private long lastException = System.nanoTime() - EXCEPTION_INTERVAL - 1L;
/*    */   
/*    */   private final Appender appender;
/*    */   
/*    */   public DefaultErrorHandler(Appender appender) {
/* 45 */     this.appender = appender;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String msg) {
/* 55 */     long current = System.nanoTime();
/* 56 */     if (current - this.lastException > EXCEPTION_INTERVAL || this.exceptionCount++ < 3) {
/* 57 */       LOGGER.error(msg);
/*    */     }
/* 59 */     this.lastException = current;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String msg, Throwable t) {
/* 69 */     long current = System.nanoTime();
/* 70 */     if (current - this.lastException > EXCEPTION_INTERVAL || this.exceptionCount++ < 3) {
/* 71 */       LOGGER.error(msg, t);
/*    */     }
/* 73 */     this.lastException = current;
/* 74 */     if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException)) {
/* 75 */       throw new AppenderLoggingException(msg, t);
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
/*    */   public void error(String msg, LogEvent event, Throwable t) {
/* 87 */     long current = System.nanoTime();
/* 88 */     if (current - this.lastException > EXCEPTION_INTERVAL || this.exceptionCount++ < 3) {
/* 89 */       LOGGER.error(msg, t);
/*    */     }
/* 91 */     this.lastException = current;
/* 92 */     if (!this.appender.ignoreExceptions() && t != null && !(t instanceof AppenderLoggingException)) {
/* 93 */       throw new AppenderLoggingException(msg, t);
/*    */     }
/*    */   }
/*    */   
/*    */   public Appender getAppender() {
/* 98 */     return this.appender;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\DefaultErrorHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */