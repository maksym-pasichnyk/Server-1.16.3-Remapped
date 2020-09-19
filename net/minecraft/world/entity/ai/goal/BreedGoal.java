/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BreedGoal extends Goal {
/* 13 */   private static final TargetingConditions PARTNER_TARGETING = (new TargetingConditions()).range(8.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
/*    */   
/*    */   protected final Animal animal;
/*    */   private final Class<? extends Animal> partnerClass;
/*    */   protected final Level level;
/*    */   protected Animal partner;
/*    */   private int loveTime;
/*    */   private final double speedModifier;
/*    */   
/*    */   public BreedGoal(Animal debug1, double debug2) {
/* 23 */     this(debug1, debug2, (Class)debug1.getClass());
/*    */   }
/*    */   
/*    */   public BreedGoal(Animal debug1, double debug2, Class<? extends Animal> debug4) {
/* 27 */     this.animal = debug1;
/* 28 */     this.level = debug1.level;
/* 29 */     this.partnerClass = debug4;
/* 30 */     this.speedModifier = debug2;
/* 31 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 36 */     if (!this.animal.isInLove()) {
/* 37 */       return false;
/*    */     }
/* 39 */     this.partner = getFreePartner();
/* 40 */     return (this.partner != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 45 */     return (this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 50 */     this.partner = null;
/* 51 */     this.loveTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 56 */     this.animal.getLookControl().setLookAt((Entity)this.partner, 10.0F, this.animal.getMaxHeadXRot());
/* 57 */     this.animal.getNavigation().moveTo((Entity)this.partner, this.speedModifier);
/* 58 */     this.loveTime++;
/* 59 */     if (this.loveTime >= 60 && this.animal.distanceToSqr((Entity)this.partner) < 9.0D) {
/* 60 */       breed();
/*    */     }
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private Animal getFreePartner() {
/* 66 */     List<Animal> debug1 = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, (LivingEntity)this.animal, this.animal.getBoundingBox().inflate(8.0D));
/* 67 */     double debug2 = Double.MAX_VALUE;
/* 68 */     Animal debug4 = null;
/* 69 */     for (Animal debug6 : debug1) {
/* 70 */       if (this.animal.canMate(debug6) && this.animal.distanceToSqr((Entity)debug6) < debug2) {
/* 71 */         debug4 = debug6;
/* 72 */         debug2 = this.animal.distanceToSqr((Entity)debug6);
/*    */       } 
/*    */     } 
/* 75 */     return debug4;
/*    */   }
/*    */   
/*    */   protected void breed() {
/* 79 */     this.animal.spawnChildFromBreeding((ServerLevel)this.level, this.partner);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreedGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */