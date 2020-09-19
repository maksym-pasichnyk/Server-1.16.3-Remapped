/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FireworkRocketRecipe extends CustomRecipe {
/* 13 */   private static final Ingredient PAPER_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.PAPER });
/* 14 */   private static final Ingredient GUNPOWDER_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.GUNPOWDER });
/* 15 */   private static final Ingredient STAR_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.FIREWORK_STAR });
/*    */   
/*    */   public FireworkRocketRecipe(ResourceLocation debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 23 */     boolean debug3 = false;
/* 24 */     int debug4 = 0;
/*    */     
/* 26 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 27 */       ItemStack debug6 = debug1.getItem(debug5);
/* 28 */       if (!debug6.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 32 */         if (PAPER_INGREDIENT.test(debug6)) {
/* 33 */           if (debug3) {
/* 34 */             return false;
/*    */           }
/* 36 */           debug3 = true;
/* 37 */         } else if (GUNPOWDER_INGREDIENT.test(debug6)) {
/* 38 */           debug4++;
/* 39 */           if (debug4 > 3) {
/* 40 */             return false;
/*    */           }
/* 42 */         } else if (!STAR_INGREDIENT.test(debug6)) {
/* 43 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 47 */     return (debug3 && debug4 >= 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 52 */     ItemStack debug2 = new ItemStack((ItemLike)Items.FIREWORK_ROCKET, 3);
/* 53 */     CompoundTag debug3 = debug2.getOrCreateTagElement("Fireworks");
/* 54 */     ListTag debug4 = new ListTag();
/*    */     
/* 56 */     int debug5 = 0;
/*    */     
/* 58 */     for (int debug6 = 0; debug6 < debug1.getContainerSize(); debug6++) {
/* 59 */       ItemStack debug7 = debug1.getItem(debug6);
/* 60 */       if (!debug7.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 64 */         if (GUNPOWDER_INGREDIENT.test(debug7)) {
/* 65 */           debug5++;
/* 66 */         } else if (STAR_INGREDIENT.test(debug7)) {
/* 67 */           CompoundTag debug8 = debug7.getTagElement("Explosion");
/* 68 */           if (debug8 != null) {
/* 69 */             debug4.add(debug8);
/*    */           }
/*    */         } 
/*    */       }
/*    */     } 
/* 74 */     debug3.putByte("Flight", (byte)debug5);
/* 75 */     if (!debug4.isEmpty()) {
/* 76 */       debug3.put("Explosions", (Tag)debug4);
/*    */     }
/*    */     
/* 79 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getResultItem() {
/* 89 */     return new ItemStack((ItemLike)Items.FIREWORK_ROCKET);
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 94 */     return RecipeSerializer.FIREWORK_ROCKET;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\FireworkRocketRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */