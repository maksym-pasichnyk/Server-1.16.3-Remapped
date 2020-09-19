/*      */ package net.minecraft.data.worldgen.biome;
/*      */ 
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*      */ import net.minecraft.data.worldgen.Carvers;
/*      */ import net.minecraft.data.worldgen.Features;
/*      */ import net.minecraft.data.worldgen.StructureFeatures;
/*      */ import net.minecraft.data.worldgen.SurfaceBuilders;
/*      */ import net.minecraft.sounds.Musics;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.level.biome.AmbientAdditionsSettings;
/*      */ import net.minecraft.world.level.biome.AmbientMoodSettings;
/*      */ import net.minecraft.world.level.biome.AmbientParticleSettings;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*      */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*      */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*      */ import net.minecraft.world.level.levelgen.GenerationStep;
/*      */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*      */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*      */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
/*      */ 
/*      */ public class VanillaBiomes {
/*      */   private static int calculateSkyColor(float debug0) {
/*   29 */     float debug1 = debug0;
/*   30 */     debug1 /= 3.0F;
/*   31 */     debug1 = Mth.clamp(debug1, -1.0F, 1.0F);
/*   32 */     return Mth.hsvToRgb(0.62222224F - debug1 * 0.05F, 0.5F + debug1 * 0.1F, 1.0F);
/*      */   }
/*      */   
/*      */   public static Biome giantTreeTaiga(float debug0, float debug1, float debug2, boolean debug3) {
/*   36 */     MobSpawnSettings.Builder debug4 = new MobSpawnSettings.Builder();
/*   37 */     BiomeDefaultFeatures.farmAnimals(debug4);
/*   38 */     debug4.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4));
/*   39 */     debug4.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
/*   40 */     debug4.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
/*   41 */     if (debug3) {
/*   42 */       BiomeDefaultFeatures.commonSpawns(debug4);
/*      */     } else {
/*   44 */       BiomeDefaultFeatures.ambientSpawns(debug4);
/*   45 */       BiomeDefaultFeatures.monsters(debug4, 100, 25, 100);
/*      */     } 
/*      */ 
/*      */     
/*   49 */     BiomeGenerationSettings.Builder debug5 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GIANT_TREE_TAIGA);
/*      */     
/*   51 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug5);
/*   52 */     debug5.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*   54 */     BiomeDefaultFeatures.addDefaultCarvers(debug5);
/*      */     
/*   56 */     BiomeDefaultFeatures.addDefaultLakes(debug5);
/*   57 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug5);
/*   58 */     BiomeDefaultFeatures.addMossyStoneBlock(debug5);
/*   59 */     BiomeDefaultFeatures.addFerns(debug5);
/*   60 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug5);
/*   61 */     BiomeDefaultFeatures.addDefaultOres(debug5);
/*   62 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug5);
/*   63 */     debug5.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, debug3 ? Features.TREES_GIANT_SPRUCE : Features.TREES_GIANT);
/*   64 */     BiomeDefaultFeatures.addDefaultFlowers(debug5);
/*   65 */     BiomeDefaultFeatures.addGiantTaigaVegetation(debug5);
/*   66 */     BiomeDefaultFeatures.addDefaultMushrooms(debug5);
/*   67 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug5);
/*   68 */     BiomeDefaultFeatures.addDefaultSprings(debug5);
/*   69 */     BiomeDefaultFeatures.addSparseBerryBushes(debug5);
/*   70 */     BiomeDefaultFeatures.addSurfaceFreezing(debug5);
/*      */     
/*   72 */     return (new Biome.BiomeBuilder())
/*   73 */       .precipitation(Biome.Precipitation.RAIN)
/*   74 */       .biomeCategory(Biome.BiomeCategory.TAIGA)
/*   75 */       .depth(debug0)
/*   76 */       .scale(debug1)
/*   77 */       .temperature(debug2)
/*   78 */       .downfall(0.8F)
/*   79 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*   80 */         .waterColor(4159204)
/*   81 */         .waterFogColor(329011)
/*   82 */         .fogColor(12638463)
/*   83 */         .skyColor(calculateSkyColor(debug2))
/*   84 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*   85 */         .build())
/*      */       
/*   87 */       .mobSpawnSettings(debug4.build())
/*   88 */       .generationSettings(debug5.build())
/*   89 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome birchForestBiome(float debug0, float debug1, boolean debug2) {
/*   93 */     MobSpawnSettings.Builder debug3 = new MobSpawnSettings.Builder();
/*   94 */     BiomeDefaultFeatures.farmAnimals(debug3);
/*   95 */     BiomeDefaultFeatures.commonSpawns(debug3);
/*      */ 
/*      */     
/*   98 */     BiomeGenerationSettings.Builder debug4 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*   99 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug4);
/*  100 */     debug4.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  102 */     BiomeDefaultFeatures.addDefaultCarvers(debug4);
/*      */     
/*  104 */     BiomeDefaultFeatures.addDefaultLakes(debug4);
/*  105 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug4);
/*  106 */     BiomeDefaultFeatures.addForestFlowers(debug4);
/*  107 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug4);
/*  108 */     BiomeDefaultFeatures.addDefaultOres(debug4);
/*  109 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug4);
/*  110 */     if (debug2) {
/*  111 */       BiomeDefaultFeatures.addTallBirchTrees(debug4);
/*      */     } else {
/*  113 */       BiomeDefaultFeatures.addBirchTrees(debug4);
/*      */     } 
/*  115 */     BiomeDefaultFeatures.addDefaultFlowers(debug4);
/*  116 */     BiomeDefaultFeatures.addForestGrass(debug4);
/*  117 */     BiomeDefaultFeatures.addDefaultMushrooms(debug4);
/*  118 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug4);
/*  119 */     BiomeDefaultFeatures.addDefaultSprings(debug4);
/*  120 */     BiomeDefaultFeatures.addSurfaceFreezing(debug4);
/*      */     
/*  122 */     return (new Biome.BiomeBuilder())
/*  123 */       .precipitation(Biome.Precipitation.RAIN)
/*  124 */       .biomeCategory(Biome.BiomeCategory.FOREST)
/*  125 */       .depth(debug0)
/*  126 */       .scale(debug1)
/*  127 */       .temperature(0.6F)
/*  128 */       .downfall(0.6F)
/*  129 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  130 */         .waterColor(4159204)
/*  131 */         .waterFogColor(329011)
/*  132 */         .fogColor(12638463)
/*  133 */         .skyColor(calculateSkyColor(0.6F))
/*  134 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  135 */         .build())
/*      */       
/*  137 */       .mobSpawnSettings(debug3.build())
/*  138 */       .generationSettings(debug4.build())
/*  139 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome jungleBiome() {
/*  143 */     return jungleBiome(0.1F, 0.2F, 40, 2, 3);
/*      */   }
/*      */   
/*      */   public static Biome jungleEdgeBiome() {
/*  147 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  148 */     BiomeDefaultFeatures.baseJungleSpawns(debug0);
/*      */     
/*  150 */     return baseJungleBiome(0.1F, 0.2F, 0.8F, false, true, false, debug0);
/*      */   }
/*      */   
/*      */   public static Biome modifiedJungleEdgeBiome() {
/*  154 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  155 */     BiomeDefaultFeatures.baseJungleSpawns(debug0);
/*      */     
/*  157 */     return baseJungleBiome(0.2F, 0.4F, 0.8F, false, true, true, debug0);
/*      */   }
/*      */   
/*      */   public static Biome modifiedJungleBiome() {
/*  161 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  162 */     BiomeDefaultFeatures.baseJungleSpawns(debug0);
/*  163 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 1))
/*  164 */       .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
/*  165 */     return baseJungleBiome(0.2F, 0.4F, 0.9F, false, false, true, debug0);
/*      */   }
/*      */   
/*      */   public static Biome jungleHillsBiome() {
/*  169 */     return jungleBiome(0.45F, 0.3F, 10, 1, 1);
/*      */   }
/*      */   
/*      */   public static Biome bambooJungleBiome() {
/*  173 */     return bambooJungleBiome(0.1F, 0.2F, 40, 2);
/*      */   }
/*      */   
/*      */   public static Biome bambooJungleHillsBiome() {
/*  177 */     return bambooJungleBiome(0.45F, 0.3F, 10, 1);
/*      */   }
/*      */   
/*      */   private static Biome jungleBiome(float debug0, float debug1, int debug2, int debug3, int debug4) {
/*  181 */     MobSpawnSettings.Builder debug5 = new MobSpawnSettings.Builder();
/*  182 */     BiomeDefaultFeatures.baseJungleSpawns(debug5);
/*  183 */     debug5.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, debug2, 1, debug3))
/*  184 */       .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, debug4))
/*  185 */       .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 1, 2));
/*      */     
/*  187 */     debug5.setPlayerCanSpawn();
/*  188 */     return baseJungleBiome(debug0, debug1, 0.9F, false, false, false, debug5);
/*      */   }
/*      */   
/*      */   private static Biome bambooJungleBiome(float debug0, float debug1, int debug2, int debug3) {
/*  192 */     MobSpawnSettings.Builder debug4 = new MobSpawnSettings.Builder();
/*  193 */     BiomeDefaultFeatures.baseJungleSpawns(debug4);
/*  194 */     debug4.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, debug2, 1, debug3))
/*  195 */       .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 80, 1, 2))
/*  196 */       .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 2, 1, 1));
/*      */     
/*  198 */     return baseJungleBiome(debug0, debug1, 0.9F, true, false, false, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Biome baseJungleBiome(float debug0, float debug1, float debug2, boolean debug3, boolean debug4, boolean debug5, MobSpawnSettings.Builder debug6) {
/*  203 */     BiomeGenerationSettings.Builder debug7 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*  204 */     if (!debug4 && !debug5) {
/*  205 */       debug7.addStructureStart(StructureFeatures.JUNGLE_TEMPLE);
/*      */     }
/*  207 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug7);
/*  208 */     debug7.addStructureStart(StructureFeatures.RUINED_PORTAL_JUNGLE);
/*      */     
/*  210 */     BiomeDefaultFeatures.addDefaultCarvers(debug7);
/*      */     
/*  212 */     BiomeDefaultFeatures.addDefaultLakes(debug7);
/*  213 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug7);
/*  214 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug7);
/*  215 */     BiomeDefaultFeatures.addDefaultOres(debug7);
/*  216 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug7);
/*  217 */     if (debug3) {
/*  218 */       BiomeDefaultFeatures.addBambooVegetation(debug7);
/*      */     } else {
/*  220 */       if (!debug4 && !debug5) {
/*  221 */         BiomeDefaultFeatures.addLightBambooVegetation(debug7);
/*      */       }
/*  223 */       if (debug4) {
/*  224 */         BiomeDefaultFeatures.addJungleEdgeTrees(debug7);
/*      */       } else {
/*  226 */         BiomeDefaultFeatures.addJungleTrees(debug7);
/*      */       } 
/*      */     } 
/*  229 */     BiomeDefaultFeatures.addWarmFlowers(debug7);
/*  230 */     BiomeDefaultFeatures.addJungleGrass(debug7);
/*  231 */     BiomeDefaultFeatures.addDefaultMushrooms(debug7);
/*  232 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug7);
/*  233 */     BiomeDefaultFeatures.addDefaultSprings(debug7);
/*  234 */     BiomeDefaultFeatures.addJungleExtraVegetation(debug7);
/*  235 */     BiomeDefaultFeatures.addSurfaceFreezing(debug7);
/*  236 */     return (new Biome.BiomeBuilder())
/*  237 */       .precipitation(Biome.Precipitation.RAIN)
/*  238 */       .biomeCategory(Biome.BiomeCategory.JUNGLE)
/*  239 */       .depth(debug0)
/*  240 */       .scale(debug1)
/*  241 */       .temperature(0.95F)
/*  242 */       .downfall(debug2)
/*  243 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  244 */         .waterColor(4159204)
/*  245 */         .waterFogColor(329011)
/*  246 */         .fogColor(12638463)
/*  247 */         .skyColor(calculateSkyColor(0.95F))
/*  248 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  249 */         .build())
/*      */       
/*  251 */       .mobSpawnSettings(debug6.build())
/*  252 */       .generationSettings(debug7.build())
/*  253 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome mountainBiome(float debug0, float debug1, ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> debug2, boolean debug3) {
/*  257 */     MobSpawnSettings.Builder debug4 = new MobSpawnSettings.Builder();
/*  258 */     BiomeDefaultFeatures.farmAnimals(debug4);
/*  259 */     debug4.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 5, 4, 6));
/*  260 */     BiomeDefaultFeatures.commonSpawns(debug4);
/*      */ 
/*      */     
/*  263 */     BiomeGenerationSettings.Builder debug5 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug2);
/*  264 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug5);
/*  265 */     debug5.addStructureStart(StructureFeatures.RUINED_PORTAL_MOUNTAIN);
/*      */     
/*  267 */     BiomeDefaultFeatures.addDefaultCarvers(debug5);
/*      */     
/*  269 */     BiomeDefaultFeatures.addDefaultLakes(debug5);
/*  270 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug5);
/*  271 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug5);
/*  272 */     BiomeDefaultFeatures.addDefaultOres(debug5);
/*  273 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug5);
/*  274 */     if (debug3) {
/*  275 */       BiomeDefaultFeatures.addMountainEdgeTrees(debug5);
/*      */     } else {
/*  277 */       BiomeDefaultFeatures.addMountainTrees(debug5);
/*      */     } 
/*  279 */     BiomeDefaultFeatures.addDefaultFlowers(debug5);
/*  280 */     BiomeDefaultFeatures.addDefaultGrass(debug5);
/*  281 */     BiomeDefaultFeatures.addDefaultMushrooms(debug5);
/*  282 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug5);
/*  283 */     BiomeDefaultFeatures.addDefaultSprings(debug5);
/*  284 */     BiomeDefaultFeatures.addExtraEmeralds(debug5);
/*  285 */     BiomeDefaultFeatures.addInfestedStone(debug5);
/*  286 */     BiomeDefaultFeatures.addSurfaceFreezing(debug5);
/*      */     
/*  288 */     return (new Biome.BiomeBuilder())
/*  289 */       .precipitation(Biome.Precipitation.RAIN)
/*  290 */       .biomeCategory(Biome.BiomeCategory.EXTREME_HILLS)
/*  291 */       .depth(debug0)
/*  292 */       .scale(debug1)
/*  293 */       .temperature(0.2F)
/*  294 */       .downfall(0.3F)
/*  295 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  296 */         .waterColor(4159204)
/*  297 */         .waterFogColor(329011)
/*  298 */         .fogColor(12638463)
/*  299 */         .skyColor(calculateSkyColor(0.2F))
/*  300 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  301 */         .build())
/*      */       
/*  303 */       .mobSpawnSettings(debug4.build())
/*  304 */       .generationSettings(debug5.build())
/*  305 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome desertBiome(float debug0, float debug1, boolean debug2, boolean debug3, boolean debug4) {
/*  309 */     MobSpawnSettings.Builder debug5 = new MobSpawnSettings.Builder();
/*  310 */     BiomeDefaultFeatures.desertSpawns(debug5);
/*      */ 
/*      */     
/*  313 */     BiomeGenerationSettings.Builder debug6 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.DESERT);
/*  314 */     if (debug2) {
/*  315 */       debug6.addStructureStart(StructureFeatures.VILLAGE_DESERT);
/*  316 */       debug6.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
/*      */     } 
/*  318 */     if (debug3) {
/*  319 */       debug6.addStructureStart(StructureFeatures.DESERT_PYRAMID);
/*      */     }
/*  321 */     if (debug4) {
/*  322 */       BiomeDefaultFeatures.addFossilDecoration(debug6);
/*      */     }
/*  324 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug6);
/*  325 */     debug6.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);
/*      */     
/*  327 */     BiomeDefaultFeatures.addDefaultCarvers(debug6);
/*  328 */     BiomeDefaultFeatures.addDesertLakes(debug6);
/*  329 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug6);
/*  330 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug6);
/*  331 */     BiomeDefaultFeatures.addDefaultOres(debug6);
/*  332 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug6);
/*  333 */     BiomeDefaultFeatures.addDefaultFlowers(debug6);
/*  334 */     BiomeDefaultFeatures.addDefaultGrass(debug6);
/*  335 */     BiomeDefaultFeatures.addDesertVegetation(debug6);
/*  336 */     BiomeDefaultFeatures.addDefaultMushrooms(debug6);
/*  337 */     BiomeDefaultFeatures.addDesertExtraVegetation(debug6);
/*  338 */     BiomeDefaultFeatures.addDefaultSprings(debug6);
/*  339 */     BiomeDefaultFeatures.addDesertExtraDecoration(debug6);
/*  340 */     BiomeDefaultFeatures.addSurfaceFreezing(debug6);
/*      */     
/*  342 */     return (new Biome.BiomeBuilder())
/*  343 */       .precipitation(Biome.Precipitation.NONE)
/*  344 */       .biomeCategory(Biome.BiomeCategory.DESERT)
/*  345 */       .depth(debug0)
/*  346 */       .scale(debug1)
/*  347 */       .temperature(2.0F)
/*  348 */       .downfall(0.0F)
/*  349 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  350 */         .waterColor(4159204)
/*  351 */         .waterFogColor(329011)
/*  352 */         .fogColor(12638463)
/*  353 */         .skyColor(calculateSkyColor(2.0F))
/*  354 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  355 */         .build())
/*      */       
/*  357 */       .mobSpawnSettings(debug5.build())
/*  358 */       .generationSettings(debug6.build())
/*  359 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome plainsBiome(boolean debug0) {
/*  363 */     MobSpawnSettings.Builder debug1 = new MobSpawnSettings.Builder();
/*  364 */     BiomeDefaultFeatures.plainsSpawns(debug1);
/*  365 */     if (!debug0) {
/*  366 */       debug1.setPlayerCanSpawn();
/*      */     }
/*      */ 
/*      */     
/*  370 */     BiomeGenerationSettings.Builder debug2 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*  371 */     if (!debug0) {
/*  372 */       debug2.addStructureStart(StructureFeatures.VILLAGE_PLAINS)
/*  373 */         .addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
/*      */     }
/*  375 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug2);
/*  376 */     debug2.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  378 */     BiomeDefaultFeatures.addDefaultCarvers(debug2);
/*      */     
/*  380 */     BiomeDefaultFeatures.addDefaultLakes(debug2);
/*  381 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug2);
/*  382 */     BiomeDefaultFeatures.addPlainGrass(debug2);
/*  383 */     if (debug0) {
/*  384 */       debug2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUNFLOWER);
/*      */     }
/*  386 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug2);
/*  387 */     BiomeDefaultFeatures.addDefaultOres(debug2);
/*  388 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug2);
/*  389 */     BiomeDefaultFeatures.addPlainVegetation(debug2);
/*  390 */     if (debug0) {
/*  391 */       debug2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_SUGAR_CANE);
/*      */     }
/*  393 */     BiomeDefaultFeatures.addDefaultMushrooms(debug2);
/*  394 */     if (debug0) {
/*  395 */       debug2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_PUMPKIN);
/*      */     } else {
/*  397 */       BiomeDefaultFeatures.addDefaultExtraVegetation(debug2);
/*      */     } 
/*  399 */     BiomeDefaultFeatures.addDefaultSprings(debug2);
/*  400 */     BiomeDefaultFeatures.addSurfaceFreezing(debug2);
/*      */     
/*  402 */     return (new Biome.BiomeBuilder())
/*  403 */       .precipitation(Biome.Precipitation.RAIN)
/*  404 */       .biomeCategory(Biome.BiomeCategory.PLAINS)
/*  405 */       .depth(0.125F)
/*  406 */       .scale(0.05F)
/*  407 */       .temperature(0.8F)
/*  408 */       .downfall(0.4F)
/*  409 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  410 */         .waterColor(4159204)
/*  411 */         .waterFogColor(329011)
/*  412 */         .fogColor(12638463)
/*  413 */         .skyColor(calculateSkyColor(0.8F))
/*  414 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  415 */         .build())
/*      */       
/*  417 */       .mobSpawnSettings(debug1.build())
/*  418 */       .generationSettings(debug2.build())
/*  419 */       .build();
/*      */   }
/*      */   
/*      */   private static Biome baseEndBiome(BiomeGenerationSettings.Builder debug0) {
/*  423 */     MobSpawnSettings.Builder debug1 = new MobSpawnSettings.Builder();
/*  424 */     BiomeDefaultFeatures.endSpawns(debug1);
/*      */     
/*  426 */     return (new Biome.BiomeBuilder())
/*  427 */       .precipitation(Biome.Precipitation.NONE)
/*  428 */       .biomeCategory(Biome.BiomeCategory.THEEND)
/*  429 */       .depth(0.1F)
/*  430 */       .scale(0.2F)
/*  431 */       .temperature(0.5F)
/*  432 */       .downfall(0.5F)
/*  433 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  434 */         .waterColor(4159204)
/*  435 */         .waterFogColor(329011)
/*  436 */         .fogColor(10518688)
/*  437 */         .skyColor(0)
/*  438 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  439 */         .build())
/*      */       
/*  441 */       .mobSpawnSettings(debug1.build())
/*  442 */       .generationSettings(debug0.build())
/*  443 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Biome endBarrensBiome() {
/*  448 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.END);
/*  449 */     return baseEndBiome(debug0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome theEndBiome() {
/*  455 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.END).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.END_SPIKE);
/*  456 */     return baseEndBiome(debug0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome endMidlandsBiome() {
/*  462 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.END).addStructureStart(StructureFeatures.END_CITY);
/*  463 */     return baseEndBiome(debug0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome endHighlandsBiome() {
/*  471 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.END).addStructureStart(StructureFeatures.END_CITY).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.END_GATEWAY).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CHORUS_PLANT);
/*  472 */     return baseEndBiome(debug0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome smallEndIslandsBiome() {
/*  478 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.END).addFeature(GenerationStep.Decoration.RAW_GENERATION, Features.END_ISLAND_DECORATED);
/*  479 */     return baseEndBiome(debug0);
/*      */   }
/*      */   
/*      */   public static Biome mushroomFieldsBiome(float debug0, float debug1) {
/*  483 */     MobSpawnSettings.Builder debug2 = new MobSpawnSettings.Builder();
/*  484 */     BiomeDefaultFeatures.mooshroomSpawns(debug2);
/*      */ 
/*      */     
/*  487 */     BiomeGenerationSettings.Builder debug3 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.MYCELIUM);
/*  488 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug3);
/*  489 */     debug3.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  491 */     BiomeDefaultFeatures.addDefaultCarvers(debug3);
/*      */     
/*  493 */     BiomeDefaultFeatures.addDefaultLakes(debug3);
/*  494 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug3);
/*  495 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug3);
/*  496 */     BiomeDefaultFeatures.addDefaultOres(debug3);
/*  497 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug3);
/*  498 */     BiomeDefaultFeatures.addMushroomFieldVegetation(debug3);
/*  499 */     BiomeDefaultFeatures.addDefaultMushrooms(debug3);
/*  500 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug3);
/*  501 */     BiomeDefaultFeatures.addDefaultSprings(debug3);
/*  502 */     BiomeDefaultFeatures.addSurfaceFreezing(debug3);
/*      */     
/*  504 */     return (new Biome.BiomeBuilder())
/*  505 */       .precipitation(Biome.Precipitation.RAIN)
/*  506 */       .biomeCategory(Biome.BiomeCategory.MUSHROOM)
/*  507 */       .depth(debug0)
/*  508 */       .scale(debug1)
/*  509 */       .temperature(0.9F)
/*  510 */       .downfall(1.0F)
/*  511 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  512 */         .waterColor(4159204)
/*  513 */         .waterFogColor(329011)
/*  514 */         .fogColor(12638463)
/*  515 */         .skyColor(calculateSkyColor(0.9F))
/*  516 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  517 */         .build())
/*      */       
/*  519 */       .mobSpawnSettings(debug2.build())
/*  520 */       .generationSettings(debug3.build())
/*  521 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   private static Biome baseSavannaBiome(float debug0, float debug1, float debug2, boolean debug3, boolean debug4, MobSpawnSettings.Builder debug5) {
/*  526 */     BiomeGenerationSettings.Builder debug6 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug4 ? SurfaceBuilders.SHATTERED_SAVANNA : SurfaceBuilders.GRASS);
/*  527 */     if (!debug3 && !debug4) {
/*  528 */       debug6.addStructureStart(StructureFeatures.VILLAGE_SAVANNA)
/*  529 */         .addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
/*      */     }
/*  531 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug6);
/*  532 */     debug6.addStructureStart(debug3 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  534 */     BiomeDefaultFeatures.addDefaultCarvers(debug6);
/*      */     
/*  536 */     BiomeDefaultFeatures.addDefaultLakes(debug6);
/*  537 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug6);
/*  538 */     if (!debug4) {
/*  539 */       BiomeDefaultFeatures.addSavannaGrass(debug6);
/*      */     }
/*  541 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug6);
/*  542 */     BiomeDefaultFeatures.addDefaultOres(debug6);
/*  543 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug6);
/*  544 */     if (debug4) {
/*  545 */       BiomeDefaultFeatures.addShatteredSavannaTrees(debug6);
/*  546 */       BiomeDefaultFeatures.addDefaultFlowers(debug6);
/*  547 */       BiomeDefaultFeatures.addShatteredSavannaGrass(debug6);
/*      */     } else {
/*  549 */       BiomeDefaultFeatures.addSavannaTrees(debug6);
/*  550 */       BiomeDefaultFeatures.addWarmFlowers(debug6);
/*  551 */       BiomeDefaultFeatures.addSavannaExtraGrass(debug6);
/*      */     } 
/*  553 */     BiomeDefaultFeatures.addDefaultMushrooms(debug6);
/*  554 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug6);
/*  555 */     BiomeDefaultFeatures.addDefaultSprings(debug6);
/*  556 */     BiomeDefaultFeatures.addSurfaceFreezing(debug6);
/*      */     
/*  558 */     return (new Biome.BiomeBuilder())
/*  559 */       .precipitation(Biome.Precipitation.NONE)
/*  560 */       .biomeCategory(Biome.BiomeCategory.SAVANNA)
/*  561 */       .depth(debug0)
/*  562 */       .scale(debug1)
/*  563 */       .temperature(debug2)
/*  564 */       .downfall(0.0F)
/*  565 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  566 */         .waterColor(4159204)
/*  567 */         .waterFogColor(329011)
/*  568 */         .fogColor(12638463)
/*  569 */         .skyColor(calculateSkyColor(debug2))
/*  570 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  571 */         .build())
/*      */       
/*  573 */       .mobSpawnSettings(debug5.build())
/*  574 */       .generationSettings(debug6.build())
/*  575 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome savannaBiome(float debug0, float debug1, float debug2, boolean debug3, boolean debug4) {
/*  579 */     MobSpawnSettings.Builder debug5 = savannaMobs();
/*  580 */     return baseSavannaBiome(debug0, debug1, debug2, debug3, debug4, debug5);
/*      */   }
/*      */   
/*      */   private static MobSpawnSettings.Builder savannaMobs() {
/*  584 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  585 */     BiomeDefaultFeatures.farmAnimals(debug0);
/*  586 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 1, 2, 6))
/*  587 */       .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 1, 1, 1));
/*  588 */     BiomeDefaultFeatures.commonSpawns(debug0);
/*  589 */     return debug0;
/*      */   }
/*      */   
/*      */   public static Biome savanaPlateauBiome() {
/*  593 */     MobSpawnSettings.Builder debug0 = savannaMobs();
/*  594 */     debug0.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 8, 4, 4));
/*  595 */     return baseSavannaBiome(1.5F, 0.025F, 1.0F, true, false, debug0);
/*      */   }
/*      */   
/*      */   private static Biome baseBadlandsBiome(ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> debug0, float debug1, float debug2, boolean debug3, boolean debug4) {
/*  599 */     MobSpawnSettings.Builder debug5 = new MobSpawnSettings.Builder();
/*  600 */     BiomeDefaultFeatures.commonSpawns(debug5);
/*      */ 
/*      */     
/*  603 */     BiomeGenerationSettings.Builder debug6 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug0);
/*  604 */     BiomeDefaultFeatures.addDefaultOverworldLandMesaStructures(debug6);
/*  605 */     debug6.addStructureStart(debug3 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  607 */     BiomeDefaultFeatures.addDefaultCarvers(debug6);
/*      */     
/*  609 */     BiomeDefaultFeatures.addDefaultLakes(debug6);
/*  610 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug6);
/*  611 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug6);
/*  612 */     BiomeDefaultFeatures.addDefaultOres(debug6);
/*  613 */     BiomeDefaultFeatures.addExtraGold(debug6);
/*  614 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug6);
/*  615 */     if (debug4) {
/*  616 */       BiomeDefaultFeatures.addBadlandsTrees(debug6);
/*      */     }
/*  618 */     BiomeDefaultFeatures.addBadlandGrass(debug6);
/*  619 */     BiomeDefaultFeatures.addDefaultMushrooms(debug6);
/*  620 */     BiomeDefaultFeatures.addBadlandExtraVegetation(debug6);
/*  621 */     BiomeDefaultFeatures.addDefaultSprings(debug6);
/*  622 */     BiomeDefaultFeatures.addSurfaceFreezing(debug6);
/*  623 */     return (new Biome.BiomeBuilder())
/*  624 */       .precipitation(Biome.Precipitation.NONE)
/*  625 */       .biomeCategory(Biome.BiomeCategory.MESA)
/*  626 */       .depth(debug1)
/*  627 */       .scale(debug2)
/*  628 */       .temperature(2.0F)
/*  629 */       .downfall(0.0F)
/*  630 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  631 */         .waterColor(4159204)
/*  632 */         .waterFogColor(329011)
/*  633 */         .fogColor(12638463)
/*  634 */         .skyColor(calculateSkyColor(2.0F))
/*  635 */         .foliageColorOverride(10387789)
/*  636 */         .grassColorOverride(9470285)
/*  637 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  638 */         .build())
/*      */       
/*  640 */       .mobSpawnSettings(debug5.build())
/*  641 */       .generationSettings(debug6.build())
/*  642 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome badlandsBiome(float debug0, float debug1, boolean debug2) {
/*  646 */     return baseBadlandsBiome(SurfaceBuilders.BADLANDS, debug0, debug1, debug2, false);
/*      */   }
/*      */   
/*      */   public static Biome woodedBadlandsPlateauBiome(float debug0, float debug1) {
/*  650 */     return baseBadlandsBiome(SurfaceBuilders.WOODED_BADLANDS, debug0, debug1, true, true);
/*      */   }
/*      */   
/*      */   public static Biome erodedBadlandsBiome() {
/*  654 */     return baseBadlandsBiome(SurfaceBuilders.ERODED_BADLANDS, 0.1F, 0.2F, true, false);
/*      */   }
/*      */   
/*      */   private static Biome baseOceanBiome(MobSpawnSettings.Builder debug0, int debug1, int debug2, boolean debug3, BiomeGenerationSettings.Builder debug4) {
/*  658 */     return (new Biome.BiomeBuilder())
/*  659 */       .precipitation(Biome.Precipitation.RAIN)
/*  660 */       .biomeCategory(Biome.BiomeCategory.OCEAN)
/*  661 */       .depth(debug3 ? -1.8F : -1.0F)
/*  662 */       .scale(0.1F)
/*  663 */       .temperature(0.5F)
/*  664 */       .downfall(0.5F)
/*  665 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  666 */         .waterColor(debug1)
/*  667 */         .waterFogColor(debug2)
/*  668 */         .fogColor(12638463)
/*  669 */         .skyColor(calculateSkyColor(0.5F))
/*  670 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  671 */         .build())
/*      */       
/*  673 */       .mobSpawnSettings(debug0.build())
/*  674 */       .generationSettings(debug4.build())
/*  675 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   private static BiomeGenerationSettings.Builder baseOceanGeneration(ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> debug0, boolean debug1, boolean debug2, boolean debug3) {
/*  680 */     BiomeGenerationSettings.Builder debug4 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug0);
/*  681 */     ConfiguredStructureFeature<?, ?> debug5 = debug2 ? StructureFeatures.OCEAN_RUIN_WARM : StructureFeatures.OCEAN_RUIN_COLD;
/*  682 */     if (debug3) {
/*  683 */       if (debug1) {
/*  684 */         debug4.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
/*      */       }
/*  686 */       BiomeDefaultFeatures.addDefaultOverworldOceanStructures(debug4);
/*  687 */       debug4.addStructureStart(debug5);
/*      */     } else {
/*  689 */       debug4.addStructureStart(debug5);
/*  690 */       if (debug1) {
/*  691 */         debug4.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
/*      */       }
/*  693 */       BiomeDefaultFeatures.addDefaultOverworldOceanStructures(debug4);
/*      */     } 
/*  695 */     debug4.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
/*      */     
/*  697 */     BiomeDefaultFeatures.addOceanCarvers(debug4);
/*      */     
/*  699 */     BiomeDefaultFeatures.addDefaultLakes(debug4);
/*  700 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug4);
/*  701 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug4);
/*  702 */     BiomeDefaultFeatures.addDefaultOres(debug4);
/*  703 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug4);
/*  704 */     BiomeDefaultFeatures.addWaterTrees(debug4);
/*  705 */     BiomeDefaultFeatures.addDefaultFlowers(debug4);
/*  706 */     BiomeDefaultFeatures.addDefaultGrass(debug4);
/*  707 */     BiomeDefaultFeatures.addDefaultMushrooms(debug4);
/*  708 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug4);
/*  709 */     BiomeDefaultFeatures.addDefaultSprings(debug4);
/*  710 */     return debug4;
/*      */   }
/*      */   
/*      */   public static Biome coldOceanBiome(boolean debug0) {
/*  714 */     MobSpawnSettings.Builder debug1 = new MobSpawnSettings.Builder();
/*  715 */     BiomeDefaultFeatures.oceanSpawns(debug1, 3, 4, 15);
/*  716 */     debug1.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5));
/*      */     
/*  718 */     boolean debug2 = !debug0;
/*  719 */     BiomeGenerationSettings.Builder debug3 = baseOceanGeneration(SurfaceBuilders.GRASS, debug0, false, debug2);
/*  720 */     debug3.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, debug0 ? Features.SEAGRASS_DEEP_COLD : Features.SEAGRASS_COLD);
/*  721 */     BiomeDefaultFeatures.addDefaultSeagrass(debug3);
/*  722 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(debug3);
/*  723 */     BiomeDefaultFeatures.addSurfaceFreezing(debug3);
/*      */     
/*  725 */     return baseOceanBiome(debug1, 4020182, 329011, debug0, debug3);
/*      */   }
/*      */   
/*      */   public static Biome oceanBiome(boolean debug0) {
/*  729 */     MobSpawnSettings.Builder debug1 = new MobSpawnSettings.Builder();
/*  730 */     BiomeDefaultFeatures.oceanSpawns(debug1, 1, 4, 10);
/*  731 */     debug1.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 1, 1, 2));
/*      */     
/*  733 */     BiomeGenerationSettings.Builder debug2 = baseOceanGeneration(SurfaceBuilders.GRASS, debug0, false, true);
/*  734 */     debug2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, debug0 ? Features.SEAGRASS_DEEP : Features.SEAGRASS_NORMAL);
/*  735 */     BiomeDefaultFeatures.addDefaultSeagrass(debug2);
/*  736 */     BiomeDefaultFeatures.addColdOceanExtraVegetation(debug2);
/*  737 */     BiomeDefaultFeatures.addSurfaceFreezing(debug2);
/*      */     
/*  739 */     return baseOceanBiome(debug1, 4159204, 329011, debug0, debug2);
/*      */   }
/*      */   
/*      */   public static Biome lukeWarmOceanBiome(boolean debug0) {
/*  743 */     MobSpawnSettings.Builder debug1 = new MobSpawnSettings.Builder();
/*  744 */     if (debug0) {
/*  745 */       BiomeDefaultFeatures.oceanSpawns(debug1, 8, 4, 8);
/*      */     } else {
/*  747 */       BiomeDefaultFeatures.oceanSpawns(debug1, 10, 2, 15);
/*      */     } 
/*  749 */     debug1.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 5, 1, 3))
/*  750 */       .addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8))
/*  751 */       .addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
/*      */     
/*  753 */     BiomeGenerationSettings.Builder debug2 = baseOceanGeneration(SurfaceBuilders.OCEAN_SAND, debug0, true, false);
/*  754 */     debug2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, debug0 ? Features.SEAGRASS_DEEP_WARM : Features.SEAGRASS_WARM);
/*  755 */     if (debug0) {
/*  756 */       BiomeDefaultFeatures.addDefaultSeagrass(debug2);
/*      */     }
/*  758 */     BiomeDefaultFeatures.addLukeWarmKelp(debug2);
/*  759 */     BiomeDefaultFeatures.addSurfaceFreezing(debug2);
/*      */     
/*  761 */     return baseOceanBiome(debug1, 4566514, 267827, debug0, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Biome warmOceanBiome() {
/*  766 */     MobSpawnSettings.Builder debug0 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PUFFERFISH, 15, 1, 3));
/*  767 */     BiomeDefaultFeatures.warmOceanSpawns(debug0, 10, 4);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  772 */     BiomeGenerationSettings.Builder debug1 = baseOceanGeneration(SurfaceBuilders.FULL_SAND, false, true, false).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARM_OCEAN_VEGETATION).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_WARM).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEA_PICKLE);
/*  773 */     BiomeDefaultFeatures.addSurfaceFreezing(debug1);
/*      */     
/*  775 */     return baseOceanBiome(debug0, 4445678, 270131, false, debug1);
/*      */   }
/*      */   
/*      */   public static Biome deepWarmOceanBiome() {
/*  779 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  780 */     BiomeDefaultFeatures.warmOceanSpawns(debug0, 5, 1);
/*  781 */     debug0.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
/*      */ 
/*      */     
/*  784 */     BiomeGenerationSettings.Builder debug1 = baseOceanGeneration(SurfaceBuilders.FULL_SAND, true, true, false).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_DEEP_WARM);
/*  785 */     BiomeDefaultFeatures.addDefaultSeagrass(debug1);
/*  786 */     BiomeDefaultFeatures.addSurfaceFreezing(debug1);
/*      */     
/*  788 */     return baseOceanBiome(debug0, 4445678, 270131, true, debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome frozenOceanBiome(boolean debug0) {
/*  795 */     MobSpawnSettings.Builder debug1 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 1, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
/*  796 */     BiomeDefaultFeatures.commonSpawns(debug1);
/*  797 */     debug1.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
/*      */     
/*  799 */     float debug2 = debug0 ? 0.5F : 0.0F;
/*      */     
/*  801 */     BiomeGenerationSettings.Builder debug3 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.FROZEN_OCEAN);
/*  802 */     debug3.addStructureStart(StructureFeatures.OCEAN_RUIN_COLD);
/*  803 */     if (debug0) {
/*  804 */       debug3.addStructureStart(StructureFeatures.OCEAN_MONUMENT);
/*      */     }
/*  806 */     BiomeDefaultFeatures.addDefaultOverworldOceanStructures(debug3);
/*  807 */     debug3.addStructureStart(StructureFeatures.RUINED_PORTAL_OCEAN);
/*      */     
/*  809 */     BiomeDefaultFeatures.addOceanCarvers(debug3);
/*      */     
/*  811 */     BiomeDefaultFeatures.addDefaultLakes(debug3);
/*  812 */     BiomeDefaultFeatures.addIcebergs(debug3);
/*  813 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug3);
/*  814 */     BiomeDefaultFeatures.addBlueIce(debug3);
/*  815 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug3);
/*  816 */     BiomeDefaultFeatures.addDefaultOres(debug3);
/*  817 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug3);
/*  818 */     BiomeDefaultFeatures.addWaterTrees(debug3);
/*  819 */     BiomeDefaultFeatures.addDefaultFlowers(debug3);
/*  820 */     BiomeDefaultFeatures.addDefaultGrass(debug3);
/*  821 */     BiomeDefaultFeatures.addDefaultMushrooms(debug3);
/*  822 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug3);
/*  823 */     BiomeDefaultFeatures.addDefaultSprings(debug3);
/*  824 */     BiomeDefaultFeatures.addSurfaceFreezing(debug3);
/*      */     
/*  826 */     return (new Biome.BiomeBuilder())
/*  827 */       .precipitation(debug0 ? Biome.Precipitation.RAIN : Biome.Precipitation.SNOW)
/*  828 */       .biomeCategory(Biome.BiomeCategory.OCEAN)
/*  829 */       .depth(debug0 ? -1.8F : -1.0F)
/*  830 */       .scale(0.1F)
/*  831 */       .temperature(debug2)
/*  832 */       .temperatureAdjustment(Biome.TemperatureModifier.FROZEN)
/*  833 */       .downfall(0.5F)
/*  834 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  835 */         .waterColor(3750089)
/*  836 */         .waterFogColor(329011)
/*  837 */         .fogColor(12638463)
/*  838 */         .skyColor(calculateSkyColor(debug2))
/*  839 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  840 */         .build())
/*      */       
/*  842 */       .mobSpawnSettings(debug1.build())
/*  843 */       .generationSettings(debug3.build())
/*  844 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   private static Biome baseForestBiome(float debug0, float debug1, boolean debug2, MobSpawnSettings.Builder debug3) {
/*  849 */     BiomeGenerationSettings.Builder debug4 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*  850 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug4);
/*  851 */     debug4.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  853 */     BiomeDefaultFeatures.addDefaultCarvers(debug4);
/*      */     
/*  855 */     BiomeDefaultFeatures.addDefaultLakes(debug4);
/*  856 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug4);
/*  857 */     if (debug2) {
/*  858 */       debug4.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_VEGETATION_COMMON);
/*      */     } else {
/*  860 */       BiomeDefaultFeatures.addForestFlowers(debug4);
/*      */     } 
/*  862 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug4);
/*  863 */     BiomeDefaultFeatures.addDefaultOres(debug4);
/*  864 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug4);
/*  865 */     if (debug2) {
/*  866 */       debug4.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FOREST_FLOWER_TREES);
/*  867 */       debug4.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_FOREST);
/*  868 */       BiomeDefaultFeatures.addDefaultGrass(debug4);
/*      */     } else {
/*  870 */       BiomeDefaultFeatures.addOtherBirchTrees(debug4);
/*  871 */       BiomeDefaultFeatures.addDefaultFlowers(debug4);
/*  872 */       BiomeDefaultFeatures.addForestGrass(debug4);
/*      */     } 
/*  874 */     BiomeDefaultFeatures.addDefaultMushrooms(debug4);
/*  875 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug4);
/*  876 */     BiomeDefaultFeatures.addDefaultSprings(debug4);
/*  877 */     BiomeDefaultFeatures.addSurfaceFreezing(debug4);
/*      */     
/*  879 */     return (new Biome.BiomeBuilder())
/*  880 */       .precipitation(Biome.Precipitation.RAIN)
/*  881 */       .biomeCategory(Biome.BiomeCategory.FOREST)
/*  882 */       .depth(debug0)
/*  883 */       .scale(debug1)
/*  884 */       .temperature(0.7F)
/*  885 */       .downfall(0.8F)
/*  886 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  887 */         .waterColor(4159204)
/*  888 */         .waterFogColor(329011)
/*  889 */         .fogColor(12638463)
/*  890 */         .skyColor(calculateSkyColor(0.7F))
/*  891 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  892 */         .build())
/*      */       
/*  894 */       .mobSpawnSettings(debug3.build())
/*  895 */       .generationSettings(debug4.build())
/*  896 */       .build();
/*      */   }
/*      */   
/*      */   private static MobSpawnSettings.Builder defaultSpawns() {
/*  900 */     MobSpawnSettings.Builder debug0 = new MobSpawnSettings.Builder();
/*  901 */     BiomeDefaultFeatures.farmAnimals(debug0);
/*  902 */     BiomeDefaultFeatures.commonSpawns(debug0);
/*  903 */     return debug0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome forestBiome(float debug0, float debug1) {
/*  909 */     MobSpawnSettings.Builder debug2 = defaultSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4)).setPlayerCanSpawn();
/*  910 */     return baseForestBiome(debug0, debug1, false, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Biome flowerForestBiome() {
/*  915 */     MobSpawnSettings.Builder debug0 = defaultSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
/*  916 */     return baseForestBiome(0.1F, 0.4F, true, debug0);
/*      */   }
/*      */   
/*      */   public static Biome taigaBiome(float debug0, float debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5) {
/*  920 */     MobSpawnSettings.Builder debug6 = new MobSpawnSettings.Builder();
/*  921 */     BiomeDefaultFeatures.farmAnimals(debug6);
/*  922 */     debug6.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4))
/*  923 */       .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3))
/*  924 */       .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));
/*      */     
/*  926 */     if (!debug2 && !debug3) {
/*  927 */       debug6.setPlayerCanSpawn();
/*      */     }
/*  929 */     BiomeDefaultFeatures.commonSpawns(debug6);
/*      */     
/*  931 */     float debug7 = debug2 ? -0.5F : 0.25F;
/*      */ 
/*      */     
/*  934 */     BiomeGenerationSettings.Builder debug8 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*  935 */     if (debug4) {
/*  936 */       debug8.addStructureStart(StructureFeatures.VILLAGE_TAIGA);
/*  937 */       debug8.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
/*      */     } 
/*  939 */     if (debug5) {
/*  940 */       debug8.addStructureStart(StructureFeatures.IGLOO);
/*      */     }
/*  942 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug8);
/*  943 */     debug8.addStructureStart(debug3 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  945 */     BiomeDefaultFeatures.addDefaultCarvers(debug8);
/*      */     
/*  947 */     BiomeDefaultFeatures.addDefaultLakes(debug8);
/*  948 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug8);
/*  949 */     BiomeDefaultFeatures.addFerns(debug8);
/*  950 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug8);
/*  951 */     BiomeDefaultFeatures.addDefaultOres(debug8);
/*  952 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug8);
/*  953 */     BiomeDefaultFeatures.addTaigaTrees(debug8);
/*  954 */     BiomeDefaultFeatures.addDefaultFlowers(debug8);
/*  955 */     BiomeDefaultFeatures.addTaigaGrass(debug8);
/*  956 */     BiomeDefaultFeatures.addDefaultMushrooms(debug8);
/*  957 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug8);
/*  958 */     BiomeDefaultFeatures.addDefaultSprings(debug8);
/*  959 */     if (debug2) {
/*  960 */       BiomeDefaultFeatures.addBerryBushes(debug8);
/*      */     } else {
/*  962 */       BiomeDefaultFeatures.addSparseBerryBushes(debug8);
/*      */     } 
/*  964 */     BiomeDefaultFeatures.addSurfaceFreezing(debug8);
/*      */     
/*  966 */     return (new Biome.BiomeBuilder())
/*  967 */       .precipitation(debug2 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN)
/*  968 */       .biomeCategory(Biome.BiomeCategory.TAIGA)
/*  969 */       .depth(debug0)
/*  970 */       .scale(debug1)
/*  971 */       .temperature(debug7)
/*  972 */       .downfall(debug2 ? 0.4F : 0.8F)
/*  973 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  974 */         .waterColor(debug2 ? 4020182 : 4159204)
/*  975 */         .waterFogColor(329011)
/*  976 */         .fogColor(12638463)
/*  977 */         .skyColor(calculateSkyColor(debug7))
/*  978 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/*  979 */         .build())
/*      */       
/*  981 */       .mobSpawnSettings(debug6.build())
/*  982 */       .generationSettings(debug8.build())
/*  983 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome darkForestBiome(float debug0, float debug1, boolean debug2) {
/*  987 */     MobSpawnSettings.Builder debug3 = new MobSpawnSettings.Builder();
/*  988 */     BiomeDefaultFeatures.farmAnimals(debug3);
/*  989 */     BiomeDefaultFeatures.commonSpawns(debug3);
/*      */ 
/*      */     
/*  992 */     BiomeGenerationSettings.Builder debug4 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/*  993 */     debug4.addStructureStart(StructureFeatures.WOODLAND_MANSION);
/*  994 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug4);
/*  995 */     debug4.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/*  997 */     BiomeDefaultFeatures.addDefaultCarvers(debug4);
/*      */     
/*  999 */     BiomeDefaultFeatures.addDefaultLakes(debug4);
/* 1000 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug4);
/* 1001 */     debug4.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, debug2 ? Features.DARK_FOREST_VEGETATION_RED : Features.DARK_FOREST_VEGETATION_BROWN);
/* 1002 */     BiomeDefaultFeatures.addForestFlowers(debug4);
/* 1003 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug4);
/* 1004 */     BiomeDefaultFeatures.addDefaultOres(debug4);
/* 1005 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug4);
/* 1006 */     BiomeDefaultFeatures.addDefaultFlowers(debug4);
/* 1007 */     BiomeDefaultFeatures.addForestGrass(debug4);
/* 1008 */     BiomeDefaultFeatures.addDefaultMushrooms(debug4);
/* 1009 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug4);
/* 1010 */     BiomeDefaultFeatures.addDefaultSprings(debug4);
/* 1011 */     BiomeDefaultFeatures.addSurfaceFreezing(debug4);
/*      */     
/* 1013 */     return (new Biome.BiomeBuilder())
/* 1014 */       .precipitation(Biome.Precipitation.RAIN)
/* 1015 */       .biomeCategory(Biome.BiomeCategory.FOREST)
/* 1016 */       .depth(debug0)
/* 1017 */       .scale(debug1)
/* 1018 */       .temperature(0.7F)
/* 1019 */       .downfall(0.8F)
/* 1020 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1021 */         .waterColor(4159204)
/* 1022 */         .waterFogColor(329011)
/* 1023 */         .fogColor(12638463)
/* 1024 */         .skyColor(calculateSkyColor(0.7F))
/* 1025 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.DARK_FOREST)
/* 1026 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1027 */         .build())
/*      */       
/* 1029 */       .mobSpawnSettings(debug3.build())
/* 1030 */       .generationSettings(debug4.build())
/* 1031 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome swampBiome(float debug0, float debug1, boolean debug2) {
/* 1035 */     MobSpawnSettings.Builder debug3 = new MobSpawnSettings.Builder();
/* 1036 */     BiomeDefaultFeatures.farmAnimals(debug3);
/* 1037 */     BiomeDefaultFeatures.commonSpawns(debug3);
/* 1038 */     debug3.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1, 1));
/*      */ 
/*      */     
/* 1041 */     BiomeGenerationSettings.Builder debug4 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.SWAMP);
/*      */     
/* 1043 */     if (!debug2) {
/* 1044 */       debug4.addStructureStart(StructureFeatures.SWAMP_HUT);
/*      */     }
/* 1046 */     debug4.addStructureStart(StructureFeatures.MINESHAFT);
/* 1047 */     debug4.addStructureStart(StructureFeatures.RUINED_PORTAL_SWAMP);
/*      */     
/* 1049 */     BiomeDefaultFeatures.addDefaultCarvers(debug4);
/* 1050 */     if (!debug2) {
/* 1051 */       BiomeDefaultFeatures.addFossilDecoration(debug4);
/*      */     }
/* 1053 */     BiomeDefaultFeatures.addDefaultLakes(debug4);
/* 1054 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug4);
/* 1055 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug4);
/* 1056 */     BiomeDefaultFeatures.addDefaultOres(debug4);
/* 1057 */     BiomeDefaultFeatures.addSwampClayDisk(debug4);
/* 1058 */     BiomeDefaultFeatures.addSwampVegetation(debug4);
/* 1059 */     BiomeDefaultFeatures.addDefaultMushrooms(debug4);
/* 1060 */     BiomeDefaultFeatures.addSwampExtraVegetation(debug4);
/* 1061 */     BiomeDefaultFeatures.addDefaultSprings(debug4);
/* 1062 */     if (debug2) {
/* 1063 */       BiomeDefaultFeatures.addFossilDecoration(debug4);
/*      */     } else {
/* 1065 */       debug4.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_SWAMP);
/*      */     } 
/* 1067 */     BiomeDefaultFeatures.addSurfaceFreezing(debug4);
/*      */     
/* 1069 */     return (new Biome.BiomeBuilder())
/* 1070 */       .precipitation(Biome.Precipitation.RAIN)
/* 1071 */       .biomeCategory(Biome.BiomeCategory.SWAMP)
/* 1072 */       .depth(debug0)
/* 1073 */       .scale(debug1)
/* 1074 */       .temperature(0.8F)
/* 1075 */       .downfall(0.9F)
/* 1076 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1077 */         .waterColor(6388580)
/* 1078 */         .waterFogColor(2302743)
/* 1079 */         .fogColor(12638463)
/* 1080 */         .skyColor(calculateSkyColor(0.8F))
/* 1081 */         .foliageColorOverride(6975545)
/* 1082 */         .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
/* 1083 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1084 */         .build())
/*      */       
/* 1086 */       .mobSpawnSettings(debug3.build())
/* 1087 */       .generationSettings(debug4.build())
/* 1088 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Biome tundraBiome(float debug0, float debug1, boolean debug2, boolean debug3) {
/* 1093 */     MobSpawnSettings.Builder debug4 = (new MobSpawnSettings.Builder()).creatureGenerationProbability(0.07F);
/* 1094 */     BiomeDefaultFeatures.snowySpawns(debug4);
/*      */ 
/*      */     
/* 1097 */     BiomeGenerationSettings.Builder debug5 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug2 ? SurfaceBuilders.ICE_SPIKES : SurfaceBuilders.GRASS);
/*      */     
/* 1099 */     if (!debug2 && !debug3) {
/* 1100 */       debug5.addStructureStart(StructureFeatures.VILLAGE_SNOWY)
/* 1101 */         .addStructureStart(StructureFeatures.IGLOO);
/*      */     }
/* 1103 */     BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug5);
/* 1104 */     if (!debug2 && !debug3) {
/* 1105 */       debug5.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
/*      */     }
/* 1107 */     debug5.addStructureStart(debug3 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/* 1109 */     BiomeDefaultFeatures.addDefaultCarvers(debug5);
/*      */     
/* 1111 */     BiomeDefaultFeatures.addDefaultLakes(debug5);
/* 1112 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug5);
/* 1113 */     if (debug2) {
/* 1114 */       debug5.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.ICE_SPIKE);
/* 1115 */       debug5.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.ICE_PATCH);
/*      */     } 
/*      */     
/* 1118 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug5);
/* 1119 */     BiomeDefaultFeatures.addDefaultOres(debug5);
/* 1120 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug5);
/* 1121 */     BiomeDefaultFeatures.addSnowyTrees(debug5);
/* 1122 */     BiomeDefaultFeatures.addDefaultFlowers(debug5);
/* 1123 */     BiomeDefaultFeatures.addDefaultGrass(debug5);
/* 1124 */     BiomeDefaultFeatures.addDefaultMushrooms(debug5);
/* 1125 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug5);
/* 1126 */     BiomeDefaultFeatures.addDefaultSprings(debug5);
/* 1127 */     BiomeDefaultFeatures.addSurfaceFreezing(debug5);
/*      */     
/* 1129 */     return (new Biome.BiomeBuilder())
/* 1130 */       .precipitation(Biome.Precipitation.SNOW)
/* 1131 */       .biomeCategory(Biome.BiomeCategory.ICY)
/* 1132 */       .depth(debug0)
/* 1133 */       .scale(debug1)
/* 1134 */       .temperature(0.0F)
/* 1135 */       .downfall(0.5F)
/* 1136 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1137 */         .waterColor(4159204)
/* 1138 */         .waterFogColor(329011)
/* 1139 */         .fogColor(12638463)
/* 1140 */         .skyColor(calculateSkyColor(0.0F))
/* 1141 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1142 */         .build())
/*      */       
/* 1144 */       .mobSpawnSettings(debug4.build())
/* 1145 */       .generationSettings(debug5.build())
/* 1146 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome riverBiome(float debug0, float debug1, float debug2, int debug3, boolean debug4) {
/* 1152 */     MobSpawnSettings.Builder debug5 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 1, 4)).addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 5, 1, 5));
/* 1153 */     BiomeDefaultFeatures.commonSpawns(debug5);
/* 1154 */     debug5.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, debug4 ? 1 : 100, 1, 1));
/*      */ 
/*      */     
/* 1157 */     BiomeGenerationSettings.Builder debug6 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.GRASS);
/* 1158 */     debug6.addStructureStart(StructureFeatures.MINESHAFT);
/* 1159 */     debug6.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/* 1161 */     BiomeDefaultFeatures.addDefaultCarvers(debug6);
/*      */     
/* 1163 */     BiomeDefaultFeatures.addDefaultLakes(debug6);
/* 1164 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug6);
/* 1165 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug6);
/* 1166 */     BiomeDefaultFeatures.addDefaultOres(debug6);
/* 1167 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug6);
/* 1168 */     BiomeDefaultFeatures.addWaterTrees(debug6);
/* 1169 */     BiomeDefaultFeatures.addDefaultFlowers(debug6);
/* 1170 */     BiomeDefaultFeatures.addDefaultGrass(debug6);
/* 1171 */     BiomeDefaultFeatures.addDefaultMushrooms(debug6);
/* 1172 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug6);
/* 1173 */     BiomeDefaultFeatures.addDefaultSprings(debug6);
/* 1174 */     if (!debug4) {
/* 1175 */       debug6.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_RIVER);
/*      */     }
/* 1177 */     BiomeDefaultFeatures.addSurfaceFreezing(debug6);
/*      */     
/* 1179 */     return (new Biome.BiomeBuilder())
/* 1180 */       .precipitation(debug4 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN)
/* 1181 */       .biomeCategory(Biome.BiomeCategory.RIVER)
/* 1182 */       .depth(debug0)
/* 1183 */       .scale(debug1)
/* 1184 */       .temperature(debug2)
/* 1185 */       .downfall(0.5F)
/* 1186 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1187 */         .waterColor(debug3)
/* 1188 */         .waterFogColor(329011)
/* 1189 */         .fogColor(12638463)
/* 1190 */         .skyColor(calculateSkyColor(debug2))
/* 1191 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1192 */         .build())
/*      */       
/* 1194 */       .mobSpawnSettings(debug5.build())
/* 1195 */       .generationSettings(debug6.build())
/* 1196 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome beachBiome(float debug0, float debug1, float debug2, float debug3, int debug4, boolean debug5, boolean debug6) {
/* 1200 */     MobSpawnSettings.Builder debug7 = new MobSpawnSettings.Builder();
/* 1201 */     if (!debug6 && !debug5) {
/* 1202 */       debug7.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.TURTLE, 5, 2, 5));
/*      */     }
/* 1204 */     BiomeDefaultFeatures.commonSpawns(debug7);
/*      */ 
/*      */     
/* 1207 */     BiomeGenerationSettings.Builder debug8 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(debug6 ? SurfaceBuilders.STONE : SurfaceBuilders.DESERT);
/* 1208 */     if (debug6) {
/* 1209 */       BiomeDefaultFeatures.addDefaultOverworldLandStructures(debug8);
/*      */     } else {
/* 1211 */       debug8.addStructureStart(StructureFeatures.MINESHAFT);
/* 1212 */       debug8.addStructureStart(StructureFeatures.BURIED_TREASURE);
/* 1213 */       debug8.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);
/*      */     } 
/* 1215 */     debug8.addStructureStart(debug6 ? StructureFeatures.RUINED_PORTAL_MOUNTAIN : StructureFeatures.RUINED_PORTAL_STANDARD);
/*      */     
/* 1217 */     BiomeDefaultFeatures.addDefaultCarvers(debug8);
/*      */     
/* 1219 */     BiomeDefaultFeatures.addDefaultLakes(debug8);
/* 1220 */     BiomeDefaultFeatures.addDefaultMonsterRoom(debug8);
/* 1221 */     BiomeDefaultFeatures.addDefaultUndergroundVariety(debug8);
/* 1222 */     BiomeDefaultFeatures.addDefaultOres(debug8);
/* 1223 */     BiomeDefaultFeatures.addDefaultSoftDisks(debug8);
/* 1224 */     BiomeDefaultFeatures.addDefaultFlowers(debug8);
/* 1225 */     BiomeDefaultFeatures.addDefaultGrass(debug8);
/* 1226 */     BiomeDefaultFeatures.addDefaultMushrooms(debug8);
/* 1227 */     BiomeDefaultFeatures.addDefaultExtraVegetation(debug8);
/* 1228 */     BiomeDefaultFeatures.addDefaultSprings(debug8);
/* 1229 */     BiomeDefaultFeatures.addSurfaceFreezing(debug8);
/*      */     
/* 1231 */     return (new Biome.BiomeBuilder())
/* 1232 */       .precipitation(debug5 ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN)
/* 1233 */       .biomeCategory(debug6 ? Biome.BiomeCategory.NONE : Biome.BiomeCategory.BEACH)
/* 1234 */       .depth(debug0)
/* 1235 */       .scale(debug1)
/* 1236 */       .temperature(debug2)
/* 1237 */       .downfall(debug3)
/* 1238 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1239 */         .waterColor(debug4)
/* 1240 */         .waterFogColor(329011)
/* 1241 */         .fogColor(12638463)
/* 1242 */         .skyColor(calculateSkyColor(debug2))
/* 1243 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1244 */         .build())
/*      */       
/* 1246 */       .mobSpawnSettings(debug7.build())
/* 1247 */       .generationSettings(debug8.build())
/* 1248 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Biome theVoidBiome() {
/* 1253 */     BiomeGenerationSettings.Builder debug0 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.NOPE);
/* 1254 */     debug0.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Features.VOID_START_PLATFORM);
/*      */     
/* 1256 */     return (new Biome.BiomeBuilder())
/* 1257 */       .precipitation(Biome.Precipitation.NONE)
/* 1258 */       .biomeCategory(Biome.BiomeCategory.NONE)
/* 1259 */       .depth(0.1F)
/* 1260 */       .scale(0.2F)
/* 1261 */       .temperature(0.5F)
/* 1262 */       .downfall(0.5F)
/* 1263 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1264 */         .waterColor(4159204)
/* 1265 */         .waterFogColor(329011)
/* 1266 */         .fogColor(12638463)
/* 1267 */         .skyColor(calculateSkyColor(0.5F))
/* 1268 */         .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
/* 1269 */         .build())
/*      */       
/* 1271 */       .mobSpawnSettings(MobSpawnSettings.EMPTY)
/* 1272 */       .generationSettings(debug0.build())
/* 1273 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome netherWastesBiome() {
/* 1284 */     MobSpawnSettings debug0 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 15, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1292 */     BiomeGenerationSettings.Builder debug1 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.NETHER).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
/*      */     
/* 1294 */     BiomeDefaultFeatures.addDefaultMushrooms(debug1);
/* 1295 */     debug1.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
/* 1296 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
/* 1297 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE)
/* 1298 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
/* 1299 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
/* 1300 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BROWN_MUSHROOM_NETHER)
/* 1301 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.RED_MUSHROOM_NETHER)
/* 1302 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
/* 1303 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED);
/*      */     
/* 1305 */     BiomeDefaultFeatures.addNetherDefaultOres(debug1);
/*      */     
/* 1307 */     return (new Biome.BiomeBuilder())
/* 1308 */       .precipitation(Biome.Precipitation.NONE)
/* 1309 */       .biomeCategory(Biome.BiomeCategory.NETHER)
/* 1310 */       .depth(0.1F)
/* 1311 */       .scale(0.2F)
/* 1312 */       .temperature(2.0F)
/* 1313 */       .downfall(0.0F)
/* 1314 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1315 */         .waterColor(4159204)
/* 1316 */         .waterFogColor(329011)
/* 1317 */         .fogColor(3344392)
/* 1318 */         .skyColor(calculateSkyColor(2.0F))
/* 1319 */         .ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
/* 1320 */         .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0D))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1326 */         .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))
/*      */ 
/*      */ 
/*      */         
/* 1330 */         .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES))
/* 1331 */         .build())
/*      */       
/* 1333 */       .mobSpawnSettings(debug0)
/* 1334 */       .generationSettings(debug1.build())
/* 1335 */       .build();
/*      */   }
/*      */   
/*      */   public static Biome soulSandValleyBiome() {
/* 1339 */     double debug0 = 0.7D;
/* 1340 */     double debug2 = 0.15D;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1351 */     MobSpawnSettings debug4 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 20, 5, 5)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 50, 4, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).addMobCharge(EntityType.SKELETON, 0.7D, 0.15D).addMobCharge(EntityType.GHAST, 0.7D, 0.15D).addMobCharge(EntityType.ENDERMAN, 0.7D, 0.15D).addMobCharge(EntityType.STRIDER, 0.7D, 0.15D).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1371 */     BiomeGenerationSettings.Builder debug5 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.SOUL_SAND_VALLEY).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.NETHER_FOSSIL).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addStructureStart(StructureFeatures.BASTION_REMNANT).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA).addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, Features.BASALT_PILLAR).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_CRIMSON_ROOTS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_SOUL_SAND);
/*      */     
/* 1373 */     BiomeDefaultFeatures.addNetherDefaultOres(debug5);
/*      */     
/* 1375 */     return (new Biome.BiomeBuilder())
/* 1376 */       .precipitation(Biome.Precipitation.NONE)
/* 1377 */       .biomeCategory(Biome.BiomeCategory.NETHER)
/* 1378 */       .depth(0.1F)
/* 1379 */       .scale(0.2F)
/* 1380 */       .temperature(2.0F)
/* 1381 */       .downfall(0.0F)
/* 1382 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1383 */         .waterColor(4159204)
/* 1384 */         .waterFogColor(329011)
/* 1385 */         .fogColor(1787717)
/* 1386 */         .skyColor(calculateSkyColor(2.0F))
/* 1387 */         .ambientParticle(new AmbientParticleSettings((ParticleOptions)ParticleTypes.ASH, 0.00625F))
/* 1388 */         .ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
/* 1389 */         .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1395 */         .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))
/*      */ 
/*      */ 
/*      */         
/* 1399 */         .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY))
/* 1400 */         .build())
/*      */       
/* 1402 */       .mobSpawnSettings(debug4)
/* 1403 */       .generationSettings(debug5.build())
/* 1404 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome basaltDeltasBiome() {
/* 1412 */     MobSpawnSettings debug0 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 40, 1, 1)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 100, 2, 5)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1435 */     BiomeGenerationSettings.Builder debug1 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.BASALT_DELTAS).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addStructureStart(StructureFeatures.NETHER_BRIDGE).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.DELTA).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA_DOUBLE).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.SMALL_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, Features.LARGE_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BASALT_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BLACKSTONE_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_DELTA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.BROWN_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.RED_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED_DOUBLE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_GOLD_DELTAS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_QUARTZ_DELTAS);
/* 1436 */     BiomeDefaultFeatures.addAncientDebris(debug1);
/*      */     
/* 1438 */     return (new Biome.BiomeBuilder())
/* 1439 */       .precipitation(Biome.Precipitation.NONE)
/* 1440 */       .biomeCategory(Biome.BiomeCategory.NETHER)
/* 1441 */       .depth(0.1F)
/* 1442 */       .scale(0.2F)
/* 1443 */       .temperature(2.0F)
/* 1444 */       .downfall(0.0F)
/* 1445 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1446 */         .waterColor(4159204)
/* 1447 */         .waterFogColor(4341314)
/* 1448 */         .fogColor(6840176)
/* 1449 */         .skyColor(calculateSkyColor(2.0F))
/* 1450 */         .ambientParticle(new AmbientParticleSettings((ParticleOptions)ParticleTypes.WHITE_ASH, 0.118093334F))
/* 1451 */         .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
/* 1452 */         .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0D))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1458 */         .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111D))
/*      */ 
/*      */ 
/*      */         
/* 1462 */         .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS))
/* 1463 */         .build())
/*      */       
/* 1465 */       .mobSpawnSettings(debug0)
/* 1466 */       .generationSettings(debug1.build())
/* 1467 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome crimsonForestBiome() {
/* 1476 */     MobSpawnSettings debug0 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 9, 3, 4)).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 5, 3, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1484 */     BiomeGenerationSettings.Builder debug1 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.CRIMSON_FOREST).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
/*      */     
/* 1486 */     BiomeDefaultFeatures.addDefaultMushrooms(debug1);
/* 1487 */     debug1.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
/* 1488 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
/* 1489 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
/* 1490 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
/* 1491 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
/* 1492 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED)
/* 1493 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WEEPING_VINES)
/* 1494 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FUNGI)
/* 1495 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CRIMSON_FOREST_VEGETATION);
/*      */     
/* 1497 */     BiomeDefaultFeatures.addNetherDefaultOres(debug1);
/*      */     
/* 1499 */     return (new Biome.BiomeBuilder())
/* 1500 */       .precipitation(Biome.Precipitation.NONE)
/* 1501 */       .biomeCategory(Biome.BiomeCategory.NETHER)
/* 1502 */       .depth(0.1F)
/* 1503 */       .scale(0.2F)
/* 1504 */       .temperature(2.0F)
/* 1505 */       .downfall(0.0F)
/* 1506 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1507 */         .waterColor(4159204)
/* 1508 */         .waterFogColor(329011)
/* 1509 */         .fogColor(3343107)
/* 1510 */         .skyColor(calculateSkyColor(2.0F))
/* 1511 */         .ambientParticle(new AmbientParticleSettings((ParticleOptions)ParticleTypes.CRIMSON_SPORE, 0.025F))
/* 1512 */         .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
/* 1513 */         .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0D))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1519 */         .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111D))
/*      */ 
/*      */ 
/*      */         
/* 1523 */         .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST))
/* 1524 */         .build())
/*      */       
/* 1526 */       .mobSpawnSettings(debug0)
/* 1527 */       .generationSettings(debug1.build())
/* 1528 */       .build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Biome warpedForestBiome() {
/* 1537 */     MobSpawnSettings debug0 = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4)).addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2)).addMobCharge(EntityType.ENDERMAN, 1.0D, 0.12D).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1545 */     BiomeGenerationSettings.Builder debug1 = (new BiomeGenerationSettings.Builder()).surfaceBuilder(SurfaceBuilders.WARPED_FOREST).addStructureStart(StructureFeatures.NETHER_BRIDGE).addStructureStart(StructureFeatures.BASTION_REMNANT).addStructureStart(StructureFeatures.RUINED_PORTAL_NETHER).addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
/*      */     
/* 1547 */     BiomeDefaultFeatures.addDefaultMushrooms(debug1);
/* 1548 */     debug1.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN)
/* 1549 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE)
/* 1550 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE)
/* 1551 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA)
/* 1552 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE)
/* 1553 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA)
/* 1554 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED)
/* 1555 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARPED_FUNGI)
/* 1556 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARPED_FOREST_VEGETATION)
/* 1557 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.NETHER_SPROUTS)
/* 1558 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.TWISTING_VINES);
/*      */     
/* 1560 */     BiomeDefaultFeatures.addNetherDefaultOres(debug1);
/*      */     
/* 1562 */     return (new Biome.BiomeBuilder())
/* 1563 */       .precipitation(Biome.Precipitation.NONE)
/* 1564 */       .biomeCategory(Biome.BiomeCategory.NETHER)
/* 1565 */       .depth(0.1F)
/* 1566 */       .scale(0.2F)
/* 1567 */       .temperature(2.0F)
/* 1568 */       .downfall(0.0F)
/* 1569 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 1570 */         .waterColor(4159204)
/* 1571 */         .waterFogColor(329011)
/* 1572 */         .fogColor(1705242)
/* 1573 */         .skyColor(calculateSkyColor(2.0F))
/* 1574 */         .ambientParticle(new AmbientParticleSettings((ParticleOptions)ParticleTypes.WARPED_SPORE, 0.01428F))
/* 1575 */         .ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
/* 1576 */         .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1582 */         .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))
/*      */ 
/*      */ 
/*      */         
/* 1586 */         .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST))
/* 1587 */         .build())
/*      */       
/* 1589 */       .mobSpawnSettings(debug0)
/* 1590 */       .generationSettings(debug1.build())
/* 1591 */       .build();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\biome\VanillaBiomes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */