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
/*    */ import net.minecraft.world.inventory.CraftingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CraftingTableBlock extends Block {
/* 19 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.crafting");
/*    */   
/*    */   protected CraftingTableBlock(BlockBehaviour.Properties debug1) {
/* 22 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 27 */     if (debug2.isClientSide) {
/* 28 */       return InteractionResult.SUCCESS;
/*    */     }
/*    */     
/* 31 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/* 32 */     debug4.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
/* 33 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 38 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new CraftingMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CraftingTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */