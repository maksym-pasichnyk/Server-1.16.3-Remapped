/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.advancements.critereon.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.critereon.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.critereon.EntityPredicate;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.ConstantIntValue;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.RandomIntGenerator;
/*     */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootTableReference;
/*     */ import net.minecraft.world.level.storage.loot.entries.TagEntry;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
/*     */ 
/*     */ public class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
/*  49 */   private static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(Boolean.valueOf(true)).build());
/*  50 */   private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES = (Set<EntityType<?>>)ImmutableSet.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER);
/*     */ 
/*     */ 
/*     */   
/*     */   private static LootTable.Builder createSheepTable(ItemLike debug0) {
/*  55 */     return LootTable.lootTable()
/*  56 */       .withPool(LootPool.lootPool()
/*  57 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  58 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(debug0)))
/*     */       
/*  60 */       .withPool(LootPool.lootPool()
/*  61 */         .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  62 */         .add((LootPoolEntryContainer.Builder)LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
/*     */   }
/*     */ 
/*     */   
/*  66 */   private final Map<ResourceLocation, LootTable.Builder> map = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public void accept(BiConsumer<ResourceLocation, LootTable.Builder> debug1) {
/*  70 */     add(EntityType.ARMOR_STAND, 
/*  71 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  74 */     add(EntityType.BAT, 
/*  75 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  78 */     add(EntityType.BEE, 
/*  79 */         LootTable.lootTable());
/*     */ 
/*     */     
/*  82 */     add(EntityType.BLAZE, 
/*  83 */         LootTable.lootTable()
/*  84 */         .withPool(LootPool.lootPool()
/*  85 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  86 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLAZE_ROD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/*  87 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/*  90 */     add(EntityType.CAT, 
/*  91 */         LootTable.lootTable()
/*  92 */         .withPool(LootPool.lootPool()
/*  93 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/*  94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))))));
/*     */ 
/*     */ 
/*     */     
/*  98 */     add(EntityType.CAVE_SPIDER, 
/*  99 */         LootTable.lootTable()
/* 100 */         .withPool(LootPool.lootPool()
/* 101 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 102 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 104 */         .withPool(LootPool.lootPool()
/* 105 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 107 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 110 */     add(EntityType.CHICKEN, 
/* 111 */         LootTable.lootTable()
/* 112 */         .withPool(LootPool.lootPool()
/* 113 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 114 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 116 */         .withPool(LootPool.lootPool()
/* 117 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 118 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 122 */     add(EntityType.COD, 
/* 123 */         LootTable.lootTable()
/* 124 */         .withPool(LootPool.lootPool()
/* 125 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 126 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))))
/*     */         
/* 128 */         .withPool(LootPool.lootPool()
/* 129 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 130 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 131 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 134 */     add(EntityType.COW, 
/* 135 */         LootTable.lootTable()
/* 136 */         .withPool(LootPool.lootPool()
/* 137 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 140 */         .withPool(LootPool.lootPool()
/* 141 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 142 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 146 */     add(EntityType.CREEPER, 
/* 147 */         LootTable.lootTable()
/* 148 */         .withPool(LootPool.lootPool()
/* 149 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 150 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 152 */         .withPool(LootPool.lootPool()
/* 153 */           .add((LootPoolEntryContainer.Builder)TagEntry.expandTag((Tag)ItemTags.CREEPER_DROP_MUSIC_DISCS))
/* 154 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of((Tag)EntityTypeTags.SKELETONS)))));
/*     */ 
/*     */     
/* 157 */     add(EntityType.DOLPHIN, 
/* 158 */         LootTable.lootTable()
/* 159 */         .withPool(LootPool.lootPool()
/* 160 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))));
/*     */ 
/*     */ 
/*     */     
/* 165 */     add(EntityType.DONKEY, 
/* 166 */         LootTable.lootTable()
/* 167 */         .withPool(LootPool.lootPool()
/* 168 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 173 */     add(EntityType.DROWNED, 
/* 174 */         LootTable.lootTable()
/* 175 */         .withPool(LootPool.lootPool()
/* 176 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 177 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 179 */         .withPool(LootPool.lootPool()
/* 180 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 181 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT))
/* 182 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 183 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05F, 0.01F))));
/*     */ 
/*     */     
/* 186 */     add(EntityType.ELDER_GUARDIAN, 
/* 187 */         LootTable.lootTable()
/* 188 */         .withPool(LootPool.lootPool()
/* 189 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 190 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 192 */         .withPool(LootPool.lootPool()
/* 193 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 194 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(3).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
/* 195 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 196 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */         
/* 198 */         .withPool(LootPool.lootPool()
/* 199 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 200 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WET_SPONGE))
/* 201 */           .when(LootItemKilledByPlayerCondition.killedByPlayer()))
/* 202 */         .withPool(LootPool.lootPool()
/* 203 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 204 */           .add((LootPoolEntryContainer.Builder)LootTableReference.lootTableReference(BuiltInLootTables.FISHING_FISH))
/* 205 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 206 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 209 */     add(EntityType.ENDER_DRAGON, 
/* 210 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 213 */     add(EntityType.ENDERMAN, 
/* 214 */         LootTable.lootTable()
/* 215 */         .withPool(LootPool.lootPool()
/* 216 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 217 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 221 */     add(EntityType.ENDERMITE, 
/* 222 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 225 */     add(EntityType.EVOKER, 
/* 226 */         LootTable.lootTable()
/* 227 */         .withPool(LootPool.lootPool()
/* 228 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 229 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TOTEM_OF_UNDYING)))
/*     */         
/* 231 */         .withPool(LootPool.lootPool()
/* 232 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 233 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 234 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 237 */     add(EntityType.FOX, 
/* 238 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 241 */     add(EntityType.GHAST, 
/* 242 */         LootTable.lootTable()
/* 243 */         .withPool(LootPool.lootPool()
/* 244 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 245 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GHAST_TEAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 247 */         .withPool(LootPool.lootPool()
/* 248 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 249 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 253 */     add(EntityType.GIANT, 
/* 254 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 257 */     add(EntityType.GUARDIAN, 
/* 258 */         LootTable.lootTable()
/* 259 */         .withPool(LootPool.lootPool()
/* 260 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 263 */         .withPool(LootPool.lootPool()
/* 264 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 265 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))))
/* 266 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 267 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */         
/* 269 */         .withPool(LootPool.lootPool()
/* 270 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 271 */           .add((LootPoolEntryContainer.Builder)LootTableReference.lootTableReference(BuiltInLootTables.FISHING_FISH))
/* 272 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 273 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 276 */     add(EntityType.HORSE, 
/* 277 */         LootTable.lootTable()
/* 278 */         .withPool(LootPool.lootPool()
/* 279 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 280 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 284 */     add(EntityType.HUSK, 
/* 285 */         LootTable.lootTable()
/* 286 */         .withPool(LootPool.lootPool()
/* 287 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 288 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 290 */         .withPool(LootPool.lootPool()
/* 291 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 292 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 293 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 294 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO))
/* 295 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 296 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 299 */     add(EntityType.RAVAGER, 
/* 300 */         LootTable.lootTable()
/* 301 */         .withPool(LootPool.lootPool()
/* 302 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 303 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))));
/*     */ 
/*     */ 
/*     */     
/* 307 */     add(EntityType.ILLUSIONER, 
/* 308 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 311 */     add(EntityType.IRON_GOLEM, 
/* 312 */         LootTable.lootTable()
/* 313 */         .withPool(LootPool.lootPool()
/* 314 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 315 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POPPY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F)))))
/*     */         
/* 317 */         .withPool(LootPool.lootPool()
/* 318 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 319 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(3.0F, 5.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 323 */     add(EntityType.LLAMA, 
/* 324 */         LootTable.lootTable()
/* 325 */         .withPool(LootPool.lootPool()
/* 326 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 327 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 331 */     add(EntityType.MAGMA_CUBE, 
/* 332 */         LootTable.lootTable()
/* 333 */         .withPool(LootPool.lootPool()
/* 334 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 335 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(-2.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 339 */     add(EntityType.MULE, 
/* 340 */         LootTable.lootTable()
/* 341 */         .withPool(LootPool.lootPool()
/* 342 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 343 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 347 */     add(EntityType.MOOSHROOM, 
/* 348 */         LootTable.lootTable()
/* 349 */         .withPool(LootPool.lootPool()
/* 350 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 351 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 353 */         .withPool(LootPool.lootPool()
/* 354 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 355 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 359 */     add(EntityType.OCELOT, 
/* 360 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 363 */     add(EntityType.PANDA, 
/* 364 */         LootTable.lootTable()
/* 365 */         .withPool(LootPool.lootPool()
/* 366 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 367 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1))))));
/*     */ 
/*     */ 
/*     */     
/* 371 */     add(EntityType.PARROT, 
/* 372 */         LootTable.lootTable()
/* 373 */         .withPool(LootPool.lootPool()
/* 374 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 375 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 379 */     add(EntityType.PHANTOM, 
/* 380 */         LootTable.lootTable()
/* 381 */         .withPool(LootPool.lootPool()
/* 382 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 383 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 384 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 387 */     add(EntityType.PIG, 
/* 388 */         LootTable.lootTable()
/* 389 */         .withPool(LootPool.lootPool()
/* 390 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 391 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 395 */     add(EntityType.PILLAGER, 
/* 396 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 399 */     add(EntityType.PLAYER, 
/* 400 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 403 */     add(EntityType.POLAR_BEAR, 
/* 404 */         LootTable.lootTable()
/* 405 */         .withPool(LootPool.lootPool()
/* 406 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 407 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 408 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 412 */     add(EntityType.PUFFERFISH, 
/* 413 */         LootTable.lootTable()
/* 414 */         .withPool(LootPool.lootPool()
/* 415 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 416 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUFFERFISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1)))))
/*     */         
/* 418 */         .withPool(LootPool.lootPool()
/* 419 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 420 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 421 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 424 */     add(EntityType.RABBIT, 
/* 425 */         LootTable.lootTable()
/* 426 */         .withPool(LootPool.lootPool()
/* 427 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 428 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 430 */         .withPool(LootPool.lootPool()
/* 431 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 432 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 434 */         .withPool(LootPool.lootPool()
/* 435 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 436 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT))
/* 437 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 438 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.03F))));
/*     */ 
/*     */     
/* 441 */     add(EntityType.SALMON, 
/* 442 */         LootTable.lootTable()
/* 443 */         .withPool(LootPool.lootPool()
/* 444 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 445 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))))
/*     */         
/* 447 */         .withPool(LootPool.lootPool()
/* 448 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 449 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 450 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 453 */     add(EntityType.SHEEP, 
/* 454 */         LootTable.lootTable()
/* 455 */         .withPool(LootPool.lootPool()
/* 456 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 457 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 461 */     add(BuiltInLootTables.SHEEP_BLACK, createSheepTable((ItemLike)Blocks.BLACK_WOOL));
/* 462 */     add(BuiltInLootTables.SHEEP_BLUE, createSheepTable((ItemLike)Blocks.BLUE_WOOL));
/* 463 */     add(BuiltInLootTables.SHEEP_BROWN, createSheepTable((ItemLike)Blocks.BROWN_WOOL));
/* 464 */     add(BuiltInLootTables.SHEEP_CYAN, createSheepTable((ItemLike)Blocks.CYAN_WOOL));
/* 465 */     add(BuiltInLootTables.SHEEP_GRAY, createSheepTable((ItemLike)Blocks.GRAY_WOOL));
/* 466 */     add(BuiltInLootTables.SHEEP_GREEN, createSheepTable((ItemLike)Blocks.GREEN_WOOL));
/* 467 */     add(BuiltInLootTables.SHEEP_LIGHT_BLUE, createSheepTable((ItemLike)Blocks.LIGHT_BLUE_WOOL));
/* 468 */     add(BuiltInLootTables.SHEEP_LIGHT_GRAY, createSheepTable((ItemLike)Blocks.LIGHT_GRAY_WOOL));
/* 469 */     add(BuiltInLootTables.SHEEP_LIME, createSheepTable((ItemLike)Blocks.LIME_WOOL));
/* 470 */     add(BuiltInLootTables.SHEEP_MAGENTA, createSheepTable((ItemLike)Blocks.MAGENTA_WOOL));
/* 471 */     add(BuiltInLootTables.SHEEP_ORANGE, createSheepTable((ItemLike)Blocks.ORANGE_WOOL));
/* 472 */     add(BuiltInLootTables.SHEEP_PINK, createSheepTable((ItemLike)Blocks.PINK_WOOL));
/* 473 */     add(BuiltInLootTables.SHEEP_PURPLE, createSheepTable((ItemLike)Blocks.PURPLE_WOOL));
/* 474 */     add(BuiltInLootTables.SHEEP_RED, createSheepTable((ItemLike)Blocks.RED_WOOL));
/* 475 */     add(BuiltInLootTables.SHEEP_WHITE, createSheepTable((ItemLike)Blocks.WHITE_WOOL));
/* 476 */     add(BuiltInLootTables.SHEEP_YELLOW, createSheepTable((ItemLike)Blocks.YELLOW_WOOL));
/*     */     
/* 478 */     add(EntityType.SHULKER, 
/* 479 */         LootTable.lootTable()
/* 480 */         .withPool(LootPool.lootPool()
/* 481 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 482 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHULKER_SHELL))
/* 483 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.0625F))));
/*     */ 
/*     */     
/* 486 */     add(EntityType.SILVERFISH, 
/* 487 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 490 */     add(EntityType.SKELETON, 
/* 491 */         LootTable.lootTable()
/* 492 */         .withPool(LootPool.lootPool()
/* 493 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 494 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 496 */         .withPool(LootPool.lootPool()
/* 497 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 498 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 502 */     add(EntityType.SKELETON_HORSE, 
/* 503 */         LootTable.lootTable()
/* 504 */         .withPool(LootPool.lootPool()
/* 505 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 506 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 510 */     add(EntityType.SLIME, 
/* 511 */         LootTable.lootTable()
/* 512 */         .withPool(LootPool.lootPool()
/* 513 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 514 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 518 */     add(EntityType.SNOW_GOLEM, 
/* 519 */         LootTable.lootTable()
/* 520 */         .withPool(LootPool.lootPool()
/* 521 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 522 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 15.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 526 */     add(EntityType.SPIDER, 
/* 527 */         LootTable.lootTable()
/* 528 */         .withPool(LootPool.lootPool()
/* 529 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 530 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 532 */         .withPool(LootPool.lootPool()
/* 533 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 534 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 535 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 538 */     add(EntityType.SQUID, 
/* 539 */         LootTable.lootTable()
/* 540 */         .withPool(LootPool.lootPool()
/* 541 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 542 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.INK_SAC).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 546 */     add(EntityType.STRAY, 
/* 547 */         LootTable.lootTable()
/* 548 */         .withPool(LootPool.lootPool()
/* 549 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 550 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 552 */         .withPool(LootPool.lootPool()
/* 553 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 554 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 556 */         .withPool(LootPool.lootPool()
/* 557 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 558 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetNbtFunction.setTag((CompoundTag)Util.make(new CompoundTag(), debug0 -> debug0.putString("Potion", "minecraft:slowness")))))
/* 559 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 562 */     add(EntityType.STRIDER, 
/* 563 */         LootTable.lootTable()
/* 564 */         .withPool(LootPool.lootPool()
/* 565 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 566 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 5.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 570 */     add(EntityType.TRADER_LLAMA, 
/* 571 */         LootTable.lootTable()
/* 572 */         .withPool(LootPool.lootPool()
/* 573 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 574 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 578 */     add(EntityType.TROPICAL_FISH, 
/* 579 */         LootTable.lootTable()
/* 580 */         .withPool(LootPool.lootPool()
/* 581 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 582 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TROPICAL_FISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)ConstantIntValue.exactly(1)))))
/*     */         
/* 584 */         .withPool(LootPool.lootPool()
/* 585 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 586 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 587 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/*     */ 
/*     */     
/* 590 */     add(EntityType.TURTLE, 
/* 591 */         LootTable.lootTable()
/* 592 */         .withPool(LootPool.lootPool()
/* 593 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 594 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SEAGRASS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 596 */         .withPool(LootPool.lootPool()
/* 597 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 598 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOWL))
/* 599 */           .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isLightning(Boolean.valueOf(true))))));
/*     */ 
/*     */ 
/*     */     
/* 603 */     add(EntityType.VEX, 
/* 604 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 607 */     add(EntityType.VILLAGER, 
/* 608 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 611 */     add(EntityType.WANDERING_TRADER, 
/* 612 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 615 */     add(EntityType.VINDICATOR, 
/* 616 */         LootTable.lootTable()
/* 617 */         .withPool(LootPool.lootPool()
/* 618 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 619 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 620 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*     */ 
/*     */     
/* 623 */     add(EntityType.WITCH, 
/* 624 */         LootTable.lootTable()
/* 625 */         .withPool(LootPool.lootPool()
/* 626 */           .setRolls((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))
/* 627 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOWSTONE_DUST).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 628 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUGAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 629 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 630 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 631 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLASS_BOTTLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 632 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))
/* 633 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 637 */     add(EntityType.WITHER, 
/* 638 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 641 */     add(EntityType.WITHER_SKELETON, 
/* 642 */         LootTable.lootTable()
/* 643 */         .withPool(LootPool.lootPool()
/* 644 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 645 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 647 */         .withPool(LootPool.lootPool()
/* 648 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 649 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 651 */         .withPool(LootPool.lootPool()
/* 652 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 653 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WITHER_SKELETON_SKULL))
/* 654 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 655 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 658 */     add(EntityType.WOLF, 
/* 659 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 662 */     add(EntityType.ZOGLIN, 
/* 663 */         LootTable.lootTable()
/* 664 */         .withPool(LootPool.lootPool()
/* 665 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 666 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 671 */     add(EntityType.ZOMBIE, 
/* 672 */         LootTable.lootTable()
/* 673 */         .withPool(LootPool.lootPool()
/* 674 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 675 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 677 */         .withPool(LootPool.lootPool()
/* 678 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 679 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 680 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 681 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO))
/* 682 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 683 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 686 */     add(EntityType.ZOMBIE_HORSE, 
/* 687 */         LootTable.lootTable()
/* 688 */         .withPool(LootPool.lootPool()
/* 689 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 690 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 694 */     add(EntityType.ZOMBIFIED_PIGLIN, 
/* 695 */         LootTable.lootTable()
/* 696 */         .withPool(LootPool.lootPool()
/* 697 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 698 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 700 */         .withPool(LootPool.lootPool()
/* 701 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 702 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 704 */         .withPool(LootPool.lootPool()
/* 705 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 706 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT))
/* 707 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 708 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 711 */     add(EntityType.HOGLIN, 
/* 712 */         LootTable.lootTable()
/* 713 */         .withPool(LootPool.lootPool()
/* 714 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 715 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(2.0F, 4.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 717 */         .withPool(LootPool.lootPool()
/* 718 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 719 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F))))));
/*     */ 
/*     */ 
/*     */     
/* 723 */     add(EntityType.PIGLIN, 
/* 724 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 727 */     add(EntityType.PIGLIN_BRUTE, 
/* 728 */         LootTable.lootTable());
/*     */ 
/*     */     
/* 731 */     add(EntityType.ZOMBIE_VILLAGER, 
/* 732 */         LootTable.lootTable()
/* 733 */         .withPool(LootPool.lootPool()
/* 734 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 735 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((RandomIntGenerator)RandomValueBounds.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0.0F, 1.0F)))))
/*     */         
/* 737 */         .withPool(LootPool.lootPool()
/* 738 */           .setRolls((RandomIntGenerator)ConstantIntValue.exactly(1))
/* 739 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 740 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 741 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO))
/* 742 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 743 */           .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
/*     */ 
/*     */     
/* 746 */     Set<ResourceLocation> debug2 = Sets.newHashSet();
/* 747 */     for (EntityType<?> debug4 : (Iterable<EntityType<?>>)Registry.ENTITY_TYPE) {
/* 748 */       ResourceLocation debug5 = debug4.getDefaultLootTable();
/* 749 */       if (SPECIAL_LOOT_TABLE_TYPES.contains(debug4) || debug4.getCategory() != MobCategory.MISC) {
/* 750 */         if (debug5 != BuiltInLootTables.EMPTY && debug2.add(debug5)) {
/* 751 */           LootTable.Builder debug6 = this.map.remove(debug5);
/* 752 */           if (debug6 == null) {
/* 753 */             throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", new Object[] { debug5, Registry.ENTITY_TYPE.getKey(debug4) }));
/*     */           }
/* 755 */           debug1.accept(debug5, debug6);
/*     */         }  continue;
/*     */       } 
/* 758 */       if (debug5 != BuiltInLootTables.EMPTY && this.map.remove(debug5) != null) {
/* 759 */         throw new IllegalStateException(String.format("Weird loottable '%s' for '%s', not a LivingEntity so should not have loot", new Object[] { debug5, Registry.ENTITY_TYPE.getKey(debug4) }));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 764 */     this.map.forEach(debug1::accept);
/*     */   }
/*     */   
/*     */   private void add(EntityType<?> debug1, LootTable.Builder debug2) {
/* 768 */     add(debug1.getDefaultLootTable(), debug2);
/*     */   }
/*     */   
/*     */   private void add(ResourceLocation debug1, LootTable.Builder debug2) {
/* 772 */     this.map.put(debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\loot\EntityLoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */