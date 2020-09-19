/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.util.UniformInt;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*     */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.ColumnPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.ForestFlowerProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.PlainFlowerProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.RotatedBlockProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ import net.minecraft.world.level.levelgen.placement.CarvingMaskDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
/*     */ import net.minecraft.world.level.levelgen.placement.DepthAverageConfigation;
/*     */ import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
/*     */ import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.NoiseCountFactorDecoratorConfiguration;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class Features {
/*  92 */   public static final ConfiguredFeature<?, ?> END_SPIKE = register("end_spike", Feature.END_SPIKE.configured((FeatureConfiguration)new SpikeConfiguration(false, (List)ImmutableList.of(), null)));
/*  93 */   public static final ConfiguredFeature<?, ?> END_GATEWAY = register("end_gateway", Feature.END_GATEWAY.configured((FeatureConfiguration)EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true)).decorated(FeatureDecorator.END_GATEWAY.configured((DecoratorConfiguration)DecoratorConfiguration.NONE)));
/*  94 */   public static final ConfiguredFeature<?, ?> END_GATEWAY_DELAYED = register("end_gateway_delayed", Feature.END_GATEWAY.configured((FeatureConfiguration)EndGatewayConfiguration.delayedExitSearch()));
/*  95 */   public static final ConfiguredFeature<?, ?> CHORUS_PLANT = register("chorus_plant", (ConfiguredFeature<?, ?>)Feature.CHORUS_PLANT.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(Decorators.HEIGHTMAP_SQUARE).countRandom(4));
/*  96 */   public static final ConfiguredFeature<?, ?> END_ISLAND = register("end_island", Feature.END_ISLAND.configured((FeatureConfiguration)FeatureConfiguration.NONE));
/*  97 */   public static final ConfiguredFeature<?, ?> END_ISLAND_DECORATED = register("end_island_decorated", END_ISLAND.decorated(FeatureDecorator.END_ISLAND.configured((DecoratorConfiguration)DecoratorConfiguration.NONE)));
/*     */ 
/*     */   
/* 100 */   public static final ConfiguredFeature<?, ?> DELTA = register("delta", Feature.DELTA_FEATURE.configured((FeatureConfiguration)new DeltaFeatureConfiguration(States.LAVA, States.MAGMA_BLOCK, UniformInt.of(3, 4), UniformInt.of(0, 2))).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(40))));
/* 101 */   public static final ConfiguredFeature<?, ?> SMALL_BASALT_COLUMNS = register("small_basalt_columns", Feature.BASALT_COLUMNS.configured((FeatureConfiguration)new ColumnFeatureConfiguration(UniformInt.fixed(1), UniformInt.of(1, 3))).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(4))));
/* 102 */   public static final ConfiguredFeature<?, ?> LARGE_BASALT_COLUMNS = register("large_basalt_columns", Feature.BASALT_COLUMNS.configured((FeatureConfiguration)new ColumnFeatureConfiguration(UniformInt.of(2, 1), UniformInt.of(5, 5))).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(2))));
/* 103 */   public static final ConfiguredFeature<?, ?> BASALT_BLOBS = register("basalt_blobs", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.REPLACE_BLOBS.configured((FeatureConfiguration)new ReplaceSphereConfiguration(States.NETHERRACK, States.BASALT, UniformInt.of(3, 4))).range(128)).squared()).count(75));
/* 104 */   public static final ConfiguredFeature<?, ?> BLACKSTONE_BLOBS = register("blackstone_blobs", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.REPLACE_BLOBS.configured((FeatureConfiguration)new ReplaceSphereConfiguration(States.NETHERRACK, States.BLACKSTONE, UniformInt.of(3, 4))).range(128)).squared()).count(25));
/* 105 */   public static final ConfiguredFeature<?, ?> GLOWSTONE_EXTRA = register("glowstone_extra", Feature.GLOWSTONE_BLOB.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(FeatureDecorator.GLOWSTONE.configured((DecoratorConfiguration)new CountConfiguration(10))));
/* 106 */   public static final ConfiguredFeature<?, ?> GLOWSTONE = register("glowstone", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.GLOWSTONE_BLOB.configured((FeatureConfiguration)FeatureConfiguration.NONE).range(128)).squared()).count(10));
/*     */   
/* 108 */   public static final ConfiguredFeature<?, ?> CRIMSON_FOREST_VEGETATION = register("crimson_forest_vegetation", Feature.NETHER_FOREST_VEGETATION.configured((FeatureConfiguration)Configs.CRIMSON_FOREST_CONFIG).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(6))));
/* 109 */   public static final ConfiguredFeature<?, ?> WARPED_FOREST_VEGETATION = register("warped_forest_vegetation", Feature.NETHER_FOREST_VEGETATION.configured((FeatureConfiguration)Configs.WARPED_FOREST_CONFIG).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(5))));
/* 110 */   public static final ConfiguredFeature<?, ?> NETHER_SPROUTS = register("nether_sprouts", Feature.NETHER_FOREST_VEGETATION.configured((FeatureConfiguration)Configs.NETHER_SPROUTS_CONFIG).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(4))));
/*     */   
/* 112 */   public static final ConfiguredFeature<?, ?> TWISTING_VINES = register("twisting_vines", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.TWISTING_VINES.configured((FeatureConfiguration)FeatureConfiguration.NONE).range(128)).squared()).count(10));
/* 113 */   public static final ConfiguredFeature<?, ?> WEEPING_VINES = register("weeping_vines", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.WEEPING_VINES.configured((FeatureConfiguration)FeatureConfiguration.NONE).range(128)).squared()).count(10));
/* 114 */   public static final ConfiguredFeature<?, ?> BASALT_PILLAR = register("basalt_pillar", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.BASALT_PILLAR.configured((FeatureConfiguration)FeatureConfiguration.NONE).range(128)).squared()).count(10));
/*     */ 
/*     */   
/* 117 */   public static final ConfiguredFeature<?, ?> SEAGRASS_COLD = register("seagrass_cold", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.3F)).count(32)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 118 */   public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP_COLD = register("seagrass_deep_cold", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.8F)).count(40)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 119 */   public static final ConfiguredFeature<?, ?> SEAGRASS_NORMAL = register("seagrass_normal", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.3F)).count(48)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 120 */   public static final ConfiguredFeature<?, ?> SEAGRASS_RIVER = register("seagrass_river", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.4F)).count(48)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 121 */   public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP = register("seagrass_deep", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.8F)).count(48)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 122 */   public static final ConfiguredFeature<?, ?> SEAGRASS_SWAMP = register("seagrass_swamp", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.6F)).count(64)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 123 */   public static final ConfiguredFeature<?, ?> SEAGRASS_WARM = register("seagrass_warm", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.3F)).count(80)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 124 */   public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP_WARM = register("seagrass_deep_warm", ((ConfiguredFeature)Feature.SEAGRASS.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.8F)).count(80)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/*     */   
/* 126 */   public static final ConfiguredFeature<?, ?> SEA_PICKLE = register("sea_pickle", (ConfiguredFeature<?, ?>)Feature.SEA_PICKLE.configured((FeatureConfiguration)new CountConfiguration(20)).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).chance(16));
/* 127 */   public static final ConfiguredFeature<?, ?> ICE_SPIKE = register("ice_spike", (ConfiguredFeature<?, ?>)Feature.ICE_SPIKE.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(Decorators.HEIGHTMAP_SQUARE).count(3));
/* 128 */   public static final ConfiguredFeature<?, ?> ICE_PATCH = register("ice_patch", (ConfiguredFeature<?, ?>)Feature.ICE_PATCH.configured((FeatureConfiguration)new DiskConfiguration(States.PACKED_ICE, UniformInt.of(2, 1), 1, (List)ImmutableList.of(States.DIRT, States.GRASS_BLOCK, States.PODZOL, States.COARSE_DIRT, States.MYCELIUM, States.SNOW_BLOCK, States.ICE))).decorated(Decorators.HEIGHTMAP_SQUARE).count(2));
/* 129 */   public static final ConfiguredFeature<?, ?> FOREST_ROCK = register("forest_rock", (ConfiguredFeature<?, ?>)Feature.FOREST_ROCK.configured((FeatureConfiguration)new BlockStateConfiguration(States.MOSSY_COBBLESTONE)).decorated(Decorators.HEIGHTMAP_SQUARE).countRandom(2));
/* 130 */   public static final ConfiguredFeature<?, ?> SEAGRASS_SIMPLE = register("seagrass_simple", Feature.SIMPLE_BLOCK.configured((FeatureConfiguration)new SimpleBlockConfiguration(States.SEAGRASS, (List)ImmutableList.of(States.STONE), (List)ImmutableList.of(States.WATER), (List)ImmutableList.of(States.WATER))).decorated(FeatureDecorator.CARVING_MASK.configured((DecoratorConfiguration)new CarvingMaskDecoratorConfiguration(GenerationStep.Carving.LIQUID, 0.1F))));
/* 131 */   public static final ConfiguredFeature<?, ?> ICEBERG_PACKED = register("iceberg_packed", (ConfiguredFeature<?, ?>)Feature.ICEBERG.configured((FeatureConfiguration)new BlockStateConfiguration(States.PACKED_ICE)).decorated(FeatureDecorator.ICEBERG.configured((DecoratorConfiguration)NoneDecoratorConfiguration.INSTANCE)).chance(16));
/* 132 */   public static final ConfiguredFeature<?, ?> ICEBERG_BLUE = register("iceberg_blue", (ConfiguredFeature<?, ?>)Feature.ICEBERG.configured((FeatureConfiguration)new BlockStateConfiguration(States.BLUE_ICE)).decorated(FeatureDecorator.ICEBERG.configured((DecoratorConfiguration)NoneDecoratorConfiguration.INSTANCE)).chance(200));
/*     */   
/* 134 */   public static final ConfiguredFeature<?, ?> KELP_COLD = register("kelp_cold", ((ConfiguredFeature)Feature.KELP.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(Decorators.TOP_SOLID_HEIGHTMAP).squared()).decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured((DecoratorConfiguration)new NoiseCountFactorDecoratorConfiguration(120, 80.0D, 0.0D))));
/* 135 */   public static final ConfiguredFeature<?, ?> KELP_WARM = register("kelp_warm", ((ConfiguredFeature)Feature.KELP.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(Decorators.TOP_SOLID_HEIGHTMAP).squared()).decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured((DecoratorConfiguration)new NoiseCountFactorDecoratorConfiguration(80, 80.0D, 0.0D))));
/* 136 */   public static final ConfiguredFeature<?, ?> BLUE_ICE = register("blue_ice", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.BLUE_ICE.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(30, 32, 64))).squared()).countRandom(19));
/*     */ 
/*     */   
/* 139 */   public static final ConfiguredFeature<?, ?> BAMBOO_LIGHT = register("bamboo_light", (ConfiguredFeature<?, ?>)Feature.BAMBOO.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.0F)).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(16));
/* 140 */   public static final ConfiguredFeature<?, ?> BAMBOO = register("bamboo", ((ConfiguredFeature)Feature.BAMBOO.configured((FeatureConfiguration)new ProbabilityFeatureConfiguration(0.2F)).decorated(Decorators.HEIGHTMAP_WORLD_SURFACE).squared()).decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured((DecoratorConfiguration)new NoiseCountFactorDecoratorConfiguration(160, 80.0D, 0.3D))));
/* 141 */   public static final ConfiguredFeature<?, ?> VINES = register("vines", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.VINES.configured((FeatureConfiguration)FeatureConfiguration.NONE).squared()).count(50));
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static final ConfiguredFeature<?, ?> LAKE_WATER = register("lake_water", Feature.LAKE.configured((FeatureConfiguration)new BlockStateConfiguration(States.WATER)).decorated(FeatureDecorator.WATER_LAKE.configured((DecoratorConfiguration)new ChanceDecoratorConfiguration(4))));
/* 146 */   public static final ConfiguredFeature<?, ?> LAKE_LAVA = register("lake_lava", Feature.LAKE.configured((FeatureConfiguration)new BlockStateConfiguration(States.LAVA)).decorated(FeatureDecorator.LAVA_LAKE.configured((DecoratorConfiguration)new ChanceDecoratorConfiguration(80))));
/*     */   
/* 148 */   public static final ConfiguredFeature<?, ?> DISK_CLAY = register("disk_clay", Feature.DISK.configured((FeatureConfiguration)new DiskConfiguration(States.CLAY, UniformInt.of(2, 1), 1, (List)ImmutableList.of(States.DIRT, States.CLAY))).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 149 */   public static final ConfiguredFeature<?, ?> DISK_GRAVEL = register("disk_gravel", Feature.DISK.configured((FeatureConfiguration)new DiskConfiguration(States.GRAVEL, UniformInt.of(2, 3), 2, (List)ImmutableList.of(States.DIRT, States.GRASS_BLOCK))).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE));
/* 150 */   public static final ConfiguredFeature<?, ?> DISK_SAND = register("disk_sand", (ConfiguredFeature<?, ?>)Feature.DISK.configured((FeatureConfiguration)new DiskConfiguration(States.SAND, UniformInt.of(2, 4), 2, (List)ImmutableList.of(States.DIRT, States.GRASS_BLOCK))).decorated(Decorators.TOP_SOLID_HEIGHTMAP_SQUARE).count(3));
/*     */   
/* 152 */   public static final ConfiguredFeature<?, ?> FREEZE_TOP_LAYER = register("freeze_top_layer", Feature.FREEZE_TOP_LAYER.configured((FeatureConfiguration)FeatureConfiguration.NONE));
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static final ConfiguredFeature<?, ?> BONUS_CHEST = register("bonus_chest", Feature.BONUS_CHEST.configured((FeatureConfiguration)FeatureConfiguration.NONE));
/* 157 */   public static final ConfiguredFeature<?, ?> VOID_START_PLATFORM = register("void_start_platform", Feature.VOID_START_PLATFORM.configured((FeatureConfiguration)FeatureConfiguration.NONE));
/* 158 */   public static final ConfiguredFeature<?, ?> MONSTER_ROOM = register("monster_room", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.MONSTER_ROOM.configured((FeatureConfiguration)FeatureConfiguration.NONE).range(256)).squared()).count(8));
/* 159 */   public static final ConfiguredFeature<?, ?> WELL = register("desert_well", (ConfiguredFeature<?, ?>)Feature.DESERT_WELL.configured((FeatureConfiguration)FeatureConfiguration.NONE).decorated(Decorators.HEIGHTMAP_SQUARE).chance(1000));
/* 160 */   public static final ConfiguredFeature<?, ?> FOSSIL = register("fossil", (ConfiguredFeature<?, ?>)Feature.FOSSIL.configured((FeatureConfiguration)FeatureConfiguration.NONE).chance(64));
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static final ConfiguredFeature<?, ?> SPRING_LAVA_DOUBLE = register("spring_lava_double", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)Configs.LAVA_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_VERY_BIASED.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(8, 16, 256))).squared()).count(40));
/* 165 */   public static final ConfiguredFeature<?, ?> SPRING_LAVA = register("spring_lava", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)Configs.LAVA_SPRING_CONFIG).decorated(FeatureDecorator.RANGE_VERY_BIASED.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(8, 16, 256))).squared()).count(20));
/* 166 */   public static final ConfiguredFeature<?, ?> SPRING_DELTA = register("spring_delta", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)new SpringConfiguration(States.LAVA_STATE, true, 4, 1, (Set)ImmutableSet.of(Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.GRAVEL, Blocks.MAGMA_BLOCK, Blocks.BLACKSTONE))).decorated(Decorators.RANGE_4_8_ROOFED).squared()).count(16));
/* 167 */   public static final ConfiguredFeature<?, ?> SPRING_CLOSED = register("spring_closed", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)Configs.CLOSED_NETHER_SPRING_CONFIG).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(16));
/* 168 */   public static final ConfiguredFeature<?, ?> SPRING_CLOSED_DOUBLE = register("spring_closed_double", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)Configs.CLOSED_NETHER_SPRING_CONFIG).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(32));
/* 169 */   public static final ConfiguredFeature<?, ?> SPRING_OPEN = register("spring_open", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)new SpringConfiguration(States.LAVA_STATE, false, 4, 1, (Set)ImmutableSet.of(Blocks.NETHERRACK))).decorated(Decorators.RANGE_4_8_ROOFED).squared()).count(8));
/* 170 */   public static final ConfiguredFeature<?, ?> SPRING_WATER = register("spring_water", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SPRING.configured((FeatureConfiguration)new SpringConfiguration(States.WATER_STATE, true, 4, 1, (Set)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE))).decorated(FeatureDecorator.RANGE_BIASED.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(8, 8, 256))).squared()).count(50));
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final ConfiguredFeature<?, ?> PILE_HAY = register("pile_hay", Feature.BLOCK_PILE.configured((FeatureConfiguration)new BlockPileConfiguration((BlockStateProvider)new RotatedBlockProvider(Blocks.HAY_BLOCK))));
/* 175 */   public static final ConfiguredFeature<?, ?> PILE_MELON = register("pile_melon", Feature.BLOCK_PILE.configured((FeatureConfiguration)new BlockPileConfiguration((BlockStateProvider)new SimpleStateProvider(States.MELON))));
/* 176 */   public static final ConfiguredFeature<?, ?> PILE_SNOW = register("pile_snow", Feature.BLOCK_PILE.configured((FeatureConfiguration)new BlockPileConfiguration((BlockStateProvider)new SimpleStateProvider(States.SNOW))));
/* 177 */   public static final ConfiguredFeature<?, ?> PILE_ICE = register("pile_ice", Feature.BLOCK_PILE.configured((FeatureConfiguration)new BlockPileConfiguration((BlockStateProvider)(new WeightedStateProvider()).add(States.BLUE_ICE, 1).add(States.PACKED_ICE, 5))));
/* 178 */   public static final ConfiguredFeature<?, ?> PILE_PUMPKIN = register("pile_pumpkin", Feature.BLOCK_PILE.configured((FeatureConfiguration)new BlockPileConfiguration((BlockStateProvider)(new WeightedStateProvider()).add(States.PUMPKIN, 19).add(States.JACK_O_LANTERN, 1))));
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static final ConfiguredFeature<?, ?> PATCH_FIRE = register("patch_fire", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.FIRE), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 186 */         .tries(64)
/* 187 */         .whitelist((Set)ImmutableSet.of(States.NETHERRACK.getBlock()))
/* 188 */         .noProjection()
/* 189 */         .build()).decorated(Decorators.FIRE));
/* 190 */   public static final ConfiguredFeature<?, ?> PATCH_SOUL_FIRE = register("patch_soul_fire", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SOUL_FIRE), (BlockPlacer)new SimpleBlockPlacer()))
/*     */ 
/*     */ 
/*     */         
/* 194 */         .tries(64)
/* 195 */         .whitelist((Set)ImmutableSet.of(States.SOUL_SOIL.getBlock()))
/* 196 */         .noProjection()
/* 197 */         .build()).decorated(Decorators.FIRE));
/* 198 */   public static final ConfiguredFeature<?, ?> PATCH_BROWN_MUSHROOM = register("patch_brown_mushroom", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.BROWN_MUSHROOM), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 202 */         .tries(64)
/* 203 */         .noProjection()
/* 204 */         .build()));
/* 205 */   public static final ConfiguredFeature<?, ?> PATCH_RED_MUSHROOM = register("patch_red_mushroom", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.RED_MUSHROOM), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 209 */         .tries(64)
/* 210 */         .noProjection()
/* 211 */         .build()));
/* 212 */   public static final ConfiguredFeature<?, ?> PATCH_CRIMSON_ROOTS = register("patch_crimson_roots", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.CRIMSON_ROOTS), (BlockPlacer)new SimpleBlockPlacer()))
/*     */ 
/*     */ 
/*     */         
/* 216 */         .tries(64)
/* 217 */         .noProjection()
/* 218 */         .build()).range(128));
/* 219 */   public static final ConfiguredFeature<?, ?> PATCH_SUNFLOWER = register("patch_sunflower", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SUNFLOWER), (BlockPlacer)new DoublePlantPlacer()))
/*     */ 
/*     */ 
/*     */         
/* 223 */         .tries(64)
/* 224 */         .noProjection()
/* 225 */         .build()).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(10));
/* 226 */   public static final ConfiguredFeature<?, ?> PATCH_PUMPKIN = register("patch_pumpkin", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.PUMPKIN), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 230 */         .tries(64)
/* 231 */         .whitelist((Set)ImmutableSet.of(States.GRASS_BLOCK.getBlock()))
/* 232 */         .noProjection()
/* 233 */         .build()).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).chance(32));
/* 234 */   public static final ConfiguredFeature<?, ?> PATCH_TAIGA_GRASS = register("patch_taiga_grass", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.TAIGA_GRASS_CONFIG));
/* 235 */   public static final ConfiguredFeature<?, ?> PATCH_BERRY_BUSH = register("patch_berry_bush", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.SWEET_BERRY_BUSH_CONFIG));
/* 236 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_PLAIN = register("patch_grass_plain", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEFAULT_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).decorated(FeatureDecorator.COUNT_NOISE.configured((DecoratorConfiguration)new NoiseDependantDecoratorConfiguration(-0.8D, 5, 10))));
/* 237 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_FOREST = register("patch_grass_forest", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEFAULT_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(2));
/* 238 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_BADLANDS = register("patch_grass_badlands", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEFAULT_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 239 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_SAVANNA = register("patch_grass_savanna", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEFAULT_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(20));
/* 240 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_NORMAL = register("patch_grass_normal", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEFAULT_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(5));
/* 241 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_TAIGA_2 = register("patch_grass_taiga_2", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.TAIGA_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 242 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_TAIGA = register("patch_grass_taiga", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.TAIGA_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(7));
/* 243 */   public static final ConfiguredFeature<?, ?> PATCH_GRASS_JUNGLE = register("patch_grass_jungle", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.JUNGLE_GRASS_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(25));
/* 244 */   public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH_2 = register("patch_dead_bush_2", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEAD_BUSH_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(2));
/* 245 */   public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH = register("patch_dead_bush", Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEAD_BUSH_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 246 */   public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH_BADLANDS = register("patch_dead_bush_badlands", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.DEAD_BUSH_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(20));
/* 247 */   public static final ConfiguredFeature<?, ?> PATCH_MELON = register("patch_melon", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.MELON), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 251 */         .tries(64)
/* 252 */         .whitelist((Set)ImmutableSet.of(States.GRASS_BLOCK.getBlock()))
/* 253 */         .canReplace()
/* 254 */         .noProjection()
/* 255 */         .build()).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 256 */   public static final ConfiguredFeature<?, ?> PATCH_BERRY_SPARSE = register("patch_berry_sparse", PATCH_BERRY_BUSH.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 257 */   public static final ConfiguredFeature<?, ?> PATCH_BERRY_DECORATED = register("patch_berry_decorated", (ConfiguredFeature<?, ?>)PATCH_BERRY_BUSH.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).chance(12));
/* 258 */   public static final ConfiguredFeature<?, ?> PATCH_WATERLILLY = register("patch_waterlilly", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.LILY_PAD), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 262 */         .tries(10)
/* 263 */         .build()).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(4));
/* 264 */   public static final ConfiguredFeature<?, ?> PATCH_TALL_GRASS_2 = register("patch_tall_grass_2", ((ConfiguredFeature)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.TALL_GRASS_CONFIG).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP).squared()).decorated(FeatureDecorator.COUNT_NOISE.configured((DecoratorConfiguration)new NoiseDependantDecoratorConfiguration(-0.8D, 0, 7))));
/* 265 */   public static final ConfiguredFeature<?, ?> PATCH_TALL_GRASS = register("patch_tall_grass", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.TALL_GRASS_CONFIG).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(7));
/* 266 */   public static final ConfiguredFeature<?, ?> PATCH_LARGE_FERN = register("patch_large_fern", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.LARGE_FERN), (BlockPlacer)new DoublePlantPlacer()))
/*     */ 
/*     */ 
/*     */         
/* 270 */         .tries(64)
/* 271 */         .noProjection()
/* 272 */         .build()).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(7));
/* 273 */   public static final ConfiguredFeature<?, ?> PATCH_CACTUS = register("patch_cactus", Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.CACTUS), (BlockPlacer)new ColumnPlacer(1, 2)))
/*     */ 
/*     */ 
/*     */         
/* 277 */         .tries(10)
/* 278 */         .noProjection()
/* 279 */         .build()));
/* 280 */   public static final ConfiguredFeature<?, ?> PATCH_CACTUS_DESERT = register("patch_cactus_desert", (ConfiguredFeature<?, ?>)PATCH_CACTUS.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(10));
/* 281 */   public static final ConfiguredFeature<?, ?> PATCH_CACTUS_DECORATED = register("patch_cactus_decorated", (ConfiguredFeature<?, ?>)PATCH_CACTUS.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(5));
/* 282 */   public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_SWAMP = register("patch_sugar_cane_swamp", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.SUGAR_CANE_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(20));
/* 283 */   public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_DESERT = register("patch_sugar_cane_desert", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.SUGAR_CANE_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(60));
/* 284 */   public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_BADLANDS = register("patch_sugar_cane_badlands", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.SUGAR_CANE_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(13));
/* 285 */   public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE = register("patch_sugar_cane", (ConfiguredFeature<?, ?>)Feature.RANDOM_PATCH.configured((FeatureConfiguration)Configs.SUGAR_CANE_CONFIG).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(10));
/*     */ 
/*     */ 
/*     */   
/* 289 */   public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_NETHER = register("brown_mushroom_nether", (ConfiguredFeature<?, ?>)((ConfiguredFeature)PATCH_BROWN_MUSHROOM.range(128)).chance(2));
/* 290 */   public static final ConfiguredFeature<?, ?> RED_MUSHROOM_NETHER = register("red_mushroom_nether", (ConfiguredFeature<?, ?>)((ConfiguredFeature)PATCH_RED_MUSHROOM.range(128)).chance(2));
/* 291 */   public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_NORMAL = register("brown_mushroom_normal", (ConfiguredFeature<?, ?>)PATCH_BROWN_MUSHROOM.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).chance(4));
/* 292 */   public static final ConfiguredFeature<?, ?> RED_MUSHROOM_NORMAL = register("red_mushroom_normal", (ConfiguredFeature<?, ?>)PATCH_RED_MUSHROOM.decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE).chance(8));
/* 293 */   public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_TAIGA = register("brown_mushroom_taiga", ((ConfiguredFeature)PATCH_BROWN_MUSHROOM.chance(4)).decorated(Decorators.HEIGHTMAP_SQUARE));
/* 294 */   public static final ConfiguredFeature<?, ?> RED_MUSHROOM_TAIGA = register("red_mushroom_taiga", ((ConfiguredFeature)PATCH_RED_MUSHROOM.chance(8)).decorated(Decorators.HEIGHTMAP_DOUBLE_SQUARE));
/* 295 */   public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_GIANT = register("brown_mushroom_giant", (ConfiguredFeature<?, ?>)BROWN_MUSHROOM_TAIGA.count(3));
/* 296 */   public static final ConfiguredFeature<?, ?> RED_MUSHROOM_GIANT = register("red_mushroom_giant", (ConfiguredFeature<?, ?>)RED_MUSHROOM_TAIGA.count(3));
/* 297 */   public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_SWAMP = register("brown_mushroom_swamp", (ConfiguredFeature<?, ?>)BROWN_MUSHROOM_TAIGA.count(8));
/* 298 */   public static final ConfiguredFeature<?, ?> RED_MUSHROOM_SWAMP = register("red_mushroom_swamp", (ConfiguredFeature<?, ?>)RED_MUSHROOM_TAIGA.count(8));
/*     */ 
/*     */ 
/*     */   
/* 302 */   public static final ConfiguredFeature<?, ?> ORE_MAGMA = register("ore_magma", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.MAGMA_BLOCK, 33)).decorated(FeatureDecorator.MAGMA.configured((DecoratorConfiguration)NoneDecoratorConfiguration.INSTANCE)).squared()).count(4));
/* 303 */   public static final ConfiguredFeature<?, ?> ORE_SOUL_SAND = register("ore_soul_sand", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.SOUL_SAND, 12)).range(32)).squared()).count(12));
/* 304 */   public static final ConfiguredFeature<?, ?> ORE_GOLD_DELTAS = register("ore_gold_deltas", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.NETHER_GOLD_ORE, 10)).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(20));
/* 305 */   public static final ConfiguredFeature<?, ?> ORE_QUARTZ_DELTAS = register("ore_quartz_deltas", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.NETHER_QUARTZ_ORE, 14)).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(32));
/* 306 */   public static final ConfiguredFeature<?, ?> ORE_GOLD_NETHER = register("ore_gold_nether", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.NETHER_GOLD_ORE, 10)).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(10));
/* 307 */   public static final ConfiguredFeature<?, ?> ORE_QUARTZ_NETHER = register("ore_quartz_nether", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.NETHER_QUARTZ_ORE, 14)).decorated(Decorators.RANGE_10_20_ROOFED).squared()).count(16));
/* 308 */   public static final ConfiguredFeature<?, ?> ORE_GRAVEL_NETHER = register("ore_gravel_nether", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.GRAVEL, 33)).decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(5, 0, 37))).squared()).count(2));
/* 309 */   public static final ConfiguredFeature<?, ?> ORE_BLACKSTONE = register("ore_blackstone", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHERRACK, States.BLACKSTONE, 33)).decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(5, 10, 37))).squared()).count(2));
/* 310 */   public static final ConfiguredFeature<?, ?> ORE_DIRT = register("ore_dirt", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.DIRT, 33)).range(256)).squared()).count(10));
/* 311 */   public static final ConfiguredFeature<?, ?> ORE_GRAVEL = register("ore_gravel", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.GRAVEL, 33)).range(256)).squared()).count(8));
/* 312 */   public static final ConfiguredFeature<?, ?> ORE_GRANITE = register("ore_granite", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.GRANITE, 33)).range(80)).squared()).count(10));
/*     */   
/* 314 */   public static final ConfiguredFeature<?, ?> ORE_DIORITE = register("ore_diorite", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.DIORITE, 33)).range(80)).squared()).count(10));
/* 315 */   public static final ConfiguredFeature<?, ?> ORE_ANDESITE = register("ore_andesite", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.ANDESITE, 33)).range(80)).squared()).count(10));
/* 316 */   public static final ConfiguredFeature<?, ?> ORE_COAL = register("ore_coal", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.COAL_ORE, 17)).range(128)).squared()).count(20));
/* 317 */   public static final ConfiguredFeature<?, ?> ORE_IRON = register("ore_iron", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.IRON_ORE, 9)).range(64)).squared()).count(20));
/* 318 */   public static final ConfiguredFeature<?, ?> ORE_GOLD_EXTRA = register("ore_gold_extra", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.GOLD_ORE, 9)).decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(32, 32, 80))).squared()).count(20));
/* 319 */   public static final ConfiguredFeature<?, ?> ORE_GOLD = register("ore_gold", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.GOLD_ORE, 9)).range(32)).squared()).count(2));
/* 320 */   public static final ConfiguredFeature<?, ?> ORE_REDSTONE = register("ore_redstone", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.REDSTONE_ORE, 8)).range(16)).squared()).count(8));
/* 321 */   public static final ConfiguredFeature<?, ?> ORE_DIAMOND = register("ore_diamond", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.DIAMOND_ORE, 8)).range(16)).squared());
/* 322 */   public static final ConfiguredFeature<?, ?> ORE_LAPIS = register("ore_lapis", (ConfiguredFeature<?, ?>)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.LAPIS_ORE, 7)).decorated(FeatureDecorator.DEPTH_AVERAGE.configured((DecoratorConfiguration)new DepthAverageConfigation(16, 16))).squared());
/* 323 */   public static final ConfiguredFeature<?, ?> ORE_INFESTED = register("ore_infested", (ConfiguredFeature<?, ?>)((ConfiguredFeature)((ConfiguredFeature)Feature.ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, States.INFESTED_STONE, 9)).range(64)).squared()).count(7));
/* 324 */   public static final ConfiguredFeature<?, ?> ORE_EMERALD = register("ore_emerald", Feature.EMERALD_ORE.configured((FeatureConfiguration)new ReplaceBlockConfiguration(States.STONE, States.EMERALD_ORE)).decorated(FeatureDecorator.EMERALD_ORE.configured((DecoratorConfiguration)DecoratorConfiguration.NONE)));
/* 325 */   public static final ConfiguredFeature<?, ?> ORE_DEBRIS_LARGE = register("ore_debris_large", (ConfiguredFeature<?, ?>)Feature.NO_SURFACE_ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES, States.ANCIENT_DEBRIS, 3)).decorated(FeatureDecorator.DEPTH_AVERAGE.configured((DecoratorConfiguration)new DepthAverageConfigation(16, 8))).squared());
/* 326 */   public static final ConfiguredFeature<?, ?> ORE_DEBRIS_SMALL = register("ore_debris_small", (ConfiguredFeature<?, ?>)Feature.NO_SURFACE_ORE.configured((FeatureConfiguration)new OreConfiguration(OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES, States.ANCIENT_DEBRIS, 2)).decorated(FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(8, 16, 128))).squared());
/*     */ 
/*     */ 
/*     */   
/* 330 */   public static final ConfiguredFeature<?, ?> CRIMSON_FUNGI = register("crimson_fungi", Feature.HUGE_FUNGUS.configured((FeatureConfiguration)HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_NOT_PLANTED_CONFIG).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(8))));
/* 331 */   public static final ConfiguredFeature<HugeFungusConfiguration, ?> CRIMSON_FUNGI_PLANTED = register("crimson_fungi_planted", Feature.HUGE_FUNGUS.configured((FeatureConfiguration)HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG));
/* 332 */   public static final ConfiguredFeature<?, ?> WARPED_FUNGI = register("warped_fungi", Feature.HUGE_FUNGUS.configured((FeatureConfiguration)HugeFungusConfiguration.HUGE_WARPED_FUNGI_NOT_PLANTED_CONFIG).decorated(FeatureDecorator.COUNT_MULTILAYER.configured((DecoratorConfiguration)new CountConfiguration(8))));
/* 333 */   public static final ConfiguredFeature<HugeFungusConfiguration, ?> WARPED_FUNGI_PLANTED = register("warped_fungi_planted", Feature.HUGE_FUNGUS.configured((FeatureConfiguration)HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG));
/*     */ 
/*     */ 
/*     */   
/* 337 */   public static final ConfiguredFeature<?, ?> HUGE_BROWN_MUSHROOM = register("huge_brown_mushroom", Feature.HUGE_BROWN_MUSHROOM.configured((FeatureConfiguration)new HugeMushroomFeatureConfiguration((BlockStateProvider)new SimpleStateProvider(States.HUGE_BROWN_MUSHROOM), (BlockStateProvider)new SimpleStateProvider(States.HUGE_MUSHROOM_STEM), 3)));
/* 338 */   public static final ConfiguredFeature<?, ?> HUGE_RED_MUSHROOM = register("huge_red_mushroom", Feature.HUGE_RED_MUSHROOM.configured((FeatureConfiguration)new HugeMushroomFeatureConfiguration((BlockStateProvider)new SimpleStateProvider(States.HUGE_RED_MUSHROOM), (BlockStateProvider)new SimpleStateProvider(States.HUGE_MUSHROOM_STEM), 2)));
/*     */   
/* 340 */   public static final ConfiguredFeature<TreeConfiguration, ?> OAK = register("oak", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.OAK_LOG), (BlockStateProvider)new SimpleStateProvider(States.OAK_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 343 */             UniformInt.fixed(2), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(4, 2, 0), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 347 */         .ignoreVines()
/* 348 */         .build()));
/* 349 */   public static final ConfiguredFeature<TreeConfiguration, ?> DARK_OAK = register("dark_oak", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.DARK_OAK_LOG), (BlockStateProvider)new SimpleStateProvider(States.DARK_OAK_LEAVES), (FoliagePlacer)new DarkOakFoliagePlacer(
/*     */ 
/*     */             
/* 352 */             UniformInt.fixed(0), UniformInt.fixed(0)), (TrunkPlacer)new DarkOakTrunkPlacer(6, 2, 1), (FeatureSize)new ThreeLayersFeatureSize(1, 1, 0, 1, 2, 
/*     */             
/* 354 */             OptionalInt.empty())))
/*     */         
/* 356 */         .maxWaterDepth(2147483647)
/*     */         
/* 358 */         .heightmap(Heightmap.Types.MOTION_BLOCKING)
/* 359 */         .ignoreVines()
/* 360 */         .build()));
/* 361 */   public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH = register("birch", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.BIRCH_LOG), (BlockStateProvider)new SimpleStateProvider(States.BIRCH_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 364 */             UniformInt.fixed(2), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(5, 2, 0), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 368 */         .ignoreVines()
/* 369 */         .build()));
/* 370 */   public static final ConfiguredFeature<TreeConfiguration, ?> ACACIA = register("acacia", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.ACACIA_LOG), (BlockStateProvider)new SimpleStateProvider(States.ACACIA_LEAVES), (FoliagePlacer)new AcaciaFoliagePlacer(
/*     */ 
/*     */             
/* 373 */             UniformInt.fixed(2), UniformInt.fixed(0)), (TrunkPlacer)new ForkingTrunkPlacer(5, 2, 2), (FeatureSize)new TwoLayersFeatureSize(1, 0, 2)))
/*     */ 
/*     */ 
/*     */         
/* 377 */         .ignoreVines()
/* 378 */         .build()));
/* 379 */   public static final ConfiguredFeature<TreeConfiguration, ?> SPRUCE = register("spruce", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LOG), (BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LEAVES), (FoliagePlacer)new SpruceFoliagePlacer(
/*     */ 
/*     */             
/* 382 */             UniformInt.of(2, 1), UniformInt.of(0, 2), UniformInt.of(1, 1)), (TrunkPlacer)new StraightTrunkPlacer(5, 2, 1), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2)))
/*     */ 
/*     */ 
/*     */         
/* 386 */         .ignoreVines()
/* 387 */         .build()));
/* 388 */   public static final ConfiguredFeature<TreeConfiguration, ?> PINE = register("pine", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LOG), (BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LEAVES), (FoliagePlacer)new PineFoliagePlacer(
/*     */ 
/*     */             
/* 391 */             UniformInt.fixed(1), UniformInt.fixed(1), UniformInt.of(3, 1)), (TrunkPlacer)new StraightTrunkPlacer(6, 4, 0), (FeatureSize)new TwoLayersFeatureSize(2, 0, 2)))
/*     */ 
/*     */ 
/*     */         
/* 395 */         .ignoreVines()
/* 396 */         .build()));
/* 397 */   public static final ConfiguredFeature<TreeConfiguration, ?> JUNGLE_TREE = register("jungle_tree", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LOG), (BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 400 */             UniformInt.fixed(2), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(4, 8, 0), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 404 */         .decorators((List)ImmutableList.of(new CocoaDecorator(0.2F), TrunkVineDecorator.INSTANCE, LeaveVineDecorator.INSTANCE))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 409 */         .ignoreVines()
/* 410 */         .build()));
/* 411 */   public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK = register("fancy_oak", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.OAK_LOG), (BlockStateProvider)new SimpleStateProvider(States.OAK_LEAVES), (FoliagePlacer)new FancyFoliagePlacer(
/*     */ 
/*     */             
/* 414 */             UniformInt.fixed(2), UniformInt.fixed(4), 4), (TrunkPlacer)new FancyTrunkPlacer(3, 11, 0), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0, 
/*     */             
/* 416 */             OptionalInt.of(4))))
/*     */         
/* 418 */         .ignoreVines()
/* 419 */         .heightmap(Heightmap.Types.MOTION_BLOCKING)
/* 420 */         .build()));
/* 421 */   public static final ConfiguredFeature<TreeConfiguration, ?> JUNGLE_TREE_NO_VINE = register("jungle_tree_no_vine", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LOG), (BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 424 */             UniformInt.fixed(2), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(4, 8, 0), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 428 */         .ignoreVines()
/* 429 */         .build()));
/* 430 */   public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_JUNGLE_TREE = register("mega_jungle_tree", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LOG), (BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LEAVES), (FoliagePlacer)new MegaJungleFoliagePlacer(
/*     */ 
/*     */             
/* 433 */             UniformInt.fixed(2), UniformInt.fixed(0), 2), (TrunkPlacer)new MegaJungleTrunkPlacer(10, 2, 19), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */ 
/*     */         
/* 437 */         .decorators((List)ImmutableList.of(TrunkVineDecorator.INSTANCE, LeaveVineDecorator.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 441 */         .build()));
/* 442 */   public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_SPRUCE = register("mega_spruce", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LOG), (BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/*     */ 
/*     */             
/* 445 */             UniformInt.fixed(0), UniformInt.fixed(0), UniformInt.of(13, 4)), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */ 
/*     */         
/* 449 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator((BlockStateProvider)new SimpleStateProvider(States.PODZOL))))
/*     */ 
/*     */         
/* 452 */         .build()));
/* 453 */   public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_PINE = register("mega_pine", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LOG), (BlockStateProvider)new SimpleStateProvider(States.SPRUCE_LEAVES), (FoliagePlacer)new MegaPineFoliagePlacer(
/*     */ 
/*     */             
/* 456 */             UniformInt.fixed(0), UniformInt.fixed(0), UniformInt.of(3, 4)), (TrunkPlacer)new GiantTrunkPlacer(13, 2, 14), (FeatureSize)new TwoLayersFeatureSize(1, 1, 2)))
/*     */ 
/*     */ 
/*     */         
/* 460 */         .decorators((List)ImmutableList.of(new AlterGroundDecorator((BlockStateProvider)new SimpleStateProvider(States.PODZOL))))
/*     */ 
/*     */         
/* 463 */         .build()));
/* 464 */   public static final ConfiguredFeature<TreeConfiguration, ?> SUPER_BIRCH_BEES_0002 = register("super_birch_bees_0002", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.BIRCH_LOG), (BlockStateProvider)new SimpleStateProvider(States.BIRCH_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 467 */             UniformInt.fixed(2), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(5, 2, 6), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 471 */         .ignoreVines()
/* 472 */         .decorators((List)ImmutableList.of(Decorators.BEEHIVE_0002))
/* 473 */         .build()));
/* 474 */   public static final ConfiguredFeature<?, ?> SWAMP_TREE = register("swamp_tree", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.OAK_LOG), (BlockStateProvider)new SimpleStateProvider(States.OAK_LEAVES), (FoliagePlacer)new BlobFoliagePlacer(
/*     */ 
/*     */             
/* 477 */             UniformInt.fixed(3), UniformInt.fixed(0), 3), (TrunkPlacer)new StraightTrunkPlacer(5, 3, 0), (FeatureSize)new TwoLayersFeatureSize(1, 0, 1)))
/*     */ 
/*     */ 
/*     */         
/* 481 */         .maxWaterDepth(1)
/* 482 */         .decorators((List)ImmutableList.of(LeaveVineDecorator.INSTANCE))
/* 483 */         .build()).decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1))));
/* 484 */   public static final ConfiguredFeature<?, ?> JUNGLE_BUSH = register("jungle_bush", Feature.TREE.configured((FeatureConfiguration)(new TreeConfiguration.TreeConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.JUNGLE_LOG), (BlockStateProvider)new SimpleStateProvider(States.OAK_LEAVES), (FoliagePlacer)new BushFoliagePlacer(
/*     */ 
/*     */             
/* 487 */             UniformInt.fixed(2), UniformInt.fixed(1), 2), (TrunkPlacer)new StraightTrunkPlacer(1, 0, 0), (FeatureSize)new TwoLayersFeatureSize(0, 0, 0)))
/*     */ 
/*     */ 
/*     */         
/* 491 */         .heightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
/* 492 */         .build()));
/*     */   
/* 494 */   public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_0002 = register("oak_bees_0002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_0002))));
/* 495 */   public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_002 = register("oak_bees_002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_002))));
/* 496 */   public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_005 = register("oak_bees_005", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_005))));
/* 497 */   public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_0002 = register("birch_bees_0002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)BIRCH.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_0002))));
/* 498 */   public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_002 = register("birch_bees_002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)BIRCH.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_002))));
/* 499 */   public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_005 = register("birch_bees_005", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)BIRCH.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_005))));
/* 500 */   public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_0002 = register("fancy_oak_bees_0002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)FANCY_OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_0002))));
/* 501 */   public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_002 = register("fancy_oak_bees_002", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)FANCY_OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_002))));
/* 502 */   public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_005 = register("fancy_oak_bees_005", Feature.TREE.configured((FeatureConfiguration)((TreeConfiguration)FANCY_OAK.config()).withDecorators((List)ImmutableList.of(Decorators.BEEHIVE_005))));
/* 503 */   public static final ConfiguredFeature<?, ?> OAK_BADLANDS = register("oak_badlands", OAK.decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(5, 0.1F, 1))));
/* 504 */   public static final ConfiguredFeature<?, ?> SPRUCE_SNOWY = register("spruce_snowy", SPRUCE.decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.1F, 1))));
/*     */ 
/*     */ 
/*     */   
/* 508 */   public static final ConfiguredFeature<?, ?> FLOWER_WARM = register("flower_warm", (ConfiguredFeature<?, ?>)Feature.FLOWER.configured((FeatureConfiguration)Configs.DEFAULT_FLOWER_CONFIG).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(4));
/* 509 */   public static final ConfiguredFeature<?, ?> FLOWER_DEFAULT = register("flower_default", (ConfiguredFeature<?, ?>)Feature.FLOWER.configured((FeatureConfiguration)Configs.DEFAULT_FLOWER_CONFIG).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(2));
/* 510 */   public static final ConfiguredFeature<?, ?> FLOWER_FOREST = register("flower_forest", (ConfiguredFeature<?, ?>)Feature.FLOWER.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)ForestFlowerProvider.INSTANCE, (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 514 */         .tries(64)
/* 515 */         .build()).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(100));
/* 516 */   public static final ConfiguredFeature<?, ?> FLOWER_SWAMP = register("flower_swamp", Feature.FLOWER.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.BLUE_ORCHID), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 520 */         .tries(64)
/* 521 */         .build()).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE));
/* 522 */   public static final ConfiguredFeature<?, ?> FLOWER_PLAIN = register("flower_plain", Feature.FLOWER.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)PlainFlowerProvider.INSTANCE, (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */         
/* 526 */         .tries(64)
/* 527 */         .build()));
/* 528 */   public static final ConfiguredFeature<?, ?> FLOWER_PLAIN_DECORATED = register("flower_plain_decorated", ((ConfiguredFeature)FLOWER_PLAIN.decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP).squared()).decorated(FeatureDecorator.COUNT_NOISE.configured((DecoratorConfiguration)new NoiseDependantDecoratorConfiguration(-0.8D, 15, 4))));
/*     */ 
/*     */ 
/*     */   
/* 532 */   private static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_FLOWER_FEATURES = ImmutableList.of(() -> Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.LILAC), (BlockPlacer)new DoublePlantPlacer())).tries(64).noProjection().build()), () -> Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.ROSE_BUSH), (BlockPlacer)new DoublePlantPlacer())).tries(64).noProjection().build()), () -> Feature.RANDOM_PATCH.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.PEONY), (BlockPlacer)new DoublePlantPlacer())).tries(64).noProjection().build()), () -> Feature.NO_BONEMEAL_FLOWER.configured((FeatureConfiguration)(new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(States.LILY_OF_THE_VALLEY), (BlockPlacer)SimpleBlockPlacer.INSTANCE)).tries(64).build()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 561 */   public static final ConfiguredFeature<?, ?> FOREST_FLOWER_VEGETATION_COMMON = register("forest_flower_vegetation_common", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SIMPLE_RANDOM_SELECTOR.configured((FeatureConfiguration)new SimpleRandomFeatureConfiguration((List)FOREST_FLOWER_FEATURES)).count(UniformInt.of(-1, 4))).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(5));
/* 562 */   public static final ConfiguredFeature<?, ?> FOREST_FLOWER_VEGETATION = register("forest_flower_vegetation", (ConfiguredFeature<?, ?>)((ConfiguredFeature)Feature.SIMPLE_RANDOM_SELECTOR.configured((FeatureConfiguration)new SimpleRandomFeatureConfiguration((List)FOREST_FLOWER_FEATURES)).count(UniformInt.of(-3, 4))).decorated(Decorators.ADD_32).decorated(Decorators.HEIGHTMAP_SQUARE).count(5));
/*     */   
/* 564 */   public static final ConfiguredFeature<?, ?> DARK_FOREST_VEGETATION_BROWN = register("dark_forest_vegetation_brown", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 565 */           (List)ImmutableList.of(HUGE_BROWN_MUSHROOM
/* 566 */             .weighted(0.025F), HUGE_RED_MUSHROOM
/* 567 */             .weighted(0.05F), DARK_OAK
/* 568 */             .weighted(0.6666667F), BIRCH
/* 569 */             .weighted(0.2F), FANCY_OAK
/* 570 */             .weighted(0.1F)), OAK))
/*     */ 
/*     */       
/* 573 */       .decorated(FeatureDecorator.DARK_OAK_TREE.configured((DecoratorConfiguration)DecoratorConfiguration.NONE)));
/*     */   
/* 575 */   public static final ConfiguredFeature<?, ?> DARK_FOREST_VEGETATION_RED = register("dark_forest_vegetation_red", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 576 */           (List)ImmutableList.of(HUGE_RED_MUSHROOM
/* 577 */             .weighted(0.025F), HUGE_BROWN_MUSHROOM
/* 578 */             .weighted(0.05F), DARK_OAK
/* 579 */             .weighted(0.6666667F), BIRCH
/* 580 */             .weighted(0.2F), FANCY_OAK
/* 581 */             .weighted(0.1F)), OAK))
/*     */ 
/*     */       
/* 584 */       .decorated(FeatureDecorator.DARK_OAK_TREE.configured((DecoratorConfiguration)DecoratorConfiguration.NONE)));
/*     */   
/* 586 */   public static final ConfiguredFeature<?, ?> WARM_OCEAN_VEGETATION = register("warm_ocean_vegetation", ((ConfiguredFeature)Feature.SIMPLE_RANDOM_SELECTOR.configured((FeatureConfiguration)new SimpleRandomFeatureConfiguration((List)ImmutableList.of(() -> Feature.CORAL_TREE.configured((FeatureConfiguration)FeatureConfiguration.NONE), () -> Feature.CORAL_CLAW.configured((FeatureConfiguration)FeatureConfiguration.NONE), () -> Feature.CORAL_MUSHROOM.configured((FeatureConfiguration)FeatureConfiguration.NONE))))
/*     */ 
/*     */ 
/*     */       
/* 590 */       .decorated(Decorators.TOP_SOLID_HEIGHTMAP).squared()).decorated(FeatureDecorator.COUNT_NOISE_BIASED.configured((DecoratorConfiguration)new NoiseCountFactorDecoratorConfiguration(20, 400.0D, 0.0D))));
/*     */   
/* 592 */   public static final ConfiguredFeature<?, ?> FOREST_FLOWER_TREES = register("forest_flower_trees", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 593 */           (List)ImmutableList.of(BIRCH_BEES_002
/* 594 */             .weighted(0.2F), FANCY_OAK_BEES_002
/* 595 */             .weighted(0.1F)), OAK_BEES_002))
/*     */       
/* 597 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(6, 0.1F, 1))));
/*     */   
/* 599 */   public static final ConfiguredFeature<?, ?> TAIGA_VEGETATION = register("taiga_vegetation", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 600 */           (List)ImmutableList.of(PINE.weighted(0.33333334F)), SPRUCE))
/*     */       
/* 602 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 604 */   public static final ConfiguredFeature<?, ?> TREES_SHATTERED_SAVANNA = register("trees_shattered_savanna", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 605 */           (List)ImmutableList.of(ACACIA.weighted(0.8F)), OAK))
/*     */       
/* 607 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1))));
/*     */   
/* 609 */   public static final ConfiguredFeature<?, ?> TREES_SAVANNA = register("trees_savanna", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 610 */           (List)ImmutableList.of(ACACIA.weighted(0.8F)), OAK))
/*     */       
/* 612 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1))));
/*     */   
/* 614 */   public static final ConfiguredFeature<?, ?> BIRCH_TALL = register("birch_tall", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 615 */           (List)ImmutableList.of(SUPER_BIRCH_BEES_0002.weighted(0.5F)), BIRCH_BEES_0002))
/*     */       
/* 617 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 619 */   public static final ConfiguredFeature<?, ?> TREES_BIRCH = register("trees_birch", BIRCH_BEES_0002
/* 620 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 622 */   public static final ConfiguredFeature<?, ?> TREES_MOUNTAIN_EDGE = register("trees_mountain_edge", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 623 */           (List)ImmutableList.of(SPRUCE
/* 624 */             .weighted(0.666F), FANCY_OAK
/* 625 */             .weighted(0.1F)), OAK))
/*     */ 
/*     */       
/* 628 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(3, 0.1F, 1))));
/*     */   
/* 630 */   public static final ConfiguredFeature<?, ?> TREES_MOUNTAIN = register("trees_mountain", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 631 */           (List)ImmutableList.of(SPRUCE
/* 632 */             .weighted(0.666F), FANCY_OAK
/* 633 */             .weighted(0.1F)), OAK))
/*     */ 
/*     */       
/* 636 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.1F, 1))));
/*     */   
/* 638 */   public static final ConfiguredFeature<?, ?> TREES_WATER = register("trees_water", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 639 */           (List)ImmutableList.of(FANCY_OAK.weighted(0.1F)), OAK))
/*     */       
/* 641 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.1F, 1))));
/*     */   
/* 643 */   public static final ConfiguredFeature<?, ?> BIRCH_OTHER = register("birch_other", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 644 */           (List)ImmutableList.of(BIRCH_BEES_0002
/* 645 */             .weighted(0.2F), FANCY_OAK_BEES_0002
/* 646 */             .weighted(0.1F)), OAK_BEES_0002))
/*     */ 
/*     */       
/* 649 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 651 */   public static final ConfiguredFeature<?, ?> PLAIN_VEGETATION = register("plain_vegetation", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 652 */           (List)ImmutableList.of(FANCY_OAK_BEES_005.weighted(0.33333334F)), OAK_BEES_005))
/*     */       
/* 654 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.05F, 1))));
/*     */   
/* 656 */   public static final ConfiguredFeature<?, ?> TREES_JUNGLE_EDGE = register("trees_jungle_edge", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 657 */           (List)ImmutableList.of(FANCY_OAK
/* 658 */             .weighted(0.1F), JUNGLE_BUSH
/* 659 */             .weighted(0.5F)), JUNGLE_TREE))
/*     */ 
/*     */       
/* 662 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1))));
/*     */   
/* 664 */   public static final ConfiguredFeature<?, ?> TREES_GIANT_SPRUCE = register("trees_giant_spruce", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 665 */           (List)ImmutableList.of(MEGA_SPRUCE
/* 666 */             .weighted(0.33333334F), PINE
/* 667 */             .weighted(0.33333334F)), SPRUCE))
/*     */ 
/*     */       
/* 670 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 672 */   public static final ConfiguredFeature<?, ?> TREES_GIANT = register("trees_giant", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 673 */           (List)ImmutableList.of(MEGA_SPRUCE
/* 674 */             .weighted(0.025641026F), MEGA_PINE
/* 675 */             .weighted(0.30769232F), PINE
/* 676 */             .weighted(0.33333334F)), SPRUCE))
/*     */ 
/*     */       
/* 679 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(10, 0.1F, 1))));
/*     */   
/* 681 */   public static final ConfiguredFeature<?, ?> TREES_JUNGLE = register("trees_jungle", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 682 */           (List)ImmutableList.of(FANCY_OAK
/* 683 */             .weighted(0.1F), JUNGLE_BUSH
/* 684 */             .weighted(0.5F), MEGA_JUNGLE_TREE
/* 685 */             .weighted(0.33333334F)), JUNGLE_TREE))
/*     */ 
/*     */       
/* 688 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(50, 0.1F, 1))));
/*     */   
/* 690 */   public static final ConfiguredFeature<?, ?> BAMBOO_VEGETATION = register("bamboo_vegetation", Feature.RANDOM_SELECTOR.configured((FeatureConfiguration)new RandomFeatureConfiguration(
/* 691 */           (List)ImmutableList.of(FANCY_OAK
/* 692 */             .weighted(0.05F), JUNGLE_BUSH
/* 693 */             .weighted(0.15F), MEGA_JUNGLE_TREE
/* 694 */             .weighted(0.7F)), Feature.RANDOM_PATCH
/*     */           
/* 696 */           .configured((FeatureConfiguration)Configs.JUNGLE_GRASS_CONFIG)))
/* 697 */       .decorated(Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured((DecoratorConfiguration)new FrequencyWithExtraChanceDecoratorConfiguration(30, 0.1F, 1))));
/*     */   
/* 699 */   public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_VEGETATION = register("mushroom_field_vegetation", Feature.RANDOM_BOOLEAN_SELECTOR.configured((FeatureConfiguration)new RandomBooleanFeatureConfiguration(() -> HUGE_RED_MUSHROOM, () -> HUGE_BROWN_MUSHROOM))
/*     */ 
/*     */       
/* 702 */       .decorated(Decorators.HEIGHTMAP_SQUARE));
/*     */   
/*     */   private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String debug0, ConfiguredFeature<FC, ?> debug1) {
/* 705 */     return (ConfiguredFeature<FC, ?>)Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, debug0, debug1);
/*     */   }
/*     */   
/*     */   public static final class Decorators {
/* 709 */     public static final BeehiveDecorator BEEHIVE_0002 = new BeehiveDecorator(0.002F);
/* 710 */     public static final BeehiveDecorator BEEHIVE_002 = new BeehiveDecorator(0.02F);
/* 711 */     public static final BeehiveDecorator BEEHIVE_005 = new BeehiveDecorator(0.05F);
/* 712 */     public static final ConfiguredDecorator<CountConfiguration> FIRE = FeatureDecorator.FIRE.configured((DecoratorConfiguration)new CountConfiguration(10));
/*     */     
/* 714 */     public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP = FeatureDecorator.HEIGHTMAP.configured((DecoratorConfiguration)DecoratorConfiguration.NONE);
/* 715 */     public static final ConfiguredDecorator<NoneDecoratorConfiguration> TOP_SOLID_HEIGHTMAP = FeatureDecorator.TOP_SOLID_HEIGHTMAP.configured((DecoratorConfiguration)DecoratorConfiguration.NONE);
/* 716 */     public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP_WORLD_SURFACE = FeatureDecorator.HEIGHTMAP_WORLD_SURFACE.configured((DecoratorConfiguration)DecoratorConfiguration.NONE);
/* 717 */     public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP_DOUBLE = FeatureDecorator.HEIGHTMAP_SPREAD_DOUBLE.configured((DecoratorConfiguration)DecoratorConfiguration.NONE);
/*     */     
/* 719 */     public static final ConfiguredDecorator<RangeDecoratorConfiguration> RANGE_10_20_ROOFED = FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(10, 20, 128));
/* 720 */     public static final ConfiguredDecorator<RangeDecoratorConfiguration> RANGE_4_8_ROOFED = FeatureDecorator.RANGE.configured((DecoratorConfiguration)new RangeDecoratorConfiguration(4, 8, 128));
/* 721 */     public static final ConfiguredDecorator<?> ADD_32 = FeatureDecorator.SPREAD_32_ABOVE.configured((DecoratorConfiguration)NoneDecoratorConfiguration.INSTANCE);
/*     */     
/* 723 */     public static final ConfiguredDecorator<?> HEIGHTMAP_SQUARE = (ConfiguredDecorator)HEIGHTMAP.squared();
/* 724 */     public static final ConfiguredDecorator<?> HEIGHTMAP_DOUBLE_SQUARE = (ConfiguredDecorator)HEIGHTMAP_DOUBLE.squared();
/* 725 */     public static final ConfiguredDecorator<?> TOP_SOLID_HEIGHTMAP_SQUARE = (ConfiguredDecorator)TOP_SOLID_HEIGHTMAP.squared();
/*     */   }
/*     */   
/*     */   public static final class States {
/* 729 */     protected static final BlockState GRASS = Blocks.GRASS.defaultBlockState();
/* 730 */     protected static final BlockState FERN = Blocks.FERN.defaultBlockState();
/* 731 */     protected static final BlockState PODZOL = Blocks.PODZOL.defaultBlockState();
/* 732 */     protected static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
/* 733 */     protected static final BlockState MYCELIUM = Blocks.MYCELIUM.defaultBlockState();
/* 734 */     protected static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
/* 735 */     protected static final BlockState ICE = Blocks.ICE.defaultBlockState();
/* 736 */     protected static final BlockState OAK_LOG = Blocks.OAK_LOG.defaultBlockState();
/* 737 */     protected static final BlockState OAK_LEAVES = Blocks.OAK_LEAVES.defaultBlockState();
/* 738 */     protected static final BlockState JUNGLE_LOG = Blocks.JUNGLE_LOG.defaultBlockState();
/* 739 */     protected static final BlockState JUNGLE_LEAVES = Blocks.JUNGLE_LEAVES.defaultBlockState();
/* 740 */     protected static final BlockState SPRUCE_LOG = Blocks.SPRUCE_LOG.defaultBlockState();
/* 741 */     protected static final BlockState SPRUCE_LEAVES = Blocks.SPRUCE_LEAVES.defaultBlockState();
/* 742 */     protected static final BlockState ACACIA_LOG = Blocks.ACACIA_LOG.defaultBlockState();
/* 743 */     protected static final BlockState ACACIA_LEAVES = Blocks.ACACIA_LEAVES.defaultBlockState();
/* 744 */     protected static final BlockState BIRCH_LOG = Blocks.BIRCH_LOG.defaultBlockState();
/* 745 */     protected static final BlockState BIRCH_LEAVES = Blocks.BIRCH_LEAVES.defaultBlockState();
/* 746 */     protected static final BlockState DARK_OAK_LOG = Blocks.DARK_OAK_LOG.defaultBlockState();
/* 747 */     protected static final BlockState DARK_OAK_LEAVES = Blocks.DARK_OAK_LEAVES.defaultBlockState();
/* 748 */     protected static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
/* 749 */     protected static final BlockState LARGE_FERN = Blocks.LARGE_FERN.defaultBlockState();
/* 750 */     protected static final BlockState TALL_GRASS = Blocks.TALL_GRASS.defaultBlockState();
/* 751 */     protected static final BlockState LILAC = Blocks.LILAC.defaultBlockState();
/* 752 */     protected static final BlockState ROSE_BUSH = Blocks.ROSE_BUSH.defaultBlockState();
/* 753 */     protected static final BlockState PEONY = Blocks.PEONY.defaultBlockState();
/* 754 */     protected static final BlockState BROWN_MUSHROOM = Blocks.BROWN_MUSHROOM.defaultBlockState();
/* 755 */     protected static final BlockState RED_MUSHROOM = Blocks.RED_MUSHROOM.defaultBlockState();
/* 756 */     protected static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
/* 757 */     protected static final BlockState BLUE_ICE = Blocks.BLUE_ICE.defaultBlockState();
/* 758 */     protected static final BlockState LILY_OF_THE_VALLEY = Blocks.LILY_OF_THE_VALLEY.defaultBlockState();
/* 759 */     protected static final BlockState BLUE_ORCHID = Blocks.BLUE_ORCHID.defaultBlockState();
/* 760 */     protected static final BlockState POPPY = Blocks.POPPY.defaultBlockState();
/* 761 */     protected static final BlockState DANDELION = Blocks.DANDELION.defaultBlockState();
/* 762 */     protected static final BlockState DEAD_BUSH = Blocks.DEAD_BUSH.defaultBlockState();
/* 763 */     protected static final BlockState MELON = Blocks.MELON.defaultBlockState();
/* 764 */     protected static final BlockState PUMPKIN = Blocks.PUMPKIN.defaultBlockState();
/* 765 */     protected static final BlockState SWEET_BERRY_BUSH = (BlockState)Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(3));
/* 766 */     protected static final BlockState FIRE = Blocks.FIRE.defaultBlockState();
/* 767 */     protected static final BlockState SOUL_FIRE = Blocks.SOUL_FIRE.defaultBlockState();
/* 768 */     protected static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
/* 769 */     protected static final BlockState SOUL_SOIL = Blocks.SOUL_SOIL.defaultBlockState();
/* 770 */     protected static final BlockState CRIMSON_ROOTS = Blocks.CRIMSON_ROOTS.defaultBlockState();
/* 771 */     protected static final BlockState LILY_PAD = Blocks.LILY_PAD.defaultBlockState();
/* 772 */     protected static final BlockState SNOW = Blocks.SNOW.defaultBlockState();
/* 773 */     protected static final BlockState JACK_O_LANTERN = Blocks.JACK_O_LANTERN.defaultBlockState();
/* 774 */     protected static final BlockState SUNFLOWER = Blocks.SUNFLOWER.defaultBlockState();
/* 775 */     protected static final BlockState CACTUS = Blocks.CACTUS.defaultBlockState();
/* 776 */     protected static final BlockState SUGAR_CANE = Blocks.SUGAR_CANE.defaultBlockState();
/* 777 */     protected static final BlockState HUGE_RED_MUSHROOM = (BlockState)Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false));
/* 778 */     protected static final BlockState HUGE_BROWN_MUSHROOM = (BlockState)((BlockState)Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(true))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false));
/* 779 */     protected static final BlockState HUGE_MUSHROOM_STEM = (BlockState)((BlockState)Blocks.MUSHROOM_STEM.defaultBlockState().setValue((Property)HugeMushroomBlock.UP, Boolean.valueOf(false))).setValue((Property)HugeMushroomBlock.DOWN, Boolean.valueOf(false));
/*     */     
/* 781 */     protected static final FluidState WATER_STATE = Fluids.WATER.defaultFluidState();
/* 782 */     protected static final FluidState LAVA_STATE = Fluids.LAVA.defaultFluidState();
/* 783 */     protected static final BlockState WATER = Blocks.WATER.defaultBlockState();
/* 784 */     protected static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
/* 785 */     protected static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
/* 786 */     protected static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/* 787 */     protected static final BlockState GRANITE = Blocks.GRANITE.defaultBlockState();
/* 788 */     protected static final BlockState DIORITE = Blocks.DIORITE.defaultBlockState();
/* 789 */     protected static final BlockState ANDESITE = Blocks.ANDESITE.defaultBlockState();
/* 790 */     protected static final BlockState COAL_ORE = Blocks.COAL_ORE.defaultBlockState();
/* 791 */     protected static final BlockState IRON_ORE = Blocks.IRON_ORE.defaultBlockState();
/* 792 */     protected static final BlockState GOLD_ORE = Blocks.GOLD_ORE.defaultBlockState();
/* 793 */     protected static final BlockState REDSTONE_ORE = Blocks.REDSTONE_ORE.defaultBlockState();
/* 794 */     protected static final BlockState DIAMOND_ORE = Blocks.DIAMOND_ORE.defaultBlockState();
/* 795 */     protected static final BlockState LAPIS_ORE = Blocks.LAPIS_ORE.defaultBlockState();
/* 796 */     protected static final BlockState STONE = Blocks.STONE.defaultBlockState();
/* 797 */     protected static final BlockState EMERALD_ORE = Blocks.EMERALD_ORE.defaultBlockState();
/* 798 */     protected static final BlockState INFESTED_STONE = Blocks.INFESTED_STONE.defaultBlockState();
/* 799 */     protected static final BlockState SAND = Blocks.SAND.defaultBlockState();
/* 800 */     protected static final BlockState CLAY = Blocks.CLAY.defaultBlockState();
/* 801 */     protected static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
/* 802 */     protected static final BlockState SEAGRASS = Blocks.SEAGRASS.defaultBlockState();
/* 803 */     protected static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.defaultBlockState();
/* 804 */     protected static final BlockState SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
/* 805 */     protected static final BlockState NETHER_GOLD_ORE = Blocks.NETHER_GOLD_ORE.defaultBlockState();
/* 806 */     protected static final BlockState NETHER_QUARTZ_ORE = Blocks.NETHER_QUARTZ_ORE.defaultBlockState();
/* 807 */     protected static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
/* 808 */     protected static final BlockState ANCIENT_DEBRIS = Blocks.ANCIENT_DEBRIS.defaultBlockState();
/* 809 */     protected static final BlockState BASALT = Blocks.BASALT.defaultBlockState();
/* 810 */     protected static final BlockState CRIMSON_FUNGUS = Blocks.CRIMSON_FUNGUS.defaultBlockState();
/* 811 */     protected static final BlockState WARPED_FUNGUS = Blocks.WARPED_FUNGUS.defaultBlockState();
/* 812 */     protected static final BlockState WARPED_ROOTS = Blocks.WARPED_ROOTS.defaultBlockState();
/* 813 */     protected static final BlockState NETHER_SPROUTS = Blocks.NETHER_SPROUTS.defaultBlockState();
/*     */   }
/*     */   
/*     */   public static final class Configs {
/* 817 */     public static final RandomPatchConfiguration DEFAULT_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(Features.States.GRASS), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */       
/* 821 */       .tries(32)
/* 822 */       .build();
/* 823 */     public static final RandomPatchConfiguration TAIGA_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)(new WeightedStateProvider())
/* 824 */         .add(Features.States.GRASS, 1).add(Features.States.FERN, 4), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */       
/* 827 */       .tries(32)
/* 828 */       .build();
/* 829 */     public static final RandomPatchConfiguration JUNGLE_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)(new WeightedStateProvider())
/* 830 */         .add(Features.States.GRASS, 3).add(Features.States.FERN, 1), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */       
/* 833 */       .blacklist((Set)ImmutableSet.of(Features.States.PODZOL))
/* 834 */       .tries(32)
/* 835 */       .build();
/* 836 */     public static final RandomPatchConfiguration DEFAULT_FLOWER_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)(new WeightedStateProvider())
/* 837 */         .add(Features.States.POPPY, 2).add(Features.States.DANDELION, 1), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */       
/* 840 */       .tries(64)
/* 841 */       .build();
/* 842 */     public static final RandomPatchConfiguration DEAD_BUSH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(Features.States.DEAD_BUSH), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */       
/* 846 */       .tries(4)
/* 847 */       .build();
/* 848 */     public static final RandomPatchConfiguration SWEET_BERRY_BUSH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(Features.States.SWEET_BERRY_BUSH), (BlockPlacer)SimpleBlockPlacer.INSTANCE))
/*     */ 
/*     */ 
/*     */       
/* 852 */       .tries(64)
/* 853 */       .whitelist((Set)ImmutableSet.of(Features.States.GRASS_BLOCK.getBlock()))
/* 854 */       .noProjection()
/* 855 */       .build();
/* 856 */     public static final RandomPatchConfiguration TALL_GRASS_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(Features.States.TALL_GRASS), (BlockPlacer)new DoublePlantPlacer()))
/*     */ 
/*     */ 
/*     */       
/* 860 */       .tries(64)
/* 861 */       .noProjection()
/* 862 */       .build();
/* 863 */     public static final RandomPatchConfiguration SUGAR_CANE_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder((BlockStateProvider)new SimpleStateProvider(Features.States.SUGAR_CANE), (BlockPlacer)new ColumnPlacer(2, 2)))
/*     */ 
/*     */ 
/*     */       
/* 867 */       .tries(20)
/* 868 */       .xspread(4)
/* 869 */       .yspread(0)
/* 870 */       .zspread(4)
/* 871 */       .noProjection()
/* 872 */       .needWater()
/* 873 */       .build();
/* 874 */     public static final SpringConfiguration LAVA_SPRING_CONFIG = new SpringConfiguration(Features.States.LAVA_STATE, true, 4, 1, (Set)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE));
/* 875 */     public static final SpringConfiguration CLOSED_NETHER_SPRING_CONFIG = new SpringConfiguration(Features.States.LAVA_STATE, false, 5, 0, (Set)ImmutableSet.of(Blocks.NETHERRACK));
/* 876 */     public static final BlockPileConfiguration CRIMSON_FOREST_CONFIG = new BlockPileConfiguration((BlockStateProvider)(new WeightedStateProvider())
/* 877 */         .add(Features.States.CRIMSON_ROOTS, 87)
/* 878 */         .add(Features.States.CRIMSON_FUNGUS, 11)
/* 879 */         .add(Features.States.WARPED_FUNGUS, 1));
/*     */     
/* 881 */     public static final BlockPileConfiguration WARPED_FOREST_CONFIG = new BlockPileConfiguration((BlockStateProvider)(new WeightedStateProvider())
/* 882 */         .add(Features.States.WARPED_ROOTS, 85)
/* 883 */         .add(Features.States.CRIMSON_ROOTS, 1)
/* 884 */         .add(Features.States.WARPED_FUNGUS, 13)
/* 885 */         .add(Features.States.CRIMSON_FUNGUS, 1));
/*     */     
/* 887 */     public static final BlockPileConfiguration NETHER_SPROUTS_CONFIG = new BlockPileConfiguration((BlockStateProvider)new SimpleStateProvider(Features.States.NETHER_SPROUTS));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\Features.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */