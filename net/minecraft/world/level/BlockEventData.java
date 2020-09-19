/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BlockEventData {
/*    */   private final BlockPos pos;
/*    */   private final Block block;
/*    */   private final int paramA;
/*    */   private final int paramB;
/*    */   
/*    */   public BlockEventData(BlockPos debug1, Block debug2, int debug3, int debug4) {
/* 13 */     this.pos = debug1;
/* 14 */     this.block = debug2;
/* 15 */     this.paramA = debug3;
/* 16 */     this.paramB = debug4;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 20 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Block getBlock() {
/* 24 */     return this.block;
/*    */   }
/*    */   
/*    */   public int getParamA() {
/* 28 */     return this.paramA;
/*    */   }
/*    */   
/*    */   public int getParamB() {
/* 32 */     return this.paramB;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 37 */     if (debug1 instanceof BlockEventData) {
/* 38 */       BlockEventData debug2 = (BlockEventData)debug1;
/* 39 */       return (this.pos.equals(debug2.pos) && this.paramA == debug2.paramA && this.paramB == debug2.paramB && this.block == debug2.block);
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 46 */     int debug1 = this.pos.hashCode();
/* 47 */     debug1 = 31 * debug1 + this.block.hashCode();
/* 48 */     debug1 = 31 * debug1 + this.paramA;
/* 49 */     debug1 = 31 * debug1 + this.paramB;
/* 50 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "TE(" + this.pos + ")," + this.paramA + "," + this.paramB + "," + this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\BlockEventData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */