/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class DolphinLookControl
/*    */   extends LookControl
/*    */ {
/*    */   private final int maxYRotFromCenter;
/*    */   
/*    */   public DolphinLookControl(Mob debug1, int debug2) {
/* 12 */     super(debug1);
/* 13 */     this.maxYRotFromCenter = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.hasWanted) {
/* 19 */       this.hasWanted = false;
/*    */       
/* 21 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, getYRotD() + 20.0F, this.yMaxRotSpeed);
/* 22 */       this.mob.xRot = rotateTowards(this.mob.xRot, getXRotD() + 10.0F, this.xMaxRotAngle);
/*    */     } else {
/* 24 */       if (this.mob.getNavigation().isDone()) {
/* 25 */         this.mob.xRot = rotateTowards(this.mob.xRot, 0.0F, 5.0F);
/*    */       }
/* 27 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
/*    */     } 
/*    */     
/* 30 */     float debug1 = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
/*    */ 
/*    */     
/* 33 */     if (debug1 < -this.maxYRotFromCenter) {
/* 34 */       this.mob.yBodyRot -= 4.0F;
/* 35 */     } else if (debug1 > this.maxYRotFromCenter) {
/* 36 */       this.mob.yBodyRot += 4.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\DolphinLookControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */