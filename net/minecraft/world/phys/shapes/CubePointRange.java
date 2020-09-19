/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ 
/*    */ public class CubePointRange extends AbstractDoubleList {
/*    */   private final int parts;
/*    */   
/*    */   CubePointRange(int debug1) {
/*  9 */     this.parts = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int debug1) {
/* 14 */     return debug1 / this.parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 19 */     return this.parts + 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\CubePointRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */