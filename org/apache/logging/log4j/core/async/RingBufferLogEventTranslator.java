/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventTranslator;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RingBufferLogEventTranslator
/*     */   implements EventTranslator<RingBufferLogEvent>
/*     */ {
/*  38 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
/*     */   private AsyncLogger asyncLogger;
/*     */   private String loggerName;
/*     */   protected Marker marker;
/*     */   protected String fqcn;
/*     */   protected Level level;
/*     */   protected Message message;
/*     */   protected Throwable thrown;
/*     */   private ThreadContext.ContextStack contextStack;
/*  47 */   private long threadId = Thread.currentThread().getId();
/*  48 */   private String threadName = Thread.currentThread().getName();
/*  49 */   private int threadPriority = Thread.currentThread().getPriority();
/*     */   
/*     */   private StackTraceElement location;
/*     */   
/*     */   private long currentTimeMillis;
/*     */   
/*     */   private long nanoTime;
/*     */   
/*     */   public void translateTo(RingBufferLogEvent event, long sequence) {
/*  58 */     event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, this.injector.injectContextData(null, (StringMap)event.getContextData()), this.contextStack, this.threadId, this.threadName, this.threadPriority, this.location, this.currentTimeMillis, this.nanoTime);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clear() {
/*  71 */     setBasicValues(null, null, null, null, null, null, null, null, null, 0L, 0L);
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
/*     */   public void setBasicValues(AsyncLogger anAsyncLogger, String aLoggerName, Marker aMarker, String theFqcn, Level aLevel, Message msg, Throwable aThrowable, ThreadContext.ContextStack aContextStack, StackTraceElement aLocation, long aCurrentTimeMillis, long aNanoTime) {
/*  89 */     this.asyncLogger = anAsyncLogger;
/*  90 */     this.loggerName = aLoggerName;
/*  91 */     this.marker = aMarker;
/*  92 */     this.fqcn = theFqcn;
/*  93 */     this.level = aLevel;
/*  94 */     this.message = msg;
/*  95 */     this.thrown = aThrowable;
/*  96 */     this.contextStack = aContextStack;
/*  97 */     this.location = aLocation;
/*  98 */     this.currentTimeMillis = aCurrentTimeMillis;
/*  99 */     this.nanoTime = aNanoTime;
/*     */   }
/*     */   
/*     */   public void updateThreadValues() {
/* 103 */     Thread currentThread = Thread.currentThread();
/* 104 */     this.threadId = currentThread.getId();
/* 105 */     this.threadName = currentThread.getName();
/* 106 */     this.threadPriority = currentThread.getPriority();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventTranslator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */