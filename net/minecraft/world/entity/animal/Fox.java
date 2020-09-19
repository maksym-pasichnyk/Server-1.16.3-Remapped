/*      */ package net.minecraft.world.entity.animal;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.NbtUtils;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.ExperienceOrb;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.JumpGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.Biomes;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Fox
/*      */   extends Animal
/*      */ {
/*   92 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.INT);
/*   93 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.BYTE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  103 */   private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_UUID);
/*  104 */   private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(Fox.class, EntityDataSerializers.OPTIONAL_UUID);
/*      */   static {
/*  106 */     ALLOWED_ITEMS = (debug0 -> (!debug0.hasPickUpDelay() && debug0.isAlive()));
/*      */     
/*  108 */     TRUSTED_TARGET_SELECTOR = (debug0 -> {
/*      */         if (debug0 instanceof LivingEntity) {
/*      */           LivingEntity debug1 = (LivingEntity)debug0;
/*  111 */           return (debug1.getLastHurtMob() != null && debug1.getLastHurtMobTimestamp() < debug1.tickCount + 600);
/*      */         } 
/*      */         
/*      */         return false;
/*      */       });
/*  116 */     STALKABLE_PREY = (debug0 -> (debug0 instanceof Chicken || debug0 instanceof Rabbit));
/*      */     
/*  118 */     AVOID_PLAYERS = (debug0 -> (!debug0.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(debug0)));
/*      */   }
/*      */   
/*      */   private static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*      */   private static final Predicate<Entity> TRUSTED_TARGET_SELECTOR;
/*      */   private static final Predicate<Entity> STALKABLE_PREY;
/*      */   private static final Predicate<Entity> AVOID_PLAYERS;
/*      */   private Goal landTargetGoal;
/*      */   private Goal turtleEggTargetGoal;
/*      */   private Goal fishTargetGoal;
/*      */   private float interestedAngle;
/*      */   private float interestedAngleO;
/*      */   private float crouchAmount;
/*      */   private float crouchAmountO;
/*      */   private int ticksSinceEaten;
/*      */   
/*      */   public enum Type { private static final Type[] BY_ID;
/*  135 */     RED(0, "red", new ResourceKey[] { Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.TAIGA_MOUNTAINS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.GIANT_SPRUCE_TAIGA_HILLS }),
/*  136 */     SNOW(1, "snow", new ResourceKey[] { Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS }); private static final Map<String, Type> BY_NAME;
/*      */     static {
/*  138 */       BY_ID = (Type[])Arrays.<Type>stream(values()).sorted(Comparator.comparingInt(Type::getId)).toArray(debug0 -> new Type[debug0]);
/*  139 */       BY_NAME = (Map<String, Type>)Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::getName, debug0 -> debug0));
/*      */     }
/*      */     private final int id;
/*      */     private final String name;
/*      */     private final List<ResourceKey<Biome>> biomes;
/*      */     
/*      */     Type(int debug3, String debug4, ResourceKey<Biome>... debug5) {
/*  146 */       this.id = debug3;
/*  147 */       this.name = debug4;
/*  148 */       this.biomes = Arrays.asList(debug5);
/*      */     }
/*      */     
/*      */     public String getName() {
/*  152 */       return this.name;
/*      */     }
/*      */     
/*      */     public int getId() {
/*  156 */       return this.id;
/*      */     }
/*      */     
/*      */     public static Type byName(String debug0) {
/*  160 */       return BY_NAME.getOrDefault(debug0, RED);
/*      */     }
/*      */     
/*      */     public static Type byId(int debug0) {
/*  164 */       if (debug0 < 0 || debug0 > BY_ID.length) {
/*  165 */         debug0 = 0;
/*      */       }
/*  167 */       return BY_ID[debug0];
/*      */     }
/*      */     
/*      */     public static Type byBiome(Optional<ResourceKey<Biome>> debug0) {
/*  171 */       return (debug0.isPresent() && SNOW.biomes.contains(debug0.get())) ? SNOW : RED;
/*      */     } }
/*      */ 
/*      */   
/*      */   public Fox(EntityType<? extends Fox> debug1, Level debug2) {
/*  176 */     super((EntityType)debug1, debug2);
/*      */     
/*  178 */     this.lookControl = new FoxLookControl();
/*  179 */     this.moveControl = new FoxMoveControl();
/*      */     
/*  181 */     setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
/*  182 */     setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);
/*      */     
/*  184 */     setCanPickUpLoot(true);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  189 */     super.defineSynchedData();
/*  190 */     this.entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
/*  191 */     this.entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
/*  192 */     this.entityData.define(DATA_TYPE_ID, Integer.valueOf(0));
/*  193 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  198 */     this.landTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, Animal.class, 10, false, false, debug0 -> (debug0 instanceof Chicken || debug0 instanceof Rabbit));
/*  199 */     this.turtleEggTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR);
/*  200 */     this.fishTargetGoal = (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractFish.class, 20, false, false, debug0 -> debug0 instanceof AbstractSchoolingFish);
/*      */     
/*  202 */     this.goalSelector.addGoal(0, (Goal)new FoxFloatGoal());
/*  203 */     this.goalSelector.addGoal(1, new FaceplantGoal());
/*  204 */     this.goalSelector.addGoal(2, (Goal)new FoxPanicGoal(2.2D));
/*  205 */     this.goalSelector.addGoal(3, (Goal)new FoxBreedGoal(1.0D));
/*  206 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 16.0F, 1.6D, 1.4D, debug1 -> (AVOID_PLAYERS.test(debug1) && !trusts(debug1.getUUID()) && !isDefending())));
/*  207 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, Wolf.class, 8.0F, 1.6D, 1.4D, debug1 -> (!((Wolf)debug1).isTame() && !isDefending())));
/*  208 */     this.goalSelector.addGoal(4, (Goal)new AvoidEntityGoal((PathfinderMob)this, PolarBear.class, 8.0F, 1.6D, 1.4D, debug1 -> !isDefending()));
/*  209 */     this.goalSelector.addGoal(5, new StalkPreyGoal());
/*  210 */     this.goalSelector.addGoal(6, (Goal)new FoxPounceGoal());
/*  211 */     this.goalSelector.addGoal(6, (Goal)new SeekShelterGoal(1.25D));
/*  212 */     this.goalSelector.addGoal(7, (Goal)new FoxMeleeAttackGoal(1.2000000476837158D, true));
/*  213 */     this.goalSelector.addGoal(7, new SleepGoal());
/*  214 */     this.goalSelector.addGoal(8, (Goal)new FoxFollowParentGoal(this, 1.25D));
/*  215 */     this.goalSelector.addGoal(9, (Goal)new FoxStrollThroughVillageGoal(32, 200));
/*  216 */     this.goalSelector.addGoal(10, (Goal)new FoxEatBerriesGoal(1.2000000476837158D, 12, 2));
/*  217 */     this.goalSelector.addGoal(10, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  218 */     this.goalSelector.addGoal(11, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  219 */     this.goalSelector.addGoal(11, new FoxSearchForItemsGoal());
/*  220 */     this.goalSelector.addGoal(12, (Goal)new FoxLookAtPlayerGoal((Mob)this, (Class)Player.class, 24.0F));
/*  221 */     this.goalSelector.addGoal(13, new PerchAndSearchGoal());
/*      */     
/*  223 */     this.targetSelector.addGoal(3, (Goal)new DefendTrustedTargetGoal(LivingEntity.class, false, false, debug1 -> (TRUSTED_TARGET_SELECTOR.test(debug1) && !trusts(debug1.getUUID()))));
/*      */   }
/*      */ 
/*      */   
/*      */   public SoundEvent getEatingSound(ItemStack debug1) {
/*  228 */     return SoundEvents.FOX_EAT;
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  233 */     if (!this.level.isClientSide && isAlive() && isEffectiveAi()) {
/*      */       
/*  235 */       this.ticksSinceEaten++;
/*  236 */       ItemStack debug1 = getItemBySlot(EquipmentSlot.MAINHAND);
/*  237 */       if (canEat(debug1)) {
/*  238 */         if (this.ticksSinceEaten > 600) {
/*  239 */           ItemStack itemStack = debug1.finishUsingItem(this.level, (LivingEntity)this);
/*  240 */           if (!itemStack.isEmpty()) {
/*  241 */             setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/*      */           }
/*  243 */           this.ticksSinceEaten = 0;
/*  244 */         } else if (this.ticksSinceEaten > 560 && 
/*  245 */           this.random.nextFloat() < 0.1F) {
/*  246 */           playSound(getEatingSound(debug1), 1.0F, 1.0F);
/*  247 */           this.level.broadcastEntityEvent((Entity)this, (byte)45);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  253 */       LivingEntity debug2 = getTarget();
/*  254 */       if (debug2 == null || !debug2.isAlive()) {
/*  255 */         setIsCrouching(false);
/*  256 */         setIsInterested(false);
/*      */       } 
/*      */     } 
/*      */     
/*  260 */     if (isSleeping() || isImmobile()) {
/*  261 */       this.jumping = false;
/*  262 */       this.xxa = 0.0F;
/*  263 */       this.zza = 0.0F;
/*      */     } 
/*      */     
/*  266 */     super.aiStep();
/*      */     
/*  268 */     if (isDefending() && this.random.nextFloat() < 0.05F) {
/*  269 */       playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isImmobile() {
/*  275 */     return isDeadOrDying();
/*      */   }
/*      */   
/*      */   private boolean canEat(ItemStack debug1) {
/*  279 */     return (debug1.getItem().isEdible() && getTarget() == null && this.onGround && !isSleeping());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/*  284 */     if (this.random.nextFloat() < 0.2F) {
/*  285 */       ItemStack debug3; float debug2 = this.random.nextFloat();
/*      */       
/*  287 */       if (debug2 < 0.05F) {
/*  288 */         debug3 = new ItemStack((ItemLike)Items.EMERALD);
/*  289 */       } else if (debug2 < 0.2F) {
/*  290 */         debug3 = new ItemStack((ItemLike)Items.EGG);
/*  291 */       } else if (debug2 < 0.4F) {
/*  292 */         debug3 = this.random.nextBoolean() ? new ItemStack((ItemLike)Items.RABBIT_FOOT) : new ItemStack((ItemLike)Items.RABBIT_HIDE);
/*  293 */       } else if (debug2 < 0.6F) {
/*  294 */         debug3 = new ItemStack((ItemLike)Items.WHEAT);
/*  295 */       } else if (debug2 < 0.8F) {
/*  296 */         debug3 = new ItemStack((ItemLike)Items.LEATHER);
/*      */       } else {
/*  298 */         debug3 = new ItemStack((ItemLike)Items.FEATHER);
/*      */       } 
/*  300 */       setItemSlot(EquipmentSlot.MAINHAND, debug3);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  323 */     return Mob.createMobAttributes()
/*  324 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  325 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  326 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/*  327 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public Fox getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  332 */     Fox debug3 = (Fox)EntityType.FOX.create((Level)debug1);
/*  333 */     debug3.setFoxType(this.random.nextBoolean() ? getFoxType() : ((Fox)debug2).getFoxType());
/*  334 */     return debug3;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*      */     FoxGroupData foxGroupData;
/*  340 */     Optional<ResourceKey<Biome>> debug6 = debug1.getBiomeName(blockPosition());
/*  341 */     Type debug7 = Type.byBiome(debug6);
/*  342 */     boolean debug8 = false;
/*  343 */     if (debug4 instanceof FoxGroupData) {
/*      */       
/*  345 */       debug7 = ((FoxGroupData)debug4).type;
/*  346 */       if (((FoxGroupData)debug4).getGroupSize() >= 2) {
/*  347 */         debug8 = true;
/*      */       }
/*      */     } else {
/*  350 */       foxGroupData = new FoxGroupData(debug7);
/*      */     } 
/*      */     
/*  353 */     setFoxType(debug7);
/*  354 */     if (debug8) {
/*  355 */       setAge(-24000);
/*      */     }
/*      */     
/*  358 */     if (debug1 instanceof ServerLevel) {
/*  359 */       setTargetGoals();
/*      */     }
/*      */     
/*  362 */     populateDefaultEquipmentSlots(debug2);
/*      */     
/*  364 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)foxGroupData, debug5);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setTargetGoals() {
/*  369 */     if (getFoxType() == Type.RED) {
/*  370 */       this.targetSelector.addGoal(4, this.landTargetGoal);
/*  371 */       this.targetSelector.addGoal(4, this.turtleEggTargetGoal);
/*  372 */       this.targetSelector.addGoal(6, this.fishTargetGoal);
/*      */     } else {
/*  374 */       this.targetSelector.addGoal(4, this.fishTargetGoal);
/*  375 */       this.targetSelector.addGoal(6, this.landTargetGoal);
/*  376 */       this.targetSelector.addGoal(6, this.turtleEggTargetGoal);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void usePlayerItem(Player debug1, ItemStack debug2) {
/*  382 */     if (isFood(debug2)) {
/*  383 */       playSound(getEatingSound(debug2), 1.0F, 1.0F);
/*      */     }
/*  385 */     super.usePlayerItem(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  390 */     if (isBaby()) {
/*  391 */       return debug2.height * 0.85F;
/*      */     }
/*  393 */     return 0.4F;
/*      */   }
/*      */   
/*      */   public Type getFoxType() {
/*  397 */     return Type.byId(((Integer)this.entityData.get(DATA_TYPE_ID)).intValue());
/*      */   }
/*      */   
/*      */   private void setFoxType(Type debug1) {
/*  401 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(debug1.getId()));
/*      */   }
/*      */   
/*      */   private List<UUID> getTrustedUUIDs() {
/*  405 */     List<UUID> debug1 = Lists.newArrayList();
/*  406 */     debug1.add(((Optional<UUID>)this.entityData.get(DATA_TRUSTED_ID_0)).orElse(null));
/*  407 */     debug1.add(((Optional<UUID>)this.entityData.get(DATA_TRUSTED_ID_1)).orElse(null));
/*  408 */     return debug1;
/*      */   }
/*      */   
/*      */   private void addTrustedUUID(@Nullable UUID debug1) {
/*  412 */     if (((Optional)this.entityData.get(DATA_TRUSTED_ID_0)).isPresent()) {
/*  413 */       this.entityData.set(DATA_TRUSTED_ID_1, Optional.ofNullable(debug1));
/*      */     } else {
/*  415 */       this.entityData.set(DATA_TRUSTED_ID_0, Optional.ofNullable(debug1));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  421 */     super.addAdditionalSaveData(debug1);
/*  422 */     List<UUID> debug2 = getTrustedUUIDs();
/*  423 */     ListTag debug3 = new ListTag();
/*  424 */     for (UUID debug5 : debug2) {
/*  425 */       if (debug5 != null) {
/*  426 */         debug3.add(NbtUtils.createUUID(debug5));
/*      */       }
/*      */     } 
/*  429 */     debug1.put("Trusted", (Tag)debug3);
/*  430 */     debug1.putBoolean("Sleeping", isSleeping());
/*  431 */     debug1.putString("Type", getFoxType().getName());
/*  432 */     debug1.putBoolean("Sitting", isSitting());
/*  433 */     debug1.putBoolean("Crouching", isCrouching());
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  438 */     super.readAdditionalSaveData(debug1);
/*  439 */     ListTag debug2 = debug1.getList("Trusted", 11);
/*  440 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  441 */       addTrustedUUID(NbtUtils.loadUUID(debug2.get(debug3)));
/*      */     }
/*  443 */     setSleeping(debug1.getBoolean("Sleeping"));
/*  444 */     setFoxType(Type.byName(debug1.getString("Type")));
/*  445 */     setSitting(debug1.getBoolean("Sitting"));
/*  446 */     setIsCrouching(debug1.getBoolean("Crouching"));
/*      */ 
/*      */     
/*  449 */     if (this.level instanceof ServerLevel) {
/*  450 */       setTargetGoals();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isSitting() {
/*  455 */     return getFlag(1);
/*      */   }
/*      */   
/*      */   public void setSitting(boolean debug1) {
/*  459 */     setFlag(1, debug1);
/*      */   }
/*      */   
/*      */   public boolean isFaceplanted() {
/*  463 */     return getFlag(64);
/*      */   }
/*      */   
/*      */   private void setFaceplanted(boolean debug1) {
/*  467 */     setFlag(64, debug1);
/*      */   }
/*      */   
/*      */   private boolean isDefending() {
/*  471 */     return getFlag(128);
/*      */   }
/*      */   
/*      */   private void setDefending(boolean debug1) {
/*  475 */     setFlag(128, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSleeping() {
/*  480 */     return getFlag(32);
/*      */   }
/*      */   
/*      */   private void setSleeping(boolean debug1) {
/*  484 */     setFlag(32, debug1);
/*      */   }
/*      */   
/*      */   private void setFlag(int debug1, boolean debug2) {
/*  488 */     if (debug2) {
/*  489 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | debug1)));
/*      */     } else {
/*  491 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (debug1 ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getFlag(int debug1) {
/*  496 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & debug1) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canTakeItem(ItemStack debug1) {
/*  501 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/*  502 */     if (!getItemBySlot(debug2).isEmpty()) {
/*  503 */       return false;
/*      */     }
/*  505 */     return (debug2 == EquipmentSlot.MAINHAND && super.canTakeItem(debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHoldItem(ItemStack debug1) {
/*  510 */     Item debug2 = debug1.getItem();
/*  511 */     ItemStack debug3 = getItemBySlot(EquipmentSlot.MAINHAND);
/*      */     
/*  513 */     return (debug3.isEmpty() || (this.ticksSinceEaten > 0 && debug2.isEdible() && !debug3.getItem().isEdible()));
/*      */   }
/*      */   
/*      */   private void spitOutItem(ItemStack debug1) {
/*  517 */     if (debug1.isEmpty() || this.level.isClientSide) {
/*      */       return;
/*      */     }
/*      */     
/*  521 */     ItemEntity debug2 = new ItemEntity(this.level, getX() + (getLookAngle()).x, getY() + 1.0D, getZ() + (getLookAngle()).z, debug1);
/*  522 */     debug2.setPickUpDelay(40);
/*  523 */     debug2.setThrower(getUUID());
/*      */     
/*  525 */     playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
/*  526 */     this.level.addFreshEntity((Entity)debug2);
/*      */   }
/*      */   
/*      */   private void dropItemStack(ItemStack debug1) {
/*  530 */     ItemEntity debug2 = new ItemEntity(this.level, getX(), getY(), getZ(), debug1);
/*  531 */     this.level.addFreshEntity((Entity)debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ItemEntity debug1) {
/*  536 */     ItemStack debug2 = debug1.getItem();
/*  537 */     if (canHoldItem(debug2)) {
/*  538 */       int debug3 = debug2.getCount();
/*  539 */       if (debug3 > 1) {
/*  540 */         dropItemStack(debug2.split(debug3 - 1));
/*      */       }
/*      */       
/*  543 */       spitOutItem(getItemBySlot(EquipmentSlot.MAINHAND));
/*      */       
/*  545 */       onItemPickup(debug1);
/*      */       
/*  547 */       setItemSlot(EquipmentSlot.MAINHAND, debug2.split(1));
/*  548 */       this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
/*  549 */       take((Entity)debug1, debug2.getCount());
/*  550 */       debug1.remove();
/*  551 */       this.ticksSinceEaten = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  557 */     super.tick();
/*      */     
/*  559 */     if (isEffectiveAi()) {
/*  560 */       boolean debug1 = isInWater();
/*  561 */       if (debug1 || getTarget() != null || this.level.isThundering()) {
/*  562 */         wakeUp();
/*      */       }
/*      */       
/*  565 */       if (debug1 || isSleeping()) {
/*  566 */         setSitting(false);
/*      */       }
/*      */       
/*  569 */       if (isFaceplanted() && this.level.random.nextFloat() < 0.2F) {
/*  570 */         BlockPos debug2 = blockPosition();
/*  571 */         BlockState debug3 = this.level.getBlockState(debug2);
/*  572 */         this.level.levelEvent(2001, debug2, Block.getId(debug3));
/*      */       } 
/*      */     } 
/*      */     
/*  576 */     this.interestedAngleO = this.interestedAngle;
/*  577 */     if (isInterested()) {
/*  578 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*      */     } else {
/*  580 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*      */     } 
/*      */     
/*  583 */     this.crouchAmountO = this.crouchAmount;
/*  584 */     if (isCrouching()) {
/*  585 */       this.crouchAmount += 0.2F;
/*  586 */       if (this.crouchAmount > 3.0F) {
/*  587 */         this.crouchAmount = 3.0F;
/*      */       }
/*      */     } else {
/*  590 */       this.crouchAmount = 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack debug1) {
/*  596 */     return (debug1.getItem() == Items.SWEET_BERRIES);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onOffspringSpawnedFromEgg(Player debug1, Mob debug2) {
/*  601 */     ((Fox)debug2).addTrustedUUID(debug1.getUUID());
/*      */   }
/*      */   
/*      */   public boolean isPouncing() {
/*  605 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public void setIsPouncing(boolean debug1) {
/*  609 */     setFlag(16, debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFullyCrouched() {
/*  617 */     return (this.crouchAmount == 3.0F);
/*      */   }
/*      */   
/*      */   public void setIsCrouching(boolean debug1) {
/*  621 */     setFlag(4, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCrouching() {
/*  626 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void setIsInterested(boolean debug1) {
/*  630 */     setFlag(8, debug1);
/*      */   }
/*      */   
/*      */   public boolean isInterested() {
/*  634 */     return getFlag(8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTarget(@Nullable LivingEntity debug1) {
/*  647 */     if (isDefending() && debug1 == null) {
/*  648 */       setDefending(false);
/*      */     }
/*  650 */     super.setTarget(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateFallDamage(float debug1, float debug2) {
/*  655 */     return Mth.ceil((debug1 - 5.0F) * debug2);
/*      */   }
/*      */   
/*      */   private void wakeUp() {
/*  659 */     setSleeping(false);
/*      */   }
/*      */   
/*      */   private void clearStates() {
/*  663 */     setIsInterested(false);
/*  664 */     setIsCrouching(false);
/*  665 */     setSitting(false);
/*  666 */     setSleeping(false);
/*  667 */     setDefending(false);
/*  668 */     setFaceplanted(false);
/*      */   }
/*      */   
/*      */   private boolean canMove() {
/*  672 */     return (!isSleeping() && !isSitting() && !isFaceplanted());
/*      */   }
/*      */ 
/*      */   
/*      */   public void playAmbientSound() {
/*  677 */     SoundEvent debug1 = getAmbientSound();
/*      */     
/*  679 */     if (debug1 == SoundEvents.FOX_SCREECH) {
/*  680 */       playSound(debug1, 2.0F, getVoicePitch());
/*      */     } else {
/*  682 */       super.playAmbientSound();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAmbientSound() {
/*  689 */     if (isSleeping()) {
/*  690 */       return SoundEvents.FOX_SLEEP;
/*      */     }
/*  692 */     if (!this.level.isDay() && this.random.nextFloat() < 0.1F) {
/*  693 */       List<Player> debug1 = this.level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
/*  694 */       if (debug1.isEmpty()) {
/*  695 */         return SoundEvents.FOX_SCREECH;
/*      */       }
/*      */     } 
/*  698 */     return SoundEvents.FOX_AMBIENT;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  704 */     return SoundEvents.FOX_HURT;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getDeathSound() {
/*  710 */     return SoundEvents.FOX_DEATH;
/*      */   }
/*      */   
/*      */   private boolean trusts(UUID debug1) {
/*  714 */     return getTrustedUUIDs().contains(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropAllDeathLoot(DamageSource debug1) {
/*  719 */     ItemStack debug2 = getItemBySlot(EquipmentSlot.MAINHAND);
/*      */     
/*  721 */     if (!debug2.isEmpty()) {
/*  722 */       spawnAtLocation(debug2);
/*  723 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*      */     } 
/*      */     
/*  726 */     super.dropAllDeathLoot(debug1);
/*      */   }
/*      */   
/*      */   public static boolean isPathClear(Fox debug0, LivingEntity debug1) {
/*  730 */     double debug2 = debug1.getZ() - debug0.getZ();
/*  731 */     double debug4 = debug1.getX() - debug0.getX();
/*  732 */     double debug6 = debug2 / debug4;
/*      */     
/*  734 */     int debug8 = 6;
/*  735 */     for (int debug9 = 0; debug9 < 6; debug9++) {
/*  736 */       double debug10 = (debug6 == 0.0D) ? 0.0D : (debug2 * (debug9 / 6.0F));
/*  737 */       double debug12 = (debug6 == 0.0D) ? (debug4 * (debug9 / 6.0F)) : (debug10 / debug6);
/*  738 */       for (int debug14 = 1; debug14 < 4; debug14++) {
/*  739 */         if (!debug0.level.getBlockState(new BlockPos(debug0.getX() + debug12, debug0.getY() + debug14, debug0.getZ() + debug10)).getMaterial().isReplaceable()) {
/*  740 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  745 */     return true;
/*      */   }
/*      */   
/*      */   class FoxSearchForItemsGoal extends Goal {
/*      */     public FoxSearchForItemsGoal() {
/*  750 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  755 */       if (!Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/*  756 */         return false;
/*      */       }
/*      */       
/*  759 */       if (Fox.this.getTarget() != null || Fox.this.getLastHurtByMob() != null) {
/*  760 */         return false;
/*      */       }
/*      */       
/*  763 */       if (!Fox.this.canMove()) {
/*  764 */         return false;
/*      */       }
/*      */       
/*  767 */       if (Fox.this.getRandom().nextInt(10) != 0) {
/*  768 */         return false;
/*      */       }
/*  770 */       List<ItemEntity> debug1 = Fox.this.level.getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  771 */       return (!debug1.isEmpty() && Fox.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  776 */       List<ItemEntity> debug1 = Fox.this.level.getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  777 */       ItemStack debug2 = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/*      */       
/*  779 */       if (debug2.isEmpty() && !debug1.isEmpty()) {
/*  780 */         Fox.this.getNavigation().moveTo((Entity)debug1.get(0), 1.2000000476837158D);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  786 */       List<ItemEntity> debug1 = Fox.this.level.getEntitiesOfClass(ItemEntity.class, Fox.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Fox.ALLOWED_ITEMS);
/*  787 */       if (!debug1.isEmpty())
/*  788 */         Fox.this.getNavigation().moveTo((Entity)debug1.get(0), 1.2000000476837158D); 
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxMoveControl
/*      */     extends MoveControl {
/*      */     public FoxMoveControl() {
/*  795 */       super((Mob)Fox.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  800 */       if (Fox.this.canMove())
/*  801 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   class StalkPreyGoal
/*      */     extends Goal {
/*      */     public StalkPreyGoal() {
/*  808 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  813 */       if (Fox.this.isSleeping()) {
/*  814 */         return false;
/*      */       }
/*      */       
/*  817 */       LivingEntity debug1 = Fox.this.getTarget();
/*  818 */       return (debug1 != null && debug1.isAlive() && Fox.STALKABLE_PREY.test(debug1) && Fox.this.distanceToSqr((Entity)debug1) > 36.0D && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.this.jumping);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  823 */       Fox.this.setSitting(false);
/*  824 */       Fox.this.setFaceplanted(false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void stop() {
/*  830 */       LivingEntity debug1 = Fox.this.getTarget();
/*  831 */       if (debug1 != null && Fox.isPathClear(Fox.this, debug1)) {
/*  832 */         Fox.this.setIsInterested(true);
/*  833 */         Fox.this.setIsCrouching(true);
/*  834 */         Fox.this.getNavigation().stop();
/*  835 */         Fox.this.getLookControl().setLookAt((Entity)debug1, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */       } else {
/*  837 */         Fox.this.setIsInterested(false);
/*  838 */         Fox.this.setIsCrouching(false);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  844 */       LivingEntity debug1 = Fox.this.getTarget();
/*  845 */       Fox.this.getLookControl().setLookAt((Entity)debug1, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*  846 */       if (Fox.this.distanceToSqr((Entity)debug1) <= 36.0D) {
/*  847 */         Fox.this.setIsInterested(true);
/*  848 */         Fox.this.setIsCrouching(true);
/*  849 */         Fox.this.getNavigation().stop();
/*      */       } else {
/*  851 */         Fox.this.getNavigation().moveTo((Entity)debug1, 1.5D);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxMeleeAttackGoal extends MeleeAttackGoal {
/*      */     public FoxMeleeAttackGoal(double debug2, boolean debug4) {
/*  858 */       super((PathfinderMob)Fox.this, debug2, debug4);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void checkAndPerformAttack(LivingEntity debug1, double debug2) {
/*  863 */       double debug4 = getAttackReachSqr(debug1);
/*  864 */       if (debug2 <= debug4 && isTimeToAttack()) {
/*  865 */         resetAttackCooldown();
/*  866 */         this.mob.doHurtTarget((Entity)debug1);
/*  867 */         Fox.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  873 */       Fox.this.setIsInterested(false);
/*  874 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  879 */       return (!Fox.this.isSitting() && !Fox.this.isSleeping() && !Fox.this.isCrouching() && !Fox.this.isFaceplanted() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxBreedGoal extends BreedGoal {
/*      */     public FoxBreedGoal(double debug2) {
/*  885 */       super(Fox.this, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  890 */       ((Fox)this.animal).clearStates();
/*  891 */       ((Fox)this.partner).clearStates();
/*  892 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void breed() {
/*  897 */       ServerLevel debug1 = (ServerLevel)this.level;
/*  898 */       Fox debug2 = (Fox)this.animal.getBreedOffspring(debug1, this.partner);
/*  899 */       if (debug2 == null) {
/*      */         return;
/*      */       }
/*      */       
/*  903 */       ServerPlayer debug3 = this.animal.getLoveCause();
/*  904 */       ServerPlayer debug4 = this.partner.getLoveCause();
/*  905 */       ServerPlayer debug5 = debug3;
/*      */       
/*  907 */       if (debug3 != null) {
/*  908 */         debug2.addTrustedUUID(debug3.getUUID());
/*      */       } else {
/*  910 */         debug5 = debug4;
/*      */       } 
/*      */       
/*  913 */       if (debug4 != null && debug3 != debug4) {
/*  914 */         debug2.addTrustedUUID(debug4.getUUID());
/*      */       }
/*      */       
/*  917 */       if (debug5 != null) {
/*  918 */         debug5.awardStat(Stats.ANIMALS_BRED);
/*  919 */         CriteriaTriggers.BRED_ANIMALS.trigger(debug5, this.animal, this.partner, debug2);
/*      */       } 
/*      */       
/*  922 */       this.animal.setAge(6000);
/*  923 */       this.partner.setAge(6000);
/*  924 */       this.animal.resetLove();
/*  925 */       this.partner.resetLove();
/*  926 */       debug2.setAge(-24000);
/*  927 */       debug2.moveTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
/*  928 */       debug1.addFreshEntityWithPassengers((Entity)debug2);
/*      */       
/*  930 */       this.level.broadcastEntityEvent((Entity)this.animal, (byte)18);
/*      */       
/*  932 */       if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
/*  933 */         this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1)); 
/*      */     }
/*      */   }
/*      */   
/*      */   class DefendTrustedTargetGoal
/*      */     extends NearestAttackableTargetGoal<LivingEntity> {
/*      */     @Nullable
/*      */     private LivingEntity trustedLastHurtBy;
/*      */     private LivingEntity trustedLastHurt;
/*      */     private int timestamp;
/*      */     
/*      */     public DefendTrustedTargetGoal(Class<LivingEntity> debug2, boolean debug3, @Nullable boolean debug4, Predicate<LivingEntity> debug5) {
/*  945 */       super((Mob)Fox.this, debug2, 10, debug3, debug4, debug5);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  950 */       if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
/*  951 */         return false;
/*      */       }
/*      */       
/*  954 */       for (UUID debug2 : Fox.this.getTrustedUUIDs()) {
/*  955 */         if (debug2 == null || !(Fox.this.level instanceof ServerLevel)) {
/*      */           continue;
/*      */         }
/*      */         
/*  959 */         Entity debug3 = ((ServerLevel)Fox.this.level).getEntity(debug2);
/*  960 */         if (!(debug3 instanceof LivingEntity)) {
/*      */           continue;
/*      */         }
/*  963 */         LivingEntity debug4 = (LivingEntity)debug3;
/*  964 */         this.trustedLastHurt = debug4;
/*  965 */         this.trustedLastHurtBy = debug4.getLastHurtByMob();
/*  966 */         int debug5 = debug4.getLastHurtByMobTimestamp();
/*  967 */         return (debug5 != this.timestamp && canAttack(this.trustedLastHurtBy, this.targetConditions));
/*      */       } 
/*  969 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  974 */       setTarget(this.trustedLastHurtBy);
/*  975 */       this.target = this.trustedLastHurtBy;
/*      */       
/*  977 */       if (this.trustedLastHurt != null) {
/*  978 */         this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp();
/*      */       }
/*      */       
/*  981 */       Fox.this.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
/*      */       
/*  983 */       Fox.this.setDefending(true);
/*      */ 
/*      */       
/*  986 */       Fox.this.wakeUp();
/*      */       
/*  988 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   class SeekShelterGoal extends FleeSunGoal {
/*      */     private int interval;
/*      */     
/*      */     public SeekShelterGoal(double debug2) {
/*  996 */       super((PathfinderMob)Fox.this, debug2);
/*  997 */       this.interval = 100;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1002 */       if (Fox.this.isSleeping() || this.mob.getTarget() != null) {
/* 1003 */         return false;
/*      */       }
/* 1005 */       if (Fox.this.level.isThundering()) {
/* 1006 */         return true;
/*      */       }
/* 1008 */       if (this.interval > 0) {
/* 1009 */         this.interval--;
/* 1010 */         return false;
/*      */       } 
/* 1012 */       this.interval = 100;
/*      */       
/* 1014 */       BlockPos debug1 = this.mob.blockPosition();
/*      */       
/* 1016 */       return (Fox.this.level.isDay() && Fox.this.level
/* 1017 */         .canSeeSky(debug1) && 
/* 1018 */         !((ServerLevel)Fox.this.level).isVillage(debug1) && 
/* 1019 */         setWantedPos());
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1024 */       Fox.this.clearStates();
/* 1025 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxAlertableEntitiesSelector
/*      */     implements Predicate<LivingEntity> {
/*      */     public boolean test(LivingEntity debug1) {
/* 1032 */       if (debug1 instanceof Fox) {
/* 1033 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1037 */       if (debug1 instanceof Chicken || debug1 instanceof Rabbit || debug1 instanceof net.minecraft.world.entity.monster.Monster) {
/* 1038 */         return true;
/*      */       }
/*      */ 
/*      */       
/* 1042 */       if (debug1 instanceof TamableAnimal) {
/* 1043 */         return !((TamableAnimal)debug1).isTame();
/*      */       }
/*      */ 
/*      */       
/* 1047 */       if (debug1 instanceof Player && (debug1.isSpectator() || ((Player)debug1).isCreative())) {
/* 1048 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1052 */       if (Fox.this.trusts(debug1.getUUID())) {
/* 1053 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1057 */       return (!debug1.isSleeping() && !debug1.isDiscrete());
/*      */     }
/*      */   }
/*      */   
/*      */   abstract class FoxBehaviorGoal extends Goal {
/* 1062 */     private final TargetingConditions alertableTargeting = (new TargetingConditions()).range(12.0D).allowUnseeable().selector(new Fox.FoxAlertableEntitiesSelector());
/*      */     
/*      */     protected boolean hasShelter() {
/* 1065 */       BlockPos debug1 = new BlockPos(Fox.this.getX(), (Fox.this.getBoundingBox()).maxY, Fox.this.getZ());
/* 1066 */       return (!Fox.this.level.canSeeSky(debug1) && Fox.this.getWalkTargetValue(debug1) >= 0.0F);
/*      */     }
/*      */     
/*      */     protected boolean alertable() {
/* 1070 */       return !Fox.this.level.getNearbyEntities(LivingEntity.class, this.alertableTargeting, (LivingEntity)Fox.this, Fox.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty();
/*      */     }
/*      */     
/*      */     private FoxBehaviorGoal() {} }
/*      */   
/*      */   class SleepGoal extends FoxBehaviorGoal {
/*      */     private int countdown;
/*      */     
/*      */     public SleepGoal() {
/* 1079 */       this.countdown = Fox.this.random.nextInt(140);
/* 1080 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1085 */       if (Fox.this.xxa != 0.0F || Fox.this.yya != 0.0F || Fox.this.zza != 0.0F) {
/* 1086 */         return false;
/*      */       }
/* 1088 */       return (canSleep() || Fox.this.isSleeping());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1093 */       return canSleep();
/*      */     }
/*      */     
/*      */     private boolean canSleep() {
/* 1097 */       if (this.countdown > 0) {
/* 1098 */         this.countdown--;
/* 1099 */         return false;
/*      */       } 
/* 1101 */       return (Fox.this.level.isDay() && hasShelter() && !alertable());
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1106 */       this.countdown = Fox.this.random.nextInt(140);
/* 1107 */       Fox.this.clearStates();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1112 */       Fox.this.setSitting(false);
/* 1113 */       Fox.this.setIsCrouching(false);
/* 1114 */       Fox.this.setIsInterested(false);
/* 1115 */       Fox.this.setJumping(false);
/* 1116 */       Fox.this.setSleeping(true);
/* 1117 */       Fox.this.getNavigation().stop();
/* 1118 */       Fox.this.getMoveControl().setWantedPosition(Fox.this.getX(), Fox.this.getY(), Fox.this.getZ(), 0.0D);
/*      */     }
/*      */   }
/*      */   
/*      */   class PerchAndSearchGoal extends FoxBehaviorGoal {
/*      */     private double relX;
/*      */     private double relZ;
/*      */     private int lookTime;
/*      */     private int looksRemaining;
/*      */     
/*      */     public PerchAndSearchGoal() {
/* 1129 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1134 */       return (Fox.this.getLastHurtByMob() == null && Fox.this.getRandom().nextFloat() < 0.02F && !Fox.this.isSleeping() && Fox.this.getTarget() == null && Fox.this.getNavigation().isDone() && !alertable() && !Fox.this.isPouncing() && !Fox.this.isCrouching());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1139 */       return (this.looksRemaining > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1144 */       resetLook();
/* 1145 */       this.looksRemaining = 2 + Fox.this.getRandom().nextInt(3);
/* 1146 */       Fox.this.setSitting(true);
/* 1147 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1152 */       Fox.this.setSitting(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1157 */       this.lookTime--;
/* 1158 */       if (this.lookTime <= 0) {
/* 1159 */         this.looksRemaining--;
/* 1160 */         resetLook();
/*      */       } 
/* 1162 */       Fox.this.getLookControl().setLookAt(Fox.this.getX() + this.relX, Fox.this.getEyeY(), Fox.this.getZ() + this.relZ, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*      */     }
/*      */     
/*      */     private void resetLook() {
/* 1166 */       double debug1 = 6.283185307179586D * Fox.this.getRandom().nextDouble();
/* 1167 */       this.relX = Math.cos(debug1);
/* 1168 */       this.relZ = Math.sin(debug1);
/* 1169 */       this.lookTime = 80 + Fox.this.getRandom().nextInt(20);
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxEatBerriesGoal
/*      */     extends MoveToBlockGoal
/*      */   {
/*      */     protected int ticksWaited;
/*      */     
/*      */     public FoxEatBerriesGoal(double debug2, int debug4, int debug5) {
/* 1179 */       super((PathfinderMob)debug1, debug2, debug4, debug5);
/*      */     }
/*      */ 
/*      */     
/*      */     public double acceptedDistance() {
/* 1184 */       return 2.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean shouldRecalculatePath() {
/* 1189 */       return (this.tryTicks % 100 == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 1194 */       BlockState debug3 = debug1.getBlockState(debug2);
/* 1195 */       return (debug3.is(Blocks.SWEET_BERRY_BUSH) && ((Integer)debug3.getValue((Property)SweetBerryBushBlock.AGE)).intValue() >= 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1200 */       if (isReachedTarget()) {
/* 1201 */         if (this.ticksWaited >= 40) {
/* 1202 */           onReachedTarget();
/*      */         } else {
/* 1204 */           this.ticksWaited++;
/*      */         } 
/* 1206 */       } else if (!isReachedTarget() && Fox.this.random.nextFloat() < 0.05F) {
/* 1207 */         Fox.this.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
/*      */       } 
/*      */       
/* 1210 */       super.tick();
/*      */     }
/*      */     
/*      */     protected void onReachedTarget() {
/* 1214 */       if (!Fox.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*      */         return;
/*      */       }
/*      */       
/* 1218 */       BlockState debug1 = Fox.this.level.getBlockState(this.blockPos);
/*      */       
/* 1220 */       if (!debug1.is(Blocks.SWEET_BERRY_BUSH)) {
/*      */         return;
/*      */       }
/*      */       
/* 1224 */       int debug2 = ((Integer)debug1.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1225 */       debug1.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1));
/* 1226 */       int debug3 = 1 + Fox.this.level.random.nextInt(2) + ((debug2 == 3) ? 1 : 0);
/* 1227 */       ItemStack debug4 = Fox.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1228 */       if (debug4.isEmpty()) {
/* 1229 */         Fox.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.SWEET_BERRIES));
/* 1230 */         debug3--;
/*      */       } 
/* 1232 */       if (debug3 > 0) {
/* 1233 */         Block.popResource(Fox.this.level, this.blockPos, new ItemStack((ItemLike)Items.SWEET_BERRIES, debug3));
/*      */       }
/* 1235 */       Fox.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
/* 1236 */       Fox.this.level.setBlock(this.blockPos, (BlockState)debug1.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(1)), 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1241 */       return (!Fox.this.isSleeping() && super.canUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1246 */       this.ticksWaited = 0;
/* 1247 */       Fox.this.setSitting(false);
/* 1248 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FoxGroupData extends AgableMob.AgableMobGroupData {
/*      */     public final Fox.Type type;
/*      */     
/*      */     public FoxGroupData(Fox.Type debug1) {
/* 1256 */       super(false);
/* 1257 */       this.type = debug1;
/*      */     }
/*      */   }
/*      */   
/*      */   class FaceplantGoal extends Goal {
/*      */     int countdown;
/*      */     
/*      */     public FaceplantGoal() {
/* 1265 */       setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1270 */       return Fox.this.isFaceplanted();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1275 */       return (canUse() && this.countdown > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1280 */       this.countdown = 40;
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1285 */       Fox.this.setFaceplanted(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1290 */       this.countdown--;
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxPanicGoal extends PanicGoal {
/*      */     public FoxPanicGoal(double debug2) {
/* 1296 */       super((PathfinderMob)Fox.this, debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1301 */       return (!Fox.this.isDefending() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxStrollThroughVillageGoal extends StrollThroughVillageGoal {
/*      */     public FoxStrollThroughVillageGoal(int debug2, int debug3) {
/* 1307 */       super((PathfinderMob)Fox.this, debug3);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1312 */       Fox.this.clearStates();
/* 1313 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1318 */       return (super.canUse() && canFoxMove());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1323 */       return (super.canContinueToUse() && canFoxMove());
/*      */     }
/*      */     
/*      */     private boolean canFoxMove() {
/* 1327 */       return (!Fox.this.isSleeping() && !Fox.this.isSitting() && !Fox.this.isDefending() && Fox.this.getTarget() == null);
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxFloatGoal extends FloatGoal {
/*      */     public FoxFloatGoal() {
/* 1333 */       super((Mob)Fox.this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1338 */       super.start();
/* 1339 */       Fox.this.clearStates();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1344 */       return ((Fox.this.isInWater() && Fox.this.getFluidHeight((Tag)FluidTags.WATER) > 0.25D) || Fox.this.isInLava());
/*      */     }
/*      */   }
/*      */   
/*      */   public class FoxPounceGoal
/*      */     extends JumpGoal {
/*      */     public boolean canUse() {
/* 1351 */       if (!Fox.this.isFullyCrouched()) {
/* 1352 */         return false;
/*      */       }
/*      */       
/* 1355 */       LivingEntity debug1 = Fox.this.getTarget();
/*      */       
/* 1357 */       if (debug1 == null || !debug1.isAlive()) {
/* 1358 */         return false;
/*      */       }
/*      */       
/* 1361 */       if (debug1.getMotionDirection() != debug1.getDirection()) {
/* 1362 */         return false;
/*      */       }
/*      */       
/* 1365 */       boolean debug2 = Fox.isPathClear(Fox.this, debug1);
/* 1366 */       if (!debug2) {
/* 1367 */         Fox.this.getNavigation().createPath((Entity)debug1, 0);
/* 1368 */         Fox.this.setIsCrouching(false);
/* 1369 */         Fox.this.setIsInterested(false);
/*      */       } 
/*      */       
/* 1372 */       return debug2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1377 */       LivingEntity debug1 = Fox.this.getTarget();
/*      */       
/* 1379 */       if (debug1 == null || !debug1.isAlive()) {
/* 1380 */         return false;
/*      */       }
/*      */       
/* 1383 */       double debug2 = (Fox.this.getDeltaMovement()).y;
/* 1384 */       return ((debug2 * debug2 >= 0.05000000074505806D || Math.abs(Fox.this.xRot) >= 15.0F || !Fox.this.onGround) && !Fox.this.isFaceplanted());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isInterruptable() {
/* 1389 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1394 */       Fox.this.setJumping(true);
/* 1395 */       Fox.this.setIsPouncing(true);
/* 1396 */       Fox.this.setIsInterested(false);
/*      */       
/* 1398 */       LivingEntity debug1 = Fox.this.getTarget();
/* 1399 */       Fox.this.getLookControl().setLookAt((Entity)debug1, 60.0F, 30.0F);
/*      */       
/* 1401 */       Vec3 debug2 = (new Vec3(debug1.getX() - Fox.this.getX(), debug1.getY() - Fox.this.getY(), debug1.getZ() - Fox.this.getZ())).normalize();
/* 1402 */       Fox.this.setDeltaMovement(Fox.this.getDeltaMovement().add(debug2.x * 0.8D, 0.9D, debug2.z * 0.8D));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1408 */       Fox.this.getNavigation().stop();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1413 */       Fox.this.setIsCrouching(false);
/* 1414 */       Fox.this.crouchAmount = 0.0F;
/* 1415 */       Fox.this.crouchAmountO = 0.0F;
/* 1416 */       Fox.this.setIsInterested(false);
/* 1417 */       Fox.this.setIsPouncing(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1422 */       LivingEntity debug1 = Fox.this.getTarget();
/*      */       
/* 1424 */       if (debug1 != null) {
/* 1425 */         Fox.this.getLookControl().setLookAt((Entity)debug1, 60.0F, 30.0F);
/*      */       }
/*      */       
/* 1428 */       if (!Fox.this.isFaceplanted()) {
/* 1429 */         Vec3 debug2 = Fox.this.getDeltaMovement();
/* 1430 */         if (debug2.y * debug2.y < 0.029999999329447746D && Fox.this.xRot != 0.0F) {
/* 1431 */           Fox.this.xRot = Mth.rotlerp(Fox.this.xRot, 0.0F, 0.2F);
/*      */         } else {
/* 1433 */           double debug3 = Math.sqrt(Entity.getHorizontalDistanceSqr(debug2));
/* 1434 */           double debug5 = Math.signum(-debug2.y) * Math.acos(debug3 / debug2.length()) * 57.2957763671875D;
/* 1435 */           Fox.this.xRot = (float)debug5;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1440 */       if (debug1 != null && Fox.this.distanceTo((Entity)debug1) <= 2.0F) {
/* 1441 */         Fox.this.doHurtTarget((Entity)debug1);
/*      */       }
/* 1443 */       else if (Fox.this.xRot > 0.0F && Fox.this.onGround && (float)(Fox.this.getDeltaMovement()).y != 0.0F && 
/* 1444 */         Fox.this.level.getBlockState(Fox.this.blockPosition()).is(Blocks.SNOW)) {
/* 1445 */         Fox.this.xRot = 60.0F;
/* 1446 */         Fox.this.setTarget((LivingEntity)null);
/* 1447 */         Fox.this.setFaceplanted(true);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class FoxLookControl
/*      */     extends LookControl
/*      */   {
/*      */     public FoxLookControl() {
/* 1461 */       super((Mob)debug1);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1466 */       if (!Fox.this.isSleeping()) {
/* 1467 */         super.tick();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean resetXRotOnTick() {
/* 1473 */       if (!Fox.this.isPouncing() && !Fox.this.isCrouching()) if (((!Fox.this.isInterested() ? 1 : 0) & (!Fox.this.isFaceplanted() ? 1 : 0)) != 0);  return false;
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxFollowParentGoal extends FollowParentGoal {
/*      */     private final Fox fox;
/*      */     
/*      */     public FoxFollowParentGoal(Fox debug2, double debug3) {
/* 1481 */       super(debug2, debug3);
/* 1482 */       this.fox = debug2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1487 */       return (!this.fox.isDefending() && super.canUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1492 */       return (!this.fox.isDefending() && super.canContinueToUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1497 */       this.fox.clearStates();
/* 1498 */       super.start();
/*      */     }
/*      */   }
/*      */   
/*      */   class FoxLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */     public FoxLookAtPlayerGoal(Mob debug2, Class<? extends LivingEntity> debug3, float debug4) {
/* 1504 */       super(debug2, debug3, debug4);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1509 */       return (super.canUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1514 */       return (super.canContinueToUse() && !Fox.this.isFaceplanted() && !Fox.this.isInterested());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Fox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */