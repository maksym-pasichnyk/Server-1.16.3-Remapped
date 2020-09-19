/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TargetBlockTrigger extends SimpleCriterionTrigger<TargetBlockTrigger.TriggerInstance> {
/* 11 */   private static final ResourceLocation ID = new ResourceLocation("target_hit");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 20 */     MinMaxBounds.Ints debug4 = MinMaxBounds.Ints.fromJson(debug1.get("signal_strength"));
/* 21 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "projectile", debug3);
/* 22 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Entity debug2, Vec3 debug3, int debug4) {
/* 26 */     LootContext debug5 = EntityPredicate.createContext(debug1, debug2);
/* 27 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final MinMaxBounds.Ints signalStrength;
/*    */     private final EntityPredicate.Composite projectile;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, MinMaxBounds.Ints debug2, EntityPredicate.Composite debug3) {
/* 35 */       super(TargetBlockTrigger.ID, debug1);
/* 36 */       this.signalStrength = debug2;
/* 37 */       this.projectile = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance targetHit(MinMaxBounds.Ints debug0, EntityPredicate.Composite debug1) {
/* 41 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 46 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 47 */       debug2.add("signal_strength", this.signalStrength.serializeToJson());
/* 48 */       debug2.add("projectile", this.projectile.toJson(debug1));
/* 49 */       return debug2;
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext debug1, Vec3 debug2, int debug3) {
/* 53 */       if (!this.signalStrength.matches(debug3)) {
/* 54 */         return false;
/*    */       }
/* 56 */       if (!this.projectile.matches(debug1)) {
/* 57 */         return false;
/*    */       }
/* 59 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\TargetBlockTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */