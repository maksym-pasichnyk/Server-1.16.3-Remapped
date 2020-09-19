/*    */ package net.minecraft.world.level.newbiome.layer.traits;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.area.AreaFactory;
/*    */ import net.minecraft.world.level.newbiome.context.BigContext;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ 
/*    */ public interface AreaTransformer0 {
/*    */   default <R extends Area> AreaFactory<R> run(BigContext<R> debug1) {
/* 10 */     return () -> debug1.createResult(());
/*    */   }
/*    */   
/*    */   int applyPixel(Context paramContext, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\AreaTransformer0.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */