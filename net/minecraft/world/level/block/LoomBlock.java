/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.LoomMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class LoomBlock extends HorizontalDirectionalBlock {
/* 21 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.loom");
/*    */   
/*    */   protected LoomBlock(BlockBehaviour.Properties debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 29 */     if (debug2.isClientSide) {
/* 30 */       return InteractionResult.SUCCESS;
/*    */     }
/*    */     
/* 33 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/* 34 */     debug4.awardStat(Stats.INTERACT_WITH_LOOM);
/* 35 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 40 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new LoomMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 45 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 50 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LoomBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */