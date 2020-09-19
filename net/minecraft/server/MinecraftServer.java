/*      */ package net.minecraft.server;
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.GameProfileRepository;
/*      */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.io.Writer;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.ThreadInfo;
/*      */ import java.lang.management.ThreadMXBean;
/*      */ import java.net.Proxy;
/*      */ import java.net.URLEncoder;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.KeyPair;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.MappedRegistry;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.data.worldgen.Features;
/*      */ import net.minecraft.gametest.framework.GameTestTicker;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.TextComponent;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*      */ import net.minecraft.network.protocol.status.ServerStatus;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.bossevents.CustomBossEvents;
/*      */ import net.minecraft.server.level.PlayerRespawnLogic;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.TicketType;
/*      */ import net.minecraft.server.level.progress.ChunkProgressListener;
/*      */ import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
/*      */ import net.minecraft.server.network.ServerConnectionListener;
/*      */ import net.minecraft.server.packs.PackResources;
/*      */ import net.minecraft.server.packs.repository.Pack;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.server.players.GameProfileCache;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.server.players.ServerOpListEntry;
/*      */ import net.minecraft.server.players.UserWhiteList;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.TagContainer;
/*      */ import net.minecraft.util.FrameTimer;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProgressListener;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.profiling.ContinuousProfiler;
/*      */ import net.minecraft.util.profiling.InactiveProfiler;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.SingleTickProfiler;
/*      */ import net.minecraft.util.thread.ReentrantBlockableEventLoop;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.Snooper;
/*      */ import net.minecraft.world.SnooperPopulator;
/*      */ import net.minecraft.world.entity.ai.village.VillageSiege;
/*      */ import net.minecraft.world.entity.npc.WanderingTraderSpawner;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.DataPackConfig;
/*      */ import net.minecraft.world.level.ForcedChunksSavedData;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelSettings;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.biome.BiomeSource;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.border.BorderChangeListener;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.dimension.LevelStem;
/*      */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.PatrolSpawner;
/*      */ import net.minecraft.world.level.levelgen.PhantomSpawner;
/*      */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*      */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ import net.minecraft.world.level.saveddata.SaveDataDirtyRunnable;
/*      */ import net.minecraft.world.level.saveddata.SavedData;
/*      */ import net.minecraft.world.level.storage.CommandStorage;
/*      */ import net.minecraft.world.level.storage.DerivedLevelData;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.LevelResource;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.level.storage.WorldData;
/*      */ import net.minecraft.world.level.storage.loot.LootTables;
/*      */ import net.minecraft.world.level.storage.loot.PredicateManager;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.ScoreboardSaveData;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public abstract class MinecraftServer extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable {
/*  153 */   private static final Logger LOGGER = LogManager.getLogger();
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
/*  165 */   public static final File USERID_CACHE_FILE = new File("usercache.json");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  175 */   public static final LevelSettings DEMO_SETTINGS = new LevelSettings("Demo World", GameType.SURVIVAL, false, Difficulty.NORMAL, false, new GameRules(), DataPackConfig.DEFAULT);
/*      */   
/*      */   protected final LevelStorageSource.LevelStorageAccess storageSource;
/*      */   
/*      */   protected final PlayerDataStorage playerDataStorage;
/*  180 */   private final Snooper snooper = new Snooper("server", this, Util.getMillis());
/*      */   
/*  182 */   private final List<Runnable> tickables = Lists.newArrayList();
/*  183 */   private final ContinuousProfiler continousProfiler = new ContinuousProfiler(Util.timeSource, this::getTickCount);
/*  184 */   private ProfilerFiller profiler = (ProfilerFiller)InactiveProfiler.INSTANCE;
/*      */   
/*      */   private final ServerConnectionListener connection;
/*      */   private final ChunkProgressListenerFactory progressListenerFactory;
/*  188 */   private final ServerStatus status = new ServerStatus();
/*  189 */   private final Random random = new Random();
/*      */   
/*      */   private final DataFixer fixerUpper;
/*      */   private String localIp;
/*  193 */   private int port = -1;
/*      */   protected final RegistryAccess.RegistryHolder registryHolder;
/*  195 */   private final Map<ResourceKey<Level>, ServerLevel> levels = Maps.newLinkedHashMap();
/*      */   
/*      */   private PlayerList playerList;
/*      */   
/*      */   private volatile boolean running = true;
/*      */   private boolean stopped;
/*      */   private int tickCount;
/*      */   protected final Proxy proxy;
/*      */   private boolean onlineMode;
/*      */   private boolean preventProxyConnections;
/*      */   private boolean pvp;
/*      */   private boolean allowFlight;
/*      */   @Nullable
/*      */   private String motd;
/*      */   private int maxBuildHeight;
/*      */   private int playerIdleTimeout;
/*  211 */   public final long[] tickTimes = new long[100];
/*      */   
/*      */   @Nullable
/*      */   private KeyPair keyPair;
/*      */   @Nullable
/*      */   private String singleplayerName;
/*      */   private boolean isDemo;
/*  218 */   private String resourcePack = "";
/*  219 */   private String resourcePackHash = "";
/*      */   private volatile boolean isReady;
/*      */   private long lastOverloadWarning;
/*      */   private boolean delayProfilerStart;
/*      */   private boolean forceGameType;
/*      */   private final MinecraftSessionService sessionService;
/*      */   private final GameProfileRepository profileRepository;
/*      */   private final GameProfileCache profileCache;
/*      */   private long lastServerStatus;
/*      */   private final Thread serverThread;
/*  229 */   private long nextTickTime = Util.getMillis();
/*      */   
/*      */   private long delayedTasksMaxNextTickTime;
/*      */   private boolean mayHaveDelayedTasks;
/*      */   private final PackRepository packRepository;
/*  234 */   private final ServerScoreboard scoreboard = new ServerScoreboard(this);
/*      */   @Nullable
/*      */   private CommandStorage commandStorage;
/*  237 */   private final CustomBossEvents customBossEvents = new CustomBossEvents();
/*      */   
/*      */   private final ServerFunctionManager functionManager;
/*  240 */   private final FrameTimer frameTimer = new FrameTimer();
/*      */   
/*      */   private boolean enforceWhitelist;
/*      */   private float averageTickTime;
/*      */   private final Executor executor;
/*      */   @Nullable
/*      */   private String serverId;
/*      */   private ServerResources resources;
/*      */   private final StructureManager structureManager;
/*      */   protected final WorldData worldData;
/*      */   
/*      */   public static <S extends MinecraftServer> S spin(Function<Thread, S> debug0) {
/*  252 */     AtomicReference<S> debug1 = new AtomicReference<>();
/*      */     
/*  254 */     Thread debug2 = new Thread(() -> ((MinecraftServer)debug0.get()).runServer(), "Server thread");
/*  255 */     debug2.setUncaughtExceptionHandler((debug0, debug1) -> LOGGER.error(debug1));
/*      */     
/*  257 */     MinecraftServer minecraftServer = (MinecraftServer)debug0.apply(debug2);
/*  258 */     debug1.set((S)minecraftServer);
/*  259 */     debug2.start();
/*  260 */     return (S)minecraftServer;
/*      */   }
/*      */   
/*      */   public MinecraftServer(Thread debug1, RegistryAccess.RegistryHolder debug2, LevelStorageSource.LevelStorageAccess debug3, WorldData debug4, PackRepository debug5, Proxy debug6, DataFixer debug7, ServerResources debug8, MinecraftSessionService debug9, GameProfileRepository debug10, GameProfileCache debug11, ChunkProgressListenerFactory debug12) {
/*  264 */     super("Server");
/*  265 */     this.registryHolder = debug2;
/*  266 */     this.worldData = debug4;
/*  267 */     this.proxy = debug6;
/*  268 */     this.packRepository = debug5;
/*  269 */     this.resources = debug8;
/*  270 */     this.sessionService = debug9;
/*  271 */     this.profileRepository = debug10;
/*  272 */     this.profileCache = debug11;
/*  273 */     this.connection = new ServerConnectionListener(this);
/*  274 */     this.progressListenerFactory = debug12;
/*  275 */     this.storageSource = debug3;
/*  276 */     this.playerDataStorage = debug3.createPlayerStorage();
/*  277 */     this.fixerUpper = debug7;
/*  278 */     this.functionManager = new ServerFunctionManager(this, debug8.getFunctionLibrary());
/*  279 */     this.structureManager = new StructureManager(debug8.getResourceManager(), debug3, debug7);
/*  280 */     this.serverThread = debug1;
/*  281 */     this.executor = Util.backgroundExecutor();
/*      */   }
/*      */   
/*      */   private void readScoreboard(DimensionDataStorage debug1) {
/*  285 */     ScoreboardSaveData debug2 = (ScoreboardSaveData)debug1.computeIfAbsent(ScoreboardSaveData::new, "scoreboard");
/*  286 */     debug2.setScoreboard(getScoreboard());
/*  287 */     getScoreboard().addDirtyListener((Runnable)new SaveDataDirtyRunnable((SavedData)debug2));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void convertFromRegionFormatIfNeeded(LevelStorageSource.LevelStorageAccess debug0) {
/*  293 */     if (debug0.requiresConversion()) {
/*  294 */       LOGGER.info("Converting map!");
/*  295 */       debug0.convertLevel(new ProgressListener() {
/*  296 */             private long timeStamp = Util.getMillis();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public void progressStartNoAbort(Component debug1) {}
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public void progressStagePercentage(int debug1) {
/*  308 */               if (Util.getMillis() - this.timeStamp >= 1000L) {
/*  309 */                 this.timeStamp = Util.getMillis();
/*  310 */                 MinecraftServer.LOGGER.info("Converting... {}%", Integer.valueOf(debug1));
/*      */               } 
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public void progressStage(Component debug1) {}
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void loadLevel() {
/*  326 */     detectBundledResources();
/*      */     
/*  328 */     this.worldData.setModdedInfo(getServerModName(), getModdedStatus().isPresent());
/*      */     
/*  330 */     ChunkProgressListener debug1 = this.progressListenerFactory.create(11);
/*      */     
/*  332 */     createLevels(debug1);
/*      */     
/*  334 */     forceDifficulty();
/*  335 */     prepareLevels(debug1);
/*      */   }
/*      */   protected void forceDifficulty() {}
/*      */   
/*      */   protected void createLevels(ChunkProgressListener debug1) {
/*      */     ChunkGenerator debug11;
/*      */     DimensionType debug13;
/*  342 */     ServerLevelData debug2 = this.worldData.overworldData();
/*  343 */     WorldGenSettings debug3 = this.worldData.worldGenSettings();
/*  344 */     boolean debug4 = debug3.isDebug();
/*  345 */     long debug5 = debug3.seed();
/*  346 */     long debug7 = BiomeManager.obfuscateSeed(debug5);
/*  347 */     ImmutableList immutableList = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(debug2));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  354 */     MappedRegistry<LevelStem> debug10 = debug3.dimensions();
/*      */     
/*  356 */     LevelStem debug12 = (LevelStem)debug10.get(LevelStem.OVERWORLD);
/*      */     
/*  358 */     if (debug12 == null) {
/*  359 */       debug13 = (DimensionType)this.registryHolder.dimensionTypes().getOrThrow(DimensionType.OVERWORLD_LOCATION);
/*  360 */       NoiseBasedChunkGenerator noiseBasedChunkGenerator = WorldGenSettings.makeDefaultOverworld((Registry)this.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), (Registry)this.registryHolder.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY), (new Random()).nextLong());
/*      */     } else {
/*  362 */       debug13 = debug12.type();
/*  363 */       debug11 = debug12.generator();
/*      */     } 
/*  365 */     ServerLevel debug14 = new ServerLevel(this, this.executor, this.storageSource, debug2, Level.OVERWORLD, debug13, debug1, debug11, debug4, debug7, (List)immutableList, true);
/*  366 */     this.levels.put(Level.OVERWORLD, debug14);
/*  367 */     DimensionDataStorage debug15 = debug14.getDataStorage();
/*  368 */     readScoreboard(debug15);
/*  369 */     this.commandStorage = new CommandStorage(debug15);
/*  370 */     WorldBorder debug16 = debug14.getWorldBorder();
/*  371 */     debug16.applySettings(debug2.getWorldBorder());
/*      */     
/*  373 */     if (!debug2.isInitialized()) {
/*      */       try {
/*  375 */         setInitialSpawn(debug14, debug2, debug3.generateBonusChest(), debug4, true);
/*  376 */         debug2.setInitialized(true);
/*  377 */         if (debug4) {
/*  378 */           setupDebugLevel(this.worldData);
/*      */         }
/*  380 */       } catch (Throwable debug17) {
/*  381 */         CrashReport debug18 = CrashReport.forThrowable(debug17, "Exception initializing level");
/*      */         try {
/*  383 */           debug14.fillReportDetails(debug18);
/*  384 */         } catch (Throwable throwable) {}
/*      */ 
/*      */         
/*  387 */         throw new ReportedException(debug18);
/*      */       } 
/*  389 */       debug2.setInitialized(true);
/*      */     } 
/*      */     
/*  392 */     getPlayerList().setLevel(debug14);
/*      */     
/*  394 */     if (this.worldData.getCustomBossEvents() != null) {
/*  395 */       getCustomBossEvents().load(this.worldData.getCustomBossEvents());
/*      */     }
/*      */     
/*  398 */     for (Map.Entry<ResourceKey<LevelStem>, LevelStem> debug18 : (Iterable<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)debug10.entrySet()) {
/*  399 */       ResourceKey<LevelStem> debug19 = debug18.getKey();
/*  400 */       if (debug19 == LevelStem.OVERWORLD) {
/*      */         continue;
/*      */       }
/*  403 */       ResourceKey<Level> debug20 = ResourceKey.create(Registry.DIMENSION_REGISTRY, debug19.location());
/*  404 */       DimensionType debug21 = ((LevelStem)debug18.getValue()).type();
/*  405 */       ChunkGenerator debug22 = ((LevelStem)debug18.getValue()).generator();
/*  406 */       DerivedLevelData debug23 = new DerivedLevelData(this.worldData, debug2);
/*  407 */       ServerLevel debug24 = new ServerLevel(this, this.executor, this.storageSource, (ServerLevelData)debug23, debug20, debug21, debug1, debug22, debug4, debug7, (List)ImmutableList.of(), false);
/*  408 */       debug16.addListener((BorderChangeListener)new BorderChangeListener.DelegateBorderChangeListener(debug24.getWorldBorder()));
/*  409 */       this.levels.put(debug20, debug24);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setInitialSpawn(ServerLevel debug0, ServerLevelData debug1, boolean debug2, boolean debug3, boolean debug4) {
/*  414 */     ChunkGenerator debug5 = debug0.getChunkSource().getGenerator();
/*  415 */     if (!debug4) {
/*  416 */       debug1.setSpawn(BlockPos.ZERO.above(debug5.getSpawnHeight()), 0.0F);
/*      */       
/*      */       return;
/*      */     } 
/*  420 */     if (debug3) {
/*  421 */       debug1.setSpawn(BlockPos.ZERO.above(), 0.0F);
/*      */       
/*      */       return;
/*      */     } 
/*  425 */     BiomeSource debug6 = debug5.getBiomeSource();
/*  426 */     Random debug7 = new Random(debug0.getSeed());
/*      */     
/*  428 */     BlockPos debug8 = debug6.findBiomeHorizontal(0, debug0.getSeaLevel(), 0, 256, debug0 -> debug0.getMobSettings().playerSpawnFriendly(), debug7);
/*  429 */     ChunkPos debug9 = (debug8 == null) ? new ChunkPos(0, 0) : new ChunkPos(debug8);
/*      */     
/*  431 */     if (debug8 == null) {
/*  432 */       LOGGER.warn("Unable to find spawn biome");
/*      */     }
/*      */     
/*  435 */     boolean debug10 = false;
/*  436 */     for (Block block : BlockTags.VALID_SPAWN.getValues()) {
/*  437 */       if (debug6.getSurfaceBlocks().contains(block.defaultBlockState())) {
/*  438 */         debug10 = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  443 */     debug1.setSpawn(debug9.getWorldPosition().offset(8, debug5.getSpawnHeight(), 8), 0.0F);
/*      */     
/*  445 */     int debug11 = 0;
/*  446 */     int debug12 = 0;
/*  447 */     int debug13 = 0;
/*  448 */     int debug14 = -1;
/*  449 */     int debug15 = 32;
/*  450 */     for (int debug16 = 0; debug16 < 1024; debug16++) {
/*  451 */       if (debug11 > -16 && debug11 <= 16 && debug12 > -16 && debug12 <= 16) {
/*  452 */         BlockPos debug17 = PlayerRespawnLogic.getSpawnPosInChunk(debug0, new ChunkPos(debug9.x + debug11, debug9.z + debug12), debug10);
/*  453 */         if (debug17 != null) {
/*  454 */           debug1.setSpawn(debug17, 0.0F);
/*      */           break;
/*      */         } 
/*      */       } 
/*  458 */       if (debug11 == debug12 || (debug11 < 0 && debug11 == -debug12) || (debug11 > 0 && debug11 == 1 - debug12)) {
/*  459 */         int debug17 = debug13;
/*  460 */         debug13 = -debug14;
/*  461 */         debug14 = debug17;
/*      */       } 
/*  463 */       debug11 += debug13;
/*  464 */       debug12 += debug14;
/*      */     } 
/*      */     
/*  467 */     if (debug2) {
/*      */ 
/*      */       
/*  470 */       ConfiguredFeature<?, ?> configuredFeature = Features.BONUS_CHEST;
/*  471 */       configuredFeature.place((WorldGenLevel)debug0, debug5, debug0.random, new BlockPos(debug1.getXSpawn(), debug1.getYSpawn(), debug1.getZSpawn()));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupDebugLevel(WorldData debug1) {
/*  476 */     debug1.setDifficulty(Difficulty.PEACEFUL);
/*  477 */     debug1.setDifficultyLocked(true);
/*      */     
/*  479 */     ServerLevelData debug2 = debug1.overworldData();
/*  480 */     debug2.setRaining(false);
/*  481 */     debug2.setThundering(false);
/*  482 */     debug2.setClearWeatherTime(1000000000);
/*  483 */     debug2.setDayTime(6000L);
/*  484 */     debug2.setGameType(GameType.SPECTATOR);
/*      */   }
/*      */   
/*      */   private void prepareLevels(ChunkProgressListener debug1) {
/*  488 */     ServerLevel debug2 = overworld();
/*  489 */     LOGGER.info("Preparing start region for dimension {}", debug2.dimension().location());
/*  490 */     BlockPos debug3 = debug2.getSharedSpawnPos();
/*  491 */     debug1.updateSpawnPos(new ChunkPos(debug3));
/*      */     
/*  493 */     ServerChunkCache debug4 = debug2.getChunkSource();
/*      */     
/*  495 */     debug4.getLightEngine().setTaskPerBatch(500);
/*  496 */     this.nextTickTime = Util.getMillis();
/*      */     
/*  498 */     debug4.addRegionTicket(TicketType.START, new ChunkPos(debug3), 11, Unit.INSTANCE);
/*      */     
/*  500 */     while (debug4.getTickingGenerated() != 441) {
/*  501 */       this.nextTickTime = Util.getMillis() + 10L;
/*  502 */       waitUntilNextTick();
/*      */     } 
/*      */     
/*  505 */     this.nextTickTime = Util.getMillis() + 10L;
/*  506 */     waitUntilNextTick();
/*      */     
/*  508 */     for (ServerLevel debug6 : this.levels.values()) {
/*  509 */       ForcedChunksSavedData debug7 = (ForcedChunksSavedData)debug6.getDataStorage().get(ForcedChunksSavedData::new, "chunks");
/*  510 */       if (debug7 != null) {
/*  511 */         LongIterator debug8 = debug7.getChunks().iterator();
/*  512 */         while (debug8.hasNext()) {
/*  513 */           long debug9 = debug8.nextLong();
/*  514 */           ChunkPos debug11 = new ChunkPos(debug9);
/*  515 */           debug6.getChunkSource().updateChunkForced(debug11, true);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  520 */     this.nextTickTime = Util.getMillis() + 10L;
/*  521 */     waitUntilNextTick();
/*      */ 
/*      */     
/*  524 */     debug1.stop();
/*      */ 
/*      */     
/*  527 */     debug4.getLightEngine().setTaskPerBatch(5);
/*      */     
/*  529 */     updateMobSpawningFlags();
/*      */   }
/*      */   
/*      */   protected void detectBundledResources() {
/*  533 */     File debug1 = this.storageSource.getLevelPath(LevelResource.MAP_RESOURCE_FILE).toFile();
/*  534 */     if (debug1.isFile()) {
/*  535 */       String debug2 = this.storageSource.getLevelId();
/*      */       try {
/*  537 */         setResourcePack("level://" + URLEncoder.encode(debug2, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
/*  538 */       } catch (UnsupportedEncodingException debug3) {
/*  539 */         LOGGER.warn("Something went wrong url encoding {}", debug2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public GameType getDefaultGameType() {
/*  545 */     return this.worldData.getGameType();
/*      */   }
/*      */   
/*      */   public boolean isHardcore() {
/*  549 */     return this.worldData.isHardcore();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean saveAllChunks(boolean debug1, boolean debug2, boolean debug3) {
/*  559 */     boolean debug4 = false;
/*  560 */     for (ServerLevel serverLevel : getAllLevels()) {
/*  561 */       if (!debug1) {
/*  562 */         LOGGER.info("Saving chunks for level '{}'/{}", serverLevel, serverLevel.dimension().location());
/*      */       }
/*  564 */       serverLevel.save(null, debug2, (serverLevel.noSave && !debug3));
/*  565 */       debug4 = true;
/*      */     } 
/*  567 */     ServerLevel debug5 = overworld();
/*  568 */     ServerLevelData debug6 = this.worldData.overworldData();
/*  569 */     debug6.setWorldBorder(debug5.getWorldBorder().createSettings());
/*  570 */     this.worldData.setCustomBossEvents(getCustomBossEvents().save());
/*  571 */     this.storageSource.saveDataTag((RegistryAccess)this.registryHolder, this.worldData, getPlayerList().getSingleplayerData());
/*  572 */     return debug4;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/*  577 */     stopServer();
/*      */   }
/*      */   
/*      */   protected void stopServer() {
/*  581 */     LOGGER.info("Stopping server");
/*  582 */     if (getConnection() != null) {
/*  583 */       getConnection().stop();
/*      */     }
/*  585 */     if (this.playerList != null) {
/*  586 */       LOGGER.info("Saving players");
/*  587 */       this.playerList.saveAll();
/*  588 */       this.playerList.removeAll();
/*      */     } 
/*  590 */     LOGGER.info("Saving worlds");
/*  591 */     for (ServerLevel debug2 : getAllLevels()) {
/*  592 */       if (debug2 != null) {
/*  593 */         debug2.noSave = false;
/*      */       }
/*      */     } 
/*  596 */     saveAllChunks(false, true, false);
/*  597 */     for (ServerLevel debug2 : getAllLevels()) {
/*  598 */       if (debug2 != null) {
/*      */         try {
/*  600 */           debug2.close();
/*  601 */         } catch (IOException debug3) {
/*  602 */           LOGGER.error("Exception closing the level", debug3);
/*      */         } 
/*      */       }
/*      */     } 
/*  606 */     if (this.snooper.isStarted()) {
/*  607 */       this.snooper.interrupt();
/*      */     }
/*  609 */     this.resources.close();
/*      */     try {
/*  611 */       this.storageSource.close();
/*  612 */     } catch (IOException debug1) {
/*  613 */       LOGGER.error("Failed to unlock level {}", this.storageSource.getLevelId(), debug1);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getLocalIp() {
/*  618 */     return this.localIp;
/*      */   }
/*      */   
/*      */   public void setLocalIp(String debug1) {
/*  622 */     this.localIp = debug1;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/*  626 */     return this.running;
/*      */   }
/*      */   
/*      */   public void halt(boolean debug1) {
/*  630 */     this.running = false;
/*  631 */     if (debug1) {
/*      */       try {
/*  633 */         this.serverThread.join();
/*  634 */       } catch (InterruptedException debug2) {
/*  635 */         LOGGER.error("Error while shutting down", debug2);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void runServer() {
/*      */     try {
/*  642 */       if (initServer()) {
/*  643 */         this.nextTickTime = Util.getMillis();
/*      */         
/*  645 */         this.status.setDescription((Component)new TextComponent(this.motd));
/*  646 */         this.status.setVersion(new ServerStatus.Version(SharedConstants.getCurrentVersion().getName(), SharedConstants.getCurrentVersion().getProtocolVersion()));
/*      */         
/*  648 */         updateStatusIcon(this.status);
/*      */         
/*  650 */         while (this.running) {
/*  651 */           long debug1 = Util.getMillis() - this.nextTickTime;
/*      */           
/*  653 */           if (debug1 > 2000L && this.nextTickTime - this.lastOverloadWarning >= 15000L) {
/*  654 */             long l = debug1 / 50L;
/*  655 */             LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", Long.valueOf(debug1), Long.valueOf(l));
/*  656 */             this.nextTickTime += l * 50L;
/*  657 */             this.lastOverloadWarning = this.nextTickTime;
/*      */           } 
/*      */           
/*  660 */           this.nextTickTime += 50L;
/*      */           
/*  662 */           SingleTickProfiler debug3 = SingleTickProfiler.createTickProfiler("Server");
/*  663 */           startProfilerTick(debug3);
/*      */           
/*  665 */           this.profiler.startTick();
/*  666 */           this.profiler.push("tick");
/*  667 */           tickServer(this::haveTime);
/*  668 */           this.profiler.popPush("nextTickWait");
/*  669 */           this.mayHaveDelayedTasks = true;
/*  670 */           this.delayedTasksMaxNextTickTime = Math.max(Util.getMillis() + 50L, this.nextTickTime);
/*  671 */           waitUntilNextTick();
/*  672 */           this.profiler.pop();
/*  673 */           this.profiler.endTick();
/*      */           
/*  675 */           endProfilerTick(debug3);
/*      */           
/*  677 */           this.isReady = true;
/*      */         } 
/*      */       } else {
/*  680 */         onServerCrash((CrashReport)null);
/*      */       } 
/*  682 */     } catch (Throwable debug1) {
/*  683 */       CrashReport debug2; LOGGER.error("Encountered an unexpected exception", debug1);
/*      */ 
/*      */       
/*  686 */       if (debug1 instanceof ReportedException) {
/*  687 */         debug2 = fillReport(((ReportedException)debug1).getReport());
/*      */       } else {
/*  689 */         debug2 = fillReport(new CrashReport("Exception in server tick loop", debug1));
/*      */       } 
/*      */       
/*  692 */       File debug3 = new File(new File(getServerDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
/*      */       
/*  694 */       if (debug2.saveToFile(debug3)) {
/*  695 */         LOGGER.error("This crash report has been saved to: {}", debug3.getAbsolutePath());
/*      */       } else {
/*  697 */         LOGGER.error("We were unable to save this crash report to disk.");
/*      */       } 
/*      */       
/*  700 */       onServerCrash(debug2);
/*      */     } finally {
/*      */       try {
/*  703 */         this.stopped = true;
/*  704 */         stopServer();
/*  705 */       } catch (Throwable debug8) {
/*  706 */         LOGGER.error("Exception stopping the server", debug8);
/*      */       } finally {
/*  708 */         onServerExit();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean haveTime() {
/*  714 */     return (runningTask() || Util.getMillis() < (this.mayHaveDelayedTasks ? this.delayedTasksMaxNextTickTime : this.nextTickTime));
/*      */   }
/*      */   
/*      */   protected void waitUntilNextTick() {
/*  718 */     runAllTasks();
/*  719 */     managedBlock(() -> !haveTime());
/*      */   }
/*      */ 
/*      */   
/*      */   protected TickTask wrapRunnable(Runnable debug1) {
/*  724 */     return new TickTask(this.tickCount, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean shouldRun(TickTask debug1) {
/*  729 */     return (debug1.getTick() + 3 < this.tickCount || haveTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean pollTask() {
/*  734 */     boolean debug1 = pollTaskInternal();
/*  735 */     this.mayHaveDelayedTasks = debug1;
/*  736 */     return debug1;
/*      */   }
/*      */   
/*      */   private boolean pollTaskInternal() {
/*  740 */     if (super.pollTask()) {
/*  741 */       return true;
/*      */     }
/*      */     
/*  744 */     if (haveTime()) {
/*  745 */       for (ServerLevel debug2 : getAllLevels()) {
/*  746 */         if (debug2.getChunkSource().pollTask()) {
/*  747 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  752 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doRunTask(TickTask debug1) {
/*  757 */     getProfiler().incrementCounter("runTask");
/*  758 */     super.doRunTask(debug1);
/*      */   }
/*      */   
/*      */   private void updateStatusIcon(ServerStatus debug1) {
/*  762 */     File debug2 = getFile("server-icon.png");
/*  763 */     if (!debug2.exists()) {
/*  764 */       debug2 = this.storageSource.getIconFile();
/*      */     }
/*  766 */     if (debug2.isFile()) {
/*  767 */       ByteBuf debug3 = Unpooled.buffer();
/*      */       
/*      */       try {
/*  770 */         BufferedImage debug4 = ImageIO.read(debug2);
/*  771 */         Validate.validState((debug4.getWidth() == 64), "Must be 64 pixels wide", new Object[0]);
/*  772 */         Validate.validState((debug4.getHeight() == 64), "Must be 64 pixels high", new Object[0]);
/*  773 */         ImageIO.write(debug4, "PNG", (OutputStream)new ByteBufOutputStream(debug3));
/*  774 */         ByteBuffer debug5 = Base64.getEncoder().encode(debug3.nioBuffer());
/*  775 */         debug1.setFavicon("data:image/png;base64," + StandardCharsets.UTF_8.decode(debug5));
/*  776 */       } catch (Exception debug4) {
/*  777 */         LOGGER.error("Couldn't load server icon", debug4);
/*      */       } finally {
/*  779 */         debug3.release();
/*      */       } 
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
/*      */   public File getServerDirectory() {
/*  794 */     return new File(".");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onServerCrash(CrashReport debug1) {}
/*      */ 
/*      */   
/*      */   protected void onServerExit() {}
/*      */   
/*      */   protected void tickServer(BooleanSupplier debug1) {
/*  804 */     long debug2 = Util.getNanos();
/*      */     
/*  806 */     this.tickCount++;
/*      */     
/*  808 */     tickChildren(debug1);
/*      */     
/*  810 */     if (debug2 - this.lastServerStatus >= 5000000000L) {
/*  811 */       this.lastServerStatus = debug2;
/*  812 */       this.status.setPlayers(new ServerStatus.Players(getMaxPlayers(), getPlayerCount()));
/*      */       
/*  814 */       GameProfile[] arrayOfGameProfile = new GameProfile[Math.min(getPlayerCount(), 12)];
/*  815 */       int debug5 = Mth.nextInt(this.random, 0, getPlayerCount() - arrayOfGameProfile.length);
/*  816 */       for (int i = 0; i < arrayOfGameProfile.length; i++) {
/*  817 */         arrayOfGameProfile[i] = ((ServerPlayer)this.playerList.getPlayers().get(debug5 + i)).getGameProfile();
/*      */       }
/*  819 */       Collections.shuffle(Arrays.asList((Object[])arrayOfGameProfile));
/*  820 */       this.status.getPlayers().setSample(arrayOfGameProfile);
/*      */     } 
/*      */     
/*  823 */     if (this.tickCount % 6000 == 0) {
/*  824 */       LOGGER.debug("Autosave started");
/*  825 */       this.profiler.push("save");
/*  826 */       this.playerList.saveAll();
/*  827 */       saveAllChunks(true, false, false);
/*  828 */       this.profiler.pop();
/*  829 */       LOGGER.debug("Autosave finished");
/*      */     } 
/*      */     
/*  832 */     this.profiler.push("snooper");
/*  833 */     if (!this.snooper.isStarted() && this.tickCount > 100) {
/*  834 */       this.snooper.start();
/*      */     }
/*      */     
/*  837 */     if (this.tickCount % 6000 == 0) {
/*  838 */       this.snooper.prepare();
/*      */     }
/*  840 */     this.profiler.pop();
/*      */     
/*  842 */     this.profiler.push("tallying");
/*  843 */     long debug4 = this.tickTimes[this.tickCount % 100] = Util.getNanos() - debug2;
/*  844 */     this.averageTickTime = this.averageTickTime * 0.8F + (float)debug4 / 1000000.0F * 0.19999999F;
/*      */     
/*  846 */     long debug6 = Util.getNanos();
/*  847 */     this.frameTimer.logFrameDuration(debug6 - debug2);
/*      */     
/*  849 */     this.profiler.pop();
/*      */   }
/*      */   
/*      */   protected void tickChildren(BooleanSupplier debug1) {
/*  853 */     this.profiler.push("commandFunctions");
/*  854 */     getFunctions().tick();
/*      */     
/*  856 */     this.profiler.popPush("levels");
/*  857 */     for (ServerLevel debug3 : getAllLevels()) {
/*  858 */       this.profiler.push(() -> debug0 + " " + debug0.dimension().location());
/*      */       
/*  860 */       if (this.tickCount % 20 == 0) {
/*  861 */         this.profiler.push("timeSync");
/*  862 */         this.playerList.broadcastAll((Packet)new ClientboundSetTimePacket(debug3.getGameTime(), debug3.getDayTime(), debug3.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)), debug3.dimension());
/*  863 */         this.profiler.pop();
/*      */       } 
/*      */       
/*  866 */       this.profiler.push("tick");
/*      */       try {
/*  868 */         debug3.tick(debug1);
/*  869 */       } catch (Throwable debug4) {
/*  870 */         CrashReport debug5 = CrashReport.forThrowable(debug4, "Exception ticking world");
/*  871 */         debug3.fillReportDetails(debug5);
/*  872 */         throw new ReportedException(debug5);
/*      */       } 
/*  874 */       this.profiler.pop();
/*  875 */       this.profiler.pop();
/*      */     } 
/*      */     
/*  878 */     this.profiler.popPush("connection");
/*  879 */     getConnection().tick();
/*  880 */     this.profiler.popPush("players");
/*  881 */     this.playerList.tick();
/*      */     
/*  883 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  884 */       GameTestTicker.singleton.tick();
/*      */     }
/*  886 */     this.profiler.popPush("server gui refresh");
/*  887 */     for (int debug2 = 0; debug2 < this.tickables.size(); debug2++) {
/*  888 */       ((Runnable)this.tickables.get(debug2)).run();
/*      */     }
/*  890 */     this.profiler.pop();
/*      */   }
/*      */   
/*      */   public boolean isNetherEnabled() {
/*  894 */     return true;
/*      */   }
/*      */   
/*      */   public void addTickable(Runnable debug1) {
/*  898 */     this.tickables.add(debug1);
/*      */   }
/*      */   
/*      */   protected void setId(String debug1) {
/*  902 */     this.serverId = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getFile(String debug1) {
/*  910 */     return new File(getServerDirectory(), debug1);
/*      */   }
/*      */   
/*      */   public final ServerLevel overworld() {
/*  914 */     return this.levels.get(Level.OVERWORLD);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ServerLevel getLevel(ResourceKey<Level> debug1) {
/*  919 */     return this.levels.get(debug1);
/*      */   }
/*      */   
/*      */   public Set<ResourceKey<Level>> levelKeys() {
/*  923 */     return this.levels.keySet();
/*      */   }
/*      */   
/*      */   public Iterable<ServerLevel> getAllLevels() {
/*  927 */     return this.levels.values();
/*      */   }
/*      */   
/*      */   public String getServerVersion() {
/*  931 */     return SharedConstants.getCurrentVersion().getName();
/*      */   }
/*      */   
/*      */   public int getPlayerCount() {
/*  935 */     return this.playerList.getPlayerCount();
/*      */   }
/*      */   
/*      */   public int getMaxPlayers() {
/*  939 */     return this.playerList.getMaxPlayers();
/*      */   }
/*      */   
/*      */   public String[] getPlayerNames() {
/*  943 */     return this.playerList.getPlayerNamesArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getServerModName() {
/*  948 */     return "vanilla";
/*      */   }
/*      */   
/*      */   public CrashReport fillReport(CrashReport debug1) {
/*  952 */     if (this.playerList != null) {
/*  953 */       debug1.getSystemDetails().setDetail("Player Count", () -> this.playerList.getPlayerCount() + " / " + this.playerList.getMaxPlayers() + "; " + this.playerList.getPlayers());
/*      */     }
/*      */     
/*  956 */     debug1.getSystemDetails().setDetail("Data Packs", () -> {
/*      */           StringBuilder debug1 = new StringBuilder();
/*      */           
/*      */           for (Pack debug3 : this.packRepository.getSelectedPacks()) {
/*      */             if (debug1.length() > 0) {
/*      */               debug1.append(", ");
/*      */             }
/*      */             debug1.append(debug3.getId());
/*      */             if (!debug3.getCompatibility().isCompatible()) {
/*      */               debug1.append(" (incompatible)");
/*      */             }
/*      */           } 
/*      */           return debug1.toString();
/*      */         });
/*  970 */     if (this.serverId != null) {
/*  971 */       debug1.getSystemDetails().setDetail("Server Id", () -> this.serverId);
/*      */     }
/*      */     
/*  974 */     return debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMessage(Component debug1, UUID debug2) {
/*  981 */     LOGGER.info(debug1.getString());
/*      */   }
/*      */   
/*      */   public KeyPair getKeyPair() {
/*  985 */     return this.keyPair;
/*      */   }
/*      */   
/*      */   public int getPort() {
/*  989 */     return this.port;
/*      */   }
/*      */   
/*      */   public void setPort(int debug1) {
/*  993 */     this.port = debug1;
/*      */   }
/*      */   
/*      */   public String getSingleplayerName() {
/*  997 */     return this.singleplayerName;
/*      */   }
/*      */   
/*      */   public void setSingleplayerName(String debug1) {
/* 1001 */     this.singleplayerName = debug1;
/*      */   }
/*      */   
/*      */   public boolean isSingleplayer() {
/* 1005 */     return (this.singleplayerName != null);
/*      */   }
/*      */   
/*      */   public void setKeyPair(KeyPair debug1) {
/* 1009 */     this.keyPair = debug1;
/*      */   }
/*      */   
/*      */   public void setDifficulty(Difficulty debug1, boolean debug2) {
/* 1013 */     if (!debug2 && this.worldData.isDifficultyLocked()) {
/*      */       return;
/*      */     }
/*      */     
/* 1017 */     this.worldData.setDifficulty(this.worldData.isHardcore() ? Difficulty.HARD : debug1);
/*      */     
/* 1019 */     updateMobSpawningFlags();
/* 1020 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   public int getScaledTrackingDistance(int debug1) {
/* 1024 */     return debug1;
/*      */   }
/*      */   
/*      */   private void updateMobSpawningFlags() {
/* 1028 */     for (ServerLevel debug2 : getAllLevels()) {
/* 1029 */       debug2.setSpawnSettings(isSpawningMonsters(), isSpawningAnimals());
/*      */     }
/*      */   }
/*      */   
/*      */   public void setDifficultyLocked(boolean debug1) {
/* 1034 */     this.worldData.setDifficultyLocked(debug1);
/* 1035 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   private void sendDifficultyUpdate(ServerPlayer debug1) {
/* 1039 */     LevelData debug2 = debug1.getLevel().getLevelData();
/* 1040 */     debug1.connection.send((Packet)new ClientboundChangeDifficultyPacket(debug2.getDifficulty(), debug2.isDifficultyLocked()));
/*      */   }
/*      */   
/*      */   protected boolean isSpawningMonsters() {
/* 1044 */     return (this.worldData.getDifficulty() != Difficulty.PEACEFUL);
/*      */   }
/*      */   
/*      */   public boolean isDemo() {
/* 1048 */     return this.isDemo;
/*      */   }
/*      */   
/*      */   public void setDemo(boolean debug1) {
/* 1052 */     this.isDemo = debug1;
/*      */   }
/*      */   
/*      */   public String getResourcePack() {
/* 1056 */     return this.resourcePack;
/*      */   }
/*      */   
/*      */   public String getResourcePackHash() {
/* 1060 */     return this.resourcePackHash;
/*      */   }
/*      */   
/*      */   public void setResourcePack(String debug1, String debug2) {
/* 1064 */     this.resourcePack = debug1;
/* 1065 */     this.resourcePackHash = debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public void populateSnooper(Snooper debug1) {
/* 1070 */     debug1.setDynamicData("whitelist_enabled", Boolean.valueOf(false));
/* 1071 */     debug1.setDynamicData("whitelist_count", Integer.valueOf(0));
/* 1072 */     if (this.playerList != null) {
/* 1073 */       debug1.setDynamicData("players_current", Integer.valueOf(getPlayerCount()));
/* 1074 */       debug1.setDynamicData("players_max", Integer.valueOf(getMaxPlayers()));
/* 1075 */       debug1.setDynamicData("players_seen", Integer.valueOf((this.playerDataStorage.getSeenPlayers()).length));
/*      */     } 
/* 1077 */     debug1.setDynamicData("uses_auth", Boolean.valueOf(this.onlineMode));
/* 1078 */     debug1.setDynamicData("gui_state", hasGui() ? "enabled" : "disabled");
/* 1079 */     debug1.setDynamicData("run_time", Long.valueOf((Util.getMillis() - debug1.getStartupTime()) / 60L * 1000L));
/*      */     
/* 1081 */     debug1.setDynamicData("avg_tick_ms", Integer.valueOf((int)(Mth.average(this.tickTimes) * 1.0E-6D)));
/*      */     
/* 1083 */     int debug2 = 0;
/* 1084 */     for (ServerLevel debug4 : getAllLevels()) {
/* 1085 */       if (debug4 != null) {
/* 1086 */         debug1.setDynamicData("world[" + debug2 + "][dimension]", debug4.dimension().location());
/* 1087 */         debug1.setDynamicData("world[" + debug2 + "][mode]", this.worldData.getGameType());
/* 1088 */         debug1.setDynamicData("world[" + debug2 + "][difficulty]", debug4.getDifficulty());
/* 1089 */         debug1.setDynamicData("world[" + debug2 + "][hardcore]", Boolean.valueOf(this.worldData.isHardcore()));
/*      */ 
/*      */ 
/*      */         
/* 1093 */         debug1.setDynamicData("world[" + debug2 + "][height]", Integer.valueOf(this.maxBuildHeight));
/* 1094 */         debug1.setDynamicData("world[" + debug2 + "][chunks_loaded]", Integer.valueOf(debug4.getChunkSource().getLoadedChunksCount()));
/*      */         
/* 1096 */         debug2++;
/*      */       } 
/*      */     } 
/*      */     
/* 1100 */     debug1.setDynamicData("worlds", Integer.valueOf(debug2));
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
/*      */   public boolean usesAuthentication() {
/* 1121 */     return this.onlineMode;
/*      */   }
/*      */   
/*      */   public void setUsesAuthentication(boolean debug1) {
/* 1125 */     this.onlineMode = debug1;
/*      */   }
/*      */   
/*      */   public boolean getPreventProxyConnections() {
/* 1129 */     return this.preventProxyConnections;
/*      */   }
/*      */   
/*      */   public void setPreventProxyConnections(boolean debug1) {
/* 1133 */     this.preventProxyConnections = debug1;
/*      */   }
/*      */   
/*      */   public boolean isSpawningAnimals() {
/* 1137 */     return true;
/*      */   }
/*      */   
/*      */   public boolean areNpcsEnabled() {
/* 1141 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPvpAllowed() {
/* 1147 */     return this.pvp;
/*      */   }
/*      */   
/*      */   public void setPvpAllowed(boolean debug1) {
/* 1151 */     this.pvp = debug1;
/*      */   }
/*      */   
/*      */   public boolean isFlightAllowed() {
/* 1155 */     return this.allowFlight;
/*      */   }
/*      */   
/*      */   public void setFlightAllowed(boolean debug1) {
/* 1159 */     this.allowFlight = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMotd() {
/* 1165 */     return this.motd;
/*      */   }
/*      */   
/*      */   public void setMotd(String debug1) {
/* 1169 */     this.motd = debug1;
/*      */   }
/*      */   
/*      */   public int getMaxBuildHeight() {
/* 1173 */     return this.maxBuildHeight;
/*      */   }
/*      */   
/*      */   public void setMaxBuildHeight(int debug1) {
/* 1177 */     this.maxBuildHeight = debug1;
/*      */   }
/*      */   
/*      */   public boolean isStopped() {
/* 1181 */     return this.stopped;
/*      */   }
/*      */   
/*      */   public PlayerList getPlayerList() {
/* 1185 */     return this.playerList;
/*      */   }
/*      */   
/*      */   public void setPlayerList(PlayerList debug1) {
/* 1189 */     this.playerList = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultGameType(GameType debug1) {
/* 1195 */     this.worldData.setGameType(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ServerConnectionListener getConnection() {
/* 1200 */     return this.connection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasGui() {
/* 1208 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTickCount() {
/* 1214 */     return this.tickCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSpawnProtectionRadius() {
/* 1222 */     return 16;
/*      */   }
/*      */   
/*      */   public boolean isUnderSpawnProtection(ServerLevel debug1, BlockPos debug2, Player debug3) {
/* 1226 */     return false;
/*      */   }
/*      */   
/*      */   public void setForceGameType(boolean debug1) {
/* 1230 */     this.forceGameType = debug1;
/*      */   }
/*      */   
/*      */   public boolean getForceGameType() {
/* 1234 */     return this.forceGameType;
/*      */   }
/*      */   
/*      */   public boolean repliesToStatus() {
/* 1238 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPlayerIdleTimeout() {
/* 1246 */     return this.playerIdleTimeout;
/*      */   }
/*      */   
/*      */   public void setPlayerIdleTimeout(int debug1) {
/* 1250 */     this.playerIdleTimeout = debug1;
/*      */   }
/*      */   
/*      */   public MinecraftSessionService getSessionService() {
/* 1254 */     return this.sessionService;
/*      */   }
/*      */   
/*      */   public GameProfileRepository getProfileRepository() {
/* 1258 */     return this.profileRepository;
/*      */   }
/*      */   
/*      */   public GameProfileCache getProfileCache() {
/* 1262 */     return this.profileCache;
/*      */   }
/*      */   
/*      */   public ServerStatus getStatus() {
/* 1266 */     return this.status;
/*      */   }
/*      */   
/*      */   public void invalidateStatus() {
/* 1270 */     this.lastServerStatus = 0L;
/*      */   }
/*      */   
/*      */   public int getAbsoluteMaxWorldSize() {
/* 1274 */     return 29999984;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean scheduleExecutables() {
/* 1279 */     return (super.scheduleExecutables() && !isStopped());
/*      */   }
/*      */ 
/*      */   
/*      */   public Thread getRunningThread() {
/* 1284 */     return this.serverThread;
/*      */   }
/*      */   
/*      */   public int getCompressionThreshold() {
/* 1288 */     return 256;
/*      */   }
/*      */   
/*      */   public long getNextTickTime() {
/* 1292 */     return this.nextTickTime;
/*      */   }
/*      */   
/*      */   public DataFixer getFixerUpper() {
/* 1296 */     return this.fixerUpper;
/*      */   }
/*      */   
/*      */   public int getSpawnRadius(@Nullable ServerLevel debug1) {
/* 1300 */     if (debug1 != null) {
/* 1301 */       return debug1.getGameRules().getInt(GameRules.RULE_SPAWN_RADIUS);
/*      */     }
/* 1303 */     return 10;
/*      */   }
/*      */   
/*      */   public ServerAdvancementManager getAdvancements() {
/* 1307 */     return this.resources.getAdvancements();
/*      */   }
/*      */   
/*      */   public ServerFunctionManager getFunctions() {
/* 1311 */     return this.functionManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompletableFuture<Void> reloadResources(Collection<String> debug1) {
/* 1321 */     CompletableFuture<Void> debug2 = CompletableFuture.supplyAsync(() -> (ImmutableList)debug1.stream().map(this.packRepository::getPack).filter(Objects::nonNull).map(Pack::open).collect(ImmutableList.toImmutableList()), (Executor)this).thenCompose(debug1 -> ServerResources.loadResources((List<PackResources>)debug1, isDedicatedServer() ? Commands.CommandSelection.DEDICATED : Commands.CommandSelection.INTEGRATED, getFunctionCompilationLevel(), this.executor, (Executor)this)).thenAcceptAsync(debug2 -> {
/*      */           this.resources.close();
/*      */           
/*      */           this.resources = debug2;
/*      */           
/*      */           this.packRepository.setSelected(debug1);
/*      */           
/*      */           this.worldData.setDataPackConfig(getSelectedPacks(this.packRepository));
/*      */           debug2.updateGlobals();
/*      */           getPlayerList().saveAll();
/*      */           getPlayerList().reloadResources();
/*      */           this.functionManager.replaceLibrary(this.resources.getFunctionLibrary());
/*      */           this.structureManager.onResourceManagerReload(this.resources.getResourceManager());
/*      */         }(Executor)this);
/* 1335 */     if (isSameThread()) {
/* 1336 */       managedBlock(debug2::isDone);
/*      */     }
/* 1338 */     return debug2;
/*      */   }
/*      */   
/*      */   public static DataPackConfig configurePackRepository(PackRepository debug0, DataPackConfig debug1, boolean debug2) {
/* 1342 */     debug0.reload();
/* 1343 */     if (debug2) {
/* 1344 */       debug0.setSelected(Collections.singleton("vanilla"));
/* 1345 */       return new DataPackConfig((List)ImmutableList.of("vanilla"), (List)ImmutableList.of());
/*      */     } 
/*      */     
/* 1348 */     Set<String> debug3 = Sets.newLinkedHashSet();
/*      */     
/* 1350 */     for (String debug5 : debug1.getEnabled()) {
/* 1351 */       if (debug0.isAvailable(debug5)) {
/* 1352 */         debug3.add(debug5); continue;
/*      */       } 
/* 1354 */       LOGGER.warn("Missing data pack {}", debug5);
/*      */     } 
/*      */ 
/*      */     
/* 1358 */     for (Pack debug5 : debug0.getAvailablePacks()) {
/* 1359 */       String debug6 = debug5.getId();
/* 1360 */       if (!debug1.getDisabled().contains(debug6) && !debug3.contains(debug6)) {
/* 1361 */         LOGGER.info("Found new data pack {}, loading it automatically", debug6);
/* 1362 */         debug3.add(debug6);
/*      */       } 
/*      */     } 
/*      */     
/* 1366 */     if (debug3.isEmpty()) {
/* 1367 */       LOGGER.info("No datapacks selected, forcing vanilla");
/* 1368 */       debug3.add("vanilla");
/*      */     } 
/*      */     
/* 1371 */     debug0.setSelected(debug3);
/*      */ 
/*      */     
/* 1374 */     return getSelectedPacks(debug0);
/*      */   }
/*      */   
/*      */   private static DataPackConfig getSelectedPacks(PackRepository debug0) {
/* 1378 */     Collection<String> debug1 = debug0.getSelectedIds();
/* 1379 */     ImmutableList immutableList = ImmutableList.copyOf(debug1);
/* 1380 */     List<String> debug3 = (List<String>)debug0.getAvailableIds().stream().filter(debug1 -> !debug0.contains(debug1)).collect(ImmutableList.toImmutableList());
/* 1381 */     return new DataPackConfig((List)immutableList, debug3);
/*      */   }
/*      */   
/*      */   public void kickUnlistedPlayers(CommandSourceStack debug1) {
/* 1385 */     if (!isEnforceWhitelist()) {
/*      */       return;
/*      */     }
/*      */     
/* 1389 */     PlayerList debug2 = debug1.getServer().getPlayerList();
/* 1390 */     UserWhiteList debug3 = debug2.getWhiteList();
/*      */     
/* 1392 */     List<ServerPlayer> debug4 = Lists.newArrayList(debug2.getPlayers());
/* 1393 */     for (ServerPlayer debug6 : debug4) {
/* 1394 */       if (!debug3.isWhiteListed(debug6.getGameProfile())) {
/* 1395 */         debug6.connection.disconnect((Component)new TranslatableComponent("multiplayer.disconnect.not_whitelisted"));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public PackRepository getPackRepository() {
/* 1401 */     return this.packRepository;
/*      */   }
/*      */   
/*      */   public Commands getCommands() {
/* 1405 */     return this.resources.getCommands();
/*      */   }
/*      */   
/*      */   public CommandSourceStack createCommandSourceStack() {
/* 1409 */     ServerLevel debug1 = overworld();
/* 1410 */     return new CommandSourceStack(this, (debug1 == null) ? Vec3.ZERO : Vec3.atLowerCornerOf((Vec3i)debug1.getSharedSpawnPos()), Vec2.ZERO, debug1, 4, "Server", (Component)new TextComponent("Server"), this, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsSuccess() {
/* 1415 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsFailure() {
/* 1420 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RecipeManager getRecipeManager() {
/* 1427 */     return this.resources.getRecipeManager();
/*      */   }
/*      */   
/*      */   public TagContainer getTags() {
/* 1431 */     return this.resources.getTags();
/*      */   }
/*      */   
/*      */   public ServerScoreboard getScoreboard() {
/* 1435 */     return this.scoreboard;
/*      */   }
/*      */   
/*      */   public CommandStorage getCommandStorage() {
/* 1439 */     if (this.commandStorage == null)
/*      */     {
/* 1441 */       throw new NullPointerException("Called before server init");
/*      */     }
/* 1443 */     return this.commandStorage;
/*      */   }
/*      */   
/*      */   public LootTables getLootTables() {
/* 1447 */     return this.resources.getLootTables();
/*      */   }
/*      */   
/*      */   public PredicateManager getPredicateManager() {
/* 1451 */     return this.resources.getPredicateManager();
/*      */   }
/*      */   
/*      */   public GameRules getGameRules() {
/* 1455 */     return overworld().getGameRules();
/*      */   }
/*      */   
/*      */   public CustomBossEvents getCustomBossEvents() {
/* 1459 */     return this.customBossEvents;
/*      */   }
/*      */   
/*      */   public boolean isEnforceWhitelist() {
/* 1463 */     return this.enforceWhitelist;
/*      */   }
/*      */   
/*      */   public void setEnforceWhitelist(boolean debug1) {
/* 1467 */     this.enforceWhitelist = debug1;
/*      */   }
/*      */   
/*      */   public float getAverageTickTime() {
/* 1471 */     return this.averageTickTime;
/*      */   }
/*      */   
/*      */   public int getProfilePermissions(GameProfile debug1) {
/* 1475 */     if (getPlayerList().isOp(debug1)) {
/* 1476 */       ServerOpListEntry debug2 = (ServerOpListEntry)getPlayerList().getOps().get(debug1);
/* 1477 */       if (debug2 != null) {
/* 1478 */         return debug2.getLevel();
/*      */       }
/* 1480 */       if (isSingleplayerOwner(debug1)) {
/* 1481 */         return 4;
/*      */       }
/* 1483 */       if (isSingleplayer()) {
/* 1484 */         return getPlayerList().isAllowCheatsForAllPlayers() ? 4 : 0;
/*      */       }
/* 1486 */       return getOperatorUserPermissionLevel();
/*      */     } 
/* 1488 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProfilerFiller getProfiler() {
/* 1496 */     return this.profiler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveDebugReport(Path debug1) throws IOException {
/* 1506 */     Path debug2 = debug1.resolve("levels");
/* 1507 */     for (Map.Entry<ResourceKey<Level>, ServerLevel> debug4 : this.levels.entrySet()) {
/* 1508 */       ResourceLocation debug5 = ((ResourceKey)debug4.getKey()).location();
/* 1509 */       Path debug6 = debug2.resolve(debug5.getNamespace()).resolve(debug5.getPath());
/* 1510 */       Files.createDirectories(debug6, (FileAttribute<?>[])new FileAttribute[0]);
/* 1511 */       ((ServerLevel)debug4.getValue()).saveDebugReport(debug6);
/*      */     } 
/*      */     
/* 1514 */     dumpGameRules(debug1.resolve("gamerules.txt"));
/* 1515 */     dumpClasspath(debug1.resolve("classpath.txt"));
/* 1516 */     dumpCrashCategory(debug1.resolve("example_crash.txt"));
/* 1517 */     dumpMiscStats(debug1.resolve("stats.txt"));
/* 1518 */     dumpThreads(debug1.resolve("threads.txt"));
/*      */   }
/*      */   
/*      */   private void dumpMiscStats(Path debug1) throws IOException {
/* 1522 */     try (Writer debug2 = Files.newBufferedWriter(debug1, new java.nio.file.OpenOption[0])) {
/* 1523 */       debug2.write(String.format("pending_tasks: %d\n", new Object[] { Integer.valueOf(getPendingTasksCount()) }));
/* 1524 */       debug2.write(String.format("average_tick_time: %f\n", new Object[] { Float.valueOf(getAverageTickTime()) }));
/* 1525 */       debug2.write(String.format("tick_times: %s\n", new Object[] { Arrays.toString(this.tickTimes) }));
/* 1526 */       debug2.write(String.format("queue: %s\n", new Object[] { Util.backgroundExecutor() }));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpCrashCategory(Path debug1) throws IOException {
/* 1531 */     CrashReport debug2 = new CrashReport("Server dump", new Exception("dummy"));
/* 1532 */     fillReport(debug2);
/* 1533 */     try (Writer debug3 = Files.newBufferedWriter(debug1, new java.nio.file.OpenOption[0])) {
/* 1534 */       debug3.write(debug2.getFriendlyReport());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpGameRules(Path debug1) throws IOException {
/* 1539 */     try (Writer debug2 = Files.newBufferedWriter(debug1, new java.nio.file.OpenOption[0])) {
/* 1540 */       final List<String> entries = Lists.newArrayList();
/* 1541 */       final GameRules gameRules = getGameRules();
/* 1542 */       GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor()
/*      */           {
/*      */             public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> debug1, GameRules.Type<T> debug2) {
/* 1545 */               entries.add(String.format("%s=%s\n", new Object[] { debug1.getId(), this.val$gameRules.getRule(debug1).toString() }));
/*      */             }
/*      */           });
/* 1548 */       for (String debug7 : debug4) {
/* 1549 */         debug2.write(debug7);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpClasspath(Path debug1) throws IOException {
/* 1555 */     try (Writer debug2 = Files.newBufferedWriter(debug1, new java.nio.file.OpenOption[0])) {
/* 1556 */       String debug4 = System.getProperty("java.class.path");
/* 1557 */       String debug5 = System.getProperty("path.separator");
/* 1558 */       for (String debug7 : Splitter.on(debug5).split(debug4)) {
/* 1559 */         debug2.write(debug7);
/* 1560 */         debug2.write("\n");
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpThreads(Path debug1) throws IOException {
/* 1566 */     ThreadMXBean debug2 = ManagementFactory.getThreadMXBean();
/* 1567 */     ThreadInfo[] debug3 = debug2.dumpAllThreads(true, true);
/* 1568 */     Arrays.sort(debug3, Comparator.comparing(ThreadInfo::getThreadName));
/*      */     
/* 1570 */     try (Writer debug4 = Files.newBufferedWriter(debug1, new java.nio.file.OpenOption[0])) {
/* 1571 */       for (ThreadInfo debug9 : debug3) {
/* 1572 */         debug4.write(debug9.toString());
/* 1573 */         debug4.write(10);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void startProfilerTick(@Nullable SingleTickProfiler debug1) {
/* 1579 */     if (this.delayProfilerStart) {
/* 1580 */       this.delayProfilerStart = false;
/* 1581 */       this.continousProfiler.enable();
/*      */     } 
/* 1583 */     this.profiler = SingleTickProfiler.decorateFiller(this.continousProfiler.getFiller(), debug1);
/*      */   }
/*      */   
/*      */   private void endProfilerTick(@Nullable SingleTickProfiler debug1) {
/* 1587 */     if (debug1 != null) {
/* 1588 */       debug1.endTick();
/*      */     }
/* 1590 */     this.profiler = this.continousProfiler.getFiller();
/*      */   }
/*      */   
/*      */   public boolean isProfiling() {
/* 1594 */     return this.continousProfiler.isEnabled();
/*      */   }
/*      */   
/*      */   public void startProfiling() {
/* 1598 */     this.delayProfilerStart = true;
/*      */   }
/*      */   
/*      */   public ProfileResults finishProfiling() {
/* 1602 */     ProfileResults debug1 = this.continousProfiler.getResults();
/* 1603 */     this.continousProfiler.disable();
/* 1604 */     return debug1;
/*      */   }
/*      */   
/*      */   public Path getWorldPath(LevelResource debug1) {
/* 1608 */     return this.storageSource.getLevelPath(debug1);
/*      */   }
/*      */   
/*      */   public boolean forceSynchronousWrites() {
/* 1612 */     return true;
/*      */   }
/*      */   
/*      */   public StructureManager getStructureManager() {
/* 1616 */     return this.structureManager;
/*      */   }
/*      */   
/*      */   public WorldData getWorldData() {
/* 1620 */     return this.worldData;
/*      */   }
/*      */   
/*      */   public RegistryAccess registryAccess() {
/* 1624 */     return (RegistryAccess)this.registryHolder;
/*      */   }
/*      */   
/*      */   protected abstract boolean initServer() throws IOException;
/*      */   
/*      */   public abstract int getOperatorUserPermissionLevel();
/*      */   
/*      */   public abstract int getFunctionCompilationLevel();
/*      */   
/*      */   public abstract boolean shouldRconBroadcast();
/*      */   
/*      */   public abstract Optional<String> getModdedStatus();
/*      */   
/*      */   public abstract boolean isDedicatedServer();
/*      */   
/*      */   public abstract int getRateLimitPacketsPerSecond();
/*      */   
/*      */   public abstract boolean isEpollEnabled();
/*      */   
/*      */   public abstract boolean isCommandBlockEnabled();
/*      */   
/*      */   public abstract boolean isPublished();
/*      */   
/*      */   public abstract boolean publishServer(GameType paramGameType, boolean paramBoolean, int paramInt);
/*      */   
/*      */   public abstract boolean isSingleplayerOwner(GameProfile paramGameProfile);
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\MinecraftServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */