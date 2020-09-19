/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.FlowerBlock;
/*    */ 
/*    */ public class SuspiciousStewRecipe extends CustomRecipe {
/*    */   public SuspiciousStewRecipe(ResourceLocation debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 22 */     boolean debug3 = false;
/* 23 */     boolean debug4 = false;
/* 24 */     boolean debug5 = false;
/* 25 */     boolean debug6 = false;
/*    */     
/* 27 */     for (int debug7 = 0; debug7 < debug1.getContainerSize(); debug7++) {
/* 28 */       ItemStack debug8 = debug1.getItem(debug7);
/* 29 */       if (!debug8.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 33 */         if (debug8.getItem() == Blocks.BROWN_MUSHROOM.asItem() && !debug5) {
/* 34 */           debug5 = true;
/* 35 */         } else if (debug8.getItem() == Blocks.RED_MUSHROOM.asItem() && !debug4) {
/* 36 */           debug4 = true;
/* 37 */         } else if (debug8.getItem().is((Tag)ItemTags.SMALL_FLOWERS) && !debug3) {
/* 38 */           debug3 = true;
/* 39 */         } else if (debug8.getItem() == Items.BOWL && !debug6) {
/* 40 */           debug6 = true;
/*    */         } else {
/* 42 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 46 */     return (debug3 && debug5 && debug4 && debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 51 */     ItemStack debug2 = ItemStack.EMPTY;
/* 52 */     for (int i = 0; i < debug1.getContainerSize(); i++) {
/* 53 */       ItemStack debug4 = debug1.getItem(i);
/* 54 */       if (!debug4.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 58 */         if (debug4.getItem().is((Tag)ItemTags.SMALL_FLOWERS)) {
/* 59 */           debug2 = debug4;
/*    */           break;
/*    */         } 
/*    */       }
/*    */     } 
/* 64 */     ItemStack debug3 = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW, 1);
/* 65 */     if (debug2.getItem() instanceof BlockItem && ((BlockItem)debug2.getItem()).getBlock() instanceof FlowerBlock) {
/* 66 */       FlowerBlock debug4 = (FlowerBlock)((BlockItem)debug2.getItem()).getBlock();
/* 67 */       MobEffect debug5 = debug4.getSuspiciousStewEffect();
/* 68 */       SuspiciousStewItem.saveMobEffect(debug3, debug5, debug4.getEffectDuration());
/*    */     } 
/*    */     
/* 71 */     return debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 81 */     return RecipeSerializer.SUSPICIOUS_STEW;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\SuspiciousStewRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */