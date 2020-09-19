/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.EventTranslatorTwoArg;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.Sequence;
/*     */ import com.lmax.disruptor.SequenceReportingEventHandler;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.LogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.MutableLogEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncLoggerConfigDisruptor
/*     */   extends AbstractLifeCycle
/*     */   implements AsyncLoggerConfigDelegate
/*     */ {
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   
/*     */   public static class Log4jEventWrapper
/*     */   {
/*     */     private AsyncLoggerConfig loggerConfig;
/*     */     private LogEvent event;
/*     */     
/*     */     public Log4jEventWrapper() {}
/*     */     
/*     */     public Log4jEventWrapper(MutableLogEvent mutableLogEvent) {
/*  71 */       this.event = (LogEvent)mutableLogEvent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/*  81 */       this.loggerConfig = null;
/*  82 */       if (this.event instanceof MutableLogEvent) {
/*  83 */         ((MutableLogEvent)this.event).clear();
/*     */       } else {
/*  85 */         this.event = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return String.valueOf(this.event);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Log4jEventWrapperHandler
/*     */     implements SequenceReportingEventHandler<Log4jEventWrapper>
/*     */   {
/*     */     private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*     */     private Sequence sequenceCallback;
/*     */     private int counter;
/*     */     
/*     */     private Log4jEventWrapperHandler() {}
/*     */     
/*     */     public void setSequenceCallback(Sequence sequenceCallback) {
/* 105 */       this.sequenceCallback = sequenceCallback;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onEvent(AsyncLoggerConfigDisruptor.Log4jEventWrapper event, long sequence, boolean endOfBatch) throws Exception {
/* 111 */       event.event.setEndOfBatch(endOfBatch);
/* 112 */       event.loggerConfig.asyncCallAppenders(event.event);
/* 113 */       event.clear();
/*     */       
/* 115 */       notifyIntermediateProgress(sequence);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void notifyIntermediateProgress(long sequence) {
/* 123 */       if (++this.counter > 50) {
/* 124 */         this.sequenceCallback.set(sequence);
/* 125 */         this.counter = 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private static final EventFactory<Log4jEventWrapper> FACTORY = new EventFactory<Log4jEventWrapper>()
/*     */     {
/*     */       public AsyncLoggerConfigDisruptor.Log4jEventWrapper newInstance() {
/* 137 */         return new AsyncLoggerConfigDisruptor.Log4jEventWrapper();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY = new EventFactory<Log4jEventWrapper>()
/*     */     {
/*     */       public AsyncLoggerConfigDisruptor.Log4jEventWrapper newInstance() {
/* 148 */         return new AsyncLoggerConfigDisruptor.Log4jEventWrapper(new MutableLogEvent());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> TRANSLATOR = new EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig>()
/*     */     {
/*     */ 
/*     */       
/*     */       public void translateTo(AsyncLoggerConfigDisruptor.Log4jEventWrapper ringBufferElement, long sequence, LogEvent logEvent, AsyncLoggerConfig loggerConfig)
/*     */       {
/* 161 */         ringBufferElement.event = logEvent;
/* 162 */         ringBufferElement.loggerConfig = loggerConfig;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> MUTABLE_TRANSLATOR = new EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig>()
/*     */     {
/*     */ 
/*     */       
/*     */       public void translateTo(AsyncLoggerConfigDisruptor.Log4jEventWrapper ringBufferElement, long sequence, LogEvent logEvent, AsyncLoggerConfig loggerConfig)
/*     */       {
/* 175 */         ((MutableLogEvent)ringBufferElement.event).initFrom(logEvent);
/* 176 */         ringBufferElement.loggerConfig = loggerConfig;
/*     */       }
/*     */     };
/*     */   
/* 180 */   private static final ThreadFactory THREAD_FACTORY = (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("AsyncLoggerConfig");
/*     */   
/*     */   private int ringBufferSize;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/* 184 */   private Boolean mutable = Boolean.FALSE;
/*     */ 
/*     */   
/*     */   private volatile Disruptor<Log4jEventWrapper> disruptor;
/*     */ 
/*     */   
/*     */   private ExecutorService executor;
/*     */   
/*     */   private long backgroundThreadId;
/*     */   
/*     */   private EventFactory<Log4jEventWrapper> factory;
/*     */   
/*     */   private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator;
/*     */ 
/*     */   
/*     */   public void setLogEventFactory(LogEventFactory logEventFactory) {
/* 200 */     this.mutable = Boolean.valueOf((this.mutable.booleanValue() || logEventFactory instanceof org.apache.logging.log4j.core.impl.ReusableLogEventFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/* 211 */     if (this.disruptor != null) {
/* 212 */       LOGGER.trace("AsyncLoggerConfigDisruptor not starting new disruptor for this configuration, using existing object.");
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     LOGGER.trace("AsyncLoggerConfigDisruptor creating new disruptor for this configuration.");
/* 217 */     this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");
/* 218 */     WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");
/* 219 */     this.executor = Executors.newSingleThreadExecutor(THREAD_FACTORY);
/* 220 */     this.backgroundThreadId = DisruptorUtil.getExecutorThreadId(this.executor);
/* 221 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/* 223 */     this.translator = this.mutable.booleanValue() ? MUTABLE_TRANSLATOR : TRANSLATOR;
/* 224 */     this.factory = this.mutable.booleanValue() ? MUTABLE_FACTORY : FACTORY;
/* 225 */     this.disruptor = new Disruptor(this.factory, this.ringBufferSize, this.executor, ProducerType.MULTI, waitStrategy);
/*     */     
/* 227 */     ExceptionHandler<Log4jEventWrapper> errorHandler = DisruptorUtil.getAsyncLoggerConfigExceptionHandler();
/* 228 */     this.disruptor.handleExceptionsWith(errorHandler);
/*     */     
/* 230 */     Log4jEventWrapperHandler[] handlers = { new Log4jEventWrapperHandler() };
/* 231 */     this.disruptor.handleEventsWith((EventHandler[])handlers);
/*     */     
/* 233 */     LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy.getClass().getSimpleName(), errorHandler);
/*     */ 
/*     */     
/* 236 */     this.disruptor.start();
/* 237 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 246 */     Disruptor<Log4jEventWrapper> temp = this.disruptor;
/* 247 */     if (temp == null) {
/* 248 */       LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor for this configuration already shut down.");
/* 249 */       return true;
/*     */     } 
/* 251 */     setStopping();
/* 252 */     LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor for this configuration.");
/*     */ 
/*     */     
/* 255 */     this.disruptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 262 */         Thread.sleep(50L);
/* 263 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */     
/* 266 */     temp.shutdown();
/*     */     
/* 268 */     LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor executor for this configuration.");
/*     */     
/* 270 */     ExecutorServices.shutdown(this.executor, timeout, timeUnit, toString());
/* 271 */     this.executor = null;
/*     */     
/* 273 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 274 */       LOGGER.trace("AsyncLoggerConfigDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/*     */     
/* 277 */     setStopped();
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> theDisruptor) {
/* 285 */     RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
/* 286 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public EventRoute getEventRoute(Level logLevel) {
/* 291 */     int remainingCapacity = remainingDisruptorCapacity();
/* 292 */     if (remainingCapacity < 0) {
/* 293 */       return EventRoute.DISCARD;
/*     */     }
/* 295 */     return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
/*     */   }
/*     */   
/*     */   private int remainingDisruptorCapacity() {
/* 299 */     Disruptor<Log4jEventWrapper> temp = this.disruptor;
/* 300 */     if (hasLog4jBeenShutDown(temp)) {
/* 301 */       return -1;
/*     */     }
/* 303 */     return (int)temp.getRingBuffer().remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasLog4jBeenShutDown(Disruptor<Log4jEventWrapper> aDisruptor) {
/* 310 */     if (aDisruptor == null) {
/* 311 */       LOGGER.warn("Ignoring log event after log4j was shut down");
/* 312 */       return true;
/*     */     } 
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueueEvent(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
/*     */     try {
/* 321 */       LogEvent logEvent = prepareEvent(event);
/* 322 */       enqueue(logEvent, asyncLoggerConfig);
/* 323 */     } catch (NullPointerException npe) {
/*     */ 
/*     */       
/* 326 */       LOGGER.warn("Ignoring log event after log4j was shut down.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private LogEvent prepareEvent(LogEvent event) {
/* 331 */     LogEvent logEvent = ensureImmutable(event);
/* 332 */     if (logEvent instanceof Log4jLogEvent && logEvent.getMessage() instanceof org.apache.logging.log4j.message.ReusableMessage) {
/* 333 */       ((Log4jLogEvent)logEvent).makeMessageImmutable();
/*     */     }
/* 335 */     return logEvent;
/*     */   }
/*     */   
/*     */   private void enqueue(LogEvent logEvent, AsyncLoggerConfig asyncLoggerConfig) {
/* 339 */     this.disruptor.getRingBuffer().publishEvent(this.translator, logEvent, asyncLoggerConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryEnqueue(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
/* 344 */     LogEvent logEvent = prepareEvent(event);
/* 345 */     return this.disruptor.getRingBuffer().tryPublishEvent(this.translator, logEvent, asyncLoggerConfig);
/*     */   }
/*     */   
/*     */   private LogEvent ensureImmutable(LogEvent event) {
/* 349 */     LogEvent result = event;
/* 350 */     if (event instanceof RingBufferLogEvent)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 357 */       result = ((RingBufferLogEvent)event).createMemento();
/*     */     }
/* 359 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName, String loggerConfigName) {
/* 370 */     return RingBufferAdmin.forAsyncLoggerConfig(this.disruptor.getRingBuffer(), contextName, loggerConfigName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigDisruptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */