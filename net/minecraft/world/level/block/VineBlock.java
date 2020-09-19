/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class VineBlock extends Block {
/*  27 */   public static final BooleanProperty UP = PipeBlock.UP;
/*  28 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  29 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  30 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  31 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   static {
/*  33 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(debug0 -> (debug0.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*     */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*  36 */   private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  37 */   private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
/*  38 */   private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  39 */   private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
/*  40 */   private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*     */   private final Map<BlockState, VoxelShape> shapesCache;
/*     */   
/*     */   public VineBlock(BlockBehaviour.Properties debug1) {
/*  45 */     super(debug1);
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false)));
/*     */     
/*  48 */     this.shapesCache = (Map<BlockState, VoxelShape>)ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), VineBlock::calculateShape)));
/*     */   }
/*     */   
/*     */   private static VoxelShape calculateShape(BlockState debug0) {
/*  52 */     VoxelShape debug1 = Shapes.empty();
/*  53 */     if (((Boolean)debug0.getValue((Property)UP)).booleanValue()) {
/*  54 */       debug1 = UP_AABB;
/*     */     }
/*  56 */     if (((Boolean)debug0.getValue((Property)NORTH)).booleanValue()) {
/*  57 */       debug1 = Shapes.or(debug1, NORTH_AABB);
/*     */     }
/*  59 */     if (((Boolean)debug0.getValue((Property)SOUTH)).booleanValue()) {
/*  60 */       debug1 = Shapes.or(debug1, SOUTH_AABB);
/*     */     }
/*  62 */     if (((Boolean)debug0.getValue((Property)EAST)).booleanValue()) {
/*  63 */       debug1 = Shapes.or(debug1, EAST_AABB);
/*     */     }
/*  65 */     if (((Boolean)debug0.getValue((Property)WEST)).booleanValue()) {
/*  66 */       debug1 = Shapes.or(debug1, WEST_AABB);
/*     */     }
/*  68 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  73 */     return this.shapesCache.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  78 */     return hasFaces(getUpdatedState(debug1, (BlockGetter)debug2, debug3));
/*     */   }
/*     */   
/*     */   private boolean hasFaces(BlockState debug1) {
/*  82 */     return (countFaces(debug1) > 0);
/*     */   }
/*     */   
/*     */   private int countFaces(BlockState debug1) {
/*  86 */     int debug2 = 0;
/*  87 */     for (BooleanProperty debug4 : PROPERTY_BY_DIRECTION.values()) {
/*  88 */       if (((Boolean)debug1.getValue((Property)debug4)).booleanValue()) {
/*  89 */         debug2++;
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return debug2;
/*     */   }
/*     */   
/*     */   private boolean canSupportAtFace(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/*  97 */     if (debug3 == Direction.DOWN) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     BlockPos debug4 = debug2.relative(debug3);
/* 102 */     if (isAcceptableNeighbour(debug1, debug4, debug3)) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     if (debug3.getAxis() != Direction.Axis.Y) {
/*     */       
/* 108 */       BooleanProperty debug5 = PROPERTY_BY_DIRECTION.get(debug3);
/* 109 */       BlockState debug6 = debug1.getBlockState(debug2.above());
/* 110 */       return (debug6.is(this) && ((Boolean)debug6.getValue((Property)debug5)).booleanValue());
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAcceptableNeighbour(BlockGetter debug0, BlockPos debug1, Direction debug2) {
/* 116 */     BlockState debug3 = debug0.getBlockState(debug1);
/*     */     
/* 118 */     return Block.isFaceFull(debug3.getCollisionShape(debug0, debug1), debug2.getOpposite());
/*     */   }
/*     */   
/*     */   private BlockState getUpdatedState(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 122 */     BlockPos debug4 = debug3.above();
/* 123 */     if (((Boolean)debug1.getValue((Property)UP)).booleanValue()) {
/* 124 */       debug1 = (BlockState)debug1.setValue((Property)UP, Boolean.valueOf(isAcceptableNeighbour(debug2, debug4, Direction.DOWN)));
/*     */     }
/*     */ 
/*     */     
/* 128 */     BlockState debug5 = null;
/* 129 */     for (Direction debug7 : Direction.Plane.HORIZONTAL) {
/* 130 */       BooleanProperty debug8 = getPropertyForFace(debug7);
/*     */       
/* 132 */       if (((Boolean)debug1.getValue((Property)debug8)).booleanValue()) {
/* 133 */         boolean debug9 = canSupportAtFace(debug2, debug3, debug7);
/* 134 */         if (!debug9) {
/* 135 */           if (debug5 == null) {
/* 136 */             debug5 = debug2.getBlockState(debug4);
/*     */           }
/* 138 */           debug9 = (debug5.is(this) && ((Boolean)debug5.getValue((Property)debug8)).booleanValue());
/*     */         } 
/* 140 */         debug1 = (BlockState)debug1.setValue((Property)debug8, Boolean.valueOf(debug9));
/*     */       } 
/*     */     } 
/* 143 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 148 */     if (debug2 == Direction.DOWN) {
/* 149 */       return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     }
/*     */     
/* 152 */     BlockState debug7 = getUpdatedState(debug1, (BlockGetter)debug4, debug5);
/*     */     
/* 154 */     if (!hasFaces(debug7)) {
/* 155 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 158 */     return debug7;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 163 */     if (debug2.random.nextInt(4) != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     Direction debug5 = Direction.getRandom(debug4);
/*     */     
/* 169 */     BlockPos debug6 = debug3.above();
/* 170 */     if (debug5.getAxis().isHorizontal() && !((Boolean)debug1.getValue((Property)getPropertyForFace(debug5))).booleanValue()) {
/* 171 */       if (!canSpread((BlockGetter)debug2, debug3)) {
/*     */         return;
/*     */       }
/*     */       
/* 175 */       BlockPos debug7 = debug3.relative(debug5);
/*     */       
/* 177 */       BlockState debug8 = debug2.getBlockState(debug7);
/* 178 */       if (debug8.isAir()) {
/*     */         
/* 180 */         Direction debug9 = debug5.getClockWise();
/* 181 */         Direction debug10 = debug5.getCounterClockWise();
/*     */ 
/*     */         
/* 184 */         boolean debug11 = ((Boolean)debug1.getValue((Property)getPropertyForFace(debug9))).booleanValue();
/* 185 */         boolean debug12 = ((Boolean)debug1.getValue((Property)getPropertyForFace(debug10))).booleanValue();
/*     */         
/* 187 */         BlockPos debug13 = debug7.relative(debug9);
/* 188 */         BlockPos debug14 = debug7.relative(debug10);
/*     */         
/* 190 */         if (debug11 && isAcceptableNeighbour((BlockGetter)debug2, debug13, debug9)) {
/* 191 */           debug2.setBlock(debug7, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(debug9), Boolean.valueOf(true)), 2);
/* 192 */         } else if (debug12 && isAcceptableNeighbour((BlockGetter)debug2, debug14, debug10)) {
/* 193 */           debug2.setBlock(debug7, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(debug10), Boolean.valueOf(true)), 2);
/*     */         } else {
/*     */           
/* 196 */           Direction debug15 = debug5.getOpposite();
/* 197 */           if (debug11 && debug2.isEmptyBlock(debug13) && isAcceptableNeighbour((BlockGetter)debug2, debug3.relative(debug9), debug15)) {
/* 198 */             debug2.setBlock(debug13, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(debug15), Boolean.valueOf(true)), 2);
/* 199 */           } else if (debug12 && debug2.isEmptyBlock(debug14) && isAcceptableNeighbour((BlockGetter)debug2, debug3.relative(debug10), debug15)) {
/* 200 */             debug2.setBlock(debug14, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(debug15), Boolean.valueOf(true)), 2);
/*     */           
/*     */           }
/* 203 */           else if (debug2.random.nextFloat() < 0.05D && isAcceptableNeighbour((BlockGetter)debug2, debug7.above(), Direction.UP)) {
/* 204 */             debug2.setBlock(debug7, (BlockState)defaultBlockState().setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */           }
/*     */         
/*     */         } 
/* 208 */       } else if (isAcceptableNeighbour((BlockGetter)debug2, debug7, debug5)) {
/*     */         
/* 210 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)getPropertyForFace(debug5), Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 215 */     if (debug5 == Direction.UP && debug3.getY() < 255) {
/* 216 */       if (canSupportAtFace((BlockGetter)debug2, debug3, debug5)) {
/* 217 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */         return;
/*     */       } 
/* 220 */       if (debug2.isEmptyBlock(debug6)) {
/* 221 */         if (!canSpread((BlockGetter)debug2, debug3)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 226 */         BlockState debug7 = debug1;
/* 227 */         for (Direction debug9 : Direction.Plane.HORIZONTAL) {
/* 228 */           if (debug4.nextBoolean() || !isAcceptableNeighbour((BlockGetter)debug2, debug6.relative(debug9), Direction.UP)) {
/* 229 */             debug7 = (BlockState)debug7.setValue((Property)getPropertyForFace(debug9), Boolean.valueOf(false));
/*     */           }
/*     */         } 
/* 232 */         if (hasHorizontalConnection(debug7)) {
/* 233 */           debug2.setBlock(debug6, debug7, 2);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/* 238 */     if (debug3.getY() > 0) {
/*     */       
/* 240 */       BlockPos debug7 = debug3.below();
/* 241 */       BlockState debug8 = debug2.getBlockState(debug7);
/*     */       
/* 243 */       if (debug8.isAir() || debug8.is(this)) {
/* 244 */         BlockState debug9 = debug8.isAir() ? defaultBlockState() : debug8;
/* 245 */         BlockState debug10 = copyRandomFaces(debug1, debug9, debug4);
/* 246 */         if (debug9 != debug10 && hasHorizontalConnection(debug10)) {
/* 247 */           debug2.setBlock(debug7, debug10, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState copyRandomFaces(BlockState debug1, BlockState debug2, Random debug3) {
/* 254 */     for (Direction debug5 : Direction.Plane.HORIZONTAL) {
/* 255 */       if (debug3.nextBoolean()) {
/* 256 */         BooleanProperty debug6 = getPropertyForFace(debug5);
/* 257 */         if (((Boolean)debug1.getValue((Property)debug6)).booleanValue()) {
/* 258 */           debug2 = (BlockState)debug2.setValue((Property)debug6, Boolean.valueOf(true));
/*     */         }
/*     */       } 
/*     */     } 
/* 262 */     return debug2;
/*     */   }
/*     */   
/*     */   private boolean hasHorizontalConnection(BlockState debug1) {
/* 266 */     return (((Boolean)debug1.getValue((Property)NORTH)).booleanValue() || ((Boolean)debug1.getValue((Property)EAST)).booleanValue() || ((Boolean)debug1.getValue((Property)SOUTH)).booleanValue() || ((Boolean)debug1.getValue((Property)WEST)).booleanValue());
/*     */   }
/*     */   
/*     */   private boolean canSpread(BlockGetter debug1, BlockPos debug2) {
/* 270 */     int debug3 = 4;
/*     */     
/* 272 */     Iterable<BlockPos> debug4 = BlockPos.betweenClosed(debug2
/* 273 */         .getX() - 4, debug2.getY() - 1, debug2.getZ() - 4, debug2
/* 274 */         .getX() + 4, debug2.getY() + 1, debug2.getZ() + 4);
/*     */ 
/*     */     
/* 277 */     int debug5 = 5;
/* 278 */     for (BlockPos debug7 : debug4) {
/* 279 */       if (debug1.getBlockState(debug7).is(this) && 
/* 280 */         --debug5 <= 0) {
/* 281 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 285 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/* 290 */     BlockState debug3 = debug2.getLevel().getBlockState(debug2.getClickedPos());
/* 291 */     if (debug3.is(this)) {
/* 292 */       return (countFaces(debug3) < PROPERTY_BY_DIRECTION.size());
/*     */     }
/*     */     
/* 295 */     return super.canBeReplaced(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 301 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos());
/* 302 */     boolean debug3 = debug2.is(this);
/* 303 */     BlockState debug4 = debug3 ? debug2 : defaultBlockState();
/*     */     
/* 305 */     for (Direction debug8 : debug1.getNearestLookingDirections()) {
/* 306 */       if (debug8 != Direction.DOWN) {
/* 307 */         BooleanProperty debug9 = getPropertyForFace(debug8);
/* 308 */         boolean debug10 = (debug3 && ((Boolean)debug2.getValue((Property)debug9)).booleanValue());
/* 309 */         if (!debug10 && canSupportAtFace((BlockGetter)debug1.getLevel(), debug1.getClickedPos(), debug8)) {
/* 310 */           return (BlockState)debug4.setValue((Property)debug9, Boolean.valueOf(true));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 315 */     return debug3 ? debug4 : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 320 */     debug1.add(new Property[] { (Property)UP, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 325 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 327 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 329 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)EAST))).setValue((Property)EAST, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)NORTH));
/*     */       case null:
/* 331 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)WEST))).setValue((Property)EAST, debug1.getValue((Property)NORTH))).setValue((Property)SOUTH, debug1.getValue((Property)EAST))).setValue((Property)WEST, debug1.getValue((Property)SOUTH));
/*     */     } 
/* 333 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 339 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 341 */         return (BlockState)((BlockState)debug1.setValue((Property)NORTH, debug1.getValue((Property)SOUTH))).setValue((Property)SOUTH, debug1.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 343 */         return (BlockState)((BlockState)debug1.setValue((Property)EAST, debug1.getValue((Property)WEST))).setValue((Property)WEST, debug1.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 347 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */   
/*     */   public static BooleanProperty getPropertyForFace(Direction debug0) {
/* 351 */     return PROPERTY_BY_DIRECTION.get(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\VineBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */