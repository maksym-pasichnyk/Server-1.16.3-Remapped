/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BodyRotationControl
/*    */ {
/*    */   private final Mob mob;
/*    */   private int headStableTime;
/*    */   private float lastStableYHeadRot;
/*    */   
/*    */   public BodyRotationControl(Mob debug1) {
/* 17 */     this.mob = debug1;
/*    */   }
/*    */   
/*    */   public void clientTick() {
/* 21 */     if (isMoving()) {
/* 22 */       this.mob.yBodyRot = this.mob.yRot;
/* 23 */       rotateHeadIfNecessary();
/*    */       
/* 25 */       this.lastStableYHeadRot = this.mob.yHeadRot;
/* 26 */       this.headStableTime = 0;
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     if (notCarryingMobPassengers()) {
/* 31 */       if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
/*    */ 
/*    */         
/* 34 */         this.headStableTime = 0;
/* 35 */         this.lastStableYHeadRot = this.mob.yHeadRot;
/* 36 */         rotateBodyIfNecessary();
/*    */       } else {
/* 38 */         this.headStableTime++;
/* 39 */         if (this.headStableTime > 10)
/*    */         {
/*    */           
/* 42 */           rotateHeadTowardsFront();
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void rotateBodyIfNecessary() {
/* 49 */     this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, this.mob.getMaxHeadYRot());
/*    */   }
/*    */   
/*    */   private void rotateHeadIfNecessary() {
/* 53 */     this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*    */   }
/*    */   
/*    */   private void rotateHeadTowardsFront() {
/* 57 */     int debug1 = this.headStableTime - 10;
/*    */ 
/*    */     
/* 60 */     float debug2 = Mth.clamp(debug1 / 10.0F, 0.0F, 1.0F);
/*    */     
/* 62 */     float debug3 = this.mob.getMaxHeadYRot() * (1.0F - debug2);
/*    */     
/* 64 */     this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, debug3);
/*    */   }
/*    */   
/*    */   private boolean notCarryingMobPassengers() {
/* 68 */     return (this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof Mob));
/*    */   }
/*    */   
/*    */   private boolean isMoving() {
/* 72 */     double debug1 = this.mob.getX() - this.mob.xo;
/* 73 */     double debug3 = this.mob.getZ() - this.mob.zo;
/*    */     
/* 75 */     return (debug1 * debug1 + debug3 * debug3 > 2.500000277905201E-7D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\BodyRotationControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */