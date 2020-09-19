/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockStateProperties
/*     */ {
/*  12 */   public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");
/*  13 */   public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
/*  14 */   public static final BooleanProperty CONDITIONAL = BooleanProperty.create("conditional");
/*  15 */   public static final BooleanProperty DISARMED = BooleanProperty.create("disarmed");
/*  16 */   public static final BooleanProperty DRAG = BooleanProperty.create("drag");
/*  17 */   public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");
/*  18 */   public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
/*  19 */   public static final BooleanProperty EYE = BooleanProperty.create("eye");
/*  20 */   public static final BooleanProperty FALLING = BooleanProperty.create("falling");
/*  21 */   public static final BooleanProperty HANGING = BooleanProperty.create("hanging");
/*  22 */   public static final BooleanProperty HAS_BOTTLE_0 = BooleanProperty.create("has_bottle_0");
/*  23 */   public static final BooleanProperty HAS_BOTTLE_1 = BooleanProperty.create("has_bottle_1");
/*  24 */   public static final BooleanProperty HAS_BOTTLE_2 = BooleanProperty.create("has_bottle_2");
/*  25 */   public static final BooleanProperty HAS_RECORD = BooleanProperty.create("has_record");
/*  26 */   public static final BooleanProperty HAS_BOOK = BooleanProperty.create("has_book");
/*  27 */   public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
/*  28 */   public static final BooleanProperty IN_WALL = BooleanProperty.create("in_wall");
/*  29 */   public static final BooleanProperty LIT = BooleanProperty.create("lit");
/*  30 */   public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
/*  31 */   public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");
/*  32 */   public static final BooleanProperty OPEN = BooleanProperty.create("open");
/*  33 */   public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");
/*  34 */   public static final BooleanProperty POWERED = BooleanProperty.create("powered");
/*  35 */   public static final BooleanProperty SHORT = BooleanProperty.create("short");
/*  36 */   public static final BooleanProperty SIGNAL_FIRE = BooleanProperty.create("signal_fire");
/*  37 */   public static final BooleanProperty SNOWY = BooleanProperty.create("snowy");
/*  38 */   public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");
/*  39 */   public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");
/*  40 */   public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");
/*  41 */   public static final BooleanProperty VINE_END = BooleanProperty.create("vine_end");
/*     */   
/*  43 */   public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = EnumProperty.create("axis", Direction.Axis.class, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z });
/*  44 */   public static final EnumProperty<Direction.Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class);
/*     */   
/*  46 */   public static final BooleanProperty UP = BooleanProperty.create("up");
/*  47 */   public static final BooleanProperty DOWN = BooleanProperty.create("down");
/*  48 */   public static final BooleanProperty NORTH = BooleanProperty.create("north");
/*  49 */   public static final BooleanProperty EAST = BooleanProperty.create("east");
/*  50 */   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
/*  51 */   public static final BooleanProperty WEST = BooleanProperty.create("west");
/*     */   public static final DirectionProperty FACING_HOPPER;
/*  53 */   public static final DirectionProperty FACING = DirectionProperty.create("facing", new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN }); static {
/*  54 */     FACING_HOPPER = DirectionProperty.create("facing", debug0 -> (debug0 != Direction.UP));
/*  55 */   } public static final DirectionProperty HORIZONTAL_FACING = DirectionProperty.create("facing", (Predicate<Direction>)Direction.Plane.HORIZONTAL);
/*     */   
/*  57 */   public static final EnumProperty<FrontAndTop> ORIENTATION = EnumProperty.create("orientation", FrontAndTop.class);
/*     */   
/*  59 */   public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);
/*  60 */   public static final EnumProperty<BellAttachType> BELL_ATTACHMENT = EnumProperty.create("attachment", BellAttachType.class);
/*     */   
/*  62 */   public static final EnumProperty<WallSide> EAST_WALL = EnumProperty.create("east", WallSide.class);
/*  63 */   public static final EnumProperty<WallSide> NORTH_WALL = EnumProperty.create("north", WallSide.class);
/*  64 */   public static final EnumProperty<WallSide> SOUTH_WALL = EnumProperty.create("south", WallSide.class);
/*  65 */   public static final EnumProperty<WallSide> WEST_WALL = EnumProperty.create("west", WallSide.class);
/*     */   
/*  67 */   public static final EnumProperty<RedstoneSide> EAST_REDSTONE = EnumProperty.create("east", RedstoneSide.class);
/*  68 */   public static final EnumProperty<RedstoneSide> NORTH_REDSTONE = EnumProperty.create("north", RedstoneSide.class);
/*  69 */   public static final EnumProperty<RedstoneSide> SOUTH_REDSTONE = EnumProperty.create("south", RedstoneSide.class);
/*  70 */   public static final EnumProperty<RedstoneSide> WEST_REDSTONE = EnumProperty.create("west", RedstoneSide.class);
/*     */   
/*  72 */   public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF = EnumProperty.create("half", DoubleBlockHalf.class);
/*  73 */   public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
/*     */   
/*  75 */   public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.create("shape", RailShape.class); static {
/*  76 */     RAIL_SHAPE_STRAIGHT = EnumProperty.create("shape", RailShape.class, debug0 -> 
/*  77 */         (debug0 != RailShape.NORTH_EAST && debug0 != RailShape.NORTH_WEST && debug0 != RailShape.SOUTH_EAST && debug0 != RailShape.SOUTH_WEST));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final IntegerProperty AGE_1 = IntegerProperty.create("age", 0, 1);
/*  91 */   public static final IntegerProperty AGE_2 = IntegerProperty.create("age", 0, 2);
/*  92 */   public static final IntegerProperty AGE_3 = IntegerProperty.create("age", 0, 3);
/*  93 */   public static final IntegerProperty AGE_5 = IntegerProperty.create("age", 0, 5);
/*  94 */   public static final IntegerProperty AGE_7 = IntegerProperty.create("age", 0, 7);
/*  95 */   public static final IntegerProperty AGE_15 = IntegerProperty.create("age", 0, 15);
/*  96 */   public static final IntegerProperty AGE_25 = IntegerProperty.create("age", 0, 25);
/*     */   
/*  98 */   public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
/*  99 */   public static final IntegerProperty DELAY = IntegerProperty.create("delay", 1, 4);
/*     */   
/* 101 */   public static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 7);
/* 102 */   public static final IntegerProperty EGGS = IntegerProperty.create("eggs", 1, 4);
/* 103 */   public static final IntegerProperty HATCH = IntegerProperty.create("hatch", 0, 2);
/* 104 */   public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static final IntegerProperty LEVEL_CAULDRON = IntegerProperty.create("level", 0, 3);
/* 109 */   public static final IntegerProperty LEVEL_COMPOSTER = IntegerProperty.create("level", 0, 8);
/* 110 */   public static final IntegerProperty LEVEL_FLOWING = IntegerProperty.create("level", 1, 8);
/* 111 */   public static final IntegerProperty LEVEL_HONEY = IntegerProperty.create("honey_level", 0, 5);
/* 112 */   public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);
/* 113 */   public static final IntegerProperty MOISTURE = IntegerProperty.create("moisture", 0, 7);
/* 114 */   public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 24);
/* 115 */   public static final IntegerProperty PICKLES = IntegerProperty.create("pickles", 1, 4);
/* 116 */   public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
/* 117 */   public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);
/*     */   
/* 119 */   public static final IntegerProperty STABILITY_DISTANCE = IntegerProperty.create("distance", 0, 7);
/*     */ 
/*     */   
/* 122 */   public static final IntegerProperty RESPAWN_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);
/*     */ 
/*     */   
/* 125 */   public static final IntegerProperty ROTATION_16 = IntegerProperty.create("rotation", 0, 15);
/*     */   
/* 127 */   public static final EnumProperty<BedPart> BED_PART = EnumProperty.create("part", BedPart.class);
/* 128 */   public static final EnumProperty<ChestType> CHEST_TYPE = EnumProperty.create("type", ChestType.class);
/* 129 */   public static final EnumProperty<ComparatorMode> MODE_COMPARATOR = EnumProperty.create("mode", ComparatorMode.class);
/* 130 */   public static final EnumProperty<DoorHingeSide> DOOR_HINGE = EnumProperty.create("hinge", DoorHingeSide.class);
/* 131 */   public static final EnumProperty<NoteBlockInstrument> NOTEBLOCK_INSTRUMENT = EnumProperty.create("instrument", NoteBlockInstrument.class);
/* 132 */   public static final EnumProperty<PistonType> PISTON_TYPE = EnumProperty.create("type", PistonType.class);
/* 133 */   public static final EnumProperty<SlabType> SLAB_TYPE = EnumProperty.create("type", SlabType.class);
/* 134 */   public static final EnumProperty<StairsShape> STAIRS_SHAPE = EnumProperty.create("shape", StairsShape.class);
/* 135 */   public static final EnumProperty<StructureMode> STRUCTUREBLOCK_MODE = EnumProperty.create("mode", StructureMode.class);
/* 136 */   public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES = EnumProperty.create("leaves", BambooLeaves.class);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\BlockStateProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */