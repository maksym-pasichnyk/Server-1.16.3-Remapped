/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.WeighedRandom;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.biome.NearestNeighborBiomeZoomer;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public final class NaturalSpawner {
/*  49 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D); private static final MobCategory[] SPAWNING_CATEGORIES; static {
/*  55 */     SPAWNING_CATEGORIES = (MobCategory[])Stream.<MobCategory>of(MobCategory.values()).filter(debug0 -> (debug0 != MobCategory.MISC)).toArray(debug0 -> new MobCategory[debug0]);
/*     */   }
/*     */   
/*     */   public static class SpawnState {
/*     */     private final int spawnableChunkCount;
/*     */     private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */     private final PotentialCalculator spawnPotential;
/*     */     private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */     @Nullable
/*     */     private BlockPos lastCheckedPos;
/*     */     @Nullable
/*     */     private EntityType<?> lastCheckedType;
/*     */     private double lastCharge;
/*     */     
/*     */     private SpawnState(int debug1, Object2IntOpenHashMap<MobCategory> debug2, PotentialCalculator debug3) {
/*  70 */       this.spawnableChunkCount = debug1;
/*  71 */       this.mobCategoryCounts = debug2;
/*  72 */       this.spawnPotential = debug3;
/*  73 */       this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable((Object2IntMap)debug2);
/*     */     }
/*     */     
/*     */     private boolean canSpawn(EntityType<?> debug1, BlockPos debug2, ChunkAccess debug3) {
/*  77 */       this.lastCheckedPos = debug2;
/*  78 */       this.lastCheckedType = debug1;
/*     */       
/*  80 */       MobSpawnSettings.MobSpawnCost debug4 = NaturalSpawner.getRoughBiome(debug2, debug3).getMobSettings().getMobSpawnCost(debug1);
/*  81 */       if (debug4 == null) {
/*  82 */         this.lastCharge = 0.0D;
/*  83 */         return true;
/*     */       } 
/*  85 */       double debug5 = debug4.getCharge();
/*  86 */       this.lastCharge = debug5;
/*  87 */       double debug7 = this.spawnPotential.getPotentialEnergyChange(debug2, debug5);
/*  88 */       return (debug7 <= debug4.getEnergyBudget());
/*     */     }
/*     */     private void afterSpawn(Mob debug1, ChunkAccess debug2) {
/*     */       double debug4;
/*  92 */       EntityType<?> debug3 = debug1.getType();
/*     */       
/*  94 */       BlockPos debug6 = debug1.blockPosition();
/*  95 */       if (debug6.equals(this.lastCheckedPos) && debug3 == this.lastCheckedType) {
/*  96 */         debug4 = this.lastCharge;
/*     */       } else {
/*     */         
/*  99 */         MobSpawnSettings.MobSpawnCost debug7 = NaturalSpawner.getRoughBiome(debug6, debug2).getMobSettings().getMobSpawnCost(debug3);
/* 100 */         if (debug7 != null) {
/* 101 */           debug4 = debug7.getCharge();
/*     */         } else {
/* 103 */           debug4 = 0.0D;
/*     */         } 
/*     */       } 
/* 106 */       this.spawnPotential.addCharge(debug6, debug4);
/* 107 */       this.mobCategoryCounts.addTo(debug3.getCategory(), 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object2IntMap<MobCategory> getMobCategoryCounts() {
/* 115 */       return this.unmodifiableMobCategoryCounts;
/*     */     }
/*     */     
/*     */     private boolean canSpawnForCategory(MobCategory debug1) {
/* 119 */       int debug2 = debug1.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 120 */       return (this.mobCategoryCounts.getInt(debug1) < debug2);
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
/*     */   public static SpawnState createState(int debug0, Iterable<Entity> debug1, ChunkGetter debug2) {
/* 143 */     PotentialCalculator debug3 = new PotentialCalculator();
/* 144 */     Object2IntOpenHashMap<MobCategory> debug4 = new Object2IntOpenHashMap();
/*     */     
/* 146 */     for (Iterator<Entity> iterator = debug1.iterator(); iterator.hasNext(); ) { Entity debug6 = iterator.next();
/* 147 */       if (debug6 instanceof Mob) {
/* 148 */         Mob mob = (Mob)debug6;
/* 149 */         if (mob.isPersistenceRequired() || mob.requiresCustomPersistence()) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 153 */       MobCategory debug7 = debug6.getType().getCategory();
/* 154 */       if (debug7 == MobCategory.MISC) {
/*     */         continue;
/*     */       }
/*     */       
/* 158 */       BlockPos debug8 = debug6.blockPosition();
/* 159 */       long debug9 = ChunkPos.asLong(debug8.getX() >> 4, debug8.getZ() >> 4);
/*     */       
/* 161 */       debug2.query(debug9, debug5 -> {
/*     */             MobSpawnSettings.MobSpawnCost debug6 = getRoughBiome(debug0, (ChunkAccess)debug5).getMobSettings().getMobSpawnCost(debug1.getType());
/*     */             if (debug6 != null) {
/*     */               debug2.addCharge(debug1.blockPosition(), debug6.getCharge());
/*     */             }
/*     */             debug3.addTo(debug4, 1);
/*     */           }); }
/*     */     
/* 169 */     return new SpawnState(debug0, debug4, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Biome getRoughBiome(BlockPos debug0, ChunkAccess debug1) {
/* 174 */     return NearestNeighborBiomeZoomer.INSTANCE.getBiome(0L, debug0.getX(), debug0.getY(), debug0.getZ(), (BiomeManager.NoiseBiomeSource)debug1.getBiomes());
/*     */   }
/*     */   
/*     */   public static void spawnForChunk(ServerLevel debug0, LevelChunk debug1, SpawnState debug2, boolean debug3, boolean debug4, boolean debug5) {
/* 178 */     debug0.getProfiler().push("spawner");
/*     */     
/* 180 */     for (MobCategory debug9 : SPAWNING_CATEGORIES) {
/* 181 */       if ((debug3 || !debug9.isFriendly()) && (debug4 || debug9
/* 182 */         .isFriendly()) && (debug5 || 
/* 183 */         !debug9.isPersistent()) && debug2
/* 184 */         .canSpawnForCategory(debug9)) {
/* 185 */         spawnCategoryForChunk(debug9, debug0, debug1, (debug1, debug2, debug3) -> debug0.canSpawn(debug1, debug2, debug3), (debug1, debug2) -> debug0.afterSpawn(debug1, debug2));
/*     */       }
/*     */     } 
/* 188 */     debug0.getProfiler().pop();
/*     */   }
/*     */   
/*     */   public static void spawnCategoryForChunk(MobCategory debug0, ServerLevel debug1, LevelChunk debug2, SpawnPredicate debug3, AfterSpawnCallback debug4) {
/* 192 */     BlockPos debug5 = getRandomPosWithin((Level)debug1, debug2);
/*     */     
/* 194 */     if (debug5.getY() < 1) {
/*     */       return;
/*     */     }
/* 197 */     spawnCategoryForPosition(debug0, debug1, (ChunkAccess)debug2, debug5, debug3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void spawnCategoryForPosition(MobCategory debug0, ServerLevel debug1, ChunkAccess debug2, BlockPos debug3, SpawnPredicate debug4, AfterSpawnCallback debug5)
/*     */   {
/* 206 */     StructureFeatureManager debug6 = debug1.structureFeatureManager();
/* 207 */     ChunkGenerator debug7 = debug1.getChunkSource().getGenerator();
/* 208 */     int debug8 = debug3.getY();
/*     */     
/* 210 */     BlockState debug9 = debug2.getBlockState(debug3);
/* 211 */     if (debug9.isRedstoneConductor((BlockGetter)debug2, debug3)) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     BlockPos.MutableBlockPos debug10 = new BlockPos.MutableBlockPos();
/* 216 */     int debug11 = 0;
/*     */     
/* 218 */     for (int debug12 = 0; debug12 < 3; debug12++) {
/* 219 */       int debug13 = debug3.getX();
/* 220 */       int debug14 = debug3.getZ();
/* 221 */       int debug15 = 6;
/*     */       
/* 223 */       MobSpawnSettings.SpawnerData debug16 = null;
/* 224 */       SpawnGroupData debug17 = null;
/*     */       
/* 226 */       int debug18 = Mth.ceil(debug1.random.nextFloat() * 4.0F);
/* 227 */       int debug19 = 0;
/*     */ 
/*     */       
/* 230 */       for (int debug20 = 0; debug20 < debug18; debug20++) {
/* 231 */         debug13 += debug1.random.nextInt(6) - debug1.random.nextInt(6);
/* 232 */         debug14 += debug1.random.nextInt(6) - debug1.random.nextInt(6);
/*     */         
/* 234 */         debug10.set(debug13, debug8, debug14);
/*     */         
/* 236 */         double debug21 = debug13 + 0.5D;
/* 237 */         double debug23 = debug14 + 0.5D;
/*     */         
/* 239 */         Player debug25 = debug1.getNearestPlayer(debug21, debug8, debug23, -1.0D, false);
/* 240 */         if (debug25 != null) {
/*     */ 
/*     */ 
/*     */           
/* 244 */           double debug26 = debug25.distanceToSqr(debug21, debug8, debug23);
/* 245 */           if (isRightDistanceToPlayerAndSpawnPoint(debug1, debug2, debug10, debug26)) {
/*     */ 
/*     */ 
/*     */             
/* 249 */             if (debug16 == null) {
/* 250 */               debug16 = getRandomSpawnMobAt(debug1, debug6, debug7, debug0, debug1.random, (BlockPos)debug10);
/* 251 */               if (debug16 == null) {
/*     */                 break;
/*     */               }
/*     */ 
/*     */               
/* 256 */               debug18 = debug16.minCount + debug1.random.nextInt(1 + debug16.maxCount - debug16.minCount);
/*     */             } 
/*     */             
/* 259 */             if (isValidSpawnPostitionForType(debug1, debug0, debug6, debug7, debug16, debug10, debug26))
/*     */             {
/*     */ 
/*     */               
/* 263 */               if (debug4.test(debug16.type, (BlockPos)debug10, debug2)) {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 268 */                 Mob debug28 = getMobForSpawn(debug1, debug16.type);
/* 269 */                 if (debug28 == null) {
/*     */                   return;
/*     */                 }
/*     */                 
/* 273 */                 debug28.moveTo(debug21, debug8, debug23, debug1.random.nextFloat() * 360.0F, 0.0F);
/*     */                 
/* 275 */                 if (isValidPositionForMob(debug1, debug28, debug26)) {
/*     */ 
/*     */ 
/*     */                   
/* 279 */                   debug17 = debug28.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug28.blockPosition()), MobSpawnType.NATURAL, debug17, null);
/*     */                   
/* 281 */                   debug11++;
/* 282 */                   debug19++;
/* 283 */                   debug1.addFreshEntityWithPassengers((Entity)debug28);
/* 284 */                   debug5.run(debug28, debug2);
/*     */                   
/* 286 */                   if (debug11 >= debug28.getMaxSpawnClusterSize()) {
/*     */                     return;
/*     */                   }
/* 289 */                   if (debug28.isMaxGroupSizeReached(debug19))
/*     */                     break; 
/*     */                 } 
/*     */               }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  } private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel debug0, ChunkAccess debug1, BlockPos.MutableBlockPos debug2, double debug3) {
/* 297 */     if (debug3 <= 576.0D) {
/* 298 */       return false;
/*     */     }
/* 300 */     if (debug0.getSharedSpawnPos().closerThan((Position)new Vec3(debug2.getX() + 0.5D, debug2.getY(), debug2.getZ() + 0.5D), 24.0D)) {
/* 301 */       return false;
/*     */     }
/*     */     
/* 304 */     ChunkPos debug5 = new ChunkPos((BlockPos)debug2);
/* 305 */     if (!Objects.equals(debug5, debug1.getPos()) && !debug0.getChunkSource().isEntityTickingChunk(debug5)) {
/* 306 */       return false;
/*     */     }
/* 308 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isValidSpawnPostitionForType(ServerLevel debug0, MobCategory debug1, StructureFeatureManager debug2, ChunkGenerator debug3, MobSpawnSettings.SpawnerData debug4, BlockPos.MutableBlockPos debug5, double debug6) {
/* 312 */     EntityType<?> debug8 = debug4.type;
/*     */     
/* 314 */     if (debug8.getCategory() == MobCategory.MISC) {
/* 315 */       return false;
/*     */     }
/*     */     
/* 318 */     if (!debug8.canSpawnFarFromPlayer() && debug6 > (debug8.getCategory().getDespawnDistance() * debug8.getCategory().getDespawnDistance())) {
/* 319 */       return false;
/*     */     }
/*     */     
/* 322 */     if (!debug8.canSummon() || !canSpawnMobAt(debug0, debug2, debug3, debug1, debug4, (BlockPos)debug5)) {
/* 323 */       return false;
/*     */     }
/*     */     
/* 326 */     SpawnPlacements.Type debug9 = SpawnPlacements.getPlacementType(debug8);
/* 327 */     if (!isSpawnPositionOk(debug9, (LevelReader)debug0, (BlockPos)debug5, debug8)) {
/* 328 */       return false;
/*     */     }
/* 330 */     if (!SpawnPlacements.checkSpawnRules(debug8, (ServerLevelAccessor)debug0, MobSpawnType.NATURAL, (BlockPos)debug5, debug0.random)) {
/* 331 */       return false;
/*     */     }
/* 333 */     if (!debug0.noCollision(debug8.getAABB(debug5.getX() + 0.5D, debug5.getY(), debug5.getZ() + 0.5D))) {
/* 334 */       return false;
/*     */     }
/* 336 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Mob getMobForSpawn(ServerLevel debug0, EntityType<?> debug1) {
/*     */     Mob debug2;
/*     */     try {
/* 343 */       Entity debug3 = debug1.create((Level)debug0);
/* 344 */       if (!(debug3 instanceof Mob)) {
/* 345 */         throw new IllegalStateException("Trying to spawn a non-mob: " + Registry.ENTITY_TYPE.getKey(debug1));
/*     */       }
/* 347 */       debug2 = (Mob)debug3;
/* 348 */     } catch (Exception debug3) {
/* 349 */       LOGGER.warn("Failed to create mob", debug3);
/* 350 */       return null;
/*     */     } 
/* 352 */     return debug2;
/*     */   }
/*     */   
/*     */   private static boolean isValidPositionForMob(ServerLevel debug0, Mob debug1, double debug2) {
/* 356 */     if (debug2 > (debug1.getType().getCategory().getDespawnDistance() * debug1.getType().getCategory().getDespawnDistance()) && debug1.removeWhenFarAway(debug2)) {
/* 357 */       return false;
/*     */     }
/* 359 */     if (!debug1.checkSpawnRules((LevelAccessor)debug0, MobSpawnType.NATURAL) || !debug1.checkSpawnObstruction((LevelReader)debug0)) {
/* 360 */       return false;
/*     */     }
/* 362 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static MobSpawnSettings.SpawnerData getRandomSpawnMobAt(ServerLevel debug0, StructureFeatureManager debug1, ChunkGenerator debug2, MobCategory debug3, Random debug4, BlockPos debug5) {
/* 367 */     Biome debug6 = debug0.getBiome(debug5);
/*     */     
/* 369 */     if (debug3 == MobCategory.WATER_AMBIENT && debug6.getBiomeCategory() == Biome.BiomeCategory.RIVER && debug4.nextFloat() < 0.98F) {
/* 370 */       return null;
/*     */     }
/* 372 */     List<MobSpawnSettings.SpawnerData> debug7 = mobsAt(debug0, debug1, debug2, debug3, debug5, debug6);
/* 373 */     if (debug7.isEmpty()) {
/* 374 */       return null;
/*     */     }
/*     */     
/* 377 */     return (MobSpawnSettings.SpawnerData)WeighedRandom.getRandomItem(debug4, debug7);
/*     */   }
/*     */   
/*     */   private static boolean canSpawnMobAt(ServerLevel debug0, StructureFeatureManager debug1, ChunkGenerator debug2, MobCategory debug3, MobSpawnSettings.SpawnerData debug4, BlockPos debug5) {
/* 381 */     return mobsAt(debug0, debug1, debug2, debug3, debug5, null).contains(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel debug0, StructureFeatureManager debug1, ChunkGenerator debug2, MobCategory debug3, BlockPos debug4, @Nullable Biome debug5) {
/* 386 */     if (debug3 == MobCategory.MONSTER && debug0.getBlockState(debug4.below()).getBlock() == Blocks.NETHER_BRICKS && debug1.getStructureAt(debug4, false, StructureFeature.NETHER_BRIDGE).isValid()) {
/* 387 */       return StructureFeature.NETHER_BRIDGE.getSpecialEnemies();
/*     */     }
/* 389 */     return debug2.getMobsAt((debug5 != null) ? debug5 : debug0.getBiome(debug4), debug1, debug3, debug4);
/*     */   }
/*     */   
/*     */   private static BlockPos getRandomPosWithin(Level debug0, LevelChunk debug1) {
/* 393 */     ChunkPos debug2 = debug1.getPos();
/* 394 */     int debug3 = debug2.getMinBlockX() + debug0.random.nextInt(16);
/* 395 */     int debug4 = debug2.getMinBlockZ() + debug0.random.nextInt(16);
/*     */     
/* 397 */     int debug5 = debug1.getHeight(Heightmap.Types.WORLD_SURFACE, debug3, debug4) + 1;
/* 398 */     int debug6 = debug0.random.nextInt(debug5 + 1);
/*     */     
/* 400 */     return new BlockPos(debug3, debug6, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidEmptySpawnBlock(BlockGetter debug0, BlockPos debug1, BlockState debug2, FluidState debug3, EntityType<?> debug4) {
/* 405 */     if (debug2.isCollisionShapeFullBlock(debug0, debug1)) {
/* 406 */       return false;
/*     */     }
/*     */     
/* 409 */     if (debug2.isSignalSource()) {
/* 410 */       return false;
/*     */     }
/*     */     
/* 413 */     if (!debug3.isEmpty()) {
/* 414 */       return false;
/*     */     }
/*     */     
/* 417 */     if (debug2.is((Tag)BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
/* 418 */       return false;
/*     */     }
/*     */     
/* 421 */     if (debug4.isBlockDangerous(debug2)) {
/* 422 */       return false;
/*     */     }
/* 424 */     return true; } @FunctionalInterface
/*     */   public static interface SpawnPredicate {
/*     */     boolean test(EntityType<?> param1EntityType, BlockPos param1BlockPos, ChunkAccess param1ChunkAccess); } @FunctionalInterface
/*     */   public static interface AfterSpawnCallback {
/* 428 */     void run(Mob param1Mob, ChunkAccess param1ChunkAccess); } public static boolean isSpawnPositionOk(SpawnPlacements.Type debug0, LevelReader debug1, BlockPos debug2, @Nullable EntityType<?> debug3) { if (debug0 == SpawnPlacements.Type.NO_RESTRICTIONS) {
/* 429 */       return true;
/*     */     }
/* 431 */     if (debug3 == null || !debug1.getWorldBorder().isWithinBounds(debug2)) {
/* 432 */       return false;
/*     */     }
/* 434 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 435 */     FluidState debug5 = debug1.getFluidState(debug2);
/*     */     
/* 437 */     BlockPos debug6 = debug2.above();
/* 438 */     BlockPos debug7 = debug2.below();
/* 439 */     switch (debug0) {
/*     */       
/*     */       case IN_WATER:
/* 442 */         return (debug5.is((Tag)FluidTags.WATER) && debug1.getFluidState(debug7).is((Tag)FluidTags.WATER) && !debug1.getBlockState(debug6).isRedstoneConductor(debug1, debug6));
/*     */       case IN_LAVA:
/* 444 */         return debug5.is((Tag)FluidTags.LAVA);
/*     */     } 
/*     */     
/* 447 */     BlockState debug8 = debug1.getBlockState(debug7);
/* 448 */     if (!debug8.isValidSpawn(debug1, debug7, debug3)) {
/* 449 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 453 */     return (isValidEmptySpawnBlock(debug1, debug2, debug4, debug5, debug3) && isValidEmptySpawnBlock(debug1, debug6, debug1.getBlockState(debug6), debug1.getFluidState(debug6), debug3)); }
/*     */    @FunctionalInterface
/*     */   public static interface ChunkGetter {
/*     */     void query(long param1Long, Consumer<LevelChunk> param1Consumer); }
/*     */   public static void spawnMobsForChunkGeneration(ServerLevelAccessor debug0, Biome debug1, int debug2, int debug3, Random debug4) {
/* 458 */     MobSpawnSettings debug5 = debug1.getMobSettings();
/* 459 */     List<MobSpawnSettings.SpawnerData> debug6 = debug5.getMobs(MobCategory.CREATURE);
/* 460 */     if (debug6.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 464 */     int debug7 = debug2 << 4;
/* 465 */     int debug8 = debug3 << 4;
/*     */     
/* 467 */     while (debug4.nextFloat() < debug5.getCreatureProbability()) {
/* 468 */       MobSpawnSettings.SpawnerData debug9 = (MobSpawnSettings.SpawnerData)WeighedRandom.getRandomItem(debug4, debug6);
/*     */       
/* 470 */       int debug10 = debug9.minCount + debug4.nextInt(1 + debug9.maxCount - debug9.minCount);
/* 471 */       SpawnGroupData debug11 = null;
/*     */       
/* 473 */       int debug12 = debug7 + debug4.nextInt(16);
/* 474 */       int debug13 = debug8 + debug4.nextInt(16);
/* 475 */       int debug14 = debug12;
/* 476 */       int debug15 = debug13;
/*     */       
/* 478 */       for (int debug16 = 0; debug16 < debug10; debug16++) {
/* 479 */         boolean debug17 = false;
/* 480 */         for (int debug18 = 0; !debug17 && debug18 < 4; debug18++) {
/*     */ 
/*     */           
/* 483 */           BlockPos debug19 = getTopNonCollidingPos(debug0, debug9.type, debug12, debug13);
/* 484 */           if (debug9.type.canSummon() && isSpawnPositionOk(SpawnPlacements.getPlacementType(debug9.type), debug0, debug19, debug9.type)) {
/* 485 */             Entity debug25; float debug20 = debug9.type.getWidth();
/* 486 */             double debug21 = Mth.clamp(debug12, debug7 + debug20, debug7 + 16.0D - debug20);
/* 487 */             double debug23 = Mth.clamp(debug13, debug8 + debug20, debug8 + 16.0D - debug20);
/*     */             
/* 489 */             if (!debug0.noCollision(debug9.type.getAABB(debug21, debug19.getY(), debug23))) {
/*     */               continue;
/*     */             }
/*     */             
/* 493 */             if (!SpawnPlacements.checkSpawnRules(debug9.type, debug0, MobSpawnType.CHUNK_GENERATION, new BlockPos(debug21, debug19.getY(), debug23), debug0.getRandom())) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/*     */             try {
/* 499 */               debug25 = debug9.type.create((Level)debug0.getLevel());
/* 500 */             } catch (Exception debug26) {
/* 501 */               LOGGER.warn("Failed to create mob", debug26);
/*     */             } 
/*     */ 
/*     */             
/* 505 */             debug25.moveTo(debug21, debug19.getY(), debug23, debug4.nextFloat() * 360.0F, 0.0F);
/*     */             
/* 507 */             if (debug25 instanceof Mob) {
/* 508 */               Mob debug26 = (Mob)debug25;
/* 509 */               if (debug26.checkSpawnRules(debug0, MobSpawnType.CHUNK_GENERATION) && debug26.checkSpawnObstruction(debug0)) {
/* 510 */                 debug11 = debug26.finalizeSpawn(debug0, debug0.getCurrentDifficultyAt(debug26.blockPosition()), MobSpawnType.CHUNK_GENERATION, debug11, null);
/* 511 */                 debug0.addFreshEntityWithPassengers((Entity)debug26);
/* 512 */                 debug17 = true;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 517 */           debug12 += debug4.nextInt(5) - debug4.nextInt(5);
/* 518 */           debug13 += debug4.nextInt(5) - debug4.nextInt(5);
/* 519 */           while (debug12 < debug7 || debug12 >= debug7 + 16 || debug13 < debug8 || debug13 >= debug8 + 16) {
/* 520 */             debug12 = debug14 + debug4.nextInt(5) - debug4.nextInt(5);
/* 521 */             debug13 = debug15 + debug4.nextInt(5) - debug4.nextInt(5);
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static BlockPos getTopNonCollidingPos(LevelReader debug0, EntityType<?> debug1, int debug2, int debug3) {
/* 529 */     int debug4 = debug0.getHeight(SpawnPlacements.getHeightmapType(debug1), debug2, debug3);
/* 530 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos(debug2, debug4, debug3);
/*     */     
/* 532 */     if (debug0.dimensionType().hasCeiling()) {
/*     */       
/*     */       do {
/* 535 */         debug5.move(Direction.DOWN);
/* 536 */       } while (!debug0.getBlockState((BlockPos)debug5).isAir());
/*     */       do {
/* 538 */         debug5.move(Direction.DOWN);
/* 539 */       } while (debug0.getBlockState((BlockPos)debug5).isAir() && debug5.getY() > 0);
/*     */     } 
/*     */     
/* 542 */     if (SpawnPlacements.getPlacementType(debug1) == SpawnPlacements.Type.ON_GROUND) {
/* 543 */       BlockPos debug6 = debug5.below();
/* 544 */       if (debug0.getBlockState(debug6).isPathfindable(debug0, debug6, PathComputationType.LAND)) {
/* 545 */         return debug6;
/*     */       }
/*     */     } 
/*     */     
/* 549 */     return debug5.immutable();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\NaturalSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */