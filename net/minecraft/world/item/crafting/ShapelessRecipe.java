/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapelessRecipe
/*     */   implements CraftingRecipe {
/*     */   private final ResourceLocation id;
/*     */   private final String group;
/*     */   
/*     */   public ShapelessRecipe(ResourceLocation debug1, String debug2, ItemStack debug3, NonNullList<Ingredient> debug4) {
/*  22 */     this.id = debug1;
/*  23 */     this.group = debug2;
/*  24 */     this.result = debug3;
/*  25 */     this.ingredients = debug4;
/*     */   }
/*     */   private final ItemStack result; private final NonNullList<Ingredient> ingredients;
/*     */   
/*     */   public ResourceLocation getId() {
/*  30 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/*  35 */     return RecipeSerializer.SHAPELESS_RECIPE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getResultItem() {
/*  45 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<Ingredient> getIngredients() {
/*  50 */     return this.ingredients;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  55 */     StackedContents debug3 = new StackedContents();
/*     */     
/*  57 */     int debug4 = 0;
/*  58 */     for (int debug5 = 0; debug5 < debug1.getContainerSize(); debug5++) {
/*  59 */       ItemStack debug6 = debug1.getItem(debug5);
/*  60 */       if (!debug6.isEmpty()) {
/*  61 */         debug4++;
/*  62 */         debug3.accountStack(debug6, 1);
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     return (debug4 == this.ingredients.size() && debug3.canCraft(this, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/*  71 */     return this.result.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     implements RecipeSerializer<ShapelessRecipe>
/*     */   {
/*     */     public ShapelessRecipe fromJson(ResourceLocation debug1, JsonObject debug2) {
/*  82 */       String debug3 = GsonHelper.getAsString(debug2, "group", "");
/*  83 */       NonNullList<Ingredient> debug4 = itemsFromJson(GsonHelper.getAsJsonArray(debug2, "ingredients"));
/*  84 */       if (debug4.isEmpty())
/*  85 */         throw new JsonParseException("No ingredients for shapeless recipe"); 
/*  86 */       if (debug4.size() > 9) {
/*  87 */         throw new JsonParseException("Too many ingredients for shapeless recipe");
/*     */       }
/*     */       
/*  90 */       ItemStack debug5 = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(debug2, "result"));
/*  91 */       return new ShapelessRecipe(debug1, debug3, debug5, debug4);
/*     */     }
/*     */     
/*     */     private static NonNullList<Ingredient> itemsFromJson(JsonArray debug0) {
/*  95 */       NonNullList<Ingredient> debug1 = NonNullList.create();
/*     */       
/*  97 */       for (int debug2 = 0; debug2 < debug0.size(); debug2++) {
/*  98 */         Ingredient debug3 = Ingredient.fromJson(debug0.get(debug2));
/*  99 */         if (!debug3.isEmpty()) {
/* 100 */           debug1.add(debug3);
/*     */         }
/*     */       } 
/*     */       
/* 104 */       return debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ShapelessRecipe fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 109 */       String debug3 = debug2.readUtf(32767);
/* 110 */       int debug4 = debug2.readVarInt();
/* 111 */       NonNullList<Ingredient> debug5 = NonNullList.withSize(debug4, Ingredient.EMPTY);
/* 112 */       for (int i = 0; i < debug5.size(); i++) {
/* 113 */         debug5.set(i, Ingredient.fromNetwork(debug2));
/*     */       }
/* 115 */       ItemStack debug6 = debug2.readItem();
/* 116 */       return new ShapelessRecipe(debug1, debug3, debug6, debug5);
/*     */     }
/*     */ 
/*     */     
/*     */     public void toNetwork(FriendlyByteBuf debug1, ShapelessRecipe debug2) {
/* 121 */       debug1.writeUtf(debug2.group);
/* 122 */       debug1.writeVarInt(debug2.ingredients.size());
/* 123 */       for (Ingredient debug4 : debug2.ingredients) {
/* 124 */         debug4.toNetwork(debug1);
/*     */       }
/* 126 */       debug1.writeItem(debug2.result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\ShapelessRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */