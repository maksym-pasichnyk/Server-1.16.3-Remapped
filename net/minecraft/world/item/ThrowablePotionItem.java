/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ThrownPotion;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ThrowablePotionItem extends PotionItem {
/*    */   public ThrowablePotionItem(Item.Properties debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 17 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 18 */     if (!debug1.isClientSide) {
/* 19 */       ThrownPotion debug5 = new ThrownPotion(debug1, (LivingEntity)debug2);
/* 20 */       debug5.setItem(debug4);
/* 21 */       debug5.shootFromRotation((Entity)debug2, debug2.xRot, debug2.yRot, -20.0F, 0.5F, 1.0F);
/* 22 */       debug1.addFreshEntity((Entity)debug5);
/*    */     } 
/* 24 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 25 */     if (!debug2.abilities.instabuild) {
/* 26 */       debug4.shrink(1);
/*    */     }
/* 28 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ThrowablePotionItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */