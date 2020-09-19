/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public abstract class VoxelShape
/*     */ {
/*     */   protected final DiscreteVoxelShape shape;
/*     */   @Nullable
/*     */   private VoxelShape[] faces;
/*     */   
/*     */   VoxelShape(DiscreteVoxelShape debug1) {
/*  25 */     this.shape = debug1;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis debug1) {
/*  29 */     int debug2 = this.shape.firstFull(debug1);
/*  30 */     if (debug2 >= this.shape.getSize(debug1)) {
/*  31 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*  33 */     return get(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public double max(Direction.Axis debug1) {
/*  38 */     int debug2 = this.shape.lastFull(debug1);
/*  39 */     if (debug2 <= 0) {
/*  40 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*  42 */     return get(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public AABB bounds() {
/*  47 */     if (isEmpty()) {
/*  48 */       throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("No bounds for empty shape."));
/*     */     }
/*  50 */     return new AABB(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   protected double get(Direction.Axis debug1, int debug2) {
/*  54 */     return getCoords(debug1).getDouble(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  60 */     return this.shape.isEmpty();
/*     */   }
/*     */   
/*     */   public VoxelShape move(double debug1, double debug3, double debug5) {
/*  64 */     if (isEmpty()) {
/*  65 */       return Shapes.empty();
/*     */     }
/*  67 */     return new ArrayVoxelShape(this.shape, (DoubleList)new OffsetDoubleList(
/*     */           
/*  69 */           getCoords(Direction.Axis.X), debug1), (DoubleList)new OffsetDoubleList(
/*  70 */           getCoords(Direction.Axis.Y), debug3), (DoubleList)new OffsetDoubleList(
/*  71 */           getCoords(Direction.Axis.Z), debug5));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape optimize() {
/*  76 */     VoxelShape[] debug1 = { Shapes.empty() };
/*  77 */     forAllBoxes((debug1, debug3, debug5, debug7, debug9, debug11) -> debug0[0] = Shapes.joinUnoptimized(debug0[0], Shapes.box(debug1, debug3, debug5, debug7, debug9, debug11), BooleanOp.OR));
/*     */ 
/*     */     
/*  80 */     return debug1[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forAllBoxes(Shapes.DoubleLineConsumer debug1) {
/*  88 */     DoubleList debug2 = getCoords(Direction.Axis.X);
/*  89 */     DoubleList debug3 = getCoords(Direction.Axis.Y);
/*  90 */     DoubleList debug4 = getCoords(Direction.Axis.Z);
/*     */     
/*  92 */     this.shape.forAllBoxes((debug4, debug5, debug6, debug7, debug8, debug9) -> debug0.consume(debug1.getDouble(debug4), debug2.getDouble(debug5), debug3.getDouble(debug6), debug1.getDouble(debug7), debug2.getDouble(debug8), debug3.getDouble(debug9)), true);
/*     */   }
/*     */   
/*     */   public List<AABB> toAabbs() {
/*  96 */     List<AABB> debug1 = Lists.newArrayList();
/*  97 */     forAllBoxes((debug1, debug3, debug5, debug7, debug9, debug11) -> debug0.add(new AABB(debug1, debug3, debug5, debug7, debug9, debug11)));
/*  98 */     return debug1;
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
/*     */   protected int findIndex(Direction.Axis debug1, double debug2) {
/* 129 */     return Mth.binarySearch(0, this.shape.getSize(debug1) + 1, debug4 -> (debug4 < 0) ? false : ((debug4 > this.shape.getSize(debug1)) ? true : ((debug2 < get(debug1, debug4))))) - 1;
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
/*     */   protected boolean isFullWide(double debug1, double debug3, double debug5) {
/* 144 */     return this.shape.isFullWide(findIndex(Direction.Axis.X, debug1), findIndex(Direction.Axis.Y, debug3), findIndex(Direction.Axis.Z, debug5));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockHitResult clip(Vec3 debug1, Vec3 debug2, BlockPos debug3) {
/* 149 */     if (isEmpty()) {
/* 150 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 154 */     Vec3 debug4 = debug2.subtract(debug1);
/* 155 */     if (debug4.lengthSqr() < 1.0E-7D) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     Vec3 debug5 = debug1.add(debug4.scale(0.001D));
/*     */ 
/*     */     
/* 162 */     if (isFullWide(debug5.x - debug3.getX(), debug5.y - debug3.getY(), debug5.z - debug3.getZ())) {
/* 163 */       return new BlockHitResult(debug5, Direction.getNearest(debug4.x, debug4.y, debug4.z).getOpposite(), debug3, true);
/*     */     }
/*     */ 
/*     */     
/* 167 */     return AABB.clip(toAabbs(), debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getFaceShape(Direction debug1) {
/* 174 */     if (isEmpty() || this == Shapes.block()) {
/* 175 */       return this;
/*     */     }
/*     */     
/* 178 */     if (this.faces != null) {
/* 179 */       VoxelShape voxelShape = this.faces[debug1.ordinal()];
/* 180 */       if (voxelShape != null) {
/* 181 */         return voxelShape;
/*     */       }
/*     */     } else {
/* 184 */       this.faces = new VoxelShape[6];
/*     */     } 
/*     */     
/* 187 */     VoxelShape debug2 = calculateFace(debug1);
/* 188 */     this.faces[debug1.ordinal()] = debug2;
/* 189 */     return debug2;
/*     */   }
/*     */   
/*     */   private VoxelShape calculateFace(Direction debug1) {
/* 193 */     Direction.Axis debug2 = debug1.getAxis();
/* 194 */     Direction.AxisDirection debug3 = debug1.getAxisDirection();
/* 195 */     DoubleList debug4 = getCoords(debug2);
/* 196 */     if (debug4.size() == 2 && DoubleMath.fuzzyEquals(debug4.getDouble(0), 0.0D, 1.0E-7D) && DoubleMath.fuzzyEquals(debug4.getDouble(1), 1.0D, 1.0E-7D)) {
/* 197 */       return this;
/*     */     }
/* 199 */     int debug5 = findIndex(debug2, (debug3 == Direction.AxisDirection.POSITIVE) ? 0.9999999D : 1.0E-7D);
/* 200 */     return new SliceShape(this, debug2, debug5);
/*     */   }
/*     */   
/*     */   public double collide(Direction.Axis debug1, AABB debug2, double debug3) {
/* 204 */     return collideX(AxisCycle.between(debug1, Direction.Axis.X), debug2, debug3);
/*     */   }
/*     */   
/*     */   protected double collideX(AxisCycle debug1, AABB debug2, double debug3) {
/* 208 */     if (isEmpty()) {
/* 209 */       return debug3;
/*     */     }
/* 211 */     if (Math.abs(debug3) < 1.0E-7D) {
/* 212 */       return 0.0D;
/*     */     }
/*     */     
/* 215 */     AxisCycle debug5 = debug1.inverse();
/* 216 */     Direction.Axis debug6 = debug5.cycle(Direction.Axis.X);
/* 217 */     Direction.Axis debug7 = debug5.cycle(Direction.Axis.Y);
/* 218 */     Direction.Axis debug8 = debug5.cycle(Direction.Axis.Z);
/*     */     
/* 220 */     double debug9 = debug2.max(debug6);
/* 221 */     double debug11 = debug2.min(debug6);
/*     */     
/* 223 */     int debug13 = findIndex(debug6, debug11 + 1.0E-7D);
/* 224 */     int debug14 = findIndex(debug6, debug9 - 1.0E-7D);
/*     */     
/* 226 */     int debug15 = Math.max(0, findIndex(debug7, debug2.min(debug7) + 1.0E-7D));
/* 227 */     int debug16 = Math.min(this.shape.getSize(debug7), findIndex(debug7, debug2.max(debug7) - 1.0E-7D) + 1);
/*     */     
/* 229 */     int debug17 = Math.max(0, findIndex(debug8, debug2.min(debug8) + 1.0E-7D));
/* 230 */     int debug18 = Math.min(this.shape.getSize(debug8), findIndex(debug8, debug2.max(debug8) - 1.0E-7D) + 1);
/*     */     
/* 232 */     int debug19 = this.shape.getSize(debug6);
/*     */     
/* 234 */     if (debug3 > 0.0D) {
/* 235 */       for (int debug20 = debug14 + 1; debug20 < debug19; debug20++) {
/* 236 */         for (int debug21 = debug15; debug21 < debug16; debug21++) {
/* 237 */           for (int debug22 = debug17; debug22 < debug18; debug22++) {
/* 238 */             if (this.shape.isFullWide(debug5, debug20, debug21, debug22)) {
/* 239 */               double debug23 = get(debug6, debug20) - debug9;
/* 240 */               if (debug23 >= -1.0E-7D) {
/* 241 */                 debug3 = Math.min(debug3, debug23);
/*     */               }
/* 243 */               return debug3;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 248 */     } else if (debug3 < 0.0D) {
/* 249 */       for (int debug20 = debug13 - 1; debug20 >= 0; debug20--) {
/* 250 */         for (int debug21 = debug15; debug21 < debug16; debug21++) {
/* 251 */           for (int debug22 = debug17; debug22 < debug18; debug22++) {
/* 252 */             if (this.shape.isFullWide(debug5, debug20, debug21, debug22)) {
/* 253 */               double debug23 = get(debug6, debug20 + 1) - debug11;
/* 254 */               if (debug23 <= 1.0E-7D) {
/* 255 */                 debug3 = Math.max(debug3, debug23);
/*     */               }
/* 257 */               return debug3;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 263 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 268 */     return isEmpty() ? "EMPTY" : ("VoxelShape[" + bounds() + "]");
/*     */   }
/*     */   
/*     */   protected abstract DoubleList getCoords(Direction.Axis paramAxis);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\VoxelShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */