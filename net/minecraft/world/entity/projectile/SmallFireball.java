/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class SmallFireball extends Fireball {
/*    */   public SmallFireball(EntityType<? extends SmallFireball> debug1, Level debug2) {
/* 18 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level debug1, LivingEntity debug2, double debug3, double debug5, double debug7) {
/* 22 */     super(EntityType.SMALL_FIREBALL, debug2, debug3, debug5, debug7, debug1);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12) {
/* 26 */     super(EntityType.SMALL_FIREBALL, debug2, debug4, debug6, debug8, debug10, debug12, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult debug1) {
/* 31 */     super.onHitEntity(debug1);
/* 32 */     if (this.level.isClientSide)
/* 33 */       return;  Entity debug2 = debug1.getEntity();
/* 34 */     if (!debug2.fireImmune()) {
/* 35 */       Entity debug3 = getOwner();
/* 36 */       int debug4 = debug2.getRemainingFireTicks();
/* 37 */       debug2.setSecondsOnFire(5);
/* 38 */       boolean debug5 = debug2.hurt(DamageSource.fireball(this, debug3), 5.0F);
/* 39 */       if (!debug5) {
/*    */ 
/*    */         
/* 42 */         debug2.setRemainingFireTicks(debug4);
/* 43 */       } else if (debug3 instanceof LivingEntity) {
/* 44 */         doEnchantDamageEffects((LivingEntity)debug3, debug2);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitBlock(BlockHitResult debug1) {
/* 51 */     super.onHitBlock(debug1);
/* 52 */     if (this.level.isClientSide)
/* 53 */       return;  Entity debug2 = getOwner();
/* 54 */     if (debug2 == null || !(debug2 instanceof net.minecraft.world.entity.Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 55 */       BlockHitResult debug3 = debug1;
/* 56 */       BlockPos debug4 = debug3.getBlockPos().relative(debug3.getDirection());
/* 57 */       if (this.level.isEmptyBlock(debug4)) {
/* 58 */         this.level.setBlockAndUpdate(debug4, BaseFireBlock.getState((BlockGetter)this.level, debug4));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 65 */     super.onHit(debug1);
/* 66 */     if (!this.level.isClientSide) {
/* 67 */       remove();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPickable() {
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurt(DamageSource debug1, float debug2) {
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\SmallFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */