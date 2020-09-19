/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnderChestBlock
/*     */   extends AbstractChestBlock<EnderChestBlockEntity>
/*     */   implements SimpleWaterloggedBlock {
/*  44 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  45 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  46 */   protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
/*  47 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.enderchest");
/*     */   
/*     */   protected EnderChestBlock(BlockBehaviour.Properties debug1) {
/*  50 */     super(debug1, () -> BlockEntityType.ENDER_CHEST);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  61 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  66 */     return RenderShape.ENTITYBLOCK_ANIMATED;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  71 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*  72 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite())).setValue((Property)WATERLOGGED, Boolean.valueOf((debug2.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  77 */     PlayerEnderChestContainer debug7 = debug4.getEnderChestInventory();
/*  78 */     BlockEntity debug8 = debug2.getBlockEntity(debug3);
/*  79 */     if (debug7 == null || !(debug8 instanceof EnderChestBlockEntity)) {
/*  80 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     }
/*     */     
/*  83 */     BlockPos debug9 = debug3.above();
/*  84 */     if (debug2.getBlockState(debug9).isRedstoneConductor((BlockGetter)debug2, debug9)) {
/*  85 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     }
/*     */     
/*  88 */     if (debug2.isClientSide) {
/*  89 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  92 */     EnderChestBlockEntity debug10 = (EnderChestBlockEntity)debug8;
/*  93 */     debug7.setActiveChest(debug10);
/*     */     
/*  95 */     debug4.openMenu((MenuProvider)new SimpleMenuProvider((debug1, debug2, debug3) -> ChestMenu.threeRows(debug1, debug2, (Container)debug0), CONTAINER_TITLE));
/*  96 */     debug4.awardStat(Stats.OPEN_ENDERCHEST);
/*  97 */     PiglinAi.angerNearbyPiglins(debug4, true);
/*  98 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 103 */     return (BlockEntity)new EnderChestBlockEntity();
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
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 125 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 130 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 135 */     debug1.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 140 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 141 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 143 */     return super.getFluidState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 148 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 149 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/* 151 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 156 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EnderChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */