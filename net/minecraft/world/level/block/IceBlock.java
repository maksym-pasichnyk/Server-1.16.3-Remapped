/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ 
/*    */ public class IceBlock extends HalfTransparentBlock {
/*    */   public IceBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void playerDestroy(Level debug1, Player debug2, BlockPos debug3, BlockState debug4, @Nullable BlockEntity debug5, ItemStack debug6) {
/* 26 */     super.playerDestroy(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     
/* 28 */     if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, debug6) == 0) {
/* 29 */       if (debug1.dimensionType().ultraWarm()) {
/* 30 */         debug1.removeBlock(debug3, false);
/*    */         
/*    */         return;
/*    */       } 
/* 34 */       Material debug7 = debug1.getBlockState(debug3.below()).getMaterial();
/* 35 */       if (debug7.blocksMotion() || debug7.isLiquid()) {
/* 36 */         debug1.setBlockAndUpdate(debug3, Blocks.WATER.defaultBlockState());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 43 */     if (debug2.getBrightness(LightLayer.BLOCK, debug3) > 11 - debug1.getLightBlock((BlockGetter)debug2, debug3)) {
/* 44 */       melt(debug1, (Level)debug2, debug3);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void melt(BlockState debug1, Level debug2, BlockPos debug3) {
/* 49 */     if (debug2.dimensionType().ultraWarm()) {
/* 50 */       debug2.removeBlock(debug3, false);
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     debug2.setBlockAndUpdate(debug3, Blocks.WATER.defaultBlockState());
/* 55 */     debug2.neighborChanged(debug3, Blocks.WATER, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 60 */     return PushReaction.NORMAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\IceBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */