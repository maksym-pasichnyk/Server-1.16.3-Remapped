/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ItemFrameItem extends HangingEntityItem {
/*    */   public ItemFrameItem(Item.Properties debug1) {
/* 11 */     super(EntityType.ITEM_FRAME, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlace(Player debug1, Direction debug2, ItemStack debug3, BlockPos debug4) {
/* 16 */     return (!Level.isOutsideBuildHeight(debug4) && debug1.mayUseItemAt(debug4, debug2, debug3));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ItemFrameItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */