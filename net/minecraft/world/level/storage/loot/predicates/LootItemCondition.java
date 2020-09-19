/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface LootItemCondition
/*    */   extends LootContextUser, Predicate<LootContext>
/*    */ {
/*    */   LootItemConditionType getType();
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Builder {
/*    */     LootItemCondition build();
/*    */     
/*    */     default Builder invert() {
/* 17 */       return InvertedLootItemCondition.invert(this);
/*    */     }
/*    */     
/*    */     default AlternativeLootItemCondition.Builder or(Builder debug1) {
/* 21 */       return AlternativeLootItemCondition.alternative(new Builder[] { this, debug1 });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */