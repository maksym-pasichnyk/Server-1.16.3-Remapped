/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ExperienceBottleItem extends Item {
/*    */   public ExperienceBottleItem(Item.Properties debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFoil(ItemStack debug1) {
/* 19 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 24 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 25 */     debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/* 26 */     if (!debug1.isClientSide) {
/* 27 */       ThrownExperienceBottle debug5 = new ThrownExperienceBottle(debug1, (LivingEntity)debug2);
/* 28 */       debug5.setItem(debug4);
/* 29 */       debug5.shootFromRotation((Entity)debug2, debug2.xRot, debug2.yRot, -20.0F, 0.7F, 1.0F);
/* 30 */       debug1.addFreshEntity((Entity)debug5);
/*    */     } 
/* 32 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 33 */     if (!debug2.abilities.instabuild) {
/* 34 */       debug4.shrink(1);
/*    */     }
/* 36 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ExperienceBottleItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */