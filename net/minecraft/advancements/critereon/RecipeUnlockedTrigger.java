/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class RecipeUnlockedTrigger extends SimpleCriterionTrigger<RecipeUnlockedTrigger.TriggerInstance> {
/* 10 */   private static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 14 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 19 */     ResourceLocation debug4 = new ResourceLocation(GsonHelper.getAsString(debug1, "recipe"));
/* 20 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Recipe<?> debug2) {
/* 24 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static TriggerInstance unlocked(ResourceLocation debug0) {
/* 28 */     return new TriggerInstance(EntityPredicate.Composite.ANY, debug0);
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ResourceLocation recipe;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ResourceLocation debug2) {
/* 35 */       super(RecipeUnlockedTrigger.ID, debug1);
/* 36 */       this.recipe = debug2;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 41 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 42 */       debug2.addProperty("recipe", this.recipe.toString());
/* 43 */       return debug2;
/*    */     }
/*    */     
/*    */     public boolean matches(Recipe<?> debug1) {
/* 47 */       return this.recipe.equals(debug1.getId());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\RecipeUnlockedTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */