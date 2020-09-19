/*     */ package net.minecraft.world.entity.raid;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PathfindToRaidGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.animal.Wolf;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.AbstractIllager;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Raider extends PatrollingMonster {
/*  48 */   protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN); private static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*     */   static {
/*  50 */     ALLOWED_ITEMS = (debug0 -> (!debug0.hasPickUpDelay() && debug0.isAlive() && ItemStack.matches(debug0.getItem(), Raid.getLeaderBannerInstance())));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Raid raid;
/*     */   private int wave;
/*     */   private boolean canJoinRaid;
/*     */   private int ticksOutsideRaid;
/*     */   
/*     */   protected Raider(EntityType<? extends Raider> debug1, Level debug2) {
/*  61 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  66 */     super.registerGoals();
/*  67 */     this.goalSelector.addGoal(1, new ObtainRaidLeaderBannerGoal<>(this));
/*  68 */     this.goalSelector.addGoal(3, (Goal)new PathfindToRaidGoal(this));
/*  69 */     this.goalSelector.addGoal(4, new RaiderMoveThroughVillageGoal(this, 1.0499999523162842D, 1));
/*  70 */     this.goalSelector.addGoal(5, new RaiderCelebration(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  75 */     super.defineSynchedData();
/*     */     
/*  77 */     this.entityData.define(IS_CELEBRATING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canJoinRaid() {
/*  83 */     return this.canJoinRaid;
/*     */   }
/*     */   
/*     */   public void setCanJoinRaid(boolean debug1) {
/*  87 */     this.canJoinRaid = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  95 */     if (this.level instanceof ServerLevel && isAlive()) {
/*  96 */       Raid debug1 = getCurrentRaid();
/*  97 */       if (canJoinRaid()) {
/*  98 */         if (debug1 == null) {
/*  99 */           if (this.level.getGameTime() % 20L == 0L) {
/* 100 */             Raid debug2 = ((ServerLevel)this.level).getRaidAt(blockPosition());
/* 101 */             if (debug2 != null && Raids.canJoinRaid(this, debug2)) {
/* 102 */               debug2.joinRaid(debug2.getGroupsSpawned(), this, null, true);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 107 */           LivingEntity debug2 = getTarget();
/* 108 */           if (debug2 != null && (debug2.getType() == EntityType.PLAYER || debug2.getType() == EntityType.IRON_GOLEM)) {
/* 109 */             this.noActionTime = 0;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 114 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateNoActionTime() {
/* 120 */     this.noActionTime += 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource debug1) {
/* 125 */     if (this.level instanceof ServerLevel) {
/* 126 */       Entity debug2 = debug1.getEntity();
/* 127 */       Raid debug3 = getCurrentRaid();
/* 128 */       if (debug3 != null) {
/* 129 */         if (isPatrolLeader()) {
/* 130 */           debug3.removeLeader(getWave());
/*     */         }
/*     */         
/* 133 */         if (debug2 != null && debug2.getType() == EntityType.PLAYER) {
/* 134 */           debug3.addHeroOfTheVillage(debug2);
/*     */         }
/*     */         
/* 137 */         debug3.removeFromRaid(this, false);
/*     */       } 
/*     */ 
/*     */       
/* 141 */       if (isPatrolLeader() && debug3 == null && ((ServerLevel)this.level).getRaidAt(blockPosition()) == null) {
/* 142 */         ItemStack debug4 = getItemBySlot(EquipmentSlot.HEAD);
/*     */         
/* 144 */         Player debug5 = null;
/* 145 */         Entity debug6 = debug2;
/* 146 */         if (debug6 instanceof Player) {
/* 147 */           debug5 = (Player)debug6;
/* 148 */         } else if (debug6 instanceof Wolf) {
/* 149 */           Wolf debug7 = (Wolf)debug6;
/* 150 */           LivingEntity debug8 = debug7.getOwner();
/* 151 */           if (debug7.isTame() && debug8 instanceof Player) {
/* 152 */             debug5 = (Player)debug8;
/*     */           }
/*     */         } 
/*     */         
/* 156 */         if (!debug4.isEmpty() && ItemStack.matches(debug4, Raid.getLeaderBannerInstance()) && debug5 != null) {
/* 157 */           MobEffectInstance debug7 = debug5.getEffect(MobEffects.BAD_OMEN);
/* 158 */           int debug8 = 1;
/*     */           
/* 160 */           if (debug7 != null) {
/* 161 */             debug8 += debug7.getAmplifier();
/* 162 */             debug5.removeEffectNoUpdate(MobEffects.BAD_OMEN);
/*     */           } else {
/*     */             
/* 165 */             debug8--;
/*     */           } 
/*     */           
/* 168 */           debug8 = Mth.clamp(debug8, 0, 4);
/*     */           
/* 170 */           MobEffectInstance debug9 = new MobEffectInstance(MobEffects.BAD_OMEN, 120000, debug8, false, false, true);
/* 171 */           if (!this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
/* 172 */             debug5.addEffect(debug9);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     super.die(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canJoinPatrol() {
/* 183 */     return !hasActiveRaid();
/*     */   }
/*     */   
/*     */   public void setCurrentRaid(@Nullable Raid debug1) {
/* 187 */     this.raid = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Raid getCurrentRaid() {
/* 192 */     return this.raid;
/*     */   }
/*     */   
/*     */   public boolean hasActiveRaid() {
/* 196 */     return (getCurrentRaid() != null && getCurrentRaid().isActive());
/*     */   }
/*     */   
/*     */   public void setWave(int debug1) {
/* 200 */     this.wave = debug1;
/*     */   }
/*     */   
/*     */   public int getWave() {
/* 204 */     return this.wave;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCelebrating(boolean debug1) {
/* 212 */     this.entityData.set(IS_CELEBRATING, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 217 */     super.addAdditionalSaveData(debug1);
/* 218 */     debug1.putInt("Wave", this.wave);
/* 219 */     debug1.putBoolean("CanJoinRaid", this.canJoinRaid);
/* 220 */     if (this.raid != null) {
/* 221 */       debug1.putInt("RaidId", this.raid.getId());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 227 */     super.readAdditionalSaveData(debug1);
/* 228 */     this.wave = debug1.getInt("Wave");
/* 229 */     this.canJoinRaid = debug1.getBoolean("CanJoinRaid");
/* 230 */     if (debug1.contains("RaidId", 3)) {
/* 231 */       if (this.level instanceof ServerLevel) {
/* 232 */         this.raid = ((ServerLevel)this.level).getRaids().get(debug1.getInt("RaidId"));
/*     */       }
/*     */       
/* 235 */       if (this.raid != null) {
/* 236 */         this.raid.addWaveMob(this.wave, this, false);
/*     */         
/* 238 */         if (isPatrolLeader()) {
/* 239 */           this.raid.setLeader(this.wave, this);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ItemEntity debug1) {
/* 247 */     ItemStack debug2 = debug1.getItem();
/* 248 */     boolean debug3 = (hasActiveRaid() && getCurrentRaid().getLeader(getWave()) != null);
/*     */ 
/*     */     
/* 251 */     if (hasActiveRaid() && !debug3 && ItemStack.matches(debug2, Raid.getLeaderBannerInstance())) {
/* 252 */       EquipmentSlot debug4 = EquipmentSlot.HEAD;
/* 253 */       ItemStack debug5 = getItemBySlot(debug4);
/* 254 */       double debug6 = getEquipmentDropChance(debug4);
/* 255 */       if (!debug5.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < debug6) {
/* 256 */         spawnAtLocation(debug5);
/*     */       }
/* 258 */       onItemPickup(debug1);
/* 259 */       setItemSlot(debug4, debug2);
/* 260 */       take((Entity)debug1, debug2.getCount());
/* 261 */       debug1.remove();
/* 262 */       getCurrentRaid().setLeader(getWave(), this);
/* 263 */       setPatrolLeader(true);
/*     */     } else {
/* 265 */       super.pickUpItem(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 271 */     if (getCurrentRaid() == null) {
/* 272 */       return super.removeWhenFarAway(debug1);
/*     */     }
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 279 */     return (super.requiresCustomPersistence() || getCurrentRaid() != null);
/*     */   }
/*     */   
/*     */   public int getTicksOutsideRaid() {
/* 283 */     return this.ticksOutsideRaid;
/*     */   }
/*     */   
/*     */   public void setTicksOutsideRaid(int debug1) {
/* 287 */     this.ticksOutsideRaid = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 292 */     if (hasActiveRaid()) {
/* 293 */       getCurrentRaid().updateBossbar();
/*     */     }
/* 295 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 302 */     setCanJoinRaid((getType() != EntityType.WITCH || debug3 != MobSpawnType.NATURAL));
/*     */     
/* 304 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   public abstract void applyRaidBuffs(int paramInt, boolean paramBoolean);
/*     */   
/*     */   public abstract SoundEvent getCelebrateSound();
/*     */   
/*     */   public class ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal { private final T mob;
/*     */     
/*     */     public ObtainRaidLeaderBannerGoal(T debug2) {
/* 313 */       this.mob = debug2;
/* 314 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 319 */       Raid debug1 = this.mob.getCurrentRaid();
/* 320 */       if (!this.mob.hasActiveRaid() || this.mob.getCurrentRaid().isOver() || !this.mob.canBeLeader() || ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getLeaderBannerInstance())) {
/* 321 */         return false;
/*     */       }
/*     */       
/* 324 */       Raider debug2 = debug1.getLeader(this.mob.getWave());
/* 325 */       if (debug2 == null || !debug2.isAlive()) {
/* 326 */         List<ItemEntity> debug3 = ((Raider)this.mob).level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(16.0D, 8.0D, 16.0D), Raider.ALLOWED_ITEMS);
/* 327 */         if (!debug3.isEmpty()) {
/* 328 */           return this.mob.getNavigation().moveTo((Entity)debug3.get(0), 1.149999976158142D);
/*     */         }
/*     */       } 
/*     */       
/* 332 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 337 */       if (this.mob.getNavigation().getTargetPos().closerThan((Position)this.mob.position(), 1.414D)) {
/* 338 */         List<ItemEntity> debug1 = ((Raider)this.mob).level.getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(4.0D, 4.0D, 4.0D), Raider.ALLOWED_ITEMS);
/* 339 */         if (!debug1.isEmpty())
/* 340 */           this.mob.pickUpItem(debug1.get(0)); 
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/*     */   public class RaiderCelebration
/*     */     extends Goal {
/*     */     private final Raider mob;
/*     */     
/*     */     RaiderCelebration(Raider debug2) {
/* 350 */       this.mob = debug2;
/* 351 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 356 */       Raid debug1 = this.mob.getCurrentRaid();
/* 357 */       return (this.mob.isAlive() && this.mob.getTarget() == null && debug1 != null && debug1.isLoss());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 362 */       this.mob.setCelebrating(true);
/* 363 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 368 */       this.mob.setCelebrating(false);
/* 369 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 374 */       if (!this.mob.isSilent() && this.mob.random.nextInt(100) == 0) {
/* 375 */         Raider.this.playSound(Raider.this.getCelebrateSound(), Raider.this.getSoundVolume(), Raider.this.getVoicePitch());
/*     */       }
/*     */       
/* 378 */       if (!this.mob.isPassenger() && this.mob.random.nextInt(50) == 0) {
/* 379 */         this.mob.getJumpControl().jump();
/*     */       }
/*     */       
/* 382 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public class HoldGroundAttackGoal extends Goal {
/*     */     private final Raider mob;
/*     */     private final float hostileRadiusSqr;
/* 389 */     public final TargetingConditions shoutTargeting = (new TargetingConditions()).range(8.0D).allowNonAttackable().allowInvulnerable().allowSameTeam().allowUnseeable().ignoreInvisibilityTesting();
/*     */     
/*     */     public HoldGroundAttackGoal(AbstractIllager debug2, float debug3) {
/* 392 */       this.mob = (Raider)debug2;
/* 393 */       this.hostileRadiusSqr = debug3 * debug3;
/* 394 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 400 */       LivingEntity debug1 = this.mob.getLastHurtByMob();
/* 401 */       return (this.mob.getCurrentRaid() == null && this.mob.isPatrolling() && this.mob.getTarget() != null && !this.mob.isAggressive() && (debug1 == null || debug1.getType() != EntityType.PLAYER));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 406 */       super.start();
/* 407 */       this.mob.getNavigation().stop();
/*     */       
/* 409 */       List<Raider> debug1 = this.mob.level.getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 410 */       for (Raider debug3 : debug1) {
/* 411 */         debug3.setTarget(this.mob.getTarget());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 417 */       super.stop();
/*     */       
/* 419 */       LivingEntity debug1 = this.mob.getTarget();
/* 420 */       if (debug1 != null) {
/* 421 */         List<Raider> debug2 = this.mob.level.getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 422 */         for (Raider debug4 : debug2) {
/* 423 */           debug4.setTarget(debug1);
/* 424 */           debug4.setAggressive(true);
/*     */         } 
/* 426 */         this.mob.setAggressive(true);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 432 */       LivingEntity debug1 = this.mob.getTarget();
/* 433 */       if (debug1 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 437 */       if (this.mob.distanceToSqr((Entity)debug1) > this.hostileRadiusSqr) {
/* 438 */         this.mob.getLookControl().setLookAt((Entity)debug1, 30.0F, 30.0F);
/*     */         
/* 440 */         if (this.mob.random.nextInt(50) == 0) {
/* 441 */           this.mob.playAmbientSound();
/*     */         }
/*     */       } else {
/* 444 */         this.mob.setAggressive(true);
/*     */       } 
/*     */       
/* 447 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   static class RaiderMoveThroughVillageGoal extends Goal {
/*     */     private final Raider raider;
/*     */     private final double speedModifier;
/*     */     private BlockPos poiPos;
/* 455 */     private final List<BlockPos> visited = Lists.newArrayList();
/*     */     private final int distanceToPoi;
/*     */     private boolean stuck;
/*     */     
/*     */     public RaiderMoveThroughVillageGoal(Raider debug1, double debug2, int debug4) {
/* 460 */       this.raider = debug1;
/* 461 */       this.speedModifier = debug2;
/* 462 */       this.distanceToPoi = debug4;
/* 463 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 468 */       updateVisited();
/* 469 */       return (isValidRaid() && hasSuitablePoi() && this.raider.getTarget() == null);
/*     */     }
/*     */     
/*     */     private boolean isValidRaid() {
/* 473 */       return (this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver());
/*     */     }
/*     */     
/*     */     private boolean hasSuitablePoi() {
/* 477 */       ServerLevel debug1 = (ServerLevel)this.raider.level;
/* 478 */       BlockPos debug2 = this.raider.blockPosition();
/* 479 */       Optional<BlockPos> debug3 = debug1.getPoiManager().getRandom(debug0 -> (debug0 == PoiType.HOME), this::hasNotVisited, PoiManager.Occupancy.ANY, debug2, 48, this.raider.random);
/* 480 */       if (!debug3.isPresent()) {
/* 481 */         return false;
/*     */       }
/*     */       
/* 484 */       this.poiPos = ((BlockPos)debug3.get()).immutable();
/*     */       
/* 486 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 491 */       if (this.raider.getNavigation().isDone()) {
/* 492 */         return false;
/*     */       }
/* 494 */       return (this.raider.getTarget() == null && !this.poiPos.closerThan((Position)this.raider.position(), (this.raider.getBbWidth() + this.distanceToPoi)) && !this.stuck);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 499 */       if (this.poiPos.closerThan((Position)this.raider.position(), this.distanceToPoi)) {
/* 500 */         this.visited.add(this.poiPos);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 506 */       super.start();
/* 507 */       this.raider.setNoActionTime(0);
/* 508 */       this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
/* 509 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 514 */       if (this.raider.getNavigation().isDone()) {
/* 515 */         Vec3 debug1 = Vec3.atBottomCenterOf((Vec3i)this.poiPos);
/* 516 */         Vec3 debug2 = RandomPos.getPosTowards((PathfinderMob)this.raider, 16, 7, debug1, 0.3141592741012573D);
/* 517 */         if (debug2 == null) {
/* 518 */           debug2 = RandomPos.getPosTowards((PathfinderMob)this.raider, 8, 7, debug1);
/*     */         }
/*     */         
/* 521 */         if (debug2 == null) {
/* 522 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 526 */         this.raider.getNavigation().moveTo(debug2.x, debug2.y, debug2.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean hasNotVisited(BlockPos debug1) {
/* 531 */       for (BlockPos debug3 : this.visited) {
/* 532 */         if (Objects.equals(debug1, debug3)) {
/* 533 */           return false;
/*     */         }
/*     */       } 
/* 536 */       return true;
/*     */     }
/*     */     
/*     */     private void updateVisited() {
/* 540 */       if (this.visited.size() > 2)
/* 541 */         this.visited.remove(0); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\raid\Raider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */