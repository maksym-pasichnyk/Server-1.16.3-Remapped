/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShulkerBullet
/*     */   extends Projectile
/*     */ {
/*     */   private Entity finalTarget;
/*     */   @Nullable
/*     */   private Direction currentMoveDirection;
/*     */   private int flightSteps;
/*     */   private double targetDeltaX;
/*     */   private double targetDeltaY;
/*     */   private double targetDeltaZ;
/*     */   @Nullable
/*     */   private UUID targetId;
/*     */   
/*     */   public ShulkerBullet(EntityType<? extends ShulkerBullet> debug1, Level debug2) {
/*  50 */     super((EntityType)debug1, debug2);
/*     */     
/*  52 */     this.noPhysics = true;
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
/*     */   public ShulkerBullet(Level debug1, LivingEntity debug2, Entity debug3, Direction.Axis debug4) {
/*  64 */     this(EntityType.SHULKER_BULLET, debug1);
/*  65 */     setOwner((Entity)debug2);
/*     */     
/*  67 */     BlockPos debug5 = debug2.blockPosition();
/*  68 */     double debug6 = debug5.getX() + 0.5D;
/*  69 */     double debug8 = debug5.getY() + 0.5D;
/*  70 */     double debug10 = debug5.getZ() + 0.5D;
/*     */     
/*  72 */     moveTo(debug6, debug8, debug10, this.yRot, this.xRot);
/*     */     
/*  74 */     this.finalTarget = debug3;
/*     */     
/*  76 */     this.currentMoveDirection = Direction.UP;
/*  77 */     selectNextMoveDirection(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  82 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  87 */     super.addAdditionalSaveData(debug1);
/*  88 */     if (this.finalTarget != null) {
/*  89 */       debug1.putUUID("Target", this.finalTarget.getUUID());
/*     */     }
/*  91 */     if (this.currentMoveDirection != null) {
/*  92 */       debug1.putInt("Dir", this.currentMoveDirection.get3DDataValue());
/*     */     }
/*  94 */     debug1.putInt("Steps", this.flightSteps);
/*  95 */     debug1.putDouble("TXD", this.targetDeltaX);
/*  96 */     debug1.putDouble("TYD", this.targetDeltaY);
/*  97 */     debug1.putDouble("TZD", this.targetDeltaZ);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 102 */     super.readAdditionalSaveData(debug1);
/* 103 */     this.flightSteps = debug1.getInt("Steps");
/* 104 */     this.targetDeltaX = debug1.getDouble("TXD");
/* 105 */     this.targetDeltaY = debug1.getDouble("TYD");
/* 106 */     this.targetDeltaZ = debug1.getDouble("TZD");
/* 107 */     if (debug1.contains("Dir", 99)) {
/* 108 */       this.currentMoveDirection = Direction.from3DDataValue(debug1.getInt("Dir"));
/*     */     }
/* 110 */     if (debug1.hasUUID("Target")) {
/* 111 */       this.targetId = debug1.getUUID("Target");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setMoveDirection(@Nullable Direction debug1) {
/* 125 */     this.currentMoveDirection = debug1;
/*     */   }
/*     */   
/*     */   private void selectNextMoveDirection(@Nullable Direction.Axis debug1) {
/*     */     BlockPos debug2;
/* 130 */     double debug3 = 0.5D;
/* 131 */     if (this.finalTarget == null) {
/* 132 */       debug2 = blockPosition().below();
/*     */     } else {
/* 134 */       debug3 = this.finalTarget.getBbHeight() * 0.5D;
/* 135 */       debug2 = new BlockPos(this.finalTarget.getX(), this.finalTarget.getY() + debug3, this.finalTarget.getZ());
/*     */     } 
/*     */     
/* 138 */     double debug5 = debug2.getX() + 0.5D;
/* 139 */     double debug7 = debug2.getY() + debug3;
/* 140 */     double debug9 = debug2.getZ() + 0.5D;
/*     */     
/* 142 */     Direction debug11 = null;
/* 143 */     if (!debug2.closerThan((Position)position(), 2.0D)) {
/* 144 */       BlockPos blockPos = blockPosition();
/* 145 */       List<Direction> debug13 = Lists.newArrayList();
/*     */       
/* 147 */       if (debug1 != Direction.Axis.X) {
/* 148 */         if (blockPos.getX() < debug2.getX() && this.level.isEmptyBlock(blockPos.east())) {
/* 149 */           debug13.add(Direction.EAST);
/* 150 */         } else if (blockPos.getX() > debug2.getX() && this.level.isEmptyBlock(blockPos.west())) {
/* 151 */           debug13.add(Direction.WEST);
/*     */         } 
/*     */       }
/* 154 */       if (debug1 != Direction.Axis.Y) {
/* 155 */         if (blockPos.getY() < debug2.getY() && this.level.isEmptyBlock(blockPos.above())) {
/* 156 */           debug13.add(Direction.UP);
/* 157 */         } else if (blockPos.getY() > debug2.getY() && this.level.isEmptyBlock(blockPos.below())) {
/* 158 */           debug13.add(Direction.DOWN);
/*     */         } 
/*     */       }
/* 161 */       if (debug1 != Direction.Axis.Z) {
/* 162 */         if (blockPos.getZ() < debug2.getZ() && this.level.isEmptyBlock(blockPos.south())) {
/* 163 */           debug13.add(Direction.SOUTH);
/* 164 */         } else if (blockPos.getZ() > debug2.getZ() && this.level.isEmptyBlock(blockPos.north())) {
/* 165 */           debug13.add(Direction.NORTH);
/*     */         } 
/*     */       }
/*     */       
/* 169 */       debug11 = Direction.getRandom(this.random);
/* 170 */       if (debug13.isEmpty()) {
/* 171 */         int i = 5;
/* 172 */         while (!this.level.isEmptyBlock(blockPos.relative(debug11)) && i > 0) {
/* 173 */           debug11 = Direction.getRandom(this.random);
/* 174 */           i--;
/*     */         } 
/*     */       } else {
/* 177 */         debug11 = debug13.get(this.random.nextInt(debug13.size()));
/*     */       } 
/*     */       
/* 180 */       debug5 = getX() + debug11.getStepX();
/* 181 */       debug7 = getY() + debug11.getStepY();
/* 182 */       debug9 = getZ() + debug11.getStepZ();
/*     */     } 
/*     */     
/* 185 */     setMoveDirection(debug11);
/*     */     
/* 187 */     double debug12 = debug5 - getX();
/* 188 */     double debug14 = debug7 - getY();
/* 189 */     double debug16 = debug9 - getZ();
/*     */     
/* 191 */     double debug18 = Mth.sqrt(debug12 * debug12 + debug14 * debug14 + debug16 * debug16);
/* 192 */     if (debug18 == 0.0D) {
/* 193 */       this.targetDeltaX = 0.0D;
/* 194 */       this.targetDeltaY = 0.0D;
/* 195 */       this.targetDeltaZ = 0.0D;
/*     */     } else {
/* 197 */       this.targetDeltaX = debug12 / debug18 * 0.15D;
/* 198 */       this.targetDeltaY = debug14 / debug18 * 0.15D;
/* 199 */       this.targetDeltaZ = debug16 / debug18 * 0.15D;
/*     */     } 
/*     */     
/* 202 */     this.hasImpulse = true;
/* 203 */     this.flightSteps = 10 + this.random.nextInt(5) * 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {
/* 208 */     if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
/* 209 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 215 */     super.tick();
/*     */     
/* 217 */     if (!this.level.isClientSide) {
/* 218 */       if (this.finalTarget == null && this.targetId != null) {
/* 219 */         this.finalTarget = ((ServerLevel)this.level).getEntity(this.targetId);
/* 220 */         if (this.finalTarget == null) {
/* 221 */           this.targetId = null;
/*     */         }
/*     */       } 
/*     */       
/* 225 */       if (this.finalTarget != null && this.finalTarget.isAlive() && (!(this.finalTarget instanceof Player) || !((Player)this.finalTarget).isSpectator())) {
/* 226 */         this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
/* 227 */         this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
/* 228 */         this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
/*     */         
/* 230 */         Vec3 vec3 = getDeltaMovement();
/* 231 */         setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2D, (this.targetDeltaY - vec3.y) * 0.2D, (this.targetDeltaZ - vec3.z) * 0.2D));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 237 */       else if (!isNoGravity()) {
/* 238 */         setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
/*     */       } 
/*     */ 
/*     */       
/* 242 */       HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
/* 243 */       if (hitResult.getType() != HitResult.Type.MISS) {
/* 244 */         onHit(hitResult);
/*     */       }
/*     */     } 
/*     */     
/* 248 */     checkInsideBlocks();
/* 249 */     Vec3 debug1 = getDeltaMovement();
/* 250 */     setPos(getX() + debug1.x, getY() + debug1.y, getZ() + debug1.z);
/*     */     
/* 252 */     ProjectileUtil.rotateTowardsMovement(this, 0.5F);
/*     */     
/* 254 */     if (this.level.isClientSide) {
/* 255 */       this.level.addParticle((ParticleOptions)ParticleTypes.END_ROD, getX() - debug1.x, getY() - debug1.y + 0.15D, getZ() - debug1.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/* 257 */     else if (this.finalTarget != null && !this.finalTarget.removed) {
/* 258 */       if (this.flightSteps > 0) {
/* 259 */         this.flightSteps--;
/* 260 */         if (this.flightSteps == 0) {
/* 261 */           selectNextMoveDirection((this.currentMoveDirection == null) ? null : this.currentMoveDirection.getAxis());
/*     */         }
/*     */       } 
/*     */       
/* 265 */       if (this.currentMoveDirection != null) {
/*     */         
/* 267 */         BlockPos debug2 = blockPosition();
/* 268 */         Direction.Axis debug3 = this.currentMoveDirection.getAxis();
/* 269 */         if (this.level.loadedAndEntityCanStandOn(debug2.relative(this.currentMoveDirection), this)) {
/* 270 */           selectNextMoveDirection(debug3);
/*     */         } else {
/* 272 */           BlockPos debug4 = this.finalTarget.blockPosition();
/* 273 */           if ((debug3 == Direction.Axis.X && debug2.getX() == debug4.getX()) || (debug3 == Direction.Axis.Z && debug2
/* 274 */             .getZ() == debug4.getZ()) || (debug3 == Direction.Axis.Y && debug2
/* 275 */             .getY() == debug4.getY()))
/*     */           {
/* 277 */             selectNextMoveDirection(debug3);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity debug1) {
/* 287 */     return (super.canHitEntity(debug1) && !debug1.noPhysics);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBrightness() {
/* 302 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/* 307 */     super.onHitEntity(debug1);
/* 308 */     Entity debug2 = debug1.getEntity();
/* 309 */     Entity debug3 = getOwner();
/* 310 */     LivingEntity debug4 = (debug3 instanceof LivingEntity) ? (LivingEntity)debug3 : null;
/* 311 */     boolean debug5 = debug2.hurt(DamageSource.indirectMobAttack(this, debug4).setProjectile(), 4.0F);
/* 312 */     if (debug5) {
/* 313 */       doEnchantDamageEffects(debug4, debug2);
/* 314 */       if (debug2 instanceof LivingEntity) {
/* 315 */         ((LivingEntity)debug2).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/* 322 */     super.onHitBlock(debug1);
/* 323 */     ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
/* 324 */     playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult debug1) {
/* 329 */     super.onHit(debug1);
/* 330 */     remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 335 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 340 */     if (!this.level.isClientSide) {
/* 341 */       playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
/* 342 */       ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.CRIT, getX(), getY(), getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
/* 343 */       remove();
/*     */     } 
/* 345 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 350 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ShulkerBullet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */