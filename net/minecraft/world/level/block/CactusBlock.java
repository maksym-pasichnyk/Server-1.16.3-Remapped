/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CactusBlock extends Block {
/*  25 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */ 
/*     */ 
/*     */   
/*  29 */   protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
/*  30 */   protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
/*     */   
/*     */   protected CactusBlock(BlockBehaviour.Properties debug1) {
/*  33 */     super(debug1);
/*  34 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  39 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/*  40 */       debug2.destroyBlock(debug3, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  46 */     BlockPos debug5 = debug3.above();
/*  47 */     if (!debug2.isEmptyBlock(debug5)) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     int debug6 = 1;
/*  52 */     while (debug2.getBlockState(debug3.below(debug6)).is(this)) {
/*  53 */       debug6++;
/*     */     }
/*     */ 
/*     */     
/*  57 */     if (debug6 >= 3) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     int debug7 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/*  62 */     if (debug7 == 15) {
/*  63 */       debug2.setBlockAndUpdate(debug5, defaultBlockState());
/*  64 */       BlockState debug8 = (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(0));
/*  65 */       debug2.setBlock(debug3, debug8, 4);
/*  66 */       debug8.neighborChanged((Level)debug2, debug5, this, debug3, false);
/*     */     } else {
/*  68 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug7 + 1)), 4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  74 */     return COLLISION_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  79 */     return OUTLINE_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  84 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  85 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*     */     
/*  88 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  93 */     for (Direction debug5 : Direction.Plane.HORIZONTAL) {
/*  94 */       BlockState debug6 = debug2.getBlockState(debug3.relative(debug5));
/*  95 */       Material debug7 = debug6.getMaterial();
/*     */       
/*  97 */       if (debug7.isSolid() || debug2.getFluidState(debug3.relative(debug5)).is((Tag)FluidTags.LAVA)) {
/*  98 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 102 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/* 103 */     return ((debug4.is(Blocks.CACTUS) || debug4.is(Blocks.SAND) || debug4.is(Blocks.RED_SAND)) && !debug2.getBlockState(debug3.above()).getMaterial().isLiquid());
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 108 */     debug4.hurt(DamageSource.CACTUS, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 113 */     debug1.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 118 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CactusBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */