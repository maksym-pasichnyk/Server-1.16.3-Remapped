/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ public class LockingReliabilityStrategy
/*     */   implements ReliabilityStrategy
/*     */ {
/*     */   private final LoggerConfig loggerConfig;
/*  35 */   private final ReadWriteLock reconfigureLock = new ReentrantReadWriteLock();
/*     */   private volatile boolean isStopping = false;
/*     */   
/*     */   public LockingReliabilityStrategy(LoggerConfig loggerConfig) {
/*  39 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig was null");
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
/*     */   public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/*  53 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  55 */       config.log(loggerName, fqcn, marker, level, data, t);
/*     */     } finally {
/*  57 */       config.getReliabilityStrategy().afterLogEvent();
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
/*     */   public void log(Supplier<LoggerConfig> reconfigured, LogEvent event) {
/*  69 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  71 */       config.log(event);
/*     */     } finally {
/*  73 */       config.getReliabilityStrategy().afterLogEvent();
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
/*     */   public LoggerConfig getActiveLoggerConfig(Supplier<LoggerConfig> next) {
/*  86 */     LoggerConfig result = this.loggerConfig;
/*  87 */     if (!beforeLogEvent()) {
/*  88 */       result = (LoggerConfig)next.get();
/*  89 */       return result.getReliabilityStrategy().getActiveLoggerConfig(next);
/*     */     } 
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   private boolean beforeLogEvent() {
/*  95 */     this.reconfigureLock.readLock().lock();
/*  96 */     if (this.isStopping) {
/*  97 */       this.reconfigureLock.readLock().unlock();
/*  98 */       return false;
/*     */     } 
/* 100 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterLogEvent() {
/* 105 */     this.reconfigureLock.readLock().unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopAppenders() {
/* 115 */     this.reconfigureLock.writeLock().lock();
/*     */     try {
/* 117 */       this.isStopping = true;
/*     */     } finally {
/* 119 */       this.reconfigureLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beforeStopConfiguration(Configuration configuration) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\LockingReliabilityStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */