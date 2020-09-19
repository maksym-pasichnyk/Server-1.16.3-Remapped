/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
/*     */ 
/*     */ public class GiftLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
/*     */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/*  24 */     debug1.accept(BuiltInLootTables.CAT_MORNING_GIFT, 
/*  25 */         LootTable.lootTable()
/*  26 */         .withPool(LootPool.lootPool()
/*  27 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  28 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).setWeight(10))
/*  29 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT).setWeight(10))
/*  30 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).setWeight(10))
/*  31 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10))
/*  32 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  33 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10))
/*  34 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).setWeight(2))));
/*     */ 
/*     */     
/*  37 */     debug1.accept(BuiltInLootTables.ARMORER_GIFT, 
/*  38 */         LootTable.lootTable()
/*  39 */         .withPool(LootPool.lootPool()
/*  40 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  41 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_HELMET))
/*  42 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/*  43 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_LEGGINGS))
/*  44 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_BOOTS))));
/*     */ 
/*     */     
/*  47 */     debug1.accept(BuiltInLootTables.BUTCHER_GIFT, 
/*  48 */         LootTable.lootTable()
/*  49 */         .withPool(LootPool.lootPool()
/*  50 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  51 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_RABBIT))
/*  52 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_CHICKEN))
/*  53 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP))
/*  54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_BEEF))
/*  55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_MUTTON))));
/*     */ 
/*     */     
/*  58 */     debug1.accept(BuiltInLootTables.CARTOGRAPHER_GIFT, 
/*  59 */         LootTable.lootTable()
/*  60 */         .withPool(LootPool.lootPool()
/*  61 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  62 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/*  63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER))));
/*     */ 
/*     */     
/*  66 */     debug1.accept(BuiltInLootTables.CLERIC_GIFT, 
/*  67 */         LootTable.lootTable()
/*  68 */         .withPool(LootPool.lootPool()
/*  69 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE))
/*  71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI))));
/*     */ 
/*     */     
/*  74 */     debug1.accept(BuiltInLootTables.FARMER_GIFT, 
/*  75 */         LootTable.lootTable()
/*  76 */         .withPool(LootPool.lootPool()
/*  77 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD))
/*  79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE))
/*  80 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKIE))));
/*     */ 
/*     */ 
/*     */     
/*  84 */     debug1.accept(BuiltInLootTables.FISHERMAN_GIFT, 
/*  85 */         LootTable.lootTable()
/*  86 */         .withPool(LootPool.lootPool()
/*  87 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  88 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD))
/*  89 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON))));
/*     */ 
/*     */     
/*  92 */     debug1.accept(BuiltInLootTables.FLETCHER_GIFT, 
/*  93 */         LootTable.lootTable()
/*  94 */         .withPool(LootPool.lootPool()
/*  95 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  96 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(26))
/*  97 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:swiftness")))))
/*  98 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:slowness")))))
/*  99 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:strength")))))
/* 100 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:healing")))))
/* 101 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:harming")))))
/* 102 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:leaping")))))
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:regeneration")))))
/* 104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:fire_resistance")))))
/* 105 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:water_breathing")))))
/* 106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:invisibility")))))
/* 107 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:night_vision")))))
/* 108 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:weakness")))))
/* 109 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:poison")))))));
/*     */ 
/*     */     
/* 112 */     debug1.accept(BuiltInLootTables.LEATHERWORKER_GIFT, 
/* 113 */         LootTable.lootTable()
/* 114 */         .withPool(LootPool.lootPool()
/* 115 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER))));
/*     */ 
/*     */     
/* 119 */     debug1.accept(BuiltInLootTables.LIBRARIAN_GIFT, 
/* 120 */         LootTable.lootTable()
/* 121 */         .withPool(LootPool.lootPool()
/* 122 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK))));
/*     */ 
/*     */     
/* 126 */     debug1.accept(BuiltInLootTables.MASON_GIFT, 
/* 127 */         LootTable.lootTable()
/* 128 */         .withPool(LootPool.lootPool()
/* 129 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 130 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY))));
/*     */ 
/*     */     
/* 133 */     debug1.accept(BuiltInLootTables.SHEPHERD_GIFT, 
/* 134 */         LootTable.lootTable()
/* 135 */         .withPool(LootPool.lootPool()
/* 136 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHITE_WOOL))
/* 138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ORANGE_WOOL))
/* 139 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGENTA_WOOL))
/* 140 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_BLUE_WOOL))
/* 141 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_WOOL))
/* 142 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIME_WOOL))
/* 143 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PINK_WOOL))
/* 144 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GRAY_WOOL))
/* 145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_GRAY_WOOL))
/* 146 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CYAN_WOOL))
/* 147 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PURPLE_WOOL))
/* 148 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLUE_WOOL))
/* 149 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_WOOL))
/* 150 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GREEN_WOOL))
/* 151 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_WOOL))
/* 152 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLACK_WOOL))));
/*     */ 
/*     */     
/* 155 */     debug1.accept(BuiltInLootTables.TOOLSMITH_GIFT, 
/* 156 */         LootTable.lootTable()
/* 157 */         .withPool(LootPool.lootPool()
/* 158 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 159 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/* 160 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_HOE))
/* 162 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SHOVEL))));
/*     */ 
/*     */     
/* 165 */     debug1.accept(BuiltInLootTables.WEAPONSMITH_GIFT, 
/* 166 */         LootTable.lootTable()
/* 167 */         .withPool(LootPool.lootPool()
/* 168 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 170 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE))
/* 171 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\GiftLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */