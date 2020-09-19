/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Projectile
/*     */   extends Entity {
/*     */   private UUID ownerUUID;
/*     */   private int ownerNetworkId;
/*     */   private boolean leftOwner;
/*     */   
/*     */   Projectile(EntityType<? extends Projectile> debug1, Level debug2) {
/*  24 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void setOwner(@Nullable Entity debug1) {
/*  28 */     if (debug1 != null) {
/*  29 */       this.ownerUUID = debug1.getUUID();
/*  30 */       this.ownerNetworkId = debug1.getId();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Entity getOwner() {
/*  36 */     if (this.ownerUUID != null && this.level instanceof ServerLevel)
/*  37 */       return ((ServerLevel)this.level).getEntity(this.ownerUUID); 
/*  38 */     if (this.ownerNetworkId != 0) {
/*  39 */       return this.level.getEntity(this.ownerNetworkId);
/*     */     }
/*  41 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  46 */     if (this.ownerUUID != null) {
/*  47 */       debug1.putUUID("Owner", this.ownerUUID);
/*     */     }
/*  49 */     if (this.leftOwner) {
/*  50 */       debug1.putBoolean("LeftOwner", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/*  56 */     if (debug1.hasUUID("Owner")) {
/*  57 */       this.ownerUUID = debug1.getUUID("Owner");
/*     */     }
/*  59 */     this.leftOwner = debug1.getBoolean("LeftOwner");
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  64 */     if (!this.leftOwner) {
/*  65 */       this.leftOwner = checkLeftOwner();
/*     */     }
/*     */     
/*  68 */     super.tick();
/*     */   }
/*     */   
/*     */   private boolean checkLeftOwner() {
/*  72 */     Entity debug1 = getOwner();
/*  73 */     if (debug1 != null) {
/*  74 */       for (Entity debug3 : this.level.getEntities(this, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), debug0 -> (!debug0.isSpectator() && debug0.isPickable()))) {
/*  75 */         if (debug3.getRootVehicle() == debug1.getRootVehicle()) {
/*  76 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shoot(double debug1, double debug3, double debug5, float debug7, float debug8) {
/*  88 */     Vec3 debug9 = (new Vec3(debug1, debug3, debug5)).normalize().add(this.random.nextGaussian() * 0.007499999832361937D * debug8, this.random.nextGaussian() * 0.007499999832361937D * debug8, this.random.nextGaussian() * 0.007499999832361937D * debug8).scale(debug7);
/*     */     
/*  90 */     setDeltaMovement(debug9);
/*     */     
/*  92 */     float debug10 = Mth.sqrt(getHorizontalDistanceSqr(debug9));
/*     */     
/*  94 */     this.yRot = (float)(Mth.atan2(debug9.x, debug9.z) * 57.2957763671875D);
/*  95 */     this.xRot = (float)(Mth.atan2(debug9.y, debug10) * 57.2957763671875D);
/*  96 */     this.yRotO = this.yRot;
/*  97 */     this.xRotO = this.xRot;
/*     */   }
/*     */   
/*     */   public void shootFromRotation(Entity debug1, float debug2, float debug3, float debug4, float debug5, float debug6) {
/* 101 */     float debug7 = -Mth.sin(debug3 * 0.017453292F) * Mth.cos(debug2 * 0.017453292F);
/* 102 */     float debug8 = -Mth.sin((debug2 + debug4) * 0.017453292F);
/* 103 */     float debug9 = Mth.cos(debug3 * 0.017453292F) * Mth.cos(debug2 * 0.017453292F);
/* 104 */     shoot(debug7, debug8, debug9, debug5, debug6);
/*     */ 
/*     */     
/* 107 */     Vec3 debug10 = debug1.getDeltaMovement();
/* 108 */     setDeltaMovement(getDeltaMovement().add(debug10.x, 
/*     */           
/* 110 */           debug1.isOnGround() ? 0.0D : debug10.y, debug10.z));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult debug1) {
/* 116 */     HitResult.Type debug2 = debug1.getType();
/* 117 */     if (debug2 == HitResult.Type.ENTITY) {
/* 118 */       onHitEntity((EntityHitResult)debug1);
/* 119 */     } else if (debug2 == HitResult.Type.BLOCK) {
/* 120 */       onHitBlock((BlockHitResult)debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {}
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/* 128 */     BlockHitResult debug2 = debug1;
/* 129 */     BlockState debug3 = this.level.getBlockState(debug2.getBlockPos());
/* 130 */     debug3.onProjectileHit(this.level, debug3, debug2, this);
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
/*     */   protected boolean canHitEntity(Entity debug1) {
/* 147 */     if (debug1.isSpectator() || !debug1.isAlive() || !debug1.isPickable()) {
/* 148 */       return false;
/*     */     }
/* 150 */     Entity debug2 = getOwner();
/* 151 */     return (debug2 == null || this.leftOwner || !debug2.isPassengerOfSameVehicle(debug1));
/*     */   }
/*     */   
/*     */   protected void updateRotation() {
/* 155 */     Vec3 debug1 = getDeltaMovement();
/* 156 */     float debug2 = Mth.sqrt(getHorizontalDistanceSqr(debug1));
/*     */     
/* 158 */     this.xRot = lerpRotation(this.xRotO, (float)(Mth.atan2(debug1.y, debug2) * 57.2957763671875D));
/* 159 */     this.yRot = lerpRotation(this.yRotO, (float)(Mth.atan2(debug1.x, debug1.z) * 57.2957763671875D));
/*     */   }
/*     */   
/*     */   protected static float lerpRotation(float debug0, float debug1) {
/* 163 */     while (debug1 - debug0 < -180.0F) {
/* 164 */       debug0 -= 360.0F;
/*     */     }
/* 166 */     while (debug1 - debug0 >= 180.0F) {
/* 167 */       debug0 += 360.0F;
/*     */     }
/* 169 */     return Mth.lerp(0.2F, debug0, debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\Projectile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */