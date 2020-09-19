/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.decoration.Motive;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.VillagerType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.entity.schedule.Schedule;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.carver.WorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElementType;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
/*     */ import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
/*     */ import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class Registry<T>
/*     */   implements Codec<T>, Keyable, IdMap<T>
/*     */ {
/*  97 */   protected static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  99 */   private static final Map<ResourceLocation, Supplier<?>> LOADERS = Maps.newLinkedHashMap();
/*     */   
/* 101 */   public static final ResourceLocation ROOT_REGISTRY_NAME = new ResourceLocation("root");
/* 102 */   protected static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY = new MappedRegistry<>((ResourceKey)createRegistryKey("root"), Lifecycle.experimental());
/* 103 */   public static final Registry<? extends Registry<?>> REGISTRY = (Registry)WRITABLE_REGISTRY;
/*     */   
/* 105 */   public static final ResourceKey<Registry<SoundEvent>> SOUND_EVENT_REGISTRY = createRegistryKey("sound_event");
/* 106 */   public static final ResourceKey<Registry<Fluid>> FLUID_REGISTRY = createRegistryKey("fluid");
/* 107 */   public static final ResourceKey<Registry<MobEffect>> MOB_EFFECT_REGISTRY = createRegistryKey("mob_effect");
/* 108 */   public static final ResourceKey<Registry<Block>> BLOCK_REGISTRY = createRegistryKey("block");
/* 109 */   public static final ResourceKey<Registry<Enchantment>> ENCHANTMENT_REGISTRY = createRegistryKey("enchantment");
/* 110 */   public static final ResourceKey<Registry<EntityType<?>>> ENTITY_TYPE_REGISTRY = createRegistryKey("entity_type");
/* 111 */   public static final ResourceKey<Registry<Item>> ITEM_REGISTRY = createRegistryKey("item");
/* 112 */   public static final ResourceKey<Registry<Potion>> POTION_REGISTRY = createRegistryKey("potion");
/* 113 */   public static final ResourceKey<Registry<ParticleType<?>>> PARTICLE_TYPE_REGISTRY = createRegistryKey("particle_type");
/* 114 */   public static final ResourceKey<Registry<BlockEntityType<?>>> BLOCK_ENTITY_TYPE_REGISTRY = createRegistryKey("block_entity_type");
/* 115 */   public static final ResourceKey<Registry<Motive>> MOTIVE_REGISTRY = createRegistryKey("motive");
/* 116 */   public static final ResourceKey<Registry<ResourceLocation>> CUSTOM_STAT_REGISTRY = createRegistryKey("custom_stat");
/* 117 */   public static final ResourceKey<Registry<ChunkStatus>> CHUNK_STATUS_REGISTRY = createRegistryKey("chunk_status");
/* 118 */   public static final ResourceKey<Registry<RuleTestType<?>>> RULE_TEST_REGISTRY = createRegistryKey("rule_test");
/* 119 */   public static final ResourceKey<Registry<PosRuleTestType<?>>> POS_RULE_TEST_REGISTRY = createRegistryKey("pos_rule_test");
/* 120 */   public static final ResourceKey<Registry<MenuType<?>>> MENU_REGISTRY = createRegistryKey("menu");
/* 121 */   public static final ResourceKey<Registry<RecipeType<?>>> RECIPE_TYPE_REGISTRY = createRegistryKey("recipe_type");
/* 122 */   public static final ResourceKey<Registry<RecipeSerializer<?>>> RECIPE_SERIALIZER_REGISTRY = createRegistryKey("recipe_serializer");
/* 123 */   public static final ResourceKey<Registry<Attribute>> ATTRIBUTE_REGISTRY = createRegistryKey("attribute");
/*     */   
/* 125 */   public static final ResourceKey<Registry<StatType<?>>> STAT_TYPE_REGISTRY = createRegistryKey("stat_type");
/*     */   
/* 127 */   public static final ResourceKey<Registry<VillagerType>> VILLAGER_TYPE_REGISTRY = createRegistryKey("villager_type");
/* 128 */   public static final ResourceKey<Registry<VillagerProfession>> VILLAGER_PROFESSION_REGISTRY = createRegistryKey("villager_profession");
/* 129 */   public static final ResourceKey<Registry<PoiType>> POINT_OF_INTEREST_TYPE_REGISTRY = createRegistryKey("point_of_interest_type");
/* 130 */   public static final ResourceKey<Registry<MemoryModuleType<?>>> MEMORY_MODULE_TYPE_REGISTRY = createRegistryKey("memory_module_type");
/* 131 */   public static final ResourceKey<Registry<SensorType<?>>> SENSOR_TYPE_REGISTRY = createRegistryKey("sensor_type");
/*     */   
/* 133 */   public static final ResourceKey<Registry<Schedule>> SCHEDULE_REGISTRY = createRegistryKey("schedule");
/* 134 */   public static final ResourceKey<Registry<Activity>> ACTIVITY_REGISTRY = createRegistryKey("activity");
/*     */   
/* 136 */   public static final ResourceKey<Registry<LootPoolEntryType>> LOOT_ENTRY_REGISTRY = createRegistryKey("loot_pool_entry_type");
/* 137 */   public static final ResourceKey<Registry<LootItemFunctionType>> LOOT_FUNCTION_REGISTRY = createRegistryKey("loot_function_type");
/* 138 */   public static final ResourceKey<Registry<LootItemConditionType>> LOOT_ITEM_REGISTRY = createRegistryKey("loot_condition_type");
/*     */   
/* 140 */   public static final ResourceKey<Registry<DimensionType>> DIMENSION_TYPE_REGISTRY = createRegistryKey("dimension_type");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static final ResourceKey<Registry<Level>> DIMENSION_REGISTRY = createRegistryKey("dimension");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public static final ResourceKey<Registry<LevelStem>> LEVEL_STEM_REGISTRY = createRegistryKey("dimension");
/*     */   
/* 152 */   public static final Registry<SoundEvent> SOUND_EVENT = registerSimple(SOUND_EVENT_REGISTRY, () -> SoundEvents.ITEM_PICKUP);
/* 153 */   public static final DefaultedRegistry<Fluid> FLUID = registerDefaulted(FLUID_REGISTRY, "empty", () -> Fluids.EMPTY);
/* 154 */   public static final Registry<MobEffect> MOB_EFFECT = registerSimple(MOB_EFFECT_REGISTRY, () -> MobEffects.LUCK);
/* 155 */   public static final DefaultedRegistry<Block> BLOCK = registerDefaulted(BLOCK_REGISTRY, "air", () -> Blocks.AIR);
/* 156 */   public static final Registry<Enchantment> ENCHANTMENT = registerSimple(ENCHANTMENT_REGISTRY, () -> Enchantments.BLOCK_FORTUNE);
/* 157 */   public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE = registerDefaulted(ENTITY_TYPE_REGISTRY, "pig", () -> EntityType.PIG);
/* 158 */   public static final DefaultedRegistry<Item> ITEM = registerDefaulted(ITEM_REGISTRY, "air", () -> Items.AIR);
/* 159 */   public static final DefaultedRegistry<Potion> POTION = registerDefaulted(POTION_REGISTRY, "empty", () -> Potions.EMPTY);
/* 160 */   public static final Registry<ParticleType<?>> PARTICLE_TYPE = registerSimple(PARTICLE_TYPE_REGISTRY, () -> ParticleTypes.BLOCK);
/* 161 */   public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = registerSimple(BLOCK_ENTITY_TYPE_REGISTRY, () -> BlockEntityType.FURNACE);
/* 162 */   public static final DefaultedRegistry<Motive> MOTIVE = registerDefaulted(MOTIVE_REGISTRY, "kebab", () -> Motive.KEBAB);
/* 163 */   public static final Registry<ResourceLocation> CUSTOM_STAT = registerSimple(CUSTOM_STAT_REGISTRY, () -> Stats.JUMP);
/* 164 */   public static final DefaultedRegistry<ChunkStatus> CHUNK_STATUS = registerDefaulted(CHUNK_STATUS_REGISTRY, "empty", () -> ChunkStatus.EMPTY);
/* 165 */   public static final Registry<RuleTestType<?>> RULE_TEST = registerSimple(RULE_TEST_REGISTRY, () -> RuleTestType.ALWAYS_TRUE_TEST);
/* 166 */   public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = registerSimple(POS_RULE_TEST_REGISTRY, () -> PosRuleTestType.ALWAYS_TRUE_TEST);
/* 167 */   public static final Registry<MenuType<?>> MENU = registerSimple(MENU_REGISTRY, () -> MenuType.ANVIL);
/* 168 */   public static final Registry<RecipeType<?>> RECIPE_TYPE = registerSimple(RECIPE_TYPE_REGISTRY, () -> RecipeType.CRAFTING);
/* 169 */   public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER = registerSimple(RECIPE_SERIALIZER_REGISTRY, () -> RecipeSerializer.SHAPELESS_RECIPE);
/* 170 */   public static final Registry<Attribute> ATTRIBUTE = registerSimple(ATTRIBUTE_REGISTRY, () -> Attributes.LUCK);
/*     */   
/* 172 */   public static final Registry<StatType<?>> STAT_TYPE = registerSimple(STAT_TYPE_REGISTRY, () -> Stats.ITEM_USED);
/*     */   
/* 174 */   public static final DefaultedRegistry<VillagerType> VILLAGER_TYPE = registerDefaulted(VILLAGER_TYPE_REGISTRY, "plains", () -> VillagerType.PLAINS);
/* 175 */   public static final DefaultedRegistry<VillagerProfession> VILLAGER_PROFESSION = registerDefaulted(VILLAGER_PROFESSION_REGISTRY, "none", () -> VillagerProfession.NONE);
/* 176 */   public static final DefaultedRegistry<PoiType> POINT_OF_INTEREST_TYPE = registerDefaulted(POINT_OF_INTEREST_TYPE_REGISTRY, "unemployed", () -> PoiType.UNEMPLOYED);
/* 177 */   public static final DefaultedRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPE = registerDefaulted(MEMORY_MODULE_TYPE_REGISTRY, "dummy", () -> MemoryModuleType.DUMMY);
/* 178 */   public static final DefaultedRegistry<SensorType<?>> SENSOR_TYPE = registerDefaulted(SENSOR_TYPE_REGISTRY, "dummy", () -> SensorType.DUMMY);
/*     */   
/* 180 */   public static final Registry<Schedule> SCHEDULE = registerSimple(SCHEDULE_REGISTRY, () -> Schedule.EMPTY);
/* 181 */   public static final Registry<Activity> ACTIVITY = registerSimple(ACTIVITY_REGISTRY, () -> Activity.IDLE);
/*     */   
/* 183 */   public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = registerSimple(LOOT_ENTRY_REGISTRY, () -> LootPoolEntries.EMPTY);
/* 184 */   public static final Registry<LootItemFunctionType> LOOT_FUNCTION_TYPE = registerSimple(LOOT_FUNCTION_REGISTRY, () -> LootItemFunctions.SET_COUNT);
/* 185 */   public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE = registerSimple(LOOT_ITEM_REGISTRY, () -> LootItemConditions.INVERTED);
/*     */   
/* 187 */   public static final ResourceKey<Registry<NoiseGeneratorSettings>> NOISE_GENERATOR_SETTINGS_REGISTRY = createRegistryKey("worldgen/noise_settings");
/*     */   
/* 189 */   public static final ResourceKey<Registry<ConfiguredSurfaceBuilder<?>>> CONFIGURED_SURFACE_BUILDER_REGISTRY = createRegistryKey("worldgen/configured_surface_builder");
/* 190 */   public static final ResourceKey<Registry<ConfiguredWorldCarver<?>>> CONFIGURED_CARVER_REGISTRY = createRegistryKey("worldgen/configured_carver");
/* 191 */   public static final ResourceKey<Registry<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURE_REGISTRY = createRegistryKey("worldgen/configured_feature");
/* 192 */   public static final ResourceKey<Registry<ConfiguredStructureFeature<?, ?>>> CONFIGURED_STRUCTURE_FEATURE_REGISTRY = createRegistryKey("worldgen/configured_structure_feature");
/* 193 */   public static final ResourceKey<Registry<StructureProcessorList>> PROCESSOR_LIST_REGISTRY = createRegistryKey("worldgen/processor_list");
/* 194 */   public static final ResourceKey<Registry<StructureTemplatePool>> TEMPLATE_POOL_REGISTRY = createRegistryKey("worldgen/template_pool");
/* 195 */   public static final ResourceKey<Registry<Biome>> BIOME_REGISTRY = createRegistryKey("worldgen/biome");
/*     */   
/* 197 */   public static final ResourceKey<Registry<SurfaceBuilder<?>>> SURFACE_BUILDER_REGISTRY = createRegistryKey("worldgen/surface_builder");
/* 198 */   public static final Registry<SurfaceBuilder<?>> SURFACE_BUILDER = registerSimple(SURFACE_BUILDER_REGISTRY, () -> SurfaceBuilder.DEFAULT);
/*     */   
/* 200 */   public static final ResourceKey<Registry<WorldCarver<?>>> CARVER_REGISTRY = createRegistryKey("worldgen/carver");
/* 201 */   public static final Registry<WorldCarver<?>> CARVER = registerSimple(CARVER_REGISTRY, () -> WorldCarver.CAVE);
/*     */   
/* 203 */   public static final ResourceKey<Registry<Feature<?>>> FEATURE_REGISTRY = createRegistryKey("worldgen/feature");
/* 204 */   public static final Registry<Feature<?>> FEATURE = registerSimple(FEATURE_REGISTRY, () -> Feature.ORE);
/*     */   
/* 206 */   public static final ResourceKey<Registry<StructureFeature<?>>> STRUCTURE_FEATURE_REGISTRY = createRegistryKey("worldgen/structure_feature");
/* 207 */   public static final Registry<StructureFeature<?>> STRUCTURE_FEATURE = registerSimple(STRUCTURE_FEATURE_REGISTRY, () -> StructureFeature.MINESHAFT);
/*     */   
/* 209 */   public static final ResourceKey<Registry<StructurePieceType>> STRUCTURE_PIECE_REGISTRY = createRegistryKey("worldgen/structure_piece");
/* 210 */   public static final Registry<StructurePieceType> STRUCTURE_PIECE = registerSimple(STRUCTURE_PIECE_REGISTRY, () -> StructurePieceType.MINE_SHAFT_ROOM);
/* 211 */   public static final ResourceKey<Registry<FeatureDecorator<?>>> DECORATOR_REGISTRY = createRegistryKey("worldgen/decorator");
/* 212 */   public static final Registry<FeatureDecorator<?>> DECORATOR = registerSimple(DECORATOR_REGISTRY, () -> FeatureDecorator.NOPE);
/*     */   
/* 214 */   public static final ResourceKey<Registry<BlockStateProviderType<?>>> BLOCK_STATE_PROVIDER_TYPE_REGISTRY = createRegistryKey("worldgen/block_state_provider_type");
/* 215 */   public static final ResourceKey<Registry<BlockPlacerType<?>>> BLOCK_PLACER_TYPE_REGISTRY = createRegistryKey("worldgen/block_placer_type");
/* 216 */   public static final ResourceKey<Registry<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPE_REGISTRY = createRegistryKey("worldgen/foliage_placer_type");
/* 217 */   public static final ResourceKey<Registry<TrunkPlacerType<?>>> TRUNK_PLACER_TYPE_REGISTRY = createRegistryKey("worldgen/trunk_placer_type");
/* 218 */   public static final ResourceKey<Registry<TreeDecoratorType<?>>> TREE_DECORATOR_TYPE_REGISTRY = createRegistryKey("worldgen/tree_decorator_type");
/* 219 */   public static final ResourceKey<Registry<FeatureSizeType<?>>> FEATURE_SIZE_TYPE_REGISTRY = createRegistryKey("worldgen/feature_size_type");
/* 220 */   public static final ResourceKey<Registry<Codec<? extends BiomeSource>>> BIOME_SOURCE_REGISTRY = createRegistryKey("worldgen/biome_source");
/* 221 */   public static final ResourceKey<Registry<Codec<? extends ChunkGenerator>>> CHUNK_GENERATOR_REGISTRY = createRegistryKey("worldgen/chunk_generator");
/* 222 */   public static final ResourceKey<Registry<StructureProcessorType<?>>> STRUCTURE_PROCESSOR_REGISTRY = createRegistryKey("worldgen/structure_processor");
/* 223 */   public static final ResourceKey<Registry<StructurePoolElementType<?>>> STRUCTURE_POOL_ELEMENT_REGISTRY = createRegistryKey("worldgen/structure_pool_element");
/*     */   
/* 225 */   public static final Registry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPES = registerSimple(BLOCK_STATE_PROVIDER_TYPE_REGISTRY, () -> BlockStateProviderType.SIMPLE_STATE_PROVIDER);
/* 226 */   public static final Registry<BlockPlacerType<?>> BLOCK_PLACER_TYPES = registerSimple(BLOCK_PLACER_TYPE_REGISTRY, () -> BlockPlacerType.SIMPLE_BLOCK_PLACER);
/* 227 */   public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = registerSimple(FOLIAGE_PLACER_TYPE_REGISTRY, () -> FoliagePlacerType.BLOB_FOLIAGE_PLACER);
/* 228 */   public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = registerSimple(TRUNK_PLACER_TYPE_REGISTRY, () -> TrunkPlacerType.STRAIGHT_TRUNK_PLACER);
/* 229 */   public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = registerSimple(TREE_DECORATOR_TYPE_REGISTRY, () -> TreeDecoratorType.LEAVE_VINE);
/* 230 */   public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPES = registerSimple(FEATURE_SIZE_TYPE_REGISTRY, () -> FeatureSizeType.TWO_LAYERS_FEATURE_SIZE);
/* 231 */   public static final Registry<Codec<? extends BiomeSource>> BIOME_SOURCE = registerSimple(BIOME_SOURCE_REGISTRY, Lifecycle.stable(), () -> BiomeSource.CODEC);
/* 232 */   public static final Registry<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = registerSimple(CHUNK_GENERATOR_REGISTRY, Lifecycle.stable(), () -> ChunkGenerator.CODEC);
/* 233 */   public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = registerSimple(STRUCTURE_PROCESSOR_REGISTRY, () -> StructureProcessorType.BLOCK_IGNORE);
/* 234 */   public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = registerSimple(STRUCTURE_POOL_ELEMENT_REGISTRY, () -> StructurePoolElementType.EMPTY); private final ResourceKey<? extends Registry<T>> key;
/*     */   
/*     */   static {
/* 237 */     BuiltinRegistries.bootstrap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     LOADERS.forEach((debug0, debug1) -> {
/*     */           if (debug1.get() == null) {
/*     */             LOGGER.error("Unable to bootstrap registry '{}'", debug0);
/*     */           }
/*     */         });
/*     */     
/* 251 */     checkRegistry(WRITABLE_REGISTRY);
/*     */   }
/*     */   private final Lifecycle lifecycle;
/*     */   public static <T extends WritableRegistry<?>> void checkRegistry(WritableRegistry<T> debug0) {
/* 255 */     debug0.forEach(debug1 -> {
/*     */           if (debug1.keySet().isEmpty()) {
/*     */             LOGGER.error("Registry '{}' was empty after loading", debug0.getKey(debug1));
/*     */             if (SharedConstants.IS_RUNNING_IN_IDE)
/*     */               throw new IllegalStateException("Registry: '" + debug0.getKey(debug1) + "' is empty, not allowed, fix me!"); 
/*     */           } 
/*     */           if (debug1 instanceof DefaultedRegistry) {
/*     */             ResourceLocation debug2 = ((DefaultedRegistry)debug1).getDefaultKey();
/*     */             Validate.notNull(debug1.get(debug2), "Missing default of DefaultedMappedRegistry: " + debug2, new Object[0]);
/*     */           } 
/*     */         });
/*     */   }
/*     */   private static <T> ResourceKey<Registry<T>> createRegistryKey(String debug0) {
/*     */     return ResourceKey.createRegistryKey(new ResourceLocation(debug0));
/*     */   }
/*     */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> debug0, Supplier<T> debug1) {
/* 271 */     return registerSimple(debug0, Lifecycle.experimental(), debug1);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> debug0, String debug1, Supplier<T> debug2) {
/* 275 */     return registerDefaulted(debug0, debug1, Lifecycle.experimental(), debug2);
/*     */   }
/*     */   
/*     */   private static <T> Registry<T> registerSimple(ResourceKey<? extends Registry<T>> debug0, Lifecycle debug1, Supplier<T> debug2) {
/* 279 */     return internalRegister(debug0, new MappedRegistry<>(debug0, debug1), debug2, debug1);
/*     */   }
/*     */   
/*     */   private static <T> DefaultedRegistry<T> registerDefaulted(ResourceKey<? extends Registry<T>> debug0, String debug1, Lifecycle debug2, Supplier<T> debug3) {
/* 283 */     return internalRegister(debug0, new DefaultedRegistry<>(debug1, debug0, debug2), debug3, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T, R extends WritableRegistry<T>> R internalRegister(ResourceKey<? extends Registry<T>> debug0, R debug1, Supplier<T> debug2, Lifecycle debug3) {
/* 288 */     ResourceLocation debug4 = debug0.location();
/* 289 */     LOADERS.put(debug4, debug2);
/*     */     
/* 291 */     WritableRegistry<WritableRegistry<?>> writableRegistry = WRITABLE_REGISTRY;
/* 292 */     return (R)writableRegistry.<WritableRegistry>register(debug0, (WritableRegistry)debug1, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Registry(ResourceKey<? extends Registry<T>> debug1, Lifecycle debug2) {
/* 299 */     this.key = debug1;
/* 300 */     this.lifecycle = debug2;
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<T>> key() {
/* 304 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 309 */     return "Registry[" + this.key + " (" + this.lifecycle + ")]";
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> DataResult<Pair<T, U>> decode(DynamicOps<U> debug1, U debug2) {
/* 314 */     if (debug1.compressMaps()) {
/* 315 */       return debug1.getNumberValue(debug2).flatMap(debug1 -> {
/*     */             T debug2 = byId(debug1.intValue());
/*     */ 
/*     */ 
/*     */             
/*     */             return (debug2 == null) ? DataResult.error("Unknown registry id: " + debug1) : DataResult.success(debug2, lifecycle(debug2));
/* 321 */           }).map(debug1 -> Pair.of(debug1, debug0.empty()));
/*     */     }
/* 323 */     return ResourceLocation.CODEC.decode(debug1, debug2).flatMap(debug1 -> {
/*     */           T debug2 = get((ResourceLocation)debug1.getFirst());
/*     */           return (debug2 == null) ? DataResult.error("Unknown registry key: " + debug1.getFirst()) : DataResult.success(Pair.of(debug2, debug1.getSecond()), lifecycle(debug2));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> DataResult<U> encode(T debug1, DynamicOps<U> debug2, U debug3) {
/* 334 */     ResourceLocation debug4 = getKey(debug1);
/* 335 */     if (debug4 == null) {
/* 336 */       return DataResult.error("Unknown registry element " + debug1);
/*     */     }
/* 338 */     if (debug2.compressMaps()) {
/* 339 */       return debug2.mergeToPrimitive(debug3, debug2.createInt(getId(debug1))).setLifecycle(this.lifecycle);
/*     */     }
/* 341 */     return debug2.mergeToPrimitive(debug3, debug2.createString(debug4.toString())).setLifecycle(this.lifecycle);
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Stream<U> keys(DynamicOps<U> debug1) {
/* 346 */     return keySet().stream().map(debug1 -> debug0.createString(debug1.toString()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<T> getOptional(@Nullable ResourceLocation debug1) {
/* 368 */     return Optional.ofNullable(get(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getOrThrow(ResourceKey<T> debug1) {
/* 376 */     T debug2 = get(debug1);
/* 377 */     if (debug2 == null) {
/* 378 */       throw new IllegalStateException("Missing: " + debug1);
/*     */     }
/* 380 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stream<T> stream() {
/* 391 */     return StreamSupport.stream(spliterator(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T register(Registry<? super T> debug0, String debug1, T debug2) {
/* 399 */     return register(debug0, new ResourceLocation(debug1), debug2);
/*     */   }
/*     */   
/*     */   public static <V, T extends V> T register(Registry<V> debug0, ResourceLocation debug1, T debug2) {
/* 403 */     return (T)((WritableRegistry)debug0).register(ResourceKey.create(debug0.key, debug1), debug2, Lifecycle.stable());
/*     */   }
/*     */   
/*     */   public static <V, T extends V> T registerMapping(Registry<V> debug0, int debug1, String debug2, T debug3) {
/* 407 */     return (T)((WritableRegistry)debug0).registerMapping(debug1, ResourceKey.create(debug0.key, new ResourceLocation(debug2)), debug3, Lifecycle.stable());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public abstract ResourceLocation getKey(T paramT);
/*     */   
/*     */   public abstract Optional<ResourceKey<T>> getResourceKey(T paramT);
/*     */   
/*     */   public abstract int getId(@Nullable T paramT);
/*     */   
/*     */   @Nullable
/*     */   public abstract T get(@Nullable ResourceKey<T> paramResourceKey);
/*     */   
/*     */   @Nullable
/*     */   public abstract T get(@Nullable ResourceLocation paramResourceLocation);
/*     */   
/*     */   protected abstract Lifecycle lifecycle(T paramT);
/*     */   
/*     */   public abstract Lifecycle elementsLifecycle();
/*     */   
/*     */   public abstract Set<ResourceLocation> keySet();
/*     */   
/*     */   public abstract Set<Map.Entry<ResourceKey<T>, T>> entrySet();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Registry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */