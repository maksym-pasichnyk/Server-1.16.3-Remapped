/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Spider extends Monster {
/*  47 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   public Spider(EntityType<? extends Spider> debug1, Level debug2) {
/*  50 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  55 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*     */     
/*  57 */     this.goalSelector.addGoal(3, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  58 */     this.goalSelector.addGoal(4, (Goal)new SpiderAttackGoal(this));
/*     */     
/*  60 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal(this, 0.8D));
/*  61 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  62 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  64 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*  65 */     this.targetSelector.addGoal(2, (Goal)new SpiderTargetGoal<>(this, Player.class));
/*  66 */     this.targetSelector.addGoal(3, (Goal)new SpiderTargetGoal<>(this, IronGolem.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/*  71 */     return (getBbHeight() * 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/*  76 */     return (PathNavigation)new WallClimberNavigation((Mob)this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  81 */     super.defineSynchedData();
/*     */     
/*  83 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  88 */     super.tick();
/*     */     
/*  90 */     if (!this.level.isClientSide)
/*     */     {
/*     */       
/*  93 */       setClimbing(this.horizontalCollision);
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  98 */     return Monster.createMonsterAttributes()
/*  99 */       .add(Attributes.MAX_HEALTH, 16.0D)
/* 100 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 105 */     return SoundEvents.SPIDER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 110 */     return SoundEvents.SPIDER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 115 */     return SoundEvents.SPIDER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 120 */     playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onClimbable() {
/* 129 */     return isClimbing();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeStuckInBlock(BlockState debug1, Vec3 debug2) {
/* 135 */     if (!debug1.is(Blocks.COBWEB)) {
/* 136 */       super.makeStuckInBlock(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 142 */     return MobType.ARTHROPOD;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance debug1) {
/* 147 */     if (debug1.getEffect() == MobEffects.POISON) {
/* 148 */       return false;
/*     */     }
/* 150 */     return super.canBeAffected(debug1);
/*     */   }
/*     */   
/*     */   public boolean isClimbing() {
/* 154 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setClimbing(boolean debug1) {
/* 158 */     byte debug2 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 159 */     if (debug1) {
/* 160 */       debug2 = (byte)(debug2 | 0x1);
/*     */     } else {
/* 162 */       debug2 = (byte)(debug2 & 0xFFFFFFFE);
/*     */     } 
/* 164 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 170 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 172 */     if (debug1.getRandom().nextInt(100) == 0) {
/* 173 */       Skeleton debug6 = (Skeleton)EntityType.SKELETON.create(this.level);
/* 174 */       debug6.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
/* 175 */       debug6.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)null, (CompoundTag)null);
/* 176 */       debug6.startRiding((Entity)this);
/*     */     } 
/*     */     
/* 179 */     if (debug4 == null) {
/* 180 */       debug4 = new SpiderEffectsGroupData();
/*     */       
/* 182 */       if (debug1.getDifficulty() == Difficulty.HARD && debug1.getRandom().nextFloat() < 0.1F * debug2.getSpecialMultiplier()) {
/* 183 */         ((SpiderEffectsGroupData)debug4).setRandomEffect(debug1.getRandom());
/*     */       }
/*     */     } 
/* 186 */     if (debug4 instanceof SpiderEffectsGroupData) {
/* 187 */       MobEffect debug6 = ((SpiderEffectsGroupData)debug4).effect;
/* 188 */       if (debug6 != null) {
/* 189 */         addEffect(new MobEffectInstance(debug6, 2147483647));
/*     */       }
/*     */     } 
/*     */     
/* 193 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 198 */     return 0.65F;
/*     */   }
/*     */   
/*     */   public static class SpiderEffectsGroupData
/*     */     implements SpawnGroupData
/*     */   {
/*     */     public MobEffect effect;
/*     */     
/*     */     public void setRandomEffect(Random debug1) {
/* 207 */       int debug2 = debug1.nextInt(5);
/* 208 */       if (debug2 <= 1) {
/* 209 */         this.effect = MobEffects.MOVEMENT_SPEED;
/* 210 */       } else if (debug2 <= 2) {
/* 211 */         this.effect = MobEffects.DAMAGE_BOOST;
/* 212 */       } else if (debug2 <= 3) {
/* 213 */         this.effect = MobEffects.REGENERATION;
/* 214 */       } else if (debug2 <= 4) {
/* 215 */         this.effect = MobEffects.INVISIBILITY;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class SpiderAttackGoal extends MeleeAttackGoal {
/*     */     public SpiderAttackGoal(Spider debug1) {
/* 222 */       super(debug1, 1.0D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 227 */       return (super.canUse() && !this.mob.isVehicle());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 232 */       float debug1 = this.mob.getBrightness();
/* 233 */       if (debug1 >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
/* 234 */         this.mob.setTarget(null);
/* 235 */         return false;
/*     */       } 
/* 237 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getAttackReachSqr(LivingEntity debug1) {
/* 242 */       return (4.0F + debug1.getBbWidth());
/*     */     }
/*     */   }
/*     */   
/*     */   static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*     */     public SpiderTargetGoal(Spider debug1, Class<T> debug2) {
/* 248 */       super((Mob)debug1, debug2, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 253 */       float debug1 = this.mob.getBrightness();
/* 254 */       if (debug1 >= 0.5F) {
/* 255 */         return false;
/*     */       }
/*     */       
/* 258 */       return super.canUse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Spider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */