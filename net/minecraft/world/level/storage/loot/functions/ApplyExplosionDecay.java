/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ApplyExplosionDecay extends LootItemConditionalFunction {
/*    */   private ApplyExplosionDecay(LootItemCondition[] debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType getType() {
/* 19 */     return LootItemFunctions.EXPLOSION_DECAY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack debug1, LootContext debug2) {
/* 24 */     Float debug3 = (Float)debug2.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
/*    */     
/* 26 */     if (debug3 != null) {
/* 27 */       Random debug4 = debug2.getRandom();
/*    */       
/* 29 */       float debug5 = 1.0F / debug3.floatValue();
/* 30 */       int debug6 = debug1.getCount();
/* 31 */       int debug7 = 0;
/* 32 */       for (int debug8 = 0; debug8 < debug6; debug8++) {
/* 33 */         if (debug4.nextFloat() <= debug5) {
/* 34 */           debug7++;
/*    */         }
/*    */       } 
/*    */       
/* 38 */       debug1.setCount(debug7);
/*    */     } 
/* 40 */     return debug1;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> explosionDecay() {
/* 44 */     return simpleBuilder(ApplyExplosionDecay::new);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     extends LootItemConditionalFunction.Serializer<ApplyExplosionDecay> {
/*    */     public ApplyExplosionDecay deserialize(JsonObject debug1, JsonDeserializationContext debug2, LootItemCondition[] debug3) {
/* 50 */       return new ApplyExplosionDecay(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyExplosionDecay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */