/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.inventory.CraftingContainer;
/*    */ import net.minecraft.world.item.BannerItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ShieldDecorationRecipe extends CustomRecipe {
/*    */   public ShieldDecorationRecipe(ResourceLocation debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(CraftingContainer debug1, Level debug2) {
/* 20 */     ItemStack debug3 = ItemStack.EMPTY;
/* 21 */     ItemStack debug4 = ItemStack.EMPTY;
/*    */     
/* 23 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/* 24 */       ItemStack debug6 = debug1.getItem(debug5);
/* 25 */       if (!debug6.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 29 */         if (debug6.getItem() instanceof BannerItem) {
/* 30 */           if (!debug4.isEmpty())
/*    */           {
/* 32 */             return false;
/*    */           }
/* 34 */           debug4 = debug6;
/* 35 */         } else if (debug6.getItem() == Items.SHIELD) {
/* 36 */           if (!debug3.isEmpty())
/*    */           {
/* 38 */             return false;
/*    */           }
/* 40 */           if (debug6.getTagElement("BlockEntityTag") != null)
/*    */           {
/* 42 */             return false;
/*    */           }
/* 44 */           debug3 = debug6;
/*    */         } else {
/*    */           
/* 47 */           return false;
/*    */         } 
/*    */       }
/*    */     } 
/* 51 */     if (debug3.isEmpty() || debug4.isEmpty())
/*    */     {
/* 53 */       return false;
/*    */     }
/*    */     
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(CraftingContainer debug1) {
/* 61 */     ItemStack debug2 = ItemStack.EMPTY;
/* 62 */     ItemStack debug3 = ItemStack.EMPTY;
/*    */     
/* 64 */     for (int i = 0; i < debug1.getContainerSize(); i++) {
/* 65 */       ItemStack itemStack = debug1.getItem(i);
/* 66 */       if (!itemStack.isEmpty())
/*    */       {
/*    */ 
/*    */         
/* 70 */         if (itemStack.getItem() instanceof BannerItem) {
/* 71 */           debug2 = itemStack;
/* 72 */         } else if (itemStack.getItem() == Items.SHIELD) {
/* 73 */           debug3 = itemStack.copy();
/*    */         } 
/*    */       }
/*    */     } 
/* 77 */     if (debug3.isEmpty()) {
/* 78 */       return debug3;
/*    */     }
/*    */     
/* 81 */     CompoundTag debug4 = debug2.getTagElement("BlockEntityTag");
/* 82 */     CompoundTag debug5 = (debug4 == null) ? new CompoundTag() : debug4.copy();
/*    */     
/* 84 */     debug5.putInt("Base", ((BannerItem)debug2.getItem()).getColor().getId());
/*    */     
/* 86 */     debug3.addTagElement("BlockEntityTag", (Tag)debug5);
/*    */     
/* 88 */     return debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 98 */     return RecipeSerializer.SHIELD_DECORATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\ShieldDecorationRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */