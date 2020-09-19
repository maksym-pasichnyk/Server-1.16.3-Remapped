/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ItemUsedOnBlockTrigger extends SimpleCriterionTrigger<ItemUsedOnBlockTrigger.TriggerInstance> {
/* 12 */   private static final ResourceLocation ID = new ResourceLocation("item_used_on_block");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 16 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 21 */     LocationPredicate debug4 = LocationPredicate.fromJson(debug1.get("location"));
/* 22 */     ItemPredicate debug5 = ItemPredicate.fromJson(debug1.get("item"));
/*    */     
/* 24 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, BlockPos debug2, ItemStack debug3) {
/* 28 */     BlockState debug4 = debug1.getLevel().getBlockState(debug2);
/*    */     
/* 30 */     trigger(debug1, debug4 -> debug4.matches(debug0, debug1.getLevel(), debug2, debug3));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final LocationPredicate location;
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, LocationPredicate debug2, ItemPredicate debug3) {
/* 38 */       super(ItemUsedOnBlockTrigger.ID, debug1);
/* 39 */       this.location = debug2;
/* 40 */       this.item = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance itemUsedOnBlock(LocationPredicate.Builder debug0, ItemPredicate.Builder debug1) {
/* 44 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0.build(), debug1.build());
/*    */     }
/*    */     
/*    */     public boolean matches(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {
/* 48 */       if (!this.location.matches(debug2, debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D)) {
/* 49 */         return false;
/*    */       }
/* 51 */       return this.item.matches(debug4);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 56 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 58 */       debug2.add("location", this.location.serializeToJson());
/* 59 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 61 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\ItemUsedOnBlockTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */