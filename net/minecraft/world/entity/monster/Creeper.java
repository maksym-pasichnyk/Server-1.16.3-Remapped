/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.AreaEffectCloud;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SwellGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.entity.animal.Ocelot;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Creeper
/*     */   extends Monster {
/*  45 */   private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);
/*  46 */   private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*  47 */   private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int oldSwell;
/*     */   private int swell;
/*  51 */   private int maxSwell = 30;
/*  52 */   private int explosionRadius = 3;
/*     */   private int droppedSkulls;
/*     */   
/*     */   public Creeper(EntityType<? extends Creeper> debug1, Level debug2) {
/*  56 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  61 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  62 */     this.goalSelector.addGoal(2, (Goal)new SwellGoal(this));
/*  63 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
/*  64 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal(this, Cat.class, 6.0F, 1.0D, 1.2D));
/*  65 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  66 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal(this, 0.8D));
/*  67 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  68 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  70 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  71 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  75 */     return Monster.createMonsterAttributes()
/*  76 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFallDistance() {
/*  81 */     if (getTarget() == null) {
/*  82 */       return 3;
/*     */     }
/*     */     
/*  85 */     return 3 + (int)(getHealth() - 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/*  90 */     boolean debug3 = super.causeFallDamage(debug1, debug2);
/*     */     
/*  92 */     this.swell = (int)(this.swell + debug1 * 1.5F);
/*  93 */     if (this.swell > this.maxSwell - 5) {
/*  94 */       this.swell = this.maxSwell - 5;
/*     */     }
/*  96 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 101 */     super.defineSynchedData();
/*     */     
/* 103 */     this.entityData.define(DATA_SWELL_DIR, Integer.valueOf(-1));
/* 104 */     this.entityData.define(DATA_IS_POWERED, Boolean.valueOf(false));
/* 105 */     this.entityData.define(DATA_IS_IGNITED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 110 */     super.addAdditionalSaveData(debug1);
/* 111 */     if (((Boolean)this.entityData.get(DATA_IS_POWERED)).booleanValue()) {
/* 112 */       debug1.putBoolean("powered", true);
/*     */     }
/* 114 */     debug1.putShort("Fuse", (short)this.maxSwell);
/* 115 */     debug1.putByte("ExplosionRadius", (byte)this.explosionRadius);
/* 116 */     debug1.putBoolean("ignited", isIgnited());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 121 */     super.readAdditionalSaveData(debug1);
/* 122 */     this.entityData.set(DATA_IS_POWERED, Boolean.valueOf(debug1.getBoolean("powered")));
/* 123 */     if (debug1.contains("Fuse", 99)) {
/* 124 */       this.maxSwell = debug1.getShort("Fuse");
/*     */     }
/* 126 */     if (debug1.contains("ExplosionRadius", 99)) {
/* 127 */       this.explosionRadius = debug1.getByte("ExplosionRadius");
/*     */     }
/* 129 */     if (debug1.getBoolean("ignited")) {
/* 130 */       ignite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 136 */     if (isAlive()) {
/* 137 */       this.oldSwell = this.swell;
/*     */ 
/*     */       
/* 140 */       if (isIgnited()) {
/* 141 */         setSwellDir(1);
/*     */       }
/*     */       
/* 144 */       int debug1 = getSwellDir();
/* 145 */       if (debug1 > 0 && this.swell == 0) {
/* 146 */         playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
/*     */       }
/* 148 */       this.swell += debug1;
/* 149 */       if (this.swell < 0) {
/* 150 */         this.swell = 0;
/*     */       }
/* 152 */       if (this.swell >= this.maxSwell) {
/* 153 */         this.swell = this.maxSwell;
/* 154 */         explodeCreeper();
/*     */       } 
/*     */     } 
/* 157 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 162 */     return SoundEvents.CREEPER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 167 */     return SoundEvents.CREEPER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 172 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/* 173 */     Entity debug4 = debug1.getEntity();
/* 174 */     if (debug4 != this && debug4 instanceof Creeper) {
/* 175 */       Creeper debug5 = (Creeper)debug4;
/* 176 */       if (debug5.canDropMobsSkull()) {
/* 177 */         debug5.increaseDroppedSkulls();
/* 178 */         spawnAtLocation((ItemLike)Items.CREEPER_HEAD);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 185 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPowered() {
/* 190 */     return ((Boolean)this.entityData.get(DATA_IS_POWERED)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSwellDir() {
/* 198 */     return ((Integer)this.entityData.get(DATA_SWELL_DIR)).intValue();
/*     */   }
/*     */   
/*     */   public void setSwellDir(int debug1) {
/* 202 */     this.entityData.set(DATA_SWELL_DIR, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/* 207 */     super.thunderHit(debug1, debug2);
/* 208 */     this.entityData.set(DATA_IS_POWERED, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 213 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 214 */     if (debug3.getItem() == Items.FLINT_AND_STEEL) {
/* 215 */       this.level.playSound(debug1, getX(), getY(), getZ(), SoundEvents.FLINTANDSTEEL_USE, getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
/* 216 */       if (!this.level.isClientSide) {
/* 217 */         ignite();
/* 218 */         debug3.hurtAndBreak(1, (LivingEntity)debug1, debug1 -> debug1.broadcastBreakEvent(debug0));
/*     */       } 
/* 220 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 223 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */   
/*     */   private void explodeCreeper() {
/* 227 */     if (!this.level.isClientSide) {
/* 228 */       Explosion.BlockInteraction debug1 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
/* 229 */       float debug2 = isPowered() ? 2.0F : 1.0F;
/* 230 */       this.dead = true;
/* 231 */       this.level.explode((Entity)this, getX(), getY(), getZ(), this.explosionRadius * debug2, debug1);
/* 232 */       remove();
/* 233 */       spawnLingeringCloud();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnLingeringCloud() {
/* 238 */     Collection<MobEffectInstance> debug1 = getActiveEffects();
/* 239 */     if (!debug1.isEmpty()) {
/* 240 */       AreaEffectCloud debug2 = new AreaEffectCloud(this.level, getX(), getY(), getZ());
/* 241 */       debug2.setRadius(2.5F);
/* 242 */       debug2.setRadiusOnUse(-0.5F);
/* 243 */       debug2.setWaitTime(10);
/* 244 */       debug2.setDuration(debug2.getDuration() / 2);
/* 245 */       debug2.setRadiusPerTick(-debug2.getRadius() / debug2.getDuration());
/* 246 */       for (MobEffectInstance debug4 : debug1) {
/* 247 */         debug2.addEffect(new MobEffectInstance(debug4));
/*     */       }
/* 249 */       this.level.addFreshEntity((Entity)debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isIgnited() {
/* 254 */     return ((Boolean)this.entityData.get(DATA_IS_IGNITED)).booleanValue();
/*     */   }
/*     */   
/*     */   public void ignite() {
/* 258 */     this.entityData.set(DATA_IS_IGNITED, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public boolean canDropMobsSkull() {
/* 262 */     return (isPowered() && this.droppedSkulls < 1);
/*     */   }
/*     */   
/*     */   public void increaseDroppedSkulls() {
/* 266 */     this.droppedSkulls++;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Creeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */