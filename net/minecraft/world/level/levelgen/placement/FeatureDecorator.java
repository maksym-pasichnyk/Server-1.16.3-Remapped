/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.nether.CountMultiLayerDecorator;
/*    */ import net.minecraft.world.level.levelgen.placement.nether.FireDecorator;
/*    */ import net.minecraft.world.level.levelgen.placement.nether.GlowstoneDecorator;
/*    */ import net.minecraft.world.level.levelgen.placement.nether.MagmaDecorator;
/*    */ 
/*    */ public abstract class FeatureDecorator<DC extends DecoratorConfiguration>
/*    */ {
/* 20 */   public static final FeatureDecorator<NoneDecoratorConfiguration> NOPE = register("nope", new NopePlacementDecorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 22 */   public static final FeatureDecorator<ChanceDecoratorConfiguration> CHANCE = register("chance", new ChanceDecorator(ChanceDecoratorConfiguration.CODEC));
/* 23 */   public static final FeatureDecorator<CountConfiguration> COUNT = register("count", new CountDecorator(CountConfiguration.CODEC));
/* 24 */   public static final FeatureDecorator<NoiseDependantDecoratorConfiguration> COUNT_NOISE = register("count_noise", new CountNoiseDecorator(NoiseDependantDecoratorConfiguration.CODEC));
/* 25 */   public static final FeatureDecorator<NoiseCountFactorDecoratorConfiguration> COUNT_NOISE_BIASED = register("count_noise_biased", new NoiseBasedDecorator(NoiseCountFactorDecoratorConfiguration.CODEC));
/* 26 */   public static final FeatureDecorator<FrequencyWithExtraChanceDecoratorConfiguration> COUNT_EXTRA = register("count_extra", new CountWithExtraChanceDecorator(FrequencyWithExtraChanceDecoratorConfiguration.CODEC));
/*    */   
/* 28 */   public static final FeatureDecorator<NoneDecoratorConfiguration> SQUARE = register("square", new SquareDecorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 30 */   public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP = register("heightmap", new HeightmapDecorator<>(NoneDecoratorConfiguration.CODEC));
/* 31 */   public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", new HeightmapDoubleDecorator<>(NoneDecoratorConfiguration.CODEC));
/* 32 */   public static final FeatureDecorator<NoneDecoratorConfiguration> TOP_SOLID_HEIGHTMAP = register("top_solid_heightmap", new TopSolidHeightMapDecorator(NoneDecoratorConfiguration.CODEC));
/* 33 */   public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP_WORLD_SURFACE = register("heightmap_world_surface", new HeightMapWorldSurfaceDecorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 35 */   public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE = register("range", new RangeDecorator(RangeDecoratorConfiguration.CODEC));
/* 36 */   public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE_BIASED = register("range_biased", new BiasedRangeDecorator(RangeDecoratorConfiguration.CODEC));
/* 37 */   public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE_VERY_BIASED = register("range_very_biased", new VeryBiasedRangeDecorator(RangeDecoratorConfiguration.CODEC));
/* 38 */   public static final FeatureDecorator<DepthAverageConfigation> DEPTH_AVERAGE = register("depth_average", new DepthAverageDecorator(DepthAverageConfigation.CODEC));
/*    */   
/* 40 */   public static final FeatureDecorator<NoneDecoratorConfiguration> SPREAD_32_ABOVE = register("spread_32_above", new Spread32Decorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 42 */   public static final FeatureDecorator<CarvingMaskDecoratorConfiguration> CARVING_MASK = register("carving_mask", new CarvingMaskDecorator(CarvingMaskDecoratorConfiguration.CODEC));
/* 43 */   public static final FeatureDecorator<CountConfiguration> FIRE = (FeatureDecorator<CountConfiguration>)register("fire", new FireDecorator(CountConfiguration.CODEC));
/* 44 */   public static final FeatureDecorator<NoneDecoratorConfiguration> MAGMA = (FeatureDecorator<NoneDecoratorConfiguration>)register("magma", new MagmaDecorator(NoneDecoratorConfiguration.CODEC));
/* 45 */   public static final FeatureDecorator<NoneDecoratorConfiguration> EMERALD_ORE = register("emerald_ore", new EmeraldPlacementDecorator(NoneDecoratorConfiguration.CODEC));
/* 46 */   public static final FeatureDecorator<ChanceDecoratorConfiguration> LAVA_LAKE = register("lava_lake", new LakeLavaPlacementDecorator(ChanceDecoratorConfiguration.CODEC));
/* 47 */   public static final FeatureDecorator<ChanceDecoratorConfiguration> WATER_LAKE = register("water_lake", new LakeWaterPlacementDecorator(ChanceDecoratorConfiguration.CODEC));
/* 48 */   public static final FeatureDecorator<CountConfiguration> GLOWSTONE = (FeatureDecorator<CountConfiguration>)register("glowstone", new GlowstoneDecorator(CountConfiguration.CODEC));
/* 49 */   public static final FeatureDecorator<NoneDecoratorConfiguration> END_GATEWAY = register("end_gateway", new EndGatewayPlacementDecorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 51 */   public static final FeatureDecorator<NoneDecoratorConfiguration> DARK_OAK_TREE = register("dark_oak_tree", new DarkOakTreePlacementDecorator(NoneDecoratorConfiguration.CODEC));
/* 52 */   public static final FeatureDecorator<NoneDecoratorConfiguration> ICEBERG = register("iceberg", new IcebergPlacementDecorator(NoneDecoratorConfiguration.CODEC));
/* 53 */   public static final FeatureDecorator<NoneDecoratorConfiguration> END_ISLAND = register("end_island", new EndIslandPlacementDecorator(NoneDecoratorConfiguration.CODEC));
/*    */   
/* 55 */   public static final FeatureDecorator<DecoratedDecoratorConfiguration> DECORATED = register("decorated", new DecoratedDecorator(DecoratedDecoratorConfiguration.CODEC));
/*    */   
/* 57 */   public static final FeatureDecorator<CountConfiguration> COUNT_MULTILAYER = (FeatureDecorator<CountConfiguration>)register("count_multilayer", new CountMultiLayerDecorator(CountConfiguration.CODEC));
/*    */   
/*    */   private static <T extends DecoratorConfiguration, G extends FeatureDecorator<T>> G register(String debug0, G debug1) {
/* 60 */     return (G)Registry.register(Registry.DECORATOR, debug0, debug1);
/*    */   }
/*    */   
/*    */   private final Codec<ConfiguredDecorator<DC>> configuredCodec;
/*    */   
/*    */   public FeatureDecorator(Codec<DC> debug1) {
/* 66 */     this.configuredCodec = debug1.fieldOf("config").xmap(debug1 -> new ConfiguredDecorator<>(this, (DC)debug1), ConfiguredDecorator::config).codec();
/*    */   }
/*    */   
/*    */   public ConfiguredDecorator<DC> configured(DC debug1) {
/* 70 */     return new ConfiguredDecorator<>(this, debug1);
/*    */   }
/*    */   
/*    */   public Codec<ConfiguredDecorator<DC>> configuredCodec() {
/* 74 */     return this.configuredCodec;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
/*    */   }
/*    */   
/*    */   public abstract Stream<BlockPos> getPositions(DecorationContext paramDecorationContext, Random paramRandom, DC paramDC, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\placement\FeatureDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */