/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Cursor3D
/*    */ {
/*    */   private int originX;
/*    */   private int originY;
/*    */   private int originZ;
/*    */   private int width;
/*    */   private int height;
/*    */   private int depth;
/*    */   private int end;
/*    */   private int index;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public Cursor3D(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 24 */     this.originX = debug1;
/* 25 */     this.originY = debug2;
/* 26 */     this.originZ = debug3;
/*    */     
/* 28 */     this.width = debug4 - debug1 + 1;
/* 29 */     this.height = debug5 - debug2 + 1;
/* 30 */     this.depth = debug6 - debug3 + 1;
/* 31 */     this.end = this.width * this.height * this.depth;
/*    */   }
/*    */   
/*    */   public boolean advance() {
/* 35 */     if (this.index == this.end) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     this.x = this.index % this.width;
/* 40 */     int debug1 = this.index / this.width;
/* 41 */     this.y = debug1 % this.height;
/* 42 */     this.z = debug1 / this.height;
/*    */     
/* 44 */     this.index++;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public int nextX() {
/* 49 */     return this.originX + this.x;
/*    */   }
/*    */   
/*    */   public int nextY() {
/* 53 */     return this.originY + this.y;
/*    */   }
/*    */   
/*    */   public int nextZ() {
/* 57 */     return this.originZ + this.z;
/*    */   }
/*    */   
/*    */   public int getNextType() {
/* 61 */     int debug1 = 0;
/* 62 */     if (this.x == 0 || this.x == this.width - 1) {
/* 63 */       debug1++;
/*    */     }
/* 65 */     if (this.y == 0 || this.y == this.height - 1) {
/* 66 */       debug1++;
/*    */     }
/* 68 */     if (this.z == 0 || this.z == this.depth - 1) {
/* 69 */       debug1++;
/*    */     }
/* 71 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Cursor3D.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */