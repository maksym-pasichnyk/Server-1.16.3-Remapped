/*     */ package net.minecraft.world.entity.monster;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ 
/*     */ public class WitherSkeleton extends AbstractSkeleton {
/*     */   public WitherSkeleton(EntityType<? extends WitherSkeleton> debug1, Level debug2) {
/*  33 */     super((EntityType)debug1, debug2);
/*     */     
/*  35 */     setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  40 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractPiglin.class, true));
/*  41 */     super.registerGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  46 */     return SoundEvents.WITHER_SKELETON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  51 */     return SoundEvents.WITHER_SKELETON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  56 */     return SoundEvents.WITHER_SKELETON_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   SoundEvent getStepSound() {
/*  61 */     return SoundEvents.WITHER_SKELETON_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/*  66 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/*  67 */     Entity debug4 = debug1.getEntity();
/*  68 */     if (debug4 instanceof Creeper) {
/*  69 */       Creeper debug5 = (Creeper)debug4;
/*  70 */       if (debug5.canDropMobsSkull()) {
/*  71 */         debug5.increaseDroppedSkulls();
/*  72 */         spawnAtLocation((ItemLike)Items.WITHER_SKELETON_SKULL);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/*  79 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.STONE_SWORD));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentEnchantments(DifficultyInstance debug1) {}
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  89 */     SpawnGroupData debug6 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/*  91 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
/*     */     
/*  93 */     reassessWeaponGoal();
/*     */     
/*  95 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 100 */     return 2.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 105 */     if (!super.doHurtTarget(debug1)) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     if (debug1 instanceof LivingEntity) {
/* 110 */       ((LivingEntity)debug1).addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
/*     */     }
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack debug1, float debug2) {
/* 117 */     AbstractArrow debug3 = super.getArrow(debug1, debug2);
/* 118 */     debug3.setSecondsOnFire(100);
/* 119 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance debug1) {
/* 124 */     if (debug1.getEffect() == MobEffects.WITHER) {
/* 125 */       return false;
/*     */     }
/* 127 */     return super.canBeAffected(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\WitherSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */