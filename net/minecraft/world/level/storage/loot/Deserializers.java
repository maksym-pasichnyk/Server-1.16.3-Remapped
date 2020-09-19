/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.gson.GsonBuilder;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*    */ 
/*    */ public class Deserializers {
/*    */   public static GsonBuilder createConditionSerializer() {
/* 13 */     return (new GsonBuilder())
/* 14 */       .registerTypeAdapter(RandomValueBounds.class, new RandomValueBounds.Serializer())
/* 15 */       .registerTypeAdapter(BinomialDistributionGenerator.class, new BinomialDistributionGenerator.Serializer())
/* 16 */       .registerTypeAdapter(ConstantIntValue.class, new ConstantIntValue.Serializer())
/* 17 */       .registerTypeHierarchyAdapter(LootItemCondition.class, LootItemConditions.createGsonAdapter())
/* 18 */       .registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer());
/*    */   }
/*    */   
/*    */   public static GsonBuilder createFunctionSerializer() {
/* 22 */     return createConditionSerializer()
/* 23 */       .registerTypeAdapter(IntLimiter.class, new IntLimiter.Serializer())
/* 24 */       .registerTypeHierarchyAdapter(LootPoolEntryContainer.class, LootPoolEntries.createGsonAdapter())
/* 25 */       .registerTypeHierarchyAdapter(LootItemFunction.class, LootItemFunctions.createGsonAdapter());
/*    */   }
/*    */   
/*    */   public static GsonBuilder createLootTableSerializer() {
/* 29 */     return createFunctionSerializer()
/* 30 */       .registerTypeAdapter(LootPool.class, new LootPool.Serializer())
/* 31 */       .registerTypeAdapter(LootTable.class, new LootTable.Serializer());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\Deserializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */