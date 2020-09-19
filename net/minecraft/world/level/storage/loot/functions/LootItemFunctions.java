/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Serializer;
/*    */ 
/*    */ public class LootItemFunctions {
/*    */   static {
/* 13 */     IDENTITY = ((debug0, debug1) -> debug0);
/*    */   }
/* 15 */   public static final LootItemFunctionType SET_COUNT = register("set_count", new SetItemCountFunction.Serializer()); public static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY;
/* 16 */   public static final LootItemFunctionType ENCHANT_WITH_LEVELS = register("enchant_with_levels", new EnchantWithLevelsFunction.Serializer());
/* 17 */   public static final LootItemFunctionType ENCHANT_RANDOMLY = register("enchant_randomly", new EnchantRandomlyFunction.Serializer());
/* 18 */   public static final LootItemFunctionType SET_NBT = register("set_nbt", new SetNbtFunction.Serializer());
/* 19 */   public static final LootItemFunctionType FURNACE_SMELT = register("furnace_smelt", new SmeltItemFunction.Serializer());
/* 20 */   public static final LootItemFunctionType LOOTING_ENCHANT = register("looting_enchant", new LootingEnchantFunction.Serializer());
/* 21 */   public static final LootItemFunctionType SET_DAMAGE = register("set_damage", new SetItemDamageFunction.Serializer());
/* 22 */   public static final LootItemFunctionType SET_ATTRIBUTES = register("set_attributes", new SetAttributesFunction.Serializer());
/* 23 */   public static final LootItemFunctionType SET_NAME = register("set_name", new SetNameFunction.Serializer());
/* 24 */   public static final LootItemFunctionType EXPLORATION_MAP = register("exploration_map", new ExplorationMapFunction.Serializer());
/* 25 */   public static final LootItemFunctionType SET_STEW_EFFECT = register("set_stew_effect", new SetStewEffectFunction.Serializer());
/* 26 */   public static final LootItemFunctionType COPY_NAME = register("copy_name", new CopyNameFunction.Serializer());
/* 27 */   public static final LootItemFunctionType SET_CONTENTS = register("set_contents", new SetContainerContents.Serializer());
/* 28 */   public static final LootItemFunctionType LIMIT_COUNT = register("limit_count", new LimitCount.Serializer());
/* 29 */   public static final LootItemFunctionType APPLY_BONUS = register("apply_bonus", new ApplyBonusCount.Serializer());
/* 30 */   public static final LootItemFunctionType SET_LOOT_TABLE = register("set_loot_table", new SetContainerLootTable.Serializer());
/* 31 */   public static final LootItemFunctionType EXPLOSION_DECAY = register("explosion_decay", new ApplyExplosionDecay.Serializer());
/* 32 */   public static final LootItemFunctionType SET_LORE = register("set_lore", new SetLoreFunction.Serializer());
/* 33 */   public static final LootItemFunctionType FILL_PLAYER_HEAD = register("fill_player_head", new FillPlayerHead.Serializer());
/* 34 */   public static final LootItemFunctionType COPY_NBT = register("copy_nbt", new CopyNbtFunction.Serializer());
/* 35 */   public static final LootItemFunctionType COPY_STATE = register("copy_state", new CopyBlockState.Serializer());
/*    */   
/*    */   private static LootItemFunctionType register(String debug0, Serializer<? extends LootItemFunction> debug1) {
/* 38 */     return (LootItemFunctionType)Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(debug0), new LootItemFunctionType(debug1));
/*    */   }
/*    */   
/*    */   public static Object createGsonAdapter() {
/* 42 */     return GsonAdapterFactory.builder(Registry.LOOT_FUNCTION_TYPE, "function", "function", LootItemFunction::getType).build();
/*    */   } public static BiFunction<ItemStack, LootContext, ItemStack> compose(BiFunction<ItemStack, LootContext, ItemStack>[] debug0) {
/*    */     BiFunction<ItemStack, LootContext, ItemStack> debug1;
/*    */     BiFunction<ItemStack, LootContext, ItemStack> debug2;
/* 46 */     switch (debug0.length) {
/*    */       case 0:
/* 48 */         return IDENTITY;
/*    */       case 1:
/* 50 */         return debug0[0];
/*    */       case 2:
/* 52 */         debug1 = debug0[0];
/* 53 */         debug2 = debug0[1];
/* 54 */         return (debug2, debug3) -> (ItemStack)debug0.apply(debug1.apply(debug2, debug3), debug3);
/*    */     } 
/*    */     
/* 57 */     return (debug1, debug2) -> {
/*    */         for (BiFunction<ItemStack, LootContext, ItemStack> debug6 : debug0)
/*    */           debug1 = debug6.apply(debug1, debug2); 
/*    */         return debug1;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */