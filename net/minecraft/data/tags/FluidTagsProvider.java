/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class FluidTagsProvider
/*    */   extends TagsProvider<Fluid> {
/*    */   public FluidTagsProvider(DataGenerator debug1) {
/* 14 */     super(debug1, (Registry<Fluid>)Registry.FLUID);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addTags() {
/* 19 */     tag(FluidTags.WATER).add(new Fluid[] { (Fluid)Fluids.WATER, (Fluid)Fluids.FLOWING_WATER });
/* 20 */     tag(FluidTags.LAVA).add(new Fluid[] { (Fluid)Fluids.LAVA, (Fluid)Fluids.FLOWING_LAVA });
/*    */   }
/*    */ 
/*    */   
/*    */   protected Path getPath(ResourceLocation debug1) {
/* 25 */     return this.generator.getOutputFolder().resolve("data/" + debug1.getNamespace() + "/tags/fluids/" + debug1.getPath() + ".json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 30 */     return "Fluid Tags";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\tags\FluidTagsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */