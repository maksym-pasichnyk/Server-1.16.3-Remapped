/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class BeeNestDestroyedTrigger
/*    */   extends SimpleCriterionTrigger<BeeNestDestroyedTrigger.TriggerInstance> {
/* 15 */   private static final ResourceLocation ID = new ResourceLocation("bee_nest_destroyed");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 19 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 24 */     Block debug4 = deserializeBlock(debug1);
/* 25 */     ItemPredicate debug5 = ItemPredicate.fromJson(debug1.get("item"));
/* 26 */     MinMaxBounds.Ints debug6 = MinMaxBounds.Ints.fromJson(debug1.get("num_bees_inside"));
/*    */     
/* 28 */     return new TriggerInstance(debug2, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private static Block deserializeBlock(JsonObject debug0) {
/* 33 */     if (debug0.has("block")) {
/* 34 */       ResourceLocation debug1 = new ResourceLocation(GsonHelper.getAsString(debug0, "block"));
/* 35 */       return (Block)Registry.BLOCK.getOptional(debug1).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + debug0 + "'"));
/*    */     } 
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, Block debug2, ItemStack debug3, int debug4) {
/* 41 */     trigger(debug1, debug3 -> debug3.matches(debug0, debug1, debug2));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     @Nullable
/*    */     private final Block block;
/*    */     private final ItemPredicate item;
/*    */     private final MinMaxBounds.Ints numBees;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, @Nullable Block debug2, ItemPredicate debug3, MinMaxBounds.Ints debug4) {
/* 51 */       super(BeeNestDestroyedTrigger.ID, debug1);
/* 52 */       this.block = debug2;
/* 53 */       this.item = debug3;
/* 54 */       this.numBees = debug4;
/*    */     }
/*    */     
/*    */     public static TriggerInstance destroyedBeeNest(Block debug0, ItemPredicate.Builder debug1, MinMaxBounds.Ints debug2) {
/* 58 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, debug1.build(), debug2);
/*    */     }
/*    */     
/*    */     public boolean matches(Block debug1, ItemStack debug2, int debug3) {
/* 62 */       if (this.block != null && debug1 != this.block) {
/* 63 */         return false;
/*    */       }
/* 65 */       if (!this.item.matches(debug2)) {
/* 66 */         return false;
/*    */       }
/* 68 */       return this.numBees.matches(debug3);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 73 */       JsonObject debug2 = super.serializeToJson(debug1);
/*    */       
/* 75 */       if (this.block != null) {
/* 76 */         debug2.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
/*    */       }
/* 78 */       debug2.add("item", this.item.serializeToJson());
/* 79 */       debug2.add("num_bees_inside", this.numBees.serializeToJson());
/*    */       
/* 81 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\BeeNestDestroyedTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */