/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventTranslatorVararg;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.ReliabilityStrategy;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.message.AsynchronouslyFormattable;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncLogger
/*     */   extends Logger
/*     */   implements EventTranslatorVararg<RingBufferLogEvent>
/*     */ {
/*  73 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  74 */   private static final Clock CLOCK = ClockFactory.getClock();
/*  75 */   private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
/*     */   
/*  77 */   private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
/*     */   
/*  79 */   private final ThreadLocal<RingBufferLogEventTranslator> threadLocalTranslator = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final AsyncLoggerDisruptor loggerDisruptor;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean includeLocation;
/*     */ 
/*     */   
/*     */   private volatile NanoClock nanoClock;
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncLogger(LoggerContext context, String name, MessageFactory messageFactory, AsyncLoggerDisruptor loggerDisruptor) {
/*  95 */     super(context, name, messageFactory);
/*  96 */     this.loggerDisruptor = loggerDisruptor;
/*  97 */     this.includeLocation = this.privateConfig.loggerConfig.isIncludeLocation();
/*  98 */     this.nanoClock = context.getConfiguration().getNanoClock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateConfiguration(Configuration newConfig) {
/* 108 */     this.nanoClock = newConfig.getNanoClock();
/* 109 */     this.includeLocation = newConfig.getLoggerConfig(this.name).isIncludeLocation();
/* 110 */     super.updateConfiguration(newConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   NanoClock getNanoClock() {
/* 115 */     return this.nanoClock;
/*     */   }
/*     */   
/*     */   private RingBufferLogEventTranslator getCachedTranslator() {
/* 119 */     RingBufferLogEventTranslator result = this.threadLocalTranslator.get();
/* 120 */     if (result == null) {
/* 121 */       result = new RingBufferLogEventTranslator();
/* 122 */       this.threadLocalTranslator.set(result);
/*     */     } 
/* 124 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 131 */     if (this.loggerDisruptor.isUseThreadLocals()) {
/* 132 */       logWithThreadLocalTranslator(fqcn, level, marker, message, thrown);
/*     */     } else {
/*     */       
/* 135 */       logWithVarargTranslator(fqcn, level, marker, message, thrown);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isReused(Message message) {
/* 140 */     return message instanceof org.apache.logging.log4j.message.ReusableMessage;
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
/*     */   private void logWithThreadLocalTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 159 */     RingBufferLogEventTranslator translator = getCachedTranslator();
/* 160 */     initTranslator(translator, fqcn, level, marker, message, thrown);
/* 161 */     initTranslatorThreadValues(translator);
/* 162 */     publish(translator);
/*     */   }
/*     */   
/*     */   private void publish(RingBufferLogEventTranslator translator) {
/* 166 */     if (!this.loggerDisruptor.tryPublish(translator)) {
/* 167 */       handleRingBufferFull(translator);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleRingBufferFull(RingBufferLogEventTranslator translator) {
/* 172 */     EventRoute eventRoute = this.loggerDisruptor.getEventRoute(translator.level);
/* 173 */     switch (eventRoute) {
/*     */       case ENQUEUE:
/* 175 */         this.loggerDisruptor.enqueueLogMessageInfo(translator);
/*     */       
/*     */       case SYNCHRONOUS:
/* 178 */         logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
/*     */       
/*     */       case DISCARD:
/*     */         return;
/*     */     } 
/*     */     
/* 184 */     throw new IllegalStateException("Unknown EventRoute " + eventRoute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTranslator(RingBufferLogEventTranslator translator, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 191 */     translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, ThreadContext.getImmutableStack(), calcLocationIfRequested(fqcn), CLOCK.currentTimeMillis(), this.nanoClock.nanoTime());
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
/*     */   private void initTranslatorThreadValues(RingBufferLogEventTranslator translator) {
/* 207 */     if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
/* 208 */       translator.updateThreadValues();
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
/*     */   private StackTraceElement calcLocationIfRequested(String fqcn) {
/* 222 */     return this.includeLocation ? Log4jLogEvent.calcLocation(fqcn) : null;
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
/*     */   private void logWithVarargTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 241 */     Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
/* 242 */     if (disruptor == null) {
/* 243 */       LOGGER.error("Ignoring log event after Log4j has been shut down.");
/*     */       
/*     */       return;
/*     */     } 
/* 247 */     if (!canFormatMessageInBackground(message) && !isReused(message)) {
/* 248 */       message.getFormattedMessage();
/*     */     }
/*     */     
/* 251 */     disruptor.getRingBuffer().publishEvent(this, new Object[] { this, calcLocationIfRequested(fqcn), fqcn, level, marker, message, thrown });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canFormatMessageInBackground(Message message) {
/* 256 */     return (Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class));
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
/*     */   public void translateTo(RingBufferLogEvent event, long sequence, Object... args) {
/* 268 */     AsyncLogger asyncLogger = (AsyncLogger)args[0];
/* 269 */     StackTraceElement location = (StackTraceElement)args[1];
/* 270 */     String fqcn = (String)args[2];
/* 271 */     Level level = (Level)args[3];
/* 272 */     Marker marker = (Marker)args[4];
/* 273 */     Message message = (Message)args[5];
/* 274 */     Throwable thrown = (Throwable)args[6];
/*     */ 
/*     */     
/* 277 */     ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
/*     */     
/* 279 */     Thread currentThread = Thread.currentThread();
/* 280 */     String threadName = THREAD_NAME_CACHING_STRATEGY.getThreadName();
/* 281 */     event.setValues(asyncLogger, asyncLogger.getName(), marker, fqcn, level, message, thrown, CONTEXT_DATA_INJECTOR.injectContextData(null, (StringMap)event.getContextData()), contextStack, currentThread.getId(), threadName, currentThread.getPriority(), location, CLOCK.currentTimeMillis(), this.nanoClock.nanoTime());
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
/*     */   void logMessageInCurrentThread(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 302 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 303 */     strategy.log((Supplier)this, getName(), fqcn, marker, level, message, thrown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actualAsyncLog(RingBufferLogEvent event) {
/* 314 */     List<Property> properties = this.privateConfig.loggerConfig.getPropertyList();
/*     */     
/* 316 */     if (properties != null) {
/* 317 */       StringMap contextData = (StringMap)event.getContextData();
/* 318 */       if (contextData.isFrozen()) {
/* 319 */         StringMap temp = ContextDataFactory.createContextData();
/* 320 */         temp.putAll((ReadOnlyStringMap)contextData);
/* 321 */         contextData = temp;
/*     */       } 
/* 323 */       for (int i = 0; i < properties.size(); i++) {
/* 324 */         Property prop = properties.get(i);
/* 325 */         if (contextData.getValue(prop.getName()) == null) {
/*     */ 
/*     */           
/* 328 */           String value = prop.isValueNeedsLookup() ? this.privateConfig.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
/*     */ 
/*     */           
/* 331 */           contextData.putValue(prop.getName(), value);
/*     */         } 
/* 333 */       }  event.setContextData(contextData);
/*     */     } 
/*     */     
/* 336 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 337 */     strategy.log((Supplier)this, event);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\AsyncLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */