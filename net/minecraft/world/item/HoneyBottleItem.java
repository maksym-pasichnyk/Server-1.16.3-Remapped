/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class HoneyBottleItem
/*    */   extends Item
/*    */ {
/*    */   public HoneyBottleItem(Item.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 25 */     super.finishUsingItem(debug1, debug2, debug3);
/* 26 */     if (debug3 instanceof ServerPlayer) {
/* 27 */       ServerPlayer debug4 = (ServerPlayer)debug3;
/* 28 */       CriteriaTriggers.CONSUME_ITEM.trigger(debug4, debug1);
/* 29 */       debug4.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/*    */ 
/*    */     
/* 33 */     if (!debug2.isClientSide) {
/* 34 */       debug3.removeEffect(MobEffects.POISON);
/*    */     }
/*    */     
/* 37 */     if (debug1.isEmpty())
/* 38 */       return new ItemStack(Items.GLASS_BOTTLE); 
/* 39 */     if (debug3 instanceof Player && !((Player)debug3).abilities.instabuild) {
/* 40 */       ItemStack debug4 = new ItemStack(Items.GLASS_BOTTLE);
/* 41 */       Player debug5 = (Player)debug3;
/* 42 */       if (!debug5.inventory.add(debug4)) {
/* 43 */         debug5.drop(debug4, false);
/*    */       }
/*    */     } 
/*    */     
/* 47 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack debug1) {
/* 52 */     return 40;
/*    */   }
/*    */ 
/*    */   
/*    */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 57 */     return UseAnim.DRINK;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getDrinkingSound() {
/* 62 */     return SoundEvents.HONEY_DRINK;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getEatingSound() {
/* 67 */     return SoundEvents.HONEY_DRINK;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 72 */     return ItemUtils.useDrink(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\HoneyBottleItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */