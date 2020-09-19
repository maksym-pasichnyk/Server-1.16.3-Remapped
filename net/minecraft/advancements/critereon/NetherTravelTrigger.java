/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class NetherTravelTrigger extends SimpleCriterionTrigger<NetherTravelTrigger.TriggerInstance> {
/* 10 */   private static final ResourceLocation ID = new ResourceLocation("nether_travel");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 14 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 19 */     LocationPredicate debug4 = LocationPredicate.fromJson(debug1.get("entered"));
/* 20 */     LocationPredicate debug5 = LocationPredicate.fromJson(debug1.get("exited"));
/* 21 */     DistancePredicate debug6 = DistancePredicate.fromJson(debug1.get("distance"));
/* 22 */     return new TriggerInstance(debug2, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Vec3 debug2) {
/* 26 */     trigger(debug1, debug2 -> debug2.matches(debug0.getLevel(), debug1, debug0.getX(), debug0.getY(), debug0.getZ()));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final LocationPredicate entered;
/*    */     private final LocationPredicate exited;
/*    */     private final DistancePredicate distance;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, LocationPredicate debug2, LocationPredicate debug3, DistancePredicate debug4) {
/* 35 */       super(NetherTravelTrigger.ID, debug1);
/* 36 */       this.entered = debug2;
/* 37 */       this.exited = debug3;
/* 38 */       this.distance = debug4;
/*    */     }
/*    */     
/*    */     public static TriggerInstance travelledThroughNether(DistancePredicate debug0) {
/* 42 */       return new TriggerInstance(EntityPredicate.Composite.ANY, LocationPredicate.ANY, LocationPredicate.ANY, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(ServerLevel debug1, Vec3 debug2, double debug3, double debug5, double debug7) {
/* 46 */       if (!this.entered.matches(debug1, debug2.x, debug2.y, debug2.z)) {
/* 47 */         return false;
/*    */       }
/* 49 */       if (!this.exited.matches(debug1, debug3, debug5, debug7)) {
/* 50 */         return false;
/*    */       }
/* 52 */       if (!this.distance.matches(debug2.x, debug2.y, debug2.z, debug3, debug5, debug7)) {
/* 53 */         return false;
/*    */       }
/* 55 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 60 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 62 */       debug2.add("entered", this.entered.serializeToJson());
/* 63 */       debug2.add("exited", this.exited.serializeToJson());
/* 64 */       debug2.add("distance", this.distance.serializeToJson());
/*    */       
/* 66 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\NetherTravelTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */