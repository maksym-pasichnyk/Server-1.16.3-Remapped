/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public final class BlockTags
/*     */ {
/*   9 */   protected static final StaticTagHelper<Block> HELPER = StaticTags.create(new ResourceLocation("block"), TagContainer::getBlocks);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  14 */   public static final Tag.Named<Block> WOOL = bind("wool");
/*  15 */   public static final Tag.Named<Block> PLANKS = bind("planks");
/*  16 */   public static final Tag.Named<Block> STONE_BRICKS = bind("stone_bricks");
/*  17 */   public static final Tag.Named<Block> WOODEN_BUTTONS = bind("wooden_buttons");
/*  18 */   public static final Tag.Named<Block> BUTTONS = bind("buttons");
/*  19 */   public static final Tag.Named<Block> CARPETS = bind("carpets");
/*  20 */   public static final Tag.Named<Block> WOODEN_DOORS = bind("wooden_doors");
/*  21 */   public static final Tag.Named<Block> WOODEN_STAIRS = bind("wooden_stairs");
/*  22 */   public static final Tag.Named<Block> WOODEN_SLABS = bind("wooden_slabs");
/*  23 */   public static final Tag.Named<Block> WOODEN_FENCES = bind("wooden_fences");
/*  24 */   public static final Tag.Named<Block> PRESSURE_PLATES = bind("pressure_plates");
/*  25 */   public static final Tag.Named<Block> WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
/*  26 */   public static final Tag.Named<Block> STONE_PRESSURE_PLATES = bind("stone_pressure_plates");
/*  27 */   public static final Tag.Named<Block> WOODEN_TRAPDOORS = bind("wooden_trapdoors");
/*  28 */   public static final Tag.Named<Block> DOORS = bind("doors");
/*  29 */   public static final Tag.Named<Block> SAPLINGS = bind("saplings");
/*  30 */   public static final Tag.Named<Block> LOGS_THAT_BURN = bind("logs_that_burn");
/*  31 */   public static final Tag.Named<Block> LOGS = bind("logs");
/*  32 */   public static final Tag.Named<Block> DARK_OAK_LOGS = bind("dark_oak_logs");
/*  33 */   public static final Tag.Named<Block> OAK_LOGS = bind("oak_logs");
/*  34 */   public static final Tag.Named<Block> BIRCH_LOGS = bind("birch_logs");
/*  35 */   public static final Tag.Named<Block> ACACIA_LOGS = bind("acacia_logs");
/*  36 */   public static final Tag.Named<Block> JUNGLE_LOGS = bind("jungle_logs");
/*  37 */   public static final Tag.Named<Block> SPRUCE_LOGS = bind("spruce_logs");
/*  38 */   public static final Tag.Named<Block> CRIMSON_STEMS = bind("crimson_stems");
/*  39 */   public static final Tag.Named<Block> WARPED_STEMS = bind("warped_stems");
/*  40 */   public static final Tag.Named<Block> BANNERS = bind("banners");
/*  41 */   public static final Tag.Named<Block> SAND = bind("sand");
/*  42 */   public static final Tag.Named<Block> STAIRS = bind("stairs");
/*  43 */   public static final Tag.Named<Block> SLABS = bind("slabs");
/*  44 */   public static final Tag.Named<Block> WALLS = bind("walls");
/*  45 */   public static final Tag.Named<Block> ANVIL = bind("anvil");
/*  46 */   public static final Tag.Named<Block> RAILS = bind("rails");
/*  47 */   public static final Tag.Named<Block> LEAVES = bind("leaves");
/*  48 */   public static final Tag.Named<Block> TRAPDOORS = bind("trapdoors");
/*  49 */   public static final Tag.Named<Block> SMALL_FLOWERS = bind("small_flowers");
/*  50 */   public static final Tag.Named<Block> BEDS = bind("beds");
/*  51 */   public static final Tag.Named<Block> FENCES = bind("fences");
/*  52 */   public static final Tag.Named<Block> TALL_FLOWERS = bind("tall_flowers");
/*  53 */   public static final Tag.Named<Block> FLOWERS = bind("flowers");
/*  54 */   public static final Tag.Named<Block> PIGLIN_REPELLENTS = bind("piglin_repellents");
/*  55 */   public static final Tag.Named<Block> GOLD_ORES = bind("gold_ores");
/*  56 */   public static final Tag.Named<Block> NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final Tag.Named<Block> FLOWER_POTS = bind("flower_pots");
/*  62 */   public static final Tag.Named<Block> ENDERMAN_HOLDABLE = bind("enderman_holdable");
/*  63 */   public static final Tag.Named<Block> ICE = bind("ice");
/*  64 */   public static final Tag.Named<Block> VALID_SPAWN = bind("valid_spawn");
/*  65 */   public static final Tag.Named<Block> IMPERMEABLE = bind("impermeable");
/*  66 */   public static final Tag.Named<Block> UNDERWATER_BONEMEALS = bind("underwater_bonemeals");
/*  67 */   public static final Tag.Named<Block> CORAL_BLOCKS = bind("coral_blocks");
/*  68 */   public static final Tag.Named<Block> WALL_CORALS = bind("wall_corals");
/*  69 */   public static final Tag.Named<Block> CORAL_PLANTS = bind("coral_plants");
/*  70 */   public static final Tag.Named<Block> CORALS = bind("corals");
/*  71 */   public static final Tag.Named<Block> BAMBOO_PLANTABLE_ON = bind("bamboo_plantable_on");
/*  72 */   public static final Tag.Named<Block> STANDING_SIGNS = bind("standing_signs");
/*  73 */   public static final Tag.Named<Block> WALL_SIGNS = bind("wall_signs");
/*  74 */   public static final Tag.Named<Block> SIGNS = bind("signs");
/*  75 */   public static final Tag.Named<Block> DRAGON_IMMUNE = bind("dragon_immune");
/*  76 */   public static final Tag.Named<Block> WITHER_IMMUNE = bind("wither_immune");
/*  77 */   public static final Tag.Named<Block> WITHER_SUMMON_BASE_BLOCKS = bind("wither_summon_base_blocks");
/*  78 */   public static final Tag.Named<Block> BEEHIVES = bind("beehives");
/*  79 */   public static final Tag.Named<Block> CROPS = bind("crops");
/*  80 */   public static final Tag.Named<Block> BEE_GROWABLES = bind("bee_growables");
/*  81 */   public static final Tag.Named<Block> PORTALS = bind("portals");
/*  82 */   public static final Tag.Named<Block> FIRE = bind("fire");
/*  83 */   public static final Tag.Named<Block> NYLIUM = bind("nylium");
/*  84 */   public static final Tag.Named<Block> WART_BLOCKS = bind("wart_blocks");
/*  85 */   public static final Tag.Named<Block> BEACON_BASE_BLOCKS = bind("beacon_base_blocks");
/*  86 */   public static final Tag.Named<Block> SOUL_SPEED_BLOCKS = bind("soul_speed_blocks");
/*  87 */   public static final Tag.Named<Block> WALL_POST_OVERRIDE = bind("wall_post_override");
/*  88 */   public static final Tag.Named<Block> CLIMBABLE = bind("climbable");
/*  89 */   public static final Tag.Named<Block> SHULKER_BOXES = bind("shulker_boxes");
/*  90 */   public static final Tag.Named<Block> HOGLIN_REPELLENTS = bind("hoglin_repellents");
/*  91 */   public static final Tag.Named<Block> SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
/*  92 */   public static final Tag.Named<Block> STRIDER_WARM_BLOCKS = bind("strider_warm_blocks");
/*  93 */   public static final Tag.Named<Block> CAMPFIRES = bind("campfires");
/*  94 */   public static final Tag.Named<Block> GUARDED_BY_PIGLINS = bind("guarded_by_piglins");
/*  95 */   public static final Tag.Named<Block> PREVENT_MOB_SPAWNING_INSIDE = bind("prevent_mob_spawning_inside");
/*  96 */   public static final Tag.Named<Block> FENCE_GATES = bind("fence_gates");
/*  97 */   public static final Tag.Named<Block> UNSTABLE_BOTTOM_CENTER = bind("unstable_bottom_center");
/*  98 */   public static final Tag.Named<Block> MUSHROOM_GROW_BLOCK = bind("mushroom_grow_block");
/*     */   
/* 100 */   public static final Tag.Named<Block> INFINIBURN_OVERWORLD = bind("infiniburn_overworld");
/* 101 */   public static final Tag.Named<Block> INFINIBURN_NETHER = bind("infiniburn_nether");
/* 102 */   public static final Tag.Named<Block> INFINIBURN_END = bind("infiniburn_end");
/*     */   
/* 104 */   public static final Tag.Named<Block> BASE_STONE_OVERWORLD = bind("base_stone_overworld");
/* 105 */   public static final Tag.Named<Block> BASE_STONE_NETHER = bind("base_stone_nether");
/*     */   
/*     */   private static Tag.Named<Block> bind(String debug0) {
/* 108 */     return HELPER.bind(debug0);
/*     */   }
/*     */   
/*     */   public static TagCollection<Block> getAllTags() {
/* 112 */     return HELPER.getAllTags();
/*     */   }
/*     */   
/*     */   public static List<? extends Tag.Named<Block>> getWrappers() {
/* 116 */     return HELPER.getWrappers();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\BlockTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */