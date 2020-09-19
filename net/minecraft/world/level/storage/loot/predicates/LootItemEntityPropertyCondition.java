/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.advancements.critereon.EntityPredicate;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LootItemEntityPropertyCondition
/*    */   implements LootItemCondition {
/*    */   private final EntityPredicate predicate;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   private LootItemEntityPropertyCondition(EntityPredicate debug1, LootContext.EntityTarget debug2) {
/* 22 */     this.predicate = debug1;
/* 23 */     this.entityTarget = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 28 */     return LootItemConditions.ENTITY_PROPERTIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 33 */     return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN, this.entityTarget.getParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 38 */     Entity debug2 = (Entity)debug1.getParamOrNull(this.entityTarget.getParam());
/* 39 */     Vec3 debug3 = (Vec3)debug1.getParamOrNull(LootContextParams.ORIGIN);
/* 40 */     return this.predicate.matches(debug1.getLevel(), debug3, debug2);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget debug0) {
/* 44 */     return hasProperties(debug0, EntityPredicate.Builder.entity());
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget debug0, EntityPredicate.Builder debug1) {
/* 48 */     return () -> new LootItemEntityPropertyCondition(debug0.build(), debug1);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget debug0, EntityPredicate debug1) {
/* 52 */     return () -> new LootItemEntityPropertyCondition(debug0, debug1);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LootItemEntityPropertyCondition> {
/*    */     public void serialize(JsonObject debug1, LootItemEntityPropertyCondition debug2, JsonSerializationContext debug3) {
/* 58 */       debug1.add("predicate", debug2.predicate.serializeToJson());
/* 59 */       debug1.add("entity", debug3.serialize(debug2.entityTarget));
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemEntityPropertyCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 64 */       EntityPredicate debug3 = EntityPredicate.fromJson(debug1.get("predicate"));
/* 65 */       return new LootItemEntityPropertyCondition(debug3, (LootContext.EntityTarget)GsonHelper.getAsObject(debug1, "entity", debug2, LootContext.EntityTarget.class));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemEntityPropertyCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */