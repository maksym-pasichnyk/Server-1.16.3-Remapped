/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public abstract class AbstractLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public LogEvent toImmutable() {
/*  43 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/*  56 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/*  61 */     return (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/*  96 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 121 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean locationRequired) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 146 */     return 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\AbstractLogEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */