/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractHurtingProjectile extends Projectile {
/*     */   public double xPower;
/*     */   public double yPower;
/*     */   public double zPower;
/*     */   
/*     */   protected AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> debug1, Level debug2) {
/*  25 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12, Level debug14) {
/*  29 */     this(debug1, debug14);
/*     */     
/*  31 */     moveTo(debug2, debug4, debug6, this.yRot, this.xRot);
/*  32 */     reapplyPosition();
/*     */     
/*  34 */     double debug15 = Mth.sqrt(debug8 * debug8 + debug10 * debug10 + debug12 * debug12);
/*  35 */     if (debug15 != 0.0D) {
/*  36 */       this.xPower = debug8 / debug15 * 0.1D;
/*  37 */       this.yPower = debug10 / debug15 * 0.1D;
/*  38 */       this.zPower = debug12 / debug15 * 0.1D;
/*     */     } 
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> debug1, LivingEntity debug2, double debug3, double debug5, double debug7, Level debug9) {
/*  43 */     this(debug1, debug2.getX(), debug2.getY(), debug2.getZ(), debug3, debug5, debug7, debug9);
/*  44 */     setOwner((Entity)debug2);
/*  45 */     setRot(debug2.yRot, debug2.xRot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  64 */     Entity debug1 = getOwner();
/*  65 */     if (!this.level.isClientSide && ((debug1 != null && debug1.removed) || !this.level.hasChunkAt(blockPosition()))) {
/*  66 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  70 */     super.tick();
/*  71 */     if (shouldBurn()) {
/*  72 */       setSecondsOnFire(1);
/*     */     }
/*     */     
/*  75 */     HitResult debug2 = ProjectileUtil.getHitResult(this, this::canHitEntity);
/*  76 */     if (debug2.getType() != HitResult.Type.MISS) {
/*  77 */       onHit(debug2);
/*     */     }
/*     */     
/*  80 */     checkInsideBlocks();
/*  81 */     Vec3 debug3 = getDeltaMovement();
/*  82 */     double debug4 = getX() + debug3.x;
/*  83 */     double debug6 = getY() + debug3.y;
/*  84 */     double debug8 = getZ() + debug3.z;
/*     */     
/*  86 */     ProjectileUtil.rotateTowardsMovement(this, 0.2F);
/*     */     
/*  88 */     float debug10 = getInertia();
/*  89 */     if (isInWater()) {
/*  90 */       for (int debug11 = 0; debug11 < 4; debug11++) {
/*  91 */         float debug12 = 0.25F;
/*  92 */         this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, debug4 - debug3.x * 0.25D, debug6 - debug3.y * 0.25D, debug8 - debug3.z * 0.25D, debug3.x, debug3.y, debug3.z);
/*     */       } 
/*  94 */       debug10 = 0.8F;
/*     */     } 
/*     */     
/*  97 */     setDeltaMovement(debug3.add(this.xPower, this.yPower, this.zPower).scale(debug10));
/*     */     
/*  99 */     this.level.addParticle(getTrailParticle(), debug4, debug6 + 0.5D, debug8, 0.0D, 0.0D, 0.0D);
/*     */     
/* 101 */     setPos(debug4, debug6, debug8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity debug1) {
/* 106 */     return (super.canHitEntity(debug1) && !debug1.noPhysics);
/*     */   }
/*     */   
/*     */   protected boolean shouldBurn() {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   protected ParticleOptions getTrailParticle() {
/* 114 */     return (ParticleOptions)ParticleTypes.SMOKE;
/*     */   }
/*     */   
/*     */   protected float getInertia() {
/* 118 */     return 0.95F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 123 */     super.addAdditionalSaveData(debug1);
/* 124 */     debug1.put("power", (Tag)newDoubleList(new double[] { this.xPower, this.yPower, this.zPower }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 129 */     super.readAdditionalSaveData(debug1);
/* 130 */     if (debug1.contains("power", 9)) {
/* 131 */       ListTag debug2 = debug1.getList("power", 6);
/* 132 */       if (debug2.size() == 3) {
/* 133 */         this.xPower = debug2.getDouble(0);
/* 134 */         this.yPower = debug2.getDouble(1);
/* 135 */         this.zPower = debug2.getDouble(2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPickRadius() {
/* 147 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 152 */     if (isInvulnerableTo(debug1)) {
/* 153 */       return false;
/*     */     }
/* 155 */     markHurt();
/*     */     
/* 157 */     Entity debug3 = debug1.getEntity();
/* 158 */     if (debug3 != null) {
/* 159 */       Vec3 debug4 = debug3.getLookAngle();
/* 160 */       setDeltaMovement(debug4);
/* 161 */       this.xPower = debug4.x * 0.1D;
/* 162 */       this.yPower = debug4.y * 0.1D;
/* 163 */       this.zPower = debug4.z * 0.1D;
/*     */       
/* 165 */       setOwner(debug3);
/* 166 */       return true;
/*     */     } 
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness() {
/* 173 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 178 */     Entity debug1 = getOwner();
/* 179 */     int debug2 = (debug1 == null) ? 0 : debug1.getId();
/* 180 */     return (Packet<?>)new ClientboundAddEntityPacket(getId(), getUUID(), getX(), getY(), getZ(), this.xRot, this.yRot, getType(), debug2, new Vec3(this.xPower, this.yPower, this.zPower));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\AbstractHurtingProjectile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */