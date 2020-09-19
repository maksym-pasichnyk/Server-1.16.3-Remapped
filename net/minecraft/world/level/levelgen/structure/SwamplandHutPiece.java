/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ 
/*     */ public class SwamplandHutPiece extends ScatteredFeaturePiece {
/*     */   private boolean spawnedWitch;
/*     */   
/*     */   public SwamplandHutPiece(Random debug1, int debug2, int debug3) {
/*  29 */     super(StructurePieceType.SWAMPLAND_HUT, debug1, debug2, 64, debug3, 7, 7, 9);
/*     */   }
/*     */   private boolean spawnedCat;
/*     */   public SwamplandHutPiece(StructureManager debug1, CompoundTag debug2) {
/*  33 */     super(StructurePieceType.SWAMPLAND_HUT, debug2);
/*  34 */     this.spawnedWitch = debug2.getBoolean("Witch");
/*  35 */     this.spawnedCat = debug2.getBoolean("Cat");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  40 */     super.addAdditionalSaveData(debug1);
/*  41 */     debug1.putBoolean("Witch", this.spawnedWitch);
/*  42 */     debug1.putBoolean("Cat", this.spawnedCat);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  47 */     if (!updateAverageGroundHeight((LevelAccessor)debug1, debug5, 0)) {
/*  48 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  52 */     generateBox(debug1, debug5, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  53 */     generateBox(debug1, debug5, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  54 */     generateBox(debug1, debug5, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  57 */     generateBox(debug1, debug5, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  58 */     generateBox(debug1, debug5, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  59 */     generateBox(debug1, debug5, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  60 */     generateBox(debug1, debug5, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  63 */     generateBox(debug1, debug5, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  64 */     generateBox(debug1, debug5, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  65 */     generateBox(debug1, debug5, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  66 */     generateBox(debug1, debug5, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*     */ 
/*     */     
/*  69 */     placeBlock(debug1, Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2, debug5);
/*  70 */     placeBlock(debug1, Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7, debug5);
/*  71 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 1, 3, 4, debug5);
/*  72 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 5, 3, 4, debug5);
/*  73 */     placeBlock(debug1, Blocks.AIR.defaultBlockState(), 5, 3, 5, debug5);
/*  74 */     placeBlock(debug1, Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5, debug5);
/*     */ 
/*     */     
/*  77 */     placeBlock(debug1, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6, debug5);
/*  78 */     placeBlock(debug1, Blocks.CAULDRON.defaultBlockState(), 4, 2, 6, debug5);
/*     */ 
/*     */     
/*  81 */     placeBlock(debug1, Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1, debug5);
/*  82 */     placeBlock(debug1, Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1, debug5);
/*     */ 
/*     */     
/*  85 */     BlockState debug8 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  86 */     BlockState debug9 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  87 */     BlockState debug10 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  88 */     BlockState debug11 = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*     */     
/*  90 */     generateBox(debug1, debug5, 0, 4, 1, 6, 4, 1, debug8, debug8, false);
/*  91 */     generateBox(debug1, debug5, 0, 4, 2, 0, 4, 7, debug9, debug9, false);
/*  92 */     generateBox(debug1, debug5, 6, 4, 2, 6, 4, 7, debug10, debug10, false);
/*  93 */     generateBox(debug1, debug5, 0, 4, 8, 6, 4, 8, debug11, debug11, false);
/*  94 */     placeBlock(debug1, (BlockState)debug8.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_RIGHT), 0, 4, 1, debug5);
/*  95 */     placeBlock(debug1, (BlockState)debug8.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_LEFT), 6, 4, 1, debug5);
/*  96 */     placeBlock(debug1, (BlockState)debug11.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_LEFT), 0, 4, 8, debug5);
/*  97 */     placeBlock(debug1, (BlockState)debug11.setValue((Property)StairBlock.SHAPE, (Comparable)StairsShape.OUTER_RIGHT), 6, 4, 8, debug5);
/*     */     
/*     */     int debug12;
/* 100 */     for (debug12 = 2; debug12 <= 7; debug12 += 5) {
/* 101 */       for (int i = 1; i <= 5; i += 4) {
/* 102 */         fillColumnDown(debug1, Blocks.OAK_LOG.defaultBlockState(), i, -1, debug12, debug5);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 107 */     debug12 = getWorldX(2, 5);
/* 108 */     int debug13 = getWorldY(2);
/* 109 */     int debug14 = getWorldZ(2, 5);
/*     */     
/* 111 */     if (!this.spawnedWitch && debug5.isInside((Vec3i)new BlockPos(debug12, debug13, debug14))) {
/* 112 */       this.spawnedWitch = true;
/*     */       
/* 114 */       Witch debug15 = (Witch)EntityType.WITCH.create((Level)debug1.getLevel());
/* 115 */       debug15.setPersistenceRequired();
/* 116 */       debug15.moveTo(debug12 + 0.5D, debug13, debug14 + 0.5D, 0.0F, 0.0F);
/* 117 */       debug15.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(new BlockPos(debug12, debug13, debug14)), MobSpawnType.STRUCTURE, null, null);
/* 118 */       debug1.addFreshEntityWithPassengers((Entity)debug15);
/*     */     } 
/*     */ 
/*     */     
/* 122 */     spawnCat((ServerLevelAccessor)debug1, debug5);
/*     */     
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnCat(ServerLevelAccessor debug1, BoundingBox debug2) {
/* 129 */     int debug3 = getWorldX(2, 5);
/* 130 */     int debug4 = getWorldY(2);
/* 131 */     int debug5 = getWorldZ(2, 5);
/*     */     
/* 133 */     if (!this.spawnedCat && debug2.isInside((Vec3i)new BlockPos(debug3, debug4, debug5))) {
/* 134 */       this.spawnedCat = true;
/*     */       
/* 136 */       Cat debug6 = (Cat)EntityType.CAT.create((Level)debug1.getLevel());
/* 137 */       debug6.setPersistenceRequired();
/* 138 */       debug6.moveTo(debug3 + 0.5D, debug4, debug5 + 0.5D, 0.0F, 0.0F);
/* 139 */       debug6.finalizeSpawn(debug1, debug1.getCurrentDifficultyAt(new BlockPos(debug3, debug4, debug5)), MobSpawnType.STRUCTURE, null, null);
/* 140 */       debug1.addFreshEntityWithPassengers((Entity)debug6);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\SwamplandHutPiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */