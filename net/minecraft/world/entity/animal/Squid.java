/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Squid
/*     */   extends WaterAnimal
/*     */ {
/*     */   public float xBodyRot;
/*     */   public float xBodyRotO;
/*     */   public float zBodyRot;
/*     */   public float zBodyRotO;
/*     */   public float tentacleMovement;
/*     */   public float oldTentacleMovement;
/*     */   public float tentacleAngle;
/*     */   public float oldTentacleAngle;
/*     */   private float speed;
/*     */   private float tentacleSpeed;
/*     */   private float rotateSpeed;
/*     */   private float tx;
/*     */   private float ty;
/*     */   private float tz;
/*     */   
/*     */   public Squid(EntityType<? extends Squid> debug1, Level debug2) {
/*  52 */     super((EntityType)debug1, debug2);
/*     */     
/*  54 */     this.random.setSeed(getId());
/*  55 */     this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  60 */     this.goalSelector.addGoal(0, new SquidRandomMovementGoal(this));
/*  61 */     this.goalSelector.addGoal(1, new SquidFleeGoal());
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  65 */     return Mob.createMobAttributes()
/*  66 */       .add(Attributes.MAX_HEALTH, 10.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  71 */     return debug2.height * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  76 */     return SoundEvents.SQUID_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  81 */     return SoundEvents.SQUID_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  86 */     return SoundEvents.SQUID_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  91 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 101 */     super.aiStep();
/*     */     
/* 103 */     this.xBodyRotO = this.xBodyRot;
/* 104 */     this.zBodyRotO = this.zBodyRot;
/*     */     
/* 106 */     this.oldTentacleMovement = this.tentacleMovement;
/* 107 */     this.oldTentacleAngle = this.tentacleAngle;
/*     */     
/* 109 */     this.tentacleMovement += this.tentacleSpeed;
/* 110 */     if (this.tentacleMovement > 6.283185307179586D) {
/* 111 */       if (this.level.isClientSide) {
/* 112 */         this.tentacleMovement = 6.2831855F;
/*     */       } else {
/* 114 */         this.tentacleMovement = (float)(this.tentacleMovement - 6.283185307179586D);
/* 115 */         if (this.random.nextInt(10) == 0) {
/* 116 */           this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
/*     */         }
/* 118 */         this.level.broadcastEntityEvent((Entity)this, (byte)19);
/*     */       } 
/*     */     }
/*     */     
/* 122 */     if (isInWaterOrBubble()) {
/* 123 */       if (this.tentacleMovement < 3.1415927F) {
/* 124 */         float f = this.tentacleMovement / 3.1415927F;
/* 125 */         this.tentacleAngle = Mth.sin(f * f * 3.1415927F) * 3.1415927F * 0.25F;
/*     */         
/* 127 */         if (f > 0.75D) {
/* 128 */           this.speed = 1.0F;
/* 129 */           this.rotateSpeed = 1.0F;
/*     */         } else {
/* 131 */           this.rotateSpeed *= 0.8F;
/*     */         } 
/*     */       } else {
/* 134 */         this.tentacleAngle = 0.0F;
/* 135 */         this.speed *= 0.9F;
/* 136 */         this.rotateSpeed *= 0.99F;
/*     */       } 
/*     */       
/* 139 */       if (!this.level.isClientSide) {
/* 140 */         setDeltaMovement((this.tx * this.speed), (this.ty * this.speed), (this.tz * this.speed));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       Vec3 debug1 = getDeltaMovement();
/* 148 */       float debug2 = Mth.sqrt(getHorizontalDistanceSqr(debug1));
/*     */       
/* 150 */       this.yBodyRot += (-((float)Mth.atan2(debug1.x, debug1.z)) * 57.295776F - this.yBodyRot) * 0.1F;
/* 151 */       this.yRot = this.yBodyRot;
/* 152 */       this.zBodyRot = (float)(this.zBodyRot + Math.PI * this.rotateSpeed * 1.5D);
/* 153 */       this.xBodyRot += (-((float)Mth.atan2(debug2, debug1.y)) * 57.295776F - this.xBodyRot) * 0.1F;
/*     */     } else {
/* 155 */       this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * 3.1415927F * 0.25F;
/*     */       
/* 157 */       if (!this.level.isClientSide) {
/* 158 */         double debug1 = (getDeltaMovement()).y;
/*     */         
/* 160 */         if (hasEffect(MobEffects.LEVITATION)) {
/* 161 */           debug1 = 0.05D * (getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
/* 162 */         } else if (!isNoGravity()) {
/* 163 */           debug1 -= 0.08D;
/*     */         } 
/*     */         
/* 166 */         setDeltaMovement(0.0D, debug1 * 0.9800000190734863D, 0.0D);
/*     */       } 
/*     */ 
/*     */       
/* 170 */       this.xBodyRot = (float)(this.xBodyRot + (-90.0F - this.xBodyRot) * 0.02D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 176 */     if (super.hurt(debug1, debug2) && getLastHurtByMob() != null) {
/* 177 */       spawnInk();
/* 178 */       return true;
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   private Vec3 rotateVector(Vec3 debug1) {
/* 185 */     Vec3 debug2 = debug1.xRot(this.xBodyRotO * 0.017453292F);
/* 186 */     debug2 = debug2.yRot(-this.yBodyRotO * 0.017453292F);
/* 187 */     return debug2;
/*     */   }
/*     */   
/*     */   private void spawnInk() {
/* 191 */     playSound(SoundEvents.SQUID_SQUIRT, getSoundVolume(), getVoicePitch());
/* 192 */     Vec3 debug1 = rotateVector(new Vec3(0.0D, -1.0D, 0.0D)).add(getX(), getY(), getZ());
/* 193 */     for (int debug2 = 0; debug2 < 30; debug2++) {
/* 194 */       Vec3 debug3 = rotateVector(new Vec3(this.random.nextFloat() * 0.6D - 0.3D, -1.0D, this.random.nextFloat() * 0.6D - 0.3D));
/* 195 */       Vec3 debug4 = debug3.scale(0.3D + (this.random.nextFloat() * 2.0F));
/* 196 */       ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.SQUID_INK, debug1.x, debug1.y + 0.5D, debug1.z, 0, debug4.x, debug4.y, debug4.z, 0.10000000149011612D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 202 */     move(MoverType.SELF, getDeltaMovement());
/*     */   }
/*     */   
/*     */   public static boolean checkSquidSpawnRules(EntityType<Squid> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 206 */     return (debug3.getY() > 45 && debug3.getY() < debug1.getSeaLevel());
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
/*     */   public void setMovementVector(float debug1, float debug2, float debug3) {
/* 219 */     this.tx = debug1;
/* 220 */     this.ty = debug2;
/* 221 */     this.tz = debug3;
/*     */   }
/*     */   
/*     */   public boolean hasMovementVector() {
/* 225 */     return (this.tx != 0.0F || this.ty != 0.0F || this.tz != 0.0F);
/*     */   }
/*     */   
/*     */   class SquidRandomMovementGoal extends Goal {
/*     */     private final Squid squid;
/*     */     
/*     */     public SquidRandomMovementGoal(Squid debug2) {
/* 232 */       this.squid = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 237 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 242 */       int debug1 = this.squid.getNoActionTime();
/*     */       
/* 244 */       if (debug1 > 100) {
/* 245 */         this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
/* 246 */       } else if (this.squid.getRandom().nextInt(50) == 0 || !this.squid.wasTouchingWater || !this.squid.hasMovementVector()) {
/* 247 */         float debug2 = this.squid.getRandom().nextFloat() * 6.2831855F;
/* 248 */         float debug3 = Mth.cos(debug2) * 0.2F;
/* 249 */         float debug4 = -0.1F + this.squid.getRandom().nextFloat() * 0.2F;
/* 250 */         float debug5 = Mth.sin(debug2) * 0.2F;
/* 251 */         this.squid.setMovementVector(debug3, debug4, debug5);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class SquidFleeGoal
/*     */     extends Goal
/*     */   {
/*     */     private int fleeTicks;
/*     */     
/*     */     private SquidFleeGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 265 */       LivingEntity debug1 = Squid.this.getLastHurtByMob();
/* 266 */       if (Squid.this.isInWater() && debug1 != null) {
/* 267 */         return (Squid.this.distanceToSqr((Entity)debug1) < 100.0D);
/*     */       }
/*     */       
/* 270 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 275 */       this.fleeTicks = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 280 */       this.fleeTicks++;
/*     */       
/* 282 */       LivingEntity debug1 = Squid.this.getLastHurtByMob();
/* 283 */       if (debug1 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 287 */       Vec3 debug2 = new Vec3(Squid.this.getX() - debug1.getX(), Squid.this.getY() - debug1.getY(), Squid.this.getZ() - debug1.getZ());
/*     */       
/* 289 */       BlockState debug3 = Squid.this.level.getBlockState(new BlockPos(Squid.this.getX() + debug2.x, Squid.this.getY() + debug2.y, Squid.this.getZ() + debug2.z));
/* 290 */       FluidState debug4 = Squid.this.level.getFluidState(new BlockPos(Squid.this.getX() + debug2.x, Squid.this.getY() + debug2.y, Squid.this.getZ() + debug2.z));
/* 291 */       if (debug4.is((Tag)FluidTags.WATER) || debug3.isAir()) {
/* 292 */         double debug5 = debug2.length();
/* 293 */         if (debug5 > 0.0D) {
/* 294 */           debug2.normalize();
/*     */           
/* 296 */           float debug7 = 3.0F;
/* 297 */           if (debug5 > 5.0D) {
/* 298 */             debug7 = (float)(debug7 - (debug5 - 5.0D) / 5.0D);
/*     */           }
/*     */           
/* 301 */           if (debug7 > 0.0F) {
/* 302 */             debug2 = debug2.scale(debug7);
/*     */           }
/*     */         } 
/*     */         
/* 306 */         if (debug3.isAir()) {
/* 307 */           debug2 = debug2.subtract(0.0D, debug2.y, 0.0D);
/*     */         }
/*     */         
/* 310 */         Squid.this.setMovementVector((float)debug2.x / 20.0F, (float)debug2.y / 20.0F, (float)debug2.z / 20.0F);
/*     */       } 
/*     */       
/* 313 */       if (this.fleeTicks % 10 == 5)
/* 314 */         Squid.this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, Squid.this.getX(), Squid.this.getY(), Squid.this.getZ(), 0.0D, 0.0D, 0.0D); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Squid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */