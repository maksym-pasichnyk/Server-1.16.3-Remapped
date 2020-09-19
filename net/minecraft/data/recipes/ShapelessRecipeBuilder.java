/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.List;
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
/*     */ public class ShapelessRecipeBuilder {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Item result;
/*     */   private final int count;
/*  30 */   private final List<Ingredient> ingredients = Lists.newArrayList();
/*  31 */   private final Advancement.Builder advancement = Advancement.Builder.advancement();
/*     */   private String group;
/*     */   
/*     */   public ShapelessRecipeBuilder(ItemLike debug1, int debug2) {
/*  35 */     this.result = debug1.asItem();
/*  36 */     this.count = debug2;
/*     */   }
/*     */   
/*     */   public static ShapelessRecipeBuilder shapeless(ItemLike debug0) {
/*  40 */     return new ShapelessRecipeBuilder(debug0, 1);
/*     */   }
/*     */   
/*     */   public static ShapelessRecipeBuilder shapeless(ItemLike debug0, int debug1) {
/*  44 */     return new ShapelessRecipeBuilder(debug0, debug1);
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Tag<Item> debug1) {
/*  48 */     return requires(Ingredient.of(debug1));
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(ItemLike debug1) {
/*  52 */     return requires(debug1, 1);
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(ItemLike debug1, int debug2) {
/*  56 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  57 */       requires(Ingredient.of(new ItemLike[] { debug1 }));
/*     */     } 
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Ingredient debug1) {
/*  63 */     return requires(debug1, 1);
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Ingredient debug1, int debug2) {
/*  67 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  68 */       this.ingredients.add(debug1);
/*     */     }
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder unlockedBy(String debug1, CriterionTriggerInstance debug2) {
/*  74 */     this.advancement.addCriterion(debug1, debug2);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public ShapelessRecipeBuilder group(String debug1) {
/*  79 */     this.group = debug1;
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1) {
/*  84 */     save(debug1, Registry.ITEM.getKey(this.result));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, String debug2) {
/*  88 */     ResourceLocation debug3 = Registry.ITEM.getKey(this.result);
/*  89 */     if ((new ResourceLocation(debug2)).equals(debug3)) {
/*  90 */       throw new IllegalStateException("Shapeless Recipe " + debug2 + " should remove its 'save' argument");
/*     */     }
/*  92 */     save(debug1, new ResourceLocation(debug2));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, ResourceLocation debug2) {
/*  96 */     ensureValid(debug2);
/*  97 */     this.advancement
/*  98 */       .parent(new ResourceLocation("recipes/root"))
/*  99 */       .addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(debug2))
/* 100 */       .rewards(AdvancementRewards.Builder.recipe(debug2))
/* 101 */       .requirements(RequirementsStrategy.OR);
/* 102 */     debug1.accept(new Result(debug2, this.result, this.count, (this.group == null) ? "" : this.group, this.ingredients, this.advancement, new ResourceLocation(debug2.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + debug2.getPath())));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceLocation debug1) {
/* 106 */     if (this.advancement.getCriteria().isEmpty())
/* 107 */       throw new IllegalStateException("No way of obtaining recipe " + debug1); 
/*     */   }
/*     */   
/*     */   public static class Result
/*     */     implements FinishedRecipe {
/*     */     private final ResourceLocation id;
/*     */     private final Item result;
/*     */     private final int count;
/*     */     private final String group;
/*     */     private final List<Ingredient> ingredients;
/*     */     private final Advancement.Builder advancement;
/*     */     private final ResourceLocation advancementId;
/*     */     
/*     */     public Result(ResourceLocation debug1, Item debug2, int debug3, String debug4, List<Ingredient> debug5, Advancement.Builder debug6, ResourceLocation debug7) {
/* 121 */       this.id = debug1;
/* 122 */       this.result = debug2;
/* 123 */       this.count = debug3;
/* 124 */       this.group = debug4;
/* 125 */       this.ingredients = debug5;
/* 126 */       this.advancement = debug6;
/* 127 */       this.advancementId = debug7;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeRecipeData(JsonObject debug1) {
/* 132 */       if (!this.group.isEmpty()) {
/* 133 */         debug1.addProperty("group", this.group);
/*     */       }
/*     */       
/* 136 */       JsonArray debug2 = new JsonArray();
/* 137 */       for (Ingredient debug4 : this.ingredients) {
/* 138 */         debug2.add(debug4.toJson());
/*     */       }
/* 140 */       debug1.add("ingredients", (JsonElement)debug2);
/*     */       
/* 142 */       JsonObject debug3 = new JsonObject();
/* 143 */       debug3.addProperty("item", Registry.ITEM.getKey(this.result).toString());
/* 144 */       if (this.count > 1) {
/* 145 */         debug3.addProperty("count", Integer.valueOf(this.count));
/*     */       }
/* 147 */       debug1.add("result", (JsonElement)debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public RecipeSerializer<?> getType() {
/* 152 */       return RecipeSerializer.SHAPELESS_RECIPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getId() {
/* 157 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonObject serializeAdvancement() {
/* 163 */       return this.advancement.serializeToJson();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getAdvancementId() {
/* 169 */       return this.advancementId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\ShapelessRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */