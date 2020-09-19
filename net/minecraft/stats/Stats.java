/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class Stats
/*     */ {
/*  11 */   public static final StatType<Block> BLOCK_MINED = makeRegistryStatType("mined", (Registry<Block>)Registry.BLOCK);
/*  12 */   public static final StatType<Item> ITEM_CRAFTED = makeRegistryStatType("crafted", (Registry<Item>)Registry.ITEM);
/*  13 */   public static final StatType<Item> ITEM_USED = makeRegistryStatType("used", (Registry<Item>)Registry.ITEM);
/*  14 */   public static final StatType<Item> ITEM_BROKEN = makeRegistryStatType("broken", (Registry<Item>)Registry.ITEM);
/*  15 */   public static final StatType<Item> ITEM_PICKED_UP = makeRegistryStatType("picked_up", (Registry<Item>)Registry.ITEM);
/*  16 */   public static final StatType<Item> ITEM_DROPPED = makeRegistryStatType("dropped", (Registry<Item>)Registry.ITEM);
/*  17 */   public static final StatType<EntityType<?>> ENTITY_KILLED = makeRegistryStatType("killed", (Registry<EntityType<?>>)Registry.ENTITY_TYPE);
/*  18 */   public static final StatType<EntityType<?>> ENTITY_KILLED_BY = makeRegistryStatType("killed_by", (Registry<EntityType<?>>)Registry.ENTITY_TYPE);
/*  19 */   public static final StatType<ResourceLocation> CUSTOM = makeRegistryStatType("custom", Registry.CUSTOM_STAT);
/*     */ 
/*     */   
/*  22 */   public static final ResourceLocation LEAVE_GAME = makeCustomStat("leave_game", StatFormatter.DEFAULT);
/*     */   
/*  24 */   public static final ResourceLocation PLAY_ONE_MINUTE = makeCustomStat("play_one_minute", StatFormatter.TIME);
/*  25 */   public static final ResourceLocation TIME_SINCE_DEATH = makeCustomStat("time_since_death", StatFormatter.TIME);
/*  26 */   public static final ResourceLocation TIME_SINCE_REST = makeCustomStat("time_since_rest", StatFormatter.TIME);
/*  27 */   public static final ResourceLocation CROUCH_TIME = makeCustomStat("sneak_time", StatFormatter.TIME);
/*  28 */   public static final ResourceLocation WALK_ONE_CM = makeCustomStat("walk_one_cm", StatFormatter.DISTANCE);
/*  29 */   public static final ResourceLocation CROUCH_ONE_CM = makeCustomStat("crouch_one_cm", StatFormatter.DISTANCE);
/*  30 */   public static final ResourceLocation SPRINT_ONE_CM = makeCustomStat("sprint_one_cm", StatFormatter.DISTANCE);
/*  31 */   public static final ResourceLocation WALK_ON_WATER_ONE_CM = makeCustomStat("walk_on_water_one_cm", StatFormatter.DISTANCE);
/*  32 */   public static final ResourceLocation FALL_ONE_CM = makeCustomStat("fall_one_cm", StatFormatter.DISTANCE);
/*  33 */   public static final ResourceLocation CLIMB_ONE_CM = makeCustomStat("climb_one_cm", StatFormatter.DISTANCE);
/*  34 */   public static final ResourceLocation FLY_ONE_CM = makeCustomStat("fly_one_cm", StatFormatter.DISTANCE);
/*  35 */   public static final ResourceLocation WALK_UNDER_WATER_ONE_CM = makeCustomStat("walk_under_water_one_cm", StatFormatter.DISTANCE);
/*  36 */   public static final ResourceLocation MINECART_ONE_CM = makeCustomStat("minecart_one_cm", StatFormatter.DISTANCE);
/*  37 */   public static final ResourceLocation BOAT_ONE_CM = makeCustomStat("boat_one_cm", StatFormatter.DISTANCE);
/*  38 */   public static final ResourceLocation PIG_ONE_CM = makeCustomStat("pig_one_cm", StatFormatter.DISTANCE);
/*  39 */   public static final ResourceLocation HORSE_ONE_CM = makeCustomStat("horse_one_cm", StatFormatter.DISTANCE);
/*  40 */   public static final ResourceLocation AVIATE_ONE_CM = makeCustomStat("aviate_one_cm", StatFormatter.DISTANCE);
/*  41 */   public static final ResourceLocation SWIM_ONE_CM = makeCustomStat("swim_one_cm", StatFormatter.DISTANCE);
/*  42 */   public static final ResourceLocation STRIDER_ONE_CM = makeCustomStat("strider_one_cm", StatFormatter.DISTANCE);
/*     */   
/*  44 */   public static final ResourceLocation JUMP = makeCustomStat("jump", StatFormatter.DEFAULT);
/*  45 */   public static final ResourceLocation DROP = makeCustomStat("drop", StatFormatter.DEFAULT);
/*     */   
/*  47 */   public static final ResourceLocation DAMAGE_DEALT = makeCustomStat("damage_dealt", StatFormatter.DIVIDE_BY_TEN);
/*  48 */   public static final ResourceLocation DAMAGE_DEALT_ABSORBED = makeCustomStat("damage_dealt_absorbed", StatFormatter.DIVIDE_BY_TEN);
/*  49 */   public static final ResourceLocation DAMAGE_DEALT_RESISTED = makeCustomStat("damage_dealt_resisted", StatFormatter.DIVIDE_BY_TEN);
/*     */   
/*  51 */   public static final ResourceLocation DAMAGE_TAKEN = makeCustomStat("damage_taken", StatFormatter.DIVIDE_BY_TEN);
/*  52 */   public static final ResourceLocation DAMAGE_BLOCKED_BY_SHIELD = makeCustomStat("damage_blocked_by_shield", StatFormatter.DIVIDE_BY_TEN);
/*  53 */   public static final ResourceLocation DAMAGE_ABSORBED = makeCustomStat("damage_absorbed", StatFormatter.DIVIDE_BY_TEN);
/*  54 */   public static final ResourceLocation DAMAGE_RESISTED = makeCustomStat("damage_resisted", StatFormatter.DIVIDE_BY_TEN);
/*     */   
/*  56 */   public static final ResourceLocation DEATHS = makeCustomStat("deaths", StatFormatter.DEFAULT);
/*  57 */   public static final ResourceLocation MOB_KILLS = makeCustomStat("mob_kills", StatFormatter.DEFAULT);
/*  58 */   public static final ResourceLocation ANIMALS_BRED = makeCustomStat("animals_bred", StatFormatter.DEFAULT);
/*  59 */   public static final ResourceLocation PLAYER_KILLS = makeCustomStat("player_kills", StatFormatter.DEFAULT);
/*  60 */   public static final ResourceLocation FISH_CAUGHT = makeCustomStat("fish_caught", StatFormatter.DEFAULT);
/*     */   
/*  62 */   public static final ResourceLocation TALKED_TO_VILLAGER = makeCustomStat("talked_to_villager", StatFormatter.DEFAULT);
/*  63 */   public static final ResourceLocation TRADED_WITH_VILLAGER = makeCustomStat("traded_with_villager", StatFormatter.DEFAULT);
/*     */   
/*  65 */   public static final ResourceLocation EAT_CAKE_SLICE = makeCustomStat("eat_cake_slice", StatFormatter.DEFAULT);
/*  66 */   public static final ResourceLocation FILL_CAULDRON = makeCustomStat("fill_cauldron", StatFormatter.DEFAULT);
/*  67 */   public static final ResourceLocation USE_CAULDRON = makeCustomStat("use_cauldron", StatFormatter.DEFAULT);
/*  68 */   public static final ResourceLocation CLEAN_ARMOR = makeCustomStat("clean_armor", StatFormatter.DEFAULT);
/*  69 */   public static final ResourceLocation CLEAN_BANNER = makeCustomStat("clean_banner", StatFormatter.DEFAULT);
/*  70 */   public static final ResourceLocation CLEAN_SHULKER_BOX = makeCustomStat("clean_shulker_box", StatFormatter.DEFAULT);
/*  71 */   public static final ResourceLocation INTERACT_WITH_BREWINGSTAND = makeCustomStat("interact_with_brewingstand", StatFormatter.DEFAULT);
/*  72 */   public static final ResourceLocation INTERACT_WITH_BEACON = makeCustomStat("interact_with_beacon", StatFormatter.DEFAULT);
/*  73 */   public static final ResourceLocation INSPECT_DROPPER = makeCustomStat("inspect_dropper", StatFormatter.DEFAULT);
/*  74 */   public static final ResourceLocation INSPECT_HOPPER = makeCustomStat("inspect_hopper", StatFormatter.DEFAULT);
/*  75 */   public static final ResourceLocation INSPECT_DISPENSER = makeCustomStat("inspect_dispenser", StatFormatter.DEFAULT);
/*  76 */   public static final ResourceLocation PLAY_NOTEBLOCK = makeCustomStat("play_noteblock", StatFormatter.DEFAULT);
/*  77 */   public static final ResourceLocation TUNE_NOTEBLOCK = makeCustomStat("tune_noteblock", StatFormatter.DEFAULT);
/*  78 */   public static final ResourceLocation POT_FLOWER = makeCustomStat("pot_flower", StatFormatter.DEFAULT);
/*  79 */   public static final ResourceLocation TRIGGER_TRAPPED_CHEST = makeCustomStat("trigger_trapped_chest", StatFormatter.DEFAULT);
/*  80 */   public static final ResourceLocation OPEN_ENDERCHEST = makeCustomStat("open_enderchest", StatFormatter.DEFAULT);
/*  81 */   public static final ResourceLocation ENCHANT_ITEM = makeCustomStat("enchant_item", StatFormatter.DEFAULT);
/*  82 */   public static final ResourceLocation PLAY_RECORD = makeCustomStat("play_record", StatFormatter.DEFAULT);
/*  83 */   public static final ResourceLocation INTERACT_WITH_FURNACE = makeCustomStat("interact_with_furnace", StatFormatter.DEFAULT);
/*  84 */   public static final ResourceLocation INTERACT_WITH_CRAFTING_TABLE = makeCustomStat("interact_with_crafting_table", StatFormatter.DEFAULT);
/*  85 */   public static final ResourceLocation OPEN_CHEST = makeCustomStat("open_chest", StatFormatter.DEFAULT);
/*  86 */   public static final ResourceLocation SLEEP_IN_BED = makeCustomStat("sleep_in_bed", StatFormatter.DEFAULT);
/*  87 */   public static final ResourceLocation OPEN_SHULKER_BOX = makeCustomStat("open_shulker_box", StatFormatter.DEFAULT);
/*  88 */   public static final ResourceLocation OPEN_BARREL = makeCustomStat("open_barrel", StatFormatter.DEFAULT);
/*  89 */   public static final ResourceLocation INTERACT_WITH_BLAST_FURNACE = makeCustomStat("interact_with_blast_furnace", StatFormatter.DEFAULT);
/*  90 */   public static final ResourceLocation INTERACT_WITH_SMOKER = makeCustomStat("interact_with_smoker", StatFormatter.DEFAULT);
/*  91 */   public static final ResourceLocation INTERACT_WITH_LECTERN = makeCustomStat("interact_with_lectern", StatFormatter.DEFAULT);
/*  92 */   public static final ResourceLocation INTERACT_WITH_CAMPFIRE = makeCustomStat("interact_with_campfire", StatFormatter.DEFAULT);
/*  93 */   public static final ResourceLocation INTERACT_WITH_CARTOGRAPHY_TABLE = makeCustomStat("interact_with_cartography_table", StatFormatter.DEFAULT);
/*  94 */   public static final ResourceLocation INTERACT_WITH_LOOM = makeCustomStat("interact_with_loom", StatFormatter.DEFAULT);
/*  95 */   public static final ResourceLocation INTERACT_WITH_STONECUTTER = makeCustomStat("interact_with_stonecutter", StatFormatter.DEFAULT);
/*  96 */   public static final ResourceLocation BELL_RING = makeCustomStat("bell_ring", StatFormatter.DEFAULT);
/*  97 */   public static final ResourceLocation RAID_TRIGGER = makeCustomStat("raid_trigger", StatFormatter.DEFAULT);
/*  98 */   public static final ResourceLocation RAID_WIN = makeCustomStat("raid_win", StatFormatter.DEFAULT);
/*  99 */   public static final ResourceLocation INTERACT_WITH_ANVIL = makeCustomStat("interact_with_anvil", StatFormatter.DEFAULT);
/* 100 */   public static final ResourceLocation INTERACT_WITH_GRINDSTONE = makeCustomStat("interact_with_grindstone", StatFormatter.DEFAULT);
/* 101 */   public static final ResourceLocation TARGET_HIT = makeCustomStat("target_hit", StatFormatter.DEFAULT);
/* 102 */   public static final ResourceLocation INTERACT_WITH_SMITHING_TABLE = makeCustomStat("interact_with_smithing_table", StatFormatter.DEFAULT);
/*     */   
/*     */   private static ResourceLocation makeCustomStat(String debug0, StatFormatter debug1) {
/* 105 */     ResourceLocation debug2 = new ResourceLocation(debug0);
/* 106 */     Registry.register(Registry.CUSTOM_STAT, debug0, debug2);
/* 107 */     CUSTOM.get(debug2, debug1);
/* 108 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> StatType<T> makeRegistryStatType(String debug0, Registry<T> debug1) {
/* 113 */     return (StatType<T>)Registry.register(Registry.STAT_TYPE, debug0, new StatType<>(debug1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */