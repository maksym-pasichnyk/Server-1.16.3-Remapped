/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class StonecutterRecipe
/*    */   extends SingleItemRecipe {
/*    */   public StonecutterRecipe(ResourceLocation debug1, String debug2, Ingredient debug3, ItemStack debug4) {
/* 11 */     super(RecipeType.STONECUTTING, RecipeSerializer.STONECUTTER, debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Container debug1, Level debug2) {
/* 16 */     return this.ingredient.test(debug1.getItem(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\StonecutterRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */