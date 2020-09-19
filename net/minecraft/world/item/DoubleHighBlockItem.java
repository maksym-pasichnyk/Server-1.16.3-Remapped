/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DoubleHighBlockItem extends BlockItem {
/*    */   public DoubleHighBlockItem(Block debug1, Item.Properties debug2) {
/* 10 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext debug1, BlockState debug2) {
/* 15 */     debug1.getLevel().setBlock(debug1.getClickedPos().above(), Blocks.AIR.defaultBlockState(), 27);
/* 16 */     return super.placeBlock(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DoubleHighBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */