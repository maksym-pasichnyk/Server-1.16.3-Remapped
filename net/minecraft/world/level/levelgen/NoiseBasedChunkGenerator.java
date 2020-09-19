/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.SurfaceNoise;
/*     */ 
/*     */ public final class NoiseBasedChunkGenerator extends ChunkGenerator {
/*     */   public static final Codec<NoiseBasedChunkGenerator> CODEC;
/*     */   
/*     */   static {
/*  52 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BiomeSource.CODEC.fieldOf("biome_source").forGetter(()), (App)Codec.LONG.fieldOf("seed").stable().forGetter(()), (App)NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(())).apply((Applicative)debug0, debug0.stable(NoiseBasedChunkGenerator::new)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     BEARD_KERNEL = (float[])Util.make(new float[13824], debug0 -> {
/*     */           for (int debug1 = 0; debug1 < 24; debug1++) {
/*     */             for (int debug2 = 0; debug2 < 24; debug2++) {
/*     */               for (int debug3 = 0; debug3 < 24; debug3++) {
/*     */                 debug0[debug1 * 24 * 24 + debug2 * 24 + debug3] = (float)computeContribution(debug2 - 12, debug3 - 12, debug1 - 12);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         });
/*     */     
/*  70 */     BIOME_WEIGHTS = (float[])Util.make(new float[25], debug0 -> {
/*     */           for (int debug1 = -2; debug1 <= 2; debug1++) {
/*     */             for (int debug2 = -2; debug2 <= 2; debug2++) {
/*     */               float debug3 = 10.0F / Mth.sqrt((debug1 * debug1 + debug2 * debug2) + 0.2F);
/*     */               debug0[debug1 + 2 + (debug2 + 2) * 5] = debug3;
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*  79 */   private static final float[] BEARD_KERNEL; private static final float[] BIOME_WEIGHTS; private static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private final int chunkHeight;
/*     */   
/*     */   private final int chunkWidth;
/*     */   
/*     */   private final int chunkCountX;
/*     */   
/*     */   private final int chunkCountY;
/*     */   private final int chunkCountZ;
/*     */   protected final WorldgenRandom random;
/*     */   private final PerlinNoise minLimitPerlinNoise;
/*     */   private final PerlinNoise maxLimitPerlinNoise;
/*     */   private final PerlinNoise mainPerlinNoise;
/*     */   private final SurfaceNoise surfaceNoise;
/*     */   private final PerlinNoise depthNoise;
/*     */   @Nullable
/*     */   private final SimplexNoise islandNoise;
/*     */   protected final BlockState defaultBlock;
/*     */   protected final BlockState defaultFluid;
/*     */   private final long seed;
/*     */   protected final Supplier<NoiseGeneratorSettings> settings;
/*     */   private final int height;
/*     */   
/*     */   public NoiseBasedChunkGenerator(BiomeSource debug1, long debug2, Supplier<NoiseGeneratorSettings> debug4) {
/* 104 */     this(debug1, debug1, debug2, debug4);
/*     */   }
/*     */   
/*     */   private NoiseBasedChunkGenerator(BiomeSource debug1, BiomeSource debug2, long debug3, Supplier<NoiseGeneratorSettings> debug5) {
/* 108 */     super(debug1, debug2, ((NoiseGeneratorSettings)debug5.get()).structureSettings(), debug3);
/* 109 */     this.seed = debug3;
/*     */     
/* 111 */     NoiseGeneratorSettings debug6 = debug5.get();
/*     */     
/* 113 */     this.settings = debug5;
/* 114 */     NoiseSettings debug7 = debug6.noiseSettings();
/* 115 */     this.height = debug7.height();
/*     */     
/* 117 */     this.chunkHeight = debug7.noiseSizeVertical() * 4;
/* 118 */     this.chunkWidth = debug7.noiseSizeHorizontal() * 4;
/* 119 */     this.defaultBlock = debug6.getDefaultBlock();
/* 120 */     this.defaultFluid = debug6.getDefaultFluid();
/*     */     
/* 122 */     this.chunkCountX = 16 / this.chunkWidth;
/* 123 */     this.chunkCountY = debug7.height() / this.chunkHeight;
/* 124 */     this.chunkCountZ = 16 / this.chunkWidth;
/*     */     
/* 126 */     this.random = new WorldgenRandom(debug3);
/* 127 */     this.minLimitPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
/* 128 */     this.maxLimitPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
/* 129 */     this.mainPerlinNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-7, 0));
/* 130 */     this.surfaceNoise = debug7.useSimplexSurfaceNoise() ? (SurfaceNoise)new PerlinSimplexNoise(this.random, IntStream.rangeClosed(-3, 0)) : (SurfaceNoise)new PerlinNoise(this.random, IntStream.rangeClosed(-3, 0));
/*     */     
/* 132 */     this.random.consumeCount(2620);
/* 133 */     this.depthNoise = new PerlinNoise(this.random, IntStream.rangeClosed(-15, 0));
/*     */     
/* 135 */     if (debug7.islandNoiseOverride()) {
/* 136 */       WorldgenRandom debug8 = new WorldgenRandom(debug3);
/*     */       
/* 138 */       debug8.consumeCount(17292);
/* 139 */       this.islandNoise = new SimplexNoise(debug8);
/*     */     } else {
/* 141 */       this.islandNoise = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends ChunkGenerator> codec() {
/* 147 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stable(long debug1, ResourceKey<NoiseGeneratorSettings> debug3) {
/* 156 */     return (this.seed == debug1 && ((NoiseGeneratorSettings)this.settings.get()).stable(debug3));
/*     */   }
/*     */   
/*     */   private double sampleAndClampNoise(int debug1, int debug2, int debug3, double debug4, double debug6, double debug8, double debug10) {
/* 160 */     double debug12 = 0.0D;
/* 161 */     double debug14 = 0.0D;
/* 162 */     double debug16 = 0.0D;
/*     */     
/* 164 */     boolean debug18 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     double debug19 = 1.0D;
/*     */     
/* 174 */     for (int debug21 = 0; debug21 < 16; debug21++) {
/* 175 */       double debug22 = PerlinNoise.wrap(debug1 * debug4 * debug19);
/* 176 */       double debug24 = PerlinNoise.wrap(debug2 * debug6 * debug19);
/* 177 */       double debug26 = PerlinNoise.wrap(debug3 * debug4 * debug19);
/* 178 */       double debug28 = debug6 * debug19;
/* 179 */       ImprovedNoise debug30 = this.minLimitPerlinNoise.getOctaveNoise(debug21);
/* 180 */       if (debug30 != null) {
/* 181 */         debug12 += debug30.noise(debug22, debug24, debug26, debug28, debug2 * debug28) / debug19;
/*     */       }
/* 183 */       ImprovedNoise debug31 = this.maxLimitPerlinNoise.getOctaveNoise(debug21);
/* 184 */       if (debug31 != null) {
/* 185 */         debug14 += debug31.noise(debug22, debug24, debug26, debug28, debug2 * debug28) / debug19;
/*     */       }
/* 187 */       if (debug21 < 8) {
/* 188 */         ImprovedNoise debug32 = this.mainPerlinNoise.getOctaveNoise(debug21);
/* 189 */         if (debug32 != null) {
/* 190 */           debug16 += debug32.noise(PerlinNoise.wrap(debug1 * debug8 * debug19), PerlinNoise.wrap(debug2 * debug10 * debug19), PerlinNoise.wrap(debug3 * debug8 * debug19), debug10 * debug19, debug2 * debug10 * debug19) / debug19;
/*     */         }
/*     */       } 
/* 193 */       debug19 /= 2.0D;
/*     */     } 
/*     */ 
/*     */     
/* 197 */     return Mth.clampedLerp(debug12 / 512.0D, debug14 / 512.0D, (debug16 / 10.0D + 1.0D) / 2.0D);
/*     */   }
/*     */   
/*     */   private double[] makeAndFillNoiseColumn(int debug1, int debug2) {
/* 201 */     double[] debug3 = new double[this.chunkCountY + 1];
/* 202 */     fillNoiseColumn(debug3, debug1, debug2);
/* 203 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillNoiseColumn(double[] debug1, int debug2, int debug3) {
/*     */     double debug4, debug6;
/* 210 */     NoiseSettings debug8 = ((NoiseGeneratorSettings)this.settings.get()).noiseSettings();
/*     */     
/* 212 */     if (this.islandNoise != null) {
/*     */       
/* 214 */       debug4 = (TheEndBiomeSource.getHeightValue(this.islandNoise, debug2, debug3) - 8.0F);
/* 215 */       if (debug4 > 0.0D) {
/* 216 */         debug6 = 0.25D;
/*     */       } else {
/* 218 */         debug6 = 1.0D;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 223 */       float f1 = 0.0F;
/* 224 */       float debug10 = 0.0F;
/* 225 */       float f2 = 0.0F;
/* 226 */       int debug12 = 2;
/* 227 */       int i = getSeaLevel();
/*     */ 
/*     */       
/* 230 */       float debug14 = this.biomeSource.getNoiseBiome(debug2, i, debug3).getDepth();
/* 231 */       for (int j = -2; j <= 2; j++) {
/* 232 */         for (int k = -2; k <= 2; k++) {
/* 233 */           float debug20, f5; Biome biome = this.biomeSource.getNoiseBiome(debug2 + j, i, debug3 + k);
/*     */           
/* 235 */           float debug18 = biome.getDepth();
/* 236 */           float f4 = biome.getScale();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 241 */           if (debug8.isAmplified() && debug18 > 0.0F) {
/* 242 */             debug20 = 1.0F + debug18 * 2.0F;
/* 243 */             f5 = 1.0F + f4 * 4.0F;
/*     */           } else {
/* 245 */             debug20 = debug18;
/* 246 */             f5 = f4;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 255 */           float debug22 = (debug18 > debug14) ? 0.5F : 1.0F;
/* 256 */           float f6 = debug22 * BIOME_WEIGHTS[j + 2 + (k + 2) * 5] / (debug20 + 2.0F);
/*     */           
/* 258 */           f1 += f5 * f6;
/* 259 */           debug10 += debug20 * f6;
/* 260 */           f2 += f6;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 266 */       float f3 = debug10 / f2;
/* 267 */       float debug16 = f1 / f2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 279 */       double d1 = (f3 * 0.5F - 0.125F);
/*     */ 
/*     */       
/* 282 */       double d2 = (debug16 * 0.9F + 0.1F);
/*     */       
/* 284 */       debug4 = d1 * 0.265625D;
/* 285 */       debug6 = 96.0D / d2;
/*     */     } 
/*     */     
/* 288 */     double debug9 = 684.412D * debug8.noiseSamplingSettings().xzScale();
/* 289 */     double debug11 = 684.412D * debug8.noiseSamplingSettings().yScale();
/*     */     
/* 291 */     double debug13 = debug9 / debug8.noiseSamplingSettings().xzFactor();
/* 292 */     double debug15 = debug11 / debug8.noiseSamplingSettings().yFactor();
/*     */     
/* 294 */     double debug17 = debug8.topSlideSettings().target();
/* 295 */     double debug19 = debug8.topSlideSettings().size();
/* 296 */     double debug21 = debug8.topSlideSettings().offset();
/*     */     
/* 298 */     double debug23 = debug8.bottomSlideSettings().target();
/* 299 */     double debug25 = debug8.bottomSlideSettings().size();
/* 300 */     double debug27 = debug8.bottomSlideSettings().offset();
/*     */     
/* 302 */     double debug29 = debug8.randomDensityOffset() ? getRandomDensity(debug2, debug3) : 0.0D;
/*     */     
/* 304 */     double debug31 = debug8.densityFactor();
/* 305 */     double debug33 = debug8.densityOffset();
/*     */ 
/*     */     
/* 308 */     for (int debug35 = 0; debug35 <= this.chunkCountY; debug35++) {
/*     */       
/* 310 */       double debug36 = sampleAndClampNoise(debug2, debug35, debug3, debug9, debug11, debug13, debug15);
/*     */ 
/*     */       
/* 313 */       double debug38 = 1.0D - debug35 * 2.0D / this.chunkCountY + debug29;
/*     */       
/* 315 */       double debug40 = debug38 * debug31 + debug33;
/*     */       
/* 317 */       double debug42 = (debug40 + debug4) * debug6;
/*     */ 
/*     */ 
/*     */       
/* 321 */       if (debug42 > 0.0D) {
/* 322 */         debug36 += debug42 * 4.0D;
/*     */       } else {
/* 324 */         debug36 += debug42;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 339 */       if (debug19 > 0.0D) {
/* 340 */         double debug44 = ((this.chunkCountY - debug35) - debug21) / debug19;
/* 341 */         debug36 = Mth.clampedLerp(debug17, debug36, debug44);
/*     */       } 
/*     */       
/* 344 */       if (debug25 > 0.0D) {
/* 345 */         double debug44 = (debug35 - debug27) / debug25;
/* 346 */         debug36 = Mth.clampedLerp(debug23, debug36, debug44);
/*     */       } 
/*     */       
/* 349 */       debug1[debug35] = debug36;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private double getRandomDensity(int debug1, int debug2) {
/* 355 */     double debug5, debug3 = this.depthNoise.getValue((debug1 * 200), 10.0D, (debug2 * 200), 1.0D, 0.0D, true);
/*     */ 
/*     */ 
/*     */     
/* 359 */     if (debug3 < 0.0D) {
/* 360 */       debug5 = -debug3 * 0.3D;
/*     */     } else {
/* 362 */       debug5 = debug3;
/*     */     } 
/*     */ 
/*     */     
/* 366 */     double debug7 = debug5 * 24.575625D - 2.0D;
/*     */ 
/*     */     
/* 369 */     if (debug7 < 0.0D) {
/* 370 */       return debug7 * 0.009486607142857142D;
/*     */     }
/* 372 */     return Math.min(debug7, 1.0D) * 0.006640625D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int debug1, int debug2, Heightmap.Types debug3) {
/* 378 */     return iterateNoiseColumn(debug1, debug2, null, debug3.isOpaque());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getBaseColumn(int debug1, int debug2) {
/* 383 */     BlockState[] debug3 = new BlockState[this.chunkCountY * this.chunkHeight];
/* 384 */     iterateNoiseColumn(debug1, debug2, debug3, null);
/* 385 */     return (BlockGetter)new NoiseColumn(debug3);
/*     */   }
/*     */   
/*     */   private int iterateNoiseColumn(int debug1, int debug2, @Nullable BlockState[] debug3, @Nullable Predicate<BlockState> debug4) {
/* 389 */     int debug5 = Math.floorDiv(debug1, this.chunkWidth);
/* 390 */     int debug6 = Math.floorDiv(debug2, this.chunkWidth);
/* 391 */     int debug7 = Math.floorMod(debug1, this.chunkWidth);
/* 392 */     int debug8 = Math.floorMod(debug2, this.chunkWidth);
/*     */     
/* 394 */     double debug9 = debug7 / this.chunkWidth;
/* 395 */     double debug11 = debug8 / this.chunkWidth;
/*     */     
/* 397 */     double[][] debug13 = new double[4][];
/*     */     
/* 399 */     debug13[0] = makeAndFillNoiseColumn(debug5, debug6);
/* 400 */     debug13[1] = makeAndFillNoiseColumn(debug5, debug6 + 1);
/* 401 */     debug13[2] = makeAndFillNoiseColumn(debug5 + 1, debug6);
/* 402 */     debug13[3] = makeAndFillNoiseColumn(debug5 + 1, debug6 + 1);
/*     */     
/* 404 */     for (int debug14 = this.chunkCountY - 1; debug14 >= 0; debug14--) {
/* 405 */       double debug15 = debug13[0][debug14];
/* 406 */       double debug17 = debug13[1][debug14];
/* 407 */       double debug19 = debug13[2][debug14];
/* 408 */       double debug21 = debug13[3][debug14];
/*     */       
/* 410 */       double debug23 = debug13[0][debug14 + 1];
/* 411 */       double debug25 = debug13[1][debug14 + 1];
/* 412 */       double debug27 = debug13[2][debug14 + 1];
/* 413 */       double debug29 = debug13[3][debug14 + 1];
/*     */       
/* 415 */       for (int debug31 = this.chunkHeight - 1; debug31 >= 0; debug31--) {
/* 416 */         double debug32 = debug31 / this.chunkHeight;
/*     */         
/* 418 */         double debug34 = Mth.lerp3(debug32, debug9, debug11, debug15, debug23, debug19, debug27, debug17, debug25, debug21, debug29);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 424 */         int debug36 = debug14 * this.chunkHeight + debug31;
/*     */         
/* 426 */         BlockState debug37 = generateBaseState(debug34, debug36);
/* 427 */         if (debug3 != null) {
/* 428 */           debug3[debug36] = debug37;
/*     */         }
/* 430 */         if (debug4 != null && debug4.test(debug37)) {
/* 431 */           return debug36 + 1;
/*     */         }
/*     */       } 
/*     */     } 
/* 435 */     return 0;
/*     */   }
/*     */   
/*     */   protected BlockState generateBaseState(double debug1, int debug3) {
/*     */     BlockState debug4;
/* 440 */     if (debug1 > 0.0D) {
/* 441 */       debug4 = this.defaultBlock;
/*     */     }
/* 443 */     else if (debug3 < getSeaLevel()) {
/* 444 */       debug4 = this.defaultFluid;
/*     */     } else {
/* 446 */       debug4 = AIR;
/*     */     } 
/*     */     
/* 449 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildSurfaceAndBedrock(WorldGenRegion debug1, ChunkAccess debug2) {
/* 454 */     ChunkPos debug3 = debug2.getPos();
/* 455 */     int debug4 = debug3.x;
/* 456 */     int debug5 = debug3.z;
/*     */     
/* 458 */     WorldgenRandom debug6 = new WorldgenRandom();
/* 459 */     debug6.setBaseChunkSeed(debug4, debug5);
/*     */     
/* 461 */     ChunkPos debug7 = debug2.getPos();
/* 462 */     int debug8 = debug7.getMinBlockX();
/* 463 */     int debug9 = debug7.getMinBlockZ();
/* 464 */     double debug10 = 0.0625D;
/*     */     
/* 466 */     BlockPos.MutableBlockPos debug12 = new BlockPos.MutableBlockPos();
/*     */     
/* 468 */     for (int debug13 = 0; debug13 < 16; debug13++) {
/* 469 */       for (int debug14 = 0; debug14 < 16; debug14++) {
/* 470 */         int debug15 = debug8 + debug13;
/* 471 */         int debug16 = debug9 + debug14;
/* 472 */         int debug17 = debug2.getHeight(Heightmap.Types.WORLD_SURFACE_WG, debug13, debug14) + 1;
/*     */ 
/*     */         
/* 475 */         double debug18 = this.surfaceNoise.getSurfaceNoiseValue(debug15 * 0.0625D, debug16 * 0.0625D, 0.0625D, debug13 * 0.0625D) * 15.0D;
/* 476 */         debug1.getBiome((BlockPos)debug12.set(debug8 + debug13, debug17, debug9 + debug14)).buildSurfaceAt(debug6, debug2, debug15, debug16, debug17, debug18, this.defaultBlock, this.defaultFluid, getSeaLevel(), debug1.getSeed());
/*     */       } 
/*     */     } 
/* 479 */     setBedrock(debug2, debug6);
/*     */   }
/*     */   
/*     */   private void setBedrock(ChunkAccess debug1, Random debug2) {
/* 483 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/* 484 */     int debug4 = debug1.getPos().getMinBlockX();
/* 485 */     int debug5 = debug1.getPos().getMinBlockZ();
/*     */     
/* 487 */     NoiseGeneratorSettings debug6 = this.settings.get();
/*     */     
/* 489 */     int debug7 = debug6.getBedrockFloorPosition();
/*     */     
/* 491 */     int debug8 = this.height - 1 - debug6.getBedrockRoofPosition();
/*     */     
/* 493 */     int debug9 = 5;
/*     */     
/* 495 */     boolean debug10 = (debug8 + 4 >= 0 && debug8 < this.height);
/* 496 */     boolean debug11 = (debug7 + 4 >= 0 && debug7 < this.height);
/*     */     
/* 498 */     if (!debug10 && !debug11) {
/*     */       return;
/*     */     }
/*     */     
/* 502 */     for (BlockPos debug13 : BlockPos.betweenClosed(debug4, 0, debug5, debug4 + 15, 0, debug5 + 15)) {
/* 503 */       if (debug10) {
/* 504 */         for (int debug14 = 0; debug14 < 5; debug14++) {
/* 505 */           if (debug14 <= debug2.nextInt(5)) {
/* 506 */             debug1.setBlockState((BlockPos)debug3.set(debug13.getX(), debug8 - debug14, debug13.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 511 */       if (debug11) {
/* 512 */         for (int debug14 = 4; debug14 >= 0; debug14--) {
/* 513 */           if (debug14 <= debug2.nextInt(5)) {
/* 514 */             debug1.setBlockState((BlockPos)debug3.set(debug13.getX(), debug7 + debug14, debug13.getZ()), Blocks.BEDROCK.defaultBlockState(), false);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillFromNoise(LevelAccessor debug1, StructureFeatureManager debug2, ChunkAccess debug3) {
/* 523 */     ObjectArrayList objectArrayList1 = new ObjectArrayList(10);
/* 524 */     ObjectArrayList objectArrayList2 = new ObjectArrayList(32);
/*     */     
/* 526 */     ChunkPos debug6 = debug3.getPos();
/*     */     
/* 528 */     int debug7 = debug6.x;
/* 529 */     int debug8 = debug6.z;
/*     */     
/* 531 */     int debug9 = debug7 << 4;
/* 532 */     int debug10 = debug8 << 4;
/*     */     
/* 534 */     for (Iterator<StructureFeature> iterator = StructureFeature.NOISE_AFFECTING_FEATURES.iterator(); iterator.hasNext(); ) { StructureFeature<?> structureFeature = iterator.next();
/*     */       
/* 536 */       debug2.startsForFeature(SectionPos.of(debug6, 0), structureFeature).forEach(debug5 -> {
/*     */             for (StructurePiece debug7 : debug5.getPieces()) {
/*     */               if (!debug7.isCloseToChunk(debug0, 12)) {
/*     */                 continue;
/*     */               }
/*     */               
/*     */               if (debug7 instanceof PoolElementStructurePiece) {
/*     */                 PoolElementStructurePiece debug8 = (PoolElementStructurePiece)debug7;
/*     */                 
/*     */                 StructureTemplatePool.Projection debug9 = debug8.getElement().getProjection();
/*     */                 
/*     */                 if (debug9 == StructureTemplatePool.Projection.RIGID) {
/*     */                   debug1.add(debug8);
/*     */                 }
/*     */                 
/*     */                 for (JigsawJunction debug11 : debug8.getJunctions()) {
/*     */                   int debug12 = debug11.getSourceX();
/*     */                   
/*     */                   int debug13 = debug11.getSourceZ();
/*     */                   
/*     */                   if (debug12 <= debug2 - 12 || debug13 <= debug3 - 12 || debug12 >= debug2 + 15 + 12 || debug13 >= debug3 + 15 + 12) {
/*     */                     continue;
/*     */                   }
/*     */                   
/*     */                   debug4.add(debug11);
/*     */                 } 
/*     */                 
/*     */                 continue;
/*     */               } 
/*     */               debug1.add(debug7);
/*     */             } 
/*     */           }); }
/*     */     
/* 569 */     double[][][] debug11 = new double[2][this.chunkCountZ + 1][this.chunkCountY + 1];
/*     */     
/* 571 */     for (int i = 0; i < this.chunkCountZ + 1; i++) {
/* 572 */       debug11[0][i] = new double[this.chunkCountY + 1];
/* 573 */       fillNoiseColumn(debug11[0][i], debug7 * this.chunkCountX, debug8 * this.chunkCountZ + i);
/* 574 */       debug11[1][i] = new double[this.chunkCountY + 1];
/*     */     } 
/*     */     
/* 577 */     ProtoChunk debug12 = (ProtoChunk)debug3;
/*     */     
/* 579 */     Heightmap debug13 = debug12.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/* 580 */     Heightmap debug14 = debug12.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/* 582 */     BlockPos.MutableBlockPos debug15 = new BlockPos.MutableBlockPos();
/* 583 */     ObjectListIterator<StructurePiece> debug16 = objectArrayList1.iterator();
/* 584 */     ObjectListIterator<JigsawJunction> debug17 = objectArrayList2.iterator();
/*     */     
/* 586 */     for (int debug18 = 0; debug18 < this.chunkCountX; debug18++) {
/* 587 */       int j; for (j = 0; j < this.chunkCountZ + 1; j++) {
/* 588 */         fillNoiseColumn(debug11[1][j], debug7 * this.chunkCountX + debug18 + 1, debug8 * this.chunkCountZ + j);
/*     */       }
/*     */       
/* 591 */       for (j = 0; j < this.chunkCountZ; j++) {
/* 592 */         LevelChunkSection debug20 = debug12.getOrCreateSection(15);
/* 593 */         debug20.acquire();
/*     */         
/* 595 */         for (int debug21 = this.chunkCountY - 1; debug21 >= 0; debug21--) {
/* 596 */           double debug22 = debug11[0][j][debug21];
/* 597 */           double debug24 = debug11[0][j + 1][debug21];
/* 598 */           double debug26 = debug11[1][j][debug21];
/* 599 */           double debug28 = debug11[1][j + 1][debug21];
/*     */           
/* 601 */           double debug30 = debug11[0][j][debug21 + 1];
/* 602 */           double debug32 = debug11[0][j + 1][debug21 + 1];
/* 603 */           double debug34 = debug11[1][j][debug21 + 1];
/* 604 */           double debug36 = debug11[1][j + 1][debug21 + 1];
/*     */           
/* 606 */           for (int debug38 = this.chunkHeight - 1; debug38 >= 0; debug38--) {
/* 607 */             int debug39 = debug21 * this.chunkHeight + debug38;
/* 608 */             int debug40 = debug39 & 0xF;
/*     */             
/* 610 */             int debug41 = debug39 >> 4;
/* 611 */             if (debug20.bottomBlockY() >> 4 != debug41) {
/* 612 */               debug20.release();
/* 613 */               debug20 = debug12.getOrCreateSection(debug41);
/* 614 */               debug20.acquire();
/*     */             } 
/*     */             
/* 617 */             double debug42 = debug38 / this.chunkHeight;
/* 618 */             double debug44 = Mth.lerp(debug42, debug22, debug30);
/* 619 */             double debug46 = Mth.lerp(debug42, debug26, debug34);
/* 620 */             double debug48 = Mth.lerp(debug42, debug24, debug32);
/* 621 */             double debug50 = Mth.lerp(debug42, debug28, debug36);
/*     */             
/* 623 */             for (int debug52 = 0; debug52 < this.chunkWidth; debug52++) {
/* 624 */               int debug53 = debug9 + debug18 * this.chunkWidth + debug52;
/* 625 */               int debug54 = debug53 & 0xF;
/*     */               
/* 627 */               double debug55 = debug52 / this.chunkWidth;
/* 628 */               double debug57 = Mth.lerp(debug55, debug44, debug46);
/* 629 */               double debug59 = Mth.lerp(debug55, debug48, debug50);
/*     */               
/* 631 */               for (int debug61 = 0; debug61 < this.chunkWidth; debug61++) {
/* 632 */                 int debug62 = debug10 + j * this.chunkWidth + debug61;
/* 633 */                 int debug63 = debug62 & 0xF;
/*     */                 
/* 635 */                 double debug64 = debug61 / this.chunkWidth;
/* 636 */                 double debug66 = Mth.lerp(debug64, debug57, debug59);
/*     */ 
/*     */                 
/* 639 */                 double debug68 = Mth.clamp(debug66 / 200.0D, -1.0D, 1.0D);
/* 640 */                 debug68 = debug68 / 2.0D - debug68 * debug68 * debug68 / 24.0D;
/*     */                 
/* 642 */                 while (debug16.hasNext()) {
/* 643 */                   StructurePiece structurePiece = (StructurePiece)debug16.next();
/* 644 */                   BoundingBox debug71 = structurePiece.getBoundingBox();
/* 645 */                   int debug72 = Math.max(0, Math.max(debug71.x0 - debug53, debug53 - debug71.x1));
/* 646 */                   int debug73 = debug39 - debug71.y0 + ((structurePiece instanceof PoolElementStructurePiece) ? ((PoolElementStructurePiece)structurePiece).getGroundLevelDelta() : 0);
/* 647 */                   int debug74 = Math.max(0, Math.max(debug71.z0 - debug62, debug62 - debug71.z1));
/* 648 */                   debug68 += getContribution(debug72, debug73, debug74) * 0.8D;
/*     */                 } 
/* 650 */                 debug16.back(objectArrayList1.size());
/*     */                 
/* 652 */                 while (debug17.hasNext()) {
/* 653 */                   JigsawJunction jigsawJunction = (JigsawJunction)debug17.next();
/* 654 */                   int debug71 = debug53 - jigsawJunction.getSourceX();
/* 655 */                   int debug72 = debug39 - jigsawJunction.getSourceGroundY();
/* 656 */                   int debug73 = debug62 - jigsawJunction.getSourceZ();
/* 657 */                   debug68 += getContribution(debug71, debug72, debug73) * 0.4D;
/*     */                 } 
/* 659 */                 debug17.back(objectArrayList2.size());
/*     */                 
/* 661 */                 BlockState debug70 = generateBaseState(debug68, debug39);
/*     */                 
/* 663 */                 if (debug70 != AIR) {
/* 664 */                   if (debug70.getLightEmission() != 0) {
/* 665 */                     debug15.set(debug53, debug39, debug62);
/* 666 */                     debug12.addLight((BlockPos)debug15);
/*     */                   } 
/* 668 */                   debug20.setBlockState(debug54, debug40, debug63, debug70, false);
/* 669 */                   debug13.update(debug54, debug39, debug63, debug70);
/* 670 */                   debug14.update(debug54, debug39, debug63, debug70);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 676 */         debug20.release();
/*     */       } 
/*     */       
/* 679 */       double[][] debug19 = debug11[0];
/* 680 */       debug11[0] = debug11[1];
/* 681 */       debug11[1] = debug19;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static double getContribution(int debug0, int debug1, int debug2) {
/* 686 */     int debug3 = debug0 + 12;
/* 687 */     int debug4 = debug1 + 12;
/* 688 */     int debug5 = debug2 + 12;
/* 689 */     if (debug3 < 0 || debug3 >= 24) {
/* 690 */       return 0.0D;
/*     */     }
/* 692 */     if (debug4 < 0 || debug4 >= 24) {
/* 693 */       return 0.0D;
/*     */     }
/* 695 */     if (debug5 < 0 || debug5 >= 24) {
/* 696 */       return 0.0D;
/*     */     }
/*     */     
/* 699 */     return BEARD_KERNEL[debug5 * 24 * 24 + debug3 * 24 + debug4];
/*     */   }
/*     */   
/*     */   private static double computeContribution(int debug0, int debug1, int debug2) {
/* 703 */     double debug3 = (debug0 * debug0 + debug2 * debug2);
/*     */ 
/*     */     
/* 706 */     double debug5 = debug1 + 0.5D;
/* 707 */     double debug7 = debug5 * debug5;
/*     */     
/* 709 */     double debug9 = Math.pow(Math.E, -(debug7 / 16.0D + debug3 / 16.0D));
/* 710 */     double debug11 = -debug5 * Mth.fastInvSqrt(debug7 / 2.0D + debug3 / 2.0D) / 2.0D;
/*     */     
/* 712 */     return debug11 * debug9;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGenDepth() {
/* 717 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 722 */     return ((NoiseGeneratorSettings)this.settings.get()).seaLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MobSpawnSettings.SpawnerData> getMobsAt(Biome debug1, StructureFeatureManager debug2, MobCategory debug3, BlockPos debug4) {
/* 728 */     if (debug2.getStructureAt(debug4, true, (StructureFeature)StructureFeature.SWAMP_HUT).isValid()) {
/* 729 */       if (debug3 == MobCategory.MONSTER)
/* 730 */         return StructureFeature.SWAMP_HUT.getSpecialEnemies(); 
/* 731 */       if (debug3 == MobCategory.CREATURE) {
/* 732 */         return StructureFeature.SWAMP_HUT.getSpecialAnimals();
/*     */       }
/*     */     } 
/* 735 */     if (debug3 == MobCategory.MONSTER) {
/* 736 */       if (debug2.getStructureAt(debug4, false, StructureFeature.PILLAGER_OUTPOST).isValid())
/* 737 */         return StructureFeature.PILLAGER_OUTPOST.getSpecialEnemies(); 
/* 738 */       if (debug2.getStructureAt(debug4, false, StructureFeature.OCEAN_MONUMENT).isValid())
/* 739 */         return StructureFeature.OCEAN_MONUMENT.getSpecialEnemies(); 
/* 740 */       if (debug2.getStructureAt(debug4, true, StructureFeature.NETHER_BRIDGE).isValid()) {
/* 741 */         return StructureFeature.NETHER_BRIDGE.getSpecialEnemies();
/*     */       }
/*     */     } 
/*     */     
/* 745 */     return super.getMobsAt(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion debug1) {
/* 751 */     if (((NoiseGeneratorSettings)this.settings.get()).disableMobGeneration()) {
/*     */       return;
/*     */     }
/* 754 */     int debug2 = debug1.getCenterX();
/* 755 */     int debug3 = debug1.getCenterZ();
/* 756 */     Biome debug4 = debug1.getBiome((new ChunkPos(debug2, debug3)).getWorldPosition());
/*     */     
/* 758 */     WorldgenRandom debug5 = new WorldgenRandom();
/* 759 */     debug5.setDecorationSeed(debug1.getSeed(), debug2 << 4, debug3 << 4);
/* 760 */     NaturalSpawner.spawnMobsForChunkGeneration((ServerLevelAccessor)debug1, debug4, debug2, debug3, debug5);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseBasedChunkGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */