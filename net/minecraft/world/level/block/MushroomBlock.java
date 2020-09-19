/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.data.worldgen.Features;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MushroomBlock extends BushBlock implements BonemealableBlock {
/*  19 */   protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
/*     */   
/*     */   public MushroomBlock(BlockBehaviour.Properties debug1) {
/*  22 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  27 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  32 */     if (debug4.nextInt(25) == 0) {
/*  33 */       int debug5 = 5;
/*  34 */       int debug6 = 4;
/*  35 */       for (BlockPos blockPos : BlockPos.betweenClosed(debug3.offset(-4, -1, -4), debug3.offset(4, 1, 4))) {
/*  36 */         if (debug2.getBlockState(blockPos).is(this) && --debug5 <= 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  42 */       BlockPos debug7 = debug3.offset(debug4.nextInt(3) - 1, debug4.nextInt(2) - debug4.nextInt(2), debug4.nextInt(3) - 1);
/*  43 */       for (int debug8 = 0; debug8 < 4; debug8++) {
/*  44 */         if (debug2.isEmptyBlock(debug7) && debug1.canSurvive((LevelReader)debug2, debug7)) {
/*  45 */           debug3 = debug7;
/*     */         }
/*  47 */         debug7 = debug3.offset(debug4.nextInt(3) - 1, debug4.nextInt(2) - debug4.nextInt(2), debug4.nextInt(3) - 1);
/*     */       } 
/*     */       
/*  50 */       if (debug2.isEmptyBlock(debug7) && debug1.canSurvive((LevelReader)debug2, debug7)) {
/*  51 */         debug2.setBlock(debug7, debug1, 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  58 */     return debug1.isSolidRender(debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  63 */     BlockPos debug4 = debug3.below();
/*  64 */     BlockState debug5 = debug2.getBlockState(debug4);
/*  65 */     if (debug5.is((Tag)BlockTags.MUSHROOM_GROW_BLOCK)) {
/*  66 */       return true;
/*     */     }
/*     */     
/*  69 */     return (debug2.getRawBrightness(debug3, 0) < 13 && mayPlaceOn(debug5, (BlockGetter)debug2, debug4));
/*     */   }
/*     */   public boolean growMushroom(ServerLevel debug1, BlockPos debug2, BlockState debug3, Random debug4) {
/*     */     ConfiguredFeature<?, ?> debug5;
/*  73 */     debug1.removeBlock(debug2, false);
/*     */ 
/*     */     
/*  76 */     if (this == Blocks.BROWN_MUSHROOM) {
/*  77 */       debug5 = Features.HUGE_BROWN_MUSHROOM;
/*  78 */     } else if (this == Blocks.RED_MUSHROOM) {
/*  79 */       debug5 = Features.HUGE_RED_MUSHROOM;
/*     */     } else {
/*  81 */       debug1.setBlock(debug2, debug3, 3);
/*  82 */       return false;
/*     */     } 
/*     */     
/*  85 */     if (debug5.place((WorldGenLevel)debug1, debug1.getChunkSource().getGenerator(), debug4, debug2)) {
/*  86 */       return true;
/*     */     }
/*     */     
/*  89 */     debug1.setBlock(debug2, debug3, 3);
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 100 */     return (debug2.nextFloat() < 0.4D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 105 */     growMushroom(debug1, debug3, debug4, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\MushroomBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */