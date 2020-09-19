/*    */ package net.minecraft.world.level.newbiome.layer.traits;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.context.BigContext;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ 
/*    */ public interface BishopTransformer
/*    */   extends AreaTransformer1, DimensionOffset1Transformer {
/*    */   int apply(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*    */   
/*    */   default int applyPixel(BigContext<?> debug1, Area debug2, int debug3, int debug4) {
/* 12 */     return apply((Context)debug1, debug2
/*    */         
/* 14 */         .get(getParentX(debug3 + 0), getParentY(debug4 + 2)), debug2
/* 15 */         .get(getParentX(debug3 + 2), getParentY(debug4 + 2)), debug2
/* 16 */         .get(getParentX(debug3 + 2), getParentY(debug4 + 0)), debug2
/* 17 */         .get(getParentX(debug3 + 0), getParentY(debug4 + 0)), debug2
/* 18 */         .get(getParentX(debug3 + 1), getParentY(debug4 + 1)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\BishopTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */