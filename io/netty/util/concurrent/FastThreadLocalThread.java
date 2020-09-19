/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.InternalThreadLocalMap;
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
/*    */ public class FastThreadLocalThread
/*    */   extends Thread
/*    */ {
/*    */   private final boolean cleanupFastThreadLocals;
/*    */   private InternalThreadLocalMap threadLocalMap;
/*    */   
/*    */   public FastThreadLocalThread() {
/* 31 */     this.cleanupFastThreadLocals = false;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(Runnable target) {
/* 35 */     super(FastThreadLocalRunnable.wrap(target));
/* 36 */     this.cleanupFastThreadLocals = true;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target) {
/* 40 */     super(group, FastThreadLocalRunnable.wrap(target));
/* 41 */     this.cleanupFastThreadLocals = true;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(String name) {
/* 45 */     super(name);
/* 46 */     this.cleanupFastThreadLocals = false;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, String name) {
/* 50 */     super(group, name);
/* 51 */     this.cleanupFastThreadLocals = false;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(Runnable target, String name) {
/* 55 */     super(FastThreadLocalRunnable.wrap(target), name);
/* 56 */     this.cleanupFastThreadLocals = true;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target, String name) {
/* 60 */     super(group, FastThreadLocalRunnable.wrap(target), name);
/* 61 */     this.cleanupFastThreadLocals = true;
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target, String name, long stackSize) {
/* 65 */     super(group, FastThreadLocalRunnable.wrap(target), name, stackSize);
/* 66 */     this.cleanupFastThreadLocals = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final InternalThreadLocalMap threadLocalMap() {
/* 74 */     return this.threadLocalMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap) {
/* 82 */     this.threadLocalMap = threadLocalMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean willCleanupFastThreadLocals() {
/* 90 */     return this.cleanupFastThreadLocals;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean willCleanupFastThreadLocals(Thread thread) {
/* 98 */     return (thread instanceof FastThreadLocalThread && ((FastThreadLocalThread)thread)
/* 99 */       .willCleanupFastThreadLocals());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\FastThreadLocalThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */