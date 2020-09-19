/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BarrelBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class BarrelBlock extends BaseEntityBlock {
/*  32 */   public static final DirectionProperty FACING = BlockStateProperties.FACING;
/*  33 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*     */   
/*     */   public BarrelBlock(BlockBehaviour.Properties debug1) {
/*  36 */     super(debug1);
/*  37 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  42 */     if (debug2.isClientSide) {
/*  43 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  46 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  47 */     if (debug7 instanceof BarrelBlockEntity) {
/*  48 */       debug4.openMenu((MenuProvider)debug7);
/*  49 */       debug4.awardStat(Stats.OPEN_BARREL);
/*  50 */       PiglinAi.angerNearbyPiglins(debug4, true);
/*     */     } 
/*     */     
/*  53 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  58 */     if (debug1.is(debug4.getBlock())) {
/*     */       return;
/*     */     }
/*  61 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/*  62 */     if (debug6 instanceof Container) {
/*  63 */       Containers.dropContents(debug2, debug3, (Container)debug6);
/*     */       
/*  65 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/*  67 */     super.onRemove(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  72 */     BlockEntity debug5 = debug2.getBlockEntity(debug3);
/*     */     
/*  74 */     if (debug5 instanceof BarrelBlockEntity) {
/*  75 */       ((BarrelBlockEntity)debug5).recheckOpen();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  82 */     return (BlockEntity)new BarrelBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  87 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/*  92 */     if (debug5.hasCustomHoverName()) {
/*  93 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/*  94 */       if (debug6 instanceof BarrelBlockEntity) {
/*  95 */         ((BarrelBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 107 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(debug2.getBlockEntity(debug3));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 112 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 117 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 122 */     debug1.add(new Property[] { (Property)FACING, (Property)OPEN });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 127 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BarrelBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */