/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ColumnPos
/*    */ {
/*    */   public final int x;
/*    */   public final int z;
/*    */   
/*    */   public ColumnPos(int debug1, int debug2) {
/* 17 */     this.x = debug1;
/* 18 */     this.z = debug2;
/*    */   }
/*    */   
/*    */   public ColumnPos(BlockPos debug1) {
/* 22 */     this.x = debug1.getX();
/* 23 */     this.z = debug1.getZ();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     return "[" + this.x + ", " + this.z + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     int debug1 = 1664525 * this.x + 1013904223;
/* 46 */     int debug2 = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
/* 47 */     return debug1 ^ debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 52 */     if (this == debug1) {
/* 53 */       return true;
/*    */     }
/*    */     
/* 56 */     if (debug1 instanceof ColumnPos) {
/* 57 */       ColumnPos debug2 = (ColumnPos)debug1;
/* 58 */       return (this.x == debug2.x && this.z == debug2.z);
/*    */     } 
/*    */     
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ColumnPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */