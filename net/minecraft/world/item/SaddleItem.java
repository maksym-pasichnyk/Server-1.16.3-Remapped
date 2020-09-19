/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Saddleable;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class SaddleItem extends Item {
/*    */   public SaddleItem(Item.Properties debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack debug1, Player debug2, LivingEntity debug3, InteractionHand debug4) {
/* 17 */     if (debug3 instanceof Saddleable && debug3.isAlive()) {
/* 18 */       Saddleable debug5 = (Saddleable)debug3;
/* 19 */       if (!debug5.isSaddled() && debug5.isSaddleable()) {
/* 20 */         if (!debug2.level.isClientSide) {
/* 21 */           debug5.equipSaddle(SoundSource.NEUTRAL);
/* 22 */           debug1.shrink(1);
/*    */         } 
/* 24 */         return InteractionResult.sidedSuccess(debug2.level.isClientSide);
/*    */       } 
/*    */     } 
/* 27 */     return InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SaddleItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */