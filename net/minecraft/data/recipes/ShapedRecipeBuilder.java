/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.RequirementsStrategy;
/*     */ import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ShapedRecipeBuilder {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Item result;
/*     */   private final int count;
/*  34 */   private final List<String> rows = Lists.newArrayList();
/*  35 */   private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
/*  36 */   private final Advancement.Builder advancement = Advancement.Builder.advancement();
/*     */   private String group;
/*     */   
/*     */   public ShapedRecipeBuilder(ItemLike debug1, int debug2) {
/*  40 */     this.result = debug1.asItem();
/*  41 */     this.count = debug2;
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(ItemLike debug0) {
/*  45 */     return shaped(debug0, 1);
/*     */   }
/*     */   
/*     */   public static ShapedRecipeBuilder shaped(ItemLike debug0, int debug1) {
/*  49 */     return new ShapedRecipeBuilder(debug0, debug1);
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character debug1, Tag<Item> debug2) {
/*  53 */     return define(debug1, Ingredient.of(debug2));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character debug1, ItemLike debug2) {
/*  57 */     return define(debug1, Ingredient.of(new ItemLike[] { debug2 }));
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder define(Character debug1, Ingredient debug2) {
/*  61 */     if (this.key.containsKey(debug1)) {
/*  62 */       throw new IllegalArgumentException("Symbol '" + debug1 + "' is already defined!");
/*     */     }
/*  64 */     if (debug1.charValue() == ' ') {
/*  65 */       throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
/*     */     }
/*  67 */     this.key.put(debug1, debug2);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder pattern(String debug1) {
/*  72 */     if (!this.rows.isEmpty() && debug1.length() != ((String)this.rows.get(0)).length()) {
/*  73 */       throw new IllegalArgumentException("Pattern must be the same width on every line!");
/*     */     }
/*  75 */     this.rows.add(debug1);
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder unlockedBy(String debug1, CriterionTriggerInstance debug2) {
/*  80 */     this.advancement.addCriterion(debug1, debug2);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public ShapedRecipeBuilder group(String debug1) {
/*  85 */     this.group = debug1;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1) {
/*  90 */     save(debug1, Registry.ITEM.getKey(this.result));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, String debug2) {
/*  94 */     ResourceLocation debug3 = Registry.ITEM.getKey(this.result);
/*  95 */     if ((new ResourceLocation(debug2)).equals(debug3)) {
/*  96 */       throw new IllegalStateException("Shaped Recipe " + debug2 + " should remove its 'save' argument");
/*     */     }
/*  98 */     save(debug1, new ResourceLocation(debug2));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, ResourceLocation debug2) {
/* 102 */     ensureValid(debug2);
/* 103 */     this.advancement
/* 104 */       .parent(new ResourceLocation("recipes/root"))
/* 105 */       .addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(debug2))
/* 106 */       .rewards(AdvancementRewards.Builder.recipe(debug2))
/* 107 */       .requirements(RequirementsStrategy.OR);
/* 108 */     debug1.accept(new Result(debug2, this.result, this.count, (this.group == null) ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(debug2.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + debug2.getPath())));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceLocation debug1) {
/* 112 */     if (this.rows.isEmpty()) {
/* 113 */       throw new IllegalStateException("No pattern is defined for shaped recipe " + debug1 + "!");
/*     */     }
/*     */     
/* 116 */     Set<Character> debug2 = Sets.newHashSet(this.key.keySet());
/* 117 */     debug2.remove(Character.valueOf(' '));
/*     */     
/* 119 */     for (String debug4 : this.rows) {
/* 120 */       for (int debug5 = 0; debug5 < debug4.length(); debug5++) {
/* 121 */         char debug6 = debug4.charAt(debug5);
/* 122 */         if (!this.key.containsKey(Character.valueOf(debug6)) && debug6 != ' ') {
/* 123 */           throw new IllegalStateException("Pattern in recipe " + debug1 + " uses undefined symbol '" + debug6 + "'");
/*     */         }
/* 125 */         debug2.remove(Character.valueOf(debug6));
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     if (!debug2.isEmpty()) {
/* 130 */       throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + debug1);
/*     */     }
/*     */     
/* 133 */     if (this.rows.size() == 1 && ((String)this.rows.get(0)).length() == 1) {
/* 134 */       throw new IllegalStateException("Shaped recipe " + debug1 + " only takes in a single item - should it be a shapeless recipe instead?");
/*     */     }
/*     */     
/* 137 */     if (this.advancement.getCriteria().isEmpty())
/* 138 */       throw new IllegalStateException("No way of obtaining recipe " + debug1); 
/*     */   }
/*     */   
/*     */   class Result
/*     */     implements FinishedRecipe {
/*     */     private final ResourceLocation id;
/*     */     private final Item result;
/*     */     private final int count;
/*     */     private final String group;
/*     */     private final List<String> pattern;
/*     */     private final Map<Character, Ingredient> key;
/*     */     private final Advancement.Builder advancement;
/*     */     private final ResourceLocation advancementId;
/*     */     
/*     */     public Result(ResourceLocation debug2, Item debug3, int debug4, String debug5, List<String> debug6, Map<Character, Ingredient> debug7, Advancement.Builder debug8, ResourceLocation debug9) {
/* 153 */       this.id = debug2;
/* 154 */       this.result = debug3;
/* 155 */       this.count = debug4;
/* 156 */       this.group = debug5;
/* 157 */       this.pattern = debug6;
/* 158 */       this.key = debug7;
/* 159 */       this.advancement = debug8;
/* 160 */       this.advancementId = debug9;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeRecipeData(JsonObject debug1) {
/* 165 */       if (!this.group.isEmpty()) {
/* 166 */         debug1.addProperty("group", this.group);
/*     */       }
/*     */       
/* 169 */       JsonArray debug2 = new JsonArray();
/* 170 */       for (String str : this.pattern) {
/* 171 */         debug2.add(str);
/*     */       }
/* 173 */       debug1.add("pattern", (JsonElement)debug2);
/*     */       
/* 175 */       JsonObject debug3 = new JsonObject();
/* 176 */       for (Map.Entry<Character, Ingredient> debug5 : this.key.entrySet()) {
/* 177 */         debug3.add(String.valueOf(debug5.getKey()), ((Ingredient)debug5.getValue()).toJson());
/*     */       }
/* 179 */       debug1.add("key", (JsonElement)debug3);
/*     */       
/* 181 */       JsonObject debug4 = new JsonObject();
/* 182 */       debug4.addProperty("item", Registry.ITEM.getKey(this.result).toString());
/* 183 */       if (this.count > 1) {
/* 184 */         debug4.addProperty("count", Integer.valueOf(this.count));
/*     */       }
/* 186 */       debug1.add("result", (JsonElement)debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public RecipeSerializer<?> getType() {
/* 191 */       return RecipeSerializer.SHAPED_RECIPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getId() {
/* 196 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonObject serializeAdvancement() {
/* 202 */       return this.advancement.serializeToJson();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getAdvancementId() {
/* 208 */       return this.advancementId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\ShapedRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */