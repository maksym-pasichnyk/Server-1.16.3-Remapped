/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PlacedBlockTrigger
/*    */   extends SimpleCriterionTrigger<PlacedBlockTrigger.TriggerInstance> {
/* 18 */   private static final ResourceLocation ID = new ResourceLocation("placed_block");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 22 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 27 */     Block debug4 = deserializeBlock(debug1);
/* 28 */     StatePropertiesPredicate debug5 = StatePropertiesPredicate.fromJson(debug1.get("state"));
/* 29 */     if (debug4 != null) {
/* 30 */       debug5.checkState(debug4.getStateDefinition(), debug1 -> {
/*    */             throw new JsonSyntaxException("Block " + debug0 + " has no property " + debug1 + ":");
/*    */           });
/*    */     }
/* 34 */     LocationPredicate debug6 = LocationPredicate.fromJson(debug1.get("location"));
/* 35 */     ItemPredicate debug7 = ItemPredicate.fromJson(debug1.get("item"));
/*    */     
/* 37 */     return new TriggerInstance(debug2, debug4, debug5, debug6, debug7);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private static Block deserializeBlock(JsonObject debug0) {
/* 42 */     if (debug0.has("block")) {
/* 43 */       ResourceLocation debug1 = new ResourceLocation(GsonHelper.getAsString(debug0, "block"));
/* 44 */       return (Block)Registry.BLOCK.getOptional(debug1).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + debug0 + "'"));
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, BlockPos debug2, ItemStack debug3) {
/* 50 */     BlockState debug4 = debug1.getLevel().getBlockState(debug2);
/* 51 */     trigger(debug1, debug4 -> debug4.matches(debug0, debug1, debug2.getLevel(), debug3));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final Block block;
/*    */     private final StatePropertiesPredicate state;
/*    */     private final LocationPredicate location;
/*    */     private final ItemPredicate item;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, @Nullable Block debug2, StatePropertiesPredicate debug3, LocationPredicate debug4, ItemPredicate debug5) {
/* 61 */       super(PlacedBlockTrigger.ID, debug1);
/* 62 */       this.block = debug2;
/* 63 */       this.state = debug3;
/* 64 */       this.location = debug4;
/* 65 */       this.item = debug5;
/*    */     }
/*    */     
/*    */     public static TriggerInstance placedBlock(Block debug0) {
/* 69 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, StatePropertiesPredicate.ANY, LocationPredicate.ANY, ItemPredicate.ANY);
/*    */     }
/*    */     
/*    */     public boolean matches(BlockState debug1, BlockPos debug2, ServerLevel debug3, ItemStack debug4) {
/* 73 */       if (this.block != null && !debug1.is(this.block)) {
/* 74 */         return false;
/*    */       }
/* 76 */       if (!this.state.matches(debug1)) {
/* 77 */         return false;
/*    */       }
/* 79 */       if (!this.location.matches(debug3, debug2.getX(), debug2.getY(), debug2.getZ())) {
/* 80 */         return false;
/*    */       }
/* 82 */       if (!this.item.matches(debug4)) {
/* 83 */         return false;
/*    */       }
/* 85 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 90 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 92 */       if (this.block != null) {
/* 93 */         debug2.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
/*    */       }
/* 95 */       debug2.add("state", this.state.serializeToJson());
/* 96 */       debug2.add("location", this.location.serializeToJson());
/* 97 */       debug2.add("item", this.item.serializeToJson());
/*    */       
/* 99 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\PlacedBlockTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */