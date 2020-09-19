/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ 
/*    */ public class FlyingMoveControl extends MoveControl {
/*    */   private final int maxTurn;
/*    */   private final boolean hoversInPlace;
/*    */   
/*    */   public FlyingMoveControl(Mob debug1, int debug2, boolean debug3) {
/* 12 */     super(debug1);
/* 13 */     this.maxTurn = debug2;
/* 14 */     this.hoversInPlace = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 19 */     if (this.operation == MoveControl.Operation.MOVE_TO) {
/* 20 */       float debug10; this.operation = MoveControl.Operation.WAIT;
/*    */       
/* 22 */       this.mob.setNoGravity(true);
/*    */       
/* 24 */       double debug1 = this.wantedX - this.mob.getX();
/* 25 */       double debug3 = this.wantedY - this.mob.getY();
/* 26 */       double debug5 = this.wantedZ - this.mob.getZ();
/* 27 */       double debug7 = debug1 * debug1 + debug3 * debug3 + debug5 * debug5;
/* 28 */       if (debug7 < 2.500000277905201E-7D) {
/* 29 */         this.mob.setYya(0.0F);
/* 30 */         this.mob.setZza(0.0F);
/*    */         return;
/*    */       } 
/* 33 */       float debug9 = (float)(Mth.atan2(debug5, debug1) * 57.2957763671875D) - 90.0F;
/* 34 */       this.mob.yRot = rotlerp(this.mob.yRot, debug9, 90.0F);
/*    */       
/* 36 */       if (this.mob.isOnGround()) {
/* 37 */         debug10 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
/*    */       } else {
/* 39 */         debug10 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
/*    */       } 
/* 41 */       this.mob.setSpeed(debug10);
/*    */       
/* 43 */       double debug11 = Mth.sqrt(debug1 * debug1 + debug5 * debug5);
/* 44 */       float debug13 = (float)-(Mth.atan2(debug3, debug11) * 57.2957763671875D);
/* 45 */       this.mob.xRot = rotlerp(this.mob.xRot, debug13, this.maxTurn);
/* 46 */       this.mob.setYya((debug3 > 0.0D) ? debug10 : -debug10);
/*    */     } else {
/* 48 */       if (!this.hoversInPlace) {
/* 49 */         this.mob.setNoGravity(false);
/*    */       }
/*    */       
/* 52 */       this.mob.setYya(0.0F);
/* 53 */       this.mob.setZza(0.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\FlyingMoveControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */