/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockAgeProcessor extends StructureProcessor {
/*     */   static {
/*  18 */     CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockAgeProcessor::new, debug0 -> Float.valueOf(debug0.mossiness)).codec();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<BlockAgeProcessor> CODEC;
/*     */   private final float mossiness;
/*     */   
/*     */   public BlockAgeProcessor(float debug1) {
/*  26 */     this.mossiness = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/*  32 */     Random debug7 = debug6.getRandom(debug5.pos);
/*     */     
/*  34 */     BlockState debug8 = debug5.state;
/*  35 */     BlockPos debug9 = debug5.pos;
/*  36 */     BlockState debug10 = null;
/*  37 */     if (debug8.is(Blocks.STONE_BRICKS) || debug8.is(Blocks.STONE) || debug8.is(Blocks.CHISELED_STONE_BRICKS)) {
/*  38 */       debug10 = maybeReplaceFullStoneBlock(debug7);
/*  39 */     } else if (debug8.is((Tag)BlockTags.STAIRS)) {
/*  40 */       debug10 = maybeReplaceStairs(debug7, debug5.state);
/*  41 */     } else if (debug8.is((Tag)BlockTags.SLABS)) {
/*  42 */       debug10 = maybeReplaceSlab(debug7);
/*  43 */     } else if (debug8.is((Tag)BlockTags.WALLS)) {
/*  44 */       debug10 = maybeReplaceWall(debug7);
/*  45 */     } else if (debug8.is(Blocks.OBSIDIAN)) {
/*  46 */       debug10 = maybeReplaceObsidian(debug7);
/*     */     } 
/*  48 */     if (debug10 != null) {
/*  49 */       return new StructureTemplate.StructureBlockInfo(debug9, debug10, debug5.nbt);
/*     */     }
/*  51 */     return debug5;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockState maybeReplaceFullStoneBlock(Random debug1) {
/*  56 */     if (debug1.nextFloat() >= 0.5F) {
/*  57 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  61 */     BlockState[] debug2 = { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(debug1, Blocks.STONE_BRICK_STAIRS) };
/*     */ 
/*     */ 
/*     */     
/*  65 */     BlockState[] debug3 = { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(debug1, Blocks.MOSSY_STONE_BRICK_STAIRS) };
/*     */ 
/*     */     
/*  68 */     return getRandomBlock(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockState maybeReplaceStairs(Random debug1, BlockState debug2) {
/*  73 */     Direction debug3 = (Direction)debug2.getValue((Property)StairBlock.FACING);
/*  74 */     Half debug4 = (Half)debug2.getValue((Property)StairBlock.HALF);
/*     */     
/*  76 */     if (debug1.nextFloat() >= 0.5F) {
/*  77 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     BlockState[] debug5 = { Blocks.STONE_SLAB.defaultBlockState(), Blocks.STONE_BRICK_SLAB.defaultBlockState() };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     BlockState[] debug6 = { (BlockState)((BlockState)Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)debug3)).setValue((Property)StairBlock.HALF, (Comparable)debug4), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
/*     */ 
/*     */     
/*  90 */     return getRandomBlock(debug1, debug5, debug6);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockState maybeReplaceSlab(Random debug1) {
/*  95 */     if (debug1.nextFloat() < this.mossiness) {
/*  96 */       return Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState();
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockState maybeReplaceWall(Random debug1) {
/* 103 */     if (debug1.nextFloat() < this.mossiness) {
/* 104 */       return Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState();
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockState maybeReplaceObsidian(Random debug1) {
/* 111 */     if (debug1.nextFloat() < 0.15F) {
/* 112 */       return Blocks.CRYING_OBSIDIAN.defaultBlockState();
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockState getRandomFacingStairs(Random debug0, Block debug1) {
/* 118 */     return (BlockState)((BlockState)debug1.defaultBlockState()
/* 119 */       .setValue((Property)StairBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(debug0)))
/* 120 */       .setValue((Property)StairBlock.HALF, (Comparable)Half.values()[debug0.nextInt((Half.values()).length)]);
/*     */   }
/*     */   
/*     */   private BlockState getRandomBlock(Random debug1, BlockState[] debug2, BlockState[] debug3) {
/* 124 */     if (debug1.nextFloat() < this.mossiness) {
/* 125 */       return getRandomBlock(debug1, debug3);
/*     */     }
/* 127 */     return getRandomBlock(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockState getRandomBlock(Random debug0, BlockState[] debug1) {
/* 132 */     return debug1[debug0.nextInt(debug1.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected StructureProcessorType<?> getType() {
/* 137 */     return StructureProcessorType.BLOCK_AGE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockAgeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */