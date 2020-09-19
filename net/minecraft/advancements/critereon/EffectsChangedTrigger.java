/*    */ package net.minecraft.advancements.critereon;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class EffectsChangedTrigger extends SimpleCriterionTrigger<EffectsChangedTrigger.TriggerInstance> {
/*  8 */   private static final ResourceLocation ID = new ResourceLocation("effects_changed");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 12 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 17 */     MobEffectsPredicate debug4 = MobEffectsPredicate.fromJson(debug1.get("effects"));
/* 18 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1) {
/* 22 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final MobEffectsPredicate effects;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, MobEffectsPredicate debug2) {
/* 29 */       super(EffectsChangedTrigger.ID, debug1);
/* 30 */       this.effects = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance hasEffects(MobEffectsPredicate debug0) {
/* 34 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(ServerPlayer debug1) {
/* 38 */       return this.effects.matches((LivingEntity)debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 43 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 45 */       debug2.add("effects", this.effects.serializeToJson());
/*    */       
/* 47 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EffectsChangedTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */