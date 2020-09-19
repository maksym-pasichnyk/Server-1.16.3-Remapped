/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ 
/*    */ public class SimpleCookingSerializer<T extends AbstractCookingRecipe>
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final int defaultCookingTime;
/*    */   private final CookieBaker<T> factory;
/*    */   
/*    */   public SimpleCookingSerializer(CookieBaker<T> debug1, int debug2) {
/* 20 */     this.defaultCookingTime = debug2;
/* 21 */     this.factory = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public T fromJson(ResourceLocation debug1, JsonObject debug2) {
/* 26 */     String debug3 = GsonHelper.getAsString(debug2, "group", "");
/*    */     
/* 28 */     JsonElement debug4 = GsonHelper.isArrayNode(debug2, "ingredient") ? (JsonElement)GsonHelper.getAsJsonArray(debug2, "ingredient") : (JsonElement)GsonHelper.getAsJsonObject(debug2, "ingredient");
/* 29 */     Ingredient debug5 = Ingredient.fromJson(debug4);
/*    */     
/* 31 */     String debug6 = GsonHelper.getAsString(debug2, "result");
/* 32 */     ResourceLocation debug7 = new ResourceLocation(debug6);
/* 33 */     ItemStack debug8 = new ItemStack((ItemLike)Registry.ITEM.getOptional(debug7).orElseThrow(() -> new IllegalStateException("Item: " + debug0 + " does not exist")));
/* 34 */     float debug9 = GsonHelper.getAsFloat(debug2, "experience", 0.0F);
/* 35 */     int debug10 = GsonHelper.getAsInt(debug2, "cookingtime", this.defaultCookingTime);
/*    */     
/* 37 */     return this.factory.create(debug1, debug3, debug5, debug8, debug9, debug10);
/*    */   }
/*    */ 
/*    */   
/*    */   public T fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 42 */     String debug3 = debug2.readUtf(32767);
/* 43 */     Ingredient debug4 = Ingredient.fromNetwork(debug2);
/* 44 */     ItemStack debug5 = debug2.readItem();
/* 45 */     float debug6 = debug2.readFloat();
/* 46 */     int debug7 = debug2.readVarInt();
/* 47 */     return this.factory.create(debug1, debug3, debug4, debug5, debug6, debug7);
/*    */   }
/*    */ 
/*    */   
/*    */   public void toNetwork(FriendlyByteBuf debug1, T debug2) {
/* 52 */     debug1.writeUtf(((AbstractCookingRecipe)debug2).group);
/* 53 */     ((AbstractCookingRecipe)debug2).ingredient.toNetwork(debug1);
/* 54 */     debug1.writeItem(((AbstractCookingRecipe)debug2).result);
/* 55 */     debug1.writeFloat(((AbstractCookingRecipe)debug2).experience);
/* 56 */     debug1.writeVarInt(((AbstractCookingRecipe)debug2).cookingTime);
/*    */   }
/*    */   
/*    */   static interface CookieBaker<T extends AbstractCookingRecipe> {
/*    */     T create(ResourceLocation param1ResourceLocation, String param1String, Ingredient param1Ingredient, ItemStack param1ItemStack, float param1Float, int param1Int);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\SimpleCookingSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */