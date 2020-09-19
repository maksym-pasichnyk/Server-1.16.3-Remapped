/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class SimpleRecipeSerializer<T extends Recipe<?>>
/*    */   implements RecipeSerializer<T> {
/*    */   private final Function<ResourceLocation, T> constructor;
/*    */   
/*    */   public SimpleRecipeSerializer(Function<ResourceLocation, T> debug1) {
/* 13 */     this.constructor = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public T fromJson(ResourceLocation debug1, JsonObject debug2) {
/* 18 */     return this.constructor.apply(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public T fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 23 */     return this.constructor.apply(debug1);
/*    */   }
/*    */   
/*    */   public void toNetwork(FriendlyByteBuf debug1, T debug2) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\SimpleRecipeSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */