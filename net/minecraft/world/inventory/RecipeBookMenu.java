/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.StackedContents;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public abstract class RecipeBookMenu<C extends Container>
/*    */   extends AbstractContainerMenu {
/*    */   public RecipeBookMenu(MenuType<?> debug1, int debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePlacement(boolean debug1, Recipe<?> debug2, ServerPlayer debug3) {
/* 17 */     (new ServerPlaceRecipe(this)).recipeClicked(debug3, debug2, debug1);
/*    */   }
/*    */   
/*    */   public abstract void fillCraftSlotsStackedContents(StackedContents paramStackedContents);
/*    */   
/*    */   public abstract void clearCraftingContent();
/*    */   
/*    */   public abstract boolean recipeMatches(Recipe<? super C> paramRecipe);
/*    */   
/*    */   public abstract int getResultSlotIndex();
/*    */   
/*    */   public abstract int getGridWidth();
/*    */   
/*    */   public abstract int getGridHeight();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\RecipeBookMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */