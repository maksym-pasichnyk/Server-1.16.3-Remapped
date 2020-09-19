/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public abstract class AbstractDragonSittingPhase
/*    */   extends AbstractDragonPhaseInstance {
/*    */   public AbstractDragonSittingPhase(EnderDragon debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSitting() {
/* 14 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public float onHurt(DamageSource debug1, float debug2) {
/* 19 */     if (debug1.getDirectEntity() instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
/* 20 */       debug1.getDirectEntity().setSecondsOnFire(1);
/* 21 */       return 0.0F;
/*    */     } 
/* 23 */     return super.onHurt(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonSittingPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */