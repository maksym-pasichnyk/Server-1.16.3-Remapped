/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Wolf;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BegGoal extends Goal {
/*    */   private final Wolf wolf;
/*    */   private Player player;
/*    */   private final Level level;
/*    */   private final float lookDistance;
/*    */   private int lookTime;
/*    */   private final TargetingConditions begTargeting;
/*    */   
/*    */   public BegGoal(Wolf debug1, float debug2) {
/* 23 */     this.wolf = debug1;
/* 24 */     this.level = debug1.level;
/* 25 */     this.lookDistance = debug2;
/* 26 */     this.begTargeting = (new TargetingConditions()).range(debug2).allowInvulnerable().allowSameTeam().allowNonAttackable();
/* 27 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 32 */     this.player = this.level.getNearestPlayer(this.begTargeting, (LivingEntity)this.wolf);
/* 33 */     if (this.player == null) {
/* 34 */       return false;
/*    */     }
/* 36 */     return playerHoldingInteresting(this.player);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 41 */     if (!this.player.isAlive()) {
/* 42 */       return false;
/*    */     }
/* 44 */     if (this.wolf.distanceToSqr((Entity)this.player) > (this.lookDistance * this.lookDistance)) {
/* 45 */       return false;
/*    */     }
/* 47 */     return (this.lookTime > 0 && playerHoldingInteresting(this.player));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 52 */     this.wolf.setIsInterested(true);
/* 53 */     this.lookTime = 40 + this.wolf.getRandom().nextInt(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.wolf.setIsInterested(false);
/* 59 */     this.player = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 64 */     this.wolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, this.wolf.getMaxHeadXRot());
/* 65 */     this.lookTime--;
/*    */   }
/*    */   
/*    */   private boolean playerHoldingInteresting(Player debug1) {
/* 69 */     for (InteractionHand debug5 : InteractionHand.values()) {
/* 70 */       ItemStack debug6 = debug1.getItemInHand(debug5);
/* 71 */       if (this.wolf.isTame() && debug6.getItem() == Items.BONE) {
/* 72 */         return true;
/*    */       }
/* 74 */       if (this.wolf.isFood(debug6)) {
/* 75 */         return true;
/*    */       }
/*    */     } 
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\BegGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */