/*     */ package net.minecraft.world.level.storage;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SerializableUUID;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryWriteOps;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.timers.TimerCallbacks;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PrimaryLevelData implements ServerLevelData, WorldData {
/*  42 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private LevelSettings settings;
/*     */ 
/*     */   
/*     */   private final WorldGenSettings worldGenSettings;
/*     */ 
/*     */   
/*     */   private final Lifecycle worldGenSettingsLifecycle;
/*     */ 
/*     */   
/*     */   private int xSpawn;
/*     */ 
/*     */   
/*     */   private int ySpawn;
/*     */ 
/*     */   
/*     */   private int zSpawn;
/*     */ 
/*     */   
/*     */   private float spawnAngle;
/*     */ 
/*     */   
/*     */   private long gameTime;
/*     */ 
/*     */   
/*     */   private long dayTime;
/*     */   
/*     */   @Nullable
/*     */   private final DataFixer fixerUpper;
/*     */   
/*     */   private final int playerDataVersion;
/*     */   
/*     */   private boolean upgradedPlayerTag;
/*     */   
/*     */   @Nullable
/*     */   private CompoundTag loadedPlayerTag;
/*     */   
/*     */   private final int version;
/*     */   
/*     */   private int clearWeatherTime;
/*     */   
/*     */   private boolean raining;
/*     */   
/*     */   private int rainTime;
/*     */   
/*     */   private boolean thundering;
/*     */   
/*     */   private int thunderTime;
/*     */   
/*     */   private boolean initialized;
/*     */   
/*     */   private boolean difficultyLocked;
/*     */   
/*     */   private WorldBorder.Settings worldBorder;
/*     */   
/*     */   private CompoundTag endDragonFightData;
/*     */   
/*     */   @Nullable
/*     */   private CompoundTag customBossEvents;
/*     */   
/*     */   private int wanderingTraderSpawnDelay;
/*     */   
/*     */   private int wanderingTraderSpawnChance;
/*     */   
/*     */   @Nullable
/*     */   private UUID wanderingTraderId;
/*     */   
/*     */   private final Set<String> knownServerBrands;
/*     */   
/*     */   private boolean wasModded;
/*     */   
/*     */   private final TimerQueue<MinecraftServer> scheduledEvents;
/*     */ 
/*     */   
/*     */   private PrimaryLevelData(@Nullable DataFixer debug1, int debug2, @Nullable CompoundTag debug3, boolean debug4, int debug5, int debug6, int debug7, float debug8, long debug9, long debug11, int debug13, int debug14, int debug15, boolean debug16, int debug17, boolean debug18, boolean debug19, boolean debug20, WorldBorder.Settings debug21, int debug22, int debug23, @Nullable UUID debug24, LinkedHashSet<String> debug25, TimerQueue<MinecraftServer> debug26, @Nullable CompoundTag debug27, CompoundTag debug28, LevelSettings debug29, WorldGenSettings debug30, Lifecycle debug31) {
/* 119 */     this.fixerUpper = debug1;
/* 120 */     this.wasModded = debug4;
/* 121 */     this.xSpawn = debug5;
/* 122 */     this.ySpawn = debug6;
/* 123 */     this.zSpawn = debug7;
/* 124 */     this.spawnAngle = debug8;
/* 125 */     this.gameTime = debug9;
/* 126 */     this.dayTime = debug11;
/* 127 */     this.version = debug13;
/* 128 */     this.clearWeatherTime = debug14;
/* 129 */     this.rainTime = debug15;
/* 130 */     this.raining = debug16;
/* 131 */     this.thunderTime = debug17;
/* 132 */     this.thundering = debug18;
/* 133 */     this.initialized = debug19;
/* 134 */     this.difficultyLocked = debug20;
/* 135 */     this.worldBorder = debug21;
/* 136 */     this.wanderingTraderSpawnDelay = debug22;
/* 137 */     this.wanderingTraderSpawnChance = debug23;
/* 138 */     this.wanderingTraderId = debug24;
/* 139 */     this.knownServerBrands = debug25;
/* 140 */     this.loadedPlayerTag = debug3;
/* 141 */     this.playerDataVersion = debug2;
/* 142 */     this.scheduledEvents = debug26;
/* 143 */     this.customBossEvents = debug27;
/* 144 */     this.endDragonFightData = debug28;
/* 145 */     this.settings = debug29;
/* 146 */     this.worldGenSettings = debug30;
/* 147 */     this.worldGenSettingsLifecycle = debug31;
/*     */   }
/*     */   
/*     */   public PrimaryLevelData(LevelSettings debug1, WorldGenSettings debug2, Lifecycle debug3) {
/* 151 */     this(null, 
/* 152 */         SharedConstants.getCurrentVersion().getWorldVersion(), null, false, 0, 0, 0, 0.0F, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, WorldBorder.DEFAULT_SETTINGS, 0, 0, null, 
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
/* 173 */         Sets.newLinkedHashSet(), new TimerQueue(TimerCallbacks.SERVER_CALLBACKS), null, new CompoundTag(), debug1
/*     */ 
/*     */ 
/*     */         
/* 177 */         .copy(), debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrimaryLevelData parse(Dynamic<Tag> debug0, DataFixer debug1, int debug2, @Nullable CompoundTag debug3, LevelSettings debug4, LevelVersion debug5, WorldGenSettings debug6, Lifecycle debug7) {
/* 184 */     long debug8 = debug0.get("Time").asLong(0L);
/*     */ 
/*     */     
/* 187 */     CompoundTag debug10 = debug0.get("DragonFight").result().map(Dynamic::getValue).orElseGet(() -> (Tag)debug0.get("DimensionData").get("1").get("DragonFight").orElseEmptyMap().getValue());
/*     */ 
/*     */ 
/*     */     
/* 191 */     return new PrimaryLevelData(debug1, debug2, debug3, debug0
/*     */ 
/*     */ 
/*     */         
/* 195 */         .get("WasModded").asBoolean(false), debug0
/* 196 */         .get("SpawnX").asInt(0), debug0
/* 197 */         .get("SpawnY").asInt(0), debug0
/* 198 */         .get("SpawnZ").asInt(0), debug0
/* 199 */         .get("SpawnAngle").asFloat(0.0F), debug8, debug0
/*     */         
/* 201 */         .get("DayTime").asLong(debug8), debug5
/* 202 */         .levelDataVersion(), debug0
/* 203 */         .get("clearWeatherTime").asInt(0), debug0
/* 204 */         .get("rainTime").asInt(0), debug0
/* 205 */         .get("raining").asBoolean(false), debug0
/* 206 */         .get("thunderTime").asInt(0), debug0
/* 207 */         .get("thundering").asBoolean(false), debug0
/* 208 */         .get("initialized").asBoolean(true), debug0
/* 209 */         .get("DifficultyLocked").asBoolean(false), 
/* 210 */         WorldBorder.Settings.read((DynamicLike)debug0, WorldBorder.DEFAULT_SETTINGS), debug0
/* 211 */         .get("WanderingTraderSpawnDelay").asInt(0), debug0
/* 212 */         .get("WanderingTraderSpawnChance").asInt(0), debug0
/* 213 */         .get("WanderingTraderId").read((Decoder)SerializableUUID.CODEC).result().orElse(null), (LinkedHashSet<String>)debug0
/* 214 */         .get("ServerBrands").asStream().flatMap(debug0 -> Util.toStream(debug0.asString().result())).collect(Collectors.toCollection(Sets::newLinkedHashSet)), new TimerQueue(TimerCallbacks.SERVER_CALLBACKS, debug0
/* 215 */           .get("ScheduledEvents").asStream()), (CompoundTag)debug0
/* 216 */         .get("CustomBossEvents").orElseEmptyMap().getValue(), debug10, debug4, debug6, debug7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag createTag(RegistryAccess debug1, @Nullable CompoundTag debug2) {
/* 226 */     updatePlayerTag();
/* 227 */     if (debug2 == null) {
/* 228 */       debug2 = this.loadedPlayerTag;
/*     */     }
/* 230 */     CompoundTag debug3 = new CompoundTag();
/* 231 */     setTagData(debug1, debug3, debug2);
/* 232 */     return debug3;
/*     */   }
/*     */   
/*     */   private void setTagData(RegistryAccess debug1, CompoundTag debug2, @Nullable CompoundTag debug3) {
/* 236 */     ListTag debug4 = new ListTag();
/* 237 */     this.knownServerBrands.stream().map(StringTag::valueOf).forEach(debug4::add);
/* 238 */     debug2.put("ServerBrands", (Tag)debug4);
/* 239 */     debug2.putBoolean("WasModded", this.wasModded);
/* 240 */     CompoundTag debug5 = new CompoundTag();
/* 241 */     debug5.putString("Name", SharedConstants.getCurrentVersion().getName());
/* 242 */     debug5.putInt("Id", SharedConstants.getCurrentVersion().getWorldVersion());
/* 243 */     debug5.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().isStable());
/* 244 */     debug2.put("Version", (Tag)debug5);
/*     */     
/* 246 */     debug2.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/*     */     
/* 248 */     RegistryWriteOps<Tag> debug6 = RegistryWriteOps.create((DynamicOps)NbtOps.INSTANCE, debug1);
/* 249 */     WorldGenSettings.CODEC
/* 250 */       .encodeStart((DynamicOps)debug6, this.worldGenSettings)
/* 251 */       .resultOrPartial(Util.prefix("WorldGenSettings: ", LOGGER::error))
/* 252 */       .ifPresent(debug1 -> debug0.put("WorldGenSettings", debug1));
/*     */     
/* 254 */     debug2.putInt("GameType", this.settings.gameType().getId());
/* 255 */     debug2.putInt("SpawnX", this.xSpawn);
/* 256 */     debug2.putInt("SpawnY", this.ySpawn);
/* 257 */     debug2.putInt("SpawnZ", this.zSpawn);
/* 258 */     debug2.putFloat("SpawnAngle", this.spawnAngle);
/* 259 */     debug2.putLong("Time", this.gameTime);
/* 260 */     debug2.putLong("DayTime", this.dayTime);
/* 261 */     debug2.putLong("LastPlayed", Util.getEpochMillis());
/* 262 */     debug2.putString("LevelName", this.settings.levelName());
/* 263 */     debug2.putInt("version", 19133);
/* 264 */     debug2.putInt("clearWeatherTime", this.clearWeatherTime);
/* 265 */     debug2.putInt("rainTime", this.rainTime);
/* 266 */     debug2.putBoolean("raining", this.raining);
/* 267 */     debug2.putInt("thunderTime", this.thunderTime);
/* 268 */     debug2.putBoolean("thundering", this.thundering);
/* 269 */     debug2.putBoolean("hardcore", this.settings.hardcore());
/* 270 */     debug2.putBoolean("allowCommands", this.settings.allowCommands());
/* 271 */     debug2.putBoolean("initialized", this.initialized);
/* 272 */     this.worldBorder.write(debug2);
/* 273 */     debug2.putByte("Difficulty", (byte)this.settings.difficulty().getId());
/* 274 */     debug2.putBoolean("DifficultyLocked", this.difficultyLocked);
/* 275 */     debug2.put("GameRules", (Tag)this.settings.gameRules().createTag());
/*     */     
/* 277 */     debug2.put("DragonFight", (Tag)this.endDragonFightData);
/*     */     
/* 279 */     if (debug3 != null) {
/* 280 */       debug2.put("Player", (Tag)debug3);
/*     */     }
/*     */     
/* 283 */     DataPackConfig.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.settings.getDataPackConfig()).result().ifPresent(debug1 -> debug0.put("DataPacks", debug1));
/*     */     
/* 285 */     if (this.customBossEvents != null) {
/* 286 */       debug2.put("CustomBossEvents", (Tag)this.customBossEvents);
/*     */     }
/*     */     
/* 289 */     debug2.put("ScheduledEvents", (Tag)this.scheduledEvents.store());
/*     */     
/* 291 */     debug2.putInt("WanderingTraderSpawnDelay", this.wanderingTraderSpawnDelay);
/* 292 */     debug2.putInt("WanderingTraderSpawnChance", this.wanderingTraderSpawnChance);
/* 293 */     if (this.wanderingTraderId != null) {
/* 294 */       debug2.putUUID("WanderingTraderId", this.wanderingTraderId);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getXSpawn() {
/* 300 */     return this.xSpawn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYSpawn() {
/* 305 */     return this.ySpawn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZSpawn() {
/* 310 */     return this.zSpawn;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpawnAngle() {
/* 315 */     return this.spawnAngle;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getGameTime() {
/* 320 */     return this.gameTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDayTime() {
/* 325 */     return this.dayTime;
/*     */   }
/*     */   
/*     */   private void updatePlayerTag() {
/* 329 */     if (this.upgradedPlayerTag || this.loadedPlayerTag == null) {
/*     */       return;
/*     */     }
/* 332 */     if (this.playerDataVersion < SharedConstants.getCurrentVersion().getWorldVersion()) {
/* 333 */       if (this.fixerUpper == null) {
/* 334 */         throw (NullPointerException)Util.pauseInIde(new NullPointerException("Fixer Upper not set inside LevelData, and the player tag is not upgraded."));
/*     */       }
/* 336 */       this.loadedPlayerTag = NbtUtils.update(this.fixerUpper, DataFixTypes.PLAYER, this.loadedPlayerTag, this.playerDataVersion);
/*     */     } 
/* 338 */     this.upgradedPlayerTag = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getLoadedPlayerTag() {
/* 343 */     updatePlayerTag();
/* 344 */     return this.loadedPlayerTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXSpawn(int debug1) {
/* 349 */     this.xSpawn = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYSpawn(int debug1) {
/* 354 */     this.ySpawn = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZSpawn(int debug1) {
/* 359 */     this.zSpawn = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnAngle(float debug1) {
/* 364 */     this.spawnAngle = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameTime(long debug1) {
/* 369 */     this.gameTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDayTime(long debug1) {
/* 374 */     this.dayTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawn(BlockPos debug1, float debug2) {
/* 379 */     this.xSpawn = debug1.getX();
/* 380 */     this.ySpawn = debug1.getY();
/* 381 */     this.zSpawn = debug1.getZ();
/* 382 */     this.spawnAngle = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevelName() {
/* 387 */     return this.settings.levelName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 392 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getClearWeatherTime() {
/* 397 */     return this.clearWeatherTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClearWeatherTime(int debug1) {
/* 402 */     this.clearWeatherTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/* 407 */     return this.thundering;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThundering(boolean debug1) {
/* 412 */     this.thundering = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/* 417 */     return this.thunderTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThunderTime(int debug1) {
/* 422 */     this.thunderTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/* 427 */     return this.raining;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRaining(boolean debug1) {
/* 432 */     this.raining = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/* 437 */     return this.rainTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRainTime(int debug1) {
/* 442 */     this.rainTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameType() {
/* 447 */     return this.settings.gameType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(GameType debug1) {
/* 452 */     this.settings = this.settings.withGameType(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 457 */     return this.settings.hardcore();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowCommands() {
/* 462 */     return this.settings.allowCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 467 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInitialized(boolean debug1) {
/* 472 */     this.initialized = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameRules getGameRules() {
/* 477 */     return this.settings.gameRules();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder.Settings getWorldBorder() {
/* 482 */     return this.worldBorder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldBorder(WorldBorder.Settings debug1) {
/* 487 */     this.worldBorder = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 492 */     return this.settings.difficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficulty(Difficulty debug1) {
/* 497 */     this.settings = this.settings.withDifficulty(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 502 */     return this.difficultyLocked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficultyLocked(boolean debug1) {
/* 507 */     this.difficultyLocked = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TimerQueue<MinecraftServer> getScheduledEvents() {
/* 512 */     return this.scheduledEvents;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory debug1) {
/* 517 */     super.fillCrashReportCategory(debug1);
/* 518 */     super.fillCrashReportCategory(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenSettings worldGenSettings() {
/* 523 */     return this.worldGenSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag endDragonFightData() {
/* 533 */     return this.endDragonFightData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndDragonFightData(CompoundTag debug1) {
/* 538 */     this.endDragonFightData = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataPackConfig getDataPackConfig() {
/* 543 */     return this.settings.getDataPackConfig();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataPackConfig(DataPackConfig debug1) {
/* 548 */     this.settings = this.settings.withDataPackConfig(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getCustomBossEvents() {
/* 554 */     return this.customBossEvents;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomBossEvents(@Nullable CompoundTag debug1) {
/* 559 */     this.customBossEvents = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnDelay() {
/* 564 */     return this.wanderingTraderSpawnDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnDelay(int debug1) {
/* 569 */     this.wanderingTraderSpawnDelay = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnChance() {
/* 574 */     return this.wanderingTraderSpawnChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnChance(int debug1) {
/* 579 */     this.wanderingTraderSpawnChance = debug1;
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
/*     */   public void setWanderingTraderId(UUID debug1) {
/* 591 */     this.wanderingTraderId = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setModdedInfo(String debug1, boolean debug2) {
/* 596 */     this.knownServerBrands.add(debug1);
/* 597 */     this.wasModded |= debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasModded() {
/* 602 */     return this.wasModded;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getKnownServerBrands() {
/* 607 */     return (Set<String>)ImmutableSet.copyOf(this.knownServerBrands);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerLevelData overworldData() {
/* 612 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\PrimaryLevelData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */