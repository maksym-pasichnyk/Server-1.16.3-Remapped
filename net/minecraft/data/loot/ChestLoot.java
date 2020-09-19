/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*     */ 
/*     */ public class ChestLoot
/*     */   implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
/*     */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/*  33 */     debug1.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*  34 */         LootTable.lootTable()
/*  35 */         .withPool(LootPool.lootPool()
/*  36 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  37 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/*  38 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/*  39 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(30))
/*  40 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  41 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  42 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5)))
/*     */         
/*  44 */         .withPool(LootPool.lootPool()
/*  45 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))
/*  46 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/*  47 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/*  49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/*  50 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/*  51 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))))
/*  52 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/*  53 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/*  54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/*  55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F)))))
/*     */         
/*  57 */         .withPool(LootPool.lootPool()
/*  58 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/*  59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.RAIL).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 8.0F))))
/*  60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POWERED_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/*  61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DETECTOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/*  62 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACTIVATOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/*  63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 16.0F))))));
/*     */ 
/*     */ 
/*     */     
/*  67 */     debug1.accept(BuiltInLootTables.BASTION_BRIDGE, 
/*  68 */         LootTable.lootTable()
/*  69 */         .withPool(LootPool.lootPool()
/*  70 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LODESTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1)))))
/*     */         
/*  73 */         .withPool(LootPool.lootPool()
/*  74 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))
/*  75 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.1F, 0.5F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  76 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(10.0F, 28.0F))))
/*  77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 12.0F))))
/*  78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))))
/*  79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/*  80 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/*  81 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/*  82 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/*  83 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  86 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*  87 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment())))
/*     */         
/*  89 */         .withPool(LootPool.lootPool()
/*  90 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))
/*  91 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 6.0F))))
/*  92 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/*  93 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 17.0F))))
/*  94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/*  95 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))));
/*     */ 
/*     */ 
/*     */     
/*  99 */     debug1.accept(BuiltInLootTables.BASTION_HOGLIN_STABLE, 
/* 100 */         LootTable.lootTable()
/* 101 */         .withPool(LootPool.lootPool()
/* 102 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.15F, 0.8F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(12).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.15F, 0.95F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 105 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 107 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))))
/* 108 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 109 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 110 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 17.0F))))
/* 111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1)))))
/*     */         
/* 113 */         .withPool(LootPool.lootPool()
/* 114 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 4.0F))
/* 115 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 117 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GLOWSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 6.0F))))
/* 118 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 119 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SOUL_SAND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 120 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_NYLIUM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 122 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 17.0F))))
/* 124 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))))
/* 125 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 126 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 127 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_FUNGUS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 128 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_ROOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 132 */     debug1.accept(BuiltInLootTables.BASTION_OTHER, 
/* 133 */         LootTable.lootTable()
/* 134 */         .withPool(LootPool.lootPool()
/* 135 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 136 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 139 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 140 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 141 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(10.0F, 22.0F))))
/* 142 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PIGLIN_BANNER_PATTERN).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 143 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_PIGSTEP).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 144 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(6.0F, 17.0F))))
/* 145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 146 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment(Enchantments.SOUL_SPEED))))
/*     */         
/* 148 */         .withPool(LootPool.lootPool()
/* 149 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(2))
/* 150 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(2).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 151 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 152 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment(Enchantments.SOUL_SPEED)))
/* 153 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 154 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 155 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 156 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 6.0F))))
/* 157 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 6.0F))))
/* 158 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 159 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 160 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 162 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 163 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F)))))
/*     */         
/* 165 */         .withPool(LootPool.lootPool()
/* 166 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 4.0F))
/* 167 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 168 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CHAIN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 10.0F))))
/* 169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/* 170 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BONE_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 6.0F))))
/* 171 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 172 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 6.0F))))
/* 173 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 174 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 6.0F))))
/* 175 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 17.0F))))
/* 176 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))));
/*     */ 
/*     */ 
/*     */     
/* 180 */     debug1.accept(BuiltInLootTables.BASTION_TREASURE, 
/* 181 */         LootTable.lootTable()
/* 182 */         .withPool(LootPool.lootPool()
/* 183 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/* 184 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 185 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 186 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))
/* 187 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))))
/* 188 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 189 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 190 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 191 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 192 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 193 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6))
/* 194 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 195 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(5))
/* 196 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(5))
/* 197 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(5))
/* 198 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/* 199 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1)))))
/*     */         
/* 201 */         .withPool(LootPool.lootPool()
/* 202 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 4.0F))
/* 203 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(12.0F, 25.0F))))
/* 204 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 205 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 206 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 9.0F))))
/* 207 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 9.0F))))
/* 208 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 5.0F))))
/* 209 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.QUARTZ).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 23.0F))))
/* 210 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 15.0F))))
/* 211 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 215 */     debug1.accept(BuiltInLootTables.BURIED_TREASURE, 
/* 216 */         LootTable.lootTable()
/* 217 */         .withPool(LootPool.lootPool()
/* 218 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 219 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEART_OF_THE_SEA)))
/*     */         
/* 221 */         .withPool(LootPool.lootPool()
/* 222 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(5.0F, 8.0F))
/* 223 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 224 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 225 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F)))))
/*     */         
/* 227 */         .withPool(LootPool.lootPool()
/* 228 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))
/* 229 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 8.0F))))
/* 230 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 231 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F)))))
/*     */         
/* 233 */         .withPool(LootPool.lootPool()
/* 234 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))
/* 235 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/* 236 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD)))
/*     */         
/* 238 */         .withPool(LootPool.lootPool()
/* 239 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(2))
/* 240 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 241 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_SALMON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 245 */     debug1.accept(BuiltInLootTables.DESERT_PYRAMID, 
/* 246 */         LootTable.lootTable()
/* 247 */         .withPool(LootPool.lootPool()
/* 248 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))
/* 249 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 250 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 251 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 252 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 253 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 6.0F))))
/* 254 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 255 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))))
/* 256 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(20))
/* 257 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/* 258 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 259 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 260 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/* 262 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 263 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(15)))
/*     */         
/* 265 */         .withPool(LootPool.lootPool()
/* 266 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(4))
/* 267 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 268 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 269 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 270 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 271 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SAND).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 275 */     debug1.accept(BuiltInLootTables.END_CITY_TREASURE, 
/* 276 */         LootTable.lootTable()
/* 277 */         .withPool(LootPool.lootPool()
/* 278 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))
/* 279 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 280 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 8.0F))))
/* 281 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 282 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/* 283 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 10.0F))))
/* 284 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 285 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 286 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 287 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 288 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 289 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 290 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 291 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 292 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 293 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 294 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 295 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 296 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 297 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 298 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 299 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 300 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))
/* 301 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)RandomValueBounds.between(20.0F, 39.0F)).allowTreasure()))));
/*     */ 
/*     */ 
/*     */     
/* 305 */     debug1.accept(BuiltInLootTables.IGLOO_CHEST, 
/* 306 */         LootTable.lootTable()
/* 307 */         .withPool(LootPool.lootPool()
/* 308 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))
/* 309 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 310 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 311 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 312 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/* 313 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/* 314 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/* 315 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F)))))
/*     */         
/* 317 */         .withPool(LootPool.lootPool()
/* 318 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 319 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))));
/*     */ 
/*     */ 
/*     */     
/* 323 */     debug1.accept(BuiltInLootTables.JUNGLE_TEMPLE, 
/* 324 */         LootTable.lootTable()
/* 325 */         .withPool(LootPool.lootPool()
/* 326 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))
/* 327 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 328 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 329 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 330 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 331 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 332 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 6.0F))))
/* 333 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))))
/* 334 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 335 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 336 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 337 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 338 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))));
/*     */ 
/*     */ 
/*     */     
/* 342 */     debug1.accept(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER, 
/* 343 */         LootTable.lootTable()
/* 344 */         .withPool(LootPool.lootPool()
/* 345 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))
/* 346 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(30).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 350 */     debug1.accept(BuiltInLootTables.NETHER_BRIDGE, 
/* 351 */         LootTable.lootTable()
/* 352 */         .withPool(LootPool.lootPool()
/* 353 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))
/* 354 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 355 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 356 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 357 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(5))
/* 358 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(5))
/* 359 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(5))
/* 360 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_WART).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))))
/* 361 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(10))
/* 362 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(8))
/* 363 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(5))
/* 364 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(3))
/* 365 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 369 */     debug1.accept(BuiltInLootTables.PILLAGER_OUTPOST, 
/* 370 */         LootTable.lootTable()
/* 371 */         .withPool(LootPool.lootPool()
/* 372 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))
/* 373 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW)))
/*     */         
/* 375 */         .withPool(LootPool.lootPool()
/* 376 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F))
/* 377 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 5.0F))))
/* 378 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))))
/* 379 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 5.0F)))))
/*     */         
/* 381 */         .withPool(LootPool.lootPool()
/* 382 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))
/* 383 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F)))))
/*     */         
/* 385 */         .withPool(LootPool.lootPool()
/* 386 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F))
/* 387 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 388 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 6.0F))))
/* 389 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 390 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIPWIRE_HOOK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 391 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 392 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))));
/*     */ 
/*     */ 
/*     */     
/* 396 */     debug1.accept(BuiltInLootTables.SHIPWRECK_MAP, 
/* 397 */         LootTable.lootTable()
/* 398 */         .withPool(LootPool.lootPool()
/* 399 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 400 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureFeature.BURIED_TREASURE).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(false))))
/*     */         
/* 402 */         .withPool(LootPool.lootPool()
/* 403 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/* 404 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 405 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 406 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK))
/* 407 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 10.0F))))
/* 408 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 409 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 413 */     debug1.accept(BuiltInLootTables.SHIPWRECK_SUPPLY, 
/* 414 */         LootTable.lootTable()
/* 415 */         .withPool(LootPool.lootPool()
/* 416 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 10.0F))
/* 417 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 12.0F))))
/* 418 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/* 419 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POISONOUS_POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))))
/* 420 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 8.0F))))
/* 421 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(8.0F, 21.0F))))
/* 422 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).setWeight(10).apply((LootItemFunction.Builder)SetStewEffectFunction.stewEffect()
/* 423 */               .withEffect(MobEffects.NIGHT_VISION, RandomValueBounds.between(7.0F, 10.0F))
/* 424 */               .withEffect(MobEffects.JUMP, RandomValueBounds.between(7.0F, 10.0F))
/* 425 */               .withEffect(MobEffects.WEAKNESS, RandomValueBounds.between(6.0F, 8.0F))
/* 426 */               .withEffect(MobEffects.BLINDNESS, RandomValueBounds.between(5.0F, 7.0F))
/* 427 */               .withEffect(MobEffects.POISON, RandomValueBounds.between(10.0F, 20.0F))
/* 428 */               .withEffect(MobEffects.SATURATION, RandomValueBounds.between(7.0F, 10.0F))))
/*     */           
/* 430 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/* 431 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(5.0F, 24.0F))))
/* 432 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.PUMPKIN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 433 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 434 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 435 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 436 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 437 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 438 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 439 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))));
/*     */ 
/*     */ 
/*     */     
/* 443 */     debug1.accept(BuiltInLootTables.SHIPWRECK_TREASURE, 
/* 444 */         LootTable.lootTable()
/* 445 */         .withPool(LootPool.lootPool()
/* 446 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 6.0F))
/* 447 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(90).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 448 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 449 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 450 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5))
/* 451 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(5)))
/*     */         
/* 453 */         .withPool(LootPool.lootPool()
/* 454 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))
/* 455 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(50).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 10.0F))))
/* 456 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 10.0F))))
/* 457 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 10.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 461 */     debug1.accept(BuiltInLootTables.SIMPLE_DUNGEON, 
/* 462 */         LootTable.lootTable()
/* 463 */         .withPool(LootPool.lootPool()
/* 464 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))
/* 465 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(20))
/* 466 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/* 467 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 468 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/* 469 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/* 470 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(20))
/* 471 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 472 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/* 473 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 474 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment())))
/*     */         
/* 476 */         .withPool(LootPool.lootPool()
/* 477 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))
/* 478 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 479 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 480 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/* 481 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 482 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/* 483 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 484 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 485 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 486 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 487 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F)))))
/*     */         
/* 489 */         .withPool(LootPool.lootPool()
/* 490 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/* 491 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 492 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 493 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 494 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 498 */     debug1.accept(BuiltInLootTables.SPAWN_BONUS_CHEST, 
/* 499 */         LootTable.lootTable()
/* 500 */         .withPool(LootPool.lootPool()
/* 501 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 502 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 503 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_AXE).setWeight(3)))
/*     */         
/* 505 */         .withPool(LootPool.lootPool()
/* 506 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 507 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/* 508 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_PICKAXE).setWeight(3)))
/*     */         
/* 510 */         .withPool(LootPool.lootPool()
/* 511 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/* 512 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 513 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 514 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F)))))
/*     */         
/* 516 */         .withPool(LootPool.lootPool()
/* 517 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(4))
/* 518 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 12.0F))))
/* 519 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_PLANKS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 12.0F))))
/* 520 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 521 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 522 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BIRCH_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 523 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.JUNGLE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 524 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 525 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 529 */     debug1.accept(BuiltInLootTables.STRONGHOLD_CORRIDOR, 
/* 530 */         LootTable.lootTable()
/* 531 */         .withPool(LootPool.lootPool()
/* 532 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F))
/* 533 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).setWeight(10))
/* 534 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 535 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 536 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 537 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/* 538 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 539 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 540 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/* 541 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/* 542 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/* 543 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/* 544 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/* 545 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/* 546 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/* 547 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE))
/* 548 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 549 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 550 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 551 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))));
/*     */ 
/*     */ 
/*     */     
/* 555 */     debug1.accept(BuiltInLootTables.STRONGHOLD_CROSSING, 
/* 556 */         LootTable.lootTable()
/* 557 */         .withPool(LootPool.lootPool()
/* 558 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))
/* 559 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 560 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 561 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))))
/* 562 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))))
/* 563 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 564 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 565 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE))
/* 566 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))));
/*     */ 
/*     */ 
/*     */     
/* 570 */     debug1.accept(BuiltInLootTables.STRONGHOLD_LIBRARY, 
/* 571 */         LootTable.lootTable()
/* 572 */         .withPool(LootPool.lootPool()
/* 573 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 10.0F))
/* 574 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 575 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 7.0F))))
/* 576 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 577 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 578 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))));
/*     */ 
/*     */ 
/*     */     
/* 582 */     debug1.accept(BuiltInLootTables.UNDERWATER_RUIN_BIG, 
/* 583 */         LootTable.lootTable()
/* 584 */         .withPool(LootPool.lootPool()
/* 585 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))
/* 586 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 587 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 588 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/* 589 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F)))))
/*     */         
/* 591 */         .withPool(LootPool.lootPool()
/* 592 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 593 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/* 594 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 595 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/* 596 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/* 597 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 598 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureFeature.BURIED_TREASURE).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(false)))));
/*     */ 
/*     */ 
/*     */     
/* 602 */     debug1.accept(BuiltInLootTables.UNDERWATER_RUIN_SMALL, 
/* 603 */         LootTable.lootTable()
/* 604 */         .withPool(LootPool.lootPool()
/* 605 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))
/* 606 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 607 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/* 608 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5))
/* 609 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/* 610 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F)))))
/*     */         
/* 612 */         .withPool(LootPool.lootPool()
/* 613 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 614 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/* 615 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/* 616 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 617 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(5).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureFeature.BURIED_TREASURE).setMapDecoration(MapDecoration.Type.RED_X).setZoom((byte)1).setSkipKnownStructures(false)))));
/*     */ 
/*     */ 
/*     */     
/* 621 */     debug1.accept(BuiltInLootTables.VILLAGE_WEAPONSMITH, 
/* 622 */         LootTable.lootTable()
/* 623 */         .withPool(LootPool.lootPool()
/* 624 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 625 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 626 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 627 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 628 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 629 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 630 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/* 631 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/* 632 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/* 633 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/* 634 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/* 635 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/* 636 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))))
/* 637 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))))
/* 638 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 639 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 640 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 641 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))));
/*     */ 
/*     */ 
/*     */     
/* 645 */     debug1.accept(BuiltInLootTables.VILLAGE_TOOLSMITH, 
/* 646 */         LootTable.lootTable()
/* 647 */         .withPool(LootPool.lootPool()
/* 648 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 649 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 650 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 651 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 652 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 653 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/* 654 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 655 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 656 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(5))));
/*     */ 
/*     */ 
/*     */     
/* 660 */     debug1.accept(BuiltInLootTables.VILLAGE_CARTOGRAPHER, 
/* 661 */         LootTable.lootTable()
/* 662 */         .withPool(LootPool.lootPool()
/* 663 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 664 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 665 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 666 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(5))
/* 667 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 668 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 672 */     debug1.accept(BuiltInLootTables.VILLAGE_MASON, 
/* 673 */         LootTable.lootTable()
/* 674 */         .withPool(LootPool.lootPool()
/* 675 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 676 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 677 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOWER_POT).setWeight(1))
/* 678 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE).setWeight(2))
/* 679 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE_BRICKS).setWeight(2))
/* 680 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 681 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_DYE).setWeight(1))
/* 682 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SMOOTH_STONE).setWeight(1))
/* 683 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*     */ 
/*     */ 
/*     */     
/* 687 */     debug1.accept(BuiltInLootTables.VILLAGE_ARMORER, 
/* 688 */         LootTable.lootTable()
/* 689 */         .withPool(LootPool.lootPool()
/* 690 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 691 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 692 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 693 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(1))
/* 694 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*     */ 
/*     */ 
/*     */     
/* 698 */     debug1.accept(BuiltInLootTables.VILLAGE_SHEPHERD, 
/* 699 */         LootTable.lootTable()
/* 700 */         .withPool(LootPool.lootPool()
/* 701 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 702 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WHITE_WOOL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 703 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BLACK_WOOL).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 704 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GRAY_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 705 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BROWN_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 706 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LIGHT_GRAY_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 707 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/* 708 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHEARS).setWeight(1))
/* 709 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 6.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 713 */     debug1.accept(BuiltInLootTables.VILLAGE_BUTCHER, 
/* 714 */         LootTable.lootTable()
/* 715 */         .withPool(LootPool.lootPool()
/* 716 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 717 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/* 718 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 719 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 720 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 721 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 722 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 726 */     debug1.accept(BuiltInLootTables.VILLAGE_FLETCHER, 
/* 727 */         LootTable.lootTable()
/* 728 */         .withPool(LootPool.lootPool()
/* 729 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 730 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/* 731 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 732 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 733 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EGG).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 734 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 735 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 739 */     debug1.accept(BuiltInLootTables.VILLAGE_FISHER, 
/* 740 */         LootTable.lootTable()
/* 741 */         .withPool(LootPool.lootPool()
/* 742 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 743 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/* 744 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 745 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 746 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 747 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BARREL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 748 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 749 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 753 */     debug1.accept(BuiltInLootTables.VILLAGE_TANNERY, 
/* 754 */         LootTable.lootTable()
/* 755 */         .withPool(LootPool.lootPool()
/* 756 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))
/* 757 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 758 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(2))
/* 759 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(2))
/* 760 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(2))
/* 761 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 762 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(2))
/* 763 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/* 764 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 768 */     debug1.accept(BuiltInLootTables.VILLAGE_TEMPLE, 
/* 769 */         LootTable.lootTable()
/* 770 */         .withPool(LootPool.lootPool()
/* 771 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 772 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 773 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 774 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 775 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 776 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 777 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 781 */     debug1.accept(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, 
/* 782 */         LootTable.lootTable()
/* 783 */         .withPool(LootPool.lootPool()
/* 784 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 785 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 786 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DANDELION).setWeight(2))
/* 787 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POPPY).setWeight(1))
/* 788 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 789 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 790 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 791 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/* 792 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(1))
/* 793 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 794 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 798 */     debug1.accept(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, 
/* 799 */         LootTable.lootTable()
/* 800 */         .withPool(LootPool.lootPool()
/* 801 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 802 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 803 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FERN).setWeight(2))
/* 804 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LARGE_FERN).setWeight(2))
/* 805 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 806 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SWEET_BERRIES).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 807 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 808 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 809 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE).setWeight(1))
/* 810 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 811 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 812 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_SIGN).setWeight(1))
/* 813 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_LOG).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 817 */     debug1.accept(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, 
/* 818 */         LootTable.lootTable()
/* 819 */         .withPool(LootPool.lootPool()
/* 820 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 821 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 822 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GRASS).setWeight(5))
/* 823 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TALL_GRASS).setWeight(5))
/* 824 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 825 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 826 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 827 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_SAPLING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 828 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/* 829 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 830 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(1))));
/*     */ 
/*     */ 
/*     */     
/* 834 */     debug1.accept(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, 
/* 835 */         LootTable.lootTable()
/* 836 */         .withPool(LootPool.lootPool()
/* 837 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 838 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BLUE_ICE).setWeight(1))
/* 839 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SNOW_BLOCK).setWeight(4))
/* 840 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 841 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 842 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 5.0F))))
/* 843 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SOUP).setWeight(1))
/* 844 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FURNACE).setWeight(1))
/* 845 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 846 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 847 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 851 */     debug1.accept(BuiltInLootTables.VILLAGE_DESERT_HOUSE, 
/* 852 */         LootTable.lootTable()
/* 853 */         .withPool(LootPool.lootPool()
/* 854 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(3.0F, 8.0F))
/* 855 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1))
/* 856 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GREEN_DYE).setWeight(1))
/* 857 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CACTUS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 858 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 7.0F))))
/* 859 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 860 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/* 861 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DEAD_BUSH).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))
/* 862 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 866 */     debug1.accept(BuiltInLootTables.WOODLAND_MANSION, 
/* 867 */         LootTable.lootTable()
/* 868 */         .withPool(LootPool.lootPool()
/* 869 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))
/* 870 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(20))
/* 871 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/* 872 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 873 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/* 874 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/* 875 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(20))
/* 876 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE).setWeight(10))
/* 877 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(15))
/* 878 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 879 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment())))
/*     */         
/* 881 */         .withPool(LootPool.lootPool()
/* 882 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))
/* 883 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 884 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 885 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/* 886 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 887 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/* 888 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 889 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 890 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 891 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))))
/* 892 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F)))))
/*     */         
/* 894 */         .withPool(LootPool.lootPool()
/* 895 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(3))
/* 896 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 897 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 898 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))
/* 899 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 8.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 903 */     debug1.accept(BuiltInLootTables.RUINED_PORTAL, 
/* 904 */         LootTable.lootTable()
/* 905 */         .withPool(LootPool.lootPool()
/* 906 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(4.0F, 8.0F))
/* 907 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OBSIDIAN).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))
/* 908 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 4.0F))))
/* 909 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(9.0F, 18.0F))))
/* 910 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(40))
/* 911 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).setWeight(40))
/*     */           
/* 913 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/* 914 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 24.0F))))
/* 915 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 916 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 917 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HOE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 918 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 919 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_PICKAXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 920 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 921 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 922 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/* 923 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment()))
/*     */           
/* 925 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLISTERING_MELON_SLICE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 12.0F))))
/* 926 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(5))
/* 927 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5))
/* 928 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 12.0F))))
/* 929 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK).setWeight(5))
/* 930 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 8.0F))))
/*     */           
/* 932 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BELL).setWeight(1))
/* 933 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
/* 934 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_BLOCK).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\ChestLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */