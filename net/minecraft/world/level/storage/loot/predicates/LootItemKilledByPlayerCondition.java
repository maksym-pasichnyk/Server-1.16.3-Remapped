/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemKilledByPlayerCondition
/*    */   implements LootItemCondition {
/* 14 */   private static final LootItemKilledByPlayerCondition INSTANCE = new LootItemKilledByPlayerCondition();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 21 */     return LootItemConditions.KILLED_BY_PLAYER;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 26 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 31 */     return debug1.hasParam(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder killedByPlayer() {
/* 35 */     return () -> INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LootItemKilledByPlayerCondition>
/*    */   {
/*    */     public void serialize(JsonObject debug1, LootItemKilledByPlayerCondition debug2, JsonSerializationContext debug3) {}
/*    */     
/*    */     public LootItemKilledByPlayerCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 45 */       return LootItemKilledByPlayerCondition.INSTANCE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemKilledByPlayerCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */