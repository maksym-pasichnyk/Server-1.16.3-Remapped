/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class CubeVoxelShape extends VoxelShape {
/*    */   protected CubeVoxelShape(DiscreteVoxelShape debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected DoubleList getCoords(Direction.Axis debug1) {
/* 14 */     return (DoubleList)new CubePointRange(this.shape.getSize(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int findIndex(Direction.Axis debug1, double debug2) {
/* 19 */     int debug4 = this.shape.getSize(debug1);
/* 20 */     return Mth.clamp(Mth.floor(debug2 * debug4), -1, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\CubeVoxelShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */