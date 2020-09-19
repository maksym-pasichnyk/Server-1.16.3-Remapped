/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ 
/*    */ public class ResultSlot extends Slot {
/*    */   private final CraftingContainer craftSlots;
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   
/*    */   public ResultSlot(Player debug1, CraftingContainer debug2, Container debug3, int debug4, int debug5, int debug6) {
/* 15 */     super(debug3, debug4, debug5, debug6);
/* 16 */     this.player = debug1;
/* 17 */     this.craftSlots = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack debug1) {
/* 22 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int debug1) {
/* 27 */     if (hasItem()) {
/* 28 */       this.removeCount += Math.min(debug1, getItem().getCount());
/*    */     }
/* 30 */     return super.remove(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack debug1, int debug2) {
/* 35 */     this.removeCount += debug2;
/* 36 */     checkTakeAchievements(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onSwapCraft(int debug1) {
/* 41 */     this.removeCount += debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack debug1) {
/* 46 */     if (this.removeCount > 0) {
/* 47 */       debug1.onCraftedBy(this.player.level, this.player, this.removeCount);
/*    */     }
/* 49 */     if (this.container instanceof RecipeHolder) {
/* 50 */       ((RecipeHolder)this.container).awardUsedRecipes(this.player);
/*    */     }
/* 52 */     this.removeCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onTake(Player debug1, ItemStack debug2) {
/* 57 */     checkTakeAchievements(debug2);
/*    */     
/* 59 */     NonNullList<ItemStack> debug3 = debug1.level.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, debug1.level);
/*    */     
/* 61 */     for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 62 */       ItemStack debug5 = this.craftSlots.getItem(debug4);
/* 63 */       ItemStack debug6 = (ItemStack)debug3.get(debug4);
/*    */       
/* 65 */       if (!debug5.isEmpty()) {
/* 66 */         this.craftSlots.removeItem(debug4, 1);
/* 67 */         debug5 = this.craftSlots.getItem(debug4);
/*    */       } 
/*    */       
/* 70 */       if (!debug6.isEmpty()) {
/* 71 */         if (debug5.isEmpty()) {
/*    */           
/* 73 */           this.craftSlots.setItem(debug4, debug6);
/* 74 */         } else if (ItemStack.isSame(debug5, debug6) && ItemStack.tagMatches(debug5, debug6)) {
/* 75 */           debug6.grow(debug5.getCount());
/* 76 */           this.craftSlots.setItem(debug4, debug6);
/* 77 */         } else if (!this.player.inventory.add(debug6)) {
/*    */           
/* 79 */           this.player.drop(debug6, false);
/*    */         } 
/*    */       }
/*    */     } 
/* 83 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ResultSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */