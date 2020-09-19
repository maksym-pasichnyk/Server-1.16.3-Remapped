/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ @Immutable
/*     */ public class BlockPos extends Vec3i {
/*     */   public static final Codec<BlockPos> CODEC;
/*     */   
/*     */   static {
/*  29 */     CODEC = Codec.INT_STREAM.comapFlatMap(debug0 -> Util.fixedSize(debug0, 3).map(()), debug0 -> IntStream.of(new int[] { debug0.getX(), debug0.getY(), debug0.getZ() })).stable();
/*     */   }
/*  31 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  33 */   public static final BlockPos ZERO = new BlockPos(0, 0, 0);
/*     */   
/*  35 */   private static final int PACKED_X_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));
/*  36 */   private static final int PACKED_Z_LENGTH = PACKED_X_LENGTH;
/*  37 */   private static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
/*     */   
/*  39 */   private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
/*  40 */   private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
/*  41 */   private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
/*     */ 
/*     */   
/*  44 */   private static final int Z_OFFSET = PACKED_Y_LENGTH;
/*  45 */   private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;
/*     */   
/*     */   public BlockPos(int debug1, int debug2, int debug3) {
/*  48 */     super(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public BlockPos(double debug1, double debug3, double debug5) {
/*  52 */     super(debug1, debug3, debug5);
/*     */   }
/*     */   
/*     */   public BlockPos(Vec3 debug1) {
/*  56 */     this(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */   
/*     */   public BlockPos(Position debug1) {
/*  60 */     this(debug1.x(), debug1.y(), debug1.z());
/*     */   }
/*     */   
/*     */   public BlockPos(Vec3i debug1) {
/*  64 */     this(debug1.getX(), debug1.getY(), debug1.getZ());
/*     */   }
/*     */   
/*     */   public static long offset(long debug0, Direction debug2) {
/*  68 */     return offset(debug0, debug2.getStepX(), debug2.getStepY(), debug2.getStepZ());
/*     */   }
/*     */   
/*     */   public static long offset(long debug0, int debug2, int debug3, int debug4) {
/*  72 */     return asLong(getX(debug0) + debug2, getY(debug0) + debug3, getZ(debug0) + debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getX(long debug0) {
/*  81 */     return (int)(debug0 << 64 - X_OFFSET - PACKED_X_LENGTH >> 64 - PACKED_X_LENGTH);
/*     */   }
/*     */   
/*     */   public static int getY(long debug0) {
/*  85 */     return (int)(debug0 << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
/*     */   }
/*     */   
/*     */   public static int getZ(long debug0) {
/*  89 */     return (int)(debug0 << 64 - Z_OFFSET - PACKED_Z_LENGTH >> 64 - PACKED_Z_LENGTH);
/*     */   }
/*     */   
/*     */   public static BlockPos of(long debug0) {
/*  93 */     return new BlockPos(getX(debug0), getY(debug0), getZ(debug0));
/*     */   }
/*     */   
/*     */   public long asLong() {
/*  97 */     return asLong(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public static long asLong(int debug0, int debug1, int debug2) {
/* 101 */     long debug3 = 0L;
/* 102 */     debug3 |= (debug0 & PACKED_X_MASK) << X_OFFSET;
/* 103 */     debug3 |= (debug1 & PACKED_Y_MASK) << 0L;
/* 104 */     debug3 |= (debug2 & PACKED_Z_MASK) << Z_OFFSET;
/* 105 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getFlatIndex(long debug0) {
/* 113 */     return debug0 & 0xFFFFFFFFFFFFFFF0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(double debug1, double debug3, double debug5) {
/* 118 */     if (debug1 == 0.0D && debug3 == 0.0D && debug5 == 0.0D) {
/* 119 */       return this;
/*     */     }
/* 121 */     return new BlockPos(getX() + debug1, getY() + debug3, getZ() + debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(int debug1, int debug2, int debug3) {
/* 126 */     if (debug1 == 0 && debug2 == 0 && debug3 == 0) {
/* 127 */       return this;
/*     */     }
/* 129 */     return new BlockPos(getX() + debug1, getY() + debug2, getZ() + debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos offset(Vec3i debug1) {
/* 134 */     return offset(debug1.getX(), debug1.getY(), debug1.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos subtract(Vec3i debug1) {
/* 139 */     return offset(-debug1.getX(), -debug1.getY(), -debug1.getZ());
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
/*     */   public BlockPos above() {
/* 154 */     return relative(Direction.UP);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos above(int debug1) {
/* 159 */     return relative(Direction.UP, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos below() {
/* 164 */     return relative(Direction.DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos below(int debug1) {
/* 169 */     return relative(Direction.DOWN, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos north() {
/* 174 */     return relative(Direction.NORTH);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos north(int debug1) {
/* 179 */     return relative(Direction.NORTH, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos south() {
/* 184 */     return relative(Direction.SOUTH);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos south(int debug1) {
/* 189 */     return relative(Direction.SOUTH, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos west() {
/* 194 */     return relative(Direction.WEST);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos west(int debug1) {
/* 199 */     return relative(Direction.WEST, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos east() {
/* 204 */     return relative(Direction.EAST);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos east(int debug1) {
/* 209 */     return relative(Direction.EAST, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos relative(Direction debug1) {
/* 214 */     return new BlockPos(getX() + debug1.getStepX(), getY() + debug1.getStepY(), getZ() + debug1.getStepZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos relative(Direction debug1, int debug2) {
/* 219 */     if (debug2 == 0) {
/* 220 */       return this;
/*     */     }
/* 222 */     return new BlockPos(getX() + debug1.getStepX() * debug2, getY() + debug1.getStepY() * debug2, getZ() + debug1.getStepZ() * debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos relative(Direction.Axis debug1, int debug2) {
/* 227 */     if (debug2 == 0) {
/* 228 */       return this;
/*     */     }
/* 230 */     int debug3 = (debug1 == Direction.Axis.X) ? debug2 : 0;
/* 231 */     int debug4 = (debug1 == Direction.Axis.Y) ? debug2 : 0;
/* 232 */     int debug5 = (debug1 == Direction.Axis.Z) ? debug2 : 0;
/* 233 */     return new BlockPos(getX() + debug3, getY() + debug4, getZ() + debug5);
/*     */   }
/*     */   
/*     */   public BlockPos rotate(Rotation debug1) {
/* 237 */     switch (debug1)
/*     */     
/*     */     { default:
/* 240 */         return this;
/*     */       case Y:
/* 242 */         return new BlockPos(-getZ(), getY(), getX());
/*     */       case Z:
/* 244 */         return new BlockPos(-getX(), getY(), -getZ());
/*     */       case null:
/* 246 */         break; }  return new BlockPos(getZ(), getY(), -getX());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos cross(Vec3i debug1) {
/* 252 */     return new BlockPos(getY() * debug1.getZ() - getZ() * debug1.getY(), getZ() * debug1.getX() - getX() * debug1.getZ(), getX() * debug1.getY() - getY() * debug1.getX());
/*     */   }
/*     */   
/*     */   public BlockPos immutable() {
/* 256 */     return this;
/*     */   }
/*     */   
/*     */   public MutableBlockPos mutable() {
/* 260 */     return new MutableBlockPos(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public static class MutableBlockPos extends BlockPos {
/*     */     public MutableBlockPos() {
/* 265 */       this(0, 0, 0);
/*     */     }
/*     */     
/*     */     public MutableBlockPos(int debug1, int debug2, int debug3) {
/* 269 */       super(debug1, debug2, debug3);
/*     */     }
/*     */     
/*     */     public MutableBlockPos(double debug1, double debug3, double debug5) {
/* 273 */       this(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5));
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos offset(double debug1, double debug3, double debug5) {
/* 278 */       return super.offset(debug1, debug3, debug5).immutable();
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos offset(int debug1, int debug2, int debug3) {
/* 283 */       return super.offset(debug1, debug2, debug3).immutable();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BlockPos relative(Direction debug1, int debug2) {
/* 293 */       return super.relative(debug1, debug2).immutable();
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos relative(Direction.Axis debug1, int debug2) {
/* 298 */       return super.relative(debug1, debug2).immutable();
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos rotate(Rotation debug1) {
/* 303 */       return super.rotate(debug1).immutable();
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(int debug1, int debug2, int debug3) {
/* 307 */       setX(debug1);
/* 308 */       setY(debug2);
/* 309 */       setZ(debug3);
/* 310 */       return this;
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(double debug1, double debug3, double debug5) {
/* 314 */       return set(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5));
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(Vec3i debug1) {
/* 318 */       return set(debug1.getX(), debug1.getY(), debug1.getZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(long debug1) {
/* 322 */       return set(getX(debug1), getY(debug1), getZ(debug1));
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(AxisCycle debug1, int debug2, int debug3, int debug4) {
/* 326 */       return set(debug1
/* 327 */           .cycle(debug2, debug3, debug4, Direction.Axis.X), debug1
/* 328 */           .cycle(debug2, debug3, debug4, Direction.Axis.Y), debug1
/* 329 */           .cycle(debug2, debug3, debug4, Direction.Axis.Z));
/*     */     }
/*     */ 
/*     */     
/*     */     public MutableBlockPos setWithOffset(Vec3i debug1, Direction debug2) {
/* 334 */       return set(debug1.getX() + debug2.getStepX(), debug1.getY() + debug2.getStepY(), debug1.getZ() + debug2.getStepZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos setWithOffset(Vec3i debug1, int debug2, int debug3, int debug4) {
/* 338 */       return set(debug1.getX() + debug2, debug1.getY() + debug3, debug1.getZ() + debug4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MutableBlockPos move(Direction debug1) {
/* 346 */       return move(debug1, 1);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(Direction debug1, int debug2) {
/* 350 */       return set(getX() + debug1.getStepX() * debug2, getY() + debug1.getStepY() * debug2, getZ() + debug1.getStepZ() * debug2);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(int debug1, int debug2, int debug3) {
/* 354 */       return set(getX() + debug1, getY() + debug2, getZ() + debug3);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(Vec3i debug1) {
/* 358 */       return set(getX() + debug1.getX(), getY() + debug1.getY(), getZ() + debug1.getZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos clamp(Direction.Axis debug1, int debug2, int debug3) {
/* 362 */       switch (debug1) {
/*     */         case X:
/* 364 */           return set(Mth.clamp(getX(), debug2, debug3), getY(), getZ());
/*     */         case Y:
/* 366 */           return set(getX(), Mth.clamp(getY(), debug2, debug3), getZ());
/*     */         case Z:
/* 368 */           return set(getX(), getY(), Mth.clamp(getZ(), debug2, debug3));
/*     */       } 
/* 370 */       throw new IllegalStateException("Unable to clamp axis " + debug1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setX(int debug1) {
/* 376 */       super.setX(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setY(int debug1) {
/* 381 */       super.setY(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setZ(int debug1) {
/* 386 */       super.setZ(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos immutable() {
/* 391 */       return new BlockPos(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> randomBetweenClosed(final Random limit, final int minX, final int random, final int width, final int minY, final int height, final int minZ, final int depth) {
/* 397 */     int debug8 = height - random + 1;
/* 398 */     int debug9 = minZ - width + 1;
/* 399 */     int debug10 = depth - minY + 1;
/*     */     
/* 401 */     return () -> new AbstractIterator<BlockPos>() {
/* 402 */         final BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/* 403 */         int counter = limit;
/*     */ 
/*     */         
/*     */         protected BlockPos computeNext() {
/* 407 */           if (this.counter <= 0) {
/* 408 */             return (BlockPos)endOfData();
/*     */           }
/*     */           
/* 411 */           BlockPos debug1 = this.nextPos.set(minX + random
/* 412 */               .nextInt(width), minY + random
/* 413 */               .nextInt(height), minZ + random
/* 414 */               .nextInt(depth));
/*     */           
/* 416 */           this.counter--;
/* 417 */           return debug1;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> withinManhattan(final BlockPos originZ, final int maxDepth, final int reachX, final int reachY) {
/* 423 */     final int reachZ = maxDepth + reachX + reachY;
/* 424 */     final int originX = originZ.getX();
/* 425 */     final int originY = originZ.getY();
/* 426 */     int debug7 = originZ.getZ();
/*     */     
/* 428 */     return () -> new AbstractIterator<BlockPos>() {
/* 429 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int currentDepth;
/*     */         
/*     */         private int maxX;
/*     */         
/*     */         private int maxY;
/*     */         
/*     */         private int x;
/*     */         private int y;
/*     */         private boolean zMirror;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 442 */           if (this.zMirror) {
/* 443 */             this.zMirror = false;
/* 444 */             this.cursor.setZ(originZ - this.cursor.getZ() - originZ);
/* 445 */             return this.cursor;
/*     */           } 
/*     */           
/* 448 */           BlockPos debug1 = null;
/* 449 */           while (debug1 == null) {
/* 450 */             if (this.y > this.maxY) {
/* 451 */               this.x++;
/* 452 */               if (this.x > this.maxX) {
/* 453 */                 this.currentDepth++;
/* 454 */                 if (this.currentDepth > maxDepth) {
/* 455 */                   return (BlockPos)endOfData();
/*     */                 }
/* 457 */                 this.maxX = Math.min(reachX, this.currentDepth);
/* 458 */                 this.x = -this.maxX;
/*     */               } 
/* 460 */               this.maxY = Math.min(reachY, this.currentDepth - Math.abs(this.x));
/* 461 */               this.y = -this.maxY;
/*     */             } 
/*     */             
/* 464 */             int debug2 = this.x;
/* 465 */             int debug3 = this.y;
/* 466 */             int debug4 = this.currentDepth - Math.abs(debug2) - Math.abs(debug3);
/* 467 */             if (debug4 <= reachZ) {
/* 468 */               this.zMirror = (debug4 != 0);
/* 469 */               debug1 = this.cursor.set(originX + debug2, originY + debug3, originZ + debug4);
/*     */             } 
/* 471 */             this.y++;
/*     */           } 
/* 473 */           return debug1;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findClosestMatch(BlockPos debug0, int debug1, int debug2, Predicate<BlockPos> debug3) {
/* 479 */     return withinManhattanStream(debug0, debug1, debug2, debug1).filter(debug3).findFirst();
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> withinManhattanStream(BlockPos debug0, int debug1, int debug2, int debug3) {
/* 483 */     return StreamSupport.stream(withinManhattan(debug0, debug1, debug2, debug3).spliterator(), false);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(BlockPos debug0, BlockPos debug1) {
/* 487 */     return betweenClosed(
/* 488 */         Math.min(debug0.getX(), debug1.getX()), 
/* 489 */         Math.min(debug0.getY(), debug1.getY()), 
/* 490 */         Math.min(debug0.getZ(), debug1.getZ()), 
/* 491 */         Math.max(debug0.getX(), debug1.getX()), 
/* 492 */         Math.max(debug0.getY(), debug1.getY()), 
/* 493 */         Math.max(debug0.getZ(), debug1.getZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(BlockPos debug0, BlockPos debug1) {
/* 498 */     return StreamSupport.stream(betweenClosed(debug0, debug1).spliterator(), false);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(BoundingBox debug0) {
/* 502 */     return betweenClosedStream(
/* 503 */         Math.min(debug0.x0, debug0.x1), 
/* 504 */         Math.min(debug0.y0, debug0.y1), 
/* 505 */         Math.min(debug0.z0, debug0.z1), 
/* 506 */         Math.max(debug0.x0, debug0.x1), 
/* 507 */         Math.max(debug0.y0, debug0.y1), 
/* 508 */         Math.max(debug0.z0, debug0.z1));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(AABB debug0) {
/* 513 */     return betweenClosedStream(Mth.floor(debug0.minX), Mth.floor(debug0.minY), Mth.floor(debug0.minZ), Mth.floor(debug0.maxX), Mth.floor(debug0.maxY), Mth.floor(debug0.maxZ));
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(int debug0, int debug1, int debug2, int debug3, int debug4, int debug5) {
/* 517 */     return StreamSupport.stream(betweenClosed(debug0, debug1, debug2, debug3, debug4, debug5).spliterator(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(final int end, final int width, final int height, final int minX, final int minY, final int minZ) {
/* 522 */     int debug6 = minX - end + 1;
/* 523 */     int debug7 = minY - width + 1;
/* 524 */     int debug8 = minZ - height + 1;
/* 525 */     int debug9 = debug6 * debug7 * debug8;
/*     */     
/* 527 */     return () -> new AbstractIterator<BlockPos>() {
/* 528 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int index;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 533 */           if (this.index == end) {
/* 534 */             return (BlockPos)endOfData();
/*     */           }
/*     */           
/* 537 */           int debug1 = this.index % width;
/* 538 */           int debug2 = this.index / width;
/* 539 */           int debug3 = debug2 % height;
/* 540 */           int debug4 = debug2 / height;
/*     */           
/* 542 */           this.index++;
/* 543 */           return this.cursor.set(minX + debug1, minY + debug3, minZ + debug4);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<MutableBlockPos> spiralAround(final BlockPos firstDirection, final int secondDirection, final Direction center, final Direction radius) {
/* 549 */     Validate.validState((center.getAxis() != radius.getAxis()), "The two directions cannot be on the same axis", new Object[0]);
/*     */     
/* 551 */     return () -> new AbstractIterator<MutableBlockPos>() {
/* 552 */         private final Direction[] directions = new Direction[] { this.val$firstDirection, this.val$secondDirection, this.val$firstDirection
/*     */ 
/*     */             
/* 555 */             .getOpposite(), this.val$secondDirection
/* 556 */             .getOpposite() };
/*     */         
/* 558 */         private final BlockPos.MutableBlockPos cursor = center.mutable().move(secondDirection);
/* 559 */         private final int legs = 4 * radius;
/* 560 */         private int leg = -1;
/*     */         
/*     */         private int legSize;
/*     */         private int legIndex;
/* 564 */         private int lastX = this.cursor.getX();
/* 565 */         private int lastY = this.cursor.getY();
/* 566 */         private int lastZ = this.cursor.getZ();
/*     */ 
/*     */         
/*     */         protected BlockPos.MutableBlockPos computeNext() {
/* 570 */           this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
/*     */           
/* 572 */           this.lastX = this.cursor.getX();
/* 573 */           this.lastY = this.cursor.getY();
/* 574 */           this.lastZ = this.cursor.getZ();
/*     */           
/* 576 */           if (this.legIndex >= this.legSize) {
/* 577 */             if (this.leg >= this.legs) {
/* 578 */               return (BlockPos.MutableBlockPos)endOfData();
/*     */             }
/* 580 */             this.leg++;
/* 581 */             this.legIndex = 0;
/* 582 */             this.legSize = this.leg / 2 + 1;
/*     */           } 
/* 584 */           this.legIndex++;
/*     */           
/* 586 */           return this.cursor;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\BlockPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */