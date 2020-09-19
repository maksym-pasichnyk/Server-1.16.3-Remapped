/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.critereon.DamageSourcePredicate;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DamageSourceCondition
/*    */   implements LootItemCondition {
/*    */   private final DamageSourcePredicate predicate;
/*    */   
/*    */   private DamageSourceCondition(DamageSourcePredicate debug1) {
/* 20 */     this.predicate = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 25 */     return LootItemConditions.DAMAGE_SOURCE_PROPERTIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 30 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN, LootContextParams.DAMAGE_SOURCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 35 */     DamageSource debug2 = (DamageSource)debug1.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
/* 36 */     Vec3 debug3 = (Vec3)debug1.getParamOrNull(LootContextParams.ORIGIN);
/*    */     
/* 38 */     return (debug3 != null && debug2 != null && this.predicate.matches(debug1.getLevel(), debug3, debug2));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasDamageSource(DamageSourcePredicate.Builder debug0) {
/* 42 */     return () -> new DamageSourceCondition(debug0.build());
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<DamageSourceCondition> {
/*    */     public void serialize(JsonObject debug1, DamageSourceCondition debug2, JsonSerializationContext debug3) {
/* 48 */       debug1.add("predicate", debug2.predicate.serializeToJson());
/*    */     }
/*    */ 
/*    */     
/*    */     public DamageSourceCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 53 */       DamageSourcePredicate debug3 = DamageSourcePredicate.fromJson(debug1.get("predicate"));
/* 54 */       return new DamageSourceCondition(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\DamageSourceCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */