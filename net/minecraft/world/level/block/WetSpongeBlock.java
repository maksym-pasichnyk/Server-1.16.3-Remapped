/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WetSpongeBlock
/*    */   extends Block
/*    */ {
/*    */   protected WetSpongeBlock(BlockBehaviour.Properties debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 21 */     if (debug2.dimensionType().ultraWarm()) {
/* 22 */       debug2.setBlock(debug3, Blocks.SPONGE.defaultBlockState(), 3);
/* 23 */       debug2.levelEvent(2009, debug3, 0);
/* 24 */       debug2.playSound(null, debug3, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, (1.0F + debug2.getRandom().nextFloat() * 0.2F) * 0.7F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WetSpongeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */