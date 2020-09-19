/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.critereon.ItemPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class MatchTool
/*    */   implements LootItemCondition {
/*    */   private final ItemPredicate predicate;
/*    */   
/*    */   public MatchTool(ItemPredicate debug1) {
/* 19 */     this.predicate = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 24 */     return LootItemConditions.MATCH_TOOL;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 29 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.TOOL);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 34 */     ItemStack debug2 = (ItemStack)debug1.getParamOrNull(LootContextParams.TOOL);
/* 35 */     return (debug2 != null && this.predicate.matches(debug2));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder debug0) {
/* 39 */     return () -> new MatchTool(debug0.build());
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<MatchTool> {
/*    */     public void serialize(JsonObject debug1, MatchTool debug2, JsonSerializationContext debug3) {
/* 45 */       debug1.add("predicate", debug2.predicate.serializeToJson());
/*    */     }
/*    */ 
/*    */     
/*    */     public MatchTool deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 50 */       ItemPredicate debug3 = ItemPredicate.fromJson(debug1.get("predicate"));
/* 51 */       return new MatchTool(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\MatchTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */