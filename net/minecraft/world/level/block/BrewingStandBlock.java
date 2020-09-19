/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BrewingStandBlock extends BaseEntityBlock {
/*  30 */   public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[] { BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   protected static final VoxelShape SHAPE = Shapes.or(
/*  37 */       Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D), 
/*  38 */       Block.box(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D));
/*     */ 
/*     */   
/*     */   public BrewingStandBlock(BlockBehaviour.Properties debug1) {
/*  42 */     super(debug1);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_BOTTLE[0], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[1], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[2], Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  48 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  53 */     return (BlockEntity)new BrewingStandBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  58 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  63 */     if (debug2.isClientSide) {
/*  64 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  67 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  68 */     if (debug7 instanceof BrewingStandBlockEntity) {
/*  69 */       debug4.openMenu((MenuProvider)debug7);
/*  70 */       debug4.awardStat(Stats.INTERACT_WITH_BREWINGSTAND);
/*     */     } 
/*     */     
/*  73 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/*  78 */     if (debug5.hasCustomHoverName()) {
/*  79 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*  80 */       if (debug6 instanceof BrewingStandBlockEntity) {
/*  81 */         ((BrewingStandBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
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
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  97 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/* 100 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 101 */     if (debug6 instanceof BrewingStandBlockEntity) {
/* 102 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*     */     }
/* 104 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 114 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(debug2.getBlockEntity(debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 119 */     debug1.add(new Property[] { (Property)HAS_BOTTLE[0], (Property)HAS_BOTTLE[1], (Property)HAS_BOTTLE[2] });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 124 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BrewingStandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */