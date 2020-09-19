/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class FireChargeItem extends Item {
/*    */   public FireChargeItem(Item.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 20 */     Level debug2 = debug1.getLevel();
/* 21 */     BlockPos debug3 = debug1.getClickedPos();
/* 22 */     BlockState debug4 = debug2.getBlockState(debug3);
/* 23 */     boolean debug5 = false;
/*    */     
/* 25 */     if (CampfireBlock.canLight(debug4)) {
/* 26 */       playSound(debug2, debug3);
/* 27 */       debug2.setBlockAndUpdate(debug3, (BlockState)debug4.setValue((Property)CampfireBlock.LIT, Boolean.valueOf(true)));
/* 28 */       debug5 = true;
/*    */     } else {
/* 30 */       debug3 = debug3.relative(debug1.getClickedFace());
/* 31 */       if (BaseFireBlock.canBePlacedAt(debug2, debug3, debug1.getHorizontalDirection())) {
/* 32 */         playSound(debug2, debug3);
/* 33 */         debug2.setBlockAndUpdate(debug3, BaseFireBlock.getState((BlockGetter)debug2, debug3));
/* 34 */         debug5 = true;
/*    */       } 
/*    */     } 
/*    */     
/* 38 */     if (debug5) {
/* 39 */       debug1.getItemInHand().shrink(1);
/* 40 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */     } 
/*    */     
/* 43 */     return InteractionResult.FAIL;
/*    */   }
/*    */   
/*    */   private void playSound(Level debug1, BlockPos debug2) {
/* 47 */     debug1.playSound(null, debug2, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FireChargeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */