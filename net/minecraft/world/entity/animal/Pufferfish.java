/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Pufferfish extends AbstractFish {
/*  31 */   private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);
/*     */   private int inflateCounter;
/*     */   
/*     */   static {
/*  35 */     NO_SPECTATORS_AND_NO_WATER_MOB = (debug0 -> (debug0 == null) ? false : (
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  40 */       (debug0 instanceof Player && (debug0.isSpectator() || ((Player)debug0).isCreative())) ? false : ((debug0.getMobType() != MobType.WATER))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int deflateTimer;
/*     */ 
/*     */   
/*     */   private static final Predicate<LivingEntity> NO_SPECTATORS_AND_NO_WATER_MOB;
/*     */ 
/*     */   
/*     */   public Pufferfish(EntityType<? extends Pufferfish> debug1, Level debug2) {
/*  52 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  57 */     super.defineSynchedData();
/*     */     
/*  59 */     this.entityData.define(PUFF_STATE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public int getPuffState() {
/*  63 */     return ((Integer)this.entityData.get(PUFF_STATE)).intValue();
/*     */   }
/*     */   
/*     */   public void setPuffState(int debug1) {
/*  67 */     this.entityData.set(PUFF_STATE, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/*  72 */     if (PUFF_STATE.equals(debug1)) {
/*  73 */       refreshDimensions();
/*     */     }
/*     */     
/*  76 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  81 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  83 */     debug1.putInt("PuffState", getPuffState());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  88 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  90 */     setPuffState(debug1.getInt("PuffState"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getBucketItemStack() {
/*  95 */     return new ItemStack((ItemLike)Items.PUFFERFISH_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 100 */     super.registerGoals();
/*     */     
/* 102 */     this.goalSelector.addGoal(1, new PufferfishPuffGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 107 */     if (!this.level.isClientSide && isAlive() && isEffectiveAi()) {
/* 108 */       if (this.inflateCounter > 0) {
/*     */         
/* 110 */         if (getPuffState() == 0) {
/* 111 */           playSound(SoundEvents.PUFFER_FISH_BLOW_UP, getSoundVolume(), getVoicePitch());
/* 112 */           setPuffState(1);
/*     */         }
/* 114 */         else if (this.inflateCounter > 40 && getPuffState() == 1) {
/* 115 */           playSound(SoundEvents.PUFFER_FISH_BLOW_UP, getSoundVolume(), getVoicePitch());
/* 116 */           setPuffState(2);
/*     */         } 
/*     */ 
/*     */         
/* 120 */         this.inflateCounter++;
/* 121 */       } else if (getPuffState() != 0) {
/*     */         
/* 123 */         if (this.deflateTimer > 60 && getPuffState() == 2) {
/* 124 */           playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, getSoundVolume(), getVoicePitch());
/* 125 */           setPuffState(1);
/* 126 */         } else if (this.deflateTimer > 100 && getPuffState() == 1) {
/* 127 */           playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, getSoundVolume(), getVoicePitch());
/* 128 */           setPuffState(0);
/*     */         } 
/*     */         
/* 131 */         this.deflateTimer++;
/*     */       } 
/*     */     }
/* 134 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 139 */     super.aiStep();
/*     */     
/* 141 */     if (isAlive() && getPuffState() > 0) {
/* 142 */       List<Mob> debug1 = this.level.getEntitiesOfClass(Mob.class, getBoundingBox().inflate(0.3D), NO_SPECTATORS_AND_NO_WATER_MOB);
/* 143 */       for (Mob debug3 : debug1) {
/* 144 */         if (debug3.isAlive()) {
/* 145 */           touch(debug3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void touch(Mob debug1) {
/* 152 */     int debug2 = getPuffState();
/* 153 */     if (debug1.hurt(DamageSource.mobAttack((LivingEntity)this), (1 + debug2))) {
/* 154 */       debug1.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * debug2, 0));
/* 155 */       playSound(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 161 */     int debug2 = getPuffState();
/* 162 */     if (debug1 instanceof ServerPlayer && debug2 > 0 && 
/* 163 */       debug1.hurt(DamageSource.mobAttack((LivingEntity)this), (1 + debug2))) {
/* 164 */       if (!isSilent()) {
/* 165 */         ((ServerPlayer)debug1).connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
/*     */       }
/* 167 */       debug1.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * debug2, 0));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 174 */     return SoundEvents.PUFFER_FISH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 179 */     return SoundEvents.PUFFER_FISH_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 184 */     return SoundEvents.PUFFER_FISH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 189 */     return SoundEvents.PUFFER_FISH_FLOP;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 194 */     return super.getDimensions(debug1).scale(getScale(getPuffState()));
/*     */   }
/*     */   
/*     */   private static float getScale(int debug0) {
/* 198 */     switch (debug0) {
/*     */       case 1:
/* 200 */         return 0.7F;
/*     */       case 0:
/* 202 */         return 0.5F;
/*     */     } 
/* 204 */     return 1.0F;
/*     */   }
/*     */   
/*     */   static class PufferfishPuffGoal
/*     */     extends Goal {
/*     */     private final Pufferfish fish;
/*     */     
/*     */     public PufferfishPuffGoal(Pufferfish debug1) {
/* 212 */       this.fish = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 217 */       List<LivingEntity> debug1 = this.fish.level.getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), Pufferfish.NO_SPECTATORS_AND_NO_WATER_MOB);
/*     */       
/* 219 */       return !debug1.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 224 */       this.fish.inflateCounter = 1;
/* 225 */       this.fish.deflateTimer = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 230 */       this.fish.inflateCounter = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 235 */       List<LivingEntity> debug1 = this.fish.level.getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), Pufferfish.NO_SPECTATORS_AND_NO_WATER_MOB);
/*     */       
/* 237 */       return !debug1.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Pufferfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */