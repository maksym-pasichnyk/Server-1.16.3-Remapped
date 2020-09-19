/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PumpkinBlock extends StemGrownBlock {
/*    */   protected PumpkinBlock(BlockBehaviour.Properties debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 24 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/* 25 */     if (debug7.getItem() == Items.SHEARS) {
/* 26 */       if (!debug2.isClientSide) {
/* 27 */         Direction debug8 = debug6.getDirection();
/* 28 */         Direction debug9 = (debug8.getAxis() == Direction.Axis.Y) ? debug4.getDirection().getOpposite() : debug8;
/*    */         
/* 30 */         debug2.playSound(null, debug3, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 31 */         debug2.setBlock(debug3, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue((Property)CarvedPumpkinBlock.FACING, (Comparable)debug9), 11);
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 36 */         ItemEntity debug10 = new ItemEntity(debug2, debug3.getX() + 0.5D + debug9.getStepX() * 0.65D, debug3.getY() + 0.1D, debug3.getZ() + 0.5D + debug9.getStepZ() * 0.65D, new ItemStack((ItemLike)Items.PUMPKIN_SEEDS, 4));
/*    */ 
/*    */ 
/*    */         
/* 40 */         debug10.setDeltaMovement(0.05D * debug9
/* 41 */             .getStepX() + debug2.random.nextDouble() * 0.02D, 0.05D, 0.05D * debug9
/*    */             
/* 43 */             .getStepZ() + debug2.random.nextDouble() * 0.02D);
/*    */ 
/*    */         
/* 46 */         debug2.addFreshEntity((Entity)debug10);
/*    */         
/* 48 */         debug7.hurtAndBreak(1, (LivingEntity)debug4, debug1 -> debug1.broadcastBreakEvent(debug0));
/*    */       } 
/*    */       
/* 51 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*    */     } 
/*    */     
/* 54 */     return super.use(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public StemBlock getStem() {
/* 59 */     return (StemBlock)Blocks.PUMPKIN_STEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public AttachedStemBlock getAttachedStem() {
/* 64 */     return (AttachedStemBlock)Blocks.ATTACHED_PUMPKIN_STEM;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PumpkinBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */