/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeFormatterBuilder;
/*     */ import java.time.format.SignStyle;
/*     */ import java.time.temporal.ChronoField;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryLookupCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.DirectoryLock;
/*     */ import net.minecraft.util.ProgressListener;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class LevelStorageSource
/*     */ {
/*  62 */   private static final Logger LOGGER = LogManager.getLogger();
/*  63 */   private static final DateTimeFormatter FORMATTER = (new DateTimeFormatterBuilder())
/*  64 */     .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
/*  65 */     .appendLiteral('-')
/*  66 */     .appendValue(ChronoField.MONTH_OF_YEAR, 2)
/*  67 */     .appendLiteral('-')
/*  68 */     .appendValue(ChronoField.DAY_OF_MONTH, 2)
/*  69 */     .appendLiteral('_')
/*  70 */     .appendValue(ChronoField.HOUR_OF_DAY, 2)
/*  71 */     .appendLiteral('-')
/*  72 */     .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
/*  73 */     .appendLiteral('-')
/*  74 */     .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
/*  75 */     .toFormatter();
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final ImmutableList<String> OLD_SETTINGS_KEYS = ImmutableList.of("RandomSeed", "generatorName", "generatorOptions", "generatorVersion", "legacy_custom_options", "MapFeatures", "BonusChest");
/*     */ 
/*     */ 
/*     */   
/*     */   private final Path baseDir;
/*     */ 
/*     */   
/*     */   private final Path backupDir;
/*     */ 
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelStorageSource(Path debug1, Path debug2, DataFixer debug3) {
/*  94 */     this.fixerUpper = debug3;
/*     */     try {
/*  96 */       Files.createDirectories(Files.exists(debug1, new java.nio.file.LinkOption[0]) ? debug1.toRealPath(new java.nio.file.LinkOption[0]) : debug1, (FileAttribute<?>[])new FileAttribute[0]);
/*  97 */     } catch (IOException debug4) {
/*  98 */       throw new RuntimeException(debug4);
/*     */     } 
/* 100 */     this.baseDir = debug1;
/* 101 */     this.backupDir = debug2;
/*     */   }
/*     */   
/*     */   public static LevelStorageSource createDefault(Path debug0) {
/* 105 */     return new LevelStorageSource(debug0, debug0.resolve("../backups"), DataFixers.getDataFixer());
/*     */   }
/*     */   
/*     */   private static <T> Pair<WorldGenSettings, Lifecycle> readWorldGenSettings(Dynamic<T> debug0, DataFixer debug1, int debug2) {
/* 109 */     Dynamic<T> debug3 = debug0.get("WorldGenSettings").orElseEmptyMap();
/*     */     
/* 111 */     for (UnmodifiableIterator<String> unmodifiableIterator = OLD_SETTINGS_KEYS.iterator(); unmodifiableIterator.hasNext(); ) { String str = unmodifiableIterator.next();
/* 112 */       Optional<? extends Dynamic<?>> debug6 = debug0.get(str).result();
/* 113 */       if (debug6.isPresent()) {
/* 114 */         debug3 = debug3.set(str, debug6.get());
/*     */       } }
/*     */ 
/*     */     
/* 118 */     Dynamic<T> debug4 = debug1.update(References.WORLD_GEN_SETTINGS, debug3, debug2, SharedConstants.getCurrentVersion().getWorldVersion());
/* 119 */     DataResult<WorldGenSettings> debug5 = WorldGenSettings.CODEC.parse(debug4);
/* 120 */     return Pair.of(debug5.resultOrPartial(Util.prefix("WorldGenSettings: ", LOGGER::error)).orElseGet(() -> { Registry<DimensionType> debug1 = (Registry<DimensionType>)RegistryLookupCodec.create(Registry.DIMENSION_TYPE_REGISTRY).codec().parse(debug0).resultOrPartial(Util.prefix("Dimension type registry: ", LOGGER::error)).orElseThrow(()); Registry<Biome> debug2 = (Registry<Biome>)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).codec().parse(debug0).resultOrPartial(Util.prefix("Biome registry: ", LOGGER::error)).orElseThrow(()); Registry<NoiseGeneratorSettings> debug3 = (Registry<NoiseGeneratorSettings>)RegistryLookupCodec.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).codec().parse(debug0).resultOrPartial(Util.prefix("Noise settings registry: ", LOGGER::error)).orElseThrow(()); return WorldGenSettings.makeDefault(debug1, debug2, debug3); }), debug5
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 125 */         .lifecycle());
/*     */   }
/*     */   
/*     */   private static DataPackConfig readDataPackConfig(Dynamic<?> debug0) {
/* 129 */     return DataPackConfig.CODEC.parse(debug0).resultOrPartial(LOGGER::error).orElse(DataPackConfig.DEFAULT);
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
/*     */   private int getStorageVersion() {
/* 167 */     return 19133;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private <T> T readLevelData(File debug1, BiFunction<File, DataFixer, T> debug2) {
/* 172 */     if (!debug1.exists()) {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     File debug3 = new File(debug1, "level.dat");
/* 177 */     if (debug3.exists()) {
/* 178 */       T debug4 = debug2.apply(debug3, this.fixerUpper);
/* 179 */       if (debug4 != null) {
/* 180 */         return debug4;
/*     */       }
/*     */     } 
/*     */     
/* 184 */     debug3 = new File(debug1, "level.dat_old");
/* 185 */     if (debug3.exists()) {
/* 186 */       return debug2.apply(debug3, this.fixerUpper);
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static DataPackConfig getDataPacks(File debug0, DataFixer debug1) {
/*     */     try {
/* 194 */       CompoundTag debug2 = NbtIo.readCompressed(debug0);
/* 195 */       CompoundTag debug3 = debug2.getCompound("Data");
/* 196 */       debug3.remove("Player");
/* 197 */       int debug4 = debug3.contains("DataVersion", 99) ? debug3.getInt("DataVersion") : -1;
/* 198 */       Dynamic<Tag> debug5 = debug1.update(DataFixTypes.LEVEL.getType(), new Dynamic((DynamicOps)NbtOps.INSTANCE, debug3), debug4, SharedConstants.getCurrentVersion().getWorldVersion());
/* 199 */       return debug5.get("DataPacks").result().map(LevelStorageSource::readDataPackConfig).orElse(DataPackConfig.DEFAULT);
/* 200 */     } catch (Exception debug2) {
/* 201 */       LOGGER.error("Exception reading {}", debug0, debug2);
/*     */       
/* 203 */       return null;
/*     */     } 
/*     */   }
/*     */   private static BiFunction<File, DataFixer, PrimaryLevelData> getLevelData(DynamicOps<Tag> debug0, DataPackConfig debug1) {
/* 207 */     return (debug2, debug3) -> {
/*     */         try {
/*     */           CompoundTag debug4 = NbtIo.readCompressed(debug2);
/*     */           CompoundTag debug5 = debug4.getCompound("Data");
/*     */           CompoundTag debug6 = debug5.contains("Player", 10) ? debug5.getCompound("Player") : null;
/*     */           debug5.remove("Player");
/*     */           int debug7 = debug5.contains("DataVersion", 99) ? debug5.getInt("DataVersion") : -1;
/*     */           Dynamic<Tag> debug8 = debug3.update(DataFixTypes.LEVEL.getType(), new Dynamic(debug0, debug5), debug7, SharedConstants.getCurrentVersion().getWorldVersion());
/*     */           Pair<WorldGenSettings, Lifecycle> debug9 = readWorldGenSettings(debug8, debug3, debug7);
/*     */           LevelVersion debug10 = LevelVersion.parse(debug8);
/*     */           LevelSettings debug11 = LevelSettings.parse(debug8, debug1);
/*     */           return PrimaryLevelData.parse(debug8, debug3, debug7, debug6, debug11, debug10, (WorldGenSettings)debug9.getFirst(), (Lifecycle)debug9.getSecond());
/* 219 */         } catch (Exception debug4) {
/*     */           LOGGER.error("Exception reading {}", debug2, debug4);
/*     */           return null;
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   private BiFunction<File, DataFixer, LevelSummary> levelSummaryReader(File debug1, boolean debug2) {
/* 227 */     return (debug3, debug4) -> {
/*     */         try {
/*     */           CompoundTag debug5 = NbtIo.readCompressed(debug3);
/*     */           CompoundTag debug6 = debug5.getCompound("Data");
/*     */           debug6.remove("Player");
/*     */           int debug7 = debug6.contains("DataVersion", 99) ? debug6.getInt("DataVersion") : -1;
/*     */           Dynamic<Tag> debug8 = debug4.update(DataFixTypes.LEVEL.getType(), new Dynamic((DynamicOps)NbtOps.INSTANCE, debug6), debug7, SharedConstants.getCurrentVersion().getWorldVersion());
/*     */           LevelVersion debug9 = LevelVersion.parse(debug8);
/*     */           int debug10 = debug9.levelDataVersion();
/*     */           if (debug10 == 19132 || debug10 == 19133) {
/*     */             boolean debug11 = (debug10 != getStorageVersion());
/*     */             File debug12 = new File(debug1, "icon.png");
/*     */             DataPackConfig debug13 = debug8.get("DataPacks").result().map(LevelStorageSource::readDataPackConfig).orElse(DataPackConfig.DEFAULT);
/*     */             LevelSettings debug14 = LevelSettings.parse(debug8, debug13);
/*     */             return new LevelSummary(debug14, debug9, debug1.getName(), debug11, debug2, debug12);
/*     */           } 
/*     */           return null;
/* 244 */         } catch (Exception debug5) {
/*     */           LOGGER.error("Exception reading {}", debug3, debug5);
/*     */           return null;
/*     */         } 
/*     */       };
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
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelStorageAccess createAccess(String debug1) throws IOException {
/* 275 */     return new LevelStorageAccess(debug1);
/*     */   }
/*     */   
/*     */   public class LevelStorageAccess implements AutoCloseable {
/*     */     private final DirectoryLock lock;
/*     */     private final Path levelPath;
/*     */     private final String levelId;
/* 282 */     private final Map<LevelResource, Path> resources = Maps.newHashMap();
/*     */     
/*     */     public LevelStorageAccess(String debug2) throws IOException {
/* 285 */       this.levelId = debug2;
/* 286 */       this.levelPath = debug1.baseDir.resolve(debug2);
/* 287 */       this.lock = DirectoryLock.create(this.levelPath);
/*     */     }
/*     */     
/*     */     public String getLevelId() {
/* 291 */       return this.levelId;
/*     */     }
/*     */     
/*     */     public Path getLevelPath(LevelResource debug1) {
/* 295 */       return this.resources.computeIfAbsent(debug1, debug1 -> this.levelPath.resolve(debug1.getId()));
/*     */     }
/*     */     
/*     */     public File getDimensionPath(ResourceKey<Level> debug1) {
/* 299 */       return DimensionType.getStorageFolder(debug1, this.levelPath.toFile());
/*     */     }
/*     */     
/*     */     private void checkLock() {
/* 303 */       if (!this.lock.isValid()) {
/* 304 */         throw new IllegalStateException("Lock is no longer valid");
/*     */       }
/*     */     }
/*     */     
/*     */     public PlayerDataStorage createPlayerStorage() {
/* 309 */       checkLock();
/* 310 */       return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
/*     */     }
/*     */     
/*     */     public boolean requiresConversion() {
/* 314 */       LevelSummary debug1 = getSummary();
/* 315 */       return (debug1 != null && debug1.levelVersion().levelDataVersion() != LevelStorageSource.this.getStorageVersion());
/*     */     }
/*     */     
/*     */     public boolean convertLevel(ProgressListener debug1) {
/* 319 */       checkLock();
/* 320 */       return McRegionUpgrader.convertLevel(this, debug1);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public LevelSummary getSummary() {
/* 325 */       checkLock();
/* 326 */       return (LevelSummary)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (BiFunction)LevelStorageSource.this.levelSummaryReader(this.levelPath.toFile(), false));
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public WorldData getDataTag(DynamicOps<Tag> debug1, DataPackConfig debug2) {
/* 331 */       checkLock();
/* 332 */       return (WorldData)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (BiFunction)LevelStorageSource.getLevelData(debug1, debug2));
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public DataPackConfig getDataPacks() {
/* 337 */       checkLock();
/* 338 */       return (DataPackConfig)LevelStorageSource.this.readLevelData(this.levelPath.toFile(), (debug0, debug1) -> LevelStorageSource.getDataPacks(debug0, debug1));
/*     */     }
/*     */     
/*     */     public void saveDataTag(RegistryAccess debug1, WorldData debug2) {
/* 342 */       saveDataTag(debug1, debug2, null);
/*     */     }
/*     */     
/*     */     public void saveDataTag(RegistryAccess debug1, WorldData debug2, @Nullable CompoundTag debug3) {
/* 346 */       File debug4 = this.levelPath.toFile();
/*     */       
/* 348 */       CompoundTag debug5 = debug2.createTag(debug1, debug3);
/*     */       
/* 350 */       CompoundTag debug6 = new CompoundTag();
/* 351 */       debug6.put("Data", (Tag)debug5);
/*     */       
/*     */       try {
/* 354 */         File debug7 = File.createTempFile("level", ".dat", debug4);
/* 355 */         NbtIo.writeCompressed(debug6, debug7);
/*     */         
/* 357 */         File debug8 = new File(debug4, "level.dat_old");
/* 358 */         File debug9 = new File(debug4, "level.dat");
/* 359 */         Util.safeReplaceFile(debug9, debug7, debug8);
/* 360 */       } catch (Exception debug7) {
/* 361 */         LevelStorageSource.LOGGER.error("Failed to save level {}", debug4, debug7);
/*     */       } 
/*     */     }
/*     */     
/*     */     public File getIconFile() {
/* 366 */       checkLock();
/* 367 */       return this.levelPath.resolve("icon.png").toFile();
/*     */     }
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
/*     */     public void close() throws IOException {
/* 473 */       this.lock.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\LevelStorageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */