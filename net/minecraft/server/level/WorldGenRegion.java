/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class WorldGenRegion
/*     */   implements WorldGenLevel
/*     */ {
/*  52 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final List<ChunkAccess> cache;
/*     */   private final int x;
/*     */   private final int z;
/*     */   private final int size;
/*     */   private final ServerLevel level;
/*     */   private final long seed;
/*     */   private final LevelData levelData;
/*     */   
/*     */   public WorldGenRegion(ServerLevel debug1, List<ChunkAccess> debug2) {
/*  63 */     this.blockTicks = new WorldGenTickList<>(debug1 -> getChunk(debug1).getBlockTicks());
/*  64 */     this.liquidTicks = new WorldGenTickList<>(debug1 -> getChunk(debug1).getLiquidTicks());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     int debug3 = Mth.floor(Math.sqrt(debug2.size()));
/*  71 */     if (debug3 * debug3 != debug2.size()) {
/*  72 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Cache size is not a square."));
/*     */     }
/*  74 */     ChunkPos debug4 = ((ChunkAccess)debug2.get(debug2.size() / 2)).getPos();
/*     */     
/*  76 */     this.cache = debug2;
/*  77 */     this.x = debug4.x;
/*  78 */     this.z = debug4.z;
/*  79 */     this.size = debug3;
/*  80 */     this.level = debug1;
/*  81 */     this.seed = debug1.getSeed();
/*  82 */     this.levelData = debug1.getLevelData();
/*  83 */     this.random = debug1.getRandom();
/*  84 */     this.dimensionType = debug1.dimensionType();
/*  85 */     this.biomeManager = new BiomeManager((BiomeManager.NoiseBiomeSource)this, BiomeManager.obfuscateSeed(this.seed), debug1.dimensionType().getBiomeZoomer());
/*  86 */     this.firstPos = ((ChunkAccess)debug2.get(0)).getPos();
/*  87 */     this.lastPos = ((ChunkAccess)debug2.get(debug2.size() - 1)).getPos();
/*     */   }
/*     */   private final Random random; private final DimensionType dimensionType; private final TickList<Block> blockTicks; private final TickList<Fluid> liquidTicks; private final BiomeManager biomeManager; private final ChunkPos firstPos; private final ChunkPos lastPos;
/*     */   public int getCenterX() {
/*  91 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getCenterZ() {
/*  95 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int debug1, int debug2) {
/* 100 */     return getChunk(debug1, debug2, ChunkStatus.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ChunkAccess getChunk(int debug1, int debug2, ChunkStatus debug3, boolean debug4) {
/*     */     ChunkAccess debug5;
/* 107 */     if (hasChunk(debug1, debug2)) {
/* 108 */       int debug6 = debug1 - this.firstPos.x;
/* 109 */       int debug7 = debug2 - this.firstPos.z;
/* 110 */       debug5 = this.cache.get(debug6 + debug7 * this.size);
/* 111 */       if (debug5.getStatus().isOrAfter(debug3)) {
/* 112 */         return debug5;
/*     */       }
/*     */     } else {
/* 115 */       debug5 = null;
/*     */     } 
/*     */     
/* 118 */     if (!debug4) {
/* 119 */       return null;
/*     */     }
/*     */     
/* 122 */     LOGGER.error("Requested chunk : {} {}", Integer.valueOf(debug1), Integer.valueOf(debug2));
/* 123 */     LOGGER.error("Region bounds : {} {} | {} {}", Integer.valueOf(this.firstPos.x), Integer.valueOf(this.firstPos.z), Integer.valueOf(this.lastPos.x), Integer.valueOf(this.lastPos.z));
/* 124 */     if (debug5 != null) {
/* 125 */       throw (RuntimeException)Util.pauseInIde(new RuntimeException(String.format("Chunk is not of correct status. Expecting %s, got %s | %s %s", new Object[] { debug3, debug5.getStatus(), Integer.valueOf(debug1), Integer.valueOf(debug2) })));
/*     */     }
/* 127 */     throw (RuntimeException)Util.pauseInIde(new RuntimeException(String.format("We are asking a region for a chunk out of bound | %s %s", new Object[] { Integer.valueOf(debug1), Integer.valueOf(debug2) })));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int debug1, int debug2) {
/* 133 */     return (debug1 >= this.firstPos.x && debug1 <= this.lastPos.x && debug2 >= this.firstPos.z && debug2 <= this.lastPos.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos debug1) {
/* 138 */     return getChunk(debug1.getX() >> 4, debug1.getZ() >> 4).getBlockState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/* 143 */     return getChunk(debug1).getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Player getNearestPlayer(double debug1, double debug3, double debug5, double debug7, Predicate<Entity> debug9) {
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkyDarken() {
/* 154 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 159 */     return this.biomeManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Biome getUncachedNoiseBiome(int debug1, int debug2, int debug3) {
/* 164 */     return this.level.getUncachedNoiseBiome(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 174 */     return this.level.getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos debug1, boolean debug2, @Nullable Entity debug3, int debug4) {
/* 179 */     BlockState debug5 = getBlockState(debug1);
/* 180 */     if (debug5.isAir()) {
/* 181 */       return false;
/*     */     }
/*     */     
/* 184 */     if (debug2) {
/* 185 */       BlockEntity debug6 = debug5.getBlock().isEntityBlock() ? getBlockEntity(debug1) : null;
/* 186 */       Block.dropResources(debug5, this.level, debug1, debug6, debug3, ItemStack.EMPTY);
/*     */     } 
/* 188 */     return setBlock(debug1, Blocks.AIR.defaultBlockState(), 3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1) {
/* 195 */     ChunkAccess debug2 = getChunk(debug1);
/* 196 */     BlockEntity debug3 = debug2.getBlockEntity(debug1);
/*     */     
/* 198 */     if (debug3 != null) {
/* 199 */       return debug3;
/*     */     }
/*     */     
/* 202 */     CompoundTag debug4 = debug2.getBlockEntityNbt(debug1);
/* 203 */     BlockState debug5 = debug2.getBlockState(debug1);
/* 204 */     if (debug4 != null) {
/* 205 */       if ("DUMMY".equals(debug4.getString("id"))) {
/* 206 */         Block debug6 = debug5.getBlock();
/* 207 */         if (!(debug6 instanceof EntityBlock)) {
/* 208 */           return null;
/*     */         }
/* 210 */         debug3 = ((EntityBlock)debug6).newBlockEntity((BlockGetter)this.level);
/*     */       } else {
/* 212 */         debug3 = BlockEntity.loadStatic(debug5, debug4);
/*     */       } 
/*     */       
/* 215 */       if (debug3 != null) {
/* 216 */         debug2.setBlockEntity(debug1, debug3);
/* 217 */         return debug3;
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     if (debug5.getBlock() instanceof EntityBlock) {
/* 222 */       LOGGER.warn("Tried to access a block entity before it was created. {}", debug1);
/*     */     }
/*     */     
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos debug1, BlockState debug2, int debug3, int debug4) {
/* 230 */     ChunkAccess debug5 = getChunk(debug1);
/* 231 */     BlockState debug6 = debug5.setBlockState(debug1, debug2, false);
/*     */     
/* 233 */     if (debug6 != null) {
/* 234 */       this.level.onBlockStateChange(debug1, debug6, debug2);
/*     */     }
/*     */     
/* 237 */     Block debug7 = debug2.getBlock();
/*     */ 
/*     */     
/* 240 */     if (debug7.isEntityBlock()) {
/* 241 */       if (debug5.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
/* 242 */         debug5.setBlockEntity(debug1, ((EntityBlock)debug7).newBlockEntity((BlockGetter)this));
/*     */       } else {
/* 244 */         CompoundTag debug8 = new CompoundTag();
/* 245 */         debug8.putInt("x", debug1.getX());
/* 246 */         debug8.putInt("y", debug1.getY());
/* 247 */         debug8.putInt("z", debug1.getZ());
/* 248 */         debug8.putString("id", "DUMMY");
/* 249 */         debug5.setBlockEntityNbt(debug8);
/*     */       } 
/* 251 */     } else if (debug6 != null && debug6.getBlock().isEntityBlock()) {
/* 252 */       debug5.removeBlockEntity(debug1);
/*     */     } 
/*     */     
/* 255 */     if (debug2.hasPostProcess((BlockGetter)this, debug1)) {
/* 256 */       markPosForPostprocessing(debug1);
/*     */     }
/*     */     
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   private void markPosForPostprocessing(BlockPos debug1) {
/* 263 */     getChunk(debug1).markPosForPostprocessing(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addFreshEntity(Entity debug1) {
/* 271 */     int debug2 = Mth.floor(debug1.getX() / 16.0D);
/* 272 */     int debug3 = Mth.floor(debug1.getZ() / 16.0D);
/*     */     
/* 274 */     getChunk(debug2, debug3).addEntity(debug1);
/* 275 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeBlock(BlockPos debug1, boolean debug2) {
/* 280 */     return setBlock(debug1, Blocks.AIR.defaultBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/* 285 */     return this.level.getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerLevel getLevel() {
/* 296 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 301 */     return this.level.registryAccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData getLevelData() {
/* 306 */     return this.levelData;
/*     */   }
/*     */ 
/*     */   
/*     */   public DifficultyInstance getCurrentDifficultyAt(BlockPos debug1) {
/* 311 */     if (!hasChunk(debug1.getX() >> 4, debug1.getZ() >> 4)) {
/* 312 */       throw new RuntimeException("We are asking a region for a chunk out of bound");
/*     */     }
/*     */     
/* 315 */     return new DifficultyInstance(this.level.getDifficulty(), this.level.getDayTime(), 0L, this.level.getMoonBrightness());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkSource getChunkSource() {
/* 326 */     return this.level.getChunkSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/* 331 */     return this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickList<Block> getBlockTicks() {
/* 336 */     return this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickList<Fluid> getLiquidTicks() {
/* 341 */     return this.liquidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 346 */     return this.level.getSeaLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   public Random getRandom() {
/* 351 */     return this.random;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/* 356 */     return getChunk(debug2 >> 4, debug3 >> 4).getHeight(debug1, debug2 & 0xF, debug3 & 0xF) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(@Nullable Player debug1, BlockPos debug2, SoundEvent debug3, SoundSource debug4, float debug5, float debug6) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void levelEvent(@Nullable Player debug1, int debug2, BlockPos debug3, int debug4) {}
/*     */ 
/*     */   
/*     */   public DimensionType dimensionType() {
/* 373 */     return this.dimensionType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos debug1, Predicate<BlockState> debug2) {
/* 378 */     return debug2.test(getBlockState(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> debug1, AABB debug2, @Nullable Predicate<? super T> debug3) {
/* 388 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getEntities(@Nullable Entity debug1, AABB debug2, @Nullable Predicate<? super Entity> debug3) {
/* 393 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> players() {
/* 398 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<? extends StructureStart<?>> startsForFeature(SectionPos debug1, StructureFeature<?> debug2) {
/* 403 */     return this.level.startsForFeature(debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\WorldGenRegion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */