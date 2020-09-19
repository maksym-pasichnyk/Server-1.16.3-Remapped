/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*    */ 
/*    */ public interface FinishedRecipe
/*    */ {
/*    */   void serializeRecipeData(JsonObject paramJsonObject);
/*    */   
/*    */   default JsonObject serializeRecipe() {
/* 14 */     JsonObject debug1 = new JsonObject();
/* 15 */     debug1.addProperty("type", Registry.RECIPE_SERIALIZER.getKey(getType()).toString());
/* 16 */     serializeRecipeData(debug1);
/* 17 */     return debug1;
/*    */   }
/*    */   
/*    */   ResourceLocation getId();
/*    */   
/*    */   RecipeSerializer<?> getType();
/*    */   
/*    */   @Nullable
/*    */   JsonObject serializeAdvancement();
/*    */   
/*    */   @Nullable
/*    */   ResourceLocation getAdvancementId();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\FinishedRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */