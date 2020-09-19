/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class Ocelot
/*     */   extends Animal
/*     */ {
/*  60 */   private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.COD, (ItemLike)Items.SALMON });
/*     */   
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(Ocelot.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private OcelotAvoidEntityGoal<Player> ocelotAvoidPlayersGoal;
/*     */   private OcelotTemptGoal temptGoal;
/*     */   
/*     */   public Ocelot(EntityType<? extends Ocelot> debug1, Level debug2) {
/*  68 */     super((EntityType)debug1, debug2);
/*     */     
/*  70 */     reassessTrustingGoals();
/*     */   }
/*     */   
/*     */   private boolean isTrusting() {
/*  74 */     return ((Boolean)this.entityData.get(DATA_TRUSTING)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setTrusting(boolean debug1) {
/*  78 */     this.entityData.set(DATA_TRUSTING, Boolean.valueOf(debug1));
/*     */     
/*  80 */     reassessTrustingGoals();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  85 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  87 */     debug1.putBoolean("Trusting", isTrusting());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  92 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  94 */     setTrusting(debug1.getBoolean("Trusting"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  99 */     super.defineSynchedData();
/*     */     
/* 101 */     this.entityData.define(DATA_TRUSTING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 106 */     this.temptGoal = new OcelotTemptGoal(this, 0.6D, TEMPT_INGREDIENT, true);
/* 107 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 108 */     this.goalSelector.addGoal(3, (Goal)this.temptGoal);
/* 109 */     this.goalSelector.addGoal(7, (Goal)new LeapAtTargetGoal((Mob)this, 0.3F));
/* 110 */     this.goalSelector.addGoal(8, (Goal)new OcelotAttackGoal((Mob)this));
/* 111 */     this.goalSelector.addGoal(9, (Goal)new BreedGoal(this, 0.8D));
/* 112 */     this.goalSelector.addGoal(10, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D, 1.0000001E-5F));
/* 113 */     this.goalSelector.addGoal(11, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/*     */     
/* 115 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Chicken.class, false));
/* 116 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep() {
/* 121 */     if (getMoveControl().hasWanted()) {
/* 122 */       double debug1 = getMoveControl().getSpeedModifier();
/* 123 */       if (debug1 == 0.6D) {
/* 124 */         setPose(Pose.CROUCHING);
/* 125 */         setSprinting(false);
/* 126 */       } else if (debug1 == 1.33D) {
/* 127 */         setPose(Pose.STANDING);
/* 128 */         setSprinting(true);
/*     */       } else {
/* 130 */         setPose(Pose.STANDING);
/* 131 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 134 */       setPose(Pose.STANDING);
/* 135 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 141 */     return (!isTrusting() && this.tickCount > 2400);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 145 */     return Mob.createMobAttributes()
/* 146 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 147 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 148 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 159 */     return SoundEvents.OCELOT_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 164 */     return 900;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 169 */     return SoundEvents.OCELOT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 174 */     return SoundEvents.OCELOT_DEATH;
/*     */   }
/*     */   
/*     */   private float getAttackDamage() {
/* 178 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 183 */     return debug1.hurt(DamageSource.mobAttack((LivingEntity)this), getAttackDamage());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 188 */     if (isInvulnerableTo(debug1)) {
/* 189 */       return false;
/*     */     }
/*     */     
/* 192 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 197 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 198 */     if ((this.temptGoal == null || this.temptGoal.isRunning()) && !isTrusting() && isFood(debug3) && debug1.distanceToSqr((Entity)this) < 9.0D) {
/* 199 */       usePlayerItem(debug1, debug3);
/*     */       
/* 201 */       if (!this.level.isClientSide) {
/* 202 */         if (this.random.nextInt(3) == 0) {
/* 203 */           setTrusting(true);
/* 204 */           spawnTrustingParticles(true);
/* 205 */           this.level.broadcastEntityEvent((Entity)this, (byte)41);
/*     */         } else {
/* 207 */           spawnTrustingParticles(false);
/* 208 */           this.level.broadcastEntityEvent((Entity)this, (byte)40);
/*     */         } 
/*     */       }
/*     */       
/* 212 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 215 */     return super.mobInteract(debug1, debug2);
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
/*     */   private void spawnTrustingParticles(boolean debug1) {
/* 230 */     SimpleParticleType simpleParticleType = ParticleTypes.HEART;
/* 231 */     if (!debug1) {
/* 232 */       simpleParticleType = ParticleTypes.SMOKE;
/*     */     }
/* 234 */     for (int debug3 = 0; debug3 < 7; debug3++) {
/* 235 */       double debug4 = this.random.nextGaussian() * 0.02D;
/* 236 */       double debug6 = this.random.nextGaussian() * 0.02D;
/* 237 */       double debug8 = this.random.nextGaussian() * 0.02D;
/* 238 */       this.level.addParticle((ParticleOptions)simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), debug4, debug6, debug8);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void reassessTrustingGoals() {
/* 243 */     if (this.ocelotAvoidPlayersGoal == null) {
/* 244 */       this.ocelotAvoidPlayersGoal = new OcelotAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 247 */     this.goalSelector.removeGoal((Goal)this.ocelotAvoidPlayersGoal);
/*     */     
/* 249 */     if (!isTrusting()) {
/* 250 */       this.goalSelector.addGoal(4, (Goal)this.ocelotAvoidPlayersGoal);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Ocelot getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 256 */     return (Ocelot)EntityType.OCELOT.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 261 */     return TEMPT_INGREDIENT.test(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkOcelotSpawnRules(EntityType<Ocelot> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 267 */     return (debug4.nextInt(3) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 272 */     if (debug1.isUnobstructed((Entity)this) && !debug1.containsAnyLiquid(getBoundingBox())) {
/* 273 */       BlockPos debug2 = blockPosition();
/* 274 */       if (debug2.getY() < debug1.getSeaLevel()) {
/* 275 */         return false;
/*     */       }
/*     */       
/* 278 */       BlockState debug3 = debug1.getBlockState(debug2.below());
/* 279 */       if (debug3.is(Blocks.GRASS_BLOCK) || debug3.is((Tag)BlockTags.LEAVES)) {
/* 280 */         return true;
/*     */       }
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 289 */     if (debug4 == null)
/*     */     {
/* 291 */       agableMobGroupData = new AgableMob.AgableMobGroupData(1.0F);
/*     */     }
/*     */     
/* 294 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class OcelotAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T>
/*     */   {
/*     */     private final Ocelot ocelot;
/*     */ 
/*     */     
/*     */     public OcelotAvoidEntityGoal(Ocelot debug1, Class<T> debug2, float debug3, double debug4, double debug6) {
/* 306 */       super((PathfinderMob)debug1, debug2, debug3, debug4, debug6, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
/* 307 */       this.ocelot = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 312 */       return (!this.ocelot.isTrusting() && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 317 */       return (!this.ocelot.isTrusting() && super.canContinueToUse());
/*     */     }
/*     */   }
/*     */   
/*     */   static class OcelotTemptGoal extends TemptGoal {
/*     */     private final Ocelot ocelot;
/*     */     
/*     */     public OcelotTemptGoal(Ocelot debug1, double debug2, Ingredient debug4, boolean debug5) {
/* 325 */       super((PathfinderMob)debug1, debug2, debug4, debug5);
/* 326 */       this.ocelot = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean canScare() {
/* 331 */       return (super.canScare() && !this.ocelot.isTrusting());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Ocelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */