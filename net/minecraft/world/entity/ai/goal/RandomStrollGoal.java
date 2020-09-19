/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class RandomStrollGoal
/*    */   extends Goal
/*    */ {
/*    */   protected final PathfinderMob mob;
/*    */   protected double wantedX;
/*    */   protected double wantedY;
/*    */   protected double wantedZ;
/*    */   protected final double speedModifier;
/*    */   protected int interval;
/*    */   protected boolean forceTrigger;
/*    */   private boolean checkNoActionTime;
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob debug1, double debug2) {
/* 23 */     this(debug1, debug2, 120);
/*    */   }
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob debug1, double debug2, int debug4) {
/* 27 */     this(debug1, debug2, debug4, true);
/*    */   }
/*    */   
/*    */   public RandomStrollGoal(PathfinderMob debug1, double debug2, int debug4, boolean debug5) {
/* 31 */     this.mob = debug1;
/* 32 */     this.speedModifier = debug2;
/* 33 */     this.interval = debug4;
/* 34 */     this.checkNoActionTime = debug5;
/* 35 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 39 */     if (this.mob.isVehicle()) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!this.forceTrigger) {
/* 43 */       if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
/* 44 */         return false;
/*    */       }
/* 46 */       if (this.mob.getRandom().nextInt(this.interval) != 0) {
/* 47 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     Vec3 debug1 = getPosition();
/*    */     
/* 53 */     if (debug1 == null) {
/* 54 */       return false;
/*    */     }
/*    */     
/* 57 */     this.wantedX = debug1.x;
/* 58 */     this.wantedY = debug1.y;
/* 59 */     this.wantedZ = debug1.z;
/* 60 */     this.forceTrigger = false;
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getPosition() {
/* 66 */     return RandomPos.getPos(this.mob, 10, 7);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 71 */     return (!this.mob.getNavigation().isDone() && !this.mob.isVehicle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 76 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 81 */     this.mob.getNavigation().stop();
/* 82 */     super.stop();
/*    */   }
/*    */   
/*    */   public void trigger() {
/* 86 */     this.forceTrigger = true;
/*    */   }
/*    */   
/*    */   public void setInterval(int debug1) {
/* 90 */     this.interval = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomStrollGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */