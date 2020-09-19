/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SequentialEntry
/*    */   extends CompositeEntryBase {
/*    */   SequentialEntry(LootPoolEntryContainer[] debug1, LootItemCondition[] debug2) {
/* 10 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 15 */     return LootPoolEntries.SEQUENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(ComposableEntryContainer[] debug1) {
/* 20 */     switch (debug1.length) {
/*    */       case 0:
/* 22 */         return ALWAYS_TRUE;
/*    */       case 1:
/* 24 */         return debug1[0];
/*    */       case 2:
/* 26 */         return debug1[0].and(debug1[1]);
/*    */     } 
/* 28 */     return (debug1, debug2) -> {
/*    */         for (ComposableEntryContainer debug6 : debug0) {
/*    */           if (!debug6.expand(debug1, debug2))
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\SequentialEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */