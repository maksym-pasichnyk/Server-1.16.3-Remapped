/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonBaseBlock extends DirectionalBlock {
/*  36 */   public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D);
/*  44 */   protected static final VoxelShape WEST_AABB = Block.box(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  45 */   protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D);
/*  46 */   protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 4.0D, 16.0D, 16.0D, 16.0D);
/*  47 */   protected static final VoxelShape UP_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
/*  48 */   protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 4.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */   public PistonBaseBlock(boolean debug1, BlockBehaviour.Properties debug2) {
/*  53 */     super(debug2);
/*  54 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)EXTENDED, Boolean.valueOf(false)));
/*  55 */     this.isSticky = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  60 */     if (((Boolean)debug1.getValue((Property)EXTENDED)).booleanValue()) {
/*  61 */       switch ((Direction)debug1.getValue((Property)FACING))
/*     */       { case BLOCK:
/*  63 */           return DOWN_AABB;
/*     */         
/*     */         default:
/*  66 */           return UP_AABB;
/*     */         case PUSH_ONLY:
/*  68 */           return NORTH_AABB;
/*     */         case null:
/*  70 */           return SOUTH_AABB;
/*     */         case null:
/*  72 */           return WEST_AABB;
/*     */         case null:
/*  74 */           break; }  return EAST_AABB;
/*     */     } 
/*     */     
/*  77 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/*  83 */     if (!debug1.isClientSide) {
/*  84 */       checkIfExtend(debug1, debug2, debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  90 */     if (!debug2.isClientSide) {
/*  91 */       checkIfExtend(debug2, debug3, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  97 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/* 100 */     if (!debug2.isClientSide && debug2.getBlockEntity(debug3) == null) {
/* 101 */       checkIfExtend(debug2, debug3, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 107 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getNearestLookingDirection().getOpposite())).setValue((Property)EXTENDED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private void checkIfExtend(Level debug1, BlockPos debug2, BlockState debug3) {
/* 111 */     Direction debug4 = (Direction)debug3.getValue((Property)FACING);
/*     */     
/* 113 */     boolean debug5 = getNeighborSignal(debug1, debug2, debug4);
/*     */     
/* 115 */     if (debug5 && !((Boolean)debug3.getValue((Property)EXTENDED)).booleanValue()) {
/* 116 */       if ((new PistonStructureResolver(debug1, debug2, debug4, true)).resolve()) {
/* 117 */         debug1.blockEvent(debug2, (Block)this, 0, debug4.get3DDataValue());
/*     */       }
/* 119 */     } else if (!debug5 && ((Boolean)debug3.getValue((Property)EXTENDED)).booleanValue()) {
/* 120 */       BlockPos debug6 = debug2.relative(debug4, 2);
/* 121 */       BlockState debug7 = debug1.getBlockState(debug6);
/*     */       
/* 123 */       int debug8 = 1;
/* 124 */       if (debug7.is(Blocks.MOVING_PISTON) && debug7.getValue((Property)FACING) == debug4) {
/* 125 */         BlockEntity debug9 = debug1.getBlockEntity(debug6);
/*     */         
/* 127 */         if (debug9 instanceof PistonMovingBlockEntity) {
/* 128 */           PistonMovingBlockEntity debug10 = (PistonMovingBlockEntity)debug9;
/* 129 */           if (debug10.isExtending() && (debug10.getProgress(0.0F) < 0.5F || debug1.getGameTime() == debug10.getLastTicked() || ((ServerLevel)debug1).isHandlingTick())) {
/* 130 */             debug8 = 2;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 135 */       debug1.blockEvent(debug2, (Block)this, debug8, debug4.get3DDataValue());
/*     */     } 
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
/*     */   private boolean getNeighborSignal(Level debug1, BlockPos debug2, Direction debug3) {
/* 151 */     for (Direction debug7 : Direction.values()) {
/* 152 */       if (debug7 != debug3 && debug1.hasSignal(debug2.relative(debug7), debug7)) {
/* 153 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 158 */     if (debug1.hasSignal(debug2, Direction.DOWN)) {
/* 159 */       return true;
/*     */     }
/*     */     
/* 162 */     BlockPos debug4 = debug2.above();
/* 163 */     for (Direction debug8 : Direction.values()) {
/* 164 */       if (debug8 != Direction.DOWN && debug1.hasSignal(debug4.relative(debug8), debug8)) {
/* 165 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(BlockState debug1, Level debug2, BlockPos debug3, int debug4, int debug5) {
/* 174 */     Direction debug6 = (Direction)debug1.getValue((Property)FACING);
/* 175 */     if (!debug2.isClientSide) {
/* 176 */       boolean debug7 = getNeighborSignal(debug2, debug3, debug6);
/*     */       
/* 178 */       if (debug7 && (debug4 == 1 || debug4 == 2)) {
/*     */         
/* 180 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)EXTENDED, Boolean.valueOf(true)), 2);
/* 181 */         return false;
/* 182 */       }  if (!debug7 && debug4 == 0) {
/* 183 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (debug4 == 0) {
/* 188 */       if (moveBlocks(debug2, debug3, debug6, true)) {
/* 189 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)EXTENDED, Boolean.valueOf(true)), 67);
/* 190 */         debug2.playSound(null, debug3, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, debug2.random.nextFloat() * 0.25F + 0.6F);
/*     */       } else {
/* 192 */         return false;
/*     */       } 
/* 194 */     } else if (debug4 == 1 || debug4 == 2) {
/* 195 */       BlockEntity debug7 = debug2.getBlockEntity(debug3.relative(debug6));
/* 196 */       if (debug7 instanceof PistonMovingBlockEntity) {
/* 197 */         ((PistonMovingBlockEntity)debug7).finalTick();
/*     */       }
/*     */       
/* 200 */       BlockState debug8 = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)debug6)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/* 201 */       debug2.setBlock(debug3, debug8, 20);
/* 202 */       debug2.setBlockEntity(debug3, MovingPistonBlock.newMovingBlockEntity((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.from3DDataValue(debug5 & 0x7)), debug6, false, true));
/*     */       
/* 204 */       debug2.blockUpdated(debug3, debug8.getBlock());
/* 205 */       debug8.updateNeighbourShapes((LevelAccessor)debug2, debug3, 2);
/*     */ 
/*     */       
/* 208 */       if (this.isSticky) {
/* 209 */         BlockPos debug9 = debug3.offset(debug6.getStepX() * 2, debug6.getStepY() * 2, debug6.getStepZ() * 2);
/* 210 */         BlockState debug10 = debug2.getBlockState(debug9);
/* 211 */         boolean debug11 = false;
/*     */         
/* 213 */         if (debug10.is(Blocks.MOVING_PISTON)) {
/*     */ 
/*     */           
/* 216 */           BlockEntity debug12 = debug2.getBlockEntity(debug9);
/* 217 */           if (debug12 instanceof PistonMovingBlockEntity) {
/* 218 */             PistonMovingBlockEntity debug13 = (PistonMovingBlockEntity)debug12;
/*     */             
/* 220 */             if (debug13.getDirection() == debug6 && debug13.isExtending()) {
/*     */               
/* 222 */               debug13.finalTick();
/* 223 */               debug11 = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 228 */         if (!debug11) {
/* 229 */           if (debug4 == 1 && !debug10.isAir() && isPushable(debug10, debug2, debug9, debug6.getOpposite(), false, debug6) && (debug10.getPistonPushReaction() == PushReaction.NORMAL || debug10.is(Blocks.PISTON) || debug10.is(Blocks.STICKY_PISTON))) {
/* 230 */             moveBlocks(debug2, debug3, debug6, false);
/*     */           } else {
/* 232 */             debug2.removeBlock(debug3.relative(debug6), false);
/*     */           } 
/*     */         }
/*     */       } else {
/* 236 */         debug2.removeBlock(debug3.relative(debug6), false);
/*     */       } 
/*     */       
/* 239 */       debug2.playSound(null, debug3, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, debug2.random.nextFloat() * 0.15F + 0.6F);
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPushable(BlockState debug0, Level debug1, BlockPos debug2, Direction debug3, boolean debug4, Direction debug5) {
/* 245 */     if (debug2.getY() < 0 || debug2.getY() > debug1.getMaxBuildHeight() - 1 || !debug1.getWorldBorder().isWithinBounds(debug2)) {
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 250 */     if (debug0.isAir()) {
/* 251 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 255 */     if (debug0.is(Blocks.OBSIDIAN) || debug0.is(Blocks.CRYING_OBSIDIAN) || debug0.is(Blocks.RESPAWN_ANCHOR)) {
/* 256 */       return false;
/*     */     }
/*     */     
/* 259 */     if (debug3 == Direction.DOWN && debug2.getY() == 0) {
/* 260 */       return false;
/*     */     }
/*     */     
/* 263 */     if (debug3 == Direction.UP && debug2.getY() == debug1.getMaxBuildHeight() - 1) {
/* 264 */       return false;
/*     */     }
/*     */     
/* 267 */     if (debug0.is(Blocks.PISTON) || debug0.is(Blocks.STICKY_PISTON)) {
/*     */       
/* 269 */       if (((Boolean)debug0.getValue((Property)EXTENDED)).booleanValue()) {
/* 270 */         return false;
/*     */       }
/*     */     } else {
/* 273 */       if (debug0.getDestroySpeed((BlockGetter)debug1, debug2) == -1.0F) {
/* 274 */         return false;
/*     */       }
/*     */       
/* 277 */       switch (debug0.getPistonPushReaction()) {
/*     */         case BLOCK:
/* 279 */           return false;
/*     */         case DESTROY:
/* 281 */           return debug4;
/*     */         case PUSH_ONLY:
/* 283 */           return (debug3 == debug5);
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 288 */     return !debug0.getBlock().isEntityBlock();
/*     */   }
/*     */   
/*     */   private boolean moveBlocks(Level debug1, BlockPos debug2, Direction debug3, boolean debug4) {
/* 292 */     BlockPos debug5 = debug2.relative(debug3);
/* 293 */     if (!debug4 && debug1.getBlockState(debug5).is(Blocks.PISTON_HEAD))
/*     */     {
/* 295 */       debug1.setBlock(debug5, Blocks.AIR.defaultBlockState(), 20);
/*     */     }
/*     */     
/* 298 */     PistonStructureResolver debug6 = new PistonStructureResolver(debug1, debug2, debug3, debug4);
/* 299 */     if (!debug6.resolve()) {
/* 300 */       return false;
/*     */     }
/*     */     
/* 303 */     Map<BlockPos, BlockState> debug7 = Maps.newHashMap();
/* 304 */     List<BlockPos> debug8 = debug6.getToPush();
/* 305 */     List<BlockState> debug9 = Lists.newArrayList();
/* 306 */     for (int i = 0; i < debug8.size(); i++) {
/* 307 */       BlockPos blockPos = debug8.get(i);
/* 308 */       BlockState blockState = debug1.getBlockState(blockPos);
/* 309 */       debug9.add(blockState);
/* 310 */       debug7.put(blockPos, blockState);
/*     */     } 
/* 312 */     List<BlockPos> debug10 = debug6.getToDestroy();
/*     */     
/* 314 */     BlockState[] debug11 = new BlockState[debug8.size() + debug10.size()];
/* 315 */     Direction debug12 = debug4 ? debug3 : debug3.getOpposite();
/*     */     
/* 317 */     int debug13 = 0;
/*     */     int j;
/* 319 */     for (j = debug10.size() - 1; j >= 0; j--) {
/* 320 */       BlockPos blockPos = debug10.get(j);
/*     */       
/* 322 */       BlockState debug16 = debug1.getBlockState(blockPos);
/*     */       
/* 324 */       BlockEntity debug17 = debug16.getBlock().isEntityBlock() ? debug1.getBlockEntity(blockPos) : null;
/*     */       
/* 326 */       dropResources(debug16, (LevelAccessor)debug1, blockPos, debug17);
/* 327 */       debug1.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 18);
/*     */       
/* 329 */       debug11[debug13++] = debug16;
/*     */     } 
/*     */ 
/*     */     
/* 333 */     for (j = debug8.size() - 1; j >= 0; j--) {
/* 334 */       BlockPos blockPos = debug8.get(j);
/* 335 */       BlockState debug16 = debug1.getBlockState(blockPos);
/*     */       
/* 337 */       blockPos = blockPos.relative(debug12);
/*     */       
/* 339 */       debug7.remove(blockPos);
/*     */       
/* 341 */       debug1.setBlock(blockPos, (BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)FACING, (Comparable)debug3), 68);
/* 342 */       debug1.setBlockEntity(blockPos, MovingPistonBlock.newMovingBlockEntity(debug9.get(j), debug3, debug4, false));
/*     */       
/* 344 */       debug11[debug13++] = debug16;
/*     */     } 
/*     */     
/* 347 */     if (debug4) {
/* 348 */       PistonType pistonType = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
/* 349 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)debug3)).setValue((Property)PistonHeadBlock.TYPE, (Comparable)pistonType);
/*     */ 
/*     */ 
/*     */       
/* 353 */       BlockState debug16 = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue((Property)MovingPistonBlock.FACING, (Comparable)debug3)).setValue((Property)MovingPistonBlock.TYPE, this.isSticky ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT);
/*     */       
/* 355 */       debug7.remove(debug5);
/*     */       
/* 357 */       debug1.setBlock(debug5, debug16, 68);
/* 358 */       debug1.setBlockEntity(debug5, MovingPistonBlock.newMovingBlockEntity(blockState1, debug3, true, true));
/*     */     } 
/*     */     
/* 361 */     BlockState debug14 = Blocks.AIR.defaultBlockState();
/* 362 */     for (BlockPos debug16 : debug7.keySet()) {
/* 363 */       debug1.setBlock(debug16, debug14, 82);
/*     */     }
/*     */     
/* 366 */     for (Map.Entry<BlockPos, BlockState> debug16 : debug7.entrySet()) {
/* 367 */       BlockPos debug17 = debug16.getKey();
/* 368 */       BlockState debug18 = debug16.getValue();
/* 369 */       debug18.updateIndirectNeighbourShapes((LevelAccessor)debug1, debug17, 2);
/* 370 */       debug14.updateNeighbourShapes((LevelAccessor)debug1, debug17, 2);
/* 371 */       debug14.updateIndirectNeighbourShapes((LevelAccessor)debug1, debug17, 2);
/*     */     } 
/*     */     
/* 374 */     debug13 = 0;
/*     */     int debug15;
/* 376 */     for (debug15 = debug10.size() - 1; debug15 >= 0; debug15--) {
/* 377 */       BlockState debug16 = debug11[debug13++];
/* 378 */       BlockPos debug17 = debug10.get(debug15);
/*     */       
/* 380 */       debug16.updateIndirectNeighbourShapes((LevelAccessor)debug1, debug17, 2);
/* 381 */       debug1.updateNeighborsAt(debug17, debug16.getBlock());
/*     */     } 
/*     */ 
/*     */     
/* 385 */     for (debug15 = debug8.size() - 1; debug15 >= 0; debug15--) {
/* 386 */       debug1.updateNeighborsAt(debug8.get(debug15), debug11[debug13++].getBlock());
/*     */     }
/*     */     
/* 389 */     if (debug4) {
/* 390 */       debug1.updateNeighborsAt(debug5, Blocks.PISTON_HEAD);
/*     */     }
/*     */     
/* 393 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 398 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 403 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 408 */     debug1.add(new Property[] { (Property)FACING, (Property)EXTENDED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/* 413 */     return ((Boolean)debug1.getValue((Property)EXTENDED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 418 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\piston\PistonBaseBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */