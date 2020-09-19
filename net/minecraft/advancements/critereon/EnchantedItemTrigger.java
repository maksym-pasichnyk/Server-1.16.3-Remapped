/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class EnchantedItemTrigger extends SimpleCriterionTrigger<EnchantedItemTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("enchanted_item");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("item"));
/* 19 */     MinMaxBounds.Ints debug5 = MinMaxBounds.Ints.fromJson(debug1.get("levels"));
/* 20 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2, int debug3) {
/* 24 */     trigger(debug1, debug2 -> debug2.matches(debug0, debug1));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate item;
/*    */     private final MinMaxBounds.Ints levels;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2, MinMaxBounds.Ints debug3) {
/* 32 */       super(EnchantedItemTrigger.ID, debug1);
/* 33 */       this.item = debug2;
/* 34 */       this.levels = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance enchantedItem() {
/* 38 */       return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, MinMaxBounds.Ints.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1, int debug2) {
/* 42 */       if (!this.item.matches(debug1)) {
/* 43 */         return false;
/*    */       }
/* 45 */       if (!this.levels.matches(debug2)) {
/* 46 */         return false;
/*    */       }
/* 48 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 53 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 55 */       debug2.add("item", this.item.serializeToJson());
/* 56 */       debug2.add("levels", this.levels.serializeToJson());
/*    */       
/* 58 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EnchantedItemTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */