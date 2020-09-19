/*     */ package org.apache.commons.io;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ThreadMonitor
/*     */   implements Runnable
/*     */ {
/*     */   private final Thread thread;
/*     */   private final long timeout;
/*     */   
/*     */   public static Thread start(long timeout) {
/*  55 */     return start(Thread.currentThread(), timeout);
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
/*     */   public static Thread start(Thread thread, long timeout) {
/*  68 */     Thread monitor = null;
/*  69 */     if (timeout > 0L) {
/*  70 */       ThreadMonitor timout = new ThreadMonitor(thread, timeout);
/*  71 */       monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
/*  72 */       monitor.setDaemon(true);
/*  73 */       monitor.start();
/*     */     } 
/*  75 */     return monitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void stop(Thread thread) {
/*  84 */     if (thread != null) {
/*  85 */       thread.interrupt();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadMonitor(Thread thread, long timeout) {
/*  96 */     this.thread = thread;
/*  97 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 108 */       sleep(this.timeout);
/* 109 */       this.thread.interrupt();
/* 110 */     } catch (InterruptedException interruptedException) {}
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
/*     */   private static void sleep(long ms) throws InterruptedException {
/* 125 */     long finishAt = System.currentTimeMillis() + ms;
/* 126 */     long remaining = ms;
/*     */     do {
/* 128 */       Thread.sleep(remaining);
/* 129 */       remaining = finishAt - System.currentTimeMillis();
/* 130 */     } while (remaining > 0L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\ThreadMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */