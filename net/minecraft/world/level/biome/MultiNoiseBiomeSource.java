/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.worldgen.biome.Biomes;
/*     */ import net.minecraft.resources.RegistryLookupCodec;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public class MultiNoiseBiomeSource extends BiomeSource {
/*     */   static class NoiseParameters {
/*     */     static {
/*  33 */       CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave), (App)Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)).apply((Applicative)debug0, NoiseParameters::new));
/*     */     }
/*     */     private final int firstOctave; private final DoubleList amplitudes;
/*     */     public static final Codec<NoiseParameters> CODEC;
/*     */     
/*     */     public NoiseParameters(int debug1, List<Double> debug2) {
/*  39 */       this.firstOctave = debug1;
/*  40 */       this.amplitudes = (DoubleList)new DoubleArrayList(debug2);
/*     */     }
/*     */     
/*     */     public int firstOctave() {
/*  44 */       return this.firstOctave;
/*     */     }
/*     */     
/*     */     public DoubleList amplitudes() {
/*  48 */       return this.amplitudes;
/*     */     }
/*     */   }
/*     */   
/*  52 */   private static final NoiseParameters DEFAULT_NOISE_PARAMETERS = new NoiseParameters(-7, (List<Double>)ImmutableList.of(Double.valueOf(1.0D), Double.valueOf(1.0D))); public static final MapCodec<MultiNoiseBiomeSource> DIRECT_CODEC;
/*     */   static {
/*  54 */     DIRECT_CODEC = RecordCodecBuilder.mapCodec(debug0 -> debug0.group((App)Codec.LONG.fieldOf("seed").forGetter(()), (App)RecordCodecBuilder.create(()).listOf().fieldOf("biomes").forGetter(()), (App)NoiseParameters.CODEC.fieldOf("temperature_noise").forGetter(()), (App)NoiseParameters.CODEC.fieldOf("humidity_noise").forGetter(()), (App)NoiseParameters.CODEC.fieldOf("altitude_noise").forGetter(()), (App)NoiseParameters.CODEC.fieldOf("weirdness_noise").forGetter(())).apply((Applicative)debug0, MultiNoiseBiomeSource::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     CODEC = Codec.mapEither(PresetInstance.CODEC, DIRECT_CODEC).xmap(debug0 -> (MultiNoiseBiomeSource)debug0.map(PresetInstance::biomeSource, Function.identity()), debug0 -> (Either)debug0.preset().<Either>map(Either::left).orElseGet(())).codec();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<MultiNoiseBiomeSource> CODEC;
/*     */   
/*     */   private final NoiseParameters temperatureParams;
/*     */   
/*     */   private final NoiseParameters humidityParams;
/*     */   private final NoiseParameters altitudeParams;
/*     */   private final NoiseParameters weirdnessParams;
/*     */   private final NormalNoise temperatureNoise;
/*     */   private final NormalNoise humidityNoise;
/*     */   private final NormalNoise altitudeNoise;
/*     */   private final NormalNoise weirdnessNoise;
/*     */   private final List<Pair<Biome.ClimateParameters, Supplier<Biome>>> parameters;
/*     */   private final boolean useY;
/*     */   private final long seed;
/*     */   private final Optional<Pair<Registry<Biome>, Preset>> preset;
/*     */   
/*     */   private MultiNoiseBiomeSource(long debug1, List<Pair<Biome.ClimateParameters, Supplier<Biome>>> debug3, Optional<Pair<Registry<Biome>, Preset>> debug4) {
/*  93 */     this(debug1, debug3, DEFAULT_NOISE_PARAMETERS, DEFAULT_NOISE_PARAMETERS, DEFAULT_NOISE_PARAMETERS, DEFAULT_NOISE_PARAMETERS, debug4);
/*     */   }
/*     */   
/*     */   private MultiNoiseBiomeSource(long debug1, List<Pair<Biome.ClimateParameters, Supplier<Biome>>> debug3, NoiseParameters debug4, NoiseParameters debug5, NoiseParameters debug6, NoiseParameters debug7) {
/*  97 */     this(debug1, debug3, debug4, debug5, debug6, debug7, Optional.empty());
/*     */   }
/*     */   
/*     */   private MultiNoiseBiomeSource(long debug1, List<Pair<Biome.ClimateParameters, Supplier<Biome>>> debug3, NoiseParameters debug4, NoiseParameters debug5, NoiseParameters debug6, NoiseParameters debug7, Optional<Pair<Registry<Biome>, Preset>> debug8) {
/* 101 */     super(debug3.stream().map(Pair::getSecond));
/* 102 */     this.seed = debug1;
/* 103 */     this.preset = debug8;
/*     */     
/* 105 */     this.temperatureParams = debug4;
/* 106 */     this.humidityParams = debug5;
/* 107 */     this.altitudeParams = debug6;
/* 108 */     this.weirdnessParams = debug7;
/*     */     
/* 110 */     this.temperatureNoise = NormalNoise.create(new WorldgenRandom(debug1), debug4.firstOctave(), debug4.amplitudes());
/* 111 */     this.humidityNoise = NormalNoise.create(new WorldgenRandom(debug1 + 1L), debug5.firstOctave(), debug5.amplitudes());
/* 112 */     this.altitudeNoise = NormalNoise.create(new WorldgenRandom(debug1 + 2L), debug6.firstOctave(), debug6.amplitudes());
/* 113 */     this.weirdnessNoise = NormalNoise.create(new WorldgenRandom(debug1 + 3L), debug7.firstOctave(), debug7.amplitudes());
/* 114 */     this.parameters = debug3;
/* 115 */     this.useY = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends BiomeSource> codec() {
/* 120 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<PresetInstance> preset() {
/* 129 */     return this.preset.map(debug1 -> new PresetInstance((Preset)debug1.getSecond(), (Registry)debug1.getFirst(), this.seed));
/*     */   }
/*     */ 
/*     */   
/*     */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/* 134 */     int debug4 = this.useY ? debug2 : 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     Biome.ClimateParameters debug5 = new Biome.ClimateParameters((float)this.temperatureNoise.getValue(debug1, debug4, debug3), (float)this.humidityNoise.getValue(debug1, debug4, debug3), (float)this.altitudeNoise.getValue(debug1, debug4, debug3), (float)this.weirdnessNoise.getValue(debug1, debug4, debug3), 0.0F);
/*     */ 
/*     */ 
/*     */     
/* 144 */     return this.parameters.stream()
/* 145 */       .min(Comparator.comparing(debug1 -> Float.valueOf(((Biome.ClimateParameters)debug1.getFirst()).fitness(debug0))))
/* 146 */       .map(Pair::getSecond)
/* 147 */       .map(Supplier::get)
/* 148 */       .orElse(Biomes.THE_VOID);
/*     */   }
/*     */   
/*     */   public boolean stable(long debug1) {
/* 152 */     return (this.seed == debug1 && this.preset.isPresent() && Objects.equals(((Pair)this.preset.get()).getSecond(), Preset.NETHER));
/*     */   }
/*     */   
/*     */   static final class PresetInstance { static {
/* 156 */       CODEC = RecordCodecBuilder.mapCodec(debug0 -> debug0.group((App)ResourceLocation.CODEC.flatXmap((), ()).fieldOf("preset").stable().forGetter(PresetInstance::preset), (App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(PresetInstance::biomes), (App)Codec.LONG.fieldOf("seed").stable().forGetter(PresetInstance::seed)).apply((Applicative)debug0, debug0.stable(PresetInstance::new)));
/*     */     }
/*     */ 
/*     */     
/*     */     public static final MapCodec<PresetInstance> CODEC;
/*     */     
/*     */     private final MultiNoiseBiomeSource.Preset preset;
/*     */     
/*     */     private final Registry<Biome> biomes;
/*     */     
/*     */     private final long seed;
/*     */ 
/*     */     
/*     */     private PresetInstance(MultiNoiseBiomeSource.Preset debug1, Registry<Biome> debug2, long debug3) {
/* 170 */       this.preset = debug1;
/* 171 */       this.biomes = debug2;
/* 172 */       this.seed = debug3;
/*     */     }
/*     */     
/*     */     public MultiNoiseBiomeSource.Preset preset() {
/* 176 */       return this.preset;
/*     */     }
/*     */     
/*     */     public Registry<Biome> biomes() {
/* 180 */       return this.biomes;
/*     */     }
/*     */     
/*     */     public long seed() {
/* 184 */       return this.seed;
/*     */     }
/*     */     
/*     */     public MultiNoiseBiomeSource biomeSource() {
/* 188 */       return this.preset.biomeSource(this.biomes, this.seed);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Preset {
/* 193 */     private static final Map<ResourceLocation, Preset> BY_NAME = Maps.newHashMap(); public static final Preset NETHER;
/*     */     static {
/* 195 */       NETHER = new Preset(new ResourceLocation("nether"), (debug0, debug1, debug2) -> new MultiNoiseBiomeSource(debug2.longValue(), (List)ImmutableList.of(Pair.of(new Biome.ClimateParameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), ()), Pair.of(new Biome.ClimateParameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F), ()), Pair.of(new Biome.ClimateParameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F), ()), Pair.of(new Biome.ClimateParameters(0.0F, 0.5F, 0.0F, 0.0F, 0.375F), ()), Pair.of(new Biome.ClimateParameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.175F), ())), Optional.of(Pair.of(debug1, debug0))));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final ResourceLocation name;
/*     */ 
/*     */     
/*     */     private final Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> biomeSource;
/*     */ 
/*     */     
/*     */     public Preset(ResourceLocation debug1, Function3<Preset, Registry<Biome>, Long, MultiNoiseBiomeSource> debug2) {
/* 207 */       this.name = debug1;
/* 208 */       this.biomeSource = debug2;
/* 209 */       BY_NAME.put(debug1, this);
/*     */     }
/*     */     
/*     */     public MultiNoiseBiomeSource biomeSource(Registry<Biome> debug1, long debug2) {
/* 213 */       return (MultiNoiseBiomeSource)this.biomeSource.apply(this, debug1, Long.valueOf(debug2));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */