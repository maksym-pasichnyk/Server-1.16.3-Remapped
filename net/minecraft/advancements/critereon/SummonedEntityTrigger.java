/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class SummonedEntityTrigger extends SimpleCriterionTrigger<SummonedEntityTrigger.TriggerInstance> {
/* 10 */   private static final ResourceLocation ID = new ResourceLocation("summoned_entity");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 14 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 19 */     EntityPredicate.Composite debug4 = EntityPredicate.Composite.fromJson(debug1, "entity", debug3);
/* 20 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Entity debug2) {
/* 24 */     LootContext debug3 = EntityPredicate.createContext(debug1, debug2);
/* 25 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite entity;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite debug2) {
/* 32 */       super(SummonedEntityTrigger.ID, debug1);
/* 33 */       this.entity = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance summonedEntity(EntityPredicate.Builder debug0) {
/* 37 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(debug0.build()));
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext debug1) {
/* 41 */       return this.entity.matches(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 46 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 48 */       debug2.add("entity", this.entity.toJson(debug1));
/*    */       
/* 50 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\SummonedEntityTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */