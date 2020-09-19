/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class IronGolem
/*     */   extends AbstractGolem
/*     */   implements NeutralMob
/*     */ {
/*  60 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private int attackAnimationTick;
/*     */   
/*     */   private int offerFlowerTick;
/*     */   
/*  66 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private int remainingPersistentAngerTime;
/*     */   private UUID persistentAngerTarget;
/*     */   
/*     */   public IronGolem(EntityType<? extends IronGolem> debug1, Level debug2) {
/*  71 */     super((EntityType)debug1, debug2);
/*  72 */     this.maxUpStep = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  77 */     this.goalSelector.addGoal(1, (Goal)new MeleeAttackGoal(this, 1.0D, true));
/*  78 */     this.goalSelector.addGoal(2, (Goal)new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
/*  79 */     this.goalSelector.addGoal(2, (Goal)new MoveBackToVillageGoal(this, 0.6D, false));
/*  80 */     this.goalSelector.addGoal(4, (Goal)new GolemRandomStrollInVillageGoal(this, 0.6D));
/*  81 */     this.goalSelector.addGoal(5, (Goal)new OfferFlowerGoal(this));
/*  82 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  83 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  85 */     this.targetSelector.addGoal(1, (Goal)new DefendVillageTargetGoal(this));
/*  86 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*  87 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  88 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 5, false, false, debug0 -> (debug0 instanceof net.minecraft.world.entity.monster.Enemy && !(debug0 instanceof net.minecraft.world.entity.monster.Creeper))));
/*  89 */     this.targetSelector.addGoal(4, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  94 */     super.defineSynchedData();
/*  95 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  99 */     return Mob.createMobAttributes()
/* 100 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 101 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 102 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/* 103 */       .add(Attributes.ATTACK_DAMAGE, 15.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int decreaseAirSupply(int debug1) {
/* 109 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPush(Entity debug1) {
/* 114 */     if (debug1 instanceof net.minecraft.world.entity.monster.Enemy && !(debug1 instanceof net.minecraft.world.entity.monster.Creeper) && 
/* 115 */       getRandom().nextInt(20) == 0) {
/* 116 */       setTarget((LivingEntity)debug1);
/*     */     }
/*     */     
/* 119 */     super.doPush(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 124 */     super.aiStep();
/*     */     
/* 126 */     if (this.attackAnimationTick > 0) {
/* 127 */       this.attackAnimationTick--;
/*     */     }
/* 129 */     if (this.offerFlowerTick > 0) {
/* 130 */       this.offerFlowerTick--;
/*     */     }
/*     */     
/* 133 */     if (getHorizontalDistanceSqr(getDeltaMovement()) > 2.500000277905201E-7D && this.random.nextInt(5) == 0) {
/* 134 */       int debug1 = Mth.floor(getX());
/* 135 */       int debug2 = Mth.floor(getY() - 0.20000000298023224D);
/* 136 */       int debug3 = Mth.floor(getZ());
/* 137 */       BlockState debug4 = this.level.getBlockState(new BlockPos(debug1, debug2, debug3));
/* 138 */       if (!debug4.isAir()) {
/* 139 */         this.level.addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, debug4), getX() + (this.random.nextFloat() - 0.5D) * getBbWidth(), getY() + 0.1D, getZ() + (this.random.nextFloat() - 0.5D) * getBbWidth(), 4.0D * (this.random.nextFloat() - 0.5D), 0.5D, (this.random.nextFloat() - 0.5D) * 4.0D);
/*     */       }
/*     */     } 
/*     */     
/* 143 */     if (!this.level.isClientSide) {
/* 144 */       updatePersistentAnger((ServerLevel)this.level, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackType(EntityType<?> debug1) {
/* 150 */     if (isPlayerCreated() && debug1 == EntityType.PLAYER) {
/* 151 */       return false;
/*     */     }
/* 153 */     if (debug1 == EntityType.CREEPER) {
/* 154 */       return false;
/*     */     }
/* 156 */     return super.canAttackType(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 161 */     super.addAdditionalSaveData(debug1);
/* 162 */     debug1.putBoolean("PlayerCreated", isPlayerCreated());
/* 163 */     addPersistentAngerSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 168 */     super.readAdditionalSaveData(debug1);
/* 169 */     setPlayerCreated(debug1.getBoolean("PlayerCreated"));
/* 170 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 175 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemainingPersistentAngerTime(int debug1) {
/* 180 */     this.remainingPersistentAngerTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingPersistentAngerTime() {
/* 185 */     return this.remainingPersistentAngerTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/* 190 */     this.persistentAngerTarget = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getPersistentAngerTarget() {
/* 195 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   private float getAttackDamage() {
/* 199 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 204 */     this.attackAnimationTick = 10;
/* 205 */     this.level.broadcastEntityEvent((Entity)this, (byte)4);
/* 206 */     float debug2 = getAttackDamage();
/* 207 */     float debug3 = ((int)debug2 > 0) ? (debug2 / 2.0F + this.random.nextInt((int)debug2)) : debug2;
/* 208 */     boolean debug4 = debug1.hurt(DamageSource.mobAttack((LivingEntity)this), debug3);
/* 209 */     if (debug4) {
/* 210 */       debug1.setDeltaMovement(debug1.getDeltaMovement().add(0.0D, 0.4000000059604645D, 0.0D));
/* 211 */       doEnchantDamageEffects((LivingEntity)this, debug1);
/*     */     } 
/* 213 */     playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 214 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 219 */     Crackiness debug3 = getCrackiness();
/* 220 */     boolean debug4 = super.hurt(debug1, debug2);
/* 221 */     if (debug4 && getCrackiness() != debug3) {
/* 222 */       playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
/*     */     }
/* 224 */     return debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Crackiness getCrackiness() {
/* 231 */     return Crackiness.byFraction(getHealth() / getMaxHealth());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offerFlower(boolean debug1) {
/* 253 */     if (debug1) {
/* 254 */       this.offerFlowerTick = 400;
/* 255 */       this.level.broadcastEntityEvent((Entity)this, (byte)11);
/*     */     } else {
/* 257 */       this.offerFlowerTick = 0;
/* 258 */       this.level.broadcastEntityEvent((Entity)this, (byte)34);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 264 */     return SoundEvents.IRON_GOLEM_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 269 */     return SoundEvents.IRON_GOLEM_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 274 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 275 */     Item debug4 = debug3.getItem();
/* 276 */     if (debug4 != Items.IRON_INGOT) {
/* 277 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 280 */     float debug5 = getHealth();
/* 281 */     heal(25.0F);
/* 282 */     if (getHealth() == debug5) {
/* 283 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 286 */     float debug6 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
/* 287 */     playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, debug6);
/*     */     
/* 289 */     if (!debug1.abilities.instabuild) {
/* 290 */       debug3.shrink(1);
/*     */     }
/* 292 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 297 */     playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPlayerCreated() {
/* 305 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setPlayerCreated(boolean debug1) {
/* 309 */     byte debug2 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 310 */     if (debug1) {
/* 311 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 | 0x1)));
/*     */     } else {
/* 313 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void die(DamageSource debug1) {
/* 320 */     super.die(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 326 */     BlockPos debug2 = blockPosition();
/* 327 */     BlockPos debug3 = debug2.below();
/* 328 */     BlockState debug4 = debug1.getBlockState(debug3);
/* 329 */     if (debug4.entityCanStandOn((BlockGetter)debug1, debug3, (Entity)this)) {
/* 330 */       for (int debug5 = 1; debug5 < 3; debug5++) {
/* 331 */         BlockPos debug6 = debug2.above(debug5);
/* 332 */         BlockState debug7 = debug1.getBlockState(debug6);
/* 333 */         if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)debug1, debug6, debug7, debug7.getFluidState(), EntityType.IRON_GOLEM)) {
/* 334 */           return false;
/*     */         }
/*     */       } 
/* 337 */       return (NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)debug1, debug2, debug1.getBlockState(debug2), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && debug1
/* 338 */         .isUnobstructed((Entity)this));
/*     */     } 
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Crackiness
/*     */   {
/* 349 */     NONE(1.0F),
/* 350 */     LOW(0.75F),
/* 351 */     MEDIUM(0.5F),
/* 352 */     HIGH(0.25F); private static final List<Crackiness> BY_DAMAGE;
/*     */     
/*     */     static {
/* 355 */       BY_DAMAGE = (List<Crackiness>)Stream.<Crackiness>of(values()).sorted(Comparator.comparingDouble(debug0 -> debug0.fraction)).collect(ImmutableList.toImmutableList());
/*     */     }
/*     */     private final float fraction;
/*     */     
/*     */     Crackiness(float debug3) {
/* 360 */       this.fraction = debug3;
/*     */     }
/*     */     
/*     */     public static Crackiness byFraction(float debug0) {
/* 364 */       for (Crackiness debug2 : BY_DAMAGE) {
/* 365 */         if (debug0 < debug2.fraction) {
/* 366 */           return debug2;
/*     */         }
/*     */       } 
/*     */       
/* 370 */       return NONE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\IronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */