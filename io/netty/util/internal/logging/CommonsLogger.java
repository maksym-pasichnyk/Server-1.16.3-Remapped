/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
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
/*     */ @Deprecated
/*     */ class CommonsLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*     */   private static final long serialVersionUID = 8647838678388394885L;
/*     */   private final transient Log logger;
/*     */   
/*     */   CommonsLogger(Log logger, String name) {
/*  59 */     super(name);
/*  60 */     if (logger == null) {
/*  61 */       throw new NullPointerException("logger");
/*     */     }
/*  63 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  72 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/*  83 */     this.logger.trace(msg);
/*     */   }
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
/*     */   public void trace(String format, Object arg) {
/* 102 */     if (this.logger.isTraceEnabled()) {
/* 103 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 104 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   
/*     */   public void trace(String format, Object argA, Object argB) {
/* 126 */     if (this.logger.isTraceEnabled()) {
/* 127 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 128 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void trace(String format, Object... arguments) {
/* 146 */     if (this.logger.isTraceEnabled()) {
/* 147 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 148 */       this.logger.trace(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void trace(String msg, Throwable t) {
/* 163 */     this.logger.trace(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 172 */     return this.logger.isDebugEnabled();
/*     */   }
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
/*     */   public void debug(String msg) {
/* 185 */     this.logger.debug(msg);
/*     */   }
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
/*     */   public void debug(String format, Object arg) {
/* 204 */     if (this.logger.isDebugEnabled()) {
/* 205 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 206 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   
/*     */   public void debug(String format, Object argA, Object argB) {
/* 228 */     if (this.logger.isDebugEnabled()) {
/* 229 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 230 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void debug(String format, Object... arguments) {
/* 248 */     if (this.logger.isDebugEnabled()) {
/* 249 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 250 */       this.logger.debug(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void debug(String msg, Throwable t) {
/* 265 */     this.logger.debug(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 274 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 285 */     this.logger.info(msg);
/*     */   }
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
/*     */   public void info(String format, Object arg) {
/* 305 */     if (this.logger.isInfoEnabled()) {
/* 306 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 307 */       this.logger.info(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void info(String format, Object argA, Object argB) {
/* 328 */     if (this.logger.isInfoEnabled()) {
/* 329 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 330 */       this.logger.info(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void info(String format, Object... arguments) {
/* 348 */     if (this.logger.isInfoEnabled()) {
/* 349 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 350 */       this.logger.info(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void info(String msg, Throwable t) {
/* 365 */     this.logger.info(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 374 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 385 */     this.logger.warn(msg);
/*     */   }
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
/*     */   public void warn(String format, Object arg) {
/* 404 */     if (this.logger.isWarnEnabled()) {
/* 405 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 406 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   
/*     */   public void warn(String format, Object argA, Object argB) {
/* 428 */     if (this.logger.isWarnEnabled()) {
/* 429 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 430 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void warn(String format, Object... arguments) {
/* 448 */     if (this.logger.isWarnEnabled()) {
/* 449 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 450 */       this.logger.warn(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void warn(String msg, Throwable t) {
/* 466 */     this.logger.warn(msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 475 */     return this.logger.isErrorEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 486 */     this.logger.error(msg);
/*     */   }
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
/*     */   public void error(String format, Object arg) {
/* 505 */     if (this.logger.isErrorEnabled()) {
/* 506 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 507 */       this.logger.error(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   
/*     */   public void error(String format, Object argA, Object argB) {
/* 529 */     if (this.logger.isErrorEnabled()) {
/* 530 */       FormattingTuple ft = MessageFormatter.format(format, argA, argB);
/* 531 */       this.logger.error(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void error(String format, Object... arguments) {
/* 549 */     if (this.logger.isErrorEnabled()) {
/* 550 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 551 */       this.logger.error(ft.getMessage(), ft.getThrowable());
/*     */     } 
/*     */   }
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
/*     */   public void error(String msg, Throwable t) {
/* 566 */     this.logger.error(msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\CommonsLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */