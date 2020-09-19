/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LeapAtTargetGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private LivingEntity target;
/*    */   private final float yd;
/*    */   
/*    */   public LeapAtTargetGoal(Mob debug1, float debug2) {
/* 16 */     this.mob = debug1;
/* 17 */     this.yd = debug2;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (this.mob.isVehicle()) {
/* 24 */       return false;
/*    */     }
/* 26 */     this.target = this.mob.getTarget();
/* 27 */     if (this.target == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     double debug1 = this.mob.distanceToSqr((Entity)this.target);
/* 31 */     if (debug1 < 4.0D || debug1 > 16.0D) {
/* 32 */       return false;
/*    */     }
/* 34 */     if (!this.mob.isOnGround()) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (this.mob.getRandom().nextInt(5) != 0) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 45 */     return !this.mob.isOnGround();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 51 */     Vec3 debug1 = this.mob.getDeltaMovement();
/* 52 */     Vec3 debug2 = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
/* 53 */     if (debug2.lengthSqr() > 1.0E-7D) {
/* 54 */       debug2 = debug2.normalize().scale(0.4D).add(debug1.scale(0.2D));
/*    */     }
/*    */     
/* 57 */     this.mob.setDeltaMovement(debug2.x, this.yd, debug2.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\LeapAtTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */