/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ 
/*     */ public class AwaitCompletionReliabilityStrategy
/*     */   implements ReliabilityStrategy
/*     */ {
/*     */   private static final int MAX_RETRIES = 3;
/*  40 */   private final AtomicInteger counter = new AtomicInteger();
/*  41 */   private final AtomicBoolean shutdown = new AtomicBoolean(false);
/*  42 */   private final Lock shutdownLock = new ReentrantLock();
/*  43 */   private final Condition noLogEvents = this.shutdownLock.newCondition();
/*     */   private final LoggerConfig loggerConfig;
/*     */   
/*     */   public AwaitCompletionReliabilityStrategy(LoggerConfig loggerConfig) {
/*  47 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig is null");
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
/*  61 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  63 */       config.log(loggerName, fqcn, marker, level, data, t);
/*     */     } finally {
/*  65 */       config.getReliabilityStrategy().afterLogEvent();
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
/*  77 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  79 */       config.log(event);
/*     */     } finally {
/*  81 */       config.getReliabilityStrategy().afterLogEvent();
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
/*  94 */     LoggerConfig result = this.loggerConfig;
/*  95 */     if (!beforeLogEvent()) {
/*  96 */       result = (LoggerConfig)next.get();
/*  97 */       return result.getReliabilityStrategy().getActiveLoggerConfig(next);
/*     */     } 
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   private boolean beforeLogEvent() {
/* 103 */     return (this.counter.incrementAndGet() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterLogEvent() {
/* 108 */     if (this.counter.decrementAndGet() == 0 && this.shutdown.get()) {
/* 109 */       signalCompletionIfShutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   private void signalCompletionIfShutdown() {
/* 114 */     Lock lock = this.shutdownLock;
/* 115 */     lock.lock();
/*     */     try {
/* 117 */       this.noLogEvents.signalAll();
/*     */     } finally {
/* 119 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopAppenders() {
/* 130 */     waitForCompletion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void waitForCompletion() {
/* 137 */     this.shutdownLock.lock();
/*     */     try {
/* 139 */       if (this.shutdown.compareAndSet(false, true)) {
/* 140 */         int retries = 0;
/*     */         
/* 142 */         while (!this.counter.compareAndSet(0, -2147483648)) {
/*     */ 
/*     */           
/* 145 */           if (this.counter.get() < 0) {
/*     */             return;
/*     */           }
/*     */           
/*     */           try {
/* 150 */             this.noLogEvents.await((retries + 1), TimeUnit.SECONDS);
/* 151 */           } catch (InterruptedException ie) {
/* 152 */             if (++retries > 3) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 159 */       this.shutdownLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beforeStopConfiguration(Configuration configuration) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\AwaitCompletionReliabilityStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */