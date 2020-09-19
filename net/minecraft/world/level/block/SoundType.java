/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ 
/*     */ public class SoundType {
/*   7 */   public static final SoundType WOOD = new SoundType(1.0F, 1.0F, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.WOOD_PLACE, SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL);
/*   8 */   public static final SoundType GRAVEL = new SoundType(1.0F, 1.0F, SoundEvents.GRAVEL_BREAK, SoundEvents.GRAVEL_STEP, SoundEvents.GRAVEL_PLACE, SoundEvents.GRAVEL_HIT, SoundEvents.GRAVEL_FALL);
/*   9 */   public static final SoundType GRASS = new SoundType(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.GRASS_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
/*  10 */   public static final SoundType LILY_PAD = new SoundType(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
/*  11 */   public static final SoundType STONE = new SoundType(1.0F, 1.0F, SoundEvents.STONE_BREAK, SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
/*  12 */   public static final SoundType METAL = new SoundType(1.0F, 1.5F, SoundEvents.METAL_BREAK, SoundEvents.METAL_STEP, SoundEvents.METAL_PLACE, SoundEvents.METAL_HIT, SoundEvents.METAL_FALL);
/*  13 */   public static final SoundType GLASS = new SoundType(1.0F, 1.0F, SoundEvents.GLASS_BREAK, SoundEvents.GLASS_STEP, SoundEvents.GLASS_PLACE, SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL);
/*  14 */   public static final SoundType WOOL = new SoundType(1.0F, 1.0F, SoundEvents.WOOL_BREAK, SoundEvents.WOOL_STEP, SoundEvents.WOOL_PLACE, SoundEvents.WOOL_HIT, SoundEvents.WOOL_FALL);
/*  15 */   public static final SoundType SAND = new SoundType(1.0F, 1.0F, SoundEvents.SAND_BREAK, SoundEvents.SAND_STEP, SoundEvents.SAND_PLACE, SoundEvents.SAND_HIT, SoundEvents.SAND_FALL);
/*  16 */   public static final SoundType SNOW = new SoundType(1.0F, 1.0F, SoundEvents.SNOW_BREAK, SoundEvents.SNOW_STEP, SoundEvents.SNOW_PLACE, SoundEvents.SNOW_HIT, SoundEvents.SNOW_FALL);
/*  17 */   public static final SoundType LADDER = new SoundType(1.0F, 1.0F, SoundEvents.LADDER_BREAK, SoundEvents.LADDER_STEP, SoundEvents.LADDER_PLACE, SoundEvents.LADDER_HIT, SoundEvents.LADDER_FALL);
/*  18 */   public static final SoundType ANVIL = new SoundType(0.3F, 1.0F, SoundEvents.ANVIL_BREAK, SoundEvents.ANVIL_STEP, SoundEvents.ANVIL_PLACE, SoundEvents.ANVIL_HIT, SoundEvents.ANVIL_FALL);
/*  19 */   public static final SoundType SLIME_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.SLIME_BLOCK_BREAK, SoundEvents.SLIME_BLOCK_STEP, SoundEvents.SLIME_BLOCK_PLACE, SoundEvents.SLIME_BLOCK_HIT, SoundEvents.SLIME_BLOCK_FALL);
/*  20 */   public static final SoundType HONEY_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.HONEY_BLOCK_BREAK, SoundEvents.HONEY_BLOCK_STEP, SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_HIT, SoundEvents.HONEY_BLOCK_FALL);
/*  21 */   public static final SoundType WET_GRASS = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.WET_GRASS_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
/*  22 */   public static final SoundType CORAL_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.CORAL_BLOCK_BREAK, SoundEvents.CORAL_BLOCK_STEP, SoundEvents.CORAL_BLOCK_PLACE, SoundEvents.CORAL_BLOCK_HIT, SoundEvents.CORAL_BLOCK_FALL);
/*  23 */   public static final SoundType BAMBOO = new SoundType(1.0F, 1.0F, SoundEvents.BAMBOO_BREAK, SoundEvents.BAMBOO_STEP, SoundEvents.BAMBOO_PLACE, SoundEvents.BAMBOO_HIT, SoundEvents.BAMBOO_FALL);
/*  24 */   public static final SoundType BAMBOO_SAPLING = new SoundType(1.0F, 1.0F, SoundEvents.BAMBOO_SAPLING_BREAK, SoundEvents.BAMBOO_STEP, SoundEvents.BAMBOO_SAPLING_PLACE, SoundEvents.BAMBOO_SAPLING_HIT, SoundEvents.BAMBOO_FALL);
/*  25 */   public static final SoundType SCAFFOLDING = new SoundType(1.0F, 1.0F, SoundEvents.SCAFFOLDING_BREAK, SoundEvents.SCAFFOLDING_STEP, SoundEvents.SCAFFOLDING_PLACE, SoundEvents.SCAFFOLDING_HIT, SoundEvents.SCAFFOLDING_FALL);
/*  26 */   public static final SoundType SWEET_BERRY_BUSH = new SoundType(1.0F, 1.0F, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundEvents.GRASS_STEP, SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
/*  27 */   public static final SoundType CROP = new SoundType(1.0F, 1.0F, SoundEvents.CROP_BREAK, SoundEvents.GRASS_STEP, SoundEvents.CROP_PLANTED, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
/*  28 */   public static final SoundType HARD_CROP = new SoundType(1.0F, 1.0F, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.CROP_PLANTED, SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL);
/*  29 */   public static final SoundType VINE = new SoundType(1.0F, 1.0F, SoundEvents.GRASS_BREAK, SoundEvents.VINE_STEP, SoundEvents.GRASS_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
/*  30 */   public static final SoundType NETHER_WART = new SoundType(1.0F, 1.0F, SoundEvents.NETHER_WART_BREAK, SoundEvents.STONE_STEP, SoundEvents.NETHER_WART_PLANTED, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
/*  31 */   public static final SoundType LANTERN = new SoundType(1.0F, 1.0F, SoundEvents.LANTERN_BREAK, SoundEvents.LANTERN_STEP, SoundEvents.LANTERN_PLACE, SoundEvents.LANTERN_HIT, SoundEvents.LANTERN_FALL);
/*  32 */   public static final SoundType STEM = new SoundType(1.0F, 1.0F, SoundEvents.STEM_BREAK, SoundEvents.STEM_STEP, SoundEvents.STEM_PLACE, SoundEvents.STEM_HIT, SoundEvents.STEM_FALL);
/*  33 */   public static final SoundType NYLIUM = new SoundType(1.0F, 1.0F, SoundEvents.NYLIUM_BREAK, SoundEvents.NYLIUM_STEP, SoundEvents.NYLIUM_PLACE, SoundEvents.NYLIUM_HIT, SoundEvents.NYLIUM_FALL);
/*  34 */   public static final SoundType FUNGUS = new SoundType(1.0F, 1.0F, SoundEvents.FUNGUS_BREAK, SoundEvents.FUNGUS_STEP, SoundEvents.FUNGUS_PLACE, SoundEvents.FUNGUS_HIT, SoundEvents.FUNGUS_FALL);
/*  35 */   public static final SoundType ROOTS = new SoundType(1.0F, 1.0F, SoundEvents.ROOTS_BREAK, SoundEvents.ROOTS_STEP, SoundEvents.ROOTS_PLACE, SoundEvents.ROOTS_HIT, SoundEvents.ROOTS_FALL);
/*  36 */   public static final SoundType SHROOMLIGHT = new SoundType(1.0F, 1.0F, SoundEvents.SHROOMLIGHT_BREAK, SoundEvents.SHROOMLIGHT_STEP, SoundEvents.SHROOMLIGHT_PLACE, SoundEvents.SHROOMLIGHT_HIT, SoundEvents.SHROOMLIGHT_FALL);
/*  37 */   public static final SoundType WEEPING_VINES = new SoundType(1.0F, 1.0F, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP, SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL);
/*     */   
/*  39 */   public static final SoundType TWISTING_VINES = new SoundType(1.0F, 0.5F, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP, SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL);
/*     */   
/*  41 */   public static final SoundType SOUL_SAND = new SoundType(1.0F, 1.0F, SoundEvents.SOUL_SAND_BREAK, SoundEvents.SOUL_SAND_STEP, SoundEvents.SOUL_SAND_PLACE, SoundEvents.SOUL_SAND_HIT, SoundEvents.SOUL_SAND_FALL);
/*  42 */   public static final SoundType SOUL_SOIL = new SoundType(1.0F, 1.0F, SoundEvents.SOUL_SOIL_BREAK, SoundEvents.SOUL_SOIL_STEP, SoundEvents.SOUL_SOIL_PLACE, SoundEvents.SOUL_SOIL_HIT, SoundEvents.SOUL_SOIL_FALL);
/*  43 */   public static final SoundType BASALT = new SoundType(1.0F, 1.0F, SoundEvents.BASALT_BREAK, SoundEvents.BASALT_STEP, SoundEvents.BASALT_PLACE, SoundEvents.BASALT_HIT, SoundEvents.BASALT_FALL);
/*  44 */   public static final SoundType WART_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.WART_BLOCK_BREAK, SoundEvents.WART_BLOCK_STEP, SoundEvents.WART_BLOCK_PLACE, SoundEvents.WART_BLOCK_HIT, SoundEvents.WART_BLOCK_FALL);
/*  45 */   public static final SoundType NETHERRACK = new SoundType(1.0F, 1.0F, SoundEvents.NETHERRACK_BREAK, SoundEvents.NETHERRACK_STEP, SoundEvents.NETHERRACK_PLACE, SoundEvents.NETHERRACK_HIT, SoundEvents.NETHERRACK_FALL);
/*  46 */   public static final SoundType NETHER_BRICKS = new SoundType(1.0F, 1.0F, SoundEvents.NETHER_BRICKS_BREAK, SoundEvents.NETHER_BRICKS_STEP, SoundEvents.NETHER_BRICKS_PLACE, SoundEvents.NETHER_BRICKS_HIT, SoundEvents.NETHER_BRICKS_FALL);
/*  47 */   public static final SoundType NETHER_SPROUTS = new SoundType(1.0F, 1.0F, SoundEvents.NETHER_SPROUTS_BREAK, SoundEvents.NETHER_SPROUTS_STEP, SoundEvents.NETHER_SPROUTS_PLACE, SoundEvents.NETHER_SPROUTS_HIT, SoundEvents.NETHER_SPROUTS_FALL);
/*  48 */   public static final SoundType NETHER_ORE = new SoundType(1.0F, 1.0F, SoundEvents.NETHER_ORE_BREAK, SoundEvents.NETHER_ORE_STEP, SoundEvents.NETHER_ORE_PLACE, SoundEvents.NETHER_ORE_HIT, SoundEvents.NETHER_ORE_FALL);
/*  49 */   public static final SoundType BONE_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.BONE_BLOCK_BREAK, SoundEvents.BONE_BLOCK_STEP, SoundEvents.BONE_BLOCK_PLACE, SoundEvents.BONE_BLOCK_HIT, SoundEvents.BONE_BLOCK_FALL);
/*  50 */   public static final SoundType NETHERITE_BLOCK = new SoundType(1.0F, 1.0F, SoundEvents.NETHERITE_BLOCK_BREAK, SoundEvents.NETHERITE_BLOCK_STEP, SoundEvents.NETHERITE_BLOCK_PLACE, SoundEvents.NETHERITE_BLOCK_HIT, SoundEvents.NETHERITE_BLOCK_FALL);
/*  51 */   public static final SoundType ANCIENT_DEBRIS = new SoundType(1.0F, 1.0F, SoundEvents.ANCIENT_DEBRIS_BREAK, SoundEvents.ANCIENT_DEBRIS_STEP, SoundEvents.ANCIENT_DEBRIS_PLACE, SoundEvents.ANCIENT_DEBRIS_HIT, SoundEvents.ANCIENT_DEBRIS_FALL);
/*  52 */   public static final SoundType LODESTONE = new SoundType(1.0F, 1.0F, SoundEvents.LODESTONE_BREAK, SoundEvents.LODESTONE_STEP, SoundEvents.LODESTONE_PLACE, SoundEvents.LODESTONE_HIT, SoundEvents.LODESTONE_FALL);
/*     */   
/*  54 */   public static final SoundType CHAIN = new SoundType(1.0F, 1.0F, SoundEvents.CHAIN_BREAK, SoundEvents.CHAIN_STEP, SoundEvents.CHAIN_PLACE, SoundEvents.CHAIN_HIT, SoundEvents.CHAIN_FALL);
/*  55 */   public static final SoundType NETHER_GOLD_ORE = new SoundType(1.0F, 1.0F, SoundEvents.NETHER_GOLD_ORE_BREAK, SoundEvents.NETHER_GOLD_ORE_STEP, SoundEvents.NETHER_GOLD_ORE_PLACE, SoundEvents.NETHER_GOLD_ORE_HIT, SoundEvents.NETHER_GOLD_ORE_FALL);
/*  56 */   public static final SoundType GILDED_BLACKSTONE = new SoundType(1.0F, 1.0F, SoundEvents.GILDED_BLACKSTONE_BREAK, SoundEvents.GILDED_BLACKSTONE_STEP, SoundEvents.GILDED_BLACKSTONE_PLACE, SoundEvents.GILDED_BLACKSTONE_HIT, SoundEvents.GILDED_BLACKSTONE_FALL);
/*     */   
/*     */   public final float volume;
/*     */   
/*     */   public final float pitch;
/*     */   private final SoundEvent breakSound;
/*     */   private final SoundEvent stepSound;
/*     */   private final SoundEvent placeSound;
/*     */   private final SoundEvent hitSound;
/*     */   private final SoundEvent fallSound;
/*     */   
/*     */   public SoundType(float debug1, float debug2, SoundEvent debug3, SoundEvent debug4, SoundEvent debug5, SoundEvent debug6, SoundEvent debug7) {
/*  68 */     this.volume = debug1;
/*  69 */     this.pitch = debug2;
/*     */     
/*  71 */     this.breakSound = debug3;
/*  72 */     this.stepSound = debug4;
/*  73 */     this.placeSound = debug5;
/*  74 */     this.hitSound = debug6;
/*  75 */     this.fallSound = debug7;
/*     */   }
/*     */   
/*     */   public float getVolume() {
/*  79 */     return this.volume;
/*     */   }
/*     */   
/*     */   public float getPitch() {
/*  83 */     return this.pitch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoundEvent getStepSound() {
/*  91 */     return this.stepSound;
/*     */   }
/*     */   
/*     */   public SoundEvent getPlaceSound() {
/*  95 */     return this.placeSound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoundEvent getFallSound() {
/* 103 */     return this.fallSound;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */