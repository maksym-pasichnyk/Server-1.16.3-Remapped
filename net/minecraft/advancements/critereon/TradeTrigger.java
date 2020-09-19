/*    */ package net.minecraft.advancements.critereon;
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.npc.AbstractVillager;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class TradeTrigger extends SimpleCriterionTrigger<TradeTrigger.TriggerInstance> {
/* 11 */   private static final ResourceLocation ID = new ResourceLocation("villager_trade");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 15 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 20 */     EntityPredicate.Composite debug4 = EntityPredicate.Composite.fromJson(debug1, "villager", debug3);
/* 21 */     ItemPredicate debug5 = ItemPredicate.fromJson(debug1.get("item"));
/* 22 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, AbstractVillager debug2, ItemStack debug3) {
/* 26 */     LootContext debug4 = EntityPredicate.createContext(debug1, (Entity)debug2);
/* 27 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final EntityPredicate.Composite villager;
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, EntityPredicate.Composite debug2, ItemPredicate debug3) {
/* 35 */       super(TradeTrigger.ID, debug1);
/* 36 */       this.villager = debug2;
/* 37 */       this.item = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance tradedWithVillager() {
/* 41 */       return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, ItemPredicate.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext debug1, ItemStack debug2) {
/* 45 */       if (!this.villager.matches(debug1)) {
/* 46 */         return false;
/*    */       }
/* 48 */       if (!this.item.matches(debug2)) {
/* 49 */         return false;
/*    */       }
/* 51 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 56 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 58 */       debug2.add("item", this.item.serializeToJson());
/* 59 */       debug2.add("villager", this.villager.toJson(debug1));
/*    */       
/* 61 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\TradeTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */