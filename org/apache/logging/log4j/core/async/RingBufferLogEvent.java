/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.message.AsynchronouslyFormattable;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*     */ import org.apache.logging.log4j.message.ReusableMessage;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.message.TimestampMessage;
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
/*     */ 
/*     */ 
/*     */ public class RingBufferLogEvent
/*     */   implements LogEvent, ReusableMessage, CharSequence
/*     */ {
/*  50 */   public static final Factory FACTORY = new Factory(); private static final long serialVersionUID = 8462119088943934758L; private int threadPriority;
/*     */   private long threadId;
/*     */   private long currentTimeMillis;
/*  53 */   private static final Message EMPTY = (Message)new SimpleMessage("");
/*     */   private long nanoTime;
/*     */   private short parameterCount;
/*     */   private boolean includeLocation;
/*     */   
/*     */   private static class Factory
/*     */     implements EventFactory<RingBufferLogEvent>
/*     */   {
/*     */     public RingBufferLogEvent newInstance() {
/*  62 */       RingBufferLogEvent result = new RingBufferLogEvent();
/*  63 */       if (Constants.ENABLE_THREADLOCALS) {
/*  64 */         result.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
/*  65 */         result.parameters = new Object[10];
/*     */       } 
/*  67 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private Factory() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean endOfBatch = false;
/*     */   
/*     */   private Level level;
/*     */   
/*     */   private String threadName;
/*     */   private String loggerName;
/*     */   private Message message;
/*     */   private StringBuilder messageText;
/*     */   private Object[] parameters;
/*     */   private transient Throwable thrown;
/*     */   private ThrowableProxy thrownProxy;
/*  86 */   private StringMap contextData = ContextDataFactory.createContextData();
/*     */   
/*     */   private Marker marker;
/*     */   
/*     */   private String fqcn;
/*     */   
/*     */   private StackTraceElement location;
/*     */   
/*     */   private ThreadContext.ContextStack contextStack;
/*     */   
/*     */   private transient AsyncLogger asyncLogger;
/*     */   
/*     */   public void setValues(AsyncLogger anAsyncLogger, String aLoggerName, Marker aMarker, String theFqcn, Level aLevel, Message msg, Throwable aThrowable, StringMap mutableContextData, ThreadContext.ContextStack aContextStack, long threadId, String threadName, int threadPriority, StackTraceElement aLocation, long aCurrentTimeMillis, long aNanoTime) {
/*  99 */     this.threadPriority = threadPriority;
/* 100 */     this.threadId = threadId;
/* 101 */     this.currentTimeMillis = aCurrentTimeMillis;
/* 102 */     this.nanoTime = aNanoTime;
/* 103 */     this.level = aLevel;
/* 104 */     this.threadName = threadName;
/* 105 */     this.loggerName = aLoggerName;
/* 106 */     setMessage(msg);
/* 107 */     this.thrown = aThrowable;
/* 108 */     this.thrownProxy = null;
/* 109 */     this.marker = aMarker;
/* 110 */     this.fqcn = theFqcn;
/* 111 */     this.location = aLocation;
/* 112 */     this.contextData = mutableContextData;
/* 113 */     this.contextStack = aContextStack;
/* 114 */     this.asyncLogger = anAsyncLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public LogEvent toImmutable() {
/* 119 */     return createMemento();
/*     */   }
/*     */   
/*     */   private void setMessage(Message msg) {
/* 123 */     if (msg instanceof ReusableMessage) {
/* 124 */       ReusableMessage reusable = (ReusableMessage)msg;
/* 125 */       reusable.formatTo(getMessageTextForWriting());
/* 126 */       if (this.parameters != null) {
/* 127 */         this.parameters = reusable.swapParameters(this.parameters);
/* 128 */         this.parameterCount = reusable.getParameterCount();
/*     */       } 
/*     */     } else {
/*     */       
/* 132 */       if (msg != null && !canFormatMessageInBackground(msg)) {
/* 133 */         msg.getFormattedMessage();
/*     */       }
/* 135 */       this.message = msg;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canFormatMessageInBackground(Message message) {
/* 140 */     return (Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class));
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder getMessageTextForWriting() {
/* 145 */     if (this.messageText == null)
/*     */     {
/*     */       
/* 148 */       this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
/*     */     }
/* 150 */     this.messageText.setLength(0);
/* 151 */     return this.messageText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(boolean endOfBatch) {
/* 160 */     this.endOfBatch = endOfBatch;
/* 161 */     this.asyncLogger.actualAsyncLog(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 171 */     return this.endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {
/* 176 */     this.endOfBatch = endOfBatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 181 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean includeLocation) {
/* 186 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/* 191 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/* 196 */     return this.marker;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/* 201 */     return this.fqcn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 206 */     if (this.level == null) {
/* 207 */       this.level = Level.OFF;
/*     */     }
/* 209 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/* 214 */     if (this.message == null) {
/* 215 */       return (this.messageText == null) ? EMPTY : (Message)this;
/*     */     }
/* 217 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 225 */     return (this.messageText != null) ? this.messageText.toString() : ((this.message == null) ? null : this.message.getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 243 */     return (this.parameters == null) ? null : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 251 */     return getThrown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 259 */     buffer.append(this.messageText);
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
/* 270 */     Object[] result = this.parameters;
/* 271 */     this.parameters = emptyReplacement;
/* 272 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 280 */     return this.parameterCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 285 */     if (this.message != null) {
/* 286 */       return this.message;
/*     */     }
/* 288 */     Object[] params = (this.parameters == null) ? new Object[0] : Arrays.<Object>copyOf(this.parameters, this.parameterCount);
/* 289 */     return (Message)new ParameterizedMessage(this.messageText.toString(), params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 296 */     return this.messageText.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 301 */     return this.messageText.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 306 */     return this.messageText.subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   private Message getNonNullImmutableMessage() {
/* 311 */     return (this.message != null) ? this.message : (Message)new SimpleMessage(String.valueOf(this.messageText));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 317 */     if (this.thrown == null && 
/* 318 */       this.thrownProxy != null) {
/* 319 */       this.thrown = this.thrownProxy.getThrowable();
/*     */     }
/*     */     
/* 322 */     return this.thrown;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 328 */     if (this.thrownProxy == null && 
/* 329 */       this.thrown != null) {
/* 330 */       this.thrownProxy = new ThrowableProxy(this.thrown);
/*     */     }
/*     */     
/* 333 */     return this.thrownProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/* 339 */     return (ReadOnlyStringMap)this.contextData;
/*     */   }
/*     */   
/*     */   void setContextData(StringMap contextData) {
/* 343 */     this.contextData = contextData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/* 349 */     return this.contextData.toMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/* 354 */     return this.contextStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 359 */     return this.threadId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 364 */     return this.threadName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 369 */     return this.threadPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/* 374 */     return this.location;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 379 */     return (this.message instanceof TimestampMessage) ? ((TimestampMessage)this.message).getTimestamp() : this.currentTimeMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 384 */     return this.nanoTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 391 */     this.asyncLogger = null;
/* 392 */     this.loggerName = null;
/* 393 */     this.marker = null;
/* 394 */     this.fqcn = null;
/* 395 */     this.level = null;
/* 396 */     this.message = null;
/* 397 */     this.thrown = null;
/* 398 */     this.thrownProxy = null;
/* 399 */     this.contextStack = null;
/* 400 */     this.location = null;
/* 401 */     if (this.contextData != null) {
/* 402 */       if (this.contextData.isFrozen()) {
/* 403 */         this.contextData = null;
/*     */       } else {
/* 405 */         this.contextData.clear();
/*     */       } 
/*     */     }
/*     */     
/* 409 */     trimMessageText();
/*     */     
/* 411 */     if (this.parameters != null) {
/* 412 */       for (int i = 0; i < this.parameters.length; i++) {
/* 413 */         this.parameters[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void trimMessageText() {
/* 420 */     if (this.messageText != null && this.messageText.length() > Constants.MAX_REUSABLE_MESSAGE_SIZE) {
/* 421 */       this.messageText.setLength(Constants.MAX_REUSABLE_MESSAGE_SIZE);
/* 422 */       this.messageText.trimToSize();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 427 */     getThrownProxy();
/* 428 */     out.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent createMemento() {
/* 437 */     return (LogEvent)(new Log4jLogEvent.Builder(this)).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeBuilder(Log4jLogEvent.Builder builder) {
/* 446 */     builder.setContextData(this.contextData).setContextStack(this.contextStack).setEndOfBatch(this.endOfBatch).setIncludeLocation(this.includeLocation).setLevel(getLevel()).setLoggerFqcn(this.fqcn).setLoggerName(this.loggerName).setMarker(this.marker).setMessage(getNonNullImmutableMessage()).setNanoTime(this.nanoTime).setSource(this.location).setThreadId(this.threadId).setThreadName(this.threadName).setThreadPriority(this.threadPriority).setThrown(getThrown()).setThrownProxy(this.thrownProxy).setTimeMillis(this.currentTimeMillis);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\RingBufferLogEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */