/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class FireworkRocketEntity
/*     */   extends Projectile
/*     */ {
/*  37 */   private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);
/*  38 */   private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*  39 */   private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int life;
/*     */   private int lifetime;
/*     */   private LivingEntity attachedToEntity;
/*     */   
/*     */   public FireworkRocketEntity(EntityType<? extends FireworkRocketEntity> debug1, Level debug2) {
/*  46 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level debug1, double debug2, double debug4, double debug6, ItemStack debug8) {
/*  50 */     super(EntityType.FIREWORK_ROCKET, debug1);
/*  51 */     this.life = 0;
/*     */     
/*  53 */     setPos(debug2, debug4, debug6);
/*     */     
/*  55 */     int debug9 = 1;
/*  56 */     if (!debug8.isEmpty() && debug8.hasTag()) {
/*  57 */       this.entityData.set(DATA_ID_FIREWORKS_ITEM, debug8.copy());
/*     */       
/*  59 */       debug9 += debug8.getOrCreateTagElement("Fireworks").getByte("Flight");
/*     */     } 
/*  61 */     setDeltaMovement(this.random
/*  62 */         .nextGaussian() * 0.001D, 0.05D, this.random
/*     */         
/*  64 */         .nextGaussian() * 0.001D);
/*     */ 
/*     */     
/*  67 */     this.lifetime = 10 * debug9 + this.random.nextInt(6) + this.random.nextInt(7);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level debug1, @Nullable Entity debug2, double debug3, double debug5, double debug7, ItemStack debug9) {
/*  71 */     this(debug1, debug3, debug5, debug7, debug9);
/*  72 */     setOwner(debug2);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level debug1, ItemStack debug2, LivingEntity debug3) {
/*  76 */     this(debug1, (Entity)debug3, debug3.getX(), debug3.getY(), debug3.getZ(), debug2);
/*  77 */     this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(debug3.getId()));
/*  78 */     this.attachedToEntity = debug3;
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level debug1, ItemStack debug2, double debug3, double debug5, double debug7, boolean debug9) {
/*  82 */     this(debug1, debug3, debug5, debug7, debug2);
/*  83 */     this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(debug9));
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level debug1, ItemStack debug2, Entity debug3, double debug4, double debug6, double debug8, boolean debug10) {
/*  87 */     this(debug1, debug2, debug4, debug6, debug8, debug10);
/*  88 */     setOwner(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  93 */     this.entityData.define(DATA_ID_FIREWORKS_ITEM, ItemStack.EMPTY);
/*  94 */     this.entityData.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
/*  95 */     this.entityData.define(DATA_SHOT_AT_ANGLE, Boolean.valueOf(false));
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
/*     */   public void tick() {
/* 110 */     super.tick();
/*     */     
/* 112 */     if (isAttachedToEntity()) {
/* 113 */       if (this.attachedToEntity == null) {
/* 114 */         ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).ifPresent(debug1 -> {
/*     */               Entity debug2 = this.level.getEntity(debug1);
/*     */               if (debug2 instanceof LivingEntity) {
/*     */                 this.attachedToEntity = (LivingEntity)debug2;
/*     */               }
/*     */             });
/*     */       }
/* 121 */       if (this.attachedToEntity != null) {
/* 122 */         if (this.attachedToEntity.isFallFlying()) {
/* 123 */           Vec3 vec31 = this.attachedToEntity.getLookAngle();
/* 124 */           double debug2 = 1.5D;
/* 125 */           double debug4 = 0.1D;
/*     */           
/* 127 */           Vec3 debug6 = this.attachedToEntity.getDeltaMovement();
/* 128 */           this.attachedToEntity.setDeltaMovement(debug6.add(vec31.x * 0.1D + (vec31.x * 1.5D - debug6.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - debug6.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - debug6.z) * 0.5D));
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 135 */         setPos(this.attachedToEntity.getX(), this.attachedToEntity.getY(), this.attachedToEntity.getZ());
/*     */         
/* 137 */         setDeltaMovement(this.attachedToEntity.getDeltaMovement());
/*     */       } 
/*     */     } else {
/* 140 */       if (!isShotAtAngle()) {
/* 141 */         setDeltaMovement(getDeltaMovement().multiply(1.15D, 1.0D, 1.15D).add(0.0D, 0.04D, 0.0D));
/*     */       }
/* 143 */       Vec3 vec3 = getDeltaMovement();
/* 144 */       move(MoverType.SELF, vec3);
/* 145 */       setDeltaMovement(vec3);
/*     */     } 
/*     */     
/* 148 */     HitResult debug1 = ProjectileUtil.getHitResult(this, this::canHitEntity);
/*     */     
/* 150 */     if (!this.noPhysics) {
/* 151 */       onHit(debug1);
/* 152 */       this.hasImpulse = true;
/*     */     } 
/*     */     
/* 155 */     updateRotation();
/*     */     
/* 157 */     if (this.life == 0 && !isSilent()) {
/* 158 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
/*     */     }
/*     */     
/* 161 */     this.life++;
/* 162 */     if (this.level.isClientSide && this.life % 2 < 2) {
/* 163 */       this.level.addParticle((ParticleOptions)ParticleTypes.FIREWORK, getX(), getY() - 0.3D, getZ(), this.random.nextGaussian() * 0.05D, -(getDeltaMovement()).y * 0.5D, this.random.nextGaussian() * 0.05D);
/*     */     }
/* 165 */     if (!this.level.isClientSide && this.life > this.lifetime) {
/* 166 */       explode();
/*     */     }
/*     */   }
/*     */   
/*     */   private void explode() {
/* 171 */     this.level.broadcastEntityEvent(this, (byte)17);
/* 172 */     dealExplosionDamage();
/* 173 */     remove();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/* 178 */     super.onHitEntity(debug1);
/* 179 */     if (this.level.isClientSide)
/* 180 */       return;  explode();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/* 185 */     BlockPos debug2 = new BlockPos((Vec3i)debug1.getBlockPos());
/* 186 */     this.level.getBlockState(debug2).entityInside(this.level, debug2, this);
/* 187 */     if (!this.level.isClientSide() && hasExplosion()) {
/* 188 */       explode();
/*     */     }
/* 190 */     super.onHitBlock(debug1);
/*     */   }
/*     */   
/*     */   private boolean hasExplosion() {
/* 194 */     ItemStack debug1 = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 195 */     CompoundTag debug2 = debug1.isEmpty() ? null : debug1.getTagElement("Fireworks");
/* 196 */     ListTag debug3 = (debug2 != null) ? debug2.getList("Explosions", 10) : null;
/* 197 */     return (debug3 != null && !debug3.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealExplosionDamage() {
/* 202 */     float debug1 = 0.0F;
/* 203 */     ItemStack debug2 = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 204 */     CompoundTag debug3 = debug2.isEmpty() ? null : debug2.getTagElement("Fireworks");
/* 205 */     ListTag debug4 = (debug3 != null) ? debug3.getList("Explosions", 10) : null;
/* 206 */     if (debug4 != null && !debug4.isEmpty()) {
/* 207 */       debug1 = 5.0F + (debug4.size() * 2);
/*     */     }
/* 209 */     if (debug1 > 0.0F) {
/* 210 */       if (this.attachedToEntity != null) {
/* 211 */         this.attachedToEntity.hurt(DamageSource.fireworks(this, getOwner()), 5.0F + (debug4.size() * 2));
/*     */       }
/*     */       
/* 214 */       double debug5 = 5.0D;
/* 215 */       Vec3 debug7 = position();
/* 216 */       List<LivingEntity> debug8 = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(5.0D));
/* 217 */       for (LivingEntity debug10 : debug8) {
/* 218 */         if (debug10 == this.attachedToEntity) {
/*     */           continue;
/*     */         }
/* 221 */         if (distanceToSqr((Entity)debug10) > 25.0D) {
/*     */           continue;
/*     */         }
/*     */         
/* 225 */         boolean debug11 = false;
/* 226 */         for (int debug12 = 0; debug12 < 2; debug12++) {
/* 227 */           Vec3 debug13 = new Vec3(debug10.getX(), debug10.getY(0.5D * debug12), debug10.getZ());
/* 228 */           BlockHitResult blockHitResult = this.level.clip(new ClipContext(debug7, debug13, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
/* 229 */           if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 230 */             debug11 = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 234 */         if (debug11) {
/* 235 */           float f = debug1 * (float)Math.sqrt((5.0D - distanceTo((Entity)debug10)) / 5.0D);
/* 236 */           debug10.hurt(DamageSource.fireworks(this, getOwner()), f);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isAttachedToEntity() {
/* 243 */     return ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).isPresent();
/*     */   }
/*     */   
/*     */   public boolean isShotAtAngle() {
/* 247 */     return ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue();
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
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 270 */     super.addAdditionalSaveData(debug1);
/* 271 */     debug1.putInt("Life", this.life);
/* 272 */     debug1.putInt("LifeTime", this.lifetime);
/* 273 */     ItemStack debug2 = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 274 */     if (!debug2.isEmpty()) {
/* 275 */       debug1.put("FireworksItem", (Tag)debug2.save(new CompoundTag()));
/*     */     }
/* 277 */     debug1.putBoolean("ShotAtAngle", ((Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 282 */     super.readAdditionalSaveData(debug1);
/* 283 */     this.life = debug1.getInt("Life");
/* 284 */     this.lifetime = debug1.getInt("LifeTime");
/*     */     
/* 286 */     ItemStack debug2 = ItemStack.of(debug1.getCompound("FireworksItem"));
/* 287 */     if (!debug2.isEmpty()) {
/* 288 */       this.entityData.set(DATA_ID_FIREWORKS_ITEM, debug2);
/*     */     }
/*     */     
/* 291 */     if (debug1.contains("ShotAtAngle")) {
/* 292 */       this.entityData.set(DATA_SHOT_AT_ANGLE, Boolean.valueOf(debug1.getBoolean("ShotAtAngle")));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 304 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 309 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\FireworkRocketEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */