/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.CartographyTableMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CartographyTableBlock extends Block {
/* 21 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.cartography_table");
/*    */   
/*    */   protected CartographyTableBlock(BlockBehaviour.Properties debug1) {
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
/* 34 */     debug4.awardStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
/* 35 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 41 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new CartographyTableMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CartographyTableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */