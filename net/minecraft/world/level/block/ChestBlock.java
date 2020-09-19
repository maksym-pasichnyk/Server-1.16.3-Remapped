/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
/*  55 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  56 */   public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected static final VoxelShape NORTH_AABB = Block.box(1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D);
/*  63 */   protected static final VoxelShape SOUTH_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D);
/*  64 */   protected static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
/*  65 */   protected static final VoxelShape EAST_AABB = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D);
/*  66 */   protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
/*     */   
/*     */   protected ChestBlock(BlockBehaviour.Properties debug1, Supplier<BlockEntityType<? extends ChestBlockEntity>> debug2) {
/*  69 */     super(debug1, debug2);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)ChestType.SINGLE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState debug0) {
/*  74 */     ChestType debug1 = (ChestType)debug0.getValue((Property)TYPE);
/*  75 */     if (debug1 == ChestType.SINGLE) {
/*  76 */       return DoubleBlockCombiner.BlockType.SINGLE;
/*     */     }
/*  78 */     if (debug1 == ChestType.RIGHT) {
/*  79 */       return DoubleBlockCombiner.BlockType.FIRST;
/*     */     }
/*  81 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  86 */     return RenderShape.ENTITYBLOCK_ANIMATED;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  91 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  92 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*     */     }
/*  94 */     if (debug3.is(this) && debug2.getAxis().isHorizontal()) {
/*  95 */       ChestType debug7 = (ChestType)debug3.getValue((Property)TYPE);
/*  96 */       if (debug1.getValue((Property)TYPE) == ChestType.SINGLE && debug7 != ChestType.SINGLE && 
/*  97 */         debug1.getValue((Property)FACING) == debug3.getValue((Property)FACING) && getConnectedDirection(debug3) == debug2.getOpposite()) {
/*  98 */         return (BlockState)debug1.setValue((Property)TYPE, (Comparable)debug7.getOpposite());
/*     */       }
/*     */     }
/* 101 */     else if (getConnectedDirection(debug1) == debug2) {
/* 102 */       return (BlockState)debug1.setValue((Property)TYPE, (Comparable)ChestType.SINGLE);
/*     */     } 
/* 104 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 109 */     if (debug1.getValue((Property)TYPE) == ChestType.SINGLE) {
/* 110 */       return AABB;
/*     */     }
/*     */     
/* 113 */     switch (getConnectedDirection(debug1))
/*     */     
/*     */     { default:
/* 116 */         return NORTH_AABB;
/*     */       case SOUTH:
/* 118 */         return SOUTH_AABB;
/*     */       case WEST:
/* 120 */         return WEST_AABB;
/*     */       case EAST:
/* 122 */         break; }  return EAST_AABB;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState debug0) {
/* 127 */     Direction debug1 = (Direction)debug0.getValue((Property)FACING);
/* 128 */     return (debug0.getValue((Property)TYPE) == ChestType.LEFT) ? debug1.getClockWise() : debug1.getCounterClockWise();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 133 */     ChestType debug2 = ChestType.SINGLE;
/* 134 */     Direction debug3 = debug1.getHorizontalDirection().getOpposite();
/* 135 */     FluidState debug4 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*     */     
/* 137 */     boolean debug5 = debug1.isSecondaryUseActive();
/* 138 */     Direction debug6 = debug1.getClickedFace();
/*     */     
/* 140 */     if (debug6.getAxis().isHorizontal() && debug5) {
/* 141 */       Direction debug7 = candidatePartnerFacing(debug1, debug6.getOpposite());
/* 142 */       if (debug7 != null && debug7.getAxis() != debug6.getAxis()) {
/* 143 */         debug3 = debug7;
/* 144 */         debug2 = (debug3.getCounterClockWise() == debug6.getOpposite()) ? ChestType.RIGHT : ChestType.LEFT;
/*     */       } 
/*     */     } 
/* 147 */     if (debug2 == ChestType.SINGLE && !debug5) {
/* 148 */       if (debug3 == candidatePartnerFacing(debug1, debug3.getClockWise())) {
/* 149 */         debug2 = ChestType.LEFT;
/* 150 */       } else if (debug3 == candidatePartnerFacing(debug1, debug3.getCounterClockWise())) {
/* 151 */         debug2 = ChestType.RIGHT;
/*     */       } 
/*     */     }
/*     */     
/* 155 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug3)).setValue((Property)TYPE, (Comparable)debug2)).setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockState debug1) {
/* 160 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 161 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 163 */     return super.getFluidState(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Direction candidatePartnerFacing(BlockPlaceContext debug1, Direction debug2) {
/* 168 */     BlockState debug3 = debug1.getLevel().getBlockState(debug1.getClickedPos().relative(debug2));
/*     */     
/* 170 */     return (debug3.is(this) && debug3.getValue((Property)TYPE) == ChestType.SINGLE) ? (Direction)debug3.getValue((Property)FACING) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 175 */     if (debug5.hasCustomHoverName()) {
/* 176 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 177 */       if (debug6 instanceof ChestBlockEntity) {
/* 178 */         ((ChestBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 185 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 188 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 189 */     if (debug6 instanceof Container) {
/* 190 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*     */       
/* 192 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/* 194 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 199 */     if (debug2.isClientSide) {
/* 200 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 203 */     MenuProvider debug7 = getMenuProvider(debug1, debug2, debug3);
/* 204 */     if (debug7 != null) {
/* 205 */       debug4.openMenu(debug7);
/* 206 */       debug4.awardStat(getOpenChestStat());
/* 207 */       PiglinAi.angerNearbyPiglins(debug4, true);
/*     */     } 
/*     */     
/* 210 */     return InteractionResult.CONSUME;
/*     */   }
/*     */   
/*     */   protected Stat<ResourceLocation> getOpenChestStat() {
/* 214 */     return Stats.CUSTOM.get(Stats.OPEN_CHEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>()
/*     */     {
/*     */       public Optional<Container> acceptDouble(ChestBlockEntity debug1, ChestBlockEntity debug2) {
/* 224 */         return (Optional)Optional.of(new CompoundContainer((Container)debug1, (Container)debug2));
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Container> acceptSingle(ChestBlockEntity debug1) {
/* 229 */         return (Optional)Optional.of(debug1);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Container> acceptNone() {
/* 234 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   @Nullable
/*     */   public static Container getContainer(ChestBlock debug0, BlockState debug1, Level debug2, BlockPos debug3, boolean debug4) {
/* 240 */     return ((Optional<Container>)debug0.combine(debug1, debug2, debug3, debug4).<Optional<Container>>apply(CHEST_COMBINER)).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState debug1, Level debug2, BlockPos debug3, boolean debug4) {
/*     */     BiPredicate<LevelAccessor, BlockPos> debug5;
/* 246 */     if (debug4) {
/* 247 */       debug5 = ((debug0, debug1) -> false);
/*     */     } else {
/* 249 */       debug5 = ChestBlock::isChestBlockedAt;
/*     */     } 
/* 251 */     return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityType.get(), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, FACING, debug1, (LevelAccessor)debug2, debug3, debug5);
/*     */   }
/*     */   
/* 254 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>()
/*     */     {
/*     */       public Optional<MenuProvider> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
/* 257 */         final CompoundContainer container = new CompoundContainer((Container)first, (Container)second);
/* 258 */         return Optional.of(new MenuProvider()
/*     */             {
/*     */               @Nullable
/*     */               public AbstractContainerMenu createMenu(int debug1, Inventory debug2, Player debug3) {
/* 262 */                 if (first.canOpen(debug3) && second.canOpen(debug3)) {
/* 263 */                   first.unpackLootTable(debug2.player);
/* 264 */                   second.unpackLootTable(debug2.player);
/*     */                   
/* 266 */                   return (AbstractContainerMenu)ChestMenu.sixRows(debug1, debug2, container);
/*     */                 } 
/* 268 */                 return null;
/*     */               }
/*     */ 
/*     */               
/*     */               public Component getDisplayName() {
/* 273 */                 if (first.hasCustomName()) {
/* 274 */                   return first.getDisplayName();
/*     */                 }
/* 276 */                 if (second.hasCustomName()) {
/* 277 */                   return second.getDisplayName();
/*     */                 }
/* 279 */                 return (Component)new TranslatableComponent("container.chestDouble");
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<MenuProvider> acceptSingle(ChestBlockEntity debug1) {
/* 286 */         return (Optional)Optional.of(debug1);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<MenuProvider> acceptNone() {
/* 291 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 298 */     return ((Optional<MenuProvider>)combine(debug1, debug2, debug3, false).<Optional<MenuProvider>>apply(MENU_PROVIDER_COMBINER)).orElse(null);
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
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 322 */     return (BlockEntity)new ChestBlockEntity();
/*     */   }
/*     */   
/*     */   public static boolean isChestBlockedAt(LevelAccessor debug0, BlockPos debug1) {
/* 326 */     return (isBlockedChestByBlock((BlockGetter)debug0, debug1) || isCatSittingOnChest(debug0, debug1));
/*     */   }
/*     */   
/*     */   private static boolean isBlockedChestByBlock(BlockGetter debug0, BlockPos debug1) {
/* 330 */     BlockPos debug2 = debug1.above();
/* 331 */     return debug0.getBlockState(debug2).isRedstoneConductor(debug0, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isCatSittingOnChest(LevelAccessor debug0, BlockPos debug1) {
/* 336 */     List<Cat> debug2 = debug0.getEntitiesOfClass(Cat.class, new AABB(debug1.getX(), (debug1.getY() + 1), debug1.getZ(), (debug1.getX() + 1), (debug1.getY() + 2), (debug1.getZ() + 1)));
/* 337 */     if (!debug2.isEmpty()) {
/* 338 */       for (Cat debug4 : debug2) {
/* 339 */         if (debug4.isInSittingPose()) {
/* 340 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 344 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 349 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 354 */     return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, debug1, debug2, debug3, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 359 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 364 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 369 */     debug1.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 374 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */