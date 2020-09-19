/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.Hopper;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HopperBlock extends BaseEntityBlock {
/*  33 */   public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
/*  34 */   public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
/*     */   
/*  36 */   private static final VoxelShape TOP = Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);
/*  37 */   private static final VoxelShape FUNNEL = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D);
/*     */   
/*  39 */   private static final VoxelShape CONVEX_BASE = Shapes.or(FUNNEL, TOP);
/*  40 */   private static final VoxelShape BASE = Shapes.join(CONVEX_BASE, Hopper.INSIDE, BooleanOp.ONLY_FIRST);
/*     */   
/*  42 */   private static final VoxelShape DOWN_SHAPE = Shapes.or(BASE, Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
/*  43 */   private static final VoxelShape EAST_SHAPE = Shapes.or(BASE, Block.box(12.0D, 4.0D, 6.0D, 16.0D, 8.0D, 10.0D));
/*  44 */   private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, Block.box(6.0D, 4.0D, 0.0D, 10.0D, 8.0D, 4.0D));
/*  45 */   private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, Block.box(6.0D, 4.0D, 12.0D, 10.0D, 8.0D, 16.0D));
/*  46 */   private static final VoxelShape WEST_SHAPE = Shapes.or(BASE, Block.box(0.0D, 4.0D, 6.0D, 4.0D, 8.0D, 10.0D));
/*     */   
/*  48 */   private static final VoxelShape DOWN_INTERACTION_SHAPE = Hopper.INSIDE;
/*  49 */   private static final VoxelShape EAST_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(12.0D, 8.0D, 6.0D, 16.0D, 10.0D, 10.0D));
/*  50 */   private static final VoxelShape NORTH_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(6.0D, 8.0D, 0.0D, 10.0D, 10.0D, 4.0D));
/*  51 */   private static final VoxelShape SOUTH_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(6.0D, 8.0D, 12.0D, 10.0D, 10.0D, 16.0D));
/*  52 */   private static final VoxelShape WEST_INTERACTION_SHAPE = Shapes.or(Hopper.INSIDE, Block.box(0.0D, 8.0D, 6.0D, 4.0D, 10.0D, 10.0D));
/*     */   
/*     */   public HopperBlock(BlockBehaviour.Properties debug1) {
/*  55 */     super(debug1);
/*  56 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.DOWN)).setValue((Property)ENABLED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  61 */     switch ((Direction)debug1.getValue((Property)FACING)) {
/*     */       case DOWN:
/*  63 */         return DOWN_SHAPE;
/*     */       case NORTH:
/*  65 */         return NORTH_SHAPE;
/*     */       case SOUTH:
/*  67 */         return SOUTH_SHAPE;
/*     */       case WEST:
/*  69 */         return WEST_SHAPE;
/*     */       case EAST:
/*  71 */         return EAST_SHAPE;
/*     */     } 
/*  73 */     return BASE;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getInteractionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  78 */     switch ((Direction)debug1.getValue((Property)FACING)) {
/*     */       case DOWN:
/*  80 */         return DOWN_INTERACTION_SHAPE;
/*     */       case NORTH:
/*  82 */         return NORTH_INTERACTION_SHAPE;
/*     */       case SOUTH:
/*  84 */         return SOUTH_INTERACTION_SHAPE;
/*     */       case WEST:
/*  86 */         return WEST_INTERACTION_SHAPE;
/*     */       case EAST:
/*  88 */         return EAST_INTERACTION_SHAPE;
/*     */     } 
/*  90 */     return Hopper.INSIDE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  95 */     Direction debug2 = debug1.getClickedFace().getOpposite();
/*  96 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (debug2.getAxis() == Direction.Axis.Y) ? (Comparable)Direction.DOWN : (Comparable)debug2)).setValue((Property)ENABLED, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 101 */     return (BlockEntity)new HopperBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 106 */     if (debug5.hasCustomHoverName()) {
/* 107 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 108 */       if (debug6 instanceof HopperBlockEntity) {
/* 109 */         ((HopperBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 116 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/* 119 */     checkPoweredState(debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 124 */     if (debug2.isClientSide) {
/* 125 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 128 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/* 129 */     if (debug7 instanceof HopperBlockEntity) {
/* 130 */       debug4.openMenu((MenuProvider)debug7);
/* 131 */       debug4.awardStat(Stats.INSPECT_HOPPER);
/*     */     } 
/*     */     
/* 134 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 139 */     checkPoweredState(debug2, debug3, debug1);
/*     */   }
/*     */   
/*     */   private void checkPoweredState(Level debug1, BlockPos debug2, BlockState debug3) {
/* 143 */     boolean debug4 = !debug1.hasNeighborSignal(debug2);
/* 144 */     if (debug4 != ((Boolean)debug3.getValue((Property)ENABLED)).booleanValue()) {
/* 145 */       debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)ENABLED, Boolean.valueOf(debug4)), 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 151 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 154 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 155 */     if (debug6 instanceof HopperBlockEntity) {
/* 156 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*     */       
/* 158 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/*     */     
/* 161 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 166 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 176 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(debug2.getBlockEntity(debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 181 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 186 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 191 */     debug1.add(new Property[] { (Property)FACING, (Property)ENABLED });
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/* 196 */     BlockEntity debug5 = debug2.getBlockEntity(debug3);
/* 197 */     if (debug5 instanceof HopperBlockEntity) {
/* 198 */       ((HopperBlockEntity)debug5).entityInside(debug4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 204 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\HopperBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */