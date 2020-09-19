/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class BlastingRecipe
/*    */   extends AbstractCookingRecipe {
/*    */   public BlastingRecipe(ResourceLocation debug1, String debug2, Ingredient debug3, ItemStack debug4, float debug5, int debug6) {
/*  9 */     super(RecipeType.BLASTING, debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 19 */     return RecipeSerializer.BLASTING_RECIPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\BlastingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */