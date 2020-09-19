/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ClassInstanceMultiMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.boss.EnderDragonPart;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ChunkTickList;
/*     */ import net.minecraft.world.level.EmptyTickList;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.TickList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LevelChunk implements ChunkAccess {
/*  59 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  66 */   public static final LevelChunkSection EMPTY_SECTION = null;
/*     */   
/*  68 */   private final LevelChunkSection[] sections = new LevelChunkSection[16];
/*     */   private ChunkBiomeContainer biomes;
/*  70 */   private final Map<BlockPos, CompoundTag> pendingBlockEntities = Maps.newHashMap();
/*     */   private boolean loaded;
/*     */   private final Level level;
/*  73 */   private final Map<Heightmap.Types, Heightmap> heightmaps = Maps.newEnumMap(Heightmap.Types.class);
/*     */   
/*     */   private final UpgradeData upgradeData;
/*     */   
/*  77 */   private final Map<BlockPos, BlockEntity> blockEntities = Maps.newHashMap();
/*     */   
/*     */   private final ClassInstanceMultiMap<Entity>[] entitySections;
/*  80 */   private final Map<StructureFeature<?>, StructureStart<?>> structureStarts = Maps.newHashMap();
/*  81 */   private final Map<StructureFeature<?>, LongSet> structuresRefences = Maps.newHashMap();
/*     */   
/*  83 */   private final ShortList[] postProcessing = new ShortList[16];
/*     */   
/*     */   private TickList<Block> blockTicks;
/*     */   
/*     */   private TickList<Fluid> liquidTicks;
/*     */   private boolean lastSaveHadEntities;
/*     */   private long lastSaveTime;
/*     */   private volatile boolean unsaved;
/*     */   private long inhabitedTime;
/*     */   @Nullable
/*     */   private Supplier<ChunkHolder.FullChunkStatus> fullStatus;
/*     */   @Nullable
/*     */   private Consumer<LevelChunk> postLoad;
/*     */   private final ChunkPos chunkPos;
/*     */   private volatile boolean isLightCorrect;
/*     */   
/*     */   public LevelChunk(Level debug1, ChunkPos debug2, ChunkBiomeContainer debug3) {
/* 100 */     this(debug1, debug2, debug3, UpgradeData.EMPTY, (TickList<Block>)EmptyTickList.empty(), (TickList<Fluid>)EmptyTickList.empty(), 0L, null, null);
/*     */   }
/*     */   
/*     */   public LevelChunk(Level debug1, ChunkPos debug2, ChunkBiomeContainer debug3, UpgradeData debug4, TickList<Block> debug5, TickList<Fluid> debug6, long debug7, @Nullable LevelChunkSection[] debug9, @Nullable Consumer<LevelChunk> debug10) {
/* 104 */     this.entitySections = (ClassInstanceMultiMap<Entity>[])new ClassInstanceMultiMap[16];
/* 105 */     this.level = debug1;
/* 106 */     this.chunkPos = debug2;
/* 107 */     this.upgradeData = debug4;
/*     */     
/* 109 */     for (Heightmap.Types debug14 : Heightmap.Types.values()) {
/* 110 */       if (ChunkStatus.FULL.heightmapsAfter().contains(debug14)) {
/* 111 */         this.heightmaps.put(debug14, new Heightmap(this, debug14));
/*     */       }
/*     */     } 
/*     */     
/* 115 */     for (int debug11 = 0; debug11 < this.entitySections.length; debug11++) {
/* 116 */       this.entitySections[debug11] = new ClassInstanceMultiMap(Entity.class);
/*     */     }
/*     */     
/* 119 */     this.biomes = debug3;
/* 120 */     this.blockTicks = debug5;
/* 121 */     this.liquidTicks = debug6;
/* 122 */     this.inhabitedTime = debug7;
/* 123 */     this.postLoad = debug10;
/* 124 */     if (debug9 != null) {
/* 125 */       if (this.sections.length == debug9.length) {
/* 126 */         System.arraycopy(debug9, 0, this.sections, 0, this.sections.length);
/*     */       } else {
/* 128 */         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", Integer.valueOf(debug9.length), Integer.valueOf(this.sections.length));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public LevelChunk(Level debug1, ProtoChunk debug2) {
/* 134 */     this(debug1, debug2.getPos(), debug2.getBiomes(), debug2.getUpgradeData(), debug2.getBlockTicks(), debug2.getLiquidTicks(), debug2.getInhabitedTime(), debug2.getSections(), null);
/*     */     
/* 136 */     for (CompoundTag debug4 : debug2.getEntities()) {
/* 137 */       EntityType.loadEntityRecursive(debug4, debug1, debug1 -> {
/*     */             addEntity(debug1);
/*     */             
/*     */             return debug1;
/*     */           });
/*     */     } 
/* 143 */     for (BlockEntity debug4 : debug2.getBlockEntities().values()) {
/* 144 */       addBlockEntity(debug4);
/*     */     }
/*     */     
/* 147 */     this.pendingBlockEntities.putAll(debug2.getBlockEntityNbts());
/*     */     
/* 149 */     for (int debug3 = 0; debug3 < (debug2.getPostProcessing()).length; debug3++) {
/* 150 */       this.postProcessing[debug3] = debug2.getPostProcessing()[debug3];
/*     */     }
/*     */     
/* 153 */     setAllStarts(debug2.getAllStarts());
/* 154 */     setAllReferences(debug2.getAllReferences());
/*     */     
/* 156 */     for (Map.Entry<Heightmap.Types, Heightmap> debug4 : debug2.getHeightmaps()) {
/* 157 */       if (ChunkStatus.FULL.heightmapsAfter().contains(debug4.getKey())) {
/* 158 */         getOrCreateHeightmapUnprimed(debug4.getKey()).setRawData(((Heightmap)debug4.getValue()).getRawData());
/*     */       }
/*     */     } 
/*     */     
/* 162 */     setLightCorrect(debug2.isLightCorrect());
/*     */     
/* 164 */     this.unsaved = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types debug1) {
/* 169 */     return this.heightmaps.computeIfAbsent(debug1, debug1 -> new Heightmap(this, debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<BlockPos> getBlockEntitiesPos() {
/* 174 */     Set<BlockPos> debug1 = Sets.newHashSet(this.pendingBlockEntities.keySet());
/* 175 */     debug1.addAll(this.blockEntities.keySet());
/* 176 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/* 181 */     return this.sections;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos debug1) {
/* 186 */     int debug2 = debug1.getX();
/* 187 */     int debug3 = debug1.getY();
/* 188 */     int debug4 = debug1.getZ();
/* 189 */     if (this.level.isDebug()) {
/* 190 */       BlockState debug5 = null;
/* 191 */       if (debug3 == 60) {
/* 192 */         debug5 = Blocks.BARRIER.defaultBlockState();
/*     */       }
/* 194 */       if (debug3 == 70) {
/* 195 */         debug5 = DebugLevelSource.getBlockStateFor(debug2, debug4);
/*     */       }
/* 197 */       return (debug5 == null) ? Blocks.AIR.defaultBlockState() : debug5;
/*     */     } 
/*     */     
/*     */     try {
/* 201 */       if (debug3 >= 0 && debug3 >> 4 < this.sections.length) {
/* 202 */         LevelChunkSection debug5 = this.sections[debug3 >> 4];
/* 203 */         if (!LevelChunkSection.isEmpty(debug5)) {
/* 204 */           return debug5.getBlockState(debug2 & 0xF, debug3 & 0xF, debug4 & 0xF);
/*     */         }
/*     */       } 
/* 207 */       return Blocks.AIR.defaultBlockState();
/* 208 */     } catch (Throwable debug5) {
/* 209 */       CrashReport debug6 = CrashReport.forThrowable(debug5, "Getting block state");
/* 210 */       CrashReportCategory debug7 = debug6.addCategory("Block being got");
/* 211 */       debug7.setDetail("Location", () -> CrashReportCategory.formatLocation(debug0, debug1, debug2));
/* 212 */       throw new ReportedException(debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/* 218 */     return getFluidState(debug1.getX(), debug1.getY(), debug1.getZ());
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(int debug1, int debug2, int debug3) {
/*     */     try {
/* 223 */       if (debug2 >= 0 && debug2 >> 4 < this.sections.length) {
/* 224 */         LevelChunkSection debug4 = this.sections[debug2 >> 4];
/* 225 */         if (!LevelChunkSection.isEmpty(debug4)) {
/* 226 */           return debug4.getFluidState(debug1 & 0xF, debug2 & 0xF, debug3 & 0xF);
/*     */         }
/*     */       } 
/* 229 */       return Fluids.EMPTY.defaultFluidState();
/* 230 */     } catch (Throwable debug4) {
/* 231 */       CrashReport debug5 = CrashReport.forThrowable(debug4, "Getting fluid state");
/* 232 */       CrashReportCategory debug6 = debug5.addCategory("Block being got");
/* 233 */       debug6.setDetail("Location", () -> CrashReportCategory.formatLocation(debug0, debug1, debug2));
/* 234 */       throw new ReportedException(debug5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState setBlockState(BlockPos debug1, BlockState debug2, boolean debug3) {
/* 241 */     int debug4 = debug1.getX() & 0xF;
/* 242 */     int debug5 = debug1.getY();
/* 243 */     int debug6 = debug1.getZ() & 0xF;
/*     */     
/* 245 */     LevelChunkSection debug7 = this.sections[debug5 >> 4];
/* 246 */     if (debug7 == EMPTY_SECTION) {
/*     */       
/* 248 */       if (debug2.isAir()) {
/* 249 */         return null;
/*     */       }
/*     */       
/* 252 */       debug7 = new LevelChunkSection(debug5 >> 4 << 4);
/* 253 */       this.sections[debug5 >> 4] = debug7;
/*     */     } 
/*     */     
/* 256 */     boolean debug8 = debug7.isEmpty();
/* 257 */     BlockState debug9 = debug7.setBlockState(debug4, debug5 & 0xF, debug6, debug2);
/*     */     
/* 259 */     if (debug9 == debug2) {
/* 260 */       return null;
/*     */     }
/*     */     
/* 263 */     Block debug10 = debug2.getBlock();
/* 264 */     Block debug11 = debug9.getBlock();
/*     */     
/* 266 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING)).update(debug4, debug5, debug6, debug2);
/* 267 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)).update(debug4, debug5, debug6, debug2);
/* 268 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR)).update(debug4, debug5, debug6, debug2);
/* 269 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.WORLD_SURFACE)).update(debug4, debug5, debug6, debug2);
/* 270 */     boolean debug12 = debug7.isEmpty();
/* 271 */     if (debug8 != debug12) {
/* 272 */       this.level.getChunkSource().getLightEngine().updateSectionStatus(debug1, debug12);
/*     */     }
/*     */     
/* 275 */     if (!this.level.isClientSide) {
/* 276 */       debug9.onRemove(this.level, debug1, debug2, debug3);
/* 277 */     } else if (debug11 != debug10 && debug11 instanceof EntityBlock) {
/* 278 */       this.level.removeBlockEntity(debug1);
/*     */     } 
/*     */     
/* 281 */     if (!debug7.getBlockState(debug4, debug5 & 0xF, debug6).is(debug10)) {
/* 282 */       return null;
/*     */     }
/*     */     
/* 285 */     if (debug11 instanceof EntityBlock) {
/* 286 */       BlockEntity debug13 = getBlockEntity(debug1, EntityCreationType.CHECK);
/* 287 */       if (debug13 != null) {
/* 288 */         debug13.clearCache();
/*     */       }
/*     */     } 
/*     */     
/* 292 */     if (!this.level.isClientSide) {
/* 293 */       debug2.onPlace(this.level, debug1, debug9, debug3);
/*     */     }
/* 295 */     if (debug10 instanceof EntityBlock) {
/* 296 */       BlockEntity debug13 = getBlockEntity(debug1, EntityCreationType.CHECK);
/* 297 */       if (debug13 == null) {
/* 298 */         debug13 = ((EntityBlock)debug10).newBlockEntity((BlockGetter)this.level);
/* 299 */         this.level.setBlockEntity(debug1, debug13);
/*     */       } else {
/* 301 */         debug13.clearCache();
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     this.unsaved = true;
/* 306 */     return debug9;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelLightEngine getLightEngine() {
/* 312 */     return this.level.getChunkSource().getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntity(Entity debug1) {
/* 317 */     this.lastSaveHadEntities = true;
/*     */     
/* 319 */     int debug2 = Mth.floor(debug1.getX() / 16.0D);
/* 320 */     int debug3 = Mth.floor(debug1.getZ() / 16.0D);
/* 321 */     if (debug2 != this.chunkPos.x || debug3 != this.chunkPos.z) {
/* 322 */       LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", Integer.valueOf(debug2), Integer.valueOf(debug3), Integer.valueOf(this.chunkPos.x), Integer.valueOf(this.chunkPos.z), debug1);
/*     */       
/* 324 */       debug1.removed = true;
/*     */     } 
/* 326 */     int debug4 = Mth.floor(debug1.getY() / 16.0D);
/* 327 */     if (debug4 < 0) {
/* 328 */       debug4 = 0;
/*     */     }
/* 330 */     if (debug4 >= this.entitySections.length) {
/* 331 */       debug4 = this.entitySections.length - 1;
/*     */     }
/* 333 */     debug1.inChunk = true;
/* 334 */     debug1.xChunk = this.chunkPos.x;
/* 335 */     debug1.yChunk = debug4;
/* 336 */     debug1.zChunk = this.chunkPos.z;
/* 337 */     this.entitySections[debug4].add(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightmap(Heightmap.Types debug1, long[] debug2) {
/* 342 */     ((Heightmap)this.heightmaps.get(debug1)).setRawData(debug2);
/*     */   }
/*     */   
/*     */   public void removeEntity(Entity debug1) {
/* 346 */     removeEntity(debug1, debug1.yChunk);
/*     */   }
/*     */   
/*     */   public void removeEntity(Entity debug1, int debug2) {
/* 350 */     if (debug2 < 0) {
/* 351 */       debug2 = 0;
/*     */     }
/* 353 */     if (debug2 >= this.entitySections.length) {
/* 354 */       debug2 = this.entitySections.length - 1;
/*     */     }
/* 356 */     this.entitySections[debug2].remove(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/* 361 */     return ((Heightmap)this.heightmaps.get(debug1)).getFirstAvailable(debug2 & 0xF, debug3 & 0xF) - 1;
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
/*     */   @Nullable
/*     */   private BlockEntity createBlockEntity(BlockPos debug1) {
/* 383 */     BlockState debug2 = getBlockState(debug1);
/* 384 */     Block debug3 = debug2.getBlock();
/* 385 */     if (!debug3.isEntityBlock()) {
/* 386 */       return null;
/*     */     }
/*     */     
/* 389 */     return ((EntityBlock)debug3).newBlockEntity((BlockGetter)this.level);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1) {
/* 395 */     return getBlockEntity(debug1, EntityCreationType.CHECK);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1, EntityCreationType debug2) {
/* 400 */     BlockEntity debug3 = this.blockEntities.get(debug1);
/* 401 */     if (debug3 == null) {
/* 402 */       CompoundTag debug4 = this.pendingBlockEntities.remove(debug1);
/* 403 */       if (debug4 != null) {
/* 404 */         BlockEntity debug5 = promotePendingBlockEntity(debug1, debug4);
/* 405 */         if (debug5 != null) {
/* 406 */           return debug5;
/*     */         }
/*     */       } 
/*     */     } 
/* 410 */     if (debug3 == null) {
/* 411 */       if (debug2 == EntityCreationType.IMMEDIATE) {
/* 412 */         debug3 = createBlockEntity(debug1);
/* 413 */         this.level.setBlockEntity(debug1, debug3);
/*     */       } 
/* 415 */     } else if (debug3.isRemoved()) {
/* 416 */       this.blockEntities.remove(debug1);
/* 417 */       return null;
/*     */     } 
/*     */     
/* 420 */     return debug3;
/*     */   }
/*     */   
/*     */   public void addBlockEntity(BlockEntity debug1) {
/* 424 */     setBlockEntity(debug1.getBlockPos(), debug1);
/* 425 */     if (this.loaded || this.level.isClientSide()) {
/* 426 */       this.level.setBlockEntity(debug1.getBlockPos(), debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockPos debug1, BlockEntity debug2) {
/* 432 */     if (!(getBlockState(debug1).getBlock() instanceof EntityBlock)) {
/*     */       return;
/*     */     }
/*     */     
/* 436 */     debug2.setLevelAndPosition(this.level, debug1);
/* 437 */     debug2.clearRemoved();
/*     */     
/* 439 */     BlockEntity debug3 = this.blockEntities.put(debug1.immutable(), debug2);
/* 440 */     if (debug3 != null && debug3 != debug2) {
/* 441 */       debug3.setRemoved();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag debug1) {
/* 447 */     this.pendingBlockEntities.put(new BlockPos(debug1.getInt("x"), debug1.getInt("y"), debug1.getInt("z")), debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos debug1) {
/* 453 */     BlockEntity debug2 = getBlockEntity(debug1);
/* 454 */     if (debug2 != null && !debug2.isRemoved()) {
/* 455 */       CompoundTag compoundTag = debug2.save(new CompoundTag());
/* 456 */       compoundTag.putBoolean("keepPacked", false);
/* 457 */       return compoundTag;
/*     */     } 
/* 459 */     CompoundTag debug3 = this.pendingBlockEntities.get(debug1);
/* 460 */     if (debug3 != null) {
/* 461 */       debug3 = debug3.copy();
/* 462 */       debug3.putBoolean("keepPacked", true);
/*     */     } 
/* 464 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos debug1) {
/* 469 */     if (this.loaded || this.level.isClientSide()) {
/* 470 */       BlockEntity debug2 = this.blockEntities.remove(debug1);
/* 471 */       if (debug2 != null) {
/* 472 */         debug2.setRemoved();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runPostLoad() {
/* 478 */     if (this.postLoad != null) {
/* 479 */       this.postLoad.accept(this);
/* 480 */       this.postLoad = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void markUnsaved() {
/* 485 */     this.unsaved = true;
/*     */   }
/*     */   
/*     */   public void getEntities(@Nullable Entity debug1, AABB debug2, List<Entity> debug3, @Nullable Predicate<? super Entity> debug4) {
/* 489 */     int debug5 = Mth.floor((debug2.minY - 2.0D) / 16.0D);
/* 490 */     int debug6 = Mth.floor((debug2.maxY + 2.0D) / 16.0D);
/* 491 */     debug5 = Mth.clamp(debug5, 0, this.entitySections.length - 1);
/* 492 */     debug6 = Mth.clamp(debug6, 0, this.entitySections.length - 1);
/*     */     
/* 494 */     for (int debug7 = debug5; debug7 <= debug6; debug7++) {
/* 495 */       ClassInstanceMultiMap<Entity> debug8 = this.entitySections[debug7];
/* 496 */       List<Entity> debug9 = debug8.getAllInstances();
/* 497 */       int debug10 = debug9.size();
/*     */       
/* 499 */       for (int debug11 = 0; debug11 < debug10; debug11++) {
/* 500 */         Entity debug12 = debug9.get(debug11);
/* 501 */         if (debug12.getBoundingBox().intersects(debug2) && debug12 != debug1) {
/* 502 */           if (debug4 == null || debug4.test(debug12)) {
/* 503 */             debug3.add(debug12);
/*     */           }
/*     */           
/* 506 */           if (debug12 instanceof EnderDragon) {
/* 507 */             for (EnderDragonPart debug16 : ((EnderDragon)debug12).getSubEntities()) {
/* 508 */               if (debug16 != debug1 && debug16.getBoundingBox().intersects(debug2) && (debug4 == null || debug4.test(debug16))) {
/* 509 */                 debug3.add(debug16);
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends Entity> void getEntities(@Nullable EntityType<?> debug1, AABB debug2, List<? super T> debug3, Predicate<? super T> debug4) {
/* 519 */     int debug5 = Mth.floor((debug2.minY - 2.0D) / 16.0D);
/* 520 */     int debug6 = Mth.floor((debug2.maxY + 2.0D) / 16.0D);
/* 521 */     debug5 = Mth.clamp(debug5, 0, this.entitySections.length - 1);
/* 522 */     debug6 = Mth.clamp(debug6, 0, this.entitySections.length - 1);
/*     */     
/* 524 */     for (int debug7 = debug5; debug7 <= debug6; debug7++) {
/* 525 */       for (Entity debug9 : this.entitySections[debug7].find(Entity.class)) {
/* 526 */         if (debug1 != null && debug9.getType() != debug1) {
/*     */           continue;
/*     */         }
/*     */         
/* 530 */         Entity entity1 = debug9;
/* 531 */         if (debug9.getBoundingBox().intersects(debug2) && 
/* 532 */           debug4.test((T)entity1)) {
/* 533 */           debug3.add((T)entity1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> void getEntitiesOfClass(Class<? extends T> debug1, AABB debug2, List<T> debug3, @Nullable Predicate<? super T> debug4) {
/* 541 */     int debug5 = Mth.floor((debug2.minY - 2.0D) / 16.0D);
/* 542 */     int debug6 = Mth.floor((debug2.maxY + 2.0D) / 16.0D);
/* 543 */     debug5 = Mth.clamp(debug5, 0, this.entitySections.length - 1);
/* 544 */     debug6 = Mth.clamp(debug6, 0, this.entitySections.length - 1);
/*     */     
/* 546 */     for (int debug7 = debug5; debug7 <= debug6; debug7++) {
/* 547 */       for (Entity entity : this.entitySections[debug7].find(debug1)) {
/* 548 */         if (entity.getBoundingBox().intersects(debug2) && (
/* 549 */           debug4 == null || debug4.test((T)entity))) {
/* 550 */           debug3.add((T)entity);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 558 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkPos getPos() {
/* 563 */     return this.chunkPos;
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
/*     */   public ChunkBiomeContainer getBiomes() {
/* 605 */     return this.biomes;
/*     */   }
/*     */   
/*     */   public void setLoaded(boolean debug1) {
/* 609 */     this.loaded = debug1;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 613 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
/* 618 */     return Collections.unmodifiableSet(this.heightmaps.entrySet());
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 622 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public ClassInstanceMultiMap<Entity>[] getEntitySections() {
/* 626 */     return this.entitySections;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbt(BlockPos debug1) {
/* 631 */     return this.pendingBlockEntities.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<BlockPos> getLights() {
/* 636 */     return StreamSupport.<BlockPos>stream(BlockPos.betweenClosed(this.chunkPos.getMinBlockX(), 0, this.chunkPos.getMinBlockZ(), this.chunkPos.getMaxBlockX(), 255, this.chunkPos.getMaxBlockZ()).spliterator(), false).filter(debug1 -> (getBlockState(debug1).getLightEmission() != 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public TickList<Block> getBlockTicks() {
/* 641 */     return this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickList<Fluid> getLiquidTicks() {
/* 646 */     return this.liquidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnsaved(boolean debug1) {
/* 651 */     this.unsaved = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnsaved() {
/* 656 */     return (this.unsaved || (this.lastSaveHadEntities && this.level.getGameTime() != this.lastSaveTime));
/*     */   }
/*     */   
/*     */   public void setLastSaveHadEntities(boolean debug1) {
/* 660 */     this.lastSaveHadEntities = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastSaveTime(long debug1) {
/* 665 */     this.lastSaveTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public StructureStart<?> getStartForFeature(StructureFeature<?> debug1) {
/* 671 */     return this.structureStarts.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStartForFeature(StructureFeature<?> debug1, StructureStart<?> debug2) {
/* 676 */     this.structureStarts.put(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
/* 681 */     return this.structureStarts;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<StructureFeature<?>, StructureStart<?>> debug1) {
/* 686 */     this.structureStarts.clear();
/* 687 */     this.structureStarts.putAll(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForFeature(StructureFeature<?> debug1) {
/* 692 */     return this.structuresRefences.computeIfAbsent(debug1, debug0 -> new LongOpenHashSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addReferenceForFeature(StructureFeature<?> debug1, long debug2) {
/* 697 */     ((LongSet)this.structuresRefences.computeIfAbsent(debug1, debug0 -> new LongOpenHashSet())).add(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<StructureFeature<?>, LongSet> getAllReferences() {
/* 702 */     return this.structuresRefences;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<StructureFeature<?>, LongSet> debug1) {
/* 707 */     this.structuresRefences.clear();
/* 708 */     this.structuresRefences.putAll(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getInhabitedTime() {
/* 713 */     return this.inhabitedTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInhabitedTime(long debug1) {
/* 718 */     this.inhabitedTime = debug1;
/*     */   }
/*     */   
/*     */   public void postProcessGeneration() {
/* 722 */     ChunkPos debug1 = getPos();
/* 723 */     for (int debug2 = 0; debug2 < this.postProcessing.length; debug2++) {
/* 724 */       if (this.postProcessing[debug2] != null) {
/* 725 */         for (ShortListIterator<Short> shortListIterator = this.postProcessing[debug2].iterator(); shortListIterator.hasNext(); ) { Short debug4 = shortListIterator.next();
/* 726 */           BlockPos debug5 = ProtoChunk.unpackOffsetCoordinates(debug4.shortValue(), debug2, debug1);
/* 727 */           BlockState debug6 = getBlockState(debug5);
/* 728 */           BlockState debug7 = Block.updateFromNeighbourShapes(debug6, (LevelAccessor)this.level, debug5);
/* 729 */           this.level.setBlock(debug5, debug7, 20); }
/*     */         
/* 731 */         this.postProcessing[debug2].clear();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 736 */     unpackTicks();
/* 737 */     for (BlockPos debug3 : Sets.newHashSet(this.pendingBlockEntities.keySet())) {
/* 738 */       getBlockEntity(debug3);
/*     */     }
/* 740 */     this.pendingBlockEntities.clear();
/* 741 */     this.upgradeData.upgrade(this);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockEntity promotePendingBlockEntity(BlockPos debug1, CompoundTag debug2) {
/*     */     BlockEntity debug3;
/* 747 */     BlockState debug4 = getBlockState(debug1);
/* 748 */     if ("DUMMY".equals(debug2.getString("id"))) {
/* 749 */       Block debug5 = debug4.getBlock();
/* 750 */       if (debug5 instanceof EntityBlock) {
/* 751 */         debug3 = ((EntityBlock)debug5).newBlockEntity((BlockGetter)this.level);
/*     */       } else {
/* 753 */         debug3 = null;
/* 754 */         LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", debug1, debug4);
/*     */       } 
/*     */     } else {
/* 757 */       debug3 = BlockEntity.loadStatic(debug4, debug2);
/*     */     } 
/*     */     
/* 760 */     if (debug3 != null) {
/* 761 */       debug3.setLevelAndPosition(this.level, debug1);
/* 762 */       addBlockEntity(debug3);
/*     */     } else {
/* 764 */       LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", debug4, debug1);
/*     */     } 
/*     */     
/* 767 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpgradeData getUpgradeData() {
/* 772 */     return this.upgradeData;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortList[] getPostProcessing() {
/* 777 */     return this.postProcessing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unpackTicks() {
/* 782 */     if (this.blockTicks instanceof ProtoTickList) {
/* 783 */       ((ProtoTickList)this.blockTicks).copyOut(this.level.getBlockTicks(), debug1 -> getBlockState(debug1).getBlock());
/* 784 */       this.blockTicks = (TickList<Block>)EmptyTickList.empty();
/* 785 */     } else if (this.blockTicks instanceof ChunkTickList) {
/* 786 */       ((ChunkTickList)this.blockTicks).copyOut(this.level.getBlockTicks());
/* 787 */       this.blockTicks = (TickList<Block>)EmptyTickList.empty();
/*     */     } 
/*     */     
/* 790 */     if (this.liquidTicks instanceof ProtoTickList) {
/* 791 */       ((ProtoTickList)this.liquidTicks).copyOut(this.level.getLiquidTicks(), debug1 -> getFluidState(debug1).getType());
/* 792 */       this.liquidTicks = (TickList<Fluid>)EmptyTickList.empty();
/* 793 */     } else if (this.liquidTicks instanceof ChunkTickList) {
/* 794 */       ((ChunkTickList)this.liquidTicks).copyOut(this.level.getLiquidTicks());
/* 795 */       this.liquidTicks = (TickList<Fluid>)EmptyTickList.empty();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void packTicks(ServerLevel debug1) {
/* 801 */     if (this.blockTicks == EmptyTickList.empty()) {
/* 802 */       this.blockTicks = (TickList<Block>)new ChunkTickList(Registry.BLOCK::getKey, debug1.getBlockTicks().fetchTicksInChunk(this.chunkPos, true, false), debug1.getGameTime());
/* 803 */       setUnsaved(true);
/*     */     } 
/* 805 */     if (this.liquidTicks == EmptyTickList.empty()) {
/* 806 */       this.liquidTicks = (TickList<Fluid>)new ChunkTickList(Registry.FLUID::getKey, debug1.getLiquidTicks().fetchTicksInChunk(this.chunkPos, true, false), debug1.getGameTime());
/* 807 */       setUnsaved(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum EntityCreationType {
/* 812 */     IMMEDIATE,
/* 813 */     QUEUED,
/* 814 */     CHECK;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getStatus() {
/* 819 */     return ChunkStatus.FULL;
/*     */   }
/*     */   
/*     */   public ChunkHolder.FullChunkStatus getFullStatus() {
/* 823 */     if (this.fullStatus == null) {
/* 824 */       return ChunkHolder.FullChunkStatus.BORDER;
/*     */     }
/* 826 */     return this.fullStatus.get();
/*     */   }
/*     */   
/*     */   public void setFullStatus(Supplier<ChunkHolder.FullChunkStatus> debug1) {
/* 830 */     this.fullStatus = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLightCorrect() {
/* 835 */     return this.isLightCorrect;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean debug1) {
/* 840 */     this.isLightCorrect = debug1;
/* 841 */     setUnsaved(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\LevelChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */