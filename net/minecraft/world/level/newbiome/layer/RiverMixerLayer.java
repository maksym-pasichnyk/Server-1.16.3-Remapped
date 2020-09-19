/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;
/*    */ 
/*    */ public enum RiverMixerLayer implements AreaTransformer2, DimensionOffset0Transformer {
/*  9 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int applyPixel(Context debug1, Area debug2, Area debug3, int debug4, int debug5) {
/* 13 */     int debug6 = debug2.get(getParentX(debug4), getParentY(debug5));
/* 14 */     int debug7 = debug3.get(getParentX(debug4), getParentY(debug5));
/*    */     
/* 16 */     if (Layers.isOcean(debug6)) {
/* 17 */       return debug6;
/*    */     }
/* 19 */     if (debug7 == 7) {
/* 20 */       if (debug6 == 12) {
/* 21 */         return 11;
/*    */       }
/* 23 */       if (debug6 == 14 || debug6 == 15) {
/* 24 */         return 15;
/*    */       }
/* 26 */       return debug7 & 0xFF;
/*    */     } 
/* 28 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\RiverMixerLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */