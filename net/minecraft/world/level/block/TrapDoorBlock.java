/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*  29 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  30 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  31 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  32 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */ 
/*     */   
/*  35 */   protected static final VoxelShape EAST_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
/*  36 */   protected static final VoxelShape WEST_OPEN_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  37 */   protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
/*  38 */   protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
/*  39 */   protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
/*  40 */   protected static final VoxelShape TOP_AABB = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*     */   protected TrapDoorBlock(BlockBehaviour.Properties debug1) {
/*  43 */     super(debug1);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  49 */     if (!((Boolean)debug1.getValue((Property)OPEN)).booleanValue()) {
/*  50 */       return (debug1.getValue((Property)HALF) == Half.TOP) ? TOP_AABB : BOTTOM_AABB;
/*     */     }
/*     */     
/*  53 */     switch ((Direction)debug1.getValue((Property)FACING))
/*     */     
/*     */     { default:
/*  56 */         return NORTH_OPEN_AABB;
/*     */       case WATER:
/*  58 */         return SOUTH_OPEN_AABB;
/*     */       case AIR:
/*  60 */         return WEST_OPEN_AABB;
/*     */       case null:
/*  62 */         break; }  return EAST_OPEN_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  68 */     switch (debug4) {
/*     */       case LAND:
/*  70 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */       case WATER:
/*  72 */         return ((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue();
/*     */       case AIR:
/*  74 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  82 */     if (this.material == Material.METAL) {
/*  83 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  86 */     debug1 = (BlockState)debug1.cycle((Property)OPEN);
/*  87 */     debug2.setBlock(debug3, debug1, 2);
/*     */     
/*  89 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  90 */       debug2.getLiquidTicks().scheduleTick(debug3, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug2));
/*     */     }
/*     */     
/*  93 */     playSound(debug4, debug2, debug3, ((Boolean)debug1.getValue((Property)OPEN)).booleanValue());
/*  94 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */   
/*     */   protected void playSound(@Nullable Player debug1, Level debug2, BlockPos debug3, boolean debug4) {
/*  98 */     if (debug4) {
/*  99 */       int debug5 = (this.material == Material.METAL) ? 1037 : 1007;
/* 100 */       debug2.levelEvent(debug1, debug5, debug3, 0);
/*     */     } else {
/* 102 */       int debug5 = (this.material == Material.METAL) ? 1036 : 1013;
/* 103 */       debug2.levelEvent(debug1, debug5, debug3, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 109 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 113 */     boolean debug7 = debug2.hasNeighborSignal(debug3);
/* 114 */     if (debug7 != ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 115 */       if (((Boolean)debug1.getValue((Property)OPEN)).booleanValue() != debug7) {
/* 116 */         debug1 = (BlockState)debug1.setValue((Property)OPEN, Boolean.valueOf(debug7));
/* 117 */         playSound((Player)null, debug2, debug3, debug7);
/*     */       } 
/* 119 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug7)), 2);
/*     */       
/* 121 */       if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 122 */         debug2.getLiquidTicks().scheduleTick(debug3, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug2));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 129 */     BlockState debug2 = defaultBlockState();
/* 130 */     FluidState debug3 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/* 132 */     Direction debug4 = debug1.getClickedFace();
/* 133 */     if (debug1.replacingClickedOnBlock() || !debug4.getAxis().isHorizontal()) {
/* 134 */       debug2 = (BlockState)((BlockState)debug2.setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite())).setValue((Property)HALF, (debug4 == Direction.UP) ? (Comparable)Half.BOTTOM : (Comparable)Half.TOP);
/*     */     } else {
/* 136 */       debug2 = (BlockState)((BlockState)debug2.setValue((Property)FACING, (Comparable)debug4)).setValue((Property)HALF, ((debug1.getClickLocation()).y - debug1.getClickedPos().getY() > 0.5D) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM);
/*     */     } 
/* 138 */     if (debug1.getLevel().hasNeighborSignal(debug1.getClickedPos())) {
/* 139 */       debug2 = (BlockState)((BlockState)debug2.setValue((Property)OPEN, Boolean.valueOf(true))).setValue((Property)POWERED, Boolean.valueOf(true));
/*     */     }
/* 141 */     return (BlockState)debug2.setValue((Property)WATERLOGGED, Boolean.valueOf((debug3.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 146 */     debug1.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)HALF, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 151 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 152 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 154 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 159 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 160 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/* 163 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TrapDoorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */