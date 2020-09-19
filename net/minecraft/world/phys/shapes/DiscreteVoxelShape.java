/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public abstract class DiscreteVoxelShape {
/*   7 */   private static final Direction.Axis[] AXIS_VALUES = Direction.Axis.values();
/*     */   
/*     */   protected final int xSize;
/*     */   protected final int ySize;
/*     */   protected final int zSize;
/*     */   
/*     */   protected DiscreteVoxelShape(int debug1, int debug2, int debug3) {
/*  14 */     this.xSize = debug1;
/*  15 */     this.ySize = debug2;
/*  16 */     this.zSize = debug3;
/*     */   }
/*     */   
/*     */   public boolean isFullWide(AxisCycle debug1, int debug2, int debug3, int debug4) {
/*  20 */     return isFullWide(debug1
/*  21 */         .cycle(debug2, debug3, debug4, Direction.Axis.X), debug1
/*  22 */         .cycle(debug2, debug3, debug4, Direction.Axis.Y), debug1
/*  23 */         .cycle(debug2, debug3, debug4, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullWide(int debug1, int debug2, int debug3) {
/*  28 */     if (debug1 < 0 || debug2 < 0 || debug3 < 0) {
/*  29 */       return false;
/*     */     }
/*  31 */     if (debug1 >= this.xSize || debug2 >= this.ySize || debug3 >= this.zSize) {
/*  32 */       return false;
/*     */     }
/*  34 */     return isFull(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public boolean isFull(AxisCycle debug1, int debug2, int debug3, int debug4) {
/*  38 */     return isFull(debug1
/*  39 */         .cycle(debug2, debug3, debug4, Direction.Axis.X), debug1
/*  40 */         .cycle(debug2, debug3, debug4, Direction.Axis.Y), debug1
/*  41 */         .cycle(debug2, debug3, debug4, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isFull(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract void setFull(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public boolean isEmpty() {
/*  50 */     for (Direction.Axis debug4 : AXIS_VALUES) {
/*  51 */       if (firstFull(debug4) >= lastFull(debug4)) {
/*  52 */         return true;
/*     */       }
/*     */     } 
/*  55 */     return false;
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
/*     */   public abstract int firstFull(Direction.Axis paramAxis);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int lastFull(Direction.Axis paramAxis);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize(Direction.Axis debug1) {
/* 102 */     return debug1.choose(this.xSize, this.ySize, this.zSize);
/*     */   }
/*     */   
/*     */   public int getXSize() {
/* 106 */     return getSize(Direction.Axis.X);
/*     */   }
/*     */   
/*     */   public int getYSize() {
/* 110 */     return getSize(Direction.Axis.Y);
/*     */   }
/*     */   
/*     */   public int getZSize() {
/* 114 */     return getSize(Direction.Axis.Z);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isZStripFull(int debug1, int debug2, int debug3, int debug4) {
/* 195 */     for (int debug5 = debug1; debug5 < debug2; debug5++) {
/* 196 */       if (!isFullWide(debug3, debug4, debug5)) {
/* 197 */         return false;
/*     */       }
/*     */     } 
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   protected void setZStrip(int debug1, int debug2, int debug3, int debug4, boolean debug5) {
/* 204 */     for (int debug6 = debug1; debug6 < debug2; debug6++) {
/* 205 */       setFull(debug3, debug4, debug6, false, debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isXZRectangleFull(int debug1, int debug2, int debug3, int debug4, int debug5) {
/* 210 */     for (int debug6 = debug1; debug6 < debug2; debug6++) {
/* 211 */       if (!isZStripFull(debug3, debug4, debug6, debug5)) {
/* 212 */         return false;
/*     */       }
/*     */     } 
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forAllBoxes(IntLineConsumer debug1, boolean debug2) {
/* 223 */     DiscreteVoxelShape debug3 = new BitSetDiscreteVoxelShape(this);
/* 224 */     for (int debug4 = 0; debug4 <= this.xSize; debug4++) {
/* 225 */       for (int debug5 = 0; debug5 <= this.ySize; debug5++) {
/* 226 */         int debug6 = -1;
/* 227 */         for (int debug7 = 0; debug7 <= this.zSize; debug7++) {
/* 228 */           if (debug3.isFullWide(debug4, debug5, debug7)) {
/* 229 */             if (debug2) {
/*     */               
/* 231 */               if (debug6 == -1) {
/* 232 */                 debug6 = debug7;
/*     */               }
/*     */             } else {
/* 235 */               debug1.consume(debug4, debug5, debug7, debug4 + 1, debug5 + 1, debug7 + 1);
/*     */             } 
/* 237 */           } else if (debug6 != -1) {
/*     */ 
/*     */             
/* 240 */             int debug8 = debug4;
/* 241 */             int debug9 = debug4;
/* 242 */             int debug10 = debug5;
/* 243 */             int debug11 = debug5;
/*     */ 
/*     */             
/* 246 */             debug3.setZStrip(debug6, debug7, debug4, debug5, false);
/*     */ 
/*     */             
/* 249 */             while (debug3.isZStripFull(debug6, debug7, debug8 - 1, debug10)) {
/* 250 */               debug3.setZStrip(debug6, debug7, debug8 - 1, debug10, false);
/* 251 */               debug8--;
/*     */             } 
/* 253 */             while (debug3.isZStripFull(debug6, debug7, debug9 + 1, debug10)) {
/* 254 */               debug3.setZStrip(debug6, debug7, debug9 + 1, debug10, false);
/* 255 */               debug9++;
/*     */             } 
/*     */ 
/*     */             
/* 259 */             while (debug3.isXZRectangleFull(debug8, debug9 + 1, debug6, debug7, debug10 - 1)) {
/* 260 */               for (int debug12 = debug8; debug12 <= debug9; debug12++) {
/* 261 */                 debug3.setZStrip(debug6, debug7, debug12, debug10 - 1, false);
/*     */               }
/* 263 */               debug10--;
/*     */             } 
/* 265 */             while (debug3.isXZRectangleFull(debug8, debug9 + 1, debug6, debug7, debug11 + 1)) {
/* 266 */               for (int debug12 = debug8; debug12 <= debug9; debug12++) {
/* 267 */                 debug3.setZStrip(debug6, debug7, debug12, debug11 + 1, false);
/*     */               }
/* 269 */               debug11++;
/*     */             } 
/*     */             
/* 272 */             debug1.consume(debug8, debug10, debug6, debug9 + 1, debug11 + 1, debug7);
/* 273 */             debug6 = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void forAllFaces(IntFaceConsumer debug1) {
/* 282 */     forAllAxisFaces(debug1, AxisCycle.NONE);
/* 283 */     forAllAxisFaces(debug1, AxisCycle.FORWARD);
/* 284 */     forAllAxisFaces(debug1, AxisCycle.BACKWARD);
/*     */   }
/*     */   
/*     */   private void forAllAxisFaces(IntFaceConsumer debug1, AxisCycle debug2) {
/* 288 */     AxisCycle debug3 = debug2.inverse();
/*     */     
/* 290 */     Direction.Axis debug4 = debug3.cycle(Direction.Axis.Z);
/*     */     
/* 292 */     int debug5 = getSize(debug3.cycle(Direction.Axis.X));
/* 293 */     int debug6 = getSize(debug3.cycle(Direction.Axis.Y));
/* 294 */     int debug7 = getSize(debug4);
/*     */     
/* 296 */     Direction debug8 = Direction.fromAxisAndDirection(debug4, Direction.AxisDirection.NEGATIVE);
/* 297 */     Direction debug9 = Direction.fromAxisAndDirection(debug4, Direction.AxisDirection.POSITIVE);
/*     */     
/* 299 */     for (int debug10 = 0; debug10 < debug5; debug10++) {
/* 300 */       for (int debug11 = 0; debug11 < debug6; debug11++) {
/* 301 */         boolean debug12 = false;
/* 302 */         for (int debug13 = 0; debug13 <= debug7; debug13++) {
/* 303 */           boolean debug14 = (debug13 != debug7 && isFull(debug3, debug10, debug11, debug13));
/* 304 */           if (!debug12 && debug14) {
/* 305 */             debug1.consume(debug8, debug3
/*     */                 
/* 307 */                 .cycle(debug10, debug11, debug13, Direction.Axis.X), debug3
/* 308 */                 .cycle(debug10, debug11, debug13, Direction.Axis.Y), debug3
/* 309 */                 .cycle(debug10, debug11, debug13, Direction.Axis.Z));
/*     */           }
/*     */           
/* 312 */           if (debug12 && !debug14) {
/* 313 */             debug1.consume(debug9, debug3
/*     */                 
/* 315 */                 .cycle(debug10, debug11, debug13 - 1, Direction.Axis.X), debug3
/* 316 */                 .cycle(debug10, debug11, debug13 - 1, Direction.Axis.Y), debug3
/* 317 */                 .cycle(debug10, debug11, debug13 - 1, Direction.Axis.Z));
/*     */           }
/*     */           
/* 320 */           debug12 = debug14;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface IntFaceConsumer {
/*     */     void consume(Direction param1Direction, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */   
/*     */   public static interface IntLineConsumer {
/*     */     void consume(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\DiscreteVoxelShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */