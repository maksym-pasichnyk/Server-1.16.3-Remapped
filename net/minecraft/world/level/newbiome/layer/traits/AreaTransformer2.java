/*    */ package net.minecraft.world.level.newbiome.layer.traits;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.area.AreaFactory;
/*    */ import net.minecraft.world.level.newbiome.context.BigContext;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ 
/*    */ public interface AreaTransformer2 extends DimensionTransformer {
/*    */   default <R extends Area> AreaFactory<R> run(BigContext<R> debug1, AreaFactory<R> debug2, AreaFactory<R> debug3) {
/* 10 */     return () -> {
/*    */         Area area1 = debug1.make();
/*    */         Area area2 = debug2.make();
/*    */         return debug3.createResult((), area1, area2);
/*    */       };
/*    */   }
/*    */   
/*    */   int applyPixel(Context paramContext, Area paramArea1, Area paramArea2, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\AreaTransformer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */