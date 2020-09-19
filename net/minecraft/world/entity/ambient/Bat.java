/*     */ package net.minecraft.world.entity.ambient;
/*     */ import java.time.LocalDate;
/*     */ import java.time.temporal.ChronoField;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Bat extends AmbientCreature {
/*  33 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Bat.class, EntityDataSerializers.BYTE);
/*     */   
/*  35 */   private static final TargetingConditions BAT_RESTING_TARGETING = (new TargetingConditions()).range(4.0D).allowSameTeam();
/*     */   
/*     */   private BlockPos targetPosition;
/*     */   
/*     */   public Bat(EntityType<? extends Bat> debug1, Level debug2) {
/*  40 */     super((EntityType)debug1, debug2);
/*     */     
/*  42 */     setResting(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  47 */     super.defineSynchedData();
/*     */     
/*  49 */     this.entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  54 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getVoicePitch() {
/*  59 */     return super.getVoicePitch() * 0.95F;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SoundEvent getAmbientSound() {
/*  65 */     if (isResting() && this.random.nextInt(4) != 0) {
/*  66 */       return null;
/*     */     }
/*  68 */     return SoundEvents.BAT_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  73 */     return SoundEvents.BAT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  78 */     return SoundEvents.BAT_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushEntities() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  98 */     return Mob.createMobAttributes()
/*  99 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean isResting() {
/* 103 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setResting(boolean debug1) {
/* 107 */     byte debug2 = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/* 108 */     if (debug1) {
/* 109 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug2 | 0x1)));
/*     */     } else {
/* 111 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug2 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 117 */     super.tick();
/*     */     
/* 119 */     if (isResting()) {
/* 120 */       setDeltaMovement(Vec3.ZERO);
/* 121 */       setPosRaw(getX(), Mth.floor(getY()) + 1.0D - getBbHeight(), getZ());
/*     */     } else {
/* 123 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 129 */     super.customServerAiStep();
/*     */     
/* 131 */     BlockPos debug1 = blockPosition();
/* 132 */     BlockPos debug2 = debug1.above();
/*     */     
/* 134 */     if (isResting()) {
/* 135 */       boolean debug3 = isSilent();
/* 136 */       if (this.level.getBlockState(debug2).isRedstoneConductor((BlockGetter)this.level, debug1)) {
/* 137 */         if (this.random.nextInt(200) == 0) {
/* 138 */           this.yHeadRot = this.random.nextInt(360);
/*     */         }
/*     */         
/* 141 */         if (this.level.getNearestPlayer(BAT_RESTING_TARGETING, (LivingEntity)this) != null) {
/* 142 */           setResting(false);
/* 143 */           if (!debug3) {
/* 144 */             this.level.levelEvent(null, 1025, debug1, 0);
/*     */           }
/*     */         } 
/*     */       } else {
/* 148 */         setResting(false);
/* 149 */         if (!debug3) {
/* 150 */           this.level.levelEvent(null, 1025, debug1, 0);
/*     */         }
/*     */       } 
/*     */     } else {
/* 154 */       if (this.targetPosition != null && (!this.level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() < 1)) {
/* 155 */         this.targetPosition = null;
/*     */       }
/* 157 */       if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerThan((Position)position(), 2.0D)) {
/* 158 */         this.targetPosition = new BlockPos(getX() + this.random.nextInt(7) - this.random.nextInt(7), getY() + this.random.nextInt(6) - 2.0D, getZ() + this.random.nextInt(7) - this.random.nextInt(7));
/*     */       }
/*     */ 
/*     */       
/* 162 */       double debug3 = this.targetPosition.getX() + 0.5D - getX();
/* 163 */       double debug5 = this.targetPosition.getY() + 0.1D - getY();
/* 164 */       double debug7 = this.targetPosition.getZ() + 0.5D - getZ();
/*     */       
/* 166 */       Vec3 debug9 = getDeltaMovement();
/* 167 */       Vec3 debug10 = debug9.add((
/* 168 */           Math.signum(debug3) * 0.5D - debug9.x) * 0.10000000149011612D, (
/* 169 */           Math.signum(debug5) * 0.699999988079071D - debug9.y) * 0.10000000149011612D, (
/* 170 */           Math.signum(debug7) * 0.5D - debug9.z) * 0.10000000149011612D);
/*     */       
/* 172 */       setDeltaMovement(debug10);
/*     */       
/* 174 */       float debug11 = (float)(Mth.atan2(debug10.z, debug10.x) * 57.2957763671875D) - 90.0F;
/* 175 */       float debug12 = Mth.wrapDegrees(debug11 - this.yRot);
/* 176 */       this.zza = 0.5F;
/* 177 */       this.yRot += debug12;
/*     */       
/* 179 */       if (this.random.nextInt(100) == 0 && this.level.getBlockState(debug2).isRedstoneConductor((BlockGetter)this.level, debug2)) {
/* 180 */         setResting(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 208 */     if (isInvulnerableTo(debug1)) {
/* 209 */       return false;
/*     */     }
/* 211 */     if (!this.level.isClientSide && 
/* 212 */       isResting()) {
/* 213 */       setResting(false);
/*     */     }
/*     */ 
/*     */     
/* 217 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 222 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 224 */     this.entityData.set(DATA_ID_FLAGS, Byte.valueOf(debug1.getByte("BatFlags")));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 229 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 231 */     debug1.putByte("BatFlags", ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue());
/*     */   }
/*     */   
/*     */   public static boolean checkBatSpawnRules(EntityType<Bat> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 235 */     if (debug3.getY() >= debug1.getSeaLevel()) {
/* 236 */       return false;
/*     */     }
/*     */     
/* 239 */     int debug5 = debug1.getMaxLocalRawBrightness(debug3);
/* 240 */     int debug6 = 4;
/*     */     
/* 242 */     if (isHalloween()) {
/* 243 */       debug6 = 7;
/* 244 */     } else if (debug4.nextBoolean()) {
/* 245 */       return false;
/*     */     } 
/*     */     
/* 248 */     if (debug5 > debug4.nextInt(debug6)) {
/* 249 */       return false;
/*     */     }
/*     */     
/* 252 */     return checkMobSpawnRules(debug0, debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private static boolean isHalloween() {
/* 256 */     LocalDate debug0 = LocalDate.now();
/* 257 */     int debug1 = debug0.get(ChronoField.DAY_OF_MONTH);
/* 258 */     int debug2 = debug0.get(ChronoField.MONTH_OF_YEAR);
/*     */     
/* 260 */     return ((debug2 == 10 && debug1 >= 20) || (debug2 == 11 && debug1 <= 3));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 265 */     return debug2.height / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ambient\Bat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */