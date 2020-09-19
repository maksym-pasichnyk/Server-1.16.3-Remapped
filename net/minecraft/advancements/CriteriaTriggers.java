/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.critereon.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.critereon.BrewedPotionTrigger;
/*     */ import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.critereon.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
/*     */ import net.minecraft.advancements.critereon.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.critereon.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.critereon.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.critereon.EnterBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.critereon.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.critereon.ImpossibleTrigger;
/*     */ import net.minecraft.advancements.critereon.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemPickedUpByEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.KilledByCrossbowTrigger;
/*     */ import net.minecraft.advancements.critereon.KilledTrigger;
/*     */ import net.minecraft.advancements.critereon.LevitationTrigger;
/*     */ import net.minecraft.advancements.critereon.LocationTrigger;
/*     */ import net.minecraft.advancements.critereon.LootTableTrigger;
/*     */ import net.minecraft.advancements.critereon.NetherTravelTrigger;
/*     */ import net.minecraft.advancements.critereon.PlacedBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
/*     */ import net.minecraft.advancements.critereon.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.critereon.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.critereon.TameAnimalTrigger;
/*     */ import net.minecraft.advancements.critereon.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.critereon.TickTrigger;
/*     */ import net.minecraft.advancements.critereon.TradeTrigger;
/*     */ import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
/*     */ import net.minecraft.advancements.critereon.UsedTotemTrigger;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ 
/*     */ public class CriteriaTriggers
/*     */ {
/*  48 */   private static final Map<ResourceLocation, CriterionTrigger<?>> CRITERIA = Maps.newHashMap();
/*     */   
/*  50 */   public static final ImpossibleTrigger IMPOSSIBLE = register(new ImpossibleTrigger());
/*  51 */   public static final KilledTrigger PLAYER_KILLED_ENTITY = register(new KilledTrigger(new ResourceLocation("player_killed_entity")));
/*  52 */   public static final KilledTrigger ENTITY_KILLED_PLAYER = register(new KilledTrigger(new ResourceLocation("entity_killed_player")));
/*  53 */   public static final EnterBlockTrigger ENTER_BLOCK = register(new EnterBlockTrigger());
/*  54 */   public static final InventoryChangeTrigger INVENTORY_CHANGED = register(new InventoryChangeTrigger());
/*  55 */   public static final RecipeUnlockedTrigger RECIPE_UNLOCKED = register(new RecipeUnlockedTrigger());
/*  56 */   public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY = register(new PlayerHurtEntityTrigger());
/*  57 */   public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER = register(new EntityHurtPlayerTrigger());
/*  58 */   public static final EnchantedItemTrigger ENCHANTED_ITEM = register(new EnchantedItemTrigger());
/*  59 */   public static final FilledBucketTrigger FILLED_BUCKET = register(new FilledBucketTrigger());
/*  60 */   public static final BrewedPotionTrigger BREWED_POTION = register(new BrewedPotionTrigger());
/*  61 */   public static final ConstructBeaconTrigger CONSTRUCT_BEACON = register(new ConstructBeaconTrigger());
/*  62 */   public static final UsedEnderEyeTrigger USED_ENDER_EYE = register(new UsedEnderEyeTrigger());
/*  63 */   public static final SummonedEntityTrigger SUMMONED_ENTITY = register(new SummonedEntityTrigger());
/*  64 */   public static final BredAnimalsTrigger BRED_ANIMALS = register(new BredAnimalsTrigger());
/*  65 */   public static final LocationTrigger LOCATION = register(new LocationTrigger(new ResourceLocation("location")));
/*  66 */   public static final LocationTrigger SLEPT_IN_BED = register(new LocationTrigger(new ResourceLocation("slept_in_bed")));
/*  67 */   public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER = register(new CuredZombieVillagerTrigger());
/*  68 */   public static final TradeTrigger TRADE = register(new TradeTrigger());
/*  69 */   public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED = register(new ItemDurabilityTrigger());
/*  70 */   public static final LevitationTrigger LEVITATION = register(new LevitationTrigger());
/*  71 */   public static final ChangeDimensionTrigger CHANGED_DIMENSION = register(new ChangeDimensionTrigger());
/*  72 */   public static final TickTrigger TICK = register(new TickTrigger());
/*  73 */   public static final TameAnimalTrigger TAME_ANIMAL = register(new TameAnimalTrigger());
/*  74 */   public static final PlacedBlockTrigger PLACED_BLOCK = register(new PlacedBlockTrigger());
/*  75 */   public static final ConsumeItemTrigger CONSUME_ITEM = register(new ConsumeItemTrigger());
/*  76 */   public static final EffectsChangedTrigger EFFECTS_CHANGED = register(new EffectsChangedTrigger());
/*  77 */   public static final UsedTotemTrigger USED_TOTEM = register(new UsedTotemTrigger());
/*  78 */   public static final NetherTravelTrigger NETHER_TRAVEL = register(new NetherTravelTrigger());
/*  79 */   public static final FishingRodHookedTrigger FISHING_ROD_HOOKED = register(new FishingRodHookedTrigger());
/*  80 */   public static final ChanneledLightningTrigger CHANNELED_LIGHTNING = register(new ChanneledLightningTrigger());
/*  81 */   public static final ShotCrossbowTrigger SHOT_CROSSBOW = register(new ShotCrossbowTrigger());
/*  82 */   public static final KilledByCrossbowTrigger KILLED_BY_CROSSBOW = register(new KilledByCrossbowTrigger());
/*  83 */   public static final LocationTrigger RAID_WIN = register(new LocationTrigger(new ResourceLocation("hero_of_the_village")));
/*  84 */   public static final LocationTrigger BAD_OMEN = register(new LocationTrigger(new ResourceLocation("voluntary_exile")));
/*  85 */   public static final SlideDownBlockTrigger HONEY_BLOCK_SLIDE = register(new SlideDownBlockTrigger());
/*  86 */   public static final BeeNestDestroyedTrigger BEE_NEST_DESTROYED = register(new BeeNestDestroyedTrigger());
/*  87 */   public static final TargetBlockTrigger TARGET_BLOCK_HIT = register(new TargetBlockTrigger());
/*  88 */   public static final ItemUsedOnBlockTrigger ITEM_USED_ON_BLOCK = register(new ItemUsedOnBlockTrigger());
/*  89 */   public static final LootTableTrigger GENERATE_LOOT = register(new LootTableTrigger());
/*  90 */   public static final ItemPickedUpByEntityTrigger ITEM_PICKED_UP_BY_ENTITY = register(new ItemPickedUpByEntityTrigger());
/*  91 */   public static final PlayerInteractTrigger PLAYER_INTERACTED_WITH_ENTITY = register(new PlayerInteractTrigger());
/*     */   
/*     */   private static <T extends CriterionTrigger<?>> T register(T debug0) {
/*  94 */     if (CRITERIA.containsKey(debug0.getId())) {
/*  95 */       throw new IllegalArgumentException("Duplicate criterion id " + debug0.getId());
/*     */     }
/*  97 */     CRITERIA.put(debug0.getId(), (CriterionTrigger<?>)debug0);
/*  98 */     return debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T extends CriterionTriggerInstance> CriterionTrigger<T> getCriterion(ResourceLocation debug0) {
/* 104 */     return (CriterionTrigger<T>)CRITERIA.get(debug0);
/*     */   }
/*     */   
/*     */   public static Iterable<? extends CriterionTrigger<?>> all() {
/* 108 */     return CRITERIA.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\CriteriaTriggers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */