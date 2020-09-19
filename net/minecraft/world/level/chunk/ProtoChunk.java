/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ProtoChunk implements ChunkAccess {
/*  41 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ChunkPos chunkPos;
/*     */   
/*     */   private volatile boolean isDirty;
/*     */   
/*     */   @Nullable
/*     */   private ChunkBiomeContainer biomes;
/*     */   
/*     */   @Nullable
/*     */   private volatile LevelLightEngine lightEngine;
/*     */   
/*  53 */   private final Map<Heightmap.Types, Heightmap> heightmaps = Maps.newEnumMap(Heightmap.Types.class);
/*     */   
/*  55 */   private volatile ChunkStatus status = ChunkStatus.EMPTY;
/*  56 */   private final Map<BlockPos, BlockEntity> blockEntities = Maps.newHashMap();
/*  57 */   private final Map<BlockPos, CompoundTag> blockEntityNbts = Maps.newHashMap();
/*  58 */   private final LevelChunkSection[] sections = new LevelChunkSection[16];
/*  59 */   private final List<CompoundTag> entities = Lists.newArrayList();
/*     */   
/*  61 */   private final List<BlockPos> lights = Lists.newArrayList();
/*  62 */   private final ShortList[] postProcessing = new ShortList[16];
/*     */   
/*  64 */   private final Map<StructureFeature<?>, StructureStart<?>> structureStarts = Maps.newHashMap();
/*  65 */   private final Map<StructureFeature<?>, LongSet> structuresRefences = Maps.newHashMap();
/*     */   
/*     */   private final UpgradeData upgradeData;
/*     */   
/*     */   private final ProtoTickList<Block> blockTicks;
/*     */   private final ProtoTickList<Fluid> liquidTicks;
/*     */   private long inhabitedTime;
/*  72 */   private final Map<GenerationStep.Carving, BitSet> carvingMasks = (Map<GenerationStep.Carving, BitSet>)new Object2ObjectArrayMap();
/*     */   
/*     */   private volatile boolean isLightCorrect;
/*     */   
/*     */   public ProtoChunk(ChunkPos debug1, UpgradeData debug2) {
/*  77 */     this(debug1, debug2, null, new ProtoTickList<>(debug0 -> 
/*     */ 
/*     */ 
/*     */           
/*  81 */           (debug0 == null || debug0.defaultBlockState().isAir()), debug1), new ProtoTickList<>(debug0 -> 
/*  82 */           (debug0 == null || debug0 == Fluids.EMPTY), debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoChunk(ChunkPos debug1, UpgradeData debug2, @Nullable LevelChunkSection[] debug3, ProtoTickList<Block> debug4, ProtoTickList<Fluid> debug5) {
/*  87 */     this.chunkPos = debug1;
/*  88 */     this.upgradeData = debug2;
/*  89 */     this.blockTicks = debug4;
/*  90 */     this.liquidTicks = debug5;
/*  91 */     if (debug3 != null) {
/*  92 */       if (this.sections.length == debug3.length) {
/*  93 */         System.arraycopy(debug3, 0, this.sections, 0, this.sections.length);
/*     */       } else {
/*  95 */         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(debug3.length), Integer.valueOf(this.sections.length));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos debug1) {
/* 102 */     int debug2 = debug1.getY();
/* 103 */     if (Level.isOutsideBuildHeight(debug2)) {
/* 104 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/* 107 */     LevelChunkSection debug3 = getSections()[debug2 >> 4];
/* 108 */     if (LevelChunkSection.isEmpty(debug3)) {
/* 109 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 112 */     return debug3.getBlockState(debug1.getX() & 0xF, debug2 & 0xF, debug1.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/* 117 */     int debug2 = debug1.getY();
/* 118 */     if (Level.isOutsideBuildHeight(debug2)) {
/* 119 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 122 */     LevelChunkSection debug3 = getSections()[debug2 >> 4];
/* 123 */     if (LevelChunkSection.isEmpty(debug3)) {
/* 124 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 127 */     return debug3.getFluidState(debug1.getX() & 0xF, debug2 & 0xF, debug1.getZ() & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<BlockPos> getLights() {
/* 132 */     return this.lights.stream();
/*     */   }
/*     */   
/*     */   public ShortList[] getPackedLights() {
/* 136 */     ShortList[] debug1 = new ShortList[16];
/* 137 */     for (BlockPos debug3 : this.lights) {
/* 138 */       ChunkAccess.getOrCreateOffsetList(debug1, debug3.getY() >> 4).add(packOffsetCoordinates(debug3));
/*     */     }
/* 140 */     return debug1;
/*     */   }
/*     */   
/*     */   public void addLight(short debug1, int debug2) {
/* 144 */     addLight(unpackOffsetCoordinates(debug1, debug2, this.chunkPos));
/*     */   }
/*     */   
/*     */   public void addLight(BlockPos debug1) {
/* 148 */     this.lights.add(debug1.immutable());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState setBlockState(BlockPos debug1, BlockState debug2, boolean debug3) {
/* 154 */     int debug4 = debug1.getX();
/* 155 */     int debug5 = debug1.getY();
/* 156 */     int debug6 = debug1.getZ();
/*     */     
/* 158 */     if (debug5 < 0 || debug5 >= 256) {
/* 159 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/*     */     
/* 162 */     if (this.sections[debug5 >> 4] == LevelChunk.EMPTY_SECTION && debug2.is(Blocks.AIR)) {
/* 163 */       return debug2;
/*     */     }
/*     */     
/* 166 */     if (debug2.getLightEmission() > 0) {
/* 167 */       this.lights.add(new BlockPos((debug4 & 0xF) + getPos().getMinBlockX(), debug5, (debug6 & 0xF) + getPos().getMinBlockZ()));
/*     */     }
/*     */     
/* 170 */     LevelChunkSection debug7 = getOrCreateSection(debug5 >> 4);
/* 171 */     BlockState debug8 = debug7.setBlockState(debug4 & 0xF, debug5 & 0xF, debug6 & 0xF, debug2);
/*     */     
/* 173 */     if (this.status.isOrAfter(ChunkStatus.FEATURES) && 
/* 174 */       debug2 != debug8 && (debug2
/* 175 */       .getLightBlock(this, debug1) != debug8.getLightBlock(this, debug1) || debug2
/* 176 */       .getLightEmission() != debug8.getLightEmission() || debug2
/* 177 */       .useShapeForLightOcclusion() || debug8.useShapeForLightOcclusion())) {
/*     */       
/* 179 */       LevelLightEngine levelLightEngine = getLightEngine();
/* 180 */       levelLightEngine.checkBlock(debug1);
/*     */     } 
/*     */ 
/*     */     
/* 184 */     EnumSet<Heightmap.Types> debug9 = getStatus().heightmapsAfter();
/* 185 */     EnumSet<Heightmap.Types> debug10 = null;
/*     */     
/* 187 */     for (Heightmap.Types debug12 : debug9) {
/* 188 */       Heightmap debug13 = this.heightmaps.get(debug12);
/* 189 */       if (debug13 == null) {
/* 190 */         if (debug10 == null) {
/* 191 */           debug10 = EnumSet.noneOf(Heightmap.Types.class);
/*     */         }
/* 193 */         debug10.add(debug12);
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     if (debug10 != null) {
/* 198 */       Heightmap.primeHeightmaps(this, debug10);
/*     */     }
/*     */     
/* 201 */     for (Heightmap.Types debug12 : debug9) {
/* 202 */       ((Heightmap)this.heightmaps.get(debug12)).update(debug4 & 0xF, debug5, debug6 & 0xF, debug2);
/*     */     }
/*     */     
/* 205 */     return debug8;
/*     */   }
/*     */   
/*     */   public LevelChunkSection getOrCreateSection(int debug1) {
/* 209 */     if (this.sections[debug1] == LevelChunk.EMPTY_SECTION) {
/* 210 */       this.sections[debug1] = new LevelChunkSection(debug1 << 4);
/*     */     }
/*     */     
/* 213 */     return this.sections[debug1];
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockPos debug1, BlockEntity debug2) {
/* 218 */     debug2.setPosition(debug1);
/* 219 */     this.blockEntities.put(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<BlockPos> getBlockEntitiesPos() {
/* 224 */     Set<BlockPos> debug1 = Sets.newHashSet(this.blockEntityNbts.keySet());
/* 225 */     debug1.addAll(this.blockEntities.keySet());
/* 226 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1) {
/* 232 */     return this.blockEntities.get(debug1);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 236 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public void addEntity(CompoundTag debug1) {
/* 240 */     this.entities.add(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntity(Entity debug1) {
/* 245 */     if (debug1.isPassenger()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     CompoundTag debug2 = new CompoundTag();
/* 252 */     debug1.save(debug2);
/* 253 */     addEntity(debug2);
/*     */   }
/*     */   
/*     */   public List<CompoundTag> getEntities() {
/* 257 */     return this.entities;
/*     */   }
/*     */   
/*     */   public void setBiomes(ChunkBiomeContainer debug1) {
/* 261 */     this.biomes = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ChunkBiomeContainer getBiomes() {
/* 267 */     return this.biomes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnsaved(boolean debug1) {
/* 272 */     this.isDirty = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnsaved() {
/* 277 */     return this.isDirty;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getStatus() {
/* 282 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(ChunkStatus debug1) {
/* 286 */     this.status = debug1;
/* 287 */     setUnsaved(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/* 292 */     return this.sections;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelLightEngine getLightEngine() {
/* 298 */     return this.lightEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
/* 303 */     return Collections.unmodifiableSet(this.heightmaps.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightmap(Heightmap.Types debug1, long[] debug2) {
/* 308 */     getOrCreateHeightmapUnprimed(debug1).setRawData(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types debug1) {
/* 313 */     return this.heightmaps.computeIfAbsent(debug1, debug1 -> new Heightmap(this, debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/* 318 */     Heightmap debug4 = this.heightmaps.get(debug1);
/* 319 */     if (debug4 == null) {
/* 320 */       Heightmap.primeHeightmaps(this, EnumSet.of(debug1));
/* 321 */       debug4 = this.heightmaps.get(debug1);
/*     */     } 
/* 323 */     return debug4.getFirstAvailable(debug2 & 0xF, debug3 & 0xF) - 1;
/*     */   }
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
/*     */   public ChunkPos getPos() {
/* 344 */     return this.chunkPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastSaveTime(long debug1) {}
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public StructureStart<?> getStartForFeature(StructureFeature<?> debug1) {
/* 354 */     return this.structureStarts.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStartForFeature(StructureFeature<?> debug1, StructureStart<?> debug2) {
/* 359 */     this.structureStarts.put(debug1, debug2);
/* 360 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
/* 365 */     return Collections.unmodifiableMap(this.structureStarts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<StructureFeature<?>, StructureStart<?>> debug1) {
/* 370 */     this.structureStarts.clear();
/* 371 */     this.structureStarts.putAll(debug1);
/* 372 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForFeature(StructureFeature<?> debug1) {
/* 377 */     return this.structuresRefences.computeIfAbsent(debug1, debug0 -> new LongOpenHashSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addReferenceForFeature(StructureFeature<?> debug1, long debug2) {
/* 382 */     ((LongSet)this.structuresRefences.computeIfAbsent(debug1, debug0 -> new LongOpenHashSet())).add(debug2);
/* 383 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, LongSet> getAllReferences() {
/* 388 */     return Collections.unmodifiableMap(this.structuresRefences);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<StructureFeature<?>, LongSet> debug1) {
/* 393 */     this.structuresRefences.clear();
/* 394 */     this.structuresRefences.putAll(debug1);
/* 395 */     this.isDirty = true;
/*     */   }
/*     */   
/*     */   public static short packOffsetCoordinates(BlockPos debug0) {
/* 399 */     int debug1 = debug0.getX();
/* 400 */     int debug2 = debug0.getY();
/* 401 */     int debug3 = debug0.getZ();
/* 402 */     int debug4 = debug1 & 0xF;
/* 403 */     int debug5 = debug2 & 0xF;
/* 404 */     int debug6 = debug3 & 0xF;
/* 405 */     return (short)(debug4 | debug5 << 4 | debug6 << 8);
/*     */   }
/*     */   
/*     */   public static BlockPos unpackOffsetCoordinates(short debug0, int debug1, ChunkPos debug2) {
/* 409 */     int debug3 = (debug0 & 0xF) + (debug2.x << 4);
/* 410 */     int debug4 = (debug0 >>> 4 & 0xF) + (debug1 << 4);
/* 411 */     int debug5 = (debug0 >>> 8 & 0xF) + (debug2.z << 4);
/* 412 */     return new BlockPos(debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos debug1) {
/* 417 */     if (!Level.isOutsideBuildHeight(debug1)) {
/* 418 */       ChunkAccess.getOrCreateOffsetList(this.postProcessing, debug1.getY() >> 4).add(packOffsetCoordinates(debug1));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortList[] getPostProcessing() {
/* 424 */     return this.postProcessing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPackedPostProcess(short debug1, int debug2) {
/* 429 */     ChunkAccess.getOrCreateOffsetList(this.postProcessing, debug2).add(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoTickList<Block> getBlockTicks() {
/* 434 */     return this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtoTickList<Fluid> getLiquidTicks() {
/* 439 */     return this.liquidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpgradeData getUpgradeData() {
/* 444 */     return this.upgradeData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInhabitedTime(long debug1) {
/* 449 */     this.inhabitedTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getInhabitedTime() {
/* 454 */     return this.inhabitedTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag debug1) {
/* 459 */     this.blockEntityNbts.put(new BlockPos(debug1.getInt("x"), debug1.getInt("y"), debug1.getInt("z")), debug1);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, CompoundTag> getBlockEntityNbts() {
/* 463 */     return Collections.unmodifiableMap(this.blockEntityNbts);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbt(BlockPos debug1) {
/* 468 */     return this.blockEntityNbts.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos debug1) {
/* 474 */     BlockEntity debug2 = getBlockEntity(debug1);
/* 475 */     if (debug2 != null) {
/* 476 */       return debug2.save(new CompoundTag());
/*     */     }
/* 478 */     return this.blockEntityNbts.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos debug1) {
/* 483 */     this.blockEntities.remove(debug1);
/* 484 */     this.blockEntityNbts.remove(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BitSet getCarvingMask(GenerationStep.Carving debug1) {
/* 489 */     return this.carvingMasks.get(debug1);
/*     */   }
/*     */   
/*     */   public BitSet getOrCreateCarvingMask(GenerationStep.Carving debug1) {
/* 493 */     return this.carvingMasks.computeIfAbsent(debug1, debug0 -> new BitSet(65536));
/*     */   }
/*     */   
/*     */   public void setCarvingMask(GenerationStep.Carving debug1, BitSet debug2) {
/* 497 */     this.carvingMasks.put(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void setLightEngine(LevelLightEngine debug1) {
/* 501 */     this.lightEngine = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLightCorrect() {
/* 506 */     return this.isLightCorrect;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean debug1) {
/* 511 */     this.isLightCorrect = debug1;
/* 512 */     setUnsaved(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\ProtoChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */