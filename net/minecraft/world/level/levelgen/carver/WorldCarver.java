/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class WorldCarver<C extends CarverConfiguration> {
/*  28 */   public static final WorldCarver<ProbabilityFeatureConfiguration> CAVE = register("cave", new CaveWorldCarver(ProbabilityFeatureConfiguration.CODEC, 256));
/*  29 */   public static final WorldCarver<ProbabilityFeatureConfiguration> NETHER_CAVE = register("nether_cave", new NetherWorldCarver(ProbabilityFeatureConfiguration.CODEC));
/*  30 */   public static final WorldCarver<ProbabilityFeatureConfiguration> CANYON = register("canyon", new CanyonWorldCarver(ProbabilityFeatureConfiguration.CODEC));
/*  31 */   public static final WorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CANYON = register("underwater_canyon", new UnderwaterCanyonWorldCarver(ProbabilityFeatureConfiguration.CODEC));
/*  32 */   public static final WorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CAVE = register("underwater_cave", new UnderwaterCaveWorldCarver(ProbabilityFeatureConfiguration.CODEC));
/*     */   
/*  34 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  35 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*  36 */   protected static final FluidState WATER = Fluids.WATER.defaultFluidState();
/*  37 */   protected static final FluidState LAVA = Fluids.LAVA.defaultFluidState();
/*     */   
/*     */   private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String debug0, F debug1) {
/*  40 */     return (F)Registry.register(Registry.CARVER, debug0, debug1);
/*     */   }
/*     */   
/*  43 */   protected Set<Block> replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.PACKED_ICE });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected Set<Fluid> liquids = (Set<Fluid>)ImmutableSet.of(Fluids.WATER);
/*     */   
/*     */   private final Codec<ConfiguredWorldCarver<C>> configuredCodec;
/*     */   
/*     */   protected final int genHeight;
/*     */ 
/*     */   
/*     */   public WorldCarver(Codec<C> debug1, int debug2) {
/*  84 */     this.genHeight = debug2;
/*  85 */     this.configuredCodec = debug1.fieldOf("config").xmap(this::configured, ConfiguredWorldCarver::config).codec();
/*     */   }
/*     */   
/*     */   public ConfiguredWorldCarver<C> configured(C debug1) {
/*  89 */     return new ConfiguredWorldCarver<>(this, debug1);
/*     */   }
/*     */   
/*     */   public Codec<ConfiguredWorldCarver<C>> configuredCodec() {
/*  93 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   public int getRange() {
/*  97 */     return 4;
/*     */   }
/*     */   
/*     */   protected boolean carveSphere(ChunkAccess debug1, Function<BlockPos, Biome> debug2, long debug3, int debug5, int debug6, int debug7, double debug8, double debug10, double debug12, double debug14, double debug16, BitSet debug18) {
/* 101 */     Random debug19 = new Random(debug3 + debug6 + debug7);
/*     */     
/* 103 */     double debug20 = (debug6 * 16 + 8);
/* 104 */     double debug22 = (debug7 * 16 + 8);
/*     */     
/* 106 */     if (debug8 < debug20 - 16.0D - debug14 * 2.0D || debug12 < debug22 - 16.0D - debug14 * 2.0D || debug8 > debug20 + 16.0D + debug14 * 2.0D || debug12 > debug22 + 16.0D + debug14 * 2.0D) {
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     int debug24 = Math.max(Mth.floor(debug8 - debug14) - debug6 * 16 - 1, 0);
/* 111 */     int debug25 = Math.min(Mth.floor(debug8 + debug14) - debug6 * 16 + 1, 16);
/*     */     
/* 113 */     int debug26 = Math.max(Mth.floor(debug10 - debug16) - 1, 1);
/* 114 */     int debug27 = Math.min(Mth.floor(debug10 + debug16) + 1, this.genHeight - 8);
/*     */     
/* 116 */     int debug28 = Math.max(Mth.floor(debug12 - debug14) - debug7 * 16 - 1, 0);
/* 117 */     int debug29 = Math.min(Mth.floor(debug12 + debug14) - debug7 * 16 + 1, 16);
/*     */     
/* 119 */     if (hasWater(debug1, debug6, debug7, debug24, debug25, debug26, debug27, debug28, debug29)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     boolean debug30 = false;
/* 124 */     BlockPos.MutableBlockPos debug31 = new BlockPos.MutableBlockPos();
/* 125 */     BlockPos.MutableBlockPos debug32 = new BlockPos.MutableBlockPos();
/* 126 */     BlockPos.MutableBlockPos debug33 = new BlockPos.MutableBlockPos();
/*     */     
/* 128 */     for (int debug34 = debug24; debug34 < debug25; debug34++) {
/* 129 */       int debug35 = debug34 + debug6 * 16;
/* 130 */       double debug36 = (debug35 + 0.5D - debug8) / debug14;
/* 131 */       for (int debug38 = debug28; debug38 < debug29; debug38++) {
/* 132 */         int debug39 = debug38 + debug7 * 16;
/* 133 */         double debug40 = (debug39 + 0.5D - debug12) / debug14;
/* 134 */         if (debug36 * debug36 + debug40 * debug40 < 1.0D) {
/*     */ 
/*     */ 
/*     */           
/* 138 */           MutableBoolean debug42 = new MutableBoolean(false);
/*     */           
/* 140 */           for (int debug43 = debug27; debug43 > debug26; debug43--) {
/* 141 */             double debug44 = (debug43 - 0.5D - debug10) / debug16;
/* 142 */             if (!skip(debug36, debug44, debug40, debug43))
/*     */             {
/*     */ 
/*     */               
/* 146 */               debug30 |= carveBlock(debug1, debug2, debug18, debug19, debug31, debug32, debug33, debug5, debug6, debug7, debug35, debug39, debug34, debug43, debug38, debug42); } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 151 */     return debug30;
/*     */   }
/*     */   
/*     */   protected boolean carveBlock(ChunkAccess debug1, Function<BlockPos, Biome> debug2, BitSet debug3, Random debug4, BlockPos.MutableBlockPos debug5, BlockPos.MutableBlockPos debug6, BlockPos.MutableBlockPos debug7, int debug8, int debug9, int debug10, int debug11, int debug12, int debug13, int debug14, int debug15, MutableBoolean debug16) {
/* 155 */     int debug17 = debug13 | debug15 << 4 | debug14 << 8;
/* 156 */     if (debug3.get(debug17)) {
/* 157 */       return false;
/*     */     }
/* 159 */     debug3.set(debug17);
/*     */     
/* 161 */     debug5.set(debug11, debug14, debug12);
/*     */     
/* 163 */     BlockState debug18 = debug1.getBlockState((BlockPos)debug5);
/*     */     
/* 165 */     BlockState debug19 = debug1.getBlockState((BlockPos)debug6.setWithOffset((Vec3i)debug5, Direction.UP));
/* 166 */     if (debug18.is(Blocks.GRASS_BLOCK) || debug18.is(Blocks.MYCELIUM)) {
/* 167 */       debug16.setTrue();
/*     */     }
/* 169 */     if (!canReplaceBlock(debug18, debug19)) {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     if (debug14 < 11) {
/* 174 */       debug1.setBlockState((BlockPos)debug5, LAVA.createLegacyBlock(), false);
/*     */     } else {
/* 176 */       debug1.setBlockState((BlockPos)debug5, CAVE_AIR, false);
/*     */ 
/*     */       
/* 179 */       if (debug16.isTrue()) {
/* 180 */         debug7.setWithOffset((Vec3i)debug5, Direction.DOWN);
/* 181 */         if (debug1.getBlockState((BlockPos)debug7).is(Blocks.DIRT)) {
/* 182 */           debug1.setBlockState((BlockPos)debug7, ((Biome)debug2.apply(debug5)).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(), false);
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   public abstract boolean carve(ChunkAccess paramChunkAccess, Function<BlockPos, Biome> paramFunction, Random paramRandom, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, BitSet paramBitSet, C paramC);
/*     */   
/*     */   public abstract boolean isStartChunk(Random paramRandom, int paramInt1, int paramInt2, C paramC);
/*     */   
/*     */   protected boolean canReplaceBlock(BlockState debug1) {
/* 194 */     return this.replaceableBlocks.contains(debug1.getBlock());
/*     */   }
/*     */   
/*     */   protected boolean canReplaceBlock(BlockState debug1, BlockState debug2) {
/* 198 */     return (canReplaceBlock(debug1) || ((debug1.is(Blocks.SAND) || debug1.is(Blocks.GRAVEL)) && !debug2.getFluidState().is((Tag)FluidTags.WATER)));
/*     */   }
/*     */   
/*     */   protected boolean hasWater(ChunkAccess debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, int debug9) {
/* 202 */     BlockPos.MutableBlockPos debug10 = new BlockPos.MutableBlockPos();
/*     */     
/* 204 */     for (int debug11 = debug4; debug11 < debug5; debug11++) {
/* 205 */       for (int debug12 = debug8; debug12 < debug9; debug12++) {
/* 206 */         for (int debug13 = debug6 - 1; debug13 <= debug7 + 1; debug13++) {
/* 207 */           if (this.liquids.contains(debug1.getFluidState((BlockPos)debug10.set(debug11 + debug2 * 16, debug13, debug12 + debug3 * 16)).getType())) {
/* 208 */             return true;
/*     */           }
/*     */ 
/*     */           
/* 212 */           if (debug13 != debug7 + 1 && !isEdge(debug4, debug5, debug8, debug9, debug11, debug12)) {
/* 213 */             debug13 = debug7;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 218 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isEdge(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 222 */     return (debug5 == debug1 || debug5 == debug2 - 1 || debug6 == debug3 || debug6 == debug4 - 1);
/*     */   }
/*     */   
/*     */   protected boolean canReach(int debug1, int debug2, double debug3, double debug5, int debug7, int debug8, float debug9) {
/* 226 */     double debug10 = (debug1 * 16 + 8);
/* 227 */     double debug12 = (debug2 * 16 + 8);
/*     */     
/* 229 */     double debug14 = debug3 - debug10;
/* 230 */     double debug16 = debug5 - debug12;
/* 231 */     double debug18 = (debug8 - debug7);
/* 232 */     double debug20 = (debug9 + 2.0F + 16.0F);
/*     */     
/* 234 */     return (debug14 * debug14 + debug16 * debug16 - debug18 * debug18 <= debug20 * debug20);
/*     */   }
/*     */   
/*     */   protected abstract boolean skip(double paramDouble1, double paramDouble2, double paramDouble3, int paramInt);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\WorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */