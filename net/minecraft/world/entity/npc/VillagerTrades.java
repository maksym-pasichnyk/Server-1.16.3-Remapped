/*     */ package net.minecraft.world.entity.npc;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.DyeableLeatherItem;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.item.SuspiciousStewItem;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
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
/*     */ public class VillagerTrades
/*     */ {
/*     */   public static final Map<VillagerProfession, Int2ObjectMap<ItemListing[]>> TRADES;
/*     */   
/*     */   static {
/*  66 */     TRADES = (Map<VillagerProfession, Int2ObjectMap<ItemListing[]>>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(VillagerProfession.FARMER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.WHEAT, 20, 16, 2), new EmeraldForItems((ItemLike)Items.POTATO, 26, 16, 2), new EmeraldForItems((ItemLike)Items.CARROT, 22, 16, 2), new EmeraldForItems((ItemLike)Items.BEETROOT, 15, 16, 2), new ItemsForEmeralds(Items.BREAD, 1, 6, 16, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.PUMPKIN, 6, 12, 10), new ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5) }Integer.valueOf(3), new ItemListing[] { new ItemsForEmeralds(Items.COOKIE, 3, 18, 10), new EmeraldForItems((ItemLike)Blocks.MELON, 4, 12, 20) }Integer.valueOf(4), new ItemListing[] { new ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15), new SuspisciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15), new SuspisciousStewForEmerald(MobEffects.JUMP, 160, 15), new SuspisciousStewForEmerald(MobEffects.WEAKNESS, 140, 15), new SuspisciousStewForEmerald(MobEffects.BLINDNESS, 120, 15), new SuspisciousStewForEmerald(MobEffects.POISON, 280, 15), new SuspisciousStewForEmerald(MobEffects.SATURATION, 7, 15) }Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30) })));
/*     */           debug0.put(VillagerProfession.FISHERMAN, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.STRING, 20, 16, 2), new EmeraldForItems((ItemLike)Items.COAL, 10, 16, 2), new ItemsAndEmeraldsToItems((ItemLike)Items.COD, 6, Items.COOKED_COD, 6, 16, 1), new ItemsForEmeralds(Items.COD_BUCKET, 3, 1, 16, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COD, 15, 16, 10), new ItemsAndEmeraldsToItems((ItemLike)Items.SALMON, 6, Items.COOKED_SALMON, 6, 16, 5), new ItemsForEmeralds(Items.CAMPFIRE, 2, 1, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.SALMON, 13, 16, 20), new EnchantedItemForEmeralds(Items.FISHING_ROD, 3, 3, 10, 0.2F) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.TROPICAL_FISH, 6, 12, 30) }Integer.valueOf(5), new ItemListing[] { new EmeraldForItems((ItemLike)Items.PUFFERFISH, 4, 12, 30), new EmeraldsForVillagerTypeItem(1, 12, 30, (Map<VillagerType, Item>)ImmutableMap.builder().put(VillagerType.PLAINS, Items.OAK_BOAT).put(VillagerType.TAIGA, Items.SPRUCE_BOAT).put(VillagerType.SNOW, Items.SPRUCE_BOAT).put(VillagerType.DESERT, Items.JUNGLE_BOAT).put(VillagerType.JUNGLE, Items.JUNGLE_BOAT).put(VillagerType.SAVANNA, Items.ACACIA_BOAT).put(VillagerType.SWAMP, Items.DARK_OAK_BOAT).build()) })));
/*     */           debug0.put(VillagerProfession.SHEPHERD, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.WHITE_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.BROWN_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.BLACK_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.GRAY_WOOL, 18, 16, 2), new ItemsForEmeralds(Items.SHEARS, 2, 1, 1) }Integer.valueOf(2), new ItemListing[] { 
/*     */                     new EmeraldForItems((ItemLike)Items.WHITE_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.GRAY_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.BLACK_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.LIGHT_BLUE_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.LIME_DYE, 12, 16, 10), new ItemsForEmeralds(Blocks.WHITE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_WOOL, 1, 1, 16, 5), 
/*     */                     new ItemsForEmeralds(Blocks.LIME_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PINK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.CYAN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BROWN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GREEN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.RED_WOOL, 1, 1, 16, 5), 
/*     */                     new ItemsForEmeralds(Blocks.BLACK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.WHITE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIME_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PINK_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5), 
/*     */                     new ItemsForEmeralds(Blocks.CYAN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BROWN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GREEN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.RED_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLACK_CARPET, 1, 4, 16, 5) }Integer.valueOf(3), new ItemListing[] { 
/*     */                     new EmeraldForItems((ItemLike)Items.YELLOW_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.LIGHT_GRAY_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.ORANGE_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.RED_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.PINK_DYE, 12, 16, 20), new ItemsForEmeralds(Blocks.WHITE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.YELLOW_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.RED_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLACK_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLUE_BED, 3, 1, 12, 10), 
/*     */                     new ItemsForEmeralds(Blocks.BROWN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.CYAN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GREEN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIME_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.MAGENTA_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.ORANGE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.PINK_BED, 3, 1, 12, 10), 
/*     */                     new ItemsForEmeralds(Blocks.PURPLE_BED, 3, 1, 12, 10) }Integer.valueOf(4), new ItemListing[] { 
/*     */                     new EmeraldForItems((ItemLike)Items.BROWN_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.PURPLE_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.BLUE_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.GREEN_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.MAGENTA_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.CYAN_DYE, 12, 16, 30), new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 12, 15), 
/*     */                     new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 12, 15), 
/*     */                     new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15) }Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(Items.PAINTING, 2, 3, 30) })));
/*     */           debug0.put(VillagerProfession.FLETCHER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.STICK, 32, 16, 2), new ItemsForEmeralds(Items.ARROW, 1, 16, 1), new ItemsAndEmeraldsToItems((ItemLike)Blocks.GRAVEL, 10, Items.FLINT, 10, 12, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 26, 12, 10), new ItemsForEmeralds(Items.BOW, 2, 1, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.STRING, 14, 16, 20), new ItemsForEmeralds(Items.CROSSBOW, 3, 1, 10) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.FEATHER, 24, 16, 30), new EnchantedItemForEmeralds(Items.BOW, 2, 3, 15) }Integer.valueOf(5), new ItemListing[] { new EmeraldForItems((ItemLike)Items.TRIPWIRE_HOOK, 8, 12, 30), new EnchantedItemForEmeralds(Items.CROSSBOW, 3, 3, 15), new TippedArrowForItemsAndEmeralds(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 30) })));
/*     */           debug0.put(VillagerProfession.LIBRARIAN, toIntMap(ImmutableMap.builder().put(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.PAPER, 24, 16, 2), new EnchantBookForEmeralds(1), new ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1) }).put(Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.BOOK, 4, 12, 10), new EnchantBookForEmeralds(5), new ItemsForEmeralds(Items.LANTERN, 1, 1, 5) }).put(Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.INK_SAC, 5, 12, 20), new EnchantBookForEmeralds(10), new ItemsForEmeralds(Items.GLASS, 1, 4, 10) }).put(Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.WRITABLE_BOOK, 2, 12, 30), new EnchantBookForEmeralds(15), new ItemsForEmeralds(Items.CLOCK, 5, 1, 15), new ItemsForEmeralds(Items.COMPASS, 4, 1, 15) }).put(Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(Items.NAME_TAG, 20, 1, 30) }).build()));
/*     */           debug0.put(VillagerProfession.CARTOGRAPHER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.PAPER, 24, 16, 2), new ItemsForEmeralds(Items.MAP, 7, 1, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.GLASS_PANE, 11, 16, 10), new TreasureMapForEmeralds(13, StructureFeature.OCEAN_MONUMENT, MapDecoration.Type.MONUMENT, 12, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COMPASS, 1, 12, 20), new TreasureMapForEmeralds(14, StructureFeature.WOODLAND_MANSION, MapDecoration.Type.MANSION, 12, 10) }Integer.valueOf(4), new ItemListing[] { 
/*     */                     new ItemsForEmeralds(Items.ITEM_FRAME, 7, 1, 15), new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 15), 
/*     */                     new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 15) }Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(Items.GLOBE_BANNER_PATTER, 8, 1, 30) })));
/*     */           debug0.put(VillagerProfession.CLERIC, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.ROTTEN_FLESH, 32, 16, 2), new ItemsForEmeralds(Items.REDSTONE, 1, 2, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.GOLD_INGOT, 3, 12, 10), new ItemsForEmeralds(Items.LAPIS_LAZULI, 1, 1, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.RABBIT_FOOT, 2, 12, 20), new ItemsForEmeralds(Blocks.GLOWSTONE, 4, 1, 12, 10) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.SCUTE, 4, 12, 30), new EmeraldForItems((ItemLike)Items.GLASS_BOTTLE, 9, 12, 30), new ItemsForEmeralds(Items.ENDER_PEARL, 5, 1, 15) }Integer.valueOf(5), new ItemListing[] { new EmeraldForItems((ItemLike)Items.NETHER_WART, 22, 12, 30), new ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 3, 1, 30) })));
/*     */           debug0.put(VillagerProfession.ARMORER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_HELMET), 5, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.LAVA_BUCKET, 1, 12, 20), new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 20), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.SHIELD), 5, 1, 12, 10, 0.2F) }Integer.valueOf(4), new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F) }Integer.valueOf(5), new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F) })));
/*     */           debug0.put(VillagerProfession.WEAPONSMITH, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_AXE), 3, 1, 12, 1, 0.2F), new EnchantedItemForEmeralds(Items.IRON_SWORD, 2, 3, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 24, 12, 20) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F) }Integer.valueOf(5), new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F) })));
/*     */           debug0.put(VillagerProfession.TOOLSMITH, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_AXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_HOE), 1, 1, 12, 1, 0.2F) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 30, 12, 20), new EnchantedItemForEmeralds(Items.IRON_AXE, 1, 3, 10, 0.2F), new EnchantedItemForEmeralds(Items.IRON_SHOVEL, 2, 3, 10, 0.2F), new EnchantedItemForEmeralds(Items.IRON_PICKAXE, 3, 3, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F) }Integer.valueOf(5), new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F) })));
/*     */           debug0.put(VillagerProfession.BUTCHER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.CHICKEN, 14, 16, 2), new EmeraldForItems((ItemLike)Items.PORKCHOP, 7, 16, 2), new EmeraldForItems((ItemLike)Items.RABBIT, 4, 16, 2), new ItemsForEmeralds(Items.RABBIT_STEW, 1, 1, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(Items.COOKED_PORKCHOP, 1, 5, 16, 5), new ItemsForEmeralds(Items.COOKED_CHICKEN, 1, 8, 16, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.MUTTON, 7, 16, 20), new EmeraldForItems((ItemLike)Items.BEEF, 10, 16, 20) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.DRIED_KELP_BLOCK, 10, 12, 30) }Integer.valueOf(5), new ItemListing[] { new EmeraldForItems((ItemLike)Items.SWEET_BERRIES, 10, 12, 30) })));
/*     */           debug0.put(VillagerProfession.LEATHERWORKER, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.LEATHER, 6, 16, 2), new DyedArmorForEmeralds(Items.LEATHER_LEGGINGS, 3), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 26, 12, 10), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 5), new DyedArmorForEmeralds(Items.LEATHER_BOOTS, 4, 12, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Items.RABBIT_HIDE, 9, 12, 20), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }Integer.valueOf(4), new ItemListing[] { new EmeraldForItems((ItemLike)Items.SCUTE, 4, 12, 30), new DyedArmorForEmeralds(Items.LEATHER_HORSE_ARMOR, 6, 12, 15) }Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(new ItemStack((ItemLike)Items.SADDLE), 6, 1, 12, 30, 0.2F), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 30) })));
/*     */           debug0.put(VillagerProfession.MASON, toIntMap(ImmutableMap.of(Integer.valueOf(1), new ItemListing[] { new EmeraldForItems((ItemLike)Items.CLAY_BALL, 10, 16, 2), new ItemsForEmeralds(Items.BRICK, 1, 10, 16, 1) }Integer.valueOf(2), new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.STONE, 20, 16, 10), new ItemsForEmeralds(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5) }Integer.valueOf(3), new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.GRANITE, 16, 16, 20), new EmeraldForItems((ItemLike)Blocks.ANDESITE, 16, 16, 20), new EmeraldForItems((ItemLike)Blocks.DIORITE, 16, 16, 20), new ItemsForEmeralds(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_GRANITE, 1, 4, 16, 10) }Integer.valueOf(4), new ItemListing[] { 
/*     */                     new EmeraldForItems((ItemLike)Items.QUARTZ, 12, 12, 30), new ItemsForEmeralds(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_TERRACOTTA, 1, 1, 12, 15), 
/*     */                     new ItemsForEmeralds(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), 
/*     */                     new ItemsForEmeralds(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 15), 
/*     */                     new ItemsForEmeralds(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15) }Integer.valueOf(5), new ItemListing[] { new ItemsForEmeralds(Blocks.QUARTZ_PILLAR, 1, 1, 12, 30), new ItemsForEmeralds(Blocks.QUARTZ_BLOCK, 1, 1, 12, 30) })));
/*     */         });
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
/* 526 */   public static final Int2ObjectMap<ItemListing[]> WANDERING_TRADER_TRADES = toIntMap(ImmutableMap.of(
/* 527 */         Integer.valueOf(1), new ItemListing[] { new ItemsForEmeralds(Items.SEA_PICKLE, 2, 1, 5, 1), new ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1), new ItemsForEmeralds(Items.GLOWSTONE, 2, 1, 5, 1), new ItemsForEmeralds(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new ItemsForEmeralds(Items.FERN, 1, 1, 12, 1), new ItemsForEmeralds(Items.SUGAR_CANE, 1, 1, 8, 1), new ItemsForEmeralds(Items.PUMPKIN, 1, 1, 4, 1), new ItemsForEmeralds(Items.KELP, 3, 1, 12, 1), new ItemsForEmeralds(Items.CACTUS, 3, 1, 8, 1), new ItemsForEmeralds(Items.DANDELION, 1, 1, 12, 1), new ItemsForEmeralds(Items.POPPY, 1, 1, 12, 1), new ItemsForEmeralds(Items.BLUE_ORCHID, 1, 1, 8, 1), new ItemsForEmeralds(Items.ALLIUM, 1, 1, 12, 1), new ItemsForEmeralds(Items.AZURE_BLUET, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.PINK_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.OXEYE_DAISY, 1, 1, 12, 1), new ItemsForEmeralds(Items.CORNFLOWER, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1), new ItemsForEmeralds(Items.WHEAT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.MELON_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.ACACIA_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.BIRCH_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.DARK_OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.JUNGLE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.SPRUCE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.RED_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.WHITE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PINK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLACK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GREEN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.MAGENTA_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.YELLOW_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PURPLE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIME_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.ORANGE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BROWN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.CYAN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.VINE, 1, 1, 12, 1), new ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_PAD, 1, 2, 5, 1), new ItemsForEmeralds(Items.SAND, 1, 8, 8, 1), new ItemsForEmeralds(Items.RED_SAND, 1, 4, 6, 1)
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
/*     */ 
/*     */         
/* 587 */         }Integer.valueOf(2), new ItemListing[] { new ItemsForEmeralds(Items.TROPICAL_FISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PUFFERFISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PACKED_ICE, 3, 1, 6, 1), new ItemsForEmeralds(Items.BLUE_ICE, 6, 1, 6, 1), new ItemsForEmeralds(Items.GUNPOWDER, 1, 1, 8, 1), new ItemsForEmeralds(Items.PODZOL, 3, 3, 6, 1) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Int2ObjectMap<ItemListing[]> toIntMap(ImmutableMap<Integer, ItemListing[]> debug0) {
/* 598 */     return (Int2ObjectMap<ItemListing[]>)new Int2ObjectOpenHashMap((Map)debug0);
/*     */   }
/*     */   
/*     */   public static interface ItemListing {
/*     */     @Nullable
/*     */     MerchantOffer getOffer(Entity param1Entity, Random param1Random);
/*     */   }
/*     */   
/*     */   static class EmeraldForItems implements ItemListing {
/*     */     private final Item item;
/*     */     private final int cost;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public EmeraldForItems(ItemLike debug1, int debug2, int debug3, int debug4) {
/* 614 */       this.item = debug1.asItem();
/* 615 */       this.cost = debug2;
/* 616 */       this.maxUses = debug3;
/* 617 */       this.villagerXp = debug4;
/* 618 */       this.priceMultiplier = 0.05F;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 623 */       ItemStack debug3 = new ItemStack((ItemLike)this.item, this.cost);
/* 624 */       return new MerchantOffer(debug3, new ItemStack((ItemLike)Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class EmeraldsForVillagerTypeItem implements ItemListing {
/*     */     private final Map<VillagerType, Item> trades;
/*     */     private final int cost;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     
/*     */     public EmeraldsForVillagerTypeItem(int debug1, int debug2, int debug3, Map<VillagerType, Item> debug4) {
/* 635 */       Registry.VILLAGER_TYPE.stream().filter(debug1 -> !debug0.containsKey(debug1)).findAny().ifPresent(debug0 -> {
/*     */             throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(debug0));
/*     */           });
/* 638 */       this.trades = debug4;
/*     */       
/* 640 */       this.cost = debug1;
/* 641 */       this.maxUses = debug2;
/* 642 */       this.villagerXp = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 648 */       if (debug1 instanceof VillagerDataHolder) {
/* 649 */         ItemStack debug3 = new ItemStack((ItemLike)this.trades.get(((VillagerDataHolder)debug1).getVillagerData().getType()), this.cost);
/* 650 */         return new MerchantOffer(debug3, new ItemStack((ItemLike)Items.EMERALD), this.maxUses, this.villagerXp, 0.05F);
/*     */       } 
/* 652 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ItemsForEmeralds implements ItemListing {
/*     */     private final ItemStack itemStack;
/*     */     private final int emeraldCost;
/*     */     private final int numberOfItems;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public ItemsForEmeralds(Block debug1, int debug2, int debug3, int debug4, int debug5) {
/* 665 */       this(new ItemStack((ItemLike)debug1), debug2, debug3, debug4, debug5);
/*     */     }
/*     */     
/*     */     public ItemsForEmeralds(Item debug1, int debug2, int debug3, int debug4) {
/* 669 */       this(new ItemStack((ItemLike)debug1), debug2, debug3, 12, debug4);
/*     */     }
/*     */     
/*     */     public ItemsForEmeralds(Item debug1, int debug2, int debug3, int debug4, int debug5) {
/* 673 */       this(new ItemStack((ItemLike)debug1), debug2, debug3, debug4, debug5);
/*     */     }
/*     */     
/*     */     public ItemsForEmeralds(ItemStack debug1, int debug2, int debug3, int debug4, int debug5) {
/* 677 */       this(debug1, debug2, debug3, debug4, debug5, 0.05F);
/*     */     }
/*     */     
/*     */     public ItemsForEmeralds(ItemStack debug1, int debug2, int debug3, int debug4, int debug5, float debug6) {
/* 681 */       this.itemStack = debug1;
/* 682 */       this.emeraldCost = debug2;
/* 683 */       this.numberOfItems = debug3;
/* 684 */       this.maxUses = debug4;
/* 685 */       this.villagerXp = debug5;
/* 686 */       this.priceMultiplier = debug6;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 691 */       return new MerchantOffer(new ItemStack((ItemLike)Items.EMERALD, this.emeraldCost), new ItemStack((ItemLike)this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class SuspisciousStewForEmerald implements ItemListing {
/*     */     final MobEffect effect;
/*     */     final int duration;
/*     */     final int xp;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public SuspisciousStewForEmerald(MobEffect debug1, int debug2, int debug3) {
/* 702 */       this.effect = debug1;
/* 703 */       this.duration = debug2;
/* 704 */       this.xp = debug3;
/* 705 */       this.priceMultiplier = 0.05F;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 711 */       ItemStack debug3 = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW, 1);
/* 712 */       SuspiciousStewItem.saveMobEffect(debug3, this.effect, this.duration);
/* 713 */       return new MerchantOffer(new ItemStack((ItemLike)Items.EMERALD, 1), debug3, 12, this.xp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class EnchantedItemForEmeralds implements ItemListing {
/*     */     private final ItemStack itemStack;
/*     */     private final int baseEmeraldCost;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public EnchantedItemForEmeralds(Item debug1, int debug2, int debug3, int debug4) {
/* 725 */       this(debug1, debug2, debug3, debug4, 0.05F);
/*     */     }
/*     */     
/*     */     public EnchantedItemForEmeralds(Item debug1, int debug2, int debug3, int debug4, float debug5) {
/* 729 */       this.itemStack = new ItemStack((ItemLike)debug1);
/* 730 */       this.baseEmeraldCost = debug2;
/* 731 */       this.maxUses = debug3;
/* 732 */       this.villagerXp = debug4;
/* 733 */       this.priceMultiplier = debug5;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 738 */       int debug3 = 5 + debug2.nextInt(15);
/* 739 */       ItemStack debug4 = EnchantmentHelper.enchantItem(debug2, new ItemStack((ItemLike)this.itemStack.getItem()), debug3, false);
/* 740 */       int debug5 = Math.min(this.baseEmeraldCost + debug3, 64);
/* 741 */       ItemStack debug6 = new ItemStack((ItemLike)Items.EMERALD, debug5);
/*     */       
/* 743 */       return new MerchantOffer(debug6, debug4, this.maxUses, this.villagerXp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class TippedArrowForItemsAndEmeralds implements ItemListing {
/*     */     private final ItemStack toItem;
/*     */     private final int toCount;
/*     */     private final int emeraldCost;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     private final Item fromItem;
/*     */     private final int fromCount;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public TippedArrowForItemsAndEmeralds(Item debug1, int debug2, Item debug3, int debug4, int debug5, int debug6, int debug7) {
/* 758 */       this.toItem = new ItemStack((ItemLike)debug3);
/* 759 */       this.emeraldCost = debug5;
/* 760 */       this.maxUses = debug6;
/* 761 */       this.villagerXp = debug7;
/* 762 */       this.fromItem = debug1;
/* 763 */       this.fromCount = debug2;
/* 764 */       this.toCount = debug4;
/* 765 */       this.priceMultiplier = 0.05F;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 770 */       ItemStack debug3 = new ItemStack((ItemLike)Items.EMERALD, this.emeraldCost);
/* 771 */       List<Potion> debug4 = (List<Potion>)Registry.POTION.stream().filter(debug0 -> (!debug0.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(debug0))).collect(Collectors.toList());
/* 772 */       Potion debug5 = debug4.get(debug2.nextInt(debug4.size()));
/* 773 */       ItemStack debug6 = PotionUtils.setPotion(new ItemStack((ItemLike)this.toItem.getItem(), this.toCount), debug5);
/*     */       
/* 775 */       return new MerchantOffer(debug3, new ItemStack((ItemLike)this.fromItem, this.fromCount), debug6, this.maxUses, this.villagerXp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class DyedArmorForEmeralds implements ItemListing {
/*     */     private final Item item;
/*     */     private final int value;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     
/*     */     public DyedArmorForEmeralds(Item debug1, int debug2) {
/* 786 */       this(debug1, debug2, 12, 1);
/*     */     }
/*     */     
/*     */     public DyedArmorForEmeralds(Item debug1, int debug2, int debug3, int debug4) {
/* 790 */       this.item = debug1;
/* 791 */       this.value = debug2;
/* 792 */       this.maxUses = debug3;
/* 793 */       this.villagerXp = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 798 */       ItemStack debug3 = new ItemStack((ItemLike)Items.EMERALD, this.value);
/* 799 */       ItemStack debug4 = new ItemStack((ItemLike)this.item);
/*     */       
/* 801 */       if (this.item instanceof net.minecraft.world.item.DyeableArmorItem) {
/* 802 */         List<DyeItem> debug5 = Lists.newArrayList();
/* 803 */         debug5.add(getRandomDye(debug2));
/*     */         
/* 805 */         if (debug2.nextFloat() > 0.7F) {
/* 806 */           debug5.add(getRandomDye(debug2));
/*     */         }
/*     */         
/* 809 */         if (debug2.nextFloat() > 0.8F) {
/* 810 */           debug5.add(getRandomDye(debug2));
/*     */         }
/*     */         
/* 813 */         debug4 = DyeableLeatherItem.dyeArmor(debug4, debug5);
/*     */       } 
/*     */       
/* 816 */       return new MerchantOffer(debug3, debug4, this.maxUses, this.villagerXp, 0.2F);
/*     */     }
/*     */     
/*     */     private static DyeItem getRandomDye(Random debug0) {
/* 820 */       return DyeItem.byColor(DyeColor.byId(debug0.nextInt(16)));
/*     */     }
/*     */   }
/*     */   
/*     */   static class EnchantBookForEmeralds implements ItemListing {
/*     */     private final int villagerXp;
/*     */     
/*     */     public EnchantBookForEmeralds(int debug1) {
/* 828 */       this.villagerXp = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 833 */       List<Enchantment> debug3 = (List<Enchantment>)Registry.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
/* 834 */       Enchantment debug4 = debug3.get(debug2.nextInt(debug3.size()));
/* 835 */       int debug5 = Mth.nextInt(debug2, debug4.getMinLevel(), debug4.getMaxLevel());
/* 836 */       ItemStack debug6 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(debug4, debug5));
/* 837 */       int debug7 = 2 + debug2.nextInt(5 + debug5 * 10) + 3 * debug5;
/* 838 */       if (debug4.isTreasureOnly()) {
/* 839 */         debug7 *= 2;
/*     */       }
/* 841 */       if (debug7 > 64) {
/* 842 */         debug7 = 64;
/*     */       }
/*     */       
/* 845 */       return new MerchantOffer(new ItemStack((ItemLike)Items.EMERALD, debug7), new ItemStack((ItemLike)Items.BOOK), debug6, 12, this.villagerXp, 0.2F);
/*     */     }
/*     */   }
/*     */   
/*     */   static class TreasureMapForEmeralds implements ItemListing {
/*     */     private final int emeraldCost;
/*     */     private final StructureFeature<?> destination;
/*     */     private final MapDecoration.Type destinationType;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     
/*     */     public TreasureMapForEmeralds(int debug1, StructureFeature<?> debug2, MapDecoration.Type debug3, int debug4, int debug5) {
/* 857 */       this.emeraldCost = debug1;
/* 858 */       this.destination = debug2;
/* 859 */       this.destinationType = debug3;
/* 860 */       this.maxUses = debug4;
/* 861 */       this.villagerXp = debug5;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 868 */       if (!(debug1.level instanceof ServerLevel)) {
/* 869 */         return null;
/*     */       }
/*     */       
/* 872 */       ServerLevel debug3 = (ServerLevel)debug1.level;
/* 873 */       BlockPos debug4 = debug3.findNearestMapFeature(this.destination, debug1.blockPosition(), 100, true);
/* 874 */       if (debug4 != null) {
/* 875 */         ItemStack debug5 = MapItem.create((Level)debug3, debug4.getX(), debug4.getZ(), (byte)2, true, true);
/* 876 */         MapItem.renderBiomePreviewMap(debug3, debug5);
/* 877 */         MapItemSavedData.addTargetDecoration(debug5, debug4, "+", this.destinationType);
/* 878 */         debug5.setHoverName((Component)new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
/* 879 */         return new MerchantOffer(new ItemStack((ItemLike)Items.EMERALD, this.emeraldCost), new ItemStack((ItemLike)Items.COMPASS), debug5, this.maxUses, this.villagerXp, 0.2F);
/*     */       } 
/* 881 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ItemsAndEmeraldsToItems implements ItemListing {
/*     */     private final ItemStack fromItem;
/*     */     private final int fromCount;
/*     */     private final int emeraldCost;
/*     */     private final ItemStack toItem;
/*     */     private final int toCount;
/*     */     private final int maxUses;
/*     */     private final int villagerXp;
/*     */     private final float priceMultiplier;
/*     */     
/*     */     public ItemsAndEmeraldsToItems(ItemLike debug1, int debug2, Item debug3, int debug4, int debug5, int debug6) {
/* 896 */       this(debug1, debug2, 1, debug3, debug4, debug5, debug6);
/*     */     }
/*     */     
/*     */     public ItemsAndEmeraldsToItems(ItemLike debug1, int debug2, int debug3, Item debug4, int debug5, int debug6, int debug7) {
/* 900 */       this.fromItem = new ItemStack(debug1);
/* 901 */       this.fromCount = debug2;
/* 902 */       this.emeraldCost = debug3;
/* 903 */       this.toItem = new ItemStack((ItemLike)debug4);
/* 904 */       this.toCount = debug5;
/* 905 */       this.maxUses = debug6;
/* 906 */       this.villagerXp = debug7;
/* 907 */       this.priceMultiplier = 0.05F;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MerchantOffer getOffer(Entity debug1, Random debug2) {
/* 913 */       return new MerchantOffer(new ItemStack((ItemLike)Items.EMERALD, this.emeraldCost), new ItemStack((ItemLike)this.fromItem.getItem(), this.fromCount), new ItemStack((ItemLike)this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\VillagerTrades.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */