/*     */ package net.minecraft.world.level.dimension;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function14;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.File;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.BiomeZoomer;
/*     */ import net.minecraft.world.level.biome.FuzzyOffsetBiomeZoomer;
/*     */ import net.minecraft.world.level.biome.FuzzyOffsetConstantColumnBiomeZoomer;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ 
/*     */ public class DimensionType {
/*  36 */   public static final ResourceLocation OVERWORLD_EFFECTS = new ResourceLocation("overworld");
/*  37 */   public static final ResourceLocation NETHER_EFFECTS = new ResourceLocation("the_nether");
/*  38 */   public static final ResourceLocation END_EFFECTS = new ResourceLocation("the_end"); public static final Codec<DimensionType> DIRECT_CODEC;
/*     */   static {
/*  40 */     DIRECT_CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.LONG.optionalFieldOf("fixed_time").xmap((), ()).forGetter(()), (App)Codec.BOOL.fieldOf("has_skylight").forGetter(DimensionType::hasSkyLight), (App)Codec.BOOL.fieldOf("has_ceiling").forGetter(DimensionType::hasCeiling), (App)Codec.BOOL.fieldOf("ultrawarm").forGetter(DimensionType::ultraWarm), (App)Codec.BOOL.fieldOf("natural").forGetter(DimensionType::natural), (App)Codec.doubleRange(9.999999747378752E-6D, 3.0E7D).fieldOf("coordinate_scale").forGetter(DimensionType::coordinateScale), (App)Codec.BOOL.fieldOf("piglin_safe").forGetter(DimensionType::piglinSafe), (App)Codec.BOOL.fieldOf("bed_works").forGetter(DimensionType::bedWorks), (App)Codec.BOOL.fieldOf("respawn_anchor_works").forGetter(DimensionType::respawnAnchorWorks), (App)Codec.BOOL.fieldOf("has_raids").forGetter(DimensionType::hasRaids), (App)Codec.intRange(0, 256).fieldOf("logical_height").forGetter(DimensionType::logicalHeight), (App)ResourceLocation.CODEC.fieldOf("infiniburn").forGetter(()), (App)ResourceLocation.CODEC.fieldOf("effects").orElse(OVERWORLD_EFFECTS).forGetter(()), (App)Codec.FLOAT.fieldOf("ambient_light").forGetter(())).apply((Applicative)debug0, DimensionType::new));
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
/*  58 */   public static final float[] MOON_BRIGHTNESS_PER_PHASE = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final ResourceKey<DimensionType> OVERWORLD_LOCATION = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("overworld"));
/*  63 */   public static final ResourceKey<DimensionType> NETHER_LOCATION = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("the_nether"));
/*  64 */   public static final ResourceKey<DimensionType> END_LOCATION = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("the_end"));
/*     */ 
/*     */   
/*  67 */   protected static final DimensionType DEFAULT_OVERWORLD = new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 256, (BiomeZoomer)FuzzyOffsetConstantColumnBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), OVERWORLD_EFFECTS, 0.0F);
/*  68 */   protected static final DimensionType DEFAULT_NETHER = new DimensionType(OptionalLong.of(18000L), false, true, true, false, 8.0D, false, true, false, true, false, 128, (BiomeZoomer)FuzzyOffsetBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_NETHER.getName(), NETHER_EFFECTS, 0.1F);
/*  69 */   protected static final DimensionType DEFAULT_END = new DimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0D, true, false, false, false, true, 256, (BiomeZoomer)FuzzyOffsetBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_END.getName(), END_EFFECTS, 0.0F);
/*     */   
/*  71 */   public static final ResourceKey<DimensionType> OVERWORLD_CAVES_LOCATION = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("overworld_caves"));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected static final DimensionType DEFAULT_OVERWORLD_CAVES = new DimensionType(OptionalLong.empty(), true, true, false, true, 1.0D, false, false, true, false, true, 256, (BiomeZoomer)FuzzyOffsetConstantColumnBiomeZoomer.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), OVERWORLD_EFFECTS, 0.0F);
/*     */   
/*  78 */   public static final Codec<Supplier<DimensionType>> CODEC = (Codec<Supplier<DimensionType>>)RegistryFileCodec.create(Registry.DIMENSION_TYPE_REGISTRY, DIRECT_CODEC);
/*     */   
/*     */   private final OptionalLong fixedTime;
/*     */   
/*     */   private final boolean hasSkylight;
/*     */   
/*     */   private final boolean hasCeiling;
/*     */   private final boolean ultraWarm;
/*     */   private final boolean natural;
/*     */   private final double coordinateScale;
/*     */   private final boolean createDragonFight;
/*     */   private final boolean piglinSafe;
/*     */   private final boolean bedWorks;
/*     */   private final boolean respawnAnchorWorks;
/*     */   private final boolean hasRaids;
/*     */   private final int logicalHeight;
/*     */   private final BiomeZoomer biomeZoomer;
/*     */   private final ResourceLocation infiniburn;
/*     */   private final ResourceLocation effectsLocation;
/*     */   private final float ambientLight;
/*     */   private final transient float[] brightnessRamp;
/*     */   
/*     */   protected DimensionType(OptionalLong debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5, double debug6, boolean debug8, boolean debug9, boolean debug10, boolean debug11, int debug12, ResourceLocation debug13, ResourceLocation debug14, float debug15) {
/* 101 */     this(debug1, debug2, debug3, debug4, debug5, debug6, false, debug8, debug9, debug10, debug11, debug12, (BiomeZoomer)FuzzyOffsetBiomeZoomer.INSTANCE, debug13, debug14, debug15);
/*     */   }
/*     */   
/*     */   protected DimensionType(OptionalLong debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5, double debug6, boolean debug8, boolean debug9, boolean debug10, boolean debug11, boolean debug12, int debug13, BiomeZoomer debug14, ResourceLocation debug15, ResourceLocation debug16, float debug17) {
/* 105 */     this.fixedTime = debug1;
/* 106 */     this.hasSkylight = debug2;
/* 107 */     this.hasCeiling = debug3;
/* 108 */     this.ultraWarm = debug4;
/* 109 */     this.natural = debug5;
/* 110 */     this.coordinateScale = debug6;
/* 111 */     this.createDragonFight = debug8;
/* 112 */     this.piglinSafe = debug9;
/* 113 */     this.bedWorks = debug10;
/* 114 */     this.respawnAnchorWorks = debug11;
/* 115 */     this.hasRaids = debug12;
/* 116 */     this.logicalHeight = debug13;
/* 117 */     this.biomeZoomer = debug14;
/* 118 */     this.infiniburn = debug15;
/* 119 */     this.effectsLocation = debug16;
/* 120 */     this.ambientLight = debug17;
/* 121 */     this.brightnessRamp = fillBrightnessRamp(debug17);
/*     */   }
/*     */   
/*     */   private static float[] fillBrightnessRamp(float debug0) {
/* 125 */     float[] debug1 = new float[16];
/* 126 */     for (int debug2 = 0; debug2 <= 15; debug2++) {
/*     */       
/* 128 */       float debug3 = debug2 / 15.0F;
/*     */       
/* 130 */       float debug4 = debug3 / (4.0F - 3.0F * debug3);
/* 131 */       debug1[debug2] = Mth.lerp(debug0, debug4, 1.0F);
/*     */     } 
/* 133 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static DataResult<ResourceKey<Level>> parseLegacy(Dynamic<?> debug0) {
/* 139 */     Optional<Number> debug1 = debug0.asNumber().result();
/* 140 */     if (debug1.isPresent()) {
/* 141 */       int debug2 = ((Number)debug1.get()).intValue();
/* 142 */       if (debug2 == -1)
/* 143 */         return DataResult.success(Level.NETHER); 
/* 144 */       if (debug2 == 0)
/* 145 */         return DataResult.success(Level.OVERWORLD); 
/* 146 */       if (debug2 == 1) {
/* 147 */         return DataResult.success(Level.END);
/*     */       }
/*     */     } 
/*     */     
/* 151 */     return Level.RESOURCE_KEY_CODEC.parse(debug0);
/*     */   }
/*     */   
/*     */   public static RegistryAccess.RegistryHolder registerBuiltin(RegistryAccess.RegistryHolder debug0) {
/* 155 */     WritableRegistry<DimensionType> debug1 = debug0.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
/* 156 */     debug1.register(OVERWORLD_LOCATION, DEFAULT_OVERWORLD, Lifecycle.stable());
/* 157 */     debug1.register(OVERWORLD_CAVES_LOCATION, DEFAULT_OVERWORLD_CAVES, Lifecycle.stable());
/* 158 */     debug1.register(NETHER_LOCATION, DEFAULT_NETHER, Lifecycle.stable());
/* 159 */     debug1.register(END_LOCATION, DEFAULT_END, Lifecycle.stable());
/* 160 */     return debug0;
/*     */   }
/*     */   
/*     */   private static ChunkGenerator defaultEndGenerator(Registry<Biome> debug0, Registry<NoiseGeneratorSettings> debug1, long debug2) {
/* 164 */     return (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)new TheEndBiomeSource(debug0, debug2), debug2, () -> (NoiseGeneratorSettings)debug0.getOrThrow(NoiseGeneratorSettings.END));
/*     */   }
/*     */   
/*     */   private static ChunkGenerator defaultNetherGenerator(Registry<Biome> debug0, Registry<NoiseGeneratorSettings> debug1, long debug2) {
/* 168 */     return (ChunkGenerator)new NoiseBasedChunkGenerator((BiomeSource)MultiNoiseBiomeSource.Preset.NETHER.biomeSource(debug0, debug2), debug2, () -> (NoiseGeneratorSettings)debug0.getOrThrow(NoiseGeneratorSettings.NETHER));
/*     */   }
/*     */   
/*     */   public static MappedRegistry<LevelStem> defaultDimensions(Registry<DimensionType> debug0, Registry<Biome> debug1, Registry<NoiseGeneratorSettings> debug2, long debug3) {
/* 172 */     MappedRegistry<LevelStem> debug5 = new MappedRegistry(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
/*     */     
/* 174 */     debug5.register(LevelStem.NETHER, new LevelStem(() -> (DimensionType)debug0.getOrThrow(NETHER_LOCATION), defaultNetherGenerator(debug1, debug2, debug3)), Lifecycle.stable());
/* 175 */     debug5.register(LevelStem.END, new LevelStem(() -> (DimensionType)debug0.getOrThrow(END_LOCATION), defaultEndGenerator(debug1, debug2, debug3)), Lifecycle.stable());
/* 176 */     return debug5;
/*     */   }
/*     */   
/*     */   public static double getTeleportationScale(DimensionType debug0, DimensionType debug1) {
/* 180 */     double debug2 = debug0.coordinateScale();
/* 181 */     double debug4 = debug1.coordinateScale();
/*     */     
/* 183 */     return debug2 / debug4;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String getFileSuffix() {
/* 188 */     if (equalTo(DEFAULT_END)) {
/* 189 */       return "_end";
/*     */     }
/* 191 */     return "";
/*     */   }
/*     */   
/*     */   public static File getStorageFolder(ResourceKey<Level> debug0, File debug1) {
/* 195 */     if (debug0 == Level.OVERWORLD) {
/* 196 */       return debug1;
/*     */     }
/* 198 */     if (debug0 == Level.END) {
/* 199 */       return new File(debug1, "DIM1");
/*     */     }
/* 201 */     if (debug0 == Level.NETHER) {
/* 202 */       return new File(debug1, "DIM-1");
/*     */     }
/* 204 */     return new File(debug1, "dimensions/" + debug0.location().getNamespace() + "/" + debug0.location().getPath());
/*     */   }
/*     */   
/*     */   public boolean hasSkyLight() {
/* 208 */     return this.hasSkylight;
/*     */   }
/*     */   
/*     */   public boolean hasCeiling() {
/* 212 */     return this.hasCeiling;
/*     */   }
/*     */   
/*     */   public boolean ultraWarm() {
/* 216 */     return this.ultraWarm;
/*     */   }
/*     */   
/*     */   public boolean natural() {
/* 220 */     return this.natural;
/*     */   }
/*     */   
/*     */   public double coordinateScale() {
/* 224 */     return this.coordinateScale;
/*     */   }
/*     */   
/*     */   public boolean piglinSafe() {
/* 228 */     return this.piglinSafe;
/*     */   }
/*     */   
/*     */   public boolean bedWorks() {
/* 232 */     return this.bedWorks;
/*     */   }
/*     */   
/*     */   public boolean respawnAnchorWorks() {
/* 236 */     return this.respawnAnchorWorks;
/*     */   }
/*     */   
/*     */   public boolean hasRaids() {
/* 240 */     return this.hasRaids;
/*     */   }
/*     */   
/*     */   public int logicalHeight() {
/* 244 */     return this.logicalHeight;
/*     */   }
/*     */   
/*     */   public boolean createDragonFight() {
/* 248 */     return this.createDragonFight;
/*     */   }
/*     */   
/*     */   public BiomeZoomer getBiomeZoomer() {
/* 252 */     return this.biomeZoomer;
/*     */   }
/*     */   
/*     */   public boolean hasFixedTime() {
/* 256 */     return this.fixedTime.isPresent();
/*     */   }
/*     */ 
/*     */   
/*     */   public float timeOfDay(long debug1) {
/* 261 */     double debug3 = Mth.frac(this.fixedTime.orElse(debug1) / 24000.0D - 0.25D);
/*     */ 
/*     */     
/* 264 */     double debug5 = 0.5D - Math.cos(debug3 * Math.PI) / 2.0D;
/*     */     
/* 266 */     return (float)(debug3 * 2.0D + debug5) / 3.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int moonPhase(long debug1) {
/* 271 */     return (int)(debug1 / 24000L % 8L + 8L) % 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public float brightness(int debug1) {
/* 276 */     return this.brightnessRamp[debug1];
/*     */   }
/*     */   
/*     */   public Tag<Block> infiniburn() {
/* 280 */     Tag<Block> debug1 = BlockTags.getAllTags().getTag(this.infiniburn);
/* 281 */     return (debug1 != null) ? debug1 : (Tag<Block>)BlockTags.INFINIBURN_OVERWORLD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalTo(DimensionType debug1) {
/* 289 */     if (this == debug1) {
/* 290 */       return true;
/*     */     }
/* 292 */     return (this.hasSkylight == debug1.hasSkylight && this.hasCeiling == debug1.hasCeiling && this.ultraWarm == debug1.ultraWarm && this.natural == debug1.natural && this.coordinateScale == debug1.coordinateScale && this.createDragonFight == debug1.createDragonFight && this.piglinSafe == debug1.piglinSafe && this.bedWorks == debug1.bedWorks && this.respawnAnchorWorks == debug1.respawnAnchorWorks && this.hasRaids == debug1.hasRaids && this.logicalHeight == debug1.logicalHeight && 
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
/* 303 */       Float.compare(debug1.ambientLight, this.ambientLight) == 0 && this.fixedTime
/* 304 */       .equals(debug1.fixedTime) && this.biomeZoomer
/* 305 */       .equals(debug1.biomeZoomer) && this.infiniburn
/* 306 */       .equals(debug1.infiniburn) && this.effectsLocation
/* 307 */       .equals(debug1.effectsLocation));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\dimension\DimensionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */