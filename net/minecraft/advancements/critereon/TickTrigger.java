/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class TickTrigger extends SimpleCriterionTrigger<TickTrigger.TriggerInstance> {
/*  8 */   public static final ResourceLocation ID = new ResourceLocation("tick");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 12 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 17 */     return new TriggerInstance(debug2);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1) {
/* 21 */     trigger(debug1, debug0 -> true);
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     public TriggerInstance(EntityPredicate.Composite debug1) {
/* 26 */       super(TickTrigger.ID, debug1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\TickTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */