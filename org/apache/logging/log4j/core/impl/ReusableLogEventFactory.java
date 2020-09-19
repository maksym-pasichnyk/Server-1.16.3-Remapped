/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.ThreadNameCachingStrategy;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ public class ReusableLogEventFactory
/*     */   implements LogEventFactory
/*     */ {
/*  39 */   private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
/*  40 */   private static final Clock CLOCK = ClockFactory.getClock();
/*     */   
/*  42 */   private static ThreadLocal<MutableLogEvent> mutableLogEventThreadLocal = new ThreadLocal<>();
/*  43 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
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
/*     */   public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message message, List<Property> properties, Throwable t) {
/*  61 */     MutableLogEvent result = mutableLogEventThreadLocal.get();
/*  62 */     if (result == null || result.reserved) {
/*  63 */       boolean initThreadLocal = (result == null);
/*  64 */       result = new MutableLogEvent();
/*     */ 
/*     */       
/*  67 */       result.setThreadId(Thread.currentThread().getId());
/*  68 */       result.setThreadName(Thread.currentThread().getName());
/*  69 */       result.setThreadPriority(Thread.currentThread().getPriority());
/*  70 */       if (initThreadLocal) {
/*  71 */         mutableLogEventThreadLocal.set(result);
/*     */       }
/*     */     } 
/*  74 */     result.reserved = true;
/*  75 */     result.clear();
/*     */     
/*  77 */     result.setLoggerName(loggerName);
/*  78 */     result.setMarker(marker);
/*  79 */     result.setLoggerFqcn(fqcn);
/*  80 */     result.setLevel((level == null) ? Level.OFF : level);
/*  81 */     result.setMessage(message);
/*  82 */     result.setThrown(t);
/*  83 */     result.setContextData(this.injector.injectContextData(properties, (StringMap)result.getContextData()));
/*  84 */     result.setContextStack((ThreadContext.getDepth() == 0) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : ThreadContext.cloneStack());
/*  85 */     result.setTimeMillis((message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : CLOCK.currentTimeMillis());
/*     */ 
/*     */     
/*  88 */     result.setNanoTime(Log4jLogEvent.getNanoClock().nanoTime());
/*     */     
/*  90 */     if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
/*  91 */       result.setThreadName(Thread.currentThread().getName());
/*  92 */       result.setThreadPriority(Thread.currentThread().getPriority());
/*     */     } 
/*  94 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void release(LogEvent logEvent) {
/* 104 */     if (logEvent instanceof MutableLogEvent)
/* 105 */       ((MutableLogEvent)logEvent).reserved = false; 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ReusableLogEventFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */