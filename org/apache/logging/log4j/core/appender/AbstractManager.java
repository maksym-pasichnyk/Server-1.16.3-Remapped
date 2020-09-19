/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractManager
/*     */   implements AutoCloseable
/*     */ {
/*  46 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final Map<String, AbstractManager> MAP = new HashMap<>();
/*     */   
/*  52 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */   
/*     */   protected int count;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final LoggerContext loggerContext;
/*     */ 
/*     */   
/*     */   protected AbstractManager(LoggerContext loggerContext, String name) {
/*  64 */     this.loggerContext = loggerContext;
/*  65 */     this.name = name;
/*  66 */     LOGGER.debug("Starting {} {}", getClass().getSimpleName(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  74 */     stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  78 */     boolean stopped = true;
/*  79 */     LOCK.lock();
/*     */     try {
/*  81 */       this.count--;
/*  82 */       if (this.count <= 0) {
/*  83 */         MAP.remove(this.name);
/*  84 */         LOGGER.debug("Shutting down {} {}", getClass().getSimpleName(), getName());
/*  85 */         stopped = releaseSub(timeout, timeUnit);
/*  86 */         LOGGER.debug("Shut down {} {}, all resources released: {}", getClass().getSimpleName(), getName(), Boolean.valueOf(stopped));
/*     */       } 
/*     */     } finally {
/*  89 */       LOCK.unlock();
/*     */     } 
/*  91 */     return stopped;
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
/*     */   public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data) {
/* 107 */     LOCK.lock();
/*     */     
/*     */     try {
/* 110 */       AbstractManager abstractManager = MAP.get(name);
/* 111 */       if (abstractManager == null) {
/* 112 */         abstractManager = (AbstractManager)factory.createManager(name, data);
/* 113 */         if (abstractManager == null) {
/* 114 */           throw new IllegalStateException("ManagerFactory [" + factory + "] unable to create manager for [" + name + "] with data [" + data + "]");
/*     */         }
/*     */         
/* 117 */         MAP.put(name, abstractManager);
/*     */       } else {
/* 119 */         abstractManager.updateData(data);
/*     */       } 
/* 121 */       abstractManager.count++;
/* 122 */       return (M)abstractManager;
/*     */     } finally {
/* 124 */       LOCK.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateData(Object data) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasManager(String name) {
/* 138 */     LOCK.lock();
/*     */     try {
/* 140 */       return MAP.containsKey(name);
/*     */     } finally {
/* 142 */       LOCK.unlock();
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
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   protected int getCount() {
/* 159 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getLoggerContext() {
/* 170 */     return this.loggerContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void release() {
/* 179 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 187 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 198 */     return new HashMap<>();
/*     */   }
/*     */   
/*     */   protected void log(Level level, String message, Throwable throwable) {
/* 202 */     Message m = LOGGER.getMessageFactory().newMessage("{} {} {}: {}", new Object[] { getClass().getSimpleName(), getName(), message, throwable });
/*     */     
/* 204 */     LOGGER.log(level, m, throwable);
/*     */   }
/*     */   
/*     */   protected void logDebug(String message, Throwable throwable) {
/* 208 */     log(Level.DEBUG, message, throwable);
/*     */   }
/*     */   
/*     */   protected void logError(String message, Throwable throwable) {
/* 212 */     log(Level.ERROR, message, throwable);
/*     */   }
/*     */   
/*     */   protected void logWarn(String message, Throwable throwable) {
/* 216 */     log(Level.WARN, message, throwable);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\AbstractManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */