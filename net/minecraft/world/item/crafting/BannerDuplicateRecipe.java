/*     */ package net.minecraft.world.item.crafting;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.BannerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*     */ 
/*     */ public class BannerDuplicateRecipe extends CustomRecipe {
/*     */   public BannerDuplicateRecipe(ResourceLocation debug1) {
/*  15 */     super(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  21 */     DyeColor debug3 = null;
/*  22 */     ItemStack debug4 = null;
/*  23 */     ItemStack debug5 = null;
/*     */     
/*  25 */     for (int debug6 = 0; debug6 < debug1.getContainerSize(); debug6++) {
/*  26 */       ItemStack debug7 = debug1.getItem(debug6);
/*  27 */       Item debug8 = debug7.getItem();
/*  28 */       if (debug8 instanceof BannerItem) {
/*     */ 
/*     */ 
/*     */         
/*  32 */         BannerItem debug9 = (BannerItem)debug8;
/*     */         
/*  34 */         if (debug3 == null) {
/*  35 */           debug3 = debug9.getColor();
/*  36 */         } else if (debug3 != debug9.getColor()) {
/*  37 */           return false;
/*     */         } 
/*     */         
/*  40 */         int debug10 = BannerBlockEntity.getPatternCount(debug7);
/*  41 */         if (debug10 > 6) {
/*  42 */           return false;
/*     */         }
/*     */         
/*  45 */         if (debug10 > 0) {
/*  46 */           if (debug4 == null) {
/*  47 */             debug4 = debug7;
/*     */           } else {
/*  49 */             return false;
/*     */           }
/*     */         
/*  52 */         } else if (debug5 == null) {
/*  53 */           debug5 = debug7;
/*     */         } else {
/*  55 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return (debug4 != null && debug5 != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/*  66 */     for (int debug2 = 0; debug2 < debug1.getContainerSize(); debug2++) {
/*  67 */       ItemStack debug3 = debug1.getItem(debug2);
/*  68 */       if (!debug3.isEmpty()) {
/*     */ 
/*     */         
/*  71 */         int debug4 = BannerBlockEntity.getPatternCount(debug3);
/*  72 */         if (debug4 > 0 && debug4 <= 6) {
/*  73 */           ItemStack debug5 = debug3.copy();
/*  74 */           debug5.setCount(1);
/*  75 */           return debug5;
/*     */         } 
/*     */       } 
/*     */     } 
/*  79 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingContainer debug1) {
/*  84 */     NonNullList<ItemStack> debug2 = NonNullList.withSize(debug1.getContainerSize(), ItemStack.EMPTY);
/*     */     
/*  86 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  87 */       ItemStack debug4 = debug1.getItem(debug3);
/*  88 */       if (!debug4.isEmpty()) {
/*  89 */         if (debug4.getItem().hasCraftingRemainingItem()) {
/*  90 */           debug2.set(debug3, new ItemStack((ItemLike)debug4.getItem().getCraftingRemainingItem()));
/*  91 */         } else if (debug4.hasTag() && 
/*  92 */           BannerBlockEntity.getPatternCount(debug4) > 0) {
/*  93 */           ItemStack debug5 = debug4.copy();
/*  94 */           debug5.setCount(1);
/*  95 */           debug2.set(debug3, debug5);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 101 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/* 106 */     return RecipeSerializer.BANNER_DUPLICATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\BannerDuplicateRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */