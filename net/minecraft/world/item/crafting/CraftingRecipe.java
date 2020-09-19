/*   */ package net.minecraft.world.item.crafting;
/*   */ 
/*   */ import net.minecraft.world.inventory.CraftingContainer;
/*   */ 
/*   */ public interface CraftingRecipe
/*   */   extends Recipe<CraftingContainer> {
/*   */   default RecipeType<?> getType() {
/* 8 */     return RecipeType.CRAFTING;
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\CraftingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */