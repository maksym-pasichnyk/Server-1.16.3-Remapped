/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
/*     */ import org.apache.logging.log4j.core.config.CronScheduledFuture;
/*     */ import org.apache.logging.log4j.core.config.Scheduled;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.CronExpression;
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
/*     */ @Plugin(name = "CronTriggeringPolicy", category = "Core", printObject = true)
/*     */ @Scheduled
/*     */ public final class CronTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private static final String defaultSchedule = "0 0 0 * * ?";
/*     */   private RollingFileManager manager;
/*     */   private final CronExpression cronExpression;
/*     */   private final Configuration configuration;
/*     */   private final boolean checkOnStartup;
/*     */   private volatile Date lastRollDate;
/*     */   private CronScheduledFuture<?> future;
/*     */   
/*     */   private CronTriggeringPolicy(CronExpression schedule, boolean checkOnStartup, Configuration configuration) {
/*  55 */     this.cronExpression = Objects.<CronExpression>requireNonNull(schedule, "schedule");
/*  56 */     this.configuration = Objects.<Configuration>requireNonNull(configuration, "configuration");
/*  57 */     this.checkOnStartup = checkOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager aManager) {
/*  68 */     this.manager = aManager;
/*  69 */     Date now = new Date();
/*  70 */     Date lastRollForFile = this.cronExpression.getPrevFireTime(new Date(this.manager.getFileTime()));
/*  71 */     Date lastRegularRoll = this.cronExpression.getPrevFireTime(new Date());
/*  72 */     aManager.getPatternProcessor().setCurrentFileTime(lastRegularRoll.getTime());
/*  73 */     LOGGER.debug("LastRollForFile {}, LastRegularRole {}", lastRollForFile, lastRegularRoll);
/*  74 */     aManager.getPatternProcessor().setPrevFileTime(lastRegularRoll.getTime());
/*  75 */     if (this.checkOnStartup && lastRollForFile != null && lastRegularRoll != null && lastRollForFile.before(lastRegularRoll)) {
/*     */       
/*  77 */       this.lastRollDate = lastRollForFile;
/*  78 */       rollover();
/*     */     } 
/*     */     
/*  81 */     ConfigurationScheduler scheduler = this.configuration.getScheduler();
/*  82 */     if (!scheduler.isExecutorServiceSet())
/*     */     {
/*  84 */       scheduler.incrementScheduledItems();
/*     */     }
/*  86 */     if (!scheduler.isStarted()) {
/*  87 */       scheduler.start();
/*     */     }
/*  89 */     this.lastRollDate = lastRegularRoll;
/*  90 */     this.future = scheduler.scheduleWithCron(this.cronExpression, now, new CronTrigger());
/*  91 */     LOGGER.debug(scheduler.toString());
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
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   public CronExpression getCronExpression() {
/* 107 */     return this.cronExpression;
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
/*     */   @PluginFactory
/*     */   public static CronTriggeringPolicy createPolicy(@PluginConfiguration Configuration configuration, @PluginAttribute("evaluateOnStartup") String evaluateOnStartup, @PluginAttribute("schedule") String schedule) {
/*     */     CronExpression cronExpression;
/* 126 */     boolean checkOnStartup = Boolean.parseBoolean(evaluateOnStartup);
/* 127 */     if (schedule == null) {
/* 128 */       LOGGER.info("No schedule specified, defaulting to Daily");
/* 129 */       cronExpression = getSchedule("0 0 0 * * ?");
/*     */     } else {
/* 131 */       cronExpression = getSchedule(schedule);
/* 132 */       if (cronExpression == null) {
/* 133 */         LOGGER.error("Invalid expression specified. Defaulting to Daily");
/* 134 */         cronExpression = getSchedule("0 0 0 * * ?");
/*     */       } 
/*     */     } 
/* 137 */     return new CronTriggeringPolicy(cronExpression, checkOnStartup, configuration);
/*     */   }
/*     */   
/*     */   private static CronExpression getSchedule(String expression) {
/*     */     try {
/* 142 */       return new CronExpression(expression);
/* 143 */     } catch (ParseException pe) {
/* 144 */       LOGGER.error("Invalid cron expression - " + expression, pe);
/* 145 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rollover() {
/* 150 */     this.manager.getPatternProcessor().setPrevFileTime(this.lastRollDate.getTime());
/* 151 */     Date thisRoll = this.cronExpression.getPrevFireTime(new Date());
/* 152 */     this.manager.getPatternProcessor().setCurrentFileTime(thisRoll.getTime());
/* 153 */     this.manager.rollover();
/* 154 */     if (this.future != null) {
/* 155 */       this.lastRollDate = this.future.getFireTime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 161 */     setStopping();
/* 162 */     boolean stopped = stop((Future)this.future);
/* 163 */     setStopped();
/* 164 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     return "CronTriggeringPolicy(schedule=" + this.cronExpression.getCronExpression() + ")";
/*     */   }
/*     */   
/*     */   private class CronTrigger implements Runnable {
/*     */     private CronTrigger() {}
/*     */     
/*     */     public void run() {
/* 176 */       CronTriggeringPolicy.this.rollover();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\CronTriggeringPolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */