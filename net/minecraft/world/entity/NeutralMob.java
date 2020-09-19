/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface NeutralMob
/*     */ {
/*     */   int getRemainingPersistentAngerTime();
/*     */   
/*     */   void setRemainingPersistentAngerTime(int paramInt);
/*     */   
/*     */   @Nullable
/*     */   UUID getPersistentAngerTarget();
/*     */   
/*     */   void setPersistentAngerTarget(@Nullable UUID paramUUID);
/*     */   
/*     */   void startPersistentAngerTimer();
/*     */   
/*     */   default void addPersistentAngerSaveData(CompoundTag debug1) {
/*  57 */     debug1.putInt("AngerTime", getRemainingPersistentAngerTime());
/*  58 */     if (getPersistentAngerTarget() != null) {
/*  59 */       debug1.putUUID("AngryAt", getPersistentAngerTarget());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void readPersistentAngerSaveData(ServerLevel debug1, CompoundTag debug2) {
/*  67 */     setRemainingPersistentAngerTime(debug2.getInt("AngerTime"));
/*  68 */     if (!debug2.hasUUID("AngryAt")) {
/*  69 */       setPersistentAngerTarget(null);
/*     */       
/*     */       return;
/*     */     } 
/*  73 */     UUID debug3 = debug2.getUUID("AngryAt");
/*  74 */     setPersistentAngerTarget(debug3);
/*     */     
/*  76 */     Entity debug4 = debug1.getEntity(debug3);
/*  77 */     if (debug4 == null) {
/*     */       return;
/*     */     }
/*  80 */     if (debug4 instanceof Mob) {
/*  81 */       setLastHurtByMob((Mob)debug4);
/*     */     }
/*  83 */     if (debug4.getType() == EntityType.PLAYER) {
/*  84 */       setLastHurtByPlayer((Player)debug4);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void updatePersistentAnger(ServerLevel debug1, boolean debug2) {
/*  92 */     LivingEntity debug3 = getTarget();
/*     */     
/*  94 */     UUID debug4 = getPersistentAngerTarget();
/*  95 */     if ((debug3 == null || debug3.isDeadOrDying()) && debug4 != null && debug1.getEntity(debug4) instanceof Mob) {
/*     */ 
/*     */ 
/*     */       
/*  99 */       stopBeingAngry();
/*     */       
/*     */       return;
/*     */     } 
/* 103 */     if (debug3 != null && !Objects.equals(debug4, debug3.getUUID())) {
/*     */       
/* 105 */       setPersistentAngerTarget(debug3.getUUID());
/* 106 */       startPersistentAngerTimer();
/*     */     } 
/*     */ 
/*     */     
/* 110 */     if (getRemainingPersistentAngerTime() > 0 && (
/* 111 */       debug3 == null || debug3.getType() != EntityType.PLAYER || !debug2)) {
/* 112 */       setRemainingPersistentAngerTime(getRemainingPersistentAngerTime() - 1);
/* 113 */       if (getRemainingPersistentAngerTime() == 0) {
/* 114 */         stopBeingAngry();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAngryAt(LivingEntity debug1) {
/* 124 */     if (!EntitySelector.ATTACK_ALLOWED.test(debug1)) {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     if (debug1.getType() == EntityType.PLAYER && isAngryAtAllPlayers(debug1.level)) {
/* 129 */       return true;
/*     */     }
/*     */     
/* 132 */     return debug1.getUUID().equals(getPersistentAngerTarget());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAngryAtAllPlayers(Level debug1) {
/* 138 */     return (debug1.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && isAngry() && getPersistentAngerTarget() == null);
/*     */   }
/*     */   
/*     */   default boolean isAngry() {
/* 142 */     return (getRemainingPersistentAngerTime() > 0);
/*     */   }
/*     */   
/*     */   default void playerDied(Player debug1) {
/* 146 */     if (!debug1.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
/*     */       return;
/*     */     }
/*     */     
/* 150 */     if (!debug1.getUUID().equals(getPersistentAngerTarget())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 155 */     stopBeingAngry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void forgetCurrentTargetAndRefreshUniversalAnger() {
/* 162 */     stopBeingAngry();
/* 163 */     startPersistentAngerTimer();
/*     */   }
/*     */   
/*     */   default void stopBeingAngry() {
/* 167 */     setLastHurtByMob(null);
/* 168 */     setPersistentAngerTarget(null);
/* 169 */     setTarget(null);
/* 170 */     setRemainingPersistentAngerTime(0);
/*     */   }
/*     */   
/*     */   void setLastHurtByMob(@Nullable LivingEntity paramLivingEntity);
/*     */   
/*     */   void setLastHurtByPlayer(@Nullable Player paramPlayer);
/*     */   
/*     */   void setTarget(@Nullable LivingEntity paramLivingEntity);
/*     */   
/*     */   @Nullable
/*     */   LivingEntity getTarget();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\NeutralMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */