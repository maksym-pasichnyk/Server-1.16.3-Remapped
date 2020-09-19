/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import net.minecraft.data.BuiltinRegistries;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*    */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
/*    */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
/*    */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
/*    */ 
/*    */ public class SurfaceBuilders {
/* 11 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> BADLANDS = register("badlands", SurfaceBuilder.BADLANDS.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_BADLANDS));
/* 12 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> BASALT_DELTAS = register("basalt_deltas", SurfaceBuilder.BASALT_DELTAS.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_BASALT_DELTAS));
/* 13 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CRIMSON_FOREST = register("crimson_forest", SurfaceBuilder.NETHER_FOREST.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_CRIMSON_FOREST));
/* 14 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> DESERT = register("desert", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_DESERT));
/* 15 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> END = register("end", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_THEEND));
/* 16 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ERODED_BADLANDS = register("eroded_badlands", SurfaceBuilder.ERODED_BADLANDS.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_BADLANDS));
/* 17 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> FROZEN_OCEAN = register("frozen_ocean", SurfaceBuilder.FROZEN_OCEAN.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 18 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> FULL_SAND = register("full_sand", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_FULL_SAND));
/* 19 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GIANT_TREE_TAIGA = register("giant_tree_taiga", SurfaceBuilder.GIANT_TREE_TAIGA.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 20 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GRASS = register("grass", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 21 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GRAVELLY_MOUNTAIN = register("gravelly_mountain", SurfaceBuilder.GRAVELLY_MOUNTAIN.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 22 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ICE_SPIKES = register("ice_spikes", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)new SurfaceBuilderBaseConfiguration(Blocks.SNOW_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState())));
/* 23 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> MOUNTAIN = register("mountain", SurfaceBuilder.MOUNTAIN.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 24 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> MYCELIUM = register("mycelium", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_MYCELIUM));
/* 25 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER = register("nether", SurfaceBuilder.NETHER.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_HELL));
/* 26 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> NOPE = register("nope", SurfaceBuilder.NOPE.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_STONE));
/* 27 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> OCEAN_SAND = register("ocean_sand", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_OCEAN_SAND));
/* 28 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SHATTERED_SAVANNA = register("shattered_savanna", SurfaceBuilder.SHATTERED_SAVANNA.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 29 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SOUL_SAND_VALLEY = register("soul_sand_valley", SurfaceBuilder.SOUL_SAND_VALLEY.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_SOUL_SAND_VALLEY));
/* 30 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> STONE = register("stone", SurfaceBuilder.DEFAULT.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_STONE));
/* 31 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SWAMP = register("swamp", SurfaceBuilder.SWAMP.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_GRASS));
/* 32 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> WARPED_FOREST = register("warped_forest", SurfaceBuilder.NETHER_FOREST.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_WARPED_FOREST));
/* 33 */   public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> WOODED_BADLANDS = register("wooded_badlands", SurfaceBuilder.WOODED_BADLANDS.configured((SurfaceBuilderConfiguration)SurfaceBuilder.CONFIG_BADLANDS));
/*    */   
/*    */   private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> register(String debug0, ConfiguredSurfaceBuilder<SC> debug1) {
/* 36 */     return (ConfiguredSurfaceBuilder<SC>)BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\SurfaceBuilders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */