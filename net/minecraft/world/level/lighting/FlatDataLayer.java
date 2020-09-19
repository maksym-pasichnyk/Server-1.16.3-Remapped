/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public class FlatDataLayer
/*    */   extends DataLayer
/*    */ {
/*    */   public FlatDataLayer() {
/*  9 */     super(128);
/*    */   }
/*    */ 
/*    */   
/*    */   public FlatDataLayer(DataLayer debug1, int debug2) {
/* 14 */     super(128);
/* 15 */     System.arraycopy(debug1.getData(), debug2 * 128, this.data, 0, 128);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getIndex(int debug1, int debug2, int debug3) {
/* 20 */     return debug3 << 4 | debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getData() {
/* 25 */     byte[] debug1 = new byte[2048];
/* 26 */     for (int debug2 = 0; debug2 < 16; debug2++) {
/* 27 */       System.arraycopy(this.data, 0, debug1, debug2 * 128, 128);
/*    */     }
/* 29 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\FlatDataLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */