/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface ComposableEntryContainer
/*    */ {
/*    */   public static final ComposableEntryContainer ALWAYS_FALSE = (debug0, debug1) -> false;
/*    */   public static final ComposableEntryContainer ALWAYS_TRUE = (debug0, debug1) -> true;
/*    */   
/*    */   default ComposableEntryContainer and(ComposableEntryContainer debug1) {
/* 16 */     Objects.requireNonNull(debug1);
/* 17 */     return (debug2, debug3) -> (expand(debug2, debug3) && debug1.expand(debug2, debug3));
/*    */   }
/*    */   
/*    */   default ComposableEntryContainer or(ComposableEntryContainer debug1) {
/* 21 */     Objects.requireNonNull(debug1);
/* 22 */     return (debug2, debug3) -> (expand(debug2, debug3) || debug1.expand(debug2, debug3));
/*    */   }
/*    */   
/*    */   boolean expand(LootContext paramLootContext, Consumer<LootPoolEntry> paramConsumer);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\ComposableEntryContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */