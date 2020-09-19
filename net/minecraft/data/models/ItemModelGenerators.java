/*     */ package net.minecraft.data.models;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.data.models.model.ModelLocationUtils;
/*     */ import net.minecraft.data.models.model.ModelTemplate;
/*     */ import net.minecraft.data.models.model.ModelTemplates;
/*     */ import net.minecraft.data.models.model.TextureMapping;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ 
/*     */ public class ItemModelGenerators
/*     */ {
/*     */   private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;
/*     */   
/*     */   public ItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> debug1) {
/*  20 */     this.output = debug1;
/*     */   }
/*     */   
/*     */   private void generateFlatItem(Item debug1, ModelTemplate debug2) {
/*  24 */     debug2.create(ModelLocationUtils.getModelLocation(debug1), TextureMapping.layer0(debug1), this.output);
/*     */   }
/*     */   
/*     */   private void generateFlatItem(Item debug1, String debug2, ModelTemplate debug3) {
/*  28 */     debug3.create(ModelLocationUtils.getModelLocation(debug1, debug2), TextureMapping.layer0(TextureMapping.getItemTexture(debug1, debug2)), this.output);
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateFlatItem(Item debug1, Item debug2, ModelTemplate debug3) {
/*  33 */     debug3.create(ModelLocationUtils.getModelLocation(debug1), TextureMapping.layer0(debug2), this.output);
/*     */   }
/*     */   
/*     */   public void run() {
/*  37 */     generateFlatItem(Items.ACACIA_BOAT, ModelTemplates.FLAT_ITEM);
/*  38 */     generateFlatItem(Items.APPLE, ModelTemplates.FLAT_ITEM);
/*  39 */     generateFlatItem(Items.ARMOR_STAND, ModelTemplates.FLAT_ITEM);
/*  40 */     generateFlatItem(Items.ARROW, ModelTemplates.FLAT_ITEM);
/*  41 */     generateFlatItem(Items.BAKED_POTATO, ModelTemplates.FLAT_ITEM);
/*  42 */     generateFlatItem(Items.BAMBOO, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  43 */     generateFlatItem(Items.BEEF, ModelTemplates.FLAT_ITEM);
/*  44 */     generateFlatItem(Items.BEETROOT, ModelTemplates.FLAT_ITEM);
/*  45 */     generateFlatItem(Items.BEETROOT_SOUP, ModelTemplates.FLAT_ITEM);
/*  46 */     generateFlatItem(Items.BIRCH_BOAT, ModelTemplates.FLAT_ITEM);
/*  47 */     generateFlatItem(Items.BLACK_DYE, ModelTemplates.FLAT_ITEM);
/*  48 */     generateFlatItem(Items.BLAZE_POWDER, ModelTemplates.FLAT_ITEM);
/*  49 */     generateFlatItem(Items.BLAZE_ROD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  50 */     generateFlatItem(Items.BLUE_DYE, ModelTemplates.FLAT_ITEM);
/*  51 */     generateFlatItem(Items.BONE_MEAL, ModelTemplates.FLAT_ITEM);
/*  52 */     generateFlatItem(Items.BOOK, ModelTemplates.FLAT_ITEM);
/*  53 */     generateFlatItem(Items.BOWL, ModelTemplates.FLAT_ITEM);
/*  54 */     generateFlatItem(Items.BREAD, ModelTemplates.FLAT_ITEM);
/*  55 */     generateFlatItem(Items.BRICK, ModelTemplates.FLAT_ITEM);
/*  56 */     generateFlatItem(Items.BROWN_DYE, ModelTemplates.FLAT_ITEM);
/*  57 */     generateFlatItem(Items.BUCKET, ModelTemplates.FLAT_ITEM);
/*  58 */     generateFlatItem(Items.CARROT_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  59 */     generateFlatItem(Items.WARPED_FUNGUS_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  60 */     generateFlatItem(Items.CHAINMAIL_BOOTS, ModelTemplates.FLAT_ITEM);
/*  61 */     generateFlatItem(Items.CHAINMAIL_CHESTPLATE, ModelTemplates.FLAT_ITEM);
/*  62 */     generateFlatItem(Items.CHAINMAIL_HELMET, ModelTemplates.FLAT_ITEM);
/*  63 */     generateFlatItem(Items.CHAINMAIL_LEGGINGS, ModelTemplates.FLAT_ITEM);
/*  64 */     generateFlatItem(Items.CHARCOAL, ModelTemplates.FLAT_ITEM);
/*  65 */     generateFlatItem(Items.CHEST_MINECART, ModelTemplates.FLAT_ITEM);
/*  66 */     generateFlatItem(Items.CHICKEN, ModelTemplates.FLAT_ITEM);
/*  67 */     generateFlatItem(Items.CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/*  68 */     generateFlatItem(Items.CLAY_BALL, ModelTemplates.FLAT_ITEM);
/*     */     int debug1;
/*  70 */     for (debug1 = 1; debug1 < 64; debug1++) {
/*  71 */       generateFlatItem(Items.CLOCK, String.format("_%02d", new Object[] { Integer.valueOf(debug1) }), ModelTemplates.FLAT_ITEM);
/*     */     } 
/*     */     
/*  74 */     generateFlatItem(Items.COAL, ModelTemplates.FLAT_ITEM);
/*  75 */     generateFlatItem(Items.COD_BUCKET, ModelTemplates.FLAT_ITEM);
/*  76 */     generateFlatItem(Items.COMMAND_BLOCK_MINECART, ModelTemplates.FLAT_ITEM);
/*     */     
/*  78 */     for (debug1 = 0; debug1 < 32; debug1++) {
/*  79 */       if (debug1 != 16)
/*     */       {
/*     */         
/*  82 */         generateFlatItem(Items.COMPASS, String.format("_%02d", new Object[] { Integer.valueOf(debug1) }), ModelTemplates.FLAT_ITEM);
/*     */       }
/*     */     } 
/*  85 */     generateFlatItem(Items.COOKED_BEEF, ModelTemplates.FLAT_ITEM);
/*  86 */     generateFlatItem(Items.COOKED_CHICKEN, ModelTemplates.FLAT_ITEM);
/*  87 */     generateFlatItem(Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
/*  88 */     generateFlatItem(Items.COOKED_MUTTON, ModelTemplates.FLAT_ITEM);
/*  89 */     generateFlatItem(Items.COOKED_PORKCHOP, ModelTemplates.FLAT_ITEM);
/*  90 */     generateFlatItem(Items.COOKED_RABBIT, ModelTemplates.FLAT_ITEM);
/*  91 */     generateFlatItem(Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
/*  92 */     generateFlatItem(Items.COOKIE, ModelTemplates.FLAT_ITEM);
/*  93 */     generateFlatItem(Items.CREEPER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  94 */     generateFlatItem(Items.CYAN_DYE, ModelTemplates.FLAT_ITEM);
/*  95 */     generateFlatItem(Items.DARK_OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  96 */     generateFlatItem(Items.DIAMOND, ModelTemplates.FLAT_ITEM);
/*  97 */     generateFlatItem(Items.DIAMOND_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  98 */     generateFlatItem(Items.DIAMOND_BOOTS, ModelTemplates.FLAT_ITEM);
/*  99 */     generateFlatItem(Items.DIAMOND_CHESTPLATE, ModelTemplates.FLAT_ITEM);
/* 100 */     generateFlatItem(Items.DIAMOND_HELMET, ModelTemplates.FLAT_ITEM);
/* 101 */     generateFlatItem(Items.DIAMOND_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 102 */     generateFlatItem(Items.DIAMOND_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/* 103 */     generateFlatItem(Items.DIAMOND_LEGGINGS, ModelTemplates.FLAT_ITEM);
/* 104 */     generateFlatItem(Items.DIAMOND_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 105 */     generateFlatItem(Items.DIAMOND_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 106 */     generateFlatItem(Items.DIAMOND_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 107 */     generateFlatItem(Items.DRAGON_BREATH, ModelTemplates.FLAT_ITEM);
/* 108 */     generateFlatItem(Items.DRIED_KELP, ModelTemplates.FLAT_ITEM);
/* 109 */     generateFlatItem(Items.EGG, ModelTemplates.FLAT_ITEM);
/* 110 */     generateFlatItem(Items.EMERALD, ModelTemplates.FLAT_ITEM);
/* 111 */     generateFlatItem(Items.ENCHANTED_BOOK, ModelTemplates.FLAT_ITEM);
/* 112 */     generateFlatItem(Items.ENDER_EYE, ModelTemplates.FLAT_ITEM);
/* 113 */     generateFlatItem(Items.ENDER_PEARL, ModelTemplates.FLAT_ITEM);
/* 114 */     generateFlatItem(Items.END_CRYSTAL, ModelTemplates.FLAT_ITEM);
/* 115 */     generateFlatItem(Items.EXPERIENCE_BOTTLE, ModelTemplates.FLAT_ITEM);
/* 116 */     generateFlatItem(Items.FERMENTED_SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/* 117 */     generateFlatItem(Items.FIREWORK_ROCKET, ModelTemplates.FLAT_ITEM);
/* 118 */     generateFlatItem(Items.FIRE_CHARGE, ModelTemplates.FLAT_ITEM);
/* 119 */     generateFlatItem(Items.FLINT, ModelTemplates.FLAT_ITEM);
/* 120 */     generateFlatItem(Items.FLINT_AND_STEEL, ModelTemplates.FLAT_ITEM);
/* 121 */     generateFlatItem(Items.FLOWER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/* 122 */     generateFlatItem(Items.FURNACE_MINECART, ModelTemplates.FLAT_ITEM);
/* 123 */     generateFlatItem(Items.GHAST_TEAR, ModelTemplates.FLAT_ITEM);
/* 124 */     generateFlatItem(Items.GLASS_BOTTLE, ModelTemplates.FLAT_ITEM);
/* 125 */     generateFlatItem(Items.GLISTERING_MELON_SLICE, ModelTemplates.FLAT_ITEM);
/* 126 */     generateFlatItem(Items.GLOBE_BANNER_PATTER, ModelTemplates.FLAT_ITEM);
/* 127 */     generateFlatItem(Items.GLOWSTONE_DUST, ModelTemplates.FLAT_ITEM);
/* 128 */     generateFlatItem(Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/* 129 */     generateFlatItem(Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 130 */     generateFlatItem(Items.GOLDEN_BOOTS, ModelTemplates.FLAT_ITEM);
/* 131 */     generateFlatItem(Items.GOLDEN_CARROT, ModelTemplates.FLAT_ITEM);
/* 132 */     generateFlatItem(Items.GOLDEN_CHESTPLATE, ModelTemplates.FLAT_ITEM);
/* 133 */     generateFlatItem(Items.GOLDEN_HELMET, ModelTemplates.FLAT_ITEM);
/* 134 */     generateFlatItem(Items.GOLDEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 135 */     generateFlatItem(Items.GOLDEN_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/* 136 */     generateFlatItem(Items.GOLDEN_LEGGINGS, ModelTemplates.FLAT_ITEM);
/* 137 */     generateFlatItem(Items.GOLDEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 138 */     generateFlatItem(Items.GOLDEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 139 */     generateFlatItem(Items.GOLDEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 140 */     generateFlatItem(Items.GOLD_INGOT, ModelTemplates.FLAT_ITEM);
/* 141 */     generateFlatItem(Items.GOLD_NUGGET, ModelTemplates.FLAT_ITEM);
/* 142 */     generateFlatItem(Items.GRAY_DYE, ModelTemplates.FLAT_ITEM);
/* 143 */     generateFlatItem(Items.GREEN_DYE, ModelTemplates.FLAT_ITEM);
/* 144 */     generateFlatItem(Items.GUNPOWDER, ModelTemplates.FLAT_ITEM);
/* 145 */     generateFlatItem(Items.HEART_OF_THE_SEA, ModelTemplates.FLAT_ITEM);
/* 146 */     generateFlatItem(Items.HONEYCOMB, ModelTemplates.FLAT_ITEM);
/* 147 */     generateFlatItem(Items.HONEY_BOTTLE, ModelTemplates.FLAT_ITEM);
/* 148 */     generateFlatItem(Items.HOPPER_MINECART, ModelTemplates.FLAT_ITEM);
/* 149 */     generateFlatItem(Items.INK_SAC, ModelTemplates.FLAT_ITEM);
/* 150 */     generateFlatItem(Items.IRON_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 151 */     generateFlatItem(Items.IRON_BOOTS, ModelTemplates.FLAT_ITEM);
/* 152 */     generateFlatItem(Items.IRON_CHESTPLATE, ModelTemplates.FLAT_ITEM);
/* 153 */     generateFlatItem(Items.IRON_HELMET, ModelTemplates.FLAT_ITEM);
/* 154 */     generateFlatItem(Items.IRON_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 155 */     generateFlatItem(Items.IRON_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/* 156 */     generateFlatItem(Items.IRON_INGOT, ModelTemplates.FLAT_ITEM);
/* 157 */     generateFlatItem(Items.IRON_LEGGINGS, ModelTemplates.FLAT_ITEM);
/* 158 */     generateFlatItem(Items.IRON_NUGGET, ModelTemplates.FLAT_ITEM);
/* 159 */     generateFlatItem(Items.IRON_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 160 */     generateFlatItem(Items.IRON_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 161 */     generateFlatItem(Items.IRON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 162 */     generateFlatItem(Items.ITEM_FRAME, ModelTemplates.FLAT_ITEM);
/* 163 */     generateFlatItem(Items.JUNGLE_BOAT, ModelTemplates.FLAT_ITEM);
/* 164 */     generateFlatItem(Items.KNOWLEDGE_BOOK, ModelTemplates.FLAT_ITEM);
/* 165 */     generateFlatItem(Items.LAPIS_LAZULI, ModelTemplates.FLAT_ITEM);
/* 166 */     generateFlatItem(Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
/* 167 */     generateFlatItem(Items.LEATHER, ModelTemplates.FLAT_ITEM);
/* 168 */     generateFlatItem(Items.LEATHER_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/* 169 */     generateFlatItem(Items.LIGHT_BLUE_DYE, ModelTemplates.FLAT_ITEM);
/* 170 */     generateFlatItem(Items.LIGHT_GRAY_DYE, ModelTemplates.FLAT_ITEM);
/* 171 */     generateFlatItem(Items.LIME_DYE, ModelTemplates.FLAT_ITEM);
/* 172 */     generateFlatItem(Items.MAGENTA_DYE, ModelTemplates.FLAT_ITEM);
/* 173 */     generateFlatItem(Items.MAGMA_CREAM, ModelTemplates.FLAT_ITEM);
/* 174 */     generateFlatItem(Items.MAP, ModelTemplates.FLAT_ITEM);
/* 175 */     generateFlatItem(Items.MELON_SLICE, ModelTemplates.FLAT_ITEM);
/* 176 */     generateFlatItem(Items.MILK_BUCKET, ModelTemplates.FLAT_ITEM);
/* 177 */     generateFlatItem(Items.MINECART, ModelTemplates.FLAT_ITEM);
/* 178 */     generateFlatItem(Items.MOJANG_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/* 179 */     generateFlatItem(Items.MUSHROOM_STEW, ModelTemplates.FLAT_ITEM);
/* 180 */     generateFlatItem(Items.MUSIC_DISC_11, ModelTemplates.FLAT_ITEM);
/* 181 */     generateFlatItem(Items.MUSIC_DISC_13, ModelTemplates.FLAT_ITEM);
/* 182 */     generateFlatItem(Items.MUSIC_DISC_BLOCKS, ModelTemplates.FLAT_ITEM);
/* 183 */     generateFlatItem(Items.MUSIC_DISC_CAT, ModelTemplates.FLAT_ITEM);
/* 184 */     generateFlatItem(Items.MUSIC_DISC_CHIRP, ModelTemplates.FLAT_ITEM);
/* 185 */     generateFlatItem(Items.MUSIC_DISC_FAR, ModelTemplates.FLAT_ITEM);
/* 186 */     generateFlatItem(Items.MUSIC_DISC_MALL, ModelTemplates.FLAT_ITEM);
/* 187 */     generateFlatItem(Items.MUSIC_DISC_MELLOHI, ModelTemplates.FLAT_ITEM);
/* 188 */     generateFlatItem(Items.MUSIC_DISC_PIGSTEP, ModelTemplates.FLAT_ITEM);
/* 189 */     generateFlatItem(Items.MUSIC_DISC_STAL, ModelTemplates.FLAT_ITEM);
/* 190 */     generateFlatItem(Items.MUSIC_DISC_STRAD, ModelTemplates.FLAT_ITEM);
/* 191 */     generateFlatItem(Items.MUSIC_DISC_WAIT, ModelTemplates.FLAT_ITEM);
/* 192 */     generateFlatItem(Items.MUSIC_DISC_WARD, ModelTemplates.FLAT_ITEM);
/* 193 */     generateFlatItem(Items.MUTTON, ModelTemplates.FLAT_ITEM);
/* 194 */     generateFlatItem(Items.NAME_TAG, ModelTemplates.FLAT_ITEM);
/* 195 */     generateFlatItem(Items.NAUTILUS_SHELL, ModelTemplates.FLAT_ITEM);
/* 196 */     generateFlatItem(Items.NETHERITE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 197 */     generateFlatItem(Items.NETHERITE_BOOTS, ModelTemplates.FLAT_ITEM);
/* 198 */     generateFlatItem(Items.NETHERITE_CHESTPLATE, ModelTemplates.FLAT_ITEM);
/* 199 */     generateFlatItem(Items.NETHERITE_HELMET, ModelTemplates.FLAT_ITEM);
/* 200 */     generateFlatItem(Items.NETHERITE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 201 */     generateFlatItem(Items.NETHERITE_INGOT, ModelTemplates.FLAT_ITEM);
/* 202 */     generateFlatItem(Items.NETHERITE_LEGGINGS, ModelTemplates.FLAT_ITEM);
/* 203 */     generateFlatItem(Items.NETHERITE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 204 */     generateFlatItem(Items.NETHERITE_SCRAP, ModelTemplates.FLAT_ITEM);
/* 205 */     generateFlatItem(Items.NETHERITE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 206 */     generateFlatItem(Items.NETHERITE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 207 */     generateFlatItem(Items.NETHER_BRICK, ModelTemplates.FLAT_ITEM);
/* 208 */     generateFlatItem(Items.NETHER_STAR, ModelTemplates.FLAT_ITEM);
/* 209 */     generateFlatItem(Items.OAK_BOAT, ModelTemplates.FLAT_ITEM);
/* 210 */     generateFlatItem(Items.ORANGE_DYE, ModelTemplates.FLAT_ITEM);
/* 211 */     generateFlatItem(Items.PAINTING, ModelTemplates.FLAT_ITEM);
/* 212 */     generateFlatItem(Items.PAPER, ModelTemplates.FLAT_ITEM);
/* 213 */     generateFlatItem(Items.PHANTOM_MEMBRANE, ModelTemplates.FLAT_ITEM);
/* 214 */     generateFlatItem(Items.PIGLIN_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/* 215 */     generateFlatItem(Items.PINK_DYE, ModelTemplates.FLAT_ITEM);
/* 216 */     generateFlatItem(Items.POISONOUS_POTATO, ModelTemplates.FLAT_ITEM);
/* 217 */     generateFlatItem(Items.POPPED_CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/* 218 */     generateFlatItem(Items.PORKCHOP, ModelTemplates.FLAT_ITEM);
/* 219 */     generateFlatItem(Items.PRISMARINE_CRYSTALS, ModelTemplates.FLAT_ITEM);
/* 220 */     generateFlatItem(Items.PRISMARINE_SHARD, ModelTemplates.FLAT_ITEM);
/* 221 */     generateFlatItem(Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
/* 222 */     generateFlatItem(Items.PUFFERFISH_BUCKET, ModelTemplates.FLAT_ITEM);
/* 223 */     generateFlatItem(Items.PUMPKIN_PIE, ModelTemplates.FLAT_ITEM);
/* 224 */     generateFlatItem(Items.PURPLE_DYE, ModelTemplates.FLAT_ITEM);
/* 225 */     generateFlatItem(Items.QUARTZ, ModelTemplates.FLAT_ITEM);
/* 226 */     generateFlatItem(Items.RABBIT, ModelTemplates.FLAT_ITEM);
/* 227 */     generateFlatItem(Items.RABBIT_FOOT, ModelTemplates.FLAT_ITEM);
/* 228 */     generateFlatItem(Items.RABBIT_HIDE, ModelTemplates.FLAT_ITEM);
/* 229 */     generateFlatItem(Items.RABBIT_STEW, ModelTemplates.FLAT_ITEM);
/* 230 */     generateFlatItem(Items.RED_DYE, ModelTemplates.FLAT_ITEM);
/* 231 */     generateFlatItem(Items.ROTTEN_FLESH, ModelTemplates.FLAT_ITEM);
/* 232 */     generateFlatItem(Items.SADDLE, ModelTemplates.FLAT_ITEM);
/* 233 */     generateFlatItem(Items.SALMON, ModelTemplates.FLAT_ITEM);
/* 234 */     generateFlatItem(Items.SALMON_BUCKET, ModelTemplates.FLAT_ITEM);
/* 235 */     generateFlatItem(Items.SCUTE, ModelTemplates.FLAT_ITEM);
/* 236 */     generateFlatItem(Items.SHEARS, ModelTemplates.FLAT_ITEM);
/* 237 */     generateFlatItem(Items.SHULKER_SHELL, ModelTemplates.FLAT_ITEM);
/* 238 */     generateFlatItem(Items.SKULL_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/* 239 */     generateFlatItem(Items.SLIME_BALL, ModelTemplates.FLAT_ITEM);
/* 240 */     generateFlatItem(Items.SNOWBALL, ModelTemplates.FLAT_ITEM);
/* 241 */     generateFlatItem(Items.SPECTRAL_ARROW, ModelTemplates.FLAT_ITEM);
/* 242 */     generateFlatItem(Items.SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/* 243 */     generateFlatItem(Items.SPRUCE_BOAT, ModelTemplates.FLAT_ITEM);
/* 244 */     generateFlatItem(Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 245 */     generateFlatItem(Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 246 */     generateFlatItem(Items.STONE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 247 */     generateFlatItem(Items.STONE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 248 */     generateFlatItem(Items.STONE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 249 */     generateFlatItem(Items.STONE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 250 */     generateFlatItem(Items.SUGAR, ModelTemplates.FLAT_ITEM);
/* 251 */     generateFlatItem(Items.SUSPICIOUS_STEW, ModelTemplates.FLAT_ITEM);
/* 252 */     generateFlatItem(Items.TNT_MINECART, ModelTemplates.FLAT_ITEM);
/* 253 */     generateFlatItem(Items.TOTEM_OF_UNDYING, ModelTemplates.FLAT_ITEM);
/* 254 */     generateFlatItem(Items.TRIDENT, ModelTemplates.FLAT_ITEM);
/* 255 */     generateFlatItem(Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
/* 256 */     generateFlatItem(Items.TROPICAL_FISH_BUCKET, ModelTemplates.FLAT_ITEM);
/* 257 */     generateFlatItem(Items.TURTLE_HELMET, ModelTemplates.FLAT_ITEM);
/* 258 */     generateFlatItem(Items.WATER_BUCKET, ModelTemplates.FLAT_ITEM);
/* 259 */     generateFlatItem(Items.WHEAT, ModelTemplates.FLAT_ITEM);
/* 260 */     generateFlatItem(Items.WHITE_DYE, ModelTemplates.FLAT_ITEM);
/* 261 */     generateFlatItem(Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 262 */     generateFlatItem(Items.WOODEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 263 */     generateFlatItem(Items.WOODEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 264 */     generateFlatItem(Items.WOODEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 265 */     generateFlatItem(Items.WOODEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 266 */     generateFlatItem(Items.WRITABLE_BOOK, ModelTemplates.FLAT_ITEM);
/* 267 */     generateFlatItem(Items.WRITTEN_BOOK, ModelTemplates.FLAT_ITEM);
/* 268 */     generateFlatItem(Items.YELLOW_DYE, ModelTemplates.FLAT_ITEM);
/*     */     
/* 270 */     generateFlatItem(Items.DEBUG_STICK, Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/* 271 */     generateFlatItem(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\ItemModelGenerators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */