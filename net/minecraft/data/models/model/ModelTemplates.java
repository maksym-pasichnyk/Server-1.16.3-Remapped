/*     */ package net.minecraft.data.models.model;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ 
/*     */ public class ModelTemplates
/*     */ {
/*   9 */   public static final ModelTemplate CUBE = create("cube", new TextureSlot[] { TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN });
/*  10 */   public static final ModelTemplate CUBE_DIRECTIONAL = create("cube_directional", new TextureSlot[] { TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN });
/*  11 */   public static final ModelTemplate CUBE_ALL = create("cube_all", new TextureSlot[] { TextureSlot.ALL });
/*  12 */   public static final ModelTemplate CUBE_MIRRORED_ALL = create("cube_mirrored_all", "_mirrored", new TextureSlot[] { TextureSlot.ALL });
/*  13 */   public static final ModelTemplate CUBE_COLUMN = create("cube_column", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*  14 */   public static final ModelTemplate CUBE_COLUMN_HORIZONTAL = create("cube_column_horizontal", "_horizontal", new TextureSlot[] { TextureSlot.END, TextureSlot.SIDE });
/*  15 */   public static final ModelTemplate CUBE_TOP = create("cube_top", new TextureSlot[] { TextureSlot.TOP, TextureSlot.SIDE });
/*  16 */   public static final ModelTemplate CUBE_BOTTOM_TOP = create("cube_bottom_top", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE });
/*  17 */   public static final ModelTemplate CUBE_ORIENTABLE = create("orientable", new TextureSlot[] { TextureSlot.TOP, TextureSlot.FRONT, TextureSlot.SIDE });
/*  18 */   public static final ModelTemplate CUBE_ORIENTABLE_TOP_BOTTOM = create("orientable_with_bottom", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.FRONT });
/*  19 */   public static final ModelTemplate CUBE_ORIENTABLE_VERTICAL = create("orientable_vertical", "_vertical", new TextureSlot[] { TextureSlot.FRONT, TextureSlot.SIDE });
/*     */   
/*  21 */   public static final ModelTemplate BUTTON = create("button", new TextureSlot[] { TextureSlot.TEXTURE });
/*  22 */   public static final ModelTemplate BUTTON_PRESSED = create("button_pressed", "_pressed", new TextureSlot[] { TextureSlot.TEXTURE });
/*  23 */   public static final ModelTemplate BUTTON_INVENTORY = create("button_inventory", "_inventory", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  25 */   public static final ModelTemplate DOOR_BOTTOM = create("door_bottom", "_bottom", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*  26 */   public static final ModelTemplate DOOR_BOTTOM_HINGE = create("door_bottom_rh", "_bottom_hinge", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*  27 */   public static final ModelTemplate DOOR_TOP = create("door_top", "_top", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*  28 */   public static final ModelTemplate DOOR_TOP_HINGE = create("door_top_rh", "_top_hinge", new TextureSlot[] { TextureSlot.TOP, TextureSlot.BOTTOM });
/*     */   
/*  30 */   public static final ModelTemplate FENCE_POST = create("fence_post", "_post", new TextureSlot[] { TextureSlot.TEXTURE });
/*  31 */   public static final ModelTemplate FENCE_SIDE = create("fence_side", "_side", new TextureSlot[] { TextureSlot.TEXTURE });
/*  32 */   public static final ModelTemplate FENCE_INVENTORY = create("fence_inventory", "_inventory", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  34 */   public static final ModelTemplate WALL_POST = create("template_wall_post", "_post", new TextureSlot[] { TextureSlot.WALL });
/*  35 */   public static final ModelTemplate WALL_LOW_SIDE = create("template_wall_side", "_side", new TextureSlot[] { TextureSlot.WALL });
/*  36 */   public static final ModelTemplate WALL_TALL_SIDE = create("template_wall_side_tall", "_side_tall", new TextureSlot[] { TextureSlot.WALL });
/*  37 */   public static final ModelTemplate WALL_INVENTORY = create("wall_inventory", "_inventory", new TextureSlot[] { TextureSlot.WALL });
/*     */   
/*  39 */   public static final ModelTemplate FENCE_GATE_CLOSED = create("template_fence_gate", new TextureSlot[] { TextureSlot.TEXTURE });
/*  40 */   public static final ModelTemplate FENCE_GATE_OPEN = create("template_fence_gate_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*  41 */   public static final ModelTemplate FENCE_GATE_WALL_CLOSED = create("template_fence_gate_wall", "_wall", new TextureSlot[] { TextureSlot.TEXTURE });
/*  42 */   public static final ModelTemplate FENCE_GATE_WALL_OPEN = create("template_fence_gate_wall_open", "_wall_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  44 */   public static final ModelTemplate PRESSURE_PLATE_UP = create("pressure_plate_up", new TextureSlot[] { TextureSlot.TEXTURE });
/*  45 */   public static final ModelTemplate PRESSURE_PLATE_DOWN = create("pressure_plate_down", "_down", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  47 */   public static final ModelTemplate PARTICLE_ONLY = create(new TextureSlot[] { TextureSlot.PARTICLE });
/*     */   
/*  49 */   public static final ModelTemplate SLAB_BOTTOM = create("slab", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*  50 */   public static final ModelTemplate SLAB_TOP = create("slab_top", "_top", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  52 */   public static final ModelTemplate LEAVES = create("leaves", new TextureSlot[] { TextureSlot.ALL });
/*     */   
/*  54 */   public static final ModelTemplate STAIRS_STRAIGHT = create("stairs", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*  55 */   public static final ModelTemplate STAIRS_INNER = create("inner_stairs", "_inner", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*  56 */   public static final ModelTemplate STAIRS_OUTER = create("outer_stairs", "_outer", new TextureSlot[] { TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  58 */   public static final ModelTemplate TRAPDOOR_TOP = create("template_trapdoor_top", "_top", new TextureSlot[] { TextureSlot.TEXTURE });
/*  59 */   public static final ModelTemplate TRAPDOOR_BOTTOM = create("template_trapdoor_bottom", "_bottom", new TextureSlot[] { TextureSlot.TEXTURE });
/*  60 */   public static final ModelTemplate TRAPDOOR_OPEN = create("template_trapdoor_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  62 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_TOP = create("template_orientable_trapdoor_top", "_top", new TextureSlot[] { TextureSlot.TEXTURE });
/*  63 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_BOTTOM = create("template_orientable_trapdoor_bottom", "_bottom", new TextureSlot[] { TextureSlot.TEXTURE });
/*  64 */   public static final ModelTemplate ORIENTABLE_TRAPDOOR_OPEN = create("template_orientable_trapdoor_open", "_open", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/*  66 */   public static final ModelTemplate CROSS = create("cross", new TextureSlot[] { TextureSlot.CROSS });
/*  67 */   public static final ModelTemplate TINTED_CROSS = create("tinted_cross", new TextureSlot[] { TextureSlot.CROSS });
/*     */   
/*  69 */   public static final ModelTemplate FLOWER_POT_CROSS = create("flower_pot_cross", new TextureSlot[] { TextureSlot.PLANT });
/*  70 */   public static final ModelTemplate TINTED_FLOWER_POT_CROSS = create("tinted_flower_pot_cross", new TextureSlot[] { TextureSlot.PLANT });
/*     */   
/*  72 */   public static final ModelTemplate RAIL_FLAT = create("rail_flat", new TextureSlot[] { TextureSlot.RAIL });
/*  73 */   public static final ModelTemplate RAIL_CURVED = create("rail_curved", "_corner", new TextureSlot[] { TextureSlot.RAIL });
/*  74 */   public static final ModelTemplate RAIL_RAISED_NE = create("template_rail_raised_ne", "_raised_ne", new TextureSlot[] { TextureSlot.RAIL });
/*  75 */   public static final ModelTemplate RAIL_RAISED_SW = create("template_rail_raised_sw", "_raised_sw", new TextureSlot[] { TextureSlot.RAIL });
/*     */   
/*  77 */   public static final ModelTemplate CARPET = create("carpet", new TextureSlot[] { TextureSlot.WOOL });
/*  78 */   public static final ModelTemplate CORAL_FAN = create("coral_fan", new TextureSlot[] { TextureSlot.FAN });
/*  79 */   public static final ModelTemplate CORAL_WALL_FAN = create("coral_wall_fan", new TextureSlot[] { TextureSlot.FAN });
/*  80 */   public static final ModelTemplate GLAZED_TERRACOTTA = create("template_glazed_terracotta", new TextureSlot[] { TextureSlot.PATTERN });
/*  81 */   public static final ModelTemplate CHORUS_FLOWER = create("template_chorus_flower", new TextureSlot[] { TextureSlot.TEXTURE });
/*  82 */   public static final ModelTemplate DAYLIGHT_DETECTOR = create("template_daylight_detector", new TextureSlot[] { TextureSlot.TOP, TextureSlot.SIDE });
/*     */   
/*  84 */   public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE = create("template_glass_pane_noside", "_noside", new TextureSlot[] { TextureSlot.PANE });
/*  85 */   public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE_ALT = create("template_glass_pane_noside_alt", "_noside_alt", new TextureSlot[] { TextureSlot.PANE });
/*  86 */   public static final ModelTemplate STAINED_GLASS_PANE_POST = create("template_glass_pane_post", "_post", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*  87 */   public static final ModelTemplate STAINED_GLASS_PANE_SIDE = create("template_glass_pane_side", "_side", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*  88 */   public static final ModelTemplate STAINED_GLASS_PANE_SIDE_ALT = create("template_glass_pane_side_alt", "_side_alt", new TextureSlot[] { TextureSlot.PANE, TextureSlot.EDGE });
/*     */   
/*  90 */   public static final ModelTemplate COMMAND_BLOCK = create("template_command_block", new TextureSlot[] { TextureSlot.FRONT, TextureSlot.BACK, TextureSlot.SIDE }); public static final ModelTemplate[] STEMS;
/*  91 */   public static final ModelTemplate ANVIL = create("template_anvil", new TextureSlot[] { TextureSlot.TOP }); static {
/*  92 */     STEMS = (ModelTemplate[])IntStream.range(0, 8).mapToObj(debug0 -> create("stem_growth" + debug0, "_stage" + debug0, new TextureSlot[] { TextureSlot.STEM })).toArray(debug0 -> new ModelTemplate[debug0]);
/*  93 */   } public static final ModelTemplate ATTACHED_STEM = create("stem_fruit", new TextureSlot[] { TextureSlot.STEM, TextureSlot.UPPER_STEM });
/*  94 */   public static final ModelTemplate CROP = create("crop", new TextureSlot[] { TextureSlot.CROP });
/*  95 */   public static final ModelTemplate FARMLAND = create("template_farmland", new TextureSlot[] { TextureSlot.DIRT, TextureSlot.TOP });
/*     */   
/*  97 */   public static final ModelTemplate FIRE_FLOOR = create("template_fire_floor", new TextureSlot[] { TextureSlot.FIRE });
/*  98 */   public static final ModelTemplate FIRE_SIDE = create("template_fire_side", new TextureSlot[] { TextureSlot.FIRE });
/*  99 */   public static final ModelTemplate FIRE_SIDE_ALT = create("template_fire_side_alt", new TextureSlot[] { TextureSlot.FIRE });
/* 100 */   public static final ModelTemplate FIRE_UP = create("template_fire_up", new TextureSlot[] { TextureSlot.FIRE });
/* 101 */   public static final ModelTemplate FIRE_UP_ALT = create("template_fire_up_alt", new TextureSlot[] { TextureSlot.FIRE });
/*     */   
/* 103 */   public static final ModelTemplate CAMPFIRE = create("template_campfire", new TextureSlot[] { TextureSlot.FIRE, TextureSlot.LIT_LOG });
/*     */   
/* 105 */   public static final ModelTemplate LANTERN = create("template_lantern", new TextureSlot[] { TextureSlot.LANTERN });
/* 106 */   public static final ModelTemplate HANGING_LANTERN = create("template_hanging_lantern", "_hanging", new TextureSlot[] { TextureSlot.LANTERN });
/*     */   
/* 108 */   public static final ModelTemplate TORCH = create("template_torch", new TextureSlot[] { TextureSlot.TORCH });
/* 109 */   public static final ModelTemplate WALL_TORCH = create("template_torch_wall", new TextureSlot[] { TextureSlot.TORCH });
/*     */   
/* 111 */   public static final ModelTemplate PISTON = create("template_piston", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.BOTTOM, TextureSlot.SIDE });
/* 112 */   public static final ModelTemplate PISTON_HEAD = create("template_piston_head", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY });
/* 113 */   public static final ModelTemplate PISTON_HEAD_SHORT = create("template_piston_head_short", new TextureSlot[] { TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY });
/* 114 */   public static final ModelTemplate SEAGRASS = create("template_seagrass", new TextureSlot[] { TextureSlot.TEXTURE });
/* 115 */   public static final ModelTemplate TURTLE_EGG = create("template_turtle_egg", new TextureSlot[] { TextureSlot.ALL });
/* 116 */   public static final ModelTemplate TWO_TURTLE_EGGS = create("template_two_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/* 117 */   public static final ModelTemplate THREE_TURTLE_EGGS = create("template_three_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/* 118 */   public static final ModelTemplate FOUR_TURTLE_EGGS = create("template_four_turtle_eggs", new TextureSlot[] { TextureSlot.ALL });
/* 119 */   public static final ModelTemplate SINGLE_FACE = create("template_single_face", new TextureSlot[] { TextureSlot.TEXTURE });
/*     */   
/* 121 */   public static final ModelTemplate FLAT_ITEM = createItem("generated", new TextureSlot[] { TextureSlot.LAYER0 });
/* 122 */   public static final ModelTemplate FLAT_HANDHELD_ITEM = createItem("handheld", new TextureSlot[] { TextureSlot.LAYER0 });
/* 123 */   public static final ModelTemplate FLAT_HANDHELD_ROD_ITEM = createItem("handheld_rod", new TextureSlot[] { TextureSlot.LAYER0 });
/* 124 */   public static final ModelTemplate SHULKER_BOX_INVENTORY = createItem("template_shulker_box", new TextureSlot[] { TextureSlot.PARTICLE });
/* 125 */   public static final ModelTemplate BED_INVENTORY = createItem("template_bed", new TextureSlot[] { TextureSlot.PARTICLE });
/* 126 */   public static final ModelTemplate BANNER_INVENTORY = createItem("template_banner", new TextureSlot[0]);
/* 127 */   public static final ModelTemplate SKULL_INVENTORY = createItem("template_skull", new TextureSlot[0]);
/*     */   
/*     */   private static ModelTemplate create(TextureSlot... debug0) {
/* 130 */     return new ModelTemplate(Optional.empty(), Optional.empty(), debug0);
/*     */   }
/*     */   
/*     */   private static ModelTemplate create(String debug0, TextureSlot... debug1) {
/* 134 */     return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + debug0)), Optional.empty(), debug1);
/*     */   }
/*     */   
/*     */   private static ModelTemplate createItem(String debug0, TextureSlot... debug1) {
/* 138 */     return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/" + debug0)), Optional.empty(), debug1);
/*     */   }
/*     */   
/*     */   private static ModelTemplate create(String debug0, String debug1, TextureSlot... debug2) {
/* 142 */     return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + debug0)), Optional.of(debug1), debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\ModelTemplates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */