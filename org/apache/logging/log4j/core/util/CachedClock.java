/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CachedClock
/*    */   implements Clock
/*    */ {
/*    */   private static final int UPDATE_THRESHOLD = 1000;
/*    */   private static volatile CachedClock instance;
/* 32 */   private static final Object INSTANCE_LOCK = new Object();
/* 33 */   private volatile long millis = System.currentTimeMillis();
/* 34 */   private short count = 0;
/*    */   
/*    */   private CachedClock() {
/* 37 */     Thread updater = new Log4jThread(new Runnable()
/*    */         {
/*    */           public void run() {
/*    */             while (true) {
/* 41 */               long time = System.currentTimeMillis();
/* 42 */               CachedClock.this.millis = time;
/*    */ 
/*    */               
/* 45 */               LockSupport.parkNanos(1000000L);
/*    */             } 
/*    */           }
/*    */         },  "CachedClock Updater Thread");
/* 49 */     updater.setDaemon(true);
/* 50 */     updater.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public static CachedClock instance() {
/* 55 */     CachedClock result = instance;
/* 56 */     if (result == null) {
/* 57 */       synchronized (INSTANCE_LOCK) {
/* 58 */         result = instance;
/* 59 */         if (result == null) {
/* 60 */           instance = result = new CachedClock();
/*    */         }
/*    */       } 
/*    */     }
/* 64 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long currentTimeMillis() {
/* 82 */     if ((this.count = (short)(this.count + 1)) > 1000) {
/* 83 */       this.millis = System.currentTimeMillis();
/* 84 */       this.count = 0;
/*    */     } 
/* 86 */     return this.millis;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\CachedClock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */