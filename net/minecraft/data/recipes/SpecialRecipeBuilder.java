/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*    */ import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
/*    */ 
/*    */ public class SpecialRecipeBuilder
/*    */ {
/*    */   private final SimpleRecipeSerializer<?> serializer;
/*    */   
/*    */   public SpecialRecipeBuilder(SimpleRecipeSerializer<?> debug1) {
/* 15 */     this.serializer = debug1;
/*    */   }
/*    */   
/*    */   public static SpecialRecipeBuilder special(SimpleRecipeSerializer<?> debug0) {
/* 19 */     return new SpecialRecipeBuilder(debug0);
/*    */   }
/*    */   
/*    */   public void save(Consumer<FinishedRecipe> debug1, final String id) {
/* 23 */     debug1.accept(new FinishedRecipe()
/*    */         {
/*    */           public void serializeRecipeData(JsonObject debug1) {}
/*    */ 
/*    */ 
/*    */           
/*    */           public RecipeSerializer<?> getType() {
/* 30 */             return (RecipeSerializer<?>)SpecialRecipeBuilder.this.serializer;
/*    */           }
/*    */ 
/*    */           
/*    */           public ResourceLocation getId() {
/* 35 */             return new ResourceLocation(id);
/*    */           }
/*    */ 
/*    */           
/*    */           @Nullable
/*    */           public JsonObject serializeAdvancement() {
/* 41 */             return null;
/*    */           }
/*    */ 
/*    */           
/*    */           public ResourceLocation getAdvancementId() {
/* 46 */             return new ResourceLocation("");
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\SpecialRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */