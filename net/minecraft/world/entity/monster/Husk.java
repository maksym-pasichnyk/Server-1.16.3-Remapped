/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ 
/*    */ public class Husk
/*    */   extends Zombie
/*    */ {
/*    */   public Husk(EntityType<? extends Husk> debug1, Level debug2) {
/* 23 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public static boolean checkHuskSpawnRules(EntityType<Husk> debug0, ServerLevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 27 */     return (checkMonsterSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4) && (debug2 == MobSpawnType.SPAWNER || debug1
/* 28 */       .canSeeSky(debug3)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSunSensitive() {
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 38 */     return SoundEvents.HUSK_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 43 */     return SoundEvents.HUSK_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 48 */     return SoundEvents.HUSK_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getStepSound() {
/* 53 */     return SoundEvents.HUSK_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doHurtTarget(Entity debug1) {
/* 58 */     boolean debug2 = super.doHurtTarget(debug1);
/* 59 */     if (debug2 && getMainHandItem().isEmpty() && debug1 instanceof LivingEntity) {
/* 60 */       float debug3 = this.level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/* 61 */       ((LivingEntity)debug1).addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)debug3));
/*    */     } 
/*    */     
/* 64 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean convertsInWater() {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doUnderWaterConversion() {
/* 74 */     convertToZombieType(EntityType.ZOMBIE);
/* 75 */     if (!isSilent()) {
/* 76 */       this.level.levelEvent(null, 1041, blockPosition(), 0);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getSkull() {
/* 82 */     return ItemStack.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Husk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */