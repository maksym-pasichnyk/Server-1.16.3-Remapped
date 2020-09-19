/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WallSide;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallBlock extends Block implements SimpleWaterloggedBlock {
/*  29 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/*  30 */   public static final EnumProperty<WallSide> EAST_WALL = BlockStateProperties.EAST_WALL;
/*  31 */   public static final EnumProperty<WallSide> NORTH_WALL = BlockStateProperties.NORTH_WALL;
/*  32 */   public static final EnumProperty<WallSide> SOUTH_WALL = BlockStateProperties.SOUTH_WALL;
/*  33 */   public static final EnumProperty<WallSide> WEST_WALL = BlockStateProperties.WEST_WALL;
/*  34 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<BlockState, VoxelShape> shapeByIndex;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<BlockState, VoxelShape> collisionShapeByIndex;
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static final VoxelShape POST_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
/*  48 */   private static final VoxelShape NORTH_TEST = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
/*  49 */   private static final VoxelShape SOUTH_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
/*  50 */   private static final VoxelShape WEST_TEST = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
/*  51 */   private static final VoxelShape EAST_TEST = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
/*     */   
/*     */   public WallBlock(BlockBehaviour.Properties debug1) {
/*  54 */     super(debug1);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, Boolean.valueOf(true))).setValue((Property)NORTH_WALL, (Comparable)WallSide.NONE)).setValue((Property)EAST_WALL, (Comparable)WallSide.NONE)).setValue((Property)SOUTH_WALL, (Comparable)WallSide.NONE)).setValue((Property)WEST_WALL, (Comparable)WallSide.NONE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  57 */     this.shapeByIndex = makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
/*  58 */     this.collisionShapeByIndex = makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
/*     */   }
/*     */   
/*     */   private static VoxelShape applyWallShape(VoxelShape debug0, WallSide debug1, VoxelShape debug2, VoxelShape debug3) {
/*  62 */     if (debug1 == WallSide.TALL) {
/*  63 */       return Shapes.or(debug0, debug3);
/*     */     }
/*  65 */     if (debug1 == WallSide.LOW) {
/*  66 */       return Shapes.or(debug0, debug2);
/*     */     }
/*  68 */     return debug0;
/*     */   }
/*     */   
/*     */   private Map<BlockState, VoxelShape> makeShapes(float debug1, float debug2, float debug3, float debug4, float debug5, float debug6) {
/*  72 */     float debug7 = 8.0F - debug1;
/*  73 */     float debug8 = 8.0F + debug1;
/*     */     
/*  75 */     float debug9 = 8.0F - debug2;
/*  76 */     float debug10 = 8.0F + debug2;
/*     */     
/*  78 */     VoxelShape debug11 = Block.box(debug7, 0.0D, debug7, debug8, debug3, debug8);
/*  79 */     VoxelShape debug12 = Block.box(debug9, debug4, 0.0D, debug10, debug5, debug10);
/*  80 */     VoxelShape debug13 = Block.box(debug9, debug4, debug9, debug10, debug5, 16.0D);
/*  81 */     VoxelShape debug14 = Block.box(0.0D, debug4, debug9, debug10, debug5, debug10);
/*  82 */     VoxelShape debug15 = Block.box(debug9, debug4, debug9, 16.0D, debug5, debug10);
/*     */     
/*  84 */     VoxelShape debug16 = Block.box(debug9, debug4, 0.0D, debug10, debug6, debug10);
/*  85 */     VoxelShape debug17 = Block.box(debug9, debug4, debug9, debug10, debug6, 16.0D);
/*  86 */     VoxelShape debug18 = Block.box(0.0D, debug4, debug9, debug10, debug6, debug10);
/*  87 */     VoxelShape debug19 = Block.box(debug9, debug4, debug9, 16.0D, debug6, debug10);
/*     */ 
/*     */     
/*  90 */     ImmutableMap.Builder<BlockState, VoxelShape> debug20 = ImmutableMap.builder();
/*     */     
/*  92 */     for (Boolean debug22 : UP.getPossibleValues()) {
/*  93 */       for (WallSide debug24 : EAST_WALL.getPossibleValues()) {
/*  94 */         for (WallSide debug26 : NORTH_WALL.getPossibleValues()) {
/*  95 */           for (WallSide debug28 : WEST_WALL.getPossibleValues()) {
/*  96 */             for (WallSide debug30 : SOUTH_WALL.getPossibleValues()) {
/*  97 */               VoxelShape debug31 = Shapes.empty();
/*  98 */               debug31 = applyWallShape(debug31, debug24, debug15, debug19);
/*  99 */               debug31 = applyWallShape(debug31, debug28, debug14, debug18);
/* 100 */               debug31 = applyWallShape(debug31, debug26, debug12, debug16);
/* 101 */               debug31 = applyWallShape(debug31, debug30, debug13, debug17);
/* 102 */               if (debug22.booleanValue()) {
/* 103 */                 debug31 = Shapes.or(debug31, debug11);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 110 */               BlockState debug32 = (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)UP, debug22)).setValue((Property)EAST_WALL, (Comparable)debug24)).setValue((Property)WEST_WALL, (Comparable)debug28)).setValue((Property)NORTH_WALL, (Comparable)debug26)).setValue((Property)SOUTH_WALL, (Comparable)debug30);
/*     */               
/* 112 */               debug20.put(debug32.setValue((Property)WATERLOGGED, Boolean.valueOf(false)), debug31);
/* 113 */               debug20.put(debug32.setValue((Property)WATERLOGGED, Boolean.valueOf(true)), debug31);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 119 */     return (Map<BlockState, VoxelShape>)debug20.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 124 */     return this.shapeByIndex.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 129 */     return this.collisionShapeByIndex.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   private boolean connectsTo(BlockState debug1, boolean debug2, Direction debug3) {
/* 138 */     Block debug4 = debug1.getBlock();
/*     */     
/* 140 */     boolean debug5 = (debug4 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(debug1, debug3));
/* 141 */     return (debug1.is((Tag)BlockTags.WALLS) || (!isExceptionForConnection(debug4) && debug2) || debug4 instanceof IronBarsBlock || debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 146 */     Level level = debug1.getLevel();
/* 147 */     BlockPos debug3 = debug1.getClickedPos();
/* 148 */     FluidState debug4 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/* 150 */     BlockPos debug5 = debug3.north();
/* 151 */     BlockPos debug6 = debug3.east();
/* 152 */     BlockPos debug7 = debug3.south();
/* 153 */     BlockPos debug8 = debug3.west();
/* 154 */     BlockPos debug9 = debug3.above();
/*     */     
/* 156 */     BlockState debug10 = level.getBlockState(debug5);
/* 157 */     BlockState debug11 = level.getBlockState(debug6);
/* 158 */     BlockState debug12 = level.getBlockState(debug7);
/* 159 */     BlockState debug13 = level.getBlockState(debug8);
/* 160 */     BlockState debug14 = level.getBlockState(debug9);
/*     */     
/* 162 */     boolean debug15 = connectsTo(debug10, debug10.isFaceSturdy((BlockGetter)level, debug5, Direction.SOUTH), Direction.SOUTH);
/* 163 */     boolean debug16 = connectsTo(debug11, debug11.isFaceSturdy((BlockGetter)level, debug6, Direction.WEST), Direction.WEST);
/* 164 */     boolean debug17 = connectsTo(debug12, debug12.isFaceSturdy((BlockGetter)level, debug7, Direction.NORTH), Direction.NORTH);
/* 165 */     boolean debug18 = connectsTo(debug13, debug13.isFaceSturdy((BlockGetter)level, debug8, Direction.EAST), Direction.EAST);
/*     */     
/* 167 */     BlockState debug19 = (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/* 168 */     return updateShape((LevelReader)level, debug19, debug9, debug14, debug15, debug16, debug17, debug18);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 173 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 174 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*     */     
/* 177 */     if (debug2 == Direction.DOWN) {
/* 178 */       return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */     }
/*     */     
/* 181 */     if (debug2 == Direction.UP) {
/* 182 */       return topUpdate((LevelReader)debug4, debug1, debug6, debug3);
/*     */     }
/*     */     
/* 185 */     return sideUpdate((LevelReader)debug4, debug5, debug1, debug6, debug3, debug2);
/*     */   }
/*     */   
/*     */   private static boolean isConnected(BlockState debug0, Property<WallSide> debug1) {
/* 189 */     return (debug0.getValue(debug1) != WallSide.NONE);
/*     */   }
/*     */   
/*     */   private static boolean isCovered(VoxelShape debug0, VoxelShape debug1) {
/* 193 */     return !Shapes.joinIsNotEmpty(debug1, debug0, BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   private BlockState topUpdate(LevelReader debug1, BlockState debug2, BlockPos debug3, BlockState debug4) {
/* 197 */     boolean debug5 = isConnected(debug2, (Property<WallSide>)NORTH_WALL);
/* 198 */     boolean debug6 = isConnected(debug2, (Property<WallSide>)EAST_WALL);
/* 199 */     boolean debug7 = isConnected(debug2, (Property<WallSide>)SOUTH_WALL);
/* 200 */     boolean debug8 = isConnected(debug2, (Property<WallSide>)WEST_WALL);
/*     */     
/* 202 */     return updateShape(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8);
/*     */   }
/*     */   
/*     */   private BlockState sideUpdate(LevelReader debug1, BlockPos debug2, BlockState debug3, BlockPos debug4, BlockState debug5, Direction debug6) {
/* 206 */     Direction debug7 = debug6.getOpposite();
/* 207 */     boolean debug8 = (debug6 == Direction.NORTH) ? connectsTo(debug5, debug5.isFaceSturdy((BlockGetter)debug1, debug4, debug7), debug7) : isConnected(debug3, (Property<WallSide>)NORTH_WALL);
/* 208 */     boolean debug9 = (debug6 == Direction.EAST) ? connectsTo(debug5, debug5.isFaceSturdy((BlockGetter)debug1, debug4, debug7), debug7) : isConnected(debug3, (Property<WallSide>)EAST_WALL);
/* 209 */     boolean debug10 = (debug6 == Direction.SOUTH) ? connectsTo(debug5, debug5.isFaceSturdy((BlockGetter)debug1, debug4, debug7), debug7) : isConnected(debug3, (Property<WallSide>)SOUTH_WALL);
/* 210 */     boolean debug11 = (debug6 == Direction.WEST) ? connectsTo(debug5, debug5.isFaceSturdy((BlockGetter)debug1, debug4, debug7), debug7) : isConnected(debug3, (Property<WallSide>)WEST_WALL);
/*     */     
/* 212 */     BlockPos debug12 = debug2.above();
/* 213 */     BlockState debug13 = debug1.getBlockState(debug12);
/* 214 */     return updateShape(debug1, debug3, debug12, debug13, debug8, debug9, debug10, debug11);
/*     */   }
/*     */   
/*     */   private BlockState updateShape(LevelReader debug1, BlockState debug2, BlockPos debug3, BlockState debug4, boolean debug5, boolean debug6, boolean debug7, boolean debug8) {
/* 218 */     VoxelShape debug9 = debug4.getCollisionShape((BlockGetter)debug1, debug3).getFaceShape(Direction.DOWN);
/* 219 */     BlockState debug10 = updateSides(debug2, debug5, debug6, debug7, debug8, debug9);
/*     */     
/* 221 */     return (BlockState)debug10.setValue((Property)UP, Boolean.valueOf(shouldRaisePost(debug10, debug4, debug9)));
/*     */   }
/*     */   
/*     */   private boolean shouldRaisePost(BlockState debug1, BlockState debug2, VoxelShape debug3) {
/* 225 */     boolean debug4 = (debug2.getBlock() instanceof WallBlock && ((Boolean)debug2.getValue((Property)UP)).booleanValue());
/* 226 */     if (debug4) {
/* 227 */       return true;
/*     */     }
/*     */     
/* 230 */     WallSide debug5 = (WallSide)debug1.getValue((Property)NORTH_WALL);
/* 231 */     WallSide debug6 = (WallSide)debug1.getValue((Property)SOUTH_WALL);
/* 232 */     WallSide debug7 = (WallSide)debug1.getValue((Property)EAST_WALL);
/* 233 */     WallSide debug8 = (WallSide)debug1.getValue((Property)WEST_WALL);
/*     */     
/* 235 */     boolean debug9 = (debug6 == WallSide.NONE);
/* 236 */     boolean debug10 = (debug8 == WallSide.NONE);
/* 237 */     boolean debug11 = (debug7 == WallSide.NONE);
/* 238 */     boolean debug12 = (debug5 == WallSide.NONE);
/*     */     
/* 240 */     boolean debug13 = ((debug12 && debug9 && debug10 && debug11) || debug12 != debug9 || debug10 != debug11);
/*     */ 
/*     */     
/* 243 */     if (debug13) {
/* 244 */       return true;
/*     */     }
/*     */     
/* 247 */     boolean debug14 = ((debug5 == WallSide.TALL && debug6 == WallSide.TALL) || (debug7 == WallSide.TALL && debug8 == WallSide.TALL));
/*     */ 
/*     */     
/* 250 */     if (debug14) {
/* 251 */       return false;
/*     */     }
/*     */     
/* 254 */     return (debug2.getBlock().is((Tag<Block>)BlockTags.WALL_POST_OVERRIDE) || isCovered(debug3, POST_TEST));
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockState updateSides(BlockState debug1, boolean debug2, boolean debug3, boolean debug4, boolean debug5, VoxelShape debug6) {
/* 259 */     return (BlockState)((BlockState)((BlockState)((BlockState)debug1
/* 260 */       .setValue((Property)NORTH_WALL, (Comparable)makeWallState(debug2, debug6, NORTH_TEST)))
/* 261 */       .setValue((Property)EAST_WALL, (Comparable)makeWallState(debug3, debug6, EAST_TEST)))
/* 262 */       .setValue((Property)SOUTH_WALL, (Comparable)makeWallState(debug4, debug6, SOUTH_TEST)))
/* 263 */       .setValue((Property)WEST_WALL, (Comparable)makeWallState(debug5, debug6, WEST_TEST));
/*     */   }
/*     */   
/*     */   private WallSide makeWallState(boolean debug1, VoxelShape debug2, VoxelShape debug3) {
/* 267 */     if (debug1) {
/* 268 */       if (isCovered(debug2, debug3)) {
/* 269 */         return WallSide.TALL;
/*     */       }
/* 271 */       return WallSide.LOW;
/*     */     } 
/*     */     
/* 274 */     return WallSide.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 280 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 281 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 283 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 288 */     return !((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 293 */     debug1.add(new Property[] { (Property)UP, (Property)NORTH_WALL, (Property)EAST_WALL, (Property)WEST_WALL, (Property)SOUTH_WALL, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 298 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 300 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH_WALL, debug1.getValue((Property)SOUTH_WALL))).setValue((Property)EAST_WALL, debug1.getValue((Property)WEST_WALL))).setValue((Property)SOUTH_WALL, debug1.getValue((Property)NORTH_WALL))).setValue((Property)WEST_WALL, debug1.getValue((Property)EAST_WALL));
/*     */       case FRONT_BACK:
/* 302 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH_WALL, debug1.getValue((Property)EAST_WALL))).setValue((Property)EAST_WALL, debug1.getValue((Property)SOUTH_WALL))).setValue((Property)SOUTH_WALL, debug1.getValue((Property)WEST_WALL))).setValue((Property)WEST_WALL, debug1.getValue((Property)NORTH_WALL));
/*     */       case null:
/* 304 */         return (BlockState)((BlockState)((BlockState)((BlockState)debug1.setValue((Property)NORTH_WALL, debug1.getValue((Property)WEST_WALL))).setValue((Property)EAST_WALL, debug1.getValue((Property)NORTH_WALL))).setValue((Property)SOUTH_WALL, debug1.getValue((Property)EAST_WALL))).setValue((Property)WEST_WALL, debug1.getValue((Property)SOUTH_WALL));
/*     */     } 
/* 306 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 312 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 314 */         return (BlockState)((BlockState)debug1.setValue((Property)NORTH_WALL, debug1.getValue((Property)SOUTH_WALL))).setValue((Property)SOUTH_WALL, debug1.getValue((Property)NORTH_WALL));
/*     */       case FRONT_BACK:
/* 316 */         return (BlockState)((BlockState)debug1.setValue((Property)EAST_WALL, debug1.getValue((Property)WEST_WALL))).setValue((Property)WEST_WALL, debug1.getValue((Property)EAST_WALL));
/*     */     } 
/*     */ 
/*     */     
/* 320 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WallBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */