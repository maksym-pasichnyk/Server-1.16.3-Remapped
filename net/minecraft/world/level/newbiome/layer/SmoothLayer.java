/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public enum SmoothLayer implements CastleTransformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 11 */     boolean debug7 = (debug3 == debug5);
/* 12 */     boolean debug8 = (debug2 == debug4);
/*    */     
/* 14 */     if (debug7 == debug8) {
/* 15 */       if (debug7) {
/* 16 */         return (debug1.nextRandom(2) == 0) ? debug5 : debug2;
/*    */       }
/* 18 */       return debug6;
/*    */     } 
/* 20 */     return debug7 ? debug5 : debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\SmoothLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */