/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ 
/*     */ public class BiomeDefaultFeatures {
/*     */   public static void addDefaultOverworldLandMesaStructures(BiomeGenerationSettings.Builder debug0) {
/*  11 */     debug0.addStructureStart(StructureFeatures.MINESHAFT_MESA);
/*  12 */     debug0.addStructureStart(StructureFeatures.STRONGHOLD);
/*     */   }
/*     */   
/*     */   public static void addDefaultOverworldLandStructures(BiomeGenerationSettings.Builder debug0) {
/*  16 */     debug0.addStructureStart(StructureFeatures.MINESHAFT);
/*  17 */     debug0.addStructureStart(StructureFeatures.STRONGHOLD);
/*     */   }
/*     */   
/*     */   public static void addDefaultOverworldOceanStructures(BiomeGenerationSettings.Builder debug0) {
/*  21 */     debug0.addStructureStart(StructureFeatures.MINESHAFT);
/*  22 */     debug0.addStructureStart(StructureFeatures.SHIPWRECK);
/*     */   }
/*     */   
/*     */   public static void addDefaultCarvers(BiomeGenerationSettings.Builder debug0) {
/*  26 */     debug0.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
/*  27 */     debug0.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
/*     */   }
/*     */   
/*     */   public static void addOceanCarvers(BiomeGenerationSettings.Builder debug0) {
/*  31 */     debug0.addCarver(GenerationStep.Carving.AIR, Carvers.OCEAN_CAVE);
/*  32 */     debug0.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
/*  33 */     debug0.addCarver(GenerationStep.Carving.LIQUID, Carvers.UNDERWATER_CANYON);
/*  34 */     debug0.addCarver(GenerationStep.Carving.LIQUID, Carvers.UNDERWATER_CAVE);
/*     */   }
/*     */   
/*     */   public static void addDefaultLakes(BiomeGenerationSettings.Builder debug0) {
/*  38 */     debug0.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER);
/*  39 */     debug0.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
/*     */   }
/*     */   
/*     */   public static void addDesertLakes(BiomeGenerationSettings.Builder debug0) {
/*  43 */     debug0.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
/*     */   }
/*     */   
/*     */   public static void addDefaultMonsterRoom(BiomeGenerationSettings.Builder debug0) {
/*  47 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, Features.MONSTER_ROOM);
/*     */   }
/*     */   
/*     */   public static void addDefaultUndergroundVariety(BiomeGenerationSettings.Builder debug0) {
/*  51 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIRT);
/*  52 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GRAVEL);
/*  53 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GRANITE);
/*  54 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIORITE);
/*  55 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_ANDESITE);
/*     */   }
/*     */   
/*     */   public static void addDefaultOres(BiomeGenerationSettings.Builder debug0) {
/*  59 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_COAL);
/*  60 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_IRON);
/*  61 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GOLD);
/*  62 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_REDSTONE);
/*  63 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_DIAMOND);
/*  64 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_LAPIS);
/*     */   }
/*     */   
/*     */   public static void addExtraGold(BiomeGenerationSettings.Builder debug0) {
/*  68 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_GOLD_EXTRA);
/*     */   }
/*     */   
/*     */   public static void addExtraEmeralds(BiomeGenerationSettings.Builder debug0) {
/*  72 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.ORE_EMERALD);
/*     */   }
/*     */   
/*     */   public static void addInfestedStone(BiomeGenerationSettings.Builder debug0) {
/*  76 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_INFESTED);
/*     */   }
/*     */   
/*     */   public static void addDefaultSoftDisks(BiomeGenerationSettings.Builder debug0) {
/*  80 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_SAND);
/*  81 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_CLAY);
/*  82 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_GRAVEL);
/*     */   }
/*     */   
/*     */   public static void addSwampClayDisk(BiomeGenerationSettings.Builder debug0) {
/*  86 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Features.DISK_CLAY);
/*     */   }
/*     */   
/*     */   public static void addMossyStoneBlock(BiomeGenerationSettings.Builder debug0) {
/*  90 */     debug0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.FOREST_ROCK);
/*     */   }
/*     */   
/*     */   public static void addFerns(BiomeGenerationSettings.Builder debug0) {
/*  94 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_LARGE_FERN);
/*     */   }
/*     */   
/*     */   public static void addBerryBushes(BiomeGenerationSettings.Builder debug0) {
/*  98 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_BERRY_DECORATED);
/*     */   }
/*     */   
/*     */   public static void addSparseBerryBushes(BiomeGenerationSettings.Builder debug0) {
/* 102 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_BERRY_SPARSE);
/*     */   }
/*     */   
/*     */   public static void addLightBambooVegetation(BiomeGenerationSettings.Builder debug0) {
/* 106 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO_LIGHT);
/*     */   }
/*     */   
/*     */   public static void addBambooVegetation(BiomeGenerationSettings.Builder debug0) {
/* 110 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO);
/* 111 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BAMBOO_VEGETATION);
/*     */   }
/*     */   
/*     */   public static void addTaigaTrees(BiomeGenerationSettings.Builder debug0) {
/* 115 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TAIGA_VEGETATION);
/*     */   }
/*     */   
/*     */   public static void addWaterTrees(BiomeGenerationSettings.Builder debug0) {
/* 119 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_WATER);
/*     */   }
/*     */   
/*     */   public static void addBirchTrees(BiomeGenerationSettings.Builder debug0) {
/* 123 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_BIRCH);
/*     */   }
/*     */   
/*     */   public static void addOtherBirchTrees(BiomeGenerationSettings.Builder debug0) {
/* 127 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BIRCH_OTHER);
/*     */   }
/*     */   
/*     */   public static void addTallBirchTrees(BiomeGenerationSettings.Builder debug0) {
/* 131 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BIRCH_TALL);
/*     */   }
/*     */   
/*     */   public static void addSavannaTrees(BiomeGenerationSettings.Builder debug0) {
/* 135 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addShatteredSavannaTrees(BiomeGenerationSettings.Builder debug0) {
/* 139 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_SHATTERED_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addMountainTrees(BiomeGenerationSettings.Builder debug0) {
/* 143 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_MOUNTAIN);
/*     */   }
/*     */   
/*     */   public static void addMountainEdgeTrees(BiomeGenerationSettings.Builder debug0) {
/* 147 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_MOUNTAIN_EDGE);
/*     */   }
/*     */   
/*     */   public static void addJungleTrees(BiomeGenerationSettings.Builder debug0) {
/* 151 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_JUNGLE);
/*     */   }
/*     */   
/*     */   public static void addJungleEdgeTrees(BiomeGenerationSettings.Builder debug0) {
/* 155 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TREES_JUNGLE_EDGE);
/*     */   }
/*     */   
/*     */   public static void addBadlandsTrees(BiomeGenerationSettings.Builder debug0) {
/* 159 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.OAK_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addSnowyTrees(BiomeGenerationSettings.Builder debug0) {
/* 163 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRUCE_SNOWY);
/*     */   }
/*     */   
/*     */   public static void addJungleGrass(BiomeGenerationSettings.Builder debug0) {
/* 167 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_JUNGLE);
/*     */   }
/*     */   
/*     */   public static void addSavannaGrass(BiomeGenerationSettings.Builder debug0) {
/* 171 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS);
/*     */   }
/*     */   
/*     */   public static void addShatteredSavannaGrass(BiomeGenerationSettings.Builder debug0) {
/* 175 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_NORMAL);
/*     */   }
/*     */   
/*     */   public static void addSavannaExtraGrass(BiomeGenerationSettings.Builder debug0) {
/* 179 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_SAVANNA);
/*     */   }
/*     */   
/*     */   public static void addBadlandGrass(BiomeGenerationSettings.Builder debug0) {
/* 183 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_BADLANDS);
/* 184 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addForestFlowers(BiomeGenerationSettings.Builder debug0) {
/* 188 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION);
/*     */   }
/*     */   
/*     */   public static void addForestGrass(BiomeGenerationSettings.Builder debug0) {
/* 192 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST);
/*     */   }
/*     */   
/*     */   public static void addSwampVegetation(BiomeGenerationSettings.Builder debug0) {
/* 196 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SWAMP_TREE);
/* 197 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_SWAMP);
/* 198 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_NORMAL);
/* 199 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
/* 200 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);
/* 201 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_SWAMP);
/* 202 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_SWAMP);
/*     */   }
/*     */   
/*     */   public static void addMushroomFieldVegetation(BiomeGenerationSettings.Builder debug0) {
/* 206 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.MUSHROOM_FIELD_VEGETATION);
/* 207 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
/* 208 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addPlainVegetation(BiomeGenerationSettings.Builder debug0) {
/* 212 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PLAIN_VEGETATION);
/* 213 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
/* 214 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
/*     */   }
/*     */   
/*     */   public static void addDesertVegetation(BiomeGenerationSettings.Builder debug0) {
/* 218 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH_2);
/*     */   }
/*     */   
/*     */   public static void addGiantTaigaVegetation(BiomeGenerationSettings.Builder debug0) {
/* 222 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_TAIGA);
/* 223 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_DEAD_BUSH);
/* 224 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_GIANT);
/* 225 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_GIANT);
/*     */   }
/*     */   
/*     */   public static void addDefaultFlowers(BiomeGenerationSettings.Builder debug0) {
/* 229 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_DEFAULT);
/*     */   }
/*     */   
/*     */   public static void addWarmFlowers(BiomeGenerationSettings.Builder debug0) {
/* 233 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_WARM);
/*     */   }
/*     */   
/*     */   public static void addDefaultGrass(BiomeGenerationSettings.Builder debug0) {
/* 237 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_BADLANDS);
/*     */   }
/*     */   
/*     */   public static void addTaigaGrass(BiomeGenerationSettings.Builder debug0) {
/* 241 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_TAIGA_2);
/* 242 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_TAIGA);
/* 243 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_TAIGA);
/*     */   }
/*     */   
/*     */   public static void addPlainGrass(BiomeGenerationSettings.Builder debug0) {
/* 247 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS_2);
/*     */   }
/*     */   
/*     */   public static void addDefaultMushrooms(BiomeGenerationSettings.Builder debug0) {
/* 251 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.BROWN_MUSHROOM_NORMAL);
/* 252 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.RED_MUSHROOM_NORMAL);
/*     */   }
/*     */   
/*     */   public static void addDefaultExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 256 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE);
/* 257 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
/*     */   }
/*     */   
/*     */   public static void addBadlandExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 261 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_BADLANDS);
/* 262 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
/* 263 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS_DECORATED);
/*     */   }
/*     */   
/*     */   public static void addJungleExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 267 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_MELON);
/* 268 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.VINES);
/*     */   }
/*     */   
/*     */   public static void addDesertExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 272 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_DESERT);
/* 273 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
/* 274 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS_DESERT);
/*     */   }
/*     */   
/*     */   public static void addSwampExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 278 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE_SWAMP);
/* 279 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
/*     */   }
/*     */   
/*     */   public static void addDesertExtraDecoration(BiomeGenerationSettings.Builder debug0) {
/* 283 */     debug0.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.WELL);
/*     */   }
/*     */   
/*     */   public static void addFossilDecoration(BiomeGenerationSettings.Builder debug0) {
/* 287 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, Features.FOSSIL);
/*     */   }
/*     */   
/*     */   public static void addColdOceanExtraVegetation(BiomeGenerationSettings.Builder debug0) {
/* 291 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.KELP_COLD);
/*     */   }
/*     */   
/*     */   public static void addDefaultSeagrass(BiomeGenerationSettings.Builder debug0) {
/* 295 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_SIMPLE);
/*     */   }
/*     */   
/*     */   public static void addLukeWarmKelp(BiomeGenerationSettings.Builder debug0) {
/* 299 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.KELP_WARM);
/*     */   }
/*     */   
/*     */   public static void addDefaultSprings(BiomeGenerationSettings.Builder debug0) {
/* 303 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_WATER);
/* 304 */     debug0.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
/*     */   }
/*     */   
/*     */   public static void addIcebergs(BiomeGenerationSettings.Builder debug0) {
/* 308 */     debug0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.ICEBERG_PACKED);
/* 309 */     debug0.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.ICEBERG_BLUE);
/*     */   }
/*     */   
/*     */   public static void addBlueIce(BiomeGenerationSettings.Builder debug0) {
/* 313 */     debug0.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.BLUE_ICE);
/*     */   }
/*     */   
/*     */   public static void addSurfaceFreezing(BiomeGenerationSettings.Builder debug0) {
/* 317 */     debug0.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Features.FREEZE_TOP_LAYER);
/*     */   }
/*     */   
/*     */   public static void addNetherDefaultOres(BiomeGenerationSettings.Builder debug0) {
/* 321 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GRAVEL_NETHER);
/* 322 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_BLACKSTONE);
/*     */     
/* 324 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GOLD_NETHER);
/* 325 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_QUARTZ_NETHER);
/* 326 */     addAncientDebris(debug0);
/*     */   }
/*     */   
/*     */   public static void addAncientDebris(BiomeGenerationSettings.Builder debug0) {
/* 330 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_DEBRIS_LARGE);
/* 331 */     debug0.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_DEBRIS_SMALL);
/*     */   }
/*     */   
/*     */   public static void farmAnimals(MobSpawnSettings.Builder debug0) {
/* 335 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
/* 336 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 4, 4));
/* 337 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
/* 338 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 4, 4));
/*     */   }
/*     */   
/*     */   public static void ambientSpawns(MobSpawnSettings.Builder debug0) {
/* 342 */     debug0.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
/*     */   }
/*     */   
/*     */   public static void commonSpawns(MobSpawnSettings.Builder debug0) {
/* 346 */     ambientSpawns(debug0);
/* 347 */     monsters(debug0, 95, 5, 100);
/*     */   }
/*     */   
/*     */   public static void oceanSpawns(MobSpawnSettings.Builder debug0, int debug1, int debug2, int debug3) {
/* 351 */     debug0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, debug1, 1, debug2));
/* 352 */     debug0.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, debug3, 3, 6));
/* 353 */     commonSpawns(debug0);
/* 354 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
/*     */   }
/*     */   
/*     */   public static void warmOceanSpawns(MobSpawnSettings.Builder debug0, int debug1, int debug2) {
/* 358 */     debug0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, debug1, debug2, 4));
/* 359 */     debug0.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
/* 360 */     debug0.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
/* 361 */     commonSpawns(debug0);
/*     */   }
/*     */   
/*     */   public static void plainsSpawns(MobSpawnSettings.Builder debug0) {
/* 365 */     farmAnimals(debug0);
/* 366 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 5, 2, 6));
/* 367 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 3));
/* 368 */     commonSpawns(debug0);
/*     */   }
/*     */   
/*     */   public static void snowySpawns(MobSpawnSettings.Builder debug0) {
/* 372 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
/* 373 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
/* 374 */     ambientSpawns(debug0);
/* 375 */     monsters(debug0, 95, 5, 20);
/* 376 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 80, 4, 4));
/*     */   }
/*     */   
/*     */   public static void desertSpawns(MobSpawnSettings.Builder debug0) {
/* 380 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
/* 381 */     ambientSpawns(debug0);
/* 382 */     monsters(debug0, 19, 1, 100);
/* 383 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 80, 4, 4));
/*     */   }
/*     */   
/*     */   public static void monsters(MobSpawnSettings.Builder debug0, int debug1, int debug2, int debug3) {
/* 387 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
/* 388 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, debug1, 4, 4));
/* 389 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, debug2, 1, 1));
/* 390 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, debug3, 4, 4));
/* 391 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
/* 392 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
/* 393 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
/* 394 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
/*     */   }
/*     */   
/*     */   public static void mooshroomSpawns(MobSpawnSettings.Builder debug0) {
/* 398 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 8, 4, 8));
/* 399 */     ambientSpawns(debug0);
/*     */   }
/*     */   
/*     */   public static void baseJungleSpawns(MobSpawnSettings.Builder debug0) {
/* 403 */     farmAnimals(debug0);
/* 404 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
/* 405 */     commonSpawns(debug0);
/*     */   }
/*     */   
/*     */   public static void endSpawns(MobSpawnSettings.Builder debug0) {
/* 409 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 4, 4));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\BiomeDefaultFeatures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */