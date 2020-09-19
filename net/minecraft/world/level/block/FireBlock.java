/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FireBlock extends BaseFireBlock {
/*  32 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*  34 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  35 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  36 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  37 */   public static final BooleanProperty WEST = PipeBlock.WEST; private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*  38 */   public static final BooleanProperty UP = PipeBlock.UP;
/*     */   static {
/*  40 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(debug0 -> (debug0.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*  42 */   private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  43 */   private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
/*  44 */   private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  45 */   private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
/*  46 */   private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<BlockState, VoxelShape> shapesCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private final Object2IntMap<Block> flameOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*  62 */   private final Object2IntMap<Block> burnOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*     */   
/*     */   public FireBlock(BlockBehaviour.Properties debug1) {
/*  65 */     super(debug1, 1.0F);
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false)));
/*     */     
/*  68 */     this.shapesCache = (Map<BlockState, VoxelShape>)ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().filter(debug0 -> (((Integer)debug0.getValue((Property)AGE)).intValue() == 0)).collect(Collectors.toMap(Function.identity(), FireBlock::calculateShape)));
/*     */   }
/*     */   
/*     */   private static VoxelShape calculateShape(BlockState debug0) {
/*  72 */     VoxelShape debug1 = Shapes.empty();
/*  73 */     if (((Boolean)debug0.getValue((Property)UP)).booleanValue()) {
/*  74 */       debug1 = UP_AABB;
/*     */     }
/*  76 */     if (((Boolean)debug0.getValue((Property)NORTH)).booleanValue()) {
/*  77 */       debug1 = Shapes.or(debug1, NORTH_AABB);
/*     */     }
/*  79 */     if (((Boolean)debug0.getValue((Property)SOUTH)).booleanValue()) {
/*  80 */       debug1 = Shapes.or(debug1, SOUTH_AABB);
/*     */     }
/*  82 */     if (((Boolean)debug0.getValue((Property)EAST)).booleanValue()) {
/*  83 */       debug1 = Shapes.or(debug1, EAST_AABB);
/*     */     }
/*  85 */     if (((Boolean)debug0.getValue((Property)WEST)).booleanValue()) {
/*  86 */       debug1 = Shapes.or(debug1, WEST_AABB);
/*     */     }
/*  88 */     return debug1.isEmpty() ? DOWN_AABB : debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  93 */     if (canSurvive(debug1, (LevelReader)debug4, debug5)) {
/*  94 */       return getStateWithAge(debug4, debug5, ((Integer)debug1.getValue((Property)AGE)).intValue());
/*     */     }
/*     */     
/*  97 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 102 */     return this.shapesCache.get(debug1.setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 107 */     return getStateForPlacement((BlockGetter)debug1.getLevel(), debug1.getClickedPos());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState getStateForPlacement(BlockGetter debug1, BlockPos debug2) {
/* 113 */     BlockPos debug3 = debug2.below();
/* 114 */     BlockState debug4 = debug1.getBlockState(debug3);
/* 115 */     if (canBurn(debug4) || debug4.isFaceSturdy(debug1, debug3, Direction.UP)) {
/* 116 */       return defaultBlockState();
/*     */     }
/*     */     
/* 119 */     BlockState debug5 = defaultBlockState();
/* 120 */     for (Direction debug9 : Direction.values()) {
/* 121 */       BooleanProperty debug10 = PROPERTY_BY_DIRECTION.get(debug9);
/* 122 */       if (debug10 != null) {
/* 123 */         debug5 = (BlockState)debug5.setValue((Property)debug10, Boolean.valueOf(canBurn(debug1.getBlockState(debug2.relative(debug9)))));
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return debug5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 132 */     BlockPos debug4 = debug3.below();
/* 133 */     return (debug2.getBlockState(debug4).isFaceSturdy((BlockGetter)debug2, debug4, Direction.UP) || isValidFireLocation((BlockGetter)debug2, debug3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 139 */     debug2.getBlockTicks().scheduleTick(debug3, this, getFireTickDelay(debug2.random));
/*     */     
/* 141 */     if (!debug2.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
/*     */       return;
/*     */     }
/*     */     
/* 145 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 146 */       debug2.removeBlock(debug3, false);
/*     */     }
/*     */     
/* 149 */     BlockState debug5 = debug2.getBlockState(debug3.below());
/* 150 */     boolean debug6 = debug5.is(debug2.dimensionType().infiniburn());
/*     */     
/* 152 */     int debug7 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/* 153 */     if (!debug6 && debug2.isRaining() && isNearRain((Level)debug2, debug3) && debug4.nextFloat() < 0.2F + debug7 * 0.03F) {
/* 154 */       debug2.removeBlock(debug3, false);
/*     */       
/*     */       return;
/*     */     } 
/* 158 */     int debug8 = Math.min(15, debug7 + debug4.nextInt(3) / 2);
/* 159 */     if (debug7 != debug8) {
/* 160 */       debug1 = (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug8));
/* 161 */       debug2.setBlock(debug3, debug1, 4);
/*     */     } 
/*     */     
/* 164 */     if (!debug6) {
/* 165 */       if (!isValidFireLocation((BlockGetter)debug2, debug3)) {
/* 166 */         BlockPos blockPos = debug3.below();
/* 167 */         if (!debug2.getBlockState(blockPos).isFaceSturdy((BlockGetter)debug2, blockPos, Direction.UP) || debug7 > 3) {
/* 168 */           debug2.removeBlock(debug3, false);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 174 */       if (debug7 == 15 && debug4.nextInt(4) == 0 && !canBurn(debug2.getBlockState(debug3.below()))) {
/* 175 */         debug2.removeBlock(debug3, false);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 180 */     boolean debug9 = debug2.isHumidAt(debug3);
/* 181 */     int debug10 = debug9 ? -50 : 0;
/*     */     
/* 183 */     checkBurnOut((Level)debug2, debug3.east(), 300 + debug10, debug4, debug7);
/* 184 */     checkBurnOut((Level)debug2, debug3.west(), 300 + debug10, debug4, debug7);
/* 185 */     checkBurnOut((Level)debug2, debug3.below(), 250 + debug10, debug4, debug7);
/* 186 */     checkBurnOut((Level)debug2, debug3.above(), 250 + debug10, debug4, debug7);
/* 187 */     checkBurnOut((Level)debug2, debug3.north(), 300 + debug10, debug4, debug7);
/* 188 */     checkBurnOut((Level)debug2, debug3.south(), 300 + debug10, debug4, debug7);
/*     */     
/* 190 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/* 191 */     for (int debug12 = -1; debug12 <= 1; debug12++) {
/* 192 */       for (int debug13 = -1; debug13 <= 1; debug13++) {
/* 193 */         for (int debug14 = -1; debug14 <= 4; debug14++) {
/* 194 */           if (debug12 != 0 || debug14 != 0 || debug13 != 0) {
/*     */ 
/*     */ 
/*     */             
/* 198 */             int debug15 = 100;
/* 199 */             if (debug14 > 1) {
/* 200 */               debug15 += (debug14 - 1) * 100;
/*     */             }
/*     */             
/* 203 */             debug11.setWithOffset((Vec3i)debug3, debug12, debug14, debug13);
/* 204 */             int debug16 = getFireOdds((LevelReader)debug2, (BlockPos)debug11);
/* 205 */             if (debug16 > 0) {
/*     */ 
/*     */ 
/*     */               
/* 209 */               int debug17 = (debug16 + 40 + debug2.getDifficulty().getId() * 7) / (debug7 + 30);
/* 210 */               if (debug9) {
/* 211 */                 debug17 /= 2;
/*     */               }
/* 213 */               if (debug17 > 0 && debug4.nextInt(debug15) <= debug17 && (
/* 214 */                 !debug2.isRaining() || !isNearRain((Level)debug2, (BlockPos)debug11))) {
/*     */ 
/*     */ 
/*     */                 
/* 218 */                 int debug18 = Math.min(15, debug7 + debug4.nextInt(5) / 4);
/* 219 */                 debug2.setBlock((BlockPos)debug11, getStateWithAge((LevelAccessor)debug2, (BlockPos)debug11, debug18), 3);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } protected boolean isNearRain(Level debug1, BlockPos debug2) {
/* 227 */     return (debug1.isRainingAt(debug2) || debug1.isRainingAt(debug2.west()) || debug1.isRainingAt(debug2.east()) || debug1.isRainingAt(debug2.north()) || debug1.isRainingAt(debug2.south()));
/*     */   }
/*     */   
/*     */   private int getBurnOdd(BlockState debug1) {
/* 231 */     if (debug1.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)debug1.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 232 */       return 0;
/*     */     }
/* 234 */     return this.burnOdds.getInt(debug1.getBlock());
/*     */   }
/*     */   
/*     */   private int getFlameOdds(BlockState debug1) {
/* 238 */     if (debug1.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)debug1.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 239 */       return 0;
/*     */     }
/* 241 */     return this.flameOdds.getInt(debug1.getBlock());
/*     */   }
/*     */   
/*     */   private void checkBurnOut(Level debug1, BlockPos debug2, int debug3, Random debug4, int debug5) {
/* 245 */     int debug6 = getBurnOdd(debug1.getBlockState(debug2));
/* 246 */     if (debug4.nextInt(debug3) < debug6) {
/* 247 */       BlockState debug7 = debug1.getBlockState(debug2);
/*     */       
/* 249 */       if (debug4.nextInt(debug5 + 10) < 5 && !debug1.isRainingAt(debug2)) {
/* 250 */         int i = Math.min(debug5 + debug4.nextInt(5) / 4, 15);
/* 251 */         debug1.setBlock(debug2, getStateWithAge((LevelAccessor)debug1, debug2, i), 3);
/*     */       } else {
/* 253 */         debug1.removeBlock(debug2, false);
/*     */       } 
/*     */       
/* 256 */       Block debug8 = debug7.getBlock();
/* 257 */       if (debug8 instanceof TntBlock) {
/* 258 */         (TntBlock)debug8; TntBlock.explode(debug1, debug2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState getStateWithAge(LevelAccessor debug1, BlockPos debug2, int debug3) {
/* 264 */     BlockState debug4 = getState((BlockGetter)debug1, debug2);
/* 265 */     if (debug4.is(Blocks.FIRE)) {
/* 266 */       return (BlockState)debug4.setValue((Property)AGE, Integer.valueOf(debug3));
/*     */     }
/*     */     
/* 269 */     return debug4;
/*     */   }
/*     */   
/*     */   private boolean isValidFireLocation(BlockGetter debug1, BlockPos debug2) {
/* 273 */     for (Direction debug6 : Direction.values()) {
/* 274 */       if (canBurn(debug1.getBlockState(debug2.relative(debug6)))) {
/* 275 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 279 */     return false;
/*     */   }
/*     */   
/*     */   private int getFireOdds(LevelReader debug1, BlockPos debug2) {
/* 283 */     if (!debug1.isEmptyBlock(debug2)) {
/* 284 */       return 0;
/*     */     }
/*     */     
/* 287 */     int debug3 = 0;
/* 288 */     for (Direction debug7 : Direction.values()) {
/* 289 */       BlockState debug8 = debug1.getBlockState(debug2.relative(debug7));
/* 290 */       debug3 = Math.max(getFlameOdds(debug8), debug3);
/*     */     } 
/*     */     
/* 293 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBurn(BlockState debug1) {
/* 298 */     return (getFlameOdds(debug1) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 303 */     super.onPlace(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 305 */     debug2.getBlockTicks().scheduleTick(debug3, this, getFireTickDelay(debug2.random));
/*     */   }
/*     */   
/*     */   private static int getFireTickDelay(Random debug0) {
/* 309 */     return 30 + debug0.nextInt(10);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 314 */     debug1.add(new Property[] { (Property)AGE, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP });
/*     */   }
/*     */   
/*     */   private void setFlammable(Block debug1, int debug2, int debug3) {
/* 318 */     this.flameOdds.put(debug1, debug2);
/* 319 */     this.burnOdds.put(debug1, debug3);
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 323 */     FireBlock debug0 = (FireBlock)Blocks.FIRE;
/* 324 */     debug0.setFlammable(Blocks.OAK_PLANKS, 5, 20);
/* 325 */     debug0.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
/* 326 */     debug0.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
/* 327 */     debug0.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
/* 328 */     debug0.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
/* 329 */     debug0.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
/* 330 */     debug0.setFlammable(Blocks.OAK_SLAB, 5, 20);
/* 331 */     debug0.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
/* 332 */     debug0.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
/* 333 */     debug0.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
/* 334 */     debug0.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
/* 335 */     debug0.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
/* 336 */     debug0.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
/* 337 */     debug0.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
/* 338 */     debug0.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
/* 339 */     debug0.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
/* 340 */     debug0.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
/* 341 */     debug0.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
/* 342 */     debug0.setFlammable(Blocks.OAK_FENCE, 5, 20);
/* 343 */     debug0.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
/* 344 */     debug0.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
/* 345 */     debug0.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
/* 346 */     debug0.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
/* 347 */     debug0.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
/* 348 */     debug0.setFlammable(Blocks.OAK_STAIRS, 5, 20);
/* 349 */     debug0.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
/* 350 */     debug0.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
/* 351 */     debug0.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
/* 352 */     debug0.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
/* 353 */     debug0.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
/* 354 */     debug0.setFlammable(Blocks.OAK_LOG, 5, 5);
/* 355 */     debug0.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
/* 356 */     debug0.setFlammable(Blocks.BIRCH_LOG, 5, 5);
/* 357 */     debug0.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
/* 358 */     debug0.setFlammable(Blocks.ACACIA_LOG, 5, 5);
/* 359 */     debug0.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
/* 360 */     debug0.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
/* 361 */     debug0.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
/* 362 */     debug0.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
/* 363 */     debug0.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
/* 364 */     debug0.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
/* 365 */     debug0.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
/* 366 */     debug0.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
/* 367 */     debug0.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
/* 368 */     debug0.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
/* 369 */     debug0.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
/* 370 */     debug0.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
/* 371 */     debug0.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
/* 372 */     debug0.setFlammable(Blocks.OAK_WOOD, 5, 5);
/* 373 */     debug0.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
/* 374 */     debug0.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
/* 375 */     debug0.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
/* 376 */     debug0.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
/* 377 */     debug0.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
/* 378 */     debug0.setFlammable(Blocks.OAK_LEAVES, 30, 60);
/* 379 */     debug0.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
/* 380 */     debug0.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
/* 381 */     debug0.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
/* 382 */     debug0.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
/* 383 */     debug0.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
/* 384 */     debug0.setFlammable(Blocks.BOOKSHELF, 30, 20);
/* 385 */     debug0.setFlammable(Blocks.TNT, 15, 100);
/* 386 */     debug0.setFlammable(Blocks.GRASS, 60, 100);
/* 387 */     debug0.setFlammable(Blocks.FERN, 60, 100);
/* 388 */     debug0.setFlammable(Blocks.DEAD_BUSH, 60, 100);
/* 389 */     debug0.setFlammable(Blocks.SUNFLOWER, 60, 100);
/* 390 */     debug0.setFlammable(Blocks.LILAC, 60, 100);
/* 391 */     debug0.setFlammable(Blocks.ROSE_BUSH, 60, 100);
/* 392 */     debug0.setFlammable(Blocks.PEONY, 60, 100);
/* 393 */     debug0.setFlammable(Blocks.TALL_GRASS, 60, 100);
/* 394 */     debug0.setFlammable(Blocks.LARGE_FERN, 60, 100);
/* 395 */     debug0.setFlammable(Blocks.DANDELION, 60, 100);
/* 396 */     debug0.setFlammable(Blocks.POPPY, 60, 100);
/* 397 */     debug0.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
/* 398 */     debug0.setFlammable(Blocks.ALLIUM, 60, 100);
/* 399 */     debug0.setFlammable(Blocks.AZURE_BLUET, 60, 100);
/* 400 */     debug0.setFlammable(Blocks.RED_TULIP, 60, 100);
/* 401 */     debug0.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
/* 402 */     debug0.setFlammable(Blocks.WHITE_TULIP, 60, 100);
/* 403 */     debug0.setFlammable(Blocks.PINK_TULIP, 60, 100);
/* 404 */     debug0.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
/* 405 */     debug0.setFlammable(Blocks.CORNFLOWER, 60, 100);
/* 406 */     debug0.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
/* 407 */     debug0.setFlammable(Blocks.WITHER_ROSE, 60, 100);
/* 408 */     debug0.setFlammable(Blocks.WHITE_WOOL, 30, 60);
/* 409 */     debug0.setFlammable(Blocks.ORANGE_WOOL, 30, 60);
/* 410 */     debug0.setFlammable(Blocks.MAGENTA_WOOL, 30, 60);
/* 411 */     debug0.setFlammable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
/* 412 */     debug0.setFlammable(Blocks.YELLOW_WOOL, 30, 60);
/* 413 */     debug0.setFlammable(Blocks.LIME_WOOL, 30, 60);
/* 414 */     debug0.setFlammable(Blocks.PINK_WOOL, 30, 60);
/* 415 */     debug0.setFlammable(Blocks.GRAY_WOOL, 30, 60);
/* 416 */     debug0.setFlammable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
/* 417 */     debug0.setFlammable(Blocks.CYAN_WOOL, 30, 60);
/* 418 */     debug0.setFlammable(Blocks.PURPLE_WOOL, 30, 60);
/* 419 */     debug0.setFlammable(Blocks.BLUE_WOOL, 30, 60);
/* 420 */     debug0.setFlammable(Blocks.BROWN_WOOL, 30, 60);
/* 421 */     debug0.setFlammable(Blocks.GREEN_WOOL, 30, 60);
/* 422 */     debug0.setFlammable(Blocks.RED_WOOL, 30, 60);
/* 423 */     debug0.setFlammable(Blocks.BLACK_WOOL, 30, 60);
/* 424 */     debug0.setFlammable(Blocks.VINE, 15, 100);
/* 425 */     debug0.setFlammable(Blocks.COAL_BLOCK, 5, 5);
/* 426 */     debug0.setFlammable(Blocks.HAY_BLOCK, 60, 20);
/* 427 */     debug0.setFlammable(Blocks.TARGET, 15, 20);
/* 428 */     debug0.setFlammable(Blocks.WHITE_CARPET, 60, 20);
/* 429 */     debug0.setFlammable(Blocks.ORANGE_CARPET, 60, 20);
/* 430 */     debug0.setFlammable(Blocks.MAGENTA_CARPET, 60, 20);
/* 431 */     debug0.setFlammable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
/* 432 */     debug0.setFlammable(Blocks.YELLOW_CARPET, 60, 20);
/* 433 */     debug0.setFlammable(Blocks.LIME_CARPET, 60, 20);
/* 434 */     debug0.setFlammable(Blocks.PINK_CARPET, 60, 20);
/* 435 */     debug0.setFlammable(Blocks.GRAY_CARPET, 60, 20);
/* 436 */     debug0.setFlammable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
/* 437 */     debug0.setFlammable(Blocks.CYAN_CARPET, 60, 20);
/* 438 */     debug0.setFlammable(Blocks.PURPLE_CARPET, 60, 20);
/* 439 */     debug0.setFlammable(Blocks.BLUE_CARPET, 60, 20);
/* 440 */     debug0.setFlammable(Blocks.BROWN_CARPET, 60, 20);
/* 441 */     debug0.setFlammable(Blocks.GREEN_CARPET, 60, 20);
/* 442 */     debug0.setFlammable(Blocks.RED_CARPET, 60, 20);
/* 443 */     debug0.setFlammable(Blocks.BLACK_CARPET, 60, 20);
/* 444 */     debug0.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
/* 445 */     debug0.setFlammable(Blocks.BAMBOO, 60, 60);
/* 446 */     debug0.setFlammable(Blocks.SCAFFOLDING, 60, 60);
/* 447 */     debug0.setFlammable(Blocks.LECTERN, 30, 20);
/* 448 */     debug0.setFlammable(Blocks.COMPOSTER, 5, 20);
/* 449 */     debug0.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
/* 450 */     debug0.setFlammable(Blocks.BEEHIVE, 5, 20);
/* 451 */     debug0.setFlammable(Blocks.BEE_NEST, 30, 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FireBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */