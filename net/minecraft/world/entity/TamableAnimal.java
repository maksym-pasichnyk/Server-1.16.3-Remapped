/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ public abstract class TamableAnimal
/*     */   extends Animal
/*     */ {
/*  25 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.BYTE);
/*  26 */   protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_UUID);
/*     */   
/*     */   private boolean orderedToSit;
/*     */   
/*     */   protected TamableAnimal(EntityType<? extends TamableAnimal> debug1, Level debug2) {
/*  31 */     super(debug1, debug2);
/*  32 */     reassessTameGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  37 */     super.defineSynchedData();
/*  38 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  39 */     this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  44 */     super.addAdditionalSaveData(debug1);
/*  45 */     if (getOwnerUUID() != null) {
/*  46 */       debug1.putUUID("Owner", getOwnerUUID());
/*     */     }
/*  48 */     debug1.putBoolean("Sitting", this.orderedToSit);
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*     */     UUID debug2;
/*  53 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  55 */     if (debug1.hasUUID("Owner")) {
/*  56 */       debug2 = debug1.getUUID("Owner");
/*     */     } else {
/*  58 */       String debug3 = debug1.getString("Owner");
/*  59 */       debug2 = OldUsersConverter.convertMobOwnerIfNecessary(getServer(), debug3);
/*     */     } 
/*  61 */     if (debug2 != null) {
/*     */       try {
/*  63 */         setOwnerUUID(debug2);
/*  64 */         setTame(true);
/*  65 */       } catch (Throwable debug3) {
/*  66 */         setTame(false);
/*     */       } 
/*     */     }
/*  69 */     this.orderedToSit = debug1.getBoolean("Sitting");
/*  70 */     setInSittingPose(this.orderedToSit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/*  75 */     return !isLeashed();
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
/*     */   public boolean isTame() {
/* 103 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public void setTame(boolean debug1) {
/* 107 */     byte debug2 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 108 */     if (debug1) {
/* 109 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 | 0x4)));
/*     */     } else {
/* 111 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 & 0xFFFFFFFB)));
/*     */     } 
/*     */     
/* 114 */     reassessTameGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reassessTameGoals() {}
/*     */   
/*     */   public boolean isInSittingPose() {
/* 121 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setInSittingPose(boolean debug1) {
/* 125 */     byte debug2 = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 126 */     if (debug1) {
/* 127 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 | 0x1)));
/*     */     } else {
/* 129 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(debug2 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public UUID getOwnerUUID() {
/* 136 */     return ((Optional<UUID>)this.entityData.get(DATA_OWNERUUID_ID)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setOwnerUUID(@Nullable UUID debug1) {
/* 140 */     this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(debug1));
/*     */   }
/*     */   
/*     */   public void tame(Player debug1) {
/* 144 */     setTame(true);
/* 145 */     setOwnerUUID(debug1.getUUID());
/* 146 */     if (debug1 instanceof ServerPlayer) {
/* 147 */       CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)debug1, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getOwner() {
/*     */     try {
/* 155 */       UUID debug1 = getOwnerUUID();
/* 156 */       if (debug1 == null) {
/* 157 */         return null;
/*     */       }
/* 159 */       return (LivingEntity)this.level.getPlayerByUUID(debug1);
/* 160 */     } catch (IllegalArgumentException debug1) {
/* 161 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttack(LivingEntity debug1) {
/* 167 */     if (isOwnedBy(debug1)) {
/* 168 */       return false;
/*     */     }
/* 170 */     return super.canAttack(debug1);
/*     */   }
/*     */   
/*     */   public boolean isOwnedBy(LivingEntity debug1) {
/* 174 */     return (debug1 == getOwner());
/*     */   }
/*     */   
/*     */   public boolean wantsToAttack(LivingEntity debug1, LivingEntity debug2) {
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Team getTeam() {
/* 183 */     if (isTame()) {
/* 184 */       LivingEntity debug1 = getOwner();
/* 185 */       if (debug1 != null) {
/* 186 */         return debug1.getTeam();
/*     */       }
/*     */     } 
/* 189 */     return super.getTeam();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlliedTo(Entity debug1) {
/* 194 */     if (isTame()) {
/* 195 */       LivingEntity debug2 = getOwner();
/* 196 */       if (debug1 == debug2) {
/* 197 */         return true;
/*     */       }
/* 199 */       if (debug2 != null) {
/* 200 */         return debug2.isAlliedTo(debug1);
/*     */       }
/*     */     } 
/* 203 */     return super.isAlliedTo(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource debug1) {
/* 208 */     if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && 
/* 209 */       getOwner() instanceof ServerPlayer) {
/* 210 */       getOwner().sendMessage(getCombatTracker().getDeathMessage(), Util.NIL_UUID);
/*     */     }
/*     */     
/* 213 */     super.die(debug1);
/*     */   }
/*     */   
/*     */   public boolean isOrderedToSit() {
/* 217 */     return this.orderedToSit;
/*     */   }
/*     */   
/*     */   public void setOrderedToSit(boolean debug1) {
/* 221 */     this.orderedToSit = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\TamableAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */