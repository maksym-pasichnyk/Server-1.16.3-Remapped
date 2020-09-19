/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultShutdownCallbackRegistry
/*     */   implements ShutdownCallbackRegistry, LifeCycle2, Runnable
/*     */ {
/*  43 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  45 */   private final AtomicReference<LifeCycle.State> state = new AtomicReference<>(LifeCycle.State.INITIALIZED);
/*     */   private final ThreadFactory threadFactory;
/*  47 */   private final Collection<Cancellable> hooks = new CopyOnWriteArrayList<>();
/*     */ 
/*     */   
/*     */   private Reference<Thread> shutdownHookRef;
/*     */ 
/*     */   
/*     */   public DefaultShutdownCallbackRegistry() {
/*  54 */     this(Executors.defaultThreadFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultShutdownCallbackRegistry(ThreadFactory threadFactory) {
/*  63 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  71 */     if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
/*  72 */       for (Runnable hook : this.hooks) {
/*     */         try {
/*  74 */           hook.run();
/*  75 */         } catch (Throwable t1) {
/*     */           try {
/*  77 */             LOGGER.error(SHUTDOWN_HOOK_MARKER, "Caught exception executing shutdown hook {}", hook, t1);
/*  78 */           } catch (Throwable t2) {
/*  79 */             System.err.println("Caught exception " + t2.getClass() + " logging exception " + t1.getClass());
/*  80 */             t1.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*  84 */       this.state.set(LifeCycle.State.STOPPED);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class RegisteredCancellable
/*     */     implements Cancellable {
/*     */     private final Reference<Runnable> hook;
/*     */     private Collection<Cancellable> registered;
/*     */     
/*     */     RegisteredCancellable(Runnable callback, Collection<Cancellable> registered) {
/*  94 */       this.registered = registered;
/*  95 */       this.hook = new SoftReference<>(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 100 */       this.hook.clear();
/* 101 */       this.registered.remove(this);
/* 102 */       this.registered = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 107 */       Runnable runnableHook = this.hook.get();
/* 108 */       if (runnableHook != null) {
/* 109 */         runnableHook.run();
/* 110 */         this.hook.clear();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 116 */       return String.valueOf(this.hook.get());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Cancellable addShutdownCallback(Runnable callback) {
/* 122 */     if (isStarted()) {
/* 123 */       Cancellable receipt = new RegisteredCancellable(callback, this.hooks);
/* 124 */       this.hooks.add(receipt);
/* 125 */       return receipt;
/*     */     } 
/* 127 */     throw new IllegalStateException("Cannot add new shutdown hook as this is not started. Current state: " + ((LifeCycle.State)this.state.get()).name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 140 */     if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
/*     */       try {
/* 142 */         addShutdownHook(this.threadFactory.newThread(this));
/* 143 */         this.state.set(LifeCycle.State.STARTED);
/* 144 */       } catch (IllegalStateException ex) {
/* 145 */         this.state.set(LifeCycle.State.STOPPED);
/* 146 */         throw ex;
/* 147 */       } catch (Exception e) {
/* 148 */         LOGGER.catching(e);
/* 149 */         this.state.set(LifeCycle.State.STOPPED);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void addShutdownHook(Thread thread) {
/* 155 */     this.shutdownHookRef = new WeakReference<>(thread);
/* 156 */     Runtime.getRuntime().addShutdownHook(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 161 */     stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 169 */     if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
/*     */       try {
/* 171 */         removeShutdownHook();
/*     */       } finally {
/* 173 */         this.state.set(LifeCycle.State.STOPPED);
/*     */       } 
/*     */     }
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   private void removeShutdownHook() {
/* 180 */     Thread shutdownThread = this.shutdownHookRef.get();
/* 181 */     if (shutdownThread != null) {
/* 182 */       Runtime.getRuntime().removeShutdownHook(shutdownThread);
/* 183 */       this.shutdownHookRef.enqueue();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LifeCycle.State getState() {
/* 189 */     return this.state.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 199 */     return (this.state.get() == LifeCycle.State.STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/* 204 */     return (this.state.get() == LifeCycle.State.STOPPED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\DefaultShutdownCallbackRegistry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */