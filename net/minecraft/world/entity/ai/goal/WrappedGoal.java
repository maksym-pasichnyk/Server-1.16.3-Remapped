/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class WrappedGoal extends Goal {
/*    */   private final Goal goal;
/*    */   private final int priority;
/*    */   private boolean isRunning;
/*    */   
/*    */   public WrappedGoal(int debug1, Goal debug2) {
/* 12 */     this.priority = debug1;
/* 13 */     this.goal = debug2;
/*    */   }
/*    */   
/*    */   public boolean canBeReplacedBy(WrappedGoal debug1) {
/* 17 */     return (isInterruptable() && debug1.getPriority() < getPriority());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 22 */     return this.goal.canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 27 */     return this.goal.canContinueToUse();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptable() {
/* 32 */     return this.goal.isInterruptable();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     if (this.isRunning) {
/*    */       return;
/*    */     }
/* 40 */     this.isRunning = true;
/* 41 */     this.goal.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 46 */     if (!this.isRunning) {
/*    */       return;
/*    */     }
/* 49 */     this.isRunning = false;
/* 50 */     this.goal.stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 55 */     this.goal.tick();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFlags(EnumSet<Goal.Flag> debug1) {
/* 60 */     this.goal.setFlags(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumSet<Goal.Flag> getFlags() {
/* 65 */     return this.goal.getFlags();
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 69 */     return this.isRunning;
/*    */   }
/*    */   
/*    */   public int getPriority() {
/* 73 */     return this.priority;
/*    */   }
/*    */   
/*    */   public Goal getGoal() {
/* 77 */     return this.goal;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object debug1) {
/* 82 */     if (this == debug1) {
/* 83 */       return true;
/*    */     }
/* 85 */     if (debug1 == null || getClass() != debug1.getClass()) {
/* 86 */       return false;
/*    */     }
/* 88 */     return this.goal.equals(((WrappedGoal)debug1).goal);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 93 */     return this.goal.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\WrappedGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */