/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class WorldGenSettings
/*     */ {
/*     */   public static final Codec<WorldGenSettings> CODEC;
/*     */   
/*     */   static {
/*  47 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.LONG.fieldOf("seed").stable().forGetter(WorldGenSettings::seed), (App)Codec.BOOL.fieldOf("generate_features").orElse(Boolean.valueOf(true)).stable().forGetter(WorldGenSettings::generateFeatures), (App)Codec.BOOL.fieldOf("bonus_chest").orElse(Boolean.valueOf(false)).stable().forGetter(WorldGenSettings::generateBonusChest), (App)MappedRegistry.dataPackCodec(Registry.LEVEL_STEM_REGISTRY, Lifecycle.stable(), LevelStem.CODEC).xmap(LevelStem::sortMap, Function.identity()).fieldOf("dimensions").forGetter(WorldGenSettings::dimensions), (App)Codec.STRING.optionalFieldOf("legacy_custom_options").stable().forGetter(())).apply((Applicative)debug0, debug0.stable(WorldGenSettings::new))).comapFlatMap(WorldGenSettings::guardExperimental, Function.identity());
/*     */   }
/*  49 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final long seed;
/*     */   
/*     */   private final boolean generateFeatures;
/*     */   
/*     */   private final boolean generateBonusChest;
/*     */   private final MappedRegistry<LevelStem> dimensions;
/*     */   private final Optional<String> legacyCustomOptions;
/*     */   
/*     */   private DataResult<WorldGenSettings> guardExperimental() {
/*  60 */     LevelStem debug1 = (LevelStem)this.dimensions.get(LevelStem.OVERWORLD);
/*  61 */     if (debug1 == null) {
/*  62 */       return DataResult.error("Overworld settings missing");
/*     */     }
/*  64 */     if (stable()) {
/*  65 */       return DataResult.success(this, Lifecycle.stable());
/*     */     }
/*  67 */     return DataResult.success(this);
/*     */   }
/*     */   
/*     */   private boolean stable() {
/*  71 */     return LevelStem.stable(this.seed, this.dimensions);
/*     */   }
/*     */   
/*     */   public WorldGenSettings(long debug1, boolean debug3, boolean debug4, MappedRegistry<LevelStem> debug5) {
/*  75 */     this(debug1, debug3, debug4, debug5, Optional.empty());
/*     */     
/*  77 */     LevelStem debug6 = (LevelStem)debug5.get(LevelStem.OVERWORLD);
/*  78 */     if (debug6 == null) {
/*  79 */       throw new IllegalStateException("Overworld settings missing");
/*     */     }
/*     */   }
/*     */   
/*     */   private WorldGenSettings(long debug1, boolean debug3, boolean debug4, MappedRegistry<LevelStem> debug5, Optional<String> debug6) {
/*  84 */     this.seed = debug1;
/*  85 */     this.generateFeatures = debug3;
/*  86 */     this.generateBonusChest = debug4;
/*  87 */     this.dimensions = debug5;
/*  88 */     this.legacyCustomOptions = debug6;
/*     */   }
/*     */   
/*     */   public static WorldGenSettings demoSettings(RegistryAccess debug0) {
/*  92 */     WritableRegistry writableRegistry1 = debug0.registryOrThrow(Registry.BIOME_REGISTRY);
/*  93 */     int debug2 = "North Carolina".hashCode();
/*  94 */     WritableRegistry writableRegistry2 = debug0.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
/*  95 */     WritableRegistry writableRegistry3 = debug0.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
/*  96 */     return new WorldGenSettings(debug2, true, true, withOverworld((Registry<DimensionType>)writableRegistry2, DimensionType.defaultDimensions((Registry)writableRegistry2, (Registry)writableRegistry1, (Registry)writableRegistry3, debug2), makeDefaultOverworld((Registry<Biome>)writableRegistry1, (Registry<NoiseGeneratorSettings>)writableRegistry3, debug2)));
/*     */   }
/*     */   
/*     */   public static WorldGenSettings makeDefault(Registry<DimensionType> debug0, Registry<Biome> debug1, Registry<NoiseGeneratorSettings> debug2) {
/* 100 */     long debug3 = (new Random()).nextLong();
/* 101 */     return new WorldGenSettings(debug3, true, false, withOverworld(debug0, DimensionType.defaultDimensions(debug0, debug1, debug2, debug3), makeDefaultOverworld(debug1, debug2, debug3)));
/*     */   }
/*     */   
/*     */   public static NoiseBasedChunkGenerator makeDefaultOverworld(Registry<Biome> debug0, Registry<NoiseGeneratorSettings> debug1, long debug2) {
/* 105 */     return new NoiseBasedChunkGenerator((BiomeSource)new OverworldBiomeSource(debug2, false, false, debug0), debug2, () -> (NoiseGeneratorSettings)debug0.getOrThrow(NoiseGeneratorSettings.OVERWORLD));
/*     */   }
/*     */   
/*     */   public long seed() {
/* 109 */     return this.seed;
/*     */   }
/*     */   
/*     */   public boolean generateFeatures() {
/* 113 */     return this.generateFeatures;
/*     */   }
/*     */   
/*     */   public boolean generateBonusChest() {
/* 117 */     return this.generateBonusChest;
/*     */   }
/*     */   
/*     */   public static MappedRegistry<LevelStem> withOverworld(Registry<DimensionType> debug0, MappedRegistry<LevelStem> debug1, ChunkGenerator debug2) {
/* 121 */     LevelStem debug3 = (LevelStem)debug1.get(LevelStem.OVERWORLD);
/* 122 */     Supplier<DimensionType> debug4 = () -> (debug0 == null) ? (DimensionType)debug1.getOrThrow(DimensionType.OVERWORLD_LOCATION) : debug0.type();
/*     */     
/* 124 */     return withOverworld(debug1, debug4, debug2);
/*     */   }
/*     */   
/*     */   public static MappedRegistry<LevelStem> withOverworld(MappedRegistry<LevelStem> debug0, Supplier<DimensionType> debug1, ChunkGenerator debug2) {
/* 128 */     MappedRegistry<LevelStem> debug3 = new MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
/*     */     
/* 130 */     debug3.register(LevelStem.OVERWORLD, new LevelStem(debug1, debug2), Lifecycle.stable());
/* 131 */     for (Map.Entry<ResourceKey<LevelStem>, LevelStem> debug5 : (Iterable<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)debug0.entrySet()) {
/* 132 */       ResourceKey<LevelStem> debug6 = debug5.getKey();
/* 133 */       if (debug6 == LevelStem.OVERWORLD) {
/*     */         continue;
/*     */       }
/* 136 */       debug3.register(debug6, debug5.getValue(), debug0.lifecycle(debug5.getValue()));
/*     */     } 
/* 138 */     return debug3;
/*     */   }
/*     */   
/*     */   public MappedRegistry<LevelStem> dimensions() {
/* 142 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   public ChunkGenerator overworld() {
/* 146 */     LevelStem debug1 = (LevelStem)this.dimensions.get(LevelStem.OVERWORLD);
/* 147 */     if (debug1 == null) {
/* 148 */       throw new IllegalStateException("Overworld settings missing");
/*     */     }
/* 150 */     return debug1.generator();
/*     */   }
/*     */   
/*     */   public ImmutableSet<ResourceKey<Level>> levels() {
/* 154 */     return (ImmutableSet<ResourceKey<Level>>)dimensions().entrySet().stream().map(debug0 -> ResourceKey.create(Registry.DIMENSION_REGISTRY, ((ResourceKey)debug0.getKey()).location())).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*     */   
/*     */   public boolean isDebug() {
/* 158 */     return overworld() instanceof DebugLevelSource;
/*     */   }
/*     */   
/*     */   public boolean isFlatWorld() {
/* 162 */     return overworld() instanceof FlatLevelSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldGenSettings withBonusChest() {
/* 170 */     return new WorldGenSettings(this.seed, this.generateFeatures, true, this.dimensions, this.legacyCustomOptions);
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
/*     */   public static WorldGenSettings create(RegistryAccess debug0, Properties debug1) {
/*     */     JsonObject debug16;
/*     */     Dynamic<JsonElement> debug17;
/* 184 */     String debug2 = (String)MoreObjects.firstNonNull(debug1.get("generator-settings"), "");
/* 185 */     debug1.put("generator-settings", debug2);
/*     */     
/* 187 */     String debug3 = (String)MoreObjects.firstNonNull(debug1.get("level-seed"), "");
/* 188 */     debug1.put("level-seed", debug3);
/*     */     
/* 190 */     String debug4 = (String)debug1.get("generate-structures");
/* 191 */     boolean debug5 = (debug4 == null || Boolean.parseBoolean(debug4));
/* 192 */     debug1.put("generate-structures", Objects.toString(Boolean.valueOf(debug5)));
/*     */     
/* 194 */     String debug6 = (String)debug1.get("level-type");
/* 195 */     String debug7 = Optional.<String>ofNullable(debug6).map(debug0 -> debug0.toLowerCase(Locale.ROOT)).orElse("default");
/* 196 */     debug1.put("level-type", debug7);
/*     */     
/* 198 */     long debug8 = (new Random()).nextLong();
/* 199 */     if (!debug3.isEmpty()) {
/*     */       try {
/* 201 */         long debug10 = Long.parseLong(debug3);
/* 202 */         if (debug10 != 0L) {
/* 203 */           debug8 = debug10;
/*     */         }
/* 205 */       } catch (NumberFormatException debug10) {
/* 206 */         debug8 = debug3.hashCode();
/*     */       } 
/*     */     }
/*     */     
/* 210 */     WritableRegistry writableRegistry1 = debug0.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
/* 211 */     WritableRegistry writableRegistry2 = debug0.registryOrThrow(Registry.BIOME_REGISTRY);
/* 212 */     WritableRegistry writableRegistry3 = debug0.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
/*     */     
/* 214 */     MappedRegistry<LevelStem> debug13 = DimensionType.defaultDimensions((Registry)writableRegistry1, (Registry)writableRegistry2, (Registry)writableRegistry3, debug8);
/* 215 */     switch (debug7) {
/*     */       case "flat":
/* 217 */         debug16 = !debug2.isEmpty() ? GsonHelper.parse(debug2) : new JsonObject();
/* 218 */         debug17 = new Dynamic((DynamicOps)JsonOps.INSTANCE, debug16);
/* 219 */         return new WorldGenSettings(debug8, debug5, false, withOverworld((Registry<DimensionType>)writableRegistry1, debug13, new FlatLevelSource(FlatLevelGeneratorSettings.CODEC.parse(debug17).resultOrPartial(LOGGER::error).orElseGet(() -> FlatLevelGeneratorSettings.getDefault(debug0)))));
/*     */       case "debug_all_block_states":
/* 221 */         return new WorldGenSettings(debug8, debug5, false, withOverworld((Registry<DimensionType>)writableRegistry1, debug13, new DebugLevelSource((Registry<Biome>)writableRegistry2)));
/*     */       case "amplified":
/* 223 */         return new WorldGenSettings(debug8, debug5, false, withOverworld((Registry<DimensionType>)writableRegistry1, debug13, new NoiseBasedChunkGenerator((BiomeSource)new OverworldBiomeSource(debug8, false, false, (Registry)writableRegistry2), debug8, () -> (NoiseGeneratorSettings)debug0.getOrThrow(NoiseGeneratorSettings.AMPLIFIED))));
/*     */       case "largebiomes":
/* 225 */         return new WorldGenSettings(debug8, debug5, false, withOverworld((Registry<DimensionType>)writableRegistry1, debug13, new NoiseBasedChunkGenerator((BiomeSource)new OverworldBiomeSource(debug8, false, true, (Registry)writableRegistry2), debug8, () -> (NoiseGeneratorSettings)debug0.getOrThrow(NoiseGeneratorSettings.OVERWORLD))));
/*     */     } 
/* 227 */     return new WorldGenSettings(debug8, debug5, false, withOverworld((Registry<DimensionType>)writableRegistry1, debug13, makeDefaultOverworld((Registry<Biome>)writableRegistry2, (Registry<NoiseGeneratorSettings>)writableRegistry3, debug8)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\WorldGenSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */