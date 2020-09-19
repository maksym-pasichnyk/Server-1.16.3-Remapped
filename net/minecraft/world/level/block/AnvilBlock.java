/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.AnvilMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AnvilBlock extends FallingBlock {
/*  31 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  33 */   private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
/*     */   
/*  35 */   private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
/*  36 */   private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
/*  37 */   private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
/*     */   
/*  39 */   private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
/*  40 */   private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
/*  41 */   private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
/*     */   
/*  43 */   private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, new VoxelShape[] { X_LEG1, X_LEG2, X_TOP });
/*  44 */   private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, new VoxelShape[] { Z_LEG1, Z_LEG2, Z_TOP });
/*     */   
/*  46 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.repair");
/*     */   
/*     */   public AnvilBlock(BlockBehaviour.Properties debug1) {
/*  49 */     super(debug1);
/*  50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  55 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getClockWise());
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  60 */     if (debug2.isClientSide) {
/*  61 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  64 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/*  65 */     debug4.awardStat(Stats.INTERACT_WITH_ANVIL);
/*  66 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/*  72 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new AnvilMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  77 */     Direction debug5 = (Direction)debug1.getValue((Property)FACING);
/*  78 */     if (debug5.getAxis() == Direction.Axis.X) {
/*  79 */       return X_AXIS_AABB;
/*     */     }
/*  81 */     return Z_AXIS_AABB;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void falling(FallingBlockEntity debug1) {
/*  87 */     debug1.setHurtsEntities(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLand(Level debug1, BlockPos debug2, BlockState debug3, BlockState debug4, FallingBlockEntity debug5) {
/*  92 */     if (!debug5.isSilent()) {
/*  93 */       debug1.levelEvent(1031, debug2, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBroken(Level debug1, BlockPos debug2, FallingBlockEntity debug3) {
/*  99 */     if (!debug3.isSilent()) {
/* 100 */       debug1.levelEvent(1029, debug2, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BlockState damage(BlockState debug0) {
/* 106 */     if (debug0.is(Blocks.ANVIL)) {
/* 107 */       return (BlockState)Blocks.CHIPPED_ANVIL.defaultBlockState().setValue((Property)FACING, debug0.getValue((Property)FACING));
/*     */     }
/* 109 */     if (debug0.is(Blocks.CHIPPED_ANVIL)) {
/* 110 */       return (BlockState)Blocks.DAMAGED_ANVIL.defaultBlockState().setValue((Property)FACING, debug0.getValue((Property)FACING));
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 117 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 122 */     debug1.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 127 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AnvilBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */