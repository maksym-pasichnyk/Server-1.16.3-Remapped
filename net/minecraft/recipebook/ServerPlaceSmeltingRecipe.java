/*    */ package net.minecraft.recipebook;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.StackedContents;
/*    */ import net.minecraft.world.inventory.RecipeBookMenu;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class ServerPlaceSmeltingRecipe<C extends Container>
/*    */   extends ServerPlaceRecipe<C>
/*    */ {
/*    */   private boolean recipeMatchesPlaced;
/*    */   
/*    */   public ServerPlaceSmeltingRecipe(RecipeBookMenu<C> debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleRecipeClicked(Recipe<C> debug1, boolean debug2) {
/* 24 */     this.recipeMatchesPlaced = this.menu.recipeMatches(debug1);
/* 25 */     int debug3 = this.stackedContents.getBiggestCraftableStack(debug1, null);
/*    */ 
/*    */     
/* 28 */     if (this.recipeMatchesPlaced) {
/* 29 */       ItemStack itemStack = this.menu.getSlot(0).getItem();
/* 30 */       if (itemStack.isEmpty() || debug3 <= itemStack.getCount()) {
/*    */         return;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 36 */     int debug4 = getStackSize(debug2, debug3, this.recipeMatchesPlaced);
/*    */ 
/*    */     
/* 39 */     IntArrayList intArrayList = new IntArrayList();
/* 40 */     if (!this.stackedContents.canCraft(debug1, (IntList)intArrayList, debug4)) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     if (!this.recipeMatchesPlaced) {
/* 45 */       moveItemToInventory(this.menu.getResultSlotIndex());
/* 46 */       moveItemToInventory(0);
/*    */     } 
/* 48 */     placeRecipe(debug4, (IntList)intArrayList);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void clearGrid() {
/* 53 */     moveItemToInventory(this.menu.getResultSlotIndex());
/* 54 */     super.clearGrid();
/*    */   }
/*    */   
/*    */   protected void placeRecipe(int debug1, IntList debug2) {
/* 58 */     IntListIterator<Integer> intListIterator = debug2.iterator();
/*    */     
/* 60 */     Slot debug4 = this.menu.getSlot(0);
/* 61 */     ItemStack debug5 = StackedContents.fromStackingIndex(((Integer)intListIterator.next()).intValue());
/* 62 */     if (debug5.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 66 */     int debug6 = Math.min(debug5.getMaxStackSize(), debug1);
/* 67 */     if (this.recipeMatchesPlaced) {
/* 68 */       debug6 -= debug4.getItem().getCount();
/*    */     }
/* 70 */     for (int debug7 = 0; debug7 < debug6; debug7++)
/* 71 */       moveItemToGrid(debug4, debug5); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\recipebook\ServerPlaceSmeltingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */