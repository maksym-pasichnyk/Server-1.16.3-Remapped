/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.animal.ShoulderRidingEntity;
/*    */ 
/*    */ public class LandOnOwnersShoulderGoal
/*    */   extends Goal {
/*    */   private final ShoulderRidingEntity entity;
/*    */   private ServerPlayer owner;
/*    */   private boolean isSittingOnShoulder;
/*    */   
/*    */   public LandOnOwnersShoulderGoal(ShoulderRidingEntity debug1) {
/* 13 */     this.entity = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 18 */     ServerPlayer debug1 = (ServerPlayer)this.entity.getOwner();
/* 19 */     boolean debug2 = (debug1 != null && !debug1.isSpectator() && !debug1.abilities.flying && !debug1.isInWater());
/* 20 */     return (!this.entity.isOrderedToSit() && debug2 && this.entity.canSitOnShoulder());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptable() {
/* 25 */     return !this.isSittingOnShoulder;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 30 */     this.owner = (ServerPlayer)this.entity.getOwner();
/* 31 */     this.isSittingOnShoulder = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     if (this.isSittingOnShoulder || this.entity.isInSittingPose() || this.entity.isLeashed()) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     if (this.entity.getBoundingBox().intersects(this.owner.getBoundingBox()))
/* 41 */       this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\LandOnOwnersShoulderGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */