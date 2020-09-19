/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.critereon.StatePropertiesPredicate;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemBlockStatePropertyCondition
/*    */   implements LootItemCondition {
/*    */   private final Block block;
/*    */   private final StatePropertiesPredicate properties;
/*    */   
/*    */   private LootItemBlockStatePropertyCondition(Block debug1, StatePropertiesPredicate debug2) {
/* 25 */     this.block = debug1;
/* 26 */     this.properties = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 31 */     return LootItemConditions.BLOCK_STATE_PROPERTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 36 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.BLOCK_STATE);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 41 */     BlockState debug2 = (BlockState)debug1.getParamOrNull(LootContextParams.BLOCK_STATE);
/* 42 */     return (debug2 != null && this.block == debug2.getBlock() && this.properties.matches(debug2));
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/*    */     private final Block block;
/* 47 */     private StatePropertiesPredicate properties = StatePropertiesPredicate.ANY;
/*    */     
/*    */     public Builder(Block debug1) {
/* 50 */       this.block = debug1;
/*    */     }
/*    */     
/*    */     public Builder setProperties(StatePropertiesPredicate.Builder debug1) {
/* 54 */       this.properties = debug1.build();
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemCondition build() {
/* 60 */       return new LootItemBlockStatePropertyCondition(this.block, this.properties);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder hasBlockStateProperties(Block debug0) {
/* 65 */     return new Builder(debug0);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LootItemBlockStatePropertyCondition> {
/*    */     public void serialize(JsonObject debug1, LootItemBlockStatePropertyCondition debug2, JsonSerializationContext debug3) {
/* 71 */       debug1.addProperty("block", Registry.BLOCK.getKey(debug2.block).toString());
/* 72 */       debug1.add("properties", debug2.properties.serializeToJson());
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemBlockStatePropertyCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 77 */       ResourceLocation debug3 = new ResourceLocation(GsonHelper.getAsString(debug1, "block"));
/*    */       
/* 79 */       Block debug4 = (Block)Registry.BLOCK.getOptional(debug3).orElseThrow(() -> new IllegalArgumentException("Can't find block " + debug0));
/* 80 */       StatePropertiesPredicate debug5 = StatePropertiesPredicate.fromJson(debug1.get("properties"));
/* 81 */       debug5.checkState(debug4.getStateDefinition(), debug1 -> {
/*    */             throw new JsonSyntaxException("Block " + debug0 + " has no property " + debug1);
/*    */           });
/*    */       
/* 85 */       return new LootItemBlockStatePropertyCondition(debug4, debug5);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemBlockStatePropertyCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */