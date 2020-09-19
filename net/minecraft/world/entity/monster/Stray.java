/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public class Stray
/*    */   extends AbstractSkeleton
/*    */ {
/*    */   public Stray(EntityType<? extends Stray> debug1, Level debug2) {
/* 22 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public static boolean checkStraySpawnRules(EntityType<Stray> debug0, ServerLevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 26 */     return (checkMonsterSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4) && (debug2 == MobSpawnType.SPAWNER || debug1
/* 27 */       .canSeeSky(debug3)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 32 */     return SoundEvents.STRAY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 37 */     return SoundEvents.STRAY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 42 */     return SoundEvents.STRAY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 47 */     return SoundEvents.STRAY_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack debug1, float debug2) {
/* 52 */     AbstractArrow debug3 = super.getArrow(debug1, debug2);
/* 53 */     if (debug3 instanceof Arrow) {
/* 54 */       ((Arrow)debug3).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
/*    */     }
/* 56 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Stray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */