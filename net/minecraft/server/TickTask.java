/*    */ package net.minecraft.server;
/*    */ 
/*    */ public class TickTask implements Runnable {
/*    */   private final int tick;
/*    */   private final Runnable runnable;
/*    */   
/*    */   public TickTask(int debug1, Runnable debug2) {
/*  8 */     this.tick = debug1;
/*  9 */     this.runnable = debug2;
/*    */   }
/*    */   
/*    */   public int getTick() {
/* 13 */     return this.tick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 18 */     this.runnable.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\TickTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */