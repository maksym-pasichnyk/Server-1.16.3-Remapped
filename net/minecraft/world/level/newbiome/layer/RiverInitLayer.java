/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;
/*    */ 
/*    */ public enum RiverInitLayer implements C0Transformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2) {
/* 11 */     return Layers.isShallowOcean(debug2) ? debug2 : (debug1.nextRandom(299999) + 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\RiverInitLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */