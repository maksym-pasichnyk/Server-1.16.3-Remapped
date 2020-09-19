/*    */ package net.minecraft.world.level.newbiome.layer.traits;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.context.BigContext;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ 
/*    */ public interface C1Transformer
/*    */   extends AreaTransformer1, DimensionOffset1Transformer {
/*    */   int apply(Context paramContext, int paramInt);
/*    */   
/*    */   default int applyPixel(BigContext<?> debug1, Area debug2, int debug3, int debug4) {
/* 12 */     int debug5 = debug2.get(getParentX(debug3 + 1), getParentY(debug4 + 1));
/* 13 */     return apply((Context)debug1, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\C1Transformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */