/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvokerFangs
/*     */   extends Entity
/*     */ {
/*     */   private int warmupDelayTicks;
/*     */   private boolean sentSpikeEvent;
/*  29 */   private int lifeTicks = 22;
/*     */   
/*     */   private boolean clientSideAttackStarted;
/*     */   private LivingEntity owner;
/*     */   private UUID ownerUUID;
/*     */   
/*     */   public EvokerFangs(EntityType<? extends EvokerFangs> debug1, Level debug2) {
/*  36 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public EvokerFangs(Level debug1, double debug2, double debug4, double debug6, float debug8, int debug9, LivingEntity debug10) {
/*  40 */     this(EntityType.EVOKER_FANGS, debug1);
/*  41 */     this.warmupDelayTicks = debug9;
/*  42 */     setOwner(debug10);
/*  43 */     this.yRot = debug8 * 57.295776F;
/*  44 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */   
/*     */   public void setOwner(@Nullable LivingEntity debug1) {
/*  52 */     this.owner = debug1;
/*  53 */     this.ownerUUID = (debug1 == null) ? null : debug1.getUUID();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getOwner() {
/*  58 */     if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
/*  59 */       Entity debug1 = ((ServerLevel)this.level).getEntity(this.ownerUUID);
/*  60 */       if (debug1 instanceof LivingEntity) {
/*  61 */         this.owner = (LivingEntity)debug1;
/*     */       }
/*     */     } 
/*     */     
/*  65 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/*  70 */     this.warmupDelayTicks = debug1.getInt("Warmup");
/*  71 */     if (debug1.hasUUID("Owner")) {
/*  72 */       this.ownerUUID = debug1.getUUID("Owner");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  78 */     debug1.putInt("Warmup", this.warmupDelayTicks);
/*     */     
/*  80 */     if (this.ownerUUID != null) {
/*  81 */       debug1.putUUID("Owner", this.ownerUUID);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  87 */     super.tick();
/*     */     
/*  89 */     if (this.level.isClientSide) {
/*  90 */       if (this.clientSideAttackStarted) {
/*  91 */         this.lifeTicks--;
/*  92 */         if (this.lifeTicks == 14) {
/*  93 */           for (int debug1 = 0; debug1 < 12; debug1++) {
/*  94 */             double debug2 = getX() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  95 */             double debug4 = getY() + 0.05D + this.random.nextDouble();
/*  96 */             double debug6 = getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  97 */             double debug8 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  98 */             double debug10 = 0.3D + this.random.nextDouble() * 0.3D;
/*  99 */             double debug12 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/* 100 */             this.level.addParticle((ParticleOptions)ParticleTypes.CRIT, debug2, debug4 + 1.0D, debug6, debug8, debug10, debug12);
/*     */           }
/*     */         
/*     */         }
/*     */       } 
/* 105 */     } else if (--this.warmupDelayTicks < 0) {
/* 106 */       if (this.warmupDelayTicks == -8) {
/*     */         
/* 108 */         List<LivingEntity> debug1 = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.0D, 0.2D));
/* 109 */         for (LivingEntity debug3 : debug1) {
/* 110 */           dealDamageTo(debug3);
/*     */         }
/*     */       } 
/* 113 */       if (!this.sentSpikeEvent) {
/* 114 */         this.level.broadcastEntityEvent(this, (byte)4);
/* 115 */         this.sentSpikeEvent = true;
/*     */       } 
/* 117 */       if (--this.lifeTicks < 0) {
/* 118 */         remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealDamageTo(LivingEntity debug1) {
/* 125 */     LivingEntity debug2 = getOwner();
/* 126 */     if (!debug1.isAlive() || debug1.isInvulnerable() || debug1 == debug2) {
/*     */       return;
/*     */     }
/* 129 */     if (debug2 == null) {
/* 130 */       debug1.hurt(DamageSource.MAGIC, 6.0F);
/*     */     } else {
/* 132 */       if (debug2.isAlliedTo((Entity)debug1)) {
/*     */         return;
/*     */       }
/* 135 */       debug1.hurt(DamageSource.indirectMagic(this, (Entity)debug2), 6.0F);
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
/*     */   public Packet<?> getAddEntityPacket() {
/* 164 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\EvokerFangs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */