/*    */ package net.minecraft.world.level.storage.loot.predicates;
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
/*    */ 
/*    */ public class AlternativeLootItemCondition
/*    */   implements LootItemCondition
/*    */ {
/*    */   private final LootItemCondition[] terms;
/*    */   private final Predicate<LootContext> composedPredicate;
/*    */   
/*    */   private AlternativeLootItemCondition(LootItemCondition[] debug1) {
/* 20 */     this.terms = debug1;
/* 21 */     this.composedPredicate = LootItemConditions.orConditions((Predicate<LootContext>[])debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 26 */     return LootItemConditions.ALTERNATIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean test(LootContext debug1) {
/* 31 */     return this.composedPredicate.test(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 36 */     super.validate(debug1);
/*    */     
/* 38 */     for (int debug2 = 0; debug2 < this.terms.length; debug2++)
/* 39 */       this.terms[debug2].validate(debug1.forChild(".term[" + debug2 + "]")); 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     implements LootItemCondition.Builder {
/* 44 */     private final List<LootItemCondition> terms = Lists.newArrayList();
/*    */     
/*    */     public Builder(LootItemCondition.Builder... debug1) {
/* 47 */       for (LootItemCondition.Builder debug5 : debug1) {
/* 48 */         this.terms.add(debug5.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder or(LootItemCondition.Builder debug1) {
/* 54 */       this.terms.add(debug1.build());
/* 55 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemCondition build() {
/* 60 */       return new AlternativeLootItemCondition(this.terms.<LootItemCondition>toArray(new LootItemCondition[0]));
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder alternative(LootItemCondition.Builder... debug0) {
/* 65 */     return new Builder(debug0);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<AlternativeLootItemCondition> {
/*    */     public void serialize(JsonObject debug1, AlternativeLootItemCondition debug2, JsonSerializationContext debug3) {
/* 71 */       debug1.add("terms", debug3.serialize(debug2.terms));
/*    */     }
/*    */ 
/*    */     
/*    */     public AlternativeLootItemCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 76 */       LootItemCondition[] debug3 = (LootItemCondition[])GsonHelper.getAsObject(debug1, "terms", debug2, LootItemCondition[].class);
/* 77 */       return new AlternativeLootItemCondition(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\AlternativeLootItemCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */