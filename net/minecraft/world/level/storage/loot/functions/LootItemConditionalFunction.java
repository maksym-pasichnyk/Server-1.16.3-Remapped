/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ 
/*    */ public abstract class LootItemConditionalFunction
/*    */   implements LootItemFunction {
/*    */   protected final LootItemCondition[] predicates;
/*    */   private final Predicate<LootContext> compositePredicates;
/*    */   
/*    */   protected LootItemConditionalFunction(LootItemCondition[] debug1) {
/* 25 */     this.predicates = debug1;
/* 26 */     this.compositePredicates = LootItemConditions.andConditions((Predicate[])debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public final ItemStack apply(ItemStack debug1, LootContext debug2) {
/* 31 */     return this.compositePredicates.test(debug2) ? run(debug1, debug2) : debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 38 */     super.validate(debug1);
/*    */     
/* 40 */     for (int debug2 = 0; debug2 < this.predicates.length; debug2++)
/* 41 */       this.predicates[debug2].validate(debug1.forChild(".conditions[" + debug2 + "]")); 
/*    */   }
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>>
/*    */     implements LootItemFunction.Builder, ConditionUserBuilder<T> {
/* 46 */     private final List<LootItemCondition> conditions = Lists.newArrayList();
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder debug1) {
/* 50 */       this.conditions.add(debug1.build());
/* 51 */       return getThis();
/*    */     }
/*    */ 
/*    */     
/*    */     public final T unwrap() {
/* 56 */       return getThis();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected LootItemCondition[] getConditions() {
/* 62 */       return this.conditions.<LootItemCondition>toArray(new LootItemCondition[0]);
/*    */     }
/*    */     
/*    */     protected abstract T getThis();
/*    */   }
/*    */   
/*    */   static final class DummyBuilder extends Builder<DummyBuilder> {
/*    */     public DummyBuilder(Function<LootItemCondition[], LootItemFunction> debug1) {
/* 70 */       this.constructor = debug1;
/*    */     }
/*    */     private final Function<LootItemCondition[], LootItemFunction> constructor;
/*    */     
/*    */     protected DummyBuilder getThis() {
/* 75 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 80 */       return this.constructor.apply(getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   protected static Builder<?> simpleBuilder(Function<LootItemCondition[], LootItemFunction> debug0) {
/* 85 */     return new DummyBuilder(debug0);
/*    */   }
/*    */   
/*    */   protected abstract ItemStack run(ItemStack paramItemStack, LootContext paramLootContext);
/*    */   
/*    */   public static abstract class Serializer<T extends LootItemConditionalFunction> implements net.minecraft.world.level.storage.loot.Serializer<T> { public void serialize(JsonObject debug1, T debug2, JsonSerializationContext debug3) {
/* 91 */       if (!ArrayUtils.isEmpty((Object[])((LootItemConditionalFunction)debug2).predicates)) {
/* 92 */         debug1.add("conditions", debug3.serialize(((LootItemConditionalFunction)debug2).predicates));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public final T deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 98 */       LootItemCondition[] debug3 = (LootItemCondition[])GsonHelper.getAsObject(debug1, "conditions", new LootItemCondition[0], debug2, LootItemCondition[].class);
/* 99 */       return deserialize(debug1, debug2, debug3);
/*    */     }
/*    */     
/*    */     public abstract T deserialize(JsonObject param1JsonObject, JsonDeserializationContext param1JsonDeserializationContext, LootItemCondition[] param1ArrayOfLootItemCondition); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */