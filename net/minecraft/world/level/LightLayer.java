/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ public enum LightLayer {
/*  4 */   SKY(15),
/*  5 */   BLOCK(0);
/*    */   
/*    */   public final int surrounding;
/*    */   
/*    */   LightLayer(int debug3) {
/* 10 */     this.surrounding = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LightLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */