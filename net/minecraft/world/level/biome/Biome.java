/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Biome
/*     */ {
/*  59 */   public static final Logger LOGGER = LogManager.getLogger(); public static final Codec<Biome> DIRECT_CODEC; public static final Codec<Biome> NETWORK_CODEC;
/*     */   static {
/*  61 */     DIRECT_CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ClimateSettings.CODEC.forGetter(()), (App)BiomeCategory.CODEC.fieldOf("category").forGetter(()), (App)Codec.FLOAT.fieldOf("depth").forGetter(()), (App)Codec.FLOAT.fieldOf("scale").forGetter(()), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(()), (App)BiomeGenerationSettings.CODEC.forGetter(()), (App)MobSpawnSettings.CODEC.forGetter(())).apply((Applicative)debug0, Biome::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     NETWORK_CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ClimateSettings.CODEC.forGetter(()), (App)BiomeCategory.CODEC.fieldOf("category").forGetter(()), (App)Codec.FLOAT.fieldOf("depth").forGetter(()), (App)Codec.FLOAT.fieldOf("scale").forGetter(()), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(())).apply((Applicative)debug0, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Codec<Supplier<Biome>> CODEC = (Codec<Supplier<Biome>>)RegistryFileCodec.create(Registry.BIOME_REGISTRY, DIRECT_CODEC);
/*  80 */   public static final Codec<List<Supplier<Biome>>> LIST_CODEC = RegistryFileCodec.homogeneousList(Registry.BIOME_REGISTRY, DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<Integer, List<StructureFeature<?>>> structuresByStep;
/*     */ 
/*     */ 
/*     */   
/*  88 */   private static final PerlinSimplexNoise TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(1234L), (List)ImmutableList.of(Integer.valueOf(0)));
/*  89 */   private static final PerlinSimplexNoise FROZEN_TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(3456L), (List)ImmutableList.of(Integer.valueOf(-2), Integer.valueOf(-1), Integer.valueOf(0))); private final ClimateSettings climateSettings; private final BiomeGenerationSettings generationSettings; private final MobSpawnSettings mobSettings; private final float depth;
/*  90 */   public static final PerlinSimplexNoise BIOME_INFO_NOISE = new PerlinSimplexNoise(new WorldgenRandom(2345L), (List)ImmutableList.of(Integer.valueOf(0)));
/*     */   private final float scale;
/*     */   private final BiomeCategory biomeCategory;
/*     */   private final BiomeSpecialEffects specialEffects;
/*     */   private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;
/*     */   
/*     */   public enum BiomeCategory implements StringRepresentable {
/*  97 */     NONE("none"),
/*  98 */     TAIGA("taiga"),
/*  99 */     EXTREME_HILLS("extreme_hills"),
/* 100 */     JUNGLE("jungle"),
/* 101 */     MESA("mesa"),
/* 102 */     PLAINS("plains"),
/* 103 */     SAVANNA("savanna"),
/* 104 */     ICY("icy"),
/* 105 */     THEEND("the_end"),
/* 106 */     BEACH("beach"),
/* 107 */     FOREST("forest"),
/* 108 */     OCEAN("ocean"),
/* 109 */     DESERT("desert"),
/* 110 */     RIVER("river"),
/* 111 */     SWAMP("swamp"),
/* 112 */     MUSHROOM("mushroom"),
/* 113 */     NETHER("nether");
/*     */ 
/*     */     
/* 116 */     public static final Codec<BiomeCategory> CODEC = StringRepresentable.fromEnum(BiomeCategory::values, BiomeCategory::byName); private static final Map<String, BiomeCategory> BY_NAME; private final String name;
/*     */     static {
/* 118 */       BY_NAME = (Map<String, BiomeCategory>)Arrays.<BiomeCategory>stream(values()).collect(Collectors.toMap(BiomeCategory::getName, debug0 -> debug0));
/*     */     }
/*     */     
/*     */     BiomeCategory(String debug3) {
/* 122 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 126 */       return this.name;
/*     */     }
/*     */     
/*     */     public static BiomeCategory byName(String debug0) {
/* 130 */       return BY_NAME.get(debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 135 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Precipitation implements StringRepresentable {
/* 140 */     NONE("none"),
/* 141 */     RAIN("rain"),
/* 142 */     SNOW("snow");
/*     */ 
/*     */     
/* 145 */     public static final Codec<Precipitation> CODEC = StringRepresentable.fromEnum(Precipitation::values, Precipitation::byName); private static final Map<String, Precipitation> BY_NAME; private final String name;
/*     */     static {
/* 147 */       BY_NAME = (Map<String, Precipitation>)Arrays.<Precipitation>stream(values()).collect(Collectors.toMap(Precipitation::getName, debug0 -> debug0));
/*     */     }
/*     */     
/*     */     Precipitation(String debug3) {
/* 151 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 155 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Precipitation byName(String debug0) {
/* 159 */       return BY_NAME.get(debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 164 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum TemperatureModifier implements StringRepresentable {
/* 169 */     NONE("none")
/*     */     {
/*     */       public float modifyTemperature(BlockPos debug1, float debug2) {
/* 172 */         return debug2;
/*     */       }
/*     */     },
/* 175 */     FROZEN("frozen")
/*     */     {
/*     */       public float modifyTemperature(BlockPos debug1, float debug2) {
/* 178 */         double debug3 = Biome.FROZEN_TEMPERATURE_NOISE.getValue(debug1.getX() * 0.05D, debug1.getZ() * 0.05D, false) * 7.0D;
/* 179 */         double debug5 = Biome.BIOME_INFO_NOISE.getValue(debug1.getX() * 0.2D, debug1.getZ() * 0.2D, false);
/* 180 */         double debug7 = debug3 + debug5;
/* 181 */         if (debug7 < 0.3D) {
/* 182 */           double debug9 = Biome.BIOME_INFO_NOISE.getValue(debug1.getX() * 0.09D, debug1.getZ() * 0.09D, false);
/* 183 */           if (debug9 < 0.8D) {
/* 184 */             return 0.2F;
/*     */           }
/*     */         } 
/*     */         
/* 188 */         return debug2;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     private final String name;
/*     */     private static final Map<String, TemperatureModifier> BY_NAME;
/*     */     
/*     */     TemperatureModifier(String debug3) {
/*     */       this.name = debug3;
/*     */     }
/*     */     
/* 200 */     public static final Codec<TemperatureModifier> CODEC = StringRepresentable.fromEnum(TemperatureModifier::values, TemperatureModifier::byName);
/*     */     static {
/* 202 */       BY_NAME = (Map<String, TemperatureModifier>)Arrays.<TemperatureModifier>stream(values()).collect(Collectors.toMap(TemperatureModifier::getName, debug0 -> debug0));
/*     */     }
/*     */     public String getName() {
/* 205 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 210 */       return this.name;
/*     */     }
/*     */     
/*     */     public static TemperatureModifier byName(String debug0) {
/* 214 */       return BY_NAME.get(debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract float modifyTemperature(BlockPos param1BlockPos, float param1Float);
/*     */   }
/*     */   
/*     */   private Biome(ClimateSettings debug1, BiomeCategory debug2, float debug3, float debug4, BiomeSpecialEffects debug5, BiomeGenerationSettings debug6, MobSpawnSettings debug7) {
/*     */     this.structuresByStep = (Map<Integer, List<StructureFeature<?>>>)Registry.STRUCTURE_FEATURE.stream().collect(Collectors.groupingBy(debug0 -> Integer.valueOf(debug0.step().ordinal())));
/* 223 */     this.temperatureCache = ThreadLocal.withInitial(() -> (Long2FloatLinkedOpenHashMap)Util.make(()));
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
/* 242 */     this.climateSettings = debug1;
/* 243 */     this.generationSettings = debug6;
/* 244 */     this.mobSettings = debug7;
/*     */     
/* 246 */     this.biomeCategory = debug2;
/* 247 */     this.depth = debug3;
/* 248 */     this.scale = debug4;
/* 249 */     this.specialEffects = debug5;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MobSpawnSettings getMobSettings() {
/* 257 */     return this.mobSettings;
/*     */   }
/*     */   
/*     */   public Precipitation getPrecipitation() {
/* 261 */     return this.climateSettings.precipitation;
/*     */   }
/*     */   
/*     */   public boolean isHumid() {
/* 265 */     return (getDownfall() > 0.85F);
/*     */   }
/*     */   
/*     */   private float getHeightAdjustedTemperature(BlockPos debug1) {
/* 269 */     float debug2 = this.climateSettings.temperatureModifier.modifyTemperature(debug1, getBaseTemperature());
/*     */     
/* 271 */     if (debug1.getY() > 64) {
/* 272 */       float debug3 = (float)(TEMPERATURE_NOISE.getValue((debug1.getX() / 8.0F), (debug1.getZ() / 8.0F), false) * 4.0D);
/* 273 */       return debug2 - (debug3 + debug1.getY() - 64.0F) * 0.05F / 30.0F;
/*     */     } 
/* 275 */     return debug2;
/*     */   }
/*     */   
/*     */   public final float getTemperature(BlockPos debug1) {
/* 279 */     long debug2 = debug1.asLong();
/* 280 */     Long2FloatLinkedOpenHashMap debug4 = this.temperatureCache.get();
/* 281 */     float debug5 = debug4.get(debug2);
/* 282 */     if (!Float.isNaN(debug5)) {
/* 283 */       return debug5;
/*     */     }
/* 285 */     float debug6 = getHeightAdjustedTemperature(debug1);
/* 286 */     if (debug4.size() == 1024) {
/* 287 */       debug4.removeFirstFloat();
/*     */     }
/* 289 */     debug4.put(debug2, debug6);
/* 290 */     return debug6;
/*     */   }
/*     */   
/*     */   public boolean shouldFreeze(LevelReader debug1, BlockPos debug2) {
/* 294 */     return shouldFreeze(debug1, debug2, true);
/*     */   }
/*     */   
/*     */   public boolean shouldFreeze(LevelReader debug1, BlockPos debug2, boolean debug3) {
/* 298 */     if (getTemperature(debug2) >= 0.15F) {
/* 299 */       return false;
/*     */     }
/*     */     
/* 302 */     if (debug2.getY() >= 0 && debug2.getY() < 256 && debug1.getBrightness(LightLayer.BLOCK, debug2) < 10) {
/* 303 */       BlockState debug4 = debug1.getBlockState(debug2);
/* 304 */       FluidState debug5 = debug1.getFluidState(debug2);
/* 305 */       if (debug5.getType() == Fluids.WATER && debug4.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
/* 306 */         if (!debug3) {
/* 307 */           return true;
/*     */         }
/*     */         
/* 310 */         boolean debug6 = (debug1.isWaterAt(debug2.west()) && debug1.isWaterAt(debug2.east()) && debug1.isWaterAt(debug2.north()) && debug1.isWaterAt(debug2.south()));
/* 311 */         if (!debug6) {
/* 312 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 316 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldSnow(LevelReader debug1, BlockPos debug2) {
/* 320 */     if (getTemperature(debug2) >= 0.15F) {
/* 321 */       return false;
/*     */     }
/*     */     
/* 324 */     if (debug2.getY() >= 0 && debug2.getY() < 256 && debug1.getBrightness(LightLayer.BLOCK, debug2) < 10) {
/* 325 */       BlockState debug3 = debug1.getBlockState(debug2);
/*     */ 
/*     */       
/* 328 */       if (debug3.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(debug1, debug2)) {
/* 329 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 333 */     return false;
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings getGenerationSettings() {
/* 337 */     return this.generationSettings;
/*     */   }
/*     */   
/*     */   public void generate(StructureFeatureManager debug1, ChunkGenerator debug2, WorldGenRegion debug3, long debug4, WorldgenRandom debug6, BlockPos debug7) {
/* 341 */     List<List<Supplier<ConfiguredFeature<?, ?>>>> debug8 = this.generationSettings.features();
/* 342 */     int debug9 = (GenerationStep.Decoration.values()).length;
/* 343 */     for (int debug10 = 0; debug10 < debug9; debug10++) {
/*     */       
/* 345 */       int debug11 = 0;
/* 346 */       if (debug1.shouldGenerateFeatures()) {
/* 347 */         List<StructureFeature<?>> debug12 = this.structuresByStep.getOrDefault(Integer.valueOf(debug10), Collections.emptyList());
/* 348 */         for (StructureFeature<?> debug14 : debug12) {
/* 349 */           debug6.setFeatureSeed(debug4, debug11, debug10);
/* 350 */           int debug15 = debug7.getX() >> 4;
/* 351 */           int debug16 = debug7.getZ() >> 4;
/* 352 */           int debug17 = debug15 << 4;
/* 353 */           int debug18 = debug16 << 4;
/*     */           try {
/* 355 */             debug1.startsForFeature(SectionPos.of(debug7), debug14).forEach(debug8 -> debug8.placeInChunk((WorldGenLevel)debug0, debug1, debug2, (Random)debug3, new BoundingBox(debug4, debug5, debug4 + 15, debug5 + 15), new ChunkPos(debug6, debug7)));
/*     */           
/*     */           }
/* 358 */           catch (Exception debug19) {
/* 359 */             CrashReport debug20 = CrashReport.forThrowable(debug19, "Feature placement");
/* 360 */             debug20.addCategory("Feature")
/* 361 */               .setDetail("Id", Registry.STRUCTURE_FEATURE.getKey(debug14))
/* 362 */               .setDetail("Description", () -> debug0.toString());
/* 363 */             throw new ReportedException(debug20);
/*     */           } 
/* 365 */           debug11++;
/*     */         } 
/*     */       } 
/* 368 */       if (debug8.size() > debug10) {
/* 369 */         for (Supplier<ConfiguredFeature<?, ?>> debug13 : debug8.get(debug10)) {
/* 370 */           ConfiguredFeature<?, ?> debug14 = debug13.get();
/* 371 */           debug6.setFeatureSeed(debug4, debug11, debug10);
/*     */           try {
/* 373 */             debug14.place((WorldGenLevel)debug3, debug2, (Random)debug6, debug7);
/* 374 */           } catch (Exception debug15) {
/* 375 */             CrashReport debug16 = CrashReport.forThrowable(debug15, "Feature placement");
/* 376 */             debug16.addCategory("Feature")
/* 377 */               .setDetail("Id", Registry.FEATURE.getKey(debug14.feature))
/* 378 */               .setDetail("Config", debug14.config)
/* 379 */               .setDetail("Description", () -> debug0.feature.toString());
/* 380 */             throw new ReportedException(debug16);
/*     */           } 
/* 382 */           debug11++;
/*     */         } 
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurfaceAt(Random debug1, ChunkAccess debug2, int debug3, int debug4, int debug5, double debug6, BlockState debug8, BlockState debug9, int debug10, long debug11) {
/* 416 */     ConfiguredSurfaceBuilder<?> debug13 = this.generationSettings.getSurfaceBuilder().get();
/* 417 */     debug13.initNoise(debug11);
/* 418 */     debug13.apply(debug1, debug2, this, debug3, debug4, debug5, debug6, debug8, debug9, debug10, debug11);
/*     */   }
/*     */   
/*     */   public final float getDepth() {
/* 422 */     return this.depth;
/*     */   }
/*     */   
/*     */   public final float getDownfall() {
/* 426 */     return this.climateSettings.downfall;
/*     */   }
/*     */   
/*     */   public final float getScale() {
/* 430 */     return this.scale;
/*     */   }
/*     */   
/*     */   public final float getBaseTemperature() {
/* 434 */     return this.climateSettings.temperature;
/*     */   }
/*     */   
/*     */   public BiomeSpecialEffects getSpecialEffects() {
/* 438 */     return this.specialEffects;
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
/*     */   public final BiomeCategory getBiomeCategory() {
/* 470 */     return this.biomeCategory;
/*     */   }
/*     */   
/*     */   public static class BiomeBuilder {
/*     */     @Nullable
/*     */     private Biome.Precipitation precipitation;
/*     */     @Nullable
/*     */     private Biome.BiomeCategory biomeCategory;
/*     */     @Nullable
/*     */     private Float depth;
/*     */     @Nullable
/*     */     private Float scale;
/*     */     @Nullable
/*     */     private Float temperature;
/* 484 */     private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */     @Nullable
/*     */     private Float downfall;
/*     */     @Nullable
/*     */     private BiomeSpecialEffects specialEffects;
/*     */     @Nullable
/*     */     private MobSpawnSettings mobSpawnSettings;
/*     */     @Nullable
/*     */     private BiomeGenerationSettings generationSettings;
/*     */     
/*     */     public BiomeBuilder precipitation(Biome.Precipitation debug1) {
/* 495 */       this.precipitation = debug1;
/* 496 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder biomeCategory(Biome.BiomeCategory debug1) {
/* 500 */       this.biomeCategory = debug1;
/* 501 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder depth(float debug1) {
/* 505 */       this.depth = Float.valueOf(debug1);
/* 506 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder scale(float debug1) {
/* 510 */       this.scale = Float.valueOf(debug1);
/* 511 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperature(float debug1) {
/* 515 */       this.temperature = Float.valueOf(debug1);
/* 516 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder downfall(float debug1) {
/* 520 */       this.downfall = Float.valueOf(debug1);
/* 521 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder specialEffects(BiomeSpecialEffects debug1) {
/* 525 */       this.specialEffects = debug1;
/* 526 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder mobSpawnSettings(MobSpawnSettings debug1) {
/* 530 */       this.mobSpawnSettings = debug1;
/* 531 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder generationSettings(BiomeGenerationSettings debug1) {
/* 535 */       this.generationSettings = debug1;
/* 536 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier debug1) {
/* 540 */       this.temperatureModifier = debug1;
/* 541 */       return this;
/*     */     }
/*     */     
/*     */     public Biome build() {
/* 545 */       if (this.precipitation == null || this.biomeCategory == null || this.depth == null || this.scale == null || this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 555 */         throw new IllegalStateException("You are missing parameters to build a proper biome\n" + this);
/*     */       }
/*     */       
/* 558 */       return new Biome(new Biome.ClimateSettings(this.precipitation, this.temperature
/* 559 */             .floatValue(), this.temperatureModifier, this.downfall.floatValue()), this.biomeCategory, this.depth
/*     */           
/* 561 */           .floatValue(), this.scale
/* 562 */           .floatValue(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 571 */       return "BiomeBuilder{\nprecipitation=" + this.precipitation + ",\nbiomeCategory=" + this.biomeCategory + ",\ndepth=" + this.depth + ",\nscale=" + this.scale + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + this.temperatureModifier + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + this.specialEffects + ",\nmobSpawnSettings=" + this.mobSpawnSettings + ",\ngenerationSettings=" + this.generationSettings + ",\n" + '}';
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ClimateParameters
/*     */   {
/*     */     public static final Codec<ClimateParameters> CODEC;
/*     */     
/*     */     private final float temperature;
/*     */     private final float humidity;
/*     */     private final float altitude;
/*     */     private final float weirdness;
/*     */     private final float offset;
/*     */     
/*     */     static {
/* 587 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.floatRange(-2.0F, 2.0F).fieldOf("temperature").forGetter(()), (App)Codec.floatRange(-2.0F, 2.0F).fieldOf("humidity").forGetter(()), (App)Codec.floatRange(-2.0F, 2.0F).fieldOf("altitude").forGetter(()), (App)Codec.floatRange(-2.0F, 2.0F).fieldOf("weirdness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("offset").forGetter(())).apply((Applicative)debug0, ClimateParameters::new));
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
/*     */     public ClimateParameters(float debug1, float debug2, float debug3, float debug4, float debug5) {
/* 602 */       this.temperature = debug1;
/* 603 */       this.humidity = debug2;
/* 604 */       this.altitude = debug3;
/* 605 */       this.weirdness = debug4;
/* 606 */       this.offset = debug5;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 611 */       if (this == debug1) {
/* 612 */         return true;
/*     */       }
/* 614 */       if (debug1 == null || getClass() != debug1.getClass()) {
/* 615 */         return false;
/*     */       }
/*     */       
/* 618 */       ClimateParameters debug2 = (ClimateParameters)debug1;
/*     */       
/* 620 */       if (Float.compare(debug2.temperature, this.temperature) != 0) {
/* 621 */         return false;
/*     */       }
/* 623 */       if (Float.compare(debug2.humidity, this.humidity) != 0) {
/* 624 */         return false;
/*     */       }
/* 626 */       if (Float.compare(debug2.altitude, this.altitude) != 0) {
/* 627 */         return false;
/*     */       }
/* 629 */       return (Float.compare(debug2.weirdness, this.weirdness) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 634 */       int debug1 = (this.temperature != 0.0F) ? Float.floatToIntBits(this.temperature) : 0;
/* 635 */       debug1 = 31 * debug1 + ((this.humidity != 0.0F) ? Float.floatToIntBits(this.humidity) : 0);
/* 636 */       debug1 = 31 * debug1 + ((this.altitude != 0.0F) ? Float.floatToIntBits(this.altitude) : 0);
/* 637 */       debug1 = 31 * debug1 + ((this.weirdness != 0.0F) ? Float.floatToIntBits(this.weirdness) : 0);
/* 638 */       return debug1;
/*     */     }
/*     */     
/*     */     public float fitness(ClimateParameters debug1) {
/* 642 */       return (this.temperature - debug1.temperature) * (this.temperature - debug1.temperature) + (this.humidity - debug1.humidity) * (this.humidity - debug1.humidity) + (this.altitude - debug1.altitude) * (this.altitude - debug1.altitude) + (this.weirdness - debug1.weirdness) * (this.weirdness - debug1.weirdness) + (this.offset - debug1.offset) * (this.offset - debug1.offset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 652 */     ResourceLocation debug1 = BuiltinRegistries.BIOME.getKey(this);
/* 653 */     return (debug1 == null) ? super.toString() : debug1.toString();
/*     */   }
/*     */   static class ClimateSettings { public static final MapCodec<ClimateSettings> CODEC; private final Biome.Precipitation precipitation;
/*     */     static {
/* 657 */       CODEC = RecordCodecBuilder.mapCodec(debug0 -> debug0.group((App)Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(()), (App)Codec.FLOAT.fieldOf("temperature").forGetter(()), (App)Biome.TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", Biome.TemperatureModifier.NONE).forGetter(()), (App)Codec.FLOAT.fieldOf("downfall").forGetter(())).apply((Applicative)debug0, ClimateSettings::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final float temperature;
/*     */     
/*     */     private final Biome.TemperatureModifier temperatureModifier;
/*     */     
/*     */     private final float downfall;
/*     */ 
/*     */     
/*     */     private ClimateSettings(Biome.Precipitation debug1, float debug2, Biome.TemperatureModifier debug3, float debug4) {
/* 670 */       this.precipitation = debug1;
/* 671 */       this.temperature = debug2;
/* 672 */       this.temperatureModifier = debug3;
/* 673 */       this.downfall = debug4;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\Biome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */