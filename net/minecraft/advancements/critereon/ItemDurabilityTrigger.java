/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemDurabilityTrigger extends SimpleCriterionTrigger<ItemDurabilityTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("item_durability_changed");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("item"));
/* 19 */     MinMaxBounds.Ints debug5 = MinMaxBounds.Ints.fromJson(debug1.get("durability"));
/* 20 */     MinMaxBounds.Ints debug6 = MinMaxBounds.Ints.fromJson(debug1.get("delta"));
/* 21 */     return new TriggerInstance(debug2, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2, int debug3) {
/* 25 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate item;
/*    */     private final MinMaxBounds.Ints durability;
/*    */     private final MinMaxBounds.Ints delta;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2, MinMaxBounds.Ints debug3, MinMaxBounds.Ints debug4) {
/* 34 */       super(ItemDurabilityTrigger.ID, debug1);
/* 35 */       this.item = debug2;
/* 36 */       this.durability = debug3;
/* 37 */       this.delta = debug4;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance changedDurability(EntityPredicate.Composite debug0, ItemPredicate debug1, MinMaxBounds.Ints debug2) {
/* 45 */       return new TriggerInstance(debug0, debug1, debug2, MinMaxBounds.Ints.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1, int debug2) {
/* 49 */       if (!this.item.matches(debug1)) {
/* 50 */         return false;
/*    */       }
/* 52 */       if (!this.durability.matches(debug1.getMaxDamage() - debug2)) {
/* 53 */         return false;
/*    */       }
/* 55 */       if (!this.delta.matches(debug1.getDamageValue() - debug2)) {
/* 56 */         return false;
/*    */       }
/* 58 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 63 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 65 */       debug2.add("item", this.item.serializeToJson());
/* 66 */       debug2.add("durability", this.durability.serializeToJson());
/* 67 */       debug2.add("delta", this.delta.serializeToJson());
/*    */       
/* 69 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ItemDurabilityTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */