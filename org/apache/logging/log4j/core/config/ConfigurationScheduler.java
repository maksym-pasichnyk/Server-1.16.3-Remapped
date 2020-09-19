/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.util.CronExpression;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
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
/*     */ public class ConfigurationScheduler
/*     */   extends AbstractLifeCycle
/*     */ {
/*  39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*  40 */   private static final String SIMPLE_NAME = "Log4j2 " + ConfigurationScheduler.class.getSimpleName();
/*     */   
/*     */   private static final int MAX_SCHEDULED_ITEMS = 5;
/*     */   private ScheduledExecutorService executorService;
/*  44 */   private int scheduledItems = 0;
/*     */ 
/*     */   
/*     */   public void start() {
/*  48 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  53 */     setStopping();
/*  54 */     if (isExecutorServiceSet()) {
/*  55 */       LOGGER.debug("{} shutting down threads in {}", SIMPLE_NAME, getExecutorService());
/*  56 */       this.executorService.shutdown();
/*     */       try {
/*  58 */         this.executorService.awaitTermination(timeout, timeUnit);
/*  59 */       } catch (InterruptedException ie) {
/*  60 */         this.executorService.shutdownNow();
/*     */         try {
/*  62 */           this.executorService.awaitTermination(timeout, timeUnit);
/*  63 */         } catch (InterruptedException inner) {
/*  64 */           LOGGER.warn("ConfigurationScheduler stopped but some scheduled services may not have completed.");
/*     */         } 
/*     */         
/*  67 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*  70 */     setStopped();
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isExecutorServiceSet() {
/*  75 */     return (this.executorService != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementScheduledItems() {
/*  82 */     if (isExecutorServiceSet()) {
/*  83 */       LOGGER.error("{} attempted to increment scheduled items after start", SIMPLE_NAME);
/*     */     } else {
/*  85 */       this.scheduledItems++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decrementScheduledItems() {
/*  93 */     if (!isStarted() && this.scheduledItems > 0) {
/*  94 */       this.scheduledItems--;
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
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 108 */     return getExecutorService().schedule(callable, delay, unit);
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
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 120 */     return getExecutorService().schedule(command, delay, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Runnable command) {
/* 131 */     return scheduleWithCron(cronExpression, new Date(), command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Date startDate, Runnable command) {
/* 142 */     Date fireDate = cronExpression.getNextValidTimeAfter((startDate == null) ? new Date() : startDate);
/* 143 */     CronRunnable runnable = new CronRunnable(command, cronExpression);
/* 144 */     ScheduledFuture<?> future = schedule(runnable, nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
/* 145 */     CronScheduledFuture<?> cronScheduledFuture = new CronScheduledFuture(future, fireDate);
/* 146 */     runnable.setScheduledFuture(cronScheduledFuture);
/* 147 */     LOGGER.debug("Scheduled cron expression {} to fire at {}", cronExpression.getCronExpression(), fireDate);
/* 148 */     return cronScheduledFuture;
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
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 164 */     return getExecutorService().scheduleAtFixedRate(command, initialDelay, period, unit);
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
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 178 */     return getExecutorService().scheduleWithFixedDelay(command, initialDelay, delay, unit);
/*     */   }
/*     */   
/*     */   public long nextFireInterval(Date fireDate) {
/* 182 */     return fireDate.getTime() - (new Date()).getTime();
/*     */   }
/*     */   
/*     */   private ScheduledExecutorService getExecutorService() {
/* 186 */     if (this.executorService == null) {
/* 187 */       if (this.scheduledItems > 0) {
/* 188 */         LOGGER.debug("{} starting {} threads", SIMPLE_NAME, Integer.valueOf(this.scheduledItems));
/* 189 */         this.scheduledItems = Math.min(this.scheduledItems, 5);
/* 190 */         ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(this.scheduledItems, (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("Scheduled"));
/*     */         
/* 192 */         executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
/* 193 */         executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 194 */         this.executorService = executor;
/*     */       } else {
/*     */         
/* 197 */         LOGGER.debug("{}: No scheduled items", SIMPLE_NAME);
/*     */       } 
/*     */     }
/* 200 */     return this.executorService;
/*     */   }
/*     */   
/*     */   public class CronRunnable
/*     */     implements Runnable {
/*     */     private final CronExpression cronExpression;
/*     */     private final Runnable runnable;
/*     */     private CronScheduledFuture<?> scheduledFuture;
/*     */     
/*     */     public CronRunnable(Runnable runnable, CronExpression cronExpression) {
/* 210 */       this.cronExpression = cronExpression;
/* 211 */       this.runnable = runnable;
/*     */     }
/*     */     
/*     */     public void setScheduledFuture(CronScheduledFuture<?> future) {
/* 215 */       this.scheduledFuture = future;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 221 */         long millis = this.scheduledFuture.getFireTime().getTime() - System.currentTimeMillis();
/* 222 */         if (millis > 0L) {
/* 223 */           ConfigurationScheduler.LOGGER.debug("Cron thread woke up {} millis early. Sleeping", Long.valueOf(millis));
/*     */           try {
/* 225 */             Thread.sleep(millis);
/* 226 */           } catch (InterruptedException interruptedException) {}
/*     */         } 
/*     */ 
/*     */         
/* 230 */         this.runnable.run();
/* 231 */       } catch (Throwable ex) {
/* 232 */         ConfigurationScheduler.LOGGER.error("{} caught error running command", ConfigurationScheduler.SIMPLE_NAME, ex);
/*     */       } finally {
/* 234 */         Date fireDate = this.cronExpression.getNextValidTimeAfter(new Date());
/* 235 */         ScheduledFuture<?> future = ConfigurationScheduler.this.schedule(this, ConfigurationScheduler.this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
/* 236 */         ConfigurationScheduler.LOGGER.debug("Cron expression {} scheduled to fire again at {}", this.cronExpression.getCronExpression(), fireDate);
/*     */         
/* 238 */         this.scheduledFuture.reset(future, fireDate);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String toString() {
/* 243 */       return "CronRunnable{" + this.cronExpression.getCronExpression() + " - " + this.scheduledFuture.getFireTime();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 248 */     StringBuilder sb = new StringBuilder("ConfigurationScheduler {");
/* 249 */     Queue<Runnable> queue = ((ScheduledThreadPoolExecutor)this.executorService).getQueue();
/* 250 */     boolean first = true;
/* 251 */     for (Runnable runnable : queue) {
/* 252 */       if (!first) {
/* 253 */         sb.append(", ");
/*     */       }
/* 255 */       sb.append(runnable.toString());
/* 256 */       first = false;
/*     */     } 
/* 258 */     sb.append("}");
/* 259 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\ConfigurationScheduler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */