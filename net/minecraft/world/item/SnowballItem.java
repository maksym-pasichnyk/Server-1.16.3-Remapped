/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.Snowball;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SnowballItem extends Item {
/*    */   public SnowballItem(Item.Properties debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 19 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 20 */     debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
/* 21 */     if (!debug1.isClientSide) {
/* 22 */       Snowball debug5 = new Snowball(debug1, (LivingEntity)debug2);
/* 23 */       debug5.setItem(debug4);
/* 24 */       debug5.shootFromRotation((Entity)debug2, debug2.xRot, debug2.yRot, 0.0F, 1.5F, 1.0F);
/* 25 */       debug1.addFreshEntity((Entity)debug5);
/*    */     } 
/* 27 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 28 */     if (!debug2.abilities.instabuild) {
/* 29 */       debug4.shrink(1);
/*    */     }
/* 31 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SnowballItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */