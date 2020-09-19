/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ 
/*    */ public abstract class LootPoolEntryContainer
/*    */   implements ComposableEntryContainer {
/*    */   protected final LootItemCondition[] conditions;
/*    */   private final Predicate<LootContext> compositeCondition;
/*    */   
/*    */   protected LootPoolEntryContainer(LootItemCondition[] debug1) {
/* 23 */     this.conditions = debug1;
/* 24 */     this.compositeCondition = LootItemConditions.andConditions((Predicate[])debug1);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 28 */     for (int debug2 = 0; debug2 < this.conditions.length; debug2++) {
/* 29 */       this.conditions[debug2].validate(debug1.forChild(".condition[" + debug2 + "]"));
/*    */     }
/*    */   }
/*    */   
/*    */   protected final boolean canRun(LootContext debug1) {
/* 34 */     return this.compositeCondition.test(debug1);
/*    */   }
/*    */   
/*    */   public abstract LootPoolEntryType getType();
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>> implements ConditionUserBuilder<T> {
/* 40 */     private final List<LootItemCondition> conditions = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder debug1) {
/* 46 */       this.conditions.add(debug1.build());
/* 47 */       return getThis();
/*    */     }
/*    */ 
/*    */     
/*    */     public final T unwrap() {
/* 52 */       return getThis();
/*    */     }
/*    */     
/*    */     protected LootItemCondition[] getConditions() {
/* 56 */       return this.conditions.<LootItemCondition>toArray(new LootItemCondition[0]);
/*    */     }
/*    */     
/*    */     public AlternativesEntry.Builder otherwise(Builder<?> debug1) {
/* 60 */       return new AlternativesEntry.Builder((Builder<?>[])new Builder[] { this, debug1 });
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected abstract T getThis();
/*    */ 
/*    */ 
/*    */     
/*    */     public abstract LootPoolEntryContainer build();
/*    */   }
/*    */ 
/*    */   
/*    */   public static abstract class Serializer<T extends LootPoolEntryContainer>
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<T>
/*    */   {
/*    */     public final void serialize(JsonObject debug1, T debug2, JsonSerializationContext debug3) {
/* 77 */       if (!ArrayUtils.isEmpty((Object[])((LootPoolEntryContainer)debug2).conditions)) {
/* 78 */         debug1.add("conditions", debug3.serialize(((LootPoolEntryContainer)debug2).conditions));
/*    */       }
/* 80 */       serializeCustom(debug1, debug2, debug3);
/*    */     }
/*    */ 
/*    */     
/*    */     public final T deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 85 */       LootItemCondition[] debug3 = (LootItemCondition[])GsonHelper.getAsObject(debug1, "conditions", new LootItemCondition[0], debug2, LootItemCondition[].class);
/* 86 */       return deserializeCustom(debug1, debug2, debug3);
/*    */     }
/*    */     
/*    */     public abstract void serializeCustom(JsonObject param1JsonObject, T param1T, JsonSerializationContext param1JsonSerializationContext);
/*    */     
/*    */     public abstract T deserializeCustom(JsonObject param1JsonObject, JsonDeserializationContext param1JsonDeserializationContext, LootItemCondition[] param1ArrayOfLootItemCondition);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntryContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */