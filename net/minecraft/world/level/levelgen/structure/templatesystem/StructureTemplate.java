/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.decoration.Painting;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.EmptyBlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
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
/*     */ public class StructureTemplate
/*     */ {
/*  68 */   private final List<Palette> palettes = Lists.newArrayList();
/*  69 */   private final List<StructureEntityInfo> entityInfoList = Lists.newArrayList();
/*  70 */   private BlockPos size = BlockPos.ZERO;
/*  71 */   private String author = "?";
/*     */   
/*     */   public BlockPos getSize() {
/*  74 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setAuthor(String debug1) {
/*  78 */     this.author = debug1;
/*     */   }
/*     */   
/*     */   public String getAuthor() {
/*  82 */     return this.author;
/*     */   }
/*     */   
/*     */   public void fillFromWorld(Level debug1, BlockPos debug2, BlockPos debug3, boolean debug4, @Nullable Block debug5) {
/*  86 */     if (debug3.getX() < 1 || debug3.getY() < 1 || debug3.getZ() < 1) {
/*     */       return;
/*     */     }
/*  89 */     BlockPos debug6 = debug2.offset((Vec3i)debug3).offset(-1, -1, -1);
/*  90 */     List<StructureBlockInfo> debug7 = Lists.newArrayList();
/*  91 */     List<StructureBlockInfo> debug8 = Lists.newArrayList();
/*  92 */     List<StructureBlockInfo> debug9 = Lists.newArrayList();
/*     */     
/*  94 */     BlockPos debug10 = new BlockPos(Math.min(debug2.getX(), debug6.getX()), Math.min(debug2.getY(), debug6.getY()), Math.min(debug2.getZ(), debug6.getZ()));
/*  95 */     BlockPos debug11 = new BlockPos(Math.max(debug2.getX(), debug6.getX()), Math.max(debug2.getY(), debug6.getY()), Math.max(debug2.getZ(), debug6.getZ()));
/*  96 */     this.size = debug3;
/*     */     
/*  98 */     for (BlockPos debug13 : BlockPos.betweenClosed(debug10, debug11)) {
/*  99 */       StructureBlockInfo debug17; BlockPos debug14 = debug13.subtract((Vec3i)debug10);
/* 100 */       BlockState debug15 = debug1.getBlockState(debug13);
/* 101 */       if (debug5 != null && debug5 == debug15.getBlock()) {
/*     */         continue;
/*     */       }
/* 104 */       BlockEntity debug16 = debug1.getBlockEntity(debug13);
/*     */ 
/*     */ 
/*     */       
/* 108 */       if (debug16 != null) {
/* 109 */         CompoundTag debug18 = debug16.save(new CompoundTag());
/* 110 */         debug18.remove("x");
/* 111 */         debug18.remove("y");
/* 112 */         debug18.remove("z");
/* 113 */         debug17 = new StructureBlockInfo(debug14, debug15, debug18.copy());
/*     */       } else {
/* 115 */         debug17 = new StructureBlockInfo(debug14, debug15, null);
/*     */       } 
/*     */       
/* 118 */       addToLists(debug17, debug7, debug8, debug9);
/*     */     } 
/* 120 */     List<StructureBlockInfo> debug12 = buildInfoList(debug7, debug8, debug9);
/*     */     
/* 122 */     this.palettes.clear();
/* 123 */     this.palettes.add(new Palette(debug12));
/*     */     
/* 125 */     if (debug4) {
/* 126 */       fillEntityList(debug1, debug10, debug11.offset(1, 1, 1));
/*     */     } else {
/* 128 */       this.entityInfoList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addToLists(StructureBlockInfo debug0, List<StructureBlockInfo> debug1, List<StructureBlockInfo> debug2, List<StructureBlockInfo> debug3) {
/* 133 */     if (debug0.nbt != null) {
/* 134 */       debug2.add(debug0);
/* 135 */     } else if (!debug0.state.getBlock().hasDynamicShape() && debug0.state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
/* 136 */       debug1.add(debug0);
/*     */     } else {
/* 138 */       debug3.add(debug0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<StructureBlockInfo> buildInfoList(List<StructureBlockInfo> debug0, List<StructureBlockInfo> debug1, List<StructureBlockInfo> debug2) {
/* 144 */     Comparator<StructureBlockInfo> debug3 = Comparator.<StructureBlockInfo>comparingInt(debug0 -> debug0.pos.getY()).thenComparingInt(debug0 -> debug0.pos.getX()).thenComparingInt(debug0 -> debug0.pos.getZ());
/* 145 */     debug0.sort(debug3);
/* 146 */     debug2.sort(debug3);
/* 147 */     debug1.sort(debug3);
/*     */     
/* 149 */     List<StructureBlockInfo> debug4 = Lists.newArrayList();
/* 150 */     debug4.addAll(debug0);
/* 151 */     debug4.addAll(debug2);
/* 152 */     debug4.addAll(debug1);
/* 153 */     return debug4;
/*     */   }
/*     */   
/*     */   private void fillEntityList(Level debug1, BlockPos debug2, BlockPos debug3) {
/* 157 */     List<Entity> debug4 = debug1.getEntitiesOfClass(Entity.class, new AABB(debug2, debug3), debug0 -> !(debug0 instanceof net.minecraft.world.entity.player.Player));
/* 158 */     this.entityInfoList.clear();
/* 159 */     for (Entity debug6 : debug4) {
/* 160 */       BlockPos debug9; Vec3 debug7 = new Vec3(debug6.getX() - debug2.getX(), debug6.getY() - debug2.getY(), debug6.getZ() - debug2.getZ());
/* 161 */       CompoundTag debug8 = new CompoundTag();
/* 162 */       debug6.save(debug8);
/*     */       
/* 164 */       if (debug6 instanceof Painting) {
/* 165 */         debug9 = ((Painting)debug6).getPos().subtract((Vec3i)debug2);
/*     */       } else {
/* 167 */         debug9 = new BlockPos(debug7);
/*     */       } 
/* 169 */       this.entityInfoList.add(new StructureEntityInfo(debug7, debug9, debug8.copy()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<StructureBlockInfo> filterBlocks(BlockPos debug1, StructurePlaceSettings debug2, Block debug3) {
/* 174 */     return filterBlocks(debug1, debug2, debug3, true);
/*     */   }
/*     */   
/*     */   public List<StructureBlockInfo> filterBlocks(BlockPos debug1, StructurePlaceSettings debug2, Block debug3, boolean debug4) {
/* 178 */     List<StructureBlockInfo> debug5 = Lists.newArrayList();
/* 179 */     BoundingBox debug6 = debug2.getBoundingBox();
/*     */     
/* 181 */     if (this.palettes.isEmpty()) {
/* 182 */       return Collections.emptyList();
/*     */     }
/* 184 */     for (StructureBlockInfo debug8 : debug2.getRandomPalette(this.palettes, debug1).blocks(debug3)) {
/* 185 */       BlockPos debug9 = debug4 ? calculateRelativePosition(debug2, debug8.pos).offset((Vec3i)debug1) : debug8.pos;
/* 186 */       if (debug6 != null && !debug6.isInside((Vec3i)debug9)) {
/*     */         continue;
/*     */       }
/* 189 */       debug5.add(new StructureBlockInfo(debug9, debug8.state.rotate(debug2.getRotation()), debug8.nbt));
/*     */     } 
/* 191 */     return debug5;
/*     */   }
/*     */   
/*     */   public BlockPos calculateConnectedPosition(StructurePlaceSettings debug1, BlockPos debug2, StructurePlaceSettings debug3, BlockPos debug4) {
/* 195 */     BlockPos debug5 = calculateRelativePosition(debug1, debug2);
/* 196 */     BlockPos debug6 = calculateRelativePosition(debug3, debug4);
/* 197 */     return debug5.subtract((Vec3i)debug6);
/*     */   }
/*     */   
/*     */   public static BlockPos calculateRelativePosition(StructurePlaceSettings debug0, BlockPos debug1) {
/* 201 */     return transform(debug1, debug0.getMirror(), debug0.getRotation(), debug0.getRotationPivot());
/*     */   }
/*     */   
/*     */   public void placeInWorldChunk(ServerLevelAccessor debug1, BlockPos debug2, StructurePlaceSettings debug3, Random debug4) {
/* 205 */     debug3.updateBoundingBoxFromChunkPos();
/* 206 */     placeInWorld(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public void placeInWorld(ServerLevelAccessor debug1, BlockPos debug2, StructurePlaceSettings debug3, Random debug4) {
/* 210 */     placeInWorld(debug1, debug2, debug2, debug3, debug4, 2);
/*     */   }
/*     */   
/*     */   public boolean placeInWorld(ServerLevelAccessor debug1, BlockPos debug2, BlockPos debug3, StructurePlaceSettings debug4, Random debug5, int debug6) {
/* 214 */     if (this.palettes.isEmpty()) {
/* 215 */       return false;
/*     */     }
/* 217 */     List<StructureBlockInfo> debug7 = debug4.getRandomPalette(this.palettes, debug2).blocks();
/* 218 */     if ((debug7.isEmpty() && (debug4.isIgnoreEntities() || this.entityInfoList.isEmpty())) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     BoundingBox debug8 = debug4.getBoundingBox();
/* 223 */     List<BlockPos> debug9 = Lists.newArrayListWithCapacity(debug4.shouldKeepLiquids() ? debug7.size() : 0);
/* 224 */     List<Pair<BlockPos, CompoundTag>> debug10 = Lists.newArrayListWithCapacity(debug7.size());
/*     */     
/* 226 */     int debug11 = Integer.MAX_VALUE;
/* 227 */     int debug12 = Integer.MAX_VALUE;
/* 228 */     int debug13 = Integer.MAX_VALUE;
/*     */     
/* 230 */     int debug14 = Integer.MIN_VALUE;
/* 231 */     int debug15 = Integer.MIN_VALUE;
/* 232 */     int debug16 = Integer.MIN_VALUE;
/*     */     
/* 234 */     List<StructureBlockInfo> debug17 = processBlockInfos((LevelAccessor)debug1, debug2, debug3, debug4, debug7);
/*     */     
/* 236 */     for (StructureBlockInfo structureBlockInfo : debug17) {
/* 237 */       BlockPos debug20 = structureBlockInfo.pos;
/*     */       
/* 239 */       if (debug8 != null && !debug8.isInside((Vec3i)debug20)) {
/*     */         continue;
/*     */       }
/*     */       
/* 243 */       FluidState debug21 = debug4.shouldKeepLiquids() ? debug1.getFluidState(debug20) : null;
/* 244 */       BlockState debug22 = structureBlockInfo.state.mirror(debug4.getMirror()).rotate(debug4.getRotation());
/*     */       
/* 246 */       if (structureBlockInfo.nbt != null) {
/*     */ 
/*     */         
/* 249 */         BlockEntity debug23 = debug1.getBlockEntity(debug20);
/* 250 */         Clearable.tryClear(debug23);
/*     */ 
/*     */         
/* 253 */         debug1.setBlock(debug20, Blocks.BARRIER.defaultBlockState(), 20);
/*     */       } 
/* 255 */       if (debug1.setBlock(debug20, debug22, debug6)) {
/* 256 */         debug11 = Math.min(debug11, debug20.getX());
/* 257 */         debug12 = Math.min(debug12, debug20.getY());
/* 258 */         debug13 = Math.min(debug13, debug20.getZ());
/*     */         
/* 260 */         debug14 = Math.max(debug14, debug20.getX());
/* 261 */         debug15 = Math.max(debug15, debug20.getY());
/* 262 */         debug16 = Math.max(debug16, debug20.getZ());
/* 263 */         debug10.add(Pair.of(debug20, structureBlockInfo.nbt));
/*     */         
/* 265 */         if (structureBlockInfo.nbt != null) {
/* 266 */           BlockEntity debug23 = debug1.getBlockEntity(debug20);
/* 267 */           if (debug23 != null) {
/* 268 */             structureBlockInfo.nbt.putInt("x", debug20.getX());
/* 269 */             structureBlockInfo.nbt.putInt("y", debug20.getY());
/* 270 */             structureBlockInfo.nbt.putInt("z", debug20.getZ());
/*     */             
/* 272 */             if (debug23 instanceof net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity) {
/* 273 */               structureBlockInfo.nbt.putLong("LootTableSeed", debug5.nextLong());
/*     */             }
/* 275 */             debug23.load(structureBlockInfo.state, structureBlockInfo.nbt);
/* 276 */             debug23.mirror(debug4.getMirror());
/* 277 */             debug23.rotate(debug4.getRotation());
/*     */           } 
/*     */         } 
/* 280 */         if (debug21 != null && 
/* 281 */           debug22.getBlock() instanceof LiquidBlockContainer) {
/* 282 */           ((LiquidBlockContainer)debug22.getBlock()).placeLiquid((LevelAccessor)debug1, debug20, debug22, debug21);
/* 283 */           if (!debug21.isSource()) {
/* 284 */             debug9.add(debug20);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 291 */     boolean debug18 = true;
/* 292 */     Direction[] debug19 = { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
/*     */     
/* 294 */     while (debug18 && !debug9.isEmpty()) {
/* 295 */       debug18 = false;
/* 296 */       for (Iterator<BlockPos> debug20 = debug9.iterator(); debug20.hasNext(); ) {
/* 297 */         BlockPos debug21 = debug20.next();
/*     */ 
/*     */         
/* 300 */         BlockPos debug22 = debug21;
/* 301 */         FluidState debug23 = debug1.getFluidState(debug22);
/* 302 */         for (int debug24 = 0; debug24 < debug19.length && !debug23.isSource(); debug24++) {
/* 303 */           BlockPos debug25 = debug22.relative(debug19[debug24]);
/* 304 */           FluidState debug26 = debug1.getFluidState(debug25);
/* 305 */           if (debug26.getHeight((BlockGetter)debug1, debug25) > debug23.getHeight((BlockGetter)debug1, debug22) || (debug26.isSource() && !debug23.isSource())) {
/* 306 */             debug23 = debug26;
/* 307 */             debug22 = debug25;
/*     */           } 
/*     */         } 
/*     */         
/* 311 */         if (debug23.isSource()) {
/* 312 */           BlockState blockState = debug1.getBlockState(debug21);
/* 313 */           Block debug25 = blockState.getBlock();
/* 314 */           if (debug25 instanceof LiquidBlockContainer) {
/* 315 */             ((LiquidBlockContainer)debug25).placeLiquid((LevelAccessor)debug1, debug21, blockState, debug23);
/* 316 */             debug18 = true;
/* 317 */             debug20.remove();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 323 */     if (debug11 <= debug14) {
/* 324 */       if (!debug4.getKnownShape()) {
/* 325 */         BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(debug14 - debug11 + 1, debug15 - debug12 + 1, debug16 - debug13 + 1);
/*     */         
/* 327 */         int debug21 = debug11;
/* 328 */         int debug22 = debug12;
/* 329 */         int debug23 = debug13;
/*     */         
/* 331 */         for (Pair<BlockPos, CompoundTag> debug25 : debug10) {
/* 332 */           BlockPos debug26 = (BlockPos)debug25.getFirst();
/* 333 */           bitSetDiscreteVoxelShape.setFull(debug26.getX() - debug21, debug26.getY() - debug22, debug26.getZ() - debug23, true, true);
/*     */         } 
/*     */         
/* 336 */         updateShapeAtEdge((LevelAccessor)debug1, debug6, (DiscreteVoxelShape)bitSetDiscreteVoxelShape, debug21, debug22, debug23);
/*     */       } 
/*     */       
/* 339 */       for (Pair<BlockPos, CompoundTag> debug21 : debug10) {
/* 340 */         BlockPos debug22 = (BlockPos)debug21.getFirst();
/* 341 */         if (!debug4.getKnownShape()) {
/* 342 */           BlockState debug23 = debug1.getBlockState(debug22);
/* 343 */           BlockState debug24 = Block.updateFromNeighbourShapes(debug23, (LevelAccessor)debug1, debug22);
/* 344 */           if (debug23 != debug24) {
/* 345 */             debug1.setBlock(debug22, debug24, debug6 & 0xFFFFFFFE | 0x10);
/*     */           }
/* 347 */           debug1.blockUpdated(debug22, debug24.getBlock());
/*     */         } 
/*     */         
/* 350 */         if (debug21.getSecond() != null) {
/* 351 */           BlockEntity debug23 = debug1.getBlockEntity(debug22);
/* 352 */           if (debug23 != null) {
/* 353 */             debug23.setChanged();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 359 */     if (!debug4.isIgnoreEntities()) {
/* 360 */       placeEntities(debug1, debug2, debug4.getMirror(), debug4.getRotation(), debug4.getRotationPivot(), debug8, debug4.shouldFinalizeEntities());
/*     */     }
/*     */     
/* 363 */     return true;
/*     */   }
/*     */   
/*     */   public static void updateShapeAtEdge(LevelAccessor debug0, int debug1, DiscreteVoxelShape debug2, int debug3, int debug4, int debug5) {
/* 367 */     debug2.forAllFaces((debug5, debug6, debug7, debug8) -> {
/*     */           BlockPos debug9 = new BlockPos(debug0 + debug6, debug1 + debug7, debug2 + debug8);
/*     */           BlockPos debug10 = debug9.relative(debug5);
/*     */           BlockState debug11 = debug3.getBlockState(debug9);
/*     */           BlockState debug12 = debug3.getBlockState(debug10);
/*     */           BlockState debug13 = debug11.updateShape(debug5, debug12, debug3, debug9, debug10);
/*     */           if (debug11 != debug13) {
/*     */             debug3.setBlock(debug9, debug13, debug4 & 0xFFFFFFFE);
/*     */           }
/*     */           BlockState debug14 = debug12.updateShape(debug5.getOpposite(), debug13, debug3, debug10, debug9);
/*     */           if (debug12 != debug14) {
/*     */             debug3.setBlock(debug10, debug14, debug4 & 0xFFFFFFFE);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static List<StructureBlockInfo> processBlockInfos(LevelAccessor debug0, BlockPos debug1, BlockPos debug2, StructurePlaceSettings debug3, List<StructureBlockInfo> debug4) {
/* 384 */     List<StructureBlockInfo> debug5 = Lists.newArrayList();
/* 385 */     for (StructureBlockInfo debug7 : debug4) {
/* 386 */       BlockPos debug8 = calculateRelativePosition(debug3, debug7.pos).offset((Vec3i)debug1);
/* 387 */       StructureBlockInfo debug9 = new StructureBlockInfo(debug8, debug7.state, (debug7.nbt != null) ? debug7.nbt.copy() : null);
/*     */       
/* 389 */       Iterator<StructureProcessor> debug10 = debug3.getProcessors().iterator();
/* 390 */       while (debug9 != null && debug10.hasNext()) {
/* 391 */         debug9 = ((StructureProcessor)debug10.next()).processBlock((LevelReader)debug0, debug1, debug2, debug7, debug9, debug3);
/*     */       }
/*     */       
/* 394 */       if (debug9 != null) {
/* 395 */         debug5.add(debug9);
/*     */       }
/*     */     } 
/* 398 */     return debug5;
/*     */   }
/*     */   
/*     */   private void placeEntities(ServerLevelAccessor debug1, BlockPos debug2, Mirror debug3, Rotation debug4, BlockPos debug5, @Nullable BoundingBox debug6, boolean debug7) {
/* 402 */     for (StructureEntityInfo debug9 : this.entityInfoList) {
/* 403 */       BlockPos debug10 = transform(debug9.blockPos, debug3, debug4, debug5).offset((Vec3i)debug2);
/* 404 */       if (debug6 != null && !debug6.isInside((Vec3i)debug10)) {
/*     */         continue;
/*     */       }
/*     */       
/* 408 */       CompoundTag debug11 = debug9.nbt.copy();
/* 409 */       Vec3 debug12 = transform(debug9.pos, debug3, debug4, debug5);
/* 410 */       Vec3 debug13 = debug12.add(debug2.getX(), debug2.getY(), debug2.getZ());
/*     */       
/* 412 */       ListTag debug14 = new ListTag();
/* 413 */       debug14.add(DoubleTag.valueOf(debug13.x));
/* 414 */       debug14.add(DoubleTag.valueOf(debug13.y));
/* 415 */       debug14.add(DoubleTag.valueOf(debug13.z));
/* 416 */       debug11.put("Pos", (Tag)debug14);
/*     */       
/* 418 */       debug11.remove("UUID");
/*     */       
/* 420 */       createEntityIgnoreException(debug1, debug11).ifPresent(debug6 -> {
/*     */             float debug7 = debug6.mirror(debug0);
/*     */             debug7 += debug6.yRot - debug6.rotate(debug1);
/*     */             debug6.moveTo(debug2.x, debug2.y, debug2.z, debug7, debug6.xRot);
/*     */             if (debug3 && debug6 instanceof Mob) {
/*     */               ((Mob)debug6).finalizeSpawn(debug4, debug4.getCurrentDifficultyAt(new BlockPos(debug2)), MobSpawnType.STRUCTURE, null, debug5);
/*     */             }
/*     */             debug4.addFreshEntityWithPassengers(debug6);
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<Entity> createEntityIgnoreException(ServerLevelAccessor debug0, CompoundTag debug1) {
/*     */     try {
/* 435 */       return EntityType.create(debug1, (Level)debug0.getLevel());
/* 436 */     } catch (Exception debug2) {
/* 437 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockPos getSize(Rotation debug1) {
/* 442 */     switch (debug1) {
/*     */       case LEFT_RIGHT:
/*     */       case FRONT_BACK:
/* 445 */         return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());
/*     */     } 
/* 447 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockPos transform(BlockPos debug0, Mirror debug1, Rotation debug2, BlockPos debug3) {
/* 452 */     int debug4 = debug0.getX();
/* 453 */     int debug5 = debug0.getY();
/* 454 */     int debug6 = debug0.getZ();
/*     */     
/* 456 */     boolean debug7 = true;
/* 457 */     switch (debug1) {
/*     */       case LEFT_RIGHT:
/* 459 */         debug6 = -debug6;
/*     */         break;
/*     */       case FRONT_BACK:
/* 462 */         debug4 = -debug4;
/*     */         break;
/*     */       default:
/* 465 */         debug7 = false;
/*     */         break;
/*     */     } 
/*     */     
/* 469 */     int debug8 = debug3.getX();
/* 470 */     int debug9 = debug3.getZ();
/* 471 */     switch (debug2) {
/*     */       case NONE:
/* 473 */         return new BlockPos(debug8 + debug8 - debug4, debug5, debug9 + debug9 - debug6);
/*     */       case LEFT_RIGHT:
/* 475 */         return new BlockPos(debug8 - debug9 + debug6, debug5, debug8 + debug9 - debug4);
/*     */       case FRONT_BACK:
/* 477 */         return new BlockPos(debug8 + debug9 - debug6, debug5, debug9 - debug8 + debug4);
/*     */     } 
/* 479 */     return debug7 ? new BlockPos(debug4, debug5, debug6) : debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 transform(Vec3 debug0, Mirror debug1, Rotation debug2, BlockPos debug3) {
/* 484 */     double debug4 = debug0.x;
/* 485 */     double debug6 = debug0.y;
/* 486 */     double debug8 = debug0.z;
/*     */     
/* 488 */     boolean debug10 = true;
/* 489 */     switch (debug1) {
/*     */       case LEFT_RIGHT:
/* 491 */         debug8 = 1.0D - debug8;
/*     */         break;
/*     */       case FRONT_BACK:
/* 494 */         debug4 = 1.0D - debug4;
/*     */         break;
/*     */       default:
/* 497 */         debug10 = false;
/*     */         break;
/*     */     } 
/*     */     
/* 501 */     int debug11 = debug3.getX();
/* 502 */     int debug12 = debug3.getZ();
/* 503 */     switch (debug2) {
/*     */       case NONE:
/* 505 */         return new Vec3((debug11 + debug11 + 1) - debug4, debug6, (debug12 + debug12 + 1) - debug8);
/*     */       case LEFT_RIGHT:
/* 507 */         return new Vec3((debug11 - debug12) + debug8, debug6, (debug11 + debug12 + 1) - debug4);
/*     */       case FRONT_BACK:
/* 509 */         return new Vec3((debug11 + debug12 + 1) - debug8, debug6, (debug12 - debug11) + debug4);
/*     */     } 
/* 511 */     return debug10 ? new Vec3(debug4, debug6, debug8) : debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getZeroPositionWithTransform(BlockPos debug1, Mirror debug2, Rotation debug3) {
/* 516 */     return getZeroPositionWithTransform(debug1, debug2, debug3, getSize().getX(), getSize().getZ());
/*     */   }
/*     */   
/*     */   public static BlockPos getZeroPositionWithTransform(BlockPos debug0, Mirror debug1, Rotation debug2, int debug3, int debug4) {
/* 520 */     debug3--;
/* 521 */     debug4--;
/*     */     
/* 523 */     int debug5 = (debug1 == Mirror.FRONT_BACK) ? debug3 : 0;
/* 524 */     int debug6 = (debug1 == Mirror.LEFT_RIGHT) ? debug4 : 0;
/*     */     
/* 526 */     BlockPos debug7 = debug0;
/*     */     
/* 528 */     switch (debug2) {
/*     */       case null:
/* 530 */         debug7 = debug0.offset(debug5, 0, debug6);
/*     */         break;
/*     */       case FRONT_BACK:
/* 533 */         debug7 = debug0.offset(debug4 - debug6, 0, debug5);
/*     */         break;
/*     */       case NONE:
/* 536 */         debug7 = debug0.offset(debug3 - debug5, 0, debug4 - debug6);
/*     */         break;
/*     */       case LEFT_RIGHT:
/* 539 */         debug7 = debug0.offset(debug6, 0, debug3 - debug5);
/*     */         break;
/*     */     } 
/* 542 */     return debug7;
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(StructurePlaceSettings debug1, BlockPos debug2) {
/* 546 */     return getBoundingBox(debug2, debug1.getRotation(), debug1.getRotationPivot(), debug1.getMirror());
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(BlockPos debug1, Rotation debug2, BlockPos debug3, Mirror debug4) {
/* 550 */     BlockPos debug5 = getSize(debug2);
/*     */     
/* 552 */     int debug6 = debug3.getX();
/* 553 */     int debug7 = debug3.getZ();
/* 554 */     int debug8 = debug5.getX() - 1;
/* 555 */     int debug9 = debug5.getY() - 1;
/* 556 */     int debug10 = debug5.getZ() - 1;
/*     */     
/* 558 */     BoundingBox debug11 = new BoundingBox(0, 0, 0, 0, 0, 0);
/* 559 */     switch (debug2) {
/*     */       case null:
/* 561 */         debug11 = new BoundingBox(0, 0, 0, debug8, debug9, debug10);
/*     */         break;
/*     */       case NONE:
/* 564 */         debug11 = new BoundingBox(debug6 + debug6 - debug8, 0, debug7 + debug7 - debug10, debug6 + debug6, debug9, debug7 + debug7);
/*     */         break;
/*     */       case LEFT_RIGHT:
/* 567 */         debug11 = new BoundingBox(debug6 - debug7, 0, debug6 + debug7 - debug10, debug6 - debug7 + debug8, debug9, debug6 + debug7);
/*     */         break;
/*     */       case FRONT_BACK:
/* 570 */         debug11 = new BoundingBox(debug6 + debug7 - debug8, 0, debug7 - debug6, debug6 + debug7, debug9, debug7 - debug6 + debug10);
/*     */         break;
/*     */     } 
/* 573 */     switch (debug4) {
/*     */ 
/*     */       
/*     */       case FRONT_BACK:
/* 577 */         mirrorAABB(debug2, debug8, debug10, debug11, Direction.WEST, Direction.EAST);
/*     */         break;
/*     */       case LEFT_RIGHT:
/* 580 */         mirrorAABB(debug2, debug10, debug8, debug11, Direction.NORTH, Direction.SOUTH);
/*     */         break;
/*     */     } 
/* 583 */     debug11.move(debug1.getX(), debug1.getY(), debug1.getZ());
/* 584 */     return debug11;
/*     */   }
/*     */   
/*     */   private void mirrorAABB(Rotation debug1, int debug2, int debug3, BoundingBox debug4, Direction debug5, Direction debug6) {
/* 588 */     BlockPos debug7 = BlockPos.ZERO;
/* 589 */     if (debug1 == Rotation.CLOCKWISE_90 || debug1 == Rotation.COUNTERCLOCKWISE_90) {
/* 590 */       debug7 = debug7.relative(debug1.rotate(debug5), debug3);
/* 591 */     } else if (debug1 == Rotation.CLOCKWISE_180) {
/* 592 */       debug7 = debug7.relative(debug6, debug2);
/*     */     } else {
/* 594 */       debug7 = debug7.relative(debug5, debug2);
/*     */     } 
/* 596 */     debug4.move(debug7.getX(), 0, debug7.getZ());
/*     */   }
/*     */   
/*     */   static class SimplePalette implements Iterable<BlockState> {
/* 600 */     public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */     
/* 602 */     private final IdMapper<BlockState> ids = new IdMapper(16);
/*     */     private int lastId;
/*     */     
/*     */     public int idFor(BlockState debug1) {
/* 606 */       int debug2 = this.ids.getId(debug1);
/* 607 */       if (debug2 == -1) {
/* 608 */         debug2 = this.lastId++;
/* 609 */         this.ids.addMapping(debug1, debug2);
/*     */       } 
/*     */       
/* 612 */       return debug2;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public BlockState stateFor(int debug1) {
/* 617 */       BlockState debug2 = (BlockState)this.ids.byId(debug1);
/* 618 */       return (debug2 == null) ? DEFAULT_BLOCK_STATE : debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<BlockState> iterator() {
/* 623 */       return this.ids.iterator();
/*     */     }
/*     */     private SimplePalette() {}
/*     */     public void addMapping(BlockState debug1, int debug2) {
/* 627 */       this.ids.addMapping(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 632 */     if (this.palettes.isEmpty()) {
/* 633 */       debug1.put("blocks", (Tag)new ListTag());
/* 634 */       debug1.put("palette", (Tag)new ListTag());
/*     */     } else {
/* 636 */       List<SimplePalette> list = Lists.newArrayList();
/* 637 */       SimplePalette debug3 = new SimplePalette();
/* 638 */       list.add(debug3);
/*     */       
/* 640 */       for (int i = 1; i < this.palettes.size(); i++) {
/* 641 */         list.add(new SimplePalette());
/*     */       }
/*     */       
/* 644 */       ListTag debug4 = new ListTag();
/* 645 */       List<StructureBlockInfo> debug5 = ((Palette)this.palettes.get(0)).blocks();
/* 646 */       for (int debug6 = 0; debug6 < debug5.size(); debug6++) {
/* 647 */         StructureBlockInfo debug7 = debug5.get(debug6);
/* 648 */         CompoundTag debug8 = new CompoundTag();
/* 649 */         debug8.put("pos", (Tag)newIntegerList(new int[] { debug7.pos.getX(), debug7.pos.getY(), debug7.pos.getZ() }));
/* 650 */         int debug9 = debug3.idFor(debug7.state);
/* 651 */         debug8.putInt("state", debug9);
/* 652 */         if (debug7.nbt != null) {
/* 653 */           debug8.put("nbt", (Tag)debug7.nbt);
/*     */         }
/* 655 */         debug4.add(debug8);
/*     */         
/* 657 */         for (int debug10 = 1; debug10 < this.palettes.size(); debug10++) {
/* 658 */           SimplePalette debug11 = list.get(debug10);
/* 659 */           debug11.addMapping(((StructureBlockInfo)((Palette)this.palettes.get(debug10)).blocks().get(debug6)).state, debug9);
/*     */         } 
/*     */       } 
/* 662 */       debug1.put("blocks", (Tag)debug4);
/*     */       
/* 664 */       if (list.size() == 1) {
/* 665 */         ListTag listTag = new ListTag();
/* 666 */         for (BlockState debug8 : debug3) {
/* 667 */           listTag.add(NbtUtils.writeBlockState(debug8));
/*     */         }
/* 669 */         debug1.put("palette", (Tag)listTag);
/*     */       } else {
/* 671 */         ListTag listTag = new ListTag();
/* 672 */         for (SimplePalette debug8 : list) {
/* 673 */           ListTag debug9 = new ListTag();
/* 674 */           for (BlockState debug11 : debug8) {
/* 675 */             debug9.add(NbtUtils.writeBlockState(debug11));
/*     */           }
/* 677 */           listTag.add(debug9);
/*     */         } 
/* 679 */         debug1.put("palettes", (Tag)listTag);
/*     */       } 
/*     */     } 
/*     */     
/* 683 */     ListTag debug2 = new ListTag();
/* 684 */     for (StructureEntityInfo debug4 : this.entityInfoList) {
/* 685 */       CompoundTag debug5 = new CompoundTag();
/* 686 */       debug5.put("pos", (Tag)newDoubleList(new double[] { debug4.pos.x, debug4.pos.y, debug4.pos.z }));
/* 687 */       debug5.put("blockPos", (Tag)newIntegerList(new int[] { debug4.blockPos.getX(), debug4.blockPos.getY(), debug4.blockPos.getZ() }));
/* 688 */       if (debug4.nbt != null) {
/* 689 */         debug5.put("nbt", (Tag)debug4.nbt);
/*     */       }
/* 691 */       debug2.add(debug5);
/*     */     } 
/*     */     
/* 694 */     debug1.put("entities", (Tag)debug2);
/* 695 */     debug1.put("size", (Tag)newIntegerList(new int[] { this.size.getX(), this.size.getY(), this.size.getZ() }));
/* 696 */     debug1.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/*     */     
/* 698 */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(CompoundTag debug1) {
/* 702 */     this.palettes.clear();
/* 703 */     this.entityInfoList.clear();
/*     */     
/* 705 */     ListTag debug2 = debug1.getList("size", 3);
/* 706 */     this.size = new BlockPos(debug2.getInt(0), debug2.getInt(1), debug2.getInt(2));
/*     */     
/* 708 */     ListTag debug3 = debug1.getList("blocks", 10);
/* 709 */     if (debug1.contains("palettes", 9)) {
/* 710 */       ListTag listTag = debug1.getList("palettes", 9);
/* 711 */       for (int i = 0; i < listTag.size(); i++) {
/* 712 */         loadPalette(listTag.getList(i), debug3);
/*     */       }
/*     */     } else {
/* 715 */       loadPalette(debug1.getList("palette", 10), debug3);
/*     */     } 
/*     */     
/* 718 */     ListTag debug4 = debug1.getList("entities", 10);
/* 719 */     for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 720 */       CompoundTag debug6 = debug4.getCompound(debug5);
/* 721 */       ListTag debug7 = debug6.getList("pos", 6);
/* 722 */       Vec3 debug8 = new Vec3(debug7.getDouble(0), debug7.getDouble(1), debug7.getDouble(2));
/* 723 */       ListTag debug9 = debug6.getList("blockPos", 3);
/* 724 */       BlockPos debug10 = new BlockPos(debug9.getInt(0), debug9.getInt(1), debug9.getInt(2));
/* 725 */       if (debug6.contains("nbt")) {
/* 726 */         CompoundTag debug11 = debug6.getCompound("nbt");
/* 727 */         this.entityInfoList.add(new StructureEntityInfo(debug8, debug10, debug11));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadPalette(ListTag debug1, ListTag debug2) {
/* 733 */     SimplePalette debug3 = new SimplePalette();
/*     */     
/* 735 */     for (int i = 0; i < debug1.size(); i++) {
/* 736 */       debug3.addMapping(NbtUtils.readBlockState(debug1.getCompound(i)), i);
/*     */     }
/*     */     
/* 739 */     List<StructureBlockInfo> debug4 = Lists.newArrayList();
/* 740 */     List<StructureBlockInfo> debug5 = Lists.newArrayList();
/* 741 */     List<StructureBlockInfo> debug6 = Lists.newArrayList();
/*     */     
/* 743 */     for (int j = 0; j < debug2.size(); j++) {
/* 744 */       CompoundTag debug12, debug8 = debug2.getCompound(j);
/* 745 */       ListTag debug9 = debug8.getList("pos", 3);
/* 746 */       BlockPos debug10 = new BlockPos(debug9.getInt(0), debug9.getInt(1), debug9.getInt(2));
/* 747 */       BlockState debug11 = debug3.stateFor(debug8.getInt("state"));
/*     */       
/* 749 */       if (debug8.contains("nbt")) {
/* 750 */         debug12 = debug8.getCompound("nbt");
/*     */       } else {
/* 752 */         debug12 = null;
/*     */       } 
/* 754 */       StructureBlockInfo debug13 = new StructureBlockInfo(debug10, debug11, debug12);
/*     */       
/* 756 */       addToLists(debug13, debug4, debug5, debug6);
/*     */     } 
/*     */     
/* 759 */     List<StructureBlockInfo> debug7 = buildInfoList(debug4, debug5, debug6);
/*     */     
/* 761 */     this.palettes.add(new Palette(debug7));
/*     */   }
/*     */   
/*     */   private ListTag newIntegerList(int... debug1) {
/* 765 */     ListTag debug2 = new ListTag();
/* 766 */     for (int debug6 : debug1) {
/* 767 */       debug2.add(IntTag.valueOf(debug6));
/*     */     }
/* 769 */     return debug2;
/*     */   }
/*     */   
/*     */   private ListTag newDoubleList(double... debug1) {
/* 773 */     ListTag debug2 = new ListTag();
/* 774 */     for (double debug6 : debug1) {
/* 775 */       debug2.add(DoubleTag.valueOf(debug6));
/*     */     }
/* 777 */     return debug2;
/*     */   }
/*     */   
/*     */   public static class StructureBlockInfo {
/*     */     public final BlockPos pos;
/*     */     public final BlockState state;
/*     */     public final CompoundTag nbt;
/*     */     
/*     */     public StructureBlockInfo(BlockPos debug1, BlockState debug2, @Nullable CompoundTag debug3) {
/* 786 */       this.pos = debug1;
/* 787 */       this.state = debug2;
/* 788 */       this.nbt = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 793 */       return String.format("<StructureBlockInfo | %s | %s | %s>", new Object[] { this.pos, this.state, this.nbt });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StructureEntityInfo {
/*     */     public final Vec3 pos;
/*     */     public final BlockPos blockPos;
/*     */     public final CompoundTag nbt;
/*     */     
/*     */     public StructureEntityInfo(Vec3 debug1, BlockPos debug2, CompoundTag debug3) {
/* 803 */       this.pos = debug1;
/* 804 */       this.blockPos = debug2;
/* 805 */       this.nbt = debug3;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Palette
/*     */   {
/*     */     private final List<StructureTemplate.StructureBlockInfo> blocks;
/* 812 */     private final Map<Block, List<StructureTemplate.StructureBlockInfo>> cache = Maps.newHashMap();
/*     */     
/*     */     private Palette(List<StructureTemplate.StructureBlockInfo> debug1) {
/* 815 */       this.blocks = debug1;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks() {
/* 819 */       return this.blocks;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks(Block debug1) {
/* 823 */       return this.cache.computeIfAbsent(debug1, debug1 -> (List)this.blocks.stream().filter(()).collect(Collectors.toList()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */