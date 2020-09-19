/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;
/*    */ 
/*    */ public enum IslandLayer implements AreaTransformer0 {
/*  7 */   INSTANCE;
/*    */ 
/*    */ 
/*    */   
/*    */   public int applyPixel(Context debug1, int debug2, int debug3) {
/* 12 */     if (debug2 == 0 && debug3 == 0) {
/* 13 */       return 1;
/*    */     }
/*    */     
/* 16 */     return (debug1.nextRandom(10) == 0) ? 1 : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\IslandLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */