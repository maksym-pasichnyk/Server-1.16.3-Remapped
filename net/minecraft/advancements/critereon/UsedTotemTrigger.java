/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class UsedTotemTrigger
/*    */   extends SimpleCriterionTrigger<UsedTotemTrigger.TriggerInstance>
/*    */ {
/* 12 */   private static final ResourceLocation ID = new ResourceLocation("used_totem");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 16 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 21 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("item"));
/* 22 */     return new TriggerInstance(debug2, debug4);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, ItemStack debug2) {
/* 26 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, ItemPredicate debug2) {
/* 33 */       super(UsedTotemTrigger.ID, debug1);
/* 34 */       this.item = debug2;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static TriggerInstance usedTotem(ItemLike debug0) {
/* 42 */       return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(debug0).build());
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1) {
/* 46 */       return this.item.matches(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 51 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 53 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 55 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\UsedTotemTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */