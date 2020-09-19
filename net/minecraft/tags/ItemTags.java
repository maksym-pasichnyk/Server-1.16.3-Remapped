/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class ItemTags
/*    */ {
/*  9 */   protected static final StaticTagHelper<Item> HELPER = StaticTags.create(new ResourceLocation("item"), TagContainer::getItems);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final Tag.Named<Item> WOOL = bind("wool");
/* 15 */   public static final Tag.Named<Item> PLANKS = bind("planks");
/* 16 */   public static final Tag.Named<Item> STONE_BRICKS = bind("stone_bricks");
/* 17 */   public static final Tag.Named<Item> WOODEN_BUTTONS = bind("wooden_buttons");
/* 18 */   public static final Tag.Named<Item> BUTTONS = bind("buttons");
/* 19 */   public static final Tag.Named<Item> CARPETS = bind("carpets");
/* 20 */   public static final Tag.Named<Item> WOODEN_DOORS = bind("wooden_doors");
/* 21 */   public static final Tag.Named<Item> WOODEN_STAIRS = bind("wooden_stairs");
/* 22 */   public static final Tag.Named<Item> WOODEN_SLABS = bind("wooden_slabs");
/* 23 */   public static final Tag.Named<Item> WOODEN_FENCES = bind("wooden_fences");
/* 24 */   public static final Tag.Named<Item> WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
/* 25 */   public static final Tag.Named<Item> WOODEN_TRAPDOORS = bind("wooden_trapdoors");
/* 26 */   public static final Tag.Named<Item> DOORS = bind("doors");
/* 27 */   public static final Tag.Named<Item> SAPLINGS = bind("saplings");
/* 28 */   public static final Tag.Named<Item> LOGS_THAT_BURN = bind("logs_that_burn");
/* 29 */   public static final Tag.Named<Item> LOGS = bind("logs");
/* 30 */   public static final Tag.Named<Item> DARK_OAK_LOGS = bind("dark_oak_logs");
/* 31 */   public static final Tag.Named<Item> OAK_LOGS = bind("oak_logs");
/* 32 */   public static final Tag.Named<Item> BIRCH_LOGS = bind("birch_logs");
/* 33 */   public static final Tag.Named<Item> ACACIA_LOGS = bind("acacia_logs");
/* 34 */   public static final Tag.Named<Item> JUNGLE_LOGS = bind("jungle_logs");
/* 35 */   public static final Tag.Named<Item> SPRUCE_LOGS = bind("spruce_logs");
/* 36 */   public static final Tag.Named<Item> CRIMSON_STEMS = bind("crimson_stems");
/* 37 */   public static final Tag.Named<Item> WARPED_STEMS = bind("warped_stems");
/* 38 */   public static final Tag.Named<Item> BANNERS = bind("banners");
/* 39 */   public static final Tag.Named<Item> SAND = bind("sand");
/* 40 */   public static final Tag.Named<Item> STAIRS = bind("stairs");
/* 41 */   public static final Tag.Named<Item> SLABS = bind("slabs");
/* 42 */   public static final Tag.Named<Item> WALLS = bind("walls");
/* 43 */   public static final Tag.Named<Item> ANVIL = bind("anvil");
/* 44 */   public static final Tag.Named<Item> RAILS = bind("rails");
/* 45 */   public static final Tag.Named<Item> LEAVES = bind("leaves");
/* 46 */   public static final Tag.Named<Item> TRAPDOORS = bind("trapdoors");
/* 47 */   public static final Tag.Named<Item> SMALL_FLOWERS = bind("small_flowers");
/* 48 */   public static final Tag.Named<Item> BEDS = bind("beds");
/* 49 */   public static final Tag.Named<Item> FENCES = bind("fences");
/* 50 */   public static final Tag.Named<Item> TALL_FLOWERS = bind("tall_flowers");
/* 51 */   public static final Tag.Named<Item> FLOWERS = bind("flowers");
/* 52 */   public static final Tag.Named<Item> PIGLIN_REPELLENTS = bind("piglin_repellents");
/* 53 */   public static final Tag.Named<Item> PIGLIN_LOVED = bind("piglin_loved");
/* 54 */   public static final Tag.Named<Item> GOLD_ORES = bind("gold_ores");
/* 55 */   public static final Tag.Named<Item> NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
/* 56 */   public static final Tag.Named<Item> SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static final Tag.Named<Item> BOATS = bind("boats");
/* 62 */   public static final Tag.Named<Item> FISHES = bind("fishes");
/* 63 */   public static final Tag.Named<Item> SIGNS = bind("signs");
/* 64 */   public static final Tag.Named<Item> MUSIC_DISCS = bind("music_discs");
/* 65 */   public static final Tag.Named<Item> CREEPER_DROP_MUSIC_DISCS = bind("creeper_drop_music_discs");
/* 66 */   public static final Tag.Named<Item> COALS = bind("coals");
/* 67 */   public static final Tag.Named<Item> ARROWS = bind("arrows");
/* 68 */   public static final Tag.Named<Item> LECTERN_BOOKS = bind("lectern_books");
/* 69 */   public static final Tag.Named<Item> BEACON_PAYMENT_ITEMS = bind("beacon_payment_items");
/* 70 */   public static final Tag.Named<Item> STONE_TOOL_MATERIALS = bind("stone_tool_materials");
/* 71 */   public static final Tag.Named<Item> STONE_CRAFTING_MATERIALS = bind("stone_crafting_materials");
/*    */   
/*    */   private static Tag.Named<Item> bind(String debug0) {
/* 74 */     return HELPER.bind(debug0);
/*    */   }
/*    */   
/*    */   public static TagCollection<Item> getAllTags() {
/* 78 */     return HELPER.getAllTags();
/*    */   }
/*    */   
/*    */   public static List<? extends Tag.Named<Item>> getWrappers() {
/* 82 */     return HELPER.getWrappers();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\ItemTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */