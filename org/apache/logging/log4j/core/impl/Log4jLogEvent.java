/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.rmi.MarshalledObject;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.RingBufferLogEvent;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.core.util.DummyNanoClock;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.message.LoggerNameAwareMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.ReusableMessage;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public class Log4jLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = -8393305700508709443L;
/*  56 */   private static final Clock CLOCK = ClockFactory.getClock();
/*  57 */   private static volatile NanoClock nanoClock = (NanoClock)new DummyNanoClock();
/*  58 */   private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
/*     */   
/*     */   private final String loggerFqcn;
/*     */   
/*     */   private final Marker marker;
/*     */   private final Level level;
/*     */   private final String loggerName;
/*     */   private Message message;
/*     */   private final long timeMillis;
/*     */   private final transient Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*     */   private final StringMap contextData;
/*     */   private final ThreadContext.ContextStack contextStack;
/*     */   private long threadId;
/*     */   private String threadName;
/*     */   private int threadPriority;
/*     */   private StackTraceElement source;
/*     */   private boolean includeLocation;
/*     */   private boolean endOfBatch = false;
/*     */   private final transient long nanoTime;
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<LogEvent>
/*     */   {
/*     */     private String loggerFqcn;
/*     */     private Marker marker;
/*     */     private Level level;
/*     */     private String loggerName;
/*     */     private Message message;
/*     */     private Throwable thrown;
/*  88 */     private long timeMillis = Log4jLogEvent.CLOCK.currentTimeMillis();
/*     */     private ThrowableProxy thrownProxy;
/*  90 */     private StringMap contextData = Log4jLogEvent.createContextData((List)null);
/*  91 */     private ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
/*     */     
/*     */     private long threadId;
/*     */     
/*     */     private String threadName;
/*     */     
/*     */     private int threadPriority;
/*     */     private StackTraceElement source;
/*     */     private boolean includeLocation;
/*     */     private boolean endOfBatch = false;
/*     */     private long nanoTime;
/*     */     
/*     */     public Builder(LogEvent other) {
/* 104 */       Objects.requireNonNull(other);
/* 105 */       if (other instanceof RingBufferLogEvent) {
/* 106 */         ((RingBufferLogEvent)other).initializeBuilder(this);
/*     */         return;
/*     */       } 
/* 109 */       if (other instanceof MutableLogEvent) {
/* 110 */         ((MutableLogEvent)other).initializeBuilder(this);
/*     */         return;
/*     */       } 
/* 113 */       this.loggerFqcn = other.getLoggerFqcn();
/* 114 */       this.marker = other.getMarker();
/* 115 */       this.level = other.getLevel();
/* 116 */       this.loggerName = other.getLoggerName();
/* 117 */       this.message = other.getMessage();
/* 118 */       this.timeMillis = other.getTimeMillis();
/* 119 */       this.thrown = other.getThrown();
/* 120 */       this.contextStack = other.getContextStack();
/* 121 */       this.includeLocation = other.isIncludeLocation();
/* 122 */       this.endOfBatch = other.isEndOfBatch();
/* 123 */       this.nanoTime = other.getNanoTime();
/*     */ 
/*     */       
/* 126 */       if (other instanceof Log4jLogEvent) {
/* 127 */         Log4jLogEvent evt = (Log4jLogEvent)other;
/* 128 */         this.contextData = evt.contextData;
/* 129 */         this.thrownProxy = evt.thrownProxy;
/* 130 */         this.source = evt.source;
/* 131 */         this.threadId = evt.threadId;
/* 132 */         this.threadName = evt.threadName;
/* 133 */         this.threadPriority = evt.threadPriority;
/*     */       } else {
/* 135 */         if (other.getContextData() instanceof StringMap) {
/* 136 */           this.contextData = (StringMap)other.getContextData();
/*     */         } else {
/* 138 */           if (this.contextData.isFrozen()) {
/* 139 */             this.contextData = ContextDataFactory.createContextData();
/*     */           } else {
/* 141 */             this.contextData.clear();
/*     */           } 
/* 143 */           this.contextData.putAll(other.getContextData());
/*     */         } 
/*     */         
/* 146 */         this.thrownProxy = other.getThrownProxy();
/* 147 */         this.source = other.getSource();
/* 148 */         this.threadId = other.getThreadId();
/* 149 */         this.threadName = other.getThreadName();
/* 150 */         this.threadPriority = other.getThreadPriority();
/*     */       } 
/*     */     }
/*     */     
/*     */     public Builder setLevel(Level level) {
/* 155 */       this.level = level;
/* 156 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLoggerFqcn(String loggerFqcn) {
/* 160 */       this.loggerFqcn = loggerFqcn;
/* 161 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLoggerName(String loggerName) {
/* 165 */       this.loggerName = loggerName;
/* 166 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMarker(Marker marker) {
/* 170 */       this.marker = marker;
/* 171 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMessage(Message message) {
/* 175 */       this.message = message;
/* 176 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setThrown(Throwable thrown) {
/* 180 */       this.thrown = thrown;
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTimeMillis(long timeMillis) {
/* 185 */       this.timeMillis = timeMillis;
/* 186 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setThrownProxy(ThrowableProxy thrownProxy) {
/* 190 */       this.thrownProxy = thrownProxy;
/* 191 */       return this;
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Builder setContextMap(Map<String, String> contextMap) {
/* 196 */       this.contextData = ContextDataFactory.createContextData();
/* 197 */       if (contextMap != null) {
/* 198 */         for (Map.Entry<String, String> entry : contextMap.entrySet()) {
/* 199 */           this.contextData.putValue(entry.getKey(), entry.getValue());
/*     */         }
/*     */       }
/* 202 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setContextData(StringMap contextData) {
/* 206 */       this.contextData = contextData;
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setContextStack(ThreadContext.ContextStack contextStack) {
/* 211 */       this.contextStack = contextStack;
/* 212 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setThreadId(long threadId) {
/* 216 */       this.threadId = threadId;
/* 217 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setThreadName(String threadName) {
/* 221 */       this.threadName = threadName;
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setThreadPriority(int threadPriority) {
/* 226 */       this.threadPriority = threadPriority;
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSource(StackTraceElement source) {
/* 231 */       this.source = source;
/* 232 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIncludeLocation(boolean includeLocation) {
/* 236 */       this.includeLocation = includeLocation;
/* 237 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setEndOfBatch(boolean endOfBatch) {
/* 241 */       this.endOfBatch = endOfBatch;
/* 242 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setNanoTime(long nanoTime) {
/* 252 */       this.nanoTime = nanoTime;
/* 253 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Log4jLogEvent build() {
/* 258 */       Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFqcn, this.level, this.message, this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.timeMillis, this.nanoTime);
/*     */ 
/*     */       
/* 261 */       result.setIncludeLocation(this.includeLocation);
/* 262 */       result.setEndOfBatch(this.endOfBatch);
/* 263 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static Builder newBuilder() {
/* 272 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Log4jLogEvent() {
/* 276 */     this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, CLOCK.currentTimeMillis(), nanoClock.nanoTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Log4jLogEvent(long timestamp) {
/* 286 */     this("", null, "", null, null, (Throwable)null, null, null, null, 0L, null, 0, null, timestamp, nanoClock.nanoTime());
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
/*     */   @Deprecated
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t) {
/* 303 */     this(loggerName, marker, loggerFQCN, level, message, null, t);
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
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, List<Property> properties, Throwable t) {
/* 319 */     this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(properties), (ThreadContext.getDepth() == 0) ? null : ThreadContext.cloneStack(), 0L, null, 0, null, (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : CLOCK.currentTimeMillis(), nanoClock.nanoTime());
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable t, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestampMillis) {
/* 350 */     this(loggerName, marker, loggerFQCN, level, message, t, null, createContextData(mdc), ndc, 0L, threadName, 0, location, timestampMillis, nanoClock.nanoTime());
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
/*     */   public static Log4jLogEvent createEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, Map<String, String> mdc, ThreadContext.ContextStack ndc, String threadName, StackTraceElement location, long timestamp) {
/* 378 */     Log4jLogEvent result = new Log4jLogEvent(loggerName, marker, loggerFQCN, level, message, thrown, thrownProxy, createContextData(mdc), ndc, 0L, threadName, 0, location, timestamp, nanoClock.nanoTime());
/*     */     
/* 380 */     return result;
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
/*     */   private Log4jLogEvent(String loggerName, Marker marker, String loggerFQCN, Level level, Message message, Throwable thrown, ThrowableProxy thrownProxy, StringMap contextData, ThreadContext.ContextStack contextStack, long threadId, String threadName, int threadPriority, StackTraceElement source, long timestampMillis, long nanoTime) {
/* 407 */     this.loggerName = loggerName;
/* 408 */     this.marker = marker;
/* 409 */     this.loggerFqcn = loggerFQCN;
/* 410 */     this.level = (level == null) ? Level.OFF : level;
/* 411 */     this.message = message;
/* 412 */     this.thrown = thrown;
/* 413 */     this.thrownProxy = thrownProxy;
/* 414 */     this.contextData = (contextData == null) ? ContextDataFactory.createContextData() : contextData;
/* 415 */     this.contextStack = (contextStack == null) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : contextStack;
/* 416 */     this.timeMillis = (message instanceof TimestampMessage) ? ((TimestampMessage)message).getTimestamp() : timestampMillis;
/*     */ 
/*     */     
/* 419 */     this.threadId = threadId;
/* 420 */     this.threadName = threadName;
/* 421 */     this.threadPriority = threadPriority;
/* 422 */     this.source = source;
/* 423 */     if (message != null && message instanceof LoggerNameAwareMessage) {
/* 424 */       ((LoggerNameAwareMessage)message).setLoggerName(loggerName);
/*     */     }
/* 426 */     this.nanoTime = nanoTime;
/*     */   }
/*     */   
/*     */   private static StringMap createContextData(Map<String, String> contextMap) {
/* 430 */     StringMap result = ContextDataFactory.createContextData();
/* 431 */     if (contextMap != null) {
/* 432 */       for (Map.Entry<String, String> entry : contextMap.entrySet()) {
/* 433 */         result.putValue(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 436 */     return result;
/*     */   }
/*     */   
/*     */   private static StringMap createContextData(List<Property> properties) {
/* 440 */     StringMap reusable = ContextDataFactory.createContextData();
/* 441 */     return CONTEXT_DATA_INJECTOR.injectContextData(properties, reusable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NanoClock getNanoClock() {
/* 449 */     return nanoClock;
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
/*     */   public static void setNanoClock(NanoClock nanoClock) {
/* 461 */     Log4jLogEvent.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "NanoClock must be non-null");
/* 462 */     StatusLogger.getLogger().trace("Using {} for nanosecond timestamps.", nanoClock.getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder asBuilder() {
/* 470 */     return new Builder(this);
/*     */   }
/*     */   
/*     */   public Log4jLogEvent toImmutable() {
/* 474 */     if (getMessage() instanceof ReusableMessage) {
/* 475 */       makeMessageImmutable();
/*     */     }
/* 477 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 486 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 495 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 504 */     return this.message;
/*     */   }
/*     */   
/*     */   public void makeMessageImmutable() {
/* 508 */     this.message = (Message)new SimpleMessage(this.message.getFormattedMessage());
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 513 */     if (this.threadId == 0L) {
/* 514 */       this.threadId = Thread.currentThread().getId();
/*     */     }
/* 516 */     return this.threadId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 525 */     if (this.threadName == null) {
/* 526 */       this.threadName = Thread.currentThread().getName();
/*     */     }
/* 528 */     return this.threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 533 */     if (this.threadPriority == 0) {
/* 534 */       this.threadPriority = Thread.currentThread().getPriority();
/*     */     }
/* 536 */     return this.threadPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 545 */     return this.timeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 554 */     return this.thrown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 563 */     if (this.thrownProxy == null && this.thrown != null) {
/* 564 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/* 566 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 576 */     return this.marker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 585 */     return this.loggerFqcn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/* 595 */     return (ReadOnlyStringMap)this.contextData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 603 */     return this.contextData.toMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 612 */     return this.contextStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 622 */     if (this.source != null) {
/* 623 */       return this.source;
/*     */     }
/* 625 */     if (this.loggerFqcn == null || !this.includeLocation) {
/* 626 */       return null;
/*     */     }
/* 628 */     this.source = calcLocation(this.loggerFqcn);
/* 629 */     return this.source;
/*     */   }
/*     */   
/*     */   public static StackTraceElement calcLocation(String fqcnOfLogger) {
/* 633 */     if (fqcnOfLogger == null) {
/* 634 */       return null;
/*     */     }
/*     */     
/* 637 */     StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
/* 638 */     StackTraceElement last = null;
/* 639 */     for (int i = stackTrace.length - 1; i > 0; i--) {
/* 640 */       String className = stackTrace[i].getClassName();
/* 641 */       if (fqcnOfLogger.equals(className)) {
/* 642 */         return last;
/*     */       }
/* 644 */       last = stackTrace[i];
/*     */     } 
/* 646 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 651 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 656 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 661 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 666 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 671 */     return this.nanoTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 679 */     getThrownProxy();
/* 680 */     return new LogEventProxy(this, this.includeLocation);
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
/*     */   public static Serializable serialize(LogEvent event, boolean includeLocation) {
/* 693 */     if (event instanceof Log4jLogEvent) {
/* 694 */       event.getThrownProxy();
/* 695 */       return new LogEventProxy((Log4jLogEvent)event, includeLocation);
/*     */     } 
/* 697 */     return new LogEventProxy(event, includeLocation);
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
/*     */   public static Serializable serialize(Log4jLogEvent event, boolean includeLocation) {
/* 710 */     event.getThrownProxy();
/* 711 */     return new LogEventProxy(event, includeLocation);
/*     */   }
/*     */   
/*     */   public static boolean canDeserialize(Serializable event) {
/* 715 */     return event instanceof LogEventProxy;
/*     */   }
/*     */   
/*     */   public static Log4jLogEvent deserialize(Serializable event) {
/* 719 */     Objects.requireNonNull(event, "Event cannot be null");
/* 720 */     if (event instanceof LogEventProxy) {
/* 721 */       LogEventProxy proxy = (LogEventProxy)event;
/* 722 */       Log4jLogEvent result = new Log4jLogEvent(proxy.loggerName, proxy.marker, proxy.loggerFQCN, proxy.level, proxy.message, proxy.thrown, proxy.thrownProxy, proxy.contextData, proxy.contextStack, proxy.threadId, proxy.threadName, proxy.threadPriority, proxy.source, proxy.timeMillis, proxy.nanoTime);
/*     */ 
/*     */ 
/*     */       
/* 726 */       result.setEndOfBatch(proxy.isEndOfBatch);
/* 727 */       result.setIncludeLocation(proxy.isLocationRequired);
/* 728 */       return result;
/*     */     } 
/* 730 */     throw new IllegalArgumentException("Event is not a serialized LogEvent: " + event.toString());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 734 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */   
/*     */   public LogEvent createMemento() {
/* 738 */     return createMemento(this);
/*     */   }
/*     */   
/*     */   public static LogEvent createMemento(LogEvent logEvent) {
/* 742 */     return (new Builder(logEvent)).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Log4jLogEvent createMemento(LogEvent event, boolean includeLocation) {
/* 751 */     return deserialize(serialize(event, includeLocation));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 756 */     StringBuilder sb = new StringBuilder();
/* 757 */     String n = this.loggerName.isEmpty() ? "root" : this.loggerName;
/* 758 */     sb.append("Logger=").append(n);
/* 759 */     sb.append(" Level=").append(this.level.name());
/* 760 */     sb.append(" Message=").append((this.message == null) ? null : this.message.getFormattedMessage());
/* 761 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 766 */     if (this == o) {
/* 767 */       return true;
/*     */     }
/* 769 */     if (o == null || getClass() != o.getClass()) {
/* 770 */       return false;
/*     */     }
/*     */     
/* 773 */     Log4jLogEvent that = (Log4jLogEvent)o;
/*     */     
/* 775 */     if (this.endOfBatch != that.endOfBatch) {
/* 776 */       return false;
/*     */     }
/* 778 */     if (this.includeLocation != that.includeLocation) {
/* 779 */       return false;
/*     */     }
/* 781 */     if (this.timeMillis != that.timeMillis) {
/* 782 */       return false;
/*     */     }
/* 784 */     if (this.nanoTime != that.nanoTime) {
/* 785 */       return false;
/*     */     }
/* 787 */     if ((this.loggerFqcn != null) ? !this.loggerFqcn.equals(that.loggerFqcn) : (that.loggerFqcn != null)) {
/* 788 */       return false;
/*     */     }
/* 790 */     if ((this.level != null) ? !this.level.equals(that.level) : (that.level != null)) {
/* 791 */       return false;
/*     */     }
/* 793 */     if ((this.source != null) ? !this.source.equals(that.source) : (that.source != null)) {
/* 794 */       return false;
/*     */     }
/* 796 */     if ((this.marker != null) ? !this.marker.equals(that.marker) : (that.marker != null)) {
/* 797 */       return false;
/*     */     }
/* 799 */     if ((this.contextData != null) ? !this.contextData.equals(that.contextData) : (that.contextData != null)) {
/* 800 */       return false;
/*     */     }
/* 802 */     if (!this.message.equals(that.message)) {
/* 803 */       return false;
/*     */     }
/* 805 */     if (!this.loggerName.equals(that.loggerName)) {
/* 806 */       return false;
/*     */     }
/* 808 */     if ((this.contextStack != null) ? !this.contextStack.equals(that.contextStack) : (that.contextStack != null)) {
/* 809 */       return false;
/*     */     }
/* 811 */     if (this.threadId != that.threadId) {
/* 812 */       return false;
/*     */     }
/* 814 */     if ((this.threadName != null) ? !this.threadName.equals(that.threadName) : (that.threadName != null)) {
/* 815 */       return false;
/*     */     }
/* 817 */     if (this.threadPriority != that.threadPriority) {
/* 818 */       return false;
/*     */     }
/* 820 */     if ((this.thrown != null) ? !this.thrown.equals(that.thrown) : (that.thrown != null)) {
/* 821 */       return false;
/*     */     }
/* 823 */     if ((this.thrownProxy != null) ? !this.thrownProxy.equals(that.thrownProxy) : (that.thrownProxy != null)) {
/* 824 */       return false;
/*     */     }
/*     */     
/* 827 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 833 */     int result = (this.loggerFqcn != null) ? this.loggerFqcn.hashCode() : 0;
/* 834 */     result = 31 * result + ((this.marker != null) ? this.marker.hashCode() : 0);
/* 835 */     result = 31 * result + ((this.level != null) ? this.level.hashCode() : 0);
/* 836 */     result = 31 * result + this.loggerName.hashCode();
/* 837 */     result = 31 * result + this.message.hashCode();
/* 838 */     result = 31 * result + (int)(this.timeMillis ^ this.timeMillis >>> 32L);
/* 839 */     result = 31 * result + (int)(this.nanoTime ^ this.nanoTime >>> 32L);
/* 840 */     result = 31 * result + ((this.thrown != null) ? this.thrown.hashCode() : 0);
/* 841 */     result = 31 * result + ((this.thrownProxy != null) ? this.thrownProxy.hashCode() : 0);
/* 842 */     result = 31 * result + ((this.contextData != null) ? this.contextData.hashCode() : 0);
/* 843 */     result = 31 * result + ((this.contextStack != null) ? this.contextStack.hashCode() : 0);
/* 844 */     result = 31 * result + (int)(this.threadId ^ this.threadId >>> 32L);
/* 845 */     result = 31 * result + ((this.threadName != null) ? this.threadName.hashCode() : 0);
/* 846 */     result = 31 * result + (this.threadPriority ^ this.threadPriority >>> 32);
/* 847 */     result = 31 * result + ((this.source != null) ? this.source.hashCode() : 0);
/* 848 */     result = 31 * result + (this.includeLocation ? 1 : 0);
/* 849 */     result = 31 * result + (this.endOfBatch ? 1 : 0);
/*     */     
/* 851 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   static class LogEventProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -8634075037355293699L;
/*     */     
/*     */     private final String loggerFQCN;
/*     */     
/*     */     private final Marker marker;
/*     */     
/*     */     private final Level level;
/*     */     
/*     */     private final String loggerName;
/*     */     
/*     */     private final transient Message message;
/*     */     
/*     */     private MarshalledObject<Message> marshalledMessage;
/*     */     
/*     */     private String messageString;
/*     */     
/*     */     private final long timeMillis;
/*     */     private final transient Throwable thrown;
/*     */     private final ThrowableProxy thrownProxy;
/*     */     private final StringMap contextData;
/*     */     private final ThreadContext.ContextStack contextStack;
/*     */     private final long threadId;
/*     */     private final String threadName;
/*     */     private final int threadPriority;
/*     */     private final StackTraceElement source;
/*     */     private final boolean isLocationRequired;
/*     */     private final boolean isEndOfBatch;
/*     */     private final transient long nanoTime;
/*     */     
/*     */     public LogEventProxy(Log4jLogEvent event, boolean includeLocation) {
/* 888 */       this.loggerFQCN = event.loggerFqcn;
/* 889 */       this.marker = event.marker;
/* 890 */       this.level = event.level;
/* 891 */       this.loggerName = event.loggerName;
/* 892 */       this.message = (event.message instanceof ReusableMessage) ? memento((ReusableMessage)event.message) : event.message;
/*     */ 
/*     */       
/* 895 */       this.timeMillis = event.timeMillis;
/* 896 */       this.thrown = event.thrown;
/* 897 */       this.thrownProxy = event.thrownProxy;
/* 898 */       this.contextData = event.contextData;
/* 899 */       this.contextStack = event.contextStack;
/* 900 */       this.source = includeLocation ? event.getSource() : null;
/* 901 */       this.threadId = event.getThreadId();
/* 902 */       this.threadName = event.getThreadName();
/* 903 */       this.threadPriority = event.getThreadPriority();
/* 904 */       this.isLocationRequired = includeLocation;
/* 905 */       this.isEndOfBatch = event.endOfBatch;
/* 906 */       this.nanoTime = event.nanoTime;
/*     */     }
/*     */     
/*     */     public LogEventProxy(LogEvent event, boolean includeLocation) {
/* 910 */       this.loggerFQCN = event.getLoggerFqcn();
/* 911 */       this.marker = event.getMarker();
/* 912 */       this.level = event.getLevel();
/* 913 */       this.loggerName = event.getLoggerName();
/*     */       
/* 915 */       Message temp = event.getMessage();
/* 916 */       this.message = (temp instanceof ReusableMessage) ? memento((ReusableMessage)temp) : temp;
/*     */ 
/*     */       
/* 919 */       this.timeMillis = event.getTimeMillis();
/* 920 */       this.thrown = event.getThrown();
/* 921 */       this.thrownProxy = event.getThrownProxy();
/* 922 */       this.contextData = memento(event.getContextData());
/* 923 */       this.contextStack = event.getContextStack();
/* 924 */       this.source = includeLocation ? event.getSource() : null;
/* 925 */       this.threadId = event.getThreadId();
/* 926 */       this.threadName = event.getThreadName();
/* 927 */       this.threadPriority = event.getThreadPriority();
/* 928 */       this.isLocationRequired = includeLocation;
/* 929 */       this.isEndOfBatch = event.isEndOfBatch();
/* 930 */       this.nanoTime = event.getNanoTime();
/*     */     }
/*     */     
/*     */     private static Message memento(ReusableMessage message) {
/* 934 */       return message.memento();
/*     */     }
/*     */     
/*     */     private static StringMap memento(ReadOnlyStringMap data) {
/* 938 */       StringMap result = ContextDataFactory.createContextData();
/* 939 */       result.putAll(data);
/* 940 */       return result;
/*     */     }
/*     */     
/*     */     private static MarshalledObject<Message> marshall(Message msg) {
/*     */       try {
/* 945 */         return new MarshalledObject<>(msg);
/* 946 */       } catch (Exception ex) {
/* 947 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 952 */       this.messageString = this.message.getFormattedMessage();
/* 953 */       this.marshalledMessage = marshall(this.message);
/* 954 */       s.defaultWriteObject();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 962 */       Log4jLogEvent result = new Log4jLogEvent(this.loggerName, this.marker, this.loggerFQCN, this.level, message(), this.thrown, this.thrownProxy, this.contextData, this.contextStack, this.threadId, this.threadName, this.threadPriority, this.source, this.timeMillis, this.nanoTime);
/*     */ 
/*     */       
/* 965 */       result.setEndOfBatch(this.isEndOfBatch);
/* 966 */       result.setIncludeLocation(this.isLocationRequired);
/* 967 */       return result;
/*     */     }
/*     */     
/*     */     private Message message() {
/* 971 */       if (this.marshalledMessage != null) {
/*     */         try {
/* 973 */           return this.marshalledMessage.get();
/* 974 */         } catch (Exception exception) {}
/*     */       }
/*     */       
/* 977 */       return (Message)new SimpleMessage(this.messageString);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\Log4jLogEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */