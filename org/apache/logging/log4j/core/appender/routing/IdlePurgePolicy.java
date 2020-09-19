/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
/*     */ import org.apache.logging.log4j.core.config.Scheduled;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "IdlePurgePolicy", category = "Core", printObject = true)
/*     */ @Scheduled
/*     */ public class IdlePurgePolicy
/*     */   extends AbstractLifeCycle
/*     */   implements PurgePolicy, Runnable
/*     */ {
/*     */   private final long timeToLive;
/*     */   private final long checkInterval;
/*  45 */   private final ConcurrentMap<String, Long> appendersUsage = new ConcurrentHashMap<>();
/*     */   private RoutingAppender routingAppender;
/*     */   private final ConfigurationScheduler scheduler;
/*     */   private volatile ScheduledFuture<?> future;
/*     */   
/*     */   public IdlePurgePolicy(long timeToLive, long checkInterval, ConfigurationScheduler scheduler) {
/*  51 */     this.timeToLive = timeToLive;
/*  52 */     this.checkInterval = checkInterval;
/*  53 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(RoutingAppender routingAppender) {
/*  58 */     this.routingAppender = routingAppender;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  63 */     setStopping();
/*  64 */     boolean stopped = stop(this.future);
/*  65 */     setStopped();
/*  66 */     return stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void purge() {
/*  74 */     long createTime = System.currentTimeMillis() - this.timeToLive;
/*  75 */     for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
/*  76 */       if (((Long)entry.getValue()).longValue() < createTime) {
/*  77 */         LOGGER.debug("Removing appender " + (String)entry.getKey());
/*  78 */         if (this.appendersUsage.remove(entry.getKey(), entry.getValue())) {
/*  79 */           this.routingAppender.deleteAppender(entry.getKey());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(String key, LogEvent event) {
/*  87 */     long now = System.currentTimeMillis();
/*  88 */     this.appendersUsage.put(key, Long.valueOf(now));
/*  89 */     if (this.future == null) {
/*  90 */       synchronized (this) {
/*  91 */         if (this.future == null) {
/*  92 */           scheduleNext();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 101 */     purge();
/* 102 */     scheduleNext();
/*     */   }
/*     */   
/*     */   private void scheduleNext() {
/* 106 */     long updateTime = Long.MAX_VALUE;
/* 107 */     for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
/* 108 */       if (((Long)entry.getValue()).longValue() < updateTime) {
/* 109 */         updateTime = ((Long)entry.getValue()).longValue();
/*     */       }
/*     */     } 
/*     */     
/* 113 */     if (updateTime < Long.MAX_VALUE) {
/* 114 */       long interval = this.timeToLive - System.currentTimeMillis() - updateTime;
/* 115 */       this.future = this.scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
/*     */     } else {
/*     */       
/* 118 */       this.future = this.scheduler.schedule(this, this.checkInterval, TimeUnit.MILLISECONDS);
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
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static PurgePolicy createPurgePolicy(@PluginAttribute("timeToLive") String timeToLive, @PluginAttribute("checkInterval") String checkInterval, @PluginAttribute("timeUnit") String timeUnit, @PluginConfiguration Configuration configuration) {
/*     */     TimeUnit units;
/*     */     long ci;
/* 137 */     if (timeToLive == null) {
/* 138 */       LOGGER.error("A timeToLive value is required");
/* 139 */       return null;
/*     */     } 
/*     */     
/* 142 */     if (timeUnit == null) {
/* 143 */       units = TimeUnit.MINUTES;
/*     */     } else {
/*     */       try {
/* 146 */         units = TimeUnit.valueOf(timeUnit.toUpperCase());
/* 147 */       } catch (Exception ex) {
/* 148 */         LOGGER.error("Invalid timeUnit value {}. timeUnit set to MINUTES", timeUnit, ex);
/* 149 */         units = TimeUnit.MINUTES;
/*     */       } 
/*     */     } 
/*     */     
/* 153 */     long ttl = units.toMillis(Long.parseLong(timeToLive));
/* 154 */     if (ttl < 0L) {
/* 155 */       LOGGER.error("timeToLive must be positive. timeToLive set to 0");
/* 156 */       ttl = 0L;
/*     */     } 
/*     */ 
/*     */     
/* 160 */     if (checkInterval == null) {
/* 161 */       ci = ttl;
/*     */     } else {
/* 163 */       ci = units.toMillis(Long.parseLong(checkInterval));
/* 164 */       if (ci < 0L) {
/* 165 */         LOGGER.error("checkInterval must be positive. checkInterval set equal to timeToLive = {}", Long.valueOf(ttl));
/* 166 */         ci = ttl;
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     return new IdlePurgePolicy(ttl, ci, configuration.getScheduler());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     return "timeToLive=" + this.timeToLive;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\routing\IdlePurgePolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */