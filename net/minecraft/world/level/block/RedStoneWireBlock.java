/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.math.Vector3f;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedStoneWireBlock extends Block {
/*  37 */   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
/*  38 */   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
/*  39 */   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
/*  40 */   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
/*  41 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  43 */   public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
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
/*  57 */   private static final VoxelShape SHAPE_DOT = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D);
/*  58 */   private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/*  59 */         Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Direction.SOUTH, 
/*  60 */         Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Direction.EAST, 
/*  61 */         Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Direction.WEST, 
/*  62 */         Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D)));
/*     */   
/*  64 */   private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/*  65 */         Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 16.0D, 1.0D)), Direction.SOUTH, 
/*  66 */         Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0D, 0.0D, 15.0D, 13.0D, 16.0D, 16.0D)), Direction.EAST, 
/*  67 */         Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0D, 0.0D, 3.0D, 16.0D, 16.0D, 13.0D)), Direction.WEST, 
/*  68 */         Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0D, 0.0D, 3.0D, 1.0D, 16.0D, 13.0D))));
/*     */ 
/*     */   
/*  71 */   private final Map<BlockState, VoxelShape> SHAPES_CACHE = Maps.newHashMap();
/*     */   
/*  73 */   private static final Vector3f[] COLORS = new Vector3f[16];
/*     */   static {
/*  75 */     for (int debug0 = 0; debug0 <= 15; debug0++) {
/*  76 */       float debug1 = debug0 / 15.0F;
/*  77 */       float debug2 = debug1 * 0.6F + ((debug1 > 0.0F) ? 0.4F : 0.3F);
/*  78 */       float debug3 = Mth.clamp(debug1 * debug1 * 0.7F - 0.5F, 0.0F, 1.0F);
/*  79 */       float debug4 = Mth.clamp(debug1 * debug1 * 0.6F - 0.7F, 0.0F, 1.0F);
/*  80 */       COLORS[debug0] = new Vector3f(debug2, debug3, debug4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final BlockState crossState;
/*     */   private boolean shouldSignal = true;
/*     */   
/*     */   public RedStoneWireBlock(BlockBehaviour.Properties debug1) {
/*  89 */     super(debug1);
/*  90 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, (Comparable)RedstoneSide.NONE)).setValue((Property)EAST, (Comparable)RedstoneSide.NONE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.NONE)).setValue((Property)WEST, (Comparable)RedstoneSide.NONE)).setValue((Property)POWER, Integer.valueOf(0)));
/*  91 */     this.crossState = (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)WEST, (Comparable)RedstoneSide.SIDE);
/*  92 */     for (UnmodifiableIterator<BlockState> unmodifiableIterator = getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { BlockState debug3 = unmodifiableIterator.next();
/*  93 */       if (((Integer)debug3.getValue((Property)POWER)).intValue() == 0) {
/*  94 */         this.SHAPES_CACHE.put(debug3, calculateShape(debug3));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private VoxelShape calculateShape(BlockState debug1) {
/* 100 */     VoxelShape debug2 = SHAPE_DOT;
/* 101 */     for (Direction debug4 : Direction.Plane.HORIZONTAL) {
/* 102 */       RedstoneSide debug5 = (RedstoneSide)debug1.getValue((Property)PROPERTY_BY_DIRECTION.get(debug4));
/* 103 */       if (debug5 == RedstoneSide.SIDE) {
/* 104 */         debug2 = Shapes.or(debug2, SHAPES_FLOOR.get(debug4)); continue;
/* 105 */       }  if (debug5 == RedstoneSide.UP) {
/* 106 */         debug2 = Shapes.or(debug2, SHAPES_UP.get(debug4));
/*     */       }
/*     */     } 
/* 109 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 114 */     return this.SHAPES_CACHE.get(debug1.setValue((Property)POWER, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 119 */     return getConnectionState((BlockGetter)debug1.getLevel(), this.crossState, debug1.getClickedPos());
/*     */   }
/*     */   
/*     */   private BlockState getConnectionState(BlockGetter debug1, BlockState debug2, BlockPos debug3) {
/* 123 */     boolean debug4 = isDot(debug2);
/* 124 */     debug2 = getMissingConnections(debug1, (BlockState)defaultBlockState().setValue((Property)POWER, debug2.getValue((Property)POWER)), debug3);
/*     */ 
/*     */     
/* 127 */     if (debug4 && isDot(debug2)) {
/* 128 */       return debug2;
/*     */     }
/*     */     
/* 131 */     boolean debug5 = ((RedstoneSide)debug2.getValue((Property)NORTH)).isConnected();
/* 132 */     boolean debug6 = ((RedstoneSide)debug2.getValue((Property)SOUTH)).isConnected();
/* 133 */     boolean debug7 = ((RedstoneSide)debug2.getValue((Property)EAST)).isConnected();
/* 134 */     boolean debug8 = ((RedstoneSide)debug2.getValue((Property)WEST)).isConnected();
/* 135 */     boolean debug9 = (!debug5 && !debug6);
/* 136 */     boolean debug10 = (!debug7 && !debug8);
/*     */     
/* 138 */     if (!debug8 && debug9) {
/* 139 */       debug2 = (BlockState)debug2.setValue((Property)WEST, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 141 */     if (!debug7 && debug9) {
/* 142 */       debug2 = (BlockState)debug2.setValue((Property)EAST, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 144 */     if (!debug5 && debug10) {
/* 145 */       debug2 = (BlockState)debug2.setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 147 */     if (!debug6 && debug10) {
/* 148 */       debug2 = (BlockState)debug2.setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 150 */     return debug2;
/*     */   }
/*     */   
/*     */   private BlockState getMissingConnections(BlockGetter debug1, BlockState debug2, BlockPos debug3) {
/* 154 */     boolean debug4 = !debug1.getBlockState(debug3.above()).isRedstoneConductor(debug1, debug3);
/* 155 */     for (Direction debug6 : Direction.Plane.HORIZONTAL) {
/* 156 */       if (!((RedstoneSide)debug2.getValue((Property)PROPERTY_BY_DIRECTION.get(debug6))).isConnected()) {
/* 157 */         RedstoneSide debug7 = getConnectingSide(debug1, debug3, debug6, debug4);
/* 158 */         debug2 = (BlockState)debug2.setValue((Property)PROPERTY_BY_DIRECTION.get(debug6), (Comparable)debug7);
/*     */       } 
/*     */     } 
/* 161 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 166 */     if (debug2 == Direction.DOWN) {
/* 167 */       return debug1;
/*     */     }
/* 169 */     if (debug2 == Direction.UP) {
/* 170 */       return getConnectionState((BlockGetter)debug4, debug1, debug5);
/*     */     }
/*     */     
/* 173 */     RedstoneSide debug7 = getConnectingSide((BlockGetter)debug4, debug5, debug2);
/* 174 */     if (debug7.isConnected() == ((RedstoneSide)debug1.getValue((Property)PROPERTY_BY_DIRECTION.get(debug2))).isConnected() && !isCross(debug1)) {
/* 175 */       return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), (Comparable)debug7);
/*     */     }
/* 177 */     return getConnectionState((BlockGetter)debug4, (BlockState)((BlockState)this.crossState.setValue((Property)POWER, debug1.getValue((Property)POWER))).setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), (Comparable)debug7), debug5);
/*     */   }
/*     */   
/*     */   private static boolean isCross(BlockState debug0) {
/* 181 */     return (((RedstoneSide)debug0.getValue((Property)NORTH)).isConnected() && ((RedstoneSide)debug0.getValue((Property)SOUTH)).isConnected() && ((RedstoneSide)debug0.getValue((Property)EAST)).isConnected() && ((RedstoneSide)debug0.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */   
/*     */   private static boolean isDot(BlockState debug0) {
/* 185 */     return (!((RedstoneSide)debug0.getValue((Property)NORTH)).isConnected() && !((RedstoneSide)debug0.getValue((Property)SOUTH)).isConnected() && !((RedstoneSide)debug0.getValue((Property)EAST)).isConnected() && !((RedstoneSide)debug0.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndirectNeighbourShapes(BlockState debug1, LevelAccessor debug2, BlockPos debug3, int debug4, int debug5) {
/* 190 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 191 */     for (Direction debug8 : Direction.Plane.HORIZONTAL) {
/* 192 */       RedstoneSide debug9 = (RedstoneSide)debug1.getValue((Property)PROPERTY_BY_DIRECTION.get(debug8));
/* 193 */       if (debug9 != RedstoneSide.NONE && !debug2.getBlockState((BlockPos)debug6.setWithOffset((Vec3i)debug3, debug8)).is(this)) {
/* 194 */         debug6.move(Direction.DOWN);
/* 195 */         BlockState debug10 = debug2.getBlockState((BlockPos)debug6);
/* 196 */         if (!debug10.is(Blocks.OBSERVER)) {
/* 197 */           BlockPos blockPos = debug6.relative(debug8.getOpposite());
/* 198 */           BlockState debug12 = debug10.updateShape(debug8.getOpposite(), debug2.getBlockState(blockPos), debug2, (BlockPos)debug6, blockPos);
/* 199 */           updateOrDestroy(debug10, debug12, debug2, (BlockPos)debug6, debug4, debug5);
/*     */         } 
/*     */         
/* 202 */         debug6.setWithOffset((Vec3i)debug3, debug8).move(Direction.UP);
/* 203 */         BlockState debug11 = debug2.getBlockState((BlockPos)debug6);
/* 204 */         if (!debug11.is(Blocks.OBSERVER)) {
/* 205 */           BlockPos debug12 = debug6.relative(debug8.getOpposite());
/* 206 */           BlockState debug13 = debug11.updateShape(debug8.getOpposite(), debug2.getBlockState(debug12), debug2, (BlockPos)debug6, debug12);
/* 207 */           updateOrDestroy(debug11, debug13, debug2, (BlockPos)debug6, debug4, debug5);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/* 214 */     return getConnectingSide(debug1, debug2, debug3, !debug1.getBlockState(debug2.above()).isRedstoneConductor(debug1, debug2));
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter debug1, BlockPos debug2, Direction debug3, boolean debug4) {
/* 218 */     BlockPos debug5 = debug2.relative(debug3);
/* 219 */     BlockState debug6 = debug1.getBlockState(debug5);
/* 220 */     if (debug4) {
/* 221 */       boolean debug7 = canSurviveOn(debug1, debug5, debug6);
/* 222 */       if (debug7 && shouldConnectTo(debug1.getBlockState(debug5.above()))) {
/*     */ 
/*     */         
/* 225 */         if (debug6.isFaceSturdy(debug1, debug5, debug3.getOpposite())) {
/* 226 */           return RedstoneSide.UP;
/*     */         }
/* 228 */         return RedstoneSide.SIDE;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 233 */     if (shouldConnectTo(debug6, debug3) || (!debug6.isRedstoneConductor(debug1, debug5) && shouldConnectTo(debug1.getBlockState(debug5.below())))) {
/* 234 */       return RedstoneSide.SIDE;
/*     */     }
/* 236 */     return RedstoneSide.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 241 */     BlockPos debug4 = debug3.below();
/* 242 */     BlockState debug5 = debug2.getBlockState(debug4);
/* 243 */     return canSurviveOn((BlockGetter)debug2, debug4, debug5);
/*     */   }
/*     */   
/*     */   private boolean canSurviveOn(BlockGetter debug1, BlockPos debug2, BlockState debug3) {
/* 247 */     return (debug3.isFaceSturdy(debug1, debug2, Direction.UP) || debug3.is(Blocks.HOPPER));
/*     */   }
/*     */   
/*     */   private void updatePowerStrength(Level debug1, BlockPos debug2, BlockState debug3) {
/* 251 */     int debug4 = calculateTargetStrength(debug1, debug2);
/*     */     
/* 253 */     if (((Integer)debug3.getValue((Property)POWER)).intValue() != debug4) {
/* 254 */       if (debug1.getBlockState(debug2) == debug3) {
/* 255 */         debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)POWER, Integer.valueOf(debug4)), 2);
/*     */       }
/*     */ 
/*     */       
/* 259 */       Set<BlockPos> debug5 = Sets.newHashSet();
/* 260 */       debug5.add(debug2);
/* 261 */       for (Direction debug9 : Direction.values()) {
/* 262 */         debug5.add(debug2.relative(debug9));
/*     */       }
/* 264 */       for (BlockPos debug7 : debug5) {
/* 265 */         debug1.updateNeighborsAt(debug7, this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int calculateTargetStrength(Level debug1, BlockPos debug2) {
/* 271 */     this.shouldSignal = false;
/* 272 */     int debug3 = debug1.getBestNeighborSignal(debug2);
/* 273 */     this.shouldSignal = true;
/*     */     
/* 275 */     int debug4 = 0;
/* 276 */     if (debug3 < 15) {
/* 277 */       for (Direction debug6 : Direction.Plane.HORIZONTAL) {
/* 278 */         BlockPos debug7 = debug2.relative(debug6);
/* 279 */         BlockState debug8 = debug1.getBlockState(debug7);
/*     */         
/* 281 */         debug4 = Math.max(debug4, getWireSignal(debug8));
/*     */         
/* 283 */         BlockPos debug9 = debug2.above();
/* 284 */         if (debug8.isRedstoneConductor((BlockGetter)debug1, debug7) && !debug1.getBlockState(debug9).isRedstoneConductor((BlockGetter)debug1, debug9)) {
/* 285 */           debug4 = Math.max(debug4, getWireSignal(debug1.getBlockState(debug7.above()))); continue;
/* 286 */         }  if (!debug8.isRedstoneConductor((BlockGetter)debug1, debug7)) {
/* 287 */           debug4 = Math.max(debug4, getWireSignal(debug1.getBlockState(debug7.below())));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 292 */     return Math.max(debug3, debug4 - 1);
/*     */   }
/*     */   
/*     */   private int getWireSignal(BlockState debug1) {
/* 296 */     return debug1.is(this) ? ((Integer)debug1.getValue((Property)POWER)).intValue() : 0;
/*     */   }
/*     */   
/*     */   private void checkCornerChangeAt(Level debug1, BlockPos debug2) {
/* 300 */     if (!debug1.getBlockState(debug2).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/* 304 */     debug1.updateNeighborsAt(debug2, this);
/* 305 */     for (Direction debug6 : Direction.values()) {
/* 306 */       debug1.updateNeighborsAt(debug2.relative(debug6), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 312 */     if (debug4.is(debug1.getBlock()) || debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 316 */     updatePowerStrength(debug2, debug3, debug1);
/*     */     
/* 318 */     for (Direction debug7 : Direction.Plane.VERTICAL) {
/* 319 */       debug2.updateNeighborsAt(debug3.relative(debug7), this);
/*     */     }
/*     */     
/* 322 */     updateNeighborsOfNeighboringWires(debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 327 */     if (debug5 || debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 330 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/* 331 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 335 */     for (Direction debug9 : Direction.values()) {
/* 336 */       debug2.updateNeighborsAt(debug3.relative(debug9), this);
/*     */     }
/* 338 */     updatePowerStrength(debug2, debug3, debug1);
/*     */     
/* 340 */     updateNeighborsOfNeighboringWires(debug2, debug3);
/*     */   }
/*     */   
/*     */   private void updateNeighborsOfNeighboringWires(Level debug1, BlockPos debug2) {
/* 344 */     for (Direction debug4 : Direction.Plane.HORIZONTAL) {
/* 345 */       checkCornerChangeAt(debug1, debug2.relative(debug4));
/*     */     }
/*     */     
/* 348 */     for (Direction debug4 : Direction.Plane.HORIZONTAL) {
/* 349 */       BlockPos debug5 = debug2.relative(debug4);
/*     */       
/* 351 */       if (debug1.getBlockState(debug5).isRedstoneConductor((BlockGetter)debug1, debug5)) {
/* 352 */         checkCornerChangeAt(debug1, debug5.above()); continue;
/*     */       } 
/* 354 */       checkCornerChangeAt(debug1, debug5.below());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 361 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 365 */     if (debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 366 */       updatePowerStrength(debug2, debug3, debug1);
/*     */     } else {
/* 368 */       dropResources(debug1, debug2, debug3);
/* 369 */       debug2.removeBlock(debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 375 */     if (!this.shouldSignal) {
/* 376 */       return 0;
/*     */     }
/* 378 */     return debug1.getSignal(debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 383 */     if (!this.shouldSignal || debug4 == Direction.DOWN) {
/* 384 */       return 0;
/*     */     }
/* 386 */     int debug5 = ((Integer)debug1.getValue((Property)POWER)).intValue();
/* 387 */     if (debug5 == 0) {
/* 388 */       return 0;
/*     */     }
/*     */     
/* 391 */     if (debug4 == Direction.UP || ((RedstoneSide)getConnectionState(debug2, debug1, debug3).getValue((Property)PROPERTY_BY_DIRECTION.get(debug4.getOpposite()))).isConnected()) {
/* 392 */       return debug5;
/*     */     }
/* 394 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState debug0) {
/* 399 */     return shouldConnectTo(debug0, (Direction)null);
/*     */   }
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState debug0, @Nullable Direction debug1) {
/* 403 */     if (debug0.is(Blocks.REDSTONE_WIRE)) {
/* 404 */       return true;
/*     */     }
/*     */     
/* 407 */     if (debug0.is(Blocks.REPEATER)) {
/* 408 */       Direction debug2 = (Direction)debug0.getValue((Property)RepeaterBlock.FACING);
/* 409 */       return (debug2 == debug1 || debug2.getOpposite() == debug1);
/*     */     } 
/*     */     
/* 412 */     if (debug0.is(Blocks.OBSERVER)) {
/* 413 */       return (debug1 == debug0.getValue((Property)ObserverBlock.FACING));
/*     */     }
/*     */     
/* 416 */     return (debug0.isSignalSource() && debug1 != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 421 */     return this.shouldSignal;
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
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 466 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 468 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 470 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)EAST))).setValue((Property)EAST, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)NORTH));
/*     */       case null:
/* 472 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)WEST))).setValue((Property)EAST, debug1.getValue((Property)NORTH))).setValue((Property)SOUTH, debug1.getValue((Property)EAST))).setValue((Property)WEST, debug1.getValue((Property)SOUTH));
/*     */     } 
/* 474 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 480 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 482 */         return (BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 484 */         return (BlockState)((BlockState)debug1.setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 488 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 493 */     debug1.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)POWER });
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 498 */     if (!debug4.abilities.mayBuild) {
/* 499 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 502 */     if (isCross(debug1) || isDot(debug1)) {
/* 503 */       BlockState debug7 = isCross(debug1) ? defaultBlockState() : this.crossState;
/* 504 */       debug7 = (BlockState)debug7.setValue((Property)POWER, debug1.getValue((Property)POWER));
/* 505 */       debug7 = getConnectionState((BlockGetter)debug2, debug7, debug3);
/* 506 */       if (debug7 != debug1) {
/* 507 */         debug2.setBlock(debug3, debug7, 3);
/*     */         
/* 509 */         updatesOnShapeChange(debug2, debug3, debug1, debug7);
/* 510 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 513 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private void updatesOnShapeChange(Level debug1, BlockPos debug2, BlockState debug3, BlockState debug4) {
/* 517 */     for (Direction debug6 : Direction.Plane.HORIZONTAL) {
/* 518 */       BlockPos debug7 = debug2.relative(debug6);
/* 519 */       if (((RedstoneSide)debug3.getValue((Property)PROPERTY_BY_DIRECTION.get(debug6))).isConnected() != ((RedstoneSide)debug4.getValue((Property)PROPERTY_BY_DIRECTION.get(debug6))).isConnected() && debug1.getBlockState(debug7).isRedstoneConductor((BlockGetter)debug1, debug7))
/* 520 */         debug1.updateNeighborsAtExceptFromFacing(debug7, debug4.getBlock(), debug6.getOpposite()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RedStoneWireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */