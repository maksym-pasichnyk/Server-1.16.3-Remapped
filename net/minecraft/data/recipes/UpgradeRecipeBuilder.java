/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.gson.JsonElement;
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
/*     */ 
/*     */ public class UpgradeRecipeBuilder {
/*     */   private final Ingredient base;
/*     */   private final Ingredient addition;
/*     */   private final Item result;
/*  22 */   private final Advancement.Builder advancement = Advancement.Builder.advancement();
/*     */   private final RecipeSerializer<?> type;
/*     */   
/*     */   public UpgradeRecipeBuilder(RecipeSerializer<?> debug1, Ingredient debug2, Ingredient debug3, Item debug4) {
/*  26 */     this.type = debug1;
/*  27 */     this.base = debug2;
/*  28 */     this.addition = debug3;
/*  29 */     this.result = debug4;
/*     */   }
/*     */   
/*     */   public static UpgradeRecipeBuilder smithing(Ingredient debug0, Ingredient debug1, Item debug2) {
/*  33 */     return new UpgradeRecipeBuilder(RecipeSerializer.SMITHING, debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public UpgradeRecipeBuilder unlocks(String debug1, CriterionTriggerInstance debug2) {
/*  37 */     this.advancement.addCriterion(debug1, debug2);
/*  38 */     return this;
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, String debug2) {
/*  42 */     save(debug1, new ResourceLocation(debug2));
/*     */   }
/*     */   
/*     */   public void save(Consumer<FinishedRecipe> debug1, ResourceLocation debug2) {
/*  46 */     ensureValid(debug2);
/*  47 */     this.advancement
/*  48 */       .parent(new ResourceLocation("recipes/root"))
/*  49 */       .addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(debug2))
/*  50 */       .rewards(AdvancementRewards.Builder.recipe(debug2))
/*  51 */       .requirements(RequirementsStrategy.OR);
/*     */     
/*  53 */     debug1.accept(new Result(debug2, this.type, this.base, this.addition, this.result, this.advancement, new ResourceLocation(debug2.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + debug2.getPath())));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceLocation debug1) {
/*  57 */     if (this.advancement.getCriteria().isEmpty())
/*  58 */       throw new IllegalStateException("No way of obtaining recipe " + debug1); 
/*     */   }
/*     */   
/*     */   public static class Result
/*     */     implements FinishedRecipe {
/*     */     private final ResourceLocation id;
/*     */     private final Ingredient base;
/*     */     private final Ingredient addition;
/*     */     private final Item result;
/*     */     private final Advancement.Builder advancement;
/*     */     private final ResourceLocation advancementId;
/*     */     private final RecipeSerializer<?> type;
/*     */     
/*     */     public Result(ResourceLocation debug1, RecipeSerializer<?> debug2, Ingredient debug3, Ingredient debug4, Item debug5, Advancement.Builder debug6, ResourceLocation debug7) {
/*  72 */       this.id = debug1;
/*  73 */       this.type = debug2;
/*  74 */       this.base = debug3;
/*  75 */       this.addition = debug4;
/*  76 */       this.result = debug5;
/*  77 */       this.advancement = debug6;
/*  78 */       this.advancementId = debug7;
/*     */     }
/*     */ 
/*     */     
/*     */     public void serializeRecipeData(JsonObject debug1) {
/*  83 */       debug1.add("base", this.base.toJson());
/*  84 */       debug1.add("addition", this.addition.toJson());
/*  85 */       JsonObject debug2 = new JsonObject();
/*  86 */       debug2.addProperty("item", Registry.ITEM.getKey(this.result).toString());
/*  87 */       debug1.add("result", (JsonElement)debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLocation getId() {
/*  92 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecipeSerializer<?> getType() {
/*  97 */       return this.type;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public JsonObject serializeAdvancement() {
/* 103 */       return this.advancement.serializeToJson();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getAdvancementId() {
/* 109 */       return this.advancementId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\recipes\UpgradeRecipeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */