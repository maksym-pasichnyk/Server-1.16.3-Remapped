/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BellBlock extends BaseEntityBlock {
/*  37 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  38 */   public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
/*  39 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  41 */   private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 12.0D);
/*  42 */   private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
/*     */   
/*  44 */   private static final VoxelShape BELL_TOP_SHAPE = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D);
/*  45 */   private static final VoxelShape BELL_BOTTOM_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D);
/*  46 */   private static final VoxelShape BELL_SHAPE = Shapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);
/*     */   
/*  48 */   private static final VoxelShape NORTH_SOUTH_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 16.0D));
/*  49 */   private static final VoxelShape EAST_WEST_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
/*  50 */   private static final VoxelShape TO_WEST = Shapes.or(BELL_SHAPE, Block.box(0.0D, 13.0D, 7.0D, 13.0D, 15.0D, 9.0D));
/*  51 */   private static final VoxelShape TO_EAST = Shapes.or(BELL_SHAPE, Block.box(3.0D, 13.0D, 7.0D, 16.0D, 15.0D, 9.0D));
/*  52 */   private static final VoxelShape TO_NORTH = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 0.0D, 9.0D, 15.0D, 13.0D));
/*  53 */   private static final VoxelShape TO_SOUTH = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 3.0D, 9.0D, 15.0D, 16.0D));
/*  54 */   private static final VoxelShape CEILING_SHAPE = Shapes.or(BELL_SHAPE, Block.box(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D));
/*     */ 
/*     */ 
/*     */   
/*     */   public BellBlock(BlockBehaviour.Properties debug1) {
/*  59 */     super(debug1);
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ATTACHMENT, (Comparable)BellAttachType.FLOOR)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  65 */     boolean debug7 = debug2.hasNeighborSignal(debug3);
/*     */     
/*  67 */     if (debug7 != ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  68 */       if (debug7) {
/*  69 */         attemptToRing(debug2, debug3, (Direction)null);
/*     */       }
/*  71 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug7)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProjectileHit(Level debug1, BlockState debug2, BlockHitResult debug3, Projectile debug4) {
/*  77 */     Entity debug5 = debug4.getOwner();
/*  78 */     Player debug6 = (debug5 instanceof Player) ? (Player)debug5 : null;
/*  79 */     onHit(debug1, debug2, debug3, debug6, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  84 */     return onHit(debug2, debug1, debug6, debug4, true) ? InteractionResult.sidedSuccess(debug2.isClientSide) : InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public boolean onHit(Level debug1, BlockState debug2, BlockHitResult debug3, @Nullable Player debug4, boolean debug5) {
/*  88 */     Direction debug6 = debug3.getDirection();
/*  89 */     BlockPos debug7 = debug3.getBlockPos();
/*  90 */     boolean debug8 = (!debug5 || isProperHit(debug2, debug6, (debug3.getLocation()).y - debug7.getY()));
/*  91 */     if (debug8) {
/*  92 */       boolean debug9 = attemptToRing(debug1, debug7, debug6);
/*  93 */       if (debug9 && debug4 != null) {
/*  94 */         debug4.awardStat(Stats.BELL_RING);
/*     */       }
/*  96 */       return true;
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isProperHit(BlockState debug1, Direction debug2, double debug3) {
/* 102 */     if (debug2.getAxis() == Direction.Axis.Y || debug3 > 0.8123999834060669D) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     Direction debug5 = (Direction)debug1.getValue((Property)FACING);
/* 107 */     BellAttachType debug6 = (BellAttachType)debug1.getValue((Property)ATTACHMENT);
/*     */     
/* 109 */     switch (debug6) {
/*     */       case FLOOR:
/* 111 */         return (debug5.getAxis() == debug2.getAxis());
/*     */       case SINGLE_WALL:
/*     */       case DOUBLE_WALL:
/* 114 */         return (debug5.getAxis() != debug2.getAxis());
/*     */       case CEILING:
/* 116 */         return true;
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attemptToRing(Level debug1, BlockPos debug2, @Nullable Direction debug3) {
/* 123 */     BlockEntity debug4 = debug1.getBlockEntity(debug2);
/* 124 */     if (!debug1.isClientSide && debug4 instanceof BellBlockEntity) {
/* 125 */       if (debug3 == null) {
/* 126 */         debug3 = (Direction)debug1.getBlockState(debug2).getValue((Property)FACING);
/*     */       }
/* 128 */       ((BellBlockEntity)debug4).onHit(debug3);
/* 129 */       debug1.playSound(null, debug2, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
/* 130 */       return true;
/*     */     } 
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState debug1) {
/* 136 */     Direction debug2 = (Direction)debug1.getValue((Property)FACING);
/* 137 */     BellAttachType debug3 = (BellAttachType)debug1.getValue((Property)ATTACHMENT);
/*     */     
/* 139 */     if (debug3 == BellAttachType.FLOOR) {
/* 140 */       if (debug2 == Direction.NORTH || debug2 == Direction.SOUTH) {
/* 141 */         return NORTH_SOUTH_FLOOR_SHAPE;
/*     */       }
/* 143 */       return EAST_WEST_FLOOR_SHAPE;
/* 144 */     }  if (debug3 == BellAttachType.CEILING)
/* 145 */       return CEILING_SHAPE; 
/* 146 */     if (debug3 == BellAttachType.DOUBLE_WALL) {
/* 147 */       if (debug2 == Direction.NORTH || debug2 == Direction.SOUTH) {
/* 148 */         return NORTH_SOUTH_BETWEEN;
/*     */       }
/* 150 */       return EAST_WEST_BETWEEN;
/*     */     } 
/* 152 */     if (debug2 == Direction.NORTH)
/* 153 */       return TO_NORTH; 
/* 154 */     if (debug2 == Direction.SOUTH)
/* 155 */       return TO_SOUTH; 
/* 156 */     if (debug2 == Direction.EAST) {
/* 157 */       return TO_EAST;
/*     */     }
/* 159 */     return TO_WEST;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 165 */     return getVoxelShape(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 170 */     return getVoxelShape(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 175 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 182 */     Direction debug3 = debug1.getClickedFace();
/* 183 */     BlockPos debug4 = debug1.getClickedPos();
/* 184 */     Level debug5 = debug1.getLevel();
/* 185 */     Direction.Axis debug6 = debug3.getAxis();
/*     */     
/* 187 */     if (debug6 == Direction.Axis.Y) {
/* 188 */       BlockState debug2 = (BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHMENT, (debug3 == Direction.DOWN) ? (Comparable)BellAttachType.CEILING : (Comparable)BellAttachType.FLOOR)).setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection());
/*     */       
/* 190 */       if (debug2.canSurvive((LevelReader)debug1.getLevel(), debug4)) {
/* 191 */         return debug2;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 196 */       boolean debug7 = ((debug6 == Direction.Axis.X && debug5.getBlockState(debug4.west()).isFaceSturdy((BlockGetter)debug5, debug4.west(), Direction.EAST) && debug5.getBlockState(debug4.east()).isFaceSturdy((BlockGetter)debug5, debug4.east(), Direction.WEST)) || (debug6 == Direction.Axis.Z && debug5.getBlockState(debug4.north()).isFaceSturdy((BlockGetter)debug5, debug4.north(), Direction.SOUTH) && debug5.getBlockState(debug4.south()).isFaceSturdy((BlockGetter)debug5, debug4.south(), Direction.NORTH)));
/*     */       
/* 198 */       BlockState debug2 = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug3.getOpposite())).setValue((Property)ATTACHMENT, debug7 ? (Comparable)BellAttachType.DOUBLE_WALL : (Comparable)BellAttachType.SINGLE_WALL);
/*     */       
/* 200 */       if (debug2.canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/* 201 */         return debug2;
/*     */       }
/* 203 */       boolean debug8 = debug5.getBlockState(debug4.below()).isFaceSturdy((BlockGetter)debug5, debug4.below(), Direction.UP);
/*     */       
/* 205 */       debug2 = (BlockState)debug2.setValue((Property)ATTACHMENT, debug8 ? (Comparable)BellAttachType.FLOOR : (Comparable)BellAttachType.CEILING);
/*     */       
/* 207 */       if (debug2.canSurvive((LevelReader)debug1.getLevel(), debug1.getClickedPos())) {
/* 208 */         return debug2;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 218 */     BellAttachType debug7 = (BellAttachType)debug1.getValue((Property)ATTACHMENT);
/*     */     
/* 220 */     Direction debug8 = getConnectedDirection(debug1).getOpposite();
/* 221 */     if (debug8 == debug2 && !debug1.canSurvive((LevelReader)debug4, debug5) && debug7 != BellAttachType.DOUBLE_WALL) {
/* 222 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 225 */     if (debug2.getAxis() == ((Direction)debug1.getValue((Property)FACING)).getAxis()) {
/* 226 */       if (debug7 == BellAttachType.DOUBLE_WALL && !debug3.isFaceSturdy((BlockGetter)debug4, debug6, debug2))
/* 227 */         return (BlockState)((BlockState)debug1.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.SINGLE_WALL)).setValue((Property)FACING, (Comparable)debug2.getOpposite()); 
/* 228 */       if (debug7 == BellAttachType.SINGLE_WALL && debug8.getOpposite() == debug2 && debug3.isFaceSturdy((BlockGetter)debug4, debug6, (Direction)debug1.getValue((Property)FACING))) {
/* 229 */         return (BlockState)debug1.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.DOUBLE_WALL);
/*     */       }
/*     */     } 
/*     */     
/* 233 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 238 */     Direction debug4 = getConnectedDirection(debug1).getOpposite();
/*     */     
/* 240 */     if (debug4 == Direction.UP) {
/* 241 */       return Block.canSupportCenter(debug2, debug3.above(), Direction.DOWN);
/*     */     }
/* 243 */     return FaceAttachedHorizontalDirectionalBlock.canAttach(debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Direction getConnectedDirection(BlockState debug0) {
/* 248 */     switch ((BellAttachType)debug0.getValue((Property)ATTACHMENT)) {
/*     */       case CEILING:
/* 250 */         return Direction.DOWN;
/*     */       case FLOOR:
/* 252 */         return Direction.UP;
/*     */     } 
/* 254 */     return ((Direction)debug0.getValue((Property)FACING)).getOpposite();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 260 */     return PushReaction.DESTROY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 265 */     debug1.add(new Property[] { (Property)FACING, (Property)ATTACHMENT, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 271 */     return (BlockEntity)new BellBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 276 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BellBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */