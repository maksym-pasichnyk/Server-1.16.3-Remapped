/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LevitationTrigger extends SimpleCriterionTrigger<LevitationTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("levitation");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     DistancePredicate debug4 = DistancePredicate.fromJson(debug1.get("distance"));
/* 19 */     MinMaxBounds.Ints debug5 = MinMaxBounds.Ints.fromJson(debug1.get("duration"));
/* 20 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Vec3 debug2, int debug3) {
/* 24 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final DistancePredicate distance;
/*    */     private final MinMaxBounds.Ints duration;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, DistancePredicate debug2, MinMaxBounds.Ints debug3) {
/* 32 */       super(LevitationTrigger.ID, debug1);
/* 33 */       this.distance = debug2;
/* 34 */       this.duration = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance levitated(DistancePredicate debug0) {
/* 38 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, MinMaxBounds.Ints.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(ServerPlayer debug1, Vec3 debug2, int debug3) {
/* 42 */       if (!this.distance.matches(debug2.x, debug2.y, debug2.z, debug1.getX(), debug1.getY(), debug1.getZ())) {
/* 43 */         return false;
/*    */       }
/* 45 */       if (!this.duration.matches(debug3)) {
/* 46 */         return false;
/*    */       }
/* 48 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 53 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 55 */       debug2.add("distance", this.distance.serializeToJson());
/* 56 */       debug2.add("duration", this.duration.serializeToJson());
/*    */       
/* 58 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\LevitationTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */