/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class PlayerInteractTrigger extends SimpleCriterionTrigger<PlayerInteractTrigger.TriggerInstance> {
/* 11 */   private static final ResourceLocation ID = new ResourceLocation("player_interacted_with_entity");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 20 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("item"));
/* 21 */     EntityPredicate.Composite debug5 = EntityPredicate.Composite.fromJson(debug1, "entity", debug3);
/* 22 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2, Entity debug3) {
/* 26 */     LootContext debug4 = EntityPredicate.createContext(debug1, debug3);
/* 27 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate item;
/*    */     private final EntityPredicate.Composite entity;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2, EntityPredicate.Composite debug3) {
/* 35 */       super(PlayerInteractTrigger.ID, debug1);
/* 36 */       this.item = debug2;
/* 37 */       this.entity = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance itemUsedOnEntity(EntityPredicate.Composite debug0, ItemPredicate.Builder debug1, EntityPredicate.Composite debug2) {
/* 41 */       return new TriggerInstance(debug0, debug1.build(), debug2);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1, LootContext debug2) {
/* 45 */       if (!this.item.matches(debug1)) {
/* 46 */         return false;
/*    */       }
/*    */       
/* 49 */       return this.entity.matches(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 54 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 55 */       debug2.add("item", this.item.serializeToJson());
/* 56 */       debug2.add("entity", this.entity.toJson(debug1));
/* 57 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\PlayerInteractTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */