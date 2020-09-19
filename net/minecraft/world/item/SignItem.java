/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SignItem
/*    */   extends StandingAndWallBlockItem {
/*    */   public SignItem(Item.Properties debug1, Block debug2, Block debug3) {
/* 14 */     super(debug2, debug3, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateCustomBlockEntityTag(BlockPos debug1, Level debug2, @Nullable Player debug3, ItemStack debug4, BlockState debug5) {
/* 19 */     boolean debug6 = super.updateCustomBlockEntityTag(debug1, debug2, debug3, debug4, debug5);
/*    */     
/* 21 */     if (!debug2.isClientSide && !debug6 && debug3 != null) {
/* 22 */       debug3.openTextEdit((SignBlockEntity)debug2.getBlockEntity(debug1));
/*    */     }
/*    */     
/* 25 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SignItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */