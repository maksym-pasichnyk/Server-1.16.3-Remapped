/*      */ package net.minecraft.server.level;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSets;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.level.progress.ChunkProgressListener;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.TagContainer;
/*      */ import net.minecraft.util.ClassInstanceMultiMap;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProgressListener;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LightningBolt;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.entity.ReputationEventHandler;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.animal.horse.SkeletonHorse;
/*      */ import net.minecraft.world.entity.boss.EnderDragonPart;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.entity.raid.Raids;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.BlockEventData;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.CustomSpawner;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*      */ import net.minecraft.world.level.ForcedChunksSavedData;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.NaturalSpawner;
/*      */ import net.minecraft.world.level.ServerTickList;
/*      */ import net.minecraft.world.level.StructureFeatureManager;
/*      */ import net.minecraft.world.level.TickList;
/*      */ import net.minecraft.world.level.TickNextTickData;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.portal.PortalForcer;
/*      */ import net.minecraft.world.level.saveddata.SavedData;
/*      */ import net.minecraft.world.level.saveddata.maps.MapIndex;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class ServerLevel extends Level implements WorldGenLevel {
/*  142 */   public static final BlockPos END_SPAWN_POINT = new BlockPos(100, 50, 0);
/*      */   
/*  144 */   private static final Logger LOGGER = LogManager.getLogger();
/*      */ 
/*      */ 
/*      */   
/*  148 */   private final Int2ObjectMap<Entity> entitiesById = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
/*  149 */   private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
/*  150 */   private final Queue<Entity> toAddAfterTick = Queues.newArrayDeque();
/*  151 */   private final List<ServerPlayer> players = Lists.newArrayList();
/*      */   
/*      */   private final ServerChunkCache chunkSource;
/*      */   
/*      */   boolean tickingEntities;
/*      */   
/*      */   private final MinecraftServer server;
/*      */   
/*      */   private final ServerLevelData serverLevelData;
/*      */   
/*      */   public boolean noSave;
/*      */   
/*      */   private boolean allPlayersSleeping;
/*      */   
/*      */   private int emptyTime;
/*      */   
/*      */   private final PortalForcer portalForcer;
/*      */   private final ServerTickList<Block> blockTicks;
/*      */   private final ServerTickList<Fluid> liquidTicks;
/*      */   private final Set<PathNavigation> navigations;
/*      */   protected final Raids raids;
/*      */   private final ObjectLinkedOpenHashSet<BlockEventData> blockEvents;
/*      */   private boolean handlingTick;
/*      */   private final List<CustomSpawner> customSpawners;
/*      */   @Nullable
/*      */   private final EndDragonFight dragonFight;
/*      */   private final StructureFeatureManager structureFeatureManager;
/*      */   private final boolean tickTime;
/*      */   
/*      */   public ServerLevel(MinecraftServer debug1, Executor debug2, LevelStorageSource.LevelStorageAccess debug3, ServerLevelData debug4, ResourceKey<Level> debug5, DimensionType debug6, ChunkProgressListener debug7, ChunkGenerator debug8, boolean debug9, long debug10, List<CustomSpawner> debug12, boolean debug13) {
/*  181 */     super((WritableLevelData)debug4, debug5, debug6, debug1::getProfiler, false, debug9, debug10); this.blockTicks = new ServerTickList(this, debug0 -> (debug0 == null || debug0.defaultBlockState().isAir()), Registry.BLOCK::getKey, this::tickBlock); this.liquidTicks = new ServerTickList(this, debug0 -> (debug0 == null || debug0 == Fluids.EMPTY), Registry.FLUID::getKey, this::tickLiquid); this.navigations = Sets.newHashSet(); this.blockEvents = new ObjectLinkedOpenHashSet();
/*  182 */     this.tickTime = debug13;
/*  183 */     this.server = debug1;
/*  184 */     this.customSpawners = debug12;
/*  185 */     this.serverLevelData = debug4;
/*  186 */     this.chunkSource = new ServerChunkCache(this, debug3, debug1.getFixerUpper(), debug1.getStructureManager(), debug2, debug8, debug1.getPlayerList().getViewDistance(), debug1.forceSynchronousWrites(), debug7, () -> debug0.overworld().getDataStorage());
/*      */     
/*  188 */     this.portalForcer = new PortalForcer(this);
/*      */     
/*  190 */     updateSkyBrightness();
/*  191 */     prepareWeather();
/*      */     
/*  193 */     getWorldBorder().setAbsoluteMaxSize(debug1.getAbsoluteMaxWorldSize());
/*      */     
/*  195 */     this.raids = (Raids)getDataStorage().computeIfAbsent(() -> new Raids(this), Raids.getFileId(dimensionType()));
/*      */     
/*  197 */     if (!debug1.isSingleplayer()) {
/*  198 */       debug4.setGameType(debug1.getDefaultGameType());
/*      */     }
/*      */     
/*  201 */     this.structureFeatureManager = new StructureFeatureManager((LevelAccessor)this, debug1.getWorldData().worldGenSettings());
/*      */     
/*  203 */     if (dimensionType().createDragonFight()) {
/*  204 */       this.dragonFight = new EndDragonFight(this, debug1.getWorldData().worldGenSettings().seed(), debug1.getWorldData().endDragonFightData());
/*      */     } else {
/*  206 */       this.dragonFight = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setWeatherParameters(int debug1, int debug2, boolean debug3, boolean debug4) {
/*  211 */     this.serverLevelData.setClearWeatherTime(debug1);
/*  212 */     this.serverLevelData.setRainTime(debug2);
/*  213 */     this.serverLevelData.setThunderTime(debug2);
/*  214 */     this.serverLevelData.setRaining(debug3);
/*  215 */     this.serverLevelData.setThundering(debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public Biome getUncachedNoiseBiome(int debug1, int debug2, int debug3) {
/*  220 */     return getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   public StructureFeatureManager structureFeatureManager() {
/*  224 */     return this.structureFeatureManager;
/*      */   }
/*      */   
/*      */   public void tick(BooleanSupplier debug1) {
/*  228 */     ProfilerFiller debug2 = getProfiler();
/*      */     
/*  230 */     this.handlingTick = true;
/*  231 */     debug2.push("world border");
/*  232 */     getWorldBorder().tick();
/*  233 */     debug2.popPush("weather");
/*  234 */     boolean debug3 = isRaining();
/*  235 */     if (dimensionType().hasSkyLight()) {
/*  236 */       if (getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
/*  237 */         int i = this.serverLevelData.getClearWeatherTime();
/*  238 */         int debug5 = this.serverLevelData.getThunderTime();
/*  239 */         int debug6 = this.serverLevelData.getRainTime();
/*  240 */         boolean debug7 = this.levelData.isThundering();
/*  241 */         boolean debug8 = this.levelData.isRaining();
/*      */ 
/*      */         
/*  244 */         if (i > 0) {
/*  245 */           i--;
/*  246 */           debug5 = debug7 ? 0 : 1;
/*  247 */           debug6 = debug8 ? 0 : 1;
/*  248 */           debug7 = false;
/*  249 */           debug8 = false;
/*      */         } else {
/*      */           
/*  252 */           if (debug5 > 0) {
/*  253 */             debug5--;
/*  254 */             if (debug5 == 0)
/*      */             {
/*  256 */               debug7 = !debug7;
/*      */             
/*      */             }
/*      */           }
/*  260 */           else if (debug7) {
/*      */             
/*  262 */             debug5 = this.random.nextInt(12000) + 3600;
/*      */           } else {
/*      */             
/*  265 */             debug5 = this.random.nextInt(168000) + 12000;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  270 */           if (debug6 > 0) {
/*  271 */             debug6--;
/*  272 */             if (debug6 == 0) {
/*  273 */               debug8 = !debug8;
/*      */             }
/*      */           }
/*  276 */           else if (debug8) {
/*      */             
/*  278 */             debug6 = this.random.nextInt(12000) + 12000;
/*      */           } else {
/*      */             
/*  281 */             debug6 = this.random.nextInt(168000) + 12000;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  286 */         this.serverLevelData.setThunderTime(debug5);
/*  287 */         this.serverLevelData.setRainTime(debug6);
/*  288 */         this.serverLevelData.setClearWeatherTime(i);
/*  289 */         this.serverLevelData.setThundering(debug7);
/*  290 */         this.serverLevelData.setRaining(debug8);
/*      */       } 
/*      */       
/*  293 */       this.oThunderLevel = this.thunderLevel;
/*  294 */       if (this.levelData.isThundering()) {
/*  295 */         this.thunderLevel = (float)(this.thunderLevel + 0.01D);
/*      */       } else {
/*  297 */         this.thunderLevel = (float)(this.thunderLevel - 0.01D);
/*      */       } 
/*  299 */       this.thunderLevel = Mth.clamp(this.thunderLevel, 0.0F, 1.0F);
/*      */       
/*  301 */       this.oRainLevel = this.rainLevel;
/*  302 */       if (this.levelData.isRaining()) {
/*  303 */         this.rainLevel = (float)(this.rainLevel + 0.01D);
/*      */       } else {
/*  305 */         this.rainLevel = (float)(this.rainLevel - 0.01D);
/*      */       } 
/*  307 */       this.rainLevel = Mth.clamp(this.rainLevel, 0.0F, 1.0F);
/*      */     } 
/*      */     
/*  310 */     if (this.oRainLevel != this.rainLevel) {
/*  311 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel), dimension());
/*      */     }
/*  313 */     if (this.oThunderLevel != this.thunderLevel) {
/*  314 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel), dimension());
/*      */     }
/*      */     
/*  317 */     if (debug3 != isRaining()) {
/*  318 */       if (debug3) {
/*  319 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0F));
/*      */       } else {
/*  321 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
/*      */       } 
/*  323 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel));
/*  324 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel));
/*      */     } 
/*      */ 
/*      */     
/*  328 */     if (this.allPlayersSleeping && this.players.stream().noneMatch(debug0 -> (!debug0.isSpectator() && !debug0.isSleepingLongEnough()))) {
/*  329 */       this.allPlayersSleeping = false;
/*  330 */       if (getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
/*      */         
/*  332 */         long l = this.levelData.getDayTime() + 24000L;
/*  333 */         setDayTime(l - l % 24000L);
/*      */       } 
/*      */       
/*  336 */       wakeUpAllPlayers();
/*      */       
/*  338 */       if (getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
/*  339 */         stopWeather();
/*      */       }
/*      */     } 
/*      */     
/*  343 */     updateSkyBrightness();
/*      */     
/*  345 */     tickTime();
/*      */     
/*  347 */     debug2.popPush("chunkSource");
/*  348 */     getChunkSource().tick(debug1);
/*      */     
/*  350 */     debug2.popPush("tickPending");
/*  351 */     if (!isDebug()) {
/*  352 */       this.blockTicks.tick();
/*  353 */       this.liquidTicks.tick();
/*      */     } 
/*      */     
/*  356 */     debug2.popPush("raid");
/*  357 */     this.raids.tick();
/*      */     
/*  359 */     debug2.popPush("blockEvents");
/*      */     
/*  361 */     runBlockEvents();
/*  362 */     this.handlingTick = false;
/*      */     
/*  364 */     debug2.popPush("entities");
/*      */ 
/*      */     
/*  367 */     boolean debug4 = (!this.players.isEmpty() || !getForcedChunks().isEmpty());
/*      */     
/*  369 */     if (debug4) {
/*  370 */       resetEmptyTime();
/*      */     }
/*  372 */     if (debug4 || this.emptyTime++ < 300) {
/*  373 */       if (this.dragonFight != null) {
/*  374 */         this.dragonFight.tick();
/*      */       }
/*  376 */       this.tickingEntities = true;
/*  377 */       ObjectIterator<Int2ObjectMap.Entry<Entity>> debug5 = this.entitiesById.int2ObjectEntrySet().iterator();
/*  378 */       while (debug5.hasNext()) {
/*  379 */         Int2ObjectMap.Entry<Entity> entry = (Int2ObjectMap.Entry<Entity>)debug5.next();
/*  380 */         Entity debug7 = (Entity)entry.getValue();
/*  381 */         Entity debug8 = debug7.getVehicle();
/*  382 */         if (!this.server.isSpawningAnimals() && (debug7 instanceof net.minecraft.world.entity.animal.Animal || debug7 instanceof net.minecraft.world.entity.animal.WaterAnimal)) {
/*  383 */           debug7.remove();
/*      */         }
/*  385 */         if (!this.server.areNpcsEnabled() && debug7 instanceof net.minecraft.world.entity.npc.Npc) {
/*  386 */           debug7.remove();
/*      */         }
/*      */         
/*  389 */         debug2.push("checkDespawn");
/*  390 */         if (!debug7.removed) {
/*  391 */           debug7.checkDespawn();
/*      */         }
/*  393 */         debug2.pop();
/*      */         
/*  395 */         if (debug8 != null) {
/*  396 */           if (debug8.removed || !debug8.hasPassenger(debug7)) {
/*  397 */             debug7.stopRiding();
/*      */           } else {
/*      */             continue;
/*      */           } 
/*      */         }
/*      */         
/*  403 */         debug2.push("tick");
/*      */         
/*  405 */         if (!debug7.removed && !(debug7 instanceof EnderDragonPart)) {
/*  406 */           guardEntityTick(this::tickNonPassenger, debug7);
/*      */         }
/*  408 */         debug2.pop();
/*      */         
/*  410 */         debug2.push("remove");
/*  411 */         if (debug7.removed) {
/*  412 */           removeFromChunk(debug7);
/*  413 */           debug5.remove();
/*  414 */           onEntityRemoved(debug7);
/*      */         } 
/*  416 */         debug2.pop();
/*      */       } 
/*  418 */       this.tickingEntities = false;
/*      */       Entity debug6;
/*  420 */       while ((debug6 = this.toAddAfterTick.poll()) != null) {
/*  421 */         add(debug6);
/*      */       }
/*      */       
/*  424 */       tickBlockEntities();
/*      */     } 
/*      */     
/*  427 */     debug2.pop();
/*      */   }
/*      */   
/*      */   protected void tickTime() {
/*  431 */     if (!this.tickTime) {
/*      */       return;
/*      */     }
/*  434 */     long debug1 = this.levelData.getGameTime() + 1L;
/*  435 */     this.serverLevelData.setGameTime(debug1);
/*  436 */     this.serverLevelData.getScheduledEvents().tick(this.server, debug1);
/*  437 */     if (this.levelData.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
/*  438 */       setDayTime(this.levelData.getDayTime() + 1L);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setDayTime(long debug1) {
/*  443 */     this.serverLevelData.setDayTime(debug1);
/*      */   }
/*      */   
/*      */   public void tickCustomSpawners(boolean debug1, boolean debug2) {
/*  447 */     for (CustomSpawner debug4 : this.customSpawners) {
/*  448 */       debug4.tick(this, debug1, debug2);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void wakeUpAllPlayers() {
/*  454 */     ((List)this.players.stream().filter(LivingEntity::isSleeping).collect(Collectors.toList())).forEach(debug0 -> debug0.stopSleepInBed(false, false));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void tickChunk(LevelChunk debug1, int debug2) {
/*  460 */     ChunkPos debug3 = debug1.getPos();
/*  461 */     boolean debug4 = isRaining();
/*  462 */     int debug5 = debug3.getMinBlockX();
/*  463 */     int debug6 = debug3.getMinBlockZ();
/*      */     
/*  465 */     ProfilerFiller debug7 = getProfiler();
/*  466 */     debug7.push("thunder");
/*  467 */     if (debug4 && isThundering() && this.random.nextInt(100000) == 0) {
/*  468 */       BlockPos debug8 = findLightingTargetAround(getBlockRandomPos(debug5, 0, debug6, 15));
/*  469 */       if (isRainingAt(debug8)) {
/*  470 */         DifficultyInstance debug9 = getCurrentDifficultyAt(debug8);
/*      */         
/*  472 */         boolean debug10 = (getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && this.random.nextDouble() < debug9.getEffectiveDifficulty() * 0.01D);
/*  473 */         if (debug10) {
/*  474 */           SkeletonHorse skeletonHorse = (SkeletonHorse)EntityType.SKELETON_HORSE.create(this);
/*  475 */           skeletonHorse.setTrap(true);
/*  476 */           skeletonHorse.setAge(0);
/*  477 */           skeletonHorse.setPos(debug8.getX(), debug8.getY(), debug8.getZ());
/*  478 */           addFreshEntity((Entity)skeletonHorse);
/*      */         } 
/*  480 */         LightningBolt debug11 = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this);
/*  481 */         debug11.moveTo(Vec3.atBottomCenterOf((Vec3i)debug8));
/*  482 */         debug11.setVisualOnly(debug10);
/*  483 */         addFreshEntity((Entity)debug11);
/*      */       } 
/*      */     } 
/*      */     
/*  487 */     debug7.popPush("iceandsnow");
/*  488 */     if (this.random.nextInt(16) == 0) {
/*  489 */       BlockPos debug8 = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, getBlockRandomPos(debug5, 0, debug6, 15));
/*  490 */       BlockPos debug9 = debug8.below();
/*  491 */       Biome debug10 = getBiome(debug8);
/*      */       
/*  493 */       if (debug10.shouldFreeze((LevelReader)this, debug9)) {
/*  494 */         setBlockAndUpdate(debug9, Blocks.ICE.defaultBlockState());
/*      */       }
/*      */       
/*  497 */       if (debug4 && debug10.shouldSnow((LevelReader)this, debug8)) {
/*  498 */         setBlockAndUpdate(debug8, Blocks.SNOW.defaultBlockState());
/*      */       }
/*      */       
/*  501 */       if (debug4 && getBiome(debug9).getPrecipitation() == Biome.Precipitation.RAIN) {
/*  502 */         getBlockState(debug9).getBlock().handleRain(this, debug9);
/*      */       }
/*      */     } 
/*      */     
/*  506 */     debug7.popPush("tickBlocks");
/*  507 */     if (debug2 > 0) {
/*  508 */       for (LevelChunkSection debug11 : debug1.getSections()) {
/*  509 */         if (debug11 != LevelChunk.EMPTY_SECTION && debug11.isRandomlyTicking()) {
/*  510 */           int debug12 = debug11.bottomBlockY();
/*  511 */           for (int debug13 = 0; debug13 < debug2; debug13++) {
/*  512 */             BlockPos debug14 = getBlockRandomPos(debug5, debug12, debug6, 15);
/*      */             
/*  514 */             debug7.push("randomTick");
/*  515 */             BlockState debug15 = debug11.getBlockState(debug14.getX() - debug5, debug14.getY() - debug12, debug14.getZ() - debug6);
/*  516 */             if (debug15.isRandomlyTicking()) {
/*  517 */               debug15.randomTick(this, debug14, this.random);
/*      */             }
/*  519 */             FluidState debug16 = debug15.getFluidState();
/*  520 */             if (debug16.isRandomlyTicking()) {
/*  521 */               debug16.randomTick(this, debug14, this.random);
/*      */             }
/*  523 */             debug7.pop();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  528 */     debug7.pop();
/*      */   }
/*      */   
/*      */   protected BlockPos findLightingTargetAround(BlockPos debug1) {
/*  532 */     BlockPos debug2 = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, debug1);
/*  533 */     AABB debug3 = (new AABB(debug2, new BlockPos(debug2.getX(), getMaxBuildHeight(), debug2.getZ()))).inflate(3.0D);
/*      */     
/*  535 */     List<LivingEntity> debug4 = getEntitiesOfClass(LivingEntity.class, debug3, debug1 -> (debug1 != null && debug1.isAlive() && canSeeSky(debug1.blockPosition())));
/*      */     
/*  537 */     if (!debug4.isEmpty()) {
/*  538 */       return ((LivingEntity)debug4.get(this.random.nextInt(debug4.size()))).blockPosition();
/*      */     }
/*      */     
/*  541 */     if (debug2.getY() == -1) {
/*  542 */       debug2 = debug2.above(2);
/*      */     }
/*      */     
/*  545 */     return debug2;
/*      */   }
/*      */   
/*      */   public boolean isHandlingTick() {
/*  549 */     return this.handlingTick;
/*      */   }
/*      */   
/*      */   public void updateSleepingPlayerList() {
/*  553 */     this.allPlayersSleeping = false;
/*      */     
/*  555 */     if (!this.players.isEmpty()) {
/*  556 */       int debug1 = 0;
/*  557 */       int debug2 = 0;
/*      */       
/*  559 */       for (ServerPlayer debug4 : this.players) {
/*  560 */         if (debug4.isSpectator()) {
/*  561 */           debug1++; continue;
/*  562 */         }  if (debug4.isSleeping()) {
/*  563 */           debug2++;
/*      */         }
/*      */       } 
/*      */       
/*  567 */       this.allPlayersSleeping = (debug2 > 0 && debug2 >= this.players.size() - debug1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerScoreboard getScoreboard() {
/*  573 */     return this.server.getScoreboard();
/*      */   }
/*      */   
/*      */   private void stopWeather() {
/*  577 */     this.serverLevelData.setRainTime(0);
/*  578 */     this.serverLevelData.setRaining(false);
/*  579 */     this.serverLevelData.setThunderTime(0);
/*  580 */     this.serverLevelData.setThundering(false);
/*      */   }
/*      */   
/*      */   public void resetEmptyTime() {
/*  584 */     this.emptyTime = 0;
/*      */   }
/*      */   
/*      */   private void tickLiquid(TickNextTickData<Fluid> debug1) {
/*  588 */     FluidState debug2 = getFluidState(debug1.pos);
/*  589 */     if (debug2.getType() == debug1.getType()) {
/*  590 */       debug2.tick(this, debug1.pos);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tickBlock(TickNextTickData<Block> debug1) {
/*  595 */     BlockState debug2 = getBlockState(debug1.pos);
/*  596 */     if (debug2.is((Block)debug1.getType())) {
/*  597 */       debug2.tick(this, debug1.pos, this.random);
/*      */     }
/*      */   }
/*      */   
/*      */   public void tickNonPassenger(Entity debug1) {
/*  602 */     if (!(debug1 instanceof Player) && !getChunkSource().isEntityTickingChunk(debug1)) {
/*  603 */       updateChunkPos(debug1);
/*      */       
/*      */       return;
/*      */     } 
/*  607 */     debug1.setPosAndOldPos(debug1.getX(), debug1.getY(), debug1.getZ());
/*  608 */     debug1.yRotO = debug1.yRot;
/*  609 */     debug1.xRotO = debug1.xRot;
/*      */     
/*  611 */     if (debug1.inChunk) {
/*  612 */       debug1.tickCount++;
/*  613 */       ProfilerFiller debug2 = getProfiler();
/*  614 */       debug2.push(() -> Registry.ENTITY_TYPE.getKey(debug0.getType()).toString());
/*  615 */       debug2.incrementCounter("tickNonPassenger");
/*  616 */       debug1.tick();
/*  617 */       debug2.pop();
/*      */     } 
/*      */     
/*  620 */     updateChunkPos(debug1);
/*      */     
/*  622 */     if (debug1.inChunk) {
/*  623 */       for (Entity debug3 : debug1.getPassengers()) {
/*  624 */         tickPassenger(debug1, debug3);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void tickPassenger(Entity debug1, Entity debug2) {
/*  630 */     if (debug2.removed || debug2.getVehicle() != debug1) {
/*  631 */       debug2.stopRiding();
/*      */       return;
/*      */     } 
/*  634 */     if (!(debug2 instanceof Player) && !getChunkSource().isEntityTickingChunk(debug2)) {
/*      */       return;
/*      */     }
/*      */     
/*  638 */     debug2.setPosAndOldPos(debug2.getX(), debug2.getY(), debug2.getZ());
/*  639 */     debug2.yRotO = debug2.yRot;
/*  640 */     debug2.xRotO = debug2.xRot;
/*      */     
/*  642 */     if (debug2.inChunk) {
/*  643 */       debug2.tickCount++;
/*  644 */       ProfilerFiller debug3 = getProfiler();
/*  645 */       debug3.push(() -> Registry.ENTITY_TYPE.getKey(debug0.getType()).toString());
/*  646 */       debug3.incrementCounter("tickPassenger");
/*  647 */       debug2.rideTick();
/*  648 */       debug3.pop();
/*      */     } 
/*      */     
/*  651 */     updateChunkPos(debug2);
/*      */     
/*  653 */     if (debug2.inChunk) {
/*  654 */       for (Entity debug4 : debug2.getPassengers()) {
/*  655 */         tickPassenger(debug2, debug4);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateChunkPos(Entity debug1) {
/*  661 */     if (!debug1.checkAndResetUpdateChunkPos()) {
/*      */       return;
/*      */     }
/*  664 */     getProfiler().push("chunkCheck");
/*  665 */     int debug2 = Mth.floor(debug1.getX() / 16.0D);
/*  666 */     int debug3 = Mth.floor(debug1.getY() / 16.0D);
/*  667 */     int debug4 = Mth.floor(debug1.getZ() / 16.0D);
/*      */     
/*  669 */     if (!debug1.inChunk || debug1.xChunk != debug2 || debug1.yChunk != debug3 || debug1.zChunk != debug4) {
/*  670 */       if (debug1.inChunk && hasChunk(debug1.xChunk, debug1.zChunk)) {
/*  671 */         getChunk(debug1.xChunk, debug1.zChunk).removeEntity(debug1, debug1.yChunk);
/*      */       }
/*      */       
/*  674 */       if (debug1.checkAndResetForcedChunkAdditionFlag() || hasChunk(debug2, debug4)) {
/*  675 */         getChunk(debug2, debug4).addEntity(debug1);
/*      */       } else {
/*  677 */         if (debug1.inChunk) {
/*  678 */           LOGGER.warn("Entity {} left loaded chunk area", debug1);
/*      */         }
/*  680 */         debug1.inChunk = false;
/*      */       } 
/*      */     } 
/*  683 */     getProfiler().pop();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayInteract(Player debug1, BlockPos debug2) {
/*  688 */     return (!this.server.isUnderSpawnProtection(this, debug2, debug1) && getWorldBorder().isWithinBounds(debug2));
/*      */   }
/*      */   
/*      */   public void save(@Nullable ProgressListener debug1, boolean debug2, boolean debug3) {
/*  692 */     ServerChunkCache debug4 = getChunkSource();
/*  693 */     if (debug3) {
/*      */       return;
/*      */     }
/*      */     
/*  697 */     if (debug1 != null) {
/*  698 */       debug1.progressStartNoAbort((Component)new TranslatableComponent("menu.savingLevel"));
/*      */     }
/*  700 */     saveLevelData();
/*      */     
/*  702 */     if (debug1 != null) {
/*  703 */       debug1.progressStage((Component)new TranslatableComponent("menu.savingChunks"));
/*      */     }
/*  705 */     debug4.save(debug2);
/*      */   }
/*      */   
/*      */   private void saveLevelData() {
/*  709 */     if (this.dragonFight != null) {
/*  710 */       this.server.getWorldData().setEndDragonFightData(this.dragonFight.saveData());
/*      */     }
/*  712 */     getChunkSource().getDataStorage().save();
/*      */   }
/*      */   
/*      */   public List<Entity> getEntities(@Nullable EntityType<?> debug1, Predicate<? super Entity> debug2) {
/*  716 */     List<Entity> debug3 = Lists.newArrayList();
/*  717 */     ServerChunkCache debug4 = getChunkSource();
/*  718 */     for (ObjectIterator<Entity> objectIterator = this.entitiesById.values().iterator(); objectIterator.hasNext(); ) { Entity debug6 = objectIterator.next();
/*  719 */       if ((debug1 == null || debug6.getType() == debug1) && debug4.hasChunk(Mth.floor(debug6.getX()) >> 4, Mth.floor(debug6.getZ()) >> 4) && debug2.test(debug6)) {
/*  720 */         debug3.add(debug6);
/*      */       } }
/*      */     
/*  723 */     return debug3;
/*      */   }
/*      */   
/*      */   public List<EnderDragon> getDragons() {
/*  727 */     List<EnderDragon> debug1 = Lists.newArrayList();
/*  728 */     for (ObjectIterator<Entity> objectIterator = this.entitiesById.values().iterator(); objectIterator.hasNext(); ) { Entity debug3 = objectIterator.next();
/*  729 */       if (debug3 instanceof EnderDragon && debug3.isAlive()) {
/*  730 */         debug1.add((EnderDragon)debug3);
/*      */       } }
/*      */     
/*  733 */     return debug1;
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> debug1) {
/*  737 */     List<ServerPlayer> debug2 = Lists.newArrayList();
/*  738 */     for (ServerPlayer debug4 : this.players) {
/*  739 */       if (debug1.test(debug4)) {
/*  740 */         debug2.add(debug4);
/*      */       }
/*      */     } 
/*  743 */     return debug2;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ServerPlayer getRandomPlayer() {
/*  748 */     List<ServerPlayer> debug1 = getPlayers(LivingEntity::isAlive);
/*  749 */     if (debug1.isEmpty()) {
/*  750 */       return null;
/*      */     }
/*  752 */     return debug1.get(this.random.nextInt(debug1.size()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addFreshEntity(Entity debug1) {
/*  760 */     return addEntity(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addWithUUID(Entity debug1) {
/*  767 */     return addEntity(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFromAnotherDimension(Entity debug1) {
/*  775 */     boolean debug2 = debug1.forcedLoading;
/*  776 */     debug1.forcedLoading = true;
/*  777 */     addWithUUID(debug1);
/*  778 */     debug1.forcedLoading = debug2;
/*  779 */     updateChunkPos(debug1);
/*      */   }
/*      */   
/*      */   public void addDuringCommandTeleport(ServerPlayer debug1) {
/*  783 */     addPlayer(debug1);
/*  784 */     updateChunkPos((Entity)debug1);
/*      */   }
/*      */   
/*      */   public void addDuringPortalTeleport(ServerPlayer debug1) {
/*  788 */     addPlayer(debug1);
/*  789 */     updateChunkPos((Entity)debug1);
/*      */   }
/*      */   
/*      */   public void addNewPlayer(ServerPlayer debug1) {
/*  793 */     addPlayer(debug1);
/*      */   }
/*      */   
/*      */   public void addRespawnedPlayer(ServerPlayer debug1) {
/*  797 */     addPlayer(debug1);
/*      */   }
/*      */   
/*      */   private void addPlayer(ServerPlayer debug1) {
/*  801 */     Entity debug2 = this.entitiesByUuid.get(debug1.getUUID());
/*  802 */     if (debug2 != null) {
/*  803 */       LOGGER.warn("Force-added player with duplicate UUID {}", debug1.getUUID().toString());
/*  804 */       debug2.unRide();
/*  805 */       removePlayerImmediately((ServerPlayer)debug2);
/*      */     } 
/*  807 */     this.players.add(debug1);
/*  808 */     updateSleepingPlayerList();
/*      */     
/*  810 */     ChunkAccess debug3 = getChunk(Mth.floor(debug1.getX() / 16.0D), Mth.floor(debug1.getZ() / 16.0D), ChunkStatus.FULL, true);
/*  811 */     if (debug3 instanceof LevelChunk) {
/*  812 */       debug3.addEntity((Entity)debug1);
/*      */     }
/*  814 */     add((Entity)debug1);
/*      */   }
/*      */   
/*      */   private boolean addEntity(Entity debug1) {
/*  818 */     if (debug1.removed) {
/*  819 */       LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityType.getKey(debug1.getType()));
/*  820 */       return false;
/*      */     } 
/*  822 */     if (isUUIDUsed(debug1)) {
/*  823 */       return false;
/*      */     }
/*  825 */     ChunkAccess debug2 = getChunk(Mth.floor(debug1.getX() / 16.0D), Mth.floor(debug1.getZ() / 16.0D), ChunkStatus.FULL, debug1.forcedLoading);
/*  826 */     if (!(debug2 instanceof LevelChunk)) {
/*  827 */       return false;
/*      */     }
/*  829 */     debug2.addEntity(debug1);
/*  830 */     add(debug1);
/*  831 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean loadFromChunk(Entity debug1) {
/*  838 */     if (isUUIDUsed(debug1)) {
/*  839 */       return false;
/*      */     }
/*  841 */     add(debug1);
/*  842 */     return true;
/*      */   }
/*      */   
/*      */   private boolean isUUIDUsed(Entity debug1) {
/*  846 */     UUID debug2 = debug1.getUUID();
/*  847 */     Entity debug3 = findAddedOrPendingEntity(debug2);
/*  848 */     if (debug3 == null) {
/*  849 */       return false;
/*      */     }
/*  851 */     LOGGER.warn("Trying to add entity with duplicated UUID {}. Existing {}#{}, new: {}#{}", debug2, 
/*      */         
/*  853 */         EntityType.getKey(debug3.getType()), Integer.valueOf(debug3.getId()), 
/*  854 */         EntityType.getKey(debug1.getType()), Integer.valueOf(debug1.getId()));
/*  855 */     return true;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Entity findAddedOrPendingEntity(UUID debug1) {
/*  860 */     Entity debug2 = this.entitiesByUuid.get(debug1);
/*  861 */     if (debug2 != null) {
/*  862 */       return debug2;
/*      */     }
/*  864 */     if (this.tickingEntities) {
/*  865 */       for (Entity debug4 : this.toAddAfterTick) {
/*  866 */         if (debug4.getUUID().equals(debug1)) {
/*  867 */           return debug4;
/*      */         }
/*      */       } 
/*      */     }
/*  871 */     return null;
/*      */   }
/*      */   
/*      */   public boolean tryAddFreshEntityWithPassengers(Entity debug1) {
/*  875 */     if (debug1.getSelfAndPassengers().anyMatch(this::isUUIDUsed)) {
/*  876 */       return false;
/*      */     }
/*      */     
/*  879 */     addFreshEntityWithPassengers(debug1);
/*  880 */     return true;
/*      */   }
/*      */   
/*      */   public void unload(LevelChunk debug1) {
/*  884 */     this.blockEntitiesToUnload.addAll(debug1.getBlockEntities().values());
/*  885 */     for (ClassInstanceMultiMap<Entity> debug5 : debug1.getEntitySections()) {
/*  886 */       for (Entity debug7 : debug5) {
/*  887 */         if (debug7 instanceof ServerPlayer) {
/*      */           continue;
/*      */         }
/*  890 */         if (this.tickingEntities) {
/*  891 */           throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Removing entity while ticking!"));
/*      */         }
/*  893 */         this.entitiesById.remove(debug7.getId());
/*  894 */         onEntityRemoved(debug7);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onEntityRemoved(Entity debug1) {
/*  900 */     if (debug1 instanceof EnderDragon) {
/*  901 */       for (EnderDragonPart debug5 : ((EnderDragon)debug1).getSubEntities()) {
/*  902 */         debug5.remove();
/*      */       }
/*      */     }
/*  905 */     this.entitiesByUuid.remove(debug1.getUUID());
/*  906 */     getChunkSource().removeEntity(debug1);
/*  907 */     if (debug1 instanceof ServerPlayer) {
/*  908 */       ServerPlayer debug2 = (ServerPlayer)debug1;
/*  909 */       this.players.remove(debug2);
/*      */     } 
/*  911 */     getScoreboard().entityRemoved(debug1);
/*  912 */     if (debug1 instanceof Mob) {
/*  913 */       this.navigations.remove(((Mob)debug1).getNavigation());
/*      */     }
/*      */   }
/*      */   
/*      */   private void add(Entity debug1) {
/*  918 */     if (this.tickingEntities) {
/*  919 */       this.toAddAfterTick.add(debug1);
/*      */     } else {
/*  921 */       this.entitiesById.put(debug1.getId(), debug1);
/*  922 */       if (debug1 instanceof EnderDragon) {
/*  923 */         for (EnderDragonPart debug5 : ((EnderDragon)debug1).getSubEntities()) {
/*  924 */           this.entitiesById.put(debug5.getId(), debug5);
/*      */         }
/*      */       }
/*  927 */       this.entitiesByUuid.put(debug1.getUUID(), debug1);
/*  928 */       getChunkSource().addEntity(debug1);
/*  929 */       if (debug1 instanceof Mob) {
/*  930 */         this.navigations.add(((Mob)debug1).getNavigation());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void despawn(Entity debug1) {
/*  936 */     if (this.tickingEntities) {
/*  937 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Removing entity while ticking!"));
/*      */     }
/*  939 */     removeFromChunk(debug1);
/*  940 */     this.entitiesById.remove(debug1.getId());
/*  941 */     onEntityRemoved(debug1);
/*      */   }
/*      */   
/*      */   private void removeFromChunk(Entity debug1) {
/*  945 */     ChunkAccess debug2 = getChunk(debug1.xChunk, debug1.zChunk, ChunkStatus.FULL, false);
/*  946 */     if (debug2 instanceof LevelChunk) {
/*  947 */       ((LevelChunk)debug2).removeEntity(debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   public void removePlayerImmediately(ServerPlayer debug1) {
/*  952 */     debug1.remove();
/*  953 */     despawn((Entity)debug1);
/*  954 */     updateSleepingPlayerList();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBlockProgress(int debug1, BlockPos debug2, int debug3) {
/*  959 */     for (ServerPlayer debug5 : this.server.getPlayerList().getPlayers()) {
/*  960 */       if (debug5 == null || debug5.level != this || debug5.getId() == debug1) {
/*      */         continue;
/*      */       }
/*  963 */       double debug6 = debug2.getX() - debug5.getX();
/*  964 */       double debug8 = debug2.getY() - debug5.getY();
/*  965 */       double debug10 = debug2.getZ() - debug5.getZ();
/*      */       
/*  967 */       if (debug6 * debug6 + debug8 * debug8 + debug10 * debug10 < 1024.0D) {
/*  968 */         debug5.connection.send((Packet)new ClientboundBlockDestructionPacket(debug1, debug2, debug3));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSound(@Nullable Player debug1, double debug2, double debug4, double debug6, SoundEvent debug8, SoundSource debug9, float debug10, float debug11) {
/*  975 */     this.server.getPlayerList().broadcast(debug1, debug2, debug4, debug6, (debug10 > 1.0F) ? (16.0F * debug10) : 16.0D, dimension(), (Packet)new ClientboundSoundPacket(debug8, debug9, debug2, debug4, debug6, debug10, debug11));
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSound(@Nullable Player debug1, Entity debug2, SoundEvent debug3, SoundSource debug4, float debug5, float debug6) {
/*  980 */     this.server.getPlayerList().broadcast(debug1, debug2.getX(), debug2.getY(), debug2.getZ(), (debug5 > 1.0F) ? (16.0F * debug5) : 16.0D, dimension(), (Packet)new ClientboundSoundEntityPacket(debug3, debug4, debug2, debug5, debug6));
/*      */   }
/*      */ 
/*      */   
/*      */   public void globalLevelEvent(int debug1, BlockPos debug2, int debug3) {
/*  985 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundLevelEventPacket(debug1, debug2, debug3, true));
/*      */   }
/*      */ 
/*      */   
/*      */   public void levelEvent(@Nullable Player debug1, int debug2, BlockPos debug3, int debug4) {
/*  990 */     this.server.getPlayerList().broadcast(debug1, debug3.getX(), debug3.getY(), debug3.getZ(), 64.0D, dimension(), (Packet)new ClientboundLevelEventPacket(debug2, debug3, debug4, false));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendBlockUpdated(BlockPos debug1, BlockState debug2, BlockState debug3, int debug4) {
/*  995 */     getChunkSource().blockChanged(debug1);
/*  996 */     VoxelShape debug5 = debug2.getCollisionShape((BlockGetter)this, debug1);
/*  997 */     VoxelShape debug6 = debug3.getCollisionShape((BlockGetter)this, debug1);
/*  998 */     if (!Shapes.joinIsNotEmpty(debug5, debug6, BooleanOp.NOT_SAME)) {
/*      */       return;
/*      */     }
/*      */     
/* 1002 */     for (PathNavigation debug8 : this.navigations) {
/* 1003 */       if (debug8.hasDelayedRecomputation()) {
/*      */         continue;
/*      */       }
/*      */       
/* 1007 */       debug8.recomputePath(debug1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastEntityEvent(Entity debug1, byte debug2) {
/* 1013 */     getChunkSource().broadcastAndSend(debug1, (Packet<?>)new ClientboundEntityEventPacket(debug1, debug2));
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerChunkCache getChunkSource() {
/* 1018 */     return this.chunkSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Explosion explode(@Nullable Entity debug1, @Nullable DamageSource debug2, @Nullable ExplosionDamageCalculator debug3, double debug4, double debug6, double debug8, float debug10, boolean debug11, Explosion.BlockInteraction debug12) {
/* 1025 */     Explosion debug13 = new Explosion(this, debug1, debug2, debug3, debug4, debug6, debug8, debug10, debug11, debug12);
/* 1026 */     debug13.explode();
/* 1027 */     debug13.finalizeExplosion(false);
/*      */     
/* 1029 */     if (debug12 == Explosion.BlockInteraction.NONE) {
/* 1030 */       debug13.clearToBlow();
/*      */     }
/*      */     
/* 1033 */     for (ServerPlayer debug15 : this.players) {
/* 1034 */       if (debug15.distanceToSqr(debug4, debug6, debug8) < 4096.0D) {
/* 1035 */         debug15.connection.send((Packet)new ClientboundExplodePacket(debug4, debug6, debug8, debug10, debug13.getToBlow(), (Vec3)debug13.getHitPlayers().get(debug15)));
/*      */       }
/*      */     } 
/*      */     
/* 1039 */     return debug13;
/*      */   }
/*      */ 
/*      */   
/*      */   public void blockEvent(BlockPos debug1, Block debug2, int debug3, int debug4) {
/* 1044 */     this.blockEvents.add(new BlockEventData(debug1, debug2, debug3, debug4));
/*      */   }
/*      */   
/*      */   private void runBlockEvents() {
/* 1048 */     while (!this.blockEvents.isEmpty()) {
/* 1049 */       BlockEventData debug1 = (BlockEventData)this.blockEvents.removeFirst();
/* 1050 */       if (doBlockEvent(debug1)) {
/* 1051 */         this.server.getPlayerList().broadcast(null, debug1.getPos().getX(), debug1.getPos().getY(), debug1.getPos().getZ(), 64.0D, dimension(), (Packet)new ClientboundBlockEventPacket(debug1.getPos(), debug1.getBlock(), debug1.getParamA(), debug1.getParamB()));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean doBlockEvent(BlockEventData debug1) {
/* 1057 */     BlockState debug2 = getBlockState(debug1.getPos());
/* 1058 */     if (debug2.is(debug1.getBlock())) {
/* 1059 */       return debug2.triggerEvent(this, debug1.getPos(), debug1.getParamA(), debug1.getParamB());
/*      */     }
/* 1061 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerTickList<Block> getBlockTicks() {
/* 1066 */     return this.blockTicks;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerTickList<Fluid> getLiquidTicks() {
/* 1071 */     return this.liquidTicks;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MinecraftServer getServer() {
/* 1077 */     return this.server;
/*      */   }
/*      */   
/*      */   public PortalForcer getPortalForcer() {
/* 1081 */     return this.portalForcer;
/*      */   }
/*      */   
/*      */   public StructureManager getStructureManager() {
/* 1085 */     return this.server.getStructureManager();
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> int sendParticles(T debug1, double debug2, double debug4, double debug6, int debug8, double debug9, double debug11, double debug13, double debug15) {
/* 1089 */     ClientboundLevelParticlesPacket debug17 = new ClientboundLevelParticlesPacket((ParticleOptions)debug1, false, debug2, debug4, debug6, (float)debug9, (float)debug11, (float)debug13, (float)debug15, debug8);
/* 1090 */     int debug18 = 0;
/*      */     
/* 1092 */     for (int debug19 = 0; debug19 < this.players.size(); debug19++) {
/* 1093 */       ServerPlayer debug20 = this.players.get(debug19);
/*      */       
/* 1095 */       if (sendParticles(debug20, false, debug2, debug4, debug6, (Packet<?>)debug17)) {
/* 1096 */         debug18++;
/*      */       }
/*      */     } 
/*      */     
/* 1100 */     return debug18;
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> boolean sendParticles(ServerPlayer debug1, T debug2, boolean debug3, double debug4, double debug6, double debug8, int debug10, double debug11, double debug13, double debug15, double debug17) {
/* 1104 */     ClientboundLevelParticlesPacket clientboundLevelParticlesPacket = new ClientboundLevelParticlesPacket((ParticleOptions)debug2, debug3, debug4, debug6, debug8, (float)debug11, (float)debug13, (float)debug15, (float)debug17, debug10);
/*      */     
/* 1106 */     return sendParticles(debug1, debug3, debug4, debug6, debug8, (Packet<?>)clientboundLevelParticlesPacket);
/*      */   }
/*      */   
/*      */   private boolean sendParticles(ServerPlayer debug1, boolean debug2, double debug3, double debug5, double debug7, Packet<?> debug9) {
/* 1110 */     if (debug1.getLevel() != this) {
/* 1111 */       return false;
/*      */     }
/*      */     
/* 1114 */     BlockPos debug10 = debug1.blockPosition();
/*      */     
/* 1116 */     if (debug10.closerThan((Position)new Vec3(debug3, debug5, debug7), debug2 ? 512.0D : 32.0D)) {
/* 1117 */       debug1.connection.send(debug9);
/* 1118 */       return true;
/*      */     } 
/*      */     
/* 1121 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Entity getEntity(int debug1) {
/* 1127 */     return (Entity)this.entitiesById.get(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity getEntity(UUID debug1) {
/* 1132 */     return this.entitiesByUuid.get(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public BlockPos findNearestMapFeature(StructureFeature<?> debug1, BlockPos debug2, int debug3, boolean debug4) {
/* 1137 */     if (!this.server.getWorldData().worldGenSettings().generateFeatures()) {
/* 1138 */       return null;
/*      */     }
/* 1140 */     return getChunkSource().getGenerator().findNearestMapFeature(this, debug1, debug2, debug3, debug4);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public BlockPos findNearestBiome(Biome debug1, BlockPos debug2, int debug3, int debug4) {
/* 1145 */     return getChunkSource().getGenerator().getBiomeSource().findBiomeHorizontal(debug2.getX(), debug2.getY(), debug2.getZ(), debug3, debug4, debug1 -> (debug1 == debug0), this.random, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public RecipeManager getRecipeManager() {
/* 1150 */     return this.server.getRecipeManager();
/*      */   }
/*      */ 
/*      */   
/*      */   public TagContainer getTagManager() {
/* 1155 */     return this.server.getTags();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean noSave() {
/* 1160 */     return this.noSave;
/*      */   }
/*      */ 
/*      */   
/*      */   public RegistryAccess registryAccess() {
/* 1165 */     return this.server.registryAccess();
/*      */   }
/*      */   
/*      */   public DimensionDataStorage getDataStorage() {
/* 1169 */     return getChunkSource().getDataStorage();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public MapItemSavedData getMapData(String debug1) {
/* 1175 */     return (MapItemSavedData)getServer().overworld().getDataStorage().get(() -> new MapItemSavedData(debug0), debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMapData(MapItemSavedData debug1) {
/* 1180 */     getServer().overworld().getDataStorage().set((SavedData)debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFreeMapId() {
/* 1185 */     return ((MapIndex)getServer().overworld().getDataStorage().computeIfAbsent(MapIndex::new, "idcounts")).getFreeAuxValueForMap();
/*      */   }
/*      */   
/*      */   public void setDefaultSpawnPos(BlockPos debug1, float debug2) {
/* 1189 */     ChunkPos debug3 = new ChunkPos(new BlockPos(this.levelData.getXSpawn(), 0, this.levelData.getZSpawn()));
/* 1190 */     this.levelData.setSpawn(debug1, debug2);
/* 1191 */     getChunkSource().removeRegionTicket(TicketType.START, debug3, 11, Unit.INSTANCE);
/* 1192 */     getChunkSource().addRegionTicket(TicketType.START, new ChunkPos(debug1), 11, Unit.INSTANCE);
/* 1193 */     getServer().getPlayerList().broadcastAll((Packet)new ClientboundSetDefaultSpawnPositionPacket(debug1, debug2));
/*      */   }
/*      */   
/*      */   public BlockPos getSharedSpawnPos() {
/* 1197 */     BlockPos debug1 = new BlockPos(this.levelData.getXSpawn(), this.levelData.getYSpawn(), this.levelData.getZSpawn());
/* 1198 */     if (!getWorldBorder().isWithinBounds(debug1)) {
/* 1199 */       debug1 = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ()));
/*      */     }
/* 1201 */     return debug1;
/*      */   }
/*      */   
/*      */   public float getSharedSpawnAngle() {
/* 1205 */     return this.levelData.getSpawnAngle();
/*      */   }
/*      */   
/*      */   public LongSet getForcedChunks() {
/* 1209 */     ForcedChunksSavedData debug1 = (ForcedChunksSavedData)getDataStorage().get(ForcedChunksSavedData::new, "chunks");
/* 1210 */     return (debug1 != null) ? LongSets.unmodifiable(debug1.getChunks()) : (LongSet)LongSets.EMPTY_SET;
/*      */   }
/*      */   public boolean setChunkForced(int debug1, int debug2, boolean debug3) {
/*      */     boolean debug8;
/* 1214 */     ForcedChunksSavedData debug4 = (ForcedChunksSavedData)getDataStorage().computeIfAbsent(ForcedChunksSavedData::new, "chunks");
/*      */     
/* 1216 */     ChunkPos debug5 = new ChunkPos(debug1, debug2);
/* 1217 */     long debug6 = debug5.toLong();
/*      */     
/* 1219 */     if (debug3) {
/* 1220 */       debug8 = debug4.getChunks().add(debug6);
/* 1221 */       if (debug8) {
/* 1222 */         getChunk(debug1, debug2);
/*      */       }
/*      */     } else {
/* 1225 */       debug8 = debug4.getChunks().remove(debug6);
/*      */     } 
/* 1227 */     debug4.setDirty(debug8);
/* 1228 */     if (debug8) {
/* 1229 */       getChunkSource().updateChunkForced(debug5, debug3);
/*      */     }
/* 1231 */     return debug8;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ServerPlayer> players() {
/* 1236 */     return this.players;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onBlockStateChange(BlockPos debug1, BlockState debug2, BlockState debug3) {
/* 1241 */     Optional<PoiType> debug4 = PoiType.forState(debug2);
/* 1242 */     Optional<PoiType> debug5 = PoiType.forState(debug3);
/*      */     
/* 1244 */     if (Objects.equals(debug4, debug5)) {
/*      */       return;
/*      */     }
/*      */     
/* 1248 */     BlockPos debug6 = debug1.immutable();
/* 1249 */     debug4.ifPresent(debug2 -> getServer().execute(()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1254 */     debug5.ifPresent(debug2 -> getServer().execute(()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PoiManager getPoiManager() {
/* 1261 */     return getChunkSource().getPoiManager();
/*      */   }
/*      */   
/*      */   public boolean isVillage(BlockPos debug1) {
/* 1265 */     return isCloseToVillage(debug1, 1);
/*      */   }
/*      */   
/*      */   public boolean isVillage(SectionPos debug1) {
/* 1269 */     return isVillage(debug1.center());
/*      */   }
/*      */   
/*      */   public boolean isCloseToVillage(BlockPos debug1, int debug2) {
/* 1273 */     if (debug2 > 6) {
/* 1274 */       return false;
/*      */     }
/* 1276 */     return (sectionsToVillage(SectionPos.of(debug1)) <= debug2);
/*      */   }
/*      */   
/*      */   public int sectionsToVillage(SectionPos debug1) {
/* 1280 */     return getPoiManager().sectionsToVillage(debug1);
/*      */   }
/*      */   
/*      */   public Raids getRaids() {
/* 1284 */     return this.raids;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Raid getRaidAt(BlockPos debug1) {
/* 1292 */     return this.raids.getNearbyRaid(debug1, 9216);
/*      */   }
/*      */   
/*      */   public boolean isRaided(BlockPos debug1) {
/* 1296 */     return (getRaidAt(debug1) != null);
/*      */   }
/*      */   
/*      */   public void onReputationEvent(ReputationEventType debug1, Entity debug2, ReputationEventHandler debug3) {
/* 1300 */     debug3.onReputationEventFrom(debug1, debug2);
/*      */   }
/*      */   
/*      */   public void saveDebugReport(Path debug1) throws IOException {
/* 1304 */     ChunkMap debug2 = (getChunkSource()).chunkMap;
/*      */     
/* 1306 */     try (Writer debug3 = Files.newBufferedWriter(debug1.resolve("stats.txt"), new java.nio.file.OpenOption[0])) {
/* 1307 */       writer1.write(String.format("spawning_chunks: %d\n", new Object[] { Integer.valueOf(debug2.getDistanceManager().getNaturalSpawnChunkCount()) }));
/* 1308 */       NaturalSpawner.SpawnState spawnState = getChunkSource().getLastSpawnState();
/* 1309 */       if (spawnState != null) {
/* 1310 */         for (ObjectIterator<Object2IntMap.Entry<MobCategory>> objectIterator = spawnState.getMobCategoryCounts().object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<MobCategory> entry = objectIterator.next();
/* 1311 */           writer1.write(String.format("spawn_count.%s: %d\n", new Object[] { ((MobCategory)entry.getKey()).getName(), Integer.valueOf(entry.getIntValue()) })); }
/*      */       
/*      */       }
/* 1314 */       writer1.write(String.format("entities: %d\n", new Object[] { Integer.valueOf(this.entitiesById.size()) }));
/* 1315 */       writer1.write(String.format("block_entities: %d\n", new Object[] { Integer.valueOf(this.blockEntityList.size()) }));
/* 1316 */       writer1.write(String.format("block_ticks: %d\n", new Object[] { Integer.valueOf(getBlockTicks().size()) }));
/* 1317 */       writer1.write(String.format("fluid_ticks: %d\n", new Object[] { Integer.valueOf(getLiquidTicks().size()) }));
/* 1318 */       writer1.write("distance_manager: " + debug2.getDistanceManager().getDebugStatus() + "\n");
/* 1319 */       writer1.write(String.format("pending_tasks: %d\n", new Object[] { Integer.valueOf(getChunkSource().getPendingTasksCount()) }));
/*      */     } 
/*      */     
/* 1322 */     CrashReport debug3 = new CrashReport("Level dump", new Exception("dummy"));
/* 1323 */     fillReportDetails(debug3);
/* 1324 */     try (Writer debug4 = Files.newBufferedWriter(debug1.resolve("example_crash.txt"), new java.nio.file.OpenOption[0])) {
/* 1325 */       writer2.write(debug3.getFriendlyReport());
/*      */     } 
/*      */     
/* 1328 */     Path debug4 = debug1.resolve("chunks.csv");
/* 1329 */     try (Writer debug5 = Files.newBufferedWriter(debug4, new java.nio.file.OpenOption[0])) {
/* 1330 */       debug2.dumpChunks(writer3);
/*      */     } 
/*      */     
/* 1333 */     Path debug5 = debug1.resolve("entities.csv");
/* 1334 */     try (Writer debug6 = Files.newBufferedWriter(debug5, new java.nio.file.OpenOption[0])) {
/* 1335 */       dumpEntities(writer4, (Iterable<Entity>)this.entitiesById.values());
/*      */     } 
/*      */     
/* 1338 */     Path debug6 = debug1.resolve("block_entities.csv");
/* 1339 */     try (Writer debug7 = Files.newBufferedWriter(debug6, new java.nio.file.OpenOption[0])) {
/* 1340 */       dumpBlockEntities(debug7);
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
/*      */   private static void dumpEntities(Writer debug0, Iterable<Entity> debug1) throws IOException {
/* 1354 */     CsvOutput debug2 = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("uuid").addColumn("type").addColumn("alive").addColumn("display_name").addColumn("custom_name").build(debug0);
/*      */     
/* 1356 */     for (Entity debug4 : debug1) {
/* 1357 */       Component debug5 = debug4.getCustomName();
/* 1358 */       Component debug6 = debug4.getDisplayName();
/* 1359 */       debug2.writeRow(new Object[] {
/* 1360 */             Double.valueOf(debug4.getX()), 
/* 1361 */             Double.valueOf(debug4.getY()), 
/* 1362 */             Double.valueOf(debug4.getZ()), debug4
/* 1363 */             .getUUID(), Registry.ENTITY_TYPE
/* 1364 */             .getKey(debug4.getType()), 
/* 1365 */             Boolean.valueOf(debug4.isAlive()), debug6
/* 1366 */             .getString(), (debug5 != null) ? debug5
/* 1367 */             .getString() : null
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void dumpBlockEntities(Writer debug1) throws IOException {
/* 1378 */     CsvOutput debug2 = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("type").build(debug1);
/*      */     
/* 1380 */     for (BlockEntity debug4 : this.blockEntityList) {
/* 1381 */       BlockPos debug5 = debug4.getBlockPos();
/* 1382 */       debug2.writeRow(new Object[] {
/* 1383 */             Integer.valueOf(debug5.getX()), 
/* 1384 */             Integer.valueOf(debug5.getY()), 
/* 1385 */             Integer.valueOf(debug5.getZ()), Registry.BLOCK_ENTITY_TYPE
/* 1386 */             .getKey(debug4.getType())
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void clearBlockEvents(BoundingBox debug1) {
/* 1393 */     this.blockEvents.removeIf(debug1 -> debug0.isInside((Vec3i)debug1.getPos()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void blockUpdated(BlockPos debug1, Block debug2) {
/* 1398 */     if (!isDebug()) {
/* 1399 */       updateNeighborsAt(debug1, debug2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<Entity> getAllEntities() {
/* 1409 */     return Iterables.unmodifiableIterable((Iterable)this.entitiesById.values());
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1414 */     return "ServerLevel[" + this.serverLevelData.getLevelName() + "]";
/*      */   }
/*      */   
/*      */   public boolean isFlat() {
/* 1418 */     return this.server.getWorldData().worldGenSettings().isFlatWorld();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getSeed() {
/* 1423 */     return this.server.getWorldData().worldGenSettings().seed();
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public EndDragonFight dragonFight() {
/* 1428 */     return this.dragonFight;
/*      */   }
/*      */ 
/*      */   
/*      */   public Stream<? extends StructureStart<?>> startsForFeature(SectionPos debug1, StructureFeature<?> debug2) {
/* 1433 */     return structureFeatureManager().startsForFeature(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerLevel getLevel() {
/* 1438 */     return this;
/*      */   }
/*      */   
/*      */   public static void makeObsidianPlatform(ServerLevel debug0) {
/* 1442 */     BlockPos debug1 = END_SPAWN_POINT;
/* 1443 */     int debug2 = debug1.getX();
/* 1444 */     int debug3 = debug1.getY() - 2;
/* 1445 */     int debug4 = debug1.getZ();
/* 1446 */     BlockPos.betweenClosed(debug2 - 2, debug3 + 1, debug4 - 2, debug2 + 2, debug3 + 3, debug4 + 2)
/* 1447 */       .forEach(debug1 -> debug0.setBlockAndUpdate(debug1, Blocks.AIR.defaultBlockState()));
/* 1448 */     BlockPos.betweenClosed(debug2 - 2, debug3, debug4 - 2, debug2 + 2, debug3, debug4 + 2)
/* 1449 */       .forEach(debug1 -> debug0.setBlockAndUpdate(debug1, Blocks.OBSIDIAN.defaultBlockState()));
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */