/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ 
/*    */ public class ConstructBeaconTrigger extends SimpleCriterionTrigger<ConstructBeaconTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("construct_beacon");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     MinMaxBounds.Ints debug4 = MinMaxBounds.Ints.fromJson(debug1.get("level"));
/* 19 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, BeaconBlockEntity debug2) {
/* 23 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final MinMaxBounds.Ints level;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, MinMaxBounds.Ints debug2) {
/* 30 */       super(ConstructBeaconTrigger.ID, debug1);
/* 31 */       this.level = debug2;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance constructedBeacon(MinMaxBounds.Ints debug0) {
/* 39 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(BeaconBlockEntity debug1) {
/* 43 */       return this.level.matches(debug1.getLevels());
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 48 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 50 */       debug2.add("level", this.level.serializeToJson());
/*    */       
/* 52 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ConstructBeaconTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */