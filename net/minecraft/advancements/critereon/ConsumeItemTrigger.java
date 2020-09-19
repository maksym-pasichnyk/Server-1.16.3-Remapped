/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class ConsumeItemTrigger extends SimpleCriterionTrigger<ConsumeItemTrigger.TriggerInstance> {
/* 10 */   private static final ResourceLocation ID = new ResourceLocation("consume_item");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 14 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 19 */     return new TriggerInstance(debug2, ItemPredicate.fromJson(debug1.get("item")));
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2) {
/* 23 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2) {
/* 30 */       super(ConsumeItemTrigger.ID, debug1);
/* 31 */       this.item = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance usedItem() {
/* 35 */       return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance usedItem(ItemLike debug0) {
/* 43 */       return new TriggerInstance(EntityPredicate.Composite.ANY, new ItemPredicate(null, debug0.asItem(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY));
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1) {
/* 47 */       return this.item.matches(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 52 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 54 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 56 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ConsumeItemTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */