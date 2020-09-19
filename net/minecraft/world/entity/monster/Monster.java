/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public abstract class Monster extends PathfinderMob implements Enemy {
/*     */   protected Monster(EntityType<? extends Monster> debug1, Level debug2) {
/*  30 */     super(debug1, debug2);
/*  31 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  36 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  41 */     updateSwingTime();
/*  42 */     updateNoActionTime();
/*  43 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateNoActionTime() {
/*  47 */     float debug1 = getBrightness();
/*  48 */     if (debug1 > 0.5F) {
/*  49 */       this.noActionTime += 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDespawnInPeaceful() {
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  60 */     return SoundEvents.HOSTILE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/*  65 */     return SoundEvents.HOSTILE_SPLASH;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/*  70 */     if (isInvulnerableTo(debug1)) {
/*  71 */       return false;
/*     */     }
/*  73 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  78 */     return SoundEvents.HOSTILE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  83 */     return SoundEvents.HOSTILE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFallDamageSound(int debug1) {
/*  88 */     if (debug1 > 4) {
/*  89 */       return SoundEvents.HOSTILE_BIG_FALL;
/*     */     }
/*  91 */     return SoundEvents.HOSTILE_SMALL_FALL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/*  97 */     return 0.5F - debug2.getBrightness(debug1);
/*     */   }
/*     */   
/*     */   public static boolean isDarkEnoughToSpawn(ServerLevelAccessor debug0, BlockPos debug1, Random debug2) {
/* 101 */     if (debug0.getBrightness(LightLayer.SKY, debug1) > debug2.nextInt(32)) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     int debug3 = debug0.getLevel().isThundering() ? debug0.getMaxLocalRawBrightness(debug1, 10) : debug0.getMaxLocalRawBrightness(debug1);
/* 106 */     return (debug3 <= debug2.nextInt(8));
/*     */   }
/*     */   
/*     */   public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> debug0, ServerLevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 110 */     return (debug1.getDifficulty() != Difficulty.PEACEFUL && 
/* 111 */       isDarkEnoughToSpawn(debug1, debug3, debug4) && 
/* 112 */       checkMobSpawnRules(debug0, (LevelAccessor)debug1, debug2, debug3, debug4));
/*     */   }
/*     */   
/*     */   public static boolean checkAnyLightMonsterSpawnRules(EntityType<? extends Monster> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 116 */     return (debug1.getDifficulty() != Difficulty.PEACEFUL && 
/* 117 */       checkMobSpawnRules(debug0, debug1, debug2, debug3, debug4));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createMonsterAttributes() {
/* 121 */     return Mob.createMobAttributes()
/* 122 */       .add(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDropExperience() {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDropLoot() {
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPreventingPlayerRest(Player debug1) {
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getProjectile(ItemStack debug1) {
/* 141 */     if (debug1.getItem() instanceof ProjectileWeaponItem) {
/* 142 */       Predicate<ItemStack> debug2 = ((ProjectileWeaponItem)debug1.getItem()).getSupportedHeldProjectiles();
/* 143 */       ItemStack debug3 = ProjectileWeaponItem.getHeldProjectile((LivingEntity)this, debug2);
/* 144 */       return debug3.isEmpty() ? new ItemStack((ItemLike)Items.ARROW) : debug3;
/*     */     } 
/* 146 */     return ItemStack.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Monster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */