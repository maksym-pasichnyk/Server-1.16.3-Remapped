/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StairBlock extends Block implements SimpleWaterloggedBlock {
/*  35 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  36 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  37 */   public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
/*  38 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  40 */   protected static final VoxelShape TOP_AABB = SlabBlock.TOP_AABB;
/*  41 */   protected static final VoxelShape BOTTOM_AABB = SlabBlock.BOTTOM_AABB;
/*     */   
/*  43 */   protected static final VoxelShape OCTET_NNN = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
/*  44 */   protected static final VoxelShape OCTET_NNP = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
/*  45 */   protected static final VoxelShape OCTET_NPN = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
/*  46 */   protected static final VoxelShape OCTET_NPP = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);
/*  47 */   protected static final VoxelShape OCTET_PNN = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
/*  48 */   protected static final VoxelShape OCTET_PNP = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
/*  49 */   protected static final VoxelShape OCTET_PPN = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
/*  50 */   protected static final VoxelShape OCTET_PPP = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*  52 */   protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
/*  53 */   protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
/*     */   
/*     */   private static VoxelShape[] makeShapes(VoxelShape debug0, VoxelShape debug1, VoxelShape debug2, VoxelShape debug3, VoxelShape debug4) {
/*  56 */     return (VoxelShape[])IntStream.range(0, 16).mapToObj(debug5 -> makeStairShape(debug5, debug0, debug1, debug2, debug3, debug4)).toArray(debug0 -> new VoxelShape[debug0]);
/*     */   }
/*     */   
/*     */   private static VoxelShape makeStairShape(int debug0, VoxelShape debug1, VoxelShape debug2, VoxelShape debug3, VoxelShape debug4, VoxelShape debug5) {
/*  60 */     VoxelShape debug6 = debug1;
/*  61 */     if ((debug0 & 0x1) != 0) {
/*  62 */       debug6 = Shapes.or(debug6, debug2);
/*     */     }
/*  64 */     if ((debug0 & 0x2) != 0) {
/*  65 */       debug6 = Shapes.or(debug6, debug3);
/*     */     }
/*  67 */     if ((debug0 & 0x4) != 0) {
/*  68 */       debug6 = Shapes.or(debug6, debug4);
/*     */     }
/*  70 */     if ((debug0 & 0x8) != 0) {
/*  71 */       debug6 = Shapes.or(debug6, debug5);
/*     */     }
/*  73 */     return debug6;
/*     */   }
/*     */   
/*  76 */   private static final int[] SHAPE_BY_STATE = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8 };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Block base;
/*     */ 
/*     */   
/*     */   private final BlockState baseState;
/*     */ 
/*     */ 
/*     */   
/*     */   protected StairBlock(BlockState debug1, BlockBehaviour.Properties debug2) {
/*  88 */     super(debug2);
/*  89 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)SHAPE, (Comparable)StairsShape.STRAIGHT)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*  90 */     this.base = debug1.getBlock();
/*  91 */     this.baseState = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 101 */     return ((debug1.getValue((Property)HALF) == Half.TOP) ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[getShapeIndex(debug1)]];
/*     */   }
/*     */   
/*     */   private int getShapeIndex(BlockState debug1) {
/* 105 */     return ((StairsShape)debug1.getValue((Property)SHAPE)).ordinal() * 4 + ((Direction)debug1.getValue((Property)FACING)).get2DDataValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attack(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/* 115 */     this.baseState.attack(debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/* 120 */     this.base.destroy(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getExplosionResistance() {
/* 125 */     return this.base.getExplosionResistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 130 */     if (debug1.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/* 133 */     this.baseState.neighborChanged(debug2, debug3, Blocks.AIR, debug3, false);
/* 134 */     this.base.onPlace(this.baseState, debug2, debug3, debug4, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 139 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 142 */     this.baseState.onRemove(debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stepOn(Level debug1, BlockPos debug2, Entity debug3) {
/* 152 */     this.base.stepOn(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/* 157 */     return this.base.isRandomlyTicking(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 162 */     this.base.randomTick(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 167 */     this.base.tick(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 172 */     return this.baseState.use(debug2, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void wasExploded(Level debug1, BlockPos debug2, Explosion debug3) {
/* 177 */     this.base.wasExploded(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 182 */     Direction debug2 = debug1.getClickedFace();
/* 183 */     BlockPos debug3 = debug1.getClickedPos();
/* 184 */     FluidState debug4 = debug1.getLevel().getFluidState(debug3);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     BlockState debug5 = (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection())).setValue((Property)HALF, (debug2 == Direction.DOWN || (debug2 != Direction.UP && (debug1.getClickLocation()).y - debug3.getY() > 0.5D)) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/*     */     
/* 191 */     return (BlockState)debug5.setValue((Property)SHAPE, (Comparable)getStairsShape(debug5, (BlockGetter)debug1.getLevel(), debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 196 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 197 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/* 199 */     if (debug2.getAxis().isHorizontal()) {
/* 200 */       return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)getStairsShape(debug1, (BlockGetter)debug4, debug5));
/*     */     }
/* 202 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   private static StairsShape getStairsShape(BlockState debug0, BlockGetter debug1, BlockPos debug2) {
/* 206 */     Direction debug3 = (Direction)debug0.getValue((Property)FACING);
/* 207 */     BlockState debug4 = debug1.getBlockState(debug2.relative(debug3));
/* 208 */     if (isStairs(debug4) && debug0.getValue((Property)HALF) == debug4.getValue((Property)HALF)) {
/* 209 */       Direction direction = (Direction)debug4.getValue((Property)FACING);
/* 210 */       if (direction.getAxis() != ((Direction)debug0.getValue((Property)FACING)).getAxis() && canTakeShape(debug0, debug1, debug2, direction.getOpposite())) {
/* 211 */         if (direction == debug3.getCounterClockWise()) {
/* 212 */           return StairsShape.OUTER_LEFT;
/*     */         }
/* 214 */         return StairsShape.OUTER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 218 */     BlockState debug5 = debug1.getBlockState(debug2.relative(debug3.getOpposite()));
/* 219 */     if (isStairs(debug5) && debug0.getValue((Property)HALF) == debug5.getValue((Property)HALF)) {
/* 220 */       Direction debug6 = (Direction)debug5.getValue((Property)FACING);
/* 221 */       if (debug6.getAxis() != ((Direction)debug0.getValue((Property)FACING)).getAxis() && canTakeShape(debug0, debug1, debug2, debug6)) {
/* 222 */         if (debug6 == debug3.getCounterClockWise()) {
/* 223 */           return StairsShape.INNER_LEFT;
/*     */         }
/* 225 */         return StairsShape.INNER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     return StairsShape.STRAIGHT;
/*     */   }
/*     */   
/*     */   private static boolean canTakeShape(BlockState debug0, BlockGetter debug1, BlockPos debug2, Direction debug3) {
/* 233 */     BlockState debug4 = debug1.getBlockState(debug2.relative(debug3));
/* 234 */     return (!isStairs(debug4) || debug4.getValue((Property)FACING) != debug0.getValue((Property)FACING) || debug4.getValue((Property)HALF) != debug0.getValue((Property)HALF));
/*     */   }
/*     */   
/*     */   public static boolean isStairs(BlockState debug0) {
/* 238 */     return debug0.getBlock() instanceof StairBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 243 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 248 */     Direction debug3 = (Direction)debug1.getValue((Property)FACING);
/* 249 */     StairsShape debug4 = (StairsShape)debug1.getValue((Property)SHAPE);
/* 250 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 252 */         if (debug3.getAxis() == Direction.Axis.Z) {
/* 253 */           switch (debug4) {
/*     */             case LEFT_RIGHT:
/* 255 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case FRONT_BACK:
/* 257 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case null:
/* 259 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 261 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */           } 
/* 263 */           return debug1.rotate(Rotation.CLOCKWISE_180);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 268 */         if (debug3.getAxis() == Direction.Axis.X) {
/* 269 */           switch (debug4) {
/*     */             case LEFT_RIGHT:
/* 271 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case FRONT_BACK:
/* 273 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case null:
/* 275 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case null:
/* 277 */               return (BlockState)debug1.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */             case null:
/* 279 */               return debug1.rotate(Rotation.CLOCKWISE_180);
/*     */           } 
/*     */         
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 286 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 291 */     debug1.add(new Property[] { (Property)FACING, (Property)HALF, (Property)SHAPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 296 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 297 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 299 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 304 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StairBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */