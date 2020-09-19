/*      */ package net.minecraft.world.level;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.io.IOException;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.level.ChunkHolder;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.TagContainer;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.entity.TickableBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.apache.logging.log4j.util.Supplier;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Level
/*      */   implements LevelAccessor, AutoCloseable
/*      */ {
/*   72 */   protected static final Logger LOGGER = LogManager.getLogger();
/*      */   
/*   74 */   public static final Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceLocation.CODEC.xmap(ResourceKey.elementKey(Registry.DIMENSION_REGISTRY), ResourceKey::location);
/*      */   
/*   76 */   public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("overworld"));
/*   77 */   public static final ResourceKey<Level> NETHER = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_nether"));
/*   78 */   public static final ResourceKey<Level> END = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   private static final Direction[] DIRECTIONS = Direction.values();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   99 */   public final List<BlockEntity> blockEntityList = Lists.newArrayList();
/*  100 */   public final List<BlockEntity> tickableBlockEntities = Lists.newArrayList();
/*  101 */   protected final List<BlockEntity> pendingBlockEntities = Lists.newArrayList();
/*  102 */   protected final List<BlockEntity> blockEntitiesToUnload = Lists.newArrayList();
/*      */   
/*      */   private final Thread thread;
/*      */   
/*      */   private final boolean isDebug;
/*      */   
/*      */   private int skyDarken;
/*  109 */   protected int randValue = (new Random()).nextInt();
/*  110 */   protected final int addend = 1013904223;
/*      */   
/*      */   protected float oRainLevel;
/*      */   protected float rainLevel;
/*      */   protected float oThunderLevel;
/*      */   protected float thunderLevel;
/*  116 */   public final Random random = new Random();
/*      */   
/*      */   private final DimensionType dimensionType;
/*      */   
/*      */   protected final WritableLevelData levelData;
/*      */   
/*      */   private final Supplier<ProfilerFiller> profiler;
/*      */   public final boolean isClientSide;
/*      */   protected boolean updatingBlockEntities;
/*      */   private final WorldBorder worldBorder;
/*      */   private final BiomeManager biomeManager;
/*      */   private final ResourceKey<Level> dimension;
/*      */   
/*      */   protected Level(WritableLevelData debug1, ResourceKey<Level> debug2, final DimensionType dimensionType, Supplier<ProfilerFiller> debug4, boolean debug5, boolean debug6, long debug7) {
/*  130 */     this.profiler = debug4;
/*  131 */     this.levelData = debug1;
/*  132 */     this.dimensionType = dimensionType;
/*  133 */     this.dimension = debug2;
/*  134 */     this.isClientSide = debug5;
/*  135 */     if (dimensionType.coordinateScale() != 1.0D) {
/*  136 */       this.worldBorder = new WorldBorder()
/*      */         {
/*      */           public double getCenterX() {
/*  139 */             return super.getCenterX() / dimensionType.coordinateScale();
/*      */           }
/*      */ 
/*      */           
/*      */           public double getCenterZ() {
/*  144 */             return super.getCenterZ() / dimensionType.coordinateScale();
/*      */           }
/*      */         };
/*      */     } else {
/*  148 */       this.worldBorder = new WorldBorder();
/*      */     } 
/*  150 */     this.thread = Thread.currentThread();
/*  151 */     this.biomeManager = new BiomeManager(this, debug7, dimensionType.getBiomeZoomer());
/*  152 */     this.isDebug = debug6;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isClientSide() {
/*  157 */     return this.isClientSide;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public MinecraftServer getServer() {
/*  163 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInWorldBounds(BlockPos debug0) {
/*  175 */     return (!isOutsideBuildHeight(debug0) && isInWorldBoundsHorizontal(debug0));
/*      */   }
/*      */   
/*      */   public static boolean isInSpawnableBounds(BlockPos debug0) {
/*  179 */     return (!isOutsideSpawnableHeight(debug0.getY()) && isInWorldBoundsHorizontal(debug0));
/*      */   }
/*      */   
/*      */   private static boolean isInWorldBoundsHorizontal(BlockPos debug0) {
/*  183 */     return (debug0.getX() >= -30000000 && debug0.getZ() >= -30000000 && debug0.getX() < 30000000 && debug0.getZ() < 30000000);
/*      */   }
/*      */   
/*      */   private static boolean isOutsideSpawnableHeight(int debug0) {
/*  187 */     return (debug0 < -20000000 || debug0 >= 20000000);
/*      */   }
/*      */   
/*      */   public static boolean isOutsideBuildHeight(BlockPos debug0) {
/*  191 */     return isOutsideBuildHeight(debug0.getY());
/*      */   }
/*      */   
/*      */   public static boolean isOutsideBuildHeight(int debug0) {
/*  195 */     return (debug0 < 0 || debug0 >= 256);
/*      */   }
/*      */   
/*      */   public LevelChunk getChunkAt(BlockPos debug1) {
/*  199 */     return getChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelChunk getChunk(int debug1, int debug2) {
/*  204 */     return (LevelChunk)getChunk(debug1, debug2, ChunkStatus.FULL);
/*      */   }
/*      */ 
/*      */   
/*      */   public ChunkAccess getChunk(int debug1, int debug2, ChunkStatus debug3, boolean debug4) {
/*  209 */     ChunkAccess debug5 = getChunkSource().getChunk(debug1, debug2, debug3, debug4);
/*  210 */     if (debug5 == null && debug4) {
/*  211 */       throw new IllegalStateException("Should always be able to create a chunk!");
/*      */     }
/*  213 */     return debug5;
/*      */   }
/*      */   
/*      */   public boolean setBlock(BlockPos debug1, BlockState debug2, int debug3) {
/*  217 */     return setBlock(debug1, debug2, debug3, 512);
/*      */   }
/*      */   
/*      */   public boolean setBlock(BlockPos debug1, BlockState debug2, int debug3, int debug4) {
/*  221 */     if (isOutsideBuildHeight(debug1)) {
/*  222 */       return false;
/*      */     }
/*      */     
/*  225 */     if (!this.isClientSide && isDebug()) {
/*  226 */       return false;
/*      */     }
/*      */     
/*  229 */     LevelChunk debug5 = getChunkAt(debug1);
/*  230 */     Block debug6 = debug2.getBlock();
/*  231 */     BlockState debug7 = debug5.setBlockState(debug1, debug2, ((debug3 & 0x40) != 0));
/*      */ 
/*      */     
/*  234 */     if (debug7 != null) {
/*      */       
/*  236 */       BlockState debug8 = getBlockState(debug1);
/*      */       
/*  238 */       if ((debug3 & 0x80) == 0 && debug8 != debug7 && (debug8.getLightBlock(this, debug1) != debug7.getLightBlock(this, debug1) || debug8.getLightEmission() != debug7.getLightEmission() || debug8.useShapeForLightOcclusion() || debug7.useShapeForLightOcclusion())) {
/*  239 */         getProfiler().push("queueCheckLight");
/*  240 */         getChunkSource().getLightEngine().checkBlock(debug1);
/*  241 */         getProfiler().pop();
/*      */       } 
/*      */ 
/*      */       
/*  245 */       if (debug8 == debug2) {
/*  246 */         if (debug7 != debug8) {
/*  247 */           setBlocksDirty(debug1, debug7, debug8);
/*      */         }
/*      */         
/*  250 */         if ((debug3 & 0x2) != 0 && (!this.isClientSide || (debug3 & 0x4) == 0) && (this.isClientSide || (debug5.getFullStatus() != null && debug5.getFullStatus().isOrAfter(ChunkHolder.FullChunkStatus.TICKING)))) {
/*  251 */           sendBlockUpdated(debug1, debug7, debug2, debug3);
/*      */         }
/*      */         
/*  254 */         if ((debug3 & 0x1) != 0) {
/*  255 */           blockUpdated(debug1, debug7.getBlock());
/*  256 */           if (!this.isClientSide && debug2.hasAnalogOutputSignal()) {
/*  257 */             updateNeighbourForOutputSignal(debug1, debug6);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  262 */         if ((debug3 & 0x10) == 0 && debug4 > 0) {
/*  263 */           int debug9 = debug3 & 0xFFFFFFDE;
/*  264 */           debug7.updateIndirectNeighbourShapes(this, debug1, debug9, debug4 - 1);
/*  265 */           debug2.updateNeighbourShapes(this, debug1, debug9, debug4 - 1);
/*  266 */           debug2.updateIndirectNeighbourShapes(this, debug1, debug9, debug4 - 1);
/*      */         } 
/*      */         
/*  269 */         onBlockStateChange(debug1, debug7, debug8);
/*      */       } 
/*      */       
/*  272 */       return true;
/*      */     } 
/*      */     
/*  275 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onBlockStateChange(BlockPos debug1, BlockState debug2, BlockState debug3) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeBlock(BlockPos debug1, boolean debug2) {
/*  290 */     FluidState debug3 = getFluidState(debug1);
/*  291 */     return setBlock(debug1, debug3.createLegacyBlock(), 0x3 | (debug2 ? 64 : 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean destroyBlock(BlockPos debug1, boolean debug2, @Nullable Entity debug3, int debug4) {
/*  304 */     BlockState debug5 = getBlockState(debug1);
/*  305 */     if (debug5.isAir()) {
/*  306 */       return false;
/*      */     }
/*      */     
/*  309 */     FluidState debug6 = getFluidState(debug1);
/*  310 */     if (!(debug5.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock)) {
/*  311 */       levelEvent(2001, debug1, Block.getId(debug5));
/*      */     }
/*  313 */     if (debug2) {
/*  314 */       BlockEntity debug7 = debug5.getBlock().isEntityBlock() ? getBlockEntity(debug1) : null;
/*  315 */       Block.dropResources(debug5, this, debug1, debug7, debug3, ItemStack.EMPTY);
/*      */     } 
/*  317 */     return setBlock(debug1, debug6.createLegacyBlock(), 3, debug4);
/*      */   }
/*      */   
/*      */   public boolean setBlockAndUpdate(BlockPos debug1, BlockState debug2) {
/*  321 */     return setBlock(debug1, debug2, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlocksDirty(BlockPos debug1, BlockState debug2, BlockState debug3) {}
/*      */ 
/*      */   
/*      */   public void updateNeighborsAt(BlockPos debug1, Block debug2) {
/*  330 */     neighborChanged(debug1.west(), debug2, debug1);
/*  331 */     neighborChanged(debug1.east(), debug2, debug1);
/*  332 */     neighborChanged(debug1.below(), debug2, debug1);
/*  333 */     neighborChanged(debug1.above(), debug2, debug1);
/*  334 */     neighborChanged(debug1.north(), debug2, debug1);
/*  335 */     neighborChanged(debug1.south(), debug2, debug1);
/*      */   }
/*      */   
/*      */   public void updateNeighborsAtExceptFromFacing(BlockPos debug1, Block debug2, Direction debug3) {
/*  339 */     if (debug3 != Direction.WEST) {
/*  340 */       neighborChanged(debug1.west(), debug2, debug1);
/*      */     }
/*  342 */     if (debug3 != Direction.EAST) {
/*  343 */       neighborChanged(debug1.east(), debug2, debug1);
/*      */     }
/*  345 */     if (debug3 != Direction.DOWN) {
/*  346 */       neighborChanged(debug1.below(), debug2, debug1);
/*      */     }
/*  348 */     if (debug3 != Direction.UP) {
/*  349 */       neighborChanged(debug1.above(), debug2, debug1);
/*      */     }
/*  351 */     if (debug3 != Direction.NORTH) {
/*  352 */       neighborChanged(debug1.north(), debug2, debug1);
/*      */     }
/*  354 */     if (debug3 != Direction.SOUTH) {
/*  355 */       neighborChanged(debug1.south(), debug2, debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   public void neighborChanged(BlockPos debug1, Block debug2, BlockPos debug3) {
/*  360 */     if (this.isClientSide) {
/*      */       return;
/*      */     }
/*  363 */     BlockState debug4 = getBlockState(debug1);
/*      */     
/*      */     try {
/*  366 */       debug4.neighborChanged(this, debug1, debug2, debug3, false);
/*  367 */     } catch (Throwable debug5) {
/*  368 */       CrashReport debug6 = CrashReport.forThrowable(debug5, "Exception while updating neighbours");
/*  369 */       CrashReportCategory debug7 = debug6.addCategory("Block being updated");
/*      */       
/*  371 */       debug7.setDetail("Source block type", () -> {
/*      */             try {
/*      */               return String.format("ID #%s (%s // %s)", new Object[] { Registry.BLOCK.getKey(debug0), debug0.getDescriptionId(), debug0.getClass().getCanonicalName() });
/*  374 */             } catch (Throwable debug1) {
/*      */               return "ID #" + Registry.BLOCK.getKey(debug0);
/*      */             } 
/*      */           });
/*      */       
/*  379 */       CrashReportCategory.populateBlockDetails(debug7, debug1, debug4);
/*      */       
/*  381 */       throw new ReportedException(debug6);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHeight(Heightmap.Types debug1, int debug2, int debug3) {
/*      */     int debug4;
/*  388 */     if (debug2 < -30000000 || debug3 < -30000000 || debug2 >= 30000000 || debug3 >= 30000000) {
/*  389 */       debug4 = getSeaLevel() + 1;
/*  390 */     } else if (hasChunk(debug2 >> 4, debug3 >> 4)) {
/*  391 */       debug4 = getChunk(debug2 >> 4, debug3 >> 4).getHeight(debug1, debug2 & 0xF, debug3 & 0xF) + 1;
/*      */     } else {
/*  393 */       debug4 = 0;
/*      */     } 
/*  395 */     return debug4;
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelLightEngine getLightEngine() {
/*  400 */     return getChunkSource().getLightEngine();
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockState getBlockState(BlockPos debug1) {
/*  405 */     if (isOutsideBuildHeight(debug1)) {
/*  406 */       return Blocks.VOID_AIR.defaultBlockState();
/*      */     }
/*  408 */     LevelChunk debug2 = getChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*  409 */     return debug2.getBlockState(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public FluidState getFluidState(BlockPos debug1) {
/*  414 */     if (isOutsideBuildHeight(debug1)) {
/*  415 */       return Fluids.EMPTY.defaultFluidState();
/*      */     }
/*  417 */     LevelChunk debug2 = getChunkAt(debug1);
/*  418 */     return debug2.getFluidState(debug1);
/*      */   }
/*      */   
/*      */   public boolean isDay() {
/*  422 */     return (!dimensionType().hasFixedTime() && this.skyDarken < 4);
/*      */   }
/*      */   
/*      */   public boolean isNight() {
/*  426 */     return (!dimensionType().hasFixedTime() && !isDay());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(@Nullable Player debug1, BlockPos debug2, SoundEvent debug3, SoundSource debug4, float debug5, float debug6) {
/*  432 */     playSound(debug1, debug2.getX() + 0.5D, debug2.getY() + 0.5D, debug2.getZ() + 0.5D, debug3, debug4, debug5, debug6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playLocalSound(double debug1, double debug3, double debug5, SoundEvent debug7, SoundSource debug8, float debug9, float debug10, boolean debug11) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addParticle(ParticleOptions debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions debug1, boolean debug2, double debug3, double debug5, double debug7, double debug9, double debug11, double debug13) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSunAngle(float debug1) {
/*  460 */     float debug2 = getTimeOfDay(debug1);
/*  461 */     return debug2 * 6.2831855F;
/*      */   }
/*      */   
/*      */   public boolean addBlockEntity(BlockEntity debug1) {
/*  465 */     if (this.updatingBlockEntities) {
/*  466 */       LOGGER.error("Adding block entity while ticking: {} @ {}", new Supplier[] { () -> Registry.BLOCK_ENTITY_TYPE.getKey(debug0.getType()), debug1::getBlockPos });
/*      */     }
/*  468 */     boolean debug2 = this.blockEntityList.add(debug1);
/*  469 */     if (debug2 && debug1 instanceof TickableBlockEntity) {
/*  470 */       this.tickableBlockEntities.add(debug1);
/*      */     }
/*      */     
/*  473 */     if (this.isClientSide) {
/*  474 */       BlockPos debug3 = debug1.getBlockPos();
/*  475 */       BlockState debug4 = getBlockState(debug3);
/*  476 */       sendBlockUpdated(debug3, debug4, debug4, 2);
/*      */     } 
/*      */     
/*  479 */     return debug2;
/*      */   }
/*      */   
/*      */   public void addAllPendingBlockEntities(Collection<BlockEntity> debug1) {
/*  483 */     if (this.updatingBlockEntities) {
/*  484 */       this.pendingBlockEntities.addAll(debug1);
/*      */     } else {
/*  486 */       for (BlockEntity debug3 : debug1) {
/*  487 */         addBlockEntity(debug3);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void tickBlockEntities() {
/*  493 */     ProfilerFiller debug1 = getProfiler();
/*  494 */     debug1.push("blockEntities");
/*  495 */     if (!this.blockEntitiesToUnload.isEmpty()) {
/*  496 */       this.tickableBlockEntities.removeAll(this.blockEntitiesToUnload);
/*  497 */       this.blockEntityList.removeAll(this.blockEntitiesToUnload);
/*  498 */       this.blockEntitiesToUnload.clear();
/*      */     } 
/*  500 */     this.updatingBlockEntities = true;
/*  501 */     Iterator<BlockEntity> debug2 = this.tickableBlockEntities.iterator();
/*  502 */     while (debug2.hasNext()) {
/*  503 */       BlockEntity debug3 = debug2.next();
/*  504 */       if (!debug3.isRemoved() && debug3.hasLevel()) {
/*  505 */         BlockPos debug4 = debug3.getBlockPos();
/*  506 */         if (getChunkSource().isTickingChunk(debug4) && getWorldBorder().isWithinBounds(debug4)) {
/*      */           try {
/*  508 */             debug1.push(() -> String.valueOf(BlockEntityType.getKey(debug0.getType())));
/*  509 */             if (debug3.getType().isValid(getBlockState(debug4).getBlock())) {
/*  510 */               ((TickableBlockEntity)debug3).tick();
/*      */             } else {
/*  512 */               debug3.logInvalidState();
/*      */             } 
/*  514 */             debug1.pop();
/*  515 */           } catch (Throwable debug5) {
/*  516 */             CrashReport debug6 = CrashReport.forThrowable(debug5, "Ticking block entity");
/*  517 */             CrashReportCategory debug7 = debug6.addCategory("Block entity being ticked");
/*      */             
/*  519 */             debug3.fillCrashReportCategory(debug7);
/*      */             
/*  521 */             throw new ReportedException(debug6);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  526 */       if (debug3.isRemoved()) {
/*  527 */         debug2.remove();
/*  528 */         this.blockEntityList.remove(debug3);
/*      */         
/*  530 */         if (hasChunkAt(debug3.getBlockPos())) {
/*  531 */           getChunkAt(debug3.getBlockPos()).removeBlockEntity(debug3.getBlockPos());
/*      */         }
/*      */       } 
/*      */     } 
/*  535 */     this.updatingBlockEntities = false;
/*      */     
/*  537 */     debug1.popPush("pendingBlockEntities");
/*  538 */     if (!this.pendingBlockEntities.isEmpty()) {
/*  539 */       for (int debug3 = 0; debug3 < this.pendingBlockEntities.size(); debug3++) {
/*  540 */         BlockEntity debug4 = this.pendingBlockEntities.get(debug3);
/*  541 */         if (!debug4.isRemoved()) {
/*  542 */           if (!this.blockEntityList.contains(debug4)) {
/*  543 */             addBlockEntity(debug4);
/*      */           }
/*      */           
/*  546 */           if (hasChunkAt(debug4.getBlockPos())) {
/*  547 */             LevelChunk debug5 = getChunkAt(debug4.getBlockPos());
/*  548 */             BlockState debug6 = debug5.getBlockState(debug4.getBlockPos());
/*  549 */             debug5.setBlockEntity(debug4.getBlockPos(), debug4);
/*  550 */             sendBlockUpdated(debug4.getBlockPos(), debug6, debug6, 3);
/*      */           } 
/*      */         } 
/*      */       } 
/*  554 */       this.pendingBlockEntities.clear();
/*      */     } 
/*  556 */     debug1.pop();
/*      */   }
/*      */   
/*      */   public void guardEntityTick(Consumer<Entity> debug1, Entity debug2) {
/*      */     try {
/*  561 */       debug1.accept(debug2);
/*  562 */     } catch (Throwable debug3) {
/*  563 */       CrashReport debug4 = CrashReport.forThrowable(debug3, "Ticking entity");
/*  564 */       CrashReportCategory debug5 = debug4.addCategory("Entity being ticked");
/*      */       
/*  566 */       debug2.fillCrashReportCategory(debug5);
/*      */       
/*  568 */       throw new ReportedException(debug4);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Explosion explode(@Nullable Entity debug1, double debug2, double debug4, double debug6, float debug8, Explosion.BlockInteraction debug9) {
/*  573 */     return explode(debug1, (DamageSource)null, (ExplosionDamageCalculator)null, debug2, debug4, debug6, debug8, false, debug9);
/*      */   }
/*      */   
/*      */   public Explosion explode(@Nullable Entity debug1, double debug2, double debug4, double debug6, float debug8, boolean debug9, Explosion.BlockInteraction debug10) {
/*  577 */     return explode(debug1, (DamageSource)null, (ExplosionDamageCalculator)null, debug2, debug4, debug6, debug8, debug9, debug10);
/*      */   }
/*      */   
/*      */   public Explosion explode(@Nullable Entity debug1, @Nullable DamageSource debug2, @Nullable ExplosionDamageCalculator debug3, double debug4, double debug6, double debug8, float debug10, boolean debug11, Explosion.BlockInteraction debug12) {
/*  581 */     Explosion debug13 = new Explosion(this, debug1, debug2, debug3, debug4, debug6, debug8, debug10, debug11, debug12);
/*  582 */     debug13.explode();
/*  583 */     debug13.finalizeExplosion(true);
/*  584 */     return debug13;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BlockEntity getBlockEntity(BlockPos debug1) {
/*  594 */     if (isOutsideBuildHeight(debug1)) {
/*  595 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  599 */     if (!this.isClientSide && Thread.currentThread() != this.thread) {
/*  600 */       return null;
/*      */     }
/*      */     
/*  603 */     BlockEntity debug2 = null;
/*      */     
/*  605 */     if (this.updatingBlockEntities) {
/*  606 */       debug2 = getPendingBlockEntityAt(debug1);
/*      */     }
/*      */     
/*  609 */     if (debug2 == null) {
/*  610 */       debug2 = getChunkAt(debug1).getBlockEntity(debug1, LevelChunk.EntityCreationType.IMMEDIATE);
/*      */     }
/*      */     
/*  613 */     if (debug2 == null) {
/*  614 */       debug2 = getPendingBlockEntityAt(debug1);
/*      */     }
/*  616 */     return debug2;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private BlockEntity getPendingBlockEntityAt(BlockPos debug1) {
/*  621 */     for (int debug2 = 0; debug2 < this.pendingBlockEntities.size(); debug2++) {
/*  622 */       BlockEntity debug3 = this.pendingBlockEntities.get(debug2);
/*  623 */       if (!debug3.isRemoved() && debug3.getBlockPos().equals(debug1)) {
/*  624 */         return debug3;
/*      */       }
/*      */     } 
/*  627 */     return null;
/*      */   }
/*      */   
/*      */   public void setBlockEntity(BlockPos debug1, @Nullable BlockEntity debug2) {
/*  631 */     if (isOutsideBuildHeight(debug1)) {
/*      */       return;
/*      */     }
/*      */     
/*  635 */     if (debug2 != null && !debug2.isRemoved()) {
/*  636 */       if (this.updatingBlockEntities) {
/*  637 */         debug2.setLevelAndPosition(this, debug1);
/*      */ 
/*      */         
/*  640 */         Iterator<BlockEntity> debug3 = this.pendingBlockEntities.iterator();
/*  641 */         while (debug3.hasNext()) {
/*  642 */           BlockEntity debug4 = debug3.next();
/*  643 */           if (debug4.getBlockPos().equals(debug1)) {
/*  644 */             debug4.setRemoved();
/*  645 */             debug3.remove();
/*      */           } 
/*      */         } 
/*      */         
/*  649 */         this.pendingBlockEntities.add(debug2);
/*      */       } else {
/*  651 */         getChunkAt(debug1).setBlockEntity(debug1, debug2);
/*  652 */         addBlockEntity(debug2);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeBlockEntity(BlockPos debug1) {
/*  658 */     BlockEntity debug2 = getBlockEntity(debug1);
/*  659 */     if (debug2 != null && this.updatingBlockEntities) {
/*  660 */       debug2.setRemoved();
/*  661 */       this.pendingBlockEntities.remove(debug2);
/*      */     } else {
/*  663 */       if (debug2 != null) {
/*  664 */         this.pendingBlockEntities.remove(debug2);
/*  665 */         this.blockEntityList.remove(debug2);
/*  666 */         this.tickableBlockEntities.remove(debug2);
/*      */       } 
/*      */       
/*  669 */       getChunkAt(debug1).removeBlockEntity(debug1);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isLoaded(BlockPos debug1) {
/*  674 */     if (isOutsideBuildHeight(debug1)) {
/*  675 */       return false;
/*      */     }
/*  677 */     return getChunkSource().hasChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean loadedAndEntityCanStandOnFace(BlockPos debug1, Entity debug2, Direction debug3) {
/*  686 */     if (isOutsideBuildHeight(debug1)) {
/*  687 */       return false;
/*      */     }
/*      */     
/*  690 */     ChunkAccess debug4 = getChunk(debug1.getX() >> 4, debug1.getZ() >> 4, ChunkStatus.FULL, false);
/*  691 */     if (debug4 == null) {
/*  692 */       return false;
/*      */     }
/*      */     
/*  695 */     return debug4.getBlockState(debug1).entityCanStandOnFace(this, debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   public boolean loadedAndEntityCanStandOn(BlockPos debug1, Entity debug2) {
/*  699 */     return loadedAndEntityCanStandOnFace(debug1, debug2, Direction.UP);
/*      */   }
/*      */   
/*      */   public void updateSkyBrightness() {
/*  703 */     double debug1 = 1.0D - (getRainLevel(1.0F) * 5.0F) / 16.0D;
/*  704 */     double debug3 = 1.0D - (getThunderLevel(1.0F) * 5.0F) / 16.0D;
/*      */     
/*  706 */     double debug5 = 0.5D + 2.0D * Mth.clamp(Mth.cos(getTimeOfDay(1.0F) * 6.2831855F), -0.25D, 0.25D);
/*      */     
/*  708 */     this.skyDarken = (int)((1.0D - debug5 * debug1 * debug3) * 11.0D);
/*      */   }
/*      */   
/*      */   public void setSpawnSettings(boolean debug1, boolean debug2) {
/*  712 */     getChunkSource().setSpawnSettings(debug1, debug2);
/*      */   }
/*      */   
/*      */   protected void prepareWeather() {
/*  716 */     if (this.levelData.isRaining()) {
/*  717 */       this.rainLevel = 1.0F;
/*  718 */       if (this.levelData.isThundering()) {
/*  719 */         this.thunderLevel = 1.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  726 */     getChunkSource().close();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BlockGetter getChunkForCollisions(int debug1, int debug2) {
/*  732 */     return (BlockGetter)getChunk(debug1, debug2, ChunkStatus.FULL, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Entity> getEntities(@Nullable Entity debug1, AABB debug2, @Nullable Predicate<? super Entity> debug3) {
/*  737 */     getProfiler().incrementCounter("getEntities");
/*  738 */     List<Entity> debug4 = Lists.newArrayList();
/*  739 */     int debug5 = Mth.floor((debug2.minX - 2.0D) / 16.0D);
/*  740 */     int debug6 = Mth.floor((debug2.maxX + 2.0D) / 16.0D);
/*  741 */     int debug7 = Mth.floor((debug2.minZ - 2.0D) / 16.0D);
/*  742 */     int debug8 = Mth.floor((debug2.maxZ + 2.0D) / 16.0D);
/*  743 */     ChunkSource debug9 = getChunkSource();
/*  744 */     for (int debug10 = debug5; debug10 <= debug6; debug10++) {
/*  745 */       for (int debug11 = debug7; debug11 <= debug8; debug11++) {
/*  746 */         LevelChunk debug12 = debug9.getChunk(debug10, debug11, false);
/*  747 */         if (debug12 != null) {
/*  748 */           debug12.getEntities(debug1, debug2, debug4, debug3);
/*      */         }
/*      */       } 
/*      */     } 
/*  752 */     return debug4;
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(@Nullable EntityType<T> debug1, AABB debug2, Predicate<? super T> debug3) {
/*  756 */     getProfiler().incrementCounter("getEntities");
/*  757 */     int debug4 = Mth.floor((debug2.minX - 2.0D) / 16.0D);
/*  758 */     int debug5 = Mth.ceil((debug2.maxX + 2.0D) / 16.0D);
/*  759 */     int debug6 = Mth.floor((debug2.minZ - 2.0D) / 16.0D);
/*  760 */     int debug7 = Mth.ceil((debug2.maxZ + 2.0D) / 16.0D);
/*  761 */     List<T> debug8 = Lists.newArrayList();
/*      */     
/*  763 */     for (int debug9 = debug4; debug9 < debug5; debug9++) {
/*  764 */       for (int debug10 = debug6; debug10 < debug7; debug10++) {
/*  765 */         LevelChunk debug11 = getChunkSource().getChunk(debug9, debug10, false);
/*  766 */         if (debug11 != null) {
/*  767 */           debug11.getEntities(debug1, debug2, debug8, debug3);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  772 */     return debug8;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getEntitiesOfClass(Class<? extends T> debug1, AABB debug2, @Nullable Predicate<? super T> debug3) {
/*  777 */     getProfiler().incrementCounter("getEntities");
/*  778 */     int debug4 = Mth.floor((debug2.minX - 2.0D) / 16.0D);
/*  779 */     int debug5 = Mth.ceil((debug2.maxX + 2.0D) / 16.0D);
/*  780 */     int debug6 = Mth.floor((debug2.minZ - 2.0D) / 16.0D);
/*  781 */     int debug7 = Mth.ceil((debug2.maxZ + 2.0D) / 16.0D);
/*  782 */     List<T> debug8 = Lists.newArrayList();
/*      */     
/*  784 */     ChunkSource debug9 = getChunkSource();
/*  785 */     for (int debug10 = debug4; debug10 < debug5; debug10++) {
/*  786 */       for (int debug11 = debug6; debug11 < debug7; debug11++) {
/*  787 */         LevelChunk debug12 = debug9.getChunk(debug10, debug11, false);
/*  788 */         if (debug12 != null) {
/*  789 */           debug12.getEntitiesOfClass(debug1, debug2, debug8, debug3);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  794 */     return debug8;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getLoadedEntitiesOfClass(Class<? extends T> debug1, AABB debug2, @Nullable Predicate<? super T> debug3) {
/*  799 */     getProfiler().incrementCounter("getLoadedEntities");
/*  800 */     int debug4 = Mth.floor((debug2.minX - 2.0D) / 16.0D);
/*  801 */     int debug5 = Mth.ceil((debug2.maxX + 2.0D) / 16.0D);
/*  802 */     int debug6 = Mth.floor((debug2.minZ - 2.0D) / 16.0D);
/*  803 */     int debug7 = Mth.ceil((debug2.maxZ + 2.0D) / 16.0D);
/*  804 */     List<T> debug8 = Lists.newArrayList();
/*      */     
/*  806 */     ChunkSource debug9 = getChunkSource();
/*  807 */     for (int debug10 = debug4; debug10 < debug5; debug10++) {
/*  808 */       for (int debug11 = debug6; debug11 < debug7; debug11++) {
/*  809 */         LevelChunk debug12 = debug9.getChunkNow(debug10, debug11);
/*  810 */         if (debug12 != null) {
/*  811 */           debug12.getEntitiesOfClass(debug1, debug2, debug8, debug3);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  816 */     return debug8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void blockEntityChanged(BlockPos debug1, BlockEntity debug2) {
/*  823 */     if (hasChunkAt(debug1)) {
/*  824 */       getChunkAt(debug1).markUnsaved();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSeaLevel() {
/*  830 */     return 63;
/*      */   }
/*      */   
/*      */   public int getDirectSignalTo(BlockPos debug1) {
/*  834 */     int debug2 = 0;
/*  835 */     debug2 = Math.max(debug2, getDirectSignal(debug1.below(), Direction.DOWN));
/*  836 */     if (debug2 >= 15) {
/*  837 */       return debug2;
/*      */     }
/*  839 */     debug2 = Math.max(debug2, getDirectSignal(debug1.above(), Direction.UP));
/*  840 */     if (debug2 >= 15) {
/*  841 */       return debug2;
/*      */     }
/*  843 */     debug2 = Math.max(debug2, getDirectSignal(debug1.north(), Direction.NORTH));
/*  844 */     if (debug2 >= 15) {
/*  845 */       return debug2;
/*      */     }
/*  847 */     debug2 = Math.max(debug2, getDirectSignal(debug1.south(), Direction.SOUTH));
/*  848 */     if (debug2 >= 15) {
/*  849 */       return debug2;
/*      */     }
/*  851 */     debug2 = Math.max(debug2, getDirectSignal(debug1.west(), Direction.WEST));
/*  852 */     if (debug2 >= 15) {
/*  853 */       return debug2;
/*      */     }
/*  855 */     debug2 = Math.max(debug2, getDirectSignal(debug1.east(), Direction.EAST));
/*  856 */     if (debug2 >= 15) {
/*  857 */       return debug2;
/*      */     }
/*  859 */     return debug2;
/*      */   }
/*      */   
/*      */   public boolean hasSignal(BlockPos debug1, Direction debug2) {
/*  863 */     return (getSignal(debug1, debug2) > 0);
/*      */   }
/*      */   
/*      */   public int getSignal(BlockPos debug1, Direction debug2) {
/*  867 */     BlockState debug3 = getBlockState(debug1);
/*      */     
/*  869 */     int debug4 = debug3.getSignal(this, debug1, debug2);
/*  870 */     if (debug3.isRedstoneConductor(this, debug1)) {
/*  871 */       return Math.max(debug4, getDirectSignalTo(debug1));
/*      */     }
/*  873 */     return debug4;
/*      */   }
/*      */   
/*      */   public boolean hasNeighborSignal(BlockPos debug1) {
/*  877 */     if (getSignal(debug1.below(), Direction.DOWN) > 0) {
/*  878 */       return true;
/*      */     }
/*  880 */     if (getSignal(debug1.above(), Direction.UP) > 0) {
/*  881 */       return true;
/*      */     }
/*  883 */     if (getSignal(debug1.north(), Direction.NORTH) > 0) {
/*  884 */       return true;
/*      */     }
/*  886 */     if (getSignal(debug1.south(), Direction.SOUTH) > 0) {
/*  887 */       return true;
/*      */     }
/*  889 */     if (getSignal(debug1.west(), Direction.WEST) > 0) {
/*  890 */       return true;
/*      */     }
/*  892 */     if (getSignal(debug1.east(), Direction.EAST) > 0) {
/*  893 */       return true;
/*      */     }
/*  895 */     return false;
/*      */   }
/*      */   
/*      */   public int getBestNeighborSignal(BlockPos debug1) {
/*  899 */     int debug2 = 0;
/*      */     
/*  901 */     for (Direction debug6 : DIRECTIONS) {
/*  902 */       int debug7 = getSignal(debug1.relative(debug6), debug6);
/*      */       
/*  904 */       if (debug7 >= 15) {
/*  905 */         return 15;
/*      */       }
/*  907 */       if (debug7 > debug2) {
/*  908 */         debug2 = debug7;
/*      */       }
/*      */     } 
/*      */     
/*  912 */     return debug2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getGameTime() {
/*  922 */     return this.levelData.getGameTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDayTime() {
/*  930 */     return this.levelData.getDayTime();
/*      */   }
/*      */   
/*      */   public boolean mayInteract(Player debug1, BlockPos debug2) {
/*  934 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastEntityEvent(Entity debug1, byte debug2) {}
/*      */   
/*      */   public void blockEvent(BlockPos debug1, Block debug2, int debug3, int debug4) {
/*  941 */     getBlockState(debug1).triggerEvent(this, debug1, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelData getLevelData() {
/*  946 */     return (LevelData)this.levelData;
/*      */   }
/*      */   
/*      */   public GameRules getGameRules() {
/*  950 */     return this.levelData.getGameRules();
/*      */   }
/*      */   
/*      */   public float getThunderLevel(float debug1) {
/*  954 */     return Mth.lerp(debug1, this.oThunderLevel, this.thunderLevel) * getRainLevel(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getRainLevel(float debug1) {
/*  963 */     return Mth.lerp(debug1, this.oRainLevel, this.rainLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isThundering() {
/*  972 */     if (!dimensionType().hasSkyLight() || dimensionType().hasCeiling()) {
/*  973 */       return false;
/*      */     }
/*  975 */     return (getThunderLevel(1.0F) > 0.9D);
/*      */   }
/*      */   
/*      */   public boolean isRaining() {
/*  979 */     return (getRainLevel(1.0F) > 0.2D);
/*      */   }
/*      */   
/*      */   public boolean isRainingAt(BlockPos debug1) {
/*  983 */     if (!isRaining()) {
/*  984 */       return false;
/*      */     }
/*  986 */     if (!canSeeSky(debug1)) {
/*  987 */       return false;
/*      */     }
/*  989 */     if (getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, debug1).getY() > debug1.getY()) {
/*  990 */       return false;
/*      */     }
/*      */     
/*  993 */     Biome debug2 = getBiome(debug1);
/*      */     
/*  995 */     return (debug2.getPrecipitation() == Biome.Precipitation.RAIN && debug2.getTemperature(debug1) >= 0.15F);
/*      */   }
/*      */   
/*      */   public boolean isHumidAt(BlockPos debug1) {
/*  999 */     Biome debug2 = getBiome(debug1);
/* 1000 */     return debug2.isHumid();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void globalLevelEvent(int debug1, BlockPos debug2, int debug3) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CrashReportCategory fillReportDetails(CrashReport debug1) {
/* 1014 */     CrashReportCategory debug2 = debug1.addCategory("Affected level", 1);
/*      */     
/* 1016 */     debug2.setDetail("All players", () -> players().size() + " total; " + players());
/* 1017 */     debug2.setDetail("Chunk stats", getChunkSource()::gatherStats);
/* 1018 */     debug2.setDetail("Level dimension", () -> dimension().location().toString());
/*      */     
/*      */     try {
/* 1021 */       this.levelData.fillCrashReportCategory(debug2);
/* 1022 */     } catch (Throwable debug3) {
/* 1023 */       debug2.setDetailError("Level Data Unobtainable", debug3);
/*      */     } 
/*      */     
/* 1026 */     return debug2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateNeighbourForOutputSignal(BlockPos debug1, Block debug2) {
/* 1037 */     for (Direction debug4 : Direction.Plane.HORIZONTAL) {
/* 1038 */       BlockPos debug5 = debug1.relative(debug4);
/*      */       
/* 1040 */       if (hasChunkAt(debug5)) {
/* 1041 */         BlockState debug6 = getBlockState(debug5);
/* 1042 */         if (debug6.is(Blocks.COMPARATOR)) {
/* 1043 */           debug6.neighborChanged(this, debug5, debug2, debug1, false); continue;
/* 1044 */         }  if (debug6.isRedstoneConductor(this, debug5)) {
/* 1045 */           debug5 = debug5.relative(debug4);
/* 1046 */           debug6 = getBlockState(debug5);
/*      */           
/* 1048 */           if (debug6.is(Blocks.COMPARATOR)) {
/* 1049 */             debug6.neighborChanged(this, debug5, debug2, debug1, false);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public DifficultyInstance getCurrentDifficultyAt(BlockPos debug1) {
/* 1058 */     long debug2 = 0L;
/* 1059 */     float debug4 = 0.0F;
/* 1060 */     if (hasChunkAt(debug1)) {
/* 1061 */       debug4 = getMoonBrightness();
/* 1062 */       debug2 = getChunkAt(debug1).getInhabitedTime();
/*      */     } 
/*      */     
/* 1065 */     return new DifficultyInstance(getDifficulty(), getDayTime(), debug2, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSkyDarken() {
/* 1070 */     return this.skyDarken;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSkyFlashTime(int debug1) {}
/*      */ 
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/* 1078 */     return this.worldBorder;
/*      */   }
/*      */   
/*      */   public void sendPacketToServer(Packet<?> debug1) {
/* 1082 */     throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
/*      */   }
/*      */ 
/*      */   
/*      */   public DimensionType dimensionType() {
/* 1087 */     return this.dimensionType;
/*      */   }
/*      */   
/*      */   public ResourceKey<Level> dimension() {
/* 1091 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   
/*      */   public Random getRandom() {
/* 1096 */     return this.random;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isStateAtPosition(BlockPos debug1, Predicate<BlockState> debug2) {
/* 1101 */     return debug2.test(getBlockState(debug1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos getBlockRandomPos(int debug1, int debug2, int debug3, int debug4) {
/* 1114 */     this.randValue = this.randValue * 3 + 1013904223;
/* 1115 */     int debug5 = this.randValue >> 2;
/*      */     
/* 1117 */     return new BlockPos(debug1 + (debug5 & 0xF), debug2 + (debug5 >> 16 & debug4), debug3 + (debug5 >> 8 & 0xF));
/*      */   }
/*      */   
/*      */   public boolean noSave() {
/* 1121 */     return false;
/*      */   }
/*      */   
/*      */   public ProfilerFiller getProfiler() {
/* 1125 */     return this.profiler.get();
/*      */   }
/*      */   
/*      */   public Supplier<ProfilerFiller> getProfilerSupplier() {
/* 1129 */     return this.profiler;
/*      */   }
/*      */ 
/*      */   
/*      */   public BiomeManager getBiomeManager() {
/* 1134 */     return this.biomeManager;
/*      */   }
/*      */   
/*      */   public final boolean isDebug() {
/* 1138 */     return this.isDebug;
/*      */   }
/*      */   
/*      */   public abstract void sendBlockUpdated(BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, int paramInt);
/*      */   
/*      */   public abstract void playSound(@Nullable Player paramPlayer, double paramDouble1, double paramDouble2, double paramDouble3, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2);
/*      */   
/*      */   public abstract void playSound(@Nullable Player paramPlayer, Entity paramEntity, SoundEvent paramSoundEvent, SoundSource paramSoundSource, float paramFloat1, float paramFloat2);
/*      */   
/*      */   @Nullable
/*      */   public abstract Entity getEntity(int paramInt);
/*      */   
/*      */   @Nullable
/*      */   public abstract MapItemSavedData getMapData(String paramString);
/*      */   
/*      */   public abstract void setMapData(MapItemSavedData paramMapItemSavedData);
/*      */   
/*      */   public abstract int getFreeMapId();
/*      */   
/*      */   public abstract void destroyBlockProgress(int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*      */   
/*      */   public abstract Scoreboard getScoreboard();
/*      */   
/*      */   public abstract RecipeManager getRecipeManager();
/*      */   
/*      */   public abstract TagContainer getTagManager();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\Level.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */