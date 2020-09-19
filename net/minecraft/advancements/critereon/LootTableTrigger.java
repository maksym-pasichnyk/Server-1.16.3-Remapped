/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class LootTableTrigger extends SimpleCriterionTrigger<LootTableTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("player_generates_container_loot");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     ResourceLocation debug4 = new ResourceLocation(GsonHelper.getAsString(debug1, "loot_table"));
/*    */     
/* 20 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ResourceLocation debug2) {
/* 24 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ResourceLocation lootTable;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ResourceLocation debug2) {
/* 31 */       super(LootTableTrigger.ID, debug1);
/* 32 */       this.lootTable = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance lootTableUsed(ResourceLocation debug0) {
/* 36 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(ResourceLocation debug1) {
/* 40 */       return this.lootTable.equals(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 45 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 46 */       debug2.addProperty("loot_table", this.lootTable.toString());
/* 47 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\LootTableTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */