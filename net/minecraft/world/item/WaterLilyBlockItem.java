/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class WaterLilyBlockItem extends BlockItem {
/*    */   public WaterLilyBlockItem(Block debug1, Item.Properties debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 20 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 25 */     BlockHitResult debug4 = getPlayerPOVHitResult(debug1, debug2, ClipContext.Fluid.SOURCE_ONLY);
/* 26 */     BlockHitResult debug5 = debug4.withPosition(debug4.getBlockPos().above());
/* 27 */     InteractionResult debug6 = super.useOn(new UseOnContext(debug2, debug3, debug5));
/* 28 */     return new InteractionResultHolder(debug6, debug2.getItemInHand(debug3));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\WaterLilyBlockItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */