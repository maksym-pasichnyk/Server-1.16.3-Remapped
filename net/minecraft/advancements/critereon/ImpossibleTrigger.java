/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.advancements.CriterionTrigger;
/*    */ import net.minecraft.advancements.CriterionTriggerInstance;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.PlayerAdvancements;
/*    */ 
/*    */ public class ImpossibleTrigger implements CriterionTrigger<ImpossibleTrigger.TriggerInstance> {
/* 10 */   private static final ResourceLocation ID = new ResourceLocation("impossible");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 14 */     return ID;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addPlayerListener(PlayerAdvancements debug1, CriterionTrigger.Listener<TriggerInstance> debug2) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removePlayerListener(PlayerAdvancements debug1, CriterionTrigger.Listener<TriggerInstance> debug2) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void removePlayerListeners(PlayerAdvancements debug1) {}
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, DeserializationContext debug2) {
/* 31 */     return new TriggerInstance();
/*    */   }
/*    */   
/*    */   public static class TriggerInstance
/*    */     implements CriterionTriggerInstance {
/*    */     public ResourceLocation getCriterion() {
/* 37 */       return ImpossibleTrigger.ID;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 42 */       return new JsonObject();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ImpossibleTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */