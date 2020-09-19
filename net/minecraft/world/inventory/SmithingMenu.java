/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.item.crafting.UpgradeRecipe;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SmithingMenu
/*    */   extends ItemCombinerMenu
/*    */ {
/*    */   private final Level level;
/*    */   @Nullable
/*    */   private UpgradeRecipe selectedRecipe;
/*    */   private final List<UpgradeRecipe> recipes;
/*    */   
/*    */   public SmithingMenu(int debug1, Inventory debug2) {
/* 25 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*    */   }
/*    */   
/*    */   public SmithingMenu(int debug1, Inventory debug2, ContainerLevelAccess debug3) {
/* 29 */     super(MenuType.SMITHING, debug1, debug2, debug3);
/* 30 */     this.level = debug2.player.level;
/* 31 */     this.recipes = this.level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidBlock(BlockState debug1) {
/* 36 */     return debug1.is(Blocks.SMITHING_TABLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPickup(Player debug1, boolean debug2) {
/* 41 */     return (this.selectedRecipe != null && this.selectedRecipe.matches(this.inputSlots, this.level));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack onTake(Player debug1, ItemStack debug2) {
/* 46 */     debug2.onCraftedBy(debug1.level, debug1, debug2.getCount());
/* 47 */     this.resultSlots.awardUsedRecipes(debug1);
/*    */ 
/*    */     
/* 50 */     shrinkStackInSlot(0);
/* 51 */     shrinkStackInSlot(1);
/*    */     
/* 53 */     this.access.execute((debug0, debug1) -> debug0.levelEvent(1044, debug1, 0));
/* 54 */     return debug2;
/*    */   }
/*    */   
/*    */   private void shrinkStackInSlot(int debug1) {
/* 58 */     ItemStack debug2 = this.inputSlots.getItem(debug1);
/* 59 */     debug2.shrink(1);
/* 60 */     this.inputSlots.setItem(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void createResult() {
/* 65 */     List<UpgradeRecipe> debug1 = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.inputSlots, this.level);
/* 66 */     if (debug1.isEmpty()) {
/* 67 */       this.resultSlots.setItem(0, ItemStack.EMPTY);
/*    */     } else {
/* 69 */       this.selectedRecipe = debug1.get(0);
/* 70 */       ItemStack debug2 = this.selectedRecipe.assemble(this.inputSlots);
/* 71 */       this.resultSlots.setRecipeUsed((Recipe<?>)this.selectedRecipe);
/* 72 */       this.resultSlots.setItem(0, debug2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldQuickMoveToAdditionalSlot(ItemStack debug1) {
/* 78 */     return this.recipes.stream().anyMatch(debug1 -> debug1.isAdditionIngredient(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 83 */     return (debug2.container != this.resultSlots && super.canTakeItemForPickAll(debug1, debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\SmithingMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */