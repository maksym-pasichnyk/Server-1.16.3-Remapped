/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.SmithingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SmithingTableBlock extends CraftingTableBlock {
/*    */   protected SmithingTableBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */   
/* 23 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.upgrade");
/*    */ 
/*    */   
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 27 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new SmithingMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 32 */     if (debug2.isClientSide) {
/* 33 */       return InteractionResult.SUCCESS;
/*    */     }
/*    */     
/* 36 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/* 37 */     debug4.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
/* 38 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SmithingTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */