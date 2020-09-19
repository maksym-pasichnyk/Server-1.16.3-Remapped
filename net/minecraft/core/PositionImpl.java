/*    */ package net.minecraft.core;
/*    */ 
/*    */ public class PositionImpl implements Position {
/*    */   protected final double x;
/*    */   protected final double y;
/*    */   protected final double z;
/*    */   
/*    */   public PositionImpl(double debug1, double debug3, double debug5) {
/*  9 */     this.x = debug1;
/* 10 */     this.y = debug3;
/* 11 */     this.z = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public double x() {
/* 16 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public double y() {
/* 21 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public double z() {
/* 26 */     return this.z;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\PositionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */