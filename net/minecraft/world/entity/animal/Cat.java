/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class Cat
/*     */   extends TamableAnimal {
/*  80 */   private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.COD, (ItemLike)Items.SALMON });
/*     */   
/*  82 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);
/*  83 */   private static final EntityDataAccessor<Boolean> IS_LYING = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*  84 */   private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*  85 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE;
/*     */   
/*     */   private CatAvoidEntityGoal<Player> avoidPlayersGoal;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   
/*     */   private float lieDownAmount;
/*     */   
/*     */   private float lieDownAmountO;
/*     */   private float lieDownAmountTail;
/*     */   private float lieDownAmountOTail;
/*     */   private float relaxStateOneAmount;
/*     */   private float relaxStateOneAmountO;
/*     */   
/*     */   static {
/* 102 */     TEXTURE_BY_TYPE = (Map<Integer, ResourceLocation>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(Integer.valueOf(0), new ResourceLocation("textures/entity/cat/tabby.png"));
/*     */           debug0.put(Integer.valueOf(1), new ResourceLocation("textures/entity/cat/black.png"));
/*     */           debug0.put(Integer.valueOf(2), new ResourceLocation("textures/entity/cat/red.png"));
/*     */           debug0.put(Integer.valueOf(3), new ResourceLocation("textures/entity/cat/siamese.png"));
/*     */           debug0.put(Integer.valueOf(4), new ResourceLocation("textures/entity/cat/british_shorthair.png"));
/*     */           debug0.put(Integer.valueOf(5), new ResourceLocation("textures/entity/cat/calico.png"));
/*     */           debug0.put(Integer.valueOf(6), new ResourceLocation("textures/entity/cat/persian.png"));
/*     */           debug0.put(Integer.valueOf(7), new ResourceLocation("textures/entity/cat/ragdoll.png"));
/*     */           debug0.put(Integer.valueOf(8), new ResourceLocation("textures/entity/cat/white.png"));
/*     */           debug0.put(Integer.valueOf(9), new ResourceLocation("textures/entity/cat/jellie.png"));
/*     */           debug0.put(Integer.valueOf(10), new ResourceLocation("textures/entity/cat/all_black.png"));
/*     */         });
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
/*     */   public Cat(EntityType<? extends Cat> debug1, Level debug2) {
/* 127 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public ResourceLocation getResourceLocation() {
/* 131 */     return TEXTURE_BY_TYPE.getOrDefault(Integer.valueOf(getCatType()), TEXTURE_BY_TYPE.get(Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 136 */     this.temptGoal = new CatTemptGoal(this, 0.6D, TEMPT_INGREDIENT, true);
/*     */     
/* 138 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 139 */     this.goalSelector.addGoal(1, (Goal)new SitWhenOrderedToGoal(this));
/* 140 */     this.goalSelector.addGoal(2, new CatRelaxOnOwnerGoal(this));
/* 141 */     this.goalSelector.addGoal(3, (Goal)this.temptGoal);
/* 142 */     this.goalSelector.addGoal(5, (Goal)new CatLieOnBedGoal(this, 1.1D, 8));
/* 143 */     this.goalSelector.addGoal(6, (Goal)new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
/* 144 */     this.goalSelector.addGoal(7, (Goal)new CatSitOnBlockGoal(this, 0.8D));
/* 145 */     this.goalSelector.addGoal(8, (Goal)new LeapAtTargetGoal((Mob)this, 0.3F));
/* 146 */     this.goalSelector.addGoal(9, (Goal)new OcelotAttackGoal((Mob)this));
/* 147 */     this.goalSelector.addGoal(10, (Goal)new BreedGoal((Animal)this, 0.8D));
/* 148 */     this.goalSelector.addGoal(11, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D, 1.0000001E-5F));
/* 149 */     this.goalSelector.addGoal(12, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/*     */     
/* 151 */     this.targetSelector.addGoal(1, (Goal)new NonTameRandomTargetGoal(this, Rabbit.class, false, null));
/* 152 */     this.targetSelector.addGoal(1, (Goal)new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public int getCatType() {
/* 156 */     return ((Integer)this.entityData.get(DATA_TYPE_ID)).intValue();
/*     */   }
/*     */   
/*     */   public void setCatType(int debug1) {
/* 160 */     if (debug1 < 0 || debug1 >= 11) {
/* 161 */       debug1 = this.random.nextInt(10);
/*     */     }
/*     */     
/* 164 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public void setLying(boolean debug1) {
/* 168 */     this.entityData.set(IS_LYING, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isLying() {
/* 172 */     return ((Boolean)this.entityData.get(IS_LYING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setRelaxStateOne(boolean debug1) {
/* 176 */     this.entityData.set(RELAX_STATE_ONE, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isRelaxStateOne() {
/* 180 */     return ((Boolean)this.entityData.get(RELAX_STATE_ONE)).booleanValue();
/*     */   }
/*     */   
/*     */   public DyeColor getCollarColor() {
/* 184 */     return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue());
/*     */   }
/*     */   
/*     */   public void setCollarColor(DyeColor debug1) {
/* 188 */     this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(debug1.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 193 */     super.defineSynchedData();
/*     */     
/* 195 */     this.entityData.define(DATA_TYPE_ID, Integer.valueOf(1));
/* 196 */     this.entityData.define(IS_LYING, Boolean.valueOf(false));
/* 197 */     this.entityData.define(RELAX_STATE_ONE, Boolean.valueOf(false));
/* 198 */     this.entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DyeColor.RED.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 203 */     super.addAdditionalSaveData(debug1);
/* 204 */     debug1.putInt("CatType", getCatType());
/* 205 */     debug1.putByte("CollarColor", (byte)getCollarColor().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 210 */     super.readAdditionalSaveData(debug1);
/* 211 */     setCatType(debug1.getInt("CatType"));
/* 212 */     if (debug1.contains("CollarColor", 99)) {
/* 213 */       setCollarColor(DyeColor.byId(debug1.getInt("CollarColor")));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep() {
/* 219 */     if (getMoveControl().hasWanted()) {
/* 220 */       double debug1 = getMoveControl().getSpeedModifier();
/* 221 */       if (debug1 == 0.6D) {
/* 222 */         setPose(Pose.CROUCHING);
/* 223 */         setSprinting(false);
/* 224 */       } else if (debug1 == 1.33D) {
/* 225 */         setPose(Pose.STANDING);
/* 226 */         setSprinting(true);
/*     */       } else {
/* 228 */         setPose(Pose.STANDING);
/* 229 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 232 */       setPose(Pose.STANDING);
/* 233 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 240 */     if (isTame()) {
/* 241 */       if (isInLove()) {
/* 242 */         return SoundEvents.CAT_PURR;
/*     */       }
/* 244 */       if (this.random.nextInt(4) == 0) {
/* 245 */         return SoundEvents.CAT_PURREOW;
/*     */       }
/* 247 */       return SoundEvents.CAT_AMBIENT;
/*     */     } 
/*     */     
/* 250 */     return SoundEvents.CAT_STRAY_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 255 */     return 120;
/*     */   }
/*     */   
/*     */   public void hiss() {
/* 259 */     playSound(SoundEvents.CAT_HISS, getSoundVolume(), getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 264 */     return SoundEvents.CAT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 269 */     return SoundEvents.CAT_DEATH;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 273 */     return Mob.createMobAttributes()
/* 274 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 275 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 276 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 281 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void usePlayerItem(Player debug1, ItemStack debug2) {
/* 286 */     if (isFood(debug2)) {
/* 287 */       playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
/*     */     }
/* 289 */     super.usePlayerItem(debug1, debug2);
/*     */   }
/*     */   
/*     */   private float getAttackDamage() {
/* 293 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 298 */     return debug1.hurt(DamageSource.mobAttack((LivingEntity)this), getAttackDamage());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 303 */     super.tick();
/*     */     
/* 305 */     if (this.temptGoal != null && this.temptGoal.isRunning() && !isTame() && this.tickCount % 100 == 0) {
/* 306 */       playSound(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
/*     */     }
/* 308 */     handleLieDown();
/*     */   }
/*     */   
/*     */   private void handleLieDown() {
/* 312 */     if ((isLying() || isRelaxStateOne()) && this.tickCount % 5 == 0) {
/* 313 */       playSound(SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
/*     */     }
/* 315 */     updateLieDownAmount();
/* 316 */     updateRelaxStateOneAmount();
/*     */   }
/*     */   
/*     */   private void updateLieDownAmount() {
/* 320 */     this.lieDownAmountO = this.lieDownAmount;
/* 321 */     this.lieDownAmountOTail = this.lieDownAmountTail;
/* 322 */     if (isLying()) {
/* 323 */       this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
/* 324 */       this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
/*     */     } else {
/* 326 */       this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
/* 327 */       this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateRelaxStateOneAmount() {
/* 332 */     this.relaxStateOneAmountO = this.relaxStateOneAmount;
/* 333 */     if (isRelaxStateOne()) {
/* 334 */       this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
/*     */     } else {
/* 336 */       this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
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
/*     */   public Cat getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 354 */     Cat debug3 = (Cat)EntityType.CAT.create((Level)debug1);
/* 355 */     if (debug2 instanceof Cat) {
/* 356 */       if (this.random.nextBoolean()) {
/* 357 */         debug3.setCatType(getCatType());
/*     */       } else {
/* 359 */         debug3.setCatType(((Cat)debug2).getCatType());
/*     */       } 
/*     */       
/* 362 */       if (isTame()) {
/* 363 */         debug3.setOwnerUUID(getOwnerUUID());
/* 364 */         debug3.setTame(true);
/* 365 */         if (this.random.nextBoolean()) {
/* 366 */           debug3.setCollarColor(getCollarColor());
/*     */         } else {
/* 368 */           debug3.setCollarColor(((Cat)debug2).getCollarColor());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 373 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 378 */     if (!isTame()) {
/* 379 */       return false;
/*     */     }
/*     */     
/* 382 */     if (!(debug1 instanceof Cat)) {
/* 383 */       return false;
/*     */     }
/*     */     
/* 386 */     Cat debug2 = (Cat)debug1;
/* 387 */     return (debug2.isTame() && super.canMate(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 393 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 395 */     if (debug1.getMoonBrightness() > 0.9F) {
/*     */       
/* 397 */       setCatType(this.random.nextInt(11));
/*     */     } else {
/* 399 */       setCatType(this.random.nextInt(10));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 405 */     ServerLevel serverLevel = debug1.getLevel();
/* 406 */     if (serverLevel instanceof ServerLevel && 
/* 407 */       serverLevel.structureFeatureManager().getStructureAt(blockPosition(), true, (StructureFeature)StructureFeature.SWAMP_HUT).isValid()) {
/* 408 */       setCatType(10);
/* 409 */       setPersistenceRequired();
/*     */     } 
/*     */ 
/*     */     
/* 413 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 418 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 419 */     Item debug4 = debug3.getItem();
/*     */     
/* 421 */     if (this.level.isClientSide) {
/* 422 */       if (isTame() && isOwnedBy((LivingEntity)debug1)) {
/* 423 */         return InteractionResult.SUCCESS;
/*     */       }
/* 425 */       if (isFood(debug3) && (getHealth() < getMaxHealth() || !isTame())) {
/* 426 */         return InteractionResult.SUCCESS;
/*     */       }
/* 428 */       return InteractionResult.PASS;
/*     */     } 
/*     */     
/* 431 */     if (isTame()) {
/* 432 */       if (isOwnedBy((LivingEntity)debug1)) {
/* 433 */         if (debug4 instanceof DyeItem)
/* 434 */         { DyeColor dyeColor = ((DyeItem)debug4).getDyeColor();
/* 435 */           if (dyeColor != getCollarColor()) {
/* 436 */             setCollarColor(dyeColor);
/*     */             
/* 438 */             if (!debug1.abilities.instabuild) {
/* 439 */               debug3.shrink(1);
/*     */             }
/*     */             
/* 442 */             setPersistenceRequired();
/* 443 */             return InteractionResult.CONSUME;
/*     */           }  }
/* 445 */         else { if (debug4.isEdible() && isFood(debug3) && getHealth() < getMaxHealth()) {
/* 446 */             usePlayerItem(debug1, debug3);
/* 447 */             heal(debug4.getFoodProperties().getNutrition());
/* 448 */             return InteractionResult.CONSUME;
/*     */           } 
/*     */           
/* 451 */           InteractionResult interactionResult = super.mobInteract(debug1, debug2);
/* 452 */           if (!interactionResult.consumesAction() || isBaby()) {
/* 453 */             setOrderedToSit(!isOrderedToSit());
/*     */           }
/* 455 */           return interactionResult; }
/*     */ 
/*     */       
/*     */       }
/* 459 */     } else if (isFood(debug3)) {
/* 460 */       usePlayerItem(debug1, debug3);
/*     */       
/* 462 */       if (this.random.nextInt(3) == 0) {
/* 463 */         tame(debug1);
/* 464 */         setOrderedToSit(true);
/* 465 */         this.level.broadcastEntityEvent((Entity)this, (byte)7);
/*     */       } else {
/* 467 */         this.level.broadcastEntityEvent((Entity)this, (byte)6);
/*     */       } 
/*     */       
/* 470 */       setPersistenceRequired();
/* 471 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */ 
/*     */     
/* 475 */     InteractionResult debug5 = super.mobInteract(debug1, debug2);
/*     */     
/* 477 */     if (debug5.consumesAction()) {
/* 478 */       setPersistenceRequired();
/*     */     }
/*     */     
/* 481 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 486 */     return TEMPT_INGREDIENT.test(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 491 */     return debug2.height * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 496 */     return (!isTame() && this.tickCount > 2400);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reassessTameGoals() {
/* 501 */     if (this.avoidPlayersGoal == null) {
/* 502 */       this.avoidPlayersGoal = new CatAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 505 */     this.goalSelector.removeGoal((Goal)this.avoidPlayersGoal);
/*     */     
/* 507 */     if (!isTame())
/* 508 */       this.goalSelector.addGoal(4, (Goal)this.avoidPlayersGoal); 
/*     */   }
/*     */   
/*     */   static class CatAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T> {
/*     */     private final Cat cat;
/*     */     
/*     */     public CatAvoidEntityGoal(Cat debug1, Class<T> debug2, float debug3, double debug4, double debug6) {
/* 516 */       super((PathfinderMob)debug1, debug2, debug3, debug4, debug6, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
/* 517 */       this.cat = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 522 */       return (!this.cat.isTame() && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 527 */       return (!this.cat.isTame() && super.canContinueToUse());
/*     */     }
/*     */   }
/*     */   
/*     */   static class CatTemptGoal extends TemptGoal {
/*     */     @Nullable
/*     */     private Player selectedPlayer;
/*     */     private final Cat cat;
/*     */     
/*     */     public CatTemptGoal(Cat debug1, double debug2, Ingredient debug4, boolean debug5) {
/* 537 */       super((PathfinderMob)debug1, debug2, debug4, debug5);
/* 538 */       this.cat = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 543 */       super.tick();
/*     */       
/* 545 */       if (this.selectedPlayer == null && this.mob.getRandom().nextInt(600) == 0) {
/* 546 */         this.selectedPlayer = this.player;
/* 547 */       } else if (this.mob.getRandom().nextInt(500) == 0) {
/* 548 */         this.selectedPlayer = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean canScare() {
/* 554 */       if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player)) {
/* 555 */         return false;
/*     */       }
/*     */       
/* 558 */       return super.canScare();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 563 */       return (super.canUse() && !this.cat.isTame());
/*     */     }
/*     */   }
/*     */   
/*     */   static class CatRelaxOnOwnerGoal extends Goal {
/*     */     private final Cat cat;
/*     */     private Player ownerPlayer;
/*     */     private BlockPos goalPos;
/*     */     private int onBedTicks;
/*     */     
/*     */     public CatRelaxOnOwnerGoal(Cat debug1) {
/* 574 */       this.cat = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 579 */       if (!this.cat.isTame()) {
/* 580 */         return false;
/*     */       }
/*     */       
/* 583 */       if (this.cat.isOrderedToSit()) {
/* 584 */         return false;
/*     */       }
/*     */       
/* 587 */       LivingEntity debug1 = this.cat.getOwner();
/* 588 */       if (debug1 instanceof Player) {
/* 589 */         this.ownerPlayer = (Player)debug1;
/*     */         
/* 591 */         if (!debug1.isSleeping()) {
/* 592 */           return false;
/*     */         }
/*     */         
/* 595 */         if (this.cat.distanceToSqr((Entity)this.ownerPlayer) > 100.0D) {
/* 596 */           return false;
/*     */         }
/*     */         
/* 599 */         BlockPos debug2 = this.ownerPlayer.blockPosition();
/* 600 */         BlockState debug3 = this.cat.level.getBlockState(debug2);
/* 601 */         if (debug3.getBlock().is((Tag)BlockTags.BEDS)) {
/* 602 */           this.goalPos = debug3.getOptionalValue((Property)BedBlock.FACING).map(debug1 -> debug0.relative(debug1.getOpposite())).orElseGet(() -> new BlockPos((Vec3i)debug0));
/* 603 */           return !spaceIsOccupied();
/*     */         } 
/*     */       } 
/*     */       
/* 607 */       return false;
/*     */     }
/*     */     
/*     */     private boolean spaceIsOccupied() {
/* 611 */       List<Cat> debug1 = this.cat.level.getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 612 */       for (Cat debug3 : debug1) {
/* 613 */         if (debug3 != this.cat && (debug3.isLying() || debug3.isRelaxStateOne())) {
/* 614 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 618 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 623 */       return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 628 */       if (this.goalPos != null) {
/* 629 */         this.cat.setInSittingPose(false);
/* 630 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 636 */       this.cat.setLying(false);
/*     */       
/* 638 */       float debug1 = this.cat.level.getTimeOfDay(1.0F);
/* 639 */       if (this.ownerPlayer.getSleepTimer() >= 100 && debug1 > 0.77D && debug1 < 0.8D && this.cat.level.getRandom().nextFloat() < 0.7D) {
/* 640 */         giveMorningGift();
/*     */       }
/*     */       
/* 643 */       this.onBedTicks = 0;
/* 644 */       this.cat.setRelaxStateOne(false);
/* 645 */       this.cat.getNavigation().stop();
/*     */     }
/*     */     
/*     */     private void giveMorningGift() {
/* 649 */       Random debug1 = this.cat.getRandom();
/* 650 */       BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/* 651 */       debug2.set((Vec3i)this.cat.blockPosition());
/*     */       
/* 653 */       this.cat.randomTeleport((debug2.getX() + debug1.nextInt(11) - 5), (debug2.getY() + debug1.nextInt(5) - 2), (debug2.getZ() + debug1.nextInt(11) - 5), false);
/*     */       
/* 655 */       debug2.set((Vec3i)this.cat.blockPosition());
/* 656 */       LootTable debug3 = this.cat.level.getServer().getLootTables().get(BuiltInLootTables.CAT_MORNING_GIFT);
/*     */ 
/*     */ 
/*     */       
/* 660 */       LootContext.Builder debug4 = (new LootContext.Builder((ServerLevel)this.cat.level)).withParameter(LootContextParams.ORIGIN, this.cat.position()).withParameter(LootContextParams.THIS_ENTITY, this.cat).withRandom(debug1);
/* 661 */       List<ItemStack> debug5 = debug3.getRandomItems(debug4.create(LootContextParamSets.GIFT));
/* 662 */       for (ItemStack debug7 : debug5) {
/* 663 */         this.cat.level.addFreshEntity((Entity)new ItemEntity(this.cat.level, debug2.getX() - Mth.sin(this.cat.yBodyRot * 0.017453292F), debug2.getY(), debug2.getZ() + Mth.cos(this.cat.yBodyRot * 0.017453292F), debug7));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 669 */       if (this.ownerPlayer != null && this.goalPos != null) {
/* 670 */         this.cat.setInSittingPose(false);
/* 671 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 672 */         if (this.cat.distanceToSqr((Entity)this.ownerPlayer) < 2.5D) {
/* 673 */           this.onBedTicks++;
/* 674 */           if (this.onBedTicks > 16) {
/* 675 */             this.cat.setLying(true);
/* 676 */             this.cat.setRelaxStateOne(false);
/*     */           } else {
/* 678 */             this.cat.lookAt((Entity)this.ownerPlayer, 45.0F, 45.0F);
/* 679 */             this.cat.setRelaxStateOne(true);
/*     */           } 
/*     */         } else {
/* 682 */           this.cat.setLying(false);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Cat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */