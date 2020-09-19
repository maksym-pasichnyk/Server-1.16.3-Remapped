/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.data.BuiltinRegistries;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
/*     */ 
/*     */ public class ProcessorLists {
/*  24 */   private static final ProcessorRule ADD_GILDED_BLACKSTONE = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState());
/*  25 */   private static final ProcessorRule REMOVE_GILDED_BLACKSTONE = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GILDED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE.defaultBlockState());
/*     */   
/*  27 */   public static final StructureProcessorList EMPTY = register("empty", ImmutableList.of());
/*     */   
/*  29 */   public static final StructureProcessorList ZOMBIE_PLAINS = register("zombie_plains", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/*  30 */               .defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest((Tag)BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  31 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  32 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  33 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  34 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MOSSY_COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  35 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHITE_TERRACOTTA, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  36 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  37 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_PLANKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  38 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_STAIRS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  39 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_OAK_LOG, 0.02F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  40 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  41 */               .defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  42 */                   .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  43 */                   .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/*  44 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/*  45 */                 .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/*  46 */                 .defaultBlockState()) }))));
/*     */ 
/*     */   
/*  49 */   public static final StructureProcessorList ZOMBIE_SAVANNA = register("zombie_savanna", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest((Tag)BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  50 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  51 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  52 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  53 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_STAIRS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  54 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  55 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  56 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ORANGE_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  57 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.YELLOW_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  58 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.RED_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  59 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  60 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  61 */                 .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  62 */                   .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/*  63 */                 .defaultBlockState()) }))));
/*     */ 
/*     */   
/*  66 */   public static final StructureProcessorList ZOMBIE_SNOWY = register("zombie_snowy", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest((Tag)BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  67 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  68 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  69 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.LANTERN), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  70 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  71 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_SLAB, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  72 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  73 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  74 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  75 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  76 */                 .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  77 */                 .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/*  78 */               .defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/*  79 */                 .defaultBlockState()) }))));
/*     */ 
/*     */   
/*  82 */   public static final StructureProcessorList ZOMBIE_TAIGA = register("zombie_taiga", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/*  83 */               .defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest((Tag)BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  84 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  85 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  86 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.CAMPFIRE), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)Blocks.CAMPFIRE
/*  87 */               .defaultBlockState().setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  88 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_LOG, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  89 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/*  90 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  91 */                 .defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)Blocks.GLASS_PANE
/*  92 */                 .defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PUMPKIN_STEM
/*  93 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/*  94 */               .defaultBlockState()), (Object[])new ProcessorRule[0]))));
/*     */ 
/*     */   
/*  97 */   public static final StructureProcessorList ZOMBIE_DESERT = register("zombie_desert", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest((Tag)BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  98 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/*  99 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 100 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 101 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CUT_SANDSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 102 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.TERRACOTTA, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 103 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_STAIRS, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 104 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_SLAB, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.COBWEB
/* 105 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 106 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 107 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 110 */   public static final StructureProcessorList MOSSIFY_10_PERCENT = register("mossify_10_percent", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 111 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 114 */   public static final StructureProcessorList MOSSIFY_20_PERCENT = register("mossify_20_percent", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 115 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 118 */   public static final StructureProcessorList MOSSIFY_70_PERCENT = register("mossify_70_percent", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.7F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE
/* 119 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 122 */   public static final StructureProcessorList STREET_PLAINS = register("street_plains", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.OAK_PLANKS
/* 123 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRASS_PATH, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 124 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 125 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 126 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 129 */   public static final StructureProcessorList STREET_SAVANNA = register("street_savanna", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.ACACIA_PLANKS
/* 130 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRASS_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 131 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 132 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 133 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 136 */   public static final StructureProcessorList STREET_SNOWY_OR_TAIGA = register("street_snowy_or_taiga", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.SPRUCE_PLANKS
/* 137 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRASS_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GRASS_BLOCK
/* 138 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 139 */               .defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), Blocks.WATER
/* 140 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 143 */   public static final StructureProcessorList FARM_PLAINS = register("farm_plains", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 144 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 145 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 146 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 149 */   public static final StructureProcessorList FARM_SAVANNA = register("farm_savanna", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 150 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 153 */   public static final StructureProcessorList FARM_SNOWY = register("farm_snowy", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CARROTS
/* 154 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 155 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 158 */   public static final StructureProcessorList FARM_TAIGA = register("farm_taiga", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.PUMPKIN_STEM
/* 159 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POTATOES
/* 160 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 163 */   public static final StructureProcessorList FARM_DESERT = register("farm_desert", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BEETROOTS
/* 164 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.MELON_STEM
/* 165 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 168 */   public static final StructureProcessorList OUTPOST_ROT = register("outpost_rot", ImmutableList.of(new BlockRotProcessor(0.05F)));
/*     */   
/* 170 */   public static final StructureProcessorList BOTTOM_RAMPART = register("bottom_rampart", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MAGMA_BLOCK, 0.75F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 171 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.POLISHED_BLACKSTONE_BRICKS
/* 172 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static final StructureProcessorList TREASURE_ROOMS = register("treasure_rooms", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.35F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 178 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 179 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static final StructureProcessorList HOUSING = register("housing", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 185 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 186 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   public static final StructureProcessorList SIDE_WALL_DEGRADATION = register("side_wall_degradation", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 192 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 193 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   public static final StructureProcessorList STABLE_DEGRADATION = register("stable_degradation", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 199 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 200 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static final StructureProcessorList BASTION_GENERIC_DEGRADATION = register("bastion_generic_degradation", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 206 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 207 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 208 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   public static final StructureProcessorList RAMPART_DEGRADATION = register("rampart_degradation", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 214 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 215 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 216 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 217 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 218 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public static final StructureProcessorList ENTRANCE_REPLACEMENT = register("entrance_replacement", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 224 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.6F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 225 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 230 */   public static final StructureProcessorList BRIDGE = register("bridge", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 231 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 232 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 235 */   public static final StructureProcessorList ROOF = register("roof", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 236 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 237 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE
/* 238 */               .defaultBlockState())))));
/*     */ 
/*     */   
/* 241 */   public static final StructureProcessorList HIGH_WALL = register("high_wall", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.AIR
/* 242 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 243 */               .defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE
/* 244 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */   
/* 248 */   public static final StructureProcessorList HIGH_RAMPART = register("high_rampart", ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS
/* 249 */               .defaultBlockState()), new ProcessorRule((RuleTest)AlwaysTrueTest.INSTANCE, (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)new AxisAlignedLinearPosTest(0.0F, 0.05F, 0, 100, Direction.Axis.Y), Blocks.AIR
/* 250 */               .defaultBlockState()), REMOVE_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */   
/*     */   private static StructureProcessorList register(String debug0, ImmutableList<StructureProcessor> debug1) {
/* 255 */     ResourceLocation debug2 = new ResourceLocation(debug0);
/* 256 */     StructureProcessorList debug3 = new StructureProcessorList((List)debug1);
/* 257 */     return (StructureProcessorList)BuiltinRegistries.register(BuiltinRegistries.PROCESSOR_LIST, debug2, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\ProcessorLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */