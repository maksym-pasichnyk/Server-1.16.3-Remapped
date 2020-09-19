/*    */ package net.minecraft.world.entity.projectile;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpectralArrow extends AbstractArrow {
/* 15 */   private int duration = 200;
/*    */   
/*    */   public SpectralArrow(EntityType<? extends SpectralArrow> debug1, Level debug2) {
/* 18 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level debug1, LivingEntity debug2) {
/* 22 */     super(EntityType.SPECTRAL_ARROW, debug2, debug1);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level debug1, double debug2, double debug4, double debug6) {
/* 26 */     super(EntityType.SPECTRAL_ARROW, debug2, debug4, debug6, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 31 */     super.tick();
/*    */     
/* 33 */     if (this.level.isClientSide && !this.inGround) {
/* 34 */       this.level.addParticle((ParticleOptions)ParticleTypes.INSTANT_EFFECT, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getPickupItem() {
/* 40 */     return new ItemStack((ItemLike)Items.SPECTRAL_ARROW);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doPostHurtEffects(LivingEntity debug1) {
/* 45 */     super.doPostHurtEffects(debug1);
/*    */     
/* 47 */     MobEffectInstance debug2 = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
/* 48 */     debug1.addEffect(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 53 */     super.readAdditionalSaveData(debug1);
/* 54 */     if (debug1.contains("Duration")) {
/* 55 */       this.duration = debug1.getInt("Duration");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 61 */     super.addAdditionalSaveData(debug1);
/* 62 */     debug1.putInt("Duration", this.duration);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\SpectralArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */