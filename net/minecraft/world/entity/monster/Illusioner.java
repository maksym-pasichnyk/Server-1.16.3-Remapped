/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Illusioner
/*     */   extends SpellcasterIllager
/*     */   implements RangedAttackMob
/*     */ {
/*     */   private int clientSideIllusionTicks;
/*     */   private final Vec3[][] clientSideIllusionOffsets;
/*     */   
/*     */   public Illusioner(EntityType<? extends Illusioner> debug1, Level debug2) {
/*  54 */     super((EntityType)debug1, debug2);
/*     */     
/*  56 */     this.xpReward = 5;
/*     */     
/*  58 */     this.clientSideIllusionOffsets = new Vec3[2][4];
/*  59 */     for (int debug3 = 0; debug3 < 4; debug3++) {
/*  60 */       this.clientSideIllusionOffsets[0][debug3] = Vec3.ZERO;
/*  61 */       this.clientSideIllusionOffsets[1][debug3] = Vec3.ZERO;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  67 */     super.registerGoals();
/*     */     
/*  69 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  70 */     this.goalSelector.addGoal(1, new SpellcasterIllager.SpellcasterCastingSpellGoal(this));
/*  71 */     this.goalSelector.addGoal(4, new IllusionerMirrorSpellGoal());
/*  72 */     this.goalSelector.addGoal(5, new IllusionerBlindnessSpellGoal());
/*  73 */     this.goalSelector.addGoal(6, (Goal)new RangedBowAttackGoal((Monster)this, 0.5D, 20, 15.0F));
/*  74 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  75 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  76 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */     
/*  78 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  79 */     this.targetSelector.addGoal(2, (Goal)(new NearestAttackableTargetGoal((Mob)this, Player.class, true)).setUnseenMemoryTicks(300));
/*  80 */     this.targetSelector.addGoal(3, (Goal)(new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
/*  81 */     this.targetSelector.addGoal(3, (Goal)(new NearestAttackableTargetGoal((Mob)this, IronGolem.class, false)).setUnseenMemoryTicks(300));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  85 */     return Monster.createMonsterAttributes()
/*  86 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  87 */       .add(Attributes.FOLLOW_RANGE, 18.0D)
/*  88 */       .add(Attributes.MAX_HEALTH, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  93 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.BOW));
/*     */     
/*  95 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 100 */     super.defineSynchedData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 110 */     super.aiStep();
/*     */     
/* 112 */     if (this.level.isClientSide && isInvisible()) {
/* 113 */       this.clientSideIllusionTicks--;
/* 114 */       if (this.clientSideIllusionTicks < 0) {
/* 115 */         this.clientSideIllusionTicks = 0;
/*     */       }
/*     */       
/* 118 */       if (this.hurtTime == 1 || this.tickCount % 1200 == 0) {
/* 119 */         this.clientSideIllusionTicks = 3;
/*     */         
/* 121 */         float debug1 = -6.0F;
/* 122 */         int debug2 = 13;
/*     */         int debug3;
/* 124 */         for (debug3 = 0; debug3 < 4; debug3++) {
/* 125 */           this.clientSideIllusionOffsets[0][debug3] = this.clientSideIllusionOffsets[1][debug3];
/* 126 */           this.clientSideIllusionOffsets[1][debug3] = new Vec3((-6.0F + this.random.nextInt(13)) * 0.5D, Math.max(0, this.random.nextInt(6) - 4), (-6.0F + this.random.nextInt(13)) * 0.5D);
/*     */         } 
/* 128 */         for (debug3 = 0; debug3 < 16; debug3++) {
/* 129 */           this.level.addParticle((ParticleOptions)ParticleTypes.CLOUD, getRandomX(0.5D), getRandomY(), getZ(0.5D), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */         
/* 132 */         this.level.playLocalSound(getX(), getY(), getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, getSoundSource(), 1.0F, 1.0F, false);
/* 133 */       } else if (this.hurtTime == this.hurtDuration - 1) {
/* 134 */         this.clientSideIllusionTicks = 3;
/* 135 */         for (int debug1 = 0; debug1 < 4; debug1++) {
/* 136 */           this.clientSideIllusionOffsets[0][debug1] = this.clientSideIllusionOffsets[1][debug1];
/* 137 */           this.clientSideIllusionOffsets[1][debug1] = new Vec3(0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 145 */     return SoundEvents.ILLUSIONER_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAlliedTo(Entity debug1) {
/* 163 */     if (super.isAlliedTo(debug1)) {
/* 164 */       return true;
/*     */     }
/* 166 */     if (debug1 instanceof LivingEntity && ((LivingEntity)debug1).getMobType() == MobType.ILLAGER)
/*     */     {
/* 168 */       return (getTeam() == null && debug1.getTeam() == null);
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 175 */     return SoundEvents.ILLUSIONER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 180 */     return SoundEvents.ILLUSIONER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 185 */     return SoundEvents.ILLUSIONER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getCastingSoundEvent() {
/* 190 */     return SoundEvents.ILLUSIONER_CAST_SPELL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(int debug1, boolean debug2) {}
/*     */ 
/*     */   
/*     */   class IllusionerMirrorSpellGoal
/*     */     extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */   {
/*     */     public boolean canUse() {
/* 201 */       if (!super.canUse()) {
/* 202 */         return false;
/*     */       }
/* 204 */       if (Illusioner.this.hasEffect(MobEffects.INVISIBILITY)) {
/* 205 */         return false;
/*     */       }
/* 207 */       return true;
/*     */     }
/*     */     private IllusionerMirrorSpellGoal() {}
/*     */     
/*     */     protected int getCastingTime() {
/* 212 */       return 20;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingInterval() {
/* 217 */       return 340;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 222 */       Illusioner.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 228 */       return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 233 */       return SpellcasterIllager.IllagerSpell.DISAPPEAR;
/*     */     } }
/*     */   
/*     */   class IllusionerBlindnessSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private int lastTargetId;
/*     */     
/*     */     private IllusionerBlindnessSpellGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 242 */       if (!super.canUse()) {
/* 243 */         return false;
/*     */       }
/* 245 */       if (Illusioner.this.getTarget() == null) {
/* 246 */         return false;
/*     */       }
/* 248 */       if (Illusioner.this.getTarget().getId() == this.lastTargetId) {
/* 249 */         return false;
/*     */       }
/* 251 */       if (!Illusioner.this.level.getCurrentDifficultyAt(Illusioner.this.blockPosition()).isHarderThan(Difficulty.NORMAL.ordinal())) {
/* 252 */         return false;
/*     */       }
/* 254 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 259 */       super.start();
/*     */       
/* 261 */       this.lastTargetId = Illusioner.this.getTarget().getId();
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingTime() {
/* 266 */       return 20;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getCastingInterval() {
/* 271 */       return 180;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void performSpellCasting() {
/* 276 */       Illusioner.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
/*     */     }
/*     */ 
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 281 */       return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 286 */       return SpellcasterIllager.IllagerSpell.BLINDNESS;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 292 */     ItemStack debug3 = getProjectile(getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, Items.BOW)));
/* 293 */     AbstractArrow debug4 = ProjectileUtil.getMobArrow((LivingEntity)this, debug3, debug2);
/*     */     
/* 295 */     double debug5 = debug1.getX() - getX();
/* 296 */     double debug7 = debug1.getY(0.3333333333333333D) - debug4.getY();
/* 297 */     double debug9 = debug1.getZ() - getZ();
/* 298 */     double debug11 = Mth.sqrt(debug5 * debug5 + debug9 * debug9);
/* 299 */     debug4.shoot(debug5, debug7 + debug11 * 0.20000000298023224D, debug9, 1.6F, (14 - this.level.getDifficulty().getId() * 4));
/* 300 */     playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/* 301 */     this.level.addFreshEntity((Entity)debug4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Illusioner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */