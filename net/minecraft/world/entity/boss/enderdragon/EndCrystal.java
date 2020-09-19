/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ 
/*     */ public class EndCrystal extends Entity {
/*  25 */   private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*  26 */   private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public int time;
/*     */   
/*     */   public EndCrystal(EntityType<? extends EndCrystal> debug1, Level debug2) {
/*  31 */     super(debug1, debug2);
/*  32 */     this.blocksBuilding = true;
/*     */     
/*  34 */     this.time = this.random.nextInt(100000);
/*     */   }
/*     */   
/*     */   public EndCrystal(Level debug1, double debug2, double debug4, double debug6) {
/*  38 */     this(EntityType.END_CRYSTAL, debug1);
/*  39 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  44 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  49 */     getEntityData().define(DATA_BEAM_TARGET, Optional.empty());
/*  50 */     getEntityData().define(DATA_SHOW_BOTTOM, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     this.time++;
/*     */     
/*  57 */     if (this.level instanceof ServerLevel) {
/*  58 */       BlockPos debug1 = blockPosition();
/*  59 */       if (((ServerLevel)this.level).dragonFight() != null && this.level.getBlockState(debug1).isAir()) {
/*  60 */         this.level.setBlockAndUpdate(debug1, BaseFireBlock.getState((BlockGetter)this.level, debug1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  67 */     if (getBeamTarget() != null) {
/*  68 */       debug1.put("BeamTarget", (Tag)NbtUtils.writeBlockPos(getBeamTarget()));
/*     */     }
/*  70 */     debug1.putBoolean("ShowBottom", showsBottom());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/*  75 */     if (debug1.contains("BeamTarget", 10)) {
/*  76 */       setBeamTarget(NbtUtils.readBlockPos(debug1.getCompound("BeamTarget")));
/*     */     }
/*  78 */     if (debug1.contains("ShowBottom", 1)) {
/*  79 */       setShowBottom(debug1.getBoolean("ShowBottom"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/*  90 */     if (isInvulnerableTo(debug1)) {
/*  91 */       return false;
/*     */     }
/*  93 */     if (debug1.getEntity() instanceof EnderDragon) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (!this.removed && !this.level.isClientSide) {
/*  97 */       remove();
/*     */       
/*  99 */       if (!debug1.isExplosion()) {
/* 100 */         this.level.explode(null, getX(), getY(), getZ(), 6.0F, Explosion.BlockInteraction.DESTROY);
/*     */       }
/* 102 */       onDestroyedBy(debug1);
/*     */     } 
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill() {
/* 109 */     onDestroyedBy(DamageSource.GENERIC);
/* 110 */     super.kill();
/*     */   }
/*     */   
/*     */   private void onDestroyedBy(DamageSource debug1) {
/* 114 */     if (this.level instanceof ServerLevel) {
/* 115 */       EndDragonFight debug2 = ((ServerLevel)this.level).dragonFight();
/* 116 */       if (debug2 != null) {
/* 117 */         debug2.onCrystalDestroyed(this, debug1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBeamTarget(@Nullable BlockPos debug1) {
/* 123 */     getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos getBeamTarget() {
/* 128 */     return ((Optional<BlockPos>)getEntityData().get(DATA_BEAM_TARGET)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setShowBottom(boolean debug1) {
/* 132 */     getEntityData().set(DATA_SHOW_BOTTOM, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean showsBottom() {
/* 136 */     return ((Boolean)getEntityData().get(DATA_SHOW_BOTTOM)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 146 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\EndCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */