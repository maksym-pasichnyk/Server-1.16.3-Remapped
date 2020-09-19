/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ZombifiedPiglin extends Zombie implements NeutralMob {
/*  42 */   private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
/*  43 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.05D, AttributeModifier.Operation.ADDITION);
/*     */   
/*  45 */   private static final IntRange FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
/*     */   
/*     */   private int playFirstAngerSoundIn;
/*  48 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private int remainingPersistentAngerTime;
/*     */   
/*     */   private UUID persistentAngerTarget;
/*  53 */   private static final IntRange ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
/*     */   private int ticksUntilNextAlert;
/*     */   
/*     */   public ZombifiedPiglin(EntityType<? extends ZombifiedPiglin> debug1, Level debug2) {
/*  57 */     super((EntityType)debug1, debug2);
/*  58 */     setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/*  63 */     this.persistentAngerTarget = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/*  68 */     return isBaby() ? -0.05D : -0.45D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  73 */     this.goalSelector.addGoal(2, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/*  74 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*     */     
/*  76 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  77 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  78 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  82 */     return Zombie.createAttributes()
/*  83 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
/*  84 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/*  85 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/*  95 */     AttributeInstance debug1 = getAttribute(Attributes.MOVEMENT_SPEED);
/*  96 */     if (isAngry()) {
/*  97 */       if (!isBaby() && !debug1.hasModifier(SPEED_MODIFIER_ATTACKING)) {
/*  98 */         debug1.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/* 100 */       maybePlayFirstAngerSound();
/* 101 */     } else if (debug1.hasModifier(SPEED_MODIFIER_ATTACKING)) {
/* 102 */       debug1.removeModifier(SPEED_MODIFIER_ATTACKING);
/*     */     } 
/*     */     
/* 105 */     updatePersistentAnger((ServerLevel)this.level, true);
/* 106 */     if (getTarget() != null) {
/* 107 */       maybeAlertOthers();
/*     */     }
/*     */     
/* 110 */     if (isAngry())
/*     */     {
/*     */ 
/*     */       
/* 114 */       this.lastHurtByPlayerTime = this.tickCount;
/*     */     }
/*     */     
/* 117 */     super.customServerAiStep();
/*     */   }
/*     */   
/*     */   private void maybePlayFirstAngerSound() {
/* 121 */     if (this.playFirstAngerSoundIn > 0) {
/* 122 */       this.playFirstAngerSoundIn--;
/* 123 */       if (this.playFirstAngerSoundIn == 0) {
/* 124 */         playAngerSound();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeAlertOthers() {
/* 134 */     if (this.ticksUntilNextAlert > 0) {
/* 135 */       this.ticksUntilNextAlert--;
/*     */       return;
/*     */     } 
/* 138 */     if (getSensing().canSee((Entity)getTarget())) {
/* 139 */       alertOthers();
/*     */     }
/* 141 */     this.ticksUntilNextAlert = ALERT_INTERVAL.randomValue(this.random);
/*     */   }
/*     */   
/*     */   private void alertOthers() {
/* 145 */     double debug1 = getAttributeValue(Attributes.FOLLOW_RANGE);
/* 146 */     AABB debug3 = AABB.unitCubeFromLowerCorner(position()).inflate(debug1, 10.0D, debug1);
/* 147 */     this.level.getLoadedEntitiesOfClass(ZombifiedPiglin.class, debug3).stream()
/* 148 */       .filter(debug1 -> (debug1 != this))
/* 149 */       .filter(debug0 -> (debug0.getTarget() == null))
/* 150 */       .filter(debug1 -> !debug1.isAlliedTo((Entity)getTarget()))
/* 151 */       .forEach(debug1 -> debug1.setTarget(getTarget()));
/*     */   }
/*     */   
/*     */   private void playAngerSound() {
/* 155 */     playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, getSoundVolume() * 2.0F, getVoicePitch() * 1.8F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(@Nullable LivingEntity debug1) {
/* 160 */     if (getTarget() == null && debug1 != null) {
/*     */ 
/*     */       
/* 163 */       this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.randomValue(this.random);
/* 164 */       this.ticksUntilNextAlert = ALERT_INTERVAL.randomValue(this.random);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     if (debug1 instanceof Player) {
/* 172 */       setLastHurtByPlayer((Player)debug1);
/*     */     }
/* 174 */     super.setTarget(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 179 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*     */   }
/*     */   
/*     */   public static boolean checkZombifiedPiglinSpawnRules(EntityType<ZombifiedPiglin> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 183 */     return (debug1.getDifficulty() != Difficulty.PEACEFUL && debug1.getBlockState(debug3.below()).getBlock() != Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 188 */     return (debug1.isUnobstructed((Entity)this) && !debug1.containsAnyLiquid(getBoundingBox()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 193 */     super.addAdditionalSaveData(debug1);
/* 194 */     addPersistentAngerSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 199 */     super.readAdditionalSaveData(debug1);
/* 200 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemainingPersistentAngerTime(int debug1) {
/* 205 */     this.remainingPersistentAngerTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingPersistentAngerTime() {
/* 210 */     return this.remainingPersistentAngerTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 215 */     if (isInvulnerableTo(debug1)) {
/* 216 */       return false;
/*     */     }
/* 218 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 223 */     return isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 228 */     return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 233 */     return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 238 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_SWORD));
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getSkull() {
/* 243 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 248 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getPersistentAngerTarget() {
/* 253 */     return this.persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPreventingPlayerRest(Player debug1) {
/* 258 */     return isAngryAt((LivingEntity)debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\ZombifiedPiglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */