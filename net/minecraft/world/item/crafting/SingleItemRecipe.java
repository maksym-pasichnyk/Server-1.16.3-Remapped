/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public abstract class SingleItemRecipe
/*     */   implements Recipe<Container> {
/*     */   protected final Ingredient ingredient;
/*     */   protected final ItemStack result;
/*     */   private final RecipeType<?> type;
/*     */   
/*     */   public SingleItemRecipe(RecipeType<?> debug1, RecipeSerializer<?> debug2, ResourceLocation debug3, String debug4, Ingredient debug5, ItemStack debug6) {
/*  21 */     this.type = debug1;
/*  22 */     this.serializer = debug2;
/*  23 */     this.id = debug3;
/*  24 */     this.group = debug4;
/*  25 */     this.ingredient = debug5;
/*  26 */     this.result = debug6;
/*     */   }
/*     */   private final RecipeSerializer<?> serializer; protected final ResourceLocation id; protected final String group;
/*     */   
/*     */   public RecipeType<?> getType() {
/*  31 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/*  36 */     return this.serializer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getId() {
/*  41 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getResultItem() {
/*  51 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<Ingredient> getIngredients() {
/*  56 */     NonNullList<Ingredient> debug1 = NonNullList.create();
/*  57 */     debug1.add(this.ingredient);
/*  58 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack assemble(Container debug1) {
/*  68 */     return this.result.copy();
/*     */   }
/*     */   
/*     */   public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> {
/*     */     final SingleItemMaker<T> factory;
/*     */     
/*     */     protected Serializer(SingleItemMaker<T> debug1) {
/*  75 */       this.factory = debug1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T fromJson(ResourceLocation debug1, JsonObject debug2) {
/*     */       Ingredient debug4;
/*  84 */       String debug3 = GsonHelper.getAsString(debug2, "group", "");
/*     */ 
/*     */       
/*  87 */       if (GsonHelper.isArrayNode(debug2, "ingredient")) {
/*  88 */         debug4 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonArray(debug2, "ingredient"));
/*     */       } else {
/*  90 */         debug4 = Ingredient.fromJson((JsonElement)GsonHelper.getAsJsonObject(debug2, "ingredient"));
/*     */       } 
/*     */       
/*  93 */       String debug5 = GsonHelper.getAsString(debug2, "result");
/*  94 */       int debug6 = GsonHelper.getAsInt(debug2, "count");
/*  95 */       ItemStack debug7 = new ItemStack((ItemLike)Registry.ITEM.get(new ResourceLocation(debug5)), debug6);
/*     */       
/*  97 */       return this.factory.create(debug1, debug3, debug4, debug7);
/*     */     }
/*     */ 
/*     */     
/*     */     public T fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 102 */       String debug3 = debug2.readUtf(32767);
/* 103 */       Ingredient debug4 = Ingredient.fromNetwork(debug2);
/* 104 */       ItemStack debug5 = debug2.readItem();
/* 105 */       return this.factory.create(debug1, debug3, debug4, debug5);
/*     */     }
/*     */ 
/*     */     
/*     */     public void toNetwork(FriendlyByteBuf debug1, T debug2) {
/* 110 */       debug1.writeUtf(((SingleItemRecipe)debug2).group);
/* 111 */       ((SingleItemRecipe)debug2).ingredient.toNetwork(debug1);
/* 112 */       debug1.writeItem(((SingleItemRecipe)debug2).result);
/*     */     }
/*     */     
/*     */     static interface SingleItemMaker<T extends SingleItemRecipe> {
/*     */       T create(ResourceLocation param2ResourceLocation, String param2String, Ingredient param2Ingredient, ItemStack param2ItemStack);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\SingleItemRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */