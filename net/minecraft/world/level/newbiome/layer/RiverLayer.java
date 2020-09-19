/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public enum RiverLayer implements CastleTransformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 11 */     int debug7 = riverFilter(debug6);
/* 12 */     if (debug7 == riverFilter(debug5) && debug7 == 
/* 13 */       riverFilter(debug2) && debug7 == 
/* 14 */       riverFilter(debug3) && debug7 == 
/* 15 */       riverFilter(debug4))
/*    */     {
/* 17 */       return -1;
/*    */     }
/* 19 */     return 7;
/*    */   }
/*    */   
/*    */   private static int riverFilter(int debug0) {
/* 23 */     if (debug0 >= 2) {
/* 24 */       return 2 + (debug0 & 0x1);
/*    */     }
/* 26 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\RiverLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */