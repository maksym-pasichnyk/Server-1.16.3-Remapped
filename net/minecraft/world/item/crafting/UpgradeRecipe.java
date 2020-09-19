/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class UpgradeRecipe implements Recipe<Container> {
/*    */   private final Ingredient base;
/*    */   private final Ingredient addition;
/*    */   private final ItemStack result;
/*    */   private final ResourceLocation id;
/*    */   
/*    */   public UpgradeRecipe(ResourceLocation debug1, Ingredient debug2, Ingredient debug3, ItemStack debug4) {
/* 20 */     this.id = debug1;
/* 21 */     this.base = debug2;
/* 22 */     this.addition = debug3;
/* 23 */     this.result = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Container debug1, Level debug2) {
/* 28 */     return (this.base.test(debug1.getItem(0)) && this.addition.test(debug1.getItem(1)));
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack assemble(Container debug1) {
/* 33 */     ItemStack debug2 = this.result.copy();
/* 34 */     CompoundTag debug3 = debug1.getItem(0).getTag();
/* 35 */     if (debug3 != null) {
/* 36 */       debug2.setTag(debug3.copy());
/*    */     }
/* 38 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getResultItem() {
/* 48 */     return this.result;
/*    */   }
/*    */   
/*    */   public boolean isAdditionIngredient(ItemStack debug1) {
/* 52 */     return this.addition.test(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 62 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeSerializer<?> getSerializer() {
/* 67 */     return RecipeSerializer.SMITHING;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecipeType<?> getType() {
/* 72 */     return RecipeType.SMITHING;
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements RecipeSerializer<UpgradeRecipe> {
/*    */     public UpgradeRecipe fromJson(ResourceLocation debug1, JsonObject debug2) {
/* 78 */       Ingredient debug3 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(debug2, "base"));
/* 79 */       Ingredient debug4 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(debug2, "addition"));
/* 80 */       ItemStack debug5 = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(debug2, "result"));
/* 81 */       return new UpgradeRecipe(debug1, debug3, debug4, debug5);
/*    */     }
/*    */ 
/*    */     
/*    */     public UpgradeRecipe fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 86 */       Ingredient debug3 = Ingredient.fromNetwork(debug2);
/* 87 */       Ingredient debug4 = Ingredient.fromNetwork(debug2);
/* 88 */       ItemStack debug5 = debug2.readItem();
/* 89 */       return new UpgradeRecipe(debug1, debug3, debug4, debug5);
/*    */     }
/*    */ 
/*    */     
/*    */     public void toNetwork(FriendlyByteBuf debug1, UpgradeRecipe debug2) {
/* 94 */       debug2.base.toNetwork(debug1);
/* 95 */       debug2.addition.toNetwork(debug1);
/* 96 */       debug1.writeItem(debug2.result);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\UpgradeRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */