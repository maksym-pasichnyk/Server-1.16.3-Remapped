/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class MapCloningRecipe extends CustomRecipe {
/*    */   public MapCloningRecipe(ResourceLocation debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 16 */     int debug3 = 0;
/* 17 */     ItemStack debug4 = ItemStack.EMPTY;
/*    */     
/* 19 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 20 */       ItemStack debug6 = debug1.getItem(debug5);
/* 21 */       if (!debug6.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 25 */         if (debug6.getItem() == Items.FILLED_MAP) {
/* 26 */           if (!debug4.isEmpty()) {
/* 27 */             return false;
/*    */           }
/* 29 */           debug4 = debug6;
/* 30 */         } else if (debug6.getItem() == Items.MAP) {
/* 31 */           debug3++;
/*    */         } else {
/* 33 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 37 */     return (!debug4.isEmpty() && debug3 > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 42 */     int debug2 = 0;
/* 43 */     ItemStack debug3 = ItemStack.EMPTY;
/*    */     
/* 45 */     for (int i = 0; i < debug1.getContainerSize(); i++) {
/* 46 */       ItemStack debug5 = debug1.getItem(i);
/* 47 */       if (!debug5.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 51 */         if (debug5.getItem() == Items.FILLED_MAP) {
/* 52 */           if (!debug3.isEmpty()) {
/* 53 */             return ItemStack.EMPTY;
/*    */           }
/* 55 */           debug3 = debug5;
/* 56 */         } else if (debug5.getItem() == Items.MAP) {
/* 57 */           debug2++;
/*    */         } else {
/* 59 */           return ItemStack.EMPTY;
/*    */         } 
/*    */       }
/*    */     } 
/* 63 */     if (debug3.isEmpty() || debug2 < 1) {
/* 64 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 67 */     ItemStack debug4 = debug3.copy();
/* 68 */     debug4.setCount(debug2 + 1);
/*    */     
/* 70 */     return debug4;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 80 */     return RecipeSerializer.MAP_CLONING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\MapCloningRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */