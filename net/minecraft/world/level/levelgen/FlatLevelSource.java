/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ 
/*     */ public class FlatLevelSource extends ChunkGenerator {
/*  21 */   public static final Codec<FlatLevelSource> CODEC = FlatLevelGeneratorSettings.CODEC.fieldOf("settings").xmap(FlatLevelSource::new, FlatLevelSource::settings).codec();
/*     */   
/*     */   private final FlatLevelGeneratorSettings settings;
/*     */   
/*     */   public FlatLevelSource(FlatLevelGeneratorSettings debug1) {
/*  26 */     super((BiomeSource)new FixedBiomeSource(debug1.getBiomeFromSettings()), (BiomeSource)new FixedBiomeSource(debug1.getBiome()), debug1.structureSettings(), 0L);
/*  27 */     this.settings = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<? extends ChunkGenerator> codec() {
/*  32 */     return (Codec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlatLevelGeneratorSettings settings() {
/*  41 */     return this.settings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurfaceAndBedrock(WorldGenRegion debug1, ChunkAccess debug2) {}
/*     */ 
/*     */   
/*     */   public int getSpawnHeight() {
/*  50 */     BlockState[] debug1 = this.settings.getLayers();
/*  51 */     for (int debug2 = 0; debug2 < debug1.length; debug2++) {
/*  52 */       BlockState debug3 = (debug1[debug2] == null) ? Blocks.AIR.defaultBlockState() : debug1[debug2];
/*  53 */       if (!Heightmap.Types.MOTION_BLOCKING.isOpaque().test(debug3)) {
/*  54 */         return debug2 - 1;
/*     */       }
/*     */     } 
/*  57 */     return debug1.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillFromNoise(LevelAccessor debug1, StructureFeatureManager debug2, ChunkAccess debug3) {
/*  64 */     BlockState[] debug4 = this.settings.getLayers();
/*     */     
/*  66 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*  67 */     Heightmap debug6 = debug3.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/*  68 */     Heightmap debug7 = debug3.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/*  70 */     for (int debug8 = 0; debug8 < debug4.length; debug8++) {
/*  71 */       BlockState debug9 = debug4[debug8];
/*  72 */       if (debug9 != null)
/*     */       {
/*     */ 
/*     */         
/*  76 */         for (int debug10 = 0; debug10 < 16; debug10++) {
/*  77 */           for (int debug11 = 0; debug11 < 16; debug11++) {
/*  78 */             debug3.setBlockState((BlockPos)debug5.set(debug10, debug8, debug11), debug9, false);
/*  79 */             debug6.update(debug10, debug8, debug11, debug9);
/*  80 */             debug7.update(debug10, debug8, debug11, debug9);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getBaseHeight(int debug1, int debug2, Heightmap.Types debug3) {
/*  88 */     BlockState[] debug4 = this.settings.getLayers();
/*  89 */     for (int debug5 = debug4.length - 1; debug5 >= 0; debug5--) {
/*  90 */       BlockState debug6 = debug4[debug5];
/*  91 */       if (debug6 != null)
/*     */       {
/*     */         
/*  94 */         if (debug3.isOpaque().test(debug6))
/*  95 */           return debug5 + 1; 
/*     */       }
/*     */     } 
/*  98 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getBaseColumn(int debug1, int debug2) {
/* 103 */     return (BlockGetter)new NoiseColumn((BlockState[])Arrays.<BlockState>stream(this.settings.getLayers()).map(debug0 -> (debug0 == null) ? Blocks.AIR.defaultBlockState() : debug0).toArray(debug0 -> new BlockState[debug0]));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\FlatLevelSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */