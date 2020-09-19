/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class EnterBlockTrigger
/*    */   extends SimpleCriterionTrigger<EnterBlockTrigger.TriggerInstance> {
/* 15 */   private static final ResourceLocation ID = new ResourceLocation("enter_block");
/*    */ 
/*    */   
/*    */   public ResourceLocation getId() {
/* 19 */     return ID;
/*    */   }
/*    */ 
/*    */   
/*    */   public TriggerInstance createInstance(JsonObject debug1, EntityPredicate.Composite debug2, DeserializationContext debug3) {
/* 24 */     Block debug4 = deserializeBlock(debug1);
/* 25 */     StatePropertiesPredicate debug5 = StatePropertiesPredicate.fromJson(debug1.get("state"));
/* 26 */     if (debug4 != null) {
/* 27 */       debug5.checkState(debug4.getStateDefinition(), debug1 -> {
/*    */             throw new JsonSyntaxException("Block " + debug0 + " has no property " + debug1);
/*    */           });
/*    */     }
/* 31 */     return new TriggerInstance(debug2, debug4, debug5);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private static Block deserializeBlock(JsonObject debug0) {
/* 36 */     if (debug0.has("block")) {
/* 37 */       ResourceLocation debug1 = new ResourceLocation(GsonHelper.getAsString(debug0, "block"));
/* 38 */       return (Block)Registry.BLOCK.getOptional(debug1).orElseThrow(() -> new JsonSyntaxException("Unknown block type '" + debug0 + "'"));
/*    */     } 
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer debug1, BlockState debug2) {
/* 44 */     trigger(debug1, debug1 -> debug1.matches(debug0));
/*    */   }
/*    */   
/*    */   public static class TriggerInstance extends AbstractCriterionTriggerInstance {
/*    */     private final Block block;
/*    */     private final StatePropertiesPredicate state;
/*    */     
/*    */     public TriggerInstance(EntityPredicate.Composite debug1, @Nullable Block debug2, StatePropertiesPredicate debug3) {
/* 52 */       super(EnterBlockTrigger.ID, debug1);
/* 53 */       this.block = debug2;
/* 54 */       this.state = debug3;
/*    */     }
/*    */     
/*    */     public static TriggerInstance entersBlock(Block debug0) {
/* 58 */       return new TriggerInstance(EntityPredicate.Composite.ANY, debug0, StatePropertiesPredicate.ANY);
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonObject serializeToJson(SerializationContext debug1) {
/* 63 */       JsonObject debug2 = super.serializeToJson(debug1);
/* 64 */       if (this.block != null) {
/* 65 */         debug2.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
/*    */       }
/* 67 */       debug2.add("state", this.state.serializeToJson());
/* 68 */       return debug2;
/*    */     }
/*    */     
/*    */     public boolean matches(BlockState debug1) {
/* 72 */       if (this.block != null && !debug1.is(this.block)) {
/* 73 */         return false;
/*    */       }
/* 75 */       if (!this.state.matches(debug1)) {
/* 76 */         return false;
/*    */       }
/* 78 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EnterBlockTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */