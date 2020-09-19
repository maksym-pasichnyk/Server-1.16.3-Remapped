/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class PlayerHurtEntityTrigger extends SimpleCriterionTrigger<PlayerHurtEntityTrigger.TriggerInstance> {
/* 11 */   private static final ResourceLocation ID = new ResourceLocation("player_hurt_entity");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 20 */     DamagePredicate debug4 = DamagePredicate.fromJson(debug1.get("damage"));
/* 21 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "entity", debug3);
/* 22 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Entity debug2, DamageSource debug3, float debug4, float debug5, boolean debug6) {
/* 26 */     LootContext debug7 = EntityPredicate.createContext(debug1, debug2);
/* 27 */     trigger(debug1, debug6 -> debug6.matches(debug0, debug1, debug2, debug3, debug4, debug5));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final DamagePredicate damage;
/*    */     private final EntityPredicate.Composite entity;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, DamagePredicate debug2, EntityPredicate.Composite debug3) {
/* 35 */       super(PlayerHurtEntityTrigger.ID, debug1);
/* 36 */       this.damage = debug2;
/* 37 */       this.entity = debug3;
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
/*    */     public static TriggerInstance playerHurtEntity(DamagePredicate.Builder debug0) {
/* 49 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0.build(), EntityPredicate.Composite.ANY);
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
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean matches(ServerPlayer debug1, LootContext debug2, DamageSource debug3, float debug4, float debug5, boolean debug6) {
/* 65 */       if (!this.damage.matches(debug1, debug3, debug4, debug5, debug6)) {
/* 66 */         return false;
/*    */       }
/* 68 */       if (!this.entity.matches(debug2)) {
/* 69 */         return false;
/*    */       }
/* 71 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 76 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 78 */       debug2.add("damage", this.damage.serializeToJson());
/* 79 */       debug2.add("entity", this.entity.toJson(debug1));
/*    */       
/* 81 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\PlayerHurtEntityTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */