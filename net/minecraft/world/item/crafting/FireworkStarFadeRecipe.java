/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FireworkStarFadeRecipe extends CustomRecipe {
/* 16 */   private static final Ingredient STAR_INGREDIENT = Ingredient.of(new ItemLike[] { (ItemLike)Items.FIREWORK_STAR });
/*    */   
/*    */   public FireworkStarFadeRecipe(ResourceLocation debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 24 */     boolean debug3 = false;
/* 25 */     boolean debug4 = false;
/*    */     
/* 27 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 28 */       ItemStack debug6 = debug1.getItem(debug5);
/* 29 */       if (!debug6.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 33 */         if (debug6.getItem() instanceof DyeItem) {
/* 34 */           debug3 = true;
/* 35 */         } else if (STAR_INGREDIENT.test(debug6)) {
/* 36 */           if (debug4) {
/* 37 */             return false;
/*    */           }
/* 39 */           debug4 = true;
/*    */         } else {
/* 41 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 45 */     return (debug4 && debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 50 */     List<Integer> debug2 = Lists.newArrayList();
/* 51 */     ItemStack debug3 = null;
/*    */     
/* 53 */     for (int debug4 = 0; debug4 < debug1.getContainerSize(); debug4++) {
/* 54 */       ItemStack debug5 = debug1.getItem(debug4);
/*    */       
/* 56 */       Item debug6 = debug5.getItem();
/* 57 */       if (debug6 instanceof DyeItem) {
/* 58 */         debug2.add(Integer.valueOf(((DyeItem)debug6).getDyeColor().getFireworkColor()));
/* 59 */       } else if (STAR_INGREDIENT.test(debug5)) {
/* 60 */         debug3 = debug5.copy();
/* 61 */         debug3.setCount(1);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 66 */     if (debug3 == null || debug2.isEmpty()) {
/* 67 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 70 */     debug3.getOrCreateTagElement("Explosion").putIntArray("FadeColors", debug2);
/*    */     
/* 72 */     return debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 82 */     return RecipeSerializer.FIREWORK_STAR_FADE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\FireworkStarFadeRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */