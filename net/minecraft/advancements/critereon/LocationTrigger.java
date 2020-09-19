/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class LocationTrigger
/*    */   extends SimpleCriterionTrigger<LocationTrigger.TriggerInstance> {
/*    */   public LocationTrigger(ResourceLocation debug1) {
/* 14 */     this.id = debug1;
/*    */   }
/*    */   private final ResourceLocation id;
/*    */   
/*    */   public ResourceLocation getId() {
/* 19 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 25 */     JsonObject debug4 = GsonHelper.getAsJsonObject(debug1, "location", debug1);
/* 26 */     LocationPredicate debug5 = LocationPredicate.fromJson((JsonElement)debug4);
/* 27 */     return new TriggerInstance(this.id, debug2, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1) {
/* 31 */     trigger(debug1, debug1 -> debug1.matches(debug0.getLevel(), debug0.getX(), debug0.getY(), debug0.getZ()));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final LocationPredicate location;
/*    */     
/*    */     public TriggerInstance(ResourceLocation debug1, EntityPredicate.Composite debug2, LocationPredicate debug3) {
/* 38 */       super(debug1, debug2);
/* 39 */       this.location = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance located(LocationPredicate debug0) {
/* 43 */       return new TriggerInstance(CriteriaTriggers.LOCATION.id, EntityPredicate.Composite.ANY, debug0);
/*    */     }
/*    */     
/*    */     public static TriggerInstance sleptInBed() {
/* 47 */       return new TriggerInstance(CriteriaTriggers.SLEPT_IN_BED.id, EntityPredicate.Composite.ANY, LocationPredicate.ANY);
/*    */     }
/*    */     
/*    */     public static TriggerInstance raidWon() {
/* 51 */       return new TriggerInstance(CriteriaTriggers.RAID_WIN.id, EntityPredicate.Composite.ANY, LocationPredicate.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(ServerLevel debug1, double debug2, double debug4, double debug6) {
/* 55 */       return this.location.matches(debug1, debug2, debug4, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 60 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 61 */       debug2.add("location", this.location.serializeToJson());
/* 62 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\LocationTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */