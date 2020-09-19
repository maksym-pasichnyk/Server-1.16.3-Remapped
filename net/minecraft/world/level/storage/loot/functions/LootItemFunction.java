/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface LootItemFunction
/*    */   extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack> {
/*    */   LootItemFunctionType getType();
/*    */   
/*    */   static Consumer<ItemStack> decorate(BiFunction<ItemStack, LootContext, ItemStack> debug0, Consumer<ItemStack> debug1, LootContext debug2) {
/* 14 */     return debug3 -> debug0.accept(debug1.apply(debug3, debug2));
/*    */   }
/*    */   
/*    */   public static interface Builder {
/*    */     LootItemFunction build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */