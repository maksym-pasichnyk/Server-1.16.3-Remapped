/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import com.mojang.math.Vector3f;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class Vec3
/*     */   implements Position {
/*  12 */   public static final Vec3 ZERO = new Vec3(0.0D, 0.0D, 0.0D);
/*     */ 
/*     */   
/*     */   public final double x;
/*     */ 
/*     */   
/*     */   public final double y;
/*     */ 
/*     */   
/*     */   public final double z;
/*     */ 
/*     */   
/*     */   public static Vec3 atCenterOf(Vec3i debug0) {
/*  25 */     return new Vec3(debug0.getX() + 0.5D, debug0.getY() + 0.5D, debug0.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   public static Vec3 atLowerCornerOf(Vec3i debug0) {
/*  29 */     return new Vec3(debug0.getX(), debug0.getY(), debug0.getZ());
/*     */   }
/*     */   
/*     */   public static Vec3 atBottomCenterOf(Vec3i debug0) {
/*  33 */     return new Vec3(debug0.getX() + 0.5D, debug0.getY(), debug0.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   public static Vec3 upFromBottomCenterOf(Vec3i debug0, double debug1) {
/*  37 */     return new Vec3(debug0.getX() + 0.5D, debug0.getY() + debug1, debug0.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   public Vec3(double debug1, double debug3, double debug5) {
/*  41 */     this.x = debug1;
/*  42 */     this.y = debug3;
/*  43 */     this.z = debug5;
/*     */   }
/*     */   
/*     */   public Vec3(Vector3f debug1) {
/*  47 */     this(debug1.x(), debug1.y(), debug1.z());
/*     */   }
/*     */   
/*     */   public Vec3 vectorTo(Vec3 debug1) {
/*  51 */     return new Vec3(debug1.x - this.x, debug1.y - this.y, debug1.z - this.z);
/*     */   }
/*     */   
/*     */   public Vec3 normalize() {
/*  55 */     double debug1 = Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*  56 */     if (debug1 < 1.0E-4D) {
/*  57 */       return ZERO;
/*     */     }
/*  59 */     return new Vec3(this.x / debug1, this.y / debug1, this.z / debug1);
/*     */   }
/*     */   
/*     */   public double dot(Vec3 debug1) {
/*  63 */     return this.x * debug1.x + this.y * debug1.y + this.z * debug1.z;
/*     */   }
/*     */   
/*     */   public Vec3 cross(Vec3 debug1) {
/*  67 */     return new Vec3(this.y * debug1.z - this.z * debug1.y, this.z * debug1.x - this.x * debug1.z, this.x * debug1.y - this.y * debug1.x);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(Vec3 debug1) {
/*  71 */     return subtract(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(double debug1, double debug3, double debug5) {
/*  75 */     return add(-debug1, -debug3, -debug5);
/*     */   }
/*     */   
/*     */   public Vec3 add(Vec3 debug1) {
/*  79 */     return add(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */   
/*     */   public Vec3 add(double debug1, double debug3, double debug5) {
/*  83 */     return new Vec3(this.x + debug1, this.y + debug3, this.z + debug5);
/*     */   }
/*     */   
/*     */   public boolean closerThan(Position debug1, double debug2) {
/*  87 */     return (distanceToSqr(debug1.x(), debug1.y(), debug1.z()) < debug2 * debug2);
/*     */   }
/*     */   
/*     */   public double distanceTo(Vec3 debug1) {
/*  91 */     double debug2 = debug1.x - this.x;
/*  92 */     double debug4 = debug1.y - this.y;
/*  93 */     double debug6 = debug1.z - this.z;
/*  94 */     return Mth.sqrt(debug2 * debug2 + debug4 * debug4 + debug6 * debug6);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 debug1) {
/*  98 */     double debug2 = debug1.x - this.x;
/*  99 */     double debug4 = debug1.y - this.y;
/* 100 */     double debug6 = debug1.z - this.z;
/* 101 */     return debug2 * debug2 + debug4 * debug4 + debug6 * debug6;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(double debug1, double debug3, double debug5) {
/* 105 */     double debug7 = debug1 - this.x;
/* 106 */     double debug9 = debug3 - this.y;
/* 107 */     double debug11 = debug5 - this.z;
/* 108 */     return debug7 * debug7 + debug9 * debug9 + debug11 * debug11;
/*     */   }
/*     */   
/*     */   public Vec3 scale(double debug1) {
/* 112 */     return multiply(debug1, debug1, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 multiply(Vec3 debug1) {
/* 120 */     return multiply(debug1.x, debug1.y, debug1.z);
/*     */   }
/*     */   
/*     */   public Vec3 multiply(double debug1, double debug3, double debug5) {
/* 124 */     return new Vec3(this.x * debug1, this.y * debug3, this.z * debug5);
/*     */   }
/*     */   
/*     */   public double length() {
/* 128 */     return Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*     */   }
/*     */   
/*     */   public double lengthSqr() {
/* 132 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 137 */     if (this == debug1) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (!(debug1 instanceof Vec3)) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     Vec3 debug2 = (Vec3)debug1;
/*     */     
/* 146 */     if (Double.compare(debug2.x, this.x) != 0) {
/* 147 */       return false;
/*     */     }
/* 149 */     if (Double.compare(debug2.y, this.y) != 0) {
/* 150 */       return false;
/*     */     }
/* 152 */     return (Double.compare(debug2.z, this.z) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 159 */     long debug2 = Double.doubleToLongBits(this.x);
/* 160 */     int debug1 = (int)(debug2 ^ debug2 >>> 32L);
/* 161 */     debug2 = Double.doubleToLongBits(this.y);
/* 162 */     debug1 = 31 * debug1 + (int)(debug2 ^ debug2 >>> 32L);
/* 163 */     debug2 = Double.doubleToLongBits(this.z);
/* 164 */     debug1 = 31 * debug1 + (int)(debug2 ^ debug2 >>> 32L);
/* 165 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 170 */     return "(" + this.x + ", " + this.y + ", " + this.z + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 xRot(float debug1) {
/* 178 */     float debug2 = Mth.cos(debug1);
/* 179 */     float debug3 = Mth.sin(debug1);
/*     */     
/* 181 */     double debug4 = this.x;
/* 182 */     double debug6 = this.y * debug2 + this.z * debug3;
/* 183 */     double debug8 = this.z * debug2 - this.y * debug3;
/*     */     
/* 185 */     return new Vec3(debug4, debug6, debug8);
/*     */   }
/*     */   
/*     */   public Vec3 yRot(float debug1) {
/* 189 */     float debug2 = Mth.cos(debug1);
/* 190 */     float debug3 = Mth.sin(debug1);
/*     */     
/* 192 */     double debug4 = this.x * debug2 + this.z * debug3;
/* 193 */     double debug6 = this.y;
/* 194 */     double debug8 = this.z * debug2 - this.x * debug3;
/*     */     
/* 196 */     return new Vec3(debug4, debug6, debug8);
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
/*     */   public Vec3 align(EnumSet<Direction.Axis> debug1) {
/* 225 */     double debug2 = debug1.contains(Direction.Axis.X) ? Mth.floor(this.x) : this.x;
/* 226 */     double debug4 = debug1.contains(Direction.Axis.Y) ? Mth.floor(this.y) : this.y;
/* 227 */     double debug6 = debug1.contains(Direction.Axis.Z) ? Mth.floor(this.z) : this.z;
/* 228 */     return new Vec3(debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   public double get(Direction.Axis debug1) {
/* 232 */     return debug1.choose(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public final double x() {
/* 237 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public final double y() {
/* 242 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public final double z() {
/* 247 */     return this.z;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\Vec3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */