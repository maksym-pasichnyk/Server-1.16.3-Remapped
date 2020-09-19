/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ public class OwnerHurtByTargetGoal extends TargetGoal {
/*    */   private final TamableAnimal tameAnimal;
/*    */   private LivingEntity ownerLastHurtBy;
/*    */   private int timestamp;
/*    */   
/*    */   public OwnerHurtByTargetGoal(TamableAnimal debug1) {
/* 16 */     super((Mob)debug1, false);
/* 17 */     this.tameAnimal = debug1;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (!this.tameAnimal.isTame() || this.tameAnimal.isOrderedToSit()) {
/* 24 */       return false;
/*    */     }
/* 26 */     LivingEntity debug1 = this.tameAnimal.getOwner();
/* 27 */     if (debug1 == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     this.ownerLastHurtBy = debug1.getLastHurtByMob();
/* 31 */     int debug2 = debug1.getLastHurtByMobTimestamp();
/* 32 */     return (debug2 != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setTarget(this.ownerLastHurtBy);
/*    */     
/* 39 */     LivingEntity debug1 = this.tameAnimal.getOwner();
/* 40 */     if (debug1 != null) {
/* 41 */       this.timestamp = debug1.getLastHurtByMobTimestamp();
/*    */     }
/*    */     
/* 44 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\OwnerHurtByTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */