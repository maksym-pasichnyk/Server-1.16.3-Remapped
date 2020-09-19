/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocalThread;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Set;
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
/*     */ public final class ObjectCleaner
/*     */ {
/*  36 */   private static final int REFERENCE_QUEUE_POLL_TIMEOUT_MS = Math.max(500, SystemPropertyUtil.getInt("io.netty.util.internal.ObjectCleaner.refQueuePollTimeout", 10000));
/*     */ 
/*     */   
/*  39 */   static final String CLEANER_THREAD_NAME = ObjectCleaner.class.getSimpleName() + "Thread";
/*     */   
/*  41 */   private static final Set<AutomaticCleanerReference> LIVE_SET = new ConcurrentSet<AutomaticCleanerReference>();
/*  42 */   private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue();
/*  43 */   private static final AtomicBoolean CLEANER_RUNNING = new AtomicBoolean(false);
/*  44 */   private static final Runnable CLEANER_TASK = new Runnable()
/*     */     {
/*     */       public void run() {
/*  47 */         boolean interrupted = false;
/*     */ 
/*     */         
/*     */         do {
/*  51 */           while (!ObjectCleaner.LIVE_SET.isEmpty()) {
/*     */             ObjectCleaner.AutomaticCleanerReference reference;
/*     */             try {
/*  54 */               reference = (ObjectCleaner.AutomaticCleanerReference)ObjectCleaner.REFERENCE_QUEUE.remove(ObjectCleaner.REFERENCE_QUEUE_POLL_TIMEOUT_MS);
/*  55 */             } catch (InterruptedException ex) {
/*     */               
/*  57 */               interrupted = true;
/*     */               continue;
/*     */             } 
/*  60 */             if (reference != null) {
/*     */               try {
/*  62 */                 reference.cleanup();
/*  63 */               } catch (Throwable throwable) {}
/*     */ 
/*     */ 
/*     */               
/*  67 */               ObjectCleaner.LIVE_SET.remove(reference);
/*     */             } 
/*     */           } 
/*  70 */           ObjectCleaner.CLEANER_RUNNING.set(false);
/*     */ 
/*     */         
/*     */         }
/*  74 */         while (!ObjectCleaner.LIVE_SET.isEmpty() && ObjectCleaner.CLEANER_RUNNING.compareAndSet(false, true));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  80 */         if (interrupted)
/*     */         {
/*  82 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(Object object, Runnable cleanupTask) {
/*  96 */     AutomaticCleanerReference reference = new AutomaticCleanerReference(object, ObjectUtil.<Runnable>checkNotNull(cleanupTask, "cleanupTask"));
/*     */ 
/*     */     
/*  99 */     LIVE_SET.add(reference);
/*     */ 
/*     */     
/* 102 */     if (CLEANER_RUNNING.compareAndSet(false, true)) {
/* 103 */       final FastThreadLocalThread cleanupThread = new FastThreadLocalThread(CLEANER_TASK);
/* 104 */       fastThreadLocalThread.setPriority(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */           {
/*     */             public Void run() {
/* 113 */               cleanupThread.setContextClassLoader(null);
/* 114 */               return null;
/*     */             }
/*     */           });
/* 117 */       fastThreadLocalThread.setName(CLEANER_THREAD_NAME);
/*     */ 
/*     */ 
/*     */       
/* 121 */       fastThreadLocalThread.setDaemon(true);
/* 122 */       fastThreadLocalThread.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getLiveSetCount() {
/* 127 */     return LIVE_SET.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class AutomaticCleanerReference
/*     */     extends WeakReference<Object>
/*     */   {
/*     */     private final Runnable cleanupTask;
/*     */ 
/*     */     
/*     */     AutomaticCleanerReference(Object referent, Runnable cleanupTask) {
/* 138 */       super(referent, ObjectCleaner.REFERENCE_QUEUE);
/* 139 */       this.cleanupTask = cleanupTask;
/*     */     }
/*     */     
/*     */     void cleanup() {
/* 143 */       this.cleanupTask.run();
/*     */     }
/*     */ 
/*     */     
/*     */     public Thread get() {
/* 148 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 153 */       ObjectCleaner.LIVE_SET.remove(this);
/* 154 */       super.clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\ObjectCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */