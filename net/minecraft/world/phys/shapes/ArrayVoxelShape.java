/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public final class ArrayVoxelShape
/*    */   extends VoxelShape {
/*    */   private final DoubleList xs;
/*    */   private final DoubleList ys;
/*    */   private final DoubleList zs;
/*    */   
/*    */   protected ArrayVoxelShape(DiscreteVoxelShape debug1, double[] debug2, double[] debug3, double[] debug4) {
/* 16 */     this(debug1, 
/*    */         
/* 18 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(debug2, debug1.getXSize() + 1)), 
/* 19 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(debug3, debug1.getYSize() + 1)), 
/* 20 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(debug4, debug1.getZSize() + 1)));
/*    */   }
/*    */ 
/*    */   
/*    */   ArrayVoxelShape(DiscreteVoxelShape debug1, DoubleList debug2, DoubleList debug3, DoubleList debug4) {
/* 25 */     super(debug1);
/* 26 */     int debug5 = debug1.getXSize() + 1;
/* 27 */     int debug6 = debug1.getYSize() + 1;
/* 28 */     int debug7 = debug1.getZSize() + 1;
/* 29 */     if (debug5 != debug2.size() || debug6 != debug3.size() || debug7 != debug4.size()) {
/* 30 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape."));
/*    */     }
/* 32 */     this.xs = debug2;
/* 33 */     this.ys = debug3;
/* 34 */     this.zs = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   protected DoubleList getCoords(Direction.Axis debug1) {
/* 39 */     switch (debug1) {
/*    */       case X:
/* 41 */         return this.xs;
/*    */       case Y:
/* 43 */         return this.ys;
/*    */       case Z:
/* 45 */         return this.zs;
/*    */     } 
/* 47 */     throw new IllegalArgumentException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\ArrayVoxelShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */