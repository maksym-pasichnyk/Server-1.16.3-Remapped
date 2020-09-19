/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.math.IntMath;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public final class Shapes
/*     */ {
/*     */   static {
/*  26 */     BLOCK = (VoxelShape)Util.make(() -> {
/*     */           DiscreteVoxelShape debug0 = new BitSetDiscreteVoxelShape(1, 1, 1);
/*     */           debug0.setFull(0, 0, 0, true, true);
/*     */           return new CubeVoxelShape(debug0);
/*     */         });
/*     */   }
/*  32 */   private static final VoxelShape BLOCK; public static final VoxelShape INFINITY = box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static final VoxelShape EMPTY = new ArrayVoxelShape(new BitSetDiscreteVoxelShape(0, 0, 0), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VoxelShape empty() {
/*  45 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static VoxelShape block() {
/*  49 */     return BLOCK;
/*     */   }
/*     */   
/*     */   public static VoxelShape box(double debug0, double debug2, double debug4, double debug6, double debug8, double debug10) {
/*  53 */     return create(new AABB(debug0, debug2, debug4, debug6, debug8, debug10));
/*     */   }
/*     */   
/*     */   public static VoxelShape create(AABB debug0) {
/*  57 */     int debug1 = findBits(debug0.minX, debug0.maxX);
/*  58 */     int debug2 = findBits(debug0.minY, debug0.maxY);
/*  59 */     int debug3 = findBits(debug0.minZ, debug0.maxZ);
/*     */     
/*  61 */     if (debug1 < 0 || debug2 < 0 || debug3 < 0) {
/*  62 */       return new ArrayVoxelShape(BLOCK.shape, new double[] { debug0.minX, debug0.maxX }, new double[] { debug0.minY, debug0.maxY }, new double[] { debug0.minZ, debug0.maxZ });
/*     */     }
/*     */     
/*  65 */     if (debug1 == 0 && debug2 == 0 && debug3 == 0) {
/*  66 */       return debug0.contains(0.5D, 0.5D, 0.5D) ? block() : empty();
/*     */     }
/*     */     
/*  69 */     int debug4 = 1 << debug1;
/*  70 */     int debug5 = 1 << debug2;
/*  71 */     int debug6 = 1 << debug3;
/*     */     
/*  73 */     int debug7 = (int)Math.round(debug0.minX * debug4);
/*  74 */     int debug8 = (int)Math.round(debug0.maxX * debug4);
/*  75 */     int debug9 = (int)Math.round(debug0.minY * debug5);
/*  76 */     int debug10 = (int)Math.round(debug0.maxY * debug5);
/*  77 */     int debug11 = (int)Math.round(debug0.minZ * debug6);
/*  78 */     int debug12 = (int)Math.round(debug0.maxZ * debug6);
/*     */     
/*  80 */     BitSetDiscreteVoxelShape debug13 = new BitSetDiscreteVoxelShape(debug4, debug5, debug6, debug7, debug9, debug11, debug8, debug10, debug12);
/*     */ 
/*     */     
/*     */     long debug14;
/*     */ 
/*     */     
/*  86 */     for (debug14 = debug7; debug14 < debug8; debug14++) {
/*  87 */       long debug16; for (debug16 = debug9; debug16 < debug10; debug16++) {
/*  88 */         long debug18; for (debug18 = debug11; debug18 < debug12; debug18++) {
/*  89 */           debug13.setFull((int)debug14, (int)debug16, (int)debug18, false, true);
/*     */         }
/*     */       } 
/*     */     } 
/*  93 */     return new CubeVoxelShape(debug13);
/*     */   }
/*     */   
/*     */   private static int findBits(double debug0, double debug2) {
/*  97 */     if (debug0 < -1.0E-7D || debug2 > 1.0000001D) {
/*  98 */       return -1;
/*     */     }
/* 100 */     for (int debug4 = 0; debug4 <= 3; debug4++) {
/* 101 */       double debug5 = debug0 * (1 << debug4);
/* 102 */       double debug7 = debug2 * (1 << debug4);
/* 103 */       boolean debug9 = (Math.abs(debug5 - Math.floor(debug5)) < 1.0E-7D);
/* 104 */       boolean debug10 = (Math.abs(debug7 - Math.floor(debug7)) < 1.0E-7D);
/* 105 */       if (debug9 && debug10) {
/* 106 */         return debug4;
/*     */       }
/*     */     } 
/* 109 */     return -1;
/*     */   }
/*     */   
/*     */   protected static long lcm(int debug0, int debug1) {
/* 113 */     return debug0 * (debug1 / IntMath.gcd(debug0, debug1));
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape debug0, VoxelShape debug1) {
/* 117 */     return join(debug0, debug1, BooleanOp.OR);
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape debug0, VoxelShape... debug1) {
/* 121 */     return Arrays.<VoxelShape>stream(debug1).reduce(debug0, Shapes::or);
/*     */   }
/*     */   
/*     */   public static VoxelShape join(VoxelShape debug0, VoxelShape debug1, BooleanOp debug2) {
/* 125 */     return joinUnoptimized(debug0, debug1, debug2).optimize();
/*     */   }
/*     */   
/*     */   public static VoxelShape joinUnoptimized(VoxelShape debug0, VoxelShape debug1, BooleanOp debug2) {
/* 129 */     if (debug2.apply(false, false)) {
/* 130 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/* 132 */     if (debug0 == debug1) {
/* 133 */       return debug2.apply(true, true) ? debug0 : empty();
/*     */     }
/* 135 */     boolean debug3 = debug2.apply(true, false);
/* 136 */     boolean debug4 = debug2.apply(false, true);
/*     */     
/* 138 */     if (debug0.isEmpty()) {
/* 139 */       return debug4 ? debug1 : empty();
/*     */     }
/* 141 */     if (debug1.isEmpty()) {
/* 142 */       return debug3 ? debug0 : empty();
/*     */     }
/*     */     
/* 145 */     IndexMerger debug5 = createIndexMerger(1, debug0.getCoords(Direction.Axis.X), debug1.getCoords(Direction.Axis.X), debug3, debug4);
/* 146 */     IndexMerger debug6 = createIndexMerger(debug5.getList().size() - 1, debug0.getCoords(Direction.Axis.Y), debug1.getCoords(Direction.Axis.Y), debug3, debug4);
/* 147 */     IndexMerger debug7 = createIndexMerger((debug5.getList().size() - 1) * (debug6.getList().size() - 1), debug0.getCoords(Direction.Axis.Z), debug1.getCoords(Direction.Axis.Z), debug3, debug4);
/*     */     
/* 149 */     BitSetDiscreteVoxelShape debug8 = BitSetDiscreteVoxelShape.join(debug0.shape, debug1.shape, debug5, debug6, debug7, debug2);
/* 150 */     if (debug5 instanceof DiscreteCubeMerger && debug6 instanceof DiscreteCubeMerger && debug7 instanceof DiscreteCubeMerger) {
/* 151 */       return new CubeVoxelShape(debug8);
/*     */     }
/* 153 */     return new ArrayVoxelShape(debug8, debug5.getList(), debug6.getList(), debug7.getList());
/*     */   }
/*     */   
/*     */   public static boolean joinIsNotEmpty(VoxelShape debug0, VoxelShape debug1, BooleanOp debug2) {
/* 157 */     if (debug2.apply(false, false)) {
/* 158 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException());
/*     */     }
/* 160 */     if (debug0 == debug1) {
/* 161 */       return debug2.apply(true, true);
/*     */     }
/* 163 */     if (debug0.isEmpty()) {
/* 164 */       return debug2.apply(false, !debug1.isEmpty());
/*     */     }
/* 166 */     if (debug1.isEmpty()) {
/* 167 */       return debug2.apply(!debug0.isEmpty(), false);
/*     */     }
/* 169 */     boolean debug3 = debug2.apply(true, false);
/* 170 */     boolean debug4 = debug2.apply(false, true);
/* 171 */     for (Direction.Axis debug8 : AxisCycle.AXIS_VALUES) {
/* 172 */       if (debug0.max(debug8) < debug1.min(debug8) - 1.0E-7D) {
/* 173 */         return (debug3 || debug4);
/*     */       }
/* 175 */       if (debug1.max(debug8) < debug0.min(debug8) - 1.0E-7D) {
/* 176 */         return (debug3 || debug4);
/*     */       }
/*     */     } 
/* 179 */     IndexMerger debug5 = createIndexMerger(1, debug0.getCoords(Direction.Axis.X), debug1.getCoords(Direction.Axis.X), debug3, debug4);
/* 180 */     IndexMerger debug6 = createIndexMerger(debug5.getList().size() - 1, debug0.getCoords(Direction.Axis.Y), debug1.getCoords(Direction.Axis.Y), debug3, debug4);
/* 181 */     IndexMerger debug7 = createIndexMerger((debug5.getList().size() - 1) * (debug6.getList().size() - 1), debug0.getCoords(Direction.Axis.Z), debug1.getCoords(Direction.Axis.Z), debug3, debug4);
/* 182 */     return joinIsNotEmpty(debug5, debug6, debug7, debug0.shape, debug1.shape, debug2);
/*     */   }
/*     */   
/*     */   private static boolean joinIsNotEmpty(IndexMerger debug0, IndexMerger debug1, IndexMerger debug2, DiscreteVoxelShape debug3, DiscreteVoxelShape debug4, BooleanOp debug5) {
/* 186 */     return !debug0.forMergedIndexes((debug5, debug6, debug7) -> debug0.forMergedIndexes(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double collide(Direction.Axis debug0, AABB debug1, Stream<VoxelShape> debug2, double debug3) {
/* 196 */     Iterator<VoxelShape> debug5 = debug2.iterator();
/* 197 */     while (debug5.hasNext()) {
/* 198 */       if (Math.abs(debug3) < 1.0E-7D) {
/* 199 */         return 0.0D;
/*     */       }
/* 201 */       debug3 = ((VoxelShape)debug5.next()).collide(debug0, debug1, debug3);
/*     */     } 
/* 203 */     return debug3;
/*     */   }
/*     */   
/*     */   public static double collide(Direction.Axis debug0, AABB debug1, LevelReader debug2, double debug3, CollisionContext debug5, Stream<VoxelShape> debug6) {
/* 207 */     return collide(debug1, debug2, debug3, debug5, AxisCycle.between(debug0, Direction.Axis.Z), debug6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static double collide(AABB debug0, LevelReader debug1, double debug2, CollisionContext debug4, AxisCycle debug5, Stream<VoxelShape> debug6) {
/* 213 */     if (debug0.getXsize() < 1.0E-6D || debug0.getYsize() < 1.0E-6D || debug0.getZsize() < 1.0E-6D) {
/* 214 */       return debug2;
/*     */     }
/*     */     
/* 217 */     if (Math.abs(debug2) < 1.0E-7D) {
/* 218 */       return 0.0D;
/*     */     }
/*     */     
/* 221 */     AxisCycle debug7 = debug5.inverse();
/* 222 */     Direction.Axis debug8 = debug7.cycle(Direction.Axis.X);
/* 223 */     Direction.Axis debug9 = debug7.cycle(Direction.Axis.Y);
/* 224 */     Direction.Axis debug10 = debug7.cycle(Direction.Axis.Z);
/*     */     
/* 226 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/*     */     
/* 228 */     int debug12 = Mth.floor(debug0.min(debug8) - 1.0E-7D) - 1;
/* 229 */     int debug13 = Mth.floor(debug0.max(debug8) + 1.0E-7D) + 1;
/* 230 */     int debug14 = Mth.floor(debug0.min(debug9) - 1.0E-7D) - 1;
/* 231 */     int debug15 = Mth.floor(debug0.max(debug9) + 1.0E-7D) + 1;
/* 232 */     double debug16 = debug0.min(debug10) - 1.0E-7D;
/* 233 */     double debug18 = debug0.max(debug10) + 1.0E-7D;
/*     */     
/* 235 */     boolean debug20 = (debug2 > 0.0D);
/*     */     
/* 237 */     int debug21 = debug20 ? (Mth.floor(debug0.max(debug10) - 1.0E-7D) - 1) : (Mth.floor(debug0.min(debug10) + 1.0E-7D) + 1);
/* 238 */     int debug22 = lastC(debug2, debug16, debug18);
/* 239 */     int debug23 = debug20 ? 1 : -1;
/*     */     int i;
/* 241 */     for (i = debug21; debug20 ? (i <= debug22) : (i >= debug22); i += debug23) {
/* 242 */       for (int debug25 = debug12; debug25 <= debug13; debug25++) {
/* 243 */         for (int debug26 = debug14; debug26 <= debug15; debug26++) {
/* 244 */           int debug27 = 0;
/* 245 */           if (debug25 == debug12 || debug25 == debug13) {
/* 246 */             debug27++;
/*     */           }
/* 248 */           if (debug26 == debug14 || debug26 == debug15) {
/* 249 */             debug27++;
/*     */           }
/* 251 */           if (i == debug21 || i == debug22) {
/* 252 */             debug27++;
/*     */           }
/* 254 */           if (debug27 < 3) {
/*     */ 
/*     */             
/* 257 */             debug11.set(debug7, debug25, debug26, i);
/*     */             
/* 259 */             BlockState debug28 = debug1.getBlockState((BlockPos)debug11);
/*     */             
/* 261 */             if (debug27 != 1 || debug28.hasLargeCollisionShape())
/*     */             {
/*     */               
/* 264 */               if (debug27 != 2 || debug28.is(Blocks.MOVING_PISTON)) {
/*     */ 
/*     */                 
/* 267 */                 debug2 = debug28.getCollisionShape((BlockGetter)debug1, (BlockPos)debug11, debug4).collide(debug10, debug0.move(-debug11.getX(), -debug11.getY(), -debug11.getZ()), debug2);
/* 268 */                 if (Math.abs(debug2) < 1.0E-7D) {
/* 269 */                   return 0.0D;
/*     */                 }
/* 271 */                 debug22 = lastC(debug2, debug16, debug18);
/*     */               }  } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 276 */     }  double[] debug24 = { debug2 };
/* 277 */     debug6.forEach(debug3 -> debug0[0] = debug3.collide(debug1, debug2, debug0[0]));
/*     */ 
/*     */ 
/*     */     
/* 281 */     return debug24[0];
/*     */   }
/*     */   
/*     */   private static int lastC(double debug0, double debug2, double debug4) {
/* 285 */     return (debug0 > 0.0D) ? (Mth.floor(debug4 + debug0) + 1) : (Mth.floor(debug2 + debug0) - 1);
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
/*     */   public static VoxelShape getFaceShape(VoxelShape debug0, Direction debug1) {
/*     */     boolean debug2;
/*     */     int debug3;
/* 311 */     if (debug0 == block()) {
/* 312 */       return block();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 317 */     Direction.Axis debug4 = debug1.getAxis();
/* 318 */     if (debug1.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 319 */       debug2 = DoubleMath.fuzzyEquals(debug0.max(debug4), 1.0D, 1.0E-7D);
/* 320 */       debug3 = debug0.shape.getSize(debug4) - 1;
/*     */     } else {
/* 322 */       debug2 = DoubleMath.fuzzyEquals(debug0.min(debug4), 0.0D, 1.0E-7D);
/* 323 */       debug3 = 0;
/*     */     } 
/*     */     
/* 326 */     if (!debug2) {
/* 327 */       return empty();
/*     */     }
/*     */     
/* 330 */     return new SliceShape(debug0, debug4, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean mergedFaceOccludes(VoxelShape debug0, VoxelShape debug1, Direction debug2) {
/* 337 */     if (debug0 == block() || debug1 == block()) {
/* 338 */       return true;
/*     */     }
/*     */     
/* 341 */     Direction.Axis debug3 = debug2.getAxis();
/* 342 */     Direction.AxisDirection debug4 = debug2.getAxisDirection();
/*     */     
/* 344 */     VoxelShape debug5 = (debug4 == Direction.AxisDirection.POSITIVE) ? debug0 : debug1;
/* 345 */     VoxelShape debug6 = (debug4 == Direction.AxisDirection.POSITIVE) ? debug1 : debug0;
/*     */     
/* 347 */     if (!DoubleMath.fuzzyEquals(debug5.max(debug3), 1.0D, 1.0E-7D)) {
/* 348 */       debug5 = empty();
/*     */     }
/* 350 */     if (!DoubleMath.fuzzyEquals(debug6.min(debug3), 0.0D, 1.0E-7D)) {
/* 351 */       debug6 = empty();
/*     */     }
/*     */     
/* 354 */     return !joinIsNotEmpty(block(), joinUnoptimized(new SliceShape(debug5, debug3, debug5.shape.getSize(debug3) - 1), new SliceShape(debug6, debug3, 0), BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean faceShapeOccludes(VoxelShape debug0, VoxelShape debug1) {
/* 361 */     if (debug0 == block() || debug1 == block()) {
/* 362 */       return true;
/*     */     }
/*     */     
/* 365 */     if (debug0.isEmpty() && debug1.isEmpty()) {
/* 366 */       return false;
/*     */     }
/*     */     
/* 369 */     return !joinIsNotEmpty(
/* 370 */         block(), 
/* 371 */         joinUnoptimized(debug0, debug1, BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static IndexMerger createIndexMerger(int debug0, DoubleList debug1, DoubleList debug2, boolean debug3, boolean debug4) {
/* 382 */     int debug5 = debug1.size() - 1;
/* 383 */     int debug6 = debug2.size() - 1;
/* 384 */     if (debug1 instanceof CubePointRange && debug2 instanceof CubePointRange) {
/* 385 */       long debug7 = lcm(debug5, debug6);
/* 386 */       if (debug0 * debug7 <= 256L) {
/* 387 */         return new DiscreteCubeMerger(debug5, debug6);
/*     */       }
/*     */     } 
/*     */     
/* 391 */     if (debug1.getDouble(debug5) < debug2.getDouble(0) - 1.0E-7D)
/* 392 */       return new NonOverlappingMerger(debug1, debug2, false); 
/* 393 */     if (debug2.getDouble(debug6) < debug1.getDouble(0) - 1.0E-7D) {
/* 394 */       return new NonOverlappingMerger(debug2, debug1, true);
/*     */     }
/*     */     
/* 397 */     if (debug5 == debug6 && Objects.equals(debug1, debug2)) {
/* 398 */       if (debug1 instanceof IdenticalMerger) {
/* 399 */         return (IndexMerger)debug1;
/*     */       }
/* 401 */       if (debug2 instanceof IdenticalMerger) {
/* 402 */         return (IndexMerger)debug2;
/*     */       }
/* 404 */       return new IdenticalMerger(debug1);
/*     */     } 
/*     */     
/* 407 */     return new IndirectMerger(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public static interface DoubleLineConsumer {
/*     */     void consume(double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\Shapes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */