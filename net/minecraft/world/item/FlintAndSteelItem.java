/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class FlintAndSteelItem extends Item {
/*    */   public FlintAndSteelItem(Item.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext debug1) {
/* 26 */     Player debug2 = debug1.getPlayer();
/* 27 */     Level debug3 = debug1.getLevel();
/* 28 */     BlockPos debug4 = debug1.getClickedPos();
/*    */     
/* 30 */     BlockState debug5 = debug3.getBlockState(debug4);
/* 31 */     if (CampfireBlock.canLight(debug5)) {
/* 32 */       debug3.playSound(debug2, debug4, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
/* 33 */       debug3.setBlock(debug4, (BlockState)debug5.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
/* 34 */       if (debug2 != null) {
/* 35 */         debug1.getItemInHand().hurtAndBreak(1, debug2, debug1 -> debug1.broadcastBreakEvent(debug0.getHand()));
/*    */       }
/* 37 */       return InteractionResult.sidedSuccess(debug3.isClientSide());
/*    */     } 
/*    */     
/* 40 */     BlockPos debug6 = debug4.relative(debug1.getClickedFace());
/* 41 */     if (BaseFireBlock.canBePlacedAt(debug3, debug6, debug1.getHorizontalDirection())) {
/* 42 */       debug3.playSound(debug2, debug6, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
/*    */       
/* 44 */       BlockState debug7 = BaseFireBlock.getState((BlockGetter)debug3, debug6);
/* 45 */       debug3.setBlock(debug6, debug7, 11);
/*    */       
/* 47 */       ItemStack debug8 = debug1.getItemInHand();
/* 48 */       if (debug2 instanceof ServerPlayer) {
/* 49 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)debug2, debug6, debug8);
/* 50 */         debug8.hurtAndBreak(1, debug2, debug1 -> debug1.broadcastBreakEvent(debug0.getHand()));
/*    */       } 
/*    */       
/* 53 */       return InteractionResult.sidedSuccess(debug3.isClientSide());
/*    */     } 
/*    */     
/* 56 */     return InteractionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FlintAndSteelItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */