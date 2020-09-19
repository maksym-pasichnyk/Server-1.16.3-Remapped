/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class GameMasterBlockItem
/*    */   extends BlockItem {
/*    */   public GameMasterBlockItem(Block debug1, Item.Properties debug2) {
/* 12 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected BlockState getPlacementState(BlockPlaceContext debug1) {
/* 18 */     Player debug2 = debug1.getPlayer();
/* 19 */     return (debug2 == null || debug2.canUseGameMasterBlocks()) ? super.getPlacementState(debug1) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\GameMasterBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */