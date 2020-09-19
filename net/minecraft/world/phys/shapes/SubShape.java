/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public final class SubShape extends DiscreteVoxelShape {
/*    */   private final DiscreteVoxelShape parent;
/*    */   private final int startX;
/*    */   private final int startY;
/*    */   private final int startZ;
/*    */   private final int endX;
/*    */   private final int endY;
/*    */   private final int endZ;
/*    */   
/*    */   protected SubShape(DiscreteVoxelShape debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7) {
/* 15 */     super(debug5 - debug2, debug6 - debug3, debug7 - debug4);
/* 16 */     this.parent = debug1;
/* 17 */     this.startX = debug2;
/* 18 */     this.startY = debug3;
/* 19 */     this.startZ = debug4;
/* 20 */     this.endX = debug5;
/* 21 */     this.endY = debug6;
/* 22 */     this.endZ = debug7;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull(int debug1, int debug2, int debug3) {
/* 27 */     return this.parent.isFull(this.startX + debug1, this.startY + debug2, this.startZ + debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFull(int debug1, int debug2, int debug3, boolean debug4, boolean debug5) {
/* 32 */     this.parent.setFull(this.startX + debug1, this.startY + debug2, this.startZ + debug3, debug4, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public int firstFull(Direction.Axis debug1) {
/* 37 */     return Math.max(0, this.parent.firstFull(debug1) - debug1.choose(this.startX, this.startY, this.startZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastFull(Direction.Axis debug1) {
/* 42 */     return Math.min(debug1.choose(this.endX, this.endY, this.endZ), this.parent.lastFull(debug1) - debug1.choose(this.startX, this.startY, this.startZ));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\phys\shapes\SubShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */