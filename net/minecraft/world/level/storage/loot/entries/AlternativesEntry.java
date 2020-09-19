/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ 
/*    */ public class AlternativesEntry extends CompositeEntryBase {
/*    */   AlternativesEntry(LootPoolEntryContainer[] debug1, LootItemCondition[] debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 17 */     return LootPoolEntries.ALTERNATIVES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(ComposableEntryContainer[] debug1) {
/* 22 */     switch (debug1.length) {
/*    */       case 0:
/* 24 */         return ALWAYS_FALSE;
/*    */       case 1:
/* 26 */         return debug1[0];
/*    */       case 2:
/* 28 */         return debug1[0].or(debug1[1]);
/*    */     } 
/* 30 */     return (debug1, debug2) -> {
/*    */         for (ComposableEntryContainer debug6 : debug0) {
/*    */           if (debug6.expand(debug1, debug2)) {
/*    */             return true;
/*    */           }
/*    */         } 
/*    */         return false;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext debug1) {
/* 43 */     super.validate(debug1);
/*    */     
/* 45 */     for (int debug2 = 0; debug2 < this.children.length - 1; debug2++) {
/* 46 */       if (ArrayUtils.isEmpty((Object[])(this.children[debug2]).conditions))
/* 47 */         debug1.reportProblem("Unreachable entry!"); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 53 */     private final List<LootPoolEntryContainer> entries = Lists.newArrayList();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... debug1) {
/* 56 */       for (LootPoolEntryContainer.Builder<?> debug5 : debug1) {
/* 57 */         this.entries.add(debug5.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 63 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder otherwise(LootPoolEntryContainer.Builder<?> debug1) {
/* 68 */       this.entries.add(debug1.build());
/* 69 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 74 */       return new AlternativesEntry(this.entries.<LootPoolEntryContainer>toArray(new LootPoolEntryContainer[0]), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder alternatives(LootPoolEntryContainer.Builder<?>... debug0) {
/* 79 */     return new Builder(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\AlternativesEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */