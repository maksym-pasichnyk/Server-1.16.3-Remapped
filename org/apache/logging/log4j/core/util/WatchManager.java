/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
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
/*     */ public class WatchManager
/*     */   extends AbstractLifeCycle
/*     */ {
/*  37 */   private static Logger logger = (Logger)StatusLogger.getLogger();
/*  38 */   private final ConcurrentMap<File, FileMonitor> watchers = new ConcurrentHashMap<>();
/*  39 */   private int intervalSeconds = 0;
/*     */   private ScheduledFuture<?> future;
/*     */   private final ConfigurationScheduler scheduler;
/*     */   
/*     */   public WatchManager(ConfigurationScheduler scheduler) {
/*  44 */     this.scheduler = scheduler;
/*     */   }
/*     */   
/*     */   public void setIntervalSeconds(int intervalSeconds) {
/*  48 */     if (!isStarted()) {
/*  49 */       if (this.intervalSeconds > 0 && intervalSeconds == 0) {
/*  50 */         this.scheduler.decrementScheduledItems();
/*  51 */       } else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
/*  52 */         this.scheduler.incrementScheduledItems();
/*     */       } 
/*  54 */       this.intervalSeconds = intervalSeconds;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getIntervalSeconds() {
/*  59 */     return this.intervalSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  64 */     super.start();
/*  65 */     if (this.intervalSeconds > 0) {
/*  66 */       this.future = this.scheduler.scheduleWithFixedDelay(new WatchRunnable(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  73 */     setStopping();
/*  74 */     boolean stopped = stop(this.future);
/*  75 */     setStopped();
/*  76 */     return stopped;
/*     */   }
/*     */   
/*     */   public void watchFile(File file, FileWatcher watcher) {
/*  80 */     this.watchers.put(file, new FileMonitor(file.lastModified(), watcher));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<File, FileWatcher> getWatchers() {
/*  85 */     Map<File, FileWatcher> map = new HashMap<>();
/*  86 */     for (Map.Entry<File, FileMonitor> entry : this.watchers.entrySet()) {
/*  87 */       map.put(entry.getKey(), (entry.getValue()).fileWatcher);
/*     */     }
/*  89 */     return map;
/*     */   }
/*     */   
/*     */   private class WatchRunnable implements Runnable {
/*     */     private WatchRunnable() {}
/*     */     
/*     */     public void run() {
/*  96 */       for (Map.Entry<File, WatchManager.FileMonitor> entry : (Iterable<Map.Entry<File, WatchManager.FileMonitor>>)WatchManager.this.watchers.entrySet()) {
/*  97 */         File file = entry.getKey();
/*  98 */         WatchManager.FileMonitor fileMonitor = entry.getValue();
/*  99 */         long lastModfied = file.lastModified();
/* 100 */         if (fileModified(fileMonitor, lastModfied)) {
/* 101 */           WatchManager.logger.info("File {} was modified on {}, previous modification was {}", file, Long.valueOf(lastModfied), Long.valueOf(fileMonitor.lastModified));
/* 102 */           fileMonitor.lastModified = lastModfied;
/* 103 */           fileMonitor.fileWatcher.fileModified(file);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean fileModified(WatchManager.FileMonitor fileMonitor, long lastModfied) {
/* 109 */       return (lastModfied != fileMonitor.lastModified);
/*     */     }
/*     */   }
/*     */   
/*     */   private class FileMonitor {
/*     */     private final FileWatcher fileWatcher;
/*     */     private long lastModified;
/*     */     
/*     */     public FileMonitor(long lastModified, FileWatcher fileWatcher) {
/* 118 */       this.fileWatcher = fileWatcher;
/* 119 */       this.lastModified = lastModified;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\WatchManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */