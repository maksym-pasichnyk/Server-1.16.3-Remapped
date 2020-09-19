/*     */ package net.minecraft.core;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ @Immutable
/*     */ public class Vec3i implements Comparable<Vec3i> {
/*     */   static {
/*  13 */     CODEC = Codec.INT_STREAM.comapFlatMap(debug0 -> Util.fixedSize(debug0, 3).map(()), debug0 -> IntStream.of(new int[] { debug0.getX(), debug0.getY(), debug0.getZ() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<Vec3i> CODEC;
/*  18 */   public static final Vec3i ZERO = new Vec3i(0, 0, 0);
/*     */   
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   
/*     */   public Vec3i(int debug1, int debug2, int debug3) {
/*  25 */     this.x = debug1;
/*  26 */     this.y = debug2;
/*  27 */     this.z = debug3;
/*     */   }
/*     */   
/*     */   public Vec3i(double debug1, double debug3, double debug5) {
/*  31 */     this(Mth.floor(debug1), Mth.floor(debug3), Mth.floor(debug5));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  36 */     if (this == debug1) {
/*  37 */       return true;
/*     */     }
/*  39 */     if (!(debug1 instanceof Vec3i)) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     Vec3i debug2 = (Vec3i)debug1;
/*     */     
/*  45 */     if (getX() != debug2.getX()) {
/*  46 */       return false;
/*     */     }
/*  48 */     if (getY() != debug2.getY()) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (getZ() != debug2.getZ()) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  60 */     return (getY() + getZ() * 31) * 31 + getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Vec3i debug1) {
/*  65 */     if (getY() == debug1.getY()) {
/*  66 */       if (getZ() == debug1.getZ()) {
/*  67 */         return getX() - debug1.getX();
/*     */       }
/*  69 */       return getZ() - debug1.getZ();
/*     */     } 
/*  71 */     return getY() - debug1.getY();
/*     */   }
/*     */   
/*     */   public int getX() {
/*  75 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  79 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  83 */     return this.z;
/*     */   }
/*     */   
/*     */   protected void setX(int debug1) {
/*  87 */     this.x = debug1;
/*     */   }
/*     */   
/*     */   protected void setY(int debug1) {
/*  91 */     this.y = debug1;
/*     */   }
/*     */   
/*     */   protected void setZ(int debug1) {
/*  95 */     this.z = debug1;
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
/*     */   public Vec3i above() {
/* 130 */     return above(1);
/*     */   }
/*     */   
/*     */   public Vec3i above(int debug1) {
/* 134 */     return relative(Direction.UP, debug1);
/*     */   }
/*     */   
/*     */   public Vec3i below() {
/* 138 */     return below(1);
/*     */   }
/*     */   
/*     */   public Vec3i below(int debug1) {
/* 142 */     return relative(Direction.DOWN, debug1);
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
/*     */   public Vec3i relative(Direction debug1, int debug2) {
/* 182 */     if (debug2 == 0) {
/* 183 */       return this;
/*     */     }
/* 185 */     return new Vec3i(getX() + debug1.getStepX() * debug2, getY() + debug1.getStepY() * debug2, getZ() + debug1.getStepZ() * debug2);
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
/*     */   public Vec3i cross(Vec3i debug1) {
/* 199 */     return new Vec3i(getY() * debug1.getZ() - getZ() * debug1.getY(), getZ() * debug1.getX() - getX() * debug1.getZ(), getX() * debug1.getY() - getY() * debug1.getX());
/*     */   }
/*     */   
/*     */   public boolean closerThan(Vec3i debug1, double debug2) {
/* 203 */     return (distSqr(debug1.getX(), debug1.getY(), debug1.getZ(), false) < debug2 * debug2);
/*     */   }
/*     */   
/*     */   public boolean closerThan(Position debug1, double debug2) {
/* 207 */     return (distSqr(debug1.x(), debug1.y(), debug1.z(), true) < debug2 * debug2);
/*     */   }
/*     */   
/*     */   public double distSqr(Vec3i debug1) {
/* 211 */     return distSqr(debug1.getX(), debug1.getY(), debug1.getZ(), true);
/*     */   }
/*     */   
/*     */   public double distSqr(Position debug1, boolean debug2) {
/* 215 */     return distSqr(debug1.x(), debug1.y(), debug1.z(), debug2);
/*     */   }
/*     */   
/*     */   public double distSqr(double debug1, double debug3, double debug5, boolean debug7) {
/* 219 */     double debug8 = debug7 ? 0.5D : 0.0D;
/* 220 */     double debug10 = getX() + debug8 - debug1;
/* 221 */     double debug12 = getY() + debug8 - debug3;
/* 222 */     double debug14 = getZ() + debug8 - debug5;
/* 223 */     return debug10 * debug10 + debug12 * debug12 + debug14 * debug14;
/*     */   }
/*     */   
/*     */   public int distManhattan(Vec3i debug1) {
/* 227 */     float debug2 = Math.abs(debug1.getX() - getX());
/* 228 */     float debug3 = Math.abs(debug1.getY() - getY());
/* 229 */     float debug4 = Math.abs(debug1.getZ() - getZ());
/* 230 */     return (int)(debug2 + debug3 + debug4);
/*     */   }
/*     */   
/*     */   public int get(Direction.Axis debug1) {
/* 234 */     return debug1.choose(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 239 */     return MoreObjects.toStringHelper(this)
/* 240 */       .add("x", getX())
/* 241 */       .add("y", getY())
/* 242 */       .add("z", getZ())
/* 243 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Vec3i.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */