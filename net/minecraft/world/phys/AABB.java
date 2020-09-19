/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AABB
/*     */ {
/*     */   public final double minX;
/*     */   public final double minY;
/*     */   public final double minZ;
/*     */   public final double maxX;
/*     */   public final double maxY;
/*     */   public final double maxZ;
/*     */   
/*     */   public AABB(double debug1, double debug3, double debug5, double debug7, double debug9, double debug11) {
/*  26 */     this.minX = Math.min(debug1, debug7);
/*  27 */     this.minY = Math.min(debug3, debug9);
/*  28 */     this.minZ = Math.min(debug5, debug11);
/*  29 */     this.maxX = Math.max(debug1, debug7);
/*  30 */     this.maxY = Math.max(debug3, debug9);
/*  31 */     this.maxZ = Math.max(debug5, debug11);
/*     */   }
/*     */   
/*     */   public AABB(BlockPos debug1) {
/*  35 */     this(debug1.getX(), debug1.getY(), debug1.getZ(), (debug1.getX() + 1), (debug1.getY() + 1), (debug1.getZ() + 1));
/*     */   }
/*     */   
/*     */   public AABB(BlockPos debug1, BlockPos debug2) {
/*  39 */     this(debug1.getX(), debug1.getY(), debug1.getZ(), debug2.getX(), debug2.getY(), debug2.getZ());
/*     */   }
/*     */   
/*     */   public AABB(Vec3 debug1, Vec3 debug2) {
/*  43 */     this(debug1.x, debug1.y, debug1.z, debug2.x, debug2.y, debug2.z);
/*     */   }
/*     */   
/*     */   public static AABB of(BoundingBox debug0) {
/*  47 */     return new AABB(debug0.x0, debug0.y0, debug0.z0, (debug0.x1 + 1), (debug0.y1 + 1), (debug0.z1 + 1));
/*     */   }
/*     */   
/*     */   public static AABB unitCubeFromLowerCorner(Vec3 debug0) {
/*  51 */     return new AABB(debug0.x, debug0.y, debug0.z, debug0.x + 1.0D, debug0.y + 1.0D, debug0.z + 1.0D);
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
/*     */   public double min(Direction.Axis debug1) {
/*  79 */     return debug1.choose(this.minX, this.minY, this.minZ);
/*     */   }
/*     */   
/*     */   public double max(Direction.Axis debug1) {
/*  83 */     return debug1.choose(this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  88 */     if (this == debug1) {
/*  89 */       return true;
/*     */     }
/*  91 */     if (!(debug1 instanceof AABB)) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     AABB debug2 = (AABB)debug1;
/*     */     
/*  97 */     if (Double.compare(debug2.minX, this.minX) != 0) {
/*  98 */       return false;
/*     */     }
/* 100 */     if (Double.compare(debug2.minY, this.minY) != 0) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (Double.compare(debug2.minZ, this.minZ) != 0) {
/* 104 */       return false;
/*     */     }
/* 106 */     if (Double.compare(debug2.maxX, this.maxX) != 0) {
/* 107 */       return false;
/*     */     }
/* 109 */     if (Double.compare(debug2.maxY, this.maxY) != 0) {
/* 110 */       return false;
/*     */     }
/* 112 */     return (Double.compare(debug2.maxZ, this.maxZ) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     long debug1 = Double.doubleToLongBits(this.minX);
/* 118 */     int debug3 = (int)(debug1 ^ debug1 >>> 32L);
/* 119 */     debug1 = Double.doubleToLongBits(this.minY);
/* 120 */     debug3 = 31 * debug3 + (int)(debug1 ^ debug1 >>> 32L);
/* 121 */     debug1 = Double.doubleToLongBits(this.minZ);
/* 122 */     debug3 = 31 * debug3 + (int)(debug1 ^ debug1 >>> 32L);
/* 123 */     debug1 = Double.doubleToLongBits(this.maxX);
/* 124 */     debug3 = 31 * debug3 + (int)(debug1 ^ debug1 >>> 32L);
/* 125 */     debug1 = Double.doubleToLongBits(this.maxY);
/* 126 */     debug3 = 31 * debug3 + (int)(debug1 ^ debug1 >>> 32L);
/* 127 */     debug1 = Double.doubleToLongBits(this.maxZ);
/* 128 */     debug3 = 31 * debug3 + (int)(debug1 ^ debug1 >>> 32L);
/* 129 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB contract(double debug1, double debug3, double debug5) {
/* 139 */     double debug7 = this.minX;
/* 140 */     double debug9 = this.minY;
/* 141 */     double debug11 = this.minZ;
/* 142 */     double debug13 = this.maxX;
/* 143 */     double debug15 = this.maxY;
/* 144 */     double debug17 = this.maxZ;
/*     */     
/* 146 */     if (debug1 < 0.0D) {
/* 147 */       debug7 -= debug1;
/* 148 */     } else if (debug1 > 0.0D) {
/* 149 */       debug13 -= debug1;
/*     */     } 
/*     */     
/* 152 */     if (debug3 < 0.0D) {
/* 153 */       debug9 -= debug3;
/* 154 */     } else if (debug3 > 0.0D) {
/* 155 */       debug15 -= debug3;
/*     */     } 
/*     */     
/* 158 */     if (debug5 < 0.0D) {
/* 159 */       debug11 -= debug5;
/* 160 */     } else if (debug5 > 0.0D) {
/* 161 */       debug17 -= debug5;
/*     */     } 
/*     */     
/* 164 */     return new AABB(debug7, debug9, debug11, debug13, debug15, debug17);
/*     */   }
/*     */   
/*     */   public AABB expandTowards(Vec3 debug1) {
/* 168 */     return expandTowards(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB expandTowards(double debug1, double debug3, double debug5) {
/* 178 */     double debug7 = this.minX;
/* 179 */     double debug9 = this.minY;
/* 180 */     double debug11 = this.minZ;
/* 181 */     double debug13 = this.maxX;
/* 182 */     double debug15 = this.maxY;
/* 183 */     double debug17 = this.maxZ;
/*     */     
/* 185 */     if (debug1 < 0.0D) {
/* 186 */       debug7 += debug1;
/* 187 */     } else if (debug1 > 0.0D) {
/* 188 */       debug13 += debug1;
/*     */     } 
/*     */     
/* 191 */     if (debug3 < 0.0D) {
/* 192 */       debug9 += debug3;
/* 193 */     } else if (debug3 > 0.0D) {
/* 194 */       debug15 += debug3;
/*     */     } 
/*     */     
/* 197 */     if (debug5 < 0.0D) {
/* 198 */       debug11 += debug5;
/* 199 */     } else if (debug5 > 0.0D) {
/* 200 */       debug17 += debug5;
/*     */     } 
/*     */     
/* 203 */     return new AABB(debug7, debug9, debug11, debug13, debug15, debug17);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AABB inflate(double debug1, double debug3, double debug5) {
/* 213 */     double debug7 = this.minX - debug1;
/* 214 */     double debug9 = this.minY - debug3;
/* 215 */     double debug11 = this.minZ - debug5;
/* 216 */     double debug13 = this.maxX + debug1;
/* 217 */     double debug15 = this.maxY + debug3;
/* 218 */     double debug17 = this.maxZ + debug5;
/*     */     
/* 220 */     return new AABB(debug7, debug9, debug11, debug13, debug15, debug17);
/*     */   }
/*     */   
/*     */   public AABB inflate(double debug1) {
/* 224 */     return inflate(debug1, debug1, debug1);
/*     */   }
/*     */   
/*     */   public AABB intersect(AABB debug1) {
/* 228 */     double debug2 = Math.max(this.minX, debug1.minX);
/* 229 */     double debug4 = Math.max(this.minY, debug1.minY);
/* 230 */     double debug6 = Math.max(this.minZ, debug1.minZ);
/* 231 */     double debug8 = Math.min(this.maxX, debug1.maxX);
/* 232 */     double debug10 = Math.min(this.maxY, debug1.maxY);
/* 233 */     double debug12 = Math.min(this.maxZ, debug1.maxZ);
/*     */     
/* 235 */     return new AABB(debug2, debug4, debug6, debug8, debug10, debug12);
/*     */   }
/*     */   
/*     */   public AABB minmax(AABB debug1) {
/* 239 */     double debug2 = Math.min(this.minX, debug1.minX);
/* 240 */     double debug4 = Math.min(this.minY, debug1.minY);
/* 241 */     double debug6 = Math.min(this.minZ, debug1.minZ);
/* 242 */     double debug8 = Math.max(this.maxX, debug1.maxX);
/* 243 */     double debug10 = Math.max(this.maxY, debug1.maxY);
/* 244 */     double debug12 = Math.max(this.maxZ, debug1.maxZ);
/*     */     
/* 246 */     return new AABB(debug2, debug4, debug6, debug8, debug10, debug12);
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
/*     */   public AABB move(double debug1, double debug3, double debug5) {
/* 282 */     return new AABB(this.minX + debug1, this.minY + debug3, this.minZ + debug5, this.maxX + debug1, this.maxY + debug3, this.maxZ + debug5);
/*     */   }
/*     */   
/*     */   public AABB move(BlockPos debug1) {
/* 286 */     return new AABB(this.minX + debug1.getX(), this.minY + debug1.getY(), this.minZ + debug1.getZ(), this.maxX + debug1.getX(), this.maxY + debug1.getY(), this.maxZ + debug1.getZ());
/*     */   }
/*     */   
/*     */   public AABB move(Vec3 debug1) {
/* 290 */     return move(debug1.x, debug1.y, debug1.z);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(AABB debug1) {
/* 360 */     return intersects(debug1.minX, debug1.minY, debug1.minZ, debug1.maxX, debug1.maxY, debug1.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersects(double debug1, double debug3, double debug5, double debug7, double debug9, double debug11) {
/* 365 */     return (this.minX < debug7 && this.maxX > debug1 && this.minY < debug9 && this.maxY > debug3 && this.minZ < debug11 && this.maxZ > debug5);
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
/*     */   public boolean contains(Vec3 debug1) {
/* 378 */     return contains(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */   
/*     */   public boolean contains(double debug1, double debug3, double debug5) {
/* 382 */     return (debug1 >= this.minX && debug1 < this.maxX && debug3 >= this.minY && debug3 < this.maxY && debug5 >= this.minZ && debug5 < this.maxZ);
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 386 */     double debug1 = getXsize();
/* 387 */     double debug3 = getYsize();
/* 388 */     double debug5 = getZsize();
/* 389 */     return (debug1 + debug3 + debug5) / 3.0D;
/*     */   }
/*     */   
/*     */   public double getXsize() {
/* 393 */     return this.maxX - this.minX;
/*     */   }
/*     */   
/*     */   public double getYsize() {
/* 397 */     return this.maxY - this.minY;
/*     */   }
/*     */   
/*     */   public double getZsize() {
/* 401 */     return this.maxZ - this.minZ;
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
/*     */   public AABB deflate(double debug1) {
/* 417 */     return inflate(-debug1);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> clip(Vec3 debug1, Vec3 debug2) {
/* 421 */     double[] debug3 = { 1.0D };
/* 422 */     double debug4 = debug2.x - debug1.x;
/* 423 */     double debug6 = debug2.y - debug1.y;
/* 424 */     double debug8 = debug2.z - debug1.z;
/*     */     
/* 426 */     Direction debug10 = getDirection(this, debug1, debug3, null, debug4, debug6, debug8);
/* 427 */     if (debug10 == null) {
/* 428 */       return Optional.empty();
/*     */     }
/*     */     
/* 431 */     double debug11 = debug3[0];
/* 432 */     return Optional.of(debug1.add(debug11 * debug4, debug11 * debug6, debug11 * debug8));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BlockHitResult clip(Iterable<AABB> debug0, Vec3 debug1, Vec3 debug2, BlockPos debug3) {
/* 437 */     double[] debug4 = { 1.0D };
/* 438 */     Direction debug5 = null;
/*     */     
/* 440 */     double debug6 = debug2.x - debug1.x;
/* 441 */     double debug8 = debug2.y - debug1.y;
/* 442 */     double debug10 = debug2.z - debug1.z;
/*     */     
/* 444 */     for (AABB debug13 : debug0) {
/* 445 */       debug5 = getDirection(debug13.move(debug3), debug1, debug4, debug5, debug6, debug8, debug10);
/*     */     }
/*     */     
/* 448 */     if (debug5 == null) {
/* 449 */       return null;
/*     */     }
/*     */     
/* 452 */     double debug12 = debug4[0];
/* 453 */     return new BlockHitResult(debug1.add(debug12 * debug6, debug12 * debug8, debug12 * debug10), debug5, debug3, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Direction getDirection(AABB debug0, Vec3 debug1, double[] debug2, @Nullable Direction debug3, double debug4, double debug6, double debug8) {
/* 458 */     if (debug4 > 1.0E-7D) {
/* 459 */       debug3 = clipPoint(debug2, debug3, debug4, debug6, debug8, debug0.minX, debug0.minY, debug0.maxY, debug0.minZ, debug0.maxZ, Direction.WEST, debug1.x, debug1.y, debug1.z);
/* 460 */     } else if (debug4 < -1.0E-7D) {
/* 461 */       debug3 = clipPoint(debug2, debug3, debug4, debug6, debug8, debug0.maxX, debug0.minY, debug0.maxY, debug0.minZ, debug0.maxZ, Direction.EAST, debug1.x, debug1.y, debug1.z);
/*     */     } 
/*     */     
/* 464 */     if (debug6 > 1.0E-7D) {
/* 465 */       debug3 = clipPoint(debug2, debug3, debug6, debug8, debug4, debug0.minY, debug0.minZ, debug0.maxZ, debug0.minX, debug0.maxX, Direction.DOWN, debug1.y, debug1.z, debug1.x);
/* 466 */     } else if (debug6 < -1.0E-7D) {
/* 467 */       debug3 = clipPoint(debug2, debug3, debug6, debug8, debug4, debug0.maxY, debug0.minZ, debug0.maxZ, debug0.minX, debug0.maxX, Direction.UP, debug1.y, debug1.z, debug1.x);
/*     */     } 
/*     */     
/* 470 */     if (debug8 > 1.0E-7D) {
/* 471 */       debug3 = clipPoint(debug2, debug3, debug8, debug4, debug6, debug0.minZ, debug0.minX, debug0.maxX, debug0.minY, debug0.maxY, Direction.NORTH, debug1.z, debug1.x, debug1.y);
/* 472 */     } else if (debug8 < -1.0E-7D) {
/* 473 */       debug3 = clipPoint(debug2, debug3, debug8, debug4, debug6, debug0.maxZ, debug0.minX, debug0.maxX, debug0.minY, debug0.maxY, Direction.SOUTH, debug1.z, debug1.x, debug1.y);
/*     */     } 
/* 475 */     return debug3;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Direction clipPoint(double[] debug0, @Nullable Direction debug1, double debug2, double debug4, double debug6, double debug8, double debug10, double debug12, double debug14, double debug16, Direction debug18, double debug19, double debug21, double debug23) {
/* 480 */     double debug25 = (debug8 - debug19) / debug2;
/* 481 */     double debug27 = debug21 + debug25 * debug4;
/* 482 */     double debug29 = debug23 + debug25 * debug6;
/* 483 */     if (0.0D < debug25 && debug25 < debug0[0] && debug10 - 1.0E-7D < debug27 && debug27 < debug12 + 1.0E-7D && debug14 - 1.0E-7D < debug29 && debug29 < debug16 + 1.0E-7D) {
/*     */ 
/*     */ 
/*     */       
/* 487 */       debug0[0] = debug25;
/* 488 */       return debug18;
/*     */     } 
/* 490 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 495 */     return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getCenter() {
/* 503 */     return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), Mth.lerp(0.5D, this.minY, this.maxY), Mth.lerp(0.5D, this.minZ, this.maxZ));
/*     */   }
/*     */   
/*     */   public static AABB ofSize(double debug0, double debug2, double debug4) {
/* 507 */     return new AABB(-debug0 / 2.0D, -debug2 / 2.0D, -debug4 / 2.0D, debug0 / 2.0D, debug2 / 2.0D, debug4 / 2.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\AABB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */