/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.jctools.queues.MessagePassingQueue;
/*     */ import org.jctools.queues.MpscArrayQueue;
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
/*     */ @Plugin(name = "JCToolsBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
/*     */ public class JCToolsBlockingQueueFactory<E>
/*     */   implements BlockingQueueFactory<E>
/*     */ {
/*     */   private final WaitStrategy waitStrategy;
/*     */   
/*     */   private JCToolsBlockingQueueFactory(WaitStrategy waitStrategy) {
/*  41 */     this.waitStrategy = waitStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockingQueue<E> create(int capacity) {
/*  46 */     return new MpscBlockingQueue<>(capacity, this.waitStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static <E> JCToolsBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "WaitStrategy", defaultString = "PARK") WaitStrategy waitStrategy) {
/*  52 */     return new JCToolsBlockingQueueFactory<>(waitStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MpscBlockingQueue<E>
/*     */     extends MpscArrayQueue<E>
/*     */     implements BlockingQueue<E>
/*     */   {
/*     */     private final JCToolsBlockingQueueFactory.WaitStrategy waitStrategy;
/*     */     
/*     */     MpscBlockingQueue(int capacity, JCToolsBlockingQueueFactory.WaitStrategy waitStrategy) {
/*  63 */       super(capacity);
/*  64 */       this.waitStrategy = waitStrategy;
/*     */     }
/*     */ 
/*     */     
/*     */     public int drainTo(Collection<? super E> c) {
/*  69 */       return drainTo(c, capacity());
/*     */     }
/*     */ 
/*     */     
/*     */     public int drainTo(final Collection<? super E> c, int maxElements) {
/*  74 */       return drain(new MessagePassingQueue.Consumer<E>()
/*     */           {
/*     */             public void accept(E e) {
/*  77 */               c.add(e);
/*     */             }
/*     */           },  maxElements);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  84 */       int idleCounter = 0;
/*  85 */       long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
/*     */       while (true) {
/*  87 */         if (offer(e))
/*  88 */           return true; 
/*  89 */         if (System.nanoTime() - timeoutNanos > 0L) {
/*  90 */           return false;
/*     */         }
/*  92 */         idleCounter = this.waitStrategy.idle(idleCounter);
/*  93 */         if (Thread.interrupted())
/*  94 */           throw new InterruptedException(); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/*  99 */       int idleCounter = 0;
/* 100 */       long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
/*     */       while (true) {
/* 102 */         E result = poll();
/* 103 */         if (result != null)
/* 104 */           return result; 
/* 105 */         if (System.nanoTime() - timeoutNanos > 0L) {
/* 106 */           return null;
/*     */         }
/* 108 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 109 */         if (Thread.interrupted())
/* 110 */           throw new InterruptedException(); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void put(E e) throws InterruptedException {
/* 115 */       int idleCounter = 0;
/*     */       while (true) {
/* 117 */         if (offer(e)) {
/*     */           return;
/*     */         }
/* 120 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 121 */         if (Thread.interrupted()) {
/* 122 */           throw new InterruptedException();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean offer(E e) {
/* 128 */       return offerIfBelowThreshold(e, capacity() - 32);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remainingCapacity() {
/* 133 */       return capacity() - size();
/*     */     }
/*     */ 
/*     */     
/*     */     public E take() throws InterruptedException {
/* 138 */       int idleCounter = 100;
/*     */       while (true) {
/* 140 */         E result = (E)relaxedPoll();
/* 141 */         if (result != null) {
/* 142 */           return result;
/*     */         }
/* 144 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 145 */         if (Thread.interrupted())
/* 146 */           throw new InterruptedException(); 
/*     */       } 
/*     */     } }
/*     */   
/*     */   public enum WaitStrategy {
/* 151 */     SPIN((String)new JCToolsBlockingQueueFactory.Idle()
/*     */       {
/*     */         public int idle(int idleCounter) {
/* 154 */           return idleCounter + 1;
/*     */         }
/*     */       }),
/* 157 */     YIELD((String)new JCToolsBlockingQueueFactory.Idle()
/*     */       {
/*     */         public int idle(int idleCounter) {
/* 160 */           Thread.yield();
/* 161 */           return idleCounter + 1;
/*     */         }
/*     */       }),
/* 164 */     PARK((String)new JCToolsBlockingQueueFactory.Idle()
/*     */       {
/*     */         public int idle(int idleCounter) {
/* 167 */           LockSupport.parkNanos(1L);
/* 168 */           return idleCounter + 1;
/*     */         }
/*     */       }),
/* 171 */     PROGRESSIVE((String)new JCToolsBlockingQueueFactory.Idle()
/*     */       {
/*     */         public int idle(int idleCounter) {
/* 174 */           if (idleCounter > 200) {
/* 175 */             LockSupport.parkNanos(1L);
/* 176 */           } else if (idleCounter > 100) {
/* 177 */             Thread.yield();
/*     */           } 
/* 179 */           return idleCounter + 1;
/*     */         }
/*     */       });
/*     */     
/*     */     private final JCToolsBlockingQueueFactory.Idle idle;
/*     */     
/*     */     private int idle(int idleCounter) {
/* 186 */       return this.idle.idle(idleCounter);
/*     */     }
/*     */     
/*     */     WaitStrategy(JCToolsBlockingQueueFactory.Idle idle) {
/* 190 */       this.idle = idle;
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface Idle {
/*     */     int idle(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\async\JCToolsBlockingQueueFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */