/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class MilkBucketItem
/*    */   extends Item
/*    */ {
/*    */   public MilkBucketItem(Item.Properties debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 21 */     if (debug3 instanceof ServerPlayer) {
/* 22 */       ServerPlayer debug4 = (ServerPlayer)debug3;
/* 23 */       CriteriaTriggers.CONSUME_ITEM.trigger(debug4, debug1);
/* 24 */       debug4.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/*    */     
/* 27 */     if (debug3 instanceof Player && !((Player)debug3).abilities.instabuild) {
/* 28 */       debug1.shrink(1);
/*    */     }
/*    */     
/* 31 */     if (!debug2.isClientSide) {
/* 32 */       debug3.removeAllEffects();
/*    */     }
/*    */     
/* 35 */     if (debug1.isEmpty()) {
/* 36 */       return new ItemStack(Items.BUCKET);
/*    */     }
/* 38 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack debug1) {
/* 43 */     return 32;
/*    */   }
/*    */ 
/*    */   
/*    */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 48 */     return UseAnim.DRINK;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 53 */     return ItemUtils.useDrink(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\MilkBucketItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */