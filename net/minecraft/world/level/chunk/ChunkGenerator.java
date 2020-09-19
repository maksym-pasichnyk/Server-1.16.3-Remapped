/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.worldgen.StructureFeatures;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.StructureSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ 
/*     */ public abstract class ChunkGenerator {
/*     */   static {
/*  53 */     Registry.register(Registry.CHUNK_GENERATOR, "noise", NoiseBasedChunkGenerator.CODEC);
/*  54 */     Registry.register(Registry.CHUNK_GENERATOR, "flat", FlatLevelSource.CODEC);
/*  55 */     Registry.register(Registry.CHUNK_GENERATOR, "debug", DebugLevelSource.CODEC);
/*     */   }
/*     */   
/*  58 */   public static final Codec<ChunkGenerator> CODEC = Registry.CHUNK_GENERATOR.dispatchStable(ChunkGenerator::codec, Function.identity());
/*     */   
/*     */   protected final BiomeSource biomeSource;
/*     */   
/*     */   protected final BiomeSource runtimeBiomeSource;
/*     */   private final StructureSettings settings;
/*     */   private final long strongholdSeed;
/*  65 */   private final List<ChunkPos> strongholdPositions = Lists.newArrayList();
/*     */   
/*     */   public ChunkGenerator(BiomeSource debug1, StructureSettings debug2) {
/*  68 */     this(debug1, debug1, debug2, 0L);
/*     */   }
/*     */   
/*     */   public ChunkGenerator(BiomeSource debug1, BiomeSource debug2, StructureSettings debug3, long debug4) {
/*  72 */     this.biomeSource = debug1;
/*  73 */     this.runtimeBiomeSource = debug2;
/*  74 */     this.settings = debug3;
/*  75 */     this.strongholdSeed = debug4;
/*     */   }
/*     */   
/*     */   private void generateStrongholds() {
/*  79 */     if (!this.strongholdPositions.isEmpty()) {
/*     */       return;
/*     */     }
/*  82 */     StrongholdConfiguration debug1 = this.settings.stronghold();
/*  83 */     if (debug1 == null || debug1.count() == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  87 */     List<Biome> debug2 = Lists.newArrayList();
/*     */     
/*  89 */     for (Biome biome : this.biomeSource.possibleBiomes()) {
/*  90 */       if (biome.getGenerationSettings().isValidStart(StructureFeature.STRONGHOLD)) {
/*  91 */         debug2.add(biome);
/*     */       }
/*     */     } 
/*     */     
/*  95 */     int debug3 = debug1.distance();
/*  96 */     int debug4 = debug1.count();
/*  97 */     int debug5 = debug1.spread();
/*     */     
/*  99 */     Random debug6 = new Random();
/* 100 */     debug6.setSeed(this.strongholdSeed);
/*     */     
/* 102 */     double debug7 = debug6.nextDouble() * Math.PI * 2.0D;
/*     */     
/* 104 */     int debug9 = 0;
/* 105 */     int debug10 = 0;
/* 106 */     for (int debug11 = 0; debug11 < debug4; debug11++) {
/* 107 */       double debug12 = (4 * debug3 + debug3 * debug10 * 6) + (debug6.nextDouble() - 0.5D) * debug3 * 2.5D;
/* 108 */       int debug14 = (int)Math.round(Math.cos(debug7) * debug12);
/* 109 */       int debug15 = (int)Math.round(Math.sin(debug7) * debug12);
/*     */       
/* 111 */       BlockPos debug16 = this.biomeSource.findBiomeHorizontal((debug14 << 4) + 8, 0, (debug15 << 4) + 8, 112, debug2::contains, debug6);
/* 112 */       if (debug16 != null) {
/* 113 */         debug14 = debug16.getX() >> 4;
/* 114 */         debug15 = debug16.getZ() >> 4;
/*     */       } 
/*     */       
/* 117 */       this.strongholdPositions.add(new ChunkPos(debug14, debug15));
/*     */       
/* 119 */       debug7 += 6.283185307179586D / debug5;
/*     */       
/* 121 */       if (++debug9 == debug5) {
/* 122 */         debug10++;
/* 123 */         debug9 = 0;
/* 124 */         debug5 += 2 * debug5 / (debug10 + 1);
/* 125 */         debug5 = Math.min(debug5, debug4 - debug11);
/* 126 */         debug7 += debug6.nextDouble() * Math.PI * 2.0D;
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
/*     */   public void createBiomes(Registry<Biome> debug1, ChunkAccess debug2) {
/* 139 */     ChunkPos debug3 = debug2.getPos();
/* 140 */     ((ProtoChunk)debug2).setBiomes(new ChunkBiomeContainer((IdMap<Biome>)debug1, debug3, this.runtimeBiomeSource));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyCarvers(long debug1, BiomeManager debug3, ChunkAccess debug4, GenerationStep.Carving debug5) {
/* 147 */     BiomeManager debug6 = debug3.withDifferentSource(this.biomeSource);
/*     */     
/* 149 */     WorldgenRandom debug7 = new WorldgenRandom();
/* 150 */     int debug8 = 8;
/*     */     
/* 152 */     ChunkPos debug9 = debug4.getPos();
/* 153 */     int debug10 = debug9.x;
/* 154 */     int debug11 = debug9.z;
/*     */     
/* 156 */     BiomeGenerationSettings debug12 = this.biomeSource.getNoiseBiome(debug9.x << 2, 0, debug9.z << 2).getGenerationSettings();
/*     */     
/* 158 */     BitSet debug13 = ((ProtoChunk)debug4).getOrCreateCarvingMask(debug5);
/* 159 */     for (int debug14 = debug10 - 8; debug14 <= debug10 + 8; debug14++) {
/* 160 */       for (int debug15 = debug11 - 8; debug15 <= debug11 + 8; debug15++) {
/* 161 */         List<Supplier<ConfiguredWorldCarver<?>>> debug16 = debug12.getCarvers(debug5);
/*     */         
/* 163 */         ListIterator<Supplier<ConfiguredWorldCarver<?>>> debug17 = debug16.listIterator();
/* 164 */         while (debug17.hasNext()) {
/* 165 */           int debug18 = debug17.nextIndex();
/* 166 */           ConfiguredWorldCarver<?> debug19 = ((Supplier<ConfiguredWorldCarver>)debug17.next()).get();
/* 167 */           debug7.setLargeFeatureSeed(debug1 + debug18, debug14, debug15);
/* 168 */           if (debug19.isStartChunk((Random)debug7, debug14, debug15)) {
/* 169 */             debug19.carve(debug4, debug6::getBiome, (Random)debug7, getSeaLevel(), debug14, debug15, debug10, debug11, debug13);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos findNearestMapFeature(ServerLevel debug1, StructureFeature<?> debug2, BlockPos debug3, int debug4, boolean debug5) {
/* 178 */     if (!this.biomeSource.canGenerateStructure(debug2)) {
/* 179 */       return null;
/*     */     }
/* 181 */     if (debug2 == StructureFeature.STRONGHOLD) {
/* 182 */       generateStrongholds();
/* 183 */       BlockPos blockPos = null;
/* 184 */       double debug7 = Double.MAX_VALUE;
/* 185 */       BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/* 186 */       for (ChunkPos debug11 : this.strongholdPositions) {
/* 187 */         debug9.set((debug11.x << 4) + 8, 32, (debug11.z << 4) + 8);
/* 188 */         double debug12 = debug9.distSqr((Vec3i)debug3);
/* 189 */         if (blockPos == null) {
/* 190 */           blockPos = new BlockPos((Vec3i)debug9);
/* 191 */           debug7 = debug12; continue;
/*     */         } 
/* 193 */         if (debug12 < debug7) {
/* 194 */           blockPos = new BlockPos((Vec3i)debug9);
/* 195 */           debug7 = debug12;
/*     */         } 
/*     */       } 
/*     */       
/* 199 */       return blockPos;
/*     */     } 
/* 201 */     StructureFeatureConfiguration debug6 = this.settings.getConfig(debug2);
/* 202 */     if (debug6 == null) {
/* 203 */       return null;
/*     */     }
/*     */     
/* 206 */     return debug2.getNearestGeneratedFeature((LevelReader)debug1, debug1.structureFeatureManager(), debug3, debug4, debug5, debug1.getSeed(), debug6);
/*     */   }
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenRegion debug1, StructureFeatureManager debug2) {
/* 210 */     int debug3 = debug1.getCenterX();
/* 211 */     int debug4 = debug1.getCenterZ();
/* 212 */     int debug5 = debug3 * 16;
/* 213 */     int debug6 = debug4 * 16;
/* 214 */     BlockPos debug7 = new BlockPos(debug5, 0, debug6);
/* 215 */     Biome debug8 = this.biomeSource.getNoiseBiome((debug3 << 2) + 2, 2, (debug4 << 2) + 2);
/*     */     
/* 217 */     WorldgenRandom debug9 = new WorldgenRandom();
/* 218 */     long debug10 = debug9.setDecorationSeed(debug1.getSeed(), debug5, debug6);
/*     */     try {
/* 220 */       debug8.generate(debug2, this, debug1, debug10, debug9, debug7);
/* 221 */     } catch (Exception debug12) {
/* 222 */       CrashReport debug13 = CrashReport.forThrowable(debug12, "Biome decoration");
/* 223 */       debug13.addCategory("Generation")
/* 224 */         .setDetail("CenterX", Integer.valueOf(debug3))
/* 225 */         .setDetail("CenterZ", Integer.valueOf(debug4))
/* 226 */         .setDetail("Seed", Long.valueOf(debug10))
/* 227 */         .setDetail("Biome", debug8);
/* 228 */       throw new ReportedException(debug13);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureSettings getSettings() {
/* 241 */     return this.settings;
/*     */   }
/*     */   
/*     */   public int getSpawnHeight() {
/* 245 */     return 64;
/*     */   }
/*     */   
/*     */   public BiomeSource getBiomeSource() {
/* 249 */     return this.runtimeBiomeSource;
/*     */   }
/*     */   
/*     */   public int getGenDepth() {
/* 253 */     return 256;
/*     */   }
/*     */   
/*     */   public List<MobSpawnSettings.SpawnerData> getMobsAt(Biome debug1, StructureFeatureManager debug2, MobCategory debug3, BlockPos debug4) {
/* 257 */     return debug1.getMobSettings().getMobs(debug3);
/*     */   }
/*     */   
/*     */   public void createStructures(RegistryAccess debug1, StructureFeatureManager debug2, ChunkAccess debug3, StructureManager debug4, long debug5) {
/* 261 */     ChunkPos debug7 = debug3.getPos();
/* 262 */     Biome debug8 = this.biomeSource.getNoiseBiome((debug7.x << 2) + 2, 0, (debug7.z << 2) + 2);
/*     */     
/* 264 */     createStructure(StructureFeatures.STRONGHOLD, debug1, debug2, debug3, debug4, debug5, debug7, debug8);
/*     */     
/* 266 */     for (Supplier<ConfiguredStructureFeature<?, ?>> debug10 : (Iterable<Supplier<ConfiguredStructureFeature<?, ?>>>)debug8.getGenerationSettings().structures()) {
/* 267 */       createStructure(debug10.get(), debug1, debug2, debug3, debug4, debug5, debug7, debug8);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void createStructure(ConfiguredStructureFeature<?, ?> debug1, RegistryAccess debug2, StructureFeatureManager debug3, ChunkAccess debug4, StructureManager debug5, long debug6, ChunkPos debug8, Biome debug9) {
/* 273 */     StructureStart<?> debug10 = debug3.getStartForFeature(SectionPos.of(debug4.getPos(), 0), debug1.feature, debug4);
/* 274 */     int debug11 = (debug10 != null) ? debug10.getReferences() : 0;
/*     */     
/* 276 */     StructureFeatureConfiguration debug12 = this.settings.getConfig(debug1.feature);
/* 277 */     if (debug12 != null) {
/* 278 */       StructureStart<?> debug13 = debug1.generate(debug2, this, this.biomeSource, debug5, debug6, debug8, debug9, debug11, debug12);
/* 279 */       debug3.setStartForFeature(SectionPos.of(debug4.getPos(), 0), debug1.feature, debug13, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createReferences(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkAccess debug3) {
/* 284 */     int debug4 = 8;
/* 285 */     int debug5 = (debug3.getPos()).x;
/* 286 */     int debug6 = (debug3.getPos()).z;
/* 287 */     int debug7 = debug5 << 4;
/* 288 */     int debug8 = debug6 << 4;
/*     */     
/* 290 */     SectionPos debug9 = SectionPos.of(debug3.getPos(), 0);
/*     */     
/* 292 */     for (int debug10 = debug5 - 8; debug10 <= debug5 + 8; debug10++) {
/* 293 */       for (int debug11 = debug6 - 8; debug11 <= debug6 + 8; debug11++) {
/* 294 */         long debug12 = ChunkPos.asLong(debug10, debug11);
/*     */         
/* 296 */         for (StructureStart<?> debug15 : debug1.getChunk(debug10, debug11).getAllStarts().values()) {
/*     */           try {
/* 298 */             if (debug15 != StructureStart.INVALID_START && debug15.getBoundingBox().intersects(debug7, debug8, debug7 + 15, debug8 + 15)) {
/* 299 */               debug2.addReferenceForFeature(debug9, debug15.getFeature(), debug12, debug3);
/* 300 */               DebugPackets.sendStructurePacket(debug1, debug15);
/*     */             } 
/* 302 */           } catch (Exception debug16) {
/* 303 */             CrashReport debug17 = CrashReport.forThrowable(debug16, "Generating structure reference");
/* 304 */             CrashReportCategory debug18 = debug17.addCategory("Structure");
/* 305 */             debug18.setDetail("Id", () -> Registry.STRUCTURE_FEATURE.getKey(debug0.getFeature()).toString());
/* 306 */             debug18.setDetail("Name", () -> debug0.getFeature().getFeatureName());
/* 307 */             debug18.setDetail("Class", () -> debug0.getFeature().getClass().getCanonicalName());
/* 308 */             throw new ReportedException(debug17);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 318 */     return 63;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirstFreeHeight(int debug1, int debug2, Heightmap.Types debug3) {
/* 326 */     return getBaseHeight(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public int getFirstOccupiedHeight(int debug1, int debug2, Heightmap.Types debug3) {
/* 330 */     return getBaseHeight(debug1, debug2, debug3) - 1;
/*     */   }
/*     */   
/*     */   public boolean hasStronghold(ChunkPos debug1) {
/* 334 */     generateStrongholds();
/* 335 */     return this.strongholdPositions.contains(debug1);
/*     */   }
/*     */   
/*     */   protected abstract Codec<? extends ChunkGenerator> codec();
/*     */   
/*     */   public abstract void buildSurfaceAndBedrock(WorldGenRegion paramWorldGenRegion, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void fillFromNoise(LevelAccessor paramLevelAccessor, StructureFeatureManager paramStructureFeatureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes);
/*     */   
/*     */   public abstract BlockGetter getBaseColumn(int paramInt1, int paramInt2);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ChunkGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */