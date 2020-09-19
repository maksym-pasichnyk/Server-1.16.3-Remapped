/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public abstract class ReentrantBlockableEventLoop<R extends Runnable> extends BlockableEventLoop<R> {
/*    */   private int reentrantCount;
/*    */   
/*    */   public ReentrantBlockableEventLoop(String debug1) {
/*  7 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean scheduleExecutables() {
/* 12 */     return (runningTask() || super.scheduleExecutables());
/*    */   }
/*    */   
/*    */   protected boolean runningTask() {
/* 16 */     return (this.reentrantCount != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doRunTask(R debug1) {
/* 21 */     this.reentrantCount++;
/*    */     try {
/* 23 */       super.doRunTask(debug1);
/*    */     } finally {
/* 25 */       this.reentrantCount--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\thread\ReentrantBlockableEventLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */