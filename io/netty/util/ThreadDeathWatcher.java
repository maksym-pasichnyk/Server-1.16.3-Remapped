/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.concurrent.DefaultThreadFactory;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ @Deprecated
/*     */ public final class ThreadDeathWatcher
/*     */ {
/*  48 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
/*     */ 
/*     */   
/*     */   static final ThreadFactory threadFactory;
/*     */ 
/*     */   
/*  54 */   private static final Queue<Entry> pendingEntries = new ConcurrentLinkedQueue<Entry>();
/*  55 */   private static final Watcher watcher = new Watcher();
/*  56 */   private static final AtomicBoolean started = new AtomicBoolean();
/*     */   private static volatile Thread watcherThread;
/*     */   
/*     */   static {
/*  60 */     String poolName = "threadDeathWatcher";
/*  61 */     String serviceThreadPrefix = SystemPropertyUtil.get("io.netty.serviceThreadPrefix");
/*  62 */     if (!StringUtil.isNullOrEmpty(serviceThreadPrefix)) {
/*  63 */       poolName = serviceThreadPrefix + poolName;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  68 */     threadFactory = (ThreadFactory)new DefaultThreadFactory(poolName, true, 1, null);
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
/*     */   public static void watch(Thread thread, Runnable task) {
/*  80 */     if (thread == null) {
/*  81 */       throw new NullPointerException("thread");
/*     */     }
/*  83 */     if (task == null) {
/*  84 */       throw new NullPointerException("task");
/*     */     }
/*  86 */     if (!thread.isAlive()) {
/*  87 */       throw new IllegalArgumentException("thread must be alive.");
/*     */     }
/*     */     
/*  90 */     schedule(thread, task, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unwatch(Thread thread, Runnable task) {
/*  97 */     if (thread == null) {
/*  98 */       throw new NullPointerException("thread");
/*     */     }
/* 100 */     if (task == null) {
/* 101 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 104 */     schedule(thread, task, false);
/*     */   }
/*     */   
/*     */   private static void schedule(Thread thread, Runnable task, boolean isWatch) {
/* 108 */     pendingEntries.add(new Entry(thread, task, isWatch));
/*     */     
/* 110 */     if (started.compareAndSet(false, true)) {
/* 111 */       final Thread watcherThread = threadFactory.newThread(watcher);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */           {
/*     */             public Void run() {
/* 120 */               watcherThread.setContextClassLoader(null);
/* 121 */               return null;
/*     */             }
/*     */           });
/*     */       
/* 125 */       watcherThread.start();
/* 126 */       ThreadDeathWatcher.watcherThread = watcherThread;
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
/*     */   public static boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException {
/* 140 */     if (unit == null) {
/* 141 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 144 */     Thread watcherThread = ThreadDeathWatcher.watcherThread;
/* 145 */     if (watcherThread != null) {
/* 146 */       watcherThread.join(unit.toMillis(timeout));
/* 147 */       return !watcherThread.isAlive();
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Watcher
/*     */     implements Runnable
/*     */   {
/* 157 */     private final List<ThreadDeathWatcher.Entry> watchees = new ArrayList<ThreadDeathWatcher.Entry>();
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/* 162 */         fetchWatchees();
/* 163 */         notifyWatchees();
/*     */ 
/*     */         
/* 166 */         fetchWatchees();
/* 167 */         notifyWatchees();
/*     */         
/*     */         try {
/* 170 */           Thread.sleep(1000L);
/* 171 */         } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */         
/* 175 */         if (this.watchees.isEmpty() && ThreadDeathWatcher.pendingEntries.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 180 */           boolean stopped = ThreadDeathWatcher.started.compareAndSet(true, false);
/* 181 */           assert stopped;
/*     */ 
/*     */           
/* 184 */           if (ThreadDeathWatcher.pendingEntries.isEmpty()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 193 */           if (!ThreadDeathWatcher.started.compareAndSet(false, true)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void fetchWatchees() {
/*     */       while (true) {
/* 208 */         ThreadDeathWatcher.Entry e = ThreadDeathWatcher.pendingEntries.poll();
/* 209 */         if (e == null) {
/*     */           break;
/*     */         }
/*     */         
/* 213 */         if (e.isWatch) {
/* 214 */           this.watchees.add(e); continue;
/*     */         } 
/* 216 */         this.watchees.remove(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void notifyWatchees() {
/* 222 */       List<ThreadDeathWatcher.Entry> watchees = this.watchees;
/* 223 */       for (int i = 0; i < watchees.size(); ) {
/* 224 */         ThreadDeathWatcher.Entry e = watchees.get(i);
/* 225 */         if (!e.thread.isAlive()) {
/* 226 */           watchees.remove(i);
/*     */           try {
/* 228 */             e.task.run();
/* 229 */           } catch (Throwable t) {
/* 230 */             ThreadDeathWatcher.logger.warn("Thread death watcher task raised an exception:", t);
/*     */           }  continue;
/*     */         } 
/* 233 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     private Watcher() {} }
/*     */   
/*     */   private static final class Entry {
/*     */     final Thread thread;
/*     */     final Runnable task;
/*     */     final boolean isWatch;
/*     */     
/*     */     Entry(Thread thread, Runnable task, boolean isWatch) {
/* 245 */       this.thread = thread;
/* 246 */       this.task = task;
/* 247 */       this.isWatch = isWatch;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 252 */       return this.thread.hashCode() ^ this.task.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 257 */       if (obj == this) {
/* 258 */         return true;
/*     */       }
/*     */       
/* 261 */       if (!(obj instanceof Entry)) {
/* 262 */         return false;
/*     */       }
/*     */       
/* 265 */       Entry that = (Entry)obj;
/* 266 */       return (this.thread == that.thread && this.task == that.task);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ThreadDeathWatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */