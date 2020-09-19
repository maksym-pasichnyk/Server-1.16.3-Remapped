/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.FenceGateBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WalkNodeEvaluator
/*     */   extends NodeEvaluator
/*     */ {
/*     */   protected float oldWaterCost;
/*  37 */   private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = (Long2ObjectMap<BlockPathTypes>)new Long2ObjectOpenHashMap();
/*  38 */   private final Object2BooleanMap<AABB> collisionCache = (Object2BooleanMap<AABB>)new Object2BooleanOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion debug1, Mob debug2) {
/*  45 */     super.prepare(debug1, debug2);
/*  46 */     this.oldWaterCost = debug2.getPathfindingMalus(BlockPathTypes.WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  51 */     this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
/*  52 */     this.pathTypesByPosCache.clear();
/*  53 */     this.collisionCache.clear();
/*  54 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  60 */     BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/*  61 */     int debug1 = Mth.floor(this.mob.getY());
/*  62 */     BlockState debug3 = this.level.getBlockState((BlockPos)debug2.set(this.mob.getX(), debug1, this.mob.getZ()));
/*     */     
/*  64 */     if (this.mob.canStandOnFluid(debug3.getFluidState().getType())) {
/*  65 */       while (this.mob.canStandOnFluid(debug3.getFluidState().getType())) {
/*  66 */         debug1++;
/*  67 */         debug3 = this.level.getBlockState((BlockPos)debug2.set(this.mob.getX(), debug1, this.mob.getZ()));
/*     */       } 
/*  69 */       debug1--;
/*  70 */     } else if (canFloat() && this.mob.isInWater()) {
/*  71 */       while (debug3.getBlock() == Blocks.WATER || debug3.getFluidState() == Fluids.WATER.getSource(false)) {
/*  72 */         debug1++;
/*  73 */         debug3 = this.level.getBlockState((BlockPos)debug2.set(this.mob.getX(), debug1, this.mob.getZ()));
/*     */       } 
/*  75 */       debug1--;
/*     */     }
/*  77 */     else if (this.mob.isOnGround()) {
/*  78 */       debug1 = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } else {
/*  80 */       BlockPos blockPos = this.mob.blockPosition();
/*  81 */       while ((this.level.getBlockState(blockPos).isAir() || this.level.getBlockState(blockPos).isPathfindable((BlockGetter)this.level, blockPos, PathComputationType.LAND)) && blockPos.getY() > 0) {
/*  82 */         blockPos = blockPos.below();
/*     */       }
/*  84 */       debug1 = blockPos.above().getY();
/*     */     } 
/*     */ 
/*     */     
/*  88 */     BlockPos debug4 = this.mob.blockPosition();
/*  89 */     BlockPathTypes debug5 = getCachedBlockType(this.mob, debug4.getX(), debug1, debug4.getZ());
/*     */     
/*  91 */     if (this.mob.getPathfindingMalus(debug5) < 0.0F) {
/*  92 */       AABB aABB = this.mob.getBoundingBox();
/*     */       
/*  94 */       if (hasPositiveMalus((BlockPos)debug2.set(aABB.minX, debug1, aABB.minZ)) || 
/*  95 */         hasPositiveMalus((BlockPos)debug2.set(aABB.minX, debug1, aABB.maxZ)) || 
/*  96 */         hasPositiveMalus((BlockPos)debug2.set(aABB.maxX, debug1, aABB.minZ)) || 
/*  97 */         hasPositiveMalus((BlockPos)debug2.set(aABB.maxX, debug1, aABB.maxZ))) {
/*     */         
/*  99 */         Node debug7 = getNode((BlockPos)debug2);
/* 100 */         debug7.type = getBlockPathType(this.mob, debug7.asBlockPos());
/* 101 */         debug7.costMalus = this.mob.getPathfindingMalus(debug7.type);
/* 102 */         return debug7;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 107 */     Node debug6 = getNode(debug4.getX(), debug1, debug4.getZ());
/* 108 */     debug6.type = getBlockPathType(this.mob, debug6.asBlockPos());
/* 109 */     debug6.costMalus = this.mob.getPathfindingMalus(debug6.type);
/* 110 */     return debug6;
/*     */   }
/*     */   
/*     */   private boolean hasPositiveMalus(BlockPos debug1) {
/* 114 */     BlockPathTypes debug2 = getBlockPathType(this.mob, debug1);
/* 115 */     return (this.mob.getPathfindingMalus(debug2) >= 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Target getGoal(double debug1, double debug3, double debug5) {
/* 120 */     return new Target(getNode(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] debug1, Node debug2) {
/* 125 */     int debug3 = 0;
/* 126 */     int debug4 = 0;
/* 127 */     BlockPathTypes debug5 = getCachedBlockType(this.mob, debug2.x, debug2.y + 1, debug2.z);
/* 128 */     BlockPathTypes debug6 = getCachedBlockType(this.mob, debug2.x, debug2.y, debug2.z);
/*     */     
/* 130 */     if (this.mob.getPathfindingMalus(debug5) >= 0.0F && debug6 != BlockPathTypes.STICKY_HONEY) {
/* 131 */       debug4 = Mth.floor(Math.max(1.0F, this.mob.maxUpStep));
/*     */     }
/*     */     
/* 134 */     double debug7 = getFloorLevel((BlockGetter)this.level, new BlockPos(debug2.x, debug2.y, debug2.z));
/*     */     
/* 136 */     Node debug9 = getLandNode(debug2.x, debug2.y, debug2.z + 1, debug4, debug7, Direction.SOUTH, debug6);
/* 137 */     if (isNeighborValid(debug9, debug2)) {
/* 138 */       debug1[debug3++] = debug9;
/*     */     }
/*     */     
/* 141 */     Node debug10 = getLandNode(debug2.x - 1, debug2.y, debug2.z, debug4, debug7, Direction.WEST, debug6);
/* 142 */     if (isNeighborValid(debug10, debug2)) {
/* 143 */       debug1[debug3++] = debug10;
/*     */     }
/*     */     
/* 146 */     Node debug11 = getLandNode(debug2.x + 1, debug2.y, debug2.z, debug4, debug7, Direction.EAST, debug6);
/* 147 */     if (isNeighborValid(debug11, debug2)) {
/* 148 */       debug1[debug3++] = debug11;
/*     */     }
/*     */     
/* 151 */     Node debug12 = getLandNode(debug2.x, debug2.y, debug2.z - 1, debug4, debug7, Direction.NORTH, debug6);
/* 152 */     if (isNeighborValid(debug12, debug2)) {
/* 153 */       debug1[debug3++] = debug12;
/*     */     }
/*     */     
/* 156 */     Node debug13 = getLandNode(debug2.x - 1, debug2.y, debug2.z - 1, debug4, debug7, Direction.NORTH, debug6);
/* 157 */     if (isDiagonalValid(debug2, debug10, debug12, debug13)) {
/* 158 */       debug1[debug3++] = debug13;
/*     */     }
/*     */     
/* 161 */     Node debug14 = getLandNode(debug2.x + 1, debug2.y, debug2.z - 1, debug4, debug7, Direction.NORTH, debug6);
/* 162 */     if (isDiagonalValid(debug2, debug11, debug12, debug14)) {
/* 163 */       debug1[debug3++] = debug14;
/*     */     }
/*     */     
/* 166 */     Node debug15 = getLandNode(debug2.x - 1, debug2.y, debug2.z + 1, debug4, debug7, Direction.SOUTH, debug6);
/* 167 */     if (isDiagonalValid(debug2, debug10, debug9, debug15)) {
/* 168 */       debug1[debug3++] = debug15;
/*     */     }
/*     */     
/* 171 */     Node debug16 = getLandNode(debug2.x + 1, debug2.y, debug2.z + 1, debug4, debug7, Direction.SOUTH, debug6);
/* 172 */     if (isDiagonalValid(debug2, debug11, debug9, debug16)) {
/* 173 */       debug1[debug3++] = debug16;
/*     */     }
/*     */     
/* 176 */     return debug3;
/*     */   }
/*     */   
/*     */   private boolean isNeighborValid(Node debug1, Node debug2) {
/* 180 */     return (debug1 != null && !debug1.closed && (debug1.costMalus >= 0.0F || debug2.costMalus < 0.0F));
/*     */   }
/*     */   
/*     */   private boolean isDiagonalValid(Node debug1, @Nullable Node debug2, @Nullable Node debug3, @Nullable Node debug4) {
/* 184 */     if (debug4 == null || debug3 == null || debug2 == null) {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     if (debug4.closed) {
/* 189 */       return false;
/*     */     }
/*     */     
/* 192 */     if (debug3.y > debug1.y || debug2.y > debug1.y) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     if (debug2.type == BlockPathTypes.WALKABLE_DOOR || debug3.type == BlockPathTypes.WALKABLE_DOOR || debug4.type == BlockPathTypes.WALKABLE_DOOR)
/*     */     {
/* 198 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 202 */     boolean debug5 = (debug3.type == BlockPathTypes.FENCE && debug2.type == BlockPathTypes.FENCE && this.mob.getBbWidth() < 0.5D);
/*     */     
/* 204 */     return (debug4.costMalus >= 0.0F && (debug3.y < debug1.y || debug3.costMalus >= 0.0F || debug5) && (debug2.y < debug1.y || debug2.costMalus >= 0.0F || debug5));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReachWithoutCollision(Node debug1) {
/* 213 */     Vec3 debug2 = new Vec3(debug1.x - this.mob.getX(), debug1.y - this.mob.getY(), debug1.z - this.mob.getZ());
/*     */     
/* 215 */     AABB debug3 = this.mob.getBoundingBox();
/* 216 */     int debug4 = Mth.ceil(debug2.length() / debug3.getSize());
/* 217 */     debug2 = debug2.scale((1.0F / debug4));
/* 218 */     for (int debug5 = 1; debug5 <= debug4; debug5++) {
/* 219 */       debug3 = debug3.move(debug2);
/* 220 */       if (hasCollisions(debug3)) {
/* 221 */         return false;
/*     */       }
/*     */     } 
/* 224 */     return true;
/*     */   }
/*     */   
/*     */   public static double getFloorLevel(BlockGetter debug0, BlockPos debug1) {
/* 228 */     BlockPos debug2 = debug1.below();
/* 229 */     VoxelShape debug3 = debug0.getBlockState(debug2).getCollisionShape(debug0, debug2);
/* 230 */     return debug2.getY() + (debug3.isEmpty() ? 0.0D : debug3.max(Direction.Axis.Y));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Node getLandNode(int debug1, int debug2, int debug3, int debug4, double debug5, Direction debug7, BlockPathTypes debug8) {
/* 235 */     Node debug9 = null;
/* 236 */     BlockPos.MutableBlockPos debug10 = new BlockPos.MutableBlockPos();
/*     */     
/* 238 */     double debug11 = getFloorLevel((BlockGetter)this.level, (BlockPos)debug10.set(debug1, debug2, debug3));
/*     */ 
/*     */     
/* 241 */     if (debug11 - debug5 > 1.125D) {
/* 242 */       return null;
/*     */     }
/*     */     
/* 245 */     BlockPathTypes debug13 = getCachedBlockType(this.mob, debug1, debug2, debug3);
/*     */     
/* 247 */     float debug14 = this.mob.getPathfindingMalus(debug13);
/* 248 */     double debug15 = this.mob.getBbWidth() / 2.0D;
/*     */     
/* 250 */     if (debug14 >= 0.0F) {
/* 251 */       debug9 = getNode(debug1, debug2, debug3);
/* 252 */       debug9.type = debug13;
/* 253 */       debug9.costMalus = Math.max(debug9.costMalus, debug14);
/*     */     } 
/*     */ 
/*     */     
/* 257 */     if (debug8 == BlockPathTypes.FENCE && debug9 != null && debug9.costMalus >= 0.0F && !canReachWithoutCollision(debug9)) {
/* 258 */       debug9 = null;
/*     */     }
/*     */     
/* 261 */     if (debug13 == BlockPathTypes.WALKABLE) {
/* 262 */       return debug9;
/*     */     }
/*     */     
/* 265 */     if ((debug9 == null || debug9.costMalus < 0.0F) && debug4 > 0 && debug13 != BlockPathTypes.FENCE && debug13 != BlockPathTypes.UNPASSABLE_RAIL && debug13 != BlockPathTypes.TRAPDOOR) {
/* 266 */       debug9 = getLandNode(debug1, debug2 + 1, debug3, debug4 - 1, debug5, debug7, debug8);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 271 */       if (debug9 != null && (debug9.type == BlockPathTypes.OPEN || debug9.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0F) {
/* 272 */         double debug17 = (debug1 - debug7.getStepX()) + 0.5D;
/* 273 */         double debug19 = (debug3 - debug7.getStepZ()) + 0.5D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 280 */         AABB debug21 = new AABB(debug17 - debug15, getFloorLevel((BlockGetter)this.level, (BlockPos)debug10.set(debug17, (debug2 + 1), debug19)) + 0.001D, debug19 - debug15, debug17 + debug15, this.mob.getBbHeight() + getFloorLevel((BlockGetter)this.level, (BlockPos)debug10.set(debug9.x, debug9.y, debug9.z)) - 0.002D, debug19 + debug15);
/*     */ 
/*     */         
/* 283 */         if (hasCollisions(debug21)) {
/* 284 */           debug9 = null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 289 */     if (debug13 == BlockPathTypes.WATER && !canFloat()) {
/* 290 */       if (getCachedBlockType(this.mob, debug1, debug2 - 1, debug3) != BlockPathTypes.WATER) {
/* 291 */         return debug9;
/*     */       }
/*     */ 
/*     */       
/* 295 */       while (debug2 > 0) {
/* 296 */         debug2--;
/*     */         
/* 298 */         debug13 = getCachedBlockType(this.mob, debug1, debug2, debug3);
/*     */         
/* 300 */         if (debug13 == BlockPathTypes.WATER) {
/* 301 */           debug9 = getNode(debug1, debug2, debug3);
/* 302 */           debug9.type = debug13;
/* 303 */           debug9.costMalus = Math.max(debug9.costMalus, this.mob.getPathfindingMalus(debug13)); continue;
/*     */         } 
/* 305 */         return debug9;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 310 */     if (debug13 == BlockPathTypes.OPEN) {
/*     */ 
/*     */       
/* 313 */       int debug17 = 0;
/* 314 */       int debug18 = debug2;
/* 315 */       while (debug13 == BlockPathTypes.OPEN) {
/* 316 */         debug2--;
/*     */         
/* 318 */         if (debug2 < 0) {
/* 319 */           Node debug19 = getNode(debug1, debug18, debug3);
/* 320 */           debug19.type = BlockPathTypes.BLOCKED;
/* 321 */           debug19.costMalus = -1.0F;
/* 322 */           return debug19;
/*     */         } 
/*     */         
/* 325 */         if (debug17++ >= this.mob.getMaxFallDistance()) {
/* 326 */           Node debug19 = getNode(debug1, debug2, debug3);
/* 327 */           debug19.type = BlockPathTypes.BLOCKED;
/* 328 */           debug19.costMalus = -1.0F;
/* 329 */           return debug19;
/*     */         } 
/*     */         
/* 332 */         debug13 = getCachedBlockType(this.mob, debug1, debug2, debug3);
/* 333 */         debug14 = this.mob.getPathfindingMalus(debug13);
/*     */         
/* 335 */         if (debug13 != BlockPathTypes.OPEN && debug14 >= 0.0F) {
/* 336 */           debug9 = getNode(debug1, debug2, debug3);
/* 337 */           debug9.type = debug13;
/* 338 */           debug9.costMalus = Math.max(debug9.costMalus, debug14); break;
/*     */         } 
/* 340 */         if (debug14 < 0.0F) {
/* 341 */           Node debug19 = getNode(debug1, debug2, debug3);
/* 342 */           debug19.type = BlockPathTypes.BLOCKED;
/* 343 */           debug19.costMalus = -1.0F;
/* 344 */           return debug19;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 349 */     if (debug13 == BlockPathTypes.FENCE) {
/* 350 */       debug9 = getNode(debug1, debug2, debug3);
/* 351 */       debug9.closed = true;
/* 352 */       debug9.type = debug13;
/* 353 */       debug9.costMalus = debug13.getMalus();
/*     */     } 
/*     */     
/* 356 */     return debug9;
/*     */   }
/*     */   
/*     */   private boolean hasCollisions(AABB debug1) {
/* 360 */     return ((Boolean)this.collisionCache.computeIfAbsent(debug1, debug2 -> Boolean.valueOf(!this.level.noCollision((Entity)this.mob, debug1)))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4, Mob debug5, int debug6, int debug7, int debug8, boolean debug9, boolean debug10) {
/* 366 */     EnumSet<BlockPathTypes> debug11 = EnumSet.noneOf(BlockPathTypes.class);
/* 367 */     BlockPathTypes debug12 = BlockPathTypes.BLOCKED;
/*     */     
/* 369 */     BlockPos debug13 = debug5.blockPosition();
/*     */     
/* 371 */     debug12 = getBlockPathTypes(debug1, debug2, debug3, debug4, debug6, debug7, debug8, debug9, debug10, debug11, debug12, debug13);
/*     */     
/* 373 */     if (debug11.contains(BlockPathTypes.FENCE)) {
/* 374 */       return BlockPathTypes.FENCE;
/*     */     }
/*     */     
/* 377 */     if (debug11.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
/* 378 */       return BlockPathTypes.UNPASSABLE_RAIL;
/*     */     }
/*     */     
/* 381 */     BlockPathTypes debug14 = BlockPathTypes.BLOCKED;
/* 382 */     for (BlockPathTypes debug16 : debug11) {
/*     */       
/* 384 */       if (debug5.getPathfindingMalus(debug16) < 0.0F) {
/* 385 */         return debug16;
/*     */       }
/*     */ 
/*     */       
/* 389 */       if (debug5.getPathfindingMalus(debug16) >= debug5.getPathfindingMalus(debug14)) {
/* 390 */         debug14 = debug16;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 395 */     if (debug12 == BlockPathTypes.OPEN && debug5.getPathfindingMalus(debug14) == 0.0F && debug6 <= 1) {
/* 396 */       return BlockPathTypes.OPEN;
/*     */     }
/*     */     
/* 399 */     return debug14;
/*     */   }
/*     */   
/*     */   public BlockPathTypes getBlockPathTypes(BlockGetter debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, boolean debug8, boolean debug9, EnumSet<BlockPathTypes> debug10, BlockPathTypes debug11, BlockPos debug12) {
/* 403 */     for (int debug13 = 0; debug13 < debug5; debug13++) {
/* 404 */       for (int debug14 = 0; debug14 < debug6; debug14++) {
/* 405 */         for (int debug15 = 0; debug15 < debug7; debug15++) {
/* 406 */           int debug16 = debug13 + debug2;
/* 407 */           int debug17 = debug14 + debug3;
/* 408 */           int debug18 = debug15 + debug4;
/*     */           
/* 410 */           BlockPathTypes debug19 = getBlockPathType(debug1, debug16, debug17, debug18);
/*     */           
/* 412 */           debug19 = evaluateBlockPathType(debug1, debug8, debug9, debug12, debug19);
/*     */           
/* 414 */           if (debug13 == 0 && debug14 == 0 && debug15 == 0) {
/* 415 */             debug11 = debug19;
/*     */           }
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
/* 435 */           debug10.add(debug19);
/*     */         } 
/*     */       } 
/*     */     } 
/* 439 */     return debug11;
/*     */   }
/*     */   
/*     */   protected BlockPathTypes evaluateBlockPathType(BlockGetter debug1, boolean debug2, boolean debug3, BlockPos debug4, BlockPathTypes debug5) {
/* 443 */     if (debug5 == BlockPathTypes.DOOR_WOOD_CLOSED && debug2 && debug3) {
/* 444 */       debug5 = BlockPathTypes.WALKABLE_DOOR;
/*     */     }
/* 446 */     if (debug5 == BlockPathTypes.DOOR_OPEN && !debug3) {
/* 447 */       debug5 = BlockPathTypes.BLOCKED;
/*     */     }
/* 449 */     if (debug5 == BlockPathTypes.RAIL && !(debug1.getBlockState(debug4).getBlock() instanceof net.minecraft.world.level.block.BaseRailBlock) && !(debug1.getBlockState(debug4.below()).getBlock() instanceof net.minecraft.world.level.block.BaseRailBlock)) {
/* 450 */       debug5 = BlockPathTypes.UNPASSABLE_RAIL;
/*     */     }
/* 452 */     if (debug5 == BlockPathTypes.LEAVES) {
/* 453 */       debug5 = BlockPathTypes.BLOCKED;
/*     */     }
/* 455 */     return debug5;
/*     */   }
/*     */   
/*     */   private BlockPathTypes getBlockPathType(Mob debug1, BlockPos debug2) {
/* 459 */     return getCachedBlockType(debug1, debug2.getX(), debug2.getY(), debug2.getZ());
/*     */   }
/*     */   
/*     */   private BlockPathTypes getCachedBlockType(Mob debug1, int debug2, int debug3, int debug4) {
/* 463 */     return (BlockPathTypes)this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(debug2, debug3, debug4), debug5 -> getBlockPathType((BlockGetter)this.level, debug1, debug2, debug3, debug4, this.entityWidth, this.entityHeight, this.entityDepth, canOpenDoors(), canPassDoors()));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPathTypes getBlockPathType(BlockGetter debug1, int debug2, int debug3, int debug4) {
/* 468 */     return getBlockPathTypeStatic(debug1, new BlockPos.MutableBlockPos(debug2, debug3, debug4));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BlockPathTypes getBlockPathTypeStatic(BlockGetter debug0, BlockPos.MutableBlockPos debug1) {
/* 479 */     int debug2 = debug1.getX();
/* 480 */     int debug3 = debug1.getY();
/* 481 */     int debug4 = debug1.getZ();
/*     */     
/* 483 */     BlockPathTypes debug5 = getBlockPathTypeRaw(debug0, (BlockPos)debug1);
/*     */     
/* 485 */     if (debug5 == BlockPathTypes.OPEN && debug3 >= 1) {
/* 486 */       BlockPathTypes debug6 = getBlockPathTypeRaw(debug0, (BlockPos)debug1.set(debug2, debug3 - 1, debug4));
/* 487 */       debug5 = (debug6 == BlockPathTypes.WALKABLE || debug6 == BlockPathTypes.OPEN || debug6 == BlockPathTypes.WATER || debug6 == BlockPathTypes.LAVA) ? BlockPathTypes.OPEN : BlockPathTypes.WALKABLE;
/*     */ 
/*     */       
/* 490 */       if (debug6 == BlockPathTypes.DAMAGE_FIRE) {
/* 491 */         debug5 = BlockPathTypes.DAMAGE_FIRE;
/*     */       }
/*     */       
/* 494 */       if (debug6 == BlockPathTypes.DAMAGE_CACTUS) {
/* 495 */         debug5 = BlockPathTypes.DAMAGE_CACTUS;
/*     */       }
/*     */       
/* 498 */       if (debug6 == BlockPathTypes.DAMAGE_OTHER) {
/* 499 */         debug5 = BlockPathTypes.DAMAGE_OTHER;
/*     */       }
/*     */       
/* 502 */       if (debug6 == BlockPathTypes.STICKY_HONEY) {
/* 503 */         debug5 = BlockPathTypes.STICKY_HONEY;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 508 */     if (debug5 == BlockPathTypes.WALKABLE) {
/* 509 */       debug5 = checkNeighbourBlocks(debug0, debug1.set(debug2, debug3, debug4), debug5);
/*     */     }
/* 511 */     return debug5;
/*     */   }
/*     */   
/*     */   public static BlockPathTypes checkNeighbourBlocks(BlockGetter debug0, BlockPos.MutableBlockPos debug1, BlockPathTypes debug2) {
/* 515 */     int debug3 = debug1.getX();
/* 516 */     int debug4 = debug1.getY();
/* 517 */     int debug5 = debug1.getZ();
/*     */     
/* 519 */     for (int debug6 = -1; debug6 <= 1; debug6++) {
/* 520 */       for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 521 */         for (int debug8 = -1; debug8 <= 1; debug8++) {
/* 522 */           if (debug6 != 0 || debug8 != 0) {
/* 523 */             debug1.set(debug3 + debug6, debug4 + debug7, debug5 + debug8);
/* 524 */             BlockState debug9 = debug0.getBlockState((BlockPos)debug1);
/* 525 */             if (debug9.is(Blocks.CACTUS))
/* 526 */               return BlockPathTypes.DANGER_CACTUS; 
/* 527 */             if (debug9.is(Blocks.SWEET_BERRY_BUSH))
/* 528 */               return BlockPathTypes.DANGER_OTHER; 
/* 529 */             if (isBurningBlock(debug9))
/* 530 */               return BlockPathTypes.DANGER_FIRE; 
/* 531 */             if (debug0.getFluidState((BlockPos)debug1).is((Tag)FluidTags.WATER)) {
/* 532 */               return BlockPathTypes.WATER_BORDER;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 538 */     return debug2;
/*     */   }
/*     */   
/*     */   protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter debug0, BlockPos debug1) {
/* 542 */     BlockState debug2 = debug0.getBlockState(debug1);
/* 543 */     Block debug3 = debug2.getBlock();
/* 544 */     Material debug4 = debug2.getMaterial();
/*     */     
/* 546 */     if (debug2.isAir()) {
/* 547 */       return BlockPathTypes.OPEN;
/*     */     }
/*     */     
/* 550 */     if (debug2.is((Tag)BlockTags.TRAPDOORS) || debug2.is(Blocks.LILY_PAD)) {
/* 551 */       return BlockPathTypes.TRAPDOOR;
/*     */     }
/*     */     
/* 554 */     if (debug2.is(Blocks.CACTUS)) {
/* 555 */       return BlockPathTypes.DAMAGE_CACTUS;
/*     */     }
/*     */     
/* 558 */     if (debug2.is(Blocks.SWEET_BERRY_BUSH)) {
/* 559 */       return BlockPathTypes.DAMAGE_OTHER;
/*     */     }
/*     */     
/* 562 */     if (debug2.is(Blocks.HONEY_BLOCK)) {
/* 563 */       return BlockPathTypes.STICKY_HONEY;
/*     */     }
/*     */     
/* 566 */     if (debug2.is(Blocks.COCOA)) {
/* 567 */       return BlockPathTypes.COCOA;
/*     */     }
/*     */     
/* 570 */     FluidState debug5 = debug0.getFluidState(debug1);
/* 571 */     if (debug5.is((Tag)FluidTags.WATER))
/* 572 */       return BlockPathTypes.WATER; 
/* 573 */     if (debug5.is((Tag)FluidTags.LAVA)) {
/* 574 */       return BlockPathTypes.LAVA;
/*     */     }
/*     */     
/* 577 */     if (isBurningBlock(debug2)) {
/* 578 */       return BlockPathTypes.DAMAGE_FIRE;
/*     */     }
/*     */     
/* 581 */     if (DoorBlock.isWoodenDoor(debug2) && !((Boolean)debug2.getValue((Property)DoorBlock.OPEN)).booleanValue())
/* 582 */       return BlockPathTypes.DOOR_WOOD_CLOSED; 
/* 583 */     if (debug3 instanceof DoorBlock && debug4 == Material.METAL && !((Boolean)debug2.getValue((Property)DoorBlock.OPEN)).booleanValue())
/* 584 */       return BlockPathTypes.DOOR_IRON_CLOSED; 
/* 585 */     if (debug3 instanceof DoorBlock && ((Boolean)debug2.getValue((Property)DoorBlock.OPEN)).booleanValue()) {
/* 586 */       return BlockPathTypes.DOOR_OPEN;
/*     */     }
/*     */     
/* 589 */     if (debug3 instanceof net.minecraft.world.level.block.BaseRailBlock) {
/* 590 */       return BlockPathTypes.RAIL;
/*     */     }
/*     */     
/* 593 */     if (debug3 instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 594 */       return BlockPathTypes.LEAVES;
/*     */     }
/*     */     
/* 597 */     if (debug3.is((Tag)BlockTags.FENCES) || debug3.is((Tag)BlockTags.WALLS) || (debug3 instanceof FenceGateBlock && !((Boolean)debug2.getValue((Property)FenceGateBlock.OPEN)).booleanValue())) {
/* 598 */       return BlockPathTypes.FENCE;
/*     */     }
/*     */ 
/*     */     
/* 602 */     if (!debug2.isPathfindable(debug0, debug1, PathComputationType.LAND)) {
/* 603 */       return BlockPathTypes.BLOCKED;
/*     */     }
/*     */     
/* 606 */     return BlockPathTypes.OPEN;
/*     */   }
/*     */   
/*     */   private static boolean isBurningBlock(BlockState debug0) {
/* 610 */     return (debug0.is((Tag)BlockTags.FIRE) || debug0
/* 611 */       .is(Blocks.LAVA) || debug0
/* 612 */       .is(Blocks.MAGMA_BLOCK) || 
/* 613 */       CampfireBlock.isLitCampfire(debug0));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\WalkNodeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */