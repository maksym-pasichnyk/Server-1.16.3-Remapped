/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TransferQueue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLogEvent;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.ArrayBlockingQueueFactory;
/*     */ import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
/*     */ import org.apache.logging.log4j.core.async.AsyncQueueFullPolicyFactory;
/*     */ import org.apache.logging.log4j.core.async.BlockingQueueFactory;
/*     */ import org.apache.logging.log4j.core.async.DiscardingAsyncQueueFullPolicy;
/*     */ import org.apache.logging.log4j.core.async.EventRoute;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.message.AsynchronouslyFormattable;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Async", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class AsyncAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_QUEUE_SIZE = 128;
/*  64 */   private static final LogEvent SHUTDOWN_LOG_EVENT = (LogEvent)new AbstractLogEvent() {
/*     */     
/*     */     };
/*  67 */   private static final AtomicLong THREAD_SEQUENCE = new AtomicLong(1L);
/*     */   
/*     */   private final BlockingQueue<LogEvent> queue;
/*     */   
/*     */   private final int queueSize;
/*     */   
/*     */   private final boolean blocking;
/*     */   
/*     */   private final long shutdownTimeout;
/*     */   private final Configuration config;
/*     */   private final AppenderRef[] appenderRefs;
/*     */   private final String errorRef;
/*     */   private final boolean includeLocation;
/*     */   private AppenderControl errorAppender;
/*     */   private AsyncThread thread;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/*     */   
/*     */   private AsyncAppender(String name, Filter filter, AppenderRef[] appenderRefs, String errorRef, int queueSize, boolean blocking, boolean ignoreExceptions, long shutdownTimeout, Configuration config, boolean includeLocation, BlockingQueueFactory<LogEvent> blockingQueueFactory) {
/*  85 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions);
/*  86 */     this.queue = blockingQueueFactory.create(queueSize);
/*  87 */     this.queueSize = queueSize;
/*  88 */     this.blocking = blocking;
/*  89 */     this.shutdownTimeout = shutdownTimeout;
/*  90 */     this.config = config;
/*  91 */     this.appenderRefs = appenderRefs;
/*  92 */     this.errorRef = errorRef;
/*  93 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  98 */     Map<String, Appender> map = this.config.getAppenders();
/*  99 */     List<AppenderControl> appenders = new ArrayList<>();
/* 100 */     for (AppenderRef appenderRef : this.appenderRefs) {
/* 101 */       Appender appender = map.get(appenderRef.getRef());
/* 102 */       if (appender != null) {
/* 103 */         appenders.add(new AppenderControl(appender, appenderRef.getLevel(), appenderRef.getFilter()));
/*     */       } else {
/* 105 */         LOGGER.error("No appender named {} was configured", appenderRef);
/*     */       } 
/*     */     } 
/* 108 */     if (this.errorRef != null) {
/* 109 */       Appender appender = map.get(this.errorRef);
/* 110 */       if (appender != null) {
/* 111 */         this.errorAppender = new AppenderControl(appender, null, null);
/*     */       } else {
/* 113 */         LOGGER.error("Unable to set up error Appender. No appender named {} was configured", this.errorRef);
/*     */       } 
/*     */     } 
/* 116 */     if (appenders.size() > 0) {
/* 117 */       this.thread = new AsyncThread(appenders, this.queue);
/* 118 */       this.thread.setName("AsyncAppender-" + getName());
/* 119 */     } else if (this.errorRef == null) {
/* 120 */       throw new ConfigurationException("No appenders are available for AsyncAppender " + getName());
/*     */     } 
/* 122 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/* 124 */     this.thread.start();
/* 125 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 130 */     setStopping();
/* 131 */     stop(timeout, timeUnit, false);
/* 132 */     LOGGER.trace("AsyncAppender stopping. Queue still has {} events.", Integer.valueOf(this.queue.size()));
/* 133 */     this.thread.shutdown();
/*     */     try {
/* 135 */       this.thread.join(this.shutdownTimeout);
/* 136 */     } catch (InterruptedException ex) {
/* 137 */       LOGGER.warn("Interrupted while stopping AsyncAppender {}", getName());
/*     */     } 
/* 139 */     LOGGER.trace("AsyncAppender stopped. Queue has {} events.", Integer.valueOf(this.queue.size()));
/*     */     
/* 141 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 142 */       LOGGER.trace("AsyncAppender: {} discarded {} events.", this.asyncQueueFullPolicy, Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/*     */     
/* 145 */     setStopped();
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent logEvent) {
/* 156 */     if (!isStarted()) {
/* 157 */       throw new IllegalStateException("AsyncAppender " + getName() + " is not active");
/*     */     }
/* 159 */     if (!canFormatMessageInBackground(logEvent.getMessage())) {
/* 160 */       logEvent.getMessage().getFormattedMessage();
/*     */     }
/* 162 */     Log4jLogEvent memento = Log4jLogEvent.createMemento(logEvent, this.includeLocation);
/* 163 */     if (!transfer((LogEvent)memento)) {
/* 164 */       if (this.blocking) {
/*     */         
/* 166 */         EventRoute route = this.asyncQueueFullPolicy.getRoute(this.thread.getId(), memento.getLevel());
/* 167 */         route.logMessage(this, (LogEvent)memento);
/*     */       } else {
/* 169 */         error("Appender " + getName() + " is unable to write primary appenders. queue is full");
/* 170 */         logToErrorAppenderIfNecessary(false, (LogEvent)memento);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canFormatMessageInBackground(Message message) {
/* 176 */     return (Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean transfer(LogEvent memento) {
/* 181 */     return (this.queue instanceof TransferQueue) ? ((TransferQueue<LogEvent>)this.queue).tryTransfer(memento) : this.queue.offer(memento);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessageInCurrentThread(LogEvent logEvent) {
/* 192 */     logEvent.setEndOfBatch(this.queue.isEmpty());
/* 193 */     boolean appendSuccessful = this.thread.callAppenders(logEvent);
/* 194 */     logToErrorAppenderIfNecessary(appendSuccessful, logEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessageInBackgroundThread(LogEvent logEvent) {
/*     */     try {
/* 205 */       this.queue.put(logEvent);
/* 206 */     } catch (InterruptedException e) {
/* 207 */       boolean appendSuccessful = handleInterruptedException(logEvent);
/* 208 */       logToErrorAppenderIfNecessary(appendSuccessful, logEvent);
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
/*     */   private boolean handleInterruptedException(LogEvent memento) {
/* 224 */     boolean appendSuccessful = this.queue.offer(memento);
/* 225 */     if (!appendSuccessful) {
/* 226 */       LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", getName());
/*     */     }
/*     */ 
/*     */     
/* 230 */     Thread.currentThread().interrupt();
/* 231 */     return appendSuccessful;
/*     */   }
/*     */   
/*     */   private void logToErrorAppenderIfNecessary(boolean appendSuccessful, LogEvent logEvent) {
/* 235 */     if (!appendSuccessful && this.errorAppender != null) {
/* 236 */       this.errorAppender.callAppender(logEvent);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static AsyncAppender createAppender(AppenderRef[] appenderRefs, String errorRef, boolean blocking, long shutdownTimeout, int size, String name, boolean includeLocation, Filter filter, Configuration config, boolean ignoreExceptions) {
/* 265 */     if (name == null) {
/* 266 */       LOGGER.error("No name provided for AsyncAppender");
/* 267 */       return null;
/*     */     } 
/* 269 */     if (appenderRefs == null) {
/* 270 */       LOGGER.error("No appender references provided to AsyncAppender {}", name);
/*     */     }
/*     */     
/* 273 */     return new AsyncAppender(name, filter, appenderRefs, errorRef, size, blocking, ignoreExceptions, shutdownTimeout, config, includeLocation, (BlockingQueueFactory<LogEvent>)new ArrayBlockingQueueFactory());
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 279 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<AsyncAppender>
/*     */   {
/*     */     @PluginElement("AppenderRef")
/*     */     @Required(message = "No appender references provided to AsyncAppender")
/*     */     private AppenderRef[] appenderRefs;
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"error-ref"})
/*     */     private String errorRef;
/*     */     @PluginBuilderAttribute
/*     */     private boolean blocking = true;
/*     */     @PluginBuilderAttribute
/* 295 */     private long shutdownTimeout = 0L;
/*     */     
/*     */     @PluginBuilderAttribute
/* 298 */     private int bufferSize = 128;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No name provided for AsyncAppender")
/*     */     private String name;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeLocation = false;
/*     */     
/*     */     @PluginElement("Filter")
/*     */     private Filter filter;
/*     */     
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean ignoreExceptions = true;
/*     */     
/*     */     @PluginElement("BlockingQueueFactory")
/* 317 */     private BlockingQueueFactory<LogEvent> blockingQueueFactory = (BlockingQueueFactory<LogEvent>)new ArrayBlockingQueueFactory();
/*     */ 
/*     */     
/*     */     public Builder setAppenderRefs(AppenderRef[] appenderRefs) {
/* 321 */       this.appenderRefs = appenderRefs;
/* 322 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setErrorRef(String errorRef) {
/* 326 */       this.errorRef = errorRef;
/* 327 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlocking(boolean blocking) {
/* 331 */       this.blocking = blocking;
/* 332 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShutdownTimeout(long shutdownTimeout) {
/* 336 */       this.shutdownTimeout = shutdownTimeout;
/* 337 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBufferSize(int bufferSize) {
/* 341 */       this.bufferSize = bufferSize;
/* 342 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setName(String name) {
/* 346 */       this.name = name;
/* 347 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIncludeLocation(boolean includeLocation) {
/* 351 */       this.includeLocation = includeLocation;
/* 352 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFilter(Filter filter) {
/* 356 */       this.filter = filter;
/* 357 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 361 */       this.configuration = configuration;
/* 362 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIgnoreExceptions(boolean ignoreExceptions) {
/* 366 */       this.ignoreExceptions = ignoreExceptions;
/* 367 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlockingQueueFactory(BlockingQueueFactory<LogEvent> blockingQueueFactory) {
/* 371 */       this.blockingQueueFactory = blockingQueueFactory;
/* 372 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public AsyncAppender build() {
/* 377 */       return new AsyncAppender(this.name, this.filter, this.appenderRefs, this.errorRef, this.bufferSize, this.blocking, this.ignoreExceptions, this.shutdownTimeout, this.configuration, this.includeLocation, this.blockingQueueFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class AsyncThread
/*     */     extends Log4jThread
/*     */   {
/*     */     private volatile boolean shutdown = false;
/*     */     
/*     */     private final List<AppenderControl> appenders;
/*     */     
/*     */     private final BlockingQueue<LogEvent> queue;
/*     */     
/*     */     public AsyncThread(List<AppenderControl> appenders, BlockingQueue<LogEvent> queue) {
/* 392 */       super("AsyncAppender-" + AsyncAppender.THREAD_SEQUENCE.getAndIncrement());
/* 393 */       this.appenders = appenders;
/* 394 */       this.queue = queue;
/* 395 */       setDaemon(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 400 */       while (!this.shutdown) {
/*     */         LogEvent event;
/*     */         try {
/* 403 */           event = this.queue.take();
/* 404 */           if (event == AsyncAppender.SHUTDOWN_LOG_EVENT) {
/* 405 */             this.shutdown = true;
/*     */             continue;
/*     */           } 
/* 408 */         } catch (InterruptedException ex) {
/*     */           break;
/*     */         } 
/* 411 */         event.setEndOfBatch(this.queue.isEmpty());
/* 412 */         boolean success = callAppenders(event);
/* 413 */         if (!success && AsyncAppender.this.errorAppender != null) {
/*     */           try {
/* 415 */             AsyncAppender.this.errorAppender.callAppender(event);
/* 416 */           } catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 422 */       AsyncAppender.LOGGER.trace("AsyncAppender.AsyncThread shutting down. Processing remaining {} queue events.", Integer.valueOf(this.queue.size()));
/*     */       
/* 424 */       int count = 0;
/* 425 */       int ignored = 0;
/* 426 */       while (!this.queue.isEmpty()) {
/*     */         try {
/* 428 */           LogEvent event = this.queue.take();
/* 429 */           if (event instanceof Log4jLogEvent) {
/* 430 */             Log4jLogEvent logEvent = (Log4jLogEvent)event;
/* 431 */             logEvent.setEndOfBatch(this.queue.isEmpty());
/* 432 */             callAppenders((LogEvent)logEvent);
/* 433 */             count++; continue;
/*     */           } 
/* 435 */           ignored++;
/* 436 */           AsyncAppender.LOGGER.trace("Ignoring event of class {}", event.getClass().getName());
/*     */         }
/* 438 */         catch (InterruptedException interruptedException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 443 */       AsyncAppender.LOGGER.trace("AsyncAppender.AsyncThread stopped. Queue has {} events remaining. Processed {} and ignored {} events since shutdown started.", Integer.valueOf(this.queue.size()), Integer.valueOf(count), Integer.valueOf(ignored));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean callAppenders(LogEvent event) {
/* 456 */       boolean success = false;
/* 457 */       for (AppenderControl control : this.appenders) {
/*     */         try {
/* 459 */           control.callAppender(event);
/* 460 */           success = true;
/* 461 */         } catch (Exception exception) {}
/*     */       } 
/*     */ 
/*     */       
/* 465 */       return success;
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 469 */       this.shutdown = true;
/* 470 */       if (this.queue.isEmpty()) {
/* 471 */         this.queue.offer(AsyncAppender.SHUTDOWN_LOG_EVENT);
/*     */       }
/* 473 */       if (getState() == Thread.State.TIMED_WAITING || getState() == Thread.State.WAITING) {
/* 474 */         interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefStrings() {
/* 485 */     String[] result = new String[this.appenderRefs.length];
/* 486 */     for (int i = 0; i < result.length; i++) {
/* 487 */       result[i] = this.appenderRefs[i].getRef();
/*     */     }
/* 489 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 499 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlocking() {
/* 509 */     return this.blocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorRef() {
/* 518 */     return this.errorRef;
/*     */   }
/*     */   
/*     */   public int getQueueCapacity() {
/* 522 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public int getQueueRemainingCapacity() {
/* 526 */     return this.queue.remainingCapacity();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AsyncAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */