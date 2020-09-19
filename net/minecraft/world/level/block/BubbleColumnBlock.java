/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BubbleColumnBlock extends Block implements BucketPickup {
/*  28 */   public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;
/*     */ 
/*     */   
/*     */   public BubbleColumnBlock(BlockBehaviour.Properties debug1) {
/*  32 */     super(debug1);
/*  33 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DRAG_DOWN, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  38 */     BlockState debug5 = debug2.getBlockState(debug3.above());
/*  39 */     if (debug5.isAir()) {
/*  40 */       debug4.onAboveBubbleCol(((Boolean)debug1.getValue((Property)DRAG_DOWN)).booleanValue());
/*     */       
/*  42 */       if (!debug2.isClientSide) {
/*  43 */         ServerLevel debug6 = (ServerLevel)debug2;
/*  44 */         for (int debug7 = 0; debug7 < 2; debug7++) {
/*  45 */           debug6.sendParticles((ParticleOptions)ParticleTypes.SPLASH, debug3.getX() + debug2.random.nextDouble(), (debug3.getY() + 1), debug3.getZ() + debug2.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/*  46 */           debug6.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, debug3.getX() + debug2.random.nextDouble(), (debug3.getY() + 1), debug3.getZ() + debug2.random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
/*     */         } 
/*     */       } 
/*     */     } else {
/*  50 */       debug4.onInsideBubbleColumn(((Boolean)debug1.getValue((Property)DRAG_DOWN)).booleanValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  56 */     growColumn((LevelAccessor)debug2, debug3.above(), getDrag((BlockGetter)debug2, debug3.below()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  61 */     growColumn((LevelAccessor)debug2, debug3.above(), getDrag((BlockGetter)debug2, debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/*  66 */     return Fluids.WATER.getSource(false);
/*     */   }
/*     */   
/*     */   public static void growColumn(LevelAccessor debug0, BlockPos debug1, boolean debug2) {
/*  70 */     if (canExistIn(debug0, debug1)) {
/*  71 */       debug0.setBlock(debug1, (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(debug2)), 2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean canExistIn(LevelAccessor debug0, BlockPos debug1) {
/*  76 */     FluidState debug2 = debug0.getFluidState(debug1);
/*  77 */     return (debug0.getBlockState(debug1).is(Blocks.WATER) && debug2.getAmount() >= 8 && debug2.isSource());
/*     */   }
/*     */   
/*     */   private static boolean getDrag(BlockGetter debug0, BlockPos debug1) {
/*  81 */     BlockState debug2 = debug0.getBlockState(debug1);
/*     */     
/*  83 */     if (debug2.is(Blocks.BUBBLE_COLUMN)) {
/*  84 */       return ((Boolean)debug2.getValue((Property)DRAG_DOWN)).booleanValue();
/*     */     }
/*     */     
/*  87 */     return !debug2.is(Blocks.SOUL_SAND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 112 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 113 */       return Blocks.WATER.defaultBlockState();
/*     */     }
/*     */     
/* 116 */     if (debug2 == Direction.DOWN) {
/* 117 */       debug4.setBlock(debug5, (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(getDrag((BlockGetter)debug4, debug6))), 2);
/* 118 */     } else if (debug2 == Direction.UP && !debug3.is(Blocks.BUBBLE_COLUMN) && canExistIn(debug4, debug6)) {
/* 119 */       debug4.getBlockTicks().scheduleTick(debug5, this, 5);
/*     */     } 
/*     */     
/* 122 */     debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/* 123 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 128 */     BlockState debug4 = debug2.getBlockState(debug3.below());
/*     */     
/* 130 */     return (debug4.is(Blocks.BUBBLE_COLUMN) || debug4.is(Blocks.MAGMA_BLOCK) || debug4.is(Blocks.SOUL_SAND));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 135 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 140 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 145 */     debug1.add(new Property[] { (Property)DRAG_DOWN });
/*     */   }
/*     */ 
/*     */   
/*     */   public Fluid takeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/* 150 */     debug1.setBlock(debug2, Blocks.AIR.defaultBlockState(), 11);
/* 151 */     return (Fluid)Fluids.WATER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BubbleColumnBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */