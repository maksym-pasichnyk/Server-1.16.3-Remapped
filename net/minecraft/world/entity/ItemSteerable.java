/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface ItemSteerable {
/*    */   boolean boost();
/*    */   
/*    */   void travelWithInput(Vec3 paramVec3);
/*    */   
/*    */   float getSteeringSpeed();
/*    */   
/*    */   default boolean travel(Mob debug1, ItemBasedSteering debug2, Vec3 debug3) {
/* 14 */     if (!debug1.isAlive()) {
/* 15 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 19 */     Entity debug4 = debug1.getPassengers().isEmpty() ? null : debug1.getPassengers().get(0);
/* 20 */     if (!debug1.isVehicle() || !debug1.canBeControlledByRider() || !(debug4 instanceof net.minecraft.world.entity.player.Player)) {
/* 21 */       debug1.maxUpStep = 0.5F;
/* 22 */       debug1.flyingSpeed = 0.02F;
/* 23 */       travelWithInput(debug3);
/* 24 */       return false;
/*    */     } 
/*    */     
/* 27 */     debug1.yRot = debug4.yRot;
/* 28 */     debug1.yRotO = debug1.yRot;
/* 29 */     debug1.xRot = debug4.xRot * 0.5F;
/* 30 */     debug1.setRot(debug1.yRot, debug1.xRot);
/* 31 */     debug1.yBodyRot = debug1.yRot;
/* 32 */     debug1.yHeadRot = debug1.yRot;
/*    */     
/* 34 */     debug1.maxUpStep = 1.0F;
/* 35 */     debug1.flyingSpeed = debug1.getSpeed() * 0.1F;
/*    */     
/* 37 */     if (debug2.boosting && 
/* 38 */       debug2.boostTime++ > debug2.boostTimeTotal) {
/* 39 */       debug2.boosting = false;
/*    */     }
/*    */ 
/*    */     
/* 43 */     if (debug1.isControlledByLocalInstance()) {
/* 44 */       float debug5 = getSteeringSpeed();
/* 45 */       if (debug2.boosting) {
/* 46 */         debug5 += debug5 * 1.15F * Mth.sin(debug2.boostTime / debug2.boostTimeTotal * 3.1415927F);
/*    */       }
/* 48 */       debug1.setSpeed(debug5);
/* 49 */       travelWithInput(new Vec3(0.0D, 0.0D, 1.0D));
/* 50 */       debug1.lerpSteps = 0;
/*    */     } else {
/* 52 */       debug1.calculateEntityAnimation(debug1, false);
/* 53 */       debug1.setDeltaMovement(Vec3.ZERO);
/*    */     } 
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ItemSteerable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */