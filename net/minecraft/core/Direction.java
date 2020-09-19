/*     */ package net.minecraft.core;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public enum Direction implements StringRepresentable {
/*     */   private final int data3d;
/*     */   private final int oppositeIndex;
/*     */   private final int data2d;
/*     */   private final String name;
/*     */   private final Axis axis;
/*     */   private final AxisDirection axisDirection;
/*  28 */   DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
/*  29 */   UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
/*  30 */   NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
/*  31 */   SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
/*  32 */   WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
/*  33 */   EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
/*     */   
/*     */   private final Vec3i normal;
/*     */   
/*     */   private static final Direction[] VALUES;
/*     */   private static final Map<String, Direction> BY_NAME;
/*     */   private static final Direction[] BY_3D_DATA;
/*     */   private static final Direction[] BY_2D_DATA;
/*     */   private static final Long2ObjectMap<Direction> BY_NORMAL;
/*     */   
/*     */   static {
/*  44 */     VALUES = values();
/*     */     
/*  46 */     BY_NAME = (Map<String, Direction>)Arrays.<Direction>stream(VALUES).collect(Collectors.toMap(Direction::getName, debug0 -> debug0));
/*  47 */     BY_3D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).sorted(Comparator.comparingInt(debug0 -> debug0.data3d)).toArray(debug0 -> new Direction[debug0]);
/*  48 */     BY_2D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).filter(debug0 -> debug0.getAxis().isHorizontal()).sorted(Comparator.comparingInt(debug0 -> debug0.data2d)).toArray(debug0 -> new Direction[debug0]);
/*  49 */     BY_NORMAL = (Long2ObjectMap<Direction>)Arrays.<Direction>stream(VALUES).collect(Collectors.toMap(debug0 -> Long.valueOf((new BlockPos(debug0.getNormal())).asLong()), debug0 -> debug0, (debug0, debug1) -> { throw new IllegalArgumentException("Duplicate keys"); }it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap::new));
/*     */   }
/*     */   Direction(int debug3, int debug4, int debug5, String debug6, AxisDirection debug7, Axis debug8, Vec3i debug9) {
/*  52 */     this.data3d = debug3;
/*  53 */     this.data2d = debug5;
/*  54 */     this.oppositeIndex = debug4;
/*  55 */     this.name = debug6;
/*  56 */     this.axis = debug8;
/*  57 */     this.axisDirection = debug7;
/*  58 */     this.normal = debug9;
/*     */   }
/*     */   
/*     */   public static Direction[] orderedByNearest(Entity debug0) {
/*  62 */     float debug1 = debug0.getViewXRot(1.0F) * 0.017453292F;
/*  63 */     float debug2 = -debug0.getViewYRot(1.0F) * 0.017453292F;
/*     */     
/*  65 */     float debug3 = Mth.sin(debug1);
/*  66 */     float debug4 = Mth.cos(debug1);
/*  67 */     float debug5 = Mth.sin(debug2);
/*  68 */     float debug6 = Mth.cos(debug2);
/*     */     
/*  70 */     boolean debug7 = (debug5 > 0.0F);
/*  71 */     boolean debug8 = (debug3 < 0.0F);
/*  72 */     boolean debug9 = (debug6 > 0.0F);
/*     */     
/*  74 */     float debug10 = debug7 ? debug5 : -debug5;
/*  75 */     float debug11 = debug8 ? -debug3 : debug3;
/*  76 */     float debug12 = debug9 ? debug6 : -debug6;
/*     */     
/*  78 */     float debug13 = debug10 * debug4;
/*  79 */     float debug14 = debug12 * debug4;
/*     */     
/*  81 */     Direction debug15 = debug7 ? EAST : WEST;
/*  82 */     Direction debug16 = debug8 ? UP : DOWN;
/*  83 */     Direction debug17 = debug9 ? SOUTH : NORTH;
/*     */     
/*  85 */     if (debug10 > debug12) {
/*  86 */       if (debug11 > debug13)
/*  87 */         return makeDirectionArray(debug16, debug15, debug17); 
/*  88 */       if (debug14 > debug11) {
/*  89 */         return makeDirectionArray(debug15, debug17, debug16);
/*     */       }
/*  91 */       return makeDirectionArray(debug15, debug16, debug17);
/*     */     } 
/*     */     
/*  94 */     if (debug11 > debug14)
/*  95 */       return makeDirectionArray(debug16, debug17, debug15); 
/*  96 */     if (debug13 > debug11) {
/*  97 */       return makeDirectionArray(debug17, debug15, debug16);
/*     */     }
/*  99 */     return makeDirectionArray(debug17, debug16, debug15);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Direction[] makeDirectionArray(Direction debug0, Direction debug1, Direction debug2) {
/* 105 */     return new Direction[] { debug0, debug1, debug2, debug2.getOpposite(), debug1.getOpposite(), debug0.getOpposite() };
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
/*     */   public int get3DDataValue() {
/* 139 */     return this.data3d;
/*     */   }
/*     */   
/*     */   public int get2DDataValue() {
/* 143 */     return this.data2d;
/*     */   }
/*     */   
/*     */   public AxisDirection getAxisDirection() {
/* 147 */     return this.axisDirection;
/*     */   }
/*     */   
/*     */   public Direction getOpposite() {
/* 151 */     return from3DDataValue(this.oppositeIndex);
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
/*     */   public Direction getClockWise() {
/* 205 */     switch (this) {
/*     */       case Z:
/* 207 */         return EAST;
/*     */       case null:
/* 209 */         return SOUTH;
/*     */       case null:
/* 211 */         return WEST;
/*     */       case null:
/* 213 */         return NORTH;
/*     */     } 
/* 215 */     throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
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
/*     */   public Direction getCounterClockWise() {
/* 280 */     switch (this) {
/*     */       case Z:
/* 282 */         return WEST;
/*     */       case null:
/* 284 */         return NORTH;
/*     */       case null:
/* 286 */         return EAST;
/*     */       case null:
/* 288 */         return SOUTH;
/*     */     } 
/* 290 */     throw new IllegalStateException("Unable to get CCW facing of " + this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStepX() {
/* 295 */     return this.normal.getX();
/*     */   }
/*     */   
/*     */   public int getStepY() {
/* 299 */     return this.normal.getY();
/*     */   }
/*     */   
/*     */   public int getStepZ() {
/* 303 */     return this.normal.getZ();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 311 */     return this.name;
/*     */   }
/*     */   
/*     */   public Axis getAxis() {
/* 315 */     return this.axis;
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
/*     */   public static Direction from3DDataValue(int debug0) {
/* 327 */     return BY_3D_DATA[Mth.abs(debug0 % BY_3D_DATA.length)];
/*     */   }
/*     */   
/*     */   public static Direction from2DDataValue(int debug0) {
/* 331 */     return BY_2D_DATA[Mth.abs(debug0 % BY_2D_DATA.length)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Direction fromNormal(int debug0, int debug1, int debug2) {
/* 341 */     return (Direction)BY_NORMAL.get(BlockPos.asLong(debug0, debug1, debug2));
/*     */   }
/*     */   
/*     */   public static Direction fromYRot(double debug0) {
/* 345 */     return from2DDataValue(Mth.floor(debug0 / 90.0D + 0.5D) & 0x3);
/*     */   }
/*     */   
/*     */   public static Direction fromAxisAndDirection(Axis debug0, AxisDirection debug1) {
/* 349 */     switch (debug0) {
/*     */       case X:
/* 351 */         return (debug1 == AxisDirection.POSITIVE) ? EAST : WEST;
/*     */       case Y:
/* 353 */         return (debug1 == AxisDirection.POSITIVE) ? UP : DOWN;
/*     */     } 
/*     */     
/* 356 */     return (debug1 == AxisDirection.POSITIVE) ? SOUTH : NORTH;
/*     */   }
/*     */ 
/*     */   
/*     */   public float toYRot() {
/* 361 */     return ((this.data2d & 0x3) * 90);
/*     */   }
/*     */   
/*     */   public static Direction getRandom(Random debug0) {
/* 365 */     return (Direction)Util.getRandom((Object[])VALUES, debug0);
/*     */   }
/*     */   
/*     */   public static Direction getNearest(double debug0, double debug2, double debug4) {
/* 369 */     return getNearest((float)debug0, (float)debug2, (float)debug4);
/*     */   }
/*     */   
/*     */   public static Direction getNearest(float debug0, float debug1, float debug2) {
/* 373 */     Direction debug3 = NORTH;
/* 374 */     float debug4 = Float.MIN_VALUE;
/* 375 */     for (Direction debug8 : VALUES) {
/* 376 */       float debug9 = debug0 * debug8.normal.getX() + debug1 * debug8.normal.getY() + debug2 * debug8.normal.getZ();
/*     */       
/* 378 */       if (debug9 > debug4) {
/* 379 */         debug4 = debug9;
/* 380 */         debug3 = debug8;
/*     */       } 
/*     */     } 
/* 383 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 388 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/* 393 */     return this.name;
/*     */   }
/*     */   
/*     */   public static Direction get(AxisDirection debug0, Axis debug1) {
/* 397 */     for (Direction debug5 : VALUES) {
/* 398 */       if (debug5.getAxisDirection() == debug0 && debug5.getAxis() == debug1) {
/* 399 */         return debug5;
/*     */       }
/*     */     } 
/* 402 */     throw new IllegalArgumentException("No such direction: " + debug0 + " " + debug1);
/*     */   }
/*     */   
/*     */   public enum Axis implements StringRepresentable, Predicate<Direction> {
/* 406 */     X("x")
/*     */     {
/*     */       public int choose(int debug1, int debug2, int debug3) {
/* 409 */         return debug1;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double debug1, double debug3, double debug5) {
/* 414 */         return debug1;
/*     */       }
/*     */     },
/* 417 */     Y("y")
/*     */     {
/*     */       public int choose(int debug1, int debug2, int debug3) {
/* 420 */         return debug2;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double debug1, double debug3, double debug5) {
/* 425 */         return debug3;
/*     */       }
/*     */     },
/* 428 */     Z("z")
/*     */     {
/*     */       public int choose(int debug1, int debug2, int debug3) {
/* 431 */         return debug3;
/*     */       }
/*     */ 
/*     */       
/*     */       public double choose(double debug1, double debug3, double debug5) {
/* 436 */         return debug5;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 441 */     private static final Axis[] VALUES = values();
/*     */     
/* 443 */     public static final Codec<Axis> CODEC = StringRepresentable.fromEnum(Axis::values, Axis::byName); private static final Map<String, Axis> BY_NAME;
/*     */     static {
/* 445 */       BY_NAME = (Map<String, Axis>)Arrays.<Axis>stream(VALUES).collect(Collectors.toMap(Axis::getName, debug0 -> debug0));
/*     */     }
/*     */     private final String name;
/*     */     
/*     */     Axis(String debug3) {
/* 450 */       this.name = debug3;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public static Axis byName(String debug0) {
/* 455 */       return BY_NAME.get(debug0.toLowerCase(Locale.ROOT));
/*     */     }
/*     */     
/*     */     public String getName() {
/* 459 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isVertical() {
/* 463 */       return (this == Y);
/*     */     }
/*     */     
/*     */     public boolean isHorizontal() {
/* 467 */       return (this == X || this == Z);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 472 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Axis getRandom(Random debug0) {
/* 476 */       return (Axis)Util.getRandom((Object[])VALUES, debug0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(@Nullable Direction debug1) {
/* 481 */       return (debug1 != null && debug1.getAxis() == this);
/*     */     }
/*     */     
/*     */     public Direction.Plane getPlane() {
/* 485 */       switch (this) {
/*     */         case X:
/*     */         case Z:
/* 488 */           return Direction.Plane.HORIZONTAL;
/*     */         case Y:
/* 490 */           return Direction.Plane.VERTICAL;
/*     */       } 
/* 492 */       throw new Error("Someone's been tampering with the universe!");
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 497 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract int choose(int param1Int1, int param1Int2, int param1Int3);
/*     */     
/*     */     public abstract double choose(double param1Double1, double param1Double2, double param1Double3);
/*     */   }
/*     */   
/*     */   public enum AxisDirection {
/* 506 */     POSITIVE(1, "Towards positive"),
/* 507 */     NEGATIVE(-1, "Towards negative");
/*     */     
/*     */     private final int step;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     AxisDirection(int debug3, String debug4) {
/* 514 */       this.step = debug3;
/* 515 */       this.name = debug4;
/*     */     }
/*     */     
/*     */     public int getStep() {
/* 519 */       return this.step;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 528 */       return this.name;
/*     */     }
/*     */     
/*     */     public AxisDirection opposite() {
/* 532 */       return (this == POSITIVE) ? NEGATIVE : POSITIVE;
/*     */     }
/*     */   }
/*     */   
/*     */   public Vec3i getNormal() {
/* 537 */     return this.normal;
/*     */   }
/*     */   
/*     */   public boolean isFacingAngle(float debug1) {
/* 541 */     float debug2 = debug1 * 0.017453292F;
/* 542 */     float debug3 = -Mth.sin(debug2);
/* 543 */     float debug4 = Mth.cos(debug2);
/* 544 */     return (this.normal.getX() * debug3 + this.normal.getZ() * debug4 > 0.0F);
/*     */   }
/*     */   
/*     */   public enum Plane implements Iterable<Direction>, Predicate<Direction> {
/* 548 */     HORIZONTAL((String)new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }),
/* 549 */     VERTICAL((String)new Direction[] { Direction.UP, Direction.DOWN }, new Direction.Axis[] { Direction.Axis.Y });
/*     */     
/*     */     private final Direction[] faces;
/*     */     
/*     */     private final Direction.Axis[] axis;
/*     */     
/*     */     Plane(Direction[] debug3, Direction.Axis[] debug4) {
/* 556 */       this.faces = debug3;
/* 557 */       this.axis = debug4;
/*     */     }
/*     */     
/*     */     public Direction getRandomDirection(Random debug1) {
/* 561 */       return (Direction)Util.getRandom((Object[])this.faces, debug1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean test(@Nullable Direction debug1) {
/* 570 */       return (debug1 != null && debug1.getAxis().getPlane() == this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Direction> iterator() {
/* 575 */       return (Iterator<Direction>)Iterators.forArray((Object[])this.faces);
/*     */     }
/*     */     
/*     */     public Stream<Direction> stream() {
/* 579 */       return Arrays.stream(this.faces);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Direction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */