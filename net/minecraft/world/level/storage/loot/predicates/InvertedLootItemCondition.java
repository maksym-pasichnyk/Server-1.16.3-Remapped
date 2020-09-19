/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
/*    */ 
/*    */ public class InvertedLootItemCondition
/*    */   implements LootItemCondition {
/*    */   private final LootItemCondition term;
/*    */   
/*    */   private InvertedLootItemCondition(LootItemCondition debug1) {
/* 17 */     this.term = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 22 */     return LootItemConditions.INVERTED;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean test(LootContext debug1) {
/* 27 */     return !this.term.test(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<LootContextParam<?>> getReferencedContextParams() {
/* 32 */     return this.term.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 37 */     super.validate(debug1);
/* 38 */     this.term.validate(debug1);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder invert(LootItemCondition.Builder debug0) {
/* 42 */     InvertedLootItemCondition debug1 = new InvertedLootItemCondition(debug0.build());
/* 43 */     return () -> debug0;
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<InvertedLootItemCondition> {
/*    */     public void serialize(JsonObject debug1, InvertedLootItemCondition debug2, JsonSerializationContext debug3) {
/* 49 */       debug1.add("term", debug3.serialize(debug2.term));
/*    */     }
/*    */ 
/*    */     
/*    */     public InvertedLootItemCondition deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 54 */       LootItemCondition debug3 = (LootItemCondition)GsonHelper.getAsObject(debug1, "term", debug2, LootItemCondition.class);
/* 55 */       return new InvertedLootItemCondition(debug3);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\InvertedLootItemCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */