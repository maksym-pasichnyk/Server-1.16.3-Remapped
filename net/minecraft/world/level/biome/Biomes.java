/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public abstract class Biomes {
/*  8 */   public static final ResourceKey<Biome> OCEAN = register("ocean");
/*  9 */   public static final ResourceKey<Biome> PLAINS = register("plains");
/* 10 */   public static final ResourceKey<Biome> DESERT = register("desert");
/* 11 */   public static final ResourceKey<Biome> MOUNTAINS = register("mountains");
/* 12 */   public static final ResourceKey<Biome> FOREST = register("forest");
/* 13 */   public static final ResourceKey<Biome> TAIGA = register("taiga");
/* 14 */   public static final ResourceKey<Biome> SWAMP = register("swamp");
/* 15 */   public static final ResourceKey<Biome> RIVER = register("river");
/* 16 */   public static final ResourceKey<Biome> NETHER_WASTES = register("nether_wastes");
/* 17 */   public static final ResourceKey<Biome> THE_END = register("the_end");
/* 18 */   public static final ResourceKey<Biome> FROZEN_OCEAN = register("frozen_ocean");
/* 19 */   public static final ResourceKey<Biome> FROZEN_RIVER = register("frozen_river");
/* 20 */   public static final ResourceKey<Biome> SNOWY_TUNDRA = register("snowy_tundra");
/* 21 */   public static final ResourceKey<Biome> SNOWY_MOUNTAINS = register("snowy_mountains");
/* 22 */   public static final ResourceKey<Biome> MUSHROOM_FIELDS = register("mushroom_fields");
/* 23 */   public static final ResourceKey<Biome> MUSHROOM_FIELD_SHORE = register("mushroom_field_shore");
/* 24 */   public static final ResourceKey<Biome> BEACH = register("beach");
/* 25 */   public static final ResourceKey<Biome> DESERT_HILLS = register("desert_hills");
/* 26 */   public static final ResourceKey<Biome> WOODED_HILLS = register("wooded_hills");
/* 27 */   public static final ResourceKey<Biome> TAIGA_HILLS = register("taiga_hills");
/* 28 */   public static final ResourceKey<Biome> MOUNTAIN_EDGE = register("mountain_edge");
/* 29 */   public static final ResourceKey<Biome> JUNGLE = register("jungle");
/* 30 */   public static final ResourceKey<Biome> JUNGLE_HILLS = register("jungle_hills");
/* 31 */   public static final ResourceKey<Biome> JUNGLE_EDGE = register("jungle_edge");
/* 32 */   public static final ResourceKey<Biome> DEEP_OCEAN = register("deep_ocean");
/* 33 */   public static final ResourceKey<Biome> STONE_SHORE = register("stone_shore");
/* 34 */   public static final ResourceKey<Biome> SNOWY_BEACH = register("snowy_beach");
/* 35 */   public static final ResourceKey<Biome> BIRCH_FOREST = register("birch_forest");
/* 36 */   public static final ResourceKey<Biome> BIRCH_FOREST_HILLS = register("birch_forest_hills");
/* 37 */   public static final ResourceKey<Biome> DARK_FOREST = register("dark_forest");
/* 38 */   public static final ResourceKey<Biome> SNOWY_TAIGA = register("snowy_taiga");
/* 39 */   public static final ResourceKey<Biome> SNOWY_TAIGA_HILLS = register("snowy_taiga_hills");
/* 40 */   public static final ResourceKey<Biome> GIANT_TREE_TAIGA = register("giant_tree_taiga");
/* 41 */   public static final ResourceKey<Biome> GIANT_TREE_TAIGA_HILLS = register("giant_tree_taiga_hills");
/* 42 */   public static final ResourceKey<Biome> WOODED_MOUNTAINS = register("wooded_mountains");
/* 43 */   public static final ResourceKey<Biome> SAVANNA = register("savanna");
/* 44 */   public static final ResourceKey<Biome> SAVANNA_PLATEAU = register("savanna_plateau");
/* 45 */   public static final ResourceKey<Biome> BADLANDS = register("badlands");
/* 46 */   public static final ResourceKey<Biome> WOODED_BADLANDS_PLATEAU = register("wooded_badlands_plateau");
/* 47 */   public static final ResourceKey<Biome> BADLANDS_PLATEAU = register("badlands_plateau");
/* 48 */   public static final ResourceKey<Biome> SMALL_END_ISLANDS = register("small_end_islands");
/* 49 */   public static final ResourceKey<Biome> END_MIDLANDS = register("end_midlands");
/* 50 */   public static final ResourceKey<Biome> END_HIGHLANDS = register("end_highlands");
/* 51 */   public static final ResourceKey<Biome> END_BARRENS = register("end_barrens");
/* 52 */   public static final ResourceKey<Biome> WARM_OCEAN = register("warm_ocean");
/* 53 */   public static final ResourceKey<Biome> LUKEWARM_OCEAN = register("lukewarm_ocean");
/* 54 */   public static final ResourceKey<Biome> COLD_OCEAN = register("cold_ocean");
/* 55 */   public static final ResourceKey<Biome> DEEP_WARM_OCEAN = register("deep_warm_ocean");
/* 56 */   public static final ResourceKey<Biome> DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean");
/* 57 */   public static final ResourceKey<Biome> DEEP_COLD_OCEAN = register("deep_cold_ocean");
/* 58 */   public static final ResourceKey<Biome> DEEP_FROZEN_OCEAN = register("deep_frozen_ocean");
/*    */   
/* 60 */   public static final ResourceKey<Biome> THE_VOID = register("the_void");
/*    */   
/* 62 */   public static final ResourceKey<Biome> SUNFLOWER_PLAINS = register("sunflower_plains");
/* 63 */   public static final ResourceKey<Biome> DESERT_LAKES = register("desert_lakes");
/* 64 */   public static final ResourceKey<Biome> GRAVELLY_MOUNTAINS = register("gravelly_mountains");
/* 65 */   public static final ResourceKey<Biome> FLOWER_FOREST = register("flower_forest");
/* 66 */   public static final ResourceKey<Biome> TAIGA_MOUNTAINS = register("taiga_mountains");
/* 67 */   public static final ResourceKey<Biome> SWAMP_HILLS = register("swamp_hills");
/* 68 */   public static final ResourceKey<Biome> ICE_SPIKES = register("ice_spikes");
/* 69 */   public static final ResourceKey<Biome> MODIFIED_JUNGLE = register("modified_jungle");
/* 70 */   public static final ResourceKey<Biome> MODIFIED_JUNGLE_EDGE = register("modified_jungle_edge");
/*    */   
/* 72 */   public static final ResourceKey<Biome> TALL_BIRCH_FOREST = register("tall_birch_forest");
/* 73 */   public static final ResourceKey<Biome> TALL_BIRCH_HILLS = register("tall_birch_hills");
/* 74 */   public static final ResourceKey<Biome> DARK_FOREST_HILLS = register("dark_forest_hills");
/* 75 */   public static final ResourceKey<Biome> SNOWY_TAIGA_MOUNTAINS = register("snowy_taiga_mountains");
/* 76 */   public static final ResourceKey<Biome> GIANT_SPRUCE_TAIGA = register("giant_spruce_taiga");
/* 77 */   public static final ResourceKey<Biome> GIANT_SPRUCE_TAIGA_HILLS = register("giant_spruce_taiga_hills");
/* 78 */   public static final ResourceKey<Biome> MODIFIED_GRAVELLY_MOUNTAINS = register("modified_gravelly_mountains");
/* 79 */   public static final ResourceKey<Biome> SHATTERED_SAVANNA = register("shattered_savanna");
/* 80 */   public static final ResourceKey<Biome> SHATTERED_SAVANNA_PLATEAU = register("shattered_savanna_plateau");
/* 81 */   public static final ResourceKey<Biome> ERODED_BADLANDS = register("eroded_badlands");
/* 82 */   public static final ResourceKey<Biome> MODIFIED_WOODED_BADLANDS_PLATEAU = register("modified_wooded_badlands_plateau");
/* 83 */   public static final ResourceKey<Biome> MODIFIED_BADLANDS_PLATEAU = register("modified_badlands_plateau");
/* 84 */   public static final ResourceKey<Biome> BAMBOO_JUNGLE = register("bamboo_jungle");
/* 85 */   public static final ResourceKey<Biome> BAMBOO_JUNGLE_HILLS = register("bamboo_jungle_hills");
/*    */   
/* 87 */   public static final ResourceKey<Biome> SOUL_SAND_VALLEY = register("soul_sand_valley");
/* 88 */   public static final ResourceKey<Biome> CRIMSON_FOREST = register("crimson_forest");
/* 89 */   public static final ResourceKey<Biome> WARPED_FOREST = register("warped_forest");
/* 90 */   public static final ResourceKey<Biome> BASALT_DELTAS = register("basalt_deltas");
/*    */   
/*    */   private static ResourceKey<Biome> register(String debug0) {
/* 93 */     return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\Biomes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */