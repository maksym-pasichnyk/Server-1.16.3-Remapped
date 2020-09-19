/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
/*    */ import net.minecraft.world.level.storage.loot.Serializer;
/*    */ 
/*    */ public class LootItemConditions
/*    */ {
/* 11 */   public static final LootItemConditionType INVERTED = register("inverted", new InvertedLootItemCondition.Serializer());
/* 12 */   public static final LootItemConditionType ALTERNATIVE = register("alternative", new AlternativeLootItemCondition.Serializer());
/* 13 */   public static final LootItemConditionType RANDOM_CHANCE = register("random_chance", new LootItemRandomChanceCondition.Serializer());
/* 14 */   public static final LootItemConditionType RANDOM_CHANCE_WITH_LOOTING = register("random_chance_with_looting", new LootItemRandomChanceWithLootingCondition.Serializer());
/* 15 */   public static final LootItemConditionType ENTITY_PROPERTIES = register("entity_properties", new LootItemEntityPropertyCondition.Serializer());
/* 16 */   public static final LootItemConditionType KILLED_BY_PLAYER = register("killed_by_player", new LootItemKilledByPlayerCondition.Serializer());
/* 17 */   public static final LootItemConditionType ENTITY_SCORES = register("entity_scores", new EntityHasScoreCondition.Serializer());
/* 18 */   public static final LootItemConditionType BLOCK_STATE_PROPERTY = register("block_state_property", new LootItemBlockStatePropertyCondition.Serializer());
/* 19 */   public static final LootItemConditionType MATCH_TOOL = register("match_tool", new MatchTool.Serializer());
/* 20 */   public static final LootItemConditionType TABLE_BONUS = register("table_bonus", new BonusLevelTableCondition.Serializer());
/* 21 */   public static final LootItemConditionType SURVIVES_EXPLOSION = register("survives_explosion", new ExplosionCondition.Serializer());
/* 22 */   public static final LootItemConditionType DAMAGE_SOURCE_PROPERTIES = register("damage_source_properties", new DamageSourceCondition.Serializer());
/* 23 */   public static final LootItemConditionType LOCATION_CHECK = register("location_check", new LocationCheck.Serializer());
/* 24 */   public static final LootItemConditionType WEATHER_CHECK = register("weather_check", new WeatherCheck.Serializer());
/* 25 */   public static final LootItemConditionType REFERENCE = register("reference", new ConditionReference.Serializer());
/* 26 */   public static final LootItemConditionType TIME_CHECK = register("time_check", new TimeCheck.Serializer());
/*    */   
/*    */   private static LootItemConditionType register(String debug0, Serializer<? extends LootItemCondition> debug1) {
/* 29 */     return (LootItemConditionType)Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(debug0), new LootItemConditionType(debug1));
/*    */   }
/*    */   
/*    */   public static Object createGsonAdapter() {
/* 33 */     return GsonAdapterFactory.builder(Registry.LOOT_CONDITION_TYPE, "condition", "condition", LootItemCondition::getType).build();
/*    */   }
/*    */   
/*    */   public static <T> Predicate<T> andConditions(Predicate<T>[] debug0) {
/* 37 */     switch (debug0.length) {
/*    */       case 0:
/* 39 */         return debug0 -> true;
/*    */       case 1:
/* 41 */         return debug0[0];
/*    */       case 2:
/* 43 */         return debug0[0].and(debug0[1]);
/*    */     } 
/* 45 */     return debug1 -> {
/*    */         for (Predicate<T> debug5 : debug0) {
/*    */           if (!debug5.test((T)debug1)) {
/*    */             return false;
/*    */           }
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public static <T> Predicate<T> orConditions(Predicate<T>[] debug0) {
/* 57 */     switch (debug0.length) {
/*    */       case 0:
/* 59 */         return debug0 -> false;
/*    */       case 1:
/* 61 */         return debug0[0];
/*    */       case 2:
/* 63 */         return debug0[0].or(debug0[1]);
/*    */     } 
/*    */     
/* 66 */     return debug1 -> {
/*    */         for (Predicate<T> debug5 : debug0) {
/*    */           if (debug5.test((T)debug1))
/*    */             return true; 
/*    */         } 
/*    */         return false;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemConditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */