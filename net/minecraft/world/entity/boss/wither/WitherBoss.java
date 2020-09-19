/*     */ package net.minecraft.world.entity.boss.wither;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.WitherSkull;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class WitherBoss
/*     */   extends Monster
/*     */   implements RangedAttackMob
/*     */ {
/*  61 */   private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  62 */   private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  63 */   private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  64 */   private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = (List<EntityDataAccessor<Integer>>)ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
/*  65 */   private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*  67 */   private final float[] xRotHeads = new float[2];
/*  68 */   private final float[] yRotHeads = new float[2];
/*  69 */   private final float[] xRotOHeads = new float[2];
/*  70 */   private final float[] yRotOHeads = new float[2];
/*  71 */   private final int[] nextHeadUpdate = new int[2];
/*  72 */   private final int[] idleHeadUpdates = new int[2]; private int destroyBlocksTick;
/*     */   private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
/*  74 */   private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
/*     */   static {
/*  76 */     LIVING_ENTITY_SELECTOR = (debug0 -> (debug0.getMobType() != MobType.UNDEAD && debug0.attackable()));
/*  77 */   } private static final TargetingConditions TARGETING_CONDITIONS = (new TargetingConditions()).range(20.0D).selector(LIVING_ENTITY_SELECTOR);
/*     */   
/*     */   public WitherBoss(EntityType<? extends WitherBoss> debug1, Level debug2) {
/*  80 */     super(debug1, debug2);
/*     */     
/*  82 */     setHealth(getMaxHealth());
/*     */     
/*  84 */     getNavigation().setCanFloat(true);
/*     */     
/*  86 */     this.xpReward = 50;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  91 */     this.goalSelector.addGoal(0, new WitherDoNothingGoal());
/*  92 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 40, 20.0F));
/*     */     
/*  94 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  95 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  96 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  98 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
/*  99 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 0, false, false, LIVING_ENTITY_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 104 */     super.defineSynchedData();
/*     */     
/* 106 */     this.entityData.define(DATA_TARGET_A, Integer.valueOf(0));
/* 107 */     this.entityData.define(DATA_TARGET_B, Integer.valueOf(0));
/* 108 */     this.entityData.define(DATA_TARGET_C, Integer.valueOf(0));
/* 109 */     this.entityData.define(DATA_ID_INV, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 114 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 116 */     debug1.putInt("Invul", getInvulnerableTicks());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 121 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 123 */     setInvulnerableTicks(debug1.getInt("Invul"));
/* 124 */     if (hasCustomName()) {
/* 125 */       this.bossEvent.setName(getDisplayName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(@Nullable Component debug1) {
/* 131 */     super.setCustomName(debug1);
/* 132 */     this.bossEvent.setName(getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 137 */     return SoundEvents.WITHER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 142 */     return SoundEvents.WITHER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 147 */     return SoundEvents.WITHER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 152 */     Vec3 debug1 = getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
/*     */     
/* 154 */     if (!this.level.isClientSide && getAlternativeTarget(0) > 0) {
/* 155 */       Entity entity = this.level.getEntity(getAlternativeTarget(0));
/* 156 */       if (entity != null) {
/* 157 */         double d = debug1.y;
/* 158 */         if (getY() < entity.getY() || (!isPowered() && getY() < entity.getY() + 5.0D)) {
/* 159 */           d = Math.max(0.0D, d);
/*     */           
/* 161 */           d += 0.3D - d * 0.6000000238418579D;
/*     */         } 
/* 163 */         debug1 = new Vec3(debug1.x, d, debug1.z);
/*     */         
/* 165 */         Vec3 debug5 = new Vec3(entity.getX() - getX(), 0.0D, entity.getZ() - getZ());
/* 166 */         if (getHorizontalDistanceSqr(debug5) > 9.0D) {
/* 167 */           Vec3 debug6 = debug5.normalize();
/* 168 */           debug1 = debug1.add(debug6.x * 0.3D - debug1.x * 0.6D, 0.0D, debug6.z * 0.3D - debug1.z * 0.6D);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     setDeltaMovement(debug1);
/* 178 */     if (getHorizontalDistanceSqr(debug1) > 0.05D) {
/* 179 */       this.yRot = (float)Mth.atan2(debug1.z, debug1.x) * 57.295776F - 90.0F;
/*     */     }
/* 181 */     super.aiStep();
/*     */     int i;
/* 183 */     for (i = 0; i < 2; i++) {
/* 184 */       this.yRotOHeads[i] = this.yRotHeads[i];
/* 185 */       this.xRotOHeads[i] = this.xRotHeads[i];
/*     */     } 
/*     */     
/* 188 */     for (i = 0; i < 2; i++) {
/* 189 */       int j = getAlternativeTarget(i + 1);
/* 190 */       Entity debug4 = null;
/* 191 */       if (j > 0) {
/* 192 */         debug4 = this.level.getEntity(j);
/*     */       }
/* 194 */       if (debug4 != null) {
/* 195 */         double debug5 = getHeadX(i + 1);
/* 196 */         double debug7 = getHeadY(i + 1);
/* 197 */         double debug9 = getHeadZ(i + 1);
/*     */         
/* 199 */         double debug11 = debug4.getX() - debug5;
/* 200 */         double debug13 = debug4.getEyeY() - debug7;
/* 201 */         double debug15 = debug4.getZ() - debug9;
/* 202 */         double debug17 = Mth.sqrt(debug11 * debug11 + debug15 * debug15);
/*     */         
/* 204 */         float debug19 = (float)(Mth.atan2(debug15, debug11) * 57.2957763671875D) - 90.0F;
/* 205 */         float debug20 = (float)-(Mth.atan2(debug13, debug17) * 57.2957763671875D);
/* 206 */         this.xRotHeads[i] = rotlerp(this.xRotHeads[i], debug20, 40.0F);
/* 207 */         this.yRotHeads[i] = rotlerp(this.yRotHeads[i], debug19, 10.0F);
/*     */       } else {
/* 209 */         this.yRotHeads[i] = rotlerp(this.yRotHeads[i], this.yBodyRot, 10.0F);
/*     */       } 
/*     */     } 
/* 212 */     boolean debug2 = isPowered(); int debug3;
/* 213 */     for (debug3 = 0; debug3 < 3; debug3++) {
/* 214 */       double debug4 = getHeadX(debug3);
/* 215 */       double debug6 = getHeadY(debug3);
/* 216 */       double debug8 = getHeadZ(debug3);
/*     */       
/* 218 */       this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, debug4 + this.random.nextGaussian() * 0.30000001192092896D, debug6 + this.random.nextGaussian() * 0.30000001192092896D, debug8 + this.random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
/* 219 */       if (debug2 && this.level.random.nextInt(4) == 0) {
/* 220 */         this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, debug4 + this.random.nextGaussian() * 0.30000001192092896D, debug6 + this.random.nextGaussian() * 0.30000001192092896D, debug8 + this.random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
/*     */       }
/*     */     } 
/* 223 */     if (getInvulnerableTicks() > 0) {
/* 224 */       for (debug3 = 0; debug3 < 3; debug3++) {
/* 225 */         this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, getX() + this.random.nextGaussian(), getY() + (this.random.nextFloat() * 3.3F), getZ() + this.random.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 232 */     if (getInvulnerableTicks() > 0) {
/* 233 */       int i = getInvulnerableTicks() - 1;
/*     */       
/* 235 */       if (i <= 0) {
/* 236 */         Explosion.BlockInteraction debug2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
/* 237 */         this.level.explode((Entity)this, getX(), getEyeY(), getZ(), 7.0F, false, debug2);
/* 238 */         if (!isSilent()) {
/* 239 */           this.level.globalLevelEvent(1023, blockPosition(), 0);
/*     */         }
/*     */       } 
/*     */       
/* 243 */       setInvulnerableTicks(i);
/* 244 */       if (this.tickCount % 10 == 0) {
/* 245 */         heal(10.0F);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 251 */     super.customServerAiStep();
/*     */     int debug1;
/* 253 */     for (debug1 = 1; debug1 < 3; debug1++) {
/* 254 */       if (this.tickCount >= this.nextHeadUpdate[debug1 - 1]) {
/* 255 */         this.nextHeadUpdate[debug1 - 1] = this.tickCount + 10 + this.random.nextInt(10);
/*     */         
/* 257 */         this.idleHeadUpdates[debug1 - 1] = this.idleHeadUpdates[debug1 - 1] + 1; if ((this.level.getDifficulty() == Difficulty.NORMAL || this.level.getDifficulty() == Difficulty.HARD) && this.idleHeadUpdates[debug1 - 1] > 15) {
/* 258 */           float f1 = 10.0F;
/* 259 */           float debug3 = 5.0F;
/* 260 */           double debug4 = Mth.nextDouble(this.random, getX() - 10.0D, getX() + 10.0D);
/* 261 */           double debug6 = Mth.nextDouble(this.random, getY() - 5.0D, getY() + 5.0D);
/* 262 */           double debug8 = Mth.nextDouble(this.random, getZ() - 10.0D, getZ() + 10.0D);
/* 263 */           performRangedAttack(debug1 + 1, debug4, debug6, debug8, true);
/* 264 */           this.idleHeadUpdates[debug1 - 1] = 0;
/*     */         } 
/*     */         
/* 267 */         int debug2 = getAlternativeTarget(debug1);
/* 268 */         if (debug2 > 0) {
/* 269 */           Entity debug3 = this.level.getEntity(debug2);
/* 270 */           if (debug3 == null || !debug3.isAlive() || distanceToSqr(debug3) > 900.0D || !canSee(debug3)) {
/* 271 */             setAlternativeTarget(debug1, 0);
/*     */           }
/* 273 */           else if (debug3 instanceof Player && ((Player)debug3).abilities.invulnerable) {
/* 274 */             setAlternativeTarget(debug1, 0);
/*     */           } else {
/* 276 */             performRangedAttack(debug1 + 1, (LivingEntity)debug3);
/* 277 */             this.nextHeadUpdate[debug1 - 1] = this.tickCount + 40 + this.random.nextInt(20);
/* 278 */             this.idleHeadUpdates[debug1 - 1] = 0;
/*     */           } 
/*     */         } else {
/*     */           
/* 282 */           List<LivingEntity> debug3 = this.level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, (LivingEntity)this, getBoundingBox().inflate(20.0D, 8.0D, 20.0D));
/*     */           
/* 284 */           for (int debug4 = 0; debug4 < 10 && !debug3.isEmpty(); debug4++) {
/* 285 */             LivingEntity debug5 = debug3.get(this.random.nextInt(debug3.size()));
/*     */             
/* 287 */             if (debug5 != this && debug5.isAlive() && canSee((Entity)debug5)) {
/* 288 */               if (debug5 instanceof Player) {
/* 289 */                 if (!((Player)debug5).abilities.invulnerable) {
/* 290 */                   setAlternativeTarget(debug1, debug5.getId());
/*     */                 }
/*     */                 break;
/*     */               } 
/* 294 */               setAlternativeTarget(debug1, debug5.getId());
/*     */               
/*     */               break;
/*     */             } 
/*     */             
/* 299 */             debug3.remove(debug5);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 304 */     if (getTarget() != null) {
/* 305 */       setAlternativeTarget(0, getTarget().getId());
/*     */     } else {
/* 307 */       setAlternativeTarget(0, 0);
/*     */     } 
/*     */     
/* 310 */     if (this.destroyBlocksTick > 0) {
/* 311 */       this.destroyBlocksTick--;
/*     */       
/* 313 */       if (this.destroyBlocksTick == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*     */ 
/*     */ 
/*     */         
/* 317 */         debug1 = Mth.floor(getY());
/* 318 */         int debug2 = Mth.floor(getX());
/* 319 */         int debug3 = Mth.floor(getZ());
/* 320 */         boolean debug4 = false;
/*     */         
/* 322 */         for (int debug5 = -1; debug5 <= 1; debug5++) {
/* 323 */           for (int debug6 = -1; debug6 <= 1; debug6++) {
/* 324 */             for (int debug7 = 0; debug7 <= 3; debug7++) {
/* 325 */               int debug8 = debug2 + debug5;
/* 326 */               int debug9 = debug1 + debug7;
/* 327 */               int debug10 = debug3 + debug6;
/* 328 */               BlockPos debug11 = new BlockPos(debug8, debug9, debug10);
/* 329 */               BlockState debug12 = this.level.getBlockState(debug11);
/* 330 */               if (canDestroy(debug12)) {
/* 331 */                 debug4 = (this.level.destroyBlock(debug11, true, (Entity)this) || debug4);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 336 */         if (debug4) {
/* 337 */           this.level.levelEvent(null, 1022, blockPosition(), 0);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     if (this.tickCount % 20 == 0) {
/* 343 */       heal(1.0F);
/*     */     }
/*     */     
/* 346 */     this.bossEvent.setPercent(getHealth() / getMaxHealth());
/*     */   }
/*     */   
/*     */   public static boolean canDestroy(BlockState debug0) {
/* 350 */     return (!debug0.isAir() && !BlockTags.WITHER_IMMUNE.contains(debug0.getBlock()));
/*     */   }
/*     */   
/*     */   public void makeInvulnerable() {
/* 354 */     setInvulnerableTicks(220);
/* 355 */     setHealth(getMaxHealth() / 3.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeStuckInBlock(BlockState debug1, Vec3 debug2) {}
/*     */ 
/*     */   
/*     */   public void startSeenByPlayer(ServerPlayer debug1) {
/* 364 */     super.startSeenByPlayer(debug1);
/* 365 */     this.bossEvent.addPlayer(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopSeenByPlayer(ServerPlayer debug1) {
/* 370 */     super.stopSeenByPlayer(debug1);
/* 371 */     this.bossEvent.removePlayer(debug1);
/*     */   }
/*     */   
/*     */   private double getHeadX(int debug1) {
/* 375 */     if (debug1 <= 0) {
/* 376 */       return getX();
/*     */     }
/* 378 */     float debug2 = (this.yBodyRot + (180 * (debug1 - 1))) * 0.017453292F;
/* 379 */     float debug3 = Mth.cos(debug2);
/* 380 */     return getX() + debug3 * 1.3D;
/*     */   }
/*     */   
/*     */   private double getHeadY(int debug1) {
/* 384 */     if (debug1 <= 0) {
/* 385 */       return getY() + 3.0D;
/*     */     }
/* 387 */     return getY() + 2.2D;
/*     */   }
/*     */ 
/*     */   
/*     */   private double getHeadZ(int debug1) {
/* 392 */     if (debug1 <= 0) {
/* 393 */       return getZ();
/*     */     }
/* 395 */     float debug2 = (this.yBodyRot + (180 * (debug1 - 1))) * 0.017453292F;
/* 396 */     float debug3 = Mth.sin(debug2);
/* 397 */     return getZ() + debug3 * 1.3D;
/*     */   }
/*     */   
/*     */   private float rotlerp(float debug1, float debug2, float debug3) {
/* 401 */     float debug4 = Mth.wrapDegrees(debug2 - debug1);
/* 402 */     if (debug4 > debug3) {
/* 403 */       debug4 = debug3;
/*     */     }
/* 405 */     if (debug4 < -debug3) {
/* 406 */       debug4 = -debug3;
/*     */     }
/* 408 */     return debug1 + debug4;
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int debug1, LivingEntity debug2) {
/* 412 */     performRangedAttack(debug1, debug2.getX(), debug2.getY() + debug2.getEyeHeight() * 0.5D, debug2.getZ(), (debug1 == 0 && this.random.nextFloat() < 0.001F));
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int debug1, double debug2, double debug4, double debug6, boolean debug8) {
/* 416 */     if (!isSilent()) {
/* 417 */       this.level.levelEvent(null, 1024, blockPosition(), 0);
/*     */     }
/*     */     
/* 420 */     double debug9 = getHeadX(debug1);
/* 421 */     double debug11 = getHeadY(debug1);
/* 422 */     double debug13 = getHeadZ(debug1);
/*     */     
/* 424 */     double debug15 = debug2 - debug9;
/* 425 */     double debug17 = debug4 - debug11;
/* 426 */     double debug19 = debug6 - debug13;
/*     */     
/* 428 */     WitherSkull debug21 = new WitherSkull(this.level, (LivingEntity)this, debug15, debug17, debug19);
/* 429 */     debug21.setOwner((Entity)this);
/* 430 */     if (debug8) {
/* 431 */       debug21.setDangerous(true);
/*     */     }
/*     */     
/* 434 */     debug21.setPosRaw(debug9, debug11, debug13);
/* 435 */     this.level.addFreshEntity((Entity)debug21);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 440 */     performRangedAttack(0, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 445 */     if (isInvulnerableTo(debug1)) {
/* 446 */       return false;
/*     */     }
/* 448 */     if (debug1 == DamageSource.DROWN || debug1.getEntity() instanceof WitherBoss) {
/* 449 */       return false;
/*     */     }
/* 451 */     if (getInvulnerableTicks() > 0 && debug1 != DamageSource.OUT_OF_WORLD) {
/* 452 */       return false;
/*     */     }
/*     */     
/* 455 */     if (isPowered()) {
/* 456 */       Entity entity = debug1.getDirectEntity();
/* 457 */       if (entity instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
/* 458 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 462 */     Entity debug3 = debug1.getEntity();
/* 463 */     if (debug3 != null && 
/* 464 */       !(debug3 instanceof Player) && 
/* 465 */       debug3 instanceof LivingEntity && ((LivingEntity)debug3).getMobType() == getMobType())
/*     */     {
/* 467 */       return false;
/*     */     }
/*     */     
/* 470 */     if (this.destroyBlocksTick <= 0) {
/* 471 */       this.destroyBlocksTick = 20;
/*     */     }
/*     */     
/* 474 */     for (int debug4 = 0; debug4 < this.idleHeadUpdates.length; debug4++) {
/* 475 */       this.idleHeadUpdates[debug4] = this.idleHeadUpdates[debug4] + 3;
/*     */     }
/*     */     
/* 478 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 483 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/* 484 */     ItemEntity debug4 = spawnAtLocation((ItemLike)Items.NETHER_STAR);
/* 485 */     if (debug4 != null) {
/* 486 */       debug4.setExtendedLifetime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {
/* 492 */     if (this.level.getDifficulty() == Difficulty.PEACEFUL && shouldDespawnInPeaceful()) {
/* 493 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/* 497 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 502 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addEffect(MobEffectInstance debug1) {
/* 507 */     return false;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 511 */     return Monster.createMonsterAttributes()
/* 512 */       .add(Attributes.MAX_HEALTH, 300.0D)
/* 513 */       .add(Attributes.MOVEMENT_SPEED, 0.6000000238418579D)
/* 514 */       .add(Attributes.FOLLOW_RANGE, 40.0D)
/* 515 */       .add(Attributes.ARMOR, 4.0D);
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
/*     */   public int getInvulnerableTicks() {
/* 527 */     return ((Integer)this.entityData.get(DATA_ID_INV)).intValue();
/*     */   }
/*     */   
/*     */   public void setInvulnerableTicks(int debug1) {
/* 531 */     this.entityData.set(DATA_ID_INV, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getAlternativeTarget(int debug1) {
/* 535 */     return ((Integer)this.entityData.get(DATA_TARGETS.get(debug1))).intValue();
/*     */   }
/*     */   
/*     */   public void setAlternativeTarget(int debug1, int debug2) {
/* 539 */     this.entityData.set(DATA_TARGETS.get(debug1), Integer.valueOf(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPowered() {
/* 544 */     return (getHealth() <= getMaxHealth() / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 549 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity debug1) {
/* 554 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canChangeDimensions() {
/* 559 */     return false;
/*     */   }
/*     */   
/*     */   class WitherDoNothingGoal extends Goal {
/*     */     public WitherDoNothingGoal() {
/* 564 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 569 */       return (WitherBoss.this.getInvulnerableTicks() > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance debug1) {
/* 575 */     if (debug1.getEffect() == MobEffects.WITHER) {
/* 576 */       return false;
/*     */     }
/* 578 */     return super.canBeAffected(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\wither\WitherBoss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */