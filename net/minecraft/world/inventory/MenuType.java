/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MenuType<T extends AbstractContainerMenu>
/*    */ {
/* 11 */   public static final MenuType<ChestMenu> GENERIC_9x1 = register("generic_9x1", ChestMenu::oneRow);
/* 12 */   public static final MenuType<ChestMenu> GENERIC_9x2 = register("generic_9x2", ChestMenu::twoRows);
/* 13 */   public static final MenuType<ChestMenu> GENERIC_9x3 = register("generic_9x3", ChestMenu::threeRows);
/* 14 */   public static final MenuType<ChestMenu> GENERIC_9x4 = register("generic_9x4", ChestMenu::fourRows);
/* 15 */   public static final MenuType<ChestMenu> GENERIC_9x5 = register("generic_9x5", ChestMenu::fiveRows);
/* 16 */   public static final MenuType<ChestMenu> GENERIC_9x6 = register("generic_9x6", ChestMenu::sixRows);
/* 17 */   public static final MenuType<DispenserMenu> GENERIC_3x3 = register("generic_3x3", DispenserMenu::new);
/*    */   
/* 19 */   public static final MenuType<AnvilMenu> ANVIL = register("anvil", AnvilMenu::new);
/* 20 */   public static final MenuType<BeaconMenu> BEACON = register("beacon", BeaconMenu::new);
/* 21 */   public static final MenuType<BlastFurnaceMenu> BLAST_FURNACE = register("blast_furnace", BlastFurnaceMenu::new);
/* 22 */   public static final MenuType<BrewingStandMenu> BREWING_STAND = register("brewing_stand", BrewingStandMenu::new);
/* 23 */   public static final MenuType<CraftingMenu> CRAFTING = register("crafting", CraftingMenu::new);
/* 24 */   public static final MenuType<EnchantmentMenu> ENCHANTMENT = register("enchantment", EnchantmentMenu::new);
/* 25 */   public static final MenuType<FurnaceMenu> FURNACE = register("furnace", FurnaceMenu::new);
/* 26 */   public static final MenuType<GrindstoneMenu> GRINDSTONE = register("grindstone", GrindstoneMenu::new); public static final MenuType<LecternMenu> LECTERN;
/* 27 */   public static final MenuType<HopperMenu> HOPPER = register("hopper", HopperMenu::new); static {
/* 28 */     LECTERN = register("lectern", (debug0, debug1) -> new LecternMenu(debug0));
/* 29 */   } public static final MenuType<LoomMenu> LOOM = register("loom", LoomMenu::new);
/* 30 */   public static final MenuType<MerchantMenu> MERCHANT = register("merchant", MerchantMenu::new);
/* 31 */   public static final MenuType<ShulkerBoxMenu> SHULKER_BOX = register("shulker_box", ShulkerBoxMenu::new);
/* 32 */   public static final MenuType<SmithingMenu> SMITHING = register("smithing", SmithingMenu::new);
/* 33 */   public static final MenuType<SmokerMenu> SMOKER = register("smoker", SmokerMenu::new);
/* 34 */   public static final MenuType<CartographyTableMenu> CARTOGRAPHY_TABLE = register("cartography_table", CartographyTableMenu::new);
/* 35 */   public static final MenuType<StonecutterMenu> STONECUTTER = register("stonecutter", StonecutterMenu::new); private final MenuSupplier<T> constructor;
/*    */   
/*    */   private static <T extends AbstractContainerMenu> MenuType<T> register(String debug0, MenuSupplier<T> debug1) {
/* 38 */     return (MenuType<T>)Registry.register(Registry.MENU, debug0, new MenuType<>(debug1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private MenuType(MenuSupplier<T> debug1) {
/* 44 */     this.constructor = debug1;
/*    */   }
/*    */   
/*    */   static interface MenuSupplier<T extends AbstractContainerMenu> {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\MenuType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */