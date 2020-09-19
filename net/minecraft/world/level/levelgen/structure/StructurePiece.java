/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.material.FluidState;
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
/*     */ public abstract class StructurePiece
/*     */ {
/*  66 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   protected BoundingBox boundingBox;
/*     */   @Nullable
/*     */   private Direction orientation;
/*     */   private Mirror mirror;
/*     */   private Rotation rotation;
/*     */   protected int genDepth;
/*     */   private final StructurePieceType type;
/*     */   
/*     */   protected StructurePiece(StructurePieceType debug1, int debug2) {
/*  76 */     this.type = debug1;
/*  77 */     this.genDepth = debug2;
/*     */   }
/*     */   
/*     */   public StructurePiece(StructurePieceType debug1, CompoundTag debug2) {
/*  81 */     this(debug1, debug2.getInt("GD"));
/*     */     
/*  83 */     if (debug2.contains("BB")) {
/*  84 */       this.boundingBox = new BoundingBox(debug2.getIntArray("BB"));
/*     */     }
/*  86 */     int debug3 = debug2.getInt("O");
/*  87 */     setOrientation((debug3 == -1) ? null : Direction.from2DDataValue(debug3));
/*     */   }
/*     */   
/*     */   public final CompoundTag createTag() {
/*  91 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/*  93 */     debug1.putString("id", Registry.STRUCTURE_PIECE.getKey(getType()).toString());
/*  94 */     debug1.put("BB", (Tag)this.boundingBox.createTag());
/*  95 */     Direction debug2 = getOrientation();
/*  96 */     debug1.putInt("O", (debug2 == null) ? -1 : debug2.get2DDataValue());
/*  97 */     debug1.putInt("GD", this.genDepth);
/*     */     
/*  99 */     addAdditionalSaveData(debug1);
/*     */     
/* 101 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void addAdditionalSaveData(CompoundTag paramCompoundTag);
/*     */   
/*     */   public void addChildren(StructurePiece debug1, List<StructurePiece> debug2, Random debug3) {}
/*     */   
/*     */   public abstract boolean postProcess(WorldGenLevel paramWorldGenLevel, StructureFeatureManager paramStructureFeatureManager, ChunkGenerator paramChunkGenerator, Random paramRandom, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos);
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 112 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public int getGenDepth() {
/* 116 */     return this.genDepth;
/*     */   }
/*     */   
/*     */   public boolean isCloseToChunk(ChunkPos debug1, int debug2) {
/* 120 */     int debug3 = debug1.x << 4;
/* 121 */     int debug4 = debug1.z << 4;
/*     */     
/* 123 */     return this.boundingBox.intersects(debug3 - debug2, debug4 - debug2, debug3 + 15 + debug2, debug4 + 15 + debug2);
/*     */   }
/*     */   
/*     */   public static StructurePiece findCollisionPiece(List<StructurePiece> debug0, BoundingBox debug1) {
/* 127 */     for (StructurePiece debug3 : debug0) {
/* 128 */       if (debug3.getBoundingBox() != null && debug3.getBoundingBox().intersects(debug1)) {
/* 129 */         return debug3;
/*     */       }
/*     */     } 
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean edgesLiquid(BlockGetter debug1, BoundingBox debug2) {
/* 140 */     int debug3 = Math.max(this.boundingBox.x0 - 1, debug2.x0);
/* 141 */     int debug4 = Math.max(this.boundingBox.y0 - 1, debug2.y0);
/* 142 */     int debug5 = Math.max(this.boundingBox.z0 - 1, debug2.z0);
/* 143 */     int debug6 = Math.min(this.boundingBox.x1 + 1, debug2.x1);
/* 144 */     int debug7 = Math.min(this.boundingBox.y1 + 1, debug2.y1);
/* 145 */     int debug8 = Math.min(this.boundingBox.z1 + 1, debug2.z1);
/*     */     
/* 147 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/*     */     
/*     */     int debug10;
/* 150 */     for (debug10 = debug3; debug10 <= debug6; debug10++) {
/* 151 */       for (int debug11 = debug5; debug11 <= debug8; debug11++) {
/* 152 */         if (debug1.getBlockState((BlockPos)debug9.set(debug10, debug4, debug11)).getMaterial().isLiquid()) {
/* 153 */           return true;
/*     */         }
/* 155 */         if (debug1.getBlockState((BlockPos)debug9.set(debug10, debug7, debug11)).getMaterial().isLiquid()) {
/* 156 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     for (debug10 = debug3; debug10 <= debug6; debug10++) {
/* 162 */       for (int debug11 = debug4; debug11 <= debug7; debug11++) {
/* 163 */         if (debug1.getBlockState((BlockPos)debug9.set(debug10, debug11, debug5)).getMaterial().isLiquid()) {
/* 164 */           return true;
/*     */         }
/* 166 */         if (debug1.getBlockState((BlockPos)debug9.set(debug10, debug11, debug8)).getMaterial().isLiquid()) {
/* 167 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     for (debug10 = debug5; debug10 <= debug8; debug10++) {
/* 173 */       for (int debug11 = debug4; debug11 <= debug7; debug11++) {
/* 174 */         if (debug1.getBlockState((BlockPos)debug9.set(debug3, debug11, debug10)).getMaterial().isLiquid()) {
/* 175 */           return true;
/*     */         }
/* 177 */         if (debug1.getBlockState((BlockPos)debug9.set(debug6, debug11, debug10)).getMaterial().isLiquid()) {
/* 178 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getWorldX(int debug1, int debug2) {
/* 190 */     Direction debug3 = getOrientation();
/* 191 */     if (debug3 == null) {
/* 192 */       return debug1;
/*     */     }
/*     */     
/* 195 */     switch (debug3) {
/*     */       case NORTH:
/*     */       case SOUTH:
/* 198 */         return this.boundingBox.x0 + debug1;
/*     */       case WEST:
/* 200 */         return this.boundingBox.x1 - debug2;
/*     */       case EAST:
/* 202 */         return this.boundingBox.x0 + debug2;
/*     */     } 
/* 204 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getWorldY(int debug1) {
/* 209 */     if (getOrientation() == null) {
/* 210 */       return debug1;
/*     */     }
/* 212 */     return debug1 + this.boundingBox.y0;
/*     */   }
/*     */   
/*     */   protected int getWorldZ(int debug1, int debug2) {
/* 216 */     Direction debug3 = getOrientation();
/* 217 */     if (debug3 == null) {
/* 218 */       return debug2;
/*     */     }
/*     */     
/* 221 */     switch (debug3) {
/*     */       case NORTH:
/* 223 */         return this.boundingBox.z1 - debug2;
/*     */       case SOUTH:
/* 225 */         return this.boundingBox.z0 + debug2;
/*     */       case WEST:
/*     */       case EAST:
/* 228 */         return this.boundingBox.z0 + debug1;
/*     */     } 
/* 230 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/* 234 */   private static final Set<Block> SHAPE_CHECK_BLOCKS = (Set<Block>)ImmutableSet.builder()
/* 235 */     .add(Blocks.NETHER_BRICK_FENCE)
/* 236 */     .add(Blocks.TORCH)
/* 237 */     .add(Blocks.WALL_TORCH)
/* 238 */     .add(Blocks.OAK_FENCE)
/* 239 */     .add(Blocks.SPRUCE_FENCE)
/* 240 */     .add(Blocks.DARK_OAK_FENCE)
/* 241 */     .add(Blocks.ACACIA_FENCE)
/* 242 */     .add(Blocks.BIRCH_FENCE)
/* 243 */     .add(Blocks.JUNGLE_FENCE)
/* 244 */     .add(Blocks.LADDER)
/* 245 */     .add(Blocks.IRON_BARS)
/* 246 */     .build();
/*     */   
/*     */   protected void placeBlock(WorldGenLevel debug1, BlockState debug2, int debug3, int debug4, int debug5, BoundingBox debug6) {
/* 249 */     BlockPos debug7 = new BlockPos(getWorldX(debug3, debug5), getWorldY(debug4), getWorldZ(debug3, debug5));
/*     */     
/* 251 */     if (!debug6.isInside((Vec3i)debug7)) {
/*     */       return;
/*     */     }
/*     */     
/* 255 */     if (this.mirror != Mirror.NONE) {
/* 256 */       debug2 = debug2.mirror(this.mirror);
/*     */     }
/* 258 */     if (this.rotation != Rotation.NONE) {
/* 259 */       debug2 = debug2.rotate(this.rotation);
/*     */     }
/*     */     
/* 262 */     debug1.setBlock(debug7, debug2, 2);
/* 263 */     FluidState debug8 = debug1.getFluidState(debug7);
/* 264 */     if (!debug8.isEmpty()) {
/* 265 */       debug1.getLiquidTicks().scheduleTick(debug7, debug8.getType(), 0);
/*     */     }
/* 267 */     if (SHAPE_CHECK_BLOCKS.contains(debug2.getBlock())) {
/* 268 */       debug1.getChunk(debug7).markPosForPostprocessing(debug7);
/*     */     }
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
/*     */   protected BlockState getBlock(BlockGetter debug1, int debug2, int debug3, int debug4, BoundingBox debug5) {
/* 285 */     int debug6 = getWorldX(debug2, debug4);
/* 286 */     int debug7 = getWorldY(debug3);
/* 287 */     int debug8 = getWorldZ(debug2, debug4);
/*     */     
/* 289 */     BlockPos debug9 = new BlockPos(debug6, debug7, debug8);
/* 290 */     if (!debug5.isInside((Vec3i)debug9)) {
/* 291 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 294 */     return debug1.getBlockState(debug9);
/*     */   }
/*     */   
/*     */   protected boolean isInterior(LevelReader debug1, int debug2, int debug3, int debug4, BoundingBox debug5) {
/* 298 */     int debug6 = getWorldX(debug2, debug4);
/* 299 */     int debug7 = getWorldY(debug3 + 1);
/* 300 */     int debug8 = getWorldZ(debug2, debug4);
/*     */     
/* 302 */     BlockPos debug9 = new BlockPos(debug6, debug7, debug8);
/*     */     
/* 304 */     if (!debug5.isInside((Vec3i)debug9)) {
/* 305 */       return false;
/*     */     }
/*     */     
/* 308 */     return (debug7 < debug1.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, debug6, debug8));
/*     */   }
/*     */   
/*     */   protected void generateAirBox(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8) {
/* 312 */     for (int debug9 = debug4; debug9 <= debug7; debug9++) {
/* 313 */       for (int debug10 = debug3; debug10 <= debug6; debug10++) {
/* 314 */         for (int debug11 = debug5; debug11 <= debug8; debug11++) {
/* 315 */           placeBlock(debug1, Blocks.AIR.defaultBlockState(), debug10, debug9, debug11, debug2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BlockState debug9, BlockState debug10, boolean debug11) {
/* 322 */     for (int debug12 = debug4; debug12 <= debug7; debug12++) {
/* 323 */       for (int debug13 = debug3; debug13 <= debug6; debug13++) {
/* 324 */         for (int debug14 = debug5; debug14 <= debug8; debug14++) {
/* 325 */           if (!debug11 || !getBlock((BlockGetter)debug1, debug13, debug12, debug14, debug2).isAir())
/*     */           {
/*     */             
/* 328 */             if (debug12 == debug4 || debug12 == debug7 || debug13 == debug3 || debug13 == debug6 || debug14 == debug5 || debug14 == debug8) {
/* 329 */               placeBlock(debug1, debug9, debug13, debug12, debug14, debug2);
/*     */             } else {
/* 331 */               placeBlock(debug1, debug10, debug13, debug12, debug14, debug2);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateBox(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, boolean debug9, Random debug10, BlockSelector debug11) {
/* 343 */     for (int debug12 = debug4; debug12 <= debug7; debug12++) {
/* 344 */       for (int debug13 = debug3; debug13 <= debug6; debug13++) {
/* 345 */         for (int debug14 = debug5; debug14 <= debug8; debug14++) {
/* 346 */           if (!debug9 || !getBlock((BlockGetter)debug1, debug13, debug12, debug14, debug2).isAir()) {
/*     */ 
/*     */             
/* 349 */             debug11.next(debug10, debug13, debug12, debug14, (debug12 == debug4 || debug12 == debug7 || debug13 == debug3 || debug13 == debug6 || debug14 == debug5 || debug14 == debug8));
/* 350 */             placeBlock(debug1, debug11.getNext(), debug13, debug12, debug14, debug2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateMaybeBox(WorldGenLevel debug1, BoundingBox debug2, Random debug3, float debug4, int debug5, int debug6, int debug7, int debug8, int debug9, int debug10, BlockState debug11, BlockState debug12, boolean debug13, boolean debug14) {
/* 361 */     for (int debug15 = debug6; debug15 <= debug9; debug15++) {
/* 362 */       for (int debug16 = debug5; debug16 <= debug8; debug16++) {
/* 363 */         for (int debug17 = debug7; debug17 <= debug10; debug17++) {
/* 364 */           if (debug3.nextFloat() <= debug4)
/*     */           {
/*     */             
/* 367 */             if (!debug13 || !getBlock((BlockGetter)debug1, debug16, debug15, debug17, debug2).isAir())
/*     */             {
/*     */               
/* 370 */               if (!debug14 || isInterior((LevelReader)debug1, debug16, debug15, debug17, debug2))
/*     */               {
/*     */                 
/* 373 */                 if (debug15 == debug6 || debug15 == debug9 || debug16 == debug5 || debug16 == debug8 || debug17 == debug7 || debug17 == debug10) {
/* 374 */                   placeBlock(debug1, debug11, debug16, debug15, debug17, debug2);
/*     */                 } else {
/* 376 */                   placeBlock(debug1, debug12, debug16, debug15, debug17, debug2);
/*     */                 }  }  }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void maybeGenerateBlock(WorldGenLevel debug1, BoundingBox debug2, Random debug3, float debug4, int debug5, int debug6, int debug7, BlockState debug8) {
/* 384 */     if (debug3.nextFloat() < debug4) {
/* 385 */       placeBlock(debug1, debug8, debug5, debug6, debug7, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void generateUpperHalfSphere(WorldGenLevel debug1, BoundingBox debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BlockState debug9, boolean debug10) {
/* 390 */     float debug11 = (debug6 - debug3 + 1);
/* 391 */     float debug12 = (debug7 - debug4 + 1);
/* 392 */     float debug13 = (debug8 - debug5 + 1);
/*     */     
/* 394 */     float debug14 = debug3 + debug11 / 2.0F;
/* 395 */     float debug15 = debug5 + debug13 / 2.0F;
/*     */     
/* 397 */     for (int debug16 = debug4; debug16 <= debug7; debug16++) {
/* 398 */       float debug17 = (debug16 - debug4) / debug12;
/*     */       
/* 400 */       for (int debug18 = debug3; debug18 <= debug6; debug18++) {
/* 401 */         float debug19 = (debug18 - debug14) / debug11 * 0.5F;
/*     */         
/* 403 */         for (int debug20 = debug5; debug20 <= debug8; debug20++) {
/* 404 */           float debug21 = (debug20 - debug15) / debug13 * 0.5F;
/*     */           
/* 406 */           if (!debug10 || !getBlock((BlockGetter)debug1, debug18, debug16, debug20, debug2).isAir()) {
/*     */ 
/*     */ 
/*     */             
/* 410 */             float debug22 = debug19 * debug19 + debug17 * debug17 + debug21 * debug21;
/*     */             
/* 412 */             if (debug22 <= 1.05F) {
/* 413 */               placeBlock(debug1, debug9, debug18, debug16, debug20, debug2);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */   protected void fillColumnDown(WorldGenLevel debug1, BlockState debug2, int debug3, int debug4, int debug5, BoundingBox debug6) {
/* 434 */     int debug7 = getWorldX(debug3, debug5);
/* 435 */     int debug8 = getWorldY(debug4);
/* 436 */     int debug9 = getWorldZ(debug3, debug5);
/*     */     
/* 438 */     if (!debug6.isInside((Vec3i)new BlockPos(debug7, debug8, debug9))) {
/*     */       return;
/*     */     }
/*     */     
/* 442 */     while ((debug1.isEmptyBlock(new BlockPos(debug7, debug8, debug9)) || debug1.getBlockState(new BlockPos(debug7, debug8, debug9)).getMaterial().isLiquid()) && debug8 > 1) {
/* 443 */       debug1.setBlock(new BlockPos(debug7, debug8, debug9), debug2, 2);
/* 444 */       debug8--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean createChest(WorldGenLevel debug1, BoundingBox debug2, Random debug3, int debug4, int debug5, int debug6, ResourceLocation debug7) {
/* 449 */     BlockPos debug8 = new BlockPos(getWorldX(debug4, debug6), getWorldY(debug5), getWorldZ(debug4, debug6));
/* 450 */     return createChest((ServerLevelAccessor)debug1, debug2, debug3, debug8, debug7, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockState reorient(BlockGetter debug0, BlockPos debug1, BlockState debug2) {
/* 455 */     Direction debug3 = null;
/* 456 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 457 */       BlockPos debug6 = debug1.relative(direction);
/* 458 */       BlockState debug7 = debug0.getBlockState(debug6);
/* 459 */       if (debug7.is(Blocks.CHEST)) {
/* 460 */         return debug2;
/*     */       }
/* 462 */       if (debug7.isSolidRender(debug0, debug6)) {
/* 463 */         if (debug3 == null) {
/* 464 */           debug3 = direction; continue;
/*     */         } 
/* 466 */         debug3 = null;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 471 */     if (debug3 != null) {
/* 472 */       return (BlockState)debug2.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)debug3.getOpposite());
/*     */     }
/*     */ 
/*     */     
/* 476 */     Direction debug4 = (Direction)debug2.getValue((Property)HorizontalDirectionalBlock.FACING);
/* 477 */     BlockPos debug5 = debug1.relative(debug4);
/* 478 */     if (debug0.getBlockState(debug5).isSolidRender(debug0, debug5)) {
/* 479 */       debug4 = debug4.getOpposite();
/* 480 */       debug5 = debug1.relative(debug4);
/*     */     } 
/* 482 */     if (debug0.getBlockState(debug5).isSolidRender(debug0, debug5)) {
/* 483 */       debug4 = debug4.getClockWise();
/* 484 */       debug5 = debug1.relative(debug4);
/*     */     } 
/* 486 */     if (debug0.getBlockState(debug5).isSolidRender(debug0, debug5)) {
/* 487 */       debug4 = debug4.getOpposite();
/* 488 */       debug5 = debug1.relative(debug4);
/*     */     } 
/*     */     
/* 491 */     return (BlockState)debug2.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean createChest(ServerLevelAccessor debug1, BoundingBox debug2, Random debug3, BlockPos debug4, ResourceLocation debug5, @Nullable BlockState debug6) {
/* 498 */     if (!debug2.isInside((Vec3i)debug4) || debug1.getBlockState(debug4).is(Blocks.CHEST)) {
/* 499 */       return false;
/*     */     }
/*     */     
/* 502 */     if (debug6 == null) {
/* 503 */       debug6 = reorient((BlockGetter)debug1, debug4, Blocks.CHEST.defaultBlockState());
/*     */     }
/* 505 */     debug1.setBlock(debug4, debug6, 2);
/*     */     
/* 507 */     BlockEntity debug7 = debug1.getBlockEntity(debug4);
/* 508 */     if (debug7 instanceof ChestBlockEntity) {
/* 509 */       ((ChestBlockEntity)debug7).setLootTable(debug5, debug3.nextLong());
/*     */     }
/* 511 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean createDispenser(WorldGenLevel debug1, BoundingBox debug2, Random debug3, int debug4, int debug5, int debug6, Direction debug7, ResourceLocation debug8) {
/* 515 */     BlockPos debug9 = new BlockPos(getWorldX(debug4, debug6), getWorldY(debug5), getWorldZ(debug4, debug6));
/*     */     
/* 517 */     if (debug2.isInside((Vec3i)debug9) && 
/* 518 */       !debug1.getBlockState(debug9).is(Blocks.DISPENSER)) {
/* 519 */       placeBlock(debug1, (BlockState)Blocks.DISPENSER.defaultBlockState().setValue((Property)DispenserBlock.FACING, (Comparable)debug7), debug4, debug5, debug6, debug2);
/*     */       
/* 521 */       BlockEntity debug10 = debug1.getBlockEntity(debug9);
/* 522 */       if (debug10 instanceof DispenserBlockEntity) {
/* 523 */         ((DispenserBlockEntity)debug10).setLootTable(debug8, debug3.nextLong());
/*     */       }
/* 525 */       return true;
/*     */     } 
/*     */     
/* 528 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(int debug1, int debug2, int debug3) {
/* 537 */     this.boundingBox.move(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Direction getOrientation() {
/* 542 */     return this.orientation;
/*     */   }
/*     */   
/*     */   public void setOrientation(@Nullable Direction debug1) {
/* 546 */     this.orientation = debug1;
/* 547 */     if (debug1 == null) {
/* 548 */       this.rotation = Rotation.NONE;
/* 549 */       this.mirror = Mirror.NONE;
/*     */     } else {
/* 551 */       switch (debug1) {
/*     */         case SOUTH:
/* 553 */           this.mirror = Mirror.LEFT_RIGHT;
/* 554 */           this.rotation = Rotation.NONE;
/*     */           return;
/*     */         case WEST:
/* 557 */           this.mirror = Mirror.LEFT_RIGHT;
/* 558 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */         case EAST:
/* 561 */           this.mirror = Mirror.NONE;
/* 562 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */       } 
/* 565 */       this.mirror = Mirror.NONE;
/* 566 */       this.rotation = Rotation.NONE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 573 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructurePieceType getType() {
/* 581 */     return this.type;
/*     */   }
/*     */   
/*     */   public static abstract class BlockSelector {
/* 585 */     protected BlockState next = Blocks.AIR.defaultBlockState();
/*     */     
/*     */     public abstract void next(Random param1Random, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
/*     */     
/*     */     public BlockState getNext() {
/* 590 */       return this.next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructurePiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */