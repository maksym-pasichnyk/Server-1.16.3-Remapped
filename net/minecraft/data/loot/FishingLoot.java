/*     */ package net.minecraft.data.loot;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.advancements.critereon.EntityPredicate;
/*     */ import net.minecraft.advancements.critereon.FishingHookPredicate;
/*     */ import net.minecraft.advancements.critereon.LocationPredicate;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootTableReference;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ 
/*     */ public class FishingLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
/*  34 */   public static final LootItemCondition.Builder IN_JUNGLE = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.JUNGLE));
/*  35 */   public static final LootItemCondition.Builder IN_JUNGLE_HILLS = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.JUNGLE_HILLS));
/*  36 */   public static final LootItemCondition.Builder IN_JUNGLE_EDGE = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.JUNGLE_EDGE));
/*  37 */   public static final LootItemCondition.Builder IN_BAMBOO_JUNGLE = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.BAMBOO_JUNGLE));
/*  38 */   public static final LootItemCondition.Builder IN_MODIFIED_JUNGLE = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.MODIFIED_JUNGLE));
/*  39 */   public static final LootItemCondition.Builder IN_MODIFIED_JUNGLE_EDGE = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.MODIFIED_JUNGLE_EDGE));
/*  40 */   public static final LootItemCondition.Builder IN_BAMBOO_JUNGLE_HILLS = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.BAMBOO_JUNGLE_HILLS));
/*     */ 
/*     */   
/*     */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/*  44 */     debug1.accept(BuiltInLootTables.FISHING, 
/*  45 */         LootTable.lootTable()
/*  46 */         .withPool(LootPool.lootPool()
/*  47 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootTableReference.lootTableReference(BuiltInLootTables.FISHING_JUNK).setWeight(10).setQuality(-2))
/*  49 */           .add(LootTableReference.lootTableReference(BuiltInLootTables.FISHING_TREASURE).setWeight(5).setQuality(2)
/*  50 */             .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().fishingHook(FishingHookPredicate.inOpenWater(true)))))
/*  51 */           .add((LootPoolEntryContainer.Builder)LootTableReference.lootTableReference(BuiltInLootTables.FISHING_FISH).setWeight(85).setQuality(-1))));
/*     */ 
/*     */ 
/*     */     
/*  55 */     debug1.accept(BuiltInLootTables.FISHING_FISH, 
/*  56 */         LootTable.lootTable()
/*  57 */         .withPool(LootPool.lootPool()
/*  58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(60))
/*  59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(25))
/*  60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TROPICAL_FISH).setWeight(2))
/*  61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUFFERFISH).setWeight(13))));
/*     */ 
/*     */ 
/*     */     
/*  65 */     debug1.accept(BuiltInLootTables.FISHING_JUNK, 
/*  66 */         LootTable.lootTable()
/*  67 */         .withPool(LootPool.lootPool()
/*  68 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LILY_PAD).setWeight(17))
/*  69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(10).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.0F, 0.9F))))
/*  70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(10))
/*  71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10))
/*  72 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(10)
/*  73 */             .apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:water")))))
/*  74 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(5))
/*  75 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(2).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.0F, 0.9F))))
/*  76 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOWL).setWeight(10))
/*  77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(5))
/*  78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.INK_SAC).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(10))))
/*  79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TRIPWIRE_HOOK).setWeight(10))
/*  80 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  81 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO)
/*  82 */             .when((LootItemCondition.Builder)IN_JUNGLE.or(IN_JUNGLE_HILLS).or(IN_JUNGLE_EDGE).or(IN_BAMBOO_JUNGLE).or(IN_MODIFIED_JUNGLE).or(IN_MODIFIED_JUNGLE_EDGE).or(IN_BAMBOO_JUNGLE_HILLS)))
/*  83 */             .setWeight(10))));
/*     */ 
/*     */ 
/*     */     
/*  87 */     debug1.accept(BuiltInLootTables.FISHING_TREASURE, 
/*  88 */         LootTable.lootTable()
/*  89 */         .withPool(LootPool.lootPool()
/*  90 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG))
/*  91 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE))
/*  92 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOW)
/*  93 */             .apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.0F, 0.25F)))
/*  94 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))
/*     */           
/*  96 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD)
/*  97 */             .apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage(RandomValueBounds.between(0.0F, 0.25F)))
/*  98 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))
/*     */           
/* 100 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK)
/* 101 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels((RandomIntGenerator)ConstantIntValue.exactly(30)).allowTreasure()))
/*     */           
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAUTILUS_SHELL))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\FishingLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */