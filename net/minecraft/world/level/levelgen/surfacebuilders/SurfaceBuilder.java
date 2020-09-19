/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public abstract class SurfaceBuilder<C extends SurfaceBuilderConfiguration>
/*    */ {
/* 13 */   private static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
/* 14 */   private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
/* 15 */   private static final BlockState PODZOL = Blocks.PODZOL.defaultBlockState();
/* 16 */   private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/* 17 */   private static final BlockState STONE = Blocks.STONE.defaultBlockState();
/* 18 */   private static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
/* 19 */   private static final BlockState SAND = Blocks.SAND.defaultBlockState();
/* 20 */   private static final BlockState RED_SAND = Blocks.RED_SAND.defaultBlockState();
/* 21 */   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
/* 22 */   private static final BlockState MYCELIUM = Blocks.MYCELIUM.defaultBlockState();
/* 23 */   private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
/* 24 */   private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
/* 25 */   private static final BlockState ENDSTONE = Blocks.END_STONE.defaultBlockState();
/* 26 */   private static final BlockState CRIMSON_NYLIUM = Blocks.CRIMSON_NYLIUM.defaultBlockState();
/* 27 */   private static final BlockState WARPED_NYLIUM = Blocks.WARPED_NYLIUM.defaultBlockState();
/* 28 */   private static final BlockState NETHER_WART_BLOCK = Blocks.NETHER_WART_BLOCK.defaultBlockState();
/* 29 */   private static final BlockState WARPED_WART_BLOCK = Blocks.WARPED_WART_BLOCK.defaultBlockState();
/* 30 */   private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
/* 31 */   private static final BlockState BASALT = Blocks.BASALT.defaultBlockState();
/* 32 */   private static final BlockState MAGMA = Blocks.MAGMA_BLOCK.defaultBlockState();
/*    */   
/* 34 */   public static final SurfaceBuilderBaseConfiguration CONFIG_PODZOL = new SurfaceBuilderBaseConfiguration(PODZOL, DIRT, GRAVEL);
/* 35 */   public static final SurfaceBuilderBaseConfiguration CONFIG_GRAVEL = new SurfaceBuilderBaseConfiguration(GRAVEL, GRAVEL, GRAVEL);
/* 36 */   public static final SurfaceBuilderBaseConfiguration CONFIG_GRASS = new SurfaceBuilderBaseConfiguration(GRASS_BLOCK, DIRT, GRAVEL);
/* 37 */   public static final SurfaceBuilderBaseConfiguration CONFIG_STONE = new SurfaceBuilderBaseConfiguration(STONE, STONE, GRAVEL);
/* 38 */   public static final SurfaceBuilderBaseConfiguration CONFIG_COARSE_DIRT = new SurfaceBuilderBaseConfiguration(COARSE_DIRT, DIRT, GRAVEL);
/* 39 */   public static final SurfaceBuilderBaseConfiguration CONFIG_DESERT = new SurfaceBuilderBaseConfiguration(SAND, SAND, GRAVEL);
/* 40 */   public static final SurfaceBuilderBaseConfiguration CONFIG_OCEAN_SAND = new SurfaceBuilderBaseConfiguration(GRASS_BLOCK, DIRT, SAND);
/* 41 */   public static final SurfaceBuilderBaseConfiguration CONFIG_FULL_SAND = new SurfaceBuilderBaseConfiguration(SAND, SAND, SAND);
/* 42 */   public static final SurfaceBuilderBaseConfiguration CONFIG_BADLANDS = new SurfaceBuilderBaseConfiguration(RED_SAND, WHITE_TERRACOTTA, GRAVEL);
/* 43 */   public static final SurfaceBuilderBaseConfiguration CONFIG_MYCELIUM = new SurfaceBuilderBaseConfiguration(MYCELIUM, DIRT, GRAVEL);
/* 44 */   public static final SurfaceBuilderBaseConfiguration CONFIG_HELL = new SurfaceBuilderBaseConfiguration(NETHERRACK, NETHERRACK, NETHERRACK);
/* 45 */   public static final SurfaceBuilderBaseConfiguration CONFIG_SOUL_SAND_VALLEY = new SurfaceBuilderBaseConfiguration(SOUL_SAND, SOUL_SAND, SOUL_SAND);
/* 46 */   public static final SurfaceBuilderBaseConfiguration CONFIG_THEEND = new SurfaceBuilderBaseConfiguration(ENDSTONE, ENDSTONE, ENDSTONE);
/* 47 */   public static final SurfaceBuilderBaseConfiguration CONFIG_CRIMSON_FOREST = new SurfaceBuilderBaseConfiguration(CRIMSON_NYLIUM, NETHERRACK, NETHER_WART_BLOCK);
/* 48 */   public static final SurfaceBuilderBaseConfiguration CONFIG_WARPED_FOREST = new SurfaceBuilderBaseConfiguration(WARPED_NYLIUM, NETHERRACK, WARPED_WART_BLOCK);
/* 49 */   public static final SurfaceBuilderBaseConfiguration CONFIG_BASALT_DELTAS = new SurfaceBuilderBaseConfiguration(BLACKSTONE, BASALT, MAGMA);
/*    */   
/* 51 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> DEFAULT = register("default", new DefaultSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 52 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> MOUNTAIN = register("mountain", new MountainSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 53 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SHATTERED_SAVANNA = register("shattered_savanna", new ShatteredSavanaSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 54 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> GRAVELLY_MOUNTAIN = register("gravelly_mountain", new GravellyMountainSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 55 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> GIANT_TREE_TAIGA = register("giant_tree_taiga", new GiantTreeTaigaSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 56 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SWAMP = register("swamp", new SwampSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 57 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> BADLANDS = register("badlands", new BadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 58 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> WOODED_BADLANDS = register("wooded_badlands", new WoodedBadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 59 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> ERODED_BADLANDS = register("eroded_badlands", new ErodedBadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 60 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> FROZEN_OCEAN = register("frozen_ocean", new FrozenOceanSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 61 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER = register("nether", new NetherSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 62 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER_FOREST = register("nether_forest", new NetherForestSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 63 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SOUL_SAND_VALLEY = register("soul_sand_valley", new SoulSandValleySurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 64 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> BASALT_DELTAS = register("basalt_deltas", new BasaltDeltasSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/* 65 */   public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NOPE = register("nope", new NopeSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
/*    */   
/*    */   private static <C extends SurfaceBuilderConfiguration, F extends SurfaceBuilder<C>> F register(String debug0, F debug1) {
/* 68 */     return (F)Registry.register(Registry.SURFACE_BUILDER, debug0, debug1);
/*    */   }
/*    */   
/*    */   private final Codec<ConfiguredSurfaceBuilder<C>> configuredCodec;
/*    */   
/*    */   public SurfaceBuilder(Codec<C> debug1) {
/* 74 */     this.configuredCodec = debug1.fieldOf("config").xmap(this::configured, ConfiguredSurfaceBuilder::config).codec();
/*    */   }
/*    */   
/*    */   public Codec<ConfiguredSurfaceBuilder<C>> configuredCodec() {
/* 78 */     return this.configuredCodec;
/*    */   }
/*    */   
/*    */   public ConfiguredSurfaceBuilder<C> configured(C debug1) {
/* 82 */     return new ConfiguredSurfaceBuilder<>(this, debug1);
/*    */   }
/*    */   
/*    */   public abstract void apply(Random paramRandom, ChunkAccess paramChunkAccess, Biome paramBiome, int paramInt1, int paramInt2, int paramInt3, double paramDouble, BlockState paramBlockState1, BlockState paramBlockState2, int paramInt4, long paramLong, C paramC);
/*    */   
/*    */   public void initNoise(long debug1) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\SurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */