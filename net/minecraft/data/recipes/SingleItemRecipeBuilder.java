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
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class SingleItemRecipeBuilder
/*     */ {
/*     */   private final Item result;
/*     */   private final Ingredient ingredient;
/*     */   private final int count;
/*  23 */   private final Advancement.Builder advancement = Advancement.Builder.advancement();
/*     */   private String group;
/*     */   private final RecipeSerializer<?> type;
/*     */   
/*     */   public SingleItemRecipeBuilder(RecipeSerializer<?> debug1, Ingredient debug2, ItemLike debug3, int debug4) {
/*  28 */     this.type = debug1;
/*  29 */     this.result = debug3.asItem();
/*  30 */     this.ingredient = debug2;
/*  31 */     this.count = debug4;
/*     */   }
/*     */   
/*     */   public static SingleItemRecipeBuilder stonecutting(Ingredient debug0, ItemLike debug1) {
/*  35 */     return new SingleItemRecipeBuilder(RecipeSerializer.STONECUTTER, debug0, debug1, 1);
/*     */   }
/*     */   
/*     */   public static SingleItemRecipeBuilder stonecutting(Ingredient debug0, ItemLike debug1, int debug2) {
/*  39 */     return new SingleItemRecipeBuilder(RecipeSerializer.STONECUTTER, debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public SingleItemRecipeBuilder unlocks(String debug1, CriterionTriggerInstance debug2) {
/*  43 */     this.advancement.addCriterion(debug1, debug2);
/*  44 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, String debug2) {
/*  53 */     ResourceLocation debug3 = Registry.ITEM.getKey(this.result);
/*  54 */     if ((new ResourceLocation(debug2)).equals(debug3)) {
/*  55 */       throw new IllegalStateException("Single Item Recipe " + debug2 + " should remove its 'save' argument");
/*     */     }
/*  57 */     save(debug1, new ResourceLocation(debug2));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, ResourceLocation debug2) {
/*  61 */     ensureValid(debug2);
/*  62 */     this.advancement
/*  63 */       .parent(new ResourceLocation("recipes/root"))
/*  64 */       .addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(debug2))
/*  65 */       .rewards(AdvancementRewards.Builder.recipe(debug2))
/*  66 */       .requirements(RequirementsStrategy.OR);
/*     */     
/*  68 */     debug1.accept(new Result(debug2, this.type, (this.group == null) ? "" : this.group, this.ingredient, this.result, this.count, this.advancement, new ResourceLocation(debug2.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + debug2.getPath())));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceLocation debug1) {
/*  72 */     if (this.advancement.getCriteria().isEmpty())
/*  73 */       throw new IllegalStateException("No way of obtaining recipe " + debug1); 
/*     */   }
/*     */   
/*     */   public static class Result
/*     */     implements FinishedRecipe {
/*     */     private final ResourceLocation id;
/*     */     private final String group;
/*     */     private final Ingredient ingredient;
/*     */     private final Item result;
/*     */     private final int count;
/*     */     private final Advancement.Builder advancement;
/*     */     private final ResourceLocation advancementId;
/*     */     private final RecipeSerializer<?> type;
/*     */     
/*     */     public Result(ResourceLocation debug1, RecipeSerializer<?> debug2, String debug3, Ingredient debug4, Item debug5, int debug6, Advancement.Builder debug7, ResourceLocation debug8) {
/*  88 */       this.id = debug1;
/*  89 */       this.type = debug2;
/*  90 */       this.group = debug3;
/*  91 */       this.ingredient = debug4;
/*  92 */       this.result = debug5;
/*  93 */       this.count = debug6;
/*  94 */       this.advancement = debug7;
/*  95 */       this.advancementId = debug8;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeRecipeData(JsonObject debug1) {
/* 100 */       if (!this.group.isEmpty()) {
/* 101 */         debug1.addProperty("group", this.group);
/*     */       }
/*     */       
/* 104 */       debug1.add("ingredient", this.ingredient.toJson());
/* 105 */       debug1.addProperty("result", Registry.ITEM.getKey(this.result).toString());
/* 106 */       debug1.addProperty("count", Integer.valueOf(this.count));
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getId() {
/* 111 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecipeSerializer<?> getType() {
/* 116 */       return this.type;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonObject serializeAdvancement() {
/* 122 */       return this.advancement.serializeToJson();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getAdvancementId() {
/* 128 */       return this.advancementId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\SingleItemRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */