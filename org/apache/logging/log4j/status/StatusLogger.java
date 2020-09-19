/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
/*     */ import org.apache.logging.log4j.simple.SimpleLogger;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StatusLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
/*     */   public static final String DEFAULT_STATUS_LISTENER_LEVEL = "log4j2.StatusLogger.level";
/*     */   private static final long serialVersionUID = 2L;
/*     */   private static final String NOT_AVAIL = "?";
/*  70 */   private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
/*     */   
/*  72 */   private static final int MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
/*     */   
/*  74 */   private static final String DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
/*     */ 
/*     */   
/*  77 */   private static final StatusLogger STATUS_LOGGER = new StatusLogger(StatusLogger.class.getName(), (MessageFactory)ParameterizedNoReferenceMessageFactory.INSTANCE);
/*     */ 
/*     */   
/*     */   private final SimpleLogger logger;
/*     */   
/*  82 */   private final Collection<StatusListener> listeners = new CopyOnWriteArrayList<>();
/*     */   
/*  84 */   private final ReadWriteLock listenersLock = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */   
/*  88 */   private final Queue<StatusData> messages = new BoundedQueue<>(MAX_ENTRIES);
/*     */   
/*  90 */   private final Lock msgLock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private int listenersLevel;
/*     */ 
/*     */   
/*     */   private StatusLogger(String name, MessageFactory messageFactory) {
/*  97 */     super(name, messageFactory);
/*  98 */     this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", messageFactory, PROPS, System.err);
/*     */     
/* 100 */     this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StatusLogger getLogger() {
/* 109 */     return STATUS_LOGGER;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 113 */     this.logger.setLevel(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerListener(StatusListener listener) {
/* 122 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 124 */       this.listeners.add(listener);
/* 125 */       Level lvl = listener.getStatusLevel();
/* 126 */       if (this.listenersLevel < lvl.intLevel()) {
/* 127 */         this.listenersLevel = lvl.intLevel();
/*     */       }
/*     */     } finally {
/* 130 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(StatusListener listener) {
/* 140 */     closeSilently(listener);
/* 141 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 143 */       this.listeners.remove(listener);
/* 144 */       int lowest = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
/* 145 */       for (StatusListener statusListener : this.listeners) {
/* 146 */         int level = statusListener.getStatusLevel().intLevel();
/* 147 */         if (lowest < level) {
/* 148 */           lowest = level;
/*     */         }
/*     */       } 
/* 151 */       this.listenersLevel = lowest;
/*     */     } finally {
/* 153 */       this.listenersLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateListenerLevel(Level status) {
/* 158 */     if (status.intLevel() > this.listenersLevel) {
/* 159 */       this.listenersLevel = status.intLevel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<StatusListener> getListeners() {
/* 169 */     return this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 176 */     this.listenersLock.writeLock().lock();
/*     */     try {
/* 178 */       for (StatusListener listener : this.listeners) {
/* 179 */         closeSilently(listener);
/*     */       }
/*     */     } finally {
/* 182 */       this.listeners.clear();
/* 183 */       this.listenersLock.writeLock().unlock();
/*     */       
/* 185 */       clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeSilently(Closeable resource) {
/*     */     try {
/* 191 */       resource.close();
/* 192 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<StatusData> getStatusData() {
/* 203 */     this.msgLock.lock();
/*     */     try {
/* 205 */       return new ArrayList<>(this.messages);
/*     */     } finally {
/* 207 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 215 */     this.msgLock.lock();
/*     */     try {
/* 217 */       this.messages.clear();
/*     */     } finally {
/* 219 */       this.msgLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 225 */     return this.logger.getLevel();
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
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message msg, Throwable t) {
/* 240 */     StackTraceElement element = null;
/* 241 */     if (fqcn != null) {
/* 242 */       element = getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
/*     */     }
/* 244 */     StatusData data = new StatusData(element, level, msg, t, null);
/* 245 */     this.msgLock.lock();
/*     */     try {
/* 247 */       this.messages.add(data);
/*     */     } finally {
/* 249 */       this.msgLock.unlock();
/*     */     } 
/* 251 */     if (this.listeners.size() > 0) {
/* 252 */       for (StatusListener listener : this.listeners) {
/* 253 */         if (data.getLevel().isMoreSpecificThan(listener.getStatusLevel())) {
/* 254 */           listener.log(data);
/*     */         }
/*     */       } 
/*     */     } else {
/* 258 */       this.logger.logMessage(fqcn, level, marker, msg, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StackTraceElement getStackTraceElement(String fqcn, StackTraceElement[] stackTrace) {
/* 263 */     if (fqcn == null) {
/* 264 */       return null;
/*     */     }
/* 266 */     boolean next = false;
/* 267 */     for (StackTraceElement element : stackTrace) {
/* 268 */       String className = element.getClassName();
/* 269 */       if (next && !fqcn.equals(className)) {
/* 270 */         return element;
/*     */       }
/* 272 */       if (fqcn.equals(className)) {
/* 273 */         next = true;
/* 274 */       } else if ("?".equals(className)) {
/*     */         break;
/*     */       } 
/*     */     } 
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 283 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 288 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 293 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 298 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 304 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 310 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 316 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 323 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 330 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 337 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 345 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 353 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 361 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/* 366 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 371 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 376 */     return isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker) {
/* 381 */     if (this.listeners.size() > 0) {
/* 382 */       return (this.listenersLevel >= level.intLevel());
/*     */     }
/* 384 */     return this.logger.isEnabled(level, marker);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class BoundedQueue<E>
/*     */     extends ConcurrentLinkedQueue<E>
/*     */   {
/*     */     private static final long serialVersionUID = -3945953719763255337L;
/*     */ 
/*     */     
/*     */     private final int size;
/*     */ 
/*     */     
/*     */     BoundedQueue(int size) {
/* 399 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(E object) {
/* 404 */       super.add(object);
/* 405 */       while (StatusLogger.this.messages.size() > this.size) {
/* 406 */         StatusLogger.this.messages.poll();
/*     */       }
/* 408 */       return (this.size > 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\status\StatusLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */