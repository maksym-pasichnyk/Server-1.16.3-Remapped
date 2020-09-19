/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.message.AsynchronouslyFormattable;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*     */ import org.apache.logging.log4j.message.ReusableMessage;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
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
/*     */ public class MutableLogEvent
/*     */   implements LogEvent, ReusableMessage
/*     */ {
/*  43 */   private static final Message EMPTY = (Message)new SimpleMessage("");
/*     */   
/*     */   private int threadPriority;
/*     */   private long threadId;
/*     */   private long timeMillis;
/*     */   private long nanoTime;
/*     */   private short parameterCount;
/*     */   private boolean includeLocation;
/*     */   private boolean endOfBatch = false;
/*     */   private Level level;
/*     */   private String threadName;
/*     */   private String loggerName;
/*     */   private Message message;
/*     */   private StringBuilder messageText;
/*     */   private Object[] parameters;
/*     */   private Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*  60 */   private StringMap contextData = ContextDataFactory.createContextData();
/*     */   private Marker marker;
/*     */   private String loggerFqcn;
/*     */   private StackTraceElement source;
/*     */   private ThreadContext.ContextStack contextStack;
/*     */   transient boolean reserved = false;
/*     */   
/*     */   public MutableLogEvent() {
/*  68 */     this(new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE), new Object[10]);
/*     */   }
/*     */   
/*     */   public MutableLogEvent(StringBuilder msgText, Object[] replacementParameters) {
/*  72 */     this.messageText = msgText;
/*  73 */     this.parameters = replacementParameters;
/*     */   }
/*     */   
/*     */   public Log4jLogEvent toImmutable() {
/*  77 */     return createMemento();
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
/*     */   public void initFrom(LogEvent event) {
/*  91 */     this.loggerFqcn = event.getLoggerFqcn();
/*  92 */     this.marker = event.getMarker();
/*  93 */     this.level = event.getLevel();
/*  94 */     this.loggerName = event.getLoggerName();
/*  95 */     this.timeMillis = event.getTimeMillis();
/*  96 */     this.thrown = event.getThrown();
/*  97 */     this.thrownProxy = event.getThrownProxy();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.contextData.putAll(event.getContextData());
/*     */     
/* 104 */     this.contextStack = event.getContextStack();
/* 105 */     this.source = event.isIncludeLocation() ? event.getSource() : null;
/* 106 */     this.threadId = event.getThreadId();
/* 107 */     this.threadName = event.getThreadName();
/* 108 */     this.threadPriority = event.getThreadPriority();
/* 109 */     this.endOfBatch = event.isEndOfBatch();
/* 110 */     this.includeLocation = event.isIncludeLocation();
/* 111 */     this.nanoTime = event.getNanoTime();
/* 112 */     setMessage(event.getMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 119 */     this.loggerFqcn = null;
/* 120 */     this.marker = null;
/* 121 */     this.level = null;
/* 122 */     this.loggerName = null;
/* 123 */     this.message = null;
/* 124 */     this.thrown = null;
/* 125 */     this.thrownProxy = null;
/* 126 */     this.source = null;
/* 127 */     if (this.contextData != null) {
/* 128 */       if (this.contextData.isFrozen()) {
/* 129 */         this.contextData = null;
/*     */       } else {
/* 131 */         this.contextData.clear();
/*     */       } 
/*     */     }
/* 134 */     this.contextStack = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     trimMessageText();
/* 141 */     if (this.parameters != null) {
/* 142 */       for (int i = 0; i < this.parameters.length; i++) {
/* 143 */         this.parameters[i] = null;
/*     */       }
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
/*     */   private void trimMessageText() {
/* 158 */     if (this.messageText != null && this.messageText.length() > Constants.MAX_REUSABLE_MESSAGE_SIZE) {
/* 159 */       this.messageText.setLength(Constants.MAX_REUSABLE_MESSAGE_SIZE);
/* 160 */       this.messageText.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 166 */     return this.loggerFqcn;
/*     */   }
/*     */   
/*     */   public void setLoggerFqcn(String loggerFqcn) {
/* 170 */     this.loggerFqcn = loggerFqcn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 175 */     return this.marker;
/*     */   }
/*     */   
/*     */   public void setMarker(Marker marker) {
/* 179 */     this.marker = marker;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 184 */     if (this.level == null) {
/* 185 */       this.level = Level.OFF;
/*     */     }
/* 187 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 191 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 196 */     return this.loggerName;
/*     */   }
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/* 200 */     this.loggerName = loggerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 205 */     if (this.message == null) {
/* 206 */       return (this.messageText == null) ? EMPTY : (Message)this;
/*     */     }
/* 208 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(Message msg) {
/* 212 */     if (msg instanceof ReusableMessage) {
/* 213 */       ReusableMessage reusable = (ReusableMessage)msg;
/* 214 */       reusable.formatTo(getMessageTextForWriting());
/* 215 */       if (this.parameters != null) {
/* 216 */         this.parameters = reusable.swapParameters(this.parameters);
/* 217 */         this.parameterCount = reusable.getParameterCount();
/*     */       } 
/*     */     } else {
/*     */       
/* 221 */       if (msg != null && !canFormatMessageInBackground(msg)) {
/* 222 */         msg.getFormattedMessage();
/*     */       }
/* 224 */       this.message = msg;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canFormatMessageInBackground(Message message) {
/* 229 */     return (Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class));
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder getMessageTextForWriting() {
/* 234 */     if (this.messageText == null)
/*     */     {
/*     */       
/* 237 */       this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
/*     */     }
/* 239 */     this.messageText.setLength(0);
/* 240 */     return this.messageText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 248 */     return this.messageText.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 264 */     return (this.parameters == null) ? null : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 272 */     return getThrown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 280 */     buffer.append(this.messageText);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/* 291 */     Object[] result = this.parameters;
/* 292 */     this.parameters = emptyReplacement;
/* 293 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 301 */     return this.parameterCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 306 */     if (this.message != null) {
/* 307 */       return this.message;
/*     */     }
/* 309 */     Object[] params = (this.parameters == null) ? new Object[0] : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/* 310 */     return (Message)new ParameterizedMessage(this.messageText.toString(), params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 315 */     return this.thrown;
/*     */   }
/*     */   
/*     */   public void setThrown(Throwable thrown) {
/* 319 */     this.thrown = thrown;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 324 */     return this.timeMillis;
/*     */   }
/*     */   
/*     */   public void setTimeMillis(long timeMillis) {
/* 328 */     this.timeMillis = timeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 337 */     if (this.thrownProxy == null && this.thrown != null) {
/* 338 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/* 340 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 350 */     if (this.source != null) {
/* 351 */       return this.source;
/*     */     }
/* 353 */     if (this.loggerFqcn == null || !this.includeLocation) {
/* 354 */       return null;
/*     */     }
/* 356 */     this.source = Log4jLogEvent.calcLocation(this.loggerFqcn);
/* 357 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/* 363 */     return (ReadOnlyStringMap)this.contextData;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 368 */     return this.contextData.toMap();
/*     */   }
/*     */   
/*     */   public void setContextData(StringMap mutableContextData) {
/* 372 */     this.contextData = mutableContextData;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 377 */     return this.contextStack;
/*     */   }
/*     */   
/*     */   public void setContextStack(ThreadContext.ContextStack contextStack) {
/* 381 */     this.contextStack = contextStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 386 */     return this.threadId;
/*     */   }
/*     */   
/*     */   public void setThreadId(long threadId) {
/* 390 */     this.threadId = threadId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 395 */     return this.threadName;
/*     */   }
/*     */   
/*     */   public void setThreadName(String threadName) {
/* 399 */     this.threadName = threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 404 */     return this.threadPriority;
/*     */   }
/*     */   
/*     */   public void setThreadPriority(int threadPriority) {
/* 408 */     this.threadPriority = threadPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 413 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 418 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 423 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 428 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 433 */     return this.nanoTime;
/*     */   }
/*     */   
/*     */   public void setNanoTime(long nanoTime) {
/* 437 */     this.nanoTime = nanoTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 445 */     return new Log4jLogEvent.LogEventProxy(this, this.includeLocation);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 449 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jLogEvent createMemento() {
/* 459 */     return Log4jLogEvent.deserialize(Log4jLogEvent.serialize(this, this.includeLocation));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeBuilder(Log4jLogEvent.Builder builder) {
/* 467 */     builder.setContextData(this.contextData).setContextStack(this.contextStack).setEndOfBatch(this.endOfBatch).setIncludeLocation(this.includeLocation).setLevel(getLevel()).setLoggerFqcn(this.loggerFqcn).setLoggerName(this.loggerName).setMarker(this.marker).setMessage(getNonNullImmutableMessage()).setNanoTime(this.nanoTime).setSource(this.source).setThreadId(this.threadId).setThreadName(this.threadName).setThreadPriority(this.threadPriority).setThrown(getThrown()).setThrownProxy(this.thrownProxy).setTimeMillis(this.timeMillis);
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
/*     */   private Message getNonNullImmutableMessage() {
/* 487 */     return (this.message != null) ? this.message : (Message)new SimpleMessage(String.valueOf(this.messageText));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\MutableLogEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */