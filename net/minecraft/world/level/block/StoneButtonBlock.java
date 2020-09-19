/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class StoneButtonBlock extends ButtonBlock {
/*    */   protected StoneButtonBlock(BlockBehaviour.Properties debug1) {
/*  8 */     super(false, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getSound(boolean debug1) {
/* 13 */     return debug1 ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StoneButtonBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */