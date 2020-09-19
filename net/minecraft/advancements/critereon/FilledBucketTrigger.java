/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class FilledBucketTrigger extends SimpleCriterionTrigger<FilledBucketTrigger.TriggerInstance> {
/*  9 */   private static final ResourceLocation ID = new ResourceLocation("filled_bucket");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 13 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 18 */     ItemPredicate debug4 = ItemPredicate.fromJson(debug1.get("item"));
/* 19 */     return new TriggerInstance(debug2, debug4);
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
/* 30 */       super(FilledBucketTrigger.ID, debug1);
/* 31 */       this.item = debug2;
/*    */     }
/*    */     
/*    */     public static TriggerInstance filledBucket(ItemPredicate debug0) {
/* 35 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0);
/*    */     }
/*    */     
/*    */     public boolean matches(ItemStack debug1) {
/* 39 */       if (!this.item.matches(debug1)) {
/* 40 */         return false;
/*    */       }
/* 42 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 47 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 49 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 51 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\FilledBucketTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */