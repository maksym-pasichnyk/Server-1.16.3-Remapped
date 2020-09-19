/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.StonecutterMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class StonecutterBlock extends Block {
/* 29 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.stonecutter");
/*    */   
/* 31 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/* 32 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
/*    */   
/*    */   public StonecutterBlock(BlockBehaviour.Properties debug1) {
/* 35 */     super(debug1);
/* 36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 41 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 46 */     if (debug2.isClientSide) {
/* 47 */       return InteractionResult.SUCCESS;
/*    */     }
/*    */     
/* 50 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/* 51 */     debug4.awardStat(Stats.INTERACT_WITH_STONECUTTER);
/*    */     
/* 53 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 59 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new StonecutterMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 64 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 74 */     return RenderShape.MODEL;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 79 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 84 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 89 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 94 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StonecutterBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */