/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionUtils;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class TippedArrowRecipe extends CustomRecipe {
/*    */   public TippedArrowRecipe(ResourceLocation debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 18 */     if (debug1.getWidth() != 3 || debug1.getHeight() != 3) {
/* 19 */       return false;
/*    */     }
/*    */     
/* 22 */     for (int debug3 = 0; debug3 < debug1.getWidth(); debug3++) {
/* 23 */       for (int debug4 = 0; debug4 < debug1.getHeight(); debug4++) {
/* 24 */         ItemStack debug5 = debug1.getItem(debug3 + debug4 * debug1.getWidth());
/*    */         
/* 26 */         if (debug5.isEmpty()) {
/* 27 */           return false;
/*    */         }
/*    */         
/* 30 */         Item debug6 = debug5.getItem();
/* 31 */         if (debug3 == 1 && debug4 == 1) {
/* 32 */           if (debug6 != Items.LINGERING_POTION) {
/* 33 */             return false;
/*    */           }
/* 35 */         } else if (debug6 != Items.ARROW) {
/* 36 */           return false;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 46 */     ItemStack debug2 = debug1.getItem(1 + debug1.getWidth());
/* 47 */     if (debug2.getItem() != Items.LINGERING_POTION) {
/* 48 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 51 */     ItemStack debug3 = new ItemStack((ItemLike)Items.TIPPED_ARROW, 8);
/* 52 */     PotionUtils.setPotion(debug3, PotionUtils.getPotion(debug2));
/* 53 */     PotionUtils.setCustomEffects(debug3, PotionUtils.getCustomEffects(debug2));
/*    */     
/* 55 */     return debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 65 */     return RecipeSerializer.TIPPED_ARROW;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\TippedArrowRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */