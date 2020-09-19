/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceGateBlock extends HorizontalDirectionalBlock {
/*  24 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  25 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  26 */   public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;
/*     */   
/*  28 */   protected static final VoxelShape Z_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
/*  29 */   protected static final VoxelShape X_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
/*     */   
/*  31 */   protected static final VoxelShape Z_SHAPE_LOW = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 13.0D, 10.0D);
/*  32 */   protected static final VoxelShape X_SHAPE_LOW = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 13.0D, 16.0D);
/*     */   
/*  34 */   protected static final VoxelShape Z_COLLISION_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 24.0D, 10.0D);
/*  35 */   protected static final VoxelShape X_COLLISION_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 24.0D, 16.0D);
/*     */   
/*  37 */   protected static final VoxelShape Z_OCCLUSION_SHAPE = Shapes.or(
/*  38 */       Block.box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), 
/*  39 */       Block.box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D));
/*     */   
/*  41 */   protected static final VoxelShape X_OCCLUSION_SHAPE = Shapes.or(
/*  42 */       Block.box(7.0D, 5.0D, 0.0D, 9.0D, 16.0D, 2.0D), 
/*  43 */       Block.box(7.0D, 5.0D, 14.0D, 9.0D, 16.0D, 16.0D));
/*     */ 
/*     */   
/*  46 */   protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW = Shapes.or(
/*  47 */       Block.box(0.0D, 2.0D, 7.0D, 2.0D, 13.0D, 9.0D), 
/*  48 */       Block.box(14.0D, 2.0D, 7.0D, 16.0D, 13.0D, 9.0D));
/*     */   
/*  50 */   protected static final VoxelShape X_OCCLUSION_SHAPE_LOW = Shapes.or(
/*  51 */       Block.box(7.0D, 2.0D, 0.0D, 9.0D, 13.0D, 2.0D), 
/*  52 */       Block.box(7.0D, 2.0D, 14.0D, 9.0D, 13.0D, 16.0D));
/*     */ 
/*     */   
/*     */   public FenceGateBlock(BlockBehaviour.Properties debug1) {
/*  56 */     super(debug1);
/*     */     
/*  58 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)IN_WALL, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  63 */     if (((Boolean)debug1.getValue((Property)IN_WALL)).booleanValue()) {
/*  64 */       return (((Direction)debug1.getValue((Property)FACING)).getAxis() == Direction.Axis.X) ? X_SHAPE_LOW : Z_SHAPE_LOW;
/*     */     }
/*  66 */     return (((Direction)debug1.getValue((Property)FACING)).getAxis() == Direction.Axis.X) ? X_SHAPE : Z_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  71 */     Direction.Axis debug7 = debug2.getAxis();
/*  72 */     if (((Direction)debug1.getValue((Property)FACING)).getClockWise().getAxis() == debug7) {
/*  73 */       boolean debug8 = (isWall(debug3) || isWall(debug4.getBlockState(debug5.relative(debug2.getOpposite()))));
/*  74 */       return (BlockState)debug1.setValue((Property)IN_WALL, Boolean.valueOf(debug8));
/*     */     } 
/*     */     
/*  77 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  82 */     if (((Boolean)debug1.getValue((Property)OPEN)).booleanValue()) {
/*  83 */       return Shapes.empty();
/*     */     }
/*  85 */     return (((Direction)debug1.getValue((Property)FACING)).getAxis() == Direction.Axis.Z) ? Z_COLLISION_SHAPE : X_COLLISION_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getOcclusionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  90 */     if (((Boolean)debug1.getValue((Property)IN_WALL)).booleanValue()) {
/*  91 */       return (((Direction)debug1.getValue((Property)FACING)).getAxis() == Direction.Axis.X) ? X_OCCLUSION_SHAPE_LOW : Z_OCCLUSION_SHAPE_LOW;
/*     */     }
/*  93 */     return (((Direction)debug1.getValue((Property)FACING)).getAxis() == Direction.Axis.X) ? X_OCCLUSION_SHAPE : Z_OCCLUSION_SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/*  98 */     switch (debug4) {
/*     */       case LAND:
/* 100 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */       case WATER:
/* 102 */         return false;
/*     */       case AIR:
/* 104 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 112 */     Level debug2 = debug1.getLevel();
/* 113 */     BlockPos debug3 = debug1.getClickedPos();
/*     */     
/* 115 */     boolean debug4 = debug2.hasNeighborSignal(debug3);
/* 116 */     Direction debug5 = debug1.getHorizontalDirection();
/*     */     
/* 118 */     Direction.Axis debug6 = debug5.getAxis();
/*     */     
/* 120 */     boolean debug7 = ((debug6 == Direction.Axis.Z && (isWall(debug2.getBlockState(debug3.west())) || isWall(debug2.getBlockState(debug3.east())))) || (debug6 == Direction.Axis.X && (isWall(debug2.getBlockState(debug3.north())) || isWall(debug2.getBlockState(debug3.south())))));
/* 121 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug5)).setValue((Property)OPEN, Boolean.valueOf(debug4))).setValue((Property)POWERED, Boolean.valueOf(debug4))).setValue((Property)IN_WALL, Boolean.valueOf(debug7));
/*     */   }
/*     */   
/*     */   private boolean isWall(BlockState debug1) {
/* 125 */     return debug1.getBlock().is((Tag<Block>)BlockTags.WALLS);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 130 */     if (((Boolean)debug1.getValue((Property)OPEN)).booleanValue()) {
/* 131 */       debug1 = (BlockState)debug1.setValue((Property)OPEN, Boolean.valueOf(false));
/* 132 */       debug2.setBlock(debug3, debug1, 10);
/*     */     } else {
/*     */       
/* 135 */       Direction debug7 = debug4.getDirection();
/* 136 */       if (debug1.getValue((Property)FACING) == debug7.getOpposite()) {
/* 137 */         debug1 = (BlockState)debug1.setValue((Property)FACING, (Comparable)debug7);
/*     */       }
/* 139 */       debug1 = (BlockState)debug1.setValue((Property)OPEN, Boolean.valueOf(true));
/* 140 */       debug2.setBlock(debug3, debug1, 10);
/*     */     } 
/*     */     
/* 143 */     debug2.levelEvent(debug4, ((Boolean)debug1.getValue((Property)OPEN)).booleanValue() ? 1008 : 1014, debug3, 0);
/* 144 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 149 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 153 */     boolean debug7 = debug2.hasNeighborSignal(debug3);
/* 154 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue() != debug7) {
/* 155 */       debug2.setBlock(debug3, (BlockState)((BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug7))).setValue((Property)OPEN, Boolean.valueOf(debug7)), 2);
/* 156 */       if (((Boolean)debug1.getValue((Property)OPEN)).booleanValue() != debug7) {
/* 157 */         debug2.levelEvent(null, debug7 ? 1008 : 1014, debug3, 0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 164 */     debug1.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)POWERED, (Property)IN_WALL });
/*     */   }
/*     */   
/*     */   public static boolean connectsToDirection(BlockState debug0, Direction debug1) {
/* 168 */     return (((Direction)debug0.getValue((Property)FACING)).getAxis() == debug1.getClockWise().getAxis());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FenceGateBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */