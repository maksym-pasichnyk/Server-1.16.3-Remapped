/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.IntRange;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunSometimes<E extends LivingEntity>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private boolean resetTicks;
/*    */   private boolean wasRunning;
/*    */   private final IntRange interval;
/*    */   private final Behavior<? super E> wrappedBehavior;
/*    */   private int ticksUntilNextStart;
/*    */   
/*    */   public RunSometimes(Behavior<? super E> debug1, IntRange debug2) {
/* 19 */     this(debug1, false, debug2);
/*    */   }
/*    */   
/*    */   public RunSometimes(Behavior<? super E> debug1, boolean debug2, IntRange debug3) {
/* 23 */     super(debug1.entryCondition);
/*    */     
/* 25 */     this.wrappedBehavior = debug1;
/* 26 */     this.resetTicks = !debug2;
/* 27 */     this.interval = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 32 */     if (!this.wrappedBehavior.checkExtraStartConditions(debug1, debug2)) {
/* 33 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 37 */     if (this.resetTicks) {
/* 38 */       resetTicksUntilNextStart(debug1);
/* 39 */       this.resetTicks = false;
/*    */     } 
/*    */     
/* 42 */     if (this.ticksUntilNextStart > 0) {
/* 43 */       this.ticksUntilNextStart--;
/*    */     }
/*    */     
/* 46 */     return (!this.wasRunning && this.ticksUntilNextStart == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 51 */     this.wrappedBehavior.start(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, E debug2, long debug3) {
/* 56 */     return this.wrappedBehavior.canStillUse(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, E debug2, long debug3) {
/* 61 */     this.wrappedBehavior.tick(debug1, debug2, debug3);
/*    */     
/* 63 */     this.wasRunning = (this.wrappedBehavior.getStatus() == Behavior.Status.RUNNING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, E debug2, long debug3) {
/* 68 */     resetTicksUntilNextStart(debug1);
/* 69 */     this.wrappedBehavior.stop(debug1, debug2, debug3);
/*    */   }
/*    */   
/*    */   private void resetTicksUntilNextStart(ServerLevel debug1) {
/* 73 */     this.ticksUntilNextStart = this.interval.randomValue(debug1.random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long debug1) {
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "RunSometimes: " + this.wrappedBehavior;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RunSometimes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */