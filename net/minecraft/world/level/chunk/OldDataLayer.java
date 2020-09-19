/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ public class OldDataLayer {
/*    */   public final byte[] data;
/*    */   private final int depthBits;
/*    */   private final int depthBitsPlusFour;
/*    */   
/*    */   public OldDataLayer(byte[] debug1, int debug2) {
/*  9 */     this.data = debug1;
/* 10 */     this.depthBits = debug2;
/* 11 */     this.depthBitsPlusFour = debug2 + 4;
/*    */   }
/*    */   
/*    */   public int get(int debug1, int debug2, int debug3) {
/* 15 */     int debug4 = debug1 << this.depthBitsPlusFour | debug3 << this.depthBits | debug2;
/* 16 */     int debug5 = debug4 >> 1;
/* 17 */     int debug6 = debug4 & 0x1;
/*    */     
/* 19 */     if (debug6 == 0) {
/* 20 */       return this.data[debug5] & 0xF;
/*    */     }
/* 22 */     return this.data[debug5] >> 4 & 0xF;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\OldDataLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */