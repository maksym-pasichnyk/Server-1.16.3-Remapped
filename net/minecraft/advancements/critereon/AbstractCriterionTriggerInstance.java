/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public abstract class AbstractCriterionTriggerInstance implements CriterionTriggerInstance {
/*    */   private final ResourceLocation criterion;
/*    */   private final EntityPredicate.Composite player;
/*    */   
/*    */   public AbstractCriterionTriggerInstance(ResourceLocation debug1, EntityPredicate.Composite debug2) {
/* 12 */     this.criterion = debug1;
/* 13 */     this.player = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getCriterion() {
/* 18 */     return this.criterion;
/*    */   }
/*    */   
/*    */   protected EntityPredicate.Composite getPlayerPredicate() {
/* 22 */     return this.player;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonObject serializeToJson(SerializationContext debug1) {
/* 27 */     JsonObject debug2 = new JsonObject();
/* 28 */     debug2.add("player", this.player.toJson(debug1));
/* 29 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return "AbstractCriterionInstance{criterion=" + this.criterion + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\AbstractCriterionTriggerInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */