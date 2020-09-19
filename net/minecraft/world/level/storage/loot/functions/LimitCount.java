/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.IntLimiter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LimitCount extends LootItemConditionalFunction {
/*    */   private final IntLimiter limiter;
/*    */   
/*    */   private LimitCount(LootItemCondition[] debug1, IntLimiter debug2) {
/* 16 */     super(debug1);
/* 17 */     this.limiter = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 22 */     return LootItemFunctions.LIMIT_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 27 */     int debug3 = this.limiter.applyAsInt(debug1.getCount());
/* 28 */     debug1.setCount(debug3);
/* 29 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> limitCount(IntLimiter debug0) {
/* 33 */     return simpleBuilder(debug1 -> new LimitCount(debug1, debug0));
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<LimitCount> {
/*    */     public void serialize(JsonObject debug1, LimitCount debug2, JsonSerializationContext debug3) {
/* 39 */       super.serialize(debug1, debug2, debug3);
/*    */       
/* 41 */       debug1.add("limit", debug3.serialize(debug2.limiter));
/*    */     }
/*    */ 
/*    */     
/*    */     public LimitCount deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 46 */       IntLimiter debug4 = (IntLimiter)GsonHelper.getAsObject(debug1, "limit", debug2, IntLimiter.class);
/* 47 */       return new LimitCount(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LimitCount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */