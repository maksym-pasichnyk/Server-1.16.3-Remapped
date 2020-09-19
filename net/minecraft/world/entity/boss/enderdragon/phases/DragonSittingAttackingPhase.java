/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ 
/*    */ public class DragonSittingAttackingPhase
/*    */   extends AbstractDragonSittingPhase
/*    */ {
/*    */   private int attackingTicks;
/*    */   
/*    */   public DragonSittingAttackingPhase(EnderDragon debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doClientTick() {
/* 18 */     this.dragon.level.playLocalSound(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.dragon.getSoundSource(), 2.5F, 0.8F + this.dragon.getRandom().nextFloat() * 0.3F, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick() {
/* 23 */     if (this.attackingTicks++ >= 40) {
/* 24 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_FLAMING);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 30 */     this.attackingTicks = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnderDragonPhase<DragonSittingAttackingPhase> getPhase() {
/* 35 */     return EnderDragonPhase.SITTING_ATTACKING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingAttackingPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */