/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFurnaceBlock extends BaseEntityBlock {
/*  27 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*  28 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */   
/*     */   protected AbstractFurnaceBlock(BlockBehaviour.Properties debug1) {
/*  31 */     super(debug1);
/*  32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)LIT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  37 */     if (debug2.isClientSide) {
/*  38 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  41 */     openContainer(debug2, debug3, debug4);
/*     */     
/*  43 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer);
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  50 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/*  55 */     if (debug5.hasCustomHoverName()) {
/*  56 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*  57 */       if (debug6 instanceof AbstractFurnaceBlockEntity) {
/*  58 */         ((AbstractFurnaceBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  65 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/*  70 */     if (debug6 instanceof AbstractFurnaceBlockEntity) {
/*  71 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*  72 */       ((AbstractFurnaceBlockEntity)debug6).getRecipesToAwardAndPopExperience(debug2, Vec3.atCenterOf((Vec3i)debug3));
/*  73 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/*  75 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/*  85 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(debug2.getBlockEntity(debug3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  93 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  98 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 103 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 108 */     debug1.add(new Property[] { (Property)FACING, (Property)LIT });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AbstractFurnaceBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */