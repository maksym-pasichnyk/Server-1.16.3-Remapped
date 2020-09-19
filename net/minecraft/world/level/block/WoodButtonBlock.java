/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class WoodButtonBlock extends ButtonBlock {
/*    */   protected WoodButtonBlock(BlockBehaviour.Properties debug1) {
/*  8 */     super(true, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getSound(boolean debug1) {
/* 13 */     return debug1 ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WoodButtonBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */