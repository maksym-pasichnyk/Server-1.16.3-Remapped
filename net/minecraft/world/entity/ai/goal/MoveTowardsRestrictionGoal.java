/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveTowardsRestrictionGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   
/*    */   public MoveTowardsRestrictionGoal(PathfinderMob debug1, double debug2) {
/* 17 */     this.mob = debug1;
/* 18 */     this.speedModifier = debug2;
/* 19 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 24 */     if (this.mob.isWithinRestriction()) {
/* 25 */       return false;
/*    */     }
/* 27 */     Vec3 debug1 = RandomPos.getPosTowards(this.mob, 16, 7, Vec3.atBottomCenterOf((Vec3i)this.mob.getRestrictCenter()));
/* 28 */     if (debug1 == null) {
/* 29 */       return false;
/*    */     }
/* 31 */     this.wantedX = debug1.x;
/* 32 */     this.wantedY = debug1.y;
/* 33 */     this.wantedZ = debug1.z;
/* 34 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 39 */     return !this.mob.getNavigation().isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 44 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveTowardsRestrictionGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */