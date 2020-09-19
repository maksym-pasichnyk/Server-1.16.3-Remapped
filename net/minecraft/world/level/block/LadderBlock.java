/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LadderBlock extends Block implements SimpleWaterloggedBlock {
/*  22 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  23 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  25 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
/*  26 */   protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  27 */   protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
/*  28 */   protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*     */   protected LadderBlock(BlockBehaviour.Properties debug1) {
/*  31 */     super(debug1);
/*  32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  37 */     switch ((Direction)debug1.getValue((Property)FACING)) {
/*     */       case NORTH:
/*  39 */         return NORTH_AABB;
/*     */       case SOUTH:
/*  41 */         return SOUTH_AABB;
/*     */       case WEST:
/*  43 */         return WEST_AABB;
/*     */     } 
/*     */     
/*  46 */     return EAST_AABB;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canAttachTo(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  51 */     BlockState debug4 = debug1.getBlockState(debug2);
/*  52 */     return debug4.isFaceSturdy(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  57 */     Direction debug4 = (Direction)debug1.getValue((Property)FACING);
/*  58 */     return canAttachTo((BlockGetter)debug2, debug3.relative(debug4.getOpposite()), debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  63 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  64 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  66 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  67 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/*  70 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  77 */     if (!debug1.replacingClickedOnBlock()) {
/*  78 */       BlockState blockState = debug1.getLevel().getBlockState(debug1.getClickedPos().relative(debug1.getClickedFace().getOpposite()));
/*  79 */       if (blockState.is(this) && blockState.getValue((Property)FACING) == debug1.getClickedFace()) {
/*  80 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*  84 */     BlockState debug2 = defaultBlockState();
/*     */     
/*  86 */     Level level = debug1.getLevel();
/*  87 */     BlockPos debug4 = debug1.getClickedPos();
/*  88 */     FluidState debug5 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/*  90 */     for (Direction debug9 : debug1.getNearestLookingDirections()) {
/*  91 */       if (debug9.getAxis().isHorizontal()) {
/*  92 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug9.getOpposite());
/*  93 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/*  94 */           return (BlockState)debug2.setValue((Property)WATERLOGGED, Boolean.valueOf((debug5.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 104 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 109 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 114 */     debug1.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 119 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 120 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 122 */     return super.getFluidState(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LadderBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */