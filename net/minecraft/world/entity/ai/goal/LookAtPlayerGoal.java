/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAtPlayerGoal
/*    */   extends Goal {
/*    */   protected final Mob mob;
/*    */   protected Entity lookAt;
/*    */   protected final float lookDistance;
/*    */   private int lookTime;
/*    */   protected final float probability;
/*    */   protected final Class<? extends LivingEntity> lookAtType;
/*    */   protected final TargetingConditions lookAtContext;
/*    */   
/*    */   public LookAtPlayerGoal(Mob debug1, Class<? extends LivingEntity> debug2, float debug3) {
/* 22 */     this(debug1, debug2, debug3, 0.02F);
/*    */   }
/*    */   
/*    */   public LookAtPlayerGoal(Mob debug1, Class<? extends LivingEntity> debug2, float debug3, float debug4) {
/* 26 */     this.mob = debug1;
/* 27 */     this.lookAtType = debug2;
/* 28 */     this.lookDistance = debug3;
/* 29 */     this.probability = debug4;
/* 30 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*    */     
/* 32 */     if (debug2 == Player.class) {
/* 33 */       this.lookAtContext = (new TargetingConditions()).range(debug3).allowSameTeam().allowInvulnerable().allowNonAttackable().selector(debug1 -> EntitySelector.notRiding((Entity)debug0).test(debug1));
/*    */     } else {
/* 35 */       this.lookAtContext = (new TargetingConditions()).range(debug3).allowSameTeam().allowInvulnerable().allowNonAttackable();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 41 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 42 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 46 */     if (this.mob.getTarget() != null) {
/* 47 */       this.lookAt = (Entity)this.mob.getTarget();
/*    */     }
/* 49 */     if (this.lookAtType == Player.class) {
/* 50 */       this.lookAt = (Entity)this.mob.level.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } else {
/* 52 */       this.lookAt = (Entity)this.mob.level.getNearestLoadedEntity(this.lookAtType, this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance));
/*    */     } 
/*    */     
/* 55 */     return (this.lookAt != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 60 */     if (!this.lookAt.isAlive()) {
/* 61 */       return false;
/*    */     }
/* 63 */     if (this.mob.distanceToSqr(this.lookAt) > (this.lookDistance * this.lookDistance)) {
/* 64 */       return false;
/*    */     }
/* 66 */     return (this.lookTime > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 71 */     this.lookTime = 40 + this.mob.getRandom().nextInt(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 76 */     this.lookAt = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 81 */     this.mob.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ());
/* 82 */     this.lookTime--;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\LookAtPlayerGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */