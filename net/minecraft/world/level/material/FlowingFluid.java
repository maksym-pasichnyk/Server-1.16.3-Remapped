/*     */ package net.minecraft.world.level.material;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class FlowingFluid extends Fluid {
/*  33 */   public static final BooleanProperty FALLING = BlockStateProperties.FALLING;
/*  34 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_FLOWING; private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE;
/*     */   
/*     */   static {
/*  37 */     OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
/*     */           Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> debug0 = new Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>(200)
/*     */             {
/*     */               protected void rehash(int debug1) {}
/*     */             };
/*     */           debug0.defaultReturnValue(127);
/*     */           return debug0;
/*     */         });
/*     */   }
/*     */   
/*  47 */   private final Map<FluidState, VoxelShape> shapes = Maps.newIdentityHashMap();
/*     */ 
/*     */   
/*     */   protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> debug1) {
/*  51 */     debug1.add(new Property[] { (Property)FALLING });
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getFlow(BlockGetter debug1, BlockPos debug2, FluidState debug3) {
/*  56 */     double debug4 = 0.0D;
/*  57 */     double debug6 = 0.0D;
/*     */     
/*  59 */     BlockPos.MutableBlockPos debug8 = new BlockPos.MutableBlockPos();
/*  60 */     for (Direction debug10 : Direction.Plane.HORIZONTAL) {
/*  61 */       debug8.setWithOffset((Vec3i)debug2, debug10);
/*  62 */       FluidState debug11 = debug1.getFluidState((BlockPos)debug8);
/*  63 */       if (!affectsFlow(debug11)) {
/*     */         continue;
/*     */       }
/*  66 */       float debug12 = debug11.getOwnHeight();
/*  67 */       float debug13 = 0.0F;
/*  68 */       if (debug12 == 0.0F) {
/*  69 */         if (!debug1.getBlockState((BlockPos)debug8).getMaterial().blocksMotion()) {
/*  70 */           BlockPos debug14 = debug8.below();
/*  71 */           FluidState debug15 = debug1.getFluidState(debug14);
/*  72 */           if (affectsFlow(debug15)) {
/*  73 */             debug12 = debug15.getOwnHeight();
/*  74 */             if (debug12 > 0.0F) {
/*  75 */               debug13 = debug3.getOwnHeight() - debug12 - 0.8888889F;
/*     */             }
/*     */           } 
/*     */         } 
/*  79 */       } else if (debug12 > 0.0F) {
/*  80 */         debug13 = debug3.getOwnHeight() - debug12;
/*     */       } 
/*     */       
/*  83 */       if (debug13 != 0.0F) {
/*  84 */         debug4 += (debug10.getStepX() * debug13);
/*  85 */         debug6 += (debug10.getStepZ() * debug13);
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     Vec3 debug9 = new Vec3(debug4, 0.0D, debug6);
/*  90 */     if (((Boolean)debug3.getValue((Property)FALLING)).booleanValue()) {
/*  91 */       for (Direction debug11 : Direction.Plane.HORIZONTAL) {
/*  92 */         debug8.setWithOffset((Vec3i)debug2, debug11);
/*  93 */         if (isSolidFace(debug1, (BlockPos)debug8, debug11) || isSolidFace(debug1, debug8.above(), debug11)) {
/*  94 */           debug9 = debug9.normalize().add(0.0D, -6.0D, 0.0D);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 100 */     return debug9.normalize();
/*     */   }
/*     */   
/*     */   private boolean affectsFlow(FluidState debug1) {
/* 104 */     return (debug1.isEmpty() || debug1.getType().isSame(this));
/*     */   }
/*     */   
/*     */   protected boolean isSolidFace(BlockGetter debug1, BlockPos debug2, Direction debug3) {
/* 108 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 109 */     FluidState debug5 = debug1.getFluidState(debug2);
/* 110 */     if (debug5.getType().isSame(this)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (debug3 == Direction.UP) {
/* 114 */       return true;
/*     */     }
/* 116 */     if (debug4.getMaterial() == Material.ICE) {
/* 117 */       return false;
/*     */     }
/*     */     
/* 120 */     return debug4.isFaceSturdy(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   protected void spread(LevelAccessor debug1, BlockPos debug2, FluidState debug3) {
/* 124 */     if (debug3.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 129 */     BlockPos debug5 = debug2.below();
/* 130 */     BlockState debug6 = debug1.getBlockState(debug5);
/*     */     
/* 132 */     FluidState debug7 = getNewLiquid((LevelReader)debug1, debug5, debug6);
/* 133 */     if (canSpreadTo((BlockGetter)debug1, debug2, debug4, Direction.DOWN, debug5, debug6, debug1.getFluidState(debug5), debug7.getType())) {
/* 134 */       spreadTo(debug1, debug5, debug6, Direction.DOWN, debug7);
/*     */       
/* 136 */       if (sourceNeighborCount((LevelReader)debug1, debug2) >= 3) {
/* 137 */         spreadToSides(debug1, debug2, debug3, debug4);
/*     */       }
/* 139 */     } else if (debug3.isSource() || !isWaterHole((BlockGetter)debug1, debug7.getType(), debug2, debug4, debug5, debug6)) {
/* 140 */       spreadToSides(debug1, debug2, debug3, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spreadToSides(LevelAccessor debug1, BlockPos debug2, FluidState debug3, BlockState debug4) {
/* 145 */     int debug5 = debug3.getAmount() - getDropOff((LevelReader)debug1);
/* 146 */     if (((Boolean)debug3.getValue((Property)FALLING)).booleanValue()) {
/* 147 */       debug5 = 7;
/*     */     }
/* 149 */     if (debug5 <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 153 */     Map<Direction, FluidState> debug6 = getSpread((LevelReader)debug1, debug2, debug4);
/* 154 */     for (Map.Entry<Direction, FluidState> debug8 : debug6.entrySet()) {
/* 155 */       Direction debug9 = debug8.getKey();
/* 156 */       FluidState debug10 = debug8.getValue();
/* 157 */       BlockPos debug11 = debug2.relative(debug9);
/* 158 */       BlockState debug12 = debug1.getBlockState(debug11);
/* 159 */       if (canSpreadTo((BlockGetter)debug1, debug2, debug4, debug9, debug11, debug12, debug1.getFluidState(debug11), debug10.getType())) {
/* 160 */         spreadTo(debug1, debug11, debug12, debug9, debug10);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected FluidState getNewLiquid(LevelReader debug1, BlockPos debug2, BlockState debug3) {
/* 166 */     int debug4 = 0;
/* 167 */     int debug5 = 0;
/*     */     
/* 169 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 170 */       BlockPos blockPos = debug2.relative(direction);
/* 171 */       BlockState blockState = debug1.getBlockState(blockPos);
/* 172 */       FluidState debug10 = blockState.getFluidState();
/*     */       
/* 174 */       if (debug10.getType().isSame(this) && 
/* 175 */         canPassThroughWall(direction, (BlockGetter)debug1, debug2, debug3, blockPos, blockState)) {
/* 176 */         if (debug10.isSource()) {
/* 177 */           debug5++;
/*     */         }
/* 179 */         debug4 = Math.max(debug4, debug10.getAmount());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (canConvertToSource() && debug5 >= 2) {
/* 186 */       BlockState blockState = debug1.getBlockState(debug2.below());
/* 187 */       FluidState fluidState = blockState.getFluidState();
/* 188 */       if (blockState.getMaterial().isSolid() || isSourceBlockOfThisType(fluidState)) {
/* 189 */         return getSource(false);
/*     */       }
/*     */     } 
/*     */     
/* 193 */     BlockPos debug6 = debug2.above();
/* 194 */     BlockState debug7 = debug1.getBlockState(debug6);
/* 195 */     FluidState debug8 = debug7.getFluidState();
/*     */     
/* 197 */     if (!debug8.isEmpty() && debug8.getType().isSame(this) && canPassThroughWall(Direction.UP, (BlockGetter)debug1, debug2, debug3, debug6, debug7)) {
/* 198 */       return getFlowing(8, true);
/*     */     }
/*     */     
/* 201 */     int debug9 = debug4 - getDropOff(debug1);
/* 202 */     if (debug9 <= 0) {
/* 203 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/* 205 */     return getFlowing(debug9, false);
/*     */   }
/*     */   private boolean canPassThroughWall(Direction debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, BlockPos debug5, BlockState debug6) {
/*     */     Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> debug7;
/*     */     Block.BlockStatePairKey debug8;
/* 210 */     if (debug4.getBlock().hasDynamicShape() || debug6.getBlock().hasDynamicShape()) {
/* 211 */       debug7 = null;
/*     */     } else {
/* 213 */       debug7 = OCCLUSION_CACHE.get();
/*     */     } 
/*     */ 
/*     */     
/* 217 */     if (debug7 != null) {
/* 218 */       debug8 = new Block.BlockStatePairKey(debug4, debug6, debug1);
/* 219 */       byte b = debug7.getAndMoveToFirst(debug8);
/* 220 */       if (b != Byte.MAX_VALUE) {
/* 221 */         return (b != 0);
/*     */       }
/*     */     } else {
/* 224 */       debug8 = null;
/*     */     } 
/*     */     
/* 227 */     VoxelShape debug9 = debug4.getCollisionShape(debug2, debug3);
/* 228 */     VoxelShape debug10 = debug6.getCollisionShape(debug2, debug5);
/* 229 */     boolean debug11 = !Shapes.mergedFaceOccludes(debug9, debug10, debug1);
/*     */     
/* 231 */     if (debug7 != null) {
/* 232 */       if (debug7.size() == 200) {
/* 233 */         debug7.removeLastByte();
/*     */       }
/* 235 */       debug7.putAndMoveToFirst(debug8, (byte)(debug11 ? 1 : 0));
/*     */     } 
/* 237 */     return debug11;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FluidState getFlowing(int debug1, boolean debug2) {
/* 243 */     return (FluidState)((FluidState)getFlowing().defaultFluidState().setValue((Property)LEVEL, Integer.valueOf(debug1))).setValue((Property)FALLING, Boolean.valueOf(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FluidState getSource(boolean debug1) {
/* 249 */     return (FluidState)getSource().defaultFluidState().setValue((Property)FALLING, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spreadTo(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Direction debug4, FluidState debug5) {
/* 255 */     if (debug3.getBlock() instanceof LiquidBlockContainer) {
/* 256 */       ((LiquidBlockContainer)debug3.getBlock()).placeLiquid(debug1, debug2, debug3, debug5);
/*     */     } else {
/* 258 */       if (!debug3.isAir()) {
/* 259 */         beforeDestroyingBlock(debug1, debug2, debug3);
/*     */       }
/* 261 */       debug1.setBlock(debug2, debug5.createLegacyBlock(), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static short getCacheKey(BlockPos debug0, BlockPos debug1) {
/* 268 */     int debug2 = debug1.getX() - debug0.getX();
/* 269 */     int debug3 = debug1.getZ() - debug0.getZ();
/* 270 */     return (short)((debug2 + 128 & 0xFF) << 8 | debug3 + 128 & 0xFF);
/*     */   }
/*     */   
/*     */   protected int getSlopeDistance(LevelReader debug1, BlockPos debug2, int debug3, Direction debug4, BlockState debug5, BlockPos debug6, Short2ObjectMap<Pair<BlockState, FluidState>> debug7, Short2BooleanMap debug8) {
/* 274 */     int debug9 = 1000;
/*     */     
/* 276 */     for (Direction debug11 : Direction.Plane.HORIZONTAL) {
/* 277 */       if (debug11 == debug4) {
/*     */         continue;
/*     */       }
/*     */       
/* 281 */       BlockPos debug12 = debug2.relative(debug11);
/*     */       
/* 283 */       short debug13 = getCacheKey(debug6, debug12);
/*     */       
/* 285 */       Pair<BlockState, FluidState> debug14 = (Pair<BlockState, FluidState>)debug7.computeIfAbsent(debug13, debug2 -> {
/*     */             BlockState debug3 = debug0.getBlockState(debug1);
/*     */             
/*     */             return Pair.of(debug3, debug3.getFluidState());
/*     */           });
/* 290 */       BlockState debug15 = (BlockState)debug14.getFirst();
/* 291 */       FluidState debug16 = (FluidState)debug14.getSecond();
/*     */ 
/*     */       
/* 294 */       if (canPassThrough((BlockGetter)debug1, getFlowing(), debug2, debug5, debug11, debug12, debug15, debug16)) {
/* 295 */         boolean debug17 = debug8.computeIfAbsent(debug13, debug4 -> {
/*     */               BlockPos debug5 = debug1.below();
/*     */               BlockState debug6 = debug2.getBlockState(debug5);
/*     */               return isWaterHole((BlockGetter)debug2, getFlowing(), debug1, debug3, debug5, debug6);
/*     */             });
/* 300 */         if (debug17) {
/* 301 */           return debug3;
/*     */         }
/* 303 */         if (debug3 < getSlopeFindDistance(debug1)) {
/* 304 */           int debug18 = getSlopeDistance(debug1, debug12, debug3 + 1, debug11.getOpposite(), debug15, debug6, debug7, debug8);
/* 305 */           if (debug18 < debug9) {
/* 306 */             debug9 = debug18;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 312 */     return debug9;
/*     */   }
/*     */   
/*     */   private boolean isWaterHole(BlockGetter debug1, Fluid debug2, BlockPos debug3, BlockState debug4, BlockPos debug5, BlockState debug6) {
/* 316 */     if (!canPassThroughWall(Direction.DOWN, debug1, debug3, debug4, debug5, debug6)) {
/* 317 */       return false;
/*     */     }
/*     */     
/* 320 */     if (debug6.getFluidState().getType().isSame(this)) {
/* 321 */       return true;
/*     */     }
/*     */     
/* 324 */     return canHoldFluid(debug1, debug5, debug6, debug2);
/*     */   }
/*     */   
/*     */   private boolean canPassThrough(BlockGetter debug1, Fluid debug2, BlockPos debug3, BlockState debug4, Direction debug5, BlockPos debug6, BlockState debug7, FluidState debug8) {
/* 328 */     return (!isSourceBlockOfThisType(debug8) && 
/* 329 */       canPassThroughWall(debug5, debug1, debug3, debug4, debug6, debug7) && 
/* 330 */       canHoldFluid(debug1, debug6, debug7, debug2));
/*     */   }
/*     */   
/*     */   private boolean isSourceBlockOfThisType(FluidState debug1) {
/* 334 */     return (debug1.getType().isSame(this) && debug1.isSource());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int sourceNeighborCount(LevelReader debug1, BlockPos debug2) {
/* 340 */     int debug3 = 0;
/* 341 */     for (Direction debug5 : Direction.Plane.HORIZONTAL) {
/* 342 */       BlockPos debug6 = debug2.relative(debug5);
/* 343 */       FluidState debug7 = debug1.getFluidState(debug6);
/*     */       
/* 345 */       if (isSourceBlockOfThisType(debug7)) {
/* 346 */         debug3++;
/*     */       }
/*     */     } 
/*     */     
/* 350 */     return debug3;
/*     */   }
/*     */   
/*     */   protected Map<Direction, FluidState> getSpread(LevelReader debug1, BlockPos debug2, BlockState debug3) {
/* 354 */     int debug4 = 1000;
/* 355 */     Map<Direction, FluidState> debug5 = Maps.newEnumMap(Direction.class);
/*     */     
/* 357 */     Short2ObjectOpenHashMap short2ObjectOpenHashMap = new Short2ObjectOpenHashMap();
/* 358 */     Short2BooleanOpenHashMap short2BooleanOpenHashMap = new Short2BooleanOpenHashMap();
/*     */     
/* 360 */     for (Direction debug9 : Direction.Plane.HORIZONTAL) {
/* 361 */       BlockPos debug10 = debug2.relative(debug9);
/*     */       
/* 363 */       short debug11 = getCacheKey(debug2, debug10);
/*     */       
/* 365 */       Pair<BlockState, FluidState> debug12 = (Pair<BlockState, FluidState>)short2ObjectOpenHashMap.computeIfAbsent(debug11, debug2 -> {
/*     */             BlockState debug3 = debug0.getBlockState(debug1);
/*     */             
/*     */             return Pair.of(debug3, debug3.getFluidState());
/*     */           });
/* 370 */       BlockState debug13 = (BlockState)debug12.getFirst();
/* 371 */       FluidState debug14 = (FluidState)debug12.getSecond();
/*     */       
/* 373 */       FluidState debug15 = getNewLiquid(debug1, debug10, debug13);
/*     */       
/* 375 */       if (canPassThrough((BlockGetter)debug1, debug15.getType(), debug2, debug3, debug9, debug10, debug13, debug14)) {
/*     */         int debug16;
/* 377 */         BlockPos debug17 = debug10.below();
/*     */         
/* 379 */         boolean debug18 = short2BooleanOpenHashMap.computeIfAbsent(debug11, debug5 -> {
/*     */               BlockState debug6 = debug1.getBlockState(debug2);
/*     */               
/*     */               return isWaterHole((BlockGetter)debug1, getFlowing(), debug3, debug4, debug2, debug6);
/*     */             });
/*     */         
/* 385 */         if (debug18) {
/* 386 */           debug16 = 0;
/*     */         } else {
/* 388 */           debug16 = getSlopeDistance(debug1, debug10, 1, debug9.getOpposite(), debug13, debug2, (Short2ObjectMap<Pair<BlockState, FluidState>>)short2ObjectOpenHashMap, (Short2BooleanMap)short2BooleanOpenHashMap);
/*     */         } 
/*     */         
/* 391 */         if (debug16 < debug4) {
/* 392 */           debug5.clear();
/*     */         }
/*     */         
/* 395 */         if (debug16 <= debug4) {
/* 396 */           debug5.put(debug9, debug15);
/* 397 */           debug4 = debug16;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 402 */     return debug5;
/*     */   }
/*     */   
/*     */   private boolean canHoldFluid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 406 */     Block debug5 = debug3.getBlock();
/*     */     
/* 408 */     if (debug5 instanceof LiquidBlockContainer) {
/* 409 */       return ((LiquidBlockContainer)debug5).canPlaceLiquid(debug1, debug2, debug3, debug4);
/*     */     }
/*     */     
/* 412 */     if (debug5 instanceof net.minecraft.world.level.block.DoorBlock || debug5
/* 413 */       .is((Tag)BlockTags.SIGNS) || debug5 == Blocks.LADDER || debug5 == Blocks.SUGAR_CANE || debug5 == Blocks.BUBBLE_COLUMN)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 418 */       return false;
/*     */     }
/* 420 */     Material debug6 = debug3.getMaterial();
/* 421 */     if (debug6 == Material.PORTAL || debug6 == Material.STRUCTURAL_AIR || debug6 == Material.WATER_PLANT || debug6 == Material.REPLACEABLE_WATER_PLANT) {
/* 422 */       return false;
/*     */     }
/*     */     
/* 425 */     return !debug6.blocksMotion();
/*     */   }
/*     */   
/*     */   protected boolean canSpreadTo(BlockGetter debug1, BlockPos debug2, BlockState debug3, Direction debug4, BlockPos debug5, BlockState debug6, FluidState debug7, Fluid debug8) {
/* 429 */     return (debug7.canBeReplacedWith(debug1, debug5, debug8, debug4) && 
/* 430 */       canPassThroughWall(debug4, debug1, debug2, debug3, debug5, debug6) && 
/* 431 */       canHoldFluid(debug1, debug5, debug6, debug8));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSpreadDelay(Level debug1, BlockPos debug2, FluidState debug3, FluidState debug4) {
/* 437 */     return getTickDelay((LevelReader)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(Level debug1, BlockPos debug2, FluidState debug3) {
/* 442 */     if (!debug3.isSource()) {
/* 443 */       FluidState debug4 = getNewLiquid((LevelReader)debug1, debug2, debug1.getBlockState(debug2));
/* 444 */       int debug5 = getSpreadDelay(debug1, debug2, debug3, debug4);
/*     */       
/* 446 */       if (debug4.isEmpty()) {
/* 447 */         debug3 = debug4;
/* 448 */         debug1.setBlock(debug2, Blocks.AIR.defaultBlockState(), 3);
/* 449 */       } else if (!debug4.equals(debug3)) {
/* 450 */         debug3 = debug4;
/* 451 */         BlockState debug6 = debug3.createLegacyBlock();
/* 452 */         debug1.setBlock(debug2, debug6, 2);
/* 453 */         debug1.getLiquidTicks().scheduleTick(debug2, debug3.getType(), debug5);
/* 454 */         debug1.updateNeighborsAt(debug2, debug6.getBlock());
/*     */       } 
/*     */     } 
/*     */     
/* 458 */     spread((LevelAccessor)debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   protected static int getLegacyLevel(FluidState debug0) {
/* 462 */     if (debug0.isSource()) {
/* 463 */       return 0;
/*     */     }
/* 465 */     return 8 - Math.min(debug0.getAmount(), 8) + (((Boolean)debug0.getValue((Property)FALLING)).booleanValue() ? 8 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean hasSameAbove(FluidState debug0, BlockGetter debug1, BlockPos debug2) {
/* 470 */     return debug0.getType().isSame(debug1.getFluidState(debug2.above()).getType());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight(FluidState debug1, BlockGetter debug2, BlockPos debug3) {
/* 475 */     if (hasSameAbove(debug1, debug2, debug3)) {
/* 476 */       return 1.0F;
/*     */     }
/* 478 */     return debug1.getOwnHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOwnHeight(FluidState debug1) {
/* 483 */     return debug1.getAmount() / 9.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(FluidState debug1, BlockGetter debug2, BlockPos debug3) {
/* 491 */     if (debug1.getAmount() == 9 && hasSameAbove(debug1, debug2, debug3)) {
/* 492 */       return Shapes.block();
/*     */     }
/*     */     
/* 495 */     return this.shapes.computeIfAbsent(debug1, debug2 -> Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, debug2.getHeight(debug0, debug1), 1.0D));
/*     */   }
/*     */   
/*     */   public abstract Fluid getFlowing();
/*     */   
/*     */   public abstract Fluid getSource();
/*     */   
/*     */   protected abstract boolean canConvertToSource();
/*     */   
/*     */   protected abstract void beforeDestroyingBlock(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState);
/*     */   
/*     */   protected abstract int getSlopeFindDistance(LevelReader paramLevelReader);
/*     */   
/*     */   protected abstract int getDropOff(LevelReader paramLevelReader);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\FlowingFluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */