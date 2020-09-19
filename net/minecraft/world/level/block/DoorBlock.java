/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Material;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DoorBlock extends Block {
/*  35 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  36 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  37 */   public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
/*  38 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  39 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
/*  44 */   protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
/*  45 */   protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  46 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
/*     */   
/*     */   protected DoorBlock(BlockBehaviour.Properties debug1) {
/*  49 */     super(debug1);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)HINGE, (Comparable)DoorHingeSide.LEFT)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  55 */     Direction debug5 = (Direction)debug1.getValue((Property)FACING);
/*  56 */     boolean debug6 = !((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*  57 */     boolean debug7 = (debug1.getValue((Property)HINGE) == DoorHingeSide.RIGHT);
/*     */     
/*  59 */     switch (debug5)
/*     */     
/*     */     { default:
/*  62 */         return debug6 ? EAST_AABB : (debug7 ? NORTH_AABB : SOUTH_AABB);
/*     */       case WATER:
/*  64 */         return debug6 ? SOUTH_AABB : (debug7 ? EAST_AABB : WEST_AABB);
/*     */       case AIR:
/*  66 */         return debug6 ? WEST_AABB : (debug7 ? SOUTH_AABB : NORTH_AABB);
/*     */       case null:
/*  68 */         break; }  return debug6 ? NORTH_AABB : (debug7 ? WEST_AABB : EAST_AABB);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  74 */     DoubleBlockHalf debug7 = (DoubleBlockHalf)debug1.getValue((Property)HALF);
/*  75 */     if (debug2.getAxis() == Direction.Axis.Y) if (((debug7 == DoubleBlockHalf.LOWER) ? true : false) == ((debug2 == Direction.UP) ? true : false)) {
/*  76 */         if (debug3.is(this) && debug3.getValue((Property)HALF) != debug7)
/*     */         {
/*  78 */           return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)FACING, debug3.getValue((Property)FACING)))
/*  79 */             .setValue((Property)OPEN, debug3.getValue((Property)OPEN)))
/*  80 */             .setValue((Property)HINGE, debug3.getValue((Property)HINGE)))
/*  81 */             .setValue((Property)POWERED, debug3.getValue((Property)POWERED));
/*     */         }
/*  83 */         return Blocks.AIR.defaultBlockState();
/*     */       } 
/*     */ 
/*     */     
/*  87 */     if (debug7 == DoubleBlockHalf.LOWER && debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  88 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  91 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/*  96 */     if (!debug1.isClientSide && debug4.isCreative()) {
/*  97 */       DoublePlantBlock.preventCreativeDropFromBottomPart(debug1, debug2, debug3, debug4);
/*     */     }
/*     */     
/* 100 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 105 */     switch (debug4) {
/*     */       case LAND:
/* 107 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */       case WATER:
/* 109 */         return false;
/*     */       case AIR:
/* 111 */         return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getCloseSound() {
/* 118 */     return (this.material == Material.METAL) ? 1011 : 1012;
/*     */   }
/*     */   
/*     */   private int getOpenSound() {
/* 122 */     return (this.material == Material.METAL) ? 1005 : 1006;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 128 */     BlockPos debug2 = debug1.getClickedPos();
/* 129 */     if (debug2.getY() < 255 && debug1.getLevel().getBlockState(debug2.above()).canBeReplaced(debug1)) {
/* 130 */       Level debug3 = debug1.getLevel();
/* 131 */       boolean debug4 = (debug3.hasNeighborSignal(debug2) || debug3.hasNeighborSignal(debug2.above()));
/*     */       
/* 133 */       return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection())).setValue((Property)HINGE, (Comparable)getHinge(debug1))).setValue((Property)POWERED, Boolean.valueOf(debug4))).setValue((Property)OPEN, Boolean.valueOf(debug4))).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 141 */     debug1.setBlock(debug2.above(), (BlockState)debug3.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
/*     */   }
/*     */   
/*     */   private DoorHingeSide getHinge(BlockPlaceContext debug1) {
/* 145 */     Level level = debug1.getLevel();
/* 146 */     BlockPos debug3 = debug1.getClickedPos();
/* 147 */     Direction debug4 = debug1.getHorizontalDirection();
/* 148 */     BlockPos debug5 = debug3.above();
/*     */     
/* 150 */     Direction debug6 = debug4.getCounterClockWise();
/* 151 */     BlockPos debug7 = debug3.relative(debug6);
/* 152 */     BlockState debug8 = level.getBlockState(debug7);
/* 153 */     BlockPos debug9 = debug5.relative(debug6);
/* 154 */     BlockState debug10 = level.getBlockState(debug9);
/*     */     
/* 156 */     Direction debug11 = debug4.getClockWise();
/* 157 */     BlockPos debug12 = debug3.relative(debug11);
/* 158 */     BlockState debug13 = level.getBlockState(debug12);
/* 159 */     BlockPos debug14 = debug5.relative(debug11);
/* 160 */     BlockState debug15 = level.getBlockState(debug14);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     int debug16 = (debug8.isCollisionShapeFullBlock((BlockGetter)level, debug7) ? -1 : 0) + (debug10.isCollisionShapeFullBlock((BlockGetter)level, debug9) ? -1 : 0) + (debug13.isCollisionShapeFullBlock((BlockGetter)level, debug12) ? 1 : 0) + (debug15.isCollisionShapeFullBlock((BlockGetter)level, debug14) ? 1 : 0);
/*     */     
/* 167 */     boolean debug17 = (debug8.is(this) && debug8.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/* 168 */     boolean debug18 = (debug13.is(this) && debug13.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */     
/* 170 */     if ((debug17 && !debug18) || debug16 > 0) {
/* 171 */       return DoorHingeSide.RIGHT;
/*     */     }
/* 173 */     if ((debug18 && !debug17) || debug16 < 0) {
/* 174 */       return DoorHingeSide.LEFT;
/*     */     }
/*     */     
/* 177 */     int debug19 = debug4.getStepX();
/* 178 */     int debug20 = debug4.getStepZ();
/*     */     
/* 180 */     Vec3 debug21 = debug1.getClickLocation();
/* 181 */     double debug22 = debug21.x - debug3.getX();
/* 182 */     double debug24 = debug21.z - debug3.getZ();
/*     */     
/* 184 */     return ((debug19 < 0 && debug24 < 0.5D) || (debug19 > 0 && debug24 > 0.5D) || (debug20 < 0 && debug22 > 0.5D) || (debug20 > 0 && debug22 < 0.5D)) ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 189 */     if (this.material == Material.METAL) {
/* 190 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 193 */     debug1 = (BlockState)debug1.cycle((Property)OPEN);
/* 194 */     debug2.setBlock(debug3, debug1, 10);
/* 195 */     debug2.levelEvent(debug4, ((Boolean)debug1.getValue((Property)OPEN)).booleanValue() ? getOpenSound() : getCloseSound(), debug3, 0);
/* 196 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen(BlockState debug1) {
/* 204 */     return ((Boolean)debug1.getValue((Property)OPEN)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setOpen(Level debug1, BlockState debug2, BlockPos debug3, boolean debug4) {
/* 208 */     if (!debug2.is(this) || ((Boolean)debug2.getValue((Property)OPEN)).booleanValue() == debug4) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     debug1.setBlock(debug3, (BlockState)debug2.setValue((Property)OPEN, Boolean.valueOf(debug4)), 10);
/* 213 */     playSound(debug1, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 218 */     boolean debug7 = (debug2.hasNeighborSignal(debug3) || debug2.hasNeighborSignal(debug3.relative((debug1.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? Direction.UP : Direction.DOWN)));
/* 219 */     if (debug4 != this && debug7 != ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 220 */       if (debug7 != ((Boolean)debug1.getValue((Property)OPEN)).booleanValue()) {
/* 221 */         playSound(debug2, debug3, debug7);
/*     */       }
/* 223 */       debug2.setBlock(debug3, (BlockState)((BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug7))).setValue((Property)OPEN, Boolean.valueOf(debug7)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 230 */     BlockPos debug4 = debug3.below();
/* 231 */     BlockState debug5 = debug2.getBlockState(debug4);
/* 232 */     if (debug1.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/* 233 */       return debug5.isFaceSturdy((BlockGetter)debug2, debug4, Direction.UP);
/*     */     }
/* 235 */     return debug5.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playSound(Level debug1, BlockPos debug2, boolean debug3) {
/* 240 */     debug1.levelEvent(null, debug3 ? getOpenSound() : getCloseSound(), debug2, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 245 */     return PushReaction.DESTROY;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 250 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 255 */     if (debug2 == Mirror.NONE) {
/* 256 */       return debug1;
/*     */     }
/* 258 */     return (BlockState)debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING))).cycle((Property)HINGE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 268 */     debug1.add(new Property[] { (Property)HALF, (Property)FACING, (Property)OPEN, (Property)HINGE, (Property)POWERED });
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(Level debug0, BlockPos debug1) {
/* 272 */     return isWoodenDoor(debug0.getBlockState(debug1));
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(BlockState debug0) {
/* 276 */     return (debug0.getBlock() instanceof DoorBlock && (debug0.getMaterial() == Material.WOOD || debug0.getMaterial() == Material.NETHER_WOOD));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DoorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */