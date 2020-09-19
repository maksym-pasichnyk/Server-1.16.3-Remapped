/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FishingRodItem extends Item implements Vanishable {
/*    */   public FishingRodItem(Item.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 20 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 21 */     if (debug2.fishing != null) {
/* 22 */       if (!debug1.isClientSide) {
/* 23 */         int debug5 = debug2.fishing.retrieve(debug4);
/* 24 */         debug4.hurtAndBreak(debug5, debug2, debug1 -> debug1.broadcastBreakEvent(debug0));
/*    */       } 
/* 26 */       debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/*    */     } else {
/* 28 */       debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/* 29 */       if (!debug1.isClientSide) {
/* 30 */         int debug5 = EnchantmentHelper.getFishingSpeedBonus(debug4);
/* 31 */         int debug6 = EnchantmentHelper.getFishingLuckBonus(debug4);
/* 32 */         debug1.addFreshEntity((Entity)new FishingHook(debug2, debug1, debug6, debug5));
/*    */       } 
/* 34 */       debug2.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/* 36 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEnchantmentValue() {
/* 41 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\FishingRodItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */