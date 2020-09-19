/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.DyeItem;
/*    */ import net.minecraft.world.item.DyeableLeatherItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ArmorDyeRecipe extends CustomRecipe {
/*    */   public ArmorDyeRecipe(ResourceLocation debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 21 */     ItemStack debug3 = ItemStack.EMPTY;
/* 22 */     List<ItemStack> debug4 = Lists.newArrayList();
/*    */     
/* 24 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 25 */       ItemStack debug6 = debug1.getItem(debug5);
/* 26 */       if (!debug6.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 30 */         if (debug6.getItem() instanceof DyeableLeatherItem) {
/* 31 */           if (!debug3.isEmpty()) {
/* 32 */             return false;
/*    */           }
/* 34 */           debug3 = debug6;
/* 35 */         } else if (debug6.getItem() instanceof DyeItem) {
/* 36 */           debug4.add(debug6);
/*    */         } else {
/* 38 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 42 */     return (!debug3.isEmpty() && !debug4.isEmpty());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 47 */     List<DyeItem> debug2 = Lists.newArrayList();
/* 48 */     ItemStack debug3 = ItemStack.EMPTY;
/*    */     
/* 50 */     for (int debug4 = 0; debug4 < debug1.getContainerSize(); debug4++) {
/* 51 */       ItemStack debug5 = debug1.getItem(debug4);
/* 52 */       if (!debug5.isEmpty()) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         Item debug6 = debug5.getItem();
/* 57 */         if (debug6 instanceof DyeableLeatherItem) {
/* 58 */           if (!debug3.isEmpty()) {
/* 59 */             return ItemStack.EMPTY;
/*    */           }
/*    */           
/* 62 */           debug3 = debug5.copy();
/* 63 */         } else if (debug6 instanceof DyeItem) {
/* 64 */           debug2.add((DyeItem)debug6);
/*    */         } else {
/* 66 */           return ItemStack.EMPTY;
/*    */         } 
/*    */       } 
/*    */     } 
/* 70 */     if (debug3.isEmpty() || debug2.isEmpty()) {
/* 71 */       return ItemStack.EMPTY;
/*    */     }
/*    */     
/* 74 */     return DyeableLeatherItem.dyeArmor(debug3, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 84 */     return RecipeSerializer.ARMOR_DYE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\ArmorDyeRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */