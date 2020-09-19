/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*    */ import net.minecraft.world.level.storage.loot.RandomIntGenerators;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetItemCountFunction extends LootItemConditionalFunction {
/*    */   private final RandomIntGenerator value;
/*    */   
/*    */   private SetItemCountFunction(LootItemCondition[] debug1, RandomIntGenerator debug2) {
/* 16 */     super(debug1);
/* 17 */     this.value = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 22 */     return LootItemFunctions.SET_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 27 */     debug1.setCount(this.value.getInt(debug2.getRandom()));
/* 28 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(RandomIntGenerator debug0) {
/* 32 */     return simpleBuilder(debug1 -> new SetItemCountFunction(debug1, debug0));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<SetItemCountFunction> {
/*    */     public void serialize(JsonObject debug1, SetItemCountFunction debug2, JsonSerializationContext debug3) {
/* 38 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 40 */       debug1.add("count", RandomIntGenerators.serialize(debug2.value, debug3));
/*    */     }
/*    */ 
/*    */     
/*    */     public SetItemCountFunction deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 45 */       RandomIntGenerator debug4 = RandomIntGenerators.deserialize(debug1.get("count"), debug2);
/* 46 */       return new SetItemCountFunction(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\SetItemCountFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */