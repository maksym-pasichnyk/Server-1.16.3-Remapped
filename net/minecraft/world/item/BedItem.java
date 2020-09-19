/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedItem extends BlockItem {
/*    */   public BedItem(Block debug1, Item.Properties debug2) {
/*  9 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext debug1, BlockState debug2) {
/* 14 */     return debug1.getLevel().setBlock(debug1.getClickedPos(), debug2, 26);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\BedItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */