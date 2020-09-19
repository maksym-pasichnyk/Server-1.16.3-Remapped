/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LecternBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LecternBlock extends BaseEntityBlock {
/*  39 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  40 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  41 */   public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
/*     */   
/*  43 */   public static final VoxelShape SHAPE_BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
/*  44 */   public static final VoxelShape SHAPE_POST = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
/*     */   
/*  46 */   public static final VoxelShape SHAPE_COMMON = Shapes.or(SHAPE_BASE, SHAPE_POST);
/*     */   
/*  48 */   public static final VoxelShape SHAPE_TOP_PLATE = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
/*  49 */   public static final VoxelShape SHAPE_COLLISION = Shapes.or(SHAPE_COMMON, SHAPE_TOP_PLATE);
/*     */   
/*  51 */   public static final VoxelShape SHAPE_WEST = Shapes.or(
/*  52 */       Block.box(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), new VoxelShape[] {
/*  53 */         Block.box(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), 
/*  54 */         Block.box(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), SHAPE_COMMON
/*     */       });
/*     */   
/*  57 */   public static final VoxelShape SHAPE_NORTH = Shapes.or(
/*  58 */       Block.box(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), new VoxelShape[] {
/*  59 */         Block.box(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), 
/*  60 */         Block.box(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), SHAPE_COMMON
/*     */       });
/*     */   
/*  63 */   public static final VoxelShape SHAPE_EAST = Shapes.or(
/*  64 */       Block.box(15.0D, 10.0D, 0.0D, 10.666667D, 14.0D, 16.0D), new VoxelShape[] {
/*  65 */         Block.box(10.666667D, 12.0D, 0.0D, 6.333333D, 16.0D, 16.0D), 
/*  66 */         Block.box(6.333333D, 14.0D, 0.0D, 2.0D, 18.0D, 16.0D), SHAPE_COMMON
/*     */       });
/*     */   
/*  69 */   public static final VoxelShape SHAPE_SOUTH = Shapes.or(
/*  70 */       Block.box(0.0D, 10.0D, 15.0D, 16.0D, 14.0D, 10.666667D), new VoxelShape[] {
/*  71 */         Block.box(0.0D, 12.0D, 10.666667D, 16.0D, 16.0D, 6.333333D), 
/*  72 */         Block.box(0.0D, 14.0D, 6.333333D, 16.0D, 18.0D, 2.0D), SHAPE_COMMON
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LecternBlock(BlockBehaviour.Properties debug1) {
/*  79 */     super(debug1);
/*  80 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HAS_BOOK, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  85 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getOcclusionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  90 */     return SHAPE_COMMON;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 100 */     Level debug2 = debug1.getLevel();
/* 101 */     ItemStack debug3 = debug1.getItemInHand();
/* 102 */     CompoundTag debug4 = debug3.getTag();
/* 103 */     Player debug5 = debug1.getPlayer();
/* 104 */     boolean debug6 = false;
/*     */     
/* 106 */     if (!debug2.isClientSide && debug5 != null && debug4 != null && debug5.canUseGameMasterBlocks() && debug4.contains("BlockEntityTag")) {
/* 107 */       CompoundTag debug7 = debug4.getCompound("BlockEntityTag");
/* 108 */       if (debug7.contains("Book")) {
/* 109 */         debug6 = true;
/*     */       }
/*     */     } 
/* 112 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite())).setValue((Property)HAS_BOOK, Boolean.valueOf(debug6));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 117 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 122 */     switch ((Direction)debug1.getValue((Property)FACING)) {
/*     */       case NORTH:
/* 124 */         return SHAPE_NORTH;
/*     */       case SOUTH:
/* 126 */         return SHAPE_SOUTH;
/*     */       case EAST:
/* 128 */         return SHAPE_EAST;
/*     */       case WEST:
/* 130 */         return SHAPE_WEST;
/*     */     } 
/* 132 */     return SHAPE_COMMON;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 138 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 143 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 148 */     debug1.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)HAS_BOOK });
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 154 */     return (BlockEntity)new LecternBlockEntity();
/*     */   }
/*     */   
/*     */   public static boolean tryPlaceBook(Level debug0, BlockPos debug1, BlockState debug2, ItemStack debug3) {
/* 158 */     if (!((Boolean)debug2.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 159 */       if (!debug0.isClientSide) {
/* 160 */         placeBook(debug0, debug1, debug2, debug3);
/*     */       }
/* 162 */       return true;
/*     */     } 
/*     */     
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeBook(Level debug0, BlockPos debug1, BlockState debug2, ItemStack debug3) {
/* 169 */     BlockEntity debug4 = debug0.getBlockEntity(debug1);
/* 170 */     if (debug4 instanceof LecternBlockEntity) {
/* 171 */       LecternBlockEntity debug5 = (LecternBlockEntity)debug4;
/* 172 */       debug5.setBook(debug3.split(1));
/* 173 */       resetBookState(debug0, debug1, debug2, true);
/* 174 */       debug0.playSound(null, debug1, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void resetBookState(Level debug0, BlockPos debug1, BlockState debug2, boolean debug3) {
/* 179 */     debug0.setBlock(debug1, (BlockState)((BlockState)debug2.setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)HAS_BOOK, Boolean.valueOf(debug3)), 3);
/* 180 */     updateBelow(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static void signalPageChange(Level debug0, BlockPos debug1, BlockState debug2) {
/* 184 */     changePowered(debug0, debug1, debug2, true);
/* 185 */     debug0.getBlockTicks().scheduleTick(debug1, debug2.getBlock(), 2);
/* 186 */     debug0.levelEvent(1043, debug1, 0);
/*     */   }
/*     */   
/*     */   private static void changePowered(Level debug0, BlockPos debug1, BlockState debug2, boolean debug3) {
/* 190 */     debug0.setBlock(debug1, (BlockState)debug2.setValue((Property)POWERED, Boolean.valueOf(debug3)), 3);
/* 191 */     updateBelow(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   private static void updateBelow(Level debug0, BlockPos debug1, BlockState debug2) {
/* 195 */     debug0.updateNeighborsAt(debug1.below(), debug2.getBlock());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 200 */     changePowered((Level)debug2, debug3, debug1, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 205 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 209 */     if (((Boolean)debug1.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 210 */       popBook(debug1, debug2, debug3);
/*     */     }
/*     */     
/* 213 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 214 */       debug2.updateNeighborsAt(debug3.below(), this);
/*     */     }
/*     */     
/* 217 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   private void popBook(BlockState debug1, Level debug2, BlockPos debug3) {
/* 221 */     BlockEntity debug4 = debug2.getBlockEntity(debug3);
/* 222 */     if (debug4 instanceof LecternBlockEntity) {
/* 223 */       LecternBlockEntity debug5 = (LecternBlockEntity)debug4;
/*     */       
/* 225 */       Direction debug6 = (Direction)debug1.getValue((Property)FACING);
/* 226 */       ItemStack debug7 = debug5.getBook().copy();
/* 227 */       float debug8 = 0.25F * debug6.getStepX();
/* 228 */       float debug9 = 0.25F * debug6.getStepZ();
/* 229 */       ItemEntity debug10 = new ItemEntity(debug2, debug3.getX() + 0.5D + debug8, (debug3.getY() + 1), debug3.getZ() + 0.5D + debug9, debug7);
/* 230 */       debug10.setDefaultPickUpDelay();
/* 231 */       debug2.addFreshEntity((Entity)debug10);
/*     */       
/* 233 */       debug5.clearContent();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 239 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 244 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 249 */     return (debug4 == Direction.UP && ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 259 */     if (((Boolean)debug1.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 260 */       BlockEntity debug4 = debug2.getBlockEntity(debug3);
/* 261 */       if (debug4 instanceof LecternBlockEntity) {
/* 262 */         return ((LecternBlockEntity)debug4).getRedstoneSignal();
/*     */       }
/*     */     } 
/*     */     
/* 266 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 271 */     if (((Boolean)debug1.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 272 */       if (!debug2.isClientSide) {
/* 273 */         openScreen(debug2, debug3, debug4);
/*     */       }
/* 275 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */ 
/*     */     
/* 279 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*     */     
/* 281 */     if (debug7.isEmpty() || debug7.getItem().is((Tag)ItemTags.LECTERN_BOOKS)) {
/* 282 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 285 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 291 */     if (!((Boolean)debug1.getValue((Property)HAS_BOOK)).booleanValue()) {
/* 292 */       return null;
/*     */     }
/*     */     
/* 295 */     return super.getMenuProvider(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   private void openScreen(Level debug1, BlockPos debug2, Player debug3) {
/* 299 */     BlockEntity debug4 = debug1.getBlockEntity(debug2);
/* 300 */     if (debug4 instanceof LecternBlockEntity) {
/* 301 */       debug3.openMenu((MenuProvider)debug4);
/* 302 */       debug3.awardStat(Stats.INTERACT_WITH_LECTERN);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 308 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LecternBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */