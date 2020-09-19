/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.RegistryLookupCodec;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ 
/*     */ public class DebugLevelSource
/*     */   extends ChunkGenerator
/*     */ {
/*  32 */   public static final Codec<DebugLevelSource> CODEC = RegistryLookupCodec.create(Registry.BIOME_REGISTRY).xmap(DebugLevelSource::new, DebugLevelSource::biomes).stable().codec(); private static final List<BlockState> ALL_BLOCKS;
/*     */   
/*     */   static {
/*  35 */     ALL_BLOCKS = (List<BlockState>)StreamSupport.stream(Registry.BLOCK.spliterator(), false).flatMap(debug0 -> debug0.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toList());
/*  36 */   } private static final int GRID_WIDTH = Mth.ceil(Mth.sqrt(ALL_BLOCKS.size()));
/*  37 */   private static final int GRID_HEIGHT = Mth.ceil(ALL_BLOCKS.size() / GRID_WIDTH);
/*     */   
/*  39 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  40 */   protected static final BlockState BARRIER = Blocks.BARRIER.defaultBlockState();
/*     */   
/*     */   private final Registry<Biome> biomes;
/*     */   
/*     */   public DebugLevelSource(Registry<Biome> debug1) {
/*  45 */     super((BiomeSource)new FixedBiomeSource((Biome)debug1.getOrThrow(Biomes.PLAINS)), new StructureSettings(false));
/*  46 */     this.biomes = debug1;
/*     */   }
/*     */   
/*     */   public Registry<Biome> biomes() {
/*  50 */     return this.biomes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends ChunkGenerator> codec() {
/*  55 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurfaceAndBedrock(WorldGenRegion debug1, ChunkAccess debug2) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyCarvers(long debug1, BiomeManager debug3, ChunkAccess debug4, GenerationStep.Carving debug5) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenRegion debug1, StructureFeatureManager debug2) {
/*  73 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/*     */     
/*  75 */     int debug4 = debug1.getCenterX();
/*  76 */     int debug5 = debug1.getCenterZ();
/*     */     
/*  78 */     for (int debug6 = 0; debug6 < 16; debug6++) {
/*  79 */       for (int debug7 = 0; debug7 < 16; debug7++) {
/*  80 */         int debug8 = (debug4 << 4) + debug6;
/*  81 */         int debug9 = (debug5 << 4) + debug7;
/*  82 */         debug1.setBlock((BlockPos)debug3.set(debug8, 60, debug9), BARRIER, 2);
/*  83 */         BlockState debug10 = getBlockStateFor(debug8, debug9);
/*  84 */         if (debug10 != null) {
/*  85 */           debug1.setBlock((BlockPos)debug3.set(debug8, 70, debug9), debug10, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillFromNoise(LevelAccessor debug1, StructureFeatureManager debug2, ChunkAccess debug3) {}
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int debug1, int debug2, Heightmap.Types debug3) {
/*  97 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getBaseColumn(int debug1, int debug2) {
/* 102 */     return (BlockGetter)new NoiseColumn(new BlockState[0]);
/*     */   }
/*     */   
/*     */   public static BlockState getBlockStateFor(int debug0, int debug1) {
/* 106 */     BlockState debug2 = AIR;
/*     */     
/* 108 */     if (debug0 > 0 && debug1 > 0 && debug0 % 2 != 0 && debug1 % 2 != 0) {
/* 109 */       debug0 /= 2;
/* 110 */       debug1 /= 2;
/*     */       
/* 112 */       if (debug0 <= GRID_WIDTH && debug1 <= GRID_HEIGHT) {
/* 113 */         int debug3 = Mth.abs(debug0 * GRID_WIDTH + debug1);
/* 114 */         if (debug3 < ALL_BLOCKS.size()) {
/* 115 */           debug2 = ALL_BLOCKS.get(debug3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\DebugLevelSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */