/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.DataGenerator;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class ItemTagsProvider
/*     */   extends TagsProvider<Item> {
/*     */   private final Function<Tag.Named<Block>, Tag.Builder> blockTags;
/*     */   
/*     */   public ItemTagsProvider(DataGenerator debug1, BlockTagsProvider debug2) {
/*  20 */     super(debug1, (Registry<Item>)Registry.ITEM);
/*  21 */     this.blockTags = debug2::getOrCreateRawBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTags() {
/*  26 */     copy(BlockTags.WOOL, ItemTags.WOOL);
/*  27 */     copy(BlockTags.PLANKS, ItemTags.PLANKS);
/*  28 */     copy(BlockTags.STONE_BRICKS, ItemTags.STONE_BRICKS);
/*  29 */     copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
/*  30 */     copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
/*  31 */     copy(BlockTags.CARPETS, ItemTags.CARPETS);
/*  32 */     copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
/*  33 */     copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
/*  34 */     copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
/*  35 */     copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
/*  36 */     copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
/*  37 */     copy(BlockTags.DOORS, ItemTags.DOORS);
/*  38 */     copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
/*  39 */     copy(BlockTags.OAK_LOGS, ItemTags.OAK_LOGS);
/*  40 */     copy(BlockTags.DARK_OAK_LOGS, ItemTags.DARK_OAK_LOGS);
/*  41 */     copy(BlockTags.BIRCH_LOGS, ItemTags.BIRCH_LOGS);
/*  42 */     copy(BlockTags.ACACIA_LOGS, ItemTags.ACACIA_LOGS);
/*  43 */     copy(BlockTags.SPRUCE_LOGS, ItemTags.SPRUCE_LOGS);
/*  44 */     copy(BlockTags.JUNGLE_LOGS, ItemTags.JUNGLE_LOGS);
/*  45 */     copy(BlockTags.CRIMSON_STEMS, ItemTags.CRIMSON_STEMS);
/*  46 */     copy(BlockTags.WARPED_STEMS, ItemTags.WARPED_STEMS);
/*  47 */     copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
/*  48 */     copy(BlockTags.LOGS, ItemTags.LOGS);
/*  49 */     copy(BlockTags.SAND, ItemTags.SAND);
/*  50 */     copy(BlockTags.SLABS, ItemTags.SLABS);
/*  51 */     copy(BlockTags.WALLS, ItemTags.WALLS);
/*  52 */     copy(BlockTags.STAIRS, ItemTags.STAIRS);
/*  53 */     copy(BlockTags.ANVIL, ItemTags.ANVIL);
/*  54 */     copy(BlockTags.RAILS, ItemTags.RAILS);
/*  55 */     copy(BlockTags.LEAVES, ItemTags.LEAVES);
/*  56 */     copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
/*  57 */     copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
/*  58 */     copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
/*  59 */     copy(BlockTags.BEDS, ItemTags.BEDS);
/*  60 */     copy(BlockTags.FENCES, ItemTags.FENCES);
/*  61 */     copy(BlockTags.TALL_FLOWERS, ItemTags.TALL_FLOWERS);
/*  62 */     copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
/*  63 */     copy(BlockTags.GOLD_ORES, ItemTags.GOLD_ORES);
/*  64 */     copy(BlockTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.SOUL_FIRE_BASE_BLOCKS);
/*     */     
/*  66 */     tag(ItemTags.BANNERS).add(new Item[] { Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER, Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER, Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER, Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER });
/*  67 */     tag(ItemTags.BOATS).add(new Item[] { Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT, Items.DARK_OAK_BOAT });
/*  68 */     tag(ItemTags.FISHES).add(new Item[] { Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH });
/*  69 */     copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
/*  70 */     tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(new Item[] { Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT });
/*  71 */     tag(ItemTags.MUSIC_DISCS).addTag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(Items.MUSIC_DISC_PIGSTEP);
/*  72 */     tag(ItemTags.COALS).add(new Item[] { Items.COAL, Items.CHARCOAL });
/*  73 */     tag(ItemTags.ARROWS).add(new Item[] { Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW });
/*  74 */     tag(ItemTags.LECTERN_BOOKS).add(new Item[] { Items.WRITTEN_BOOK, Items.WRITABLE_BOOK });
/*  75 */     tag(ItemTags.BEACON_PAYMENT_ITEMS).add(new Item[] { Items.NETHERITE_INGOT, Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT });
/*  76 */     tag(ItemTags.PIGLIN_REPELLENTS).add(Items.SOUL_TORCH).add(Items.SOUL_LANTERN).add(Items.SOUL_CAMPFIRE);
/*  77 */     tag(ItemTags.PIGLIN_LOVED).addTag(ItemTags.GOLD_ORES).add(new Item[] { Items.GOLD_BLOCK, Items.GILDED_BLACKSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, Items.GOLD_INGOT, Items.BELL, Items.CLOCK, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     tag(ItemTags.NON_FLAMMABLE_WOOD).add(new Item[] { Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.WARPED_PLANKS, Items.CRIMSON_SLAB, Items.WARPED_SLAB, Items.CRIMSON_PRESSURE_PLATE, Items.WARPED_PRESSURE_PLATE, Items.CRIMSON_FENCE, Items.WARPED_FENCE, Items.CRIMSON_TRAPDOOR, Items.WARPED_TRAPDOOR, Items.CRIMSON_FENCE_GATE, Items.WARPED_FENCE_GATE, Items.CRIMSON_STAIRS, Items.WARPED_STAIRS, Items.CRIMSON_BUTTON, Items.WARPED_BUTTON, Items.CRIMSON_DOOR, Items.WARPED_DOOR, Items.CRIMSON_SIGN, Items.WARPED_SIGN });
/*  84 */     tag(ItemTags.STONE_TOOL_MATERIALS).add(new Item[] { Items.COBBLESTONE, Items.BLACKSTONE });
/*  85 */     tag(ItemTags.STONE_CRAFTING_MATERIALS).add(new Item[] { Items.COBBLESTONE, Items.BLACKSTONE });
/*     */   }
/*     */   
/*     */   protected void copy(Tag.Named<Block> debug1, Tag.Named<Item> debug2) {
/*  89 */     Tag.Builder debug3 = getOrCreateRawBuilder(debug2);
/*  90 */     Tag.Builder debug4 = this.blockTags.apply(debug1);
/*  91 */     debug4.getEntries().forEach(debug3::add);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Path getPath(ResourceLocation debug1) {
/*  96 */     return this.generator.getOutputFolder().resolve("data/" + debug1.getNamespace() + "/tags/items/" + debug1.getPath() + ".json");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 101 */     return "Item Tags";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\tags\ItemTagsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */