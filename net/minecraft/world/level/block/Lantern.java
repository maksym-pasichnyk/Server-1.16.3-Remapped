/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class Lantern extends Block implements SimpleWaterloggedBlock {
/*  24 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*  25 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  27 */   protected static final VoxelShape AABB = Shapes.or(Block.box(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D), Block.box(6.0D, 7.0D, 6.0D, 10.0D, 9.0D, 10.0D));
/*  28 */   protected static final VoxelShape HANGING_AABB = Shapes.or(Block.box(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.box(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D));
/*     */   
/*     */   public Lantern(BlockBehaviour.Properties debug1) {
/*  31 */     super(debug1);
/*  32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HANGING, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  38 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/*  40 */     for (Direction debug6 : debug1.getNearestLookingDirections()) {
/*     */       
/*  42 */       if (debug6.getAxis() == Direction.Axis.Y) {
/*  43 */         BlockState debug7 = (BlockState)defaultBlockState().setValue((Property)HANGING, Boolean.valueOf((debug6 == Direction.UP)));
/*     */         
/*  45 */         if (debug7.canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/*  46 */           return (BlockState)debug7.setValue((Property)WATERLOGGED, Boolean.valueOf((debug2.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  56 */     return ((Boolean)debug1.getValue((Property)HANGING)).booleanValue() ? HANGING_AABB : AABB;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/*  61 */     debug1.add(new Property[] { (Property)HANGING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  66 */     Direction debug4 = getConnectedDirection(debug1).getOpposite();
/*  67 */     return Block.canSupportCenter(debug2, debug3.relative(debug4), debug4.getOpposite());
/*     */   }
/*     */   
/*     */   protected static Direction getConnectedDirection(BlockState debug0) {
/*  71 */     return ((Boolean)debug0.getValue((Property)HANGING)).booleanValue() ? Direction.DOWN : Direction.UP;
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/*  76 */     return PushReaction.DESTROY;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  81 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  82 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*  84 */     if (getConnectedDirection(debug1).getOpposite() == debug2 && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  85 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  87 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/*  92 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  93 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  95 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\Lantern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */