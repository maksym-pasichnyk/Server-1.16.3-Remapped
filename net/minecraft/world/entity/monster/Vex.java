/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Vex extends Monster {
/*  44 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Vex.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private Mob owner;
/*     */   
/*     */   @Nullable
/*     */   private BlockPos boundOrigin;
/*     */   
/*     */   private boolean hasLimitedLife;
/*     */   private int limitedLifeTicks;
/*     */   
/*     */   public Vex(EntityType<? extends Vex> debug1, Level debug2) {
/*  55 */     super((EntityType)debug1, debug2);
/*     */     
/*  57 */     this.moveControl = new VexMoveControl(this);
/*     */     
/*  59 */     this.xpReward = 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType debug1, Vec3 debug2) {
/*  64 */     super.move(debug1, debug2);
/*     */     
/*  66 */     checkInsideBlocks();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  71 */     this.noPhysics = true;
/*  72 */     super.tick();
/*  73 */     this.noPhysics = false;
/*     */     
/*  75 */     setNoGravity(true);
/*     */     
/*  77 */     if (this.hasLimitedLife && 
/*  78 */       --this.limitedLifeTicks <= 0) {
/*  79 */       this.limitedLifeTicks = 20;
/*  80 */       hurt(DamageSource.STARVE, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  87 */     super.registerGoals();
/*     */     
/*  89 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  90 */     this.goalSelector.addGoal(4, new VexChargeAttackGoal());
/*  91 */     this.goalSelector.addGoal(8, new VexRandomMoveGoal());
/*  92 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  93 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*     */     
/*  95 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  96 */     this.targetSelector.addGoal(2, (Goal)new VexCopyOwnerTargetGoal(this));
/*  97 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 101 */     return Monster.createMonsterAttributes()
/* 102 */       .add(Attributes.MAX_HEALTH, 14.0D)
/* 103 */       .add(Attributes.ATTACK_DAMAGE, 4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 108 */     super.defineSynchedData();
/*     */     
/* 110 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 115 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 117 */     if (debug1.contains("BoundX")) {
/* 118 */       this.boundOrigin = new BlockPos(debug1.getInt("BoundX"), debug1.getInt("BoundY"), debug1.getInt("BoundZ"));
/*     */     }
/* 120 */     if (debug1.contains("LifeTicks")) {
/* 121 */       setLimitedLife(debug1.getInt("LifeTicks"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 127 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 129 */     if (this.boundOrigin != null) {
/* 130 */       debug1.putInt("BoundX", this.boundOrigin.getX());
/* 131 */       debug1.putInt("BoundY", this.boundOrigin.getY());
/* 132 */       debug1.putInt("BoundZ", this.boundOrigin.getZ());
/*     */     } 
/* 134 */     if (this.hasLimitedLife) {
/* 135 */       debug1.putInt("LifeTicks", this.limitedLifeTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   public Mob getOwner() {
/* 140 */     return this.owner;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos getBoundOrigin() {
/* 145 */     return this.boundOrigin;
/*     */   }
/*     */   
/*     */   public void setBoundOrigin(@Nullable BlockPos debug1) {
/* 149 */     this.boundOrigin = debug1;
/*     */   }
/*     */   
/*     */   private boolean getVexFlag(int debug1) {
/* 153 */     int debug2 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 154 */     return ((debug2 & debug1) != 0);
/*     */   }
/*     */   
/*     */   private void setVexFlag(int debug1, boolean debug2) {
/* 158 */     int debug3 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 159 */     if (debug2) {
/* 160 */       debug3 |= debug1;
/*     */     } else {
/* 162 */       debug3 &= debug1 ^ 0xFFFFFFFF;
/*     */     } 
/* 164 */     this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug3 & 0xFF)));
/*     */   }
/*     */   
/*     */   public boolean isCharging() {
/* 168 */     return getVexFlag(1);
/*     */   }
/*     */   
/*     */   public void setIsCharging(boolean debug1) {
/* 172 */     setVexFlag(1, debug1);
/*     */   }
/*     */   
/*     */   public void setOwner(Mob debug1) {
/* 176 */     this.owner = debug1;
/*     */   }
/*     */   
/*     */   public void setLimitedLife(int debug1) {
/* 180 */     this.hasLimitedLife = true;
/* 181 */     this.limitedLifeTicks = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 186 */     return SoundEvents.VEX_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 191 */     return SoundEvents.VEX_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 196 */     return SoundEvents.VEX_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness() {
/* 201 */     return 1.0F;
/*     */   }
/*     */   
/*     */   class VexMoveControl extends MoveControl {
/*     */     public VexMoveControl(Vex debug2) {
/* 206 */       super((Mob)debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 211 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       Vec3 debug1 = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
/*     */ 
/*     */       
/* 222 */       double debug2 = debug1.length();
/* 223 */       if (debug2 < Vex.this.getBoundingBox().getSize()) {
/* 224 */         this.operation = MoveControl.Operation.WAIT;
/* 225 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5D));
/*     */       } else {
/* 227 */         Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(debug1.scale(this.speedModifier * 0.05D / debug2)));
/*     */         
/* 229 */         if (Vex.this.getTarget() == null) {
/* 230 */           Vec3 debug4 = Vex.this.getDeltaMovement();
/* 231 */           Vex.this.yRot = -((float)Mth.atan2(debug4.x, debug4.z)) * 57.295776F;
/* 232 */           Vex.this.yBodyRot = Vex.this.yRot;
/*     */         } else {
/*     */           
/* 235 */           double debug4 = Vex.this.getTarget().getX() - Vex.this.getX();
/* 236 */           double debug6 = Vex.this.getTarget().getZ() - Vex.this.getZ();
/* 237 */           Vex.this.yRot = -((float)Mth.atan2(debug4, debug6)) * 57.295776F;
/* 238 */           Vex.this.yBodyRot = Vex.this.yRot;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class VexChargeAttackGoal extends Goal {
/*     */     public VexChargeAttackGoal() {
/* 246 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 251 */       if (Vex.this.getTarget() != null && !Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(7) == 0) {
/* 252 */         return (Vex.this.distanceToSqr((Entity)Vex.this.getTarget()) > 4.0D);
/*     */       }
/* 254 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 259 */       return (Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 264 */       LivingEntity debug1 = Vex.this.getTarget();
/* 265 */       Vec3 debug2 = debug1.getEyePosition(1.0F);
/* 266 */       Vex.this.moveControl.setWantedPosition(debug2.x, debug2.y, debug2.z, 1.0D);
/* 267 */       Vex.this.setIsCharging(true);
/* 268 */       Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 273 */       Vex.this.setIsCharging(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 278 */       LivingEntity debug1 = Vex.this.getTarget();
/* 279 */       if (Vex.this.getBoundingBox().intersects(debug1.getBoundingBox())) {
/* 280 */         Vex.this.doHurtTarget((Entity)debug1);
/* 281 */         Vex.this.setIsCharging(false);
/*     */       } else {
/* 283 */         double debug2 = Vex.this.distanceToSqr((Entity)debug1);
/* 284 */         if (debug2 < 9.0D) {
/* 285 */           Vec3 debug4 = debug1.getEyePosition(1.0F);
/* 286 */           Vex.this.moveControl.setWantedPosition(debug4.x, debug4.y, debug4.z, 1.0D);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class VexRandomMoveGoal extends Goal {
/*     */     public VexRandomMoveGoal() {
/* 294 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 299 */       return (!Vex.this.getMoveControl().hasWanted() && Vex.this.random.nextInt(7) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 304 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 309 */       BlockPos debug1 = Vex.this.getBoundOrigin();
/* 310 */       if (debug1 == null)
/*     */       {
/* 312 */         debug1 = Vex.this.blockPosition();
/*     */       }
/*     */       
/* 315 */       for (int debug2 = 0; debug2 < 3; debug2++) {
/* 316 */         BlockPos debug3 = debug1.offset(Vex.this.random.nextInt(15) - 7, Vex.this.random.nextInt(11) - 5, Vex.this.random.nextInt(15) - 7);
/* 317 */         if (Vex.this.level.isEmptyBlock(debug3)) {
/* 318 */           Vex.this.moveControl.setWantedPosition(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, 0.25D);
/* 319 */           if (Vex.this.getTarget() == null) {
/* 320 */             Vex.this.getLookControl().setLookAt(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, 180.0F, 20.0F);
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 331 */     populateDefaultEquipmentSlots(debug2);
/* 332 */     populateDefaultEquipmentEnchantments(debug2);
/*     */     
/* 334 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 339 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
/* 340 */     setDropChance(EquipmentSlot.MAINHAND, 0.0F);
/*     */   }
/*     */   
/*     */   class VexCopyOwnerTargetGoal extends TargetGoal {
/* 344 */     private final TargetingConditions copyOwnerTargeting = (new TargetingConditions()).allowUnseeable().ignoreInvisibilityTesting();
/*     */     
/*     */     public VexCopyOwnerTargetGoal(PathfinderMob debug2) {
/* 347 */       super((Mob)debug2, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 352 */       return (Vex.this.owner != null && Vex.this.owner.getTarget() != null && canAttack(Vex.this.owner.getTarget(), this.copyOwnerTargeting));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 357 */       Vex.this.setTarget(Vex.this.owner.getTarget());
/* 358 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Vex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */