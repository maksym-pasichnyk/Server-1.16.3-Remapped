/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ThrownEnderpearl;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EnderpearlItem extends Item {
/*    */   public EnderpearlItem(Item.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 20 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*    */     
/* 22 */     debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/* 23 */     debug2.getCooldowns().addCooldown(this, 20);
/* 24 */     if (!debug1.isClientSide) {
/* 25 */       ThrownEnderpearl debug5 = new ThrownEnderpearl(debug1, (LivingEntity)debug2);
/* 26 */       debug5.setItem(debug4);
/* 27 */       debug5.shootFromRotation((Entity)debug2, debug2.xRot, debug2.yRot, 0.0F, 1.5F, 1.0F);
/* 28 */       debug1.addFreshEntity((Entity)debug5);
/*    */     } 
/* 30 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 31 */     if (!debug2.abilities.instabuild) {
/* 32 */       debug4.shrink(1);
/*    */     }
/* 34 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\EnderpearlItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */