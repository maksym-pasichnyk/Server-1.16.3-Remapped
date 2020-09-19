/*    */ package net.minecraft.server.rcon.thread;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public abstract class GenericThread
/*    */   implements Runnable {
/* 11 */   private static final Logger LOGGER = LogManager.getLogger();
/* 12 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*    */   
/*    */   protected volatile boolean running;
/*    */   protected final String name;
/*    */   @Nullable
/*    */   protected Thread thread;
/*    */   
/*    */   protected GenericThread(String debug1) {
/* 20 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized boolean start() {
/* 27 */     if (this.running) {
/* 28 */       return true;
/*    */     }
/* 30 */     this.running = true;
/* 31 */     this.thread = new Thread(this, this.name + " #" + UNIQUE_THREAD_ID.incrementAndGet());
/* 32 */     this.thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 33 */     this.thread.start();
/* 34 */     LOGGER.info("Thread {} started", this.name);
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public synchronized void stop() {
/* 39 */     this.running = false;
/* 40 */     if (null == this.thread) {
/*    */       return;
/*    */     }
/* 43 */     int debug1 = 0;
/* 44 */     while (this.thread.isAlive()) {
/*    */       
/*    */       try {
/* 47 */         this.thread.join(1000L);
/* 48 */         debug1++;
/* 49 */         if (debug1 >= 5) {
/*    */ 
/*    */ 
/*    */           
/* 53 */           LOGGER.warn("Waited {} seconds attempting force stop!", Integer.valueOf(debug1)); continue;
/* 54 */         }  if (this.thread.isAlive()) {
/* 55 */           LOGGER.warn("Thread {} ({}) failed to exit after {} second(s)", this, this.thread.getState(), Integer.valueOf(debug1), new Exception("Stack:"));
/*    */           
/* 57 */           this.thread.interrupt();
/*    */         } 
/* 59 */       } catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */ 
/*    */     
/* 63 */     LOGGER.info("Thread {} stopped", this.name);
/* 64 */     this.thread = null;
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 68 */     return this.running;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\thread\GenericThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */