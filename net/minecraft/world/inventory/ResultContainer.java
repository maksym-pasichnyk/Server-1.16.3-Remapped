/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class ResultContainer implements Container, RecipeHolder {
/* 13 */   private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
/*    */   
/*    */   @Nullable
/*    */   private Recipe<?> recipeUsed;
/*    */   
/*    */   public int getContainerSize() {
/* 19 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 24 */     for (ItemStack debug2 : this.itemStacks) {
/* 25 */       if (!debug2.isEmpty()) {
/* 26 */         return false;
/*    */       }
/*    */     } 
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getItem(int debug1) {
/* 34 */     return (ItemStack)this.itemStacks.get(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItem(int debug1, int debug2) {
/* 39 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack removeItemNoUpdate(int debug1) {
/* 44 */     return ContainerHelper.takeItem((List)this.itemStacks, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setItem(int debug1, ItemStack debug2) {
/* 49 */     this.itemStacks.set(0, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {}
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearContent() {
/* 63 */     this.itemStacks.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRecipeUsed(@Nullable Recipe<?> debug1) {
/* 68 */     this.recipeUsed = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Recipe<?> getRecipeUsed() {
/* 74 */     return this.recipeUsed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ResultContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */