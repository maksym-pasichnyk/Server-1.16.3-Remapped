/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.TimeoutException;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.ExecutorServices;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
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
/*     */ class AsyncLoggerDisruptor
/*     */   extends AbstractLifeCycle
/*     */ {
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*     */   private volatile Disruptor<RingBufferLogEvent> disruptor;
/*     */   private ExecutorService executor;
/*     */   private String contextName;
/*     */   private boolean useThreadLocalTranslator = true;
/*     */   private long backgroundThreadId;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/*     */   private int ringBufferSize;
/*     */   
/*     */   AsyncLoggerDisruptor(String contextName) {
/*  57 */     this.contextName = contextName;
/*     */   }
/*     */   
/*     */   public String getContextName() {
/*  61 */     return this.contextName;
/*     */   }
/*     */   
/*     */   public void setContextName(String name) {
/*  65 */     this.contextName = name;
/*     */   }
/*     */   
/*     */   Disruptor<RingBufferLogEvent> getDisruptor() {
/*  69 */     return this.disruptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  79 */     if (this.disruptor != null) {
/*  80 */       LOGGER.trace("[{}] AsyncLoggerDisruptor not starting new disruptor for this context, using existing object.", this.contextName);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  85 */     LOGGER.trace("[{}] AsyncLoggerDisruptor creating new disruptor for this context.", this.contextName);
/*  86 */     this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLogger.RingBufferSize");
/*  87 */     WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLogger.WaitStrategy");
/*  88 */     this.executor = Executors.newSingleThreadExecutor((ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("AsyncLogger[" + this.contextName + "]"));
/*  89 */     this.backgroundThreadId = DisruptorUtil.getExecutorThreadId(this.executor);
/*  90 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/*  92 */     this.disruptor = new Disruptor(RingBufferLogEvent.FACTORY, this.ringBufferSize, this.executor, ProducerType.MULTI, waitStrategy);
/*     */ 
/*     */     
/*  95 */     ExceptionHandler<RingBufferLogEvent> errorHandler = DisruptorUtil.getAsyncLoggerExceptionHandler();
/*  96 */     this.disruptor.handleExceptionsWith(errorHandler);
/*     */     
/*  98 */     RingBufferLogEventHandler[] handlers = { new RingBufferLogEventHandler() };
/*  99 */     this.disruptor.handleEventsWith((EventHandler[])handlers);
/*     */     
/* 101 */     LOGGER.debug("[{}] Starting AsyncLogger disruptor for this context with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.contextName, Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy.getClass().getSimpleName(), errorHandler);
/*     */ 
/*     */     
/* 104 */     this.disruptor.start();
/*     */     
/* 106 */     LOGGER.trace("[{}] AsyncLoggers use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
/*     */     
/* 108 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 117 */     Disruptor<RingBufferLogEvent> temp = getDisruptor();
/* 118 */     if (temp == null) {
/* 119 */       LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor for this context already shut down.", this.contextName);
/* 120 */       return true;
/*     */     } 
/* 122 */     setStopping();
/* 123 */     LOGGER.debug("[{}] AsyncLoggerDisruptor: shutting down disruptor for this context.", this.contextName);
/*     */ 
/*     */     
/* 126 */     this.disruptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 133 */         Thread.sleep(50L);
/* 134 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 139 */       temp.shutdown(timeout, timeUnit);
/* 140 */     } catch (TimeoutException e) {
/* 141 */       temp.shutdown();
/*     */     } 
/*     */     
/* 144 */     LOGGER.trace("[{}] AsyncLoggerDisruptor: shutting down disruptor executor.", this.contextName);
/*     */     
/* 146 */     ExecutorServices.shutdown(this.executor, timeout, timeUnit, toString());
/* 147 */     this.executor = null;
/*     */     
/* 149 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 150 */       LOGGER.trace("AsyncLoggerDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/*     */     
/* 153 */     setStopped();
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> theDisruptor) {
/* 161 */     RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
/* 162 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String jmxContextName) {
/* 172 */     RingBuffer<RingBufferLogEvent> ring = (this.disruptor == null) ? null : this.disruptor.getRingBuffer();
/* 173 */     return RingBufferAdmin.forAsyncLogger(ring, jmxContextName);
/*     */   }
/*     */   
/*     */   EventRoute getEventRoute(Level logLevel) {
/* 177 */     int remainingCapacity = remainingDisruptorCapacity();
/* 178 */     if (remainingCapacity < 0) {
/* 179 */       return EventRoute.DISCARD;
/*     */     }
/* 181 */     return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
/*     */   }
/*     */   
/*     */   private int remainingDisruptorCapacity() {
/* 185 */     Disruptor<RingBufferLogEvent> temp = this.disruptor;
/* 186 */     if (hasLog4jBeenShutDown(temp)) {
/* 187 */       return -1;
/*     */     }
/* 189 */     return (int)temp.getRingBuffer().remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasLog4jBeenShutDown(Disruptor<RingBufferLogEvent> aDisruptor) {
/* 195 */     if (aDisruptor == null) {
/* 196 */       LOGGER.warn("Ignoring log event after log4j was shut down");
/* 197 */       return true;
/*     */     } 
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryPublish(RingBufferLogEventTranslator translator) {
/*     */     try {
/* 205 */       return this.disruptor.getRingBuffer().tryPublishEvent(translator);
/* 206 */     } catch (NullPointerException npe) {
/* 207 */       LOGGER.warn("[{}] Ignoring log event after log4j was shut down.", this.contextName);
/* 208 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void enqueueLogMessageInfo(RingBufferLogEventTranslator translator) {
/*     */     try {
/* 218 */       this.disruptor.publishEvent(translator);
/* 219 */     } catch (NullPointerException npe) {
/* 220 */       LOGGER.warn("[{}] Ignoring log event after log4j was shut down.", this.contextName);
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
/*     */   public boolean isUseThreadLocals() {
/* 232 */     return this.useThreadLocalTranslator;
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
/*     */   public void setUseThreadLocals(boolean allow) {
/* 247 */     this.useThreadLocalTranslator = allow;
/* 248 */     LOGGER.trace("[{}] AsyncLoggers have been modified to use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerDisruptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */