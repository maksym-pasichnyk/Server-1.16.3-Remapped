/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class SliceShape extends VoxelShape {
/*    */   private final VoxelShape delegate;
/*    */   private final Direction.Axis axis;
/*  9 */   private static final DoubleList SLICE_COORDS = (DoubleList)new CubePointRange(1);
/*    */   
/*    */   public SliceShape(VoxelShape debug1, Direction.Axis debug2, int debug3) {
/* 12 */     super(makeSlice(debug1.shape, debug2, debug3));
/* 13 */     this.delegate = debug1;
/* 14 */     this.axis = debug2;
/*    */   }
/*    */   
/*    */   private static DiscreteVoxelShape makeSlice(DiscreteVoxelShape debug0, Direction.Axis debug1, int debug2) {
/* 18 */     return new SubShape(debug0, debug1
/* 19 */         .choose(debug2, 0, 0), debug1
/* 20 */         .choose(0, debug2, 0), debug1
/* 21 */         .choose(0, 0, debug2), debug1
/* 22 */         .choose(debug2 + 1, debug0.xSize, debug0.xSize), debug1
/* 23 */         .choose(debug0.ySize, debug2 + 1, debug0.ySize), debug1
/* 24 */         .choose(debug0.zSize, debug0.zSize, debug2 + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected DoubleList getCoords(Direction.Axis debug1) {
/* 30 */     if (debug1 == this.axis) {
/* 31 */       return SLICE_COORDS;
/*    */     }
/* 33 */     return this.delegate.getCoords(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\SliceShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */