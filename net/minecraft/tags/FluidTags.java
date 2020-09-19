/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public final class FluidTags
/*    */ {
/*  9 */   protected static final StaticTagHelper<Fluid> HELPER = StaticTags.create(new ResourceLocation("fluid"), TagContainer::getFluids);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Tag.Named<Fluid> WATER = bind("water");
/* 15 */   public static final Tag.Named<Fluid> LAVA = bind("lava");
/*    */   
/*    */   private static Tag.Named<Fluid> bind(String debug0) {
/* 18 */     return HELPER.bind(debug0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<? extends Tag.Named<Fluid>> getWrappers() {
/* 26 */     return HELPER.getWrappers();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\FluidTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */