/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public class MapExtendingRecipe extends ShapedRecipe {
/*    */   public MapExtendingRecipe(ResourceLocation debug1) {
/* 15 */     super(debug1, "", 3, 3, 
/* 16 */         NonNullList.of(Ingredient.EMPTY, (Object[])new Ingredient[] {
/* 17 */             Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER
/* 18 */               }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER }), Ingredient.of(new ItemLike[] { (ItemLike)Items.FILLED_MAP }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER
/* 19 */               }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER }), Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER })
/*    */           }), new ItemStack((ItemLike)Items.MAP));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 27 */     if (!super.matches(debug1, debug2)) {
/* 28 */       return false;
/*    */     }
/* 30 */     ItemStack debug3 = ItemStack.EMPTY;
/*    */     
/* 32 */     for (int i = 0; i < debug1.getContainerSize() && debug3.isEmpty(); i++) {
/* 33 */       ItemStack debug5 = debug1.getItem(i);
/* 34 */       if (debug5.getItem() == Items.FILLED_MAP) {
/* 35 */         debug3 = debug5;
/*    */       }
/*    */     } 
/*    */     
/* 39 */     if (debug3.isEmpty()) {
/* 40 */       return false;
/*    */     }
/* 42 */     MapItemSavedData debug4 = MapItem.getOrCreateSavedData(debug3, debug2);
/* 43 */     if (debug4 == null) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     if (isExplorationMap(debug4)) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     return (debug4.scale < 4);
/*    */   }
/*    */   
/*    */   private boolean isExplorationMap(MapItemSavedData debug1) {
/* 55 */     if (debug1.decorations != null) {
/* 56 */       for (MapDecoration debug3 : debug1.decorations.values()) {
/* 57 */         if (debug3.getType() == MapDecoration.Type.MANSION || debug3.getType() == MapDecoration.Type.MONUMENT) {
/* 58 */           return true;
/*    */         }
/*    */       } 
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 67 */     ItemStack debug2 = ItemStack.EMPTY;
/*    */     
/* 69 */     for (int debug3 = 0; debug3 < debug1.getContainerSize() && debug2.isEmpty(); debug3++) {
/* 70 */       ItemStack debug4 = debug1.getItem(debug3);
/* 71 */       if (debug4.getItem() == Items.FILLED_MAP) {
/* 72 */         debug2 = debug4;
/*    */       }
/*    */     } 
/*    */     
/* 76 */     debug2 = debug2.copy();
/* 77 */     debug2.setCount(1);
/* 78 */     debug2.getOrCreateTag().putInt("map_scale_direction", 1);
/*    */     
/* 80 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSpecial() {
/* 85 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 90 */     return RecipeSerializer.MAP_EXTENDING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\MapExtendingRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */