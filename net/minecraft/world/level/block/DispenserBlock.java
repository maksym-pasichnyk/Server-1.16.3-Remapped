/*     */ package net.minecraft.world.level.block;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.BlockSource;
/*     */ import net.minecraft.core.BlockSourceImpl;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.PositionImpl;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class DispenserBlock extends BaseEntityBlock {
/*  41 */   public static final DirectionProperty FACING = DirectionalBlock.FACING;
/*  42 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED; private static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY;
/*     */   static {
/*  44 */     DISPENSER_REGISTRY = (Map<Item, DispenseItemBehavior>)Util.make(new Object2ObjectOpenHashMap(), debug0 -> debug0.defaultReturnValue(new DefaultDispenseItemBehavior()));
/*     */   }
/*     */   
/*     */   public static void registerBehavior(ItemLike debug0, DispenseItemBehavior debug1) {
/*  48 */     DISPENSER_REGISTRY.put(debug0.asItem(), debug1);
/*     */   }
/*     */   
/*     */   protected DispenserBlock(BlockBehaviour.Properties debug1) {
/*  52 */     super(debug1);
/*  53 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  58 */     if (debug2.isClientSide) {
/*  59 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  62 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  63 */     if (debug7 instanceof DispenserBlockEntity) {
/*  64 */       debug4.openMenu((MenuProvider)debug7);
/*  65 */       if (debug7 instanceof net.minecraft.world.level.block.entity.DropperBlockEntity) {
/*  66 */         debug4.awardStat(Stats.INSPECT_DROPPER);
/*     */       } else {
/*  68 */         debug4.awardStat(Stats.INSPECT_DISPENSER);
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     return InteractionResult.CONSUME;
/*     */   }
/*     */   
/*     */   protected void dispenseFrom(ServerLevel debug1, BlockPos debug2) {
/*  76 */     BlockSourceImpl debug3 = new BlockSourceImpl(debug1, debug2);
/*  77 */     DispenserBlockEntity debug4 = (DispenserBlockEntity)debug3.getEntity();
/*     */     
/*  79 */     int debug5 = debug4.getRandomSlot();
/*  80 */     if (debug5 < 0) {
/*  81 */       debug1.levelEvent(1001, debug2, 0);
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     ItemStack debug6 = debug4.getItem(debug5);
/*  86 */     DispenseItemBehavior debug7 = getDispenseMethod(debug6);
/*     */     
/*  88 */     if (debug7 != DispenseItemBehavior.NOOP) {
/*  89 */       debug4.setItem(debug5, debug7.dispense((BlockSource)debug3, debug6));
/*     */     }
/*     */   }
/*     */   
/*     */   protected DispenseItemBehavior getDispenseMethod(ItemStack debug1) {
/*  94 */     return DISPENSER_REGISTRY.get(debug1.getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  99 */     boolean debug7 = (debug2.hasNeighborSignal(debug3) || debug2.hasNeighborSignal(debug3.above()));
/* 100 */     boolean debug8 = ((Boolean)debug1.getValue((Property)TRIGGERED)).booleanValue();
/*     */     
/* 102 */     if (debug7 && !debug8) {
/* 103 */       debug2.getBlockTicks().scheduleTick(debug3, this, 4);
/* 104 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)TRIGGERED, Boolean.valueOf(true)), 4);
/* 105 */     } else if (!debug7 && debug8) {
/* 106 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)TRIGGERED, Boolean.valueOf(false)), 4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 112 */     dispenseFrom(debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 117 */     return (BlockEntity)new DispenserBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 122 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 127 */     if (debug5.hasCustomHoverName()) {
/* 128 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 129 */       if (debug6 instanceof DispenserBlockEntity) {
/* 130 */         ((DispenserBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 137 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 140 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 141 */     if (debug6 instanceof DispenserBlockEntity) {
/* 142 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*     */       
/* 144 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/* 146 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public static Position getDispensePosition(BlockSource debug0) {
/* 150 */     Direction debug1 = (Direction)debug0.getBlockState().getValue((Property)FACING);
/*     */     
/* 152 */     double debug2 = debug0.x() + 0.7D * debug1.getStepX();
/* 153 */     double debug4 = debug0.y() + 0.7D * debug1.getStepY();
/* 154 */     double debug6 = debug0.z() + 0.7D * debug1.getStepZ();
/*     */     
/* 156 */     return (Position)new PositionImpl(debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 161 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 166 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(debug2.getBlockEntity(debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 171 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 176 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 181 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 186 */     debug1.add(new Property[] { (Property)FACING, (Property)TRIGGERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DispenserBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */