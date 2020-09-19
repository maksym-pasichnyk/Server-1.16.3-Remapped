/*     */ package net.minecraft.world.item.crafting;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.WrittenBookItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class BookCloningRecipe extends CustomRecipe {
/*     */   public BookCloningRecipe(ResourceLocation debug1) {
/*  14 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  19 */     int debug3 = 0;
/*  20 */     ItemStack debug4 = ItemStack.EMPTY;
/*     */     
/*  22 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/*  23 */       ItemStack debug6 = debug1.getItem(debug5);
/*  24 */       if (!debug6.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  28 */         if (debug6.getItem() == Items.WRITTEN_BOOK) {
/*  29 */           if (!debug4.isEmpty()) {
/*  30 */             return false;
/*     */           }
/*  32 */           debug4 = debug6;
/*  33 */         } else if (debug6.getItem() == Items.WRITABLE_BOOK) {
/*  34 */           debug3++;
/*     */         } else {
/*  36 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/*  40 */     return (!debug4.isEmpty() && debug4.hasTag() && debug3 > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/*  45 */     int debug2 = 0;
/*  46 */     ItemStack debug3 = ItemStack.EMPTY;
/*     */     
/*  48 */     for (int i = 0; i < debug1.getContainerSize(); i++) {
/*  49 */       ItemStack itemStack = debug1.getItem(i);
/*  50 */       if (!itemStack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  54 */         if (itemStack.getItem() == Items.WRITTEN_BOOK) {
/*  55 */           if (!debug3.isEmpty()) {
/*  56 */             return ItemStack.EMPTY;
/*     */           }
/*  58 */           debug3 = itemStack;
/*  59 */         } else if (itemStack.getItem() == Items.WRITABLE_BOOK) {
/*  60 */           debug2++;
/*     */         } else {
/*  62 */           return ItemStack.EMPTY;
/*     */         } 
/*     */       }
/*     */     } 
/*  66 */     if (debug3.isEmpty() || !debug3.hasTag() || debug2 < 1 || WrittenBookItem.getGeneration(debug3) >= 2) {
/*  67 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/*  70 */     ItemStack debug4 = new ItemStack((ItemLike)Items.WRITTEN_BOOK, debug2);
/*     */     
/*  72 */     CompoundTag debug5 = debug3.getTag().copy();
/*     */     
/*  74 */     debug5.putInt("generation", WrittenBookItem.getGeneration(debug3) + 1);
/*  75 */     debug4.setTag(debug5);
/*     */     
/*  77 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingContainer debug1) {
/*  82 */     NonNullList<ItemStack> debug2 = NonNullList.withSize(debug1.getContainerSize(), ItemStack.EMPTY);
/*     */     
/*  84 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  85 */       ItemStack debug4 = debug1.getItem(debug3);
/*  86 */       if (debug4.getItem().hasCraftingRemainingItem()) {
/*  87 */         debug2.set(debug3, new ItemStack((ItemLike)debug4.getItem().getCraftingRemainingItem()));
/*  88 */       } else if (debug4.getItem() instanceof WrittenBookItem) {
/*  89 */         ItemStack debug5 = debug4.copy();
/*  90 */         debug5.setCount(1);
/*  91 */         debug2.set(debug3, debug5);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  96 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/* 101 */     return RecipeSerializer.BOOK_CLONING;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\BookCloningRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */