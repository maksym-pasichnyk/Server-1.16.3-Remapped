/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class SmeltingRecipe
/*    */   extends AbstractCookingRecipe {
/*    */   public SmeltingRecipe(ResourceLocation debug1, String debug2, Ingredient debug3, ItemStack debug4, float debug5, int debug6) {
/*  9 */     super(RecipeType.SMELTING, debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 19 */     return RecipeSerializer.SMELTING_RECIPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\SmeltingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */