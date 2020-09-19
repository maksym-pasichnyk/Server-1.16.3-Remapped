/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.ZombifiedPiglin;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ 
/*     */ public abstract class AbstractPiglin
/*     */   extends Monster
/*     */ {
/*  26 */   protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  28 */   protected int timeInOverworld = 0;
/*     */   
/*     */   public AbstractPiglin(EntityType<? extends AbstractPiglin> debug1, Level debug2) {
/*  31 */     super(debug1, debug2);
/*  32 */     setCanPickUpLoot(true);
/*  33 */     applyOpenDoorsAbility();
/*  34 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
/*  35 */     setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   private void applyOpenDoorsAbility() {
/*  39 */     if (GoalUtils.hasGroundPathNavigation((Mob)this)) {
/*  40 */       ((GroundPathNavigation)getNavigation()).setCanOpenDoors(true);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract boolean canHunt();
/*     */   
/*     */   public void setImmuneToZombification(boolean debug1) {
/*  47 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   protected boolean isImmuneToZombification() {
/*  51 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  56 */     super.defineSynchedData();
/*  57 */     this.entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  62 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  64 */     if (isImmuneToZombification()) {
/*  65 */       debug1.putBoolean("IsImmuneToZombification", true);
/*     */     }
/*  67 */     debug1.putInt("TimeInOverworld", this.timeInOverworld);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/*  72 */     return isBaby() ? -0.05D : -0.45D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  77 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  79 */     setImmuneToZombification(debug1.getBoolean("IsImmuneToZombification"));
/*  80 */     this.timeInOverworld = debug1.getInt("TimeInOverworld");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/*  85 */     super.customServerAiStep();
/*     */     
/*  87 */     if (isConverting()) {
/*  88 */       this.timeInOverworld++;
/*     */     } else {
/*  90 */       this.timeInOverworld = 0;
/*     */     } 
/*  92 */     if (this.timeInOverworld > 300) {
/*  93 */       playConvertedSound();
/*  94 */       finishConversion((ServerLevel)this.level);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/*  99 */     return (!this.level.dimensionType().piglinSafe() && !isImmuneToZombification() && !isNoAi());
/*     */   }
/*     */   
/*     */   protected void finishConversion(ServerLevel debug1) {
/* 103 */     ZombifiedPiglin debug2 = (ZombifiedPiglin)convertTo(EntityType.ZOMBIFIED_PIGLIN, true);
/* 104 */     if (debug2 != null) {
/* 105 */       debug2.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 110 */     return !isBaby();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getTarget() {
/* 119 */     return this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*     */   }
/*     */   
/*     */   protected boolean isHoldingMeleeWeapon() {
/* 123 */     return getMainHandItem().getItem() instanceof net.minecraft.world.item.TieredItem;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 128 */     if (PiglinAi.isIdle(this)) {
/* 129 */       super.playAmbientSound();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendDebugPackets() {
/* 135 */     super.sendDebugPackets();
/* 136 */     DebugPackets.sendEntityBrain((LivingEntity)this);
/*     */   }
/*     */   
/*     */   protected abstract void playConvertedSound();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\AbstractPiglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */