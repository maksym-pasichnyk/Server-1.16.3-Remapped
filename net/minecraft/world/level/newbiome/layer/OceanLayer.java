/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;
/*    */ 
/*    */ public enum OceanLayer implements AreaTransformer0 {
/*  8 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int applyPixel(Context debug1, int debug2, int debug3) {
/* 12 */     ImprovedNoise debug4 = debug1.getBiomeNoise();
/* 13 */     double debug5 = debug4.noise(debug2 / 8.0D, debug3 / 8.0D, 0.0D, 0.0D, 0.0D);
/*    */     
/* 15 */     if (debug5 > 0.4D) {
/* 16 */       return 44;
/*    */     }
/*    */     
/* 19 */     if (debug5 > 0.2D) {
/* 20 */       return 45;
/*    */     }
/*    */     
/* 23 */     if (debug5 < -0.4D) {
/* 24 */       return 10;
/*    */     }
/*    */     
/* 27 */     if (debug5 < -0.2D) {
/* 28 */       return 46;
/*    */     }
/*    */     
/* 31 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\OceanLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */