/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ 
/*     */ public abstract class Feature<FC extends FeatureConfiguration> {
/*  41 */   public static final Feature<NoneFeatureConfiguration> NO_OP = register("no_op", new NoOpFeature(NoneFeatureConfiguration.CODEC));
/*  42 */   public static final Feature<TreeConfiguration> TREE = register("tree", new TreeFeature(TreeConfiguration.CODEC));
/*     */   
/*  44 */   public static final AbstractFlowerFeature<RandomPatchConfiguration> FLOWER = register("flower", new DefaultFlowerFeature(RandomPatchConfiguration.CODEC));
/*  45 */   public static final AbstractFlowerFeature<RandomPatchConfiguration> NO_BONEMEAL_FLOWER = register("no_bonemeal_flower", new DefaultFlowerFeature(RandomPatchConfiguration.CODEC));
/*  46 */   public static final Feature<RandomPatchConfiguration> RANDOM_PATCH = register("random_patch", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
/*  47 */   public static final Feature<BlockPileConfiguration> BLOCK_PILE = register("block_pile", new BlockPileFeature(BlockPileConfiguration.CODEC));
/*  48 */   public static final Feature<SpringConfiguration> SPRING = register("spring_feature", new SpringFeature(SpringConfiguration.CODEC));
/*     */   
/*  50 */   public static final Feature<NoneFeatureConfiguration> CHORUS_PLANT = register("chorus_plant", new ChorusPlantFeature(NoneFeatureConfiguration.CODEC));
/*  51 */   public static final Feature<ReplaceBlockConfiguration> EMERALD_ORE = register("emerald_ore", new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));
/*     */   
/*  53 */   public static final Feature<NoneFeatureConfiguration> VOID_START_PLATFORM = register("void_start_platform", new VoidStartPlatformFeature(NoneFeatureConfiguration.CODEC));
/*  54 */   public static final Feature<NoneFeatureConfiguration> DESERT_WELL = register("desert_well", new DesertWellFeature(NoneFeatureConfiguration.CODEC));
/*  55 */   public static final Feature<NoneFeatureConfiguration> FOSSIL = register("fossil", new FossilFeature(NoneFeatureConfiguration.CODEC));
/*  56 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_RED_MUSHROOM = register("huge_red_mushroom", new HugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*  57 */   public static final Feature<HugeMushroomFeatureConfiguration> HUGE_BROWN_MUSHROOM = register("huge_brown_mushroom", new HugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
/*  58 */   public static final Feature<NoneFeatureConfiguration> ICE_SPIKE = register("ice_spike", new IceSpikeFeature(NoneFeatureConfiguration.CODEC));
/*  59 */   public static final Feature<NoneFeatureConfiguration> GLOWSTONE_BLOB = register("glowstone_blob", new GlowstoneFeature(NoneFeatureConfiguration.CODEC));
/*  60 */   public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER = register("freeze_top_layer", new SnowAndFreezeFeature(NoneFeatureConfiguration.CODEC));
/*  61 */   public static final Feature<NoneFeatureConfiguration> VINES = register("vines", new VinesFeature(NoneFeatureConfiguration.CODEC));
/*  62 */   public static final Feature<NoneFeatureConfiguration> MONSTER_ROOM = register("monster_room", new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));
/*  63 */   public static final Feature<NoneFeatureConfiguration> BLUE_ICE = register("blue_ice", new BlueIceFeature(NoneFeatureConfiguration.CODEC));
/*  64 */   public static final Feature<BlockStateConfiguration> ICEBERG = register("iceberg", new IcebergFeature(BlockStateConfiguration.CODEC));
/*  65 */   public static final Feature<BlockStateConfiguration> FOREST_ROCK = register("forest_rock", new BlockBlobFeature(BlockStateConfiguration.CODEC));
/*  66 */   public static final Feature<DiskConfiguration> DISK = register("disk", new DiskReplaceFeature(DiskConfiguration.CODEC));
/*  67 */   public static final Feature<DiskConfiguration> ICE_PATCH = register("ice_patch", new IcePatchFeature(DiskConfiguration.CODEC));
/*  68 */   public static final Feature<BlockStateConfiguration> LAKE = register("lake", new LakeFeature(BlockStateConfiguration.CODEC));
/*  69 */   public static final Feature<OreConfiguration> ORE = register("ore", new OreFeature(OreConfiguration.CODEC));
/*  70 */   public static final Feature<SpikeConfiguration> END_SPIKE = register("end_spike", new SpikeFeature(SpikeConfiguration.CODEC));
/*  71 */   public static final Feature<NoneFeatureConfiguration> END_ISLAND = register("end_island", new EndIslandFeature(NoneFeatureConfiguration.CODEC));
/*  72 */   public static final Feature<EndGatewayConfiguration> END_GATEWAY = register("end_gateway", new EndGatewayFeature(EndGatewayConfiguration.CODEC));
/*  73 */   public static final SeagrassFeature SEAGRASS = register("seagrass", new SeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
/*  74 */   public static final Feature<NoneFeatureConfiguration> KELP = register("kelp", new KelpFeature(NoneFeatureConfiguration.CODEC));
/*  75 */   public static final Feature<NoneFeatureConfiguration> CORAL_TREE = register("coral_tree", new CoralTreeFeature(NoneFeatureConfiguration.CODEC));
/*  76 */   public static final Feature<NoneFeatureConfiguration> CORAL_MUSHROOM = register("coral_mushroom", new CoralMushroomFeature(NoneFeatureConfiguration.CODEC));
/*  77 */   public static final Feature<NoneFeatureConfiguration> CORAL_CLAW = register("coral_claw", new CoralClawFeature(NoneFeatureConfiguration.CODEC));
/*  78 */   public static final Feature<CountConfiguration> SEA_PICKLE = register("sea_pickle", new SeaPickleFeature(CountConfiguration.CODEC));
/*  79 */   public static final Feature<SimpleBlockConfiguration> SIMPLE_BLOCK = register("simple_block", new SimpleBlockFeature(SimpleBlockConfiguration.CODEC));
/*  80 */   public static final Feature<ProbabilityFeatureConfiguration> BAMBOO = register("bamboo", new BambooFeature(ProbabilityFeatureConfiguration.CODEC));
/*     */   
/*  82 */   public static final Feature<HugeFungusConfiguration> HUGE_FUNGUS = register("huge_fungus", new HugeFungusFeature(HugeFungusConfiguration.CODEC));
/*  83 */   public static final Feature<BlockPileConfiguration> NETHER_FOREST_VEGETATION = register("nether_forest_vegetation", new NetherForestVegetationFeature(BlockPileConfiguration.CODEC));
/*  84 */   public static final Feature<NoneFeatureConfiguration> WEEPING_VINES = register("weeping_vines", new WeepingVinesFeature(NoneFeatureConfiguration.CODEC));
/*  85 */   public static final Feature<NoneFeatureConfiguration> TWISTING_VINES = register("twisting_vines", new TwistingVinesFeature(NoneFeatureConfiguration.CODEC));
/*     */   
/*  87 */   public static final Feature<ColumnFeatureConfiguration> BASALT_COLUMNS = register("basalt_columns", new BasaltColumnsFeature(ColumnFeatureConfiguration.CODEC));
/*  88 */   public static final Feature<DeltaFeatureConfiguration> DELTA_FEATURE = register("delta_feature", new DeltaFeature(DeltaFeatureConfiguration.CODEC));
/*  89 */   public static final Feature<ReplaceSphereConfiguration> REPLACE_BLOBS = register("netherrack_replace_blobs", new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC));
/*     */   
/*  91 */   public static final Feature<LayerConfiguration> FILL_LAYER = register("fill_layer", new FillLayerFeature(LayerConfiguration.CODEC));
/*  92 */   public static final BonusChestFeature BONUS_CHEST = register("bonus_chest", new BonusChestFeature(NoneFeatureConfiguration.CODEC));
/*  93 */   public static final Feature<NoneFeatureConfiguration> BASALT_PILLAR = register("basalt_pillar", new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));
/*  94 */   public static final Feature<OreConfiguration> NO_SURFACE_ORE = register("no_surface_ore", new NoSurfaceOreFeature(OreConfiguration.CODEC));
/*     */   
/*  96 */   public static final Feature<RandomFeatureConfiguration> RANDOM_SELECTOR = register("random_selector", new RandomSelectorFeature(RandomFeatureConfiguration.CODEC));
/*  97 */   public static final Feature<SimpleRandomFeatureConfiguration> SIMPLE_RANDOM_SELECTOR = register("simple_random_selector", new SimpleRandomSelectorFeature(SimpleRandomFeatureConfiguration.CODEC));
/*  98 */   public static final Feature<RandomBooleanFeatureConfiguration> RANDOM_BOOLEAN_SELECTOR = register("random_boolean_selector", new RandomBooleanSelectorFeature(RandomBooleanFeatureConfiguration.CODEC));
/*  99 */   public static final Feature<DecoratedFeatureConfiguration> DECORATED = register("decorated", new DecoratedFeature(DecoratedFeatureConfiguration.CODEC));
/*     */   
/*     */   private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String debug0, F debug1) {
/* 102 */     return (F)Registry.register(Registry.FEATURE, debug0, debug1);
/*     */   }
/*     */   
/*     */   private final Codec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;
/*     */   
/*     */   public Feature(Codec<FC> debug1) {
/* 108 */     this.configuredCodec = debug1.fieldOf("config").xmap(debug1 -> new ConfiguredFeature<>(this, debug1), debug0 -> debug0.config).codec();
/*     */   }
/*     */   
/*     */   public Codec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
/* 112 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   public ConfiguredFeature<FC, ?> configured(FC debug1) {
/* 116 */     return new ConfiguredFeature<>(this, debug1);
/*     */   }
/*     */   
/*     */   protected void setBlock(LevelWriter debug1, BlockPos debug2, BlockState debug3) {
/* 120 */     debug1.setBlock(debug2, debug3, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isStone(Block debug0) {
/* 126 */     return (debug0 == Blocks.STONE || debug0 == Blocks.GRANITE || debug0 == Blocks.DIORITE || debug0 == Blocks.ANDESITE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDirt(Block debug0) {
/* 133 */     return (debug0 == Blocks.DIRT || debug0 == Blocks.GRASS_BLOCK || debug0 == Blocks.PODZOL || debug0 == Blocks.COARSE_DIRT || debug0 == Blocks.MYCELIUM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isGrassOrDirt(LevelSimulatedReader debug0, BlockPos debug1) {
/* 141 */     return debug0.isStateAtPosition(debug1, debug0 -> isDirt(debug0.getBlock()));
/*     */   }
/*     */   
/*     */   public static boolean isAir(LevelSimulatedReader debug0, BlockPos debug1) {
/* 145 */     return debug0.isStateAtPosition(debug1, BlockBehaviour.BlockStateBase::isAir);
/*     */   }
/*     */   
/*     */   public abstract boolean place(WorldGenLevel paramWorldGenLevel, ChunkGenerator paramChunkGenerator, Random paramRandom, BlockPos paramBlockPos, FC paramFC);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */