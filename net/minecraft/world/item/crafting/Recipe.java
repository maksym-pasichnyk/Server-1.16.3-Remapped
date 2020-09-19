/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Recipe<C extends net.minecraft.world.Container>
/*    */ {
/*    */   boolean matches(C paramC, Level paramLevel);
/*    */   
/*    */   ItemStack assemble(C paramC);
/*    */   
/*    */   ItemStack getResultItem();
/*    */   
/*    */   default NonNullList<ItemStack> getRemainingItems(C debug1) {
/* 22 */     NonNullList<ItemStack> debug2 = NonNullList.withSize(debug1.getContainerSize(), ItemStack.EMPTY);
/*    */     
/* 24 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 25 */       Item debug4 = debug1.getItem(debug3).getItem();
/* 26 */       if (debug4.hasCraftingRemainingItem()) {
/* 27 */         debug2.set(debug3, new ItemStack((ItemLike)debug4.getCraftingRemainingItem()));
/*    */       }
/*    */     } 
/*    */     
/* 31 */     return debug2;
/*    */   }
/*    */   
/*    */   default NonNullList<Ingredient> getIngredients() {
/* 35 */     return NonNullList.create();
/*    */   }
/*    */   
/*    */   default boolean isSpecial() {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   ResourceLocation getId();
/*    */   
/*    */   RecipeSerializer<?> getSerializer();
/*    */   
/*    */   RecipeType<?> getType();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\Recipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */