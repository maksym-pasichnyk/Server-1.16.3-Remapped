/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ public class ChunkStatus {
/*  27 */   private static final EnumSet<Heightmap.Types> PRE_FEATURES = EnumSet.of(Heightmap.Types.OCEAN_FLOOR_WG, Heightmap.Types.WORLD_SURFACE_WG);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   private static final EnumSet<Heightmap.Types> POST_FEATURES = EnumSet.of(Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE, Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*     */ 
/*     */   
/*     */   private static final LoadingTask PASSTHROUGH_LOAD_TASK;
/*     */ 
/*     */   
/*     */   static {
/*  39 */     PASSTHROUGH_LOAD_TASK = ((debug0, debug1, debug2, debug3, debug4, debug5) -> {
/*     */         if (debug5 instanceof ProtoChunk && !debug5.getStatus().isOrAfter(debug0))
/*     */           ((ProtoChunk)debug5).setStatus(debug0); 
/*     */         return CompletableFuture.completedFuture(Either.left(debug5));
/*     */       });
/*     */   }
/*     */   public static final ChunkStatus STRUCTURE_STARTS; public static final ChunkStatus STRUCTURE_REFERENCES; public static final ChunkStatus BIOMES; public static final ChunkStatus NOISE; public static final ChunkStatus SURFACE;
/*  46 */   public static final ChunkStatus EMPTY = registerSimple("empty", null, -1, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> {
/*     */       
/*  48 */       }); public static final ChunkStatus CARVERS; public static final ChunkStatus LIQUID_CARVERS; public static final ChunkStatus FEATURES; public static final ChunkStatus LIGHT; public static final ChunkStatus SPAWN; static { STRUCTURE_STARTS = register("structure_starts", EMPTY, 0, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7) -> {
/*     */           if (!debug7.getStatus().isOrAfter(debug0)) {
/*     */             if (debug1.getServer().getWorldData().worldGenSettings().generateFeatures()) {
/*     */               debug2.createStructures(debug1.registryAccess(), debug1.structureFeatureManager(), debug7, debug3, debug1.getSeed());
/*     */             }
/*     */             
/*     */             if (debug7 instanceof ProtoChunk) {
/*     */               ((ProtoChunk)debug7).setStatus(debug0);
/*     */             }
/*     */           } 
/*     */           
/*     */           return CompletableFuture.completedFuture(Either.left(debug7));
/*     */         });
/*  61 */     STRUCTURE_REFERENCES = registerSimple("structure_references", STRUCTURE_STARTS, 8, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> {
/*     */           WorldGenRegion debug4 = new WorldGenRegion(debug0, debug2);
/*     */           
/*     */           debug1.createReferences((WorldGenLevel)debug4, debug0.structureFeatureManager().forWorldGenRegion(debug4), debug3);
/*     */         });
/*  66 */     BIOMES = registerSimple("biomes", STRUCTURE_REFERENCES, 0, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> debug1.createBiomes((Registry<Biome>)debug0.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), debug3));
/*     */ 
/*     */     
/*  69 */     NOISE = registerSimple("noise", BIOMES, 8, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> {
/*     */           WorldGenRegion debug4 = new WorldGenRegion(debug0, debug2);
/*     */           
/*     */           debug1.fillFromNoise((LevelAccessor)debug4, debug0.structureFeatureManager().forWorldGenRegion(debug4), debug3);
/*     */         });
/*  74 */     SURFACE = registerSimple("surface", NOISE, 0, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> debug1.buildSurfaceAndBedrock(new WorldGenRegion(debug0, debug2), debug3));
/*     */ 
/*     */     
/*  77 */     CARVERS = registerSimple("carvers", SURFACE, 0, PRE_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> debug1.applyCarvers(debug0.getSeed(), debug0.getBiomeManager(), debug3, GenerationStep.Carving.AIR));
/*     */ 
/*     */     
/*  80 */     LIQUID_CARVERS = registerSimple("liquid_carvers", CARVERS, 0, POST_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> debug1.applyCarvers(debug0.getSeed(), debug0.getBiomeManager(), debug3, GenerationStep.Carving.LIQUID));
/*     */ 
/*     */     
/*  83 */     FEATURES = register("features", LIQUID_CARVERS, 8, POST_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7) -> {
/*     */           ProtoChunk debug8 = (ProtoChunk)debug7;
/*     */           debug8.setLightEngine((LevelLightEngine)debug4);
/*     */           if (!debug7.getStatus().isOrAfter(debug0)) {
/*     */             Heightmap.primeHeightmaps(debug7, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
/*     */             WorldGenRegion debug9 = new WorldGenRegion(debug1, debug6);
/*     */             debug2.applyBiomeDecoration(debug9, debug1.structureFeatureManager().forWorldGenRegion(debug9));
/*     */             debug8.setStatus(debug0);
/*     */           } 
/*     */           return CompletableFuture.completedFuture(Either.left(debug7));
/*     */         });
/*  94 */     LIGHT = register("light", FEATURES, 1, POST_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7) -> lightChunk(debug0, debug4, debug7), (debug0, debug1, debug2, debug3, debug4, debug5) -> lightChunk(debug0, debug3, debug5));
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
/* 106 */     SPAWN = registerSimple("spawn", LIGHT, 0, POST_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> debug1.spawnOriginalMobs(new WorldGenRegion(debug0, debug2))); } private static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lightChunk(ChunkStatus debug0, ThreadedLevelLightEngine debug1, ChunkAccess debug2) { boolean debug3 = isLighted(debug0, debug2); if (!debug2.getStatus().isOrAfter(debug0))
/*     */       ((ProtoChunk)debug2).setStatus(debug0); 
/*     */     return debug1.lightChunk(debug2, debug3).thenApply(Either::left); }
/* 109 */   public static final ChunkStatus HEIGHTMAPS = registerSimple("heightmaps", SPAWN, 0, POST_FEATURES, ChunkType.PROTOCHUNK, (debug0, debug1, debug2, debug3) -> {
/*     */       
/* 111 */       }); public static final ChunkStatus FULL; static { FULL = register("full", HEIGHTMAPS, 0, POST_FEATURES, ChunkType.LEVELCHUNK, (debug0, debug1, debug2, debug3, debug4, debug5, debug6, debug7) -> (CompletableFuture)debug5.apply(debug7), (debug0, debug1, debug2, debug3, debug4, debug5) -> (CompletableFuture)debug4.apply(debug5)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ChunkStatus registerSimple(String debug0, @Nullable ChunkStatus debug1, int debug2, EnumSet<Heightmap.Types> debug3, ChunkType debug4, SimpleGenerationTask debug5) {
/* 117 */     return register(debug0, debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   private static ChunkStatus register(String debug0, @Nullable ChunkStatus debug1, int debug2, EnumSet<Heightmap.Types> debug3, ChunkType debug4, GenerationTask debug5) {
/* 121 */     return register(debug0, debug1, debug2, debug3, debug4, debug5, PASSTHROUGH_LOAD_TASK);
/*     */   }
/*     */   
/*     */   private static ChunkStatus register(String debug0, @Nullable ChunkStatus debug1, int debug2, EnumSet<Heightmap.Types> debug3, ChunkType debug4, GenerationTask debug5, LoadingTask debug6) {
/* 125 */     return (ChunkStatus)Registry.register((Registry)Registry.CHUNK_STATUS, debug0, new ChunkStatus(debug0, debug1, debug2, debug3, debug4, debug5, debug6));
/*     */   }
/*     */   
/*     */   public static List<ChunkStatus> getStatusList() {
/* 129 */     List<ChunkStatus> debug0 = Lists.newArrayList();
/* 130 */     ChunkStatus debug1 = FULL;
/* 131 */     while (debug1.getParent() != debug1) {
/* 132 */       debug0.add(debug1);
/* 133 */       debug1 = debug1.getParent();
/*     */     } 
/* 135 */     debug0.add(debug1);
/* 136 */     Collections.reverse(debug0);
/* 137 */     return debug0;
/*     */   }
/*     */   
/*     */   private static boolean isLighted(ChunkStatus debug0, ChunkAccess debug1) {
/* 141 */     return (debug1.getStatus().isOrAfter(debug0) && debug1.isLightCorrect());
/*     */   }
/*     */   
/* 144 */   private static final List<ChunkStatus> STATUS_BY_RANGE = (List<ChunkStatus>)ImmutableList.of(FULL, FEATURES, LIQUID_CARVERS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS, STRUCTURE_STARTS);
/*     */   
/*     */   private static final IntList RANGE_BY_STATUS;
/*     */   
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final ChunkStatus parent;
/*     */   private final GenerationTask generationTask;
/*     */   private final LoadingTask loadingTask;
/*     */   private final int range;
/*     */   private final ChunkType chunkType;
/*     */   private final EnumSet<Heightmap.Types> heightmapsAfter;
/*     */   
/*     */   static {
/* 158 */     RANGE_BY_STATUS = (IntList)Util.make(new IntArrayList(getStatusList().size()), debug0 -> {
/*     */           int debug1 = 0;
/*     */           for (int debug2 = getStatusList().size() - 1; debug2 >= 0; debug2--) {
/*     */             while (debug1 + 1 < STATUS_BY_RANGE.size() && debug2 <= ((ChunkStatus)STATUS_BY_RANGE.get(debug1 + 1)).getIndex())
/*     */               debug1++; 
/*     */             debug0.add(0, debug1);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static ChunkStatus getStatus(int debug0) {
/* 169 */     if (debug0 >= STATUS_BY_RANGE.size()) {
/* 170 */       return EMPTY;
/*     */     }
/* 172 */     if (debug0 < 0) {
/* 173 */       return FULL;
/*     */     }
/* 175 */     return STATUS_BY_RANGE.get(debug0);
/*     */   }
/*     */   
/*     */   public static int maxDistance() {
/* 179 */     return STATUS_BY_RANGE.size();
/*     */   }
/*     */   
/*     */   public static int getDistance(ChunkStatus debug0) {
/* 183 */     return RANGE_BY_STATUS.getInt(debug0.getIndex());
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
/*     */   ChunkStatus(String debug1, @Nullable ChunkStatus debug2, int debug3, EnumSet<Heightmap.Types> debug4, ChunkType debug5, GenerationTask debug6, LoadingTask debug7) {
/* 196 */     this.name = debug1;
/* 197 */     this.parent = (debug2 == null) ? this : debug2;
/* 198 */     this.generationTask = debug6;
/* 199 */     this.loadingTask = debug7;
/* 200 */     this.range = debug3;
/* 201 */     this.chunkType = debug5;
/* 202 */     this.heightmapsAfter = debug4;
/* 203 */     this.index = (debug2 == null) ? 0 : (debug2.getIndex() + 1);
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 207 */     return this.index;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 211 */     return this.name;
/*     */   }
/*     */   
/*     */   public ChunkStatus getParent() {
/* 215 */     return this.parent;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> generate(ServerLevel debug1, ChunkGenerator debug2, StructureManager debug3, ThreadedLevelLightEngine debug4, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> debug5, List<ChunkAccess> debug6) {
/* 219 */     return this.generationTask.doWork(this, debug1, debug2, debug3, debug4, debug5, debug6, debug6.get(debug6.size() / 2));
/*     */   }
/*     */   
/*     */   public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> load(ServerLevel debug1, StructureManager debug2, ThreadedLevelLightEngine debug3, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> debug4, ChunkAccess debug5) {
/* 223 */     return this.loadingTask.doWork(this, debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public int getRange() {
/* 227 */     return this.range;
/*     */   }
/*     */   
/*     */   public ChunkType getChunkType() {
/* 231 */     return this.chunkType;
/*     */   }
/*     */   
/*     */   public static ChunkStatus byName(String debug0) {
/* 235 */     return (ChunkStatus)Registry.CHUNK_STATUS.get(ResourceLocation.tryParse(debug0));
/*     */   }
/*     */   
/*     */   public EnumSet<Heightmap.Types> heightmapsAfter() {
/* 239 */     return this.heightmapsAfter;
/*     */   }
/*     */   
/*     */   public boolean isOrAfter(ChunkStatus debug1) {
/* 243 */     return (getIndex() >= debug1.getIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     return Registry.CHUNK_STATUS.getKey(this).toString();
/*     */   }
/*     */   
/*     */   static interface GenerationTask {
/*     */     CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus param1ChunkStatus, ServerLevel param1ServerLevel, ChunkGenerator param1ChunkGenerator, StructureManager param1StructureManager, ThreadedLevelLightEngine param1ThreadedLevelLightEngine, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> param1Function, List<ChunkAccess> param1List, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   static interface LoadingTask {
/*     */     CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus param1ChunkStatus, ServerLevel param1ServerLevel, StructureManager param1StructureManager, ThreadedLevelLightEngine param1ThreadedLevelLightEngine, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> param1Function, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   static interface SimpleGenerationTask
/*     */     extends GenerationTask {
/*     */     default CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(ChunkStatus debug1, ServerLevel debug2, ChunkGenerator debug3, StructureManager debug4, ThreadedLevelLightEngine debug5, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> debug6, List<ChunkAccess> debug7, ChunkAccess debug8) {
/* 262 */       if (!debug8.getStatus().isOrAfter(debug1)) {
/* 263 */         doWork(debug2, debug3, debug7, debug8);
/*     */         
/* 265 */         if (debug8 instanceof ProtoChunk) {
/* 266 */           ((ProtoChunk)debug8).setStatus(debug1);
/*     */         }
/*     */       } 
/* 269 */       return CompletableFuture.completedFuture(Either.left(debug8));
/*     */     }
/*     */     
/*     */     void doWork(ServerLevel param1ServerLevel, ChunkGenerator param1ChunkGenerator, List<ChunkAccess> param1List, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   public enum ChunkType {
/* 276 */     PROTOCHUNK,
/* 277 */     LEVELCHUNK;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ChunkStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */