/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Log4J2Logger
/*     */   extends ExtendedLoggerWrapper
/*     */   implements InternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = 5485418394879791397L;
/*     */   private static final String EXCEPTION_MESSAGE = "Unexpected exception:";
/*     */   
/*     */   Log4J2Logger(Logger logger) {
/*  32 */     super((ExtendedLogger)logger, logger.getName(), logger.getMessageFactory());
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  37 */     return getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(Throwable t) {
/*  42 */     log(Level.TRACE, "Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Throwable t) {
/*  47 */     log(Level.DEBUG, "Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Throwable t) {
/*  52 */     log(Level.INFO, "Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Throwable t) {
/*  57 */     log(Level.WARN, "Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Throwable t) {
/*  62 */     log(Level.ERROR, "Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(InternalLogLevel level) {
/*  67 */     return isEnabled(toLevel(level));
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String msg) {
/*  72 */     log(toLevel(level), msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object arg) {
/*  77 */     log(toLevel(level), format, arg);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object argA, Object argB) {
/*  82 */     log(toLevel(level), format, argA, argB);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object... arguments) {
/*  87 */     log(toLevel(level), format, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String msg, Throwable t) {
/*  92 */     log(toLevel(level), msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, Throwable t) {
/*  97 */     log(toLevel(level), "Unexpected exception:", t);
/*     */   }
/*     */   
/*     */   protected Level toLevel(InternalLogLevel level) {
/* 101 */     switch (level) {
/*     */       case INFO:
/* 103 */         return Level.INFO;
/*     */       case DEBUG:
/* 105 */         return Level.DEBUG;
/*     */       case WARN:
/* 107 */         return Level.WARN;
/*     */       case ERROR:
/* 109 */         return Level.ERROR;
/*     */       case TRACE:
/* 111 */         return Level.TRACE;
/*     */     } 
/* 113 */     throw new Error();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\Log4J2Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */