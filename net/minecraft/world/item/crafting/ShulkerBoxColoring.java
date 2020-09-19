/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*    */ 
/*    */ public class ShulkerBoxColoring extends CustomRecipe {
/*    */   public ShulkerBoxColoring(ResourceLocation debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 20 */     int debug3 = 0;
/* 21 */     int debug4 = 0;
/*    */     
/* 23 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 24 */       ItemStack debug6 = debug1.getItem(debug5);
/*    */       
/* 26 */       if (!debug6.isEmpty()) {
/*    */ 
/*    */ 
/*    */         
/* 30 */         if (Block.byItem(debug6.getItem()) instanceof ShulkerBoxBlock) {
/* 31 */           debug3++;
/* 32 */         } else if (debug6.getItem() instanceof DyeItem) {
/* 33 */           debug4++;
/*    */         } else {
/* 35 */           return false;
/*    */         } 
/*    */         
/* 38 */         if (debug4 > 1 || debug3 > 1) {
/* 39 */           return false;
/*    */         }
/*    */       } 
/*    */     } 
/* 43 */     return (debug3 == 1 && debug4 == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 48 */     ItemStack debug2 = ItemStack.EMPTY;
/* 49 */     DyeItem debug3 = (DyeItem)Items.WHITE_DYE;
/*    */     
/* 51 */     for (int i = 0; i < debug1.getContainerSize(); i++) {
/* 52 */       ItemStack debug5 = debug1.getItem(i);
/*    */       
/* 54 */       if (!debug5.isEmpty()) {
/*    */ 
/*    */ 
/*    */         
/* 58 */         Item debug6 = debug5.getItem();
/* 59 */         if (Block.byItem(debug6) instanceof ShulkerBoxBlock) {
/* 60 */           debug2 = debug5;
/* 61 */         } else if (debug6 instanceof DyeItem) {
/* 62 */           debug3 = (DyeItem)debug6;
/*    */         } 
/*    */       } 
/*    */     } 
/* 66 */     ItemStack debug4 = ShulkerBoxBlock.getColoredItemStack(debug3.getDyeColor());
/* 67 */     if (debug2.hasTag()) {
/* 68 */       debug4.setTag(debug2.getTag().copy());
/*    */     }
/*    */     
/* 71 */     return debug4;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 81 */     return RecipeSerializer.SHULKER_BOX_COLORING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\ShulkerBoxColoring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */