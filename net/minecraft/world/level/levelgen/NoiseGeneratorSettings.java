/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*     */ 
/*     */ public final class NoiseGeneratorSettings {
/*     */   static {
/*  23 */     DIRECT_CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)StructureSettings.CODEC.fieldOf("structures").forGetter(NoiseGeneratorSettings::structureSettings), (App)NoiseSettings.CODEC.fieldOf("noise").forGetter(NoiseGeneratorSettings::noiseSettings), (App)BlockState.CODEC.fieldOf("default_block").forGetter(NoiseGeneratorSettings::getDefaultBlock), (App)BlockState.CODEC.fieldOf("default_fluid").forGetter(NoiseGeneratorSettings::getDefaultFluid), (App)Codec.intRange(-20, 276).fieldOf("bedrock_roof_position").forGetter(NoiseGeneratorSettings::getBedrockRoofPosition), (App)Codec.intRange(-20, 276).fieldOf("bedrock_floor_position").forGetter(NoiseGeneratorSettings::getBedrockFloorPosition), (App)Codec.intRange(0, 255).fieldOf("sea_level").forGetter(NoiseGeneratorSettings::seaLevel), (App)Codec.BOOL.fieldOf("disable_mob_generation").forGetter(NoiseGeneratorSettings::disableMobGeneration)).apply((Applicative)debug0, NoiseGeneratorSettings::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<NoiseGeneratorSettings> DIRECT_CODEC;
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static final Codec<Supplier<NoiseGeneratorSettings>> CODEC = (Codec<Supplier<NoiseGeneratorSettings>>)RegistryFileCodec.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, DIRECT_CODEC);
/*     */   
/*     */   private final StructureSettings structureSettings;
/*     */   
/*     */   private final NoiseSettings noiseSettings;
/*     */   
/*     */   private final BlockState defaultBlock;
/*     */   
/*     */   private final BlockState defaultFluid;
/*     */   private final int bedrockRoofPosition;
/*     */   private final int bedrockFloorPosition;
/*     */   private final int seaLevel;
/*     */   private final boolean disableMobGeneration;
/*     */   
/*     */   private NoiseGeneratorSettings(StructureSettings debug1, NoiseSettings debug2, BlockState debug3, BlockState debug4, int debug5, int debug6, int debug7, boolean debug8) {
/*  49 */     this.structureSettings = debug1;
/*  50 */     this.noiseSettings = debug2;
/*     */     
/*  52 */     this.defaultBlock = debug3;
/*  53 */     this.defaultFluid = debug4;
/*     */     
/*  55 */     this.bedrockRoofPosition = debug5;
/*  56 */     this.bedrockFloorPosition = debug6;
/*  57 */     this.seaLevel = debug7;
/*     */     
/*  59 */     this.disableMobGeneration = debug8;
/*     */   }
/*     */   
/*     */   public StructureSettings structureSettings() {
/*  63 */     return this.structureSettings;
/*     */   }
/*     */   
/*     */   public NoiseSettings noiseSettings() {
/*  67 */     return this.noiseSettings;
/*     */   }
/*     */   
/*     */   public BlockState getDefaultBlock() {
/*  71 */     return this.defaultBlock;
/*     */   }
/*     */   
/*     */   public BlockState getDefaultFluid() {
/*  75 */     return this.defaultFluid;
/*     */   }
/*     */   
/*     */   public int getBedrockRoofPosition() {
/*  79 */     return this.bedrockRoofPosition;
/*     */   }
/*     */   
/*     */   public int getBedrockFloorPosition() {
/*  83 */     return this.bedrockFloorPosition;
/*     */   }
/*     */   
/*     */   public int seaLevel() {
/*  87 */     return this.seaLevel;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected boolean disableMobGeneration() {
/*  92 */     return this.disableMobGeneration;
/*     */   }
/*     */   
/*     */   public boolean stable(ResourceKey<NoiseGeneratorSettings> debug1) {
/*  96 */     return Objects.equals(this, BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(debug1));
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings register(ResourceKey<NoiseGeneratorSettings> debug0, NoiseGeneratorSettings debug1) {
/* 100 */     BuiltinRegistries.register(BuiltinRegistries.NOISE_GENERATOR_SETTINGS, debug0.location(), debug1);
/* 101 */     return debug1;
/*     */   }
/*     */   
/* 104 */   public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("overworld"));
/* 105 */   public static final ResourceKey<NoiseGeneratorSettings> AMPLIFIED = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("amplified"));
/* 106 */   public static final ResourceKey<NoiseGeneratorSettings> NETHER = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("nether"));
/* 107 */   public static final ResourceKey<NoiseGeneratorSettings> END = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("end"));
/* 108 */   public static final ResourceKey<NoiseGeneratorSettings> CAVES = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("caves"));
/* 109 */   public static final ResourceKey<NoiseGeneratorSettings> FLOATING_ISLANDS = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation("floating_islands"));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   private static final NoiseGeneratorSettings BUILTIN_OVERWORLD = register(OVERWORLD, overworld(new StructureSettings(true), false, OVERWORLD.location())); static {
/* 115 */     register(AMPLIFIED, overworld(new StructureSettings(true), true, AMPLIFIED.location()));
/* 116 */     register(NETHER, nether(new StructureSettings(false), Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), NETHER.location()));
/* 117 */     register(END, end(new StructureSettings(false), Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), END.location(), true, true));
/* 118 */     register(CAVES, nether(new StructureSettings(true), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), CAVES.location()));
/* 119 */     register(FLOATING_ISLANDS, end(new StructureSettings(true), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), FLOATING_ISLANDS.location(), false, false));
/*     */   }
/*     */   
/*     */   public static NoiseGeneratorSettings bootstrap() {
/* 123 */     return BUILTIN_OVERWORLD;
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings end(StructureSettings debug0, BlockState debug1, BlockState debug2, ResourceLocation debug3, boolean debug4, boolean debug5) {
/* 127 */     return new NoiseGeneratorSettings(debug0, new NoiseSettings(128, new NoiseSamplingSettings(2.0D, 1.0D, 80.0D, 160.0D), new NoiseSlideSettings(-3000, 64, -46), new NoiseSlideSettings(-30, 7, 1), 2, 1, 0.0D, 0.0D, true, false, debug5, false), debug1, debug2, -10, -10, 0, debug4);
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
/*     */   private static NoiseGeneratorSettings nether(StructureSettings debug0, BlockState debug1, BlockState debug2, ResourceLocation debug3) {
/* 151 */     Map<StructureFeature<?>, StructureFeatureConfiguration> debug4 = Maps.newHashMap((Map)StructureSettings.DEFAULTS);
/* 152 */     debug4.put(StructureFeature.RUINED_PORTAL, new StructureFeatureConfiguration(25, 10, 34222645));
/*     */     
/* 154 */     return new NoiseGeneratorSettings(new StructureSettings(Optional.ofNullable(debug0.stronghold()), debug4), new NoiseSettings(128, new NoiseSamplingSettings(1.0D, 3.0D, 80.0D, 60.0D), new NoiseSlideSettings(120, 3, 0), new NoiseSlideSettings(320, 4, -1), 1, 2, 0.0D, 0.019921875D, false, false, false, false), debug1, debug2, 0, 0, 32, false);
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
/*     */   private static NoiseGeneratorSettings overworld(StructureSettings debug0, boolean debug1, ResourceLocation debug2) {
/* 182 */     double debug3 = 0.9999999814507745D;
/* 183 */     return new NoiseGeneratorSettings(debug0, new NoiseSettings(256, new NoiseSamplingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new NoiseSlideSettings(-10, 3, 0), new NoiseSlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, debug1), Blocks.STONE
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
/* 197 */         .defaultBlockState(), Blocks.WATER
/* 198 */         .defaultBlockState(), -10, 0, 63, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseGeneratorSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */