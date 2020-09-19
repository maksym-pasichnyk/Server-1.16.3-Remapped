/*      */ package net.minecraft.world.entity.npc;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.ExperienceOrb;
/*      */ import net.minecraft.world.entity.LightningBolt;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.ReputationEventHandler;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.Brain;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
/*      */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*      */ import net.minecraft.world.entity.ai.gossip.GossipType;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*      */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*      */ import net.minecraft.world.entity.ai.sensing.GolemSensor;
/*      */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*      */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*      */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.animal.IronGolem;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Witch;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.entity.schedule.Activity;
/*      */ import net.minecraft.world.entity.schedule.Schedule;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.trading.MerchantOffer;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ 
/*      */ public class Villager
/*      */   extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
/*   92 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.VILLAGER_DATA);
/*      */ 
/*      */   
/*   95 */   public static final Map<Item, Integer> FOOD_POINTS = (Map<Item, Integer>)ImmutableMap.of(Items.BREAD, 
/*   96 */       Integer.valueOf(4), Items.POTATO, 
/*   97 */       Integer.valueOf(1), Items.CARROT, 
/*   98 */       Integer.valueOf(1), Items.BEETROOT, 
/*   99 */       Integer.valueOf(1));
/*      */ 
/*      */ 
/*      */   
/*  103 */   private static final Set<Item> WANTED_ITEMS = (Set<Item>)ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, (Object[])new Item[] { Items.BEETROOT_SEEDS });
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int updateMerchantTimer;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean increaseProfessionLevelOnUpdate;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Player lastTradedPlayer;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte foodLevel;
/*      */ 
/*      */ 
/*      */   
/*  127 */   private final GossipContainer gossips = new GossipContainer();
/*      */   
/*      */   private long lastGossipTime;
/*      */   
/*      */   private long lastGossipDecayTime;
/*      */   
/*      */   private int villagerXp;
/*      */   private long lastRestockGameTime;
/*      */   private int numberOfRestocksToday;
/*      */   private long lastRestockCheckDayTime;
/*      */   private boolean assignProfessionWhenSpawned;
/*  138 */   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY });
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
/*  170 */   private static final ImmutableList<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, PoiType>> POI_MEMORIES;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  182 */     POI_MEMORIES = (Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, PoiType>>)ImmutableMap.of(MemoryModuleType.HOME, (debug0, debug1) -> (debug1 == PoiType.HOME), MemoryModuleType.JOB_SITE, (debug0, debug1) -> (debug0.getVillagerData().getProfession().getJobPoiType() == debug1), MemoryModuleType.POTENTIAL_JOB_SITE, (debug0, debug1) -> PoiType.ALL_JOBS.test(debug1), MemoryModuleType.MEETING_POINT, (debug0, debug1) -> (debug1 == PoiType.MEETING));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Villager(EntityType<? extends Villager> debug1, Level debug2) {
/*  190 */     this(debug1, debug2, VillagerType.PLAINS);
/*      */   }
/*      */   
/*      */   public Villager(EntityType<? extends Villager> debug1, Level debug2, VillagerType debug3) {
/*  194 */     super((EntityType)debug1, debug2);
/*  195 */     ((GroundPathNavigation)getNavigation()).setCanOpenDoors(true);
/*  196 */     getNavigation().setCanFloat(true);
/*  197 */     setCanPickUpLoot(true);
/*  198 */     setVillagerData(getVillagerData().setType(debug3).setProfession(VillagerProfession.NONE));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Brain<Villager> getBrain() {
/*  204 */     return super.getBrain();
/*      */   }
/*      */ 
/*      */   
/*      */   protected Brain.Provider<Villager> brainProvider() {
/*  209 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/*  214 */     Brain<Villager> debug2 = brainProvider().makeBrain(debug1);
/*  215 */     registerBrainGoals(debug2);
/*  216 */     return debug2;
/*      */   }
/*      */   
/*      */   public void refreshBrain(ServerLevel debug1) {
/*  220 */     Brain<Villager> debug2 = getBrain();
/*  221 */     debug2.stopAll(debug1, (LivingEntity)this);
/*  222 */     this.brain = debug2.copyWithoutBehaviors();
/*  223 */     registerBrainGoals(getBrain());
/*      */   }
/*      */   
/*      */   private void registerBrainGoals(Brain<Villager> debug1) {
/*  227 */     VillagerProfession debug2 = getVillagerData().getProfession();
/*      */     
/*  229 */     if (isBaby()) {
/*  230 */       debug1.setSchedule(Schedule.VILLAGER_BABY);
/*  231 */       debug1.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F));
/*      */     } else {
/*  233 */       debug1.setSchedule(Schedule.VILLAGER_DEFAULT);
/*  234 */       debug1.addActivityWithConditions(Activity.WORK, VillagerGoalPackages.getWorkPackage(debug2, 0.5F), (Set)ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
/*      */     } 
/*      */     
/*  237 */     debug1.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage(debug2, 0.5F));
/*  238 */     debug1.addActivityWithConditions(Activity.MEET, VillagerGoalPackages.getMeetPackage(debug2, 0.5F), (Set)ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
/*  239 */     debug1.addActivity(Activity.REST, VillagerGoalPackages.getRestPackage(debug2, 0.5F));
/*  240 */     debug1.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage(debug2, 0.5F));
/*  241 */     debug1.addActivity(Activity.PANIC, VillagerGoalPackages.getPanicPackage(debug2, 0.5F));
/*  242 */     debug1.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(debug2, 0.5F));
/*  243 */     debug1.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(debug2, 0.5F));
/*  244 */     debug1.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(debug2, 0.5F));
/*  245 */     debug1.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  246 */     debug1.setDefaultActivity(Activity.IDLE);
/*  247 */     debug1.setActiveActivityIfPossible(Activity.IDLE);
/*  248 */     debug1.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void ageBoundaryReached() {
/*  253 */     super.ageBoundaryReached();
/*  254 */     if (this.level instanceof ServerLevel) {
/*  255 */       refreshBrain((ServerLevel)this.level);
/*      */     }
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  260 */     return Mob.createMobAttributes()
/*  261 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  262 */       .add(Attributes.FOLLOW_RANGE, 48.0D);
/*      */   }
/*      */   
/*      */   public boolean assignProfessionWhenSpawned() {
/*  266 */     return this.assignProfessionWhenSpawned;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep() {
/*  271 */     this.level.getProfiler().push("villagerBrain");
/*  272 */     getBrain().tick((ServerLevel)this.level, (LivingEntity)this);
/*  273 */     this.level.getProfiler().pop();
/*      */     
/*  275 */     if (this.assignProfessionWhenSpawned) {
/*  276 */       this.assignProfessionWhenSpawned = false;
/*      */     }
/*      */     
/*  279 */     if (!isTrading() && this.updateMerchantTimer > 0) {
/*  280 */       this.updateMerchantTimer--;
/*  281 */       if (this.updateMerchantTimer <= 0) {
/*  282 */         if (this.increaseProfessionLevelOnUpdate) {
/*  283 */           increaseMerchantCareer();
/*  284 */           this.increaseProfessionLevelOnUpdate = false;
/*      */         } 
/*  286 */         addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
/*      */       } 
/*      */     } 
/*      */     
/*  290 */     if (this.lastTradedPlayer != null && this.level instanceof ServerLevel) {
/*  291 */       ((ServerLevel)this.level).onReputationEvent(ReputationEventType.TRADE, (Entity)this.lastTradedPlayer, this);
/*  292 */       this.level.broadcastEntityEvent((Entity)this, (byte)14);
/*  293 */       this.lastTradedPlayer = null;
/*      */     } 
/*      */ 
/*      */     
/*  297 */     if (!isNoAi() && this.random.nextInt(100) == 0) {
/*  298 */       Raid debug1 = ((ServerLevel)this.level).getRaidAt(blockPosition());
/*  299 */       if (debug1 != null && debug1.isActive() && !debug1.isOver()) {
/*  300 */         this.level.broadcastEntityEvent((Entity)this, (byte)42);
/*      */       }
/*      */     } 
/*      */     
/*  304 */     if (getVillagerData().getProfession() == VillagerProfession.NONE && isTrading()) {
/*  305 */       stopTrading();
/*      */     }
/*      */     
/*  308 */     super.customServerAiStep();
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  313 */     super.tick();
/*      */     
/*  315 */     if (getUnhappyCounter() > 0) {
/*  316 */       setUnhappyCounter(getUnhappyCounter() - 1);
/*      */     }
/*      */     
/*  319 */     maybeDecayGossip();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  325 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*  326 */     if (debug3.getItem() != Items.VILLAGER_SPAWN_EGG && isAlive() && !isTrading() && !isSleeping()) {
/*  327 */       if (isBaby()) {
/*  328 */         setUnhappy();
/*  329 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */       } 
/*      */       
/*  332 */       boolean debug4 = getOffers().isEmpty();
/*      */ 
/*      */       
/*  335 */       if (debug2 == InteractionHand.MAIN_HAND) {
/*  336 */         if (debug4 && 
/*  337 */           !this.level.isClientSide) {
/*  338 */           setUnhappy();
/*      */         }
/*      */         
/*  341 */         debug1.awardStat(Stats.TALKED_TO_VILLAGER);
/*      */       } 
/*      */       
/*  344 */       if (debug4) {
/*  345 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */       }
/*      */       
/*  348 */       if (!this.level.isClientSide && !this.offers.isEmpty())
/*      */       {
/*  350 */         startTrading(debug1);
/*      */       }
/*      */       
/*  353 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */     } 
/*  355 */     return super.mobInteract(debug1, debug2);
/*      */   }
/*      */   
/*      */   private void setUnhappy() {
/*  359 */     setUnhappyCounter(40);
/*  360 */     if (!this.level.isClientSide()) {
/*  361 */       playSound(SoundEvents.VILLAGER_NO, getSoundVolume(), getVoicePitch());
/*      */     }
/*      */   }
/*      */   
/*      */   private void startTrading(Player debug1) {
/*  366 */     updateSpecialPrices(debug1);
/*  367 */     setTradingPlayer(debug1);
/*  368 */     openTradingScreen(debug1, getDisplayName(), getVillagerData().getLevel());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTradingPlayer(@Nullable Player debug1) {
/*  373 */     boolean debug2 = (getTradingPlayer() != null && debug1 == null);
/*  374 */     super.setTradingPlayer(debug1);
/*  375 */     if (debug2) {
/*  376 */       stopTrading();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void stopTrading() {
/*  382 */     super.stopTrading();
/*  383 */     resetSpecialPrices();
/*      */   }
/*      */   
/*      */   private void resetSpecialPrices() {
/*  387 */     for (MerchantOffer debug2 : getOffers()) {
/*  388 */       debug2.resetSpecialPriceDiff();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canRestock() {
/*  394 */     return true;
/*      */   }
/*      */   
/*      */   public void restock() {
/*  398 */     updateDemand();
/*  399 */     for (MerchantOffer debug2 : getOffers()) {
/*  400 */       debug2.resetUses();
/*      */     }
/*      */     
/*  403 */     this.lastRestockGameTime = this.level.getGameTime();
/*  404 */     this.numberOfRestocksToday++;
/*      */   }
/*      */   
/*      */   private boolean needsToRestock() {
/*  408 */     for (MerchantOffer debug2 : getOffers()) {
/*  409 */       if (debug2.needsRestock()) {
/*  410 */         return true;
/*      */       }
/*      */     } 
/*  413 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean allowedToRestock() {
/*  418 */     return (this.numberOfRestocksToday == 0 || (this.numberOfRestocksToday < 2 && this.level.getGameTime() > this.lastRestockGameTime + 2400L));
/*      */   }
/*      */   public boolean shouldRestock() {
/*      */     int i;
/*  422 */     long debug1 = this.lastRestockGameTime + 12000L;
/*  423 */     long debug3 = this.level.getGameTime();
/*  424 */     boolean debug5 = (debug3 > debug1);
/*      */ 
/*      */ 
/*      */     
/*  428 */     long debug6 = this.level.getDayTime();
/*  429 */     if (this.lastRestockCheckDayTime > 0L) {
/*  430 */       long debug8 = this.lastRestockCheckDayTime / 24000L;
/*  431 */       long debug10 = debug6 / 24000L;
/*  432 */       i = debug5 | ((debug10 > debug8) ? 1 : 0);
/*      */     } 
/*  434 */     this.lastRestockCheckDayTime = debug6;
/*      */     
/*  436 */     if (i != 0) {
/*  437 */       this.lastRestockGameTime = debug3;
/*  438 */       resetNumberOfRestocks();
/*      */     } 
/*      */     
/*  441 */     return (allowedToRestock() && needsToRestock());
/*      */   }
/*      */ 
/*      */   
/*      */   private void catchUpDemand() {
/*  446 */     int debug1 = 2 - this.numberOfRestocksToday;
/*  447 */     if (debug1 > 0) {
/*  448 */       for (MerchantOffer debug3 : getOffers()) {
/*  449 */         debug3.resetUses();
/*      */       }
/*      */     }
/*  452 */     for (int debug2 = 0; debug2 < debug1; debug2++) {
/*  453 */       updateDemand();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateDemand() {
/*  458 */     for (MerchantOffer debug2 : getOffers()) {
/*  459 */       debug2.updateDemand();
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateSpecialPrices(Player debug1) {
/*  464 */     int debug2 = getPlayerReputation(debug1);
/*  465 */     if (debug2 != 0) {
/*  466 */       for (MerchantOffer debug4 : getOffers()) {
/*  467 */         debug4.addToSpecialPriceDiff(-Mth.floor(debug2 * debug4.getPriceMultiplier()));
/*      */       }
/*      */     }
/*      */     
/*  471 */     if (debug1.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
/*  472 */       MobEffectInstance debug3 = debug1.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
/*  473 */       int debug4 = debug3.getAmplifier();
/*  474 */       for (MerchantOffer debug6 : getOffers()) {
/*  475 */         double debug7 = 0.3D + 0.0625D * debug4;
/*  476 */         int debug9 = (int)Math.floor(debug7 * debug6.getBaseCostA().getCount());
/*  477 */         debug6.addToSpecialPriceDiff(-Math.max(debug9, 1));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  484 */     super.defineSynchedData();
/*  485 */     this.entityData.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  490 */     super.addAdditionalSaveData(debug1);
/*  491 */     VillagerData.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, getVillagerData())
/*  492 */       .resultOrPartial(LOGGER::error)
/*  493 */       .ifPresent(debug1 -> debug0.put("VillagerData", debug1));
/*      */     
/*  495 */     debug1.putByte("FoodLevel", this.foodLevel);
/*  496 */     debug1.put("Gossips", (Tag)this.gossips.store((DynamicOps)NbtOps.INSTANCE).getValue());
/*  497 */     debug1.putInt("Xp", this.villagerXp);
/*  498 */     debug1.putLong("LastRestock", this.lastRestockGameTime);
/*  499 */     debug1.putLong("LastGossipDecay", this.lastGossipDecayTime);
/*  500 */     debug1.putInt("RestocksToday", this.numberOfRestocksToday);
/*  501 */     if (this.assignProfessionWhenSpawned) {
/*  502 */       debug1.putBoolean("AssignProfessionWhenSpawned", true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  508 */     super.readAdditionalSaveData(debug1);
/*      */     
/*  510 */     if (debug1.contains("VillagerData", 10)) {
/*  511 */       DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1.get("VillagerData")));
/*  512 */       dataResult.resultOrPartial(LOGGER::error).ifPresent(this::setVillagerData);
/*      */     } 
/*      */ 
/*      */     
/*  516 */     if (debug1.contains("Offers", 10)) {
/*  517 */       this.offers = new MerchantOffers(debug1.getCompound("Offers"));
/*      */     }
/*      */     
/*  520 */     if (debug1.contains("FoodLevel", 1)) {
/*  521 */       this.foodLevel = debug1.getByte("FoodLevel");
/*      */     }
/*      */     
/*  524 */     ListTag debug2 = debug1.getList("Gossips", 10);
/*  525 */     this.gossips.update(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug2));
/*      */     
/*  527 */     if (debug1.contains("Xp", 3)) {
/*  528 */       this.villagerXp = debug1.getInt("Xp");
/*      */     }
/*      */     
/*  531 */     this.lastRestockGameTime = debug1.getLong("LastRestock");
/*      */     
/*  533 */     this.lastGossipDecayTime = debug1.getLong("LastGossipDecay");
/*      */     
/*  535 */     setCanPickUpLoot(true);
/*      */ 
/*      */     
/*  538 */     if (this.level instanceof ServerLevel) {
/*  539 */       refreshBrain((ServerLevel)this.level);
/*      */     }
/*      */     
/*  542 */     this.numberOfRestocksToday = debug1.getInt("RestocksToday");
/*      */     
/*  544 */     if (debug1.contains("AssignProfessionWhenSpawned")) {
/*  545 */       this.assignProfessionWhenSpawned = debug1.getBoolean("AssignProfessionWhenSpawned");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeWhenFarAway(double debug1) {
/*  551 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAmbientSound() {
/*  557 */     if (isSleeping()) {
/*  558 */       return null;
/*      */     }
/*      */     
/*  561 */     if (isTrading()) {
/*  562 */       return SoundEvents.VILLAGER_TRADE;
/*      */     }
/*  564 */     return SoundEvents.VILLAGER_AMBIENT;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  569 */     return SoundEvents.VILLAGER_HURT;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  574 */     return SoundEvents.VILLAGER_DEATH;
/*      */   }
/*      */   
/*      */   public void playWorkSound() {
/*  578 */     SoundEvent debug1 = getVillagerData().getProfession().getWorkSound();
/*  579 */     if (debug1 != null) {
/*  580 */       playSound(debug1, getSoundVolume(), getVoicePitch());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVillagerData(VillagerData debug1) {
/*  586 */     VillagerData debug2 = getVillagerData();
/*  587 */     if (debug2.getProfession() != debug1.getProfession()) {
/*  588 */       this.offers = null;
/*      */     }
/*      */     
/*  591 */     this.entityData.set(DATA_VILLAGER_DATA, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public VillagerData getVillagerData() {
/*  596 */     return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void rewardTradeXp(MerchantOffer debug1) {
/*  601 */     int debug2 = 3 + this.random.nextInt(4);
/*      */     
/*  603 */     this.villagerXp += debug1.getXp();
/*  604 */     this.lastTradedPlayer = getTradingPlayer();
/*      */     
/*  606 */     if (shouldIncreaseLevel()) {
/*  607 */       this.updateMerchantTimer = 40;
/*  608 */       this.increaseProfessionLevelOnUpdate = true;
/*  609 */       debug2 += 5;
/*      */     } 
/*      */     
/*  612 */     if (debug1.shouldRewardExp()) {
/*  613 */       this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, getX(), getY() + 0.5D, getZ(), debug2));
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
/*      */   public void setLastHurtByMob(@Nullable LivingEntity debug1) {
/*  628 */     if (debug1 != null && this.level instanceof ServerLevel) {
/*  629 */       ((ServerLevel)this.level).onReputationEvent(ReputationEventType.VILLAGER_HURT, (Entity)debug1, this);
/*  630 */       if (isAlive() && debug1 instanceof Player) {
/*  631 */         this.level.broadcastEntityEvent((Entity)this, (byte)13);
/*      */       }
/*      */     } 
/*  634 */     super.setLastHurtByMob(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void die(DamageSource debug1) {
/*  639 */     LOGGER.info("Villager {} died, message: '{}'", this, debug1.getLocalizedDeathMessage((LivingEntity)this).getString());
/*  640 */     Entity debug2 = debug1.getEntity();
/*  641 */     if (debug2 != null) {
/*  642 */       tellWitnessesThatIWasMurdered(debug2);
/*      */     }
/*      */     
/*  645 */     releaseAllPois();
/*  646 */     super.die(debug1);
/*      */   }
/*      */   
/*      */   private void releaseAllPois() {
/*  650 */     releasePoi(MemoryModuleType.HOME);
/*  651 */     releasePoi(MemoryModuleType.JOB_SITE);
/*  652 */     releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
/*  653 */     releasePoi(MemoryModuleType.MEETING_POINT);
/*      */   }
/*      */   
/*      */   private void tellWitnessesThatIWasMurdered(Entity debug1) {
/*  657 */     if (!(this.level instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/*      */     
/*  661 */     Optional<List<LivingEntity>> debug2 = this.brain.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
/*  662 */     if (!debug2.isPresent()) {
/*      */       return;
/*      */     }
/*      */     
/*  666 */     ServerLevel debug3 = (ServerLevel)this.level;
/*  667 */     ((List)debug2.get()).stream()
/*  668 */       .filter(debug0 -> debug0 instanceof ReputationEventHandler)
/*  669 */       .forEach(debug2 -> debug0.onReputationEvent(ReputationEventType.VILLAGER_KILLED, debug1, (ReputationEventHandler)debug2));
/*      */   }
/*      */   
/*      */   public void releasePoi(MemoryModuleType<GlobalPos> debug1) {
/*  673 */     if (!(this.level instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/*  676 */     MinecraftServer debug2 = ((ServerLevel)this.level).getServer();
/*  677 */     this.brain.getMemory(debug1).ifPresent(debug3 -> {
/*      */           ServerLevel debug4 = debug1.getLevel(debug3.dimension());
/*      */           if (debug4 == null) {
/*      */             return;
/*      */           }
/*      */           PoiManager debug5 = debug4.getPoiManager();
/*      */           Optional<PoiType> debug6 = debug5.getType(debug3.pos());
/*      */           BiPredicate<Villager, PoiType> debug7 = POI_MEMORIES.get(debug2);
/*      */           if (debug6.isPresent() && debug7.test(this, debug6.get())) {
/*      */             debug5.release(debug3.pos());
/*      */             DebugPackets.sendPoiTicketCountPacket(debug4, debug3.pos());
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBreed() {
/*  694 */     return (this.foodLevel + countFoodPointsInInventory() >= 12 && getAge() == 0);
/*      */   }
/*      */   
/*      */   private boolean hungry() {
/*  698 */     return (this.foodLevel < 12);
/*      */   }
/*      */   
/*      */   private void eatUntilFull() {
/*  702 */     if (!hungry() || countFoodPointsInInventory() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  706 */     for (int debug1 = 0; debug1 < getInventory().getContainerSize(); debug1++) {
/*  707 */       ItemStack debug2 = getInventory().getItem(debug1);
/*      */       
/*  709 */       if (!debug2.isEmpty()) {
/*  710 */         Integer debug3 = FOOD_POINTS.get(debug2.getItem());
/*  711 */         if (debug3 != null) {
/*  712 */           int debug4 = debug2.getCount();
/*  713 */           for (int debug5 = debug4; debug5 > 0; debug5--) {
/*  714 */             this.foodLevel = (byte)(this.foodLevel + debug3.intValue());
/*  715 */             getInventory().removeItem(debug1, 1);
/*      */             
/*  717 */             if (!hungry()) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getPlayerReputation(Player debug1) {
/*  727 */     return this.gossips.getReputation(debug1.getUUID(), debug0 -> true);
/*      */   }
/*      */   
/*      */   private void digestFood(int debug1) {
/*  731 */     this.foodLevel = (byte)(this.foodLevel - debug1);
/*      */   }
/*      */   
/*      */   public void eatAndDigestFood() {
/*  735 */     eatUntilFull();
/*  736 */     digestFood(12);
/*      */   }
/*      */   
/*      */   public void setOffers(MerchantOffers debug1) {
/*  740 */     this.offers = debug1;
/*      */   }
/*      */   
/*      */   private boolean shouldIncreaseLevel() {
/*  744 */     int debug1 = getVillagerData().getLevel();
/*  745 */     return (VillagerData.canLevelUp(debug1) && this.villagerXp >= VillagerData.getMaxXpPerLevel(debug1));
/*      */   }
/*      */   
/*      */   private void increaseMerchantCareer() {
/*  749 */     setVillagerData(getVillagerData().setLevel(getVillagerData().getLevel() + 1));
/*      */     
/*  751 */     updateTrades();
/*      */   }
/*      */ 
/*      */   
/*      */   protected Component getTypeName() {
/*  756 */     return (Component)new TranslatableComponent(getType().getDescriptionId() + '.' + Registry.VILLAGER_PROFESSION.getKey(getVillagerData().getProfession()).getPath());
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
/*      */   @Nullable
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  777 */     if (debug3 == MobSpawnType.BREEDING) {
/*  778 */       setVillagerData(getVillagerData().setProfession(VillagerProfession.NONE));
/*      */     }
/*  780 */     if (debug3 == MobSpawnType.COMMAND || debug3 == MobSpawnType.SPAWN_EGG || debug3 == MobSpawnType.SPAWNER || debug3 == MobSpawnType.DISPENSER) {
/*  781 */       setVillagerData(getVillagerData().setType(VillagerType.byBiome(debug1.getBiomeName(blockPosition()))));
/*      */     }
/*      */     
/*  784 */     if (debug3 == MobSpawnType.STRUCTURE) {
/*  785 */       this.assignProfessionWhenSpawned = true;
/*      */     }
/*      */     
/*  788 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*      */   }
/*      */ 
/*      */   
/*      */   public Villager getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*      */     VillagerType debug3;
/*  794 */     double debug4 = this.random.nextDouble();
/*  795 */     if (debug4 < 0.5D) {
/*  796 */       debug3 = VillagerType.byBiome(debug1.getBiomeName(blockPosition()));
/*  797 */     } else if (debug4 < 0.75D) {
/*  798 */       debug3 = getVillagerData().getType();
/*      */     } else {
/*  800 */       debug3 = ((Villager)debug2).getVillagerData().getType();
/*      */     } 
/*      */     
/*  803 */     Villager debug6 = new Villager(EntityType.VILLAGER, (Level)debug1, debug3);
/*  804 */     debug6.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug6.blockPosition()), MobSpawnType.BREEDING, (SpawnGroupData)null, (CompoundTag)null);
/*  805 */     return debug6;
/*      */   }
/*      */ 
/*      */   
/*      */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/*  810 */     if (debug1.getDifficulty() != Difficulty.PEACEFUL) {
/*  811 */       LOGGER.info("Villager {} was struck by lightning {}.", this, debug2);
/*  812 */       Witch debug3 = (Witch)EntityType.WITCH.create((Level)debug1);
/*  813 */       debug3.moveTo(getX(), getY(), getZ(), this.yRot, this.xRot);
/*  814 */       debug3.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug3.blockPosition()), MobSpawnType.CONVERSION, null, null);
/*  815 */       debug3.setNoAi(isNoAi());
/*  816 */       if (hasCustomName()) {
/*  817 */         debug3.setCustomName(getCustomName());
/*  818 */         debug3.setCustomNameVisible(isCustomNameVisible());
/*      */       } 
/*  820 */       debug3.setPersistenceRequired();
/*  821 */       debug1.addFreshEntityWithPassengers((Entity)debug3);
/*  822 */       releaseAllPois();
/*  823 */       remove();
/*      */     } else {
/*  825 */       super.thunderHit(debug1, debug2);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ItemEntity debug1) {
/*  831 */     ItemStack debug2 = debug1.getItem();
/*      */     
/*  833 */     if (wantsToPickUp(debug2)) {
/*  834 */       SimpleContainer debug3 = getInventory();
/*      */       
/*  836 */       boolean debug4 = debug3.canAddItem(debug2);
/*  837 */       if (!debug4) {
/*      */         return;
/*      */       }
/*      */       
/*  841 */       onItemPickup(debug1);
/*  842 */       take((Entity)debug1, debug2.getCount());
/*  843 */       ItemStack debug5 = debug3.addItem(debug2);
/*  844 */       if (debug5.isEmpty()) {
/*  845 */         debug1.remove();
/*      */       } else {
/*  847 */         debug2.setCount(debug5.getCount());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean wantsToPickUp(ItemStack debug1) {
/*  854 */     Item debug2 = debug1.getItem();
/*  855 */     return ((WANTED_ITEMS.contains(debug2) || getVillagerData().getProfession().getRequestedItems().contains(debug2)) && getInventory().canAddItem(debug1));
/*      */   }
/*      */   
/*      */   public boolean hasExcessFood() {
/*  859 */     return (countFoodPointsInInventory() >= 24);
/*      */   }
/*      */   
/*      */   public boolean wantsMoreFood() {
/*  863 */     return (countFoodPointsInInventory() < 12);
/*      */   }
/*      */   
/*      */   private int countFoodPointsInInventory() {
/*  867 */     SimpleContainer debug1 = getInventory();
/*  868 */     return FOOD_POINTS.entrySet().stream().mapToInt(debug1 -> debug0.countItem((Item)debug1.getKey()) * ((Integer)debug1.getValue()).intValue()).sum();
/*      */   }
/*      */   
/*      */   public boolean hasFarmSeeds() {
/*  872 */     return getInventory().hasAnyOf((Set)ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateTrades() {
/*  877 */     VillagerData debug1 = getVillagerData();
/*      */     
/*  879 */     Int2ObjectMap<VillagerTrades.ItemListing[]> debug2 = VillagerTrades.TRADES.get(debug1.getProfession());
/*  880 */     if (debug2 == null || debug2.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  884 */     VillagerTrades.ItemListing[] debug3 = (VillagerTrades.ItemListing[])debug2.get(debug1.getLevel());
/*      */     
/*  886 */     if (debug3 == null) {
/*      */       return;
/*      */     }
/*      */     
/*  890 */     MerchantOffers debug4 = getOffers();
/*  891 */     addOffersFromItemListings(debug4, debug3, 2);
/*      */   }
/*      */   
/*      */   public void gossip(ServerLevel debug1, Villager debug2, long debug3) {
/*  895 */     if ((debug3 >= this.lastGossipTime && debug3 < this.lastGossipTime + 1200L) || (debug3 >= debug2.lastGossipTime && debug3 < debug2.lastGossipTime + 1200L)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  900 */     this.gossips.transferFrom(debug2.gossips, this.random, 10);
/*      */     
/*  902 */     this.lastGossipTime = debug3;
/*  903 */     debug2.lastGossipTime = debug3;
/*      */     
/*  905 */     spawnGolemIfNeeded(debug1, debug3, 5);
/*      */   }
/*      */   
/*      */   private void maybeDecayGossip() {
/*  909 */     long debug1 = this.level.getGameTime();
/*      */     
/*  911 */     if (this.lastGossipDecayTime == 0L) {
/*  912 */       this.lastGossipDecayTime = debug1;
/*      */       
/*      */       return;
/*      */     } 
/*  916 */     if (debug1 < this.lastGossipDecayTime + 24000L) {
/*      */       return;
/*      */     }
/*      */     
/*  920 */     this.gossips.decay();
/*  921 */     this.lastGossipDecayTime = debug1;
/*      */   }
/*      */   
/*      */   public void spawnGolemIfNeeded(ServerLevel debug1, long debug2, int debug4) {
/*  925 */     if (!wantsToSpawnGolem(debug2)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  930 */     AABB debug5 = getBoundingBox().inflate(10.0D, 10.0D, 10.0D);
/*      */     
/*  932 */     List<Villager> debug6 = debug1.getEntitiesOfClass(Villager.class, debug5);
/*      */ 
/*      */ 
/*      */     
/*  936 */     List<Villager> debug7 = (List<Villager>)debug6.stream().filter(debug2 -> debug2.wantsToSpawnGolem(debug0)).limit(5L).collect(Collectors.toList());
/*      */     
/*  938 */     if (debug7.size() < debug4) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  944 */     IronGolem debug8 = trySpawnGolem(debug1);
/*  945 */     if (debug8 == null) {
/*      */       return;
/*      */     }
/*      */     
/*  949 */     debug6.forEach(GolemSensor::golemDetected);
/*      */   }
/*      */   
/*      */   public boolean wantsToSpawnGolem(long debug1) {
/*  953 */     if (!golemSpawnConditionsMet(this.level.getGameTime())) {
/*  954 */       return false;
/*      */     }
/*  956 */     if (this.brain.hasMemoryValue(MemoryModuleType.GOLEM_DETECTED_RECENTLY)) {
/*  957 */       return false;
/*      */     }
/*  959 */     return true;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private IronGolem trySpawnGolem(ServerLevel debug1) {
/*  964 */     BlockPos debug2 = blockPosition();
/*  965 */     for (int debug3 = 0; debug3 < 10; debug3++) {
/*  966 */       double debug4 = (debug1.random.nextInt(16) - 8);
/*  967 */       double debug6 = (debug1.random.nextInt(16) - 8);
/*  968 */       BlockPos debug8 = findSpawnPositionForGolemInColumn(debug2, debug4, debug6);
/*  969 */       if (debug8 != null) {
/*      */ 
/*      */ 
/*      */         
/*  973 */         IronGolem debug9 = (IronGolem)EntityType.IRON_GOLEM.create(debug1, null, null, null, debug8, MobSpawnType.MOB_SUMMONED, false, false);
/*  974 */         if (debug9 != null) {
/*  975 */           if (debug9.checkSpawnRules((LevelAccessor)debug1, MobSpawnType.MOB_SUMMONED) && debug9.checkSpawnObstruction((LevelReader)debug1)) {
/*  976 */             debug1.addFreshEntityWithPassengers((Entity)debug9);
/*  977 */             return debug9;
/*      */           } 
/*  979 */           debug9.remove();
/*      */         } 
/*      */       } 
/*      */     } 
/*  983 */     return null;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private BlockPos findSpawnPositionForGolemInColumn(BlockPos debug1, double debug2, double debug4) {
/*  988 */     int debug6 = 6;
/*      */     
/*  990 */     BlockPos debug7 = debug1.offset(debug2, 6.0D, debug4);
/*  991 */     BlockState debug8 = this.level.getBlockState(debug7);
/*      */     
/*  993 */     for (int debug9 = 6; debug9 >= -6; debug9--) {
/*  994 */       BlockPos debug10 = debug7;
/*  995 */       BlockState debug11 = debug8;
/*  996 */       debug7 = debug10.below();
/*  997 */       debug8 = this.level.getBlockState(debug7);
/*  998 */       if ((debug11.isAir() || debug11.getMaterial().isLiquid()) && debug8.getMaterial().isSolidBlocking()) {
/*  999 */         return debug10;
/*      */       }
/*      */     } 
/* 1002 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onReputationEventFrom(ReputationEventType debug1, Entity debug2) {
/* 1007 */     if (debug1 == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
/* 1008 */       this.gossips.add(debug2.getUUID(), GossipType.MAJOR_POSITIVE, 20);
/* 1009 */       this.gossips.add(debug2.getUUID(), GossipType.MINOR_POSITIVE, 25);
/* 1010 */     } else if (debug1 == ReputationEventType.TRADE) {
/* 1011 */       this.gossips.add(debug2.getUUID(), GossipType.TRADING, 2);
/* 1012 */     } else if (debug1 == ReputationEventType.VILLAGER_HURT) {
/* 1013 */       this.gossips.add(debug2.getUUID(), GossipType.MINOR_NEGATIVE, 25);
/* 1014 */     } else if (debug1 == ReputationEventType.VILLAGER_KILLED) {
/* 1015 */       this.gossips.add(debug2.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getVillagerXp() {
/* 1021 */     return this.villagerXp;
/*      */   }
/*      */   
/*      */   public void setVillagerXp(int debug1) {
/* 1025 */     this.villagerXp = debug1;
/*      */   }
/*      */   
/*      */   private void resetNumberOfRestocks() {
/* 1029 */     catchUpDemand();
/* 1030 */     this.numberOfRestocksToday = 0;
/*      */   }
/*      */   
/*      */   public GossipContainer getGossips() {
/* 1034 */     return this.gossips;
/*      */   }
/*      */   
/*      */   public void setGossips(Tag debug1) {
/* 1038 */     this.gossips.update(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void sendDebugPackets() {
/* 1043 */     super.sendDebugPackets();
/*      */     
/* 1045 */     DebugPackets.sendEntityBrain((LivingEntity)this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void startSleeping(BlockPos debug1) {
/* 1050 */     super.startSleeping(debug1);
/* 1051 */     this.brain.setMemory(MemoryModuleType.LAST_SLEPT, Long.valueOf(this.level.getGameTime()));
/* 1052 */     this.brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 1053 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSleeping() {
/* 1058 */     super.stopSleeping();
/* 1059 */     this.brain.setMemory(MemoryModuleType.LAST_WOKEN, Long.valueOf(this.level.getGameTime()));
/*      */   }
/*      */   
/*      */   private boolean golemSpawnConditionsMet(long debug1) {
/* 1063 */     Optional<Long> debug3 = this.brain.getMemory(MemoryModuleType.LAST_SLEPT);
/* 1064 */     if (debug3.isPresent()) {
/* 1065 */       return (debug1 - ((Long)debug3.get()).longValue() < 24000L);
/*      */     }
/* 1067 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\Villager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */