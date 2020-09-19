/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ 
/*    */ public class EntityHurtPlayerTrigger extends SimpleCriterionTrigger<EntityHurtPlayerTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("entity_hurt_player");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     DamagePredicate debug4 = DamagePredicate.fromJson(debug1.get("damage"));
/* 19 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, DamageSource debug2, float debug3, float debug4, boolean debug5) {
/* 23 */     trigger(debug1, debug5 -> debug5.matches(debug0, debug1, debug2, debug3, debug4));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final DamagePredicate damage;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, DamagePredicate debug2) {
/* 30 */       super(EntityHurtPlayerTrigger.ID, debug1);
/* 31 */       this.damage = debug2;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance entityHurtPlayer(DamagePredicate.Builder debug0) {
/* 43 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0.build());
/*    */     }
/*    */     
/*    */     public boolean matches(ServerPlayer debug1, DamageSource debug2, float debug3, float debug4, boolean debug5) {
/* 47 */       if (!this.damage.matches(debug1, debug2, debug3, debug4, debug5)) {
/* 48 */         return false;
/*    */       }
/* 50 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 55 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 57 */       debug2.add("damage", this.damage.serializeToJson());
/*    */       
/* 59 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EntityHurtPlayerTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */