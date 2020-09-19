/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager>
/*     */   extends AbstractAppender
/*     */ {
/*  41 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*  42 */   private final Lock readLock = this.lock.readLock();
/*  43 */   private final Lock writeLock = this.lock.writeLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T manager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDatabaseAppender(String name, Filter filter, boolean ignoreExceptions, T manager) {
/*  58 */     super(name, filter, null, ignoreExceptions);
/*  59 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Layout<LogEvent> getLayout() {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T getManager() {
/*  79 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void start() {
/*  84 */     if (getManager() == null) {
/*  85 */       LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", getName());
/*     */     }
/*  87 */     super.start();
/*  88 */     if (getManager() != null) {
/*  89 */       getManager().startup();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  95 */     setStopping();
/*  96 */     boolean stopped = stop(timeout, timeUnit, false);
/*  97 */     if (getManager() != null) {
/*  98 */       stopped &= getManager().stop(timeout, timeUnit);
/*     */     }
/* 100 */     setStopped();
/* 101 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void append(LogEvent event) {
/* 106 */     this.readLock.lock();
/*     */     try {
/* 108 */       getManager().write(event);
/* 109 */     } catch (LoggingException e) {
/* 110 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), getName(), e);
/*     */       
/* 112 */       throw e;
/* 113 */     } catch (Exception e) {
/* 114 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), getName(), e);
/*     */       
/* 116 */       throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
/*     */     } finally {
/* 118 */       this.readLock.unlock();
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
/*     */   protected final void replaceManager(T manager) {
/* 130 */     this.writeLock.lock();
/*     */     try {
/* 132 */       T old = getManager();
/* 133 */       if (!manager.isRunning()) {
/* 134 */         manager.startup();
/*     */       }
/* 136 */       this.manager = manager;
/* 137 */       old.close();
/*     */     } finally {
/* 139 */       this.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */