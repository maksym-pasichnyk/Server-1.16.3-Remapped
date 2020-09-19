/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
/*    */   public BeaconBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public DyeColor getColor() {
/* 25 */     return DyeColor.WHITE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 30 */     return (BlockEntity)new BeaconBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 35 */     if (debug2.isClientSide) {
/* 36 */       return InteractionResult.SUCCESS;
/*    */     }
/*    */     
/* 39 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/* 40 */     if (debug7 instanceof BeaconBlockEntity) {
/* 41 */       debug4.openMenu((MenuProvider)debug7);
/* 42 */       debug4.awardStat(Stats.INTERACT_WITH_BEACON);
/*    */     } 
/*    */     
/* 45 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 50 */     return RenderShape.MODEL;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 55 */     if (debug5.hasCustomHoverName()) {
/* 56 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 57 */       if (debug6 instanceof BeaconBlockEntity)
/* 58 */         ((BeaconBlockEntity)debug6).setCustomName(debug5.getHoverName()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BeaconBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */