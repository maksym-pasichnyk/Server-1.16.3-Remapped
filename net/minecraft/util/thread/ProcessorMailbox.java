/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessorMailbox<T>
/*     */   implements ProcessorHandle<T>, AutoCloseable, Runnable
/*     */ {
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  21 */   private final AtomicInteger status = new AtomicInteger(0);
/*     */   public final StrictQueue<? super T, ? extends Runnable> queue;
/*     */   private final Executor dispatcher;
/*     */   private final String name;
/*     */   
/*     */   public static ProcessorMailbox<Runnable> create(Executor debug0, String debug1) {
/*  27 */     return new ProcessorMailbox<>(new StrictQueue.QueueStrictQueue<>(new ConcurrentLinkedQueue<>()), debug0, debug1);
/*     */   }
/*     */   
/*     */   public ProcessorMailbox(StrictQueue<? super T, ? extends Runnable> debug1, Executor debug2, String debug3) {
/*  31 */     this.dispatcher = debug2;
/*  32 */     this.queue = debug1;
/*  33 */     this.name = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean setAsScheduled() {
/*     */     while (true) {
/*  39 */       int debug1 = this.status.get();
/*  40 */       if ((debug1 & 0x3) != 0) {
/*  41 */         return false;
/*     */       }
/*  43 */       if (this.status.compareAndSet(debug1, debug1 | 0x2))
/*  44 */         return true; 
/*     */     } 
/*     */   }
/*     */   private void setAsIdle() {
/*     */     int debug1;
/*     */     do {
/*  50 */       debug1 = this.status.get();
/*  51 */     } while (!this.status.compareAndSet(debug1, debug1 & 0xFFFFFFFD));
/*     */   }
/*     */   
/*     */   private boolean canBeScheduled() {
/*  55 */     if ((this.status.get() & 0x1) != 0) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     return !this.queue.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     int debug1;
/*     */     do {
/*  66 */       debug1 = this.status.get();
/*  67 */     } while (!this.status.compareAndSet(debug1, debug1 | 0x1));
/*     */   }
/*     */   
/*     */   private boolean shouldProcess() {
/*  71 */     return ((this.status.get() & 0x2) != 0);
/*     */   } private boolean pollTask() {
/*     */     String debug2;
/*     */     Thread debug3;
/*  75 */     if (!shouldProcess()) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     Runnable debug1 = this.queue.pop();
/*  80 */     if (debug1 == null) {
/*  81 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  87 */       debug3 = Thread.currentThread();
/*  88 */       debug2 = debug3.getName();
/*  89 */       debug3.setName(this.name);
/*     */     } else {
/*  91 */       debug3 = null;
/*  92 */       debug2 = null;
/*     */     } 
/*  94 */     debug1.run();
/*  95 */     if (debug3 != null) {
/*  96 */       debug3.setName(debug2);
/*     */     }
/*     */     
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 105 */       pollUntil(debug0 -> (debug0 == 0));
/*     */     } finally {
/* 107 */       setAsIdle();
/* 108 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tell(T debug1) {
/* 114 */     this.queue.push(debug1);
/* 115 */     registerForExecution();
/*     */   }
/*     */   
/*     */   private void registerForExecution() {
/* 119 */     if (canBeScheduled() && 
/* 120 */       setAsScheduled()) {
/*     */       try {
/* 122 */         this.dispatcher.execute(this);
/* 123 */       } catch (RejectedExecutionException debug1) {
/*     */         
/*     */         try {
/* 126 */           this.dispatcher.execute(this);
/* 127 */         } catch (RejectedExecutionException debug2) {
/* 128 */           LOGGER.error("Cound not schedule mailbox", debug2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private int pollUntil(Int2BooleanFunction debug1) {
/* 136 */     int debug2 = 0;
/* 137 */     while (debug1.get(debug2) && pollTask()) {
/* 138 */       debug2++;
/*     */     }
/* 140 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     return this.name + " " + this.status.get() + " " + this.queue.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/* 150 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\thread\ProcessorMailbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */