/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.FlyingMob;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.LargeFireball;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ghast
/*     */   extends FlyingMob
/*     */   implements Enemy {
/*  39 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  41 */   private int explosionPower = 1;
/*     */   
/*     */   public Ghast(EntityType<? extends Ghast> debug1, Level debug2) {
/*  44 */     super(debug1, debug2);
/*     */     
/*  46 */     this.xpReward = 5;
/*     */     
/*  48 */     this.moveControl = new GhastMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  53 */     this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
/*     */     
/*  55 */     this.goalSelector.addGoal(7, new GhastLookGoal(this));
/*  56 */     this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
/*     */ 
/*     */     
/*  59 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, debug1 -> (Math.abs(debug1.getY() - getY()) <= 4.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharging(boolean debug1) {
/*  67 */     this.entityData.set(DATA_IS_CHARGING, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getExplosionPower() {
/*  71 */     return this.explosionPower;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDespawnInPeaceful() {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/*  81 */     if (isInvulnerableTo(debug1)) {
/*  82 */       return false;
/*     */     }
/*  84 */     if (debug1.getDirectEntity() instanceof LargeFireball && 
/*  85 */       debug1.getEntity() instanceof Player) {
/*     */       
/*  87 */       super.hurt(debug1, 1000.0F);
/*  88 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  97 */     super.defineSynchedData();
/*     */     
/*  99 */     this.entityData.define(DATA_IS_CHARGING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 103 */     return Mob.createMobAttributes()
/* 104 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 105 */       .add(Attributes.FOLLOW_RANGE, 100.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 110 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 115 */     return SoundEvents.GHAST_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 120 */     return SoundEvents.GHAST_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 125 */     return SoundEvents.GHAST_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 130 */     return 5.0F;
/*     */   }
/*     */   
/*     */   public static boolean checkGhastSpawnRules(EntityType<Ghast> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 134 */     return (debug1.getDifficulty() != Difficulty.PEACEFUL && debug4
/* 135 */       .nextInt(20) == 0 && 
/* 136 */       checkMobSpawnRules(debug0, debug1, debug2, debug3, debug4));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 141 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 146 */     super.addAdditionalSaveData(debug1);
/* 147 */     debug1.putInt("ExplosionPower", this.explosionPower);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 152 */     super.readAdditionalSaveData(debug1);
/* 153 */     if (debug1.contains("ExplosionPower", 99))
/* 154 */       this.explosionPower = debug1.getInt("ExplosionPower"); 
/*     */   }
/*     */   
/*     */   static class GhastMoveControl
/*     */     extends MoveControl {
/*     */     private final Ghast ghast;
/*     */     private int floatDuration;
/*     */     
/*     */     public GhastMoveControl(Ghast debug1) {
/* 163 */       super((Mob)debug1);
/* 164 */       this.ghast = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 169 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 174 */       if (this.floatDuration-- <= 0) {
/* 175 */         this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 180 */         Vec3 debug1 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
/*     */         
/* 182 */         double debug2 = debug1.length();
/* 183 */         debug1 = debug1.normalize();
/*     */         
/* 185 */         if (canReach(debug1, Mth.ceil(debug2))) {
/* 186 */           this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(debug1.scale(0.1D)));
/*     */         } else {
/* 188 */           this.operation = MoveControl.Operation.WAIT;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canReach(Vec3 debug1, int debug2) {
/* 194 */       AABB debug3 = this.ghast.getBoundingBox();
/* 195 */       for (int debug4 = 1; debug4 < debug2; debug4++) {
/* 196 */         debug3 = debug3.move(debug1);
/* 197 */         if (!this.ghast.level.noCollision((Entity)this.ghast, debug3)) {
/* 198 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 202 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RandomFloatAroundGoal extends Goal {
/*     */     private final Ghast ghast;
/*     */     
/*     */     public RandomFloatAroundGoal(Ghast debug1) {
/* 210 */       this.ghast = debug1;
/*     */       
/* 212 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 217 */       MoveControl debug1 = this.ghast.getMoveControl();
/* 218 */       if (!debug1.hasWanted()) {
/* 219 */         return true;
/*     */       }
/*     */       
/* 222 */       double debug2 = debug1.getWantedX() - this.ghast.getX();
/* 223 */       double debug4 = debug1.getWantedY() - this.ghast.getY();
/* 224 */       double debug6 = debug1.getWantedZ() - this.ghast.getZ();
/*     */       
/* 226 */       double debug8 = debug2 * debug2 + debug4 * debug4 + debug6 * debug6;
/*     */       
/* 228 */       if (debug8 < 1.0D || debug8 > 3600.0D) {
/* 229 */         return true;
/*     */       }
/*     */       
/* 232 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 237 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 242 */       Random debug1 = this.ghast.getRandom();
/* 243 */       double debug2 = this.ghast.getX() + ((debug1.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 244 */       double debug4 = this.ghast.getY() + ((debug1.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 245 */       double debug6 = this.ghast.getZ() + ((debug1.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 246 */       this.ghast.getMoveControl().setWantedPosition(debug2, debug4, debug6, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class GhastLookGoal extends Goal {
/*     */     private final Ghast ghast;
/*     */     
/*     */     public GhastLookGoal(Ghast debug1) {
/* 254 */       this.ghast = debug1;
/*     */       
/* 256 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 261 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 266 */       if (this.ghast.getTarget() == null) {
/* 267 */         Vec3 debug1 = this.ghast.getDeltaMovement();
/* 268 */         this.ghast.yRot = -((float)Mth.atan2(debug1.x, debug1.z)) * 57.295776F;
/* 269 */         this.ghast.yBodyRot = this.ghast.yRot;
/*     */       } else {
/* 271 */         LivingEntity debug1 = this.ghast.getTarget();
/*     */         
/* 273 */         double debug2 = 64.0D;
/* 274 */         if (debug1.distanceToSqr((Entity)this.ghast) < 4096.0D) {
/* 275 */           double debug4 = debug1.getX() - this.ghast.getX();
/* 276 */           double debug6 = debug1.getZ() - this.ghast.getZ();
/* 277 */           this.ghast.yRot = -((float)Mth.atan2(debug4, debug6)) * 57.295776F;
/* 278 */           this.ghast.yBodyRot = this.ghast.yRot;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class GhastShootFireballGoal extends Goal {
/*     */     private final Ghast ghast;
/*     */     public int chargeTime;
/*     */     
/*     */     public GhastShootFireballGoal(Ghast debug1) {
/* 289 */       this.ghast = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 294 */       return (this.ghast.getTarget() != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 299 */       this.chargeTime = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 304 */       this.ghast.setCharging(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 309 */       LivingEntity debug1 = this.ghast.getTarget();
/*     */       
/* 311 */       double debug2 = 64.0D;
/* 312 */       if (debug1.distanceToSqr((Entity)this.ghast) < 4096.0D && this.ghast.canSee((Entity)debug1)) {
/* 313 */         Level debug4 = this.ghast.level;
/*     */         
/* 315 */         this.chargeTime++;
/* 316 */         if (this.chargeTime == 10 && !this.ghast.isSilent()) {
/* 317 */           debug4.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
/*     */         }
/* 319 */         if (this.chargeTime == 20) {
/* 320 */           double debug5 = 4.0D;
/* 321 */           Vec3 debug7 = this.ghast.getViewVector(1.0F);
/*     */           
/* 323 */           double debug8 = debug1.getX() - this.ghast.getX() + debug7.x * 4.0D;
/* 324 */           double debug10 = debug1.getY(0.5D) - 0.5D + this.ghast.getY(0.5D);
/* 325 */           double debug12 = debug1.getZ() - this.ghast.getZ() + debug7.z * 4.0D;
/*     */           
/* 327 */           if (!this.ghast.isSilent()) {
/* 328 */             debug4.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
/*     */           }
/* 330 */           LargeFireball debug14 = new LargeFireball(debug4, (LivingEntity)this.ghast, debug8, debug10, debug12);
/* 331 */           debug14.explosionPower = this.ghast.getExplosionPower();
/* 332 */           debug14.setPos(this.ghast.getX() + debug7.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, debug14.getZ() + debug7.z * 4.0D);
/* 333 */           debug4.addFreshEntity((Entity)debug14);
/* 334 */           this.chargeTime = -40;
/*     */         } 
/* 336 */       } else if (this.chargeTime > 0) {
/* 337 */         this.chargeTime--;
/*     */       } 
/* 339 */       this.ghast.setCharging((this.chargeTime > 10));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 345 */     return 2.6F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Ghast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */