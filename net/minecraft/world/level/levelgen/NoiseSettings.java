/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ public class NoiseSettings {
/*     */   static {
/*   9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 256).fieldOf("height").forGetter(NoiseSettings::height), (App)NoiseSamplingSettings.CODEC.fieldOf("sampling").forGetter(NoiseSettings::noiseSamplingSettings), (App)NoiseSlideSettings.CODEC.fieldOf("top_slide").forGetter(NoiseSettings::topSlideSettings), (App)NoiseSlideSettings.CODEC.fieldOf("bottom_slide").forGetter(NoiseSettings::bottomSlideSettings), (App)Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::noiseSizeHorizontal), (App)Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::noiseSizeVertical), (App)Codec.DOUBLE.fieldOf("density_factor").forGetter(NoiseSettings::densityFactor), (App)Codec.DOUBLE.fieldOf("density_offset").forGetter(NoiseSettings::densityOffset), (App)Codec.BOOL.fieldOf("simplex_surface_noise").forGetter(NoiseSettings::useSimplexSurfaceNoise), (App)Codec.BOOL.optionalFieldOf("random_density_offset", Boolean.valueOf(false), Lifecycle.experimental()).forGetter(NoiseSettings::randomDensityOffset), (App)Codec.BOOL.optionalFieldOf("island_noise_override", Boolean.valueOf(false), Lifecycle.experimental()).forGetter(NoiseSettings::islandNoiseOverride), (App)Codec.BOOL.optionalFieldOf("amplified", Boolean.valueOf(false), Lifecycle.experimental()).forGetter(NoiseSettings::isAmplified)).apply((Applicative)debug0, NoiseSettings::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<NoiseSettings> CODEC;
/*     */ 
/*     */   
/*     */   private final int height;
/*     */ 
/*     */   
/*     */   private final NoiseSamplingSettings noiseSamplingSettings;
/*     */ 
/*     */   
/*     */   private final NoiseSlideSettings topSlideSettings;
/*     */   
/*     */   private final NoiseSlideSettings bottomSlideSettings;
/*     */   
/*     */   private final int noiseSizeHorizontal;
/*     */   
/*     */   private final int noiseSizeVertical;
/*     */   
/*     */   private final double densityFactor;
/*     */   
/*     */   private final double densityOffset;
/*     */   
/*     */   private final boolean useSimplexSurfaceNoise;
/*     */   
/*     */   private final boolean randomDensityOffset;
/*     */   
/*     */   private final boolean islandNoiseOverride;
/*     */   
/*     */   private final boolean isAmplified;
/*     */ 
/*     */   
/*     */   public NoiseSettings(int debug1, NoiseSamplingSettings debug2, NoiseSlideSettings debug3, NoiseSlideSettings debug4, int debug5, int debug6, double debug7, double debug9, boolean debug11, boolean debug12, boolean debug13, boolean debug14) {
/*  45 */     this.height = debug1;
/*     */     
/*  47 */     this.noiseSamplingSettings = debug2;
/*     */     
/*  49 */     this.topSlideSettings = debug3;
/*  50 */     this.bottomSlideSettings = debug4;
/*     */     
/*  52 */     this.noiseSizeHorizontal = debug5;
/*  53 */     this.noiseSizeVertical = debug6;
/*     */     
/*  55 */     this.densityFactor = debug7;
/*  56 */     this.densityOffset = debug9;
/*     */     
/*  58 */     this.useSimplexSurfaceNoise = debug11;
/*  59 */     this.randomDensityOffset = debug12;
/*  60 */     this.islandNoiseOverride = debug13;
/*  61 */     this.isAmplified = debug14;
/*     */   }
/*     */   
/*     */   public int height() {
/*  65 */     return this.height;
/*     */   }
/*     */   
/*     */   public NoiseSamplingSettings noiseSamplingSettings() {
/*  69 */     return this.noiseSamplingSettings;
/*     */   }
/*     */   
/*     */   public NoiseSlideSettings topSlideSettings() {
/*  73 */     return this.topSlideSettings;
/*     */   }
/*     */   
/*     */   public NoiseSlideSettings bottomSlideSettings() {
/*  77 */     return this.bottomSlideSettings;
/*     */   }
/*     */   
/*     */   public int noiseSizeHorizontal() {
/*  81 */     return this.noiseSizeHorizontal;
/*     */   }
/*     */   
/*     */   public int noiseSizeVertical() {
/*  85 */     return this.noiseSizeVertical;
/*     */   }
/*     */   
/*     */   public double densityFactor() {
/*  89 */     return this.densityFactor;
/*     */   }
/*     */   
/*     */   public double densityOffset() {
/*  93 */     return this.densityOffset;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean useSimplexSurfaceNoise() {
/*  98 */     return this.useSimplexSurfaceNoise;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean randomDensityOffset() {
/* 103 */     return this.randomDensityOffset;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean islandNoiseOverride() {
/* 108 */     return this.islandNoiseOverride;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isAmplified() {
/* 113 */     return this.isAmplified;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */