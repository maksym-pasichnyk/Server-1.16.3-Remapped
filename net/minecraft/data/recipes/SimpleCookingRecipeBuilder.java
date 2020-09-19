/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.RequirementsStrategy;
/*     */ import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.crafting.SimpleCookingSerializer;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class SimpleCookingRecipeBuilder
/*     */ {
/*     */   private final Item result;
/*     */   private final Ingredient ingredient;
/*     */   private final float experience;
/*     */   private final int cookingTime;
/*  26 */   private final Advancement.Builder advancement = Advancement.Builder.advancement();
/*     */   private String group;
/*     */   private final SimpleCookingSerializer<?> serializer;
/*     */   
/*     */   private SimpleCookingRecipeBuilder(ItemLike debug1, Ingredient debug2, float debug3, int debug4, SimpleCookingSerializer<?> debug5) {
/*  31 */     this.result = debug1.asItem();
/*  32 */     this.ingredient = debug2;
/*  33 */     this.experience = debug3;
/*  34 */     this.cookingTime = debug4;
/*  35 */     this.serializer = debug5;
/*     */   }
/*     */   
/*     */   public static SimpleCookingRecipeBuilder cooking(Ingredient debug0, ItemLike debug1, float debug2, int debug3, SimpleCookingSerializer<?> debug4) {
/*  39 */     return new SimpleCookingRecipeBuilder(debug1, debug0, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleCookingRecipeBuilder blasting(Ingredient debug0, ItemLike debug1, float debug2, int debug3) {
/*  47 */     return cooking(debug0, debug1, debug2, debug3, RecipeSerializer.BLASTING_RECIPE);
/*     */   }
/*     */   
/*     */   public static SimpleCookingRecipeBuilder smelting(Ingredient debug0, ItemLike debug1, float debug2, int debug3) {
/*  51 */     return cooking(debug0, debug1, debug2, debug3, RecipeSerializer.SMELTING_RECIPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCookingRecipeBuilder unlockedBy(String debug1, CriterionTriggerInstance debug2) {
/*  59 */     this.advancement.addCriterion(debug1, debug2);
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1) {
/*  69 */     save(debug1, Registry.ITEM.getKey(this.result));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, String debug2) {
/*  73 */     ResourceLocation debug3 = Registry.ITEM.getKey(this.result);
/*  74 */     ResourceLocation debug4 = new ResourceLocation(debug2);
/*  75 */     if (debug4.equals(debug3)) {
/*  76 */       throw new IllegalStateException("Recipe " + debug4 + " should remove its 'save' argument");
/*     */     }
/*     */     
/*  79 */     save(debug1, debug4);
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, ResourceLocation debug2) {
/*  83 */     ensureValid(debug2);
/*  84 */     this.advancement
/*  85 */       .parent(new ResourceLocation("recipes/root"))
/*  86 */       .addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(debug2))
/*  87 */       .rewards(AdvancementRewards.Builder.recipe(debug2))
/*  88 */       .requirements(RequirementsStrategy.OR);
/*     */     
/*  90 */     debug1.accept(new Result(debug2, (this.group == null) ? "" : this.group, this.ingredient, this.result, this.experience, this.cookingTime, this.advancement, new ResourceLocation(debug2.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + debug2.getPath()), (RecipeSerializer)this.serializer));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceLocation debug1) {
/*  94 */     if (this.advancement.getCriteria().isEmpty())
/*  95 */       throw new IllegalStateException("No way of obtaining recipe " + debug1); 
/*     */   }
/*     */   
/*     */   public static class Result
/*     */     implements FinishedRecipe {
/*     */     private final ResourceLocation id;
/*     */     private final String group;
/*     */     private final Ingredient ingredient;
/*     */     private final Item result;
/*     */     private final float experience;
/*     */     private final int cookingTime;
/*     */     private final Advancement.Builder advancement;
/*     */     private final ResourceLocation advancementId;
/*     */     private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;
/*     */     
/*     */     public Result(ResourceLocation debug1, String debug2, Ingredient debug3, Item debug4, float debug5, int debug6, Advancement.Builder debug7, ResourceLocation debug8, RecipeSerializer<? extends AbstractCookingRecipe> debug9) {
/* 111 */       this.id = debug1;
/* 112 */       this.group = debug2;
/* 113 */       this.ingredient = debug3;
/* 114 */       this.result = debug4;
/* 115 */       this.experience = debug5;
/* 116 */       this.cookingTime = debug6;
/* 117 */       this.advancement = debug7;
/* 118 */       this.advancementId = debug8;
/* 119 */       this.serializer = debug9;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeRecipeData(JsonObject debug1) {
/* 124 */       if (!this.group.isEmpty()) {
/* 125 */         debug1.addProperty("group", this.group);
/*     */       }
/*     */       
/* 128 */       debug1.add("ingredient", this.ingredient.toJson());
/* 129 */       debug1.addProperty("result", Registry.ITEM.getKey(this.result).toString());
/* 130 */       debug1.addProperty("experience", Float.valueOf(this.experience));
/* 131 */       debug1.addProperty("cookingtime", Integer.valueOf(this.cookingTime));
/*     */     }
/*     */ 
/*     */     
/*     */     public RecipeSerializer<?> getType() {
/* 136 */       return this.serializer;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getId() {
/* 141 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonObject serializeAdvancement() {
/* 147 */       return this.advancement.serializeToJson();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getAdvancementId() {
/* 153 */       return this.advancementId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\SimpleCookingRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */