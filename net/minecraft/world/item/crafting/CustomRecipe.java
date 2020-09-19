/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public abstract class CustomRecipe implements CraftingRecipe {
/*    */   private final ResourceLocation id;
/*    */   
/*    */   public CustomRecipe(ResourceLocation debug1) {
/* 10 */     this.id = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSpecial() {
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getResultItem() {
/* 25 */     return ItemStack.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\CustomRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */