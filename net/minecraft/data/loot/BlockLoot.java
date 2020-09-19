/*      */ package net.minecraft.data.loot;
/*      */ 
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.critereon.BlockPredicate;
/*      */ import net.minecraft.advancements.critereon.EnchantmentPredicate;
/*      */ import net.minecraft.advancements.critereon.ItemPredicate;
/*      */ import net.minecraft.advancements.critereon.LocationPredicate;
/*      */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*      */ import net.minecraft.advancements.critereon.StatePropertiesPredicate;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.BeehiveBlock;
/*      */ import net.minecraft.world.level.block.BeetrootBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CarrotBlock;
/*      */ import net.minecraft.world.level.block.CocoaBlock;
/*      */ import net.minecraft.world.level.block.ComposterBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DoorBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.FlowerPotBlock;
/*      */ import net.minecraft.world.level.block.NetherWartBlock;
/*      */ import net.minecraft.world.level.block.PotatoBlock;
/*      */ import net.minecraft.world.level.block.SeaPickleBlock;
/*      */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*      */ import net.minecraft.world.level.block.SlabBlock;
/*      */ import net.minecraft.world.level.block.SnowLayerBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.TntBlock;
/*      */ import net.minecraft.world.level.block.state.properties.BedPart;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.storage.loot.BinomialDistributionGenerator;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*      */ import net.minecraft.world.level.storage.loot.IntLimiter;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootPool;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*      */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*      */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*      */ import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*      */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*      */ import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
/*      */ import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
/*      */ import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*      */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*      */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*      */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*      */ import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BlockLoot
/*      */   implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>
/*      */ {
/*   96 */   private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
/*   97 */   private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
/*      */   
/*   99 */   private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of((ItemLike)Items.SHEARS));
/*  100 */   private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = (LootItemCondition.Builder)HAS_SHEARS.or(HAS_SILK_TOUCH);
/*      */   
/*  102 */   private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();
/*      */   
/*  104 */   private static final Set<Item> EXPLOSION_RESISTANT = (Set<Item>)Stream.<Block>of(new Block[] { Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  135 */       }).map(ItemLike::asItem).collect(ImmutableSet.toImmutableSet());
/*      */   
/*  137 */   private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
/*  138 */   private static final float[] JUNGLE_LEAVES_SAPLING_CHANGES = new float[] { 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F };
/*      */   
/*  140 */   private final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();
/*      */   
/*      */   private static <T> T applyExplosionDecay(ItemLike debug0, FunctionUserBuilder<T> debug1) {
/*  143 */     if (!EXPLOSION_RESISTANT.contains(debug0.asItem())) {
/*  144 */       return (T)debug1.apply((LootItemFunction.Builder)ApplyExplosionDecay.explosionDecay());
/*      */     }
/*      */     
/*  147 */     return (T)debug1.unwrap();
/*      */   }
/*      */   
/*      */   private static <T> T applyExplosionCondition(ItemLike debug0, ConditionUserBuilder<T> debug1) {
/*  151 */     if (!EXPLOSION_RESISTANT.contains(debug0.asItem())) {
/*  152 */       return (T)debug1.when(ExplosionCondition.survivesExplosion());
/*      */     }
/*      */     
/*  155 */     return (T)debug1.unwrap();
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createSingleItemTable(ItemLike debug0) {
/*  159 */     return LootTable.lootTable()
/*  160 */       .withPool(applyExplosionCondition(debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  161 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  162 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(debug0))));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createSelfDropDispatchTable(Block debug0, LootItemCondition.Builder debug1, LootPoolEntryContainer.Builder<?> debug2) {
/*  167 */     return LootTable.lootTable()
/*  168 */       .withPool(LootPool.lootPool()
/*  169 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  170 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  171 */           .when(debug1))
/*  172 */           .otherwise(debug2)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createSilkTouchDispatchTable(Block debug0, LootPoolEntryContainer.Builder<?> debug1) {
/*  178 */     return createSelfDropDispatchTable(debug0, HAS_SILK_TOUCH, debug1);
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createShearsDispatchTable(Block debug0, LootPoolEntryContainer.Builder<?> debug1) {
/*  182 */     return createSelfDropDispatchTable(debug0, HAS_SHEARS, debug1);
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createSilkTouchOrShearsDispatchTable(Block debug0, LootPoolEntryContainer.Builder<?> debug1) {
/*  186 */     return createSelfDropDispatchTable(debug0, HAS_SHEARS_OR_SILK_TOUCH, debug1);
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createSingleItemTableWithSilkTouch(Block debug0, ItemLike debug1) {
/*  190 */     return createSilkTouchDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(debug1)));
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createSingleItemTable(ItemLike debug0, RandomIntGenerator debug1) {
/*  194 */     return LootTable.lootTable()
/*  195 */       .withPool(LootPool.lootPool()
/*  196 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  197 */         .add(applyExplosionDecay(debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(debug0).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(debug1)))));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createSingleItemTableWithSilkTouch(Block debug0, ItemLike debug1, RandomIntGenerator debug2) {
/*  202 */     return createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(debug1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(debug2))));
/*      */   }
/*      */   
/*      */   private static LootTable.Builder createSilkTouchOnlyTable(ItemLike debug0) {
/*  206 */     return LootTable.lootTable()
/*  207 */       .withPool(LootPool.lootPool()
/*  208 */         .when(HAS_SILK_TOUCH)
/*  209 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  210 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(debug0)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createPotFlowerItemTable(ItemLike debug0) {
/*  215 */     return LootTable.lootTable()
/*  216 */       .withPool(applyExplosionCondition((ItemLike)Blocks.FLOWER_POT, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  217 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  218 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.FLOWER_POT))))
/*      */       
/*  220 */       .withPool(applyExplosionCondition(debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  221 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  222 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(debug0))));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createSlabItemTable(Block debug0) {
/*  227 */     return LootTable.lootTable()
/*  228 */       .withPool(LootPool.lootPool()
/*  229 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  230 */         .add(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)debug0)
/*  231 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2)).when(
/*  232 */                 (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE)))))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Comparable<T> & net.minecraft.util.StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block debug0, Property<T> debug1, T debug2) {
/*  239 */     return LootTable.lootTable()
/*  240 */       .withPool(applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  241 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  242 */           .add(LootItem.lootTableItem((ItemLike)debug0)
/*  243 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(debug1, (Comparable)debug2))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createNameableBlockEntityTable(Block debug0) {
/*  249 */     return LootTable.lootTable()
/*  250 */       .withPool(applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  251 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  252 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  253 */             .apply((LootItemFunction.Builder)CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createShulkerBoxDrop(Block debug0) {
/*  258 */     return LootTable.lootTable()
/*  259 */       .withPool(applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  260 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  262 */             .apply((LootItemFunction.Builder)CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
/*  263 */             .apply((LootItemFunction.Builder)CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY)
/*  264 */               .copy("Lock", "BlockEntityTag.Lock")
/*  265 */               .copy("LootTable", "BlockEntityTag.LootTable")
/*  266 */               .copy("LootTableSeed", "BlockEntityTag.LootTableSeed"))
/*      */             
/*  268 */             .apply((LootItemFunction.Builder)SetContainerContents.setContents().withEntry((LootPoolEntryContainer.Builder)DynamicLoot.dynamicEntry(ShulkerBoxBlock.CONTENTS))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createBannerDrop(Block debug0) {
/*  274 */     return LootTable.lootTable()
/*  275 */       .withPool(applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  276 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  277 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  278 */             .apply((LootItemFunction.Builder)CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
/*  279 */             .apply((LootItemFunction.Builder)CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY)
/*  280 */               .copy("Patterns", "BlockEntityTag.Patterns")))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createBeeNestDrop(Block debug0) {
/*  287 */     return LootTable.lootTable()
/*  288 */       .withPool(LootPool.lootPool()
/*  289 */         .when(HAS_SILK_TOUCH)
/*  290 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  291 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  292 */           .apply((LootItemFunction.Builder)CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY)
/*  293 */             .copy("Bees", "BlockEntityTag.Bees"))
/*      */           
/*  295 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(debug0).copy((Property)BeehiveBlock.HONEY_LEVEL))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createBeeHiveDrop(Block debug0) {
/*  301 */     return LootTable.lootTable()
/*  302 */       .withPool(LootPool.lootPool()
/*  303 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  304 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)
/*  305 */           .when(HAS_SILK_TOUCH))
/*  306 */           .apply((LootItemFunction.Builder)CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY)
/*  307 */             .copy("Bees", "BlockEntityTag.Bees"))
/*      */           
/*  309 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(debug0).copy((Property)BeehiveBlock.HONEY_LEVEL))
/*  310 */           .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createOreDrop(Block debug0, Item debug1) {
/*  316 */     return createSilkTouchDispatchTable(debug0, 
/*  317 */         applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)debug1)
/*  318 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createMushroomBlockDrop(Block debug0, ItemLike debug1) {
/*  324 */     return createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(debug1)
/*  325 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(-6.0F, 2.0F)))
/*  326 */           .apply((LootItemFunction.Builder)LimitCount.limitCount(IntLimiter.lowerBound(0)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createGrassDrops(Block debug0) {
/*  332 */     return createShearsDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)
/*  333 */           .when(LootItemRandomChanceCondition.randomChance(0.125F)))
/*  334 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createStemDrops(Block debug0, Item debug1) {
/*  340 */     return LootTable.lootTable()
/*  341 */       .withPool(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  342 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  343 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug1)
/*  344 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.06666667F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 0))))
/*  345 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.13333334F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 1))))
/*  346 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.2F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 2))))
/*  347 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.26666668F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 3))))
/*  348 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.33333334F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 4))))
/*  349 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.4F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 5))))
/*  350 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.46666667F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 6))))
/*  351 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.53333336F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, 7)))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createAttachedStemDrops(Block debug0, Item debug1) {
/*  357 */     return LootTable.lootTable()
/*  358 */       .withPool(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  359 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  360 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug1)
/*  361 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createShearsOnlyDrop(ItemLike debug0) {
/*  367 */     return LootTable.lootTable()
/*  368 */       .withPool(LootPool.lootPool()
/*  369 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  370 */         .when(HAS_SHEARS)
/*  371 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(debug0)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createLeavesDrops(Block debug0, Block debug1, float... debug2) {
/*  376 */     return createSilkTouchOrShearsDispatchTable(debug0, (
/*  377 */         (LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)debug1)))
/*  378 */         .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, debug2)))
/*      */       
/*  380 */       .withPool(LootPool.lootPool()
/*  381 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  382 */         .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
/*  383 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F)))))
/*  384 */           .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F }))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createOakLeavesDrops(Block debug0, Block debug1, float... debug2) {
/*  390 */     return 
/*  391 */       createLeavesDrops(debug0, debug1, debug2)
/*  392 */       .withPool(LootPool.lootPool()
/*  393 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  394 */         .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
/*  395 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.APPLE)))
/*  396 */           .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, new float[] { 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F }))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createCropDrops(Block debug0, Item debug1, Item debug2, LootItemCondition.Builder debug3) {
/*  402 */     return applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/*  403 */         .withPool(LootPool.lootPool()
/*  404 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)debug1)
/*  405 */             .when(debug3))
/*  406 */             .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug2))))
/*      */ 
/*      */         
/*  409 */         .withPool(LootPool.lootPool()
/*  410 */           .when(debug3)
/*  411 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug2).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createDoublePlantShearsDrop(Block debug0) {
/*  417 */     return LootTable.lootTable().withPool(LootPool.lootPool()
/*  418 */         .when(HAS_SHEARS)
/*  419 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static LootTable.Builder createDoublePlantWithSeedDrops(Block debug0, Block debug1) {
/*  425 */     AlternativesEntry.Builder builder = ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)debug1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))).when(HAS_SHEARS)).otherwise(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)))
/*  426 */         .when(LootItemRandomChanceCondition.randomChance(0.125F)));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  431 */     return LootTable.lootTable()
/*  432 */       .withPool(
/*  433 */         LootPool.lootPool()
/*  434 */         .add((LootPoolEntryContainer.Builder)builder)
/*  435 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)))
/*  436 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0))))
/*      */       
/*  438 */       .withPool(
/*  439 */         LootPool.lootPool()
/*  440 */         .add((LootPoolEntryContainer.Builder)builder)
/*  441 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER)))
/*  442 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));
/*      */   }
/*      */ 
/*      */   
/*      */   public static LootTable.Builder noDrop() {
/*  447 */     return LootTable.lootTable();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/*  453 */     dropSelf(Blocks.GRANITE);
/*  454 */     dropSelf(Blocks.POLISHED_GRANITE);
/*  455 */     dropSelf(Blocks.DIORITE);
/*  456 */     dropSelf(Blocks.POLISHED_DIORITE);
/*  457 */     dropSelf(Blocks.ANDESITE);
/*  458 */     dropSelf(Blocks.POLISHED_ANDESITE);
/*  459 */     dropSelf(Blocks.DIRT);
/*  460 */     dropSelf(Blocks.COARSE_DIRT);
/*  461 */     dropSelf(Blocks.COBBLESTONE);
/*  462 */     dropSelf(Blocks.OAK_PLANKS);
/*  463 */     dropSelf(Blocks.SPRUCE_PLANKS);
/*  464 */     dropSelf(Blocks.BIRCH_PLANKS);
/*  465 */     dropSelf(Blocks.JUNGLE_PLANKS);
/*  466 */     dropSelf(Blocks.ACACIA_PLANKS);
/*  467 */     dropSelf(Blocks.DARK_OAK_PLANKS);
/*  468 */     dropSelf(Blocks.OAK_SAPLING);
/*  469 */     dropSelf(Blocks.SPRUCE_SAPLING);
/*  470 */     dropSelf(Blocks.BIRCH_SAPLING);
/*  471 */     dropSelf(Blocks.JUNGLE_SAPLING);
/*  472 */     dropSelf(Blocks.ACACIA_SAPLING);
/*  473 */     dropSelf(Blocks.DARK_OAK_SAPLING);
/*  474 */     dropSelf(Blocks.SAND);
/*  475 */     dropSelf(Blocks.RED_SAND);
/*  476 */     dropSelf(Blocks.GOLD_ORE);
/*  477 */     dropSelf(Blocks.IRON_ORE);
/*  478 */     dropSelf(Blocks.OAK_LOG);
/*  479 */     dropSelf(Blocks.SPRUCE_LOG);
/*  480 */     dropSelf(Blocks.BIRCH_LOG);
/*  481 */     dropSelf(Blocks.JUNGLE_LOG);
/*  482 */     dropSelf(Blocks.ACACIA_LOG);
/*  483 */     dropSelf(Blocks.DARK_OAK_LOG);
/*  484 */     dropSelf(Blocks.STRIPPED_SPRUCE_LOG);
/*  485 */     dropSelf(Blocks.STRIPPED_BIRCH_LOG);
/*  486 */     dropSelf(Blocks.STRIPPED_JUNGLE_LOG);
/*  487 */     dropSelf(Blocks.STRIPPED_ACACIA_LOG);
/*  488 */     dropSelf(Blocks.STRIPPED_DARK_OAK_LOG);
/*  489 */     dropSelf(Blocks.STRIPPED_OAK_LOG);
/*  490 */     dropSelf(Blocks.STRIPPED_WARPED_STEM);
/*  491 */     dropSelf(Blocks.STRIPPED_CRIMSON_STEM);
/*  492 */     dropSelf(Blocks.OAK_WOOD);
/*  493 */     dropSelf(Blocks.SPRUCE_WOOD);
/*  494 */     dropSelf(Blocks.BIRCH_WOOD);
/*  495 */     dropSelf(Blocks.JUNGLE_WOOD);
/*  496 */     dropSelf(Blocks.ACACIA_WOOD);
/*  497 */     dropSelf(Blocks.DARK_OAK_WOOD);
/*  498 */     dropSelf(Blocks.STRIPPED_OAK_WOOD);
/*  499 */     dropSelf(Blocks.STRIPPED_SPRUCE_WOOD);
/*  500 */     dropSelf(Blocks.STRIPPED_BIRCH_WOOD);
/*  501 */     dropSelf(Blocks.STRIPPED_JUNGLE_WOOD);
/*  502 */     dropSelf(Blocks.STRIPPED_ACACIA_WOOD);
/*  503 */     dropSelf(Blocks.STRIPPED_DARK_OAK_WOOD);
/*  504 */     dropSelf(Blocks.STRIPPED_CRIMSON_HYPHAE);
/*  505 */     dropSelf(Blocks.STRIPPED_WARPED_HYPHAE);
/*  506 */     dropSelf(Blocks.SPONGE);
/*  507 */     dropSelf(Blocks.WET_SPONGE);
/*  508 */     dropSelf(Blocks.LAPIS_BLOCK);
/*  509 */     dropSelf(Blocks.SANDSTONE);
/*  510 */     dropSelf(Blocks.CHISELED_SANDSTONE);
/*  511 */     dropSelf(Blocks.CUT_SANDSTONE);
/*  512 */     dropSelf(Blocks.NOTE_BLOCK);
/*  513 */     dropSelf(Blocks.POWERED_RAIL);
/*  514 */     dropSelf(Blocks.DETECTOR_RAIL);
/*  515 */     dropSelf(Blocks.STICKY_PISTON);
/*  516 */     dropSelf(Blocks.PISTON);
/*  517 */     dropSelf(Blocks.WHITE_WOOL);
/*  518 */     dropSelf(Blocks.ORANGE_WOOL);
/*  519 */     dropSelf(Blocks.MAGENTA_WOOL);
/*  520 */     dropSelf(Blocks.LIGHT_BLUE_WOOL);
/*  521 */     dropSelf(Blocks.YELLOW_WOOL);
/*  522 */     dropSelf(Blocks.LIME_WOOL);
/*  523 */     dropSelf(Blocks.PINK_WOOL);
/*  524 */     dropSelf(Blocks.GRAY_WOOL);
/*  525 */     dropSelf(Blocks.LIGHT_GRAY_WOOL);
/*  526 */     dropSelf(Blocks.CYAN_WOOL);
/*  527 */     dropSelf(Blocks.PURPLE_WOOL);
/*  528 */     dropSelf(Blocks.BLUE_WOOL);
/*  529 */     dropSelf(Blocks.BROWN_WOOL);
/*  530 */     dropSelf(Blocks.GREEN_WOOL);
/*  531 */     dropSelf(Blocks.RED_WOOL);
/*  532 */     dropSelf(Blocks.BLACK_WOOL);
/*  533 */     dropSelf(Blocks.DANDELION);
/*  534 */     dropSelf(Blocks.POPPY);
/*  535 */     dropSelf(Blocks.BLUE_ORCHID);
/*  536 */     dropSelf(Blocks.ALLIUM);
/*  537 */     dropSelf(Blocks.AZURE_BLUET);
/*  538 */     dropSelf(Blocks.RED_TULIP);
/*  539 */     dropSelf(Blocks.ORANGE_TULIP);
/*  540 */     dropSelf(Blocks.WHITE_TULIP);
/*  541 */     dropSelf(Blocks.PINK_TULIP);
/*  542 */     dropSelf(Blocks.OXEYE_DAISY);
/*  543 */     dropSelf(Blocks.CORNFLOWER);
/*  544 */     dropSelf(Blocks.WITHER_ROSE);
/*  545 */     dropSelf(Blocks.LILY_OF_THE_VALLEY);
/*  546 */     dropSelf(Blocks.BROWN_MUSHROOM);
/*  547 */     dropSelf(Blocks.RED_MUSHROOM);
/*  548 */     dropSelf(Blocks.GOLD_BLOCK);
/*  549 */     dropSelf(Blocks.IRON_BLOCK);
/*  550 */     dropSelf(Blocks.BRICKS);
/*  551 */     dropSelf(Blocks.MOSSY_COBBLESTONE);
/*  552 */     dropSelf(Blocks.OBSIDIAN);
/*  553 */     dropSelf(Blocks.CRYING_OBSIDIAN);
/*  554 */     dropSelf(Blocks.TORCH);
/*  555 */     dropSelf(Blocks.OAK_STAIRS);
/*  556 */     dropSelf(Blocks.REDSTONE_WIRE);
/*  557 */     dropSelf(Blocks.DIAMOND_BLOCK);
/*  558 */     dropSelf(Blocks.CRAFTING_TABLE);
/*  559 */     dropSelf(Blocks.OAK_SIGN);
/*  560 */     dropSelf(Blocks.SPRUCE_SIGN);
/*  561 */     dropSelf(Blocks.BIRCH_SIGN);
/*  562 */     dropSelf(Blocks.ACACIA_SIGN);
/*  563 */     dropSelf(Blocks.JUNGLE_SIGN);
/*  564 */     dropSelf(Blocks.DARK_OAK_SIGN);
/*  565 */     dropSelf(Blocks.LADDER);
/*  566 */     dropSelf(Blocks.RAIL);
/*  567 */     dropSelf(Blocks.COBBLESTONE_STAIRS);
/*  568 */     dropSelf(Blocks.LEVER);
/*  569 */     dropSelf(Blocks.STONE_PRESSURE_PLATE);
/*  570 */     dropSelf(Blocks.OAK_PRESSURE_PLATE);
/*  571 */     dropSelf(Blocks.SPRUCE_PRESSURE_PLATE);
/*  572 */     dropSelf(Blocks.BIRCH_PRESSURE_PLATE);
/*  573 */     dropSelf(Blocks.JUNGLE_PRESSURE_PLATE);
/*  574 */     dropSelf(Blocks.ACACIA_PRESSURE_PLATE);
/*  575 */     dropSelf(Blocks.DARK_OAK_PRESSURE_PLATE);
/*  576 */     dropSelf(Blocks.REDSTONE_TORCH);
/*  577 */     dropSelf(Blocks.STONE_BUTTON);
/*  578 */     dropSelf(Blocks.CACTUS);
/*  579 */     dropSelf(Blocks.SUGAR_CANE);
/*  580 */     dropSelf(Blocks.JUKEBOX);
/*  581 */     dropSelf(Blocks.OAK_FENCE);
/*  582 */     dropSelf(Blocks.PUMPKIN);
/*  583 */     dropSelf(Blocks.NETHERRACK);
/*  584 */     dropSelf(Blocks.SOUL_SAND);
/*  585 */     dropSelf(Blocks.SOUL_SOIL);
/*  586 */     dropSelf(Blocks.BASALT);
/*  587 */     dropSelf(Blocks.POLISHED_BASALT);
/*  588 */     dropSelf(Blocks.SOUL_TORCH);
/*  589 */     dropSelf(Blocks.CARVED_PUMPKIN);
/*  590 */     dropSelf(Blocks.JACK_O_LANTERN);
/*  591 */     dropSelf(Blocks.REPEATER);
/*  592 */     dropSelf(Blocks.OAK_TRAPDOOR);
/*  593 */     dropSelf(Blocks.SPRUCE_TRAPDOOR);
/*  594 */     dropSelf(Blocks.BIRCH_TRAPDOOR);
/*  595 */     dropSelf(Blocks.JUNGLE_TRAPDOOR);
/*  596 */     dropSelf(Blocks.ACACIA_TRAPDOOR);
/*  597 */     dropSelf(Blocks.DARK_OAK_TRAPDOOR);
/*  598 */     dropSelf(Blocks.STONE_BRICKS);
/*  599 */     dropSelf(Blocks.MOSSY_STONE_BRICKS);
/*  600 */     dropSelf(Blocks.CRACKED_STONE_BRICKS);
/*  601 */     dropSelf(Blocks.CHISELED_STONE_BRICKS);
/*  602 */     dropSelf(Blocks.IRON_BARS);
/*  603 */     dropSelf(Blocks.OAK_FENCE_GATE);
/*  604 */     dropSelf(Blocks.BRICK_STAIRS);
/*  605 */     dropSelf(Blocks.STONE_BRICK_STAIRS);
/*  606 */     dropSelf(Blocks.LILY_PAD);
/*  607 */     dropSelf(Blocks.NETHER_BRICKS);
/*  608 */     dropSelf(Blocks.NETHER_BRICK_FENCE);
/*  609 */     dropSelf(Blocks.NETHER_BRICK_STAIRS);
/*  610 */     dropSelf(Blocks.CAULDRON);
/*  611 */     dropSelf(Blocks.END_STONE);
/*  612 */     dropSelf(Blocks.REDSTONE_LAMP);
/*  613 */     dropSelf(Blocks.SANDSTONE_STAIRS);
/*  614 */     dropSelf(Blocks.TRIPWIRE_HOOK);
/*  615 */     dropSelf(Blocks.EMERALD_BLOCK);
/*  616 */     dropSelf(Blocks.SPRUCE_STAIRS);
/*  617 */     dropSelf(Blocks.BIRCH_STAIRS);
/*  618 */     dropSelf(Blocks.JUNGLE_STAIRS);
/*  619 */     dropSelf(Blocks.COBBLESTONE_WALL);
/*  620 */     dropSelf(Blocks.MOSSY_COBBLESTONE_WALL);
/*  621 */     dropSelf(Blocks.FLOWER_POT);
/*  622 */     dropSelf(Blocks.OAK_BUTTON);
/*  623 */     dropSelf(Blocks.SPRUCE_BUTTON);
/*  624 */     dropSelf(Blocks.BIRCH_BUTTON);
/*  625 */     dropSelf(Blocks.JUNGLE_BUTTON);
/*  626 */     dropSelf(Blocks.ACACIA_BUTTON);
/*  627 */     dropSelf(Blocks.DARK_OAK_BUTTON);
/*  628 */     dropSelf(Blocks.SKELETON_SKULL);
/*  629 */     dropSelf(Blocks.WITHER_SKELETON_SKULL);
/*  630 */     dropSelf(Blocks.ZOMBIE_HEAD);
/*  631 */     dropSelf(Blocks.CREEPER_HEAD);
/*  632 */     dropSelf(Blocks.DRAGON_HEAD);
/*  633 */     dropSelf(Blocks.ANVIL);
/*  634 */     dropSelf(Blocks.CHIPPED_ANVIL);
/*  635 */     dropSelf(Blocks.DAMAGED_ANVIL);
/*  636 */     dropSelf(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*  637 */     dropSelf(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*  638 */     dropSelf(Blocks.COMPARATOR);
/*  639 */     dropSelf(Blocks.DAYLIGHT_DETECTOR);
/*  640 */     dropSelf(Blocks.REDSTONE_BLOCK);
/*  641 */     dropSelf(Blocks.QUARTZ_BLOCK);
/*  642 */     dropSelf(Blocks.CHISELED_QUARTZ_BLOCK);
/*  643 */     dropSelf(Blocks.QUARTZ_PILLAR);
/*  644 */     dropSelf(Blocks.QUARTZ_STAIRS);
/*  645 */     dropSelf(Blocks.ACTIVATOR_RAIL);
/*  646 */     dropSelf(Blocks.WHITE_TERRACOTTA);
/*  647 */     dropSelf(Blocks.ORANGE_TERRACOTTA);
/*  648 */     dropSelf(Blocks.MAGENTA_TERRACOTTA);
/*  649 */     dropSelf(Blocks.LIGHT_BLUE_TERRACOTTA);
/*  650 */     dropSelf(Blocks.YELLOW_TERRACOTTA);
/*  651 */     dropSelf(Blocks.LIME_TERRACOTTA);
/*  652 */     dropSelf(Blocks.PINK_TERRACOTTA);
/*  653 */     dropSelf(Blocks.GRAY_TERRACOTTA);
/*  654 */     dropSelf(Blocks.LIGHT_GRAY_TERRACOTTA);
/*  655 */     dropSelf(Blocks.CYAN_TERRACOTTA);
/*  656 */     dropSelf(Blocks.PURPLE_TERRACOTTA);
/*  657 */     dropSelf(Blocks.BLUE_TERRACOTTA);
/*  658 */     dropSelf(Blocks.BROWN_TERRACOTTA);
/*  659 */     dropSelf(Blocks.GREEN_TERRACOTTA);
/*  660 */     dropSelf(Blocks.RED_TERRACOTTA);
/*  661 */     dropSelf(Blocks.BLACK_TERRACOTTA);
/*  662 */     dropSelf(Blocks.ACACIA_STAIRS);
/*  663 */     dropSelf(Blocks.DARK_OAK_STAIRS);
/*  664 */     dropSelf(Blocks.SLIME_BLOCK);
/*  665 */     dropSelf(Blocks.IRON_TRAPDOOR);
/*  666 */     dropSelf(Blocks.PRISMARINE);
/*  667 */     dropSelf(Blocks.PRISMARINE_BRICKS);
/*  668 */     dropSelf(Blocks.DARK_PRISMARINE);
/*  669 */     dropSelf(Blocks.PRISMARINE_STAIRS);
/*  670 */     dropSelf(Blocks.PRISMARINE_BRICK_STAIRS);
/*  671 */     dropSelf(Blocks.DARK_PRISMARINE_STAIRS);
/*  672 */     dropSelf(Blocks.HAY_BLOCK);
/*  673 */     dropSelf(Blocks.WHITE_CARPET);
/*  674 */     dropSelf(Blocks.ORANGE_CARPET);
/*  675 */     dropSelf(Blocks.MAGENTA_CARPET);
/*  676 */     dropSelf(Blocks.LIGHT_BLUE_CARPET);
/*  677 */     dropSelf(Blocks.YELLOW_CARPET);
/*  678 */     dropSelf(Blocks.LIME_CARPET);
/*  679 */     dropSelf(Blocks.PINK_CARPET);
/*  680 */     dropSelf(Blocks.GRAY_CARPET);
/*  681 */     dropSelf(Blocks.LIGHT_GRAY_CARPET);
/*  682 */     dropSelf(Blocks.CYAN_CARPET);
/*  683 */     dropSelf(Blocks.PURPLE_CARPET);
/*  684 */     dropSelf(Blocks.BLUE_CARPET);
/*  685 */     dropSelf(Blocks.BROWN_CARPET);
/*  686 */     dropSelf(Blocks.GREEN_CARPET);
/*  687 */     dropSelf(Blocks.RED_CARPET);
/*  688 */     dropSelf(Blocks.BLACK_CARPET);
/*  689 */     dropSelf(Blocks.TERRACOTTA);
/*  690 */     dropSelf(Blocks.COAL_BLOCK);
/*  691 */     dropSelf(Blocks.RED_SANDSTONE);
/*  692 */     dropSelf(Blocks.CHISELED_RED_SANDSTONE);
/*  693 */     dropSelf(Blocks.CUT_RED_SANDSTONE);
/*  694 */     dropSelf(Blocks.RED_SANDSTONE_STAIRS);
/*  695 */     dropSelf(Blocks.SMOOTH_STONE);
/*  696 */     dropSelf(Blocks.SMOOTH_SANDSTONE);
/*  697 */     dropSelf(Blocks.SMOOTH_QUARTZ);
/*  698 */     dropSelf(Blocks.SMOOTH_RED_SANDSTONE);
/*  699 */     dropSelf(Blocks.SPRUCE_FENCE_GATE);
/*  700 */     dropSelf(Blocks.BIRCH_FENCE_GATE);
/*  701 */     dropSelf(Blocks.JUNGLE_FENCE_GATE);
/*  702 */     dropSelf(Blocks.ACACIA_FENCE_GATE);
/*  703 */     dropSelf(Blocks.DARK_OAK_FENCE_GATE);
/*  704 */     dropSelf(Blocks.SPRUCE_FENCE);
/*  705 */     dropSelf(Blocks.BIRCH_FENCE);
/*  706 */     dropSelf(Blocks.JUNGLE_FENCE);
/*  707 */     dropSelf(Blocks.ACACIA_FENCE);
/*  708 */     dropSelf(Blocks.DARK_OAK_FENCE);
/*  709 */     dropSelf(Blocks.END_ROD);
/*  710 */     dropSelf(Blocks.PURPUR_BLOCK);
/*  711 */     dropSelf(Blocks.PURPUR_PILLAR);
/*  712 */     dropSelf(Blocks.PURPUR_STAIRS);
/*  713 */     dropSelf(Blocks.END_STONE_BRICKS);
/*  714 */     dropSelf(Blocks.MAGMA_BLOCK);
/*  715 */     dropSelf(Blocks.NETHER_WART_BLOCK);
/*  716 */     dropSelf(Blocks.RED_NETHER_BRICKS);
/*  717 */     dropSelf(Blocks.BONE_BLOCK);
/*  718 */     dropSelf(Blocks.OBSERVER);
/*  719 */     dropSelf(Blocks.TARGET);
/*  720 */     dropSelf(Blocks.WHITE_GLAZED_TERRACOTTA);
/*  721 */     dropSelf(Blocks.ORANGE_GLAZED_TERRACOTTA);
/*  722 */     dropSelf(Blocks.MAGENTA_GLAZED_TERRACOTTA);
/*  723 */     dropSelf(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
/*  724 */     dropSelf(Blocks.YELLOW_GLAZED_TERRACOTTA);
/*  725 */     dropSelf(Blocks.LIME_GLAZED_TERRACOTTA);
/*  726 */     dropSelf(Blocks.PINK_GLAZED_TERRACOTTA);
/*  727 */     dropSelf(Blocks.GRAY_GLAZED_TERRACOTTA);
/*  728 */     dropSelf(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
/*  729 */     dropSelf(Blocks.CYAN_GLAZED_TERRACOTTA);
/*  730 */     dropSelf(Blocks.PURPLE_GLAZED_TERRACOTTA);
/*  731 */     dropSelf(Blocks.BLUE_GLAZED_TERRACOTTA);
/*  732 */     dropSelf(Blocks.BROWN_GLAZED_TERRACOTTA);
/*  733 */     dropSelf(Blocks.GREEN_GLAZED_TERRACOTTA);
/*  734 */     dropSelf(Blocks.RED_GLAZED_TERRACOTTA);
/*  735 */     dropSelf(Blocks.BLACK_GLAZED_TERRACOTTA);
/*  736 */     dropSelf(Blocks.WHITE_CONCRETE);
/*  737 */     dropSelf(Blocks.ORANGE_CONCRETE);
/*  738 */     dropSelf(Blocks.MAGENTA_CONCRETE);
/*  739 */     dropSelf(Blocks.LIGHT_BLUE_CONCRETE);
/*  740 */     dropSelf(Blocks.YELLOW_CONCRETE);
/*  741 */     dropSelf(Blocks.LIME_CONCRETE);
/*  742 */     dropSelf(Blocks.PINK_CONCRETE);
/*  743 */     dropSelf(Blocks.GRAY_CONCRETE);
/*  744 */     dropSelf(Blocks.LIGHT_GRAY_CONCRETE);
/*  745 */     dropSelf(Blocks.CYAN_CONCRETE);
/*  746 */     dropSelf(Blocks.PURPLE_CONCRETE);
/*  747 */     dropSelf(Blocks.BLUE_CONCRETE);
/*  748 */     dropSelf(Blocks.BROWN_CONCRETE);
/*  749 */     dropSelf(Blocks.GREEN_CONCRETE);
/*  750 */     dropSelf(Blocks.RED_CONCRETE);
/*  751 */     dropSelf(Blocks.BLACK_CONCRETE);
/*  752 */     dropSelf(Blocks.WHITE_CONCRETE_POWDER);
/*  753 */     dropSelf(Blocks.ORANGE_CONCRETE_POWDER);
/*  754 */     dropSelf(Blocks.MAGENTA_CONCRETE_POWDER);
/*  755 */     dropSelf(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
/*  756 */     dropSelf(Blocks.YELLOW_CONCRETE_POWDER);
/*  757 */     dropSelf(Blocks.LIME_CONCRETE_POWDER);
/*  758 */     dropSelf(Blocks.PINK_CONCRETE_POWDER);
/*  759 */     dropSelf(Blocks.GRAY_CONCRETE_POWDER);
/*  760 */     dropSelf(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
/*  761 */     dropSelf(Blocks.CYAN_CONCRETE_POWDER);
/*  762 */     dropSelf(Blocks.PURPLE_CONCRETE_POWDER);
/*  763 */     dropSelf(Blocks.BLUE_CONCRETE_POWDER);
/*  764 */     dropSelf(Blocks.BROWN_CONCRETE_POWDER);
/*  765 */     dropSelf(Blocks.GREEN_CONCRETE_POWDER);
/*  766 */     dropSelf(Blocks.RED_CONCRETE_POWDER);
/*  767 */     dropSelf(Blocks.BLACK_CONCRETE_POWDER);
/*  768 */     dropSelf(Blocks.KELP);
/*  769 */     dropSelf(Blocks.DRIED_KELP_BLOCK);
/*  770 */     dropSelf(Blocks.DEAD_TUBE_CORAL_BLOCK);
/*  771 */     dropSelf(Blocks.DEAD_BRAIN_CORAL_BLOCK);
/*  772 */     dropSelf(Blocks.DEAD_BUBBLE_CORAL_BLOCK);
/*  773 */     dropSelf(Blocks.DEAD_FIRE_CORAL_BLOCK);
/*  774 */     dropSelf(Blocks.DEAD_HORN_CORAL_BLOCK);
/*  775 */     dropSelf(Blocks.CONDUIT);
/*  776 */     dropSelf(Blocks.DRAGON_EGG);
/*  777 */     dropSelf(Blocks.BAMBOO);
/*  778 */     dropSelf(Blocks.POLISHED_GRANITE_STAIRS);
/*  779 */     dropSelf(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
/*  780 */     dropSelf(Blocks.MOSSY_STONE_BRICK_STAIRS);
/*  781 */     dropSelf(Blocks.POLISHED_DIORITE_STAIRS);
/*  782 */     dropSelf(Blocks.MOSSY_COBBLESTONE_STAIRS);
/*  783 */     dropSelf(Blocks.END_STONE_BRICK_STAIRS);
/*  784 */     dropSelf(Blocks.STONE_STAIRS);
/*  785 */     dropSelf(Blocks.SMOOTH_SANDSTONE_STAIRS);
/*  786 */     dropSelf(Blocks.SMOOTH_QUARTZ_STAIRS);
/*  787 */     dropSelf(Blocks.GRANITE_STAIRS);
/*  788 */     dropSelf(Blocks.ANDESITE_STAIRS);
/*  789 */     dropSelf(Blocks.RED_NETHER_BRICK_STAIRS);
/*  790 */     dropSelf(Blocks.POLISHED_ANDESITE_STAIRS);
/*  791 */     dropSelf(Blocks.DIORITE_STAIRS);
/*  792 */     dropSelf(Blocks.BRICK_WALL);
/*  793 */     dropSelf(Blocks.PRISMARINE_WALL);
/*  794 */     dropSelf(Blocks.RED_SANDSTONE_WALL);
/*  795 */     dropSelf(Blocks.MOSSY_STONE_BRICK_WALL);
/*  796 */     dropSelf(Blocks.GRANITE_WALL);
/*  797 */     dropSelf(Blocks.STONE_BRICK_WALL);
/*  798 */     dropSelf(Blocks.NETHER_BRICK_WALL);
/*  799 */     dropSelf(Blocks.ANDESITE_WALL);
/*  800 */     dropSelf(Blocks.RED_NETHER_BRICK_WALL);
/*  801 */     dropSelf(Blocks.SANDSTONE_WALL);
/*  802 */     dropSelf(Blocks.END_STONE_BRICK_WALL);
/*  803 */     dropSelf(Blocks.DIORITE_WALL);
/*  804 */     dropSelf(Blocks.LOOM);
/*  805 */     dropSelf(Blocks.SCAFFOLDING);
/*  806 */     dropSelf(Blocks.HONEY_BLOCK);
/*  807 */     dropSelf(Blocks.HONEYCOMB_BLOCK);
/*  808 */     dropSelf(Blocks.RESPAWN_ANCHOR);
/*  809 */     dropSelf(Blocks.LODESTONE);
/*  810 */     dropSelf(Blocks.WARPED_STEM);
/*  811 */     dropSelf(Blocks.WARPED_HYPHAE);
/*  812 */     dropSelf(Blocks.WARPED_FUNGUS);
/*  813 */     dropSelf(Blocks.WARPED_WART_BLOCK);
/*  814 */     dropSelf(Blocks.CRIMSON_STEM);
/*  815 */     dropSelf(Blocks.CRIMSON_HYPHAE);
/*  816 */     dropSelf(Blocks.CRIMSON_FUNGUS);
/*  817 */     dropSelf(Blocks.SHROOMLIGHT);
/*  818 */     dropSelf(Blocks.CRIMSON_PLANKS);
/*  819 */     dropSelf(Blocks.WARPED_PLANKS);
/*  820 */     dropSelf(Blocks.WARPED_PRESSURE_PLATE);
/*  821 */     dropSelf(Blocks.WARPED_FENCE);
/*  822 */     dropSelf(Blocks.WARPED_TRAPDOOR);
/*  823 */     dropSelf(Blocks.WARPED_FENCE_GATE);
/*  824 */     dropSelf(Blocks.WARPED_STAIRS);
/*  825 */     dropSelf(Blocks.WARPED_BUTTON);
/*  826 */     dropSelf(Blocks.WARPED_SIGN);
/*  827 */     dropSelf(Blocks.CRIMSON_PRESSURE_PLATE);
/*  828 */     dropSelf(Blocks.CRIMSON_FENCE);
/*  829 */     dropSelf(Blocks.CRIMSON_TRAPDOOR);
/*  830 */     dropSelf(Blocks.CRIMSON_FENCE_GATE);
/*  831 */     dropSelf(Blocks.CRIMSON_STAIRS);
/*  832 */     dropSelf(Blocks.CRIMSON_BUTTON);
/*  833 */     dropSelf(Blocks.CRIMSON_SIGN);
/*  834 */     dropSelf(Blocks.NETHERITE_BLOCK);
/*  835 */     dropSelf(Blocks.ANCIENT_DEBRIS);
/*  836 */     dropSelf(Blocks.BLACKSTONE);
/*  837 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICKS);
/*  838 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*  839 */     dropSelf(Blocks.BLACKSTONE_STAIRS);
/*  840 */     dropSelf(Blocks.BLACKSTONE_WALL);
/*  841 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*  842 */     dropSelf(Blocks.CHISELED_POLISHED_BLACKSTONE);
/*  843 */     dropSelf(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*  844 */     dropSelf(Blocks.POLISHED_BLACKSTONE);
/*  845 */     dropSelf(Blocks.POLISHED_BLACKSTONE_STAIRS);
/*  846 */     dropSelf(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
/*  847 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BUTTON);
/*  848 */     dropSelf(Blocks.POLISHED_BLACKSTONE_WALL);
/*  849 */     dropSelf(Blocks.CHISELED_NETHER_BRICKS);
/*  850 */     dropSelf(Blocks.CRACKED_NETHER_BRICKS);
/*  851 */     dropSelf(Blocks.QUARTZ_BRICKS);
/*  852 */     dropSelf(Blocks.CHAIN);
/*  853 */     dropSelf(Blocks.WARPED_ROOTS);
/*  854 */     dropSelf(Blocks.CRIMSON_ROOTS);
/*      */ 
/*      */     
/*  857 */     dropOther(Blocks.FARMLAND, (ItemLike)Blocks.DIRT);
/*  858 */     dropOther(Blocks.TRIPWIRE, (ItemLike)Items.STRING);
/*  859 */     dropOther(Blocks.GRASS_PATH, (ItemLike)Blocks.DIRT);
/*  860 */     dropOther(Blocks.KELP_PLANT, (ItemLike)Blocks.KELP);
/*  861 */     dropOther(Blocks.BAMBOO_SAPLING, (ItemLike)Blocks.BAMBOO);
/*      */ 
/*      */     
/*  864 */     add(Blocks.STONE, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.COBBLESTONE));
/*  865 */     add(Blocks.GRASS_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DIRT));
/*  866 */     add(Blocks.PODZOL, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DIRT));
/*  867 */     add(Blocks.MYCELIUM, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DIRT));
/*  868 */     add(Blocks.TUBE_CORAL_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DEAD_TUBE_CORAL_BLOCK));
/*  869 */     add(Blocks.BRAIN_CORAL_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DEAD_BRAIN_CORAL_BLOCK));
/*  870 */     add(Blocks.BUBBLE_CORAL_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DEAD_BUBBLE_CORAL_BLOCK));
/*  871 */     add(Blocks.FIRE_CORAL_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DEAD_FIRE_CORAL_BLOCK));
/*  872 */     add(Blocks.HORN_CORAL_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.DEAD_HORN_CORAL_BLOCK));
/*  873 */     add(Blocks.CRIMSON_NYLIUM, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.NETHERRACK));
/*  874 */     add(Blocks.WARPED_NYLIUM, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.NETHERRACK));
/*      */ 
/*      */     
/*  877 */     add(Blocks.BOOKSHELF, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Items.BOOK, (RandomIntGenerator)ConstantIntValue.exactly(3)));
/*  878 */     add(Blocks.CLAY, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Items.CLAY_BALL, (RandomIntGenerator)ConstantIntValue.exactly(4)));
/*  879 */     add(Blocks.ENDER_CHEST, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Blocks.OBSIDIAN, (RandomIntGenerator)ConstantIntValue.exactly(8)));
/*  880 */     add(Blocks.SNOW_BLOCK, debug0 -> createSingleItemTableWithSilkTouch(debug0, (ItemLike)Items.SNOWBALL, (RandomIntGenerator)ConstantIntValue.exactly(4)));
/*      */     
/*  882 */     add(Blocks.CHORUS_PLANT, createSingleItemTable((ItemLike)Items.CHORUS_FRUIT, (RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F)));
/*      */ 
/*      */     
/*  885 */     dropPottedContents(Blocks.POTTED_OAK_SAPLING);
/*  886 */     dropPottedContents(Blocks.POTTED_SPRUCE_SAPLING);
/*  887 */     dropPottedContents(Blocks.POTTED_BIRCH_SAPLING);
/*  888 */     dropPottedContents(Blocks.POTTED_JUNGLE_SAPLING);
/*  889 */     dropPottedContents(Blocks.POTTED_ACACIA_SAPLING);
/*  890 */     dropPottedContents(Blocks.POTTED_DARK_OAK_SAPLING);
/*  891 */     dropPottedContents(Blocks.POTTED_FERN);
/*  892 */     dropPottedContents(Blocks.POTTED_DANDELION);
/*  893 */     dropPottedContents(Blocks.POTTED_POPPY);
/*  894 */     dropPottedContents(Blocks.POTTED_BLUE_ORCHID);
/*  895 */     dropPottedContents(Blocks.POTTED_ALLIUM);
/*  896 */     dropPottedContents(Blocks.POTTED_AZURE_BLUET);
/*  897 */     dropPottedContents(Blocks.POTTED_RED_TULIP);
/*  898 */     dropPottedContents(Blocks.POTTED_ORANGE_TULIP);
/*  899 */     dropPottedContents(Blocks.POTTED_WHITE_TULIP);
/*  900 */     dropPottedContents(Blocks.POTTED_PINK_TULIP);
/*  901 */     dropPottedContents(Blocks.POTTED_OXEYE_DAISY);
/*  902 */     dropPottedContents(Blocks.POTTED_CORNFLOWER);
/*  903 */     dropPottedContents(Blocks.POTTED_LILY_OF_THE_VALLEY);
/*  904 */     dropPottedContents(Blocks.POTTED_WITHER_ROSE);
/*  905 */     dropPottedContents(Blocks.POTTED_RED_MUSHROOM);
/*  906 */     dropPottedContents(Blocks.POTTED_BROWN_MUSHROOM);
/*  907 */     dropPottedContents(Blocks.POTTED_DEAD_BUSH);
/*  908 */     dropPottedContents(Blocks.POTTED_CACTUS);
/*  909 */     dropPottedContents(Blocks.POTTED_BAMBOO);
/*  910 */     dropPottedContents(Blocks.POTTED_CRIMSON_FUNGUS);
/*  911 */     dropPottedContents(Blocks.POTTED_WARPED_FUNGUS);
/*  912 */     dropPottedContents(Blocks.POTTED_CRIMSON_ROOTS);
/*  913 */     dropPottedContents(Blocks.POTTED_WARPED_ROOTS);
/*      */ 
/*      */     
/*  916 */     add(Blocks.ACACIA_SLAB, BlockLoot::createSlabItemTable);
/*  917 */     add(Blocks.BIRCH_SLAB, BlockLoot::createSlabItemTable);
/*  918 */     add(Blocks.BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  919 */     add(Blocks.COBBLESTONE_SLAB, BlockLoot::createSlabItemTable);
/*  920 */     add(Blocks.DARK_OAK_SLAB, BlockLoot::createSlabItemTable);
/*  921 */     add(Blocks.DARK_PRISMARINE_SLAB, BlockLoot::createSlabItemTable);
/*  922 */     add(Blocks.JUNGLE_SLAB, BlockLoot::createSlabItemTable);
/*  923 */     add(Blocks.NETHER_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  924 */     add(Blocks.OAK_SLAB, BlockLoot::createSlabItemTable);
/*  925 */     add(Blocks.PETRIFIED_OAK_SLAB, BlockLoot::createSlabItemTable);
/*  926 */     add(Blocks.PRISMARINE_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  927 */     add(Blocks.PRISMARINE_SLAB, BlockLoot::createSlabItemTable);
/*  928 */     add(Blocks.PURPUR_SLAB, BlockLoot::createSlabItemTable);
/*  929 */     add(Blocks.QUARTZ_SLAB, BlockLoot::createSlabItemTable);
/*  930 */     add(Blocks.RED_SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  931 */     add(Blocks.SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  932 */     add(Blocks.CUT_RED_SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  933 */     add(Blocks.CUT_SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  934 */     add(Blocks.SPRUCE_SLAB, BlockLoot::createSlabItemTable);
/*  935 */     add(Blocks.STONE_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  936 */     add(Blocks.STONE_SLAB, BlockLoot::createSlabItemTable);
/*  937 */     add(Blocks.SMOOTH_STONE_SLAB, BlockLoot::createSlabItemTable);
/*  938 */     add(Blocks.POLISHED_GRANITE_SLAB, BlockLoot::createSlabItemTable);
/*  939 */     add(Blocks.SMOOTH_RED_SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  940 */     add(Blocks.MOSSY_STONE_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  941 */     add(Blocks.POLISHED_DIORITE_SLAB, BlockLoot::createSlabItemTable);
/*  942 */     add(Blocks.MOSSY_COBBLESTONE_SLAB, BlockLoot::createSlabItemTable);
/*  943 */     add(Blocks.END_STONE_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  944 */     add(Blocks.SMOOTH_SANDSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  945 */     add(Blocks.SMOOTH_QUARTZ_SLAB, BlockLoot::createSlabItemTable);
/*  946 */     add(Blocks.GRANITE_SLAB, BlockLoot::createSlabItemTable);
/*  947 */     add(Blocks.ANDESITE_SLAB, BlockLoot::createSlabItemTable);
/*  948 */     add(Blocks.RED_NETHER_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  949 */     add(Blocks.POLISHED_ANDESITE_SLAB, BlockLoot::createSlabItemTable);
/*  950 */     add(Blocks.DIORITE_SLAB, BlockLoot::createSlabItemTable);
/*  951 */     add(Blocks.CRIMSON_SLAB, BlockLoot::createSlabItemTable);
/*  952 */     add(Blocks.WARPED_SLAB, BlockLoot::createSlabItemTable);
/*  953 */     add(Blocks.BLACKSTONE_SLAB, BlockLoot::createSlabItemTable);
/*  954 */     add(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, BlockLoot::createSlabItemTable);
/*  955 */     add(Blocks.POLISHED_BLACKSTONE_SLAB, BlockLoot::createSlabItemTable);
/*      */ 
/*      */     
/*  958 */     add(Blocks.ACACIA_DOOR, BlockLoot::createDoorTable);
/*  959 */     add(Blocks.BIRCH_DOOR, BlockLoot::createDoorTable);
/*  960 */     add(Blocks.DARK_OAK_DOOR, BlockLoot::createDoorTable);
/*  961 */     add(Blocks.IRON_DOOR, BlockLoot::createDoorTable);
/*  962 */     add(Blocks.JUNGLE_DOOR, BlockLoot::createDoorTable);
/*  963 */     add(Blocks.OAK_DOOR, BlockLoot::createDoorTable);
/*  964 */     add(Blocks.SPRUCE_DOOR, BlockLoot::createDoorTable);
/*  965 */     add(Blocks.WARPED_DOOR, BlockLoot::createDoorTable);
/*  966 */     add(Blocks.CRIMSON_DOOR, BlockLoot::createDoorTable);
/*      */ 
/*      */     
/*  969 */     add(Blocks.BLACK_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  970 */     add(Blocks.BLUE_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  971 */     add(Blocks.BROWN_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  972 */     add(Blocks.CYAN_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  973 */     add(Blocks.GRAY_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  974 */     add(Blocks.GREEN_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  975 */     add(Blocks.LIGHT_BLUE_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  976 */     add(Blocks.LIGHT_GRAY_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  977 */     add(Blocks.LIME_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  978 */     add(Blocks.MAGENTA_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  979 */     add(Blocks.PURPLE_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  980 */     add(Blocks.ORANGE_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  981 */     add(Blocks.PINK_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  982 */     add(Blocks.RED_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  983 */     add(Blocks.WHITE_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*  984 */     add(Blocks.YELLOW_BED, debug0 -> createSinglePropConditionTable(debug0, (Property<BedPart>)BedBlock.PART, BedPart.HEAD));
/*      */ 
/*      */     
/*  987 */     add(Blocks.LILAC, debug0 -> createSinglePropConditionTable(debug0, (Property<DoubleBlockHalf>)DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  988 */     add(Blocks.SUNFLOWER, debug0 -> createSinglePropConditionTable(debug0, (Property<DoubleBlockHalf>)DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  989 */     add(Blocks.PEONY, debug0 -> createSinglePropConditionTable(debug0, (Property<DoubleBlockHalf>)DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  990 */     add(Blocks.ROSE_BUSH, debug0 -> createSinglePropConditionTable(debug0, (Property<DoubleBlockHalf>)DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*      */ 
/*      */     
/*  993 */     add(Blocks.TNT, LootTable.lootTable()
/*  994 */         .withPool(applyExplosionCondition((ItemLike)Blocks.TNT, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/*  995 */             .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  996 */             .add(LootItem.lootTableItem((ItemLike)Blocks.TNT)
/*  997 */               .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TNT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)TntBlock.UNSTABLE, false)))))));
/*      */ 
/*      */ 
/*      */     
/* 1001 */     add(Blocks.COCOA, debug0 -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls((RandomIntGenerator)ConstantIntValue.exactly(1)).add(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.COCOA_BEANS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(3)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CocoaBlock.AGE, 2))))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     add(Blocks.SEA_PICKLE, debug0 -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls((RandomIntGenerator)ConstantIntValue.exactly(1)).add(applyExplosionDecay((ItemLike)Blocks.SEA_PICKLE, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)debug0).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SeaPickleBlock.PICKLES, 2)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(3)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SeaPickleBlock.PICKLES, 3)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(4)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SeaPickleBlock.PICKLES, 4))))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1021 */     add(Blocks.COMPOSTER, debug0 -> LootTable.lootTable().withPool(LootPool.lootPool().add(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.COMPOSTER)))).withPool(LootPool.lootPool().add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComposterBlock.LEVEL, 8)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1031 */     add(Blocks.BEACON, BlockLoot::createNameableBlockEntityTable);
/* 1032 */     add(Blocks.BREWING_STAND, BlockLoot::createNameableBlockEntityTable);
/* 1033 */     add(Blocks.CHEST, BlockLoot::createNameableBlockEntityTable);
/* 1034 */     add(Blocks.DISPENSER, BlockLoot::createNameableBlockEntityTable);
/* 1035 */     add(Blocks.DROPPER, BlockLoot::createNameableBlockEntityTable);
/* 1036 */     add(Blocks.ENCHANTING_TABLE, BlockLoot::createNameableBlockEntityTable);
/* 1037 */     add(Blocks.FURNACE, BlockLoot::createNameableBlockEntityTable);
/* 1038 */     add(Blocks.HOPPER, BlockLoot::createNameableBlockEntityTable);
/* 1039 */     add(Blocks.TRAPPED_CHEST, BlockLoot::createNameableBlockEntityTable);
/* 1040 */     add(Blocks.SMOKER, BlockLoot::createNameableBlockEntityTable);
/* 1041 */     add(Blocks.BLAST_FURNACE, BlockLoot::createNameableBlockEntityTable);
/*      */     
/* 1043 */     add(Blocks.BARREL, BlockLoot::createNameableBlockEntityTable);
/* 1044 */     add(Blocks.CARTOGRAPHY_TABLE, BlockLoot::createNameableBlockEntityTable);
/* 1045 */     add(Blocks.FLETCHING_TABLE, BlockLoot::createNameableBlockEntityTable);
/* 1046 */     add(Blocks.GRINDSTONE, BlockLoot::createNameableBlockEntityTable);
/* 1047 */     add(Blocks.LECTERN, BlockLoot::createNameableBlockEntityTable);
/* 1048 */     add(Blocks.SMITHING_TABLE, BlockLoot::createNameableBlockEntityTable);
/* 1049 */     add(Blocks.STONECUTTER, BlockLoot::createNameableBlockEntityTable);
/*      */     
/* 1051 */     add(Blocks.BELL, BlockLoot::createSingleItemTable);
/* 1052 */     add(Blocks.LANTERN, BlockLoot::createSingleItemTable);
/* 1053 */     add(Blocks.SOUL_LANTERN, BlockLoot::createSingleItemTable);
/*      */ 
/*      */     
/* 1056 */     add(Blocks.SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1057 */     add(Blocks.BLACK_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1058 */     add(Blocks.BLUE_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1059 */     add(Blocks.BROWN_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1060 */     add(Blocks.CYAN_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1061 */     add(Blocks.GRAY_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1062 */     add(Blocks.GREEN_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1063 */     add(Blocks.LIGHT_BLUE_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1064 */     add(Blocks.LIGHT_GRAY_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1065 */     add(Blocks.LIME_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1066 */     add(Blocks.MAGENTA_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1067 */     add(Blocks.ORANGE_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1068 */     add(Blocks.PINK_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1069 */     add(Blocks.PURPLE_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1070 */     add(Blocks.RED_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1071 */     add(Blocks.WHITE_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/* 1072 */     add(Blocks.YELLOW_SHULKER_BOX, BlockLoot::createShulkerBoxDrop);
/*      */ 
/*      */     
/* 1075 */     add(Blocks.BLACK_BANNER, BlockLoot::createBannerDrop);
/* 1076 */     add(Blocks.BLUE_BANNER, BlockLoot::createBannerDrop);
/* 1077 */     add(Blocks.BROWN_BANNER, BlockLoot::createBannerDrop);
/* 1078 */     add(Blocks.CYAN_BANNER, BlockLoot::createBannerDrop);
/* 1079 */     add(Blocks.GRAY_BANNER, BlockLoot::createBannerDrop);
/* 1080 */     add(Blocks.GREEN_BANNER, BlockLoot::createBannerDrop);
/* 1081 */     add(Blocks.LIGHT_BLUE_BANNER, BlockLoot::createBannerDrop);
/* 1082 */     add(Blocks.LIGHT_GRAY_BANNER, BlockLoot::createBannerDrop);
/* 1083 */     add(Blocks.LIME_BANNER, BlockLoot::createBannerDrop);
/* 1084 */     add(Blocks.MAGENTA_BANNER, BlockLoot::createBannerDrop);
/* 1085 */     add(Blocks.ORANGE_BANNER, BlockLoot::createBannerDrop);
/* 1086 */     add(Blocks.PINK_BANNER, BlockLoot::createBannerDrop);
/* 1087 */     add(Blocks.PURPLE_BANNER, BlockLoot::createBannerDrop);
/* 1088 */     add(Blocks.RED_BANNER, BlockLoot::createBannerDrop);
/* 1089 */     add(Blocks.WHITE_BANNER, BlockLoot::createBannerDrop);
/* 1090 */     add(Blocks.YELLOW_BANNER, BlockLoot::createBannerDrop);
/*      */     
/* 1092 */     add(Blocks.PLAYER_HEAD, debug0 -> LootTable.lootTable().withPool(applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool().setRolls((RandomIntGenerator)ConstantIntValue.exactly(1)).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0).apply((LootItemFunction.Builder)CopyNbtFunction.copyData(CopyNbtFunction.DataSource.BLOCK_ENTITY).copy("SkullOwner", "SkullOwner"))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1103 */     add(Blocks.BEE_NEST, BlockLoot::createBeeNestDrop);
/* 1104 */     add(Blocks.BEEHIVE, BlockLoot::createBeeHiveDrop);
/*      */ 
/*      */     
/* 1107 */     add(Blocks.BIRCH_LEAVES, debug0 -> createLeavesDrops(debug0, Blocks.BIRCH_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1108 */     add(Blocks.ACACIA_LEAVES, debug0 -> createLeavesDrops(debug0, Blocks.ACACIA_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1109 */     add(Blocks.JUNGLE_LEAVES, debug0 -> createLeavesDrops(debug0, Blocks.JUNGLE_SAPLING, JUNGLE_LEAVES_SAPLING_CHANGES));
/* 1110 */     add(Blocks.SPRUCE_LEAVES, debug0 -> createLeavesDrops(debug0, Blocks.SPRUCE_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/*      */     
/* 1112 */     add(Blocks.OAK_LEAVES, debug0 -> createOakLeavesDrops(debug0, Blocks.OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1113 */     add(Blocks.DARK_OAK_LEAVES, debug0 -> createOakLeavesDrops(debug0, Blocks.DARK_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/*      */ 
/*      */     
/* 1116 */     LootItemBlockStatePropertyCondition.Builder builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEETROOTS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)BeetrootBlock.AGE, 3));
/* 1117 */     add(Blocks.BEETROOTS, createCropDrops(Blocks.BEETROOTS, Items.BEETROOT, Items.BEETROOT_SEEDS, (LootItemCondition.Builder)builder1));
/*      */     
/* 1119 */     LootItemBlockStatePropertyCondition.Builder builder2 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CropBlock.AGE, 7));
/* 1120 */     add(Blocks.WHEAT, createCropDrops(Blocks.WHEAT, Items.WHEAT, Items.WHEAT_SEEDS, (LootItemCondition.Builder)builder2));
/*      */     
/* 1122 */     LootItemBlockStatePropertyCondition.Builder builder3 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CARROTS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CarrotBlock.AGE, 7));
/*      */     
/* 1124 */     add(Blocks.CARROTS, applyExplosionDecay((ItemLike)Blocks.CARROTS, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/* 1125 */           .withPool(LootPool.lootPool()
/* 1126 */             .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT)))
/*      */           
/* 1128 */           .withPool(LootPool.lootPool()
/* 1129 */             .when((LootItemCondition.Builder)builder3)
/* 1130 */             .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))));
/*      */ 
/*      */ 
/*      */     
/* 1134 */     LootItemBlockStatePropertyCondition.Builder builder4 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.POTATOES).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)PotatoBlock.AGE, 7));
/* 1135 */     add(Blocks.POTATOES, applyExplosionDecay((ItemLike)Blocks.POTATOES, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/* 1136 */           .withPool(LootPool.lootPool()
/* 1137 */             .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO)))
/*      */           
/* 1139 */           .withPool(LootPool.lootPool()
/* 1140 */             .when((LootItemCondition.Builder)builder4)
/* 1141 */             .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3))))
/*      */           
/* 1143 */           .withPool(LootPool.lootPool()
/* 1144 */             .when((LootItemCondition.Builder)builder4)
/* 1145 */             .add(LootItem.lootTableItem((ItemLike)Items.POISONOUS_POTATO).when(LootItemRandomChanceCondition.randomChance(0.02F))))));
/*      */ 
/*      */ 
/*      */     
/* 1149 */     add(Blocks.SWEET_BERRY_BUSH, debug0 -> (LootTable.Builder)applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable().withPool(LootPool.lootPool().when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SweetBerryBushBlock.AGE, 3))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SWEET_BERRIES)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))).withPool(LootPool.lootPool().when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SweetBerryBushBlock.AGE, 2))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SWEET_BERRIES)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1167 */     add(Blocks.BROWN_MUSHROOM_BLOCK, debug0 -> createMushroomBlockDrop(debug0, (ItemLike)Blocks.BROWN_MUSHROOM));
/* 1168 */     add(Blocks.RED_MUSHROOM_BLOCK, debug0 -> createMushroomBlockDrop(debug0, (ItemLike)Blocks.RED_MUSHROOM));
/*      */ 
/*      */     
/* 1171 */     add(Blocks.COAL_ORE, debug0 -> createOreDrop(debug0, Items.COAL));
/* 1172 */     add(Blocks.EMERALD_ORE, debug0 -> createOreDrop(debug0, Items.EMERALD));
/* 1173 */     add(Blocks.NETHER_QUARTZ_ORE, debug0 -> createOreDrop(debug0, Items.QUARTZ));
/* 1174 */     add(Blocks.DIAMOND_ORE, debug0 -> createOreDrop(debug0, Items.DIAMOND));
/*      */     
/* 1176 */     add(Blocks.NETHER_GOLD_ORE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 6.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1183 */     add(Blocks.LAPIS_ORE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 9.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1191 */     add(Blocks.COBWEB, debug0 -> createSilkTouchOrShearsDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STRING))));
/*      */ 
/*      */ 
/*      */     
/* 1195 */     add(Blocks.DEAD_BUSH, debug0 -> createShearsDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))))));
/*      */ 
/*      */ 
/*      */     
/* 1199 */     add(Blocks.NETHER_SPROUTS, BlockLoot::createShearsOnlyDrop);
/* 1200 */     add(Blocks.SEAGRASS, BlockLoot::createShearsOnlyDrop);
/* 1201 */     add(Blocks.VINE, BlockLoot::createShearsOnlyDrop);
/* 1202 */     add(Blocks.TALL_SEAGRASS, createDoublePlantShearsDrop(Blocks.SEAGRASS));
/* 1203 */     add(Blocks.LARGE_FERN, debug0 -> createDoublePlantWithSeedDrops(debug0, Blocks.FERN));
/* 1204 */     add(Blocks.TALL_GRASS, debug0 -> createDoublePlantWithSeedDrops(debug0, Blocks.GRASS));
/*      */ 
/*      */     
/* 1207 */     add(Blocks.MELON_STEM, debug0 -> createStemDrops(debug0, Items.MELON_SEEDS));
/* 1208 */     add(Blocks.ATTACHED_MELON_STEM, debug0 -> createAttachedStemDrops(debug0, Items.MELON_SEEDS));
/* 1209 */     add(Blocks.PUMPKIN_STEM, debug0 -> createStemDrops(debug0, Items.PUMPKIN_SEEDS));
/* 1210 */     add(Blocks.ATTACHED_PUMPKIN_STEM, debug0 -> createAttachedStemDrops(debug0, Items.PUMPKIN_SEEDS));
/*      */ 
/*      */     
/* 1213 */     add(Blocks.CHORUS_FLOWER, debug0 -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls((RandomIntGenerator)ConstantIntValue.exactly(1)).add(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)debug0))).when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1220 */     add(Blocks.FERN, BlockLoot::createGrassDrops);
/* 1221 */     add(Blocks.GRASS, BlockLoot::createGrassDrops);
/*      */     
/* 1223 */     add(Blocks.GLOWSTONE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.GLOWSTONE_DUST).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply((LootItemFunction.Builder)LimitCount.limitCount(IntLimiter.clamp(1, 4))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1231 */     add(Blocks.MELON, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.MELON_SLICE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 7.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply((LootItemFunction.Builder)LimitCount.limitCount(IntLimiter.upperBound(9))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1239 */     add(Blocks.REDSTONE_ORE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.REDSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(4.0F, 5.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1246 */     add(Blocks.SEA_LANTERN, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply((LootItemFunction.Builder)LimitCount.limitCount(IntLimiter.clamp(1, 5))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1254 */     add(Blocks.NETHER_WART, debug0 -> LootTable.lootTable().withPool(applyExplosionDecay((ItemLike)debug0, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool().setRolls((RandomIntGenerator)ConstantIntValue.exactly(1)).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_WART).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)NetherWartBlock.AGE, 3)))).apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)NetherWartBlock.AGE, 3))))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1267 */     add(Blocks.SNOW, debug0 -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add((LootPoolEntryContainer.Builder)AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] { AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] { LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 1))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 2)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 3)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(3))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 4)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(4))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 5)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(5))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 6)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(6))), (LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 7)))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(7))), (LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(8))) }).when(HAS_NO_SILK_TOUCH), (LootPoolEntryContainer.Builder)AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] { LootItem.lootTableItem((ItemLike)Blocks.SNOW).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 1))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 2))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(3))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 3))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(4))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 4))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(5))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 5))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(6))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 6))), LootItem.lootTableItem((ItemLike)Blocks.SNOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(7))).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(debug0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SnowLayerBlock.LAYERS, 7))), (LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SNOW_BLOCK) }) }))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1296 */     add(Blocks.GRAVEL, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, new float[] { 0.1F, 0.14285715F, 0.25F, 1.0F }))).otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1303 */     add(Blocks.CAMPFIRE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.CHARCOAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(2))))));
/*      */ 
/*      */ 
/*      */     
/* 1307 */     add(Blocks.GILDED_BLACKSTONE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, new float[] { 0.1F, 0.14285715F, 0.25F, 1.0F }))).otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)debug0)))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1314 */     add(Blocks.SOUL_CAMPFIRE, debug0 -> createSilkTouchDispatchTable(debug0, applyExplosionCondition((ItemLike)debug0, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.SOUL_SOIL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1319 */     dropWhenSilkTouch(Blocks.GLASS);
/* 1320 */     dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS);
/* 1321 */     dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS);
/* 1322 */     dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS);
/* 1323 */     dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS);
/* 1324 */     dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS);
/* 1325 */     dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS);
/* 1326 */     dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS);
/* 1327 */     dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS);
/* 1328 */     dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS);
/* 1329 */     dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS);
/* 1330 */     dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS);
/* 1331 */     dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS);
/* 1332 */     dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS);
/* 1333 */     dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS);
/* 1334 */     dropWhenSilkTouch(Blocks.RED_STAINED_GLASS);
/* 1335 */     dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS);
/*      */     
/* 1337 */     dropWhenSilkTouch(Blocks.GLASS_PANE);
/* 1338 */     dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS_PANE);
/* 1339 */     dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS_PANE);
/* 1340 */     dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS_PANE);
/* 1341 */     dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
/* 1342 */     dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS_PANE);
/* 1343 */     dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS_PANE);
/* 1344 */     dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS_PANE);
/* 1345 */     dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS_PANE);
/* 1346 */     dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
/* 1347 */     dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS_PANE);
/* 1348 */     dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS_PANE);
/* 1349 */     dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS_PANE);
/* 1350 */     dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS_PANE);
/* 1351 */     dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS_PANE);
/* 1352 */     dropWhenSilkTouch(Blocks.RED_STAINED_GLASS_PANE);
/* 1353 */     dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS_PANE);
/*      */     
/* 1355 */     dropWhenSilkTouch(Blocks.ICE);
/* 1356 */     dropWhenSilkTouch(Blocks.PACKED_ICE);
/* 1357 */     dropWhenSilkTouch(Blocks.BLUE_ICE);
/*      */     
/* 1359 */     dropWhenSilkTouch(Blocks.TURTLE_EGG);
/*      */     
/* 1361 */     dropWhenSilkTouch(Blocks.MUSHROOM_STEM);
/*      */     
/* 1363 */     dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL);
/* 1364 */     dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL);
/* 1365 */     dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL);
/* 1366 */     dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL);
/* 1367 */     dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL);
/*      */     
/* 1369 */     dropWhenSilkTouch(Blocks.TUBE_CORAL);
/* 1370 */     dropWhenSilkTouch(Blocks.BRAIN_CORAL);
/* 1371 */     dropWhenSilkTouch(Blocks.BUBBLE_CORAL);
/* 1372 */     dropWhenSilkTouch(Blocks.FIRE_CORAL);
/* 1373 */     dropWhenSilkTouch(Blocks.HORN_CORAL);
/*      */     
/* 1375 */     dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL_FAN);
/* 1376 */     dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL_FAN);
/* 1377 */     dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL_FAN);
/* 1378 */     dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL_FAN);
/* 1379 */     dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL_FAN);
/*      */     
/* 1381 */     dropWhenSilkTouch(Blocks.TUBE_CORAL_FAN);
/* 1382 */     dropWhenSilkTouch(Blocks.BRAIN_CORAL_FAN);
/* 1383 */     dropWhenSilkTouch(Blocks.BUBBLE_CORAL_FAN);
/* 1384 */     dropWhenSilkTouch(Blocks.FIRE_CORAL_FAN);
/* 1385 */     dropWhenSilkTouch(Blocks.HORN_CORAL_FAN);
/*      */     
/* 1387 */     otherWhenSilkTouch(Blocks.INFESTED_STONE, Blocks.STONE);
/* 1388 */     otherWhenSilkTouch(Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE);
/* 1389 */     otherWhenSilkTouch(Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS);
/* 1390 */     otherWhenSilkTouch(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
/* 1391 */     otherWhenSilkTouch(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
/* 1392 */     otherWhenSilkTouch(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS);
/*      */     
/* 1394 */     addNetherVinesDropTable(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
/* 1395 */     addNetherVinesDropTable(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
/*      */ 
/*      */     
/* 1398 */     add(Blocks.CAKE, noDrop());
/* 1399 */     add(Blocks.FROSTED_ICE, noDrop());
/* 1400 */     add(Blocks.SPAWNER, noDrop());
/* 1401 */     add(Blocks.FIRE, noDrop());
/* 1402 */     add(Blocks.SOUL_FIRE, noDrop());
/* 1403 */     add(Blocks.NETHER_PORTAL, noDrop());
/*      */     
/* 1405 */     Set<ResourceLocation> debug6 = Sets.newHashSet();
/* 1406 */     for (Block debug8 : Registry.BLOCK) {
/* 1407 */       ResourceLocation debug9 = debug8.getLootTable();
/* 1408 */       if (debug9 != BuiltInLootTables.EMPTY && debug6.add(debug9)) {
/* 1409 */         LootTable.Builder debug10 = this.map.remove(debug9);
/* 1410 */         if (debug10 == null) {
/* 1411 */           throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", new Object[] { debug9, Registry.BLOCK.getKey(debug8) }));
/*      */         }
/* 1413 */         debug1.accept(debug9, debug10);
/*      */       } 
/*      */     } 
/*      */     
/* 1417 */     if (!this.map.isEmpty()) {
/* 1418 */       throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
/*      */     }
/*      */   }
/*      */   
/*      */   private void addNetherVinesDropTable(Block debug1, Block debug2) {
/* 1423 */     LootTable.Builder debug3 = createSilkTouchOrShearsDispatchTable(debug1, 
/* 1424 */         LootItem.lootTableItem((ItemLike)debug1).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, new float[] { 0.33F, 0.55F, 0.77F, 1.0F })));
/* 1425 */     add(debug1, debug3);
/* 1426 */     add(debug2, debug3);
/*      */   }
/*      */   
/*      */   public static LootTable.Builder createDoorTable(Block debug0) {
/* 1430 */     return createSinglePropConditionTable(debug0, (Property<DoubleBlockHalf>)DoorBlock.HALF, DoubleBlockHalf.LOWER);
/*      */   }
/*      */   
/*      */   public void dropPottedContents(Block debug1) {
/* 1434 */     add(debug1, debug0 -> createPotFlowerItemTable((ItemLike)((FlowerPotBlock)debug0).getContent()));
/*      */   }
/*      */   
/*      */   public void otherWhenSilkTouch(Block debug1, Block debug2) {
/* 1438 */     add(debug1, createSilkTouchOnlyTable((ItemLike)debug2));
/*      */   }
/*      */   
/*      */   public void dropOther(Block debug1, ItemLike debug2) {
/* 1442 */     add(debug1, createSingleItemTable(debug2));
/*      */   }
/*      */   
/*      */   public void dropWhenSilkTouch(Block debug1) {
/* 1446 */     otherWhenSilkTouch(debug1, debug1);
/*      */   }
/*      */   
/*      */   public void dropSelf(Block debug1) {
/* 1450 */     dropOther(debug1, (ItemLike)debug1);
/*      */   }
/*      */   
/*      */   private void add(Block debug1, Function<Block, LootTable.Builder> debug2) {
/* 1454 */     add(debug1, debug2.apply(debug1));
/*      */   }
/*      */   
/*      */   private void add(Block debug1, LootTable.Builder debug2) {
/* 1458 */     this.map.put(debug1.getLootTable(), debug2);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\BlockLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */