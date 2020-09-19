/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public final class DefaultEventExecutorChooserFactory
/*    */   implements EventExecutorChooserFactory
/*    */ {
/* 28 */   public static final DefaultEventExecutorChooserFactory INSTANCE = new DefaultEventExecutorChooserFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EventExecutorChooserFactory.EventExecutorChooser newChooser(EventExecutor[] executors) {
/* 35 */     if (isPowerOfTwo(executors.length)) {
/* 36 */       return new PowerOfTwoEventExecutorChooser(executors);
/*    */     }
/* 38 */     return new GenericEventExecutorChooser(executors);
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean isPowerOfTwo(int val) {
/* 43 */     return ((val & -val) == val);
/*    */   }
/*    */   
/*    */   private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooserFactory.EventExecutorChooser {
/* 47 */     private final AtomicInteger idx = new AtomicInteger();
/*    */     private final EventExecutor[] executors;
/*    */     
/*    */     PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
/* 51 */       this.executors = executors;
/*    */     }
/*    */ 
/*    */     
/*    */     public EventExecutor next() {
/* 56 */       return this.executors[this.idx.getAndIncrement() & this.executors.length - 1];
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class GenericEventExecutorChooser implements EventExecutorChooserFactory.EventExecutorChooser {
/* 61 */     private final AtomicInteger idx = new AtomicInteger();
/*    */     private final EventExecutor[] executors;
/*    */     
/*    */     GenericEventExecutorChooser(EventExecutor[] executors) {
/* 65 */       this.executors = executors;
/*    */     }
/*    */ 
/*    */     
/*    */     public EventExecutor next() {
/* 70 */       return this.executors[Math.abs(this.idx.getAndIncrement() % this.executors.length)];
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\DefaultEventExecutorChooserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */