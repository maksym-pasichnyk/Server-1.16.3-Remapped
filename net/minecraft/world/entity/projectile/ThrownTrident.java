/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownTrident
/*     */   extends AbstractArrow {
/*  30 */   private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
/*  31 */   private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  33 */   private ItemStack tridentItem = new ItemStack((ItemLike)Items.TRIDENT);
/*     */   
/*     */   private boolean dealtDamage;
/*     */   public int clientSideReturnTridentTickCount;
/*     */   
/*     */   public ThrownTrident(EntityType<? extends ThrownTrident> debug1, Level debug2) {
/*  39 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level debug1, LivingEntity debug2, ItemStack debug3) {
/*  43 */     super(EntityType.TRIDENT, debug2, debug1);
/*  44 */     this.tridentItem = debug3.copy();
/*  45 */     this.entityData.set(ID_LOYALTY, Byte.valueOf((byte)EnchantmentHelper.getLoyalty(debug3)));
/*  46 */     this.entityData.set(ID_FOIL, Boolean.valueOf(debug3.hasFoil()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  55 */     super.defineSynchedData();
/*     */     
/*  57 */     this.entityData.define(ID_LOYALTY, Byte.valueOf((byte)0));
/*  58 */     this.entityData.define(ID_FOIL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  63 */     if (this.inGroundTime > 4) {
/*  64 */       this.dealtDamage = true;
/*     */     }
/*     */     
/*  67 */     Entity debug1 = getOwner();
/*  68 */     if ((this.dealtDamage || isNoPhysics()) && debug1 != null) {
/*  69 */       int debug2 = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/*  70 */       if (debug2 > 0 && !isAcceptibleReturnOwner()) {
/*  71 */         if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
/*  72 */           spawnAtLocation(getPickupItem(), 0.1F);
/*     */         }
/*  74 */         remove();
/*  75 */       } else if (debug2 > 0) {
/*  76 */         setNoPhysics(true);
/*  77 */         Vec3 debug3 = new Vec3(debug1.getX() - getX(), debug1.getEyeY() - getY(), debug1.getZ() - getZ());
/*  78 */         setPosRaw(getX(), getY() + debug3.y * 0.015D * debug2, getZ());
/*  79 */         if (this.level.isClientSide) {
/*  80 */           this.yOld = getY();
/*     */         }
/*  82 */         double debug4 = 0.05D * debug2;
/*  83 */         setDeltaMovement(getDeltaMovement().scale(0.95D).add(debug3.normalize().scale(debug4)));
/*     */         
/*  85 */         if (this.clientSideReturnTridentTickCount == 0) {
/*  86 */           playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
/*     */         }
/*     */         
/*  89 */         this.clientSideReturnTridentTickCount++;
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     super.tick();
/*     */   }
/*     */   
/*     */   private boolean isAcceptibleReturnOwner() {
/*  97 */     Entity debug1 = getOwner();
/*  98 */     if (debug1 == null || !debug1.isAlive()) {
/*  99 */       return false;
/*     */     }
/* 101 */     if (debug1 instanceof ServerPlayer && debug1.isSpectator()) {
/* 102 */       return false;
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getPickupItem() {
/* 109 */     return this.tridentItem.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected EntityHitResult findHitEntity(Vec3 debug1, Vec3 debug2) {
/* 119 */     if (this.dealtDamage) {
/* 120 */       return null;
/*     */     }
/* 122 */     return super.findHitEntity(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/* 127 */     Entity debug2 = debug1.getEntity();
/* 128 */     float debug3 = 8.0F;
/*     */     
/* 130 */     if (debug2 instanceof LivingEntity) {
/* 131 */       LivingEntity livingEntity = (LivingEntity)debug2;
/* 132 */       debug3 += EnchantmentHelper.getDamageBonus(this.tridentItem, livingEntity.getMobType());
/*     */     } 
/*     */     
/* 135 */     Entity debug4 = getOwner();
/* 136 */     DamageSource debug5 = DamageSource.trident(this, (debug4 == null) ? this : debug4);
/*     */     
/* 138 */     this.dealtDamage = true;
/* 139 */     SoundEvent debug6 = SoundEvents.TRIDENT_HIT;
/*     */     
/* 141 */     if (debug2.hurt(debug5, debug3)) {
/* 142 */       if (debug2.getType() == EntityType.ENDERMAN) {
/*     */         return;
/*     */       }
/* 145 */       if (debug2 instanceof LivingEntity) {
/* 146 */         LivingEntity livingEntity = (LivingEntity)debug2;
/* 147 */         if (debug4 instanceof LivingEntity) {
/* 148 */           EnchantmentHelper.doPostHurtEffects(livingEntity, debug4);
/* 149 */           EnchantmentHelper.doPostDamageEffects((LivingEntity)debug4, (Entity)livingEntity);
/*     */         } 
/* 151 */         doPostHurtEffects(livingEntity);
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     setDeltaMovement(getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
/*     */     
/* 157 */     float debug7 = 1.0F;
/* 158 */     if (this.level instanceof net.minecraft.server.level.ServerLevel && this.level.isThundering() && EnchantmentHelper.hasChanneling(this.tridentItem)) {
/* 159 */       BlockPos debug8 = debug2.blockPosition();
/* 160 */       if (this.level.canSeeSky(debug8)) {
/* 161 */         LightningBolt debug9 = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this.level);
/* 162 */         debug9.moveTo(Vec3.atBottomCenterOf((Vec3i)debug8));
/* 163 */         debug9.setCause((debug4 instanceof ServerPlayer) ? (ServerPlayer)debug4 : null);
/* 164 */         this.level.addFreshEntity((Entity)debug9);
/* 165 */         debug6 = SoundEvents.TRIDENT_THUNDER;
/* 166 */         debug7 = 5.0F;
/*     */       } 
/*     */     } 
/* 169 */     playSound(debug6, debug7, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 174 */     return SoundEvents.TRIDENT_HIT_GROUND;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 179 */     Entity debug2 = getOwner();
/*     */     
/* 181 */     if (debug2 != null && debug2.getUUID() != debug1.getUUID()) {
/*     */       return;
/*     */     }
/* 184 */     super.playerTouch(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 189 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 191 */     if (debug1.contains("Trident", 10)) {
/* 192 */       this.tridentItem = ItemStack.of(debug1.getCompound("Trident"));
/*     */     }
/* 194 */     this.dealtDamage = debug1.getBoolean("DealtDamage");
/*     */     
/* 196 */     this.entityData.set(ID_LOYALTY, Byte.valueOf((byte)EnchantmentHelper.getLoyalty(this.tridentItem)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 201 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 203 */     debug1.put("Trident", (Tag)this.tridentItem.save(new CompoundTag()));
/* 204 */     debug1.putBoolean("DealtDamage", this.dealtDamage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickDespawn() {
/* 209 */     int debug1 = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/*     */     
/* 211 */     if (this.pickup != AbstractArrow.Pickup.ALLOWED || debug1 <= 0) {
/* 212 */       super.tickDespawn();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterInertia() {
/* 218 */     return 0.99F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrownTrident.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */