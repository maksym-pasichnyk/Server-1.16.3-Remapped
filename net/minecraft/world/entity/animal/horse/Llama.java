/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.Wolf;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.LlamaSpit;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.WoolCarpetBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Llama
/*     */   extends AbstractChestedHorse implements RangedAttackMob {
/*  63 */   private static final Ingredient FOOD_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.WHEAT, (ItemLike)Blocks.HAY_BLOCK.asItem() });
/*     */   
/*  65 */   private static final EntityDataAccessor<Integer> DATA_STRENGTH_ID = SynchedEntityData.defineId(Llama.class, EntityDataSerializers.INT);
/*  66 */   private static final EntityDataAccessor<Integer> DATA_SWAG_ID = SynchedEntityData.defineId(Llama.class, EntityDataSerializers.INT);
/*  67 */   private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Llama.class, EntityDataSerializers.INT);
/*     */   
/*     */   private boolean didSpit;
/*     */   
/*     */   @Nullable
/*     */   private Llama caravanHead;
/*     */   @Nullable
/*     */   private Llama caravanTail;
/*     */   
/*     */   public Llama(EntityType<? extends Llama> debug1, Level debug2) {
/*  77 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setStrength(int debug1) {
/*  85 */     this.entityData.set(DATA_STRENGTH_ID, Integer.valueOf(Math.max(1, Math.min(5, debug1))));
/*     */   }
/*     */   
/*     */   private void setRandomStrength() {
/*  89 */     int debug1 = (this.random.nextFloat() < 0.04F) ? 5 : 3;
/*     */     
/*  91 */     setStrength(1 + this.random.nextInt(debug1));
/*     */   }
/*     */   
/*     */   public int getStrength() {
/*  95 */     return ((Integer)this.entityData.get(DATA_STRENGTH_ID)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 100 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 102 */     debug1.putInt("Variant", getVariant());
/* 103 */     debug1.putInt("Strength", getStrength());
/*     */     
/* 105 */     if (!this.inventory.getItem(1).isEmpty()) {
/* 106 */       debug1.put("DecorItem", (Tag)this.inventory.getItem(1).save(new CompoundTag()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 112 */     setStrength(debug1.getInt("Strength"));
/*     */     
/* 114 */     super.readAdditionalSaveData(debug1);
/* 115 */     setVariant(debug1.getInt("Variant"));
/*     */     
/* 117 */     if (debug1.contains("DecorItem", 10)) {
/* 118 */       this.inventory.setItem(1, ItemStack.of(debug1.getCompound("DecorItem")));
/*     */     }
/*     */     
/* 121 */     updateContainerEquipment();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 126 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 127 */     this.goalSelector.addGoal(1, (Goal)new RunAroundLikeCrazyGoal(this, 1.2D));
/* 128 */     this.goalSelector.addGoal(2, (Goal)new LlamaFollowCaravanGoal(this, 2.0999999046325684D));
/* 129 */     this.goalSelector.addGoal(3, (Goal)new RangedAttackGoal(this, 1.25D, 40, 20.0F));
/* 130 */     this.goalSelector.addGoal(3, (Goal)new PanicGoal((PathfinderMob)this, 1.2D));
/* 131 */     this.goalSelector.addGoal(4, (Goal)new BreedGoal(this, 1.0D));
/* 132 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.0D));
/* 133 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.7D));
/* 134 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/* 135 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 137 */     this.targetSelector.addGoal(1, (Goal)new LlamaHurtByTargetGoal(this));
/* 138 */     this.targetSelector.addGoal(2, (Goal)new LlamaAttackWolfGoal(this));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 142 */     return createBaseChestedHorseAttributes()
/* 143 */       .add(Attributes.FOLLOW_RANGE, 40.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 148 */     super.defineSynchedData();
/*     */     
/* 150 */     this.entityData.define(DATA_STRENGTH_ID, Integer.valueOf(0));
/* 151 */     this.entityData.define(DATA_SWAG_ID, Integer.valueOf(-1));
/* 152 */     this.entityData.define(DATA_VARIANT_ID, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public int getVariant() {
/* 156 */     return Mth.clamp(((Integer)this.entityData.get(DATA_VARIANT_ID)).intValue(), 0, 3);
/*     */   }
/*     */   
/*     */   public void setVariant(int debug1) {
/* 160 */     this.entityData.set(DATA_VARIANT_ID, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInventorySize() {
/* 165 */     if (hasChest()) {
/* 166 */       return 2 + 3 * getInventoryColumns();
/*     */     }
/* 168 */     return super.getInventorySize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void positionRider(Entity debug1) {
/* 173 */     if (!hasPassenger(debug1)) {
/*     */       return;
/*     */     }
/* 176 */     float debug2 = Mth.cos(this.yBodyRot * 0.017453292F);
/* 177 */     float debug3 = Mth.sin(this.yBodyRot * 0.017453292F);
/* 178 */     float debug4 = 0.3F;
/* 179 */     debug1.setPos(getX() + (0.3F * debug3), getY() + getPassengersRidingOffset() + debug1.getMyRidingOffset(), getZ() - (0.3F * debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 184 */     return getBbHeight() * 0.67D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeControlledByRider() {
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 196 */     return FOOD_ITEMS.test(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleEating(Player debug1, ItemStack debug2) {
/* 201 */     int debug3 = 0;
/* 202 */     int debug4 = 0;
/* 203 */     float debug5 = 0.0F;
/* 204 */     boolean debug6 = false;
/*     */     
/* 206 */     Item debug7 = debug2.getItem();
/* 207 */     if (debug7 == Items.WHEAT) {
/* 208 */       debug3 = 10;
/* 209 */       debug4 = 3;
/* 210 */       debug5 = 2.0F;
/* 211 */     } else if (debug7 == Blocks.HAY_BLOCK.asItem()) {
/* 212 */       debug3 = 90;
/* 213 */       debug4 = 6;
/* 214 */       debug5 = 10.0F;
/* 215 */       if (isTamed() && getAge() == 0 && canFallInLove()) {
/* 216 */         debug6 = true;
/* 217 */         setInLove(debug1);
/*     */       } 
/*     */     } 
/* 220 */     if (getHealth() < getMaxHealth() && debug5 > 0.0F) {
/* 221 */       heal(debug5);
/* 222 */       debug6 = true;
/*     */     } 
/* 224 */     if (isBaby() && debug3 > 0) {
/* 225 */       this.level.addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/* 226 */       if (!this.level.isClientSide) {
/* 227 */         ageUp(debug3);
/*     */       }
/* 229 */       debug6 = true;
/*     */     } 
/* 231 */     if (debug4 > 0 && (debug6 || !isTamed()) && getTemper() < getMaxTemper()) {
/* 232 */       debug6 = true;
/* 233 */       if (!this.level.isClientSide) {
/* 234 */         modifyTemper(debug4);
/*     */       }
/*     */     } 
/* 237 */     if (debug6 && !isSilent()) {
/* 238 */       SoundEvent debug8 = getEatingSound();
/* 239 */       if (debug8 != null) {
/* 240 */         this.level.playSound(null, getX(), getY(), getZ(), getEatingSound(), getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isImmobile() {
/* 249 */     return (isDeadOrDying() || isEating());
/*     */   }
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     LlamaGroupData llamaGroupData;
/*     */     int debug6;
/* 255 */     setRandomStrength();
/*     */ 
/*     */     
/* 258 */     if (debug4 instanceof LlamaGroupData) {
/* 259 */       debug6 = ((LlamaGroupData)debug4).variant;
/*     */     } else {
/* 261 */       debug6 = this.random.nextInt(4);
/* 262 */       llamaGroupData = new LlamaGroupData(debug6);
/*     */     } 
/* 264 */     setVariant(debug6);
/*     */     
/* 266 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)llamaGroupData, debug5);
/*     */   }
/*     */   
/*     */   static class LlamaGroupData extends AgableMob.AgableMobGroupData {
/*     */     public final int variant;
/*     */     
/*     */     private LlamaGroupData(int debug1) {
/* 273 */       super(true);
/* 274 */       this.variant = debug1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAngrySound() {
/* 280 */     return SoundEvents.LLAMA_ANGRY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 285 */     return SoundEvents.LLAMA_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 290 */     return SoundEvents.LLAMA_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 295 */     return SoundEvents.LLAMA_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getEatingSound() {
/* 301 */     return SoundEvents.LLAMA_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 306 */     playSound(SoundEvents.LLAMA_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playChestEquipsSound() {
/* 311 */     playSound(SoundEvents.LLAMA_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void makeMad() {
/* 316 */     SoundEvent debug1 = getAngrySound();
/* 317 */     if (debug1 != null) {
/* 318 */       playSound(debug1, getSoundVolume(), getVoicePitch());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryColumns() {
/* 324 */     return getStrength();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWearArmor() {
/* 329 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWearingArmor() {
/* 334 */     return !this.inventory.getItem(1).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArmor(ItemStack debug1) {
/* 339 */     Item debug2 = debug1.getItem();
/* 340 */     return ItemTags.CARPETS.contains(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaddleable() {
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void containerChanged(Container debug1) {
/* 350 */     DyeColor debug2 = getSwag();
/* 351 */     super.containerChanged(debug1);
/*     */     
/* 353 */     DyeColor debug3 = getSwag();
/* 354 */     if (this.tickCount > 20 && debug3 != null && debug3 != debug2) {
/* 355 */       playSound(SoundEvents.LLAMA_SWAG, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateContainerEquipment() {
/* 361 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 365 */     super.updateContainerEquipment();
/*     */     
/* 367 */     setSwag(getDyeColor(this.inventory.getItem(1)));
/*     */   }
/*     */   
/*     */   private void setSwag(@Nullable DyeColor debug1) {
/* 371 */     this.entityData.set(DATA_SWAG_ID, Integer.valueOf((debug1 == null) ? -1 : debug1.getId()));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static DyeColor getDyeColor(ItemStack debug0) {
/* 376 */     Block debug1 = Block.byItem(debug0.getItem());
/* 377 */     if (debug1 instanceof WoolCarpetBlock) {
/* 378 */       return ((WoolCarpetBlock)debug1).getColor();
/*     */     }
/* 380 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public DyeColor getSwag() {
/* 385 */     int debug1 = ((Integer)this.entityData.get(DATA_SWAG_ID)).intValue();
/* 386 */     return (debug1 == -1) ? null : DyeColor.byId(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTemper() {
/* 391 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 396 */     return (debug1 != this && debug1 instanceof Llama && canParent() && ((Llama)debug1).canParent());
/*     */   }
/*     */ 
/*     */   
/*     */   public Llama getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 401 */     Llama debug3 = makeBabyLlama();
/*     */     
/* 403 */     setOffspringAttributes(debug2, debug3);
/*     */     
/* 405 */     Llama debug4 = (Llama)debug2;
/*     */     
/* 407 */     int debug5 = this.random.nextInt(Math.max(getStrength(), debug4.getStrength())) + 1;
/* 408 */     if (this.random.nextFloat() < 0.03F) {
/* 409 */       debug5++;
/*     */     }
/* 411 */     debug3.setStrength(debug5);
/*     */     
/* 413 */     debug3.setVariant(this.random.nextBoolean() ? getVariant() : debug4.getVariant());
/*     */     
/* 415 */     return debug3;
/*     */   }
/*     */   
/*     */   protected Llama makeBabyLlama() {
/* 419 */     return (Llama)EntityType.LLAMA.create(this.level);
/*     */   }
/*     */   
/*     */   private void spit(LivingEntity debug1) {
/* 423 */     LlamaSpit debug2 = new LlamaSpit(this.level, this);
/* 424 */     double debug3 = debug1.getX() - getX();
/* 425 */     double debug5 = debug1.getY(0.3333333333333333D) - debug2.getY();
/* 426 */     double debug7 = debug1.getZ() - getZ();
/* 427 */     float debug9 = Mth.sqrt(debug3 * debug3 + debug7 * debug7) * 0.2F;
/* 428 */     debug2.shoot(debug3, debug5 + debug9, debug7, 1.5F, 10.0F);
/* 429 */     if (!isSilent()) {
/* 430 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.LLAMA_SPIT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */     }
/*     */     
/* 433 */     this.level.addFreshEntity((Entity)debug2);
/* 434 */     this.didSpit = true;
/*     */   }
/*     */   
/*     */   private void setDidSpit(boolean debug1) {
/* 438 */     this.didSpit = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 443 */     int debug3 = calculateFallDamage(debug1, debug2);
/* 444 */     if (debug3 <= 0) {
/* 445 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 449 */     if (debug1 >= 6.0F) {
/* 450 */       hurt(DamageSource.FALL, debug3);
/*     */       
/* 452 */       if (isVehicle()) {
/* 453 */         for (Entity debug5 : getIndirectPassengers()) {
/* 454 */           debug5.hurt(DamageSource.FALL, debug3);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 459 */     playBlockFallSound();
/* 460 */     return true;
/*     */   }
/*     */   
/*     */   public void leaveCaravan() {
/* 464 */     if (this.caravanHead != null) {
/* 465 */       this.caravanHead.caravanTail = null;
/*     */     }
/* 467 */     this.caravanHead = null;
/*     */   }
/*     */   
/*     */   public void joinCaravan(Llama debug1) {
/* 471 */     this.caravanHead = debug1;
/* 472 */     this.caravanHead.caravanTail = this;
/*     */   }
/*     */   
/*     */   public boolean hasCaravanTail() {
/* 476 */     return (this.caravanTail != null);
/*     */   }
/*     */   
/*     */   public boolean inCaravan() {
/* 480 */     return (this.caravanHead != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Llama getCaravanHead() {
/* 485 */     return this.caravanHead;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double followLeashSpeed() {
/* 490 */     return 2.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void followMommy() {
/* 495 */     if (!inCaravan() && isBaby()) {
/* 496 */       super.followMommy();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEatGrass() {
/* 502 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 507 */     spit(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class LlamaHurtByTargetGoal
/*     */     extends HurtByTargetGoal
/*     */   {
/*     */     public LlamaHurtByTargetGoal(Llama debug1) {
/* 517 */       super((PathfinderMob)debug1, new Class[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 522 */       if (this.mob instanceof Llama) {
/* 523 */         Llama debug1 = (Llama)this.mob;
/* 524 */         if (debug1.didSpit) {
/* 525 */           debug1.setDidSpit(false);
/* 526 */           return false;
/*     */         } 
/*     */       } 
/* 529 */       return super.canContinueToUse();
/*     */     }
/*     */   }
/*     */   
/*     */   static class LlamaAttackWolfGoal extends NearestAttackableTargetGoal<Wolf> {
/*     */     public LlamaAttackWolfGoal(Llama debug1) {
/* 535 */       super((Mob)debug1, Wolf.class, 16, false, true, debug0 -> !((Wolf)debug0).isTame());
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getFollowDistance() {
/* 540 */       return super.getFollowDistance() * 0.25D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\Llama.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */