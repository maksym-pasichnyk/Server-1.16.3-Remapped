/*     */ package net.minecraft.world.level.levelgen.flat;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.worldgen.Features;
/*     */ import net.minecraft.data.worldgen.StructureFeatures;
/*     */ import net.minecraft.resources.RegistryLookupCodec;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.StructureSettings;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class FlatLevelGeneratorSettings {
/*  39 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public static final Codec<FlatLevelGeneratorSettings> CODEC;
/*     */   
/*     */   private static final Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> STRUCTURE_FEATURES;
/*     */   private final Registry<Biome> biomes;
/*     */   private final StructureSettings structureSettings;
/*     */   
/*     */   static {
/*  48 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(()), (App)StructureSettings.CODEC.fieldOf("structures").forGetter(FlatLevelGeneratorSettings::structureSettings), (App)FlatLayerInfo.CODEC.listOf().fieldOf("layers").forGetter(FlatLevelGeneratorSettings::getLayersInfo), (App)Codec.BOOL.fieldOf("lakes").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("features").orElse(Boolean.valueOf(false)).forGetter(()), (App)Biome.CODEC.optionalFieldOf("biome").orElseGet(Optional::empty).forGetter(())).apply((Applicative)debug0, FlatLevelGeneratorSettings::new)).stable();
/*     */     
/*  50 */     STRUCTURE_FEATURES = (Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(StructureFeature.MINESHAFT, StructureFeatures.MINESHAFT);
/*     */           debug0.put(StructureFeature.VILLAGE, StructureFeatures.VILLAGE_PLAINS);
/*     */           debug0.put(StructureFeature.STRONGHOLD, StructureFeatures.STRONGHOLD);
/*     */           debug0.put(StructureFeature.SWAMP_HUT, StructureFeatures.SWAMP_HUT);
/*     */           debug0.put(StructureFeature.DESERT_PYRAMID, StructureFeatures.DESERT_PYRAMID);
/*     */           debug0.put(StructureFeature.JUNGLE_TEMPLE, StructureFeatures.JUNGLE_TEMPLE);
/*     */           debug0.put(StructureFeature.IGLOO, StructureFeatures.IGLOO);
/*     */           debug0.put(StructureFeature.OCEAN_RUIN, StructureFeatures.OCEAN_RUIN_COLD);
/*     */           debug0.put(StructureFeature.SHIPWRECK, StructureFeatures.SHIPWRECK);
/*     */           debug0.put(StructureFeature.OCEAN_MONUMENT, StructureFeatures.OCEAN_MONUMENT);
/*     */           debug0.put(StructureFeature.END_CITY, StructureFeatures.END_CITY);
/*     */           debug0.put(StructureFeature.WOODLAND_MANSION, StructureFeatures.WOODLAND_MANSION);
/*     */           debug0.put(StructureFeature.NETHER_BRIDGE, StructureFeatures.NETHER_BRIDGE);
/*     */           debug0.put(StructureFeature.PILLAGER_OUTPOST, StructureFeatures.PILLAGER_OUTPOST);
/*     */           debug0.put(StructureFeature.RUINED_PORTAL, StructureFeatures.RUINED_PORTAL_STANDARD);
/*     */           debug0.put(StructureFeature.BASTION_REMNANT, StructureFeatures.BASTION_REMNANT);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*  71 */   private final List<FlatLayerInfo> layersInfo = Lists.newArrayList();
/*     */   private Supplier<Biome> biome;
/*  73 */   private final BlockState[] layers = new BlockState[256];
/*     */   private boolean voidGen;
/*     */   private boolean decoration = false;
/*     */   private boolean addLakes = false;
/*     */   
/*     */   public FlatLevelGeneratorSettings(Registry<Biome> debug1, StructureSettings debug2, List<FlatLayerInfo> debug3, boolean debug4, boolean debug5, Optional<Supplier<Biome>> debug6) {
/*  79 */     this(debug2, debug1);
/*  80 */     if (debug4) {
/*  81 */       setAddLakes();
/*     */     }
/*  83 */     if (debug5) {
/*  84 */       setDecoration();
/*     */     }
/*  86 */     this.layersInfo.addAll(debug3);
/*  87 */     updateLayers();
/*  88 */     if (!debug6.isPresent()) {
/*  89 */       LOGGER.error("Unknown biome, defaulting to plains");
/*  90 */       this.biome = (() -> (Biome)debug0.getOrThrow(Biomes.PLAINS));
/*     */     } else {
/*  92 */       this.biome = debug6.get();
/*     */     } 
/*     */   }
/*     */   
/*     */   public FlatLevelGeneratorSettings(StructureSettings debug1, Registry<Biome> debug2) {
/*  97 */     this.biomes = debug2;
/*  98 */     this.structureSettings = debug1;
/*  99 */     this.biome = (() -> (Biome)debug0.getOrThrow(Biomes.PLAINS));
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
/*     */   public void setDecoration() {
/* 123 */     this.decoration = true;
/*     */   }
/*     */   
/*     */   public void setAddLakes() {
/* 127 */     this.addLakes = true;
/*     */   }
/*     */   
/*     */   public Biome getBiomeFromSettings() {
/* 131 */     Biome debug1 = getBiome();
/* 132 */     BiomeGenerationSettings debug2 = debug1.getGenerationSettings();
/*     */ 
/*     */     
/* 135 */     BiomeGenerationSettings.Builder debug3 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug2.getSurfaceBuilder());
/*     */     
/* 137 */     if (this.addLakes) {
/* 138 */       debug3.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER);
/* 139 */       debug3.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
/*     */     } 
/*     */     
/* 142 */     for (Map.Entry<StructureFeature<?>, StructureFeatureConfiguration> entry : (Iterable<Map.Entry<StructureFeature<?>, StructureFeatureConfiguration>>)this.structureSettings.structureConfig().entrySet()) {
/* 143 */       debug3.addStructureStart(debug2.withBiomeConfig(STRUCTURE_FEATURES.get(entry.getKey())));
/*     */     }
/*     */     
/* 146 */     boolean debug4 = ((!this.voidGen || this.biomes.getResourceKey(debug1).equals(Optional.of(Biomes.THE_VOID))) && this.decoration);
/*     */     
/* 148 */     if (debug4) {
/* 149 */       List<List<Supplier<ConfiguredFeature<?, ?>>>> list = debug2.features();
/* 150 */       for (int i = 0; i < list.size(); i++) {
/* 151 */         if (i != GenerationStep.Decoration.UNDERGROUND_STRUCTURES.ordinal() && i != GenerationStep.Decoration.SURFACE_STRUCTURES.ordinal()) {
/*     */ 
/*     */           
/* 154 */           List<Supplier<ConfiguredFeature<?, ?>>> debug7 = list.get(i);
/* 155 */           for (Supplier<ConfiguredFeature<?, ?>> debug9 : debug7) {
/* 156 */             debug3.addFeature(i, debug9);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 161 */     BlockState[] debug5 = getLayers();
/* 162 */     for (int debug6 = 0; debug6 < debug5.length; debug6++) {
/* 163 */       BlockState debug7 = debug5[debug6];
/* 164 */       if (debug7 != null && !Heightmap.Types.MOTION_BLOCKING.isOpaque().test(debug7)) {
/* 165 */         this.layers[debug6] = null;
/* 166 */         debug3.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Feature.FILL_LAYER.configured((FeatureConfiguration)new LayerConfiguration(debug6, debug7)));
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     return (new Biome.BiomeBuilder())
/* 171 */       .precipitation(debug1.getPrecipitation())
/* 172 */       .biomeCategory(debug1.getBiomeCategory())
/* 173 */       .depth(debug1.getDepth())
/* 174 */       .scale(debug1.getScale())
/* 175 */       .temperature(debug1.getBaseTemperature())
/* 176 */       .downfall(debug1.getDownfall())
/* 177 */       .specialEffects(debug1.getSpecialEffects())
/* 178 */       .generationSettings(debug3.build())
/* 179 */       .mobSpawnSettings(debug1.getMobSettings())
/* 180 */       .build();
/*     */   }
/*     */   
/*     */   public StructureSettings structureSettings() {
/* 184 */     return this.structureSettings;
/*     */   }
/*     */   
/*     */   public Biome getBiome() {
/* 188 */     return this.biome.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FlatLayerInfo> getLayersInfo() {
/* 196 */     return this.layersInfo;
/*     */   }
/*     */   
/*     */   public BlockState[] getLayers() {
/* 200 */     return this.layers;
/*     */   }
/*     */   
/*     */   public void updateLayers() {
/* 204 */     Arrays.fill((Object[])this.layers, 0, this.layers.length, (Object)null);
/*     */     
/* 206 */     int debug1 = 0;
/*     */     
/* 208 */     for (FlatLayerInfo debug3 : this.layersInfo) {
/* 209 */       debug3.setStart(debug1);
/* 210 */       debug1 += debug3.getHeight();
/*     */     } 
/*     */ 
/*     */     
/* 214 */     this.voidGen = true;
/* 215 */     for (FlatLayerInfo debug2 : this.layersInfo) {
/* 216 */       for (int debug3 = debug2.getStart(); debug3 < debug2.getStart() + debug2.getHeight(); debug3++) {
/* 217 */         BlockState debug4 = debug2.getBlockState();
/* 218 */         if (!debug4.is(Blocks.AIR)) {
/*     */ 
/*     */           
/* 221 */           this.voidGen = false;
/* 222 */           this.layers[debug3] = debug4;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static FlatLevelGeneratorSettings getDefault(Registry<Biome> debug0) {
/* 230 */     StructureSettings debug1 = new StructureSettings(Optional.of(StructureSettings.DEFAULT_STRONGHOLD), Maps.newHashMap((Map)ImmutableMap.of(StructureFeature.VILLAGE, StructureSettings.DEFAULTS.get(StructureFeature.VILLAGE))));
/*     */ 
/*     */     
/* 233 */     FlatLevelGeneratorSettings debug2 = new FlatLevelGeneratorSettings(debug1, debug0);
/*     */     
/* 235 */     debug2.biome = (() -> (Biome)debug0.getOrThrow(Biomes.PLAINS));
/* 236 */     debug2.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
/* 237 */     debug2.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
/* 238 */     debug2.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
/* 239 */     debug2.updateLayers();
/*     */     
/* 241 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLevelGeneratorSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */