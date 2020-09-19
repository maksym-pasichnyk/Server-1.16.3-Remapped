/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeverBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.RepeaterBlock;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.TripWireBlock;
/*     */ import net.minecraft.world.level.block.TripWireHookBlock;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.piston.PistonBaseBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class JunglePyramidPiece
/*     */   extends ScatteredFeaturePiece {
/*     */   private boolean placedMainChest;
/*     */   private boolean placedHiddenChest;
/*     */   
/*     */   public JunglePyramidPiece(Random debug1, int debug2, int debug3) {
/*  35 */     super(StructurePieceType.JUNGLE_PYRAMID_PIECE, debug1, debug2, 64, debug3, 12, 10, 15);
/*     */   }
/*     */   private boolean placedTrap1; private boolean placedTrap2;
/*     */   public JunglePyramidPiece(StructureManager debug1, CompoundTag debug2) {
/*  39 */     super(StructurePieceType.JUNGLE_PYRAMID_PIECE, debug2);
/*  40 */     this.placedMainChest = debug2.getBoolean("placedMainChest");
/*  41 */     this.placedHiddenChest = debug2.getBoolean("placedHiddenChest");
/*  42 */     this.placedTrap1 = debug2.getBoolean("placedTrap1");
/*  43 */     this.placedTrap2 = debug2.getBoolean("placedTrap2");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  48 */     super.addAdditionalSaveData(debug1);
/*  49 */     debug1.putBoolean("placedMainChest", this.placedMainChest);
/*  50 */     debug1.putBoolean("placedHiddenChest", this.placedHiddenChest);
/*  51 */     debug1.putBoolean("placedTrap1", this.placedTrap1);
/*  52 */     debug1.putBoolean("placedTrap2", this.placedTrap2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  57 */     if (!updateAverageGroundHeight((LevelAccessor)debug1, debug5, 0)) {
/*  58 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  62 */     generateBox(debug1, debug5, 0, -4, 0, this.width - 1, 0, this.depth - 1, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/*  65 */     generateBox(debug1, debug5, 2, 1, 2, 9, 2, 2, false, debug4, STONE_SELECTOR);
/*  66 */     generateBox(debug1, debug5, 2, 1, 12, 9, 2, 12, false, debug4, STONE_SELECTOR);
/*  67 */     generateBox(debug1, debug5, 2, 1, 3, 2, 2, 11, false, debug4, STONE_SELECTOR);
/*  68 */     generateBox(debug1, debug5, 9, 1, 3, 9, 2, 11, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/*  71 */     generateBox(debug1, debug5, 1, 3, 1, 10, 6, 1, false, debug4, STONE_SELECTOR);
/*  72 */     generateBox(debug1, debug5, 1, 3, 13, 10, 6, 13, false, debug4, STONE_SELECTOR);
/*  73 */     generateBox(debug1, debug5, 1, 3, 2, 1, 6, 12, false, debug4, STONE_SELECTOR);
/*  74 */     generateBox(debug1, debug5, 10, 3, 2, 10, 6, 12, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/*  77 */     generateBox(debug1, debug5, 2, 3, 2, 9, 3, 12, false, debug4, STONE_SELECTOR);
/*  78 */     generateBox(debug1, debug5, 2, 6, 2, 9, 6, 12, false, debug4, STONE_SELECTOR);
/*  79 */     generateBox(debug1, debug5, 3, 7, 3, 8, 7, 11, false, debug4, STONE_SELECTOR);
/*  80 */     generateBox(debug1, debug5, 4, 8, 4, 7, 8, 10, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/*  83 */     generateAirBox(debug1, debug5, 3, 1, 3, 8, 2, 11);
/*  84 */     generateAirBox(debug1, debug5, 4, 3, 6, 7, 3, 9);
/*  85 */     generateAirBox(debug1, debug5, 2, 4, 2, 9, 5, 12);
/*  86 */     generateAirBox(debug1, debug5, 4, 6, 5, 7, 6, 9);
/*  87 */     generateAirBox(debug1, debug5, 5, 7, 6, 6, 7, 8);
/*     */ 
/*     */     
/*  90 */     generateAirBox(debug1, debug5, 5, 1, 2, 6, 2, 2);
/*  91 */     generateAirBox(debug1, debug5, 5, 2, 12, 6, 2, 12);
/*  92 */     generateAirBox(debug1, debug5, 5, 5, 1, 6, 5, 1);
/*  93 */     generateAirBox(debug1, debug5, 5, 5, 13, 6, 5, 13);
/*  94 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 1, 5, 5, debug5);
/*  95 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, 5, 5, debug5);
/*  96 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 1, 5, 9, debug5);
/*  97 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, 5, 9, debug5);
/*     */     
/*     */     int i;
/* 100 */     for (i = 0; i <= 14; i += 14) {
/* 101 */       generateBox(debug1, debug5, 2, 4, i, 2, 5, i, false, debug4, STONE_SELECTOR);
/* 102 */       generateBox(debug1, debug5, 4, 4, i, 4, 5, i, false, debug4, STONE_SELECTOR);
/* 103 */       generateBox(debug1, debug5, 7, 4, i, 7, 5, i, false, debug4, STONE_SELECTOR);
/* 104 */       generateBox(debug1, debug5, 9, 4, i, 9, 5, i, false, debug4, STONE_SELECTOR);
/*     */     } 
/* 106 */     generateBox(debug1, debug5, 5, 6, 0, 6, 6, 0, false, debug4, STONE_SELECTOR);
/* 107 */     for (i = 0; i <= 11; i += 11) {
/* 108 */       for (int k = 2; k <= 12; k += 2) {
/* 109 */         generateBox(debug1, debug5, i, 4, k, i, 5, k, false, debug4, STONE_SELECTOR);
/*     */       }
/* 111 */       generateBox(debug1, debug5, i, 6, 5, i, 6, 5, false, debug4, STONE_SELECTOR);
/* 112 */       generateBox(debug1, debug5, i, 6, 9, i, 6, 9, false, debug4, STONE_SELECTOR);
/*     */     } 
/* 114 */     generateBox(debug1, debug5, 2, 7, 2, 2, 9, 2, false, debug4, STONE_SELECTOR);
/* 115 */     generateBox(debug1, debug5, 9, 7, 2, 9, 9, 2, false, debug4, STONE_SELECTOR);
/* 116 */     generateBox(debug1, debug5, 2, 7, 12, 2, 9, 12, false, debug4, STONE_SELECTOR);
/* 117 */     generateBox(debug1, debug5, 9, 7, 12, 9, 9, 12, false, debug4, STONE_SELECTOR);
/* 118 */     generateBox(debug1, debug5, 4, 9, 4, 4, 9, 4, false, debug4, STONE_SELECTOR);
/* 119 */     generateBox(debug1, debug5, 7, 9, 4, 7, 9, 4, false, debug4, STONE_SELECTOR);
/* 120 */     generateBox(debug1, debug5, 4, 9, 10, 4, 9, 10, false, debug4, STONE_SELECTOR);
/* 121 */     generateBox(debug1, debug5, 7, 9, 10, 7, 9, 10, false, debug4, STONE_SELECTOR);
/* 122 */     generateBox(debug1, debug5, 5, 9, 7, 6, 9, 7, false, debug4, STONE_SELECTOR);
/*     */     
/* 124 */     BlockState debug8 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/* 125 */     BlockState debug9 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/* 126 */     BlockState debug10 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 127 */     BlockState debug11 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*     */     
/* 129 */     placeBlock(debug1, debug11, 5, 9, 6, debug5);
/* 130 */     placeBlock(debug1, debug11, 6, 9, 6, debug5);
/* 131 */     placeBlock(debug1, debug10, 5, 9, 8, debug5);
/* 132 */     placeBlock(debug1, debug10, 6, 9, 8, debug5);
/*     */ 
/*     */     
/* 135 */     placeBlock(debug1, debug11, 4, 0, 0, debug5);
/* 136 */     placeBlock(debug1, debug11, 5, 0, 0, debug5);
/* 137 */     placeBlock(debug1, debug11, 6, 0, 0, debug5);
/* 138 */     placeBlock(debug1, debug11, 7, 0, 0, debug5);
/*     */ 
/*     */     
/* 141 */     placeBlock(debug1, debug11, 4, 1, 8, debug5);
/* 142 */     placeBlock(debug1, debug11, 4, 2, 9, debug5);
/* 143 */     placeBlock(debug1, debug11, 4, 3, 10, debug5);
/* 144 */     placeBlock(debug1, debug11, 7, 1, 8, debug5);
/* 145 */     placeBlock(debug1, debug11, 7, 2, 9, debug5);
/* 146 */     placeBlock(debug1, debug11, 7, 3, 10, debug5);
/* 147 */     generateBox(debug1, debug5, 4, 1, 9, 4, 1, 9, false, debug4, STONE_SELECTOR);
/* 148 */     generateBox(debug1, debug5, 7, 1, 9, 7, 1, 9, false, debug4, STONE_SELECTOR);
/* 149 */     generateBox(debug1, debug5, 4, 1, 10, 7, 2, 10, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/* 152 */     generateBox(debug1, debug5, 5, 4, 5, 6, 4, 5, false, debug4, STONE_SELECTOR);
/* 153 */     placeBlock(debug1, debug8, 4, 4, 5, debug5);
/* 154 */     placeBlock(debug1, debug9, 7, 4, 5, debug5);
/*     */     
/*     */     int j;
/* 157 */     for (j = 0; j < 4; j++) {
/* 158 */       placeBlock(debug1, debug10, 5, 0 - j, 6 + j, debug5);
/* 159 */       placeBlock(debug1, debug10, 6, 0 - j, 6 + j, debug5);
/* 160 */       generateAirBox(debug1, debug5, 5, 0 - j, 7 + j, 6, 0 - j, 9 + j);
/*     */     } 
/*     */ 
/*     */     
/* 164 */     generateAirBox(debug1, debug5, 1, -3, 12, 10, -1, 13);
/* 165 */     generateAirBox(debug1, debug5, 1, -3, 1, 3, -1, 13);
/* 166 */     generateAirBox(debug1, debug5, 1, -3, 1, 9, -1, 5);
/* 167 */     for (j = 1; j <= 13; j += 2) {
/* 168 */       generateBox(debug1, debug5, 1, -3, j, 1, -2, j, false, debug4, STONE_SELECTOR);
/*     */     }
/* 170 */     for (j = 2; j <= 12; j += 2) {
/* 171 */       generateBox(debug1, debug5, 1, -1, j, 3, -1, j, false, debug4, STONE_SELECTOR);
/*     */     }
/* 173 */     generateBox(debug1, debug5, 2, -2, 1, 5, -2, 1, false, debug4, STONE_SELECTOR);
/* 174 */     generateBox(debug1, debug5, 7, -2, 1, 9, -2, 1, false, debug4, STONE_SELECTOR);
/* 175 */     generateBox(debug1, debug5, 6, -3, 1, 6, -3, 1, false, debug4, STONE_SELECTOR);
/* 176 */     generateBox(debug1, debug5, 6, -1, 1, 6, -1, 1, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/* 179 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.EAST)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, debug5);
/* 180 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.WEST)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, debug5);
/* 181 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.EAST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.WEST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, debug5);
/* 182 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.EAST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.WEST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, debug5);
/* 183 */     BlockState debug12 = (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE);
/* 184 */     placeBlock(debug1, debug12, 5, -3, 7, debug5);
/* 185 */     placeBlock(debug1, debug12, 5, -3, 6, debug5);
/* 186 */     placeBlock(debug1, debug12, 5, -3, 5, debug5);
/* 187 */     placeBlock(debug1, debug12, 5, -3, 4, debug5);
/* 188 */     placeBlock(debug1, debug12, 5, -3, 3, debug5);
/* 189 */     placeBlock(debug1, debug12, 5, -3, 2, debug5);
/* 190 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 5, -3, 1, debug5);
/* 191 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 4, -3, 1, debug5);
/* 192 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3, -3, 1, debug5);
/* 193 */     if (!this.placedTrap1) {
/* 194 */       this.placedTrap1 = createDispenser(debug1, debug5, debug4, 3, -2, 1, Direction.NORTH, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 196 */     placeBlock(debug1, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.SOUTH, Boolean.valueOf(true)), 3, -2, 2, debug5);
/*     */ 
/*     */     
/* 199 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, debug5);
/* 200 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.SOUTH)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, debug5);
/* 201 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, debug5);
/* 202 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, debug5);
/* 203 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, debug5);
/* 204 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 8, -3, 6, debug5);
/* 205 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE), 9, -3, 6, debug5);
/* 206 */     placeBlock(debug1, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.UP), 9, -3, 5, debug5);
/* 207 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 4, debug5);
/* 208 */     placeBlock(debug1, debug12, 9, -2, 4, debug5);
/* 209 */     if (!this.placedTrap2) {
/* 210 */       this.placedTrap2 = createDispenser(debug1, debug5, debug4, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 212 */     placeBlock(debug1, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.EAST, Boolean.valueOf(true)), 8, -1, 3, debug5);
/* 213 */     placeBlock(debug1, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.EAST, Boolean.valueOf(true)), 8, -2, 3, debug5);
/* 214 */     if (!this.placedMainChest) {
/* 215 */       this.placedMainChest = createChest(debug1, debug5, debug4, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
/*     */     }
/* 217 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 2, debug5);
/* 218 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 1, debug5);
/* 219 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 4, -3, 5, debug5);
/* 220 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -2, 5, debug5);
/* 221 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -1, 5, debug5);
/* 222 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 6, -3, 5, debug5);
/* 223 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -2, 5, debug5);
/* 224 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -1, 5, debug5);
/* 225 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 5, debug5);
/* 226 */     generateBox(debug1, debug5, 9, -1, 1, 9, -1, 5, false, debug4, STONE_SELECTOR);
/*     */ 
/*     */     
/* 229 */     generateAirBox(debug1, debug5, 8, -3, 8, 10, -1, 10);
/* 230 */     placeBlock(debug1, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 8, -2, 11, debug5);
/* 231 */     placeBlock(debug1, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 9, -2, 11, debug5);
/* 232 */     placeBlock(debug1, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 10, -2, 11, debug5);
/* 233 */     BlockState debug13 = (BlockState)((BlockState)Blocks.LEVER.defaultBlockState().setValue((Property)LeverBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)LeverBlock.FACE, (Comparable)AttachFace.WALL);
/* 234 */     placeBlock(debug1, debug13, 8, -2, 12, debug5);
/* 235 */     placeBlock(debug1, debug13, 9, -2, 12, debug5);
/* 236 */     placeBlock(debug1, debug13, 10, -2, 12, debug5);
/* 237 */     generateBox(debug1, debug5, 8, -3, 8, 8, -3, 10, false, debug4, STONE_SELECTOR);
/* 238 */     generateBox(debug1, debug5, 10, -3, 8, 10, -3, 10, false, debug4, STONE_SELECTOR);
/* 239 */     placeBlock(debug1, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 10, -2, 9, debug5);
/* 240 */     placeBlock(debug1, debug12, 8, -2, 9, debug5);
/* 241 */     placeBlock(debug1, debug12, 8, -2, 10, debug5);
/* 242 */     placeBlock(debug1, (BlockState)((BlockState)((BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 10, -1, 9, debug5);
/* 243 */     placeBlock(debug1, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.UP), 9, -2, 8, debug5);
/* 244 */     placeBlock(debug1, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.WEST), 10, -2, 8, debug5);
/* 245 */     placeBlock(debug1, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.WEST), 10, -1, 8, debug5);
/* 246 */     placeBlock(debug1, (BlockState)Blocks.REPEATER.defaultBlockState().setValue((Property)RepeaterBlock.FACING, (Comparable)Direction.NORTH), 10, -2, 10, debug5);
/* 247 */     if (!this.placedHiddenChest) {
/* 248 */       this.placedHiddenChest = createChest(debug1, debug5, debug4, 9, -3, 10, BuiltInLootTables.JUNGLE_TEMPLE);
/*     */     }
/*     */     
/* 251 */     return true;
/*     */   }
/*     */   
/*     */   static class MossStoneSelector extends StructurePiece.BlockSelector { private MossStoneSelector() {}
/*     */     
/*     */     public void next(Random debug1, int debug2, int debug3, int debug4, boolean debug5) {
/* 257 */       if (debug1.nextFloat() < 0.4F) {
/* 258 */         this.next = Blocks.COBBLESTONE.defaultBlockState();
/*     */       } else {
/* 260 */         this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/* 265 */   private static final MossStoneSelector STONE_SELECTOR = new MossStoneSelector();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\JunglePyramidPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */