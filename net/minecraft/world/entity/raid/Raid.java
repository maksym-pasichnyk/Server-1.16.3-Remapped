/*     */ package net.minecraft.world.entity.raid;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raid {
/*     */   enum RaidStatus {
/*  65 */     ONGOING,
/*  66 */     VICTORY,
/*  67 */     LOSS,
/*  68 */     STOPPED;
/*     */     
/*  70 */     private static final RaidStatus[] VALUES = values();
/*     */     
/*     */     private static RaidStatus getByName(String debug0) {
/*  73 */       for (RaidStatus debug4 : VALUES) {
/*  74 */         if (debug0.equalsIgnoreCase(debug4.name())) {
/*  75 */           return debug4;
/*     */         }
/*     */       } 
/*  78 */       return ONGOING;
/*     */     } static {
/*     */     
/*     */     } public String getName() {
/*  82 */       return name().toLowerCase(Locale.ROOT);
/*     */     }
/*     */   }
/*     */   
/*     */   enum RaiderType {
/*  87 */     VINDICATOR((String)EntityType.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }),
/*  88 */     EVOKER((String)EntityType.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2
/*     */       }),
/*  90 */     PILLAGER((String)EntityType.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }),
/*  91 */     WITCH((String)EntityType.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }),
/*  92 */     RAVAGER((String)EntityType.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 });
/*     */ 
/*     */     
/*  95 */     private static final RaiderType[] VALUES = values(); private final EntityType<? extends Raider> entityType; private final int[] spawnsPerWaveBeforeBonus;
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     RaiderType(EntityType<? extends Raider> debug3, int[] debug4) {
/* 101 */       this.entityType = debug3;
/* 102 */       this.spawnsPerWaveBeforeBonus = debug4;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static final Component RAID_NAME_COMPONENT = (Component)new TranslatableComponent("event.minecraft.raid");
/* 127 */   private static final Component VICTORY = (Component)new TranslatableComponent("event.minecraft.raid.victory");
/* 128 */   private static final Component DEFEAT = (Component)new TranslatableComponent("event.minecraft.raid.defeat");
/* 129 */   private static final Component RAID_BAR_VICTORY_COMPONENT = (Component)RAID_NAME_COMPONENT.copy().append(" - ").append(VICTORY);
/* 130 */   private static final Component RAID_BAR_DEFEAT_COMPONENT = (Component)RAID_NAME_COMPONENT.copy().append(" - ").append(DEFEAT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private final Map<Integer, Raider> groupToLeaderMap = Maps.newHashMap();
/* 139 */   private final Map<Integer, Set<Raider>> groupRaiderMap = Maps.newHashMap();
/*     */   
/* 141 */   private final Set<UUID> heroesOfTheVillage = Sets.newHashSet();
/*     */   
/*     */   private long ticksActive;
/*     */   private BlockPos center;
/*     */   private final ServerLevel level;
/*     */   private boolean started;
/*     */   private final int id;
/*     */   private float totalHealth;
/*     */   private int badOmenLevel;
/*     */   private boolean active;
/*     */   private int groupsSpawned;
/* 152 */   private final ServerBossEvent raidEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
/*     */   private int postRaidTicks;
/*     */   private int raidCooldownTicks;
/* 155 */   private final Random random = new Random();
/*     */   private final int numGroups;
/*     */   private RaidStatus status;
/*     */   private int celebrationTicks;
/* 159 */   private Optional<BlockPos> waveSpawnPos = Optional.empty();
/*     */   
/*     */   public Raid(int debug1, ServerLevel debug2, BlockPos debug3) {
/* 162 */     this.id = debug1;
/* 163 */     this.level = debug2;
/* 164 */     this.active = true;
/* 165 */     this.raidCooldownTicks = 300;
/* 166 */     this.raidEvent.setPercent(0.0F);
/* 167 */     this.center = debug3;
/* 168 */     this.numGroups = getNumGroups(debug2.getDifficulty());
/* 169 */     this.status = RaidStatus.ONGOING;
/*     */   }
/*     */   
/*     */   public Raid(ServerLevel debug1, CompoundTag debug2) {
/* 173 */     this.level = debug1;
/* 174 */     this.id = debug2.getInt("Id");
/* 175 */     this.started = debug2.getBoolean("Started");
/* 176 */     this.active = debug2.getBoolean("Active");
/* 177 */     this.ticksActive = debug2.getLong("TicksActive");
/* 178 */     this.badOmenLevel = debug2.getInt("BadOmenLevel");
/* 179 */     this.groupsSpawned = debug2.getInt("GroupsSpawned");
/* 180 */     this.raidCooldownTicks = debug2.getInt("PreRaidTicks");
/* 181 */     this.postRaidTicks = debug2.getInt("PostRaidTicks");
/* 182 */     this.totalHealth = debug2.getFloat("TotalHealth");
/* 183 */     this.center = new BlockPos(debug2.getInt("CX"), debug2.getInt("CY"), debug2.getInt("CZ"));
/* 184 */     this.numGroups = debug2.getInt("NumGroups");
/* 185 */     this.status = RaidStatus.getByName(debug2.getString("Status"));
/*     */     
/* 187 */     this.heroesOfTheVillage.clear();
/* 188 */     if (debug2.contains("HeroesOfTheVillage", 9)) {
/* 189 */       ListTag debug3 = debug2.getList("HeroesOfTheVillage", 11);
/* 190 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 191 */         this.heroesOfTheVillage.add(NbtUtils.loadUUID(debug3.get(debug4)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOver() {
/* 197 */     return (isVictory() || isLoss());
/*     */   }
/*     */   
/*     */   public boolean isBetweenWaves() {
/* 201 */     return (hasFirstWaveSpawned() && getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0);
/*     */   }
/*     */   
/*     */   public boolean hasFirstWaveSpawned() {
/* 205 */     return (this.groupsSpawned > 0);
/*     */   }
/*     */   
/*     */   public boolean isStopped() {
/* 209 */     return (this.status == RaidStatus.STOPPED);
/*     */   }
/*     */   
/*     */   public boolean isVictory() {
/* 213 */     return (this.status == RaidStatus.VICTORY);
/*     */   }
/*     */   
/*     */   public boolean isLoss() {
/* 217 */     return (this.status == RaidStatus.LOSS);
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
/*     */   public Level getLevel() {
/* 233 */     return (Level)this.level;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 237 */     return this.started;
/*     */   }
/*     */   
/*     */   public int getGroupsSpawned() {
/* 241 */     return this.groupsSpawned;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<ServerPlayer> validPlayer() {
/* 248 */     return debug1 -> {
/*     */         BlockPos debug2 = debug1.blockPosition();
/* 250 */         return (debug1.isAlive() && this.level.getRaidAt(debug2) == this);
/*     */       };
/*     */   }
/*     */   
/*     */   private void updatePlayers() {
/* 255 */     Set<ServerPlayer> debug1 = Sets.newHashSet(this.raidEvent.getPlayers());
/* 256 */     List<ServerPlayer> debug2 = this.level.getPlayers(validPlayer());
/*     */     
/* 258 */     for (ServerPlayer debug4 : debug2) {
/* 259 */       if (!debug1.contains(debug4)) {
/* 260 */         this.raidEvent.addPlayer(debug4);
/*     */       }
/*     */     } 
/*     */     
/* 264 */     for (ServerPlayer debug4 : debug1) {
/* 265 */       if (!debug2.contains(debug4)) {
/* 266 */         this.raidEvent.removePlayer(debug4);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxBadOmenLevel() {
/* 272 */     return 5;
/*     */   }
/*     */   
/*     */   public int getBadOmenLevel() {
/* 276 */     return this.badOmenLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorbBadOmen(Player debug1) {
/* 284 */     if (debug1.hasEffect(MobEffects.BAD_OMEN)) {
/* 285 */       this.badOmenLevel += debug1.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 1;
/* 286 */       this.badOmenLevel = Mth.clamp(this.badOmenLevel, 0, getMaxBadOmenLevel());
/*     */     } 
/* 288 */     debug1.removeEffect(MobEffects.BAD_OMEN);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 292 */     this.active = false;
/* 293 */     this.raidEvent.removeAllPlayers();
/* 294 */     this.status = RaidStatus.STOPPED;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 298 */     if (isStopped()) {
/*     */       return;
/*     */     }
/*     */     
/* 302 */     if (this.status == RaidStatus.ONGOING) {
/* 303 */       boolean debug1 = this.active;
/* 304 */       this.active = this.level.hasChunkAt(this.center);
/*     */       
/* 306 */       if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
/* 307 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/* 311 */       if (debug1 != this.active) {
/* 312 */         this.raidEvent.setVisible(this.active);
/*     */       }
/*     */ 
/*     */       
/* 316 */       if (!this.active) {
/*     */         return;
/*     */       }
/*     */       
/* 320 */       if (!this.level.isVillage(this.center))
/*     */       {
/*     */         
/* 323 */         moveRaidCenterToNearbyVillageSection();
/*     */       }
/*     */ 
/*     */       
/* 327 */       if (!this.level.isVillage(this.center))
/*     */       {
/* 329 */         if (this.groupsSpawned > 0) {
/* 330 */           this.status = RaidStatus.LOSS;
/*     */         } else {
/* 332 */           stop();
/*     */         } 
/*     */       }
/*     */       
/* 336 */       this.ticksActive++;
/*     */       
/* 338 */       if (this.ticksActive >= 48000L) {
/* 339 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/* 343 */       int debug2 = getTotalRaidersAlive();
/*     */ 
/*     */       
/* 346 */       if (debug2 == 0 && hasMoreWaves()) {
/* 347 */         if (this.raidCooldownTicks > 0) {
/* 348 */           boolean bool1 = this.waveSpawnPos.isPresent();
/* 349 */           boolean bool2 = (!bool1 && this.raidCooldownTicks % 5 == 0);
/*     */ 
/*     */           
/* 352 */           if (bool1 && !this.level.getChunkSource().isEntityTickingChunk(new ChunkPos(this.waveSpawnPos.get()))) {
/* 353 */             bool2 = true;
/*     */           }
/*     */ 
/*     */           
/* 357 */           if (bool2) {
/*     */             
/* 359 */             int debug5 = 0;
/* 360 */             if (this.raidCooldownTicks < 100) {
/* 361 */               debug5 = 1;
/* 362 */             } else if (this.raidCooldownTicks < 40) {
/* 363 */               debug5 = 2;
/*     */             } 
/* 365 */             this.waveSpawnPos = getValidSpawnPos(debug5);
/*     */           } 
/*     */           
/* 368 */           if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0) {
/* 369 */             updatePlayers();
/*     */           }
/* 371 */           this.raidCooldownTicks--;
/* 372 */           this.raidEvent.setPercent(Mth.clamp((300 - this.raidCooldownTicks) / 300.0F, 0.0F, 1.0F));
/* 373 */         } else if (this.raidCooldownTicks == 0 && this.groupsSpawned > 0) {
/* 374 */           this.raidCooldownTicks = 300;
/* 375 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/*     */       
/* 381 */       if (this.ticksActive % 20L == 0L) {
/* 382 */         updatePlayers();
/* 383 */         updateRaiders();
/*     */         
/* 385 */         if (debug2 > 0) {
/*     */           
/* 387 */           if (debug2 <= 2) {
/* 388 */             this.raidEvent.setName((Component)RAID_NAME_COMPONENT.copy().append(" - ").append((Component)new TranslatableComponent("event.minecraft.raid.raiders_remaining", new Object[] { Integer.valueOf(debug2) })));
/*     */           } else {
/* 390 */             this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           } 
/*     */         } else {
/* 393 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 403 */       boolean debug3 = false;
/* 404 */       int debug4 = 0;
/* 405 */       while (shouldSpawnGroup()) {
/*     */         
/* 407 */         BlockPos debug5 = this.waveSpawnPos.isPresent() ? this.waveSpawnPos.get() : findRandomSpawnPos(debug4, 20);
/* 408 */         if (debug5 != null) {
/* 409 */           this.started = true;
/* 410 */           spawnGroup(debug5);
/* 411 */           if (!debug3) {
/* 412 */             playSound(debug5);
/* 413 */             debug3 = true;
/*     */           } 
/*     */         } else {
/* 416 */           debug4++;
/*     */         } 
/*     */         
/* 419 */         if (debug4 > 3) {
/* 420 */           stop();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 426 */       if (isStarted() && !hasMoreWaves() && debug2 == 0) {
/* 427 */         if (this.postRaidTicks < 40) {
/* 428 */           this.postRaidTicks++;
/*     */         } else {
/* 430 */           this.status = RaidStatus.VICTORY;
/* 431 */           for (UUID debug6 : this.heroesOfTheVillage) {
/* 432 */             Entity debug7 = this.level.getEntity(debug6);
/* 433 */             if (debug7 instanceof LivingEntity && !debug7.isSpectator()) {
/* 434 */               LivingEntity debug8 = (LivingEntity)debug7;
/* 435 */               debug8.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.badOmenLevel - 1, false, false, true));
/* 436 */               if (debug8 instanceof ServerPlayer) {
/* 437 */                 ServerPlayer debug9 = (ServerPlayer)debug8;
/* 438 */                 debug9.awardStat(Stats.RAID_WIN);
/* 439 */                 CriteriaTriggers.RAID_WIN.trigger(debug9);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 446 */       setDirty();
/* 447 */     } else if (isOver()) {
/* 448 */       this.celebrationTicks++;
/* 449 */       if (this.celebrationTicks >= 600) {
/* 450 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 455 */       if (this.celebrationTicks % 20 == 0) {
/* 456 */         updatePlayers();
/* 457 */         this.raidEvent.setVisible(true);
/* 458 */         if (isVictory()) {
/* 459 */           this.raidEvent.setPercent(0.0F);
/* 460 */           this.raidEvent.setName(RAID_BAR_VICTORY_COMPONENT);
/*     */         } else {
/* 462 */           this.raidEvent.setName(RAID_BAR_DEFEAT_COMPONENT);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveRaidCenterToNearbyVillageSection() {
/* 469 */     Stream<SectionPos> debug1 = SectionPos.cube(SectionPos.of(this.center), 2);
/*     */     
/* 471 */     debug1
/* 472 */       .filter(this.level::isVillage)
/* 473 */       .map(SectionPos::center)
/* 474 */       .min(Comparator.comparingDouble(debug1 -> debug1.distSqr((Vec3i)this.center)))
/* 475 */       .ifPresent(this::setCenter);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getValidSpawnPos(int debug1) {
/* 479 */     for (int debug2 = 0; debug2 < 3; debug2++) {
/* 480 */       BlockPos debug3 = findRandomSpawnPos(debug1, 1);
/* 481 */       if (debug3 != null) {
/* 482 */         return Optional.of(debug3);
/*     */       }
/*     */     } 
/* 485 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private boolean hasMoreWaves() {
/* 489 */     if (hasBonusWave()) {
/* 490 */       return !hasSpawnedBonusWave();
/*     */     }
/* 492 */     return !isFinalWave();
/*     */   }
/*     */   
/*     */   private boolean isFinalWave() {
/* 496 */     return (getGroupsSpawned() == this.numGroups);
/*     */   }
/*     */   
/*     */   private boolean hasBonusWave() {
/* 500 */     return (this.badOmenLevel > 1);
/*     */   }
/*     */   
/*     */   private boolean hasSpawnedBonusWave() {
/* 504 */     return (getGroupsSpawned() > this.numGroups);
/*     */   }
/*     */   
/*     */   private boolean shouldSpawnBonusGroup() {
/* 508 */     return (isFinalWave() && getTotalRaidersAlive() == 0 && hasBonusWave());
/*     */   }
/*     */   
/*     */   private void updateRaiders() {
/* 512 */     Iterator<Set<Raider>> debug1 = this.groupRaiderMap.values().iterator();
/* 513 */     Set<Raider> debug2 = Sets.newHashSet();
/*     */     
/* 515 */     while (debug1.hasNext()) {
/* 516 */       Set<Raider> debug3 = debug1.next();
/* 517 */       for (Raider debug5 : debug3) {
/* 518 */         BlockPos debug6 = debug5.blockPosition();
/*     */ 
/*     */ 
/*     */         
/* 522 */         if (debug5.removed || debug5.level.dimension() != this.level.dimension() || this.center.distSqr((Vec3i)debug6) >= 12544.0D) {
/* 523 */           debug2.add(debug5); continue;
/*     */         } 
/* 525 */         if (debug5.tickCount <= 600) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 530 */         if (this.level.getEntity(debug5.getUUID()) == null) {
/* 531 */           debug2.add(debug5);
/*     */         }
/*     */ 
/*     */         
/* 535 */         if (!this.level.isVillage(debug6) && debug5.getNoActionTime() > 2400) {
/* 536 */           debug5.setTicksOutsideRaid(debug5.getTicksOutsideRaid() + 1);
/*     */         }
/*     */         
/* 539 */         if (debug5.getTicksOutsideRaid() >= 30) {
/* 540 */           debug2.add(debug5);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 545 */     for (Raider debug4 : debug2) {
/* 546 */       removeFromRaid(debug4, true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void playSound(BlockPos debug1) {
/* 551 */     float debug2 = 13.0F;
/* 552 */     int debug3 = 64;
/*     */     
/* 554 */     Collection<ServerPlayer> debug4 = this.raidEvent.getPlayers();
/* 555 */     for (ServerPlayer debug6 : this.level.players()) {
/* 556 */       Vec3 debug7 = debug6.position();
/* 557 */       Vec3 debug8 = Vec3.atCenterOf((Vec3i)debug1);
/* 558 */       float debug9 = Mth.sqrt((debug8.x - debug7.x) * (debug8.x - debug7.x) + (debug8.z - debug7.z) * (debug8.z - debug7.z));
/*     */       
/* 560 */       double debug10 = debug7.x + (13.0F / debug9) * (debug8.x - debug7.x);
/* 561 */       double debug12 = debug7.z + (13.0F / debug9) * (debug8.z - debug7.z);
/*     */       
/* 563 */       if (debug9 <= 64.0F || debug4.contains(debug6)) {
/* 564 */         debug6.connection.send((Packet)new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.NEUTRAL, debug10, debug6.getY(), debug12, 64.0F, 1.0F));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGroup(BlockPos debug1) {
/* 570 */     boolean debug2 = false;
/* 571 */     int debug3 = this.groupsSpawned + 1;
/* 572 */     this.totalHealth = 0.0F;
/* 573 */     DifficultyInstance debug4 = this.level.getCurrentDifficultyAt(debug1);
/* 574 */     boolean debug5 = shouldSpawnBonusGroup();
/*     */     
/* 576 */     for (RaiderType debug9 : RaiderType.VALUES) {
/* 577 */       int debug10 = getDefaultNumSpawns(debug9, debug3, debug5) + getPotentialBonusSpawns(debug9, this.random, debug3, debug4, debug5);
/* 578 */       int debug11 = 0;
/*     */       
/* 580 */       for (int debug12 = 0; debug12 < debug10; debug12++) {
/* 581 */         Raider debug13 = (Raider)debug9.entityType.create((Level)this.level);
/*     */         
/* 583 */         if (!debug2 && debug13.canBeLeader()) {
/* 584 */           debug13.setPatrolLeader(true);
/* 585 */           setLeader(debug3, debug13);
/* 586 */           debug2 = true;
/*     */         } 
/*     */         
/* 589 */         joinRaid(debug3, debug13, debug1, false);
/*     */         
/* 591 */         if (debug9.entityType == EntityType.RAVAGER) {
/* 592 */           Raider debug14 = null;
/* 593 */           if (debug3 == getNumGroups(Difficulty.NORMAL)) {
/* 594 */             debug14 = (Raider)EntityType.PILLAGER.create((Level)this.level);
/* 595 */           } else if (debug3 >= getNumGroups(Difficulty.HARD)) {
/*     */             
/* 597 */             if (debug11 == 0) {
/* 598 */               debug14 = (Raider)EntityType.EVOKER.create((Level)this.level);
/*     */             } else {
/* 600 */               debug14 = (Raider)EntityType.VINDICATOR.create((Level)this.level);
/*     */             } 
/*     */           } 
/* 603 */           debug11++;
/*     */           
/* 605 */           if (debug14 != null) {
/* 606 */             joinRaid(debug3, debug14, debug1, false);
/* 607 */             debug14.moveTo(debug1, 0.0F, 0.0F);
/* 608 */             debug14.startRiding((Entity)debug13);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 614 */     this.waveSpawnPos = Optional.empty();
/* 615 */     this.groupsSpawned++;
/* 616 */     updateBossbar();
/* 617 */     setDirty();
/*     */   }
/*     */   
/*     */   public void joinRaid(int debug1, Raider debug2, @Nullable BlockPos debug3, boolean debug4) {
/* 621 */     boolean debug5 = addWaveMob(debug1, debug2);
/*     */     
/* 623 */     if (debug5) {
/* 624 */       debug2.setCurrentRaid(this);
/* 625 */       debug2.setWave(debug1);
/* 626 */       debug2.setCanJoinRaid(true);
/* 627 */       debug2.setTicksOutsideRaid(0);
/*     */       
/* 629 */       if (!debug4 && debug3 != null) {
/* 630 */         debug2.setPos(debug3.getX() + 0.5D, debug3.getY() + 1.0D, debug3.getZ() + 0.5D);
/* 631 */         debug2.finalizeSpawn((ServerLevelAccessor)this.level, this.level.getCurrentDifficultyAt(debug3), MobSpawnType.EVENT, (SpawnGroupData)null, (CompoundTag)null);
/* 632 */         debug2.applyRaidBuffs(debug1, false);
/* 633 */         debug2.setOnGround(true);
/* 634 */         this.level.addFreshEntityWithPassengers((Entity)debug2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateBossbar() {
/* 640 */     this.raidEvent.setPercent(Mth.clamp(getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public float getHealthOfLivingRaiders() {
/* 644 */     float debug1 = 0.0F;
/* 645 */     for (Set<Raider> debug3 : this.groupRaiderMap.values()) {
/* 646 */       for (Raider debug5 : debug3) {
/* 647 */         debug1 += debug5.getHealth();
/*     */       }
/*     */     } 
/* 650 */     return debug1;
/*     */   }
/*     */   
/*     */   private boolean shouldSpawnGroup() {
/* 654 */     return (this.raidCooldownTicks == 0 && (this.groupsSpawned < this.numGroups || shouldSpawnBonusGroup()) && getTotalRaidersAlive() == 0);
/*     */   }
/*     */   
/*     */   public int getTotalRaidersAlive() {
/* 658 */     return this.groupRaiderMap.values().stream().mapToInt(Set::size).sum();
/*     */   }
/*     */   
/*     */   public void removeFromRaid(Raider debug1, boolean debug2) {
/* 662 */     Set<Raider> debug3 = this.groupRaiderMap.get(Integer.valueOf(debug1.getWave()));
/* 663 */     if (debug3 != null) {
/* 664 */       boolean debug4 = debug3.remove(debug1);
/* 665 */       if (debug4) {
/*     */ 
/*     */         
/* 668 */         if (debug2) {
/* 669 */           this.totalHealth -= debug1.getHealth();
/*     */         }
/* 671 */         debug1.setCurrentRaid((Raid)null);
/* 672 */         updateBossbar();
/* 673 */         setDirty();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setDirty() {
/* 679 */     this.level.getRaids().setDirty();
/*     */   }
/*     */   
/*     */   public static ItemStack getLeaderBannerInstance() {
/* 683 */     ItemStack debug0 = new ItemStack((ItemLike)Items.WHITE_BANNER);
/* 684 */     CompoundTag debug1 = debug0.getOrCreateTagElement("BlockEntityTag");
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
/* 695 */     ListTag debug2 = (new BannerPattern.Builder()).addPattern(BannerPattern.RHOMBUS_MIDDLE, DyeColor.CYAN).addPattern(BannerPattern.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.STRIPE_CENTER, DyeColor.GRAY).addPattern(BannerPattern.BORDER, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.STRIPE_MIDDLE, DyeColor.BLACK).addPattern(BannerPattern.HALF_HORIZONTAL, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).addPattern(BannerPattern.BORDER, DyeColor.BLACK).toListTag();
/*     */     
/* 697 */     debug1.put("Patterns", (Tag)debug2);
/*     */     
/* 699 */     debug0.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
/* 700 */     debug0.setHoverName((Component)(new TranslatableComponent("block.minecraft.ominous_banner")).withStyle(ChatFormatting.GOLD));
/*     */     
/* 702 */     return debug0;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Raider getLeader(int debug1) {
/* 707 */     return this.groupToLeaderMap.get(Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockPos findRandomSpawnPos(int debug1, int debug2) {
/* 712 */     int debug3 = (debug1 == 0) ? 2 : (2 - debug1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 717 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos();
/*     */     
/* 719 */     for (int debug8 = 0; debug8 < debug2; debug8++) {
/* 720 */       float debug9 = this.level.random.nextFloat() * 6.2831855F;
/* 721 */       int debug4 = this.center.getX() + Mth.floor(Mth.cos(debug9) * 32.0F * debug3) + this.level.random.nextInt(5);
/* 722 */       int debug6 = this.center.getZ() + Mth.floor(Mth.sin(debug9) * 32.0F * debug3) + this.level.random.nextInt(5);
/* 723 */       int debug5 = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, debug4, debug6);
/* 724 */       debug7.set(debug4, debug5, debug6);
/*     */ 
/*     */       
/* 727 */       if (!this.level.isVillage((BlockPos)debug7) || debug1 >= 2)
/*     */       {
/*     */ 
/*     */         
/* 731 */         if (this.level.hasChunksAt(debug7.getX() - 10, debug7.getY() - 10, debug7.getZ() - 10, debug7.getX() + 10, debug7.getY() + 10, debug7.getZ() + 10))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 737 */           if (this.level.getChunkSource().isEntityTickingChunk(new ChunkPos((BlockPos)debug7)))
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 742 */             if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, (LevelReader)this.level, (BlockPos)debug7, EntityType.RAVAGER) || (this.level
/* 743 */               .getBlockState(debug7.below()).is(Blocks.SNOW) && this.level.getBlockState((BlockPos)debug7).isAir()))
/*     */             {
/* 745 */               return (BlockPos)debug7; }  } 
/*     */         }
/*     */       }
/*     */     } 
/* 749 */     return null;
/*     */   }
/*     */   
/*     */   private boolean addWaveMob(int debug1, Raider debug2) {
/* 753 */     return addWaveMob(debug1, debug2, true);
/*     */   }
/*     */   
/*     */   public boolean addWaveMob(int debug1, Raider debug2, boolean debug3) {
/* 757 */     this.groupRaiderMap.computeIfAbsent(Integer.valueOf(debug1), debug0 -> Sets.newHashSet());
/* 758 */     Set<Raider> debug4 = this.groupRaiderMap.get(Integer.valueOf(debug1));
/* 759 */     Raider debug5 = null;
/*     */ 
/*     */ 
/*     */     
/* 763 */     for (Raider debug7 : debug4) {
/* 764 */       if (debug7.getUUID().equals(debug2.getUUID())) {
/* 765 */         debug5 = debug7;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 770 */     if (debug5 != null) {
/* 771 */       debug4.remove(debug5);
/* 772 */       debug4.add(debug2);
/*     */     } 
/*     */     
/* 775 */     debug4.add(debug2);
/* 776 */     if (debug3) {
/* 777 */       this.totalHealth += debug2.getHealth();
/*     */     }
/* 779 */     updateBossbar();
/* 780 */     setDirty();
/* 781 */     return true;
/*     */   }
/*     */   
/*     */   public void setLeader(int debug1, Raider debug2) {
/* 785 */     this.groupToLeaderMap.put(Integer.valueOf(debug1), debug2);
/* 786 */     debug2.setItemSlot(EquipmentSlot.HEAD, getLeaderBannerInstance());
/* 787 */     debug2.setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */   }
/*     */   
/*     */   public void removeLeader(int debug1) {
/* 791 */     this.groupToLeaderMap.remove(Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public BlockPos getCenter() {
/* 795 */     return this.center;
/*     */   }
/*     */   
/*     */   private void setCenter(BlockPos debug1) {
/* 799 */     this.center = debug1;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 803 */     return this.id;
/*     */   }
/*     */   
/*     */   private int getDefaultNumSpawns(RaiderType debug1, int debug2, boolean debug3) {
/* 807 */     return debug3 ? debug1.spawnsPerWaveBeforeBonus[this.numGroups] : debug1.spawnsPerWaveBeforeBonus[debug2];
/*     */   }
/*     */   
/*     */   private int getPotentialBonusSpawns(RaiderType debug1, Random debug2, int debug3, DifficultyInstance debug4, boolean debug5) {
/*     */     int debug9;
/* 812 */     Difficulty debug6 = debug4.getDifficulty();
/* 813 */     boolean debug7 = (debug6 == Difficulty.EASY);
/* 814 */     boolean debug8 = (debug6 == Difficulty.NORMAL);
/*     */     
/* 816 */     switch (debug1) {
/*     */       
/*     */       case EASY:
/* 819 */         if (!debug7 && debug3 > 2 && debug3 != 4) {
/* 820 */           int i = 1;
/*     */           break;
/*     */         } 
/* 823 */         return 0;
/*     */       
/*     */       case NORMAL:
/*     */       case HARD:
/* 827 */         if (debug7) {
/* 828 */           int i = debug2.nextInt(2); break;
/* 829 */         }  if (debug8) {
/* 830 */           int i = 1; break;
/*     */         } 
/* 832 */         debug9 = 2;
/*     */         break;
/*     */       
/*     */       case null:
/* 836 */         debug9 = (!debug7 && debug5) ? 1 : 0;
/*     */         break;
/*     */       default:
/* 839 */         return 0;
/*     */     } 
/*     */     
/* 842 */     return (debug9 > 0) ? debug2.nextInt(debug9 + 1) : 0;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 846 */     return this.active;
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 850 */     debug1.putInt("Id", this.id);
/* 851 */     debug1.putBoolean("Started", this.started);
/* 852 */     debug1.putBoolean("Active", this.active);
/* 853 */     debug1.putLong("TicksActive", this.ticksActive);
/* 854 */     debug1.putInt("BadOmenLevel", this.badOmenLevel);
/* 855 */     debug1.putInt("GroupsSpawned", this.groupsSpawned);
/* 856 */     debug1.putInt("PreRaidTicks", this.raidCooldownTicks);
/* 857 */     debug1.putInt("PostRaidTicks", this.postRaidTicks);
/* 858 */     debug1.putFloat("TotalHealth", this.totalHealth);
/* 859 */     debug1.putInt("NumGroups", this.numGroups);
/* 860 */     debug1.putString("Status", this.status.getName());
/*     */     
/* 862 */     debug1.putInt("CX", this.center.getX());
/* 863 */     debug1.putInt("CY", this.center.getY());
/* 864 */     debug1.putInt("CZ", this.center.getZ());
/*     */     
/* 866 */     ListTag debug2 = new ListTag();
/* 867 */     for (UUID debug4 : this.heroesOfTheVillage) {
/* 868 */       debug2.add(NbtUtils.createUUID(debug4));
/*     */     }
/* 870 */     debug1.put("HeroesOfTheVillage", (Tag)debug2);
/*     */     
/* 872 */     return debug1;
/*     */   }
/*     */   
/*     */   public int getNumGroups(Difficulty debug1) {
/* 876 */     switch (debug1) {
/*     */       case EASY:
/* 878 */         return 3;
/*     */       case NORMAL:
/* 880 */         return 5;
/*     */       case HARD:
/* 882 */         return 7;
/*     */     } 
/* 884 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEnchantOdds() {
/* 889 */     int debug1 = getBadOmenLevel();
/* 890 */     if (debug1 == 2) {
/* 891 */       return 0.1F;
/*     */     }
/* 893 */     if (debug1 == 3) {
/* 894 */       return 0.25F;
/*     */     }
/* 896 */     if (debug1 == 4) {
/* 897 */       return 0.5F;
/*     */     }
/* 899 */     if (debug1 == 5) {
/* 900 */       return 0.75F;
/*     */     }
/* 902 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void addHeroOfTheVillage(Entity debug1) {
/* 906 */     this.heroesOfTheVillage.add(debug1.getUUID());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\raid\Raid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */