/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemRandomChanceWithLootingCondition
/*    */   implements LootItemCondition {
/*    */   private final float percent;
/*    */   private final float lootingMultiplier;
/*    */   
/*    */   private LootItemRandomChanceWithLootingCondition(float debug1, float debug2) {
/* 22 */     this.percent = debug1;
/* 23 */     this.lootingMultiplier = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 28 */     return LootItemConditions.RANDOM_CHANCE_WITH_LOOTING;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 33 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.KILLER_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 38 */     Entity debug2 = (Entity)debug1.getParamOrNull(LootContextParams.KILLER_ENTITY);
/*    */     
/* 40 */     int debug3 = 0;
/* 41 */     if (debug2 instanceof LivingEntity) {
/* 42 */       debug3 = EnchantmentHelper.getMobLooting((LivingEntity)debug2);
/*    */     }
/* 44 */     return (debug1.getRandom().nextFloat() < this.percent + debug3 * this.lootingMultiplier);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChanceAndLootingBoost(float debug0, float debug1) {
/* 48 */     return () -> new LootItemRandomChanceWithLootingCondition(debug0, debug1);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceWithLootingCondition> {
/*    */     public void serialize(JsonObject debug1, LootItemRandomChanceWithLootingCondition debug2, JsonSerializationContext debug3) {
/* 54 */       debug1.addProperty("chance", Float.valueOf(debug2.percent));
/* 55 */       debug1.addProperty("looting_multiplier", Float.valueOf(debug2.lootingMultiplier));
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemRandomChanceWithLootingCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 60 */       return new LootItemRandomChanceWithLootingCondition(GsonHelper.getAsFloat(debug1, "chance"), GsonHelper.getAsFloat(debug1, "looting_multiplier"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceWithLootingCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */