/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveTowardsTargetGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   private LivingEntity target;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   private final float within;
/*    */   
/*    */   public MoveTowardsTargetGoal(PathfinderMob debug1, double debug2, float debug4) {
/* 20 */     this.mob = debug1;
/* 21 */     this.speedModifier = debug2;
/* 22 */     this.within = debug4;
/* 23 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 28 */     this.target = this.mob.getTarget();
/* 29 */     if (this.target == null) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (this.target.distanceToSqr((Entity)this.mob) > (this.within * this.within)) {
/* 33 */       return false;
/*    */     }
/* 35 */     Vec3 debug1 = RandomPos.getPosTowards(this.mob, 16, 7, this.target.position());
/* 36 */     if (debug1 == null) {
/* 37 */       return false;
/*    */     }
/* 39 */     this.wantedX = debug1.x;
/* 40 */     this.wantedY = debug1.y;
/* 41 */     this.wantedZ = debug1.z;
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 47 */     return (!this.mob.getNavigation().isDone() && this.target.isAlive() && this.target.distanceToSqr((Entity)this.mob) < (this.within * this.within));
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 52 */     this.target = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 57 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveTowardsTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */