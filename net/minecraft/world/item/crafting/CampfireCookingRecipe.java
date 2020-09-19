/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CampfireCookingRecipe
/*    */   extends AbstractCookingRecipe {
/*    */   public CampfireCookingRecipe(ResourceLocation debug1, String debug2, Ingredient debug3, ItemStack debug4, float debug5, int debug6) {
/*  9 */     super(RecipeType.CAMPFIRE_COOKING, debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 19 */     return RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\CampfireCookingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */