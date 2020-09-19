/*   */ package net.minecraft.world.level.newbiome.layer.traits;
/*   */ 
/*   */ import net.minecraft.world.level.newbiome.area.Area;
/*   */ import net.minecraft.world.level.newbiome.area.AreaFactory;
/*   */ import net.minecraft.world.level.newbiome.context.BigContext;
/*   */ 
/*   */ public interface AreaTransformer1 extends DimensionTransformer {
/*   */   default <R extends Area> AreaFactory<R> run(BigContext<R> debug1, AreaFactory<R> debug2) {
/* 9 */     return () -> {
/*   */         Area area = debug1.make();
/*   */         return debug2.createResult((), area);
/*   */       };
/*   */   }
/*   */   
/*   */   int applyPixel(BigContext<?> paramBigContext, Area paramArea, int paramInt1, int paramInt2);
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\AreaTransformer1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */