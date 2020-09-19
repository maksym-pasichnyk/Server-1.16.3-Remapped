/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class PrimedTnt
/*     */   extends Entity {
/*  23 */   private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.INT);
/*     */   
/*     */   @Nullable
/*     */   private LivingEntity owner;
/*     */   
/*  28 */   private int life = 80;
/*     */   
/*     */   public PrimedTnt(EntityType<? extends PrimedTnt> debug1, Level debug2) {
/*  31 */     super(debug1, debug2);
/*  32 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public PrimedTnt(Level debug1, double debug2, double debug4, double debug6, @Nullable LivingEntity debug8) {
/*  36 */     this(EntityType.TNT, debug1);
/*     */     
/*  38 */     setPos(debug2, debug4, debug6);
/*     */     
/*  40 */     double debug9 = debug1.random.nextDouble() * 6.2831854820251465D;
/*     */     
/*  42 */     setDeltaMovement(
/*  43 */         -Math.sin(debug9) * 0.02D, 0.20000000298023224D, 
/*     */         
/*  45 */         -Math.cos(debug9) * 0.02D);
/*     */ 
/*     */     
/*  48 */     setFuse(80);
/*     */     
/*  50 */     this.xo = debug2;
/*  51 */     this.yo = debug4;
/*  52 */     this.zo = debug6;
/*  53 */     this.owner = debug8;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  58 */     this.entityData.define(DATA_FUSE_ID, Integer.valueOf(80));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  68 */     return !this.removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  73 */     if (!isNoGravity()) {
/*  74 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
/*     */     }
/*  76 */     move(MoverType.SELF, getDeltaMovement());
/*  77 */     setDeltaMovement(getDeltaMovement().scale(0.98D));
/*     */     
/*  79 */     if (this.onGround)
/*     */     {
/*  81 */       setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/*     */     }
/*     */     
/*  84 */     this.life--;
/*  85 */     if (this.life <= 0) {
/*  86 */       remove();
/*  87 */       if (!this.level.isClientSide) {
/*  88 */         explode();
/*     */       }
/*     */     } else {
/*  91 */       updateInWaterStateAndDoFluidPushing();
/*  92 */       if (this.level.isClientSide) {
/*  93 */         this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void explode() {
/*  99 */     float debug1 = 4.0F;
/* 100 */     this.level.explode(this, getX(), getY(0.0625D), getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 105 */     debug1.putShort("Fuse", (short)getLife());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 110 */     setFuse(debug1.getShort("Fuse"));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getOwner() {
/* 115 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 120 */     return 0.15F;
/*     */   }
/*     */   
/*     */   public void setFuse(int debug1) {
/* 124 */     this.entityData.set(DATA_FUSE_ID, Integer.valueOf(debug1));
/* 125 */     this.life = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 130 */     if (DATA_FUSE_ID.equals(debug1)) {
/* 131 */       this.life = getFuse();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getFuse() {
/* 136 */     return ((Integer)this.entityData.get(DATA_FUSE_ID)).intValue();
/*     */   }
/*     */   
/*     */   public int getLife() {
/* 140 */     return this.life;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 145 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\item\PrimedTnt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */