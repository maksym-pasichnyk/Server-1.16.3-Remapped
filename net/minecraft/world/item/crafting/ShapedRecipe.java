/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapedRecipe
/*     */   implements CraftingRecipe
/*     */ {
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final NonNullList<Ingredient> recipeItems;
/*     */   
/*     */   public ShapedRecipe(ResourceLocation debug1, String debug2, int debug3, int debug4, NonNullList<Ingredient> debug5, ItemStack debug6) {
/*  33 */     this.id = debug1;
/*  34 */     this.group = debug2;
/*  35 */     this.width = debug3;
/*  36 */     this.height = debug4;
/*  37 */     this.recipeItems = debug5;
/*  38 */     this.result = debug6;
/*     */   }
/*     */   private final ItemStack result; private final ResourceLocation id; private final String group;
/*     */   
/*     */   public ResourceLocation getId() {
/*  43 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/*  48 */     return RecipeSerializer.SHAPED_RECIPE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getResultItem() {
/*  58 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public NonNullList<Ingredient> getIngredients() {
/*  63 */     return this.recipeItems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  73 */     for (int debug3 = 0; debug3 <= debug1.getWidth() - this.width; debug3++) {
/*  74 */       for (int debug4 = 0; debug4 <= debug1.getHeight() - this.height; debug4++) {
/*  75 */         if (matches(debug1, debug3, debug4, true)) {
/*  76 */           return true;
/*     */         }
/*  78 */         if (matches(debug1, debug3, debug4, false)) {
/*  79 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matches(CraftingContainer debug1, int debug2, int debug3, boolean debug4) {
/*  87 */     for (int debug5 = 0; debug5 < debug1.getWidth(); debug5++) {
/*  88 */       for (int debug6 = 0; debug6 < debug1.getHeight(); debug6++) {
/*  89 */         int debug7 = debug5 - debug2;
/*  90 */         int debug8 = debug6 - debug3;
/*  91 */         Ingredient debug9 = Ingredient.EMPTY;
/*  92 */         if (debug7 >= 0 && debug8 >= 0 && debug7 < this.width && debug8 < this.height) {
/*  93 */           if (debug4) {
/*  94 */             debug9 = (Ingredient)this.recipeItems.get(this.width - debug7 - 1 + debug8 * this.width);
/*     */           } else {
/*  96 */             debug9 = (Ingredient)this.recipeItems.get(debug7 + debug8 * this.width);
/*     */           } 
/*     */         }
/*  99 */         if (!debug9.test(debug1.getItem(debug5 + debug6 * debug1.getWidth()))) {
/* 100 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/* 109 */     return getResultItem().copy();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 113 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 117 */     return this.height;
/*     */   }
/*     */   
/*     */   private static NonNullList<Ingredient> dissolvePattern(String[] debug0, Map<String, Ingredient> debug1, int debug2, int debug3) {
/* 121 */     NonNullList<Ingredient> debug4 = NonNullList.withSize(debug2 * debug3, Ingredient.EMPTY);
/* 122 */     Set<String> debug5 = Sets.newHashSet(debug1.keySet());
/* 123 */     debug5.remove(" ");
/*     */     
/* 125 */     for (int debug6 = 0; debug6 < debug0.length; debug6++) {
/* 126 */       for (int debug7 = 0; debug7 < debug0[debug6].length(); debug7++) {
/* 127 */         String debug8 = debug0[debug6].substring(debug7, debug7 + 1);
/* 128 */         Ingredient debug9 = debug1.get(debug8);
/* 129 */         if (debug9 == null) {
/* 130 */           throw new JsonSyntaxException("Pattern references symbol '" + debug8 + "' but it's not defined in the key");
/*     */         }
/* 132 */         debug5.remove(debug8);
/* 133 */         debug4.set(debug7 + debug2 * debug6, debug9);
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (!debug5.isEmpty()) {
/* 138 */       throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + debug5);
/*     */     }
/*     */     
/* 141 */     return debug4;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String[] shrink(String... debug0) {
/* 146 */     int debug1 = Integer.MAX_VALUE;
/* 147 */     int debug2 = 0;
/* 148 */     int debug3 = 0;
/* 149 */     int debug4 = 0;
/*     */     
/* 151 */     for (int i = 0; i < debug0.length; i++) {
/* 152 */       String str = debug0[i];
/*     */       
/* 154 */       debug1 = Math.min(debug1, firstNonSpace(str));
/* 155 */       int debug7 = lastNonSpace(str);
/* 156 */       debug2 = Math.max(debug2, debug7);
/*     */ 
/*     */       
/* 159 */       if (debug7 < 0) {
/* 160 */         if (debug3 == i) {
/* 161 */           debug3++;
/*     */         }
/* 163 */         debug4++;
/*     */       } else {
/* 165 */         debug4 = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     if (debug0.length == debug4) {
/* 170 */       return new String[0];
/*     */     }
/* 172 */     String[] debug5 = new String[debug0.length - debug4 - debug3];
/* 173 */     for (int debug6 = 0; debug6 < debug5.length; debug6++) {
/* 174 */       debug5[debug6] = debug0[debug6 + debug3].substring(debug1, debug2 + 1);
/*     */     }
/*     */     
/* 177 */     return debug5;
/*     */   }
/*     */   
/*     */   private static int firstNonSpace(String debug0) {
/* 181 */     int debug1 = 0;
/* 182 */     while (debug1 < debug0.length() && debug0.charAt(debug1) == ' ') {
/* 183 */       debug1++;
/*     */     }
/*     */     
/* 186 */     return debug1;
/*     */   }
/*     */   
/*     */   private static int lastNonSpace(String debug0) {
/* 190 */     int debug1 = debug0.length() - 1;
/* 191 */     while (debug1 >= 0 && debug0.charAt(debug1) == ' ') {
/* 192 */       debug1--;
/*     */     }
/*     */     
/* 195 */     return debug1;
/*     */   }
/*     */   
/*     */   private static String[] patternFromJson(JsonArray debug0) {
/* 199 */     String[] debug1 = new String[debug0.size()];
/* 200 */     if (debug1.length > 3)
/* 201 */       throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum"); 
/* 202 */     if (debug1.length == 0) {
/* 203 */       throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
/*     */     }
/* 205 */     for (int debug2 = 0; debug2 < debug1.length; debug2++) {
/* 206 */       String debug3 = GsonHelper.convertToString(debug0.get(debug2), "pattern[" + debug2 + "]");
/* 207 */       if (debug3.length() > 3)
/* 208 */         throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum"); 
/* 209 */       if (debug2 > 0 && debug1[0].length() != debug3.length()) {
/* 210 */         throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
/*     */       }
/* 212 */       debug1[debug2] = debug3;
/*     */     } 
/* 214 */     return debug1;
/*     */   }
/*     */   
/*     */   private static Map<String, Ingredient> keyFromJson(JsonObject debug0) {
/* 218 */     Map<String, Ingredient> debug1 = Maps.newHashMap();
/* 219 */     for (Map.Entry<String, JsonElement> debug3 : (Iterable<Map.Entry<String, JsonElement>>)debug0.entrySet()) {
/* 220 */       if (((String)debug3.getKey()).length() != 1)
/* 221 */         throw new JsonSyntaxException("Invalid key entry: '" + (String)debug3.getKey() + "' is an invalid symbol (must be 1 character only)."); 
/* 222 */       if (" ".equals(debug3.getKey())) {
/* 223 */         throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
/*     */       }
/* 225 */       debug1.put(debug3.getKey(), Ingredient.fromJson(debug3.getValue()));
/*     */     } 
/* 227 */     debug1.put(" ", Ingredient.EMPTY);
/* 228 */     return debug1;
/*     */   }
/*     */   
/*     */   public static ItemStack itemFromJson(JsonObject debug0) {
/* 232 */     String debug1 = GsonHelper.getAsString(debug0, "item");
/* 233 */     Item debug2 = (Item)Registry.ITEM.getOptional(new ResourceLocation(debug1)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + debug0 + "'"));
/* 234 */     if (debug0.has("data")) {
/* 235 */       throw new JsonParseException("Disallowed data tag found");
/*     */     }
/* 237 */     int debug3 = GsonHelper.getAsInt(debug0, "count", 1);
/* 238 */     return new ItemStack((ItemLike)debug2, debug3);
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     implements RecipeSerializer<ShapedRecipe> {
/*     */     public ShapedRecipe fromJson(ResourceLocation debug1, JsonObject debug2) {
/* 244 */       String debug3 = GsonHelper.getAsString(debug2, "group", "");
/*     */       
/* 246 */       Map<String, Ingredient> debug4 = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(debug2, "key"));
/* 247 */       String[] debug5 = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(debug2, "pattern")));
/*     */       
/* 249 */       int debug6 = debug5[0].length();
/* 250 */       int debug7 = debug5.length;
/*     */       
/* 252 */       NonNullList<Ingredient> debug8 = ShapedRecipe.dissolvePattern(debug5, debug4, debug6, debug7);
/*     */       
/* 254 */       ItemStack debug9 = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(debug2, "result"));
/*     */       
/* 256 */       return new ShapedRecipe(debug1, debug3, debug6, debug7, debug8, debug9);
/*     */     }
/*     */ 
/*     */     
/*     */     public ShapedRecipe fromNetwork(ResourceLocation debug1, FriendlyByteBuf debug2) {
/* 261 */       int debug3 = debug2.readVarInt();
/* 262 */       int debug4 = debug2.readVarInt();
/* 263 */       String debug5 = debug2.readUtf(32767);
/* 264 */       NonNullList<Ingredient> debug6 = NonNullList.withSize(debug3 * debug4, Ingredient.EMPTY);
/* 265 */       for (int i = 0; i < debug6.size(); i++) {
/* 266 */         debug6.set(i, Ingredient.fromNetwork(debug2));
/*     */       }
/* 268 */       ItemStack debug7 = debug2.readItem();
/* 269 */       return new ShapedRecipe(debug1, debug5, debug3, debug4, debug6, debug7);
/*     */     }
/*     */ 
/*     */     
/*     */     public void toNetwork(FriendlyByteBuf debug1, ShapedRecipe debug2) {
/* 274 */       debug1.writeVarInt(debug2.width);
/* 275 */       debug1.writeVarInt(debug2.height);
/* 276 */       debug1.writeUtf(debug2.group);
/* 277 */       for (Ingredient debug4 : debug2.recipeItems) {
/* 278 */         debug4.toNetwork(debug1);
/*     */       }
/* 280 */       debug1.writeItem(debug2.result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\ShapedRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */