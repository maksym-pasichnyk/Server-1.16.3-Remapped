/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class DesertPyramidPiece extends ScatteredFeaturePiece {
/*  20 */   private final boolean[] hasPlacedChest = new boolean[4];
/*     */   
/*     */   public DesertPyramidPiece(Random debug1, int debug2, int debug3) {
/*  23 */     super(StructurePieceType.DESERT_PYRAMID_PIECE, debug1, debug2, 64, debug3, 21, 15, 21);
/*     */   }
/*     */   
/*     */   public DesertPyramidPiece(StructureManager debug1, CompoundTag debug2) {
/*  27 */     super(StructurePieceType.DESERT_PYRAMID_PIECE, debug2);
/*  28 */     this.hasPlacedChest[0] = debug2.getBoolean("hasPlacedChest0");
/*  29 */     this.hasPlacedChest[1] = debug2.getBoolean("hasPlacedChest1");
/*  30 */     this.hasPlacedChest[2] = debug2.getBoolean("hasPlacedChest2");
/*  31 */     this.hasPlacedChest[3] = debug2.getBoolean("hasPlacedChest3");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  36 */     super.addAdditionalSaveData(debug1);
/*  37 */     debug1.putBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
/*  38 */     debug1.putBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
/*  39 */     debug1.putBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
/*  40 */     debug1.putBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  46 */     generateBox(debug1, debug5, 0, -4, 0, this.width - 1, 0, this.depth - 1, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false); int i;
/*  47 */     for (i = 1; i <= 9; i++) {
/*  48 */       generateBox(debug1, debug5, i, i, i, this.width - 1 - i, i, this.depth - 1 - i, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  49 */       generateBox(debug1, debug5, i + 1, i, i + 1, this.width - 2 - i, i, this.depth - 2 - i, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */     } 
/*  51 */     for (i = 0; i < this.width; i++) {
/*  52 */       for (int j = 0; j < this.depth; j++) {
/*  53 */         int k = -5;
/*  54 */         fillColumnDown(debug1, Blocks.SANDSTONE.defaultBlockState(), i, -5, j, debug5);
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     BlockState debug8 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  59 */     BlockState debug9 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*  60 */     BlockState debug10 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  61 */     BlockState debug11 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*     */ 
/*     */     
/*  64 */     generateBox(debug1, debug5, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  65 */     generateBox(debug1, debug5, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  66 */     placeBlock(debug1, debug8, 2, 10, 0, debug5);
/*  67 */     placeBlock(debug1, debug9, 2, 10, 4, debug5);
/*  68 */     placeBlock(debug1, debug10, 0, 10, 2, debug5);
/*  69 */     placeBlock(debug1, debug11, 4, 10, 2, debug5);
/*  70 */     generateBox(debug1, debug5, this.width - 5, 0, 0, this.width - 1, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  71 */     generateBox(debug1, debug5, this.width - 4, 10, 1, this.width - 2, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  72 */     placeBlock(debug1, debug8, this.width - 3, 10, 0, debug5);
/*  73 */     placeBlock(debug1, debug9, this.width - 3, 10, 4, debug5);
/*  74 */     placeBlock(debug1, debug10, this.width - 5, 10, 2, debug5);
/*  75 */     placeBlock(debug1, debug11, this.width - 1, 10, 2, debug5);
/*     */ 
/*     */     
/*  78 */     generateBox(debug1, debug5, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  79 */     generateBox(debug1, debug5, 9, 1, 0, 11, 3, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  80 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 1, 1, debug5);
/*  81 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 2, 1, debug5);
/*  82 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 3, 1, debug5);
/*  83 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, 3, 1, debug5);
/*  84 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 3, 1, debug5);
/*  85 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 2, 1, debug5);
/*  86 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 1, 1, debug5);
/*     */ 
/*     */     
/*  89 */     generateBox(debug1, debug5, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  90 */     generateBox(debug1, debug5, 4, 1, 2, 8, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  91 */     generateBox(debug1, debug5, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  92 */     generateBox(debug1, debug5, 12, 1, 2, 16, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */ 
/*     */     
/*  95 */     generateBox(debug1, debug5, 5, 4, 5, this.width - 6, 4, this.depth - 6, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  96 */     generateBox(debug1, debug5, 9, 4, 9, 11, 4, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  97 */     generateBox(debug1, debug5, 8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/*  98 */     generateBox(debug1, debug5, 12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/*  99 */     generateBox(debug1, debug5, 8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 100 */     generateBox(debug1, debug5, 12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/*     */ 
/*     */     
/* 103 */     generateBox(debug1, debug5, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 104 */     generateBox(debug1, debug5, this.width - 5, 1, 5, this.width - 2, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 105 */     generateBox(debug1, debug5, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 106 */     generateBox(debug1, debug5, this.width - 7, 7, 9, this.width - 7, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 107 */     generateBox(debug1, debug5, 5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 108 */     generateBox(debug1, debug5, this.width - 6, 5, 9, this.width - 6, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 109 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 5, 5, 10, debug5);
/* 110 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 5, 6, 10, debug5);
/* 111 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 6, 6, 10, debug5);
/* 112 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), this.width - 6, 5, 10, debug5);
/* 113 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), this.width - 6, 6, 10, debug5);
/* 114 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), this.width - 7, 6, 10, debug5);
/*     */ 
/*     */     
/* 117 */     generateBox(debug1, debug5, 2, 4, 4, 2, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 118 */     generateBox(debug1, debug5, this.width - 3, 4, 4, this.width - 3, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 119 */     placeBlock(debug1, debug8, 2, 4, 5, debug5);
/* 120 */     placeBlock(debug1, debug8, 2, 3, 4, debug5);
/* 121 */     placeBlock(debug1, debug8, this.width - 3, 4, 5, debug5);
/* 122 */     placeBlock(debug1, debug8, this.width - 3, 3, 4, debug5);
/* 123 */     generateBox(debug1, debug5, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 124 */     generateBox(debug1, debug5, this.width - 3, 1, 3, this.width - 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 125 */     placeBlock(debug1, Blocks.SANDSTONE.defaultBlockState(), 1, 1, 2, debug5);
/* 126 */     placeBlock(debug1, Blocks.SANDSTONE.defaultBlockState(), this.width - 2, 1, 2, debug5);
/* 127 */     placeBlock(debug1, Blocks.SANDSTONE_SLAB.defaultBlockState(), 1, 2, 2, debug5);
/* 128 */     placeBlock(debug1, Blocks.SANDSTONE_SLAB.defaultBlockState(), this.width - 2, 2, 2, debug5);
/* 129 */     placeBlock(debug1, debug11, 2, 1, 2, debug5);
/* 130 */     placeBlock(debug1, debug10, this.width - 3, 1, 2, debug5);
/*     */ 
/*     */     
/* 133 */     generateBox(debug1, debug5, 4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 134 */     generateBox(debug1, debug5, this.width - 5, 3, 5, this.width - 5, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 135 */     generateBox(debug1, debug5, 3, 1, 5, 4, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 136 */     generateBox(debug1, debug5, this.width - 6, 1, 5, this.width - 5, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false); int debug12;
/* 137 */     for (debug12 = 5; debug12 <= 17; debug12 += 2) {
/* 138 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 4, 1, debug12, debug5);
/* 139 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 4, 2, debug12, debug5);
/* 140 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), this.width - 5, 1, debug12, debug5);
/* 141 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), this.width - 5, 2, debug12, debug5);
/*     */     } 
/* 143 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 7, debug5);
/* 144 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 8, debug5);
/* 145 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 9, debug5);
/* 146 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 9, debug5);
/* 147 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 8, 0, 10, debug5);
/* 148 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 12, 0, 10, debug5);
/* 149 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 7, 0, 10, debug5);
/* 150 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 13, 0, 10, debug5);
/* 151 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 11, debug5);
/* 152 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 11, debug5);
/* 153 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 12, debug5);
/* 154 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 13, debug5);
/* 155 */     placeBlock(debug1, Blocks.BLUE_TERRACOTTA.defaultBlockState(), 10, 0, 10, debug5);
/*     */ 
/*     */     
/* 158 */     for (debug12 = 0; debug12 <= this.width - 1; debug12 += this.width - 1) {
/* 159 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 2, 1, debug5);
/* 160 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 2, 2, debug5);
/* 161 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 2, 3, debug5);
/* 162 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 3, 1, debug5);
/* 163 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 3, 2, debug5);
/* 164 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 3, 3, debug5);
/* 165 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 4, 1, debug5);
/* 166 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), debug12, 4, 2, debug5);
/* 167 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 4, 3, debug5);
/* 168 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 5, 1, debug5);
/* 169 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 5, 2, debug5);
/* 170 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 5, 3, debug5);
/* 171 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 6, 1, debug5);
/* 172 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), debug12, 6, 2, debug5);
/* 173 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 6, 3, debug5);
/* 174 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 7, 1, debug5);
/* 175 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 7, 2, debug5);
/* 176 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 7, 3, debug5);
/* 177 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 8, 1, debug5);
/* 178 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 8, 2, debug5);
/* 179 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 8, 3, debug5);
/*     */     } 
/* 181 */     for (debug12 = 2; debug12 <= this.width - 3; debug12 += this.width - 3 - 2) {
/* 182 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 - 1, 2, 0, debug5);
/* 183 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 2, 0, debug5);
/* 184 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 + 1, 2, 0, debug5);
/* 185 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 - 1, 3, 0, debug5);
/* 186 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 3, 0, debug5);
/* 187 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 + 1, 3, 0, debug5);
/* 188 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 - 1, 4, 0, debug5);
/* 189 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), debug12, 4, 0, debug5);
/* 190 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 + 1, 4, 0, debug5);
/* 191 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 - 1, 5, 0, debug5);
/* 192 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 5, 0, debug5);
/* 193 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 + 1, 5, 0, debug5);
/* 194 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 - 1, 6, 0, debug5);
/* 195 */       placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), debug12, 6, 0, debug5);
/* 196 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 + 1, 6, 0, debug5);
/* 197 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 - 1, 7, 0, debug5);
/* 198 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12, 7, 0, debug5);
/* 199 */       placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), debug12 + 1, 7, 0, debug5);
/* 200 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 - 1, 8, 0, debug5);
/* 201 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12, 8, 0, debug5);
/* 202 */       placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), debug12 + 1, 8, 0, debug5);
/*     */     } 
/* 204 */     generateBox(debug1, debug5, 8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 205 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 8, 6, 0, debug5);
/* 206 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 12, 6, 0, debug5);
/* 207 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 5, 0, debug5);
/* 208 */     placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, 5, 0, debug5);
/* 209 */     placeBlock(debug1, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 5, 0, debug5);
/*     */ 
/*     */     
/* 212 */     generateBox(debug1, debug5, 8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 213 */     generateBox(debug1, debug5, 8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(), false);
/* 214 */     generateBox(debug1, debug5, 8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 215 */     generateBox(debug1, debug5, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 216 */     generateBox(debug1, debug5, 9, -11, 9, 11, -1, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 217 */     placeBlock(debug1, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 10, -11, 10, debug5);
/* 218 */     generateBox(debug1, debug5, 9, -13, 9, 11, -13, 11, Blocks.TNT.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 219 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 8, -11, 10, debug5);
/* 220 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 8, -10, 10, debug5);
/* 221 */     placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 7, -10, 10, debug5);
/* 222 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 7, -11, 10, debug5);
/* 223 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 12, -11, 10, debug5);
/* 224 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 12, -10, 10, debug5);
/* 225 */     placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 13, -10, 10, debug5);
/* 226 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 13, -11, 10, debug5);
/* 227 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, -11, 8, debug5);
/* 228 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, -10, 8, debug5);
/* 229 */     placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 7, debug5);
/* 230 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 7, debug5);
/* 231 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, -11, 12, debug5);
/* 232 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 10, -10, 12, debug5);
/* 233 */     placeBlock(debug1, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 13, debug5);
/* 234 */     placeBlock(debug1, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 13, debug5);
/*     */ 
/*     */     
/* 237 */     for (Direction debug13 : Direction.Plane.HORIZONTAL) {
/* 238 */       if (!this.hasPlacedChest[debug13.get2DDataValue()]) {
/* 239 */         int debug14 = debug13.getStepX() * 2;
/* 240 */         int debug15 = debug13.getStepZ() * 2;
/* 241 */         this.hasPlacedChest[debug13.get2DDataValue()] = createChest(debug1, debug5, debug4, 10 + debug14, -11, 10 + debug15, BuiltInLootTables.DESERT_PYRAMID);
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\DesertPyramidPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */