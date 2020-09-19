/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class LootItemRandomChanceCondition implements LootItemCondition {
/*    */   private final float probability;
/*    */   
/*    */   private LootItemRandomChanceCondition(float debug1) {
/* 13 */     this.probability = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 18 */     return LootItemConditions.RANDOM_CHANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 23 */     return (debug1.getRandom().nextFloat() < this.probability);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChance(float debug0) {
/* 27 */     return () -> new LootItemRandomChanceCondition(debug0);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceCondition> {
/*    */     public void serialize(JsonObject debug1, LootItemRandomChanceCondition debug2, JsonSerializationContext debug3) {
/* 33 */       debug1.addProperty("chance", Float.valueOf(debug2.probability));
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemRandomChanceCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 38 */       return new LootItemRandomChanceCondition(GsonHelper.getAsFloat(debug1, "chance"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */