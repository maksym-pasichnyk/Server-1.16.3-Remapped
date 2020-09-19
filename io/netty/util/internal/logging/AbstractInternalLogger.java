/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class AbstractInternalLogger
/*     */   implements InternalLogger, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6382972526573193470L;
/*     */   private static final String EXCEPTION_MESSAGE = "Unexpected exception:";
/*     */   private final String name;
/*     */   
/*     */   protected AbstractInternalLogger(String name) {
/*  40 */     if (name == null) {
/*  41 */       throw new NullPointerException("name");
/*     */     }
/*  43 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  48 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(InternalLogLevel level) {
/*  53 */     switch (level) {
/*     */       case TRACE:
/*  55 */         return isTraceEnabled();
/*     */       case DEBUG:
/*  57 */         return isDebugEnabled();
/*     */       case INFO:
/*  59 */         return isInfoEnabled();
/*     */       case WARN:
/*  61 */         return isWarnEnabled();
/*     */       case ERROR:
/*  63 */         return isErrorEnabled();
/*     */     } 
/*  65 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(Throwable t) {
/*  71 */     trace("Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(Throwable t) {
/*  76 */     debug("Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(Throwable t) {
/*  81 */     info("Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(Throwable t) {
/*  86 */     warn("Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(Throwable t) {
/*  91 */     error("Unexpected exception:", t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String msg, Throwable cause) {
/*  96 */     switch (level) {
/*     */       case TRACE:
/*  98 */         trace(msg, cause);
/*     */         return;
/*     */       case DEBUG:
/* 101 */         debug(msg, cause);
/*     */         return;
/*     */       case INFO:
/* 104 */         info(msg, cause);
/*     */         return;
/*     */       case WARN:
/* 107 */         warn(msg, cause);
/*     */         return;
/*     */       case ERROR:
/* 110 */         error(msg, cause);
/*     */         return;
/*     */     } 
/* 113 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, Throwable cause) {
/* 119 */     switch (level) {
/*     */       case TRACE:
/* 121 */         trace(cause);
/*     */         return;
/*     */       case DEBUG:
/* 124 */         debug(cause);
/*     */         return;
/*     */       case INFO:
/* 127 */         info(cause);
/*     */         return;
/*     */       case WARN:
/* 130 */         warn(cause);
/*     */         return;
/*     */       case ERROR:
/* 133 */         error(cause);
/*     */         return;
/*     */     } 
/* 136 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String msg) {
/* 142 */     switch (level) {
/*     */       case TRACE:
/* 144 */         trace(msg);
/*     */         return;
/*     */       case DEBUG:
/* 147 */         debug(msg);
/*     */         return;
/*     */       case INFO:
/* 150 */         info(msg);
/*     */         return;
/*     */       case WARN:
/* 153 */         warn(msg);
/*     */         return;
/*     */       case ERROR:
/* 156 */         error(msg);
/*     */         return;
/*     */     } 
/* 159 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object arg) {
/* 165 */     switch (level) {
/*     */       case TRACE:
/* 167 */         trace(format, arg);
/*     */         return;
/*     */       case DEBUG:
/* 170 */         debug(format, arg);
/*     */         return;
/*     */       case INFO:
/* 173 */         info(format, arg);
/*     */         return;
/*     */       case WARN:
/* 176 */         warn(format, arg);
/*     */         return;
/*     */       case ERROR:
/* 179 */         error(format, arg);
/*     */         return;
/*     */     } 
/* 182 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object argA, Object argB) {
/* 188 */     switch (level) {
/*     */       case TRACE:
/* 190 */         trace(format, argA, argB);
/*     */         return;
/*     */       case DEBUG:
/* 193 */         debug(format, argA, argB);
/*     */         return;
/*     */       case INFO:
/* 196 */         info(format, argA, argB);
/*     */         return;
/*     */       case WARN:
/* 199 */         warn(format, argA, argB);
/*     */         return;
/*     */       case ERROR:
/* 202 */         error(format, argA, argB);
/*     */         return;
/*     */     } 
/* 205 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(InternalLogLevel level, String format, Object... arguments) {
/* 211 */     switch (level) {
/*     */       case TRACE:
/* 213 */         trace(format, arguments);
/*     */         return;
/*     */       case DEBUG:
/* 216 */         debug(format, arguments);
/*     */         return;
/*     */       case INFO:
/* 219 */         info(format, arguments);
/*     */         return;
/*     */       case WARN:
/* 222 */         warn(format, arguments);
/*     */         return;
/*     */       case ERROR:
/* 225 */         error(format, arguments);
/*     */         return;
/*     */     } 
/* 228 */     throw new Error();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readResolve() throws ObjectStreamException {
/* 233 */     return InternalLoggerFactory.getInstance(name());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 238 */     return StringUtil.simpleClassName(this) + '(' + name() + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\logging\AbstractInternalLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */