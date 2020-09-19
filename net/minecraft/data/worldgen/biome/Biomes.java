/*     */ package net.minecraft.data.worldgen.biome;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.data.worldgen.SurfaceBuilders;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ public abstract class Biomes {
/*  11 */   private static final Int2ObjectMap<ResourceKey<Biome>> TO_NAME = (Int2ObjectMap<ResourceKey<Biome>>)new Int2ObjectArrayMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  17 */     register(0, net.minecraft.world.level.biome.Biomes.OCEAN, VanillaBiomes.oceanBiome(false));
/*  18 */   } public static final Biome PLAINS = register(1, net.minecraft.world.level.biome.Biomes.PLAINS, VanillaBiomes.plainsBiome(false)); static {
/*  19 */     register(2, net.minecraft.world.level.biome.Biomes.DESERT, VanillaBiomes.desertBiome(0.125F, 0.05F, true, true, true));
/*  20 */     register(3, net.minecraft.world.level.biome.Biomes.MOUNTAINS, VanillaBiomes.mountainBiome(1.0F, 0.5F, SurfaceBuilders.MOUNTAIN, false));
/*  21 */     register(4, net.minecraft.world.level.biome.Biomes.FOREST, VanillaBiomes.forestBiome(0.1F, 0.2F));
/*  22 */     register(5, net.minecraft.world.level.biome.Biomes.TAIGA, VanillaBiomes.taigaBiome(0.2F, 0.2F, false, false, true, false));
/*  23 */     register(6, net.minecraft.world.level.biome.Biomes.SWAMP, VanillaBiomes.swampBiome(-0.2F, 0.1F, false));
/*  24 */     register(7, net.minecraft.world.level.biome.Biomes.RIVER, VanillaBiomes.riverBiome(-0.5F, 0.0F, 0.5F, 4159204, false));
/*  25 */     register(8, net.minecraft.world.level.biome.Biomes.NETHER_WASTES, VanillaBiomes.netherWastesBiome());
/*  26 */     register(9, net.minecraft.world.level.biome.Biomes.THE_END, VanillaBiomes.theEndBiome());
/*  27 */     register(10, net.minecraft.world.level.biome.Biomes.FROZEN_OCEAN, VanillaBiomes.frozenOceanBiome(false));
/*  28 */     register(11, net.minecraft.world.level.biome.Biomes.FROZEN_RIVER, VanillaBiomes.riverBiome(-0.5F, 0.0F, 0.0F, 3750089, true));
/*  29 */     register(12, net.minecraft.world.level.biome.Biomes.SNOWY_TUNDRA, VanillaBiomes.tundraBiome(0.125F, 0.05F, false, false));
/*  30 */     register(13, net.minecraft.world.level.biome.Biomes.SNOWY_MOUNTAINS, VanillaBiomes.tundraBiome(0.45F, 0.3F, false, true));
/*  31 */     register(14, net.minecraft.world.level.biome.Biomes.MUSHROOM_FIELDS, VanillaBiomes.mushroomFieldsBiome(0.2F, 0.3F));
/*  32 */     register(15, net.minecraft.world.level.biome.Biomes.MUSHROOM_FIELD_SHORE, VanillaBiomes.mushroomFieldsBiome(0.0F, 0.025F));
/*  33 */     register(16, net.minecraft.world.level.biome.Biomes.BEACH, VanillaBiomes.beachBiome(0.0F, 0.025F, 0.8F, 0.4F, 4159204, false, false));
/*  34 */     register(17, net.minecraft.world.level.biome.Biomes.DESERT_HILLS, VanillaBiomes.desertBiome(0.45F, 0.3F, false, true, false));
/*  35 */     register(18, net.minecraft.world.level.biome.Biomes.WOODED_HILLS, VanillaBiomes.forestBiome(0.45F, 0.3F));
/*  36 */     register(19, net.minecraft.world.level.biome.Biomes.TAIGA_HILLS, VanillaBiomes.taigaBiome(0.45F, 0.3F, false, false, false, false));
/*  37 */     register(20, net.minecraft.world.level.biome.Biomes.MOUNTAIN_EDGE, VanillaBiomes.mountainBiome(0.8F, 0.3F, SurfaceBuilders.GRASS, true));
/*  38 */     register(21, net.minecraft.world.level.biome.Biomes.JUNGLE, VanillaBiomes.jungleBiome());
/*  39 */     register(22, net.minecraft.world.level.biome.Biomes.JUNGLE_HILLS, VanillaBiomes.jungleHillsBiome());
/*  40 */     register(23, net.minecraft.world.level.biome.Biomes.JUNGLE_EDGE, VanillaBiomes.jungleEdgeBiome());
/*  41 */     register(24, net.minecraft.world.level.biome.Biomes.DEEP_OCEAN, VanillaBiomes.oceanBiome(true));
/*  42 */     register(25, net.minecraft.world.level.biome.Biomes.STONE_SHORE, VanillaBiomes.beachBiome(0.1F, 0.8F, 0.2F, 0.3F, 4159204, false, true));
/*  43 */     register(26, net.minecraft.world.level.biome.Biomes.SNOWY_BEACH, VanillaBiomes.beachBiome(0.0F, 0.025F, 0.05F, 0.3F, 4020182, true, false));
/*  44 */     register(27, net.minecraft.world.level.biome.Biomes.BIRCH_FOREST, VanillaBiomes.birchForestBiome(0.1F, 0.2F, false));
/*  45 */     register(28, net.minecraft.world.level.biome.Biomes.BIRCH_FOREST_HILLS, VanillaBiomes.birchForestBiome(0.45F, 0.3F, false));
/*  46 */     register(29, net.minecraft.world.level.biome.Biomes.DARK_FOREST, VanillaBiomes.darkForestBiome(0.1F, 0.2F, false));
/*  47 */     register(30, net.minecraft.world.level.biome.Biomes.SNOWY_TAIGA, VanillaBiomes.taigaBiome(0.2F, 0.2F, true, false, false, true));
/*  48 */     register(31, net.minecraft.world.level.biome.Biomes.SNOWY_TAIGA_HILLS, VanillaBiomes.taigaBiome(0.45F, 0.3F, true, false, false, false));
/*  49 */     register(32, net.minecraft.world.level.biome.Biomes.GIANT_TREE_TAIGA, VanillaBiomes.giantTreeTaiga(0.2F, 0.2F, 0.3F, false));
/*  50 */     register(33, net.minecraft.world.level.biome.Biomes.GIANT_TREE_TAIGA_HILLS, VanillaBiomes.giantTreeTaiga(0.45F, 0.3F, 0.3F, false));
/*  51 */     register(34, net.minecraft.world.level.biome.Biomes.WOODED_MOUNTAINS, VanillaBiomes.mountainBiome(1.0F, 0.5F, SurfaceBuilders.GRASS, true));
/*  52 */     register(35, net.minecraft.world.level.biome.Biomes.SAVANNA, VanillaBiomes.savannaBiome(0.125F, 0.05F, 1.2F, false, false));
/*  53 */     register(36, net.minecraft.world.level.biome.Biomes.SAVANNA_PLATEAU, VanillaBiomes.savanaPlateauBiome());
/*  54 */     register(37, net.minecraft.world.level.biome.Biomes.BADLANDS, VanillaBiomes.badlandsBiome(0.1F, 0.2F, false));
/*  55 */     register(38, net.minecraft.world.level.biome.Biomes.WOODED_BADLANDS_PLATEAU, VanillaBiomes.woodedBadlandsPlateauBiome(1.5F, 0.025F));
/*  56 */     register(39, net.minecraft.world.level.biome.Biomes.BADLANDS_PLATEAU, VanillaBiomes.badlandsBiome(1.5F, 0.025F, true));
/*  57 */     register(40, net.minecraft.world.level.biome.Biomes.SMALL_END_ISLANDS, VanillaBiomes.smallEndIslandsBiome());
/*  58 */     register(41, net.minecraft.world.level.biome.Biomes.END_MIDLANDS, VanillaBiomes.endMidlandsBiome());
/*  59 */     register(42, net.minecraft.world.level.biome.Biomes.END_HIGHLANDS, VanillaBiomes.endHighlandsBiome());
/*  60 */     register(43, net.minecraft.world.level.biome.Biomes.END_BARRENS, VanillaBiomes.endBarrensBiome());
/*  61 */     register(44, net.minecraft.world.level.biome.Biomes.WARM_OCEAN, VanillaBiomes.warmOceanBiome());
/*  62 */     register(45, net.minecraft.world.level.biome.Biomes.LUKEWARM_OCEAN, VanillaBiomes.lukeWarmOceanBiome(false));
/*  63 */     register(46, net.minecraft.world.level.biome.Biomes.COLD_OCEAN, VanillaBiomes.coldOceanBiome(false));
/*  64 */     register(47, net.minecraft.world.level.biome.Biomes.DEEP_WARM_OCEAN, VanillaBiomes.deepWarmOceanBiome());
/*  65 */     register(48, net.minecraft.world.level.biome.Biomes.DEEP_LUKEWARM_OCEAN, VanillaBiomes.lukeWarmOceanBiome(true));
/*  66 */     register(49, net.minecraft.world.level.biome.Biomes.DEEP_COLD_OCEAN, VanillaBiomes.coldOceanBiome(true));
/*  67 */     register(50, net.minecraft.world.level.biome.Biomes.DEEP_FROZEN_OCEAN, VanillaBiomes.frozenOceanBiome(true));
/*     */   }
/*  69 */   public static final Biome THE_VOID = register(127, net.minecraft.world.level.biome.Biomes.THE_VOID, VanillaBiomes.theVoidBiome());
/*     */   static {
/*  71 */     register(129, net.minecraft.world.level.biome.Biomes.SUNFLOWER_PLAINS, VanillaBiomes.plainsBiome(true));
/*  72 */     register(130, net.minecraft.world.level.biome.Biomes.DESERT_LAKES, VanillaBiomes.desertBiome(0.225F, 0.25F, false, false, false));
/*  73 */     register(131, net.minecraft.world.level.biome.Biomes.GRAVELLY_MOUNTAINS, VanillaBiomes.mountainBiome(1.0F, 0.5F, SurfaceBuilders.GRAVELLY_MOUNTAIN, false));
/*  74 */     register(132, net.minecraft.world.level.biome.Biomes.FLOWER_FOREST, VanillaBiomes.flowerForestBiome());
/*  75 */     register(133, net.minecraft.world.level.biome.Biomes.TAIGA_MOUNTAINS, VanillaBiomes.taigaBiome(0.3F, 0.4F, false, true, false, false));
/*  76 */     register(134, net.minecraft.world.level.biome.Biomes.SWAMP_HILLS, VanillaBiomes.swampBiome(-0.1F, 0.3F, true));
/*  77 */     register(140, net.minecraft.world.level.biome.Biomes.ICE_SPIKES, VanillaBiomes.tundraBiome(0.425F, 0.45000002F, true, false));
/*  78 */     register(149, net.minecraft.world.level.biome.Biomes.MODIFIED_JUNGLE, VanillaBiomes.modifiedJungleBiome());
/*  79 */     register(151, net.minecraft.world.level.biome.Biomes.MODIFIED_JUNGLE_EDGE, VanillaBiomes.modifiedJungleEdgeBiome());
/*     */     
/*  81 */     register(155, net.minecraft.world.level.biome.Biomes.TALL_BIRCH_FOREST, VanillaBiomes.birchForestBiome(0.2F, 0.4F, true));
/*  82 */     register(156, net.minecraft.world.level.biome.Biomes.TALL_BIRCH_HILLS, VanillaBiomes.birchForestBiome(0.55F, 0.5F, true));
/*  83 */     register(157, net.minecraft.world.level.biome.Biomes.DARK_FOREST_HILLS, VanillaBiomes.darkForestBiome(0.2F, 0.4F, true));
/*  84 */     register(158, net.minecraft.world.level.biome.Biomes.SNOWY_TAIGA_MOUNTAINS, VanillaBiomes.taigaBiome(0.3F, 0.4F, true, true, false, false));
/*  85 */     register(160, net.minecraft.world.level.biome.Biomes.GIANT_SPRUCE_TAIGA, VanillaBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F, true));
/*  86 */     register(161, net.minecraft.world.level.biome.Biomes.GIANT_SPRUCE_TAIGA_HILLS, VanillaBiomes.giantTreeTaiga(0.2F, 0.2F, 0.25F, true));
/*  87 */     register(162, net.minecraft.world.level.biome.Biomes.MODIFIED_GRAVELLY_MOUNTAINS, VanillaBiomes.mountainBiome(1.0F, 0.5F, SurfaceBuilders.GRAVELLY_MOUNTAIN, false));
/*  88 */     register(163, net.minecraft.world.level.biome.Biomes.SHATTERED_SAVANNA, VanillaBiomes.savannaBiome(0.3625F, 1.225F, 1.1F, true, true));
/*  89 */     register(164, net.minecraft.world.level.biome.Biomes.SHATTERED_SAVANNA_PLATEAU, VanillaBiomes.savannaBiome(1.05F, 1.2125001F, 1.0F, true, true));
/*  90 */     register(165, net.minecraft.world.level.biome.Biomes.ERODED_BADLANDS, VanillaBiomes.erodedBadlandsBiome());
/*  91 */     register(166, net.minecraft.world.level.biome.Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, VanillaBiomes.woodedBadlandsPlateauBiome(0.45F, 0.3F));
/*  92 */     register(167, net.minecraft.world.level.biome.Biomes.MODIFIED_BADLANDS_PLATEAU, VanillaBiomes.badlandsBiome(0.45F, 0.3F, true));
/*  93 */     register(168, net.minecraft.world.level.biome.Biomes.BAMBOO_JUNGLE, VanillaBiomes.bambooJungleBiome());
/*  94 */     register(169, net.minecraft.world.level.biome.Biomes.BAMBOO_JUNGLE_HILLS, VanillaBiomes.bambooJungleHillsBiome());
/*     */     
/*  96 */     register(170, net.minecraft.world.level.biome.Biomes.SOUL_SAND_VALLEY, VanillaBiomes.soulSandValleyBiome());
/*  97 */     register(171, net.minecraft.world.level.biome.Biomes.CRIMSON_FOREST, VanillaBiomes.crimsonForestBiome());
/*  98 */     register(172, net.minecraft.world.level.biome.Biomes.WARPED_FOREST, VanillaBiomes.warpedForestBiome());
/*  99 */     register(173, net.minecraft.world.level.biome.Biomes.BASALT_DELTAS, VanillaBiomes.basaltDeltasBiome());
/*     */   }
/*     */   
/*     */   private static Biome register(int debug0, ResourceKey<Biome> debug1, Biome debug2) {
/* 103 */     TO_NAME.put(debug0, debug1);
/* 104 */     return (Biome)BuiltinRegistries.registerMapping(BuiltinRegistries.BIOME, debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static ResourceKey<Biome> byId(int debug0) {
/* 108 */     return (ResourceKey<Biome>)TO_NAME.get(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\biome\Biomes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */