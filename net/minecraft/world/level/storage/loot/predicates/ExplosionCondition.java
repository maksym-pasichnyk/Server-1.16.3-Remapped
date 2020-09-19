/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class ExplosionCondition
/*    */   implements LootItemCondition {
/* 15 */   private static final ExplosionCondition INSTANCE = new ExplosionCondition();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 22 */     return LootItemConditions.SURVIVES_EXPLOSION;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 27 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.EXPLOSION_RADIUS);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 32 */     Float debug2 = (Float)debug1.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
/* 33 */     if (debug2 != null) {
/* 34 */       Random debug3 = debug1.getRandom();
/* 35 */       float debug4 = 1.0F / debug2.floatValue();
/* 36 */       return (debug3.nextFloat() <= debug4);
/*    */     } 
/*    */     
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder survivesExplosion() {
/* 43 */     return () -> INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<ExplosionCondition>
/*    */   {
/*    */     public void serialize(JsonObject debug1, ExplosionCondition debug2, JsonSerializationContext debug3) {}
/*    */     
/*    */     public ExplosionCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 53 */       return ExplosionCondition.INSTANCE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\ExplosionCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */